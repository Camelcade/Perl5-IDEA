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

package com.perl5.lang.perl.idea.run.debugger.protocol;

import com.intellij.icons.AllIcons;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 07.05.2016.
 */
public class PerlDebuggingEventBreakpointDenied extends PerlDebuggingEventBreakpointBase {
  @Override
  protected void processBreakPoint(@NotNull XLineBreakpoint breakpoint, XDebugSession session) {
    XDebuggerManager.getInstance(session.getProject()).getBreakpointManager().updateBreakpointPresentation(
      breakpoint,
      AllIcons.Debugger.Db_invalid_breakpoint,
      "You can't set a breakpoint here"
    );
  }
}
