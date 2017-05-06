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

package com.perl5.lang.perl.idea.livetemplates;

import com.intellij.codeInsight.template.EverywhereContextType;
import com.intellij.codeInsight.template.TemplateContextType;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.fileTypes.PerlFileTypeTest;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ELI-HOME on 01-Jun-15.
 */
public abstract class PerlTemplateContextType extends TemplateContextType {
  protected PerlTemplateContextType(@NotNull String id,
                                    @NotNull String presentableName,
                                    Class<? extends TemplateContextType> baseContextType) {
    super(id, presentableName, baseContextType);
  }

  @Override
  public boolean isInContext(@NotNull PsiFile psiFile, int fileOffset) {
    if (PsiUtilCore.getLanguageAtOffset(psiFile, fileOffset).isKindOf(PerlLanguage.INSTANCE)) {
      PsiElement element = psiFile.findElementAt(fileOffset);

      if (element == null) {
        element = psiFile.findElementAt(fileOffset - 1);
      }

      if (element == null) {
        return false;
      }

      IElementType tokenType = element.getNode().getElementType();

      if (element instanceof PsiWhiteSpace ||
          element instanceof PerlStringContentElementImpl ||
          element instanceof PsiComment ||
          tokenType == PerlElementTypes.REGEX_TOKEN) {
        return false;
      }
      return isInContext(element);
    }
    return false;
  }

  protected abstract boolean isInContext(PsiElement element);

  public static class Generic extends PerlTemplateContextType {
    public Generic() {
      super("PERL5", PerlLanguage.NAME, EverywhereContextType.class);
    }

    @Override
    public boolean isInContext(PsiElement element) {
      return true;
    }
  }

  public static class Postfix extends PerlTemplateContextType {
    public Postfix() {
      super("PERL5_POSTFIX", "Postfix", Generic.class);
    }

    @Override
    public boolean isInContext(PsiElement element) {
      PsiPerlStatement statement = PsiTreeUtil.getParentOfType(element, PsiPerlStatement.class);

      return statement != null
             && statement.getTextOffset() < element.getTextOffset()
        ;
    }
  }

  public static class Prefix extends PerlTemplateContextType {
    public Prefix() {
      this("PERL5_PREFIX", "Prefix");
    }

    public Prefix(@NotNull String id, @NotNull String presentableName) {
      super(id, presentableName, Generic.class);
    }

    @Override
    public boolean isInContext(PsiElement element) {
      PsiPerlStatement statement = PsiTreeUtil.getParentOfType(element, PsiPerlStatement.class);

      return statement != null
             && statement.getTextOffset() == element.getTextOffset();
    }
  }

  public static class UnfinishedIf extends PerlTemplateContextType.Prefix {
    public UnfinishedIf() {
      super("PERL5_UNFINISHED_IF", "Unclosed if statement");
    }

    @Override
    public boolean isInContext(PsiElement element) {
      return super.isInContext(element) && PerlElementPatterns.ELSE_ELSIF_PLACE.accepts(element);
    }
  }

  public static class TestFile extends PerlTemplateContextType.Prefix {
    public TestFile() {
      super("PERL5_TEST_FILE", "Test file prefix");
    }

    @Override
    public boolean isInContext(PsiElement element) {
      return element.getContainingFile().getViewProvider().getVirtualFile().getFileType() == PerlFileTypeTest.INSTANCE &&
             super.isInContext(element);
    }
  }
}
