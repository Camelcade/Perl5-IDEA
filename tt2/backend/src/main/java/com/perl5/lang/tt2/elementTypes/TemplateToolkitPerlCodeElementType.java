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

package com.perl5.lang.tt2.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiParser;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.ILazyParseableElementType;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.PerlLexingContext;
import com.perl5.lang.perl.lexer.adapters.PerlMergingLexerAdapter;
import com.perl5.lang.perl.parser.PerlParserImpl;
import com.perl5.lang.perl.parser.builder.PerlPsiBuilderFactory;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.stubs.PerlStubElementTypes;
import com.perl5.lang.tt2.TemplateToolkitLanguage;
import com.perl5.lang.tt2.psi.impl.TemplateToolkitPerlBlockElementImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


public class TemplateToolkitPerlCodeElementType extends ILazyParseableElementType implements PsiElementProvider {
  public TemplateToolkitPerlCodeElementType(@NotNull @NonNls String debugName) {
    super(debugName, TemplateToolkitLanguage.INSTANCE);
  }

  @Override
  protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi) {
    Project project = psi.getProject();
    PsiBuilder builder = PerlPsiBuilderFactory.Companion.createBuilder(
      project,
      chameleon,
      new PerlMergingLexerAdapter(PerlLexingContext.create(project)),
      PerlLanguage.INSTANCE,
      chameleon.getText()
    );
    PsiParser parser = PerlParserImpl.INSTANCE;

    return parser.parse(PerlStubElementTypes.FILE, builder).getFirstChildNode();
  }

  @Override
  public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
    return new TemplateToolkitPerlBlockElementImpl(node);
  }
}
