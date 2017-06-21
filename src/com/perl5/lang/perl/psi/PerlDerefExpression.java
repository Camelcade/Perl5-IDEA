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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 07.06.2015.
 */
public interface PerlDerefExpression extends PsiPerlExpr {
  /**
   * Attempting to traverse dereference chain in order to detect package name before method element
   *
   * @param methodElement - method, for which we are traversing
   * @return package name or null
   */
  @Nullable
  String getPreviousElementNamespace(PsiElement methodElement);

  /**
   * Attempting to traverse dereference chain in order to detect package name retuning by current element
   *
   * @param currentElement current element
   * @return type or null
   */
  @Nullable
  String getCurrentElementNamespace(PsiElement currentElement);

  /**
   * Attempting to guess dereference chain result
   *
   * @return type or null
   */
  @Nullable
  String guessType();
}
