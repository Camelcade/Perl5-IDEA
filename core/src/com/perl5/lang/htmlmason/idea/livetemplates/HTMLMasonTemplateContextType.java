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

package com.perl5.lang.htmlmason.idea.livetemplates;

import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.lang.htmlmason.HTMLMasonElementPatterns;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.perl.idea.livetemplates.PerlTemplateContextType;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 08.03.2016.
 */
public class HTMLMasonTemplateContextType extends TemplateContextType implements HTMLMasonElementPatterns {
  public HTMLMasonTemplateContextType() {
    super("PERL5_HTML_MASON", "&HTML::Mason template", PerlTemplateContextType.Generic.class);
  }

  @Override
  public boolean isInContext(@NotNull PsiFile file, int offset) {
    if (file instanceof HTMLMasonFileImpl && offset > 0) {
      PsiElement element = file.findElementAt(offset - 1);
      return HTML_MASON_TEMPLATE_CONTEXT_PATTERN.accepts(element) || HTML_MASON_TEMPLATE_CONTEXT_PATTERN_BROKEN.accepts(element);
    }
    return false;
  }
}
