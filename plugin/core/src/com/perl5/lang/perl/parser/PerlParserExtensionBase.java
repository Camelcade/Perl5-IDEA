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

package com.perl5.lang.perl.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.SUB_NAME;

public abstract class PerlParserExtensionBase extends PerlParserExtension {
  /**
   * Parses nested-call like wrappers:   <code>custom_token()</code>, converting custom token to sub and making custom nested
   * call wrapper
   *
   * @param nestedCallType wrapper element type
   * @return parsing result
   */
  protected boolean parseCustomNestedCall(@NotNull PerlBuilder builder, int level, @NotNull IElementType nestedCallType) {
    PsiBuilder.Marker wrapperMarker = builder.mark();
    if (PerlParserImpl.nested_call_inner(
      builder, level,
      (b, l) -> {
        PsiBuilder.Marker m = b.mark();
        b.advanceLexer();
        m.collapse(SUB_NAME);
        return true;
      },
      PerlParserImpl.optional_expression_parser_
    )) {
      wrapperMarker.done(nestedCallType);
      return true;
    }
    wrapperMarker.rollbackTo();
    return false;
  }
}
