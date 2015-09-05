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
import com.perl5.lang.perl.lexer.PerlLexer;
import org.jetbrains.annotations.Nls;

/**
 * Created by hurricup on 04.09.2015.
 */
public class PerlIndentionSuppresor implements TemplateOptionalProcessor
{
	@Override
	public void processText(Project project, Template template, Document document, RangeMarker templateRange, Editor editor)
	{
		if (isEnabled(template))
		{
			String templateText = template.getTemplateText();

			if (PerlLexer.markerPattern.matcher(templateText).find()
					|| PerlLexer.markerPatternSQ.matcher(templateText).find()
					|| PerlLexer.markerPatternDQ.matcher(templateText).find()
					|| PerlLexer.markerPatternXQ.matcher(templateText).find()
					)
				template.setToIndent(false);
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
		if (template instanceof TemplateImpl)
			return ((TemplateImpl) template).getGroupName().startsWith("Perl5");
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
