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

package com.perl5.lang.perl.idea.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.idea.completion.providers.*;
import com.perl5.lang.perl.lexer.PerlElementTypes;

/**
 * Created by hurricup on 25.04.2015.
 */
public class PerlCompletionContributor extends CompletionContributor implements PerlElementTypes, PerlElementPatterns {
  public PerlCompletionContributor() {
    extend(
      CompletionType.BASIC,
      STRING_CONTENT_PATTERN,
      new PerlStringContentCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      LABEL_PATTERN,
      new PerlLabelCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      NAMESPACE_NAME_PATTERN,
      new PerlPackageCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      SUB_NAME_PATTERN,
      new PerlSubNameElementCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      VARIABLE_NAME_PATTERN,
      new PerlVariableNameCompletionProvider()
    );

    // refactored
    extend(
      CompletionType.BASIC,
      SUB_NAME_PATTERN.and(IN_STATIC_METHOD_PATTERN),
      new PerlSubBuiltInCompletionProvider()
    );

    // refactored
    extend(
      CompletionType.BASIC,
      SUB_NAME_PATTERN.and(IN_STATIC_METHOD_PATTERN),
      new PerlSubCallCompletionProvider()
    );

    // refactored
    extend(
      CompletionType.BASIC,
      SUB_NAME_PATTERN.and(IN_OBJECT_METHOD_PATTERN),
      new PerlSubCallCompletionProvider()
    );

    // refactored, adds packages when it's appropriate
    extend(
      CompletionType.BASIC,
      SUB_NAME_PATTERN.inside(METHOD_PATTERN),
      new PerlPackageSubCompletionProvider()
    );

    // refactored
    extend(
      CompletionType.BASIC,
      UNKNOWN_ANNOTATION_PATTERN,
      new PerlAnnotationCompletionProvider()
    );
  }
}
