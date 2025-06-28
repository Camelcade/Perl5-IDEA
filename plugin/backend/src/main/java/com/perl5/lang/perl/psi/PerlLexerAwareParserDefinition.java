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

package com.perl5.lang.perl.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public interface PerlLexerAwareParserDefinition extends ParserDefinition {
  /**
   * @param contextNode parent node or node itself for the parsed element, need to look around
   * @return lexer state that need to be used in base lexer for lexing particular {@code elementType}.
   * @apiNote This might be necessary for confirming leaf consistency with lexer in some tricky
   * cases, like templating languages. Base language elements may have different states, but template state is usually the same.   *
   */
  int getLexerStateFor(@NotNull ASTNode contextNode, @NotNull IElementType elementType);
}
