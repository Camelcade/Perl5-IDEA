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

package com.perl5.lang.pod.idea.completion;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionType;
import com.perl5.lang.pod.parser.PodElementPatterns;

/**
 * Created by hurricup on 16.04.2016.
 */
public class PodCompletionContributor extends CompletionContributor implements PodElementPatterns {
  public PodCompletionContributor() {
    extend(
      CompletionType.BASIC,
      LINK_IDENTIFIER,
      new PodLinkCompletionProvider()
    );

    extend(
      CompletionType.BASIC,
      TITLE_IDENTIFIER,
      new PodTitleCompletionProvider()
    );
  }
}
