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

package com.perl5.lang.tt2.idea.highlighting;

import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.tt2.TemplateToolkitFileViewProvider;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;

/**
 * Created by hurricup on 15.06.2016.
 */
public class TemplateToolkitTemplateLanguageErrorFilter extends SmartTemplateLanguageErrorFilter implements TemplateToolkitElementTypes {
  private static final TokenSet START_TOKENS = TokenSet.create(
    TT2_OPEN_TAG,
    TT2_OUTLINE_TAG
  );

  public TemplateToolkitTemplateLanguageErrorFilter() {
    super(START_TOKENS, TemplateToolkitFileViewProvider.class);
  }
}
