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

package com.perl5.lang.perl.idea.livetemplates;

import com.intellij.codeInsight.template.Template;
import com.intellij.codeInsight.template.impl.TemplateImpl;
import com.intellij.codeInsight.template.impl.TemplateOptionalProcessor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.RangeMarker;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;

import java.util.regex.Pattern;

/**
 * Created by hurricup on 04.09.2015.
 */
public class PerlIndentionSuppresor implements TemplateOptionalProcessor
{
	// pattern for getting marker
	public static final Pattern HEREDOC_OPENER_PATTERN = Pattern.compile("<<(.+?)");
	public static final Pattern HEREDOC_OPENER_PATTERN_DQ = Pattern.compile("<<(\\s*)(\")(.*?)\"");
	public static final Pattern HEREDOC_OPENER_PATTERN_SQ = Pattern.compile("<<(\\s*)(\')(.*?)\'");
	public static final Pattern HEREDOC_OPENER_PATTERN_XQ = Pattern.compile("<<(\\s*)(`)(.*?)`");


	@Override
	public void processText(Project project, Template template, Document document, RangeMarker templateRange, Editor editor)
	{
		if (isEnabled(template))
		{
			String templateText = template.getTemplateText();

			if (HEREDOC_OPENER_PATTERN.matcher(templateText).find()
					|| HEREDOC_OPENER_PATTERN_SQ.matcher(templateText).find()
					|| HEREDOC_OPENER_PATTERN_DQ.matcher(templateText).find()
					|| HEREDOC_OPENER_PATTERN_XQ.matcher(templateText).find()
					)
			{
				template.setToIndent(false);
			}
		}
	}

	@Nls
	@Override
	public String getOptionName()
	{
		return null;
	}

	@Override
	public boolean isEnabled(Template template)
	{
		// fixme this is probably bad. Doesn't work if it's custom group
		if (template instanceof TemplateImpl)
		{
			return ((TemplateImpl) template).getGroupName().startsWith("Perl5");
		}
		return false;
	}

	@Override
	public void setEnabled(Template template, boolean value)
	{
	}

	@Override
	public boolean isVisible(Template template)
	{
		return false;
	}
}
