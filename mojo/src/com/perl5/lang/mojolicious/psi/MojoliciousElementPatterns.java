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

package com.perl5.lang.mojolicious.psi;

import com.intellij.patterns.PsiElementPattern;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.psi.PerlSubNameElement;

/**
 * Created by hurricup on 23.04.2016.
 */
public interface MojoliciousElementPatterns extends MojoliciousElementTypes, PerlElementPatterns {
  PsiElementPattern.Capture<PerlSubNameElement> MOJO_HELPER_USAGE =
    SUB_NAME_PATTERN.and(IN_STATIC_METHOD_PATTERN).and(com.perl5.lang.mojolicious.MojoliciousElementPatterns.IN_MOJOLICIOUS_FILE);
}
