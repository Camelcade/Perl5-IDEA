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

package com.perl5.lang.mason2.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.perl5.lang.mason2.Mason2Constants;
import com.perl5.lang.perl.extensions.PerlImplicitVariablesProvider;
import com.perl5.lang.perl.psi.PerlCompositeElement;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.properties.PerlIdentifierOwner;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;


public interface MasonNamespaceDefinition extends PsiElement,
                                                  Mason2Constants,
                                                  PerlImplicitVariablesProvider,
                                                  StubBasedPsiElement<PerlNamespaceDefinitionStub>,
                                                  PerlNamespaceDefinitionElement,
                                                  PerlNamespaceElementContainer,
                                                  PerlIdentifierOwner,
                                                  PerlCompositeElement {
}
