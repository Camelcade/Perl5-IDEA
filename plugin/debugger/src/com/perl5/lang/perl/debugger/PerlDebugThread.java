/*
 * Copyright 2015-2021 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.lang.perl.debugger;

import com.google.gson.*;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.actions.StopProcessAction;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.concurrency.Semaphore;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.impl.XDebugSessionImpl;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.debugger.breakpoints.PerlLineBreakPointDescriptor;
import com.perl5.lang.perl.debugger.protocol.*;
import com.perl5.lang.perl.debugger.run.run.debugger.PerlDebugProfileStateBase;
import com.perl5.lang.perl.debugger.run.run.debugger.remote.PerlRemoteDebuggingConfiguration;
import com.perl5.lang.perl.debugger.ui.PerlScriptsPanel;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptions;
import com.perl5.lang.perl.util.PerlRunUtil;
import gnu.trove.TByteArrayList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.PropertyKey;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import static com.perl5.PerlBundle.PATH_TO_BUNDLE;
import static com.perl5.lang.perl.debugger.protocol.PerlDebuggingEventReady.MODULE_VERSION_PREFIX;
import static com.perl5.lang.perl.debugger.run.run.debugger.PerlDebugProfileState.DEBUG_PACKAGE;


public class PerlDebugThread extends Thread {
  private static final Logger LOG = Logger.getInstance(PerlDebugThread.class);
  private final ExecutorService myExecutor = Executors.newSingleThreadExecutor();
  private final ExecutionResult myExecutionResult;
  private final Gson myGson;
  private final PerlDebugProfileStateBase myDebugProfileState;
  private final PerlScriptsPanel myScriptListPanel;
  private final PerlScriptsPanel myEvalsListPanel;
  private final XDebugSession mySession;
  private Socket mySocket;
  private ServerSocket myServerSocket;
  private OutputStream myOutputStream;
  private InputStream myInputStream;
  private volatile boolean myStop = false;
  private final List<PerlLineBreakPointDescriptor> breakpointsDescriptorsQueue = new CopyOnWriteArrayList<>();
  private boolean isReady = false;
  private int transactionId = 0;
  private final ConcurrentHashMap<Integer, PerlDebuggingTransactionHandler> transactionsMap =
    new ConcurrentHashMap<>();
  private final ReentrantLock lock = new ReentrantLock();
  private final PerlRemoteFileSystem myPerlRemoteFileSystem = PerlRemoteFileSystem.getInstance();
  private final PerlDebugOptions myPerlDebugOptions;

  public PerlDebugThread(XDebugSession session, PerlDebugProfileStateBase state, ExecutionResult executionResult) {
    super("PerlDebugThread");
    mySession = session;
    myGson = createGson();
    myDebugProfileState = state;
    myExecutionResult = executionResult;
    myScriptListPanel = new PerlScriptsPanel(session.getProject(), this);
    myEvalsListPanel = new PerlScriptsPanel(session.getProject(), this);
    myPerlDebugOptions = state.getDebugOptions();
  }

  public void queueLineBreakpointDescriptor(PerlLineBreakPointDescriptor descriptor) {
    if (descriptor != null) {
      // fixme potentially risk of race condition between clear and add
      breakpointsDescriptorsQueue.add(descriptor);
      if (isReady) {
        sendQueuedBreakpoints();
      }
    }
  }

  protected void sendQueuedBreakpoints() {
    sendCommand("b", breakpointsDescriptorsQueue);
    breakpointsDescriptorsQueue.clear();
  }

  protected void setUpDebugger() {
    PerlSetUpDescriptor perlSetUpDescriptor = new PerlSetUpDescriptor(breakpointsDescriptorsQueue, myDebugProfileState.getDebugOptions());
    sendString(myGson.toJson(perlSetUpDescriptor));
    breakpointsDescriptorsQueue.clear();
  }

  private void print(@NotNull @PropertyKey(resourceBundle = PATH_TO_BUNDLE) String key, @NotNull Object... params) {
    String textToPrint = PerlBundle.message(key, params);
    LOG.debug("Printing: ", textToPrint);
    ((ConsoleView)myExecutionResult.getExecutionConsole()).print(
      textToPrint + "\n", ConsoleViewContentType.SYSTEM_OUTPUT);
  }

  private void prepareAndConnect() throws ExecutionException, IOException, InterruptedException {
    myScriptListPanel.clear();
    myEvalsListPanel.clear();
    WriteAction.runAndWait(myPerlRemoteFileSystem::dropFiles);

    int debugPort = myDebugProfileState.getDebugPort();
    String debugName;
    if (myPerlDebugOptions.getPerlRole().equals(PerlDebugOptions.ROLE_SERVER)) {
      String hostToConnect = myPerlDebugOptions.getHostToConnect();
      debugName = hostToConnect + ":" + debugPort;
      while (!myStop && !PerlDebugProfileStateBase.isReadyForConnection(myExecutionResult.getProcessHandler())) {
        print("perl.debug.waiting.start");
        Thread.sleep(1000);
      }

      if (myStop) {
        return;
      }
      print("perl.debug.connecting.to", debugName);
      for (int i = 1; i < 11 && !myStop; i++) {
        try {
          mySocket = new Socket(hostToConnect, debugPort);
          break;
        }
        catch (ConnectException e) {
          if (i == 10) {
            throw e;
          }
          LOG.info("Connection error: " + e.getMessage());
          print("perl.debug.attempting.again");
          Thread.sleep(1000);
        }
      }
    }
    else {
      String hostToBind = myPerlDebugOptions.getHostToBind();
      debugName = hostToBind + ":" + debugPort;
      print("perl.debug.listening.on", debugName);
      myServerSocket = new ServerSocket(debugPort, 50, InetAddress.getByName(hostToBind));
      mySocket = myServerSocket.accept();
    }
  }

  /**
   * @return true iff we've reached end of stream (interrupted from the script's side)
   */
  private boolean doRun() {
    try {
      prepareAndConnect();
      if (myStop) {
        return false;
      }
      print("perl.debug.connected");

      myOutputStream = mySocket.getOutputStream();
      myInputStream = mySocket.getInputStream();

      TByteArrayList response = new TByteArrayList();

      while (!myStop) {
        response.clear();

        LOG.debug("Reading data from the debugger");

        // reading bytes
        while (myInputStream != null) {
          int dataByte = myInputStream.read();
          if (dataByte == '\n') {
            break;
          }
          else if (dataByte == -1) {
            return true;
          }
          else {
            response.add((byte)dataByte);
          }
        }

        processResponse(response);
      }
    }
    catch (Exception e) {
      LOG.warn(e);
    }
    return false;
  }

  @Override
  public void run() {
    try {
      while (doRun() && myPerlDebugOptions.isReconnect()) {
        print("perl.debug.reconnecting");
        closeStreamsAndSockets();
        isReady = false;
        ((XDebugSessionImpl)mySession).reset();
        ReadAction.run(mySession::initBreakpoints);
      }
    }
    finally {
      setStop();
    }
  }

  private void processResponse(TByteArrayList responseBytes) {
    final String response = new String(responseBytes.toNativeArray(), StandardCharsets.UTF_8);
    LOG.debug("Got response: ", response);

    try {
      final PerlDebuggingEvent newEvent = myGson.fromJson(response, PerlDebuggingEvent.class);

      if (newEvent != null) {
        if (newEvent instanceof PerlDebuggingEventReady) {
          if (((PerlDebuggingEventReady)newEvent).isValid()) {
            isReady = true;
            setUpDebugger();
          }
          else {
            Notification notification = new Notification(
              PerlDebugProcess.PERL_DEBUGGER_NOTIFICATION_GROUP_ID,
              PerlBundle.message("perl.debugger.incorrect.version.title", DEBUG_PACKAGE),
              PerlBundle.message(
                "perl.debugger.incorrect.version.message", DEBUG_PACKAGE, MODULE_VERSION_PREFIX,
                ((PerlDebuggingEventReady)newEvent).version),
              NotificationType.ERROR
            );
            Project project = myDebugProfileState.getEnvironment().getProject();
            if (myPerlDebugOptions instanceof PerlRemoteDebuggingConfiguration) {
              Notifications.Bus.notify(notification, project);
            }
            else {
              PerlRunUtil.addInstallActionsAndShow(
                project, Objects.requireNonNull(PerlProjectManager.getSdk(project)),
                Collections.singletonList(DEBUG_PACKAGE),
                notification);
            }
            setStop();
          }
        }
        else {
          newEvent.setDebugSession(mySession);
          newEvent.setDebugThread(this);
          myExecutor.execute(newEvent);
        }
      }
    }
    catch (JsonSyntaxException e) {
      LOG.error("Error parsing JSON response: " + response, e);
      print("perl.debug.error.parsing.response");
      setStop();
    }
  }

  public void sendString(String string) {
    if (mySocket == null) {
      return;
    }

    string = string + "\n";

    try {
      LOG.debug("Going to send string " + string);

      lock.lock();

      LOG.debug("Sent string " + string);


      myOutputStream.write(string.getBytes(StandardCharsets.UTF_8));
    }
    catch (IOException e) {
      LOG.warn(e);
    }
    finally {
      lock.unlock();
    }
  }

  public void sendCommand(String command, Object data) {
    sendString(command + " " + myGson.toJson(data));
  }

  public void sendCommandAndGetResponse(String command, Object data, PerlDebuggingTransactionHandler transactionHandler) {
    if (mySocket == null) {
      return;
    }

    try {
      lock.lock();
      PerlDebuggingTransactionWrapper transaction = new PerlDebuggingTransactionWrapper(transactionId++, data);
      transactionsMap.put(transaction.getTransactionId(), transactionHandler);
      sendCommand(command, transaction);
    }
    finally {
      lock.unlock();
    }
  }

  public Socket getSocket() {
    return mySocket;
  }

  public void setStop() {
    if (myStop) {
      return;
    }
    myStop = true;
    closeStreamsAndSockets();
    myExecutor.shutdownNow();
    StopProcessAction.stopProcess(myExecutionResult.getProcessHandler());

    print("perl.debug.disconnected");
  }

  private void closeStreamsAndSockets() {
    //noinspection Duplicates
    try {
      if (myInputStream != null) {
        myInputStream.close();
        myInputStream = null;
      }
    }
    catch (IOException e) {
      LOG.warn(e);
    }

    //noinspection Duplicates
    try {
      if (myOutputStream != null) {
        myOutputStream.close();
        myOutputStream = null;
      }
    }
    catch (IOException e) {
      LOG.warn(e);
    }

    //noinspection Duplicates
    try {
      if (mySocket != null) {
        mySocket.close();
        mySocket = null;
      }
    }
    catch (IOException e) {
      LOG.warn(e);
    }
    try {
      if (myServerSocket != null) {
        myServerSocket.close();
        myServerSocket.close();
      }
    }
    catch (IOException e) {
      LOG.warn(e);
    }
  }

  protected Gson createGson() {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(PerlDebuggingEvent.class, new PerlDebuggingEventsDeserializer(this));
    return builder.excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
  }

  public @Nullable PerlDebuggingTransactionHandler getTransactionHandler(int transactionId) {
    return transactionsMap.remove(transactionId);
  }

  public PerlScriptsPanel getScriptListPanel() {
    return myScriptListPanel;
  }

  public PerlScriptsPanel getEvalsListPanel() {
    return myEvalsListPanel;
  }

  public @Nullable VirtualFile loadRemoteSource(String filePath) {
    LOG.debug("Loading file ", filePath);
    final Semaphore responseSemaphore = new Semaphore();
    responseSemaphore.down();

    final String[] response = new String[]{"# Source could not be loaded..."};

    PerlDebuggingTransactionHandler perlDebuggingTransactionHandler = new PerlDebuggingTransactionHandler() {

      @Override
      public void run(JsonObject eventObject, JsonDeserializationContext jsonDeserializationContext) {
        response[0] = eventObject.getAsJsonPrimitive("data").getAsString();
        responseSemaphore.up();
      }
    };

    if (mySocket != null) {
      sendCommandAndGetResponse("get_source", new PerlSourceRequestDescriptor(filePath), perlDebuggingTransactionHandler);
      responseSemaphore.waitFor(2000);
    }

    return myPerlRemoteFileSystem.registerRemoteFile(filePath, response[0]);
  }

  public PerlDebugProfileStateBase getDebugProfileState() {
    return myDebugProfileState;
  }
}
