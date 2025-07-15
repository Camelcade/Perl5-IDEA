// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.properties.PerlStatementsContainer;
import com.perl5.lang.perl.psi.properties.PerlConvertableCompoundSimple;
import com.perl5.lang.perl.psi.properties.PerlCompound;

public class PsiPerlVisitor extends PsiElementVisitor {

  public void visitAddExpr(@NotNull PsiPerlAddExpr o) {
    visitExpr(o);
  }

  public void visitAfterModifier(@NotNull PsiPerlAfterModifier o) {
    visitPerlMethodModifier(o);
  }

  public void visitAndExpr(@NotNull PsiPerlAndExpr o) {
    visitExpr(o);
  }

  public void visitAnnotationAbstract(@NotNull PsiPerlAnnotationAbstract o) {
    visitPerlAnnotation(o);
  }

  public void visitAnnotationDeprecated(@NotNull PsiPerlAnnotationDeprecated o) {
    visitPerlAnnotation(o);
  }

  public void visitAnnotationInject(@NotNull PsiPerlAnnotationInject o) {
    visitPerlAnnotation(o);
  }

  public void visitAnnotationMethod(@NotNull PsiPerlAnnotationMethod o) {
    visitPerlAnnotation(o);
  }

  public void visitAnnotationNoInject(@NotNull PsiPerlAnnotationNoInject o) {
    visitPerlAnnotation(o);
  }

  public void visitAnnotationNoinspection(@NotNull PsiPerlAnnotationNoinspection o) {
    visitPerlAnnotation(o);
  }

  public void visitAnnotationOverride(@NotNull PsiPerlAnnotationOverride o) {
    visitPerlAnnotation(o);
  }

  public void visitAnnotationReturns(@NotNull PsiPerlAnnotationReturns o) {
    visitPerlAnnotationWithValue(o);
    // visitPerlAnnotation(o);
  }

  public void visitAnnotationType(@NotNull PsiPerlAnnotationType o) {
    visitPerlAnnotationType(o);
    // visitPerlAnnotation(o);
  }

  public void visitAnnotationVariable(@NotNull PsiPerlAnnotationVariable o) {
    visitPerlAnnotationVariableElement(o);
    // visitPerlAnnotation(o);
  }

  public void visitAnonArray(@NotNull PsiPerlAnonArray o) {
    visitExpr(o);
  }

  public void visitAnonHash(@NotNull PsiPerlAnonHash o) {
    visitExpr(o);
  }

  public void visitAroundModifier(@NotNull PsiPerlAroundModifier o) {
    visitPerlMethodModifier(o);
  }

  public void visitAroundSignatureInvocants(@NotNull PsiPerlAroundSignatureInvocants o) {
    visitPsiElement(o);
  }

  public void visitArrayCastExpr(@NotNull PsiPerlArrayCastExpr o) {
    visitExpr(o);
    // visitPerlCastExpression(o);
  }

  public void visitArrayElement(@NotNull PsiPerlArrayElement o) {
    visitExpr(o);
  }

  public void visitArrayIndex(@NotNull PsiPerlArrayIndex o) {
    visitPsiElement(o);
  }

  public void visitArrayIndexVariable(@NotNull PsiPerlArrayIndexVariable o) {
    visitExpr(o);
    // visitPerlVariable(o);
  }

  public void visitArrayPopExpr(@NotNull PsiPerlArrayPopExpr o) {
    visitExpr(o);
    // visitPerlShiftPopExpr(o);
  }

  public void visitArrayPushExpr(@NotNull PsiPerlArrayPushExpr o) {
    visitExpr(o);
    // visitPerlUnshiftPushExpr(o);
  }

  public void visitArrayShiftExpr(@NotNull PsiPerlArrayShiftExpr o) {
    visitExpr(o);
    // visitPerlShiftPopExpr(o);
  }

  public void visitArraySlice(@NotNull PsiPerlArraySlice o) {
    visitExpr(o);
  }

  public void visitArrayUnshiftExpr(@NotNull PsiPerlArrayUnshiftExpr o) {
    visitExpr(o);
    // visitPerlUnshiftPushExpr(o);
  }

  public void visitArrayVariable(@NotNull PsiPerlArrayVariable o) {
    visitExpr(o);
    // visitPerlVariable(o);
  }

  public void visitArrayrefType(@NotNull PsiPerlArrayrefType o) {
    visitPsiElement(o);
  }

  public void visitAssignExpr(@NotNull PsiPerlAssignExpr o) {
    visitExpr(o);
    // visitPerlAssignExpression(o);
  }

  public void visitAttribute(@NotNull PsiPerlAttribute o) {
    visitPsiElement(o);
  }

  public void visitAttributes(@NotNull PsiPerlAttributes o) {
    visitPsiElement(o);
  }

  public void visitAugmentModifier(@NotNull PsiPerlAugmentModifier o) {
    visitPerlMethodModifier(o);
  }

  public void visitBeforeModifier(@NotNull PsiPerlBeforeModifier o) {
    visitPerlMethodModifier(o);
  }

  public void visitBitwiseAndExpr(@NotNull PsiPerlBitwiseAndExpr o) {
    visitExpr(o);
  }

  public void visitBitwiseOrXorExpr(@NotNull PsiPerlBitwiseOrXorExpr o) {
    visitExpr(o);
  }

  public void visitBlessExpr(@NotNull PsiPerlBlessExpr o) {
    visitExpr(o);
    // visitPerlBlessExpr(o);
  }

  public void visitBlock(@NotNull PsiPerlBlock o) {
    visitPerlBlock(o);
  }

  public void visitBlockArray(@NotNull PsiPerlBlockArray o) {
    visitBlock(o);
  }

  public void visitBlockBraceless(@NotNull PsiPerlBlockBraceless o) {
    visitBlock(o);
  }

  public void visitBlockCode(@NotNull PsiPerlBlockCode o) {
    visitBlock(o);
  }

  public void visitBlockCompound(@NotNull PsiPerlBlockCompound o) {
    visitPerlBlockCompound(o);
  }

  public void visitBlockGlob(@NotNull PsiPerlBlockGlob o) {
    visitBlock(o);
  }

  public void visitBlockHash(@NotNull PsiPerlBlockHash o) {
    visitBlock(o);
  }

  public void visitBlockScalar(@NotNull PsiPerlBlockScalar o) {
    visitBlock(o);
  }

  public void visitCallArguments(@NotNull PsiPerlCallArguments o) {
    visitPsiElement(o);
  }

  public void visitCaseCompound(@NotNull PsiPerlCaseCompound o) {
    visitPsiElement(o);
  }

  public void visitCaseCondition(@NotNull PsiPerlCaseCondition o) {
    visitPsiElement(o);
  }

  public void visitCaseDefault(@NotNull PsiPerlCaseDefault o) {
    visitPsiElement(o);
  }

  public void visitCatchCondition(@NotNull PsiPerlCatchCondition o) {
    visitPsiElement(o);
  }

  public void visitCatchExpr(@NotNull PsiPerlCatchExpr o) {
    visitExpr(o);
    // visitPerlCatchExpr(o);
  }

  public void visitCodeCastExpr(@NotNull PsiPerlCodeCastExpr o) {
    visitExpr(o);
    // visitPerlCastExpression(o);
  }

  public void visitCodeVariable(@NotNull PsiPerlCodeVariable o) {
    visitExpr(o);
    // visitPerlVariable(o);
  }

  public void visitCommaSequenceExpr(@NotNull PsiPerlCommaSequenceExpr o) {
    visitExpr(o);
  }

  public void visitCommentAnnotation(@NotNull PsiPerlCommentAnnotation o) {
    visitPsiElement(o);
  }

  public void visitCompareExpr(@NotNull PsiPerlCompareExpr o) {
    visitExpr(o);
  }

  public void visitCompileRegex(@NotNull PsiPerlCompileRegex o) {
    visitExpr(o);
    // visitPerlSimpleRegex(o);
  }

  public void visitCompositeAtomExpr(@NotNull PsiPerlCompositeAtomExpr o) {
    visitExpr(o);
  }

  public void visitConditionExpr(@NotNull PsiPerlConditionExpr o) {
    visitExpr(o);
    // visitPerlStatement(o);
  }

  public void visitConditionalBlock(@NotNull PsiPerlConditionalBlock o) {
    visitPerlStatementsContainerWithBlock(o);
  }

  public void visitContinuationExpr(@NotNull PsiPerlContinuationExpr o) {
    visitExpr(o);
    // visitPerlCatchExpr(o);
  }

  public void visitContinueBlock(@NotNull PsiPerlContinueBlock o) {
    visitPsiElement(o);
  }

  public void visitContinueExpr(@NotNull PsiPerlContinueExpr o) {
    visitExpr(o);
  }

  public void visitCustomAtomExpr(@NotNull PsiPerlCustomAtomExpr o) {
    visitExpr(o);
  }

  public void visitDefaultCompound(@NotNull PsiPerlDefaultCompound o) {
    visitPerlCompound(o);
  }

  public void visitDefinedExpr(@NotNull PsiPerlDefinedExpr o) {
    visitExpr(o);
    // visitPerlImplicitScalarExpr(o);
  }

  public void visitDeleteExpr(@NotNull PsiPerlDeleteExpr o) {
    visitExpr(o);
  }

  public void visitDerefExpr(@NotNull PsiPerlDerefExpr o) {
    visitExpr(o);
    // visitPerlDerefExpression(o);
  }

  public void visitDoBlockExpr(@NotNull PsiPerlDoBlockExpr o) {
    visitExpr(o);
    // visitPerlDoBlockExpr(o);
  }

  public void visitDoExpr(@NotNull PsiPerlDoExpr o) {
    visitExpr(o);
    // visitPerlDoExpr(o);
  }

  public void visitEachExpr(@NotNull PsiPerlEachExpr o) {
    visitExpr(o);
  }

  public void visitEqualExpr(@NotNull PsiPerlEqualExpr o) {
    visitExpr(o);
  }

  public void visitEscChar(@NotNull PsiPerlEscChar o) {
    visitPerlCharSubstitution(o);
  }

  public void visitEvalExpr(@NotNull PsiPerlEvalExpr o) {
    visitExpr(o);
    // visitPerlEvalExpr(o);
  }

  public void visitExceptExpr(@NotNull PsiPerlExceptExpr o) {
    visitExpr(o);
  }

  public void visitExitExpr(@NotNull PsiPerlExitExpr o) {
    visitExpr(o);
  }

  public void visitExpr(@NotNull PsiPerlExpr o) {
    visitPsiElement(o);
  }

  public void visitFileGlobExpr(@NotNull PsiPerlFileGlobExpr o) {
    visitExpr(o);
  }

  public void visitFileReadExpr(@NotNull PsiPerlFileReadExpr o) {
    visitExpr(o);
  }

  public void visitFinallyExpr(@NotNull PsiPerlFinallyExpr o) {
    visitExpr(o);
  }

  public void visitFlipflopExpr(@NotNull PsiPerlFlipflopExpr o) {
    visitExpr(o);
  }

  public void visitForCompound(@NotNull PsiPerlForCompound o) {
    visitPerlForCompound(o);
  }

  public void visitForCondition(@NotNull PsiPerlForCondition o) {
    visitPerlStatement(o);
  }

  public void visitForInit(@NotNull PsiPerlForInit o) {
    visitPerlStatement(o);
  }

  public void visitForMutator(@NotNull PsiPerlForMutator o) {
    visitPerlStatement(o);
  }

  public void visitForStatementModifier(@NotNull PsiPerlForStatementModifier o) {
    visitStatementModifier(o);
  }

  public void visitForeachCompound(@NotNull PsiPerlForeachCompound o) {
    visitPerlForeachCompound(o);
  }

  public void visitForeachIterator(@NotNull PsiPerlForeachIterator o) {
    visitPerlStatement(o);
  }

  public void visitFormatDefinition(@NotNull PsiPerlFormatDefinition o) {
    visitPsiElement(o);
  }

  public void visitFunExpr(@NotNull PsiPerlFunExpr o) {
    visitExpr(o);
    // visitPerlSubExpr(o);
  }

  public void visitFuncDefinition(@NotNull PsiPerlFuncDefinition o) {
    visitPerlSubDefinitionElement(o);
  }

  public void visitGivenCompound(@NotNull PsiPerlGivenCompound o) {
    visitPerlGivenCompound(o);
  }

  public void visitGlobCastExpr(@NotNull PsiPerlGlobCastExpr o) {
    visitExpr(o);
    // visitPerlCastExpression(o);
  }

  public void visitGlobSlot(@NotNull PsiPerlGlobSlot o) {
    visitExpr(o);
  }

  public void visitGlobVariable(@NotNull PsiPerlGlobVariable o) {
    visitExpr(o);
    // visitPerlGlobVariableElement(o);
  }

  public void visitGotoExpr(@NotNull PsiPerlGotoExpr o) {
    visitExpr(o);
  }

  public void visitGrepExpr(@NotNull PsiPerlGrepExpr o) {
    visitExpr(o);
    // visitPerlGrepExpr(o);
  }

  public void visitHashArraySlice(@NotNull PsiPerlHashArraySlice o) {
    visitExpr(o);
  }

  public void visitHashCastExpr(@NotNull PsiPerlHashCastExpr o) {
    visitExpr(o);
    // visitPerlCastExpression(o);
  }

  public void visitHashElement(@NotNull PsiPerlHashElement o) {
    visitExpr(o);
  }

  public void visitHashHashSlice(@NotNull PsiPerlHashHashSlice o) {
    visitExpr(o);
  }

  public void visitHashIndex(@NotNull PsiPerlHashIndex o) {
    visitPsiElement(o);
  }

  public void visitHashSlice(@NotNull PsiPerlHashSlice o) {
    visitExpr(o);
  }

  public void visitHashVariable(@NotNull PsiPerlHashVariable o) {
    visitExpr(o);
    // visitPerlVariable(o);
  }

  public void visitHashrefType(@NotNull PsiPerlHashrefType o) {
    visitPsiElement(o);
  }

  public void visitHeredoc(@NotNull PsiPerlHeredoc o) {
    visitPsiElement(o);
  }

  public void visitHeredocOpener(@NotNull PsiPerlHeredocOpener o) {
    visitExpr(o);
    // visitPerlHeredocOpener(o);
  }

  public void visitHeredocQq(@NotNull PsiPerlHeredocQq o) {
    visitPerlHeredocWithInterpolation(o);
  }

  public void visitHeredocQx(@NotNull PsiPerlHeredocQx o) {
    visitPerlHeredocWithInterpolation(o);
  }

  public void visitHexChar(@NotNull PsiPerlHexChar o) {
    visitPerlCharSubstitution(o);
  }

  public void visitIfCompound(@NotNull PsiPerlIfCompound o) {
    visitPerlIfUnlessCompound(o);
  }

  public void visitIfStatementModifier(@NotNull PsiPerlIfStatementModifier o) {
    visitStatementModifier(o);
  }

  public void visitIsaExpr(@NotNull PsiPerlIsaExpr o) {
    visitExpr(o);
  }

  public void visitKeysExpr(@NotNull PsiPerlKeysExpr o) {
    visitExpr(o);
  }

  public void visitLabelDeclaration(@NotNull PsiPerlLabelDeclaration o) {
    visitPerlLabelDeclaration(o);
  }

  public void visitLabelExpr(@NotNull PsiPerlLabelExpr o) {
    visitExpr(o);
  }

  public void visitLastExpr(@NotNull PsiPerlLastExpr o) {
    visitExpr(o);
    // visitPerlFlowControlExpr(o);
  }

  public void visitLpAndExpr(@NotNull PsiPerlLpAndExpr o) {
    visitExpr(o);
  }

  public void visitLpNotExpr(@NotNull PsiPerlLpNotExpr o) {
    visitExpr(o);
  }

  public void visitLpOrXorExpr(@NotNull PsiPerlLpOrXorExpr o) {
    visitExpr(o);
  }

  public void visitMapExpr(@NotNull PsiPerlMapExpr o) {
    visitExpr(o);
    // visitPerlMapExpr(o);
  }

  public void visitMatchRegex(@NotNull PsiPerlMatchRegex o) {
    visitExpr(o);
    // visitPerlSimpleRegex(o);
  }

  public void visitMethod(@NotNull PsiPerlMethod o) {
    visitPerlMethod(o);
  }

  public void visitMethodDefinition(@NotNull PsiPerlMethodDefinition o) {
    visitPerlMethodDefinition(o);
  }

  public void visitMethodExpr(@NotNull PsiPerlMethodExpr o) {
    visitExpr(o);
    // visitPerlSubExpr(o);
  }

  public void visitMethodSignatureInvocant(@NotNull PsiPerlMethodSignatureInvocant o) {
    visitPsiElement(o);
  }

  public void visitMulExpr(@NotNull PsiPerlMulExpr o) {
    visitExpr(o);
  }

  public void visitNamedBlock(@NotNull PsiPerlNamedBlock o) {
    visitPerlStatementsContainerWithBlock(o);
  }

  public void visitNamespaceContent(@NotNull PsiPerlNamespaceContent o) {
    visitPerlStatementsContainer(o);
  }

  public void visitNamespaceDefinition(@NotNull PsiPerlNamespaceDefinition o) {
    visitPerlNamespaceDefinitionWithIdentifier(o);
  }

  public void visitNextExpr(@NotNull PsiPerlNextExpr o) {
    visitExpr(o);
    // visitPerlFlowControlExpr(o);
  }

  public void visitNumberConstant(@NotNull PsiPerlNumberConstant o) {
    visitExpr(o);
  }

  public void visitNyiStatement(@NotNull PsiPerlNyiStatement o) {
    visitPsiElement(o);
  }

  public void visitOctChar(@NotNull PsiPerlOctChar o) {
    visitPerlCharSubstitution(o);
  }

  public void visitOrExpr(@NotNull PsiPerlOrExpr o) {
    visitExpr(o);
  }

  public void visitOtherwiseExpr(@NotNull PsiPerlOtherwiseExpr o) {
    visitExpr(o);
  }

  public void visitPackageExpr(@NotNull PsiPerlPackageExpr o) {
    visitExpr(o);
  }

  public void visitParenthesisedCallArguments(@NotNull PsiPerlParenthesisedCallArguments o) {
    visitCallArguments(o);
  }

  public void visitParenthesisedExpr(@NotNull PsiPerlParenthesisedExpr o) {
    visitExpr(o);
  }

  public void visitParsableStringUseVars(@NotNull PsiPerlParsableStringUseVars o) {
    visitPsiElement(o);
  }

  public void visitPerlHandleExpr(@NotNull PsiPerlPerlHandleExpr o) {
    visitExpr(o);
  }

  public void visitPerlRegex(@NotNull PsiPerlPerlRegex o) {
    visitPerlRegexPattern(o);
  }

  public void visitPerlRegexModifiers(@NotNull PsiPerlPerlRegexModifiers o) {
    visitPsiElement(o);
  }

  public void visitPostDerefArraySliceExpr(@NotNull PsiPerlPostDerefArraySliceExpr o) {
    visitExpr(o);
  }

  public void visitPostDerefExpr(@NotNull PsiPerlPostDerefExpr o) {
    visitExpr(o);
  }

  public void visitPostDerefGlobExpr(@NotNull PsiPerlPostDerefGlobExpr o) {
    visitExpr(o);
  }

  public void visitPostDerefHashSliceExpr(@NotNull PsiPerlPostDerefHashSliceExpr o) {
    visitExpr(o);
  }

  public void visitPowExpr(@NotNull PsiPerlPowExpr o) {
    visitExpr(o);
  }

  public void visitPrefPpExpr(@NotNull PsiPerlPrefPpExpr o) {
    visitExpr(o);
  }

  public void visitPrefixUnaryExpr(@NotNull PsiPerlPrefixUnaryExpr o) {
    visitExpr(o);
  }

  public void visitPrintExpr(@NotNull PsiPerlPrintExpr o) {
    visitExpr(o);
  }

  public void visitRedoExpr(@NotNull PsiPerlRedoExpr o) {
    visitExpr(o);
    // visitPerlFlowControlExpr(o);
  }

  public void visitRefExpr(@NotNull PsiPerlRefExpr o) {
    visitExpr(o);
  }

  public void visitRegexExpr(@NotNull PsiPerlRegexExpr o) {
    visitExpr(o);
  }

  public void visitRegexReplacement(@NotNull PsiPerlRegexReplacement o) {
    visitPerlRegexReplacement(o);
  }

  public void visitReplacementRegex(@NotNull PsiPerlReplacementRegex o) {
    visitExpr(o);
    // visitPerlReplacementRegex(o);
  }

  public void visitRequireExpr(@NotNull PsiPerlRequireExpr o) {
    visitExpr(o);
    // visitPerlRequireExpr(o);
  }

  public void visitReturnExpr(@NotNull PsiPerlReturnExpr o) {
    visitExpr(o);
    // visitPerlReturnExpr(o);
  }

  public void visitScalarCall(@NotNull PsiPerlScalarCall o) {
    visitPsiElement(o);
  }

  public void visitScalarCastExpr(@NotNull PsiPerlScalarCastExpr o) {
    visitExpr(o);
    // visitPerlCastExpression(o);
  }

  public void visitScalarExpr(@NotNull PsiPerlScalarExpr o) {
    visitExpr(o);
  }

  public void visitScalarIndexCastExpr(@NotNull PsiPerlScalarIndexCastExpr o) {
    visitExpr(o);
    // visitPerlCastExpression(o);
  }

  public void visitScalarVariable(@NotNull PsiPerlScalarVariable o) {
    visitExpr(o);
    // visitPerlVariable(o);
  }

  public void visitShiftExpr(@NotNull PsiPerlShiftExpr o) {
    visitExpr(o);
  }

  public void visitSignatureContent(@NotNull PsiPerlSignatureContent o) {
    visitPerlStatement(o);
  }

  public void visitSignatureElement(@NotNull PsiPerlSignatureElement o) {
    visitPerlSignatureElement(o);
  }

  public void visitSortExpr(@NotNull PsiPerlSortExpr o) {
    visitExpr(o);
    // visitPerlSortExpr(o);
  }

  public void visitSpliceExpr(@NotNull PsiPerlSpliceExpr o) {
    visitExpr(o);
  }

  public void visitStatement(@NotNull PsiPerlStatement o) {
    visitPsiElement(o);
  }

  public void visitStatementModifier(@NotNull PsiPerlStatementModifier o) {
    visitPerlStatementModifier(o);
  }

  public void visitStringBare(@NotNull PsiPerlStringBare o) {
    visitExpr(o);
    // visitPerlString(o);
  }

  public void visitStringDq(@NotNull PsiPerlStringDq o) {
    visitExpr(o);
    // visitPerlStringWithInterpolation(o);
  }

  public void visitStringList(@NotNull PsiPerlStringList o) {
    visitExpr(o);
  }

  public void visitStringSq(@NotNull PsiPerlStringSq o) {
    visitExpr(o);
    // visitPerlString(o);
  }

  public void visitStringXq(@NotNull PsiPerlStringXq o) {
    visitExpr(o);
    // visitPerlStringWithInterpolation(o);
  }

  public void visitSubCall(@NotNull PsiPerlSubCall o) {
    visitExpr(o);
    // visitPerlMethodContainer(o);
  }

  public void visitSubDeclaration(@NotNull PsiPerlSubDeclaration o) {
    visitStatement(o);
    // visitPerlSubDeclarationElement(o);
  }

  public void visitSubDefinition(@NotNull PsiPerlSubDefinition o) {
    visitPerlSubDefinitionElement(o);
  }

  public void visitSubExpr(@NotNull PsiPerlSubExpr o) {
    visitExpr(o);
    // visitPerlSubExpr(o);
  }

  public void visitSubSignatureElementIgnore(@NotNull PsiPerlSubSignatureElementIgnore o) {
    visitPsiElement(o);
  }

  public void visitSuffPpExpr(@NotNull PsiPerlSuffPpExpr o) {
    visitExpr(o);
  }

  public void visitSwitchCompound(@NotNull PsiPerlSwitchCompound o) {
    visitPsiElement(o);
  }

  public void visitSwitchCondition(@NotNull PsiPerlSwitchCondition o) {
    visitPsiElement(o);
  }

  public void visitTagScalar(@NotNull PsiPerlTagScalar o) {
    visitExpr(o);
  }

  public void visitTernaryExpr(@NotNull PsiPerlTernaryExpr o) {
    visitExpr(o);
  }

  public void visitTrModifiers(@NotNull PsiPerlTrModifiers o) {
    visitPsiElement(o);
  }

  public void visitTrRegex(@NotNull PsiPerlTrRegex o) {
    visitExpr(o);
  }

  public void visitTrReplacementlist(@NotNull PsiPerlTrReplacementlist o) {
    visitPsiElement(o);
  }

  public void visitTrSearchlist(@NotNull PsiPerlTrSearchlist o) {
    visitPsiElement(o);
  }

  public void visitTryExpr(@NotNull PsiPerlTryExpr o) {
    visitExpr(o);
    // visitPerlTryExpr(o);
  }

  public void visitTrycatchCompound(@NotNull PsiPerlTrycatchCompound o) {
    visitPerlTryCatchCompound(o);
  }

  public void visitTrycatchExpr(@NotNull PsiPerlTrycatchExpr o) {
    visitExpr(o);
    // visitPerlTryCatchExpr(o);
  }

  public void visitTypeConstraints(@NotNull PsiPerlTypeConstraints o) {
    visitPsiElement(o);
  }

  public void visitTypeSpecifier(@NotNull PsiPerlTypeSpecifier o) {
    visitPsiElement(o);
  }

  public void visitUnconditionalBlock(@NotNull PsiPerlUnconditionalBlock o) {
    visitPerlStatementsContainerWithBlock(o);
  }

  public void visitUndefExpr(@NotNull PsiPerlUndefExpr o) {
    visitExpr(o);
  }

  public void visitUnicodeChar(@NotNull PsiPerlUnicodeChar o) {
    visitPerlCharSubstitution(o);
  }

  public void visitUnlessCompound(@NotNull PsiPerlUnlessCompound o) {
    visitPerlIfUnlessCompound(o);
  }

  public void visitUnlessStatementModifier(@NotNull PsiPerlUnlessStatementModifier o) {
    visitStatementModifier(o);
  }

  public void visitUntilCompound(@NotNull PsiPerlUntilCompound o) {
    visitPerlWhileUntilCompound(o);
  }

  public void visitUntilStatementModifier(@NotNull PsiPerlUntilStatementModifier o) {
    visitStatementModifier(o);
  }

  public void visitValuesExpr(@NotNull PsiPerlValuesExpr o) {
    visitExpr(o);
  }

  public void visitVariableDeclarationElement(@NotNull PsiPerlVariableDeclarationElement o) {
    visitPerlVariableDeclarationElement(o);
  }

  public void visitVariableDeclarationGlobal(@NotNull PsiPerlVariableDeclarationGlobal o) {
    visitExpr(o);
    // visitPerlVariableDeclarationExpr(o);
  }

  public void visitVariableDeclarationLexical(@NotNull PsiPerlVariableDeclarationLexical o) {
    visitExpr(o);
    // visitPerlLexicalVariableDeclarationMarker(o);
    // visitPerlVariableDeclarationExpr(o);
  }

  public void visitVariableDeclarationLocal(@NotNull PsiPerlVariableDeclarationLocal o) {
    visitExpr(o);
    // visitPerlVariableDeclarationExpr(o);
  }

  public void visitWantarrayExpr(@NotNull PsiPerlWantarrayExpr o) {
    visitExpr(o);
  }

  public void visitWhenCompound(@NotNull PsiPerlWhenCompound o) {
    visitPerlConvertableCompoundSimple(o);
  }

  public void visitWhenStatementModifier(@NotNull PsiPerlWhenStatementModifier o) {
    visitStatementModifier(o);
  }

  public void visitWhileCompound(@NotNull PsiPerlWhileCompound o) {
    visitPerlWhileUntilCompound(o);
  }

  public void visitWhileStatementModifier(@NotNull PsiPerlWhileStatementModifier o) {
    visitStatementModifier(o);
  }

  public void visitPerlAnnotation(@NotNull PerlAnnotation o) {
    visitElement(o);
  }

  public void visitPerlAnnotationType(@NotNull PerlAnnotationType o) {
    visitElement(o);
  }

  public void visitPerlAnnotationVariableElement(@NotNull PerlAnnotationVariableElement o) {
    visitElement(o);
  }

  public void visitPerlAnnotationWithValue(@NotNull PerlAnnotationWithValue o) {
    visitElement(o);
  }

  public void visitPerlBlock(@NotNull PerlBlock o) {
    visitElement(o);
  }

  public void visitPerlBlockCompound(@NotNull PerlBlockCompound o) {
    visitElement(o);
  }

  public void visitPerlCharSubstitution(@NotNull PerlCharSubstitution o) {
    visitElement(o);
  }

  public void visitPerlForCompound(@NotNull PerlForCompound o) {
    visitElement(o);
  }

  public void visitPerlForeachCompound(@NotNull PerlForeachCompound o) {
    visitElement(o);
  }

  public void visitPerlGivenCompound(@NotNull PerlGivenCompound o) {
    visitElement(o);
  }

  public void visitPerlHeredocWithInterpolation(@NotNull PerlHeredocWithInterpolation o) {
    visitElement(o);
  }

  public void visitPerlIfUnlessCompound(@NotNull PerlIfUnlessCompound o) {
    visitElement(o);
  }

  public void visitPerlLabelDeclaration(@NotNull PerlLabelDeclaration o) {
    visitElement(o);
  }

  public void visitPerlMethod(@NotNull PerlMethod o) {
    visitElement(o);
  }

  public void visitPerlMethodDefinition(@NotNull PerlMethodDefinition o) {
    visitElement(o);
  }

  public void visitPerlMethodModifier(@NotNull PerlMethodModifier o) {
    visitElement(o);
  }

  public void visitPerlNamespaceDefinitionWithIdentifier(@NotNull PerlNamespaceDefinitionWithIdentifier o) {
    visitElement(o);
  }

  public void visitPerlRegexPattern(@NotNull PerlRegexPattern o) {
    visitElement(o);
  }

  public void visitPerlRegexReplacement(@NotNull PerlRegexReplacement o) {
    visitElement(o);
  }

  public void visitPerlSignatureElement(@NotNull PerlSignatureElement o) {
    visitElement(o);
  }

  public void visitPerlStatement(@NotNull PerlStatement o) {
    visitElement(o);
  }

  public void visitPerlStatementModifier(@NotNull PerlStatementModifier o) {
    visitElement(o);
  }

  public void visitPerlStatementsContainerWithBlock(@NotNull PerlStatementsContainerWithBlock o) {
    visitElement(o);
  }

  public void visitPerlSubDefinitionElement(@NotNull PerlSubDefinitionElement o) {
    visitElement(o);
  }

  public void visitPerlTryCatchCompound(@NotNull PerlTryCatchCompound o) {
    visitElement(o);
  }

  public void visitPerlVariableDeclarationElement(@NotNull PerlVariableDeclarationElement o) {
    visitElement(o);
  }

  public void visitPerlWhileUntilCompound(@NotNull PerlWhileUntilCompound o) {
    visitElement(o);
  }

  public void visitPerlCompound(@NotNull PerlCompound o) {
    visitElement(o);
  }

  public void visitPerlConvertableCompoundSimple(@NotNull PerlConvertableCompoundSimple o) {
    visitElement(o);
  }

  public void visitPerlStatementsContainer(@NotNull PerlStatementsContainer o) {
    visitElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
