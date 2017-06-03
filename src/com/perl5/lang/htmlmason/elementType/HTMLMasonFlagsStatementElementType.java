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

package com.perl5.lang.htmlmason.elementType;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.*;
import com.intellij.util.io.StringRef;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonFlagsStatement;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFlagsStatementImpl;
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonFlagsStatementStub;
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonFlagsStubIndex;
import com.perl5.lang.htmlmason.parser.stubs.impl.HTMLMasonFlagsStatementStubImpl;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 10.03.2016.
 */
public class HTMLMasonFlagsStatementElementType extends IStubElementType<HTMLMasonFlagsStatementStub, HTMLMasonFlagsStatement>
  implements PsiElementProvider {
  public HTMLMasonFlagsStatementElementType(@NotNull @NonNls String debugName) {
    super(debugName, HTMLMasonLanguage.INSTANCE);
  }

  @Override
  public HTMLMasonFlagsStatement createPsi(@NotNull HTMLMasonFlagsStatementStub stub) {
    return new HTMLMasonFlagsStatementImpl(stub, this);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new HTMLMasonFlagsStatementImpl(node);
  }


  @Override
  public HTMLMasonFlagsStatementStub createStub(@NotNull HTMLMasonFlagsStatement psi, StubElement parentStub) {
    return new HTMLMasonFlagsStatementStubImpl(parentStub, this, psi.getParentComponentPath());
  }

  @NotNull
  @Override
  public String getExternalId() {
    return "HTML::Mason::" + super.toString();
  }

  @Override
  public void serialize(@NotNull HTMLMasonFlagsStatementStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    String parentComponentPath = stub.getParentComponentPath();

    //noinspection StringEquality
    if (parentComponentPath == HTMLMasonFlagsStatement.UNDEF_RESULT) {
      dataStream.writeBoolean(false);
      return;
    }
    dataStream.writeBoolean(true);
    dataStream.writeName(parentComponentPath);
  }

  @NotNull
  @Override
  public HTMLMasonFlagsStatementStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    if (!dataStream.readBoolean()) {
      return new HTMLMasonFlagsStatementStubImpl(parentStub, this, HTMLMasonFlagsStatement.UNDEF_RESULT);
    }
    StringRef nameRef = dataStream.readName();
    return new HTMLMasonFlagsStatementStubImpl(parentStub, this, nameRef == null ? null : nameRef.toString());
  }

  @Override
  public void indexStub(@NotNull HTMLMasonFlagsStatementStub stub, @NotNull IndexSink sink) {
    String parentComponentPath = stub.getParentComponentPath();

    //noinspection StringEquality
    if (parentComponentPath != null && parentComponentPath != HTMLMasonFlagsStatement.UNDEF_RESULT) {
      sink.occurrence(HTMLMasonFlagsStubIndex.KEY, parentComponentPath);
    }
  }
}
