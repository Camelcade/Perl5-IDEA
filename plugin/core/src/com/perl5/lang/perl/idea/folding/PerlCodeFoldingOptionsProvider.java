/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.folding;

import com.intellij.application.options.editor.CodeFoldingOptionsProvider;
import com.intellij.openapi.options.BeanConfigurable;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;


public class PerlCodeFoldingOptionsProvider extends BeanConfigurable<PerlFoldingSettings> implements CodeFoldingOptionsProvider {
  public PerlCodeFoldingOptionsProvider(@NotNull PerlFoldingSettings beanInstance) {
    super(beanInstance);

    checkBox(PerlBundle.message("perl.options.fold.comments"),
             () -> beanInstance.COLLAPSE_COMMENTS, value -> beanInstance.COLLAPSE_COMMENTS = value);
    checkBox(PerlBundle.message("perl.options.fold.constants"),
             () -> beanInstance.COLLAPSE_CONSTANT_BLOCKS, value -> beanInstance.COLLAPSE_CONSTANT_BLOCKS = value);
    checkBox(PerlBundle.message("perl.options.fold.arrays"),
             () -> beanInstance.COLLAPSE_ANON_ARRAYS, value -> beanInstance.COLLAPSE_ANON_ARRAYS = value);
    checkBox(PerlBundle.message("perl.options.fold.hashes"),
             () -> beanInstance.COLLAPSE_ANON_HASHES, value -> beanInstance.COLLAPSE_ANON_HASHES = value);
    checkBox(PerlBundle.message("perl.options.fold.parens"),
             () -> beanInstance.COLLAPSE_PARENTHESISED, value -> beanInstance.COLLAPSE_PARENTHESISED = value);
    checkBox(PerlBundle.message("perl.options.fold.heredocs"),
             () -> beanInstance.COLLAPSE_HEREDOCS, value -> beanInstance.COLLAPSE_HEREDOCS = value);
    checkBox(PerlBundle.message("perl.options.fold.templates"),
             () -> beanInstance.COLLAPSE_TEMPLATES, value -> beanInstance.COLLAPSE_TEMPLATES = value);
    checkBox(PerlBundle.message("perl.options.fold.qw"),
             () -> beanInstance.COLLAPSE_QW, value -> beanInstance.COLLAPSE_QW = value);
  }

  @Override
  public JComponent createComponent() {
    JComponent result = super.createComponent();
    if (result != null) {
      result.setBorder(BorderFactory.createTitledBorder("Perl5"));
    }
    return result;
  }
}
