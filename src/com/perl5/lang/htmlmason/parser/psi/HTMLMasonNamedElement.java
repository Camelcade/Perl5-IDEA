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

package com.perl5.lang.htmlmason.parser.psi;

import com.intellij.psi.PsiNameIdentifierOwner;

import java.util.regex.Pattern;

/**
 * Created by hurricup on 19.03.2016.
 */
public interface HTMLMasonNamedElement extends HTMLMasonCompositeElement, PsiNameIdentifierOwner {
  Pattern HTML_MASON_IDENTIFIER_PATTERN = Pattern.compile("[\\w._-]+");
}
