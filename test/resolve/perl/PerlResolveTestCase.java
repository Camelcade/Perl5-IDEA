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

package resolve.perl;

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.perl5.lang.perl.fileTypes.PerlFileType;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import junit.framework.AssertionFailedError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by hurricup on 13.03.2016.
 */
public abstract class PerlResolveTestCase extends PerlLightCodeInsightFixtureTestCase
{
	@Override
	public String getFileExtension()
	{
		return PerlFileType.EXTENSION_PL;
	}

	public String getTestResultsFilePath()
	{
		return getTestDataPath() + "/" + getTestName(true) + "." + getFileExtension() + ".txt";
	}

	public void doTest(boolean success)
	{
		doTest(getTestName(true) + getFileExtension(), success);
	}

	public void doTest(String filename, boolean success)
	{
		doTest(filename, success, PerlVariableNameElement.class);
	}

	public void doTest(String filename, boolean success, Class clazz)
	{
		initWithFileSmart(filename);
		PsiReference reference = getFile().findReferenceAt(myFixture.getEditor().getCaretModel().getOffset());
		assertNotNull(reference);
		if (success)
		{
			assertNotNull(reference.resolve());
			validateTarget(reference.getElement(), reference.resolve());
		}
		else
		{
			assertNull(reference.resolve());
		}
	}

	public void validateTarget(PsiElement sourceElement, PsiElement targetElement)
	{
	}

	public void doTestWithFileCheck()
	{
		initWithFileSmart();
		doTestWithFileCheckWithoutInit();
	}

	public void doTestWithFileCheckWithoutInit()
	{
		CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
		StringBuilder sb = new StringBuilder();

		for (PsiReference psiReference : collectFileReferences())
		{
			sb.append(serializeReference(psiReference)).append("\n");
		}
		UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), sb.toString());
	}

	private String serializeReference(PsiReference reference)
	{
		StringBuilder sb = new StringBuilder();
		PsiElement sourceElement = reference.getElement();

		ResolveResult[] resolveResults;
		if (reference instanceof PsiPolyVariantReference)
		{
			resolveResults = ((PsiPolyVariantReference) reference).multiResolve(false);
		}
		else
		{
			PsiElement target = reference.resolve();
			resolveResults = target == null ? PsiElementResolveResult.EMPTY_ARRAY : PsiElementResolveResult.createResults(target);
		}

		TextRange referenceRange = reference.getRangeInElement();
		String sourceElementText = sourceElement.getText();
		int sourceElementOffset = sourceElement.getNode().getStartOffset();

		sb
				.append(reference.getClass().getSimpleName())
				.append(" at ")
				.append(referenceRange.shiftRight(sourceElementOffset))
				.append("; text in range: '")
				.append(referenceRange.subSequence(sourceElementText))
				.append("'")
				.append(" => ")
				.append(resolveResults.length)
				.append(" results:")
				.append('\n');

		for (ResolveResult result : resolveResults)
		{
			if (!result.isValidResult())
			{
				throw new AssertionFailedError("Invalid resolve result");
			}

			PsiElement targetElement = result.getElement();
			assertNotNull(targetElement);

			sb.append('\t');

			ASTNode targetElementNode = targetElement.getNode();

			if (targetElementNode == null)
			{
				sb.append("nodeless; ").append(targetElement.toString());
			}
			else
			{
				sb.append(PsiUtilCore.getElementType(targetElementNode))
						.append(" at ")
						.append(targetElementNode.getStartOffset())
						.append(" in ")
						.append(targetElement.getContainingFile().getName())
						.append('\n');
			}
		}
		return sb.toString();
	}

	private List<PsiReference> collectFileReferences()
	{
		final List<PsiReference> references = new ArrayList<PsiReference>();

		PsiFile file = getFile();

		file.accept(new PsiElementVisitor()
		{
			@Override
			public void visitElement(PsiElement element)
			{
				Collections.addAll(references, element.getReferences());
				element.acceptChildren(this);
			}
		});

		Collections.sort(references, new Comparator<PsiReference>()
		{
			@Override
			public int compare(PsiReference o1, PsiReference o2)
			{
				return o1.getElement().getTextRange().getStartOffset() + o1.getRangeInElement().getStartOffset() -
						o2.getElement().getTextRange().getStartOffset() + o2.getRangeInElement().getStartOffset();
			}
		});

		return references;
	}
}
