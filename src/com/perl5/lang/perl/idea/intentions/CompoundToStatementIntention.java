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
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlStatementImpl;
import com.perl5.lang.perl.psi.properties.PerlConvertableCompound;
import com.perl5.lang.perl.psi.properties.PerlConvertableCompoundSimple;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.util.PerlVariableUtil;
import org.jetbrains.annotations.*;

import java.util.ArrayList;
import java.util.List;

public class CompoundToStatementIntention extends PsiElementBaseIntentionAction {
  @Override
  public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement elementAtCursor) throws IncorrectOperationException {
    PerlConvertableCompound compound = getCompound(elementAtCursor);
    if (compound instanceof PerlForCompound) {
      convertForCompound((PerlForCompound)compound);
    }
    else if (compound instanceof PerlConvertableCompoundSimple) {
      convertSimpleCompound((PerlConvertableCompoundSimple)compound);
    }
  }

  @Override
  public boolean isAvailable(@NotNull Project project, Editor editor, @NotNull PsiElement element) {
    return getCompound(element) != null;
  }

  @NotNull
  @Override
  public String getText() {
    return PerlBundle.message("perl.intention.convert.to.statement");
  }

  @Nls
  @NotNull
  @Override
  public String getFamilyName() {
    return getText();
  }

  /**
   * Converting for/foreach compound
   */
  private static void convertForCompound(@NotNull PerlForCompound forCompound) throws IncorrectOperationException {
    PsiPerlExpr statementExpr = getStatementExpression(forCompound);

    String statementText = computeStatementText(forCompound, statementExpr);

    PsiPerlExpr controlExpr = forCompound.getConditionExpr();
    if (controlExpr == null) {
      return;
    }

    replaceWithStatement(forCompound, statementText, controlExpr);
  }

  /**
   * Converting simple compound (not for/foreach)
   */
  private static void convertSimpleCompound(@NotNull PerlConvertableCompoundSimple simpleCompound) throws IncorrectOperationException {
    PsiPerlExpr statementExpr = getStatementExpression(simpleCompound);

    PsiPerlConditionExpr controlExpr = simpleCompound.getConditionExpr();
    if (controlExpr == null) {
      error("perl.intention.convert.to.statement.error.no.control");
    }

    replaceWithStatement(simpleCompound, statementExpr.getText(), controlExpr);
  }


  /**
   * Computes statement text for foreach compound block. Replacing iterator variable with $_ if necessary
   *
   * @param forCompound   for compound statement
   * @param statementExpr statement expression
   * @return adjusted text or null if something went wrong
   */
  @NotNull
  private static String computeStatementText(@NotNull PerlForCompound forCompound, @NotNull PsiPerlExpr statementExpr) {
    String statementText = statementExpr.getText();
    PsiPerlForeachIterator foreachIterator = forCompound.getForeachIterator();
    if (foreachIterator == null) {
      return statementText;
    }

    PsiPerlExpr variableExpression = foreachIterator.getExpr();
    if (variableExpression instanceof PerlVariableDeclarationExpr) {
      List<PerlVariable> variables = ((PerlVariableDeclarationExpr)variableExpression).getVariables();
      if (variables.size() == 1) {
        variableExpression = variables.get(0);
      }
    }

    // replacing variables instatement to $_
    if (variableExpression instanceof PerlVariable) {
      TextRange statementExprTextRange = statementExpr.getTextRange();
      PerlVariable finalVariable = (PerlVariable)variableExpression;
      List<PerlVariable> varsToReplace = new ArrayList<>();
      statementExpr.accept(new PerlRecursiveVisitor() {
        @Override
        public void visitPerlVariable(@NotNull PerlVariable o) {
          if (PerlVariableUtil.equal(o, finalVariable)) {
            varsToReplace.add(o);
          }
          super.visitPerlVariable(o);
        }
      });

      for (int i = varsToReplace.size() - 1; i >= 0; i--) {
        PerlVariable variable = varsToReplace.get(i);
        PsiElement nameElement = variable.getVariableNameElement();
        if (nameElement instanceof LeafPsiElement) {
          // replacing variable name with _ => $var => $_
          statementText = nameElement
            .getTextRange()
            .shiftRight(-statementExprTextRange.getStartOffset())
            .replace(statementText, "_");
        }
      }
    }

    return statementText;
  }

  private static void error(@NotNull @NonNls @PropertyKey(resourceBundle = "messages.PerlBundle") String bundleKey)
    throws IncorrectOperationException {
    throw new IncorrectOperationException(PerlBundle.message(bundleKey));
  }

  /**
   * Generating new code, extracting declarations from control expression and replacing compound statement if possible.
   *
   * @param compound      compound statement
   * @param statementText statement text
   * @param controlExpr   control expression (condition or iterable)
   */
  private static void replaceWithStatement(@NotNull PerlConvertableCompound compound,
                                           @NotNull String statementText,
                                           @NotNull PsiPerlExpr controlExpr) {

    List<PsiElement> declarations = new ArrayList<>();
    controlExpr.accept(new PerlRecursiveVisitor() {
      @Override
      public void visitPerlVariableDeclarationExpr(@NotNull PerlVariableDeclarationExpr o) {
        declarations.add(o);
        super.visitPerlVariableDeclarationExpr(o);
      }
    });

    String controlExprText = controlExpr.getText();

    StringBuilder sb = new StringBuilder();
    if (!declarations.isEmpty()) {
      // extracting declarations from control statement
      TextRange controlExprTextRange = controlExpr.getTextRange();
      for (int i = declarations.size() - 1; i >= 0; i--) {
        PsiElement declaration = declarations.get(i);
        sb.append(declaration.getText()).append(";\n");
        PsiElement keywordElement = declaration.getFirstChild();
        if (keywordElement instanceof LeafPsiElement) {
          // removing keyword from control expression my $var => $var, my ($var1, $var2) => ($var1, $var2)
          controlExprText = keywordElement
            .getTextRange()
            .shiftRight(-controlExprTextRange.getStartOffset())
            .replace(controlExprText, "");
        }
      }
    }

    sb.append(statementText)
      .append(" ")
      .append(compound.getFirstChild().getText())
      .append(" ")
      .append(controlExprText)
      .append(";");

    PerlFileImpl perlFile = PerlElementFactory.createFile(compound.getProject(), sb.toString());
    PsiPerlStatementImpl[] statements = PsiTreeUtil.getChildrenOfType(perlFile, PsiPerlStatementImpl.class);
    if (statements == null || statements.length == 0) {
      return;
    }

    PsiElement container = compound.getParent();
    if (container == null) {
      return;
    }
    container.addRangeBefore(statements[0], statements[statements.length - 1], compound);
    compound.delete();
  }

  /**
   * Finds statement's expression text if compound has block and single statement without modifier in it.
   *
   * @return expression text or null if no statement or statement has modifier
   */
  @NotNull
  private static PsiPerlExpr getStatementExpression(@NotNull PerlConvertableCompound convertableCompound)
    throws IncorrectOperationException {
    PsiPerlBlock block = convertableCompound.getBlock();
    if (block == null) {
      error("perl.intention.convert.to.statement.error.no.block");
    }

    PsiElement[] statements = block.getChildren();
    if (statements.length != 1) {
      error("perl.intention.convert.to.statement.error.many.statements");
    }
    if (!(statements[0] instanceof PsiPerlStatementImpl)) {
      error("perl.intention.convert.to.statement.error.incorrect.statement");
    }
    if (((PsiPerlStatementImpl)statements[0]).getModifier() != null) {
      error("perl.intention.convert.to.statement.error.has.modifier");
    }

    PsiPerlExpr expr = ((PsiPerlStatementImpl)statements[0]).getExpr();
    if (expr == null) {
      error("perl.intention.convert.to.statement.error.empty.statement");
    }
    return expr;
  }

  /**
   * @return convertable compound statement wrapping element under cursor
   */
  @Nullable
  private static PerlConvertableCompound getCompound(@NotNull PsiElement elementAtCursor) {
    PerlConvertableCompound targetCompound = PsiTreeUtil.getParentOfType(elementAtCursor, PerlConvertableCompound.class);
    return targetCompound != null && targetCompound.isConvertableToModifier() ? targetCompound : null;
  }
}
