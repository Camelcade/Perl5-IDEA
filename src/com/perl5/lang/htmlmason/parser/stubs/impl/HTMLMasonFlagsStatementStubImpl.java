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

package com.perl5.lang.htmlmason.parser.stubs.impl;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonFlagsStatement;
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonFlagsStatementStub;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 10.03.2016.
 */
public class HTMLMasonFlagsStatementStubImpl extends StubBase<HTMLMasonFlagsStatement> implements HTMLMasonFlagsStatementStub {
  final String myParentComponentPath;

  public HTMLMasonFlagsStatementStubImpl(StubElement parent, IStubElementType elementType, @Nullable String parentComponentPath) {
    super(parent, elementType);
    myParentComponentPath = parentComponentPath;
  }

  @Nullable
  @Override
  public String getParentComponentPath() {
    return myParentComponentPath;
  }
}
