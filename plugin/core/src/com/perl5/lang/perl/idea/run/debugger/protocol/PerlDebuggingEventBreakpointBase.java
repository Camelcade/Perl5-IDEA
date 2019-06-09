/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.run.debugger.protocol;

import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.perl5.lang.perl.util.PerlDebugUtil;
import org.jetbrains.annotations.NotNull;


public abstract class PerlDebuggingEventBreakpointBase extends PerlDebuggingEventBase implements PerlDebuggingEventBreakpoint {
  private String path;
  private int line;

  @Override
  public void run() {
    XDebugSession session = getDebugSession();
    XLineBreakpoint breakpoint = PerlDebugUtil.findBreakpoint(session.getProject(), this);
    if (breakpoint != null) {
      processBreakPoint(breakpoint, session);
    }
  }

  protected abstract void processBreakPoint(@NotNull XLineBreakpoint breakpoint, XDebugSession session);

  public String getPath() {
    return path;
  }

  public int getLine() {
    return line;
  }
}
