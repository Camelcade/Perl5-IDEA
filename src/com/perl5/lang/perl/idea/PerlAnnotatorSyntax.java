package com.perl5.lang.perl.idea;

/**
 * Created by hurricup on 25.04.2015.
 */
import com.intellij.codeInsight.daemon.impl.AnnotationHolderImpl;
import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.impl.*;
import com.perl5.lang.perl.util.PerlScalarUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.StringContent;
import java.util.List;

public class PerlAnnotatorSyntax implements Annotator, PerlElementTypes
{
	private void colorize(Annotation annotation, TextAttributesKey key, boolean builtin)
	{
		if( builtin )
			annotation.setEnforcedTextAttributes(
					TextAttributes.merge(
							PerlSyntaxHighlighter.PERL_BUILT_IN.getDefaultAttributes(),
							key.getDefaultAttributes()
					));
		else
			annotation.setEnforcedTextAttributes(key.getDefaultAttributes());
	}

	@Override
	public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {

		IElementType elementType = element.getNode().getElementType();
		if( elementType == PERL_SCALAR )
		{
			colorize(
				holder.createInfoAnnotation(element, null),
				PerlSyntaxHighlighter.PERL_SCALAR,
				PerlScalarUtil.isBuiltIn(element.getText()));
		}
		else if( elementType == PERL_HASH )
		{
			Annotation annotation = holder.createInfoAnnotation(element, null);
			annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_HASH);
		}
		else if( elementType == PERL_ARRAY )
		{
			Annotation annotation = holder.createInfoAnnotation(element, null);
			annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_ARRAY);
		}
		else if( elementType == PERL_PACKAGE )
		{
			Annotation annotation = holder.createInfoAnnotation(element, null);
			annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_PACKAGE);
		}
		else if( elementType == PERL_STRING)
		{
			PsiElement quoteElement = element.getParent().getFirstChild();
			String quoteElementText = quoteElement.getText();

			Annotation annotation = holder.createInfoAnnotation(element, null);

			if( quoteElement == element || "'".equals(quoteElementText) || "q".equals(quoteElementText) || "qw".equals(quoteElementText)) // bareword string, single quoted string
			{
				annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_SQ_STRING);
			}
			else if( "\"".equals(quoteElementText) || "qq".equals(quoteElementText)) // interpolated
			{
				annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_DQ_STRING);
			}
			else if( "`".equals(quoteElementText) || "qx".equals(quoteElementText)) // executable
			{
				annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_DX_STRING);
			}
			else
			{
				throw new Error("Unable to detect string type");
			}
		}
		else if( elementType == PERL_FUNCTION)
		{
			Annotation annotation = holder.createInfoAnnotation(element, null);
			annotation.setTextAttributes(PerlSyntaxHighlighter.PERL_FUNCTION);
		}
	}
}