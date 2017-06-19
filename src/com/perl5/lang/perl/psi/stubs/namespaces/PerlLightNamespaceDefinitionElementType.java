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
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.mro.PerlMroType;
import com.perl5.lang.perl.psi.utils.PerlNamespaceAnnotations;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class PerlLightNamespaceDefinitionElementType extends PerlNamespaceDefinitionElementType {
  public PerlLightNamespaceDefinitionElementType(String name) {
    super(name);
  }

  @Override
  public final PerlNamespaceDefinitionElement createPsi(@NotNull PerlNamespaceDefinitionStub stub) {
    throw new IncorrectOperationException("Light elements should be created by wrappers, not element types");
  }

  @Override
  protected StubIndexKey<String, ? extends PsiElement> getDirectKey() {
    return PerlLightNamespaceIndex.KEY;
  }

  @Override
  protected StubIndexKey<String, ? extends PsiElement> getReverseKey() {
    return PerlLightNamespaceReverseIndex.KEY;
  }

  @Override
  protected PerlNamespaceDefinitionStub createStubElement(@Nullable StubElement parentStub,
                                                          @NotNull String packageName,
                                                          @NotNull PerlMroType mroType,
                                                          @NotNull List<String> parentNamespaceNames,
                                                          @NotNull List<String> export,
                                                          @NotNull List<String> exportOk,
                                                          @NotNull Map<String, List<String>> exportTags,
                                                          @Nullable PerlNamespaceAnnotations annotations) {
    return new PerlLightNamespaceDefinitionStub(parentStub,
                                                this,
                                                packageName,
                                                mroType,
                                                parentNamespaceNames,
                                                export,
                                                exportOk,
                                                exportTags,
                                                annotations
    );
  }
}
