/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.pod.elementTypes;

import com.intellij.lang.ASTFactory;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.tree.IReparseableLeafElementType;
import com.intellij.psi.tree.OuterLanguageElementType;
import com.perl5.lang.pod.PodLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PodOuterTokenType extends OuterLanguageElementType implements IReparseableLeafElementType<ASTNode> {
  private static final Logger LOG = Logger.getInstance(PodOuterTokenType.class);

  public PodOuterTokenType() {
    super("POD_OUTER", PodLanguage.INSTANCE);
  }

  private boolean isReparseable(@NotNull CharSequence newText) {
    int newTextLength = newText.length();
    if (newTextLength == 0) {
      LOG.debug("New text is empty");
      return false;
    }

    if (newTextLength > 1 && newText.charAt(0) == '=' && Character.isAlphabetic(newText.charAt(1))) {
      LOG.debug("Starts with POD opener");
      return false;
    }

    int offset = 0;
    while (true) {
      offset = StringUtil.indexOf(newText, "\n=", offset);
      if (offset < 0 || offset + 2 >= newTextLength) {
        LOG.debug("No POD starters found");
        return true;
      }
      offset += 2;
      if (Character.isAlphabetic(newText.charAt(offset))) {
        LOG.debug("Found POD starter at ", offset);
        return false;
      }
    }
  }

  @Override
  public @Nullable ASTNode reparseLeaf(@NotNull ASTNode leaf, @NotNull CharSequence newText) {
    return isReparseable(newText) ? ASTFactory.leaf(this, newText) : null;
  }

  @Override
  public String toString() {
    return "Perl5 POD: " + super.toString();
  }
}
