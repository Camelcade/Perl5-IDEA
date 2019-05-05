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
import com.perl5.PerlBundle;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.fileTypes.PerlFileTypeTest;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlIfUnlessCompound;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import com.perl5.lang.perl.psi.properties.PerlLoop;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;


public abstract class PerlTemplateContextType extends TemplateContextType {
  protected PerlTemplateContextType(@NotNull String id,
                                    @NotNull String presentableName,
                                    Class<? extends TemplateContextType> baseContextType) {
    super(id, presentableName, baseContextType);
  }

  @Override
  public boolean isInContext(@NotNull PsiFile psiFile, int fileOffset) {
    if (!PsiUtilCore.getLanguageAtOffset(psiFile, fileOffset).isKindOf(PerlLanguage.INSTANCE)) {
      return false;
    }
    PsiElement element = psiFile.findElementAt(fileOffset);

    if (element == null) {
      element = psiFile.findElementAt(fileOffset - 1);
    }

    if (element == null) {
      return false;
    }

    IElementType tokenType = PsiUtilCore.getElementType(element);

    return !(element instanceof PsiWhiteSpace ||
             element instanceof PerlStringContentElementImpl ||
             element instanceof PsiComment ||
             tokenType == PerlElementTypes.REGEX_TOKEN) &&
           isInContext(element);
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
      super("PERL5_POSTFIX", PerlBundle.message("perl.template.context.postfix"), Generic.class);
    }

    @Override
    public boolean isInContext(PsiElement element) {
      PsiPerlStatement statement = PsiTreeUtil.getParentOfType(element, PsiPerlStatement.class);

      return statement != null && statement.getTextOffset() < element.getTextOffset();
    }
  }

  public static class Prefix extends PerlTemplateContextType {
    public Prefix() {
      this("PERL5_PREFIX", PerlBundle.message("perl.template.context.prefix"));
    }

    public Prefix(@NotNull String id, @NotNull String presentableName) {
      super(id, presentableName, Generic.class);
    }

    @Override
    public boolean isInContext(PsiElement element) {
      PsiPerlStatement statement = PsiTreeUtil.getParentOfType(element, PsiPerlStatement.class);

      return statement != null && statement.getTextOffset() == element.getTextOffset();
    }
  }

  public static class UnfinishedIf extends PerlTemplateContextType.Prefix {
    public UnfinishedIf() {
      super("PERL5_UNFINISHED_IF", PerlBundle.message("perl.template.context.incomplete.if"));
    }

    @Override
    public boolean isInContext(PsiElement element) {
      if (!super.isInContext(element)) {
        return false;
      }
      PsiPerlStatement statement = PsiTreeUtil.getParentOfType(element, PsiPerlStatement.class);
      if (statement == null) {
        return false;
      }

      PsiElement ifStatement = PerlPsiUtil.getPrevSignificantSibling(statement);
      return ifStatement instanceof PerlIfUnlessCompound && ((PerlIfUnlessCompound)ifStatement).getUnconditionalBlock() == null;
    }
  }

  public static class Continue extends PerlTemplateContextType.Prefix {
    public Continue() {
      super("PERL5_CONTINUE", PerlBundle.message("perl.template.context.continue"));
    }

    @Override
    public boolean isInContext(PsiElement element) {
      if (!super.isInContext(element)) {
        return false;
      }

      PsiPerlStatement statement = PsiTreeUtil.getParentOfType(element, PsiPerlStatement.class);
      if (statement == null) {
        return false;
      }

      PsiElement perlLoop = PerlPsiUtil.getPrevSignificantSibling(statement);
      return perlLoop instanceof PerlLoop &&
             ((PerlLoop)perlLoop).canHaveContinueBlock() &&
             ((PerlLoop)perlLoop).getContinueBlock() == null;
    }
  }

  public static class TestFile extends PerlTemplateContextType.Prefix {
    public TestFile() {
      super("PERL5_TEST_FILE", PerlBundle.message("perl.template.context.test.file"));
    }

    @Override
    public boolean isInContext(PsiElement element) {
      return PerlFileTypeTest.isMy(element) && super.isInContext(element);
    }
  }
}
