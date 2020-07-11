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

import com.intellij.lang.ASTFactory;
import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IReparseableElementType;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.PerlLexingContext;
import com.perl5.lang.perl.lexer.adapters.PerlMergingLexerAdapter;
import com.perl5.lang.perl.parser.PerlParserImpl;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;


public abstract class PerlLazyBlockElementType extends IReparseableElementType implements PsiElementProvider {
  private final Function<ASTNode, PsiElement> myInstanceFactory;

  public PerlLazyBlockElementType(@NotNull @NonNls String debugName) {
    this(debugName, PerlCompositeElementImpl.class);
  }

  public PerlLazyBlockElementType(@NotNull @NonNls String debugName, @NotNull Class<? extends PsiElement> clazz) {
    super(debugName, PerlLanguage.INSTANCE);
    myInstanceFactory = PerlElementTypeEx.createInstanceFactory(clazz);
  }

  @Override
  public @Nullable ASTNode createNode(CharSequence text) {
    return ASTFactory.DefaultFactoryHolder.DEFAULT.createLazy(this, text);
  }

  @Override
  public ASTNode parseContents(@NotNull ASTNode chameleon) {
    PsiElement parentElement = chameleon.getTreeParent().getPsi();
    Project project = parentElement.getProject();
    PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(
      project,
      chameleon,
      new PerlMergingLexerAdapter(getLexingContext(parentElement.getProject(), chameleon)),
      getLanguage(),
      chameleon.getText());

    return PerlParserImpl.INSTANCE.parse(this, builder).getFirstChildNode();
  }

  protected @NotNull PerlLexingContext getLexingContext(@NotNull Project project, @NotNull ASTNode chameleon) {
    return PerlLexingContext.create(project);
  }

  @Override
  public final @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
    return myInstanceFactory.apply(node);
  }
}
