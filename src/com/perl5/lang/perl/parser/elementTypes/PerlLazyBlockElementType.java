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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.ILazyParseableElementType;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.lexer.adapters.PerlMergingLexerAdapter;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Created by hurricup on 23.10.2016.
 */
public abstract class PerlLazyBlockElementType extends ILazyParseableElementType implements PsiElementProvider {
  private final Function<ASTNode, PsiElement> myInstanceFactory;

  public PerlLazyBlockElementType(@NotNull @NonNls String debugName) {
    this(debugName, PerlCompositeElementImpl.class);
  }

  public PerlLazyBlockElementType(@NotNull @NonNls String debugName, @NotNull Class<? extends PsiElement> clazz) {
    super(debugName, PerlLanguage.INSTANCE);
    myInstanceFactory = PerlElementTypeEx.createInstanceFactory(clazz);
  }

  @Override
  public ASTNode parseContents(ASTNode chameleon) {
    PsiElement parentElement = chameleon.getTreeParent().getPsi();
    Project project = parentElement.getProject();
    PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(
      project,
      chameleon,
      getLexer(parentElement.getProject()),
      getLanguage(),
      chameleon.getText());

    return getParser().parse(this, builder).getFirstChildNode();
  }


  @NotNull
  protected Lexer getLexer(@NotNull Project project) {
    return new PerlMergingLexerAdapter(getInnerLexer(project));
  }

  @NotNull
  protected abstract Lexer getInnerLexer(@NotNull Project project);

  @NotNull
  protected abstract PsiParser getParser();

  @NotNull
  @Override
  public final PsiElement getPsiElement(@NotNull ASTNode node) {
    return myInstanceFactory.apply(node);
  }
}
