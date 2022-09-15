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

package com.perl5.lang.htmlmason.elementType;

import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.perl.parser.elementTypes.PerlReparseableTemplateTokenTypeBase;

public class HTMLMasonTemplateTokenType extends PerlReparseableTemplateTokenTypeBase {
  private static final String TOKEN_NAME = "HTML_MASON_TEMPLATE_BLOCK_HTML";

  public HTMLMasonTemplateTokenType() {
    super(TOKEN_NAME, PsiCommentImpl.class, HTMLMasonLanguage.INSTANCE);
  }

  @Override
  public String toString() {
    return "HTML::Mason: " + TOKEN_NAME;
  }
}
