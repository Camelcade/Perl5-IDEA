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

package com.perl5.lang.htmlmason.idea.editor;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import org.jetbrains.annotations.NotNull;


public class HTMLMasonBraceMatcher implements PairedBraceMatcher, HTMLMasonElementTypes {
  private static final BracePair[] PAIRS = new BracePair[]{
    new BracePair(HTML_MASON_BLOCK_OPENER, HTML_MASON_BLOCK_CLOSER, false),
    new BracePair(HTML_MASON_CALL_OPENER, HTML_MASON_CALL_CLOSER, false),
    new BracePair(HTML_MASON_CALL_FILTERING_OPENER, HTML_MASON_CALL_CLOSE_TAG_START, false),
    new BracePair(HTML_MASON_METHOD_OPENER, HTML_MASON_METHOD_CLOSER, false),
    new BracePair(HTML_MASON_DEF_OPENER, HTML_MASON_DEF_CLOSER, false),
    new BracePair(HTML_MASON_FILTER_OPENER, HTML_MASON_FILTER_CLOSER, false),
    new BracePair(HTML_MASON_DOC_OPENER, HTML_MASON_DOC_CLOSER, false),
    new BracePair(HTML_MASON_ATTR_OPENER, HTML_MASON_ATTR_CLOSER, false),
    new BracePair(HTML_MASON_ARGS_OPENER, HTML_MASON_ARGS_CLOSER, false),
    new BracePair(HTML_MASON_TEXT_OPENER, HTML_MASON_TEXT_CLOSER, false),
    new BracePair(HTML_MASON_PERL_OPENER, HTML_MASON_PERL_CLOSER, false),
    new BracePair(HTML_MASON_INIT_OPENER, HTML_MASON_INIT_CLOSER, false),
    new BracePair(HTML_MASON_ONCE_OPENER, HTML_MASON_ONCE_CLOSER, false),
    new BracePair(HTML_MASON_SHARED_OPENER, HTML_MASON_SHARED_CLOSER, false),
    new BracePair(HTML_MASON_CLEANUP_OPENER, HTML_MASON_CLEANUP_CLOSER, false),
    new BracePair(HTML_MASON_FLAGS_OPENER, HTML_MASON_FLAGS_CLOSER, false),
  };

  @NotNull
  @Override
  public BracePair[] getPairs() {
    return PAIRS;
  }

  @Override
  public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, IElementType contextType) {
    return true;
  }

  @Override
  public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
    return openingBraceOffset;
  }
}
