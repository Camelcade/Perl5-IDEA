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

package com.perl5.lang.perl.parser.moose.stubs;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseAttribute;
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseAttributeImpl;
import com.perl5.lang.perl.psi.PerlSubDefinition;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStubElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 29.11.2015.
 */
public class PerlMooseAttributeStubElementType extends PerlSubDefinitionStubElementType implements PerlElementTypes, PsiElementProvider {
  public PerlMooseAttributeStubElementType(String name) {
    super(name);
  }

  @Override
  public PerlSubDefinition createPsi(@NotNull PerlSubDefinitionStub stub) {
    return new PerlMooseAttributeImpl(stub, this);
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    PsiElement psi = node.getPsi();
    return psi instanceof PerlMooseAttribute && StringUtil.isNotEmpty(((PerlMooseAttribute)psi).getSubName());
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PerlMooseAttributeImpl(node);
  }
}
