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

package com.perl5.lang.perl.parser.constant.psi.elementTypes;

import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.parser.constant.psi.light.PerlLightConstantDefinitionElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlLightSubDefinitionElementType;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import org.jetbrains.annotations.NotNull;

public class PerlLightConstantDefinitionElementType extends PerlLightSubDefinitionElementType {
  public static final IStubElementType LIGHT_CONSTANT_DEFINITION = new PerlLightConstantDefinitionElementType("LIGHT_CONSTANT_DEFINITION");


  private PerlLightConstantDefinitionElementType(String name) {
    super(name);
  }

  @Override
  public PerlSubDefinitionElement createPsi(@NotNull PerlSubDefinitionStub stub) {
    return new PerlLightConstantDefinitionElement(stub);
  }
}
