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

package com.perl5.lang.mason2.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.htmlmason.MasonCoreUtilCore;
import com.perl5.lang.mason2.idea.configuration.MasonSettings;
import com.perl5.lang.mason2.psi.MasonNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PsiPerlNamespaceDefinitionImpl;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MasonNamespaceDefinitionImpl extends PsiPerlNamespaceDefinitionImpl implements MasonNamespaceDefinition {
  protected List<PerlVariableDeclarationElement> myImplicitVariables = null;
  protected int mySettingsChangeCounter;

  public MasonNamespaceDefinitionImpl(ASTNode node) {
    super(node);
  }

  public MasonNamespaceDefinitionImpl(PerlNamespaceDefinitionStub stub, IElementType nodeType) {
    super(stub, nodeType);
  }

  protected @NotNull List<PerlVariableDeclarationElement> buildImplicitVariables(MasonSettings masonSettings) {
    List<PerlVariableDeclarationElement> newImplicitVariables = new ArrayList<>();

    if (isValid()) {
      MasonCoreUtilCore.fillVariablesList(this, newImplicitVariables, masonSettings.globalVariables);
    }
    return newImplicitVariables;
  }

  @Override
  public PerlNamespaceElement getNamespaceElement() {
    return null;
  }

  @Override
  public @Nullable String getNamespaceName() {
    return MasonNamespaceDefinitionService.getInstance().getNamespaceName(this);
  }

  @Override
  protected @Nullable String computeNamespaceName() {
    return MasonNamespaceDefinitionService.getInstance().computeNamespaceName(this);
  }

  @Override
  public @Nullable String getPresentableName() {
    return MasonNamespaceDefinitionService.getInstance().getPresentableName(this);
  }

  @Override
  public @NotNull MasonFileImpl getContainingFile() {
    return (MasonFileImpl)super.getContainingFile();
  }

  @Override
  public @NotNull List<PerlVariableDeclarationElement> getImplicitVariables() {
    MasonSettings settings = MasonSettings.getInstance(getProject());
    if (myImplicitVariables == null || mySettingsChangeCounter != settings.getChangeCounter()) {
      myImplicitVariables = buildImplicitVariables(settings);
      mySettingsChangeCounter = settings.getChangeCounter();
    }
    return myImplicitVariables;
  }
}
