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

import com.intellij.codeInsight.documentation.DocumentationManager;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.pod.parser.psi.PodFormatterL;
import com.perl5.lang.pod.parser.psi.PodLinkDescriptor;
import com.perl5.lang.pod.parser.psi.PodRenderingContext;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PodFormatterLMixin extends PodSectionMixin implements PodFormatterL
{
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
			PodLinkDescriptor descriptor = PodLinkDescriptor.getDescriptor(contentText);
			if (descriptor != null)
			{
				builder.append("<a href=\"");
				if (!descriptor.isUrl())
				{
					builder.append(DocumentationManager.PSI_ELEMENT_PROTOCOL);
				}
				builder.append(descriptor.getCanonicalUrl());
				builder.append("\">");
				builder.append(descriptor.getTitle());
				builder.append("</a>");
			}
			else    // fallback
			{
				builder.append("<a href=\"");
				builder.append(contentText);
				builder.append("\">");
				builder.append(contentText);
				builder.append("</a>");
			}
		}
	}
}
