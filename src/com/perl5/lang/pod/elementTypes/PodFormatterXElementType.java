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
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IndexSink;
import com.perl5.lang.pod.parser.psi.PodFormatterX;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;
import com.perl5.lang.pod.parser.psi.stubs.PodStubIndex;
import com.perl5.lang.pod.psi.impl.PsiPodFormatIndexImpl;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodFormatterXElementType extends PodStubBasedSectionElementType<PodFormatterX> {
  public PodFormatterXElementType(@NotNull @NonNls String debugName) {
    super(debugName);
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PsiPodFormatIndexImpl(node);
  }

  @Override
  public PodFormatterX createPsi(@NotNull PodSectionStub stub) {
    return new PsiPodFormatIndexImpl(stub, this);
  }

  @Override
  public void indexStub(@NotNull PodSectionStub stub, @NotNull IndexSink sink) {
    sink.occurrence(PodStubIndex.KEY, stub.getTitleText());
  }
}
