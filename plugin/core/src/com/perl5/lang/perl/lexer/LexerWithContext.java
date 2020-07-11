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

package com.perl5.lang.perl.lexer;

import com.intellij.lexer.Lexer;
import org.jetbrains.annotations.NotNull;

public class LexerWithContext {
  private final @NotNull Lexer myLexer;
  private final @NotNull PerlLexingContext myLexingContext;

  private LexerWithContext(@NotNull Lexer lexer, @NotNull PerlLexingContext lexingContext) {
    myLexer = lexer;
    myLexingContext = lexingContext;
  }

  public @NotNull Lexer getLexer() {
    return myLexer;
  }

  public @NotNull PerlLexingContext getLexingContext() {
    return myLexingContext;
  }

  public static @NotNull LexerWithContext create(@NotNull Lexer lexer, @NotNull PerlLexingContext lexingContext) {
    return new LexerWithContext(lexer, lexingContext);
  }
}
