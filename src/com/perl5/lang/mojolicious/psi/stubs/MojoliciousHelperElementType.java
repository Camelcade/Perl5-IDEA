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

package com.perl5.lang.mojolicious.psi.stubs;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.mojolicious.psi.MojoliciousHelperDeclaration;
import com.perl5.lang.mojolicious.psi.impl.MojoliciousHelperDeclarationImpl;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.PerlSubDefinition;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionStubElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 23.04.2016.
 */
public class MojoliciousHelperElementType extends PerlSubDefinitionStubElementType implements PsiElementProvider {
  public MojoliciousHelperElementType(String name) {
    super(name);
  }

  @Override
  public PerlSubDefinition createPsi(@NotNull PerlSubDefinitionStub stub) {
    return new MojoliciousHelperDeclarationImpl(stub, this);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new MojoliciousHelperDeclarationImpl(node);
  }

  @Override
  public PerlSubDefinitionStub createStub(@NotNull PerlSubDefinition psi, StubElement parentStub) {
    //noinspection unchecked
    return new MojoliciousHelperDeclarationStubImpl(
      parentStub,
      psi.getPackageName(),
      psi.getSubName(),
      psi.getSubArgumentsList(),
      psi.getAnnotations(),
      this
    );
  }

  @Override
  public void indexStub(@NotNull PerlSubDefinitionStub stub, @NotNull IndexSink sink) {
    super.indexStub(stub, sink);
    sink.occurrence(MojoliciousHelpersStubIndex.KEY, stub.getSubName());
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    PsiElement psi = node.getPsi();
    return psi instanceof MojoliciousHelperDeclaration && StringUtil.isNotEmpty(((MojoliciousHelperDeclaration)psi).getName());
  }
}
