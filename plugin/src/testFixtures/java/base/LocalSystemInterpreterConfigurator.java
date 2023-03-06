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

package base;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

class LocalSystemInterpreterConfigurator extends PerlInterpreterConfigurator {
  public static final PerlInterpreterConfigurator INSTANCE = new LocalSystemInterpreterConfigurator();
  private static final String TEST_SYSTEM_PERL_INTERPRETER_PATH = "TEST_SYSTEM_PERL_INTERPRETER_PATH";


  private LocalSystemInterpreterConfigurator() {
  }

  @Override
  protected @NotNull String getInterpreterPath() {
    return Objects.requireNonNull(System.getenv(TEST_SYSTEM_PERL_INTERPRETER_PATH),
                                  "Path to interpreter should be passed via TEST_SYSTEM_PERL_INTERPRETER_PATH for " +
                                  getClass().getSimpleName());
  }

  @Override
  public String toString() {
    return "system: " + System.getenv(TEST_SYSTEM_PERL_INTERPRETER_PATH);
  }
}
