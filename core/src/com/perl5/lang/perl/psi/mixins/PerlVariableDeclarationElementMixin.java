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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlScalarValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimpleDynamicLocation;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariableDeclarationStub;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableAnnotations;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

import static com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues.UNKNOWN_VALUE;

/**
 * Created by hurricup on 29.09.2015.
 * Stubbed wrapper for variables declarations
 */
public class PerlVariableDeclarationElementMixin extends PerlStubBasedPsiElementBase<PerlVariableDeclarationStub>
  implements PerlVariableDeclarationElement {
  public PerlVariableDeclarationElementMixin(ASTNode node) {
    super(node);
  }

  public PerlVariableDeclarationElementMixin(@NotNull PerlVariableDeclarationStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @NotNull
  @Override
  public PerlVariable getVariable() {
    return Objects.requireNonNull(findChildByClass(PerlVariable.class));
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    return getVariable().getVariableNameElement();
  }


  @Override
  public String getVariableName() {
    return getName();
  }

  @Override
  public String getName() {
    PerlVariableDeclarationStub stub = getGreenStub();
    if (stub != null) {
      return stub.getVariableName();
    }
    return getVariable().getName();
  }

  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    PerlPsiUtil.renameNamedElement(this, name);
    return this;
  }

  @Override
  public int getTextOffset() {
    PsiElement nameIdentifier = getNameIdentifier();

    return nameIdentifier == null
           ? super.getTextOffset()
           : nameIdentifier.getTextOffset();
  }

  @Nullable
  @Override
  public String getExplicitNamespaceName() {
    return getVariable().getExplicitNamespaceName();
  }

  @NotNull
  @Override
  public PerlValue getDeclaredValue() {
    PerlValue valueFromAnnotations = PerlVariableDeclarationElement.super.getDeclaredValue();
    if (!valueFromAnnotations.isEmpty()) {
      return valueFromAnnotations;
    }
    return getPsiDeclaredValue();
  }

  @NotNull
  public PerlValue getPsiDeclaredValue() {
    PerlVariableDeclarationStub stub = getGreenStub();
    if (stub != null) {
      return stub.getDeclaredValue();
    }
    PerlVariableDeclarationExpr declaration = getPerlDeclaration();
    return declaration == null ? UNKNOWN_VALUE :
           PerlScalarValue.create(declaration.getDeclarationType());
  }

  @Nullable
  private PerlVariableDeclarationExpr getPerlDeclaration() {
    return PsiTreeUtil.getParentOfType(this, PerlVariableDeclarationExpr.class);
  }


  @NotNull
  @Override
  public String getNamespaceName() {
    PerlVariableDeclarationStub stub = getGreenStub();
    if (stub != null) {
      return stub.getNamespaceName();
    }
    String qualifierName = getVariable().getExplicitNamespaceName();
    if (StringUtil.isNotEmpty(qualifierName)) {
      return qualifierName;
    }
    return PerlPackageUtil.getContextNamespaceName(getVariable());
  }

  @Override
  public PerlVariableType getActualType() {
    PerlVariableDeclarationStub stub = getGreenStub();
    if (stub != null) {
      return stub.getActualType();
    }
    return getVariable().getActualType();
  }

  @NotNull
  @Override
  public SearchScope getUseScope() {
    if (isLexicalDeclaration()) {
      PerlLexicalScope lexicalScope = getVariable().getLexicalScope();
      if (lexicalScope != null) {
        return new LocalSearchScope(lexicalScope);
      }
    }

    return super.getUseScope();
  }

  @Override
  public boolean isLexicalDeclaration() {
    if (getGreenStub() != null) {
      return false;
    }
    PsiElement parent = getParent();
    return parent instanceof PerlLexicalVariableDeclarationMarker ||
           isInvocantDeclaration() || isLocalDeclaration();
  }

  @Override
  public boolean isInvocantDeclaration() {
    return getParent() instanceof PsiPerlMethodSignatureInvocant;
  }

  @Override
  public boolean isLocalDeclaration() {
    return getParent() instanceof PsiPerlVariableDeclarationLocal;
  }

  @Override
  public boolean isGlobalDeclaration() {
    return !isLexicalDeclaration();
  }

  @Override
  public ItemPresentation getPresentation() {
    return new PerlItemPresentationSimpleDynamicLocation(this, getName());
  }


  @Nullable
  @Override
  public Icon getIcon(int flags) {
    Icon iconByType = getIconByType(getActualType());
    return iconByType == null ? super.getIcon(flags) : iconByType;
  }

  @Nullable
  @Override
  public PerlVariableAnnotations getVariableAnnotations() {
    PerlVariableAnnotations variableAnnotations;

    PerlVariableDeclarationStub stub = getGreenStub();
    if (stub != null) {
      variableAnnotations = stub.getVariableAnnotations();
    }
    else {
      // re-parsing
      variableAnnotations = getLocalVariableAnnotations();
    }

    if (variableAnnotations != null) {
      return variableAnnotations;
    }

    return getExternalVariableAnnotations();
  }

  @Nullable
  @Override
  public PerlVariableAnnotations getLocalVariableAnnotations() {
    List<PerlAnnotation> perlAnnotations = PerlPsiUtil.collectAnnotations(this);
    if (perlAnnotations.isEmpty()) {
      perlAnnotations = PerlPsiUtil.collectAnnotations(getPerlDeclaration());
    }
    return PerlVariableAnnotations.createFromAnnotationsList(perlAnnotations);
  }

  @Nullable
  @Override
  public PerlVariableAnnotations getExternalVariableAnnotations() {
    // fixme NYI
    return null;
  }
}
