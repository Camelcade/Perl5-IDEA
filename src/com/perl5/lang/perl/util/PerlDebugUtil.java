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

package com.perl5.lang.perl.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.intellij.xdebugger.frame.XCompositeNode;
import com.intellij.xdebugger.frame.XValueChildrenList;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugThread;
import com.perl5.lang.perl.idea.run.debugger.PerlRemoteFileSystem;
import com.perl5.lang.perl.idea.run.debugger.PerlStackFrame;
import com.perl5.lang.perl.idea.run.debugger.PerlXNamedValue;
import com.perl5.lang.perl.idea.run.debugger.breakpoints.PerlLineBreakpointProperties;
import com.perl5.lang.perl.idea.run.debugger.breakpoints.PerlLineBreakpointType;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlDebuggingEventBreakpoint;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlDebuggingTransactionHandler;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlValueDescriptor;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlValueRequestDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;

/**
 * Created by hurricup on 07.05.2016.
 */
public class PerlDebugUtil {
  @Nullable
  public static XLineBreakpoint findBreakpoint(final Project project, final PerlDebuggingEventBreakpoint breakpointBase) {
    final XLineBreakpoint[] result = new XLineBreakpoint[]{null};

    ApplicationManager.getApplication().runReadAction(() -> {
      String path = breakpointBase.getPath();

      VirtualFile virtualFile = null;
      String virtualFileUrl = null;

      virtualFile = VfsUtil.findFileByIoFile(new File(breakpointBase.getDebugThread().getDebugProfileState().mapPathToLocal(path)), true);

      if (virtualFile == null) {
        virtualFileUrl = PerlRemoteFileSystem.PROTOCOL_PREFIX + path;
      }
      else {
        virtualFileUrl = virtualFile.getUrl();
      }

      Collection<? extends XLineBreakpoint<PerlLineBreakpointProperties>> breakpoints =
        XDebuggerManager.getInstance(project).getBreakpointManager().getBreakpoints(PerlLineBreakpointType.class);
      for (XLineBreakpoint<PerlLineBreakpointProperties> breakpoint : breakpoints) {
        if (StringUtil.equals(breakpoint.getFileUrl(), virtualFileUrl) && breakpoint.getLine() == breakpointBase.getLine()) {
          result[0] = breakpoint;
          return;
        }
      }
    });
    return result[0];
  }

  public static void requestAndComputeChildren(@NotNull final XCompositeNode node,
                                               final PerlStackFrame perlStackFrame,
                                               final int[] offset,
                                               final int size,
                                               String key) {
    PerlDebugThread thread = perlStackFrame.getPerlExecutionStack().getSuspendContext().getDebugThread();

    final int frameSize = XCompositeNode.MAX_CHILDREN_TO_SHOW;
    thread.sendCommandAndGetResponse("getchildren", new PerlValueRequestDescriptor(offset[0], frameSize, key),
                                     new PerlDebuggingTransactionHandler() {
                                       @Override
                                       public void run(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
                                         PerlValueDescriptor[] descriptors = jsonDeserializationContext.deserialize(
                                           jsonObject.getAsJsonArray("data"), PerlValueDescriptor[].class
                                         );

                                         XValueChildrenList list = new XValueChildrenList();
                                         for (PerlValueDescriptor descriptor : descriptors) {
                                           list.add(new PerlXNamedValue(descriptor, perlStackFrame));

                                           offset[0]++;
                                         }
                                         boolean isLast = offset[0] >= size;
                                         node.addChildren(list, isLast);
                                         if (!isLast) {
                                           node.tooManyChildren(size - offset[0]);
                                         }
                                       }
                                     });
  }
}
