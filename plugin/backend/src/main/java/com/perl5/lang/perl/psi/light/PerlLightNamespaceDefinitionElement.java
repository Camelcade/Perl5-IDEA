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

package com.perl5.lang.perl.psi.light;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionWithIdentifier;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.impl.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class PerlLightNamespaceDefinitionElement extends PerlDelegatingLightNamedElement<PerlPolyNamedElement<?>>
  implements PerlNamespaceDefinitionWithIdentifier {

  private final @NotNull PerlMroType myMroType;

  private final @NotNull List<String> myParentNamespacesNames;

  private final @Nullable PerlNamespaceAnnotations myAnnotations;

  private final @NotNull List<String> myExport;

  private final @NotNull List<String> myExportOk;

  private final @NotNull Map<String, List<String>> myExportTags;

  public PerlLightNamespaceDefinitionElement(@NotNull PerlPolyNamedElement<?> delegate, @NotNull PerlNamespaceDefinitionStub stub) {
    super(delegate, stub.getNamespaceName(), stub.getElementType());
    myMroType = stub.getMroType();
    myParentNamespacesNames = stub.getParentNamespacesNames();
    myAnnotations = stub.getAnnotations();
    myExport = stub.getEXPORT();
    myExportOk = stub.getEXPORT_OK();
    myExportTags = stub.getEXPORT_TAGS();
  }

  public PerlLightNamespaceDefinitionElement(@NotNull PerlPolyNamedElement<?> delegate,
                                             @NotNull String name,
                                             @NotNull IElementType elementType,
                                             @NotNull PsiElement nameIdentifier,
                                             @NotNull PerlMroType mroType,
                                             @NotNull List<String> parentNamespacesNames,
                                             @Nullable PerlNamespaceAnnotations annotations,
                                             @NotNull List<String> export,
                                             @NotNull List<String> exportOk,
                                             @NotNull Map<String, List<String>> exportTags) {
    super(delegate, name, elementType, nameIdentifier);
    myMroType = mroType;
    myParentNamespacesNames = parentNamespacesNames;
    myAnnotations = annotations;
    myExport = export;
    myExportOk = exportOk;
    myExportTags = exportTags;
  }

  @Override
  public @NotNull String getNamespaceName() {
    return getName();
  }

  @Override
  public @NotNull PerlMroType getMroType() {
    return myMroType;
  }

  @Override
  public @NotNull List<String> getParentNamespacesNames() {
    return myParentNamespacesNames;
  }

  @Override
  public @Nullable PerlNamespaceAnnotations getAnnotations() {
    return myAnnotations;
  }

  @Override
  public @NotNull List<String> getEXPORT() {
    return myExport;
  }

  @Override
  public @NotNull List<String> getEXPORT_OK() {
    return myExportOk;
  }

  @Override
  public @NotNull Map<String, List<String>> getEXPORT_TAGS() {
    return myExportTags;
  }

  @Override
  public @Nullable PerlNamespaceElement getNamespaceElement() {
    throw new IncorrectOperationException("should not");
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor perlVisitor) {
      perlVisitor.visitPerlNamespaceDefinitionWithIdentifier(this);
    }
    else {
      super.accept(visitor);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PerlLightNamespaceDefinitionElement element)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    if (getMroType() != element.getMroType()) {
      return false;
    }
    if (!getParentNamespacesNames().equals(element.getParentNamespacesNames())) {
      return false;
    }
    if (getAnnotations() != null ? !getAnnotations().equals(element.getAnnotations()) : element.getAnnotations() != null) {
      return false;
    }
    if (!myExport.equals(element.myExport)) {
      return false;
    }
    if (!myExportOk.equals(element.myExportOk)) {
      return false;
    }
    return myExportTags.equals(element.myExportTags);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + getMroType().hashCode();
    result = 31 * result + getParentNamespacesNames().hashCode();
    result = 31 * result + (getAnnotations() != null ? getAnnotations().hashCode() : 0);
    result = 31 * result + myExport.hashCode();
    result = 31 * result + myExportOk.hashCode();
    result = 31 * result + myExportTags.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return super.toString() + "@" + getNamespaceName();
  }
}
