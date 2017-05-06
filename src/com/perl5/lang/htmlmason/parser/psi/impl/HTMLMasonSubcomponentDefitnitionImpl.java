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

package com.perl5.lang.htmlmason.parser.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.htmlmason.HTMLMasonIcons;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonSubcomponentDefitnition;
import com.perl5.lang.htmlmason.parser.stubs.HTMLMasonSubcomponentDefinitionStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 09.03.2016.
 */
public class HTMLMasonSubcomponentDefitnitionImpl extends HTMLMasonStubBasedNamedElementImpl<HTMLMasonSubcomponentDefinitionStub>
  implements HTMLMasonSubcomponentDefitnition {
  public HTMLMasonSubcomponentDefitnitionImpl(@NotNull HTMLMasonSubcomponentDefinitionStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public HTMLMasonSubcomponentDefitnitionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Nullable
  @Override
  protected String getNameFromStub() {
    HTMLMasonSubcomponentDefinitionStub stub = getStub();
    return stub == null ? null : stub.getName();
  }

  @NotNull
  @Override
  public SearchScope getUseScope() {
    return new LocalSearchScope(getContainingFile());
  }

  @Nullable
  @Override
  public Icon getIcon(int flags) {
    return HTMLMasonIcons.HTML_MASON_SUBCOMPONENT_ICON;
  }
}
