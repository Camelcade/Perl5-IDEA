/*
 * Copyright 2015-2020 Alexandr Evstigneev
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
import com.intellij.util.ObjectUtils;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PodOverSectionContent extends PodCompositeElementMixin {
  public PodOverSectionContent(@NotNull ASTNode node) {
    super(node);
  }

  /**
   * @return true iff first child is an item and it's bulleted
   * @see <a href="http://perldoc.perl.org/perlpodspec.html#About-%3dover...%3dback-Regions">http://perldoc.perl.org/perlpodspec.html#About-%3dover...%3dback-Regions</a>
   */
  public boolean isBulleted() {
    PodSectionItem firstItem = getFirstItem();
    return firstItem != null && firstItem.isBulleted();
  }

  /**
   * @return true iff first child is an item and it's numbered
   * @see <a href="http://perldoc.perl.org/perlpodspec.html#About-%3dover...%3dback-Regions">http://perldoc.perl.org/perlpodspec.html#About-%3dover...%3dback-Regions</a>
   */
  public boolean isNumbered() {
    PodSectionItem firstItem = getFirstItem();
    return firstItem != null && firstItem.isNumbered();
  }

  /**
   * Returns first list item
   *
   * @return item or null if not any
   */
  public @Nullable PodSectionItem getFirstItem() {
    return ObjectUtils.tryCast(getFirstChild(), PodSectionItem.class);
  }

  @Override
  public void renderElementAsHTML(StringBuilder builder, PodRenderingContext context) {
    boolean isBulleted = isBulleted();

    String closeTag;
    if (isBulleted) {
      builder.append("<ul>");
      closeTag = "</ul>";
    }
    else if (isNumbered()) {
      builder.append("<ol>");
      closeTag = "</ol>";
    }
    else {
      builder.append("<dl>");
      closeTag = "</dl>";
    }

    super.renderElementAsHTML(builder, context);

    builder.append(closeTag);
  }

  @Override
  public int getListLevel() {
    return super.getListLevel() + 1;
  }
}
