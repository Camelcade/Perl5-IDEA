package com.perl5.lang.perl.idea;

/**
 * Created by hurricup on 25.04.2015.
 */
import com.intellij.codeInsight.daemon.impl.AnnotationHolderImpl;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.impl.*;
import org.jetbrains.annotations.NotNull;

		import java.util.List;

public class PerlAnnotator implements Annotator, PerlElementTypes
{

	// not working
	Annotation markError(@NotNull final PsiElement element, @NotNull AnnotationHolder holder, String message)
	{
		TextRange range = element.getTextRange();
		String tooltip = message == null ? null : "<html><body>" + message + "</body></html>";
		Annotation annotation = new Annotation(range.getStartOffset(), range.getEndOffset(), HighlightSeverity.ERROR, message, tooltip);
		((AnnotationHolderImpl)holder).add(annotation);
		return annotation;
	}

	@Override
	public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {

		if( element instanceof PerlPackageUseInvalidImpl)
		{
			markError(element, holder, "Proper `use` syntax: " +
					"use Module VERSION LIST; " +
					"use Module VERSION; " +
					"use Module LIST; " +
					"use Module; " +
					"use VERSION;");
		}
		else if( element instanceof PerlEvalInvalidImpl)
		{
			markError(element, holder, "Proper `eval` syntax: " +
					"eval EXPR;\n" +
					"eval BLOCK;");
		}
		else if( element instanceof PerlPackageNoInvalidImpl)
		{
			markError(element, holder, "Proper `no` syntax: " +
					"no MODULE VERSION LIST; " +
					"no MODULE VERSION; " +
					"no MODULE LIST; " +
					"no MODULE; " +
					"no VERSION;");
		}
		else if( element instanceof PerlPackageRequireInvalidImpl)
		{
			markError(element, holder, "Proper `require` syntax: " +
					"require VERSION; " +
					"require MODULE; " +
					"require FILENAME;");
		}
		else if( element instanceof PerlPackageDefinitionInvalidImpl)
		{
			markError(element, holder, "Proper `package` syntax: " +
					"package NAMESPACE; " +
					"package NAMESPACE VERSION; " +
					"package NAMESPACE BLOCK" +
					"package NAMESPACE VERSION BLOCK");
		}

/*
		if (element instanceof PsiLiteralExpression) {
			PsiLiteralExpression literalExpression = (PsiLiteralExpression) element;
			String value = (String) literalExpression.getValue();
			if (value != null && value.startsWith("simple:")) {
				Project project = element.getProject();
				List<SimpleProperty> properties = SimpleUtil.findProperties(project, value.substring(7));
				if (properties.size() == 1) {
					TextRange range = new TextRange(element.getTextRange().getStartOffset() + 7,
							element.getTextRange().getStartOffset() + 7);
					Annotation annotation = holder.createInfoAnnotation(range, null);
					annotation.setTextAttributes(SyntaxHighlighterColors.LINE_COMMENT);
				} else if (properties.size() == 0) {
					TextRange range = new TextRange(element.getTextRange().getStartOffset() + 8,
							element.getTextRange().getEndOffset());
					holder.createErrorAnnotation(range, "Unresolved property");
				}
			}
		}
	*/
	}
}