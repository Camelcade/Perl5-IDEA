/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.intellij.openapi.util.Pair;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlIcons;
import com.perl5.lang.tt2.TemplateToolkitLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

/**
 * Created by hurricup on 05.07.2016.
 */
public class TemplateToolkitColorSettingsPage implements ColorSettingsPage {
  private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
    new AttributesDescriptor("Markers", TemplateToolkitSyntaxHighlighter.TT2_MARKER_KEY),
    new AttributesDescriptor("Numbers", TemplateToolkitSyntaxHighlighter.TT2_NUMBER_KEY),
    new AttributesDescriptor("Comments", TemplateToolkitSyntaxHighlighter.TT2_COMMENT_KEY),
    new AttributesDescriptor("Identifiers", TemplateToolkitSyntaxHighlighter.TT2_IDENTIFIER_KEY),
    new AttributesDescriptor("Keywords", TemplateToolkitSyntaxHighlighter.TT2_KEYWORD_KEY),
    new AttributesDescriptor("Operators", TemplateToolkitSyntaxHighlighter.TT2_OPERATOR_KEY),
    new AttributesDescriptor("Strings, single quoted", TemplateToolkitSyntaxHighlighter.TT2_SQ_STRING_KEY),
    new AttributesDescriptor("Strings, double quoted", TemplateToolkitSyntaxHighlighter.TT2_DQ_STRING_KEY),
  };

  @Nullable
  @Override
  public Icon getIcon() {
    return PerlIcons.TTK2_ICON;
  }

  @NotNull
  @Override
  public SyntaxHighlighter getHighlighter() {
    return new TemplateToolkitSyntaxHighlighter(null);
  }

  @NotNull
  @Override
  public String getDemoText() {
    return "%% <kw>SET</kw> <id>somekey</id> <op>=</op> <sqs>'single quoted string'</sqs>\n" +
           "[% <kw>SET</kw> <id>somekey</id> <op>=</op> <dqs>\"double quoted string\"</dqs> %]\n" +
           "[% <kw>SET</kw> <id>somekey</id> <op>=</op> 42 %]\n" +
           "[% <id>somekey</id> <op>and</op> <id>someotherkey</id> <op>or</op> <id>somethingelse</id> %]\n" +
           "%%# line comment\n" +
           "[%# block comment  %]\n";
  }

  @Nullable
  @Override
  public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    //noinspection unchecked
    return ContainerUtil.newHashMap(
      Pair.create("kw", TemplateToolkitSyntaxHighlighter.TT2_KEYWORD_KEY),
      Pair.create("id", TemplateToolkitSyntaxHighlighter.TT2_IDENTIFIER_KEY),
      Pair.create("op", TemplateToolkitSyntaxHighlighter.TT2_OPERATOR_KEY),
      Pair.create("sqs", TemplateToolkitSyntaxHighlighter.TT2_SQ_STRING_KEY),
      Pair.create("dqs", TemplateToolkitSyntaxHighlighter.TT2_DQ_STRING_KEY)
    );
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
    return TemplateToolkitLanguage.NAME;
  }
}
