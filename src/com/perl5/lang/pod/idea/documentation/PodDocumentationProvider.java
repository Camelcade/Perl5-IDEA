/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.pod.idea.documentation;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.documentation.PerlDocUtil;
import com.perl5.lang.perl.documentation.PerlDocumentationProviderBase;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.lexer.PodElementTypes;
import com.perl5.lang.pod.parser.psi.PodFormatter;
import com.perl5.lang.pod.parser.psi.PodSection;
import com.perl5.lang.pod.parser.psi.PodSectionParagraph;
import com.perl5.lang.pod.parser.psi.PodSectionVerbatimParagraph;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 03.04.2016.
 */
public class PodDocumentationProvider extends PerlDocumentationProviderBase implements PodElementTypes
{

	@Nullable
	@Override
	public PsiElement getCustomDocumentationElement(@NotNull Editor editor, @NotNull PsiFile file, @Nullable PsiElement contextElement)
	{
		if (contextElement == null || contextElement.getLanguage() != PodLanguage.INSTANCE)
			return null;

		IElementType elementType = contextElement.getNode().getElementType();

		if (contextElement instanceof PodFormatter)
		{
			PsiElement tagElement = contextElement.getFirstChild();
			if (tagElement != null)
			{
				String tagText = tagElement.getText();
				if (StringUtil.isNotEmpty(tagText))
				{
					return PerlDocUtil.resolveDocLink("perlpod/" + tagText + "<", contextElement);
				}
			}
		}
		else if (contextElement instanceof PodSectionParagraph)
		{
			return PerlDocUtil.resolveDocLink("perlpod/\"Ordinary Paragraph\"", contextElement);
		}
		else if (contextElement instanceof PodSectionVerbatimParagraph)
		{
			return PerlDocUtil.resolveDocLink("perlpod/\"Verbatim Paragraph\"", contextElement);
		}
		else if (contextElement instanceof PodSection)
		{
			PsiElement tagElement = contextElement.getFirstChild();
			if (tagElement != null && tagElement.getNode().getElementType() != POD_UNKNOWN)
			{
				String tagText = tagElement.getText();
				if (StringUtil.isNotEmpty(tagText))
				{
					return PerlDocUtil.resolveDocLink("perlpod/" + tagText, contextElement);
				}
			}
		}

		return super.getCustomDocumentationElement(editor, file, contextElement);
	}

}
