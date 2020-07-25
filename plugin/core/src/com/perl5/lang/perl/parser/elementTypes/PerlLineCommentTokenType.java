/*
 * Copyright 2015-2020 Alexandr Evstigneev
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
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import org.jetbrains.annotations.NotNull;

public class PerlLineCommentTokenType extends PerlReparseableTokenType {
  public PerlLineCommentTokenType(@NotNull String debugName) {
    super(debugName, PsiCommentImpl.class);
  }

  @Override
  protected boolean isReparseable(@NotNull ASTNode leaf, @NotNull CharSequence newText) {
    int length = newText.length();
    if (length < 1 || newText.charAt(0) != '#') {
      return false;
    }
    if (length > 1 && newText.charAt(1) == '@') {
      return false;
    }

    return !StringUtil.containsLineBreak(newText);
  }
}
