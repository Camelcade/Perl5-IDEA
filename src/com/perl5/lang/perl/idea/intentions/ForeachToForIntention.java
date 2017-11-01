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
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by bcardoso on 7/28/16.
 */
public class ForeachToForIntention extends PsiElementBaseIntentionAction {
  @Nls
  @NotNull
  @Override
  public String getText() {
    return PerlBundle.message("perl.intention.foreach.to.for");
  }

  @Nls
  @NotNull
  @Override
  public String getFamilyName() {
    return getText();
  }

  private PsiPerlForCompound getForStatement(@NotNull PsiElement element) {
    PsiPerlForCompound forCompound = PsiTreeUtil.getParentOfType(element, PsiPerlForCompound.class);
    if (forCompound == null || forCompound.getForIterator() != null) {
      return null;
    }

    List<PsiPerlExpr> variableAndList = forCompound.getExprList();
    // fixme we should work without variable and without declaration
    return variableAndList.size() == 2 && variableAndList.get(0) instanceof PsiPerlVariableDeclarationLexical ? forCompound : null;
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
    return getForStatement(element) != null;
  }

  @Override
  public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
    PsiPerlForCompound forCompound = getForStatement(element);
    assert forCompound != null;
    List<PsiPerlExpr> exprList = forCompound.getExprList();
    assert exprList.size() == 2;

    PsiPerlExpr variableDeclaration = exprList.get(0);
    assert variableDeclaration instanceof PsiPerlVariableDeclarationLexical;

    PsiPerlExpr iterableList = exprList.get(1);
    assert iterableList instanceof PsiPerlConditionExpr;

    PsiPerlBlock block = forCompound.getBlock();
    assert block != null;

    PsiPerlForCompound indexedFor = createIndexedFor(project, (PsiPerlVariableDeclarationLexical)variableDeclaration, iterableList, block);
    forCompound.replace(indexedFor);
  }

  @Override
  public boolean startInWriteAction() {
    return true;
  }

  public static PsiPerlForCompound createIndexedFor(@NotNull Project project,
                                                    @NotNull PsiPerlVariableDeclarationLexical variableDeclaration,
                                                    @NotNull PsiPerlExpr iterableExpr,
                                                    @NotNull PsiPerlBlock forBlock) {
    // check if listExpr is a single array or a list expression
    // fixme how about cast?
    PsiElement[] children = iterableExpr.getChildren();
    boolean isSingleArray = children.length == 1 && children[0] instanceof PsiPerlArrayVariable;

    String arrayName;
    if (isSingleArray) {
      PsiPerlArrayVariable childArray = (PsiPerlArrayVariable)children[0];
      arrayName = childArray.getName();
    }
    else {
      arrayName = "list";
    }

    // Assign iterationVariable = iterationArray[$idx]
    String assignStatementStr = String.format("%s = $%s[$idx];",
                                              variableDeclaration.getText(),
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
      String newListSyntax = String.format("my @%s = %s;\n", arrayName, iterableExpr.getText());
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
