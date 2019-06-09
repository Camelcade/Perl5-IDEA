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

package com.perl5.lang.perl.idea.folding;

import com.intellij.application.options.editor.CodeFoldingOptionsProvider;
import com.intellij.openapi.options.BeanConfigurable;
import com.perl5.PerlBundle;

import javax.swing.*;


public class PerlCodeFoldingOptionsProvider extends BeanConfigurable<PerlFoldingSettings> implements CodeFoldingOptionsProvider {
  public PerlCodeFoldingOptionsProvider(PerlFoldingSettings beanInstance) {
    super(beanInstance);

    checkBox(PerlBundle.message("perl.options.fold.comments"),
             () -> getInstance().COLLAPSE_COMMENTS, value -> getInstance().COLLAPSE_COMMENTS = value);
    checkBox(PerlBundle.message("perl.options.fold.constants"),
             () -> getInstance().COLLAPSE_CONSTANT_BLOCKS, value -> getInstance().COLLAPSE_CONSTANT_BLOCKS = value);
    checkBox(PerlBundle.message("perl.options.fold.arrays"),
             () -> getInstance().COLLAPSE_ANON_ARRAYS, value -> getInstance().COLLAPSE_ANON_ARRAYS = value);
    checkBox(PerlBundle.message("perl.options.fold.hashes"),
             () -> getInstance().COLLAPSE_ANON_HASHES, value -> getInstance().COLLAPSE_ANON_HASHES = value);
    checkBox(PerlBundle.message("perl.options.fold.parens"),
             () -> getInstance().COLLAPSE_PARENTHESISED, value -> getInstance().COLLAPSE_PARENTHESISED = value);
    checkBox(PerlBundle.message("perl.options.fold.heredocs"),
             () -> getInstance().COLLAPSE_HEREDOCS, value -> getInstance().COLLAPSE_HEREDOCS = value);
    checkBox(PerlBundle.message("perl.options.fold.templates"),
             () -> getInstance().COLLAPSE_TEMPLATES, value -> getInstance().COLLAPSE_TEMPLATES = value);
    checkBox(PerlBundle.message("perl.options.fold.qw"),
             () -> getInstance().COLLAPSE_QW, value -> getInstance().COLLAPSE_QW = value);
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
