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

package com.perl5.lang.pod.idea.livetemplates;

import com.intellij.codeInsight.template.TemplateActionContext;
import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.PerlBundle;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.parser.psi.mixin.PodOverSectionContent;
import com.perl5.lang.pod.parser.psi.mixin.PodSectionItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.pod.lexer.PodElementTypes.POD_OUTER;
import static com.perl5.lang.pod.parser.PodElementTypesGenerated.POD_CODE;
import static com.perl5.lang.pod.parser.PodElementTypesGenerated.POD_NEWLINE;

public abstract class PodTemplateContextType extends TemplateContextType {

  public PodTemplateContextType(@NlsContexts.Label @NotNull String presentableName) {
    super(presentableName);
  }

  @Override
  public boolean isInContext(@NotNull TemplateActionContext templateActionContext) {
    var psiFile = templateActionContext.getFile();
    var startOffset = templateActionContext.getStartOffset();
    PsiFile podFile = psiFile.getViewProvider().getPsi(PodLanguage.INSTANCE);

    if (podFile == null) {
      return false;
    }

    PsiElement element = podFile.findElementAt(startOffset);

    if (element == null) {
      element = podFile.findElementAt(startOffset - 1);
    }

    if (element == null) {
      return false;
    }

    return isInContext(element);
  }

  protected abstract boolean isInContext(PsiElement element);

  public static class Generic extends PodTemplateContextType {
    public Generic() {
      this(PodLanguage.NAME);
    }

    public Generic(@NlsContexts.Label String presentableName) {
      super(presentableName);
    }

    @Override
    protected boolean isInContext(PsiElement element) {
      return false;
    }
  }

  public static class CommandPosition extends PodTemplateContextType.Generic {
    public CommandPosition() {
      this(PerlBundle.message("label.command.position"));
    }

    public CommandPosition(@NlsContexts.Label String presentableName) {
      super(presentableName);
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
        IElementType prevElementType = PsiUtilCore.getElementType(prevElement);

        if (prevElementType == POD_NEWLINE || prevElementType == POD_OUTER) {
          return true;
        }

        if (prevElement instanceof PsiWhiteSpace && StringUtil.equals(prevElement.getText(), "\n")) {
          while (true) {
            if (prevElement == null || prevElement.getTextOffset() == 0) {
              return true;
            }
            else if (prevElement.getNode().getElementType() == POD_NEWLINE) {
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
    public InsideOver(@NlsContexts.Label String presentableName) {
      super(presentableName);
    }

    @Override
    protected boolean isInContext(PsiElement element) {
      return super.isInContext(element) && PsiTreeUtil.getParentOfType(element, PodOverSectionContent.class) != null;
    }

    protected @Nullable PodSectionItem getFirstSectionItem(PsiElement element) {
      PodOverSectionContent sectionContent = PsiTreeUtil.getParentOfType(element, PodOverSectionContent.class);
      return sectionContent == null ? null : sectionContent.getFirstItem();
    }
  }

  public static class InsideOverBulleted extends InsideOver {
    public InsideOverBulleted() {
      super(PerlBundle.message("label.inside.over.block.bulleted"));
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
      super(PerlBundle.message("label.inside.over.block.not.bulleted"));
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
