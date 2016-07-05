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
import com.intellij.openapi.util.Pair;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.PerlLanguage;
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

			new AttributesDescriptor("Embedding marker", PerlSyntaxHighlighter.EMBED_MARKER_KEY),

			new AttributesDescriptor("Handle", PerlSyntaxHighlighter.PERL_HANDLE),
			new AttributesDescriptor("Label", PerlSyntaxHighlighter.PERL_LABEL),
			new AttributesDescriptor("Block name", PerlSyntaxHighlighter.PERL_BLOCK_NAME),
			new AttributesDescriptor("Special token", PerlSyntaxHighlighter.PERL_TAG),

			new AttributesDescriptor("Operator", PerlSyntaxHighlighter.PERL_OPERATOR),
			new AttributesDescriptor("Keyword", PerlSyntaxHighlighter.PERL_KEYWORD),
			new AttributesDescriptor("Dereference", PerlSyntaxHighlighter.PERL_DEREFERENCE),

			new AttributesDescriptor("Annotation", PerlSyntaxHighlighter.PERL_ANNOTATION),

			new AttributesDescriptor("Sub", PerlSyntaxHighlighter.PERL_SUB),
			new AttributesDescriptor("Sub, XS", PerlSyntaxHighlighter.PERL_XSUB),
			new AttributesDescriptor("Sub, autoloaded", PerlSyntaxHighlighter.PERL_AUTOLOAD),
			new AttributesDescriptor("Sub, constant", PerlSyntaxHighlighter.PERL_CONSTANT),
			new AttributesDescriptor("Sub, definition", PerlSyntaxHighlighter.PERL_SUB_DEFINITION),
			new AttributesDescriptor("Sub, declaration", PerlSyntaxHighlighter.PERL_SUB_DECLARATION),
			new AttributesDescriptor("Attribute", PerlSyntaxHighlighter.PERL_SUB_ATTRIBUTE),
			new AttributesDescriptor("Sub prototype", PerlSyntaxHighlighter.PERL_SUB_PROTOTYPE_TOKEN),

			new AttributesDescriptor("Package", PerlSyntaxHighlighter.PERL_PACKAGE),
			new AttributesDescriptor("Package definition", PerlSyntaxHighlighter.PERL_PACKAGE_DEFINITION),

			new AttributesDescriptor("Variable, scalar", PerlSyntaxHighlighter.PERL_SCALAR),
			new AttributesDescriptor("Variable, array", PerlSyntaxHighlighter.PERL_ARRAY),
			new AttributesDescriptor("Variable, hash", PerlSyntaxHighlighter.PERL_HASH),
			new AttributesDescriptor("Variable, typeglob", PerlSyntaxHighlighter.PERL_GLOB),

			new AttributesDescriptor("String, single-quoted", PerlSyntaxHighlighter.PERL_SQ_STRING),
			new AttributesDescriptor("String, double-quoted", PerlSyntaxHighlighter.PERL_DQ_STRING),
			new AttributesDescriptor("String, backticked", PerlSyntaxHighlighter.PERL_DX_STRING),

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
		return PerlIcons.PERL_LANGUAGE_ICON;
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
		// fixme remove redundant kw tags after and if https://youtrack.jetbrains.com/issue/IJSDK-80 will be resolved
		return "#!/usr/bin/perl\n" +
				"<kw>use</kw> <pragma>strict</pragma>;\n" +
				"<kw>use</kw> v5.10;\n" +
				"<kw>package</kw> <package_def>Foo::Bar</package_def>;\n" +
				"<kw>use</kw> constant <const>CONSTANT</const> => 42;\n" +
				"<kw>print</kw> <const>CONSTANT</const>;\n" +
				"<block>BEGIN</block> { <kw>use</kw> <package>Mojo::Base</package> <q>-strict</q>; };\n" +
				"<kw>sub</kw> <autoload>AUTOLOAD</autoload>{}\n" +
				"<autoload>autoloaded_sub_call</autoload>();\n" +
				"#@method\n" +
				"<kw>sub</kw> <sub_declaration>sub_declaration</sub_declaration>: <sub_attr>method</sub_attr>;\n" +
				"#@deprecated\n" +
				"<kw>sub</kw> <sub_definition>sub_definition</sub_definition>(<sub_proto>$$$</sub_proto>){}\n" +
				"<glob>*sub_declaration</glob> = \\&<sub>sub_definition</sub>;\n" +
				"<package>LWP::UserAgent</package>-><sub>new</sub>();\n" +
				"<package>Scalar::Util::</package><xsub>blessed</xsub>();\n" +
				"<kw>my</kw> <scalar>$scalar</scalar>: <sub_attr>someattr</sub_attr> = <qx>`Executable string`</qx>; # line comment\n" +
				"<scalar>$scalar</scalar> =~ /<rx>is there something</rx>/;\n" +
				"<kw>my</kw> <array>@array</array> = (42, <array>@{</array><scalar>$scalar</scalar><array>}</array>, <array>@</array><scalar>$scalar</scalar>, <hash>%</hash><scalar>$scalar</scalar>, <angle><</angle><handle>STDIN</handle><angle>></angle>);\n" +
				"<kw>my</kw> <hash>%hash</hash> = ( <q>bareword_string</q> => <hash>%{</hash><array>@array</array>[0]<hash>}</hash>);\n" +
				"<label>START</label>: <kw>print</kw> <q>'Single quoted string'</q>;\n" +
				"<kw>say</kw> <qq>\"Double quoted string\"</qq>;\n" +
				"<kw>say</kw> __LINE__;\n" +
				"<kw>say</kw> <<<q>'MOJO'</q>;\n" +
				"    <em>%=</em> <kw>print</kw> <qq>\"Mojo perl code\"</qq>\n" +
				"    <em><%</em> <kw>print</kw> <qq>\"Some more Mojo code\"</qq>; <em>%></em>\n" +
				"<q>MOJO</q>\n" +
				"\n";
	}

	@Nullable
	@Override
	public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap()
	{
		//noinspection unchecked
		return ContainerUtil.newHashMap(
				Pair.create("package", PerlSyntaxHighlighter.PERL_PACKAGE)
				, Pair.create("package_def", PerlSyntaxHighlighter.PERL_PACKAGE_DEFINITION)
				, Pair.create("angle", PerlSyntaxHighlighter.PERL_ANGLE)
				, Pair.create("scalar", PerlSyntaxHighlighter.PERL_SCALAR)
				, Pair.create("array", PerlSyntaxHighlighter.PERL_ARRAY)
				, Pair.create("hash", PerlSyntaxHighlighter.PERL_HASH)
				, Pair.create("autoload", PerlSyntaxHighlighter.PERL_AUTOLOAD)
				, Pair.create("q", PerlSyntaxHighlighter.PERL_SQ_STRING)
				, Pair.create("qx", PerlSyntaxHighlighter.PERL_DX_STRING)
				, Pair.create("qq", PerlSyntaxHighlighter.PERL_DQ_STRING)
				, Pair.create("rx", PerlSyntaxHighlighter.PERL_REGEX_TOKEN)
				, Pair.create("block", PerlSyntaxHighlighter.PERL_BLOCK_NAME)
				, Pair.create("const", PerlSyntaxHighlighter.PERL_CONSTANT)
				, Pair.create("em", PerlSyntaxHighlighter.EMBED_MARKER_KEY)
				, Pair.create("glob", PerlSyntaxHighlighter.PERL_GLOB)
				, Pair.create("handle", PerlSyntaxHighlighter.PERL_HANDLE)
				, Pair.create("kw", PerlSyntaxHighlighter.PERL_KEYWORD)
				, Pair.create("label", PerlSyntaxHighlighter.PERL_LABEL)
				, Pair.create("sub", PerlSyntaxHighlighter.PERL_SUB)
				, Pair.create("xsub", PerlSyntaxHighlighter.PERL_XSUB)
				, Pair.create("sub_attr", PerlSyntaxHighlighter.PERL_SUB_ATTRIBUTE)
				, Pair.create("sub_proto", PerlSyntaxHighlighter.PERL_SUB_PROTOTYPE_TOKEN)
				, Pair.create("sub_declaration", PerlSyntaxHighlighter.PERL_SUB_DECLARATION)
				, Pair.create("sub_definition", PerlSyntaxHighlighter.PERL_SUB_DEFINITION)
				, Pair.create("pragma", PerlSyntaxHighlighter.PERL_PRAGMA)
		);
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
		return PerlLanguage.NAME;
	}


}
