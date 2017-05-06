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

package com.perl5.lang.htmlmason.idea.livetemplates;

import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.codeInsight.template.XmlContextType;
import com.intellij.lang.Language;
import com.intellij.lang.html.HTMLLanguage;
import com.intellij.lang.xhtml.XHTMLLanguage;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 08.03.2016.
 */
public abstract class AbstractMasonTemplateContextType extends TemplateContextType {
  public AbstractMasonTemplateContextType(@NotNull @NonNls String id,
                                          @NotNull String presentableName,
                                          @Nullable Class<? extends TemplateContextType> baseContextType) {
    super(id, presentableName, baseContextType);
  }

  protected abstract boolean isMyFile(PsiFile file);

  @Override
  public boolean isInContext(@NotNull PsiFile file, int offset) {
    return isMyLanguage(PsiUtilCore.getLanguageAtOffset(file, offset))
           && !XmlContextType.isEmbeddedContent(file, offset)
           && isMyFile(file);
  }

  static boolean isMyLanguage(Language language) {
    return language.isKindOf(HTMLLanguage.INSTANCE) || language.isKindOf(XHTMLLanguage.INSTANCE);
  }
}
