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

package com.perl5.lang.pod.parser.psi.stubs;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.pod.parser.psi.PodTitledSection;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodSectionStubImpl extends StubBase<PodTitledSection> implements PodSectionStub {
  private String myContent;

  public PodSectionStubImpl(StubElement parent, IStubElementType elementType, String myContent) {
    super(parent, elementType);
    this.myContent = myContent;
  }

  public String getTitleText() {
    return myContent;
  }
}
