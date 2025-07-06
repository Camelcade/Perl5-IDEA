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

package com.perl5.lang.perl.psi.stubs;

import com.intellij.lang.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IFileElementType;
import com.perl5.lang.perl.parser.builder.PerlPsiBuilderFactory;
import org.jetbrains.annotations.NotNull;

public final class PerlFileElementType extends IFileElementType {

  public PerlFileElementType(String debugName, Language language) {
    super(debugName, language);
  }

  @Override
  protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi) {
    return getParser(psi).parse(this, getBuilder(psi, chameleon)).getFirstChildNode();
  }

  private @NotNull PsiParser getParser(PsiElement psi) {
    return LanguageParserDefinitions.INSTANCE.forLanguage(getLanguageForParser(psi)).createParser(psi.getProject());
  }

  private @NotNull PsiBuilder getBuilder(PsiElement psi, ASTNode chameleon) {
    return PerlPsiBuilderFactory.Companion.createBuilder(
      psi.getProject(), chameleon, null, getLanguageForParser(psi), chameleon.getChars());
  }
}
