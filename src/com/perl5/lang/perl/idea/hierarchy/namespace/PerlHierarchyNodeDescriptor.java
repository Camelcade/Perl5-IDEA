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

package com.perl5.lang.perl.idea.hierarchy.namespace;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.hierarchy.HierarchyNodeDescriptor;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.roots.ui.util.CompositeAppearance;
import com.intellij.openapi.util.Comparing;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;

/**
 * Created by hurricup on 16.08.2015.
 */
public class PerlHierarchyNodeDescriptor extends HierarchyNodeDescriptor
{
	protected final SmartPsiElementPointer myPerlElementPointer;

	public PerlHierarchyNodeDescriptor(NodeDescriptor parentDescriptor, PsiElement element, boolean isBase)
	{
		super(element.getProject(), parentDescriptor, element, isBase);
		myPerlElementPointer = SmartPointerManager.getInstance(myProject).createSmartPsiElementPointer(element);
	}

	@Override
	public boolean isValid()
	{
		PsiElement element = getPerlElement();
		return element != null && element.isValid();
	}

	@Override
	public boolean update()
	{
		boolean result = super.update();
		final CompositeAppearance oldText = myHighlightedText;

		myHighlightedText = new CompositeAppearance();

		NavigatablePsiElement element = (NavigatablePsiElement) getPerlElement();

		if (element == null)
		{
			final String invalidPrefix = IdeBundle.message("node.hierarchy.invalid");
			if (!myHighlightedText.getText().startsWith(invalidPrefix))
			{
				myHighlightedText.getBeginning().addText(invalidPrefix, HierarchyNodeDescriptor.getInvalidPrefixAttributes());
			}
			return true;
		}

		final ItemPresentation presentation = element.getPresentation();
		if (presentation != null)
		{
			myHighlightedText.getEnding().addText(presentation.getPresentableText());
			adjustAppearance(myHighlightedText, presentation);
		}
		myName = myHighlightedText.getText();

		if (!Comparing.equal(myHighlightedText, oldText))
		{
			result = true;
		}
		return result;
	}

	protected void adjustAppearance(CompositeAppearance appearance, ItemPresentation presentation)
	{
		appearance.getEnding().addText(" "
						+ "(" + ((PerlNamespaceDefinition) getPerlElement()).getMroType().toString() + "), "
						+ presentation.getLocationString(),
				HierarchyNodeDescriptor.getPackageNameAttributes());
	}

	public PsiElement getPerlElement()
	{
		return myPerlElementPointer.getElement();
	}
}
