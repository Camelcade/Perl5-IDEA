/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea;

import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlNoStatementElement;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;


public final class PerlElementPatterns {
  private PerlElementPatterns() {
  }

  public static final PsiElementPattern.Capture<PsiElement> WHITE_SPACE_AND_COMMENTS = psiElement().whitespaceCommentOrError();

  public static final PsiElementPattern.Capture<PsiElement> LABEL_EXPR_PATTERN = psiElement(LABEL_EXPR);
  public static final PsiElementPattern.Capture<PsiElement> LABEL_PATTERN = psiElement().withParent(LABEL_EXPR_PATTERN);
  public static final PsiElementPattern.Capture<PsiElement> LABEL_DECLARATION_PATTERN = psiElement().withParent(PerlLabelDeclaration.class);
  public static final PsiElementPattern.Capture<PsiElement> LABEL_IN_GOTO_PATTERN = LABEL_PATTERN.withSuperParent(2, PsiPerlGotoExpr.class);
  public static final PsiElementPattern.Capture<PsiElement> LABEL_IN_NEXT_LAST_REDO_PATTERN = LABEL_PATTERN.andOr(
    psiElement().withSuperParent(2, PsiPerlNextExpr.class),
    psiElement().withSuperParent(2, PsiPerlLastExpr.class),
    psiElement().withSuperParent(2, PsiPerlRedoExpr.class)
  );

  public static final PsiElementPattern.Capture<PerlStringContentElement> STRING_CONTENT_PATTERN =
    psiElement(PerlStringContentElement.class);

  public static final PsiElementPattern.Capture<PerlStringContentElement> STRING_CONTENT_IN_STRING_BARE =
    STRING_CONTENT_PATTERN.inside(PsiPerlStringBare.class);
  public static final PsiElementPattern.Capture<PerlStringContentElement> STRING_CONTENT_IN_SQ_STRING_BEGIN =
    STRING_CONTENT_PATTERN.afterLeaf(psiElement(QUOTE_SINGLE_OPEN)).inside(PsiPerlStringSq.class);
  public static final PsiElementPattern.Capture<PerlStringContentElement> STRING_CONTENT_IN_DQ_STRING_BEGIN =
    STRING_CONTENT_PATTERN.afterLeaf(psiElement(QUOTE_DOUBLE_OPEN)).inside(PsiPerlStringDq.class);
  public static final PsiElementPattern.Capture<PerlStringContentElement> STRING_CONTENT_IN_QW_STRING_LIST =
    STRING_CONTENT_PATTERN.inside(PsiPerlStringList.class);

  public static final PsiElementPattern.Capture<PerlStringContentElement> STRING_CONTENT_IN_LIST_OR_STRING_START =
    STRING_CONTENT_PATTERN.andOr(
      STRING_CONTENT_IN_QW_STRING_LIST,
      STRING_CONTENT_IN_SQ_STRING_BEGIN,
      STRING_CONTENT_IN_DQ_STRING_BEGIN
    );

  public static final PsiElementPattern.Capture<PerlStringContentElement> STRING_CONTENT_IN_HEREDOC_OPENER_PATTERN =
    STRING_CONTENT_PATTERN.withParent(
      psiElement(PerlString.class).withParent(psiElement(PerlHeredocOpener.class))
    );

  public static final PsiElementPattern.Capture<PerlStringContentElement> SIMPLE_HASH_INDEX =
    STRING_CONTENT_PATTERN.withSuperParent(2, PsiPerlHashIndex.class).andOr(
      STRING_CONTENT_IN_STRING_BARE,
      STRING_CONTENT_IN_SQ_STRING_BEGIN,
      STRING_CONTENT_IN_DQ_STRING_BEGIN,
      STRING_CONTENT_IN_QW_STRING_LIST
    );

  public static final PsiElementPattern.Capture<PerlNamespaceElement> NAMESPACE_NAME_PATTERN = psiElement(PerlNamespaceElement.class);
  public static final PsiElementPattern.Capture<PerlUseStatementElement> USE_STATEMENT_PATTERN = psiElement(PerlUseStatementElement.class);
  public static final PsiElementPattern.Capture<PerlNoStatementElement> NO_STATEMENT_PATTERN = psiElement(PerlNoStatementElement.class);
  public static final PsiElementPattern.Capture<PsiPerlRequireExpr> REQUIRE_EXPR_PATTERN = psiElement(PsiPerlRequireExpr.class);
  public static final PsiElementPattern.Capture<PerlNamespaceDefinitionElement> NAMESPACE_DEFINITION_PATTERN =
    psiElement(PerlNamespaceDefinitionElement.class);

  public static final PsiElementPattern.Capture<PerlNamespaceElement> NAMESPACE_IN_USE_PATTERN =
    NAMESPACE_NAME_PATTERN.andOr(
      psiElement().withParent(USE_STATEMENT_PATTERN),
      psiElement().withParent(NO_STATEMENT_PATTERN),
      psiElement().withParent(REQUIRE_EXPR_PATTERN)
    );

  public static final PsiElementPattern.Capture<PerlNamespaceElement> NAMESPACE_IN_ANNOTATION_PATTERN =
    NAMESPACE_NAME_PATTERN.inside(psiElement(PerlAnnotationWithValue.class));

  public static final PsiElementPattern.Capture<PerlNamespaceElement> NAMESPACE_IN_DEFINITION_PATTERN =
    NAMESPACE_NAME_PATTERN.withParent(NAMESPACE_DEFINITION_PATTERN);

  public static final PsiElementPattern.Capture<PerlNamespaceElement> NAMESPACE_IN_VARIABLE_DECLARATION_PATTERN =
    NAMESPACE_NAME_PATTERN.withParent(psiElement(PerlVariableDeclarationExpr.class));

  public static final PsiElementPattern.Capture<PerlSubNameElement> SUB_NAME_PATTERN = psiElement(PerlSubNameElement.class);
  public static final PsiElementPattern.Capture<PsiElement> HANDLE_PATTERN = psiElement(HANDLE);

  public static final PsiElementPattern.Capture<PsiPerlMethod> METHOD_PATTERN = psiElement(PsiPerlMethod.class);
  public static final PsiElementPattern.Capture<PsiElement> IN_OBJECT_CALL_PATTERN = psiElement().withParent(
    psiElement(SUB_CALL).withParent(psiElement(DEREF_EXPR))
  );

  public static final PsiElementPattern.Capture<PsiElement> IN_STATIC_METHOD_PATTERN =
    psiElement().withParent(METHOD_PATTERN.andNot(IN_OBJECT_CALL_PATTERN));
  public static final PsiElementPattern.Capture<PsiElement> IN_OBJECT_METHOD_PATTERN =
    psiElement().withParent(METHOD_PATTERN.and(IN_OBJECT_CALL_PATTERN));

  public static final PsiElementPattern.Capture<PerlVariable> VARIABLE_PATTERN = psiElement(PerlVariable.class);

  public static final PsiElementPattern.Capture<PerlAnnotationVariableElement> ANNOTATION_VARIABLE_PATTERN =
    psiElement(PerlAnnotationVariableElement.class);

  public static final PsiElementPattern.Capture<PsiElement> ANNOTATION_VARIABLE_NAME_PATTERN =
    psiElement().andOr(
      psiElement(ANNOTATION_SCALAR),
      psiElement(ANNOTATION_ARRAY),
      psiElement(ANNOTATION_HASH)
    );

  public static final PsiElementPattern.Capture<PerlVariableNameElement> VARIABLE_NAME_PATTERN = psiElement(PerlVariableNameElement.class);
  public static final PsiElementPattern.Capture<PerlVariableNameElement> VARIABLE_NAME_IN_DECLARATION_PATTERN =
    VARIABLE_NAME_PATTERN.withParent(
      VARIABLE_PATTERN.withParent(psiElement(PerlVariableDeclarationElement.class))
    );
  public static final PsiElementPattern.Capture<PerlVariableNameElement> VARIABLE_NAME_IN_LOCAL_DECLARATION_PATTERN =
    VARIABLE_NAME_PATTERN.inside(true, psiElement(PsiPerlVariableDeclarationLocal.class));

  public static final PsiElementPattern.Capture<PsiElement> UNKNOWN_ANNOTATION_PATTERN = psiElement(ANNOTATION_UNKNOWN_KEY);
  public static final PsiElementPattern.Capture<PsiElement> STRING_CHAR_NAME_PATTERN = psiElement(STRING_CHAR_NAME);


  // @ISA = ()
  public static final PsiElementPattern.Capture<PsiPerlArrayVariable> ISA_VARIABLE = psiElement(PsiPerlArrayVariable.class);
  public static final PsiElementPattern.Capture<PsiPerlVariableDeclarationGlobal> ISA_DECLARATION =
    psiElement(PsiPerlVariableDeclarationGlobal.class)
      .withChild(
        psiElement(PerlVariableDeclarationElement.class).withFirstChild(ISA_VARIABLE)
      );

  public static final PsiElementPattern.Capture<PsiPerlStatement> ISA_ASSIGN_STATEMENT = psiElement(PsiPerlStatement.class).withFirstChild(
    psiElement(PsiPerlAssignExpr.class).andOr(
      psiElement().withFirstChild(ISA_VARIABLE),
      psiElement().withFirstChild(ISA_DECLARATION)
    )
  );

  // @EXPORTER scanner
  // this one is to speed up the scanning
  public static final PsiElementPattern.Capture<PsiPerlStatement> ASSIGN_STATEMENT = psiElement(PsiPerlStatement.class).withFirstChild(
    psiElement(PsiPerlAssignExpr.class)
  );

  public static final PsiElementPattern.Capture<PsiPerlArrayVariable> EXPORT_VARIABLE =
    psiElement(PsiPerlArrayVariable.class).withText("@EXPORT");
  public static final PsiElementPattern.Capture<PsiPerlVariableDeclarationGlobal> EXPORT_DECLARATION =
    psiElement(PsiPerlVariableDeclarationGlobal.class)
      .withChild(
        psiElement(PerlVariableDeclarationElement.class).withFirstChild(EXPORT_VARIABLE)
      );
  public static final PsiElementPattern.Capture<PsiPerlStatement> EXPORT_ASSIGN_STATEMENT =
    psiElement(PsiPerlStatement.class).withFirstChild(
      psiElement(PsiPerlAssignExpr.class).andOr(
        psiElement().withFirstChild(EXPORT_VARIABLE),
        psiElement().withFirstChild(EXPORT_DECLARATION)
      )
    );

  public static final PsiElementPattern.Capture<PsiPerlArrayVariable> EXPORT_OK_VARIABLE =
    psiElement(PsiPerlArrayVariable.class).withText("@EXPORT_OK");
  public static final PsiElementPattern.Capture<PsiPerlVariableDeclarationGlobal> EXPORT_OK_DECLARATION =
    psiElement(PsiPerlVariableDeclarationGlobal.class)
      .withChild(
        psiElement(PerlVariableDeclarationElement.class).withFirstChild(EXPORT_OK_VARIABLE)
      );

  public static final PsiElementPattern.Capture<PsiPerlStatement> EXPORT_OK_ASSIGN_STATEMENT =
    psiElement(PsiPerlStatement.class).withFirstChild(
      psiElement(PsiPerlAssignExpr.class).andOr(
        psiElement().withFirstChild(EXPORT_OK_VARIABLE),
        psiElement().withFirstChild(EXPORT_OK_DECLARATION)
      )
    );

  // @EXPORT = ();
  public static final PsiElementPattern.Capture<PsiPerlArrayVariable> EXPORT_ARRAY_VARIABLE_PATTERN =
    psiElement(PsiPerlArrayVariable.class).andOr(
      psiElement().withText("@EXPORT"),
      psiElement().withText("@EXPORT_OK")
    );

  public static final PsiElementPattern.Capture<PsiPerlVariableDeclarationGlobal> EXPORT_ARRAY_VARIABLE_DECLARATION =
    psiElement(PsiPerlVariableDeclarationGlobal.class).withChild(
      psiElement(PerlVariableDeclarationElement.class).withChild(EXPORT_ARRAY_VARIABLE_PATTERN)
    );


  public static final PsiElementPattern.Capture<PsiElement> EXPORT_ASSIGNMENT =
    psiElement()
      .afterLeafSkipping(WHITE_SPACE_AND_COMMENTS, psiElement(OPERATOR_ASSIGN))
      .withParent(
        psiElement(PsiPerlAssignExpr.class).withFirstChild(
          psiElement().andOr(
            EXPORT_ARRAY_VARIABLE_PATTERN,
            EXPORT_ARRAY_VARIABLE_DECLARATION
          )
        )
      );

  public static final PsiElementPattern.Capture<PsiPerlParenthesisedExpr> EXPORT_ASSIGNMENT_PARENTHESISED =
    psiElement(PsiPerlParenthesisedExpr.class).and(EXPORT_ASSIGNMENT);

  public static final PsiElementPattern.Capture<PsiElement> EXPORT_ASSIGNED_STRING_CONTENT = psiElement().andOr(
    STRING_CONTENT_IN_QW_STRING_LIST.andOr(
      psiElement().withSuperParent(2, EXPORT_ASSIGNMENT),
      psiElement().withSuperParent(3, EXPORT_ASSIGNMENT_PARENTHESISED)

    ),
    STRING_CONTENT_PATTERN.withParent(
      psiElement(PerlString.class).andOr(
        psiElement().withParent(psiElement(PsiPerlCommaSequenceExpr.class).withParent(EXPORT_ASSIGNMENT_PARENTHESISED)),
        psiElement().withParent(EXPORT_ASSIGNMENT_PARENTHESISED)
      )
    )
  );

  public static final PsiElementPattern.Capture<PerlStringContentElement> USE_PARAMETERS_PATTERN = STRING_CONTENT_PATTERN.andOr(
    psiElement().inside(USE_STATEMENT_PATTERN),
    psiElement().inside(NO_STATEMENT_PATTERN)
  );

  // pattern for shift;
  public static final PsiElementPattern.Capture<PsiElement> SHIFT_PATTERN = psiElement(ARRAY_SHIFT_EXPR);

  public static final PsiElementPattern.Capture<PsiPerlArrayVariable> ALL_ARGUMENTS_PATTERN =
    psiElement(PsiPerlArrayVariable.class).withText("@_");
  public static final PsiElementPattern.Capture<PsiPerlArrayElement> ALL_ARGUMENTS_ELEMENT_PATTERN =
    psiElement(PsiPerlArrayElement.class)
      .withFirstChild(psiElement(PsiPerlScalarVariable.class).withText("$_"));

  public static final PsiElementPattern.Capture<PsiElement> TAILING_SHIFT_PATTERN = SHIFT_PATTERN.beforeLeaf(psiElement(SEMICOLON));


  public static final PsiElementPattern.Capture<PsiPerlStatement> EMPTY_SHIFT_STATEMENT_PATTERN =
    psiElement(PsiPerlStatement.class).withFirstChild(TAILING_SHIFT_PATTERN);


  public static final PsiElementPattern.Capture<PsiPerlStatement> DECLARATION_ASSIGNING_PATTERN =
    psiElement(PsiPerlStatement.class).withFirstChild(
      psiElement(PerlAssignExpression.class).withFirstChild(
        psiElement(PerlVariableDeclarationExpr.class)
      ));

}
