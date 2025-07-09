/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.pod.parser.psi.stubs;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.parser.psi.mixin.PodStubBasedSection;
import org.jetbrains.annotations.NotNull;

public class PodSectionStub extends StubBase<PodStubBasedSection> implements StubElement<PodStubBasedSection> {
  private final @NotNull String myContent;

  public PodSectionStub(StubElement parent, IElementType elementType, @NotNull String myContent) {
    super(parent, elementType);
    this.myContent = myContent;
  }

  public @NotNull String getContent() {
    return myContent;
  }

  @Override
  public String toString() {
    return super.toString() + "\n\tContent: " + myContent;
  }
}
