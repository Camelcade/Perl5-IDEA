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

package com.perl5.lang.perl.psi.properties;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * This interface allows elements to provide a POD anchor. Usually we are looking for previous POD block before a declaration.
 * And it works fine with explicit declarations. E.g.<br/><br/>
 * <pre>
 *   =pod somesub
 *
 *   Dsecription
 *
 *   =cut
 *
 *   sub somesub{}
 * </pre>
 * <p>
 * But for light elements, we need to find a wrapping statement or expression, e.g: <br/><br/>
 * <pre>
 *   =pod someattr
 *
 *   Dsecription
 *
 *   =cut
 *
 *   has someattr => sub{}
 * </pre>
 * <p>
 * In the latter case, expression starts with {@code has} is real pod anchor, but declaration starts from {@code someattr}.
 */
public interface PerlPodAwareElement {
  @NotNull
  PsiElement getPodAnchor();
}
