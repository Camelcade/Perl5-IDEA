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

package com.perl5.lang.perl.idea.structureView.filters;

import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.perl5.lang.perl.idea.structureView.elements.PerlNamespaceStructureViewElement;
import com.perl5.lang.perl.idea.structureView.elements.PerlStructureViewElement;
import org.jetbrains.annotations.NotNull;

public abstract class PerlFilter implements Filter {

  /**
   * Due to the nature of api, these filters works in reverted manner;
   */
  @Override
  public final boolean isVisible(TreeElement treeNode) {
    return treeNode instanceof PerlNamespaceStructureViewElement ||
           treeNode instanceof PerlStructureViewElement && !isMyElement((PerlStructureViewElement)treeNode);
  }

  protected abstract boolean isMyElement(@NotNull PerlStructureViewElement treeElement);

  /**
   * Works when turned off
   */
  @Override
  public final boolean isReverted() {
    return true;
  }
}
