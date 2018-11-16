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

package com.perl5.lang.perl.idea.structureView.elements;

import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 15.08.2015.
 */
public class PerlLeafStructureViewElement extends PerlStructureViewElement {
  public PerlLeafStructureViewElement(PsiElement element) {
    super(element);
  }

  @NotNull
  @Override
  public TreeElement[] getChildren() {
    return new TreeElement[0];
  }
}
