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
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonArgsBlock;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariableDeclarationStub;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableAnnotations;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

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

  @Override
  public PerlVariable getVariable() {
    return findChildByClass(PerlVariable.class);
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    PerlVariable perlVariable = getVariable();
    if (perlVariable != null) {
      return perlVariable.getVariableNameElement();
    }
    return null;
  }


  @Override
  public String getPresentableName() {
    return getName();
  }

  @Override
  public String getVariableName() {
    return getName();
  }

  @Override
  public String getName() {
    PerlVariableDeclarationStub stub = getStub();
    if (stub != null) {
      return stub.getVariableName();
    }
    return getVariable().getName();
  }

  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier != null) {
      PerlPsiUtil.renameElement(nameIdentifier, name);
    }
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
  public String getDeclaredType() {
    PerlVariableAnnotations variableAnnotations = getVariableAnnotations();
    if (variableAnnotations != null) {
      String type = variableAnnotations.getType();
      if (type != null) {
        return type;
      }
    }

    PerlVariableDeclarationStub stub = getStub();
    if (stub != null) {
      return stub.getDeclaredType();
    }
    else {
      return getLocallyDeclaredType();
    }
  }

  @Nullable
  @Override
  public String getLocallyDeclaredType() {
    PerlVariableDeclarationExpr declaration = getPerlDeclaration();
    return declaration == null ? null : declaration.getDeclarationType();
  }

  @Nullable
  private PerlVariableDeclarationExpr getPerlDeclaration() {
    return PsiTreeUtil.getParentOfType(this, PerlVariableDeclarationExpr.class);
  }


  @Nullable
  @Override
  public String getPackageName() {
    PerlVariableDeclarationStub stub = getStub();
    if (stub != null) {
      return stub.getPackageName();
    }
    return getVariable().getPackageName();
  }

  @Override
  public PerlVariableType getActualType() {
    PerlVariableDeclarationStub stub = getStub();
    if (stub != null) {
      return stub.getActualType();
    }
    return getVariable().getActualType();
  }

  @NotNull
  @Override
  public SearchScope getUseScope() {
    if (isLexicalDeclaration()) {
      return new LocalSearchScope(getVariable().getLexicalScope());
    }

    return super.getUseScope();
  }

  @Override
  public boolean isLexicalDeclaration() {
    PsiElement parent = getParent();
    return parent instanceof PsiPerlVariableDeclarationLexical ||
           parent instanceof PsiPerlSubSignature ||
           isInvocantDeclaration() ||
           isLocalDeclaration() ||
           isArgsDeclaration()
      ;
  }

  @Override
  public boolean isInvocantDeclaration() {
    return getParent() instanceof PsiPerlMethodSignatureInvocant;
  }

  // fixme temporary hack, see #899
  public boolean isArgsDeclaration() {
    return getParent() instanceof HTMLMasonArgsBlock;
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
    return new PerlItemPresentationSimple(this, getName());
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

    PerlVariableDeclarationStub stub = getStub();
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
