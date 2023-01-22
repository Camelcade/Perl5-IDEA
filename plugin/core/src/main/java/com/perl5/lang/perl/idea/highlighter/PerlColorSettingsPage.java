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
import com.perl5.PerlBundle;
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
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.version"), PerlSyntaxHighlighter.PERL_VERSION),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.number"), PerlSyntaxHighlighter.PERL_NUMBER),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.number.binary"), PerlSyntaxHighlighter.PERL_NUMBER_BIN),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.number.octal"), PerlSyntaxHighlighter.PERL_NUMBER_OCT),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.number.hexadecimal"), PerlSyntaxHighlighter.PERL_NUMBER_HEX),

      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.line.comment"), PerlSyntaxHighlighter.PERL_COMMENT),

      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.embedding.marker"), PerlSyntaxHighlighter.EMBED_MARKER_KEY),

      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.label"), PerlSyntaxHighlighter.PERL_LABEL),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.block.name"), PerlSyntaxHighlighter.PERL_BLOCK_NAME),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.special.token"), PerlSyntaxHighlighter.PERL_TAG),

      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.operator"), PerlSyntaxHighlighter.PERL_OPERATOR),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.keyword"), PerlSyntaxHighlighter.PERL_KEYWORD),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.dereference"), PerlSyntaxHighlighter.PERL_DEREFERENCE),

      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.annotation"), PerlSyntaxHighlighter.PERL_ANNOTATION),

      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.sub"), PerlSyntaxHighlighter.PERL_SUB),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.sub.built.in"), PerlSyntaxHighlighter.PERL_SUB_BUILTIN),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.sub.xs"), PerlSyntaxHighlighter.PERL_XSUB),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.sub.autoloaded"), PerlSyntaxHighlighter.PERL_AUTOLOAD),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.sub.constant"), PerlSyntaxHighlighter.PERL_CONSTANT),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.sub.definition"), PerlSyntaxHighlighter.PERL_SUB_DEFINITION),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.sub.declaration"), PerlSyntaxHighlighter.PERL_SUB_DECLARATION),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.attribute"), PerlSyntaxHighlighter.PERL_SUB_ATTRIBUTE),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.sub.prototype"), PerlSyntaxHighlighter.PERL_SUB_PROTOTYPE_TOKEN),

      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.package"), PerlSyntaxHighlighter.PERL_PACKAGE),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.package.pragma"), PerlSyntaxHighlighter.PERL_PACKAGE_PRAGMA),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.package.core"), PerlSyntaxHighlighter.PERL_PACKAGE_CORE),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.package.definition"),
                               PerlSyntaxHighlighter.PERL_PACKAGE_DEFINITION),

      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.variable.scalar"), PerlSyntaxHighlighter.PERL_SCALAR),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.variable.scalar.built.in"),
                               PerlSyntaxHighlighter.PERL_SCALAR_BUILTIN),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.variable.array"), PerlSyntaxHighlighter.PERL_ARRAY),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.variable.array.built.in"),
                               PerlSyntaxHighlighter.PERL_ARRAY_BUILTIN),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.variable.hash"), PerlSyntaxHighlighter.PERL_HASH),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.variable.hash.built.in"), PerlSyntaxHighlighter.PERL_HASH_BUILTIN),

      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.typeglob"), PerlSyntaxHighlighter.PERL_GLOB),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.typeglob.built.in"), PerlSyntaxHighlighter.PERL_GLOB_BUILTIN),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.typeglob.file.handle"), PerlSyntaxHighlighter.PERL_HANDLE),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.typeglob.file.handle.built.in"),
                               PerlSyntaxHighlighter.PERL_HANDLE_BUILTIN),

      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.string.single.quoted"), PerlSyntaxHighlighter.PERL_SQ_STRING),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.string.double.quoted"), PerlSyntaxHighlighter.PERL_DQ_STRING),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.string.backticked"), PerlSyntaxHighlighter.PERL_DX_STRING),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.string.control.chars"), PerlSyntaxHighlighter.PERL_STRING_SPECIAL),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.string.character.name"),
                               PerlSyntaxHighlighter.PERL_STRING_CHAR_NAME),

      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.regex.quote"), PerlSyntaxHighlighter.PERL_REGEX_QUOTE),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.regex.element"), PerlSyntaxHighlighter.PERL_REGEX_TOKEN),

      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.comma"), PerlSyntaxHighlighter.PERL_COMMA),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.semicolon"), PerlSyntaxHighlighter.PERL_SEMICOLON),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.braces"), PerlSyntaxHighlighter.PERL_BRACE),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.parentheses"), PerlSyntaxHighlighter.PERL_PAREN),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.brackets"), PerlSyntaxHighlighter.PERL_BRACK),
      new AttributesDescriptor(PerlBundle.message("attribute.descriptor.angles"), PerlSyntaxHighlighter.PERL_ANGLE)
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
