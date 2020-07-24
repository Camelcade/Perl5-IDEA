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
import com.perl5.lang.perl.psi.impl.PerlPodElement;
import org.jetbrains.annotations.NotNull;

import static com.perl5.lang.pod.parser.psi.PodSyntaxElements.CUT_COMMAND;

public class PerlPodTokenType extends PerlReparseableTokenType {
  public PerlPodTokenType() {
    super("POD", PerlPodElement.class);
  }

  @Override
  protected boolean isReparseable(@NotNull ASTNode leaf, @NotNull CharSequence newText) {
    if (newText.length() < 2) {
      return false;
    }

    if (newText.charAt(0) != '=' || !Character.isAlphabetic(newText.charAt(1)) || StringUtil.startsWith(newText, CUT_COMMAND)) {
      return false;
    }

    CharSequence oldText = leaf.getChars();
    int lastLineIndex = StringUtil.lastIndexOf(oldText, '\n', 0, oldText.length());
    boolean wasClosed = false;
    if (lastLineIndex >= 0) {
      CharSequence lastLineText = oldText.subSequence(lastLineIndex, oldText.length());
      wasClosed = StringUtil.startsWith(lastLineText, "\n" + CUT_COMMAND);
    }

    int firstCutIndex = StringUtil.indexOf(newText, "\n" + CUT_COMMAND);
    boolean isClosed = firstCutIndex >= 0;
    if (isClosed != wasClosed) {
      return false;
    }

    return !isClosed || StringUtil.indexOf(newText, '\n', firstCutIndex + 1) < 0;
  }
}
