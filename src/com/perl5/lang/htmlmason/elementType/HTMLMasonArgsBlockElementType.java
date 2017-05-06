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
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonArgsBlock;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonArgsBlockImpl;
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonArgsBlockStub;
import com.perl5.lang.htmlmason.parser.stubs.impl.HTMLMasonArgsBlockStubImpl;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 19.03.2016.
 */
public class HTMLMasonArgsBlockElementType extends IStubElementType<HTMLMasonArgsBlockStub, HTMLMasonArgsBlock>
  implements PsiElementProvider {
  public HTMLMasonArgsBlockElementType(@NotNull @NonNls String debugName) {
    super(debugName, HTMLMasonLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new HTMLMasonArgsBlockImpl(node);
  }


  @Override
  public HTMLMasonArgsBlock createPsi(@NotNull HTMLMasonArgsBlockStub stub) {
    return new HTMLMasonArgsBlockImpl(stub, this);
  }

  @Override
  public HTMLMasonArgsBlockStub createStub(@NotNull HTMLMasonArgsBlock psi, StubElement parentStub) {
    return new HTMLMasonArgsBlockStubImpl(parentStub, this, psi.getArgumentsList());
  }

  @NotNull
  @Override
  public String getExternalId() {
    return "HTML::Mason::" + super.toString();
  }

  @Override
  public void serialize(@NotNull HTMLMasonArgsBlockStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    PerlSubArgument.serializeList(dataStream, stub.getArgumentsList());
  }

  @NotNull
  @Override
  public HTMLMasonArgsBlockStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new HTMLMasonArgsBlockStubImpl(parentStub, this, PerlSubArgument.deserializeList(dataStream));
  }

  @Override
  public void indexStub(@NotNull HTMLMasonArgsBlockStub stub, @NotNull IndexSink sink) {

  }
}
