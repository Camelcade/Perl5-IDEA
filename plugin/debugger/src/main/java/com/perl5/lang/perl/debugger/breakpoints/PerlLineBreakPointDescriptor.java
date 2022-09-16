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

package com.perl5.lang.perl.debugger.breakpoints;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.xdebugger.XExpression;
import com.intellij.xdebugger.XSourcePosition;
import com.intellij.xdebugger.breakpoints.SuspendPolicy;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.perl5.lang.perl.debugger.PerlDebugThread;
import org.jetbrains.annotations.Nullable;


public class PerlLineBreakPointDescriptor {
  private String path;
  private int line;
  private boolean enabled;
  private String condition;
  private boolean remove;
  private String action;
  private boolean suspend;

  public String getPath() {
    return path;
  }

  public int getLine() {
    return line;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public String getCondition() {
    return condition;
  }

  public static @Nullable PerlLineBreakPointDescriptor createFromBreakpoint(XLineBreakpoint<PerlLineBreakpointProperties> breakpoint,
                                                                            PerlDebugThread debugThread) {
    String fileUrl = breakpoint.getFileUrl();

    VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl(fileUrl);
    if (virtualFile == null) {
      return null;
    }
    String virtualFilePath = virtualFile.getCanonicalPath();
    if (virtualFilePath == null) {
      return null;
    }
    String filePath = debugThread.getDebugProfileState().mapPathToRemote(virtualFilePath);
    if (filePath == null) {
      return null;
    }
    PerlLineBreakPointDescriptor descriptor = new PerlLineBreakPointDescriptor();
    descriptor.path = filePath;
    descriptor.line = breakpoint.getLine();
    descriptor.enabled = breakpoint.isEnabled();
    descriptor.remove = false;
    XExpression logExpressionObject = breakpoint.getLogExpressionObject();
    if (logExpressionObject != null) {
      descriptor.action = logExpressionObject.getExpression();
    }
    SuspendPolicy suspendPolicy = breakpoint.getSuspendPolicy();
    descriptor.suspend = suspendPolicy != SuspendPolicy.NONE;

    XExpression conditionExpression = breakpoint.getConditionExpression();
    descriptor.condition = conditionExpression != null ? conditionExpression.getExpression() : "";
    return descriptor;
  }

  public static @Nullable PerlLineBreakPointDescriptor createRemoveFromBreakpoint(XLineBreakpoint<PerlLineBreakpointProperties> breakpoint,
                                                                                  PerlDebugThread debugThread) {
    PerlLineBreakPointDescriptor descriptor = createFromBreakpoint(breakpoint, debugThread);

    if (descriptor != null) {
      descriptor.remove = true;
    }

    return descriptor;
  }

  public static @Nullable PerlLineBreakPointDescriptor createFromSourcePosition(XSourcePosition position, PerlDebugThread debugThread) {
    VirtualFile virtualFile = position.getFile();
    String virtualFilePath = virtualFile.getCanonicalPath();
    if (virtualFilePath == null) {
      return null;
    }
    String filePath = debugThread.getDebugProfileState().mapPathToRemote(virtualFilePath);
    if (filePath == null) {
      return null;
    }

    PerlLineBreakPointDescriptor descriptor = new PerlLineBreakPointDescriptor();
    descriptor.path = filePath;
    descriptor.line = position.getLine();
    return descriptor;
  }
}
