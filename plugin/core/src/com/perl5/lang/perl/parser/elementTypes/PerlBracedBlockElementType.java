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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lexer.FlexAdapter;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.lexer.PerlLexer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.LEFT_BRACE;

public abstract class PerlBracedBlockElementType extends PerlReparseableElementType {
  public PerlBracedBlockElementType(@NotNull String debugName,
                                    @NotNull Class<? extends PsiElement> clazz) {
    super(debugName, clazz);
  }

  @Override
  public boolean isParsable(@Nullable ASTNode parent,
                            @NotNull CharSequence buffer,
                            @NotNull Language fileLanguage,
                            @NotNull Project project) {
    // fixme we should probably check file for use TryCatch, hacky but still
    FlexAdapter lexer = new FlexAdapter(new PerlLexer(null).withProject(project));
    boolean result = hasProperBraceBalance(buffer, lexer, LEFT_BRACE);
    if (LOG.isDebugEnabled()) {
      LOG.debug("Block reparseable: ", result && lexer.getState() == 0,
                "; balanced: ", result,
                "; lexer state: ", lexer.getState());
    }
    return result && lexer.getState() == PerlLexer.AFTER_RIGHT_BRACE;
  }
}
