/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.moose;

import org.jetbrains.annotations.NonNls;

public final class MooseSyntax {
  @NonNls public static final String MOOSE_KEYWORD_AFTER = "after";
  @NonNls public static final String MOOSE_KEYWORD_AROUND = "around";
  @NonNls public static final String MOOSE_KEYWORD_AUGMENT = "augment";
  @NonNls public static final String MOOSE_KEYWORD_BEFORE = "before";
  @NonNls public static final String MOOSE_KEYWORD_EXTENDS = "extends";
  @NonNls public static final String MOOSE_KEYWORD_HAS = "has";
  @NonNls public static final String MOOSE_KEYWORD_META = "meta";
  @NonNls public static final String MOOSE_KEYWORD_OVERRIDE = "override";
  @NonNls public static final String MOOSE_KEYWORD_REQUIRES = "requires";
  @NonNls public static final String MOOSE_KEYWORD_WITH = "with";

  private MooseSyntax() {
  }
}
