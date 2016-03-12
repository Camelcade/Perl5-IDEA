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

package com.perl5.lang.htmlmason.parser.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.HTMLMasonUtils;
import com.perl5.lang.htmlmason.MasonCoreUtils;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.perl.extensions.PerlImplicitVariablesProvider;
import com.perl5.lang.perl.psi.PerlVariableDeclarationWrapper;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 05.03.2016.
 */
public class HTMLMasonFileImpl extends PerlFileImpl implements HTMLMasonElementTypes, PerlImplicitVariablesProvider
{
	protected final List<PerlVariableDeclarationWrapper> myImplicitVariables = new ArrayList<PerlVariableDeclarationWrapper>();
	protected int myMasonChangeCounter;
	protected Map<Integer, Boolean> myPerlLinesMap = new THashMap<Integer, Boolean>();

	public HTMLMasonFileImpl(@NotNull FileViewProvider viewProvider)
	{
		super(viewProvider, HTMLMasonLanguage.INSTANCE);
	}

	@Nullable
	public VirtualFile getComponentRoot()
	{
		return HTMLMasonUtils.getComponentRoot(getProject(), MasonCoreUtils.getContainingVirtualFile(this));
	}

	public boolean isInPerlLine(PsiElement element)
	{
		if (element == null)
			return false;

		ASTNode node = element.getNode();
		int startOffset = node.getStartOffset();
//		int endOffset = startOffset + node.getTextLength();

		return isInPerlLine(startOffset); ///|| isInPerlLine(endOffset);
	}

	protected boolean isInPerlLine(int offset)
	{
		Document document = PsiDocumentManager.getInstance(getProject()).getDocument(this);
		if (document != null)
		{
			int lineNumber = document.getLineNumber(offset);

			Boolean result = myPerlLinesMap.get(lineNumber);

			if (result != null)
				return result;

			PsiElement firstElement = findElementAt(document.getLineStartOffset(lineNumber));

			result = firstElement != null && firstElement.getNode().getElementType() == HTML_MASON_LINE_OPENER;

			myPerlLinesMap.put(lineNumber, result);
			return result;
		}
		return false;
	}

	@NotNull
	@Override
	public List<PerlVariableDeclarationWrapper> getImplicitVariables()
	{
		if (myMasonChangeCounter != HTMLMasonSettings.getInstance(getProject()).getChangeCounter())
		{
			fillImplicitVariables();
		}
		return myImplicitVariables;
	}

	protected void fillImplicitVariables()
	{
		myImplicitVariables.clear();

		if (isValid())
		{
			HTMLMasonSettings settings = HTMLMasonSettings.getInstance(getProject());
			MasonCoreUtils.fillVariablesList(this, myImplicitVariables, settings.globalVariables);
			myMasonChangeCounter = settings.getChangeCounter();
		}
	}

}
