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

package com.perl5.lang.perl.psi.utils;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.stubs.PsiFileStub;
import com.intellij.psi.stubs.Stub;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.perl5.lang.perl.idea.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.properties.PerlStatementsContainer;
import com.perl5.lang.perl.psi.references.PerlHeredocReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 09.08.2015.
 */
public class PerlPsiUtil
{
	/**
	 * Recursively searches for string content elements beginnign from specified PsiElement
	 *
	 * @param startWith PsiElement to start from (inclusive)
	 * @return list of PerlStringContentElement
	 */
	public static Collection<PerlStringContentElement> collectStringElements(@NotNull PsiElement startWith)
	{
		final List<PerlStringContentElement> result = new ArrayList<PerlStringContentElement>();
		processStringElements(startWith, new Processor<PerlStringContentElement>()
		{
			@Override
			public boolean process(PerlStringContentElement perlStringContentElement)
			{
				result.add(perlStringContentElement);
				return true;
			}
		});
		return result;
	}

	/**
	 * Recursive searcher for string content elements
	 *
	 * @param startWith element to start with (inclusive)
	 */
	public static void processStringElements(PsiElement startWith, Processor<PerlStringContentElement> processor)
	{
		while (startWith != null)
		{
			if (startWith instanceof PerlStringContentElement)
			{
				processor.process((PerlStringContentElement) startWith);
			}

			if (startWith.getFirstChild() != null)
			{
				processStringElements(startWith.getFirstChild(), processor);
			}

			startWith = startWith.getNextSibling();
		}
	}

	/**
	 * Searching for statement this element belongs
	 *
	 * @param element
	 * @return
	 */
	public static PsiPerlStatement getElementStatement(PsiElement element)
	{
		PsiElement currentStatement = PsiTreeUtil.getParentOfType(element, PerlHeredocElementImpl.class);

		if (currentStatement != null)    // we are in heredoc
		{
			PsiElement opener = PerlHeredocReference.getClosestHeredocOpener(currentStatement);

			if (opener == null)
				return null;

			return PsiTreeUtil.getParentOfType(opener, PsiPerlStatement.class);

		}
		else
			return PsiTreeUtil.getParentOfType(element, PsiPerlStatement.class);
	}


	/**
	 * Searching for last heredoc opener before offset
	 *
	 * @param file   File to search in
	 * @param marker optional marker text
	 * @param offset offset
	 * @return Marker element
	 */
	@Nullable
	public static PsiElement findHeredocOpenerByOffset(PsiElement file, String marker, int offset)
	{
		PsiElement result = null;

		if ("\n".equals(marker))
		{
			marker = "";
		}

		for (PerlHeredocOpener opener : PsiTreeUtil.findChildrenOfType(file, PerlHeredocOpener.class))
		{
			if (opener.getTextOffset() < offset)
			{
				if (marker == null || marker.equals(opener.getName()))
					result = opener;
			}
			else
			{
				break;
			}
		}
		return result;
	}

	/**
	 * Back-searching file for an element
	 *
	 * @param file        file to search in
	 * @param offset      end offset
	 * @param elementType element type to search
	 * @return PsiElement of found element
	 */
	public static PsiElement searchLineForElementByType(PsiFile file, int offset, IElementType elementType)
	{
		if (offset == file.getTextLength())
			offset--;

		if (offset >= 0)
		{
			do
			{
				PsiElement currentElement = file.findElementAt(offset);

				if (currentElement == null || StringUtil.containsChar(currentElement.getText(), '\n'))
					break;

				if (currentElement.getNode().getElementType() == elementType)
					return currentElement;

				offset = currentElement.getTextRange().getStartOffset() - 1;

			} while (offset >= 0);
		}

		return null;
	}

	/**
	 * Searching for manipulator by element
	 *
	 * @param element PsiElement to search manipulator for
	 * @return manipulator
	 */
	@NotNull
	public static ElementManipulator getManipulator(PsiElement element)
	{
		ElementManipulator manipulator = ElementManipulators.getManipulator(element);
		if (manipulator == null)
		{
			throw new IncorrectOperationException("Unable to find manipulator for " + element);
		}
		return manipulator;
	}


	/**
	 * Renaming PsiElement using manipulator
	 *
	 * @param element PsiElement to rename
	 * @param newName newName
	 * @return manipulator return value
	 */
	public static PsiElement renameElement(PsiElement element, String newName)
	{
		//noinspection unchecked
		return PerlPsiUtil.getManipulator(element).handleContentChange(element, newName);
	}


	/**
	 * Renames file referencee element. Specific method required, cause somewhere we should put package name, and somewhere filename
	 *
	 * @param element        element to rename
	 * @param newPackageName new package name
	 */
	public static void renameFileReferencee(PsiElement element, String newPackageName)
	{
		if (element instanceof PerlNamespaceElement)
		{
			PerlPsiUtil.renameElement(element, newPackageName);
		}
		else
		{
			System.err.println("Unhandled reference from: " + element + ": " + element.getText());
		}
		// todo handle string contents for require
	}

	@Nullable
	public static PsiElement getNextSignificantSibling(PsiElement element)
	{
		PsiElement result = element.getNextSibling();
		while (true)
		{
			if (result == null || !(result instanceof PsiComment || result instanceof PsiWhiteSpace))
				break;
			result = result.getNextSibling();
		}
		return result;
	}

	@Nullable
	public static ASTNode getNextSignificantSibling(ASTNode node)
	{
		ASTNode result = node.getTreeNext();
		while (true)
		{
			if (result == null || result.getElementType() != TokenType.NEW_LINE_INDENT && result.getElementType() != TokenType.WHITE_SPACE)
			{
				break;
			}
			result = result.getTreeNext();
		}
		return result;
	}

	@Nullable
	public static PsiElement getPrevSignificantSibling(PsiElement element)
	{
		PsiElement result = element.getPrevSibling();
		while (true)
		{
			if (result == null || !(result instanceof PsiComment || result instanceof PsiWhiteSpace))
				break;
			result = result.getPrevSibling();
		}
		return result;
	}

	@Nullable
	public static PsiElement getParentElementOrStub(StubBasedPsiElement currentElement,
													@NotNull final Class<? extends StubElement> stubClass,
													@NotNull final Class<? extends PsiElement> psiClass
	)
	{
		Stub stub = currentElement.getStub();
		if (stub != null)
		{
			Stub parentStub = getParentStubOfType(stub, stubClass);
			return parentStub == null ? null : ((StubBase) parentStub).getPsi();
		}
		else
		{
			return PsiTreeUtil.getParentOfType(currentElement, psiClass);
		}
	}

	@NotNull
	public static List<PsiElement> collectNamespaceMembers(@NotNull final PerlNamespaceDefinition namespaceDefinition,
														   @NotNull final Class<? extends StubElement> stubClass,
														   @NotNull final Class<? extends PsiElement> psiClass
	)
	{
		Stub stub = namespaceDefinition.getStub();
		List<PsiElement> result = new ArrayList<PsiElement>();

		if (stub != null)
		{
			collectElementsFromStubs(namespaceDefinition, stubClass, stub, result);
		}
		else
		{
			collectElementsFromPsi(namespaceDefinition, psiClass, result);
		}
		return result;
	}

	public static void collectElementsFromStubs(final PerlNamespaceDefinition namespaceDefinition,
												final Class<? extends StubElement> stubClass,
												Stub stub, List<PsiElement> result)
	{
		for (Stub element : stub.getChildrenStubs())
		{
			if (stubClass.isInstance(element) && isNamespaceStubElement(namespaceDefinition.getStub(), element))
			{
				result.add(((StubBase) element).getPsi());
			}
			collectElementsFromStubs(namespaceDefinition, stubClass, element, result);
		}
	}

	public static boolean isNamespaceStubElement(Stub namespaceStub, Stub elementStub)
	{
		if (namespaceStub == null)
			return false;

		Stub parentStub = getParentStubOfType(elementStub, PerlNamespaceDefinitionStub.class);

		return parentStub != null && parentStub.equals(namespaceStub);
	}

	@Nullable
	public static Stub getParentStubOfType(Stub currentStubElement, Class<? extends Stub> stubClass)
	{
		while (true)
		{
			if (currentStubElement == null)
				return null;

			if (stubClass.isInstance(currentStubElement))
				return currentStubElement;

			if (currentStubElement instanceof PsiFileStub)
				return null;

			currentStubElement = currentStubElement.getParentStub();
		}
	}

	public static void collectElementsFromPsi(PerlNamespaceDefinition namespaceDefinition, Class<? extends PsiElement> psiClass, List<PsiElement> result)
	{
		for (PsiElement subDefinition : PsiTreeUtil.findChildrenOfType(namespaceDefinition, psiClass))
		{
			if (subDefinition.isValid() && namespaceDefinition.equals(PsiTreeUtil.getParentOfType(subDefinition, PerlNamespaceDefinition.class)))
			{
				result.add(subDefinition);
			}
		}
	}

	public static boolean isHeredocAhead(PsiElement startElement, final int lineEndOffset)
	{
		HeredocChecker checker = new HeredocChecker(lineEndOffset);
		iteratePsiElementsRight(startElement, checker);
		return checker.getResult();
	}

	public static boolean iteratePsiElementsRight(PsiElement element, Processor<PsiElement> processor)
	{
		if (element == null) return false;

		PsiElement run = element.getNextSibling();

		if (run == null)
		{
			if (element instanceof PsiFile)
			{
				return false;
			}
			else
			{
				return iteratePsiElementsRight(element.getParent(), processor);
			}
		}

		return iteratePsiElementsRightDown(run, processor) && iteratePsiElementsRight(element.getParent(), processor);
	}

	public static boolean iteratePsiElementsRightDown(@NotNull PsiElement element, @NotNull Processor<PsiElement> processor)
	{
		boolean result = processor.process(element);
		if (result)
		{
			// checking children
			PsiElement run = element.getFirstChild();
			if (run != null)
			{
				result = iteratePsiElementsRightDown(run, processor);
			}

			// checking next sibling
			if (result)
			{
				run = element.getNextSibling();
				if (run != null)
				{
					result = iteratePsiElementsRightDown(run, processor);
				}
			}
		}
		return result;
	}

	public static boolean processNamespaceStatements(@NotNull PsiElement rootElement, Processor<PsiElement> processor)
	{
		PsiElement run = rootElement.getFirstChild();
		boolean result = true;
		while (run != null && result)
		{
			if (!(run instanceof PerlNamespaceDefinition))
			{
				if (run instanceof PerlCompositeElement)
				{
					result = processor.process(run);
				}
				if (result && run instanceof PerlStatementsContainer)
				{
					result = processNamespaceStatements(run, processor);
				}
			}
			run = run.getNextSibling();
		}
		return result;
	}

	static private class HeredocChecker implements Processor<PsiElement>
	{
		protected final int lineEndOffset;
		protected boolean myResult = false;

		public HeredocChecker(int lineEndOffset)
		{
			this.lineEndOffset = lineEndOffset;
		}

		@Override
		public boolean process(PsiElement element)
		{
			if (element instanceof PerlHeredocOpener)
			{
				myResult = true;
				return false;
			}
			if (element.getTextRange().getStartOffset() >= lineEndOffset)
			{
				myResult = element instanceof PerlHeredocElementImpl;
				return false;
			}
			return true;
		}

		public boolean getResult()
		{
			return myResult;
		}
	}

}
