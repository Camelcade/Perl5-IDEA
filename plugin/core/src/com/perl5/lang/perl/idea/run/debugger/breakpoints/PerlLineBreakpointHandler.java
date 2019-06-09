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

package com.perl5.lang.perl.idea.run.debugger.breakpoints;

import com.intellij.xdebugger.breakpoints.XBreakpoint;
import com.intellij.xdebugger.breakpoints.XBreakpointHandler;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugThread;
import org.jetbrains.annotations.NotNull;


public class PerlLineBreakpointHandler extends XBreakpointHandler {
  private final PerlDebugThread myPerlDebugThread;

  public PerlLineBreakpointHandler(@NotNull PerlDebugThread myThread) {
    super(PerlLineBreakpointType.class);
    myPerlDebugThread = myThread;
  }

  @Override
  public void registerBreakpoint(@NotNull XBreakpoint breakpoint) {
    if (breakpoint instanceof XLineBreakpoint) {
      //noinspection unchecked
      myPerlDebugThread
        .queueLineBreakpointDescriptor(PerlLineBreakPointDescriptor.createFromBreakpoint((XLineBreakpoint)breakpoint, myPerlDebugThread));
    }
    else {
      System.err.println("Don't know how to register" + breakpoint);
    }
  }

  @Override
  public void unregisterBreakpoint(@NotNull XBreakpoint breakpoint, boolean temporary) {
    if (breakpoint instanceof XLineBreakpoint) {
      //noinspection unchecked
      myPerlDebugThread.queueLineBreakpointDescriptor(
        PerlLineBreakPointDescriptor.createRemoveFromBreakpoint((XLineBreakpoint)breakpoint, myPerlDebugThread));
    }
    else {
      System.err.println("Don't know how to unregister" + breakpoint);
    }
  }
}
