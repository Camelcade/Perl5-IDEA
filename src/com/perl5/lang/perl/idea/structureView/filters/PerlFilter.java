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
import com.perl5.lang.perl.idea.structureView.elements.PerlSubStructureViewElement;
import com.perl5.lang.pod.idea.structureView.PodStructureViewElement;
import org.jetbrains.annotations.NotNull;

public abstract class PerlFilter implements Filter {

  /**
   * Due to the nature of api, these filters works in reverted manner;
   */
  @Override
  public final boolean isVisible(TreeElement treeNode) {
    if (treeNode instanceof PerlNamespaceStructureViewElement) {
      return true;
    }
    else if (treeNode instanceof PerlSubStructureViewElement) {
      return !isMyElement((PerlSubStructureViewElement)treeNode);
    }
    else if (treeNode instanceof PerlStructureViewElement) {
      return !isMyElement((PerlStructureViewElement)treeNode);
    }
    else if (treeNode instanceof PodStructureViewElement) {
      return !isMyElement((PodStructureViewElement)treeNode);
    }
    return true;
  }

  protected boolean isMyElement(@NotNull PerlSubStructureViewElement treeElement) {
    return isMyElement((PerlStructureViewElement)treeElement);
  }

  protected boolean isMyElement(@NotNull PerlStructureViewElement treeElement) {
    return false;
  }

  protected boolean isMyElement(@NotNull PodStructureViewElement treeElement) {
    return false;
  }

  @Override
  public final boolean isReverted() {
    return true;
  }
}
