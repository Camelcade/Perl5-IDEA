/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.perl.profiler.run;

import com.perl5.lang.perl.profiler.PerlProfilerBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import static com.perl5.lang.perl.profiler.PerlProfilerBundle.PATH_TO_BUNDLE;

public enum PerlProfilerStartupMode {
  BEGIN("begin", "start.immediately"),
  INIT("init", "start.at.beginning.of.init.phase.after.compilation.use.begin"),
  END("end", "start.at.beginning.of.end.phase"),
  NO("no", "don.t.automatically.start");


  private final @NonNls @NotNull String myProfilerCommand;
  private final @NotNull @PropertyKey(resourceBundle = PATH_TO_BUNDLE) String myExplanationKey;

  PerlProfilerStartupMode(@NotNull String profilerCommand,
                          @NotNull @PropertyKey(resourceBundle = PATH_TO_BUNDLE) String explanationKey) {
    myProfilerCommand = profilerCommand;
    myExplanationKey = explanationKey;
  }

  public @NonNls @NotNull String getProfilerCommand() {
    return myProfilerCommand;
  }

  public @NotNull @Nls String getExplanation() {
    return PerlProfilerBundle.message(myExplanationKey);
  }
}
