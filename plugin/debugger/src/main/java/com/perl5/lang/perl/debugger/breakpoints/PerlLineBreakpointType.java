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

package com.perl5.lang.perl.debugger.breakpoints;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.xdebugger.breakpoints.XLineBreakpoint;
import com.intellij.xdebugger.breakpoints.XLineBreakpointType;
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider;
import com.perl5.lang.perl.debugger.PerlDebuggerBundle;
import com.perl5.lang.perl.debugger.PerlDebuggerEditorsProvider;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class PerlLineBreakpointType extends XLineBreakpointType<PerlLineBreakpointProperties> {
  public PerlLineBreakpointType() {
    super("perl_line_breakpoint", PerlDebuggerBundle.message("perl.line.breakpoint"));
  }

  @Override
  public @Nullable PerlLineBreakpointProperties createBreakpointProperties(@NotNull VirtualFile file, int line) {
    return new PerlLineBreakpointProperties();
  }

  @Override
  public boolean canPutAt(@NotNull VirtualFile file, int line, @NotNull Project project) {
    // fixme add method to disable in templating
    return file.getFileType() instanceof PerlFileType;
  }

  @Override
  public @Nullable XDebuggerEditorsProvider getEditorsProvider(@NotNull XLineBreakpoint<PerlLineBreakpointProperties> breakpoint,
                                                               @NotNull Project project) {
    return PerlDebuggerEditorsProvider.INSTANCE;
  }
}
