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

package com.perl5.lang.perl.idea.run.debugger;

import gnu.trove.THashMap;

import java.util.Map;


public class PerlDebugOptionsSets {
  public static final Map<String, String> STARTUP_OPTIONS = new THashMap<>();
  public static final Map<String, String> ROLE_OPTIONS = new THashMap<>();

  static {
    STARTUP_OPTIONS.put("COMPILE", "Stop as soon as possible");
    STARTUP_OPTIONS.put("RUN", "Stop after compilation (use and BEGIN blocks are done)");
    STARTUP_OPTIONS.put("BREAKPOINT", "Stop at first breakpoint");

    ROLE_OPTIONS.put(PerlDebugOptions.ROLE_SERVER, "IDE connects to the perl process");
    ROLE_OPTIONS.put(PerlDebugOptions.ROLE_CLIENT, "Perl process connects to the IDE");
  }
}
