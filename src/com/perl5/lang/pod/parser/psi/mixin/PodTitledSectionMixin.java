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
import com.intellij.psi.PsiElement;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.PodSectionTitle;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodTitledSectionMixin extends PodSectionMixin implements PodTitledSection
{
	public PodTitledSectionMixin(@NotNull ASTNode node)
	{
		super(node);
	}

	@Override
	public PsiElement getTitleBlock()
	{
		return findChildByClass(PodSectionTitle.class);
	}

	@Override
	public void renderElement(StringBuilder builder, PodRenderingContext context)
	{

		renderElementTitle(builder, new PodRenderingContext());
		super.renderElement(builder, context);
	}

	public void renderElementTitle(StringBuilder builder, PodRenderingContext context)
	{
		PsiElement content = getTitleBlock();
		PodRenderUtil.renderPsiRange(content, content, builder, context);
	}
}
