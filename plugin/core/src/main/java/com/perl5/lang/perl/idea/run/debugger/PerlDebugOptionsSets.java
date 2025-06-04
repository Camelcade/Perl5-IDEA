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

package com.perl5.lang.perl.idea.run.debugger;

import com.perl5.PerlBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;

import java.util.Map;

/**
 * fixme this need to be fixed with run configuration extension
 */
public final class PerlDebugOptionsSets {
  private PerlDebugOptionsSets() {
  }

  public static final @NonNls String DEBUGGER_STARTUP_COMPILE = "COMPILE";
  public static final @NonNls String DEBUGGER_STARTUP_RUN = "RUN";
  public static final @NonNls String DEBUGGER_STARTUP_BREAKPOINT = "BREAKPOINT";

  public static final Map<String, @Nls String> STARTUP_OPTIONS = Map.of(
    DEBUGGER_STARTUP_COMPILE, PerlBundle.message("stop.as.soon.as.possible"),
    DEBUGGER_STARTUP_RUN, PerlBundle.message("stop.after.compilation.use.and.begin.blocks.are.done"),
    DEBUGGER_STARTUP_BREAKPOINT, PerlBundle.message("stop.at.first.breakpoint")
  );
  public static final Map<String, String> ROLE_OPTIONS = Map.of(
    PerlDebugOptions.ROLE_SERVER, "IDE connects to the perl process",
    PerlDebugOptions.ROLE_CLIENT, "Perl process connects to the IDE"
  );
}
