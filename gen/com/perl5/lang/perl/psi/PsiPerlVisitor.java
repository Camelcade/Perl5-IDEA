// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import org.jetbrains.annotations.NotNull;

public class PsiPerlVisitor extends PsiElementVisitor
{

	public void visitAddExpr(@NotNull PsiPerlAddExpr o)
	{
		visitExpr(o);
	}

	public void visitAndExpr(@NotNull PsiPerlAndExpr o)
	{
		visitExpr(o);
	}

	public void visitAnnotation(@NotNull PsiPerlAnnotation o)
	{
		visitPsiElement(o);
	}

	public void visitAnnotationAbstract(@NotNull PsiPerlAnnotationAbstract o)
	{
		visitAnnotation(o);
	}

	public void visitAnnotationDeprecated(@NotNull PsiPerlAnnotationDeprecated o)
	{
		visitAnnotation(o);
	}

	public void visitAnnotationIncomplete(@NotNull PsiPerlAnnotationIncomplete o)
	{
		visitAnnotation(o);
	}

	public void visitAnnotationMethod(@NotNull PsiPerlAnnotationMethod o)
	{
		visitAnnotation(o);
	}

	public void visitAnnotationOverride(@NotNull PsiPerlAnnotationOverride o)
	{
		visitAnnotation(o);
	}

	public void visitAnnotationReturnsArrayref(@NotNull PsiPerlAnnotationReturnsArrayref o)
	{
		visitAnnotation(o);
		// visitPerlNamespaceElementContainer(o);
	}

	public void visitAnnotationReturnsHashref(@NotNull PsiPerlAnnotationReturnsHashref o)
	{
		visitAnnotation(o);
		// visitPerlNamespaceElementContainer(o);
	}

	public void visitAnnotationReturnsRef(@NotNull PsiPerlAnnotationReturnsRef o)
	{
		visitAnnotation(o);
		// visitPerlNamespaceElementContainer(o);
	}

	public void visitAnnotationUnknown(@NotNull PsiPerlAnnotationUnknown o)
	{
		visitAnnotation(o);
	}

	public void visitAnonArray(@NotNull PsiPerlAnonArray o)
	{
		visitExpr(o);
	}

	public void visitAnonArrayElement(@NotNull PsiPerlAnonArrayElement o)
	{
		visitExpr(o);
	}

	public void visitAnonHash(@NotNull PsiPerlAnonHash o)
	{
		visitExpr(o);
	}

	public void visitAnonSub(@NotNull PsiPerlAnonSub o)
	{
		visitExpr(o);
	}

	public void visitArrayArraySlice(@NotNull PsiPerlArrayArraySlice o)
	{
		visitExpr(o);
	}

	public void visitArrayCastExpr(@NotNull PsiPerlArrayCastExpr o)
	{
		visitExpr(o);
		// visitPerlCastExpression(o);
	}

	public void visitArrayHashSlice(@NotNull PsiPerlArrayHashSlice o)
	{
		visitExpr(o);
	}

	public void visitArrayIndex(@NotNull PsiPerlArrayIndex o)
	{
		visitPsiElement(o);
	}

	public void visitArrayIndexVariable(@NotNull PsiPerlArrayIndexVariable o)
	{
		visitExpr(o);
		// visitPerlVariable(o);
	}

	public void visitArrayVariable(@NotNull PsiPerlArrayVariable o)
	{
		visitExpr(o);
		// visitPerlVariable(o);
	}

	public void visitAssignExpr(@NotNull PsiPerlAssignExpr o)
	{
		visitExpr(o);
	}

	public void visitAttribute(@NotNull PsiPerlAttribute o)
	{
		visitPsiElement(o);
	}

	public void visitBitwiseAndExpr(@NotNull PsiPerlBitwiseAndExpr o)
	{
		visitExpr(o);
	}

	public void visitBitwiseOrXorExpr(@NotNull PsiPerlBitwiseOrXorExpr o)
	{
		visitExpr(o);
	}

	public void visitBlock(@NotNull PsiPerlBlock o)
	{
		visitPerlLexicalScope(o);
	}

	public void visitCallArguments(@NotNull PsiPerlCallArguments o)
	{
		visitPsiElement(o);
	}

	public void visitCodeCastExpr(@NotNull PsiPerlCodeCastExpr o)
	{
		visitExpr(o);
		// visitPerlCastExpression(o);
	}

	public void visitCodeVariable(@NotNull PsiPerlCodeVariable o)
	{
		visitExpr(o);
		// visitPerlVariable(o);
	}

	public void visitCommaSequenceExpr(@NotNull PsiPerlCommaSequenceExpr o)
	{
		visitExpr(o);
	}

	public void visitCompareExpr(@NotNull PsiPerlCompareExpr o)
	{
		visitExpr(o);
	}

	public void visitCompileRegex(@NotNull PsiPerlCompileRegex o)
	{
		visitExpr(o);
	}

	public void visitConditionStatement(@NotNull PsiPerlConditionStatement o)
	{
		visitStatement(o);
	}

	public void visitConditionStatementWhile(@NotNull PsiPerlConditionStatementWhile o)
	{
		visitStatement(o);
	}

	public void visitConditionalBlock(@NotNull PsiPerlConditionalBlock o)
	{
		visitPsiElement(o);
	}

	public void visitConditionalBlockWhile(@NotNull PsiPerlConditionalBlockWhile o)
	{
		visitPerlLexicalScope(o);
	}

	public void visitConstantDefinition(@NotNull PsiPerlConstantDefinition o)
	{
		visitPerlConstantDefinition(o);
	}

	public void visitConstantName(@NotNull PsiPerlConstantName o)
	{
		visitPerlConstant(o);
	}

	public void visitConstantsBlock(@NotNull PsiPerlConstantsBlock o)
	{
		visitPsiElement(o);
	}

	public void visitContinueBlock(@NotNull PsiPerlContinueBlock o)
	{
		visitPsiElement(o);
	}

	public void visitDefaultCompound(@NotNull PsiPerlDefaultCompound o)
	{
		visitPerlLexicalScope(o);
	}

	public void visitDerefExpr(@NotNull PsiPerlDerefExpr o)
	{
		visitExpr(o);
		// visitPerlDerefExpression(o);
	}

	public void visitDoExpr(@NotNull PsiPerlDoExpr o)
	{
		visitExpr(o);
		// visitPerlDoExpr(o);
	}

	public void visitEqualExpr(@NotNull PsiPerlEqualExpr o)
	{
		visitExpr(o);
	}

	public void visitEvalExpr(@NotNull PsiPerlEvalExpr o)
	{
		visitExpr(o);
	}

	public void visitExpr(@NotNull PsiPerlExpr o)
	{
		visitPsiElement(o);
	}

	public void visitFileReadExpr(@NotNull PsiPerlFileReadExpr o)
	{
		visitExpr(o);
	}

	public void visitFileReadForcedExpr(@NotNull PsiPerlFileReadForcedExpr o)
	{
		visitExpr(o);
	}

	public void visitFlipflopExpr(@NotNull PsiPerlFlipflopExpr o)
	{
		visitExpr(o);
	}

	public void visitForCompound(@NotNull PsiPerlForCompound o)
	{
		visitPerlLexicalScope(o);
	}

	public void visitForIterator(@NotNull PsiPerlForIterator o)
	{
		visitPsiElement(o);
	}

	public void visitForIteratorStatement(@NotNull PsiPerlForIteratorStatement o)
	{
		visitStatement(o);
	}

	public void visitForListEpxr(@NotNull PsiPerlForListEpxr o)
	{
		visitPsiElement(o);
	}

	public void visitForListStatement(@NotNull PsiPerlForListStatement o)
	{
		visitStatement(o);
	}

	public void visitForStatementModifier(@NotNull PsiPerlForStatementModifier o)
	{
		visitStatementModifier(o);
	}

	public void visitForeachCompound(@NotNull PsiPerlForeachCompound o)
	{
		visitPerlLexicalScope(o);
	}

	public void visitForeachStatementModifier(@NotNull PsiPerlForeachStatementModifier o)
	{
		visitStatementModifier(o);
	}

	public void visitFormatDefinition(@NotNull PsiPerlFormatDefinition o)
	{
		visitPsiElement(o);
	}

	public void visitFuncDefinition(@NotNull PsiPerlFuncDefinition o)
	{
		visitPerlFuncDefinition(o);
	}

	public void visitFuncSignatureContent(@NotNull PsiPerlFuncSignatureContent o)
	{
		visitSubSignatureContent(o);
	}

	public void visitGivenCompound(@NotNull PsiPerlGivenCompound o)
	{
		visitPerlLexicalScope(o);
	}

	public void visitGlobCastExpr(@NotNull PsiPerlGlobCastExpr o)
	{
		visitExpr(o);
		// visitPerlCastExpression(o);
	}

	public void visitGlobSlot(@NotNull PsiPerlGlobSlot o)
	{
		visitPsiElement(o);
	}

	public void visitGlobVariable(@NotNull PsiPerlGlobVariable o)
	{
		visitExpr(o);
		// visitPerlGlobVariable(o);
	}

	public void visitGotoExpr(@NotNull PsiPerlGotoExpr o)
	{
		visitExpr(o);
	}

	public void visitGrepExpr(@NotNull PsiPerlGrepExpr o)
	{
		visitExpr(o);
	}

	public void visitHashCastExpr(@NotNull PsiPerlHashCastExpr o)
	{
		visitExpr(o);
		// visitPerlCastExpression(o);
	}

	public void visitHashIndex(@NotNull PsiPerlHashIndex o)
	{
		visitPsiElement(o);
	}

	public void visitHashVariable(@NotNull PsiPerlHashVariable o)
	{
		visitExpr(o);
		// visitPerlVariable(o);
	}

	public void visitHeredocOpener(@NotNull PsiPerlHeredocOpener o)
	{
		visitExpr(o);
		// visitPerlHeredocOpener(o);
	}

	public void visitIfCompound(@NotNull PsiPerlIfCompound o)
	{
		visitPerlLexicalScope(o);
	}

	public void visitIfStatementModifier(@NotNull PsiPerlIfStatementModifier o)
	{
		visitStatementModifier(o);
	}

	public void visitLabelDeclaration(@NotNull PsiPerlLabelDeclaration o)
	{
		visitPsiElement(o);
	}

	public void visitLastExpr(@NotNull PsiPerlLastExpr o)
	{
		visitExpr(o);
	}

	public void visitLpAndExpr(@NotNull PsiPerlLpAndExpr o)
	{
		visitExpr(o);
	}

	public void visitLpNotExpr(@NotNull PsiPerlLpNotExpr o)
	{
		visitExpr(o);
	}

	public void visitLpOrXorExpr(@NotNull PsiPerlLpOrXorExpr o)
	{
		visitExpr(o);
	}

	public void visitMapExpr(@NotNull PsiPerlMapExpr o)
	{
		visitExpr(o);
	}

	public void visitMatchRegex(@NotNull PsiPerlMatchRegex o)
	{
		visitExpr(o);
	}

	public void visitMethod(@NotNull PsiPerlMethod o)
	{
		visitPerlMethod(o);
	}

	public void visitMethodDefinition(@NotNull PsiPerlMethodDefinition o)
	{
		visitPerlMethodDefinition(o);
	}

	public void visitMethodSignatureContent(@NotNull PsiPerlMethodSignatureContent o)
	{
		visitSubSignatureContent(o);
	}

	public void visitMethodSignatureInvocant(@NotNull PsiPerlMethodSignatureInvocant o)
	{
		visitPsiElement(o);
	}

	public void visitMulExpr(@NotNull PsiPerlMulExpr o)
	{
		visitExpr(o);
	}

	public void visitNamedBlock(@NotNull PsiPerlNamedBlock o)
	{
		visitPsiElement(o);
	}

	public void visitNamedListExpr(@NotNull PsiPerlNamedListExpr o)
	{
		visitExpr(o);
		// visitPerlMethodContainer(o);
	}

	public void visitNamedUnaryExpr(@NotNull PsiPerlNamedUnaryExpr o)
	{
		visitExpr(o);
	}

	public void visitNamespaceContent(@NotNull PsiPerlNamespaceContent o)
	{
		visitPsiElement(o);
	}

	public void visitNamespaceDefinition(@NotNull PsiPerlNamespaceDefinition o)
	{
		visitPerlNamespaceDefinition(o);
	}

	public void visitNamespaceExpr(@NotNull PsiPerlNamespaceExpr o)
	{
		visitExpr(o);
	}

	public void visitNestedCall(@NotNull PsiPerlNestedCall o)
	{
		visitSubCallExpr(o);
	}

	public void visitNestedCallArguments(@NotNull PsiPerlNestedCallArguments o)
	{
		visitPsiElement(o);
	}

	public void visitNextExpr(@NotNull PsiPerlNextExpr o)
	{
		visitExpr(o);
	}

	public void visitNoStatement(@NotNull PsiPerlNoStatement o)
	{
		visitStatement(o);
		// visitPerlNoStatement(o);
	}

	public void visitNumberConstant(@NotNull PsiPerlNumberConstant o)
	{
		visitExpr(o);
	}

	public void visitNyiStatement(@NotNull PsiPerlNyiStatement o)
	{
		visitPsiElement(o);
	}

	public void visitOrExpr(@NotNull PsiPerlOrExpr o)
	{
		visitExpr(o);
	}

	public void visitParenthesisedExpr(@NotNull PsiPerlParenthesisedExpr o)
	{
		visitExpr(o);
	}

	public void visitPerlHandleExpr(@NotNull PsiPerlPerlHandleExpr o)
	{
		visitExpr(o);
	}

	public void visitPerlRegex(@NotNull PsiPerlPerlRegex o)
	{
		visitPsiElement(o);
	}

	public void visitPerlRegexEx(@NotNull PsiPerlPerlRegexEx o)
	{
		visitPerlRegex(o);
	}

	public void visitPerlRegexModifiers(@NotNull PsiPerlPerlRegexModifiers o)
	{
		visitPsiElement(o);
	}

	public void visitPowExpr(@NotNull PsiPerlPowExpr o)
	{
		visitExpr(o);
	}

	public void visitPrefMmExpr(@NotNull PsiPerlPrefMmExpr o)
	{
		visitExpr(o);
	}

	public void visitPrefPpExpr(@NotNull PsiPerlPrefPpExpr o)
	{
		visitExpr(o);
	}

	public void visitPrefixMinusAsStringExpr(@NotNull PsiPerlPrefixMinusAsStringExpr o)
	{
		visitExpr(o);
	}

	public void visitPrefixUnaryExpr(@NotNull PsiPerlPrefixUnaryExpr o)
	{
		visitExpr(o);
	}

	public void visitPrintExpr(@NotNull PsiPerlPrintExpr o)
	{
		visitExpr(o);
	}

	public void visitPrintHandle(@NotNull PsiPerlPrintHandle o)
	{
		visitPsiElement(o);
	}

	public void visitReadHandle(@NotNull PsiPerlReadHandle o)
	{
		visitPsiElement(o);
	}

	public void visitRedoExpr(@NotNull PsiPerlRedoExpr o)
	{
		visitExpr(o);
	}

	public void visitRefExpr(@NotNull PsiPerlRefExpr o)
	{
		visitExpr(o);
	}

	public void visitRegexExpr(@NotNull PsiPerlRegexExpr o)
	{
		visitExpr(o);
	}

	public void visitReplacementRegex(@NotNull PsiPerlReplacementRegex o)
	{
		visitExpr(o);
	}

	public void visitRequireExpr(@NotNull PsiPerlRequireExpr o)
	{
		visitExpr(o);
		// visitPerlRequireExpr(o);
	}

	public void visitReturnExpr(@NotNull PsiPerlReturnExpr o)
	{
		visitExpr(o);
	}

	public void visitScalarArrayElement(@NotNull PsiPerlScalarArrayElement o)
	{
		visitExpr(o);
	}

	public void visitScalarCall(@NotNull PsiPerlScalarCall o)
	{
		visitPsiElement(o);
	}

	public void visitScalarCastExpr(@NotNull PsiPerlScalarCastExpr o)
	{
		visitExpr(o);
		// visitPerlCastExpression(o);
	}

	public void visitScalarHashElement(@NotNull PsiPerlScalarHashElement o)
	{
		visitExpr(o);
	}

	public void visitScalarIndexCastExpr(@NotNull PsiPerlScalarIndexCastExpr o)
	{
		visitExpr(o);
		// visitPerlCastExpression(o);
	}

	public void visitScalarVariable(@NotNull PsiPerlScalarVariable o)
	{
		visitExpr(o);
		// visitPerlVariable(o);
	}

	public void visitShiftExpr(@NotNull PsiPerlShiftExpr o)
	{
		visitExpr(o);
	}

	public void visitSortExpr(@NotNull PsiPerlSortExpr o)
	{
		visitExpr(o);
	}

	public void visitStatement(@NotNull PsiPerlStatement o)
	{
		visitPsiElement(o);
	}

	public void visitStatementModifier(@NotNull PsiPerlStatementModifier o)
	{
		visitPsiElement(o);
	}

	public void visitStringBare(@NotNull PsiPerlStringBare o)
	{
		visitExpr(o);
		// visitPerlString(o);
	}

	public void visitStringDq(@NotNull PsiPerlStringDq o)
	{
		visitExpr(o);
		// visitPerlString(o);
	}

	public void visitStringList(@NotNull PsiPerlStringList o)
	{
		visitExpr(o);
	}

	public void visitStringSq(@NotNull PsiPerlStringSq o)
	{
		visitExpr(o);
		// visitPerlString(o);
	}

	public void visitStringXq(@NotNull PsiPerlStringXq o)
	{
		visitExpr(o);
		// visitPerlString(o);
	}

	public void visitSubCallExpr(@NotNull PsiPerlSubCallExpr o)
	{
		visitExpr(o);
		// visitPerlMethodContainer(o);
	}

	public void visitSubDeclaration(@NotNull PsiPerlSubDeclaration o)
	{
		visitPerlSubDeclaration(o);
	}

	public void visitSubDefinition(@NotNull PsiPerlSubDefinition o)
	{
		visitPerlSubDefinition(o);
	}

	public void visitSubExpr(@NotNull PsiPerlSubExpr o)
	{
		visitExpr(o);
	}

	public void visitSubSignatureContent(@NotNull PsiPerlSubSignatureContent o)
	{
		visitPsiElement(o);
	}

	public void visitSubSignatureElementIgnore(@NotNull PsiPerlSubSignatureElementIgnore o)
	{
		visitPsiElement(o);
	}

	public void visitSuffPpExpr(@NotNull PsiPerlSuffPpExpr o)
	{
		visitExpr(o);
	}

	public void visitTagScalar(@NotNull PsiPerlTagScalar o)
	{
		visitExpr(o);
	}

	public void visitTermExpr(@NotNull PsiPerlTermExpr o)
	{
		visitExpr(o);
	}

	public void visitTrModifiers(@NotNull PsiPerlTrModifiers o)
	{
		visitPsiElement(o);
	}

	public void visitTrRegex(@NotNull PsiPerlTrRegex o)
	{
		visitExpr(o);
	}

	public void visitTrReplacementlist(@NotNull PsiPerlTrReplacementlist o)
	{
		visitPsiElement(o);
	}

	public void visitTrSearchlist(@NotNull PsiPerlTrSearchlist o)
	{
		visitPsiElement(o);
	}

	public void visitTrenarExpr(@NotNull PsiPerlTrenarExpr o)
	{
		visitExpr(o);
	}

	public void visitUnconditionalBlock(@NotNull PsiPerlUnconditionalBlock o)
	{
		visitPsiElement(o);
	}

	public void visitUndefExpr(@NotNull PsiPerlUndefExpr o)
	{
		visitExpr(o);
	}

	public void visitUnlessCompound(@NotNull PsiPerlUnlessCompound o)
	{
		visitIfCompound(o);
	}

	public void visitUnlessStatementModifier(@NotNull PsiPerlUnlessStatementModifier o)
	{
		visitStatementModifier(o);
	}

	public void visitUntilCompound(@NotNull PsiPerlUntilCompound o)
	{
		visitPerlLexicalScope(o);
	}

	public void visitUntilStatementModifier(@NotNull PsiPerlUntilStatementModifier o)
	{
		visitStatementModifier(o);
	}

	public void visitUseStatement(@NotNull PsiPerlUseStatement o)
	{
		visitStatement(o);
		// visitPerlUseStatement(o);
	}

	public void visitUseStatementConstant(@NotNull PsiPerlUseStatementConstant o)
	{
		visitStatement(o);
	}

	public void visitUseVarsStatement(@NotNull PsiPerlUseVarsStatement o)
	{
		visitStatement(o);
		// visitIPerlUseVars(o);
	}

	public void visitVariableDeclarationGlobal(@NotNull PsiPerlVariableDeclarationGlobal o)
	{
		visitExpr(o);
		// visitPerlVariableDeclaration(o);
	}

	public void visitVariableDeclarationLexical(@NotNull PsiPerlVariableDeclarationLexical o)
	{
		visitExpr(o);
		// visitPerlVariableDeclaration(o);
	}

	public void visitVariableDeclarationLocal(@NotNull PsiPerlVariableDeclarationLocal o)
	{
		visitExpr(o);
		// visitPerlVariableDeclaration(o);
	}

	public void visitVariableDeclarationWrapper(@NotNull PsiPerlVariableDeclarationWrapper o)
	{
		visitPerlVariableDeclarationWrapper(o);
	}

	public void visitWhenCompound(@NotNull PsiPerlWhenCompound o)
	{
		visitPerlLexicalScope(o);
	}

	public void visitWhenStatementModifier(@NotNull PsiPerlWhenStatementModifier o)
	{
		visitStatementModifier(o);
	}

	public void visitWhileCompound(@NotNull PsiPerlWhileCompound o)
	{
		visitPerlLexicalScope(o);
	}

	public void visitWhileStatementModifier(@NotNull PsiPerlWhileStatementModifier o)
	{
		visitStatementModifier(o);
	}

	public void visitPerlConstant(@NotNull PerlConstant o)
	{
		visitElement(o);
	}

	public void visitPerlConstantDefinition(@NotNull PerlConstantDefinition o)
	{
		visitElement(o);
	}

	public void visitPerlFuncDefinition(@NotNull PerlFuncDefinition o)
	{
		visitElement(o);
	}

	public void visitPerlLexicalScope(@NotNull PerlLexicalScope o)
	{
		visitElement(o);
	}

	public void visitPerlMethod(@NotNull PerlMethod o)
	{
		visitElement(o);
	}

	public void visitPerlMethodDefinition(@NotNull PerlMethodDefinition o)
	{
		visitElement(o);
	}

	public void visitPerlNamespaceDefinition(@NotNull PerlNamespaceDefinition o)
	{
		visitElement(o);
	}

	public void visitPerlSubDeclaration(@NotNull PerlSubDeclaration o)
	{
		visitElement(o);
	}

	public void visitPerlSubDefinition(@NotNull PerlSubDefinition o)
	{
		visitElement(o);
	}

	public void visitPerlVariableDeclarationWrapper(@NotNull PerlVariableDeclarationWrapper o)
	{
		visitElement(o);
	}

	public void visitPsiElement(@NotNull PsiElement o)
	{
		visitElement(o);
	}

}
