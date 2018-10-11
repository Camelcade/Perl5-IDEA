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

package com.perl5.lang.pod.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.parser.psi.PodStubBasedSection;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStubImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 26.03.2016.
 */
public abstract class PodStubBasedSectionElementType<T extends PodStubBasedSection> extends IStubElementType<PodSectionStub, T>
  implements PsiElementProvider {
  public PodStubBasedSectionElementType(@NotNull @NonNls String debugName) {
    super(debugName, PodLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public PodSectionStub createStub(@NotNull T psi, StubElement parentStub) {
    return new PodSectionStubImpl(parentStub, this, psi.getTitleText());
  }

  @NotNull
  @Override
  public String getExternalId() {
    return "PodSection " + super.toString();
  }

  @Override
  public void serialize(@NotNull PodSectionStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(stub.getTitleText());
  }

  @NotNull
  @Override
  public PodSectionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    return new PodSectionStubImpl(parentStub, this, PerlStubSerializationUtil.readString(dataStream));
  }

  @Override
  public void indexStub(@NotNull PodSectionStub stub, @NotNull IndexSink sink) {

  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    return false;
    //		PsiElement psi = node.getPsi();
    //		return (psi instanceof PodStubBasedSection) && StringUtil.isNotEmpty(((PodStubBasedSection) psi).getTitleText());
  }

  @Override
  public String toString() {
    return "PodElementType." + super.toString();
  }
}
