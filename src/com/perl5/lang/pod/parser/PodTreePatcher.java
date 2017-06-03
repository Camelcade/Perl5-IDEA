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

package com.perl5.lang.pod.parser;

import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.templateLanguages.OuterLanguageElement;
import com.intellij.psi.templateLanguages.SimpleTreePatcher;
import com.perl5.lang.pod.lexer.PodElementTypes;

/**
 * Created by hurricup on 30.03.2016.
 */
public class PodTreePatcher extends SimpleTreePatcher implements PodElementTypes {
  @Override
  public void insert(CompositeElement parent, TreeElement anchorBefore, OuterLanguageElement toInsert) {
    while (anchorBefore.getTreePrev() == null && !(parent instanceof FileElement)) {
      anchorBefore = anchorBefore.getTreeParent();
      parent = anchorBefore.getTreeParent();
      assert parent != null;
    }

    super.insert(parent, anchorBefore, toInsert);
  }
}
