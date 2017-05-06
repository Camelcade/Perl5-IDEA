/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.htmlmason.elementType;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonMethodDefinition;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonMethodDefinitionImpl;
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonMethodDefinitionStub;
import com.perl5.lang.htmlmason.parser.stubs.impl.HTMLMasonMethodDefinitionStubImpl;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 19.03.2016.
 */
public class HTMLMasonMethodElementType extends IStubElementType<HTMLMasonMethodDefinitionStub, HTMLMasonMethodDefinition>
  implements PsiElementProvider {
  public HTMLMasonMethodElementType(@NotNull @NonNls String debugName) {
    super(debugName, HTMLMasonLanguage.INSTANCE);
  }

  @Override
  public HTMLMasonMethodDefinition createPsi(@NotNull HTMLMasonMethodDefinitionStub stub) {
    return new HTMLMasonMethodDefinitionImpl(stub, this);
  }

  @Override
  public HTMLMasonMethodDefinitionStub createStub(@NotNull HTMLMasonMethodDefinition psi, StubElement parentStub) {
    return new HTMLMasonMethodDefinitionStubImpl(parentStub, this, psi.getName());
  }

  @NotNull
  @Override
  public String getExternalId() {
    return "HTML::Mason::" + super.toString();
  }

  @Override
  public void serialize(@NotNull HTMLMasonMethodDefinitionStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getName());
  }

  @NotNull
  @Override
  public HTMLMasonMethodDefinitionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new HTMLMasonMethodDefinitionStubImpl(parentStub, this, dataStream.readName().toString());
  }

  @Override
  public void indexStub(@NotNull HTMLMasonMethodDefinitionStub stub, @NotNull IndexSink sink) {

  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new HTMLMasonMethodDefinitionImpl(node);
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    PsiElement psi = node.getPsi();
    return psi instanceof HTMLMasonMethodDefinition && StringUtil.isNotEmpty(((HTMLMasonMethodDefinition)psi).getName());
  }
}
