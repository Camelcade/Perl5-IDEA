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

package com.perl5.lang.perl.parser.Exception.Class.psi.elementTypes;

import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.parser.Exception.Class.psi.light.PerlLightExceptionClassDefinition;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlLightNamespaceDefinitionElementType;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import org.jetbrains.annotations.NotNull;

public class PerlLightExceptionClassElementType extends PerlLightNamespaceDefinitionElementType {
  public static final IStubElementType LIGHT_EXCEPTION_CLASS = new PerlLightExceptionClassElementType("LIGHT_EXCEPTION_CLASS");

  private PerlLightExceptionClassElementType(String name) {
    super(name);
  }

  @Override
  public PerlNamespaceDefinitionElement createPsi(@NotNull PerlNamespaceDefinitionStub stub) {
    return new PerlLightExceptionClassDefinition(stub);
  }
}
