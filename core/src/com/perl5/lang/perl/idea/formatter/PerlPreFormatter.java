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

package com.perl5.lang.perl.idea.formatter;

import com.intellij.application.options.CodeStyle;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.formatter.operations.*;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 12.11.2015.
 */
public class PerlPreFormatter extends PerlRecursiveVisitor implements PerlCodeStyleSettings.OptionalConstructions, PerlElementTypes {
  private static final Pattern ASCII_IDENTIFIER_PATTERN = Pattern.compile("[_a-zA-Z][_\\w]*");
  private static final Pattern ASCII_BARE_STRING_PATTERN = Pattern.compile("-?[_a-zA-Z][_\\w]*");

  protected final Project myProject;
  protected final PerlCodeStyleSettings myPerlSettings;

  private final List<PerlFormattingOperation> myFormattingOperations = new ArrayList<>();
  protected TextRange myRange;

  public PerlPreFormatter(Project project) {
    myProject = project;
    myPerlSettings = CodeStyle.getSettings(project).getCustomSettings(PerlCodeStyleSettings.class);
  }

  public TextRange process(PsiElement element, TextRange range) {
    myRange = range;
    final PsiDocumentManager manager = PsiDocumentManager.getInstance(myProject);
    final Document document = manager.getDocument(element.getContainingFile());
    int myDelta = 0;
    if (document != null) {
      manager.doPostponedOperationsAndUnblockDocument(document);

      try {
        // scan document
        element.accept(this);

        for (int i = myFormattingOperations.size() - 1; i >= 0; i--) {
          myDelta += myFormattingOperations.get(i).apply();
        }
      }
      finally {
        manager.commitDocument(document);
      }
    }

    return TextRange.create(range.getStartOffset(), range.getEndOffset() + myDelta);
  }

  protected void removeElement(PsiElement o) {
    myFormattingOperations.add(new PerlFormattingRemove(o));
  }

  protected void replaceElement(PsiElement what, PsiElement with) {
    myFormattingOperations.add(new PerlFormattingReplace(what, with));
  }

  protected void insertElementAfter(PsiElement element, PsiElement anchor) {
    myFormattingOperations.add(new PerlFormattingInsertAfter(element, anchor));
  }

  protected void insertElementBefore(PsiElement element, PsiElement anchor) {
    myFormattingOperations.add(new PerlFormattingInsertBefore(element, anchor));
  }

  protected boolean isStringQuotable(PsiPerlStringBare o) {
    return myPerlSettings.OPTIONAL_QUOTES == FORCE && isCommaArrowAhead(o) ||
           myPerlSettings.OPTIONAL_QUOTES_HASH_INDEX == FORCE && isHashIndexKey(o);
  }

  protected boolean isSimpleScalarCast(PerlCastExpression o) {
    return o.getLastChild() instanceof PsiPerlScalarVariable || isNotSoSimpleScalarCast(o);
  }

  protected boolean isNotSoSimpleScalarCast(PerlCastExpression o) {
    PsiPerlScalarVariable variable = PsiTreeUtil.findChildOfType(o, PsiPerlScalarVariable.class);
    if (variable != null) {
      PsiElement statement = variable.getParent();
      if (statement instanceof PsiPerlStatement && statement.getChildren().length == 1 && o.equals(statement.getParent())) {
        return true;
      }
    }
    return false;
  }

  protected boolean isStringInHeredocQuotable(PsiPerlStringBare o) {
    return myPerlSettings.OPTIONAL_QUOTES_HEREDOC_OPENER == FORCE && isInHeredocOpener(o) && !isBackrefString(o);
  }

  protected boolean isBackrefString(PsiPerlStringBare o) {
    PsiElement predecessor = o.getPrevSibling();
    return predecessor != null && predecessor.getNode().getElementType() == OPERATOR_REFERENCE;
  }

  protected boolean isStringUnqutable(PerlString o) {
    return isStringSimple(o) && (
      isHashIndexKey(o) && myPerlSettings.OPTIONAL_QUOTES_HASH_INDEX == SUPPRESS ||
      isCommaArrowAhead(o) && myPerlSettings.OPTIONAL_QUOTES == SUPPRESS)
      ;
  }

  protected boolean isStringInHeredocUnquotable(PerlString o) {
    return isStringSimpleIdentifier(o) && isInHeredocOpener(o) && myPerlSettings.OPTIONAL_QUOTES_HEREDOC_OPENER == SUPPRESS;
  }

  @Override
  public void visitStringDq(@NotNull PsiPerlStringDq o) {
    if (!myRange.contains(o.getTextRange())) {
      return;
    }
    if (isStringUnqutable(o) || isStringInHeredocUnquotable(o)) {
      unquoteString(o);
    }
    else {
      super.visitStringDq(o);
    }
  }

  @Override
  public void visitStringSq(@NotNull PsiPerlStringSq o) {
    if (!myRange.contains(o.getTextRange())) {
      return;
    }
    if (isStringUnqutable(o)) {
      unquoteString(o);
    }
    else {
      super.visitStringSq(o);
    }
  }

  @Override
  public void visitStringBare(@NotNull PsiPerlStringBare o) {
    if (!myRange.contains(o.getTextRange())) {
      return;
    }
    if (isStringQuotable(o)) {
      replaceElement(o, PerlElementFactory.createString(myProject, "'" + ElementManipulators.getValueText(o) + "'"));
    }
    else if (isStringInHeredocQuotable(o)) {
      replaceElement(o, PerlElementFactory.createString(myProject, "\"" + ElementManipulators.getValueText(o) + "\""));
    }
    else {
      super.visitStringBare(o);
    }
  }

  @Override
  public void visitHashIndex(@NotNull PsiPerlHashIndex o) {
    if (!myRange.contains(o.getTextRange())) {
      return;
    }

    processDerefExpressionIndex(o);
    super.visitHashIndex(o);
  }

  @Override
  public void visitArrayIndex(@NotNull PsiPerlArrayIndex o) {
    if (!myRange.contains(o.getTextRange())) {
      return;
    }
    processDerefExpressionIndex(o);
    super.visitArrayIndex(o);
  }

  @Override
  public void visitPerlCastExpression(@NotNull PerlCastExpression o) {
    if (!myRange.contains(o.getTextRange())) {
      return;
    }

    PsiElement parent = o.getParent();
    boolean isSimpleScalarCast = isSimpleScalarCast(o);

    if (o instanceof PsiPerlScalarCastExpr) {
      boolean isInsideHashOrArrayElement = parent instanceof PsiPerlArrayElement || parent instanceof PsiPerlHashElement;

      if (myPerlSettings.OPTIONAL_DEREFERENCE_HASHREF_ELEMENT == SUPPRESS &&
          isInsideHashOrArrayElement &&
          isSimpleScalarCast) // convert $$var{key} to $var->{key}
      {
        myFormattingOperations.add(new PerlFormattingScalarDerefExpand((PsiPerlScalarCastExpr)o));
      }
      else if (!isSimpleScalarCast && myPerlSettings.OPTIONAL_DEREFERENCE_SIMPLE == SUPPRESS)    // need to convert ${var} to $var
      {
        unwrapSimpleDereference(o);
      }
      else if (isSimpleScalarCast &&
               !isInsideHashOrArrayElement &&
               myPerlSettings.OPTIONAL_DEREFERENCE_SIMPLE == FORCE)    // need to convert $$var to ${$var}
      {
        wrapSimpleDereference(o);
      }
    }
    else // hash and array
    {
      if (!isSimpleScalarCast && myPerlSettings.OPTIONAL_DEREFERENCE_SIMPLE == SUPPRESS)    // need to convert ${$var} to $$var
      {
        unwrapSimpleDereference(o);
      }
      else if (isSimpleScalarCast && myPerlSettings.OPTIONAL_DEREFERENCE_SIMPLE == FORCE)    // need to convert $$var to ${$var}
      {
        wrapSimpleDereference(o);
      }
    }
    super.visitPerlCastExpression(o);
  }

  /**
   * Wrapping reference into braces
   *
   * @param o Cast expression
   */
  public void wrapSimpleDereference(PerlCastExpression o) {
    PsiElement referenceVariable = o.getLastChild();
    if (referenceVariable instanceof PsiPerlScalarVariable) {
      myFormattingOperations.add(new PerlFormattingSimpleDereferenceWrap(o, (PsiPerlScalarVariable)referenceVariable));
    }
  }

  public void unwrapSimpleDereference(PerlCastExpression o) {
    PsiElement closeBraceElement = o.getLastChild();

    if (closeBraceElement != null && closeBraceElement.getNode().getElementType() == RIGHT_BRACE) {
      PsiElement statementElement = PerlPsiUtil.getPrevSignificantSibling(closeBraceElement);
      if (statementElement instanceof PsiPerlStatement) {
        PsiElement openBraceElement = PerlPsiUtil.getPrevSignificantSibling(statementElement);
        if (openBraceElement != null && openBraceElement.getNode().getElementType() == LEFT_BRACE) {
          PsiElement referenceVariable = statementElement.getFirstChild();
          if (referenceVariable instanceof PsiPerlScalarVariable) {
            PsiElement optionalSemi = PerlPsiUtil.getPrevSignificantSibling(referenceVariable);
            if (optionalSemi == null || optionalSemi.getNode().getElementType() == SEMICOLON && optionalSemi.getNextSibling() == null) {
              myFormattingOperations.add(new PerlFormattingSimpleDereferenceUnwrap(o, (PsiPerlScalarVariable)referenceVariable));
            }
          }
        }
      }
    }
  }

  @Override
  public void visitDerefExpr(@NotNull PsiPerlDerefExpr o) {
    if (!myRange.contains(o.getTextRange())) {
      return;
    }
    if (myPerlSettings.OPTIONAL_DEREFERENCE_HASHREF_ELEMENT == FORCE) {
      PsiElement scalarVariableElement = o.getFirstChild();
      if (scalarVariableElement instanceof PsiPerlScalarVariable) {
        PsiElement derefElement = PerlPsiUtil.getNextSignificantSibling(scalarVariableElement);
        if (derefElement != null && derefElement.getNode().getElementType() == OPERATOR_DEREFERENCE) {
          PsiElement probableIndexElement = PerlPsiUtil.getNextSignificantSibling(derefElement);

          if (probableIndexElement instanceof PsiPerlHashIndex || probableIndexElement instanceof PsiPerlArrayIndex) {
            myFormattingOperations
              .add(new PerlFormattingScalarDerefCollapse((PsiPerlScalarVariable)scalarVariableElement, probableIndexElement));
          }
        }
      }
    }
    super.visitDerefExpr(o);
  }

  protected void processDerefExpressionIndex(PsiElement o) {
    PsiElement parent = o.getParent();
    PsiElement anchor = o;
    if (parent instanceof PsiPerlDerefExpr ||
        (((anchor = parent) instanceof PsiPerlHashElement || anchor instanceof PsiPerlArrayElement) &&
         anchor.getParent() instanceof PsiPerlDerefExpr
        )) {
      if (myPerlSettings.OPTIONAL_DEREFERENCE == FORCE) {
        PsiElement nextIndexElement = PerlPsiUtil.getNextSignificantSibling(anchor);
        if (nextIndexElement instanceof PsiPerlHashIndex || nextIndexElement instanceof PsiPerlArrayIndex) {
          insertElementAfter(PerlElementFactory.createDereference(myProject), anchor);
        }
      }
      else if (myPerlSettings.OPTIONAL_DEREFERENCE == SUPPRESS) {
        PsiElement potentialDereference = PerlPsiUtil.getNextSignificantSibling(anchor);
        if (potentialDereference != null && potentialDereference.getNode().getElementType() == OPERATOR_DEREFERENCE) {
          PsiElement nextIndexElement = PerlPsiUtil.getNextSignificantSibling(potentialDereference);
          if (nextIndexElement instanceof PsiPerlHashIndex || nextIndexElement instanceof PsiPerlArrayIndex) {
            removeElement(potentialDereference);
          }
        }
      }
    }
  }

  @Override
  public void visitStatementModifier(@NotNull PsiPerlStatementModifier o) {
    if (!myRange.contains(o.getTextRange())) {
      return;
    }
    PsiPerlExpr expression = PsiTreeUtil.getChildOfType(o, PsiPerlExpr.class);
    if (expression != null) {
      if (myPerlSettings.OPTIONAL_PARENTHESES == FORCE && !(expression instanceof PsiPerlParenthesisedExpr)) {
        myFormattingOperations.add(new PerlFormattingStatementModifierWrap(o));
      }
      else if (myPerlSettings.OPTIONAL_PARENTHESES == SUPPRESS && expression instanceof PsiPerlParenthesisedExpr) {
        myFormattingOperations.add(new PerlFormattingStatementModifierUnwrap(o));
      }
    }
    super.visitStatementModifier(o);
  }

  @Override
  public void visitNamespaceElement(@NotNull PerlNamespaceElement o) {
    if (!myRange.contains(o.getTextRange())) {
      return;
    }

    String elementContent = o.getNode().getText();

    if (myPerlSettings.MAIN_FORMAT == SUPPRESS && PerlPackageUtil.MAIN_PACKAGE_FULL.equals(elementContent)) {
      myFormattingOperations.add(new PerlFormattingReplaceWithText(o, PerlPackageUtil.PACKAGE_SEPARATOR));
    }
    else if (myPerlSettings.MAIN_FORMAT == FORCE && PerlPackageUtil.MAIN_PACKAGE_SHORT.equals(elementContent)) {
      myFormattingOperations.add(new PerlFormattingReplaceWithText(o, PerlPackageUtil.MAIN_PACKAGE_FULL));
    }
    else {
      super.visitNamespaceElement(o);
    }
  }

  protected void unquoteString(PerlString o) {
    replaceElement(o, PerlElementFactory.createBareString(myProject, ElementManipulators.getValueText(o)));
  }

  protected static boolean isStringSimple(PerlString o) {
    return o.getFirstChild().getNextSibling() == o.getLastChild().getPrevSibling() &&
           // we need this because lexer unable to properly parse utf
           ASCII_BARE_STRING_PATTERN.matcher(ElementManipulators.getValueText(o)).matches();
  }

  protected static boolean isStringSimpleIdentifier(PerlString o) {
    return o.getFirstChild().getNextSibling() == o.getLastChild().getPrevSibling() &&
           ASCII_IDENTIFIER_PATTERN.matcher(ElementManipulators.getValueText(o)).matches();
  }

  protected static boolean isCommaArrowAhead(PsiElement o) {
    PsiElement nextElement = PerlPsiUtil.getNextSignificantSibling(o);
    return nextElement != null && nextElement.getNode().getElementType() == FAT_COMMA;
  }

  protected static boolean isInHeredocOpener(PerlString o) {
    return o.getParent() instanceof PerlHeredocOpener;
  }

  protected static boolean isHashIndexKey(PsiElement o) {
    if (!(o.getParent() instanceof PsiPerlHashIndex)) {
      return false;
    }
    PsiElement prevSibling = o.getPrevSibling();
    PsiElement nextSibling = o.getNextSibling();
    return prevSibling != null && prevSibling.getNode().getElementType() == LEFT_BRACE &&
           nextSibling != null && nextSibling.getNode().getElementType() == RIGHT_BRACE;
  }
}
