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

package com.perl5.lang.perl.psi;

import com.perl5.lang.perl.parser.Exception.Class.psi.impl.PerlExceptionClassWrapper;
import com.perl5.lang.perl.parser.constant.psi.impl.PerlConstantsWrapper;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PerlHeredocTerminatorElementImpl;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 14.06.2015.
 * Extension of generated visitor
 */
public class PerlVisitor extends PsiPerlVisitor {
  public void visitNamespaceElement(@NotNull PerlNamespaceElement o) {
    visitPsiElement(o);
  }

  public void visitNamespaceDefinitionElement(@NotNull PerlNamespaceDefinitionElement o) {
    visitElement(o);
  }

  @Override
  public final void visitNamespaceDefinition(@NotNull PsiPerlNamespaceDefinition o) {
    visitNamespaceDefinitionElement(o);
  }

  @Override
  public void visitPerlNamespaceDefinitionWithIdentifier(@NotNull PerlNamespaceDefinitionWithIdentifier o) {
    visitNamespaceDefinitionElement(o);
  }

  public void visitVariableNameElement(@NotNull PerlVariableNameElement o) {
    visitPsiElement(o);
  }

  public void visitSubNameElement(@NotNull PerlSubNameElement o) {
    visitPsiElement(o);
  }

  public void visitStringContentElement(@NotNull PerlStringContentElementImpl o) {
    visitPsiElement(o);
  }

  public void visitHeredocTeminator(@NotNull PerlHeredocTerminatorElementImpl o) {
    visitComment(o);
  }

  public void visitHeredocElement(@NotNull PerlHeredocElementImpl o) {
    visitPsiElement(o);
  }


  public void visitPerlVariable(@NotNull PerlVariable o) {
    visitExpr(o);
  }

  public void visitPerlCastExpression(@NotNull PerlCastExpression o) {
    visitExpr(o);
  }

  public void visitArrayIndexVariable(@NotNull PsiPerlArrayIndexVariable o) {
    visitPerlVariable(o);
  }

  public void visitArrayVariable(@NotNull PsiPerlArrayVariable o) {
    visitPerlVariable(o);
  }

  public void visitHashVariable(@NotNull PsiPerlHashVariable o) {
    visitPerlVariable(o);
  }

  public void visitScalarVariable(@NotNull PsiPerlScalarVariable o) {
    visitPerlVariable(o);
  }

  @Override
  public void visitScalarCastExpr(@NotNull PsiPerlScalarCastExpr o) {
    visitPerlCastExpression(o);
  }

  @Override
  public void visitArrayCastExpr(@NotNull PsiPerlArrayCastExpr o) {
    visitPerlCastExpression(o);
  }

  @Override
  public void visitHashCastExpr(@NotNull PsiPerlHashCastExpr o) {
    visitPerlCastExpression(o);
  }

  @Override
  public void visitCodeCastExpr(@NotNull PsiPerlCodeCastExpr o) {
    visitPerlCastExpression(o);
  }

  @Override
  public void visitGlobCastExpr(@NotNull PsiPerlGlobCastExpr o) {
    visitPerlCastExpression(o);
  }

  @Override
  public void visitPerlMethodDefinition(@NotNull PerlMethodDefinition o) {
    visitPerlSubDefinitionElement(o);
  }

  public void visitPolyNamedElement(@NotNull PerlPolyNamedElement o) {
    visitElement(o);
  }

  public void visitConstantWrapper(@NotNull PerlConstantsWrapper o) {
    visitPolyNamedElement(o);
  }

  public void visitLightNamedElement(@NotNull PerlDelegatingLightNamedElement o) {
    visitElement(o);
  }

  public void visitExceptionClassWrapper(@NotNull PerlExceptionClassWrapper o) {
    visitPolyNamedElement(o);
  }

  @Override
  public final void visitSubDefinition(@NotNull PsiPerlSubDefinition o) {
    visitPerlSubDefinitionElement(o);
  }

  public void visitSubDeclarationElement(@NotNull PerlSubDeclarationElement o) {
    visitElement(o);
  }

  @Override
  public final void visitSubDeclaration(@NotNull PsiPerlSubDeclaration o) {
    visitSubDeclarationElement(o);
  }
}
