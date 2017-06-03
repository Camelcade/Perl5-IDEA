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

package com.perl5.lang.perl.idea.intentions;

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by bcardoso on 7/28/16.
 */
public class ForeachToForConverter extends PsiElementBaseIntentionAction {
  @Nls
  @NotNull
  @Override
  public String getText() {
    return "Convert foreach to indexed for (Alpha)";
  }

  @Nls
  @NotNull
  @Override
  public String getFamilyName() {
    return getText();
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
    if (!element.isWritable()) {
      return false;
    }

    PsiElement parent = element.getParent();
    if (!(parent instanceof PsiPerlForeachCompound)) {
      return false;
    }

    PsiPerlForListStatement forListStatement = ((PsiPerlForeachCompound)parent).getForListStatement();
    return forListStatement != null
           && forListStatement.getExpr() instanceof PsiPerlVariableDeclarationLexical;
  }

  @Override
  public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
    PsiPerlForeachCompound foreachElement = (PsiPerlForeachCompound)element.getParent();

    PsiPerlForListStatement forListStatement = foreachElement.getForListStatement();
    assert forListStatement != null;

    PsiPerlExpr forExpr = forListStatement.getExpr();
    assert forExpr != null;

    PsiPerlForListEpxr forListEpxr = forListStatement.getForListEpxr();

    PsiPerlBlock block = foreachElement.getBlock();
    assert block != null;

    PsiPerlForCompound indexedFor = createIndexedFor(project, forExpr, forListEpxr, block);
    foreachElement.replace(indexedFor);
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }

  public static PsiPerlForCompound createIndexedFor(@NotNull Project project,
                                                    @NotNull PsiPerlExpr lexicalVariableDeclaration,
                                                    @NotNull PsiPerlForListEpxr listExpr,
                                                    @NotNull PsiPerlBlock forBlock) {
    // check if listExpr is a single array or a list expression
    boolean isSingleArray = listExpr.getFirstChild() instanceof PsiPerlArrayVariable
                            && (listExpr.getChildren().length == 1);

    String arrayName;
    if (isSingleArray) {
      PsiPerlArrayVariable childArray = (PsiPerlArrayVariable)listExpr.getFirstChild();
      arrayName = childArray.getName();
    }
    else {
      arrayName = "list";
    }

    // Assign iterationVariable = iterationArray[$idx]
    String assignStatementStr = String.format("%s = $%s[$idx];",
                                              lexicalVariableDeclaration.getText(),
                                              arrayName);
    PsiPerlStatement assignStatement = createPsiOfTypeFromSyntax(project, assignStatementStr, PsiPerlStatement.class);

    // Define where to insert the new statement
    PsiPerlStatement[] statementList = PsiTreeUtil.getChildrenOfType(forBlock, PsiPerlStatement.class);
    PsiElement anchorPoint = statementList == null ? forBlock.getLastChild() : statementList[0];
    forBlock.addBefore(assignStatement, anchorPoint);

    String indexedForSyntax = String.format("for (my $idx = 0; $idx < scalar(@%s); $idx++) %s",
                                            arrayName,
                                            forBlock.getText());

    PsiPerlForCompound result = createPsiOfTypeFromSyntax(project, indexedForSyntax, PsiPerlForCompound.class);

    if (!isSingleArray) {
      // declare and initialize the new array before the new for
      String newListSyntax = String.format("my @%s = %s;\n", arrayName, listExpr.getText());
      PsiPerlStatement newListStatement = createPsiOfTypeFromSyntax(project, newListSyntax, PsiPerlStatement.class);
      PsiWhiteSpace newLineElement = createPsiOfTypeFromSyntax(project, newListSyntax, PsiWhiteSpace.class);
      result.addBefore(newLineElement, result.getFirstChild());
      result.addBefore(newListStatement, result.getFirstChild());
    }

    assert result != null : "While creating PsiPerlForCompound";
    return result;
  }

  private static <T extends PsiElement> T createPsiOfTypeFromSyntax(Project project, String syntax, Class<T> type) {
    return PsiTreeUtil.findChildOfType(PerlElementFactory.createFile(project, syntax), type);
  }
}
