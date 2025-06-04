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

package com.perl5.lang.perl.idea.annotators;

import com.intellij.codeInspection.util.InspectionMessage;
import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public abstract class PerlBaseAnnotator implements Annotator, PerlElementTypes {
  final EditorColorsScheme currentScheme = EditorColorsManager.getInstance().getGlobalScheme();

  public TextAttributes adjustTextAttributes(TextAttributes textAttributes, boolean isDeprecated) {
    if (isDeprecated) {
      textAttributes = TextAttributes.merge(textAttributes, currentScheme.getAttributes(CodeInsightColors.DEPRECATED_ATTRIBUTES));
    }
    return textAttributes;
  }

  public static void createInfoAnnotation(@NotNull AnnotationHolder holder,
                                          @Nullable PsiElement element,
                                          @InspectionMessage @Nullable String message,
                                          @NotNull TextAttributesKey key) {
    if (element == null) {
      return;
    }
    AnnotationBuilder annotationBuilder = message == null ?
                                          holder.newSilentAnnotation(HighlightSeverity.INFORMATION) :
                                          holder.newAnnotation(HighlightSeverity.INFORMATION, message);
    annotationBuilder.range(element).textAttributes(key).create();
  }
}
