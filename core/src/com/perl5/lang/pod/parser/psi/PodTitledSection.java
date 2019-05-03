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

import com.intellij.navigation.NavigationItem;
import com.intellij.pom.PomTarget;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.perl5.lang.pod.psi.PsiPodFormatIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PodTitledSection extends PodSection, PodLinkTarget, PodStructureElement, PsiNameIdentifierOwner, PomTarget,
                                          NavigationItem {
  /**
   * @return text representation of section
   */
  @Nullable
  String getTitleText();

  /**
   * @return a text of the section title with all formatting codes but indexes, or null if there is no title section
   */
  @Nullable
  default String getTitleTextWithoutIndexes() {
    PsiElement titleElement = getTitleElement();
    if (titleElement == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    titleElement.accept(new PodRecursiveVisitor() {
      @Override
      public void visitElement(@NotNull PsiElement element) {
        if (element.getChildren().length > 0) {
          super.visitElement(element);
        }
        else {
          sb.append(element.getNode().getChars());
        }
      }

      @Override
      public void visitPodFormatIndex(@NotNull PsiPodFormatIndex o) {
      }
    });
    return sb.toString().trim();
  }

  /**
   * @return an element containing title of this section, probably with formatting codes
   */
  @Nullable
  PsiElement getTitleElement();
}
