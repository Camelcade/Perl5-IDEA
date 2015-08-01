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

package com.perl5.lang.pod.idea.highlighter;

/**
 * Created by hurricup on 21.04.2015.
 */

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class PodColorSettingsPage implements ColorSettingsPage
{
	private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
			new AttributesDescriptor("Tags", PodSyntaxHighlighter.POD_TAG),
			new AttributesDescriptor("Text", PodSyntaxHighlighter.POD_TEXT),
			new AttributesDescriptor("Perl code", PodSyntaxHighlighter.POD_CODE),
	};

	@Nullable
	@Override
	public Icon getIcon()
	{
		return PerlIcons.POD_FILE;
	}

	@NotNull
	@Override
	public SyntaxHighlighter getHighlighter()
	{
		return new PodSyntaxHighlighter(null);
	}

	@NotNull
	@Override
	public String getDemoText()
	{
		return "=head1 NAME\n" +
				"\n" +
				"DTL::Fast - Perl implementation of Django templating language.\n" +
				"\n" +
				"=head1 VERSION\n" +
				"\n" +
				"Version 1.621\n" +
				"\n" +
				"=head1 SYNOPSIS\n" +
				"\n" +
				"Complie and render template from code:\n" +
				"\n" +
				"    use DTL::Fast;\n" +
				"    my $tpl = DTL::Fast::Template->new('Hello, {{ username }}!');\n" +
				"    print $tpl->render({ username => 'Alex'});\n" +
				"\n" +
				"Or create a file: template.txt in /home/alex/templates with contents:\n";
	}

	@Nullable
	@Override
	public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap()
	{
		return null;
	}

	@NotNull
	@Override
	public AttributesDescriptor[] getAttributeDescriptors()
	{
		return DESCRIPTORS;
	}

	@NotNull
	@Override
	public ColorDescriptor[] getColorDescriptors()
	{
		return ColorDescriptor.EMPTY_ARRAY;
	}

	@NotNull
	@Override
	public String getDisplayName()
	{
		return "Perl5 POD";
	}
}
