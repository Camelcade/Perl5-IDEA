/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.tt2.idea.highlighting;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.perl5.lang.tt2.TemplateToolkitBundle;
import com.perl5.lang.tt2.TemplateToolkitIcons;
import com.perl5.lang.tt2.TemplateToolkitLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;


public class TemplateToolkitColorSettingsPage implements ColorSettingsPage {

  @Override
  public @Nullable Icon getIcon() {
    return TemplateToolkitIcons.TTK2_ICON;
  }

  @Override
  public @NotNull SyntaxHighlighter getHighlighter() {
    return new TemplateToolkitSyntaxHighlighter(null);
  }

  @Override
  public @NotNull String getDemoText() {
    return """
      %% <kw>SET</kw> <id>somekey</id> <op>=</op> <sqs>'single quoted string'</sqs>
      [% <kw>SET</kw> <id>somekey</id> <op>=</op> <dqs>"double quoted string"</dqs> %]
      [% <kw>SET</kw> <id>somekey</id> <op>=</op> 42 %]
      [% <id>somekey</id> <op>and</op> <id>someotherkey</id> <op>or</op> <id>somethingelse</id> %]
      %%# line comment
      [%# block comment  %]
      """;
  }

  @Override
  public @Nullable Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    return Map.of(
      "kw", TemplateToolkitSyntaxHighlighter.TT2_KEYWORD_KEY,
      "id", TemplateToolkitSyntaxHighlighter.TT2_IDENTIFIER_KEY,
      "op", TemplateToolkitSyntaxHighlighter.TT2_OPERATOR_KEY,
      "sqs", TemplateToolkitSyntaxHighlighter.TT2_SQ_STRING_KEY,
      "dqs", TemplateToolkitSyntaxHighlighter.TT2_DQ_STRING_KEY
    );
  }

  @Override
  public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
    return new AttributesDescriptor[]{
      new AttributesDescriptor(TemplateToolkitBundle.message("attribute.descriptor.markers"),
                               TemplateToolkitSyntaxHighlighter.TT2_MARKER_KEY),
      new AttributesDescriptor(TemplateToolkitBundle.message("attribute.descriptor.numbers"),
                               TemplateToolkitSyntaxHighlighter.TT2_NUMBER_KEY),
      new AttributesDescriptor(TemplateToolkitBundle.message("attribute.descriptor.comments"),
                               TemplateToolkitSyntaxHighlighter.TT2_COMMENT_KEY),
      new AttributesDescriptor(TemplateToolkitBundle.message("attribute.descriptor.identifiers"),
                               TemplateToolkitSyntaxHighlighter.TT2_IDENTIFIER_KEY),
      new AttributesDescriptor(TemplateToolkitBundle.message("attribute.descriptor.keywords"),
                               TemplateToolkitSyntaxHighlighter.TT2_KEYWORD_KEY),
      new AttributesDescriptor(TemplateToolkitBundle.message("attribute.descriptor.operators"),
                               TemplateToolkitSyntaxHighlighter.TT2_OPERATOR_KEY),
      new AttributesDescriptor(TemplateToolkitBundle.message("attribute.descriptor.strings.single.quoted"),
                               TemplateToolkitSyntaxHighlighter.TT2_SQ_STRING_KEY),
      new AttributesDescriptor(TemplateToolkitBundle.message("attribute.descriptor.strings.double.quoted"),
                               TemplateToolkitSyntaxHighlighter.TT2_DQ_STRING_KEY),
    };
  }

  @Override
  public ColorDescriptor @NotNull [] getColorDescriptors() {
    return ColorDescriptor.EMPTY_ARRAY;
  }

  @Override
  public @NotNull String getDisplayName() {
    return TemplateToolkitLanguage.NAME;
  }
}
