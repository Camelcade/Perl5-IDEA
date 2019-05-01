/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.pod.idea.livetemplates;

import com.intellij.codeInsight.template.EverywhereContextType;
import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.livetemplates.PerlTemplateContextType;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.parser.psi.PodOverSectionContent;
import com.perl5.lang.pod.parser.psi.PodSectionItem;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PodTemplateContextType extends TemplateContextType implements PodElementTypes {

  public PodTemplateContextType(@NotNull @NonNls String id, @NotNull String presentableName) {
    this(id, presentableName, PerlTemplateContextType.class);
  }

  public PodTemplateContextType(@NotNull @NonNls String id,
                                @NotNull String presentableName,
                                @Nullable Class<? extends TemplateContextType> baseContextType) {
    super(id, presentableName, baseContextType);
  }

  @Override
  public boolean isInContext(@NotNull PsiFile file, int offset) {
    PsiFile podFile = file.getViewProvider().getPsi(PodLanguage.INSTANCE);

    if (podFile == null) {
      return false;
    }

    PsiElement element = podFile.findElementAt(offset);

    if (element == null) {
      element = podFile.findElementAt(offset - 1);
    }

    if (element == null) {
      return false;
    }

    return isInContext(element);
  }

  protected abstract boolean isInContext(PsiElement element);

  public static class Generic extends PodTemplateContextType {
    public Generic() {
      super("POD", PodLanguage.NAME, EverywhereContextType.class);
    }

    public Generic(String id, String presentableName) {
      super(id, presentableName, PodTemplateContextType.Generic.class);
    }

    @Override
    protected boolean isInContext(PsiElement element) {
      return false;
    }
  }

  public static class CommandPosition extends PodTemplateContextType.Generic {
    public CommandPosition() {
      super("POD_COMMAND", "Command position");
    }

    public CommandPosition(String id, String presentableName) {
      super(id, presentableName);
    }

    @Override
    protected boolean isInContext(PsiElement element) {
      int startOffset = element.getNode().getStartOffset();

      if (startOffset == 0) {
        return true;
      }
      else {
        FileViewProvider viewProvider = element.getContainingFile().getViewProvider();

        PsiElement currentElement = viewProvider.findElementAt(startOffset, PodLanguage.INSTANCE);
        if (currentElement != null && currentElement.getNode().getElementType() == POD_CODE) {
          return false;
        }

        PsiElement prevElement = viewProvider.findElementAt(startOffset - 1, PodLanguage.INSTANCE);

        if (prevElement != null && prevElement.getNode().getElementType() == PodElementTypes.POD_NEWLINE) {
          return true;
        }

        if (prevElement instanceof PsiWhiteSpace && StringUtil.equals(prevElement.getText(), "\n")) {
          while (true) {
            if (prevElement == null || prevElement.getTextOffset() == 0) {
              return true;
            }
            else if (prevElement.getNode().getElementType() == PodElementTypes.POD_NEWLINE) {
              return true;
            }
            else if (!(prevElement instanceof PsiWhiteSpace)) {
              return false;
            }
            prevElement = viewProvider.findElementAt(prevElement.getNode().getStartOffset() - 1, PodLanguage.INSTANCE);
          }
        }
      }
      return super.isInContext(element);
    }
  }

  public static class InsideOver extends CommandPosition {
    public InsideOver(String id, String presentableName) {
      super(id, presentableName);
    }

    @Override
    protected boolean isInContext(PsiElement element) {
      return super.isInContext(element) && PsiTreeUtil.getParentOfType(element, PodOverSectionContent.class) != null;
    }

    @Nullable
    protected PodSectionItem getFirstSectionItem(PsiElement element) {
      PodOverSectionContent sectionContent = PsiTreeUtil.getParentOfType(element, PodOverSectionContent.class);
      return sectionContent == null ? null : sectionContent.getFirstItem();
    }
  }

  public static class InsideOverBulleted extends InsideOver {
    public InsideOverBulleted() {
      super("POD_OVER_BULLETED", "Inside =over block, bulleted");
    }

    @Override
    protected boolean isInContext(PsiElement element) {
      if (!super.isInContext(element)) {
        return false;
      }

      PodSectionItem firstItem = getFirstSectionItem(element);
      return firstItem == null || firstItem.isBulleted();
    }
  }

  public static class InsideOverNotBulleted extends InsideOver {
    public InsideOverNotBulleted() {
      super("POD_OVER_NOT_BULLETED", "Inside =over block, not bulleted");
    }

    @Override
    protected boolean isInContext(PsiElement element) {
      if (!super.isInContext(element)) {
        return false;
      }

      PodSectionItem firstItem = getFirstSectionItem(element);
      return firstItem == null || !firstItem.isBulleted();
    }
  }
}
