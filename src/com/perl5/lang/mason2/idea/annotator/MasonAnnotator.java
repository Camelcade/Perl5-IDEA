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

package com.perl5.lang.mason2.idea.annotator;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.psi.PsiElement;
import com.perl5.lang.mason2.psi.MasonMethodModifierName;
import com.perl5.lang.perl.idea.annotators.PerlBaseAnnotator;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 28.01.2016.
 */
public class MasonAnnotator extends PerlBaseAnnotator {
  @Override
  public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
    if (element instanceof MasonMethodModifierName) {
      holder.createInfoAnnotation(element, null).setTextAttributes(PerlSyntaxHighlighter.PERL_SUB_DEFINITION);
    }
  }
}
