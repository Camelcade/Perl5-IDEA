/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.pod.parser.psi.mixin.PodStubBasedTitledSection;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class PodStubBasedTitledSectionElementType<Psi extends PodStubBasedTitledSection>
  extends PodStubBasedSectionElementType<Psi> {
  public PodStubBasedTitledSectionElementType(@NotNull String debugName) {
    super(debugName);
  }

  @NotNull
  @Override
  public PodSectionStub createStub(@NotNull PodStubBasedTitledSection psi, StubElement parentStub) {
    return new PodSectionStub(parentStub, this, Objects.requireNonNull(psi.getTitleText()));
  }

  @Override
  protected boolean shouldCreateStub(@NotNull Psi psi) {
    return StringUtil.isNotEmpty(psi.getTitleText());
  }
}
