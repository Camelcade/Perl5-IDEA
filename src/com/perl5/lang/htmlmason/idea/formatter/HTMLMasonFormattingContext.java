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

package com.perl5.lang.htmlmason.idea.formatter;

import com.intellij.formatting.SpacingBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.perl5.lang.perl.idea.formatter.PerlIndentProcessor;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.htmlmason.HTMLMasonElementPatterns.ATTR_OR_ARG_ELEMENT_PATTERN;
import static com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes.*;

public class HTMLMasonFormattingContext extends AbstractMasonFormattingContext {
  private static final TokenSet SIMPLE_OPENERS = TokenSet.create(
    HTML_MASON_BLOCK_OPENER,
    HTML_MASON_CALL_OPENER,
    HTML_MASON_CALL_FILTERING_OPENER
  );

  public HTMLMasonFormattingContext(@NotNull CodeStyleSettings settings) {
    super(settings);
  }

  @Override
  protected SpacingBuilder createSpacingBuilder() {
    return super.createSpacingBuilder()
      .after(SIMPLE_OPENERS).spaces(1)
      .before(HTML_MASON_BLOCK_CLOSER).spaces(1)
      .before(HTML_MASON_CALL_CLOSER).spaces(1)
      .before(HTML_MASON_CALL_CLOSER_UNMATCHED).spaces(1)
      .between(HTML_MASON_CALL_CLOSE_TAG_START, HTML_MASON_TAG_CLOSER).spaces(0)
      .after(HTML_MASON_CALL_CLOSE_TAG_START).spaces(1)
      .beforeInside(HTML_MASON_TAG_CLOSER, HTML_MASON_CALL_CLOSE_TAG).spaces(1)
      ;
  }

  @Override
  public PerlIndentProcessor getIndentProcessor() {
    return HTMLMasonIndentProcessor.INSTANCE;
  }

  @Override
  public boolean isNewLineForbiddenAt(@NotNull ASTNode node) {
    return super.isNewLineForbiddenAt(node) || ATTR_OR_ARG_ELEMENT_PATTERN.accepts(node.getPsi());
  }

  @Override
  protected IElementType getLineOpenerToken() {
    return HTML_MASON_LINE_OPENER;
  }
}
