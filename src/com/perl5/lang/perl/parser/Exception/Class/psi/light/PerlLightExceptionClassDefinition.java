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

package com.perl5.lang.perl.parser.Exception.Class.psi.light;

import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.psi.PerlPolyNamedElement;
import com.perl5.lang.perl.psi.light.PerlDelegatingNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public class PerlLightExceptionClassDefinition extends PerlDelegatingNamespaceDefinitionElement {
  public PerlLightExceptionClassDefinition(@NotNull PerlNamespaceDefinitionStub stub) {
    super(stub);
  }

  public PerlLightExceptionClassDefinition(@NotNull PerlPolyNamedElement delegate,
                                           @NotNull String name,
                                           @NotNull IStubElementType elementType,
                                           @NotNull PsiElement nameIdentifier,
                                           @NotNull PerlMroType mroType,
                                           @NotNull List<String> parentNamespacesNames,
                                           @Nullable PerlNamespaceAnnotations annotations,
                                           @NotNull List<String> export,
                                           @NotNull List<String> exportOk,
                                           @NotNull Map<String, List<String>> exportTags) {
    super(delegate, name, elementType, nameIdentifier, mroType, parentNamespacesNames, annotations, export, exportOk, exportTags);
  }

  @Nullable
  @Override
  public Icon getIcon(int flags) {
    return AllIcons.Nodes.ExceptionClass;
  }
}
