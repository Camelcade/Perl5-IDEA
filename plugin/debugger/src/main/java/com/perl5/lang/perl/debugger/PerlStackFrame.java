/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.ColoredTextContainer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator;
import com.intellij.xdebugger.frame.XCompositeNode;
import com.intellij.xdebugger.frame.XStackFrame;
import com.intellij.xdebugger.frame.XValueChildrenList;
import com.intellij.xdebugger.impl.XSourcePositionImpl;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.debugger.protocol.*;
import com.perl5.lang.perl.debugger.values.PerlXMainGroup;
import com.perl5.lang.perl.debugger.values.PerlXNamedValue;
import com.perl5.lang.perl.debugger.values.PerlXValueGroup;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.jetbrains.annotations.VisibleForTesting;

import java.io.File;


public class PerlStackFrame extends XStackFrame {
  private final @NotNull PerlStackFrameDescriptor myFrameDescriptor;
  private final @NotNull PerlExecutionStack myPerlExecutionStack;
  private final @NotNull PerlDebugThread myDebugThread;
  private final @Nullable VirtualFile myVirtualFile;

  public PerlStackFrame(@NotNull PerlStackFrameDescriptor frameDescriptor, @NotNull PerlExecutionStack stack) {
    myFrameDescriptor = frameDescriptor;
    myPerlExecutionStack = stack;
    myDebugThread = myPerlExecutionStack.getSuspendContext().getDebugThread();

    myVirtualFile = ReadAction.nonBlocking(() -> {
      String remoteFilePath = myFrameDescriptor.getFileDescriptor().getPath();
      String localFilePath = myDebugThread.getDebugProfileState().mapPathToLocal(remoteFilePath);
      VirtualFile result = VfsUtil.findFileByIoFile(new File(localFilePath), false);

      if (result == null) {
        String remoteFileUrl = PerlRemoteFileSystem.PROTOCOL_PREFIX + remoteFilePath;
        result = VirtualFileManager.getInstance().findFileByUrl(remoteFileUrl);

        if (result == null) {    // suppose that we need to fetch a file
          result = myDebugThread.loadRemoteSource(remoteFilePath);
        }
      }

      return result;
    }).executeSynchronously();

    PerlLoadedFileDescriptor fileDescriptor = myFrameDescriptor.getFileDescriptor();

    if (fileDescriptor.isEval()) {
      myDebugThread.getEvalsListPanel().add(fileDescriptor);
    }
    else {
      myDebugThread.getScriptListPanel().add(fileDescriptor);
    }
  }

  @Override
  public void customizePresentation(@NotNull ColoredTextContainer component) {
    doCustomizePresentation(myFrameDescriptor, component);
  }

  @VisibleForTesting
  public static void doCustomizePresentation(@NotNull PerlStackFrameDescriptor frameDescriptor, @NotNull ColoredTextContainer component) {
    var fileDescriptor = frameDescriptor.getFileDescriptor();
    var fqn = fileDescriptor.getName();
    var nameChunks = PerlPackageUtil.splitNames(fqn);
    var filePath = fileDescriptor.getPath();
    @NlsSafe var namespaceName = nameChunks == null ? null : nameChunks.getFirst();
    var subName = nameChunks == null ? null : nameChunks.getSecond();
    var firstName = subName == null ? filePath : subName;
    @NlsSafe var frameName =
      String.join(":", firstName, String.valueOf(frameDescriptor.getOneBasedLine()));
    component.append(frameName, SimpleTextAttributes.REGULAR_ATTRIBUTES);
    if (namespaceName != null) {
      component.append(", " + namespaceName, SimpleTextAttributes.REGULAR_ATTRIBUTES);
    }
    if (subName != null) {
      component.append(" (" + filePath + ")", SimpleTextAttributes.GRAYED_ATTRIBUTES);
    }
    component.setIcon(AllIcons.Debugger.Frame);
  }

  @Override
  public @Nullable XSourcePosition getSourcePosition() {
    VirtualFile virtualFile = myVirtualFile;
    if (virtualFile != null) {
      return XSourcePositionImpl.create(virtualFile, myFrameDescriptor.getZeroBasedLine());
    }
    return super.getSourcePosition();
  }

  @Override
  public void computeChildren(@NotNull XCompositeNode node) {
    PerlValueDescriptor[] lexicals = myFrameDescriptor.getLexicals();
    PerlValueDescriptor[] globals = myFrameDescriptor.getGlobals();
    PerlValueDescriptor[] args = myFrameDescriptor.getArgs();
    int mainSize = myFrameDescriptor.getMainSize();

    boolean fallback = true;

    XValueChildrenList list = new XValueChildrenList();

    if (globals != null && globals.length > 0) {
      list.addTopGroup(new PerlXValueGroup("Global variables", "our", PerlIcons.OUR_GUTTER_ICON, globals, this, false));
      fallback = false;
    }
    if (mainSize > 0) {
      list.addTopGroup(new PerlXMainGroup(this, mainSize));
      fallback = false;
    }
    if (args != null && args.length > 0) {
      list.addTopGroup(new PerlXValueGroup("Arguments", null, PerlIcons.ARGS_GUTTER_ICON, args, this, true));
      fallback = false;
    }
    if (lexicals != null && lexicals.length > 0) {
      list.addTopGroup(new PerlXValueGroup("Lexical variables", "my/state", PerlIcons.MY_GUTTER_ICON, lexicals, this, true));
      fallback = false;
    }


    if (fallback) {
      super.computeChildren(node);
    }
    else {
      node.addChildren(list, true);
    }
  }

  public @NotNull PerlExecutionStack getPerlExecutionStack() {
    return myPerlExecutionStack;
  }

  @TestOnly
  public @NotNull PerlStackFrameDescriptor getFrameDescriptor() {
    return myFrameDescriptor;
  }

  @Override
  public @Nullable XDebuggerEvaluator getEvaluator() {
    return new XDebuggerEvaluator() {
      @Override
      public void evaluate(@NotNull String expression,
                           final @NotNull XEvaluationCallback callback,
                           @Nullable XSourcePosition expressionPosition) {
        PerlDebugThread thread = myPerlExecutionStack.getSuspendContext().getDebugThread();

        thread.sendCommandAndGetResponse("e", new PerlEvalRequestDescriptor(expression), new PerlDebuggingTransactionHandler() {
          @Override
          public void run(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            PerlEvalResponseDescriptor descriptor = jsonDeserializationContext.deserialize(
              jsonObject.getAsJsonObject("data"), PerlEvalResponseDescriptor.class
            );

            if (descriptor == null) {
              callback.errorOccurred(
                PerlDebuggerBundle.message("dialog.message.something.bad.happened.on.perl.side.report.to.plugin.devs"));
            }
            else if (descriptor.isError()) {
              callback.errorOccurred(descriptor.getResult().getValue());
            }
            else {
              callback.evaluated(new PerlXNamedValue(descriptor.getResult(), PerlStackFrame.this));
            }
          }
        });
      }
    };
  }

  @Override
  public String toString() {
    return "PerlStackFrame{" +
           "myFrameDescriptor=" + myFrameDescriptor +
           ", myVirtualFile=" + myVirtualFile +
           '}';
  }
}
