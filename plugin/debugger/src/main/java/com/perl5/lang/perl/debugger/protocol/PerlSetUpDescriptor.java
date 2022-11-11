/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

package com.perl5.lang.perl.debugger.protocol;

import com.perl5.lang.perl.debugger.PerlDebuggerSettings;
import com.perl5.lang.perl.debugger.breakpoints.PerlLineBreakPointDescriptor;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptions;

import java.util.List;


public class PerlSetUpDescriptor {
  @SuppressWarnings("unused") private final List<PerlLineBreakPointDescriptor> breakpoints;
  @SuppressWarnings("unused") private final String charset;
  @SuppressWarnings("unused") private final String startMode;
  @SuppressWarnings("unused") private final boolean enableCompileTimeBreakpoints;
  @SuppressWarnings("unused") private final boolean enableNonInteractiveMode;
  @SuppressWarnings("unused") private final String initCode;
  @SuppressWarnings("unused") private final List<PerlDebuggerSettings.Item> renderers;

  public PerlSetUpDescriptor(List<PerlLineBreakPointDescriptor> breakpoints, PerlDebugOptions debugProfileState) {
    this.breakpoints = breakpoints;
    this.charset = debugProfileState.getScriptCharset();
    this.startMode = debugProfileState.getStartMode();
    this.enableCompileTimeBreakpoints = debugProfileState.isCompileTimeBreakpointsEnabled();
    this.enableNonInteractiveMode = debugProfileState.isNonInteractiveModeEnabled();
    this.initCode = debugProfileState.getInitCode();
    this.renderers = PerlDebuggerSettings.getInstance().getDataRenderers();
  }
}
