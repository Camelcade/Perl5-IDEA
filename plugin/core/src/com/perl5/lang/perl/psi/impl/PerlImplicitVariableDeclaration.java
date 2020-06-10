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

package com.perl5.lang.perl.psi.impl;

import com.intellij.openapi.editor.Document;
import com.intellij.psi.*;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlScalarValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValues;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mixins.PerlMethodDefinitionMixin;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariableDeclarationStub;
import com.perl5.lang.perl.psi.utils.PerlVariableAnnotations;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collections;
import java.util.List;


public class PerlImplicitVariableDeclaration extends PerlImplicitElement
  implements PerlVariable, PerlVariableNameElement, PerlVariableDeclarationElement {
  protected final PerlVariableType myVariableType;
  protected final @NotNull String myVariableName;
  protected final @NotNull PerlValue myDeclaredValue;
  protected final @Nullable String myNamespaceName;

  protected final boolean myIsLexical;
  protected final boolean myIsLocal;
  protected final boolean myIsInvocant;

  /**
   * @deprecated use constructor with value
   */
  @Deprecated
  protected PerlImplicitVariableDeclaration(@NotNull PsiManager manager,
                                            @NotNull String variableNameWithSigil,
                                            @Nullable String namespaceName,
                                            @Nullable String variableClass,
                                            boolean isLexical,
                                            boolean isLocal,
                                            boolean isInvocant,
                                            @Nullable PsiElement parent) {
    this(manager,
         computeTypeFromNameWithSigil(variableNameWithSigil),
         variableNameWithSigil.substring(1),
         namespaceName,
         PerlScalarValue.create(variableClass),
         isLexical,
         isLocal,
         isInvocant,
         parent);
  }

  protected PerlImplicitVariableDeclaration(@NotNull PsiManager manager,
                                            @NotNull PerlVariableType type,
                                            @NotNull String variableName,
                                            @Nullable String namespaceName,
                                            @NotNull PerlValue variableValue,
                                            boolean isLexical,
                                            boolean isLocal,
                                            boolean isInvocant,
                                            @Nullable PsiElement parent) {
    super(manager, parent);
    myVariableType = type;
    myVariableName = variableName;
    myDeclaredValue = variableValue;
    myIsLexical = isLexical;
    myIsLocal = isLocal;
    myIsInvocant = isInvocant;
    myNamespaceName = namespaceName;
  }

  private static PerlVariableType computeTypeFromNameWithSigil(@NotNull String variableNameWithSigil) {
    if (variableNameWithSigil.startsWith("$")) {
      return PerlVariableType.SCALAR;
    }
    else if (variableNameWithSigil.startsWith("@")) {
      return PerlVariableType.ARRAY;
    }
    else if (variableNameWithSigil.startsWith("%")) {
      return PerlVariableType.HASH;
    }
    else if (variableNameWithSigil.startsWith("*")) {
      return PerlVariableType.GLOB;
    }
    throw new RuntimeException("Incorrect variable name, should start from sigil: " + variableNameWithSigil);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) {
      ((PerlVisitor)visitor).visitPerlVariableDeclarationElement(this);
    }
    else {
      visitor.visitElement(this);
    }
  }

  @Override
  public final @NotNull String getNamespaceName() {
    String explicitPackageName = this.getExplicitNamespaceName();
    return explicitPackageName != null ? explicitPackageName : PerlPackageUtil.getContextNamespaceName(this);
  }

  @Override
  public @Nullable String getExplicitNamespaceName() {
    return myNamespaceName;
  }

  @Override
  public String toString() {
    return "Implicit variable: " + getVariableType().getSigil() + getCanonicalName() + '@' + getDeclaredValue();
  }

  @Override
  public @NotNull PerlValue getDeclaredValue() {
    return myDeclaredValue;
  }

  @Override
  public @NotNull PerlValue computePerlValue() {
    return getDeclaredValue();
  }

  @Override
  public PerlVariableType getActualType() {
    return getVariableType();
  }

  @Override
  public PerlVariableDeclarationElement getLexicalDeclaration() {
    return null;
  }

  @Override
  public @NotNull List<PerlVariableDeclarationElement> getGlobalDeclarations() {
    return Collections.emptyList();
  }

  @Override
  public @NotNull List<PerlGlobVariable> getRelatedGlobs() {
    return Collections.emptyList();
  }

  @Override
  public int getLineNumber() {
    PsiFile file = getContainingFile();
    if (file == null) {
      return 0;
    }
    Document document = PsiDocumentManager.getInstance(getProject()).getCachedDocument(file);
    return document == null ? 0 : document.getLineNumber(getTextOffset()) + 1;
  }

  @Override
  public PerlLexicalScope getLexicalScope() {
    return PsiTreeUtil.getParentOfType(getParent(), PerlLexicalScope.class, false);
  }

  @Override
  public PerlVariableNameElement getVariableNameElement() {
    return this;
  }

  @Override
  public boolean isBuiltIn() {
    return false;
  }

  @Override
  public boolean isDeprecated() {
    return false;
  }

  public PerlVariableType getVariableType() {
    return myVariableType;
  }

  @Override
  public @NotNull String getVariableName() {
    return myVariableName;
  }

  @Override
  public @NotNull PerlVariable getVariable() {
    return this;
  }

  @Override
  public boolean isLexicalDeclaration() {
    return myIsLexical;
  }

  @Override
  public boolean isLocalDeclaration() {
    return myIsLocal;
  }

  @Override
  public boolean isGlobalDeclaration() {
    return !myIsLexical;
  }

  @Override
  public boolean isInvocantDeclaration() {
    return myIsInvocant;
  }

  @Override
  public String getPresentableName() {
    return isLocalDeclaration() ? getName() : getCanonicalName();
  }

  @Override
  public @Nullable Icon getIcon(int flags) {
    Icon iconByType = getIconByType(getActualType());
    return iconByType == null ? super.getIcon(flags) : iconByType;
  }

  @Override
  public @Nullable PsiElement getNameIdentifier() {
    return null;
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    return this;
  }

  @Override
  public IStubElementType<?, ?> getElementType() {
    return null;
  }

  @Override
  public PerlVariableDeclarationStub getStub() {
    return null;
  }

  @Override
  public @NotNull String getName() {
    return getVariableName();
  }

  @Override
  public boolean isDeclaration() {
    return true;
  }

  @Override
  public @Nullable PerlVariableAnnotations getVariableAnnotations() {
    return null;
  }

  @Override
  public @Nullable PerlVariableAnnotations getLocalVariableAnnotations() {
    return null;
  }

  @Override
  public @Nullable PerlVariableAnnotations getExternalVariableAnnotations() {
    return null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    PerlImplicitVariableDeclaration that = (PerlImplicitVariableDeclaration)o;

    if (myIsLexical != that.myIsLexical) {
      return false;
    }
    if (myIsLocal != that.myIsLocal) {
      return false;
    }
    if (myIsInvocant != that.myIsInvocant) {
      return false;
    }
    if (myVariableType != that.myVariableType) {
      return false;
    }
    if (!myVariableName.equals(that.myVariableName)) {
      return false;
    }
    if (!myDeclaredValue.equals(that.myDeclaredValue)) {
      return false;
    }
    return myNamespaceName != null ? myNamespaceName.equals(that.myNamespaceName) : that.myNamespaceName == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (myVariableType != null ? myVariableType.hashCode() : 0);
    result = 31 * result + myVariableName.hashCode();
    result = 31 * result + myDeclaredValue.hashCode();
    result = 31 * result + (myNamespaceName != null ? myNamespaceName.hashCode() : 0);
    result = 31 * result + (myIsLexical ? 1 : 0);
    result = 31 * result + (myIsLocal ? 1 : 0);
    result = 31 * result + (myIsInvocant ? 1 : 0);
    return result;
  }

  public static @NotNull PerlImplicitVariableDeclaration createGlobal(@NotNull PsiElement parent,
                                                                      @NotNull String variableName,
                                                                      @Nullable String variableClass
  ) {
    return create(parent, variableName, variableClass, false, false, false);
  }

  public static @NotNull PerlImplicitVariableDeclaration createGlobal(@NotNull PsiManager psiManager,
                                                                      @NotNull String variableName,
                                                                      @Nullable String packageName) {
    return create(psiManager, variableName, packageName, null, false, false, false, null);
  }

  public static @NotNull PerlImplicitVariableDeclaration createLexical(@NotNull PsiElement parent,
                                                                       @NotNull String variableName
  ) {
    return createLexical(parent, variableName, null);
  }

  public static @NotNull PerlImplicitVariableDeclaration createLexical(@NotNull PsiElement parent,
                                                                       @NotNull String variableName,
                                                                       @Nullable String variableClass
  ) {
    return create(parent, variableName, variableClass, true, false, false);
  }

  public static @NotNull PerlImplicitVariableDeclaration createInvocant(@NotNull PsiElement parent) {
    return new PerlImplicitVariableDeclaration(
      parent.getManager(),
      PerlVariableType.SCALAR,
      PerlMethodDefinitionMixin.getDefaultInvocantName().substring(1),
      null,
      PerlValues.FIRST_ARGUMENT_VALUE,
      true,
      false,
      true,
      parent
    );
  }

  private static @NotNull PerlImplicitVariableDeclaration create(@NotNull PsiElement parent,
                                                                 @NotNull String variableName,
                                                                 @Nullable String variableClass,
                                                                 boolean isLexical,
                                                                 boolean isLocal,
                                                                 boolean isInvocant
  ) {
    return create(parent.getManager(), variableName, null, variableClass, isLexical, isLocal, isInvocant, parent);
  }

  private static @NotNull PerlImplicitVariableDeclaration create(@NotNull PsiManager psiManager,
                                                                 @NotNull String variableNameWithSigil,
                                                                 @Nullable String packageName,
                                                                 @Nullable String variableClass,
                                                                 boolean isLexical,
                                                                 boolean isLocal,
                                                                 boolean isInvocant,
                                                                 @Nullable PsiElement parent
  ) {
    return new PerlImplicitVariableDeclaration(
      psiManager, variableNameWithSigil, packageName, variableClass, isLexical, isLocal, isInvocant, parent);
  }
}

