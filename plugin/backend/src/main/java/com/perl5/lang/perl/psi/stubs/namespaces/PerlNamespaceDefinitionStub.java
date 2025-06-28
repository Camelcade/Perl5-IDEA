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

package com.perl5.lang.perl.psi.stubs.namespaces;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class PerlNamespaceDefinitionStub extends StubBase<PerlNamespaceDefinitionElement> implements PerlNamespaceDefinition {
  private final @NotNull PerlNamespaceDefinitionData myData;

  public PerlNamespaceDefinitionStub(@Nullable StubElement parent,
                                     @NotNull IStubElementType elementType,
                                     @NotNull PerlNamespaceDefinitionData data) {
    super(parent, elementType);
    myData = data;
  }

  public @NotNull PerlNamespaceDefinitionData getData() {
    return myData;
  }

  @Override
  public @NotNull String getNamespaceName() {
    return myData.getNamespaceName();
  }

  @Override
  public @NotNull PerlMroType getMroType() {
    return myData.getMroType();
  }

  @Override
  public @NotNull List<String> getParentNamespacesNames() {
    return myData.getParentNamespacesNames();
  }

  @Override
  public @Nullable PerlNamespaceAnnotations getAnnotations() {
    return myData.getAnnotations();
  }

  @Override
  public @NotNull List<String> getEXPORT() {
    return myData.getEXPORT();
  }

  @Override
  public @NotNull List<String> getEXPORT_OK() {
    return myData.getEXPORT_OK();
  }

  @Override
  public @NotNull Map<String, List<String>> getEXPORT_TAGS() {
    return myData.getEXPORT_TAGS();
  }

  @Override
  public String toString() {
    return super.toString() + "\n" + myData;
  }
}
