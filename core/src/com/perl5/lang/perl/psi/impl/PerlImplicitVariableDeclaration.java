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

package com.perl5.lang.perl.psi.impl;

import com.intellij.openapi.editor.Document;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.idea.codeInsight.typeInferrence.value.PerlValue;
import com.perl5.lang.perl.idea.codeInsight.typeInferrence.value.PerlValueStatic;
import com.perl5.lang.perl.psi.PerlGlobVariable;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
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
import java.util.List;

/**
 * Created by hurricup on 17.01.2016.
 */
public class PerlImplicitVariableDeclaration extends PerlImplicitElement
  implements PerlVariable, PerlVariableNameElement, PerlVariableDeclarationElement {
  protected final PerlVariableType myVariableType;
  @NotNull
  protected final String myVariableName;
  @NotNull
  protected final PerlValue myDeclaredValue;
  @Nullable
  protected final String myPackageName;

  protected final boolean myIsLexical;
  protected final boolean myIsLocal;
  protected final boolean myIsInvocant;

  protected PerlImplicitVariableDeclaration(@NotNull PsiManager manager,
                                            @NotNull String variableNameWithSigil,
                                            @Nullable String packageName,
                                            @Nullable String variableClass,
                                            boolean isLexical,
                                            boolean isLocal,
                                            boolean isInvocant,
                                            @Nullable PsiElement parent) {
    super(manager, parent);

    PerlVariableType type = null;

    if (variableNameWithSigil.startsWith("$")) {
      type = PerlVariableType.SCALAR;
    }
    else if (variableNameWithSigil.startsWith("@")) {
      type = PerlVariableType.ARRAY;
    }
    else if (variableNameWithSigil.startsWith("%")) {
      type = PerlVariableType.HASH;
    }
    else if (variableNameWithSigil.startsWith("*")) {
      type = PerlVariableType.GLOB;
    }

    if (type != null) {
      myVariableType = type;
      myVariableName = variableNameWithSigil.substring(1);
      myDeclaredValue = PerlValueStatic.create(variableClass);
      myIsLexical = isLexical;
      myIsLocal = isLocal;
      myIsInvocant = isInvocant;
      myPackageName = packageName;
    }
    else {
      throw new RuntimeException("Incorrect variable name, should start from sigil: " + variableNameWithSigil);
    }
  }

  @NotNull
  @Override
  public final String getNamespaceName() {
    String explicitPackageName = this.getExplicitNamespaceName();
    return explicitPackageName != null ? explicitPackageName : PerlPackageUtil.getContextNamespaceName(this);
  }

  @Nullable
  @Override
  public String getExplicitNamespaceName() {
    return myPackageName;
  }

  @Override
  public String toString() {
    return "Implicit variable: " + getVariableType().getSigil() + getCanonicalName() + '@' + getDeclaredValue();
  }

  @NotNull
  @Override
  public PerlValue getDeclaredValue() {
    return myDeclaredValue;
  }

  @NotNull
  @Override
  public PerlValue computePerlValue() {
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
  public List<PerlVariableDeclarationElement> getGlobalDeclarations() {
    return null;
  }

  @Override
  public List<PerlGlobVariable> getRelatedGlobs() {
    return null;
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

  @NotNull
  public String getVariableName() {
    return myVariableName;
  }

  @NotNull
  @Override
  public PerlVariable getVariable() {
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

  @Nullable
  @Override
  public Icon getIcon(int flags) {
    Icon iconByType = getIconByType(getActualType());
    return iconByType == null ? super.getIcon(flags) : iconByType;
  }

  @Nullable
  @Override
  public PsiElement getNameIdentifier() {
    return null;
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    return this;
  }

  @Override
  public IStubElementType getElementType() {
    return null;
  }

  @Override
  public PerlVariableDeclarationStub getStub() {
    return null;
  }

  @NotNull
  @Override
  public String getName() {
    return getVariableName();
  }

  @Override
  public boolean isDeclaration() {
    return true;
  }

  @Nullable
  @Override
  public PerlVariableAnnotations getVariableAnnotations() {
    return null;
  }

  @Nullable
  @Override
  public PerlVariableAnnotations getLocalVariableAnnotations() {
    return null;
  }

  @Nullable
  @Override
  public PerlVariableAnnotations getExternalVariableAnnotations() {
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
    return myPackageName != null ? myPackageName.equals(that.myPackageName) : that.myPackageName == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (myVariableType != null ? myVariableType.hashCode() : 0);
    result = 31 * result + myVariableName.hashCode();
    result = 31 * result + myDeclaredValue.hashCode();
    result = 31 * result + (myPackageName != null ? myPackageName.hashCode() : 0);
    result = 31 * result + (myIsLexical ? 1 : 0);
    result = 31 * result + (myIsLocal ? 1 : 0);
    result = 31 * result + (myIsInvocant ? 1 : 0);
    return result;
  }

  @NotNull
  public static PerlImplicitVariableDeclaration createGlobal(@NotNull PsiElement parent,
                                                             @NotNull String variableName,
                                                             @Nullable String variableClass
  ) {
    return create(parent, variableName, variableClass, false, false, false);
  }

  @NotNull
  public static PerlImplicitVariableDeclaration createGlobal(@NotNull PsiManager psiManager,
                                                             @NotNull String variableName,
                                                             @Nullable String packageName) {
    return create(psiManager, variableName, packageName, null, false, false, false, null);
  }

  @NotNull
  public static PerlImplicitVariableDeclaration createLexical(@NotNull PsiElement parent,
                                                              @NotNull String variableName
  ) {
    return createLexical(parent, variableName, null);
  }

  @NotNull
  public static PerlImplicitVariableDeclaration createLexical(@NotNull PsiElement parent,
                                                              @NotNull String variableName,
                                                              @Nullable String variableClass
  ) {
    return create(parent, variableName, variableClass, true, false, false);
  }

  @NotNull
  public static PerlImplicitVariableDeclaration createInvocant(@NotNull PsiElement parent) {
    return create(parent, PerlMethodDefinitionMixin.getDefaultInvocantName(), PerlPackageUtil.getContextNamespaceName(parent), true, false,
                  true);
  }

  @NotNull
  private static PerlImplicitVariableDeclaration create(@NotNull PsiElement parent,
                                                        @NotNull String variableName,
                                                        @Nullable String variableClass,
                                                        boolean isLexical,
                                                        boolean isLocal,
                                                        boolean isInvocant
  ) {
    return create(parent.getManager(), variableName, null, variableClass, isLexical, isLocal, isInvocant, parent);
  }

  @NotNull
  private static PerlImplicitVariableDeclaration create(@NotNull PsiManager psiManager,
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

