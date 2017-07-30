/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.run.debugger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.actions.StopProcessAction;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.concurrency.Semaphore;
import com.intellij.util.containers.ByteArrayList;
import com.intellij.xdebugger.XDebugSession;
import com.perl5.lang.perl.idea.run.debugger.breakpoints.PerlLineBreakPointDescriptor;
import com.perl5.lang.perl.idea.run.debugger.protocol.*;
import com.perl5.lang.perl.idea.run.debugger.ui.PerlScriptsPanel;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by hurricup on 04.05.2016.
 */
public class PerlDebugThread extends Thread {
  public static final boolean DEV_MODE = false; //ApplicationManager.getApplication().isInternal();
  private static Executor ourExecutor = Executors.newSingleThreadExecutor();
  private final ExecutionResult myExecutionResult;
  private final Gson myGson;
  private final PerlDebugProfileState myDebugProfileState;
  private final PerlScriptsPanel myScriptListPanel;
  private final PerlScriptsPanel myEvalsListPanel;
  private XDebugSession mySession;
  private Socket mySocket;
  private ServerSocket myServerSocket;
  private OutputStream myOutputStream;
  private InputStream myInputStream;
  private boolean myStop = false;
  private List<PerlLineBreakPointDescriptor> breakpointsDescriptorsQueue = new CopyOnWriteArrayList<>();
  private boolean isReady = false;
  private int transactionId = 0;
  private ConcurrentHashMap<Integer, PerlDebuggingTransactionHandler> transactionsMap =
    new ConcurrentHashMap<>();
  private ReentrantLock lock = new ReentrantLock();
  private PerlRemoteFileSystem myPerlRemoteFileSystem = PerlRemoteFileSystem.getInstance();
  private PerlDebugOptions myPerlDebugOptions;

  public PerlDebugThread(XDebugSession session, PerlDebugProfileState state, ExecutionResult executionResult) {
    super("PerlDebugThread");
    mySession = session;
    myGson = createGson();
    myDebugProfileState = state;
    myExecutionResult = executionResult;
    myScriptListPanel = new PerlScriptsPanel(session.getProject(), this);
    myEvalsListPanel = new PerlScriptsPanel(session.getProject(), this);
    myPerlRemoteFileSystem.dropFiles();
    myPerlDebugOptions = state.getDebugOptions();
  }

  public void queueLineBreakpointDescriptor(PerlLineBreakPointDescriptor descriptor) {
    if (descriptor != null) {
      // fixme potentially risk of race condition between clar and add
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

  @Override
  public void run() {
    try {
      String debugHost = myPerlDebugOptions.getDebugHost();
      int debugPort = myDebugProfileState.getDebugPort();
      String debugName = debugHost + ":" + debugPort;
      if (myPerlDebugOptions.getPerlRole().equals(PerlDebugOptions.ROLE_SERVER)) {
        ((ConsoleView)myExecutionResult.getExecutionConsole())
          .print("Connecting to " + debugName + "...\n", ConsoleViewContentType.SYSTEM_OUTPUT);
        mySocket = new Socket(debugHost, debugPort);
      }
      else {
        ((ConsoleView)myExecutionResult.getExecutionConsole())
          .print("Listening on " + debugName + "...\n", ConsoleViewContentType.SYSTEM_OUTPUT);
        myServerSocket = new ServerSocket(debugPort, 50, InetAddress.getByName(debugHost));
        mySocket = myServerSocket.accept();
      }
      ((ConsoleView)myExecutionResult.getExecutionConsole()).print("Connected\n", ConsoleViewContentType.SYSTEM_OUTPUT);

      myOutputStream = mySocket.getOutputStream();
      myInputStream = mySocket.getInputStream();

      ByteArrayList response = new ByteArrayList();

      while (!myStop) {
        response.clear();

        if (DEV_MODE) {
          System.err.println("\nReading data");
        }

        // reading bytes
        while (myInputStream != null) {
          int dataByte = myInputStream.read();
          if (dataByte == '\n') {
            break;
          }
          else if (dataByte == -1) {
            return;
          }
          else {
            response.add((byte)dataByte);
          }
        }

        if (DEV_MODE) {
          System.err.println("Got response " + response.size());
          System.err.println(new String(response.toNativeArray(), CharsetToolkit.UTF8_CHARSET));
        }

        processResponse(response);
      }
    }
    catch (IOException e) {
      //			e.printStackTrace();
    }
    catch (ExecutionException e) {
      //			e.printStackTrace();
    }
    catch (Exception e) {
      //			e.printStackTrace();
    }
    finally {
      setStop();
    }
  }

  private void processResponse(ByteArrayList responseBytes) {
    final String response = new String(responseBytes.toNativeArray(), CharsetToolkit.UTF8_CHARSET);
    final PerlDebuggingEvent newEvent = myGson.fromJson(response, PerlDebuggingEvent.class);

    if (newEvent != null) {
      if (newEvent instanceof PerlDebuggingEventReady) {
        if (((PerlDebuggingEventReady)newEvent).isValid()) {
          isReady = true;
          setUpDebugger();
        }
        else {
          setStop();
        }
      }
      else {
        newEvent.setDebugSession(mySession);
        newEvent.setDebugThread(this);
        ourExecutor.execute(newEvent);
        //				ApplicationManager.getApplication().executeOnPooledThread()invokeLater( // executeOnPooledThread - fixme concurrency problems
      }
    }
  }

  public void sendString(String string) {
    if (mySocket == null) {
      return;
    }

    string = string + "\n";

    try {
      if (DEV_MODE) {
        System.err.println("Going to send string " + string);
      }

      lock.lock();

      if (DEV_MODE) {
        System.err.println("Sent string " + string);
      }


      myOutputStream.write(string.getBytes(CharsetToolkit.UTF8));
    }
    catch (IOException e) {
      e.printStackTrace();
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
    try {
      if (myInputStream != null) {
        myInputStream.close();
        myInputStream = null;
      }
    }
    catch (IOException e) {
    }
    try {
      if (myOutputStream != null) {
        myOutputStream.close();
        myOutputStream = null;
      }
    }
    catch (IOException e) {
    }
    try {
      if (mySocket != null) {
        mySocket.close();
        mySocket = null;
      }
    }
    catch (IOException e) {
    }
    try {
      if (myServerSocket != null) {
        myServerSocket.close();
        myServerSocket.close();
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    StopProcessAction.stopProcess(myExecutionResult.getProcessHandler());

    ((ConsoleView)myExecutionResult.getExecutionConsole()).print("Disconnected\n", ConsoleViewContentType.SYSTEM_OUTPUT);
  }

  protected Gson createGson() {
    GsonBuilder builder = new GsonBuilder();
    builder.registerTypeAdapter(PerlDebuggingEvent.class, new PerlDebuggingEventsDeserializer(this));
    return builder.excludeFieldsWithModifiers(Modifier.TRANSIENT).create();
  }

  @Nullable
  public PerlDebuggingTransactionHandler getTransactionHandler(int transactionId) {
    return transactionsMap.remove(transactionId);
  }

  public PerlScriptsPanel getScriptListPanel() {
    return myScriptListPanel;
  }

  public PerlScriptsPanel getEvalsListPanel() {
    return myEvalsListPanel;
  }

  public PerlRemoteFileSystem getPerlRemoteFileSystem() {
    return myPerlRemoteFileSystem;
  }

  @Nullable
  public VirtualFile loadRemoteSource(String filePath) {
    if (DEV_MODE) {
      System.err.println("Loading file " + filePath);
    }
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

  public PerlDebugProfileState getDebugProfileState() {
    return myDebugProfileState;
  }
}
