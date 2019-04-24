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

package editor;

import base.PerlLightTestCase;
import org.jetbrains.annotations.NotNull;

public abstract class PerlSmartKeysTestCase extends PerlLightTestCase {
  protected void doTest(@NotNull String initialText, @NotNull String toType, @NotNull String expected) {
    doTestTypingWithoutFiles(initialText, toType, expected);
  }

  /**
   * Types and checks if backspace returns to initial state
   */
  protected void doTestWithBS(@NotNull String initialText, @NotNull String toType, @NotNull String expected) {
    doTestTypingWithoutFiles(initialText, toType, expected);
    doTestBackspaceWithoutFiles(expected, initialText);
  }

  protected void doTest(@NotNull String toType) {
    doTestTyping(toType);
  }

  protected void doTestBS(@NotNull String initialText, @NotNull String expected) {
    doTestBackspaceWithoutFiles(initialText, expected);
  }
}
