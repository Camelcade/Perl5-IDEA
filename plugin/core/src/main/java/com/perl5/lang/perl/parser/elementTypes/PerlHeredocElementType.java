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
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.text.CharArrayUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.HEREDOC_END;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.HEREDOC_END_INDENTABLE;


public class PerlHeredocElementType extends PerlReparseableElementType {
  public PerlHeredocElementType(@NotNull String debugName,
                                @NotNull Class<? extends PsiElement> clazz) {
    super(debugName, clazz);
  }

  /**
   * @implSpec this is low-performance heuristic reparsing detection. See #2243
   */
  @ApiStatus.ScheduledForRemoval(inVersion = "2020.3")
  @Deprecated
  @Override
  protected boolean isReparseableOld(@NotNull ASTNode parent,
                                     @NotNull CharSequence buffer,
                                     @NotNull Language fileLanguage,
                                     @NotNull Project project) {
    ASTNode run = parent.getFirstChildNode();
    if (run == null) {
      return false;
    }

    Checker checker = new Checker(buffer);

    while (run != null) {
      if (!checker.check(run)) {
        return false;
      }
      run = run.getTreeNext();
    }

    if (LOG.isDebugEnabled()) {
      LOG.debug("Heredoc reparseable: ", checker.result(),
                "; heredocs checked: ", checker.myNormalEndsChecked,
                "; indented checked: ", checker.myIndentedEndsChecked);
    }
    return checker.result();
  }

  private class Checker {
    private Set<String> myNormalEndsChecked = null;
    private Set<String> myIndentedEndsChecked = null;
    private final @NotNull CharSequence myNodeText;

    public Checker(@NotNull CharSequence nodeText) {
      int index = CharArrayUtil.shiftForward(nodeText, 0, " \t");
      myNodeText = index == 0 ? nodeText : nodeText.subSequence(index, nodeText.length());
    }

    /**
     * @return true iff it's ok to continue iteration, false otherwise.
     */
    boolean check(@NotNull ASTNode node) {
      IElementType elementType = node.getElementType();
      if (elementType != PerlHeredocElementType.this) {
        return true;
      }

      ASTNode closeMarker = node.getTreeNext();
      if (PsiUtilCore.getElementType(closeMarker) == TokenType.WHITE_SPACE) {
        closeMarker = closeMarker.getTreeNext();
      }
      if (closeMarker == null) {
        return true;
      }

      String closeMarkerText = closeMarker.getText() + "\n";
      IElementType closeMarkerType = closeMarker.getElementType();
      if (closeMarkerType == HEREDOC_END) {
        if (myNormalEndsChecked == null) {
          myNormalEndsChecked = new HashSet<>();
        }
        if (!myNormalEndsChecked.add(closeMarkerText)) {
          return true;
        }
        return !(StringUtil.startsWith(myNodeText, closeMarkerText) || StringUtil.contains(myNodeText, "\n" + closeMarkerText));
      }
      else if (closeMarkerType == HEREDOC_END_INDENTABLE) {
        if (myNormalEndsChecked == null) {
          myNormalEndsChecked = new HashSet<>();
        }
        if (myIndentedEndsChecked == null) {
          myIndentedEndsChecked = new HashSet<>();
        }
        myNormalEndsChecked.add(closeMarkerText);
        if (!myIndentedEndsChecked.add(closeMarkerText)) {
          return true;
        }
        if (StringUtil.startsWith(myNodeText, closeMarkerText)) {
          return false;
        }
        if (myNodeText.length() < closeMarkerText.length()) {
          return true;
        }
        return !Pattern.compile("\n\\s*" + closeMarkerText).matcher(myNodeText).find();
      }
      else {
        return false;
      }
    }

    boolean result() {
      return myNormalEndsChecked != null || myIndentedEndsChecked != null;
    }
  }
}
