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

package com.perl5.lang.tt2.idea.editor;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.perl5.lang.tt2.elementTypes.TemplateToolkitElementTypes;
import com.perl5.lang.tt2.parser.TemplateToolkitParserUtil;

/**
 * Created by hurricup on 12.06.2016.
 */
public class TemplateToolkitQuoteHandler extends SimpleTokenSetQuoteHandler implements TemplateToolkitElementTypes {
  public TemplateToolkitQuoteHandler() {
    super(TemplateToolkitParserUtil.OPEN_QUOTES);
  }

  @Override
  public boolean isClosingQuote(HighlighterIterator iterator, int offset) {
    return TemplateToolkitParserUtil.CLOSE_QUOTES.contains(iterator.getTokenType());
  }

  @Override
  public boolean isOpeningQuote(HighlighterIterator iterator, int offset) {
    return TemplateToolkitParserUtil.OPEN_QUOTES.contains(iterator.getTokenType());
  }
}
