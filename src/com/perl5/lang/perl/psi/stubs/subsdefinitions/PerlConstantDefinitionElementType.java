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

package com.perl5.lang.perl.psi.stubs.subsdefinitions;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.impl.PsiPerlConstantDefinitionImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 14.06.2016.
 */
public class PerlConstantDefinitionElementType extends PerlSubDefinitionElementType implements PsiElementProvider {
  public PerlConstantDefinitionElementType(String name) {
    super(name);
  }

  @Override
  public PerlSubDefinitionElement createPsi(@NotNull PerlSubDefinitionStub stub) {
    return new PsiPerlConstantDefinitionImpl(stub, this);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PsiPerlConstantDefinitionImpl(node);
  }
}


