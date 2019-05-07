/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.pod.parser.psi.mixin;

import com.intellij.lang.ASTNode;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PodOverSectionContent extends PodCompositeElementMixin {
  public PodOverSectionContent(@NotNull ASTNode node) {
    super(node);
  }

  /**
   * Checks if first element is bulleted, according to http://perldoc.perl.org/perlpodspec.html#About-%3dover...%3dback-Regions
   *
   * @return true if list should be bulleted
   */
  public boolean isBulleted() {
    PodSectionItem firstItem = getFirstItem();
    return firstItem != null && firstItem.isBulleted();
  }

  /**
   * Returns first list item
   *
   * @return item or null if not any
   */
  @Nullable
  public PodSectionItem getFirstItem() {
    return findChildByClass(PodSectionItem.class);
  }

  @Override
  public void renderElementAsHTML(StringBuilder builder, PodRenderingContext context) {
    boolean isBulleted = isBulleted();

    if (isBulleted) {
      builder.append("<ul>");
    }
    else {
      builder.append("<dl>");
    }

    super.renderElementAsHTML(builder, context);

    if (isBulleted) {
      builder.append("</ul>");
    }
    else {
      builder.append("</dl>");
    }
  }

  @Override
  public int getListLevel() {
    return super.getListLevel() + 1;
  }
}
