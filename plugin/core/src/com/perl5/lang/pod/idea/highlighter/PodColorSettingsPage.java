/*
 * Copyright 2015-2019 Alexandr Evstigneev
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


import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class PodColorSettingsPage implements ColorSettingsPage {
  private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
    new AttributesDescriptor(PerlBundle.message("pod.color.settings.commands"), PodSyntaxHighlighter.POD_TAG_KEY),
    new AttributesDescriptor(PerlBundle.message("pod.color.settings.verbatim"), PodSyntaxHighlighter.POD_VERBATIM_KEY),
    new AttributesDescriptor(PerlBundle.message("pod.color.settings.paragraph"), PodSyntaxHighlighter.POD_TEXT_KEY),
    new AttributesDescriptor(PerlBundle.message("pod.color.settings.formatter"), PodSyntaxHighlighter.POD_FORMATTER_TAG_KEY),
    new AttributesDescriptor(PerlBundle.message("pod.color.settings.formatter.markup"), PodSyntaxHighlighter.POD_FORMATTER_MARKUP_KEY),
  };

  @Nullable
  @Override
  public Icon getIcon() {
    return PerlIcons.POD_FILE;
  }

  @NotNull
  @Override
  public SyntaxHighlighter getHighlighter() {
    return new PodSyntaxHighlighter(null);
  }

  @NotNull
  @Override
  public String getDemoText() {
    return CodeStyleAbstractPanel.readFromFile(PodColorSettingsPage.class, "podColorsSample.txt");
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
    return "Perl5 POD";
  }
}
