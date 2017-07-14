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

import com.intellij.ide.util.treeView.smartTree.ActionPresentation;
import com.intellij.ide.util.treeView.smartTree.ActionPresentationData;
import com.perl5.PerlIcons;
import com.perl5.lang.pod.idea.structureView.PodStructureViewElement;
import org.jetbrains.annotations.NotNull;

public class PerlPodFilter extends PerlFilter {
  public static final PerlPodFilter INSTANCE = new PerlPodFilter();
  private static final String ID = "SHOW_POD";

  @Override
  protected boolean isMyElement(@NotNull PodStructureViewElement treeElement) {
    return true;
  }

  @NotNull
  @Override
  public ActionPresentation getPresentation() {
    return new ActionPresentationData("Show documentation", null, PerlIcons.POD_FILE);
  }

  @NotNull
  @Override
  public String getName() {
    return ID;
  }
}
