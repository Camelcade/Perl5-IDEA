package com.perl5.highlighter;

import com.intellij.openapi.options.colors.ColorSettingsPage;

/**
 * Created by hurricup on 19.04.2015.
 */

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class PerlColorSettingsPage implements ColorSettingsPage
{
	private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
			new AttributesDescriptor("Comment", PerlSyntaxHighlighter.PERL_COMMENT),
			new AttributesDescriptor("Function", PerlSyntaxHighlighter.PERL_FUNCTION),
			new AttributesDescriptor("Static method call", PerlSyntaxHighlighter.PERL_STATIC_METHOD_CALL),
			new AttributesDescriptor("Instance method call", PerlSyntaxHighlighter.PERL_INSTANCE_METHOD_CALL),
			new AttributesDescriptor("Syntax", PerlSyntaxHighlighter.PERL_SYNTAX),
			new AttributesDescriptor("Package", PerlSyntaxHighlighter.PERL_PACKAGE),
			new AttributesDescriptor("Scalar", PerlSyntaxHighlighter.PERL_SCALAR),
			new AttributesDescriptor("Array", PerlSyntaxHighlighter.PERL_ARRAY),
			new AttributesDescriptor("Hash", PerlSyntaxHighlighter.PERL_HASH),
			new AttributesDescriptor("GLOB", PerlSyntaxHighlighter.PERL_GLOB),
			new AttributesDescriptor("Single-quoted string", PerlSyntaxHighlighter.PERL_SQ_STRING),
			new AttributesDescriptor("Double-quoted string", PerlSyntaxHighlighter.PERL_DQ_STRING),
			new AttributesDescriptor("Dereference", PerlSyntaxHighlighter.PERL_DEREFERENCE),
			new AttributesDescriptor("Comma", PerlSyntaxHighlighter.PERL_COMMA),
			new AttributesDescriptor("Semicolon", PerlSyntaxHighlighter.PERL_SEMICOLON),
			new AttributesDescriptor("Braces", PerlSyntaxHighlighter.PERL_BRACE),
			new AttributesDescriptor("Parentheses", PerlSyntaxHighlighter.PERL_PAREN),
			new AttributesDescriptor("Brackets", PerlSyntaxHighlighter.PERL_BRACK)
	};

	@Nullable
	@Override
	public Icon getIcon() {
		return PerlIcons.FILE;
	}

	@NotNull
	@Override
	public SyntaxHighlighter getHighlighter() {
		return new PerlSyntaxHighlighter();
	}

	@NotNull
	@Override
	public String getDemoText() {
		return "package DTL::Fast::Filter;\n" +
				"use strict; use utf8; use warnings FATAL => 'all'; \n" +
				"use parent 'DTL::Fast::Entity';\n" +
				"\n" +
				"use DTL::Fast::Template;\n" +
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
	public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
		return null;
	}

	@NotNull
	@Override
	public AttributesDescriptor[] getAttributeDescriptors() {
		return DESCRIPTORS;
	}

	@NotNull
	@Override
	public ColorDescriptor[] getColorDescriptors() {
		return ColorDescriptor.EMPTY_ARRAY;
	}

	@NotNull
	@Override
	public String getDisplayName() {
		return "Perl5";
	}
}
