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

package com.perl5.lang.perl.idea.highlighter;

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

/**
 * Created by hurricup on 19.04.2015.
 */

public class PerlColorSettingsPage implements ColorSettingsPage
{
	private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
			new AttributesDescriptor("Version", PerlSyntaxHighlighter.PERL_VERSION),
			new AttributesDescriptor("Number", PerlSyntaxHighlighter.PERL_NUMBER),

			new AttributesDescriptor("Line comment", PerlSyntaxHighlighter.PERL_COMMENT),
			new AttributesDescriptor("Block comment", PerlSyntaxHighlighter.PERL_COMMENT_BLOCK),

			new AttributesDescriptor("Embedding marker <?/?>/%=/<%/%> ", PerlSyntaxHighlighter.EMBED_MARKER),

			new AttributesDescriptor("Handle", PerlSyntaxHighlighter.PERL_HANDLE),
			new AttributesDescriptor("Label", PerlSyntaxHighlighter.PERL_LABEL),
			new AttributesDescriptor("Block name", PerlSyntaxHighlighter.PERL_BLOCK_NAME),
			new AttributesDescriptor("Typeglob slot", PerlSyntaxHighlighter.PERL_TAG),

			new AttributesDescriptor("Operator", PerlSyntaxHighlighter.PERL_OPERATOR),
			new AttributesDescriptor("Keyword", PerlSyntaxHighlighter.PERL_KEYWORD),
			new AttributesDescriptor("Dereference", PerlSyntaxHighlighter.PERL_DEREFERENCE),

			new AttributesDescriptor("Annotation", PerlSyntaxHighlighter.PERL_ANNOTATION),

			new AttributesDescriptor("Auto-loaded sub", PerlSyntaxHighlighter.PERL_AUTOLOAD),
			new AttributesDescriptor("Constant", PerlSyntaxHighlighter.PERL_CONSTANT),
			new AttributesDescriptor("Sub call", PerlSyntaxHighlighter.PERL_SUB),
			new AttributesDescriptor("Sub definition", PerlSyntaxHighlighter.PERL_SUB_DEFINITION),
			new AttributesDescriptor("Sub declaration", PerlSyntaxHighlighter.PERL_SUB_DECLARATION),
			new AttributesDescriptor("Sub attribute", PerlSyntaxHighlighter.PERL_SUB_ATTRIBUTE),
			new AttributesDescriptor("Sub prototype", PerlSyntaxHighlighter.PERL_SUB_PROTOTYPE_TOKEN),

			new AttributesDescriptor("Package", PerlSyntaxHighlighter.PERL_PACKAGE),
			new AttributesDescriptor("Package definition", PerlSyntaxHighlighter.PERL_PACKAGE_DEFINITION),

			new AttributesDescriptor("Scalar", PerlSyntaxHighlighter.PERL_SCALAR),
			new AttributesDescriptor("Array", PerlSyntaxHighlighter.PERL_ARRAY),
			new AttributesDescriptor("Hash", PerlSyntaxHighlighter.PERL_HASH),
			new AttributesDescriptor("GLOB", PerlSyntaxHighlighter.PERL_GLOB),

			new AttributesDescriptor("Single-quoted string", PerlSyntaxHighlighter.PERL_SQ_STRING),
			new AttributesDescriptor("Double-quoted string", PerlSyntaxHighlighter.PERL_DQ_STRING),
			new AttributesDescriptor("Backticked string", PerlSyntaxHighlighter.PERL_DX_STRING),

			new AttributesDescriptor("Regex quote", PerlSyntaxHighlighter.PERL_REGEX_QUOTE),
			new AttributesDescriptor("Regex element", PerlSyntaxHighlighter.PERL_REGEX_TOKEN),

			new AttributesDescriptor("Comma", PerlSyntaxHighlighter.PERL_COMMA),
			new AttributesDescriptor("Semicolon", PerlSyntaxHighlighter.PERL_SEMICOLON),
			new AttributesDescriptor("Braces", PerlSyntaxHighlighter.PERL_BRACE),
			new AttributesDescriptor("Parentheses", PerlSyntaxHighlighter.PERL_PAREN),
			new AttributesDescriptor("Brackets", PerlSyntaxHighlighter.PERL_BRACK),
			new AttributesDescriptor("Angles", PerlSyntaxHighlighter.PERL_ANGLE)
	};

	@Nullable
	@Override
	public Icon getIcon()
	{
		return PerlIcons.PM_FILE;
	}

	@NotNull
	@Override
	public SyntaxHighlighter getHighlighter()
	{
		return new PerlSyntaxHighlighter(null);
	}


	@NotNull
	@Override
	public String getDemoText()
	{
		return "Example doesn't work. Not Yet Implemented.\n\npackage DTL::Fast::Filter v1.100_00.10;\n" +
				"use strict; use utf8; use warnings FATAL => 'all'; \n" +
				"use parent 'DTL::Fast::Entity';\n" +
				"use Module::Build::YAML;\n" +
				"\n" +
				"use DTL::Fast::Template;\n" +
				"\n" +
				"my $date = `date`;\n" +
				"\n" +
				"# This is a test comment\n" +
				"sub new\n" +
				"{\n" +
				"    my( $proto, $parameter, %kwargs)  = @_;\n" +
				"    \n" +
				"    $proto = ref $proto || $proto;\n" +
				"    \n" +
				"    $kwargs{'parameter'} = $parameter // [];\n" +
				"    \n" +
				"    die $proto->get_parse_error(\"Parameter must be an ARRAY reference\")\n" +
				"        if ref $kwargs{'parameter'} ne 'ARRAY';\n" +
				"\n" +
				"    my $self = $proto->SUPER::new(%kwargs);\n" +
				"    \n" +
				"    return $self->parse_parameters();\n" +
				"}\n" +
				"\n" +
				"sub parse_parameters{return shift;}\n" +
				"\n" +
				"sub filter\n" +
				"{\n" +
				"    my ($self) = @_;\n" +
				"    die sprintf( \"filter was not overriden in a subclass %s\", ref $self );\n" +
				"}\n" +
				"\n" +
				"1;\n";
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
		return "Perl5";
	}


}
