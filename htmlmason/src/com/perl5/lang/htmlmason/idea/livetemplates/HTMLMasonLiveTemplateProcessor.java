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

package com.perl5.lang.htmlmason.idea.livetemplates;

import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class HTMLMasonLiveTemplateProcessor extends AbstractMasonLiveTemplateProcessor {
  @Override
  protected boolean isMyFile(PsiFile file) {
    return file instanceof HTMLMasonFileImpl;
  }

  @Override
  @NotNull
  protected Language getMyLanguage() {
    return HTMLMasonLanguage.INSTANCE;
  }

  @Nullable
  @Override
  protected PsiElement getOutlineElement(PsiElement firstElement) {
    return PsiUtilCore.getElementType(firstElement) == HTMLMasonElementTypes.HTML_MASON_LINE_OPENER ? firstElement : null;
  }
}
