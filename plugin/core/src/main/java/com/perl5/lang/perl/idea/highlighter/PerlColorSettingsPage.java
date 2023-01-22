/*
 * Copyright 2015-2023 Alexandr Evstigneev
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
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.apache.groovy.util.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;


public class PerlColorSettingsPage implements ColorSettingsPage {

  @Override
  public @Nullable Icon getIcon() {
    return PerlIcons.PERL_LANGUAGE_ICON;
  }

  @Override
  public @NotNull SyntaxHighlighter getHighlighter() {
    return new PerlSyntaxHighlighter(null);
  }


  @Override
  public @NotNull String getDemoText() {
    return """
      #!/usr/bin/perl
      <kw>use</kw> <pragma>strict</pragma>;
      <kw>use</kw> <package_core>Scalar::Util</package_core>;
      <kw>use</kw> v5.10;
      <kw>package</kw> <package_def>Foo::Bar</package_def>;
      <kw>use</kw> <pragma>constant</pragma> <const>CONSTANT</const> => 42;
      <kw>print</kw> <const>CONSTANT</const>;
      <block>BEGIN</block> { <kw>use</kw> <package>Mojo::Base</package> <q>-strict</q>; };
      <kw>sub</kw> <autoload>AUTOLOAD</autoload>{}
      <autoload>autoloaded_sub_call</autoload>();
      /this[-\\s[:^alpha:]a-z-]is\\sa[^\\s]te\\\\sst/;
      <ann>#@method</ann>
      <kw>sub</kw> <sub_declaration>sub_declaration</sub_declaration>: <sub_attr>method</sub_attr>;
      <ann>#@deprecated</ann>
      <kw>sub</kw> <sub_definition>sub_definition</sub_definition>(<sub_proto>$$$</sub_proto>){}
      <glob>*sub_declaration</glob> = \\&<sub>sub_definition</sub>;
      <package>LWP::UserAgent</package>-><sub>new</sub>();
      <package_core>Scalar::Util::</package_core><xsub>blessed</xsub>();
      <kw>my</kw> <scalar>$scalar</scalar>: <sub_attr>someattr</sub_attr> = <qx>`Executable string`</qx>; # line comment
      <scalar>$scalar</scalar> =~ /<rx>is there something</rx>/;
      <scalar>$scalar</scalar> = <scalar_builtin>${^WARNING_BITS}</scalar_builtin>;
      <scalar>$scalar</scalar> = <glob_builtin>*STDERR</glob_builtin>;
      <sub_builtin>open</sub_builtin> <handle>OTHERHANDLE</handle>, <q>'somefile'</q>;
      <kw>my</kw> <array>@array</array> = (<array_builtin>@ARGV</array_builtin>, 42, <array>@{</array><scalar>$scalar</scalar><array>}</array>, <array>@</array><scalar>$scalar</scalar>, <hash>%</hash><scalar>$scalar</scalar>, <angle><</angle><handle_builtin>STDIN</handle_builtin><angle>></angle>, <angle><</angle><handle>OHTERHANDLE</handle><angle>></angle>);
      <kw>my</kw> <hash>%hash</hash> = ( <hash_builtin>%INC</hash_builtin>, <q>bareword_string</q> => <hash>%{</hash><array>@array</array>[0]<hash>}</hash>);
      <label>START</label>: <kw>print</kw> <q>'Single quoted string'</q>;
      <kw>say</kw> <qq>"Double quoted string"</qq>;
      <kw>say</kw> __LINE__;
      say "Something \\Qhere\\E \\uthere \\N{LATIN CAPITAL LETTER A} \\N{U+0028}";
      my $sum = 42 + 0x42 + 0b1010101 + 042;<kw>say</kw> <<<q>'MOJO'</q>;
          <em>%=</em> <kw>print</kw> <qq>"Mojo perl code"</qq>
          <em><%</em> <kw>print</kw> <qq>"Some more Mojo code"</qq>; <em>%></em>
      <q>MOJO</q>

      """;
  }

  @Override
  public @Nullable Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    return Maps.of(
      "package", PerlSyntaxHighlighter.PERL_PACKAGE,
      "package_core", PerlSyntaxHighlighter.PERL_PACKAGE_CORE,
      "pragma", PerlSyntaxHighlighter.PERL_PACKAGE_PRAGMA,
      "package_def", PerlSyntaxHighlighter.PERL_PACKAGE_DEFINITION,
      "angle", PerlSyntaxHighlighter.PERL_ANGLE,
      "scalar", PerlSyntaxHighlighter.PERL_SCALAR,
      "scalar_builtin", PerlSyntaxHighlighter.PERL_SCALAR_BUILTIN,
      "array", PerlSyntaxHighlighter.PERL_ARRAY,
      "array_builtin", PerlSyntaxHighlighter.PERL_ARRAY_BUILTIN,
      "hash", PerlSyntaxHighlighter.PERL_HASH,
      "hash_builtin", PerlSyntaxHighlighter.PERL_HASH_BUILTIN,
      "glob", PerlSyntaxHighlighter.PERL_GLOB,
      "glob_builtin", PerlSyntaxHighlighter.PERL_GLOB_BUILTIN,
      "handle", PerlSyntaxHighlighter.PERL_HANDLE,
      "handle_builtin", PerlSyntaxHighlighter.PERL_HANDLE_BUILTIN,
      "autoload", PerlSyntaxHighlighter.PERL_AUTOLOAD,
      PerlPsiUtil.QUOTE_Q, PerlSyntaxHighlighter.PERL_SQ_STRING,
      PerlPsiUtil.QUOTE_QX, PerlSyntaxHighlighter.PERL_DX_STRING,
      PerlPsiUtil.QUOTE_QQ, PerlSyntaxHighlighter.PERL_DQ_STRING,
      "rx", PerlSyntaxHighlighter.PERL_REGEX_TOKEN,
      "block", PerlSyntaxHighlighter.PERL_BLOCK_NAME,
      "const", PerlSyntaxHighlighter.PERL_CONSTANT,
      "em", PerlSyntaxHighlighter.EMBED_MARKER_KEY,
      "kw", PerlSyntaxHighlighter.PERL_KEYWORD,
      "label", PerlSyntaxHighlighter.PERL_LABEL,
      "sub", PerlSyntaxHighlighter.PERL_SUB,
      "sub_builtin", PerlSyntaxHighlighter.PERL_SUB_BUILTIN,
      "xsub", PerlSyntaxHighlighter.PERL_XSUB,
      "sub_attr", PerlSyntaxHighlighter.PERL_SUB_ATTRIBUTE,
      "sub_proto", PerlSyntaxHighlighter.PERL_SUB_PROTOTYPE_TOKEN,
      "sub_declaration", PerlSyntaxHighlighter.PERL_SUB_DECLARATION,
      "sub_definition", PerlSyntaxHighlighter.PERL_SUB_DEFINITION,
      "ann", PerlSyntaxHighlighter.PERL_ANNOTATION
    );
  }

  @Override
  public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
    return new AttributesDescriptor[]{
      new AttributesDescriptor("Version", PerlSyntaxHighlighter.PERL_VERSION),
      new AttributesDescriptor("Number", PerlSyntaxHighlighter.PERL_NUMBER),
      new AttributesDescriptor("Number, binary", PerlSyntaxHighlighter.PERL_NUMBER_BIN),
      new AttributesDescriptor("Number, octal", PerlSyntaxHighlighter.PERL_NUMBER_OCT),
      new AttributesDescriptor("Number, hexadecimal", PerlSyntaxHighlighter.PERL_NUMBER_HEX),

      new AttributesDescriptor("Line comment", PerlSyntaxHighlighter.PERL_COMMENT),

      new AttributesDescriptor("Embedding marker", PerlSyntaxHighlighter.EMBED_MARKER_KEY),

      new AttributesDescriptor("Label", PerlSyntaxHighlighter.PERL_LABEL),
      new AttributesDescriptor("Block name", PerlSyntaxHighlighter.PERL_BLOCK_NAME),
      new AttributesDescriptor("Special token", PerlSyntaxHighlighter.PERL_TAG),

      new AttributesDescriptor("Operator", PerlSyntaxHighlighter.PERL_OPERATOR),
      new AttributesDescriptor("Keyword", PerlSyntaxHighlighter.PERL_KEYWORD),
      new AttributesDescriptor("Dereference", PerlSyntaxHighlighter.PERL_DEREFERENCE),

      new AttributesDescriptor("Annotation", PerlSyntaxHighlighter.PERL_ANNOTATION),

      new AttributesDescriptor("Sub", PerlSyntaxHighlighter.PERL_SUB),
      new AttributesDescriptor("Sub, built-in", PerlSyntaxHighlighter.PERL_SUB_BUILTIN),
      new AttributesDescriptor("Sub, XS", PerlSyntaxHighlighter.PERL_XSUB),
      new AttributesDescriptor("Sub, autoloaded", PerlSyntaxHighlighter.PERL_AUTOLOAD),
      new AttributesDescriptor("Sub, constant", PerlSyntaxHighlighter.PERL_CONSTANT),
      new AttributesDescriptor("Sub, definition", PerlSyntaxHighlighter.PERL_SUB_DEFINITION),
      new AttributesDescriptor("Sub, declaration", PerlSyntaxHighlighter.PERL_SUB_DECLARATION),
      new AttributesDescriptor("Attribute", PerlSyntaxHighlighter.PERL_SUB_ATTRIBUTE),
      new AttributesDescriptor("Sub prototype", PerlSyntaxHighlighter.PERL_SUB_PROTOTYPE_TOKEN),

      new AttributesDescriptor("Package", PerlSyntaxHighlighter.PERL_PACKAGE),
      new AttributesDescriptor("Package, pragma", PerlSyntaxHighlighter.PERL_PACKAGE_PRAGMA),
      new AttributesDescriptor("Package, core", PerlSyntaxHighlighter.PERL_PACKAGE_CORE),
      new AttributesDescriptor("Package definition", PerlSyntaxHighlighter.PERL_PACKAGE_DEFINITION),

      new AttributesDescriptor("Variable, scalar", PerlSyntaxHighlighter.PERL_SCALAR),
      new AttributesDescriptor("Variable, scalar, built-in ", PerlSyntaxHighlighter.PERL_SCALAR_BUILTIN),
      new AttributesDescriptor("Variable, array", PerlSyntaxHighlighter.PERL_ARRAY),
      new AttributesDescriptor("Variable, array, built-in", PerlSyntaxHighlighter.PERL_ARRAY_BUILTIN),
      new AttributesDescriptor("Variable, hash", PerlSyntaxHighlighter.PERL_HASH),
      new AttributesDescriptor("Variable, hash, built-in", PerlSyntaxHighlighter.PERL_HASH_BUILTIN),

      new AttributesDescriptor("Typeglob", PerlSyntaxHighlighter.PERL_GLOB),
      new AttributesDescriptor("Typeglob, built-in", PerlSyntaxHighlighter.PERL_GLOB_BUILTIN),
      new AttributesDescriptor("Typeglob, file handle", PerlSyntaxHighlighter.PERL_HANDLE),
      new AttributesDescriptor("Typeglob, file handle, built-in", PerlSyntaxHighlighter.PERL_HANDLE_BUILTIN),

      new AttributesDescriptor("String, single-quoted", PerlSyntaxHighlighter.PERL_SQ_STRING),
      new AttributesDescriptor("String, double-quoted", PerlSyntaxHighlighter.PERL_DQ_STRING),
      new AttributesDescriptor("String, backticked", PerlSyntaxHighlighter.PERL_DX_STRING),
      new AttributesDescriptor("String, control chars", PerlSyntaxHighlighter.PERL_STRING_SPECIAL),
      new AttributesDescriptor("String, character name", PerlSyntaxHighlighter.PERL_STRING_CHAR_NAME),

      new AttributesDescriptor("Regex quote", PerlSyntaxHighlighter.PERL_REGEX_QUOTE),
      new AttributesDescriptor("Regex element", PerlSyntaxHighlighter.PERL_REGEX_TOKEN),
      //			new AttributesDescriptor("Regex character class", PerlSyntaxHighlighter.PERL_REGEX_CHAR_CLASS),

      new AttributesDescriptor("Comma", PerlSyntaxHighlighter.PERL_COMMA),
      new AttributesDescriptor("Semicolon", PerlSyntaxHighlighter.PERL_SEMICOLON),
      new AttributesDescriptor("Braces", PerlSyntaxHighlighter.PERL_BRACE),
      new AttributesDescriptor("Parentheses", PerlSyntaxHighlighter.PERL_PAREN),
      new AttributesDescriptor("Brackets", PerlSyntaxHighlighter.PERL_BRACK),
      new AttributesDescriptor("Angles", PerlSyntaxHighlighter.PERL_ANGLE)
    };
  }

  @Override
  public ColorDescriptor @NotNull [] getColorDescriptors() {
    return ColorDescriptor.EMPTY_ARRAY;
  }

  @Override
  public @NotNull String getDisplayName() {
    return PerlLanguage.NAME;
  }
}
