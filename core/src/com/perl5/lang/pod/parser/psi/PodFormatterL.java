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

package com.perl5.lang.pod.parser.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.pod.psi.PsiLinkName;
import com.perl5.lang.pod.psi.PsiLinkSection;
import com.perl5.lang.pod.psi.PsiLinkText;
import com.perl5.lang.pod.psi.PsiLinkUrl;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 26.03.2016.
 */
public interface PodFormatterL extends PsiElement, PodFormatter, PodSection {
  @Nullable
  PodLinkDescriptor getLinkDescriptor();

  @Nullable
  PsiFile getTargetFile();

  @Nullable
  default PsiLinkText getLinkTextElement() {
    return PsiTreeUtil.getChildOfType(getContentBlock(), PsiLinkText.class);
  }

  @Nullable
  default PsiLinkName getLinkNameElement() {
    return PsiTreeUtil.getChildOfType(getContentBlock(), PsiLinkName.class);
  }

  @Nullable
  default PsiLinkSection getLinkSectionElement() {
    return PsiTreeUtil.getChildOfType(getContentBlock(), PsiLinkSection.class);
  }

  @Nullable
  default PsiLinkUrl getLinkUrlElement() {
    return PsiTreeUtil.getChildOfType(getContentBlock(), PsiLinkUrl.class);
  }
}
