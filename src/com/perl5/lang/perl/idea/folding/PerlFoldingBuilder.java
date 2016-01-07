/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.folding;

import com.intellij.codeInsight.folding.CodeFoldingSettings;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.templateLanguages.OuterLanguageElementImpl;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 20.05.2015.
 */
public class PerlFoldingBuilder extends FoldingBuilderEx implements PerlElementTypes
{
	protected static final TokenSet COMMENT_EXCLUDED_TOKENS = TokenSet.EMPTY;

	@NotNull
	@Override
	public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick)
	{
		// @todo handle this
		if (root instanceof OuterLanguageElementImpl)
			return FoldingDescriptor.EMPTY;

		List<FoldingDescriptor> descriptors = new ArrayList<FoldingDescriptor>();

		descriptors.addAll(getDescriptorsFor(root, document, PsiPerlConstantsBlock.class, 0, 0, 2));
		descriptors.addAll(getDescriptorsFor(root, document, PsiPerlBlock.class, 0, 0, 1));
		descriptors.addAll(getDescriptorsFor(root, document, PsiPerlAnonHash.class, 0, 0, 2));
		descriptors.addAll(getDescriptorsFor(root, document, PsiPerlAnonArray.class, 0, 0, 2));
		descriptors.addAll(getDescriptorsFor(root, document, PsiPerlParenthesisedExpr.class, 0, 0, 2));
		descriptors.addAll(getDescriptorsFor(root, document, PerlHeredocElementImpl.class, 1, 1, 2));
		descriptors.addAll(getDescriptorsFor(root, document, PsiPerlStringList.class, 2, 0, 2));

		descriptors.addAll(getCommentsDescriptors(root, document));
		descriptors.addAll(getImportDescriptors(root, document));

		return descriptors.toArray(new FoldingDescriptor[descriptors.size()]);
	}

	/**
	 * Searching for sequential comments (starting from newline or subblock beginning) and making folding descriptors for such blocks of size > 1
	 *
	 * @param root     root to search in
	 * @param document document to search in
	 * @return list of FoldingDescriptros
	 */
	private List<FoldingDescriptor> getCommentsDescriptors(@NotNull PsiElement root, @NotNull Document document)
	{
		List<FoldingDescriptor> descriptors = new ArrayList<FoldingDescriptor>();

		Collection<PsiComment> comments = PsiTreeUtil.findChildrenOfType(root, PsiComment.class);

		TokenSet commentExcludedTokens = getCommentExcludedTokens();

		int currentOffset = 0;

		for (PsiComment comment : comments)
		{
			if (currentOffset <= comment.getTextOffset() && !commentExcludedTokens.contains(comment.getNode().getElementType()))    // skips already collapsed blocks
			{
				if (comment.getNode().getElementType() == POD)
				{
					TextRange commentRange = comment.getTextRange();
					int startOffset = commentRange.getStartOffset();
					int endOffset = commentRange.getEndOffset();

					if (comment.getText().endsWith("\n"))
					{
						endOffset--;
					}
					currentOffset = endOffset;
					descriptors.add(new FoldingDescriptor(comment.getNode(), new TextRange(startOffset, endOffset)));
					continue;
				}

				boolean isCollapsable = false;
				PsiElement lastComment = comment;

				if (comment.getNode().getElementType() == getTemplateBlockElementType())    // template blocks are always collapsable
				{
					isCollapsable = true;
				}
				else
				{
					// checking if this is a first element of block or starts from newline
					while (true)
					{
						lastComment = lastComment.getPrevSibling();

						if (lastComment == null || lastComment instanceof PsiComment)
						{
							isCollapsable = true;
							break;
						}
						else if (lastComment instanceof PsiWhiteSpace)
						{
							// whitespace with newline
							if (lastComment.getText().equals("\n"))
							{
								isCollapsable = true;
								break;
							}
						}
						// non-whitespace block
						else
							break;
					}
				}

				if (isCollapsable)
				{
					// looking for an end
					int startOffset = comment.getTextOffset();
					if (comment.getText().startsWith("\n") &&
							startOffset > 0 &&
							document.getCharsSequence().charAt(startOffset - 1) != '\n'
							)
					{
						startOffset++;
					}
					int endOffset = comment.getTextRange().getEndOffset();

					ASTNode blockNode = comment.getNode();
					PsiElement currentComment = comment;

					while (currentComment != null)
					{
						if (
								currentComment instanceof PsiComment &&
										!commentExcludedTokens.contains(currentComment.getNode().getElementType()) &&
										!currentComment.getText().contains("todo") &&
										!currentComment.getText().contains("fixme")
								)
						{
							endOffset = currentComment.getTextOffset() + currentComment.getTextLength();
							if (currentComment.getText().endsWith("\n"))
								endOffset--;
						}
						else if (!(currentComment instanceof PsiWhiteSpace))
						{
							break;
						}

						currentComment = currentComment.getNextSibling();
					}

					if (endOffset > startOffset)
					{
						int startLine = document.getLineNumber(startOffset);
						int endLine = document.getLineNumber(endOffset);

						if (endLine > startLine)
						{
							currentOffset = endOffset;
							descriptors.add(new FoldingDescriptor(blockNode, new TextRange(startOffset, endOffset)));
						}
					}
				}
			}
		}

		return descriptors;
	}

	/**
	 * Searching for sequential uses and requires, ignoring stament modifieers, comments, pods and whitespaces and making folding descriptors for such blocks of size > 1
	 *
	 * @param root     root to search in
	 * @param document document to search in
	 * @return list of FoldingDescriptros
	 */
	private List<FoldingDescriptor> getImportDescriptors(@NotNull PsiElement root, @NotNull Document document)
	{
		List<FoldingDescriptor> descriptors = new ArrayList<FoldingDescriptor>();

		@SuppressWarnings("unchecked")
		Collection<PerlNamespaceElementContainer> imports = PsiTreeUtil.<PerlNamespaceElementContainer>findChildrenOfAnyType(root, PsiPerlUseStatement.class, PsiPerlRequireExpr.class);

		int currentOffset = 0;

		for (PsiElement perlImport : imports)
		{
			if (currentOffset < perlImport.getTextOffset())
			{
				PsiElement currentStatement = perlImport;

				if (currentStatement instanceof PerlRequireExpr)
					currentStatement = currentStatement.getParent();

				if (currentStatement instanceof PerlUseStatement || currentStatement.getFirstChild() instanceof PerlRequireExpr)
				{
					int blockStart = currentStatement.getTextOffset();
					int blockEnd = blockStart;
					ASTNode blockNode = perlImport.getNode();

					int importsNumber = 0;

					while (currentStatement != null)
					{
						if (currentStatement instanceof PsiPerlUseStatement && !((PsiPerlUseStatement) currentStatement).isPragma() && !((PsiPerlUseStatement) currentStatement).isVersion()
								|| currentStatement.getFirstChild() instanceof PerlRequireExpr)
						{
							blockEnd = currentStatement.getTextOffset() + currentStatement.getTextLength();
							importsNumber++;
						}
						else if (!(currentStatement instanceof PsiComment || currentStatement instanceof PsiWhiteSpace))
							break;

						currentStatement = currentStatement.getNextSibling();
					}

					if (blockEnd != blockStart && importsNumber > 1)
					{
						currentOffset = blockEnd;
						descriptors.add(new FoldingDescriptor(blockNode, new TextRange(blockStart, blockEnd)));
					}
				}
			}
		}

		return descriptors;
	}

	/**
	 * Finding psi elements of specific types and add Folding descriptor for them if they are more than certain lines lenght
	 *
	 * @param root        root element for searching
	 * @param document    document for searching
	 * @param c           PsiElement class to search for
	 * @param startMargin beginning margin for collapsable block
	 * @param endMargin   end margin for collapsable block
	 * @param <T>         PsiElement subclass
	 * @return list of folding descriptors
	 */
	private <T extends PsiElement> List<FoldingDescriptor> getDescriptorsFor(
			@NotNull PsiElement root,
			@NotNull Document document,
			Class<? extends T> c,
			int startMargin,
			int endMargin,
			int minLines
	)
	{
		List<FoldingDescriptor> descriptors = new ArrayList<FoldingDescriptor>();

		// Anon arrays
		for (PsiElement block : PsiTreeUtil.findChildrenOfType(root, c))
		{
			if (!(block.getParent() instanceof PerlNamespaceDefinition))
			{

				TextRange range = block.getTextRange();
				int startOffset = range.getStartOffset() + startMargin;
				int endOffset = range.getEndOffset() - endMargin;
				int startLine = document.getLineNumber(startOffset);
				int endLine = document.getLineNumber(endOffset);

				if (endLine - startLine > minLines)
					descriptors.add(new FoldingDescriptor(block.getNode(), new TextRange(startOffset, endOffset)));
			}
		}
		return descriptors;
	}

	@Nullable
	@Override
	public String getPlaceholderText(@NotNull ASTNode node)
	{
		IElementType elementType = node.getElementType();

		if (elementType == BLOCK)
			return "{code block}";
		if (elementType == CONSTANTS_BLOCK)
			return "{constants definitions}";
		if (elementType == STRING_LIST)
			return "{strings list}";
		else if (elementType == ANON_ARRAY)
			return "[array]";
		else if (elementType == ANON_HASH)
			return "{hash}";
		else if (elementType == PARENTHESISED_EXPR)
			return "(list expression...)";
		else if (elementType == HEREDOC)
			return "<< heredoc >>";
		else if (elementType == POD)
			return "= POD block =";
		else if (elementType == COMMENT_BLOCK)
			return "# Block comment";
		else if (elementType == COMMENT_LINE)
			return "# comments...";
		else if (elementType == USE_STATEMENT || elementType == REQUIRE_EXPR)
			return "imports...";
		else if (elementType == getTemplateBlockElementType())
			return "/ template /";
		else
			return "unknown entity " + elementType;
	}

	@Override
	public boolean isCollapsedByDefault(@NotNull ASTNode node)
	{
		IElementType elementType = node.getElementType();
		if (elementType == POD)    // documentation comments
			return CodeFoldingSettings.getInstance().COLLAPSE_DOC_COMMENTS;
		else if (elementType == USE_STATEMENT || elementType == REQUIRE_EXPR)    // imports
			return CodeFoldingSettings.getInstance().COLLAPSE_IMPORTS;
		else if (elementType == BLOCK)    // method bodies
			return CodeFoldingSettings.getInstance().COLLAPSE_METHODS;
		else if (elementType == COMMENT_LINE || elementType == COMMENT_BLOCK)
			return PerlFoldingSettings.getInstance().COLLAPSE_COMMENTS;
		else if (elementType == CONSTANTS_BLOCK)
			return PerlFoldingSettings.getInstance().COLLAPSE_CONSTANT_BLOCKS;
		else if (elementType == ANON_ARRAY)
			return PerlFoldingSettings.getInstance().COLLAPSE_ANON_ARRAYS;
		else if (elementType == ANON_HASH)
			return PerlFoldingSettings.getInstance().COLLAPSE_ANON_HASHES;
		else if (elementType == PARENTHESISED_EXPR)
			return PerlFoldingSettings.getInstance().COLLAPSE_PARENTHESISED;
		else if (elementType == HEREDOC)
			return PerlFoldingSettings.getInstance().COLLAPSE_HEREDOCS;
		else if (elementType == STRING_LIST)
			return PerlFoldingSettings.getInstance().COLLAPSE_QW;
		else if (elementType == getTemplateBlockElementType())
			return PerlFoldingSettings.getInstance().COLLAPSE_TEMPLATES;
		else
			return false;
	}

	@Nullable
	protected IElementType getTemplateBlockElementType()
	{
		return null;
	}

	/**
	 * Returns list of tokens in PSIComment that should not be included in folding regions
	 *
	 * @return tokenset
	 */
	@NotNull
	protected TokenSet getCommentExcludedTokens()
	{
		return COMMENT_EXCLUDED_TOKENS;
	}

}
