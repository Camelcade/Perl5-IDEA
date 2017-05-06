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

package com.perl5.lang.pod.parser.psi.mixin;

import com.intellij.lang.ASTNode;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.PodSectionH2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodSectionH2Mixin extends PodTitledSectionMixin implements PodSectionH2 {
  public PodSectionH2Mixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void renderElementTitleAsHTML(StringBuilder builder, PodRenderingContext context) {
    builder.append("<h2>");
    super.renderElementTitleAsHTML(builder, context);
    builder.append("</h2>");
  }

  @Override
  public boolean isHeading() {
    return true;
  }

  @Override
  public int getHeadingLevel() {
    return 2;
  }

  @Nullable
  @Override
  public String getUsageViewTypeLocation() {
    return "2nd Level Header";
  }
}
