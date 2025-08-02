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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lexer.Lexer;
import com.intellij.psi.MultiplePsiFilesPerDocumentFileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This token is for template representation in the base language for multi-psi files
 *
 * @see MultiplePsiFilesPerDocumentFileViewProvider
 */
public abstract class PerlReparseableTemplateTokenType extends PerlReparseableTokenType {

  public PerlReparseableTemplateTokenType(@NotNull String debugName) {
    super(debugName);
  }

  public PerlReparseableTemplateTokenType(@NotNull String debugName, @Nullable Language language) {
    super(debugName, language);
  }

  @Override
  protected @Nullable Lexer createLexer(@NotNull ASTNode nodeToLex) {
    return PerlReparseableElementType.createLexer(nodeToLex);
  }
}
