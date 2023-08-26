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

package com.perl5.lang.perl.lexer.adapters;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlProtoLexer;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("FieldAccessedSynchronizedAndUnsynchronized")
final class LexerStatEntry {
  private final IElementType myElementType;
  private final int myEnterState;
  private int myLength = 0;
  private int myOuterSteps = 0;
  private int myInnerSteps = 0;
  private int myCounter = 0;

  LexerStatEntry(IElementType elementType, int enterState) {
    myElementType = elementType;
    myEnterState = enterState;
  }

  synchronized void register(@NotNull PerlProtoLexer lexer, int tokenStart) {
    myLength += lexer.getTokenEnd() - tokenStart;
    myOuterSteps += lexer.getOuterSteps();
    myInnerSteps += lexer.getInnerSteps();
    myCounter++;
  }

  int getSort() {
    return myInnerSteps;
  }

  @NotNull String asCsv() {
    return String.join(
      "\t", myElementType.toString(), String.valueOf(myEnterState), String.valueOf(myCounter), String.valueOf(myLength),
      String.valueOf(myOuterSteps), String.valueOf(myInnerSteps));
  }
}
