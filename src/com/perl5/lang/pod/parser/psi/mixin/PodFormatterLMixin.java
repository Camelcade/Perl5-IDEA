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

package com.perl5.lang.pod.parser.psi.mixin;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.AtomicNullableLazyValue;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.perl5.lang.pod.parser.psi.PodFormatterL;
import com.perl5.lang.pod.parser.psi.PodLinkDescriptor;
import com.perl5.lang.pod.parser.psi.PodLinkTarget;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.references.PodLinkToFileReference;
import com.perl5.lang.pod.parser.psi.references.PodLinkToSectionReference;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodFormatterLMixin extends PodSectionMixin implements PodFormatterL
{
	private AtomicNotNullLazyValue<PsiReference[]> lazyReference;
	private AtomicNullableLazyValue<PodLinkDescriptor> myLinkDescriptor;

	public PodFormatterLMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public void renderElementContentAsHTML(StringBuilder builder, PodRenderingContext context)
	{
		String contentText = PodRenderUtil.renderPsiElementAsText(getContentBlock());
		if (StringUtil.isNotEmpty(contentText))
		{
			PodLinkDescriptor descriptor = getLinkDescriptor();

			if (descriptor != null)
			{
				if (descriptor.getFileId() == null)
				{
					PsiFile psiFile = getContainingFile();
					if (psiFile instanceof PodLinkTarget)
					{
						descriptor.setEnforcedFileId(((PodLinkTarget) psiFile).getPodLink());
					}
				}

				builder.append(PodRenderUtil.getHTMLLink(descriptor, !isResolvable()));
			}
			else    // fallback
			{
				builder.append(PodRenderUtil.getHTMLPsiLink(contentText));
			}
		}
	}

	private boolean isResolvable()
	{
		for (PsiReference reference : getReferences())
		{
			if (reference.resolve() == null)
			{
				return false;
			}
		}
		return true;
	}

	@Nullable
	@Override
	public PodLinkDescriptor getLinkDescriptor()
	{
		if (myLinkDescriptor == null)
		{
			myLinkDescriptor = new LazyPodLinkDescriptor(this);
		}

		return myLinkDescriptor.getValue();
	}

	@NotNull
	@Override
	public PsiReference[] getReferences()
	{
		if (lazyReference == null)
		{
			lazyReference = new PodFormatterLLazyReference(this);
		}
		return lazyReference.getValue();
	}

	@Override
	public void subtreeChanged()
	{
		super.subtreeChanged();
		lazyReference = null;
		myLinkDescriptor = null;
	}

	private static class LazyPodLinkDescriptor extends AtomicNullableLazyValue<PodLinkDescriptor>
	{
		private final PodFormatterL myFormatter;

		public LazyPodLinkDescriptor(PodFormatterL myFormatter)
		{
			this.myFormatter = myFormatter;
		}

		@Nullable
		@Override
		protected PodLinkDescriptor compute()
		{
			String contentText = PodRenderUtil.renderPsiElementAsText(myFormatter.getContentBlock());
			if (StringUtil.isNotEmpty(contentText))
			{
				return PodLinkDescriptor.getDescriptor(contentText);
			}
			return null;
		}
	}

	private static class PodFormatterLLazyReference extends AtomicNotNullLazyValue<PsiReference[]>
	{
		private final PodFormatterL myElement;

		public PodFormatterLLazyReference(PodFormatterL myElement)
		{
			this.myElement = myElement;
		}

		@NotNull
		@Override
		protected PsiReference[] compute()
		{
			List<PsiReference> references = new ArrayList<PsiReference>();
			final PodLinkDescriptor descriptor = myElement.getLinkDescriptor();

			if (descriptor != null && !descriptor.isUrl())
			{
				int rangeOffset = myElement.getContentBlock().getStartOffsetInParent();

				// file reference
				TextRange fileRange = descriptor.getFileIdTextRangeInLink();
				if (fileRange != null && !fileRange.isEmpty())
				{
					references.add(new PodLinkToFileReference(myElement, fileRange.shiftRight(rangeOffset)));
				}

				// section reference
				TextRange sectionRange = descriptor.getSectionTextRangeInLink();
				if (sectionRange != null && !sectionRange.isEmpty())
				{
					references.add(new PodLinkToSectionReference(myElement, sectionRange.shiftRight(rangeOffset)));
				}
			}


			references.addAll(Arrays.asList(ReferenceProvidersRegistry.getReferencesFromProviders(myElement)));

			return references.toArray(new PsiReference[references.size()]);
		}
	}
}
