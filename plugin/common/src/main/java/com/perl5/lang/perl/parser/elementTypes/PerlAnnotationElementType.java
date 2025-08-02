/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


public class PerlAnnotationElementType extends PerlReparseableElementType {
  public PerlAnnotationElementType(@NotNull @NonNls String debugName) {
    super(debugName);
  }

  @Override
  protected boolean isReparseableOld(@NotNull ASTNode parent,
                                     @NotNull CharSequence newText,
                                     @NotNull Language fileLanguage,
                                     @NotNull Project project) {
    if (newText.length() < 3 || StringUtil.containsLineBreak(newText)) {
      return false;
    }
    return newText.charAt(0) == '#' && newText.charAt(1) == '@' && Character.isAlphabetic(newText.charAt(2));
  }
}
