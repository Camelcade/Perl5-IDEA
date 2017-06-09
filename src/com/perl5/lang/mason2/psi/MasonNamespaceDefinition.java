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

package com.perl5.lang.mason2.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.perl5.lang.mason2.Mason2Constants;
import com.perl5.lang.perl.extensions.PerlImplicitVariablesProvider;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.psi.PerlCompositeElement;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.properties.PerlIdentifierOwner;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 05.01.2016.
 */
public interface MasonNamespaceDefinition extends PsiElement,
                                                  Mason2Constants,
                                                  PerlImplicitVariablesProvider,
                                                  StubBasedPsiElement<PerlNamespaceDefinitionStub>,
                                                  PerlNamespaceDefinitionElement,
                                                  PerlNamespaceElementContainer,
                                                  PerlIdentifierOwner,
                                                  PerlElementPatterns,
                                                  PerlCompositeElement {
  /**
   * Returns file path relative to project root
   *
   * @return path, relative to root, null if it's LightVirtualFile without original
   */
  @Nullable
  String getAbsoluteComponentPath();

  /**
   * Returns file path relative to one of the component roots or project root if not under component root
   *
   * @return path, relative to root, null if it's LightVirtualFile without original
   */
  @Nullable
  String getComponentPath();
}
