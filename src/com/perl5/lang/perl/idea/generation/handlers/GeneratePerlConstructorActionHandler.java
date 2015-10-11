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

package com.perl5.lang.perl.idea.generation.handlers;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.mixins.PerlSubDeclarationImplMixin;

/**
 * Created by hurricup on 11.10.2015.
 */
public class GeneratePerlConstructorActionHandler extends GeneratePerlSubActionHandlerBase implements PerlElementTypes
{
	public static String getCode()
	{
		return "\n" +
				"sub new()\n" +
				"{\n" +
				"	my ($proto) = @_;\n" +
				"	my $self = bless {}, $proto;\n" +
				"	return $self;\n" +
				"}\n\n";
	}

	@Override
	protected int getTargetOffset(PsiElement currentElement)
	{
		PerlNamespaceDefinition namespaceDefinition = PsiTreeUtil.getParentOfType(currentElement, PerlNamespaceDefinition.class);

		if (namespaceDefinition == null)
		{
			namespaceDefinition = PsiTreeUtil.findChildOfType(currentElement.getContainingFile(), PerlNamespaceDefinition.class);
		}

		if (namespaceDefinition == null)
		{
			return -1;
		}

		PsiElement contentBlock = PsiTreeUtil.getChildOfAnyType(namespaceDefinition, PsiPerlNamespaceContent.class, PsiPerlBlock.class);

		if (contentBlock == null)
		{
			return -1;
		}

		PsiElement anchor = PsiTreeUtil.getChildOfType(contentBlock, PerlSubBase.class);
		if (anchor == null)
		{
			anchor = contentBlock.getLastChild();
		}
		if (anchor == null)
		{
			anchor = contentBlock;
		}

		ASTNode anchorNode = anchor.getNode();

		if (anchorNode.getTextLength() == 0 && anchorNode.getTreePrev() != null && anchorNode.getTreePrev().getElementType() == COMMENT_BLOCK)
		{
			return anchorNode.getTreePrev().getStartOffset();
		}

		return anchor.getNode().getStartOffset();
	}

	@Override
	protected void generateAtOffset(int targetOffset, Project project, Editor editor, PsiFile file)
	{
		Document document = editor.getDocument();
		document.insertString(targetOffset, getCode());
		PsiDocumentManager.getInstance(project).commitDocument(document);

	}
}
