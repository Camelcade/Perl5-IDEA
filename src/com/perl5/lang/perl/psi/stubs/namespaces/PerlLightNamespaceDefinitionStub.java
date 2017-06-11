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

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class PerlLightNamespaceDefinitionStub extends PerlNamespaceDefinitionStub {
  @Nullable
  private final StubElement myParent;

  public PerlLightNamespaceDefinitionStub(StubElement parent,
                                          IStubElementType elementType,
                                          String packageName,
                                          PerlMroType mroType,
                                          List<String> parentNamespaces,
                                          List<String> EXPORT,
                                          List<String> EXPORT_OK,
                                          Map<String, List<String>> EXPORT_TAGS,
                                          PerlNamespaceAnnotations namespaceAnnotations
  ) {
    super(null, elementType, packageName, mroType, parentNamespaces, EXPORT, EXPORT_OK, EXPORT_TAGS, namespaceAnnotations);
    myParent = parent;
  }

  @Override
  public StubElement getParentStub() {
    return myParent;
  }

  @Nullable
  @Override
  public <E extends PsiElement> E getParentStubOfType(@NotNull Class<E> parentClass) {
    throw new IncorrectOperationException("NYI");
  }

  @Override
  public String toString() {
    return getPackageName();
  }
}
