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

package com.perl5.lang.perl.psi;

import com.perl5.lang.perl.psi.impl.*;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import org.jetbrains.annotations.NotNull;

/**
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

  @Override
  public void visitArrayIndexVariable(@NotNull PsiPerlArrayIndexVariable o) {
    visitPerlVariable(o);
  }

  @Override
  public void visitArrayVariable(@NotNull PsiPerlArrayVariable o) {
    visitPerlVariable(o);
  }

  @Override
  public void visitHashVariable(@NotNull PsiPerlHashVariable o) {
    visitPerlVariable(o);
  }

  @Override
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

  @Override
  public void visitSubCall(@NotNull PsiPerlSubCall o) {
    visitPolyNamedElement((PerlPolyNamedElement<?>)o);
  }

  public void visitPolyNamedElement(@NotNull PerlPolyNamedElement<?> o) {
    visitElement(o);
    visitLightElements(o);
  }

  protected final void visitLightElements(@NotNull PerlPolyNamedElement<?> o) {
    if (!shouldVisitLightElements()) {
      return;
    }
    for (PerlDelegatingLightNamedElement<?> lightNamedElement : o.getLightElements()) {
      lightNamedElement.accept(this);
    }
  }

  protected boolean shouldVisitLightElements() {
    return false;
  }

  public void visitLightNamedElement(@NotNull PerlDelegatingLightNamedElement<?> o) {
    visitElement(o);
  }

  public void visitUseStatement(@NotNull PerlUseStatementElement o) {
    visitPolyNamedElement(o);
  }

  public void visitNoStatement(@NotNull PerlNoStatementElement o) {
    visitPolyNamedElement(o);
  }

  @Override
  public final void visitSubDefinition(@NotNull PsiPerlSubDefinition o) {
    visitPerlSubDefinitionElement(o);
  }

  @Override
  public void visitPerlSubDefinitionElement(@NotNull PerlSubDefinitionElement o) {
    visitPerlSubElement(o);
  }

  public void visitPerlSubElement(@NotNull PerlSubElement o) {
    visitElement(o);
  }

  public void visitSubDeclarationElement(@NotNull PerlSubDeclarationElement o) {
    visitPerlSubElement(o);
  }

  @Override
  public final void visitSubDeclaration(@NotNull PsiPerlSubDeclaration o) {
    visitSubDeclarationElement(o);
  }

  @Override
  public void visitVariableDeclarationGlobal(@NotNull PsiPerlVariableDeclarationGlobal o) {
    visitPerlVariableDeclarationExpr(o);
  }

  @Override
  public void visitVariableDeclarationLexical(@NotNull PsiPerlVariableDeclarationLexical o) {
    visitPerlVariableDeclarationExpr(o);
  }

  @Override
  public void visitVariableDeclarationLocal(@NotNull PsiPerlVariableDeclarationLocal o) {
    visitPerlVariableDeclarationExpr(o);
  }

  public void visitPerlVariableDeclarationExpr(@NotNull PerlVariableDeclarationExpr o) {
    visitExpr(o);
  }

  @Override
  public void visitNextExpr(@NotNull PsiPerlNextExpr o) {
    visitPerlFlowControlExpr(o);
  }

  public void visitPerlFlowControlExpr(PerlFlowControlExpr o) {
    visitExpr(o);
  }

  @Override
  public void visitLastExpr(@NotNull PsiPerlLastExpr o) {
    visitPerlFlowControlExpr(o);
  }

  @Override
  public void visitRedoExpr(@NotNull PsiPerlRedoExpr o) {
    visitPerlFlowControlExpr(o);
  }
}
