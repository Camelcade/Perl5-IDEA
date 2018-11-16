/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.quickfixes;

import com.intellij.codeInsight.FileModificationService;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.psi.PerlUseStatement;
import com.perl5.lang.perl.psi.PsiPerlNamespaceContent;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 19.07.2015.
 */
public class PerlUsePackageQuickFix implements LocalQuickFix {
  String myPackageName;

  public PerlUsePackageQuickFix(String packageName) {
    myPackageName = packageName;
  }

  @Nls
  @NotNull
  @Override
  public String getName() {
    return PerlBundle.message("perl.quickfix.add.use.name", myPackageName);
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return PerlBundle.message("perl.quickfix.add.use.family");
  }

  @Override
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
    PsiElement newStatementContainer = descriptor.getPsiElement();
    if (!FileModificationService.getInstance().prepareFileForWrite(newStatementContainer.getContainingFile())) {
      return;
    }

    PsiElement newStatement = PerlElementFactory.createUseStatement(project, myPackageName);

    PsiElement afterAnchor = null;
    PsiElement beforeAnchor = null;

    PsiElement baseUseStatement = PsiTreeUtil.findChildOfType(newStatementContainer, PerlUseStatement.class);
    if (baseUseStatement != null) {
      if (((PerlUseStatement)baseUseStatement).isPragmaOrVersion()) // pragma or version
      {
        while (true) {
          // trying to find next use statement
          PsiElement nextStatement = baseUseStatement;

          while ((nextStatement = nextStatement.getNextSibling()) != null
                 && PerlParserDefinition.WHITE_SPACE_AND_COMMENTS.contains(nextStatement.getNode().getElementType())
            ) {

          }

          if (nextStatement instanceof PerlUseStatement &&
              ((PerlUseStatement)nextStatement).isPragmaOrVersion())    // found more use pragma/version
          {
            baseUseStatement = nextStatement;
          }
          else {
            afterAnchor = baseUseStatement;
            break;    // we've got last pragma statement
          }
        }
      }
      else    // not a pragma
      {
        beforeAnchor = baseUseStatement;
      }
    }
    else    // no uses found
    {
      PsiPerlNamespaceDefinition baseNamespace = PsiTreeUtil.findChildOfType(newStatementContainer, PsiPerlNamespaceDefinition.class);
      if (baseNamespace != null && baseNamespace.getBlock() != null)    // got a namespace definition
      {
        newStatementContainer = baseNamespace.getBlock();
        if (newStatementContainer != null && !(newStatementContainer instanceof PsiPerlNamespaceContent)) {
          afterAnchor = newStatementContainer.getFirstChild();
        }
        else if (newStatementContainer != null && newStatementContainer.getFirstChild() != null) {
          beforeAnchor = newStatementContainer.getFirstChild();
        }
      }
      else {
        PsiElement anchor = newStatementContainer.getFirstChild();
        if (anchor instanceof PsiComment) {
          while (anchor.getNextSibling() != null && PerlPsiUtil.isCommentOrSpace(anchor.getNextSibling())) {
            anchor = anchor.getNextSibling();
          }
          afterAnchor = anchor;
        }
        else if (anchor != null) {
          beforeAnchor = anchor;
        }
      }
    }

    if (afterAnchor != null) {
      newStatementContainer = afterAnchor.getParent();
      newStatement = newStatementContainer.addAfter(newStatement, afterAnchor);
    }
    else if (beforeAnchor != null) {
      newStatementContainer = beforeAnchor.getParent();
      newStatement = newStatementContainer.addBefore(newStatement, beforeAnchor);
    }
    else if (newStatementContainer != null) {
      newStatement = newStatementContainer.add(newStatement);
    }

    if (newStatement != null) {
      PsiElement newLineElement = PerlElementFactory.createNewLine(project);
      PsiElement nextSibling = newStatement.getNextSibling();
      PsiElement preveSibling = newStatement.getPrevSibling();
      newStatementContainer = newStatement.getParent();

      if (nextSibling == null || !(nextSibling instanceof PsiWhiteSpace) || !StringUtil.equals(nextSibling.getText(), "\n")) {
        newStatementContainer.addAfter(newLineElement, newStatement);
      }
      if ((preveSibling == null && !(newStatementContainer instanceof PsiFile)) ||
          !(preveSibling instanceof PsiWhiteSpace) ||
          !StringUtil.equals(preveSibling.getText(), "\n")
        ) {
        newStatementContainer.addBefore(newLineElement, newStatement);
      }
    }
  }
}
