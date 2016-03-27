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

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.perl5.lang.pod.parser.psi.PodCompositeElement;
import com.perl5.lang.pod.parser.psi.PodFormatterX;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 26.03.2016.
 */
public abstract class PodCompositeElementMixin extends ASTWrapperPsiElement implements PodCompositeElement
{
	public PodCompositeElementMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	// fixme this is debugging method
	public String getAsHTML()
	{
		StringBuilder builder = new StringBuilder();
		renderElementAsHTML(builder, new PodRenderingContext());
		return builder.toString();
	}

	// fixme this is debugging method
	public String getAsText()
	{
		StringBuilder builder = new StringBuilder();
		renderElementAsText(builder, new PodRenderingContext());
		return builder.toString();
	}

	@Override
	public void renderElementAsHTML(StringBuilder builder, PodRenderingContext context)
	{
		PodRenderUtil.renderPsiRangeAsHTML(getFirstChild(), null, builder, context);
	}

	@Override
	public void renderElementAsText(StringBuilder builder, PodRenderingContext context)
	{
		PodRenderUtil.renderPsiRangeAsText(getFirstChild(), null, builder, context);
	}

	@Override
	public boolean isIndexed()
	{
		return findChildByClass(PodFormatterX.class) != null;
	}

	@Override
	public int getListLevel()
	{
		PsiElement parent = getParent();
		return parent instanceof PodCompositeElement ? ((PodCompositeElement) parent).getListLevel() : 0;
	}
}
