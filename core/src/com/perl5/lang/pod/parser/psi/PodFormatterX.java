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

package com.perl5.lang.pod.parser.psi;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface PodFormatterX extends PodStubBasedSection, PodFormatter {
  /**
   * @return a real target of this index. Outer item, heading or file.
   */
  @Nullable
  default PsiElement getIndexTarget() {
    PsiElement parent = getParent();
    if (parent instanceof PodSectionTitle) {
      return parent.getParent();
    }
    return parent;
  }

  /**
   * @return true iff this index makes sense.
   * @implNote for some reason perldoc may have duplicate indexes. E.g. <code>=item TEXT X<TEXT></code>. In such case there is no
   * sense to use index
   */
  default boolean isMeaningful() {
    PsiElement indexTarget = getIndexTarget();
    if (indexTarget == null) {
      Logger.getInstance(PodFormatterX.class).warn("Can't find a target element for " + getText());
    }
    if (indexTarget instanceof PodTitledSection) {
      return !StringUtil.equals(((PodTitledSection)indexTarget).getTitleText(), getTitleText());
    }
    return true;
  }
}
