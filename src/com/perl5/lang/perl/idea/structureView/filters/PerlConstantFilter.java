/*
 * Copyright 2015 Alexandr Evstigneev
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

import com.intellij.ide.util.treeView.smartTree.ActionPresentation;
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.structureView.elements.PerlConstantStructureViewElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 15.08.2015.
 */
public class PerlConstantFilter implements Filter {
  public static final PerlConstantFilter INSTANCE = new PerlConstantFilter();
  private static final String ID = "SHOW_CONSTANTS";

  @Override
  public boolean isVisible(TreeElement treeElement) {
    return !(treeElement instanceof PerlConstantStructureViewElement);
  }

  @Override
  public boolean isReverted() {
    return true;
  }

  @NotNull
  @Override
  public ActionPresentation getPresentation() {
    return new ActionPresentationData("Show constants", null, PerlIcons.CONSTANT_GUTTER_ICON);
  }

  @NotNull
  @Override
  public String getName() {
    return ID;
  }
}
