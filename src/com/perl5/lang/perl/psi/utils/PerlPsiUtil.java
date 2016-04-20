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
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.stubs.PsiFileStub;
import com.intellij.psi.stubs.Stub;
import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.Processor;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.idea.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.properties.PerlLabelScope;
import com.perl5.lang.perl.psi.properties.PerlLoop;
import com.perl5.lang.perl.psi.properties.PerlStatementsContainer;
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
			if (result == null || !(result instanceof PsiComment || result instanceof PsiWhiteSpace || result.getNode().getElementType() == PerlElementTypes.POD))
				break;
			result = result.getPrevSibling();
		}
		return result;
	}

	@Nullable
	public static PsiElement getParentElementOrStub(StubBasedPsiElement currentElement,
													@Nullable final Class<? extends StubElement> stubClass,
													@NotNull final Class<? extends PsiElement> psiClass
	)
	{
		Stub stub = currentElement.getStub();
		if (stub != null && stubClass != null)
		{
			Stub parentStub = getParentStubOfType(stub, stubClass);
			return parentStub == null ? null : ((StubBase) parentStub).getPsi();
		}
		else
		{
			return PsiTreeUtil.getParentOfType(currentElement, psiClass);
		}
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

	@NotNull
	public static List<PsiElement> collectNamespaceMembers(@NotNull final PsiElement rootElement,
														   @Nullable final Class<? extends StubElement> stubClass,
														   @NotNull final Class<? extends PsiElement> psiClass
	)
	{
		final List<PsiElement> result = new ArrayList<PsiElement>();
		if (stubClass != null && rootElement instanceof StubBasedPsiElement)
		{
			Stub rootElementStub = ((StubBasedPsiElement) rootElement).getStub();

			if (rootElementStub != null)
			{
				processElementsFromStubs(
						rootElementStub,
						new Processor<Stub>()
						{
							@Override
							public boolean process(Stub stub)
							{
								if (stubClass.isInstance(stub))
									result.add(((StubBase) stub).getPsi());

								return true;
							}
						},
						PerlNamespaceDefinitionStub.class
				);
				return result;
			}
		}

		processElementsFromPsi(
				rootElement,
				new Processor<PsiElement>()
				{
					@Override
					public boolean process(PsiElement element)
					{
						if (psiClass.isInstance(element))
							result.add(element);
						return true;
					}
				},
				PerlNamespaceDefinition.class
		);
		return result;
	}

	public static boolean processElementsFromStubs(
			@Nullable Stub stub,
			@Nullable Processor<Stub> processor,
			@Nullable Class<? extends StubElement> avoidClass
	)
	{
		if (stub == null || processor == null)
			return false;

		for (Stub child : stub.getChildrenStubs())
		{
			if (!processor.process(child))
				return false;

			if (avoidClass == null || !avoidClass.isInstance(child)) // don't enter sublcasses
			{
				if (!processElementsFromStubs(child, processor, avoidClass))
					return false;
			}
		}
		return true;
	}


	public static boolean processElementsFromPsi(
			@Nullable PsiElement element,
			@Nullable Processor<PsiElement> processor,
			@Nullable Class<? extends PsiElement> avoidClass
	)
	{
		if (element == null || processor == null)
			return false;

		for (PsiElement child : element.getChildren())
		{
			if (!processor.process(child))
				return false;

			if (avoidClass == null || !avoidClass.isInstance(child)) // don't enter subclasses
			{
				if (!processElementsFromPsi(child, processor, avoidClass))
					return false;
			}
		}
		return true;
	}

	public static boolean isHeredocAhead(PsiElement startElement, final int lineEndOffset)
	{
		HeredocChecker checker = new HeredocChecker(lineEndOffset);
		iteratePsiElementsRight(startElement, checker);
		return checker.getResult();
	}

	public static boolean iteratePsiElementsRight(PsiElement element, Processor<PsiElement> processor)
	{
		if (element == null || element instanceof PsiFile) return false;

		if (!processor.process(element))
			return false;

		PsiElement run = element.getNextSibling();

		if (run == null)
		{
			return iteratePsiElementsRight(element.getParent(), processor);
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

	public static boolean isCommentOrSpace(PsiElement element)
	{
		return element != null && PerlParserDefinition.WHITE_SPACE_AND_COMMENTS.contains(element.getNode().getElementType());
	}

	/**
	 * Traversing tree according to next/last/redo labels resolution and processes all labels declarations
	 *
	 * @param element   element to start from
	 * @param processor processor to process elements
	 */
	public static void processNextRedoLastLabelDeclarations(PsiElement element, Processor<PerlLabelDeclaration> processor)
	{
		if (element == null || element instanceof PerlLabelScope)
		{
			return;
		}

		if (element instanceof PerlLoop)
		{
			PsiElement prevElement = getPrevSignificantSibling(element);
			if (prevElement instanceof PerlLabelDeclaration)
			{
				if (!processor.process((PerlLabelDeclaration) prevElement))
					return;
			}
		}
		processNextRedoLastLabelDeclarations(element.getParent(), processor);
	}

	/**
	 * Traversing tree according to goto labels resolution and processes all labels declarations
	 *
	 * @param element   element to start from
	 * @param processor processor to process elements
	 */
	public static void processGotoLabelDeclarations(PsiElement element, Processor<PerlLabelDeclaration> processor)
	{
		if (element == null)
		{
			return;
		}

		PsiElement run = element.getFirstChild();
		while (run != null)
		{
			if (run instanceof PerlLabelDeclaration)
			{
				if (!processor.process((PerlLabelDeclaration) run))
					return;
			}
			run = run.getNextSibling();
		}

		// next iteration
		if (!(element instanceof PsiFile))
		{
			processGotoLabelDeclarations(element.getParent(), processor);
		}

	}

	/**
	 * Recursively processes all elements starting element if they are in specified range
	 *
	 * @param element   startElement
	 * @param processor processor
	 * @param range     limitation range
	 * @return false if need to stop
	 */

	public static boolean processElementsInRange(PsiElement element, @NotNull TextRange range, @NotNull final PsiElementProcessor<PsiElement> processor)
	{
		if (element == null) return true;

		TextRange elementRange = element.getNode().getTextRange();
		if (range.contains(elementRange))
		{
			if (!processor.execute(element))
				return false;
		}
		if (range.intersects(elementRange))
		{
			PsiElement run = element.getFirstChild();
			while (run != null)
			{
				if (!processElementsInRange(run, range, processor))
					return false;
				run = run.getNextSibling();
			}
		}
		return true;
	}

	public static List<PerlAnnotation> collectAnnotations(@NotNull PsiElement element)
	{
		final List<PerlAnnotation> result = new ArrayList<PerlAnnotation>();
		processElementAnnotations(element, new Processor<PerlAnnotation>()
		{
			@Override
			public boolean process(PerlAnnotation perlAnnotation)
			{
				result.add(perlAnnotation);
				return true;
			}
		});
		return result;
	}

	public static PerlAnnotation getAnnotationByClass(@NotNull PsiElement element, final Class<? extends PerlAnnotation> annotationClass)
	{
		final PerlAnnotation[] result = new PerlAnnotation[]{null};
		processElementAnnotations(element, new Processor<PerlAnnotation>()
		{
			@Override
			public boolean process(PerlAnnotation perlAnnotation)
			{
				if (annotationClass.isInstance(perlAnnotation))
				{
					result[0] = perlAnnotation;
					return false;
				}
				return true;
			}
		});
		return result[0];
	}

	public static void processElementAnnotations(@NotNull PsiElement element, @NotNull Processor<PerlAnnotation> annotationProcessor)
	{
		PsiElement run = element.getPrevSibling();
		while (run != null)
		{
			if (run instanceof PerlAnnotationContainer)
			{
				PerlAnnotation annotation = ((PerlAnnotationContainer) run).getAnnotation();
				if (annotation != null)
				{
					if (!annotationProcessor.process(annotation))
						return;
				}
			}
			else if (!(run instanceof PsiComment || run instanceof PsiWhiteSpace))
			{
				return;
			}
			run = run.getPrevSibling();
		}
	}

	static public abstract class HeredocProcessor implements Processor<PsiElement>
	{
		protected final int lineEndOffset;

		public HeredocProcessor(int lineEndOffset)
		{
			this.lineEndOffset = lineEndOffset;
		}

	}

	private static class HeredocChecker extends HeredocProcessor
	{
		protected boolean myResult = false;

		public HeredocChecker(int lineEndOffset)
		{
			super(lineEndOffset);
		}

		@Override
		public boolean process(PsiElement element)
		{
			if (element instanceof PerlHeredocOpener)
			{
				myResult = true;
				return false;
			}
			if (element != null && element.getTextRange().getStartOffset() >= lineEndOffset)
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
