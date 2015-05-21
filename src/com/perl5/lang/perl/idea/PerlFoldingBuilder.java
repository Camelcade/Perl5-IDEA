package com.perl5.lang.perl.idea;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.FoldingGroup;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiBlockStatement;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLiteralExpression;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.impl.source.tree.java.PsiBlockStatementImpl;
import com.intellij.psi.templateLanguages.OuterLanguageElementImpl;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlAnonArray;
import com.perl5.lang.perl.psi.PerlAnonHash;
import com.perl5.lang.perl.psi.impl.PerlAnonArrayImpl;
import com.perl5.lang.perl.psi.impl.PerlAnonHashImpl;
import com.perl5.lang.perl.psi.impl.PerlBlockImpl;
import com.perl5.lang.perl.psi.impl.PerlParenthesisedExprImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 20.05.2015.
 */
public class PerlFoldingBuilder extends FoldingBuilderEx
{
	@NotNull
	@Override
	public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick)
	{
		// @todo handle this
		if( root instanceof OuterLanguageElementImpl )
			return FoldingDescriptor.EMPTY;

		List<FoldingDescriptor> descriptors = new ArrayList<FoldingDescriptor>();

		descriptors.addAll(getDescriptorsFor(root, document, PerlBlockImpl.class));
		descriptors.addAll(getDescriptorsFor(root, document, PerlAnonHashImpl.class));
		descriptors.addAll(getDescriptorsFor(root, document, PerlAnonArrayImpl.class));
		descriptors.addAll(getDescriptorsFor(root, document, PerlParenthesisedExprImpl.class));
		descriptors.addAll(getDescriptorsFor(root, document, PsiComment.class));

		return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
	}

	private <T extends PsiElement> List<FoldingDescriptor> getDescriptorsFor(@NotNull PsiElement root, @NotNull Document document, Class<? extends T> c)
	{
		List<FoldingDescriptor> descriptors = new ArrayList<FoldingDescriptor>();

		// Anon arrays
		for (final T block : PsiTreeUtil.findChildrenOfType(root, c))
		{
			TextRange range = block.getTextRange();
			int startOffset = range.getStartOffset() + 1;
			int endOffset = range.getEndOffset() - 1;
			int startLine = document.getLineNumber(startOffset);
			int endLine = document.getLineNumber(endOffset);

			if( endLine - startLine > 0 )
				descriptors.add(new FoldingDescriptor(block.getNode(),new TextRange(startOffset, endOffset)));
		}
		return descriptors;
	}

	@Nullable
	@Override
	public String getPlaceholderText(@NotNull ASTNode node)
	{
		IElementType elementType = node.getElementType();

		if( elementType == PerlElementTypes.BLOCK)
			return "code block";
		else if ( elementType == PerlElementTypes.ANON_ARRAY)
			return "array";
		else if ( elementType == PerlElementTypes.ANON_HASH)
			return "hash";
		else if ( elementType == PerlElementTypes.PARENTHESISED_EXPR)
			return "list expression";
		else if ( elementType == PerlElementTypes.PERL_HEREDOC)
			return "<< heredoc >>";
		else if ( elementType == PerlElementTypes.PERL_POD)
			return "= POD block =";
		else if ( elementType == PerlElementTypes.TEMPLATE_BLOCK_HTML)
			return " HTML block ";
		else
			return "unknown entity";
	}

	@Override
	public boolean isCollapsedByDefault(@NotNull ASTNode node)
	{
		return false;
	}
}
