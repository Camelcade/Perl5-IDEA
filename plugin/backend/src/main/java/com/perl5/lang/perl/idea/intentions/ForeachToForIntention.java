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
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class ForeachToForIntention extends PsiElementBaseIntentionAction {
  @Override
  public @Nls @NotNull String getText() {
    return PerlBundle.message("perl.intention.foreach.to.for");
  }

  @Override
  public @Nls @NotNull String getFamilyName() {
    return getText();
  }

  private @Nullable PerlForeachCompound getForeachStatement(@NotNull PsiElement element) {
    PerlForeachCompound forCompound = PsiTreeUtil.getParentOfType(element, PerlForeachCompound.class);
    if (forCompound == null) {
      return null;
    }

    // fixme we should work without variable and without declaration
    PsiPerlForeachIterator foreachIterator = forCompound.getForeachIterator();
    return foreachIterator != null &&
           foreachIterator.getExpr() instanceof PsiPerlVariableDeclarationLexical ? forCompound : null;
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
    PerlForeachCompound foreachStatement = getForeachStatement(element);
    if (foreachStatement == null) {
      return false;
    }
    PsiPerlForeachIterator foreachIterator = foreachStatement.getForeachIterator();
    if( foreachIterator == null){
      return false;
    }

    PsiPerlExpr iterableList = foreachStatement.getConditionExpr();
    if( iterableList == null){
      return false;
    }

    return foreachStatement.getBlock() != null;
  }

  @Override
  public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
    PerlForeachCompound foreachStatement = Objects.requireNonNull(getForeachStatement(element));
    PsiPerlForeachIterator foreachIterator = Objects.requireNonNull(foreachStatement.getForeachIterator());
    PsiPerlExpr variableDeclaration = Objects.requireNonNull(foreachIterator.getExpr());
    PsiPerlExpr iterableList = Objects.requireNonNull(foreachStatement.getConditionExpr());
    PsiPerlBlock block = Objects.requireNonNull(foreachStatement.getBlock());

    PsiPerlForCompound indexedFor = createIndexedFor(project, (PsiPerlVariableDeclarationLexical)variableDeclaration, iterableList, block);
    foreachStatement.replace(indexedFor);
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
