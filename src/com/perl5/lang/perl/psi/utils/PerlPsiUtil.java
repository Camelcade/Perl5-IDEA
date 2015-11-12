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

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlHeredocOpener;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.references.PerlHeredocReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

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
	public static Collection<PerlStringContentElement> findStringElments(PsiElement startWith)
	{
		ArrayList<PerlStringContentElement> result = new ArrayList<PerlStringContentElement>();
		findStringElments(startWith, result);
		return result;
	}

	/**
	 * Recursive searcher for string content elements
	 *
	 * @param startWith element to start with (inclusive)
	 * @param result    list to populate
	 */
	public static void findStringElments(PsiElement startWith, Collection<PerlStringContentElement> result)
	{
		while (startWith != null)
		{
			if (startWith instanceof PerlStringContentElement)
				result.add((PerlStringContentElement) startWith);

			if (startWith.getFirstChild() != null)
				findStringElments(startWith.getFirstChild(), result);

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
		while( true )
		{
			if( result == null || !(result instanceof PsiComment || result instanceof PsiWhiteSpace) )
				break;
			result = result.getNextSibling();
		}
		return result;
	}

	@Nullable
	public static PsiElement getPrevSignificantSibling(PsiElement element)
	{
		PsiElement result = element.getPrevSibling();
		while( true )
		{
			if( result == null || !(result instanceof PsiComment || result instanceof PsiWhiteSpace) )
				break;
			result = result.getPrevSibling();
		}
		return result;
	}
}
