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

/**
 * Created by hurricup on 28.05.2015.
 */
public class PerlNamespaceDefinitionStub extends StubBase<PerlNamespaceDefinitionElement> implements PerlNamespaceDefinition {
  private final String myPackageName;
  private final PerlMroType myMroType;
  private final List<String> myParentNamespaces;
  private final List<String> myEXPORT;
  private final List<String> myEXPORT_OK;
  private final Map<String, List<String>> myEXPORT_TAGS;
  private final PerlNamespaceAnnotations myPerlNamespaceAnnotations;

  public PerlNamespaceDefinitionStub(
    StubElement parent,
    IStubElementType elementType,
    String packageName,
    PerlMroType mroType,
    List<String> parentNamespaces,
    List<String> EXPORT,
    List<String> EXPORT_OK,
    Map<String, List<String>> EXPORT_TAGS,
    PerlNamespaceAnnotations namespaceAnnotations
  ) {
    super(parent, elementType);
    myPackageName = packageName;
    myMroType = mroType;
    myParentNamespaces = parentNamespaces;
    myPerlNamespaceAnnotations = namespaceAnnotations;
    myEXPORT = EXPORT;
    myEXPORT_OK = EXPORT_OK;
    myEXPORT_TAGS = EXPORT_TAGS;
  }

  @Override
  public String getPackageName() {
    return myPackageName;
  }

  @Override
  public PerlMroType getMroType() {
    return myMroType;
  }

  @NotNull
  @Override
  public List<String> getParentNamespacesNames() {
    return myParentNamespaces;
  }

  @Nullable
  @Override
  public PerlNamespaceAnnotations getAnnotations() {
    return myPerlNamespaceAnnotations;
  }

  @NotNull
  @Override
  public List<String> getEXPORT() {
    return myEXPORT;
  }

  @NotNull
  @Override
  public List<String> getEXPORT_OK() {
    return myEXPORT_OK;
  }

  @NotNull
  @Override
  public Map<String, List<String>> getEXPORT_TAGS() {
    return myEXPORT_TAGS;
  }
}
