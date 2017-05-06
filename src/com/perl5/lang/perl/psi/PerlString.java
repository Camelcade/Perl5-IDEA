/*
 * Copyright 2015 Alexandr Evstigneev
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

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 08.08.2015.
 */
public interface PerlString extends PsiElement {
  /**
   * Returns string content text
   *
   * @return String content text
   */
  @NotNull
  String getStringContent();

  /**
   * Changing string contents
   *
   * @param newContent new string content
   */
  void setStringContent(String newContent);

  /**
   * Returns text content range
   *
   * @return text content range
   */
  @NotNull
  TextRange getContentTextRangeInParent();

  /**
   * Returns the lenght of string content
   *
   * @return string content length
   */
  int getContentLength();
}
