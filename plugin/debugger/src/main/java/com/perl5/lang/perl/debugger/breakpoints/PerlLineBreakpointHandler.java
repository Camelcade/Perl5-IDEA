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

import com.intellij.xdebugger.breakpoints.XBreakpointHandler;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.perl5.lang.perl.debugger.PerlDebugThread;
import org.jetbrains.annotations.NotNull;


public class PerlLineBreakpointHandler extends XBreakpointHandler<XLineBreakpoint<PerlLineBreakpointProperties>> {
  private final @NotNull PerlDebugThread myPerlDebugThread;

  public PerlLineBreakpointHandler(@NotNull PerlDebugThread myThread) {
    super(PerlLineBreakpointType.class);
    myPerlDebugThread = myThread;
  }

  @Override
  public void registerBreakpoint(@NotNull XLineBreakpoint<PerlLineBreakpointProperties> breakpoint) {
    myPerlDebugThread.queueLineBreakpointDescriptor(
      PerlLineBreakPointDescriptor.createFromBreakpoint(breakpoint, myPerlDebugThread));
  }

  @Override
  public void unregisterBreakpoint(@NotNull XLineBreakpoint<PerlLineBreakpointProperties> breakpoint, boolean temporary) {
    myPerlDebugThread.queueLineBreakpointDescriptor(
      PerlLineBreakPointDescriptor.createRemoveFromBreakpoint(breakpoint, myPerlDebugThread));
  }
}
