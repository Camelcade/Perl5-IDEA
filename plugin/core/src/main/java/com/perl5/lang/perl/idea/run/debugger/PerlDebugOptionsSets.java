/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

import java.util.HashMap;
import java.util.Map;

/**
 * fixme this need to be fixed with run configuration extension
 */
public class PerlDebugOptionsSets {
  public static final Map<String, String> STARTUP_OPTIONS = new HashMap<>();
  public static final Map<String, String> ROLE_OPTIONS = new HashMap<>();

  public static final String DEBUGGER_STARTUP_COMPILE = "COMPILE";
  public static final String DEBUGGER_STARTUP_RUN = "RUN";
  public static final String DEBUGGER_STARTUP_BREAKPOINT = "BREAKPOINT";

  static {
    STARTUP_OPTIONS.put(DEBUGGER_STARTUP_COMPILE, "Stop as soon as possible");
    STARTUP_OPTIONS.put(DEBUGGER_STARTUP_RUN, "Stop after compilation (use and BEGIN blocks are done)");
    STARTUP_OPTIONS.put(DEBUGGER_STARTUP_BREAKPOINT, "Stop at first breakpoint");

    ROLE_OPTIONS.put(PerlDebugOptions.ROLE_SERVER, "IDE connects to the perl process");
    ROLE_OPTIONS.put(PerlDebugOptions.ROLE_CLIENT, "Perl process connects to the IDE");
  }
}
