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

package com.perl5.lang.perl.idea.copyright;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ObjectUtils;
import com.maddyhome.idea.copyright.CopyrightProfile;
import com.maddyhome.idea.copyright.options.LanguageOptions;
import com.maddyhome.idea.copyright.psi.UpdatePsiFileCopyright;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.lexer.PerlElementTypesGenerated;
import com.perl5.lang.pod.idea.completion.PodTitleCompletionProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class UpdatePerlPackageFileCopyright extends UpdatePsiFileCopyright {
  public UpdatePerlPackageFileCopyright(Project project,
                                        Module module,
                                        VirtualFile root, CopyrightProfile options) {
    super(project, module, root, options);
  }

  @Override
  protected void scanFile() {
    PsiFile psiFile = getFile();
    PsiElement run = psiFile.getFirstChild();
    if (run == null) {
      checkComments(psiFile, true, Collections.emptyList());
      return;
    }
    PsiElement anchor = null;
    List<PsiComment> comments = new ArrayList<>();
    while (run != null) {
      IElementType elementType = PsiUtilCore.getElementType(run);
      if (elementType == PerlElementTypesGenerated.COMMENT_LINE) {
        comments.add((PsiComment)run);
      }
      else if (elementType == PerlElementTypesGenerated.POD && comments.isEmpty()) {
        comments.add((PsiComment)run);
        while (run != null && PerlParserDefinition.MEANINGLESS_TOKENS.contains(PsiUtilCore.getElementType(run))) {
          run = run.getNextSibling();
        }
        anchor = ObjectUtils.notNull(run, PsiTreeUtil.getDeepestLast(psiFile));
        break;
      }
      else if (elementType != TokenType.WHITE_SPACE) {
        anchor = run;
        break;
      }
      run = run.getNextSibling();
    }
    if (comments.isEmpty()) {
      checkComments(ObjectUtils.notNull(anchor, PsiTreeUtil.getDeepestLast(psiFile)), true, comments);
    }
    else {
      checkComments(anchor, true, comments);
    }
  }

  @Override
  protected String getCommentText(String prefix, String suffix) {
    LanguageOptions languageOptions = getLanguageOptions();
    String defaultText = super.getCommentText(prefix, suffix);
    if (languageOptions.isBlock()) {
      String podTextStart = "=head1 " + PodTitleCompletionProvider.COPYRIGHT_AND_LICENSE + "\n\n" +
                            StringUtil.trimStart(defaultText, "# \n");
      int i = podTextStart.length();
      while (i > 0 && podTextStart.charAt(i - 1) == '\n') {
        i--;
      }
      return podTextStart.substring(0, i) + "\n=cut" + podTextStart.substring(i);
    }
    return defaultText;
  }
}
