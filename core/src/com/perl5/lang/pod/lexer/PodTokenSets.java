/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.pod.lexer;

import com.intellij.psi.tree.TokenSet;

import static com.perl5.lang.pod.lexer.PodElementTypesGenerated.*;


public final class PodTokenSets {
  public static final TokenSet POD_TAGS_TOKENSET = TokenSet.create(
    POD_POD, POD_HEAD1, POD_HEAD2, POD_HEAD3, POD_HEAD4, POD_OVER, POD_ITEM, POD_BACK, POD_BEGIN, POD_END, POD_FOR, POD_ENCODING, POD_CUT,
    POD_UNKNOWN
  );

  public static final TokenSet POD_FORMATTERS_TOKENSET = TokenSet.create(
    POD_I, POD_B, POD_C, POD_L, POD_E, POD_F, POD_S, POD_X, POD_Z
  );

  private PodTokenSets() {
  }
}
