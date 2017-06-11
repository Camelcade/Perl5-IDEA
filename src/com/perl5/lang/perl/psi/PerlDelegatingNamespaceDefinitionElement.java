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

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class PerlDelegatingNamespaceDefinitionElement
  extends PerlDelegatingLightNamedElement<PerlPolyNamedElement, PerlNamespaceDefinitionStub>
  implements PerlNamespaceDefinitionElement {

  @NotNull
  private final PerlMroType myMroType;

  @NotNull
  private final List<String> myParentNamespacesNames;

  @Nullable
  private final PerlNamespaceAnnotations myAnnotations;

  @NotNull
  private final List<String> myExport;

  @NotNull
  private final List<String> myExportOk;

  @NotNull
  private final Map<String, List<String>> myExportTags;

  public PerlDelegatingNamespaceDefinitionElement(@NotNull PerlNamespaceDefinitionStub stub) {
    super(stub, stub.getPackageName());
    myMroType = stub.getMroType();
    myParentNamespacesNames = stub.getParentNamespacesNames();
    myAnnotations = stub.getAnnotations();
    myExport = stub.getEXPORT();
    myExportOk = stub.getEXPORT_OK();
    myExportTags = stub.getEXPORT_TAGS();
  }

  public PerlDelegatingNamespaceDefinitionElement(@NotNull PerlPolyNamedElement delegate,
                                                  @NotNull String name,
                                                  @NotNull IStubElementType elementType,
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
  public String getPackageName() {
    return getName();
  }

  @NotNull
  @Override
  public PerlMroType getMroType() {
    return myMroType;
  }

  @NotNull
  @Override
  public List<String> getParentNamespacesNames() {
    return myParentNamespacesNames;
  }

  @Nullable
  @Override
  public PerlNamespaceAnnotations getAnnotations() {
    return myAnnotations;
  }

  @NotNull
  @Override
  public List<String> getEXPORT() {
    return myExport;
  }

  @NotNull
  @Override
  public List<String> getEXPORT_OK() {
    return myExportOk;
  }

  @NotNull
  @Override
  public Map<String, List<String>> getEXPORT_TAGS() {
    return myExportTags;
  }
}
