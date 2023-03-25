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

package com.perl5.lang.perl.idea.folding;

import com.intellij.application.options.editor.CodeFoldingOptionsProvider;
import com.intellij.openapi.options.BeanConfigurable;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.PerlLanguage;


public class PerlFoldingOptionsProvider extends BeanConfigurable<PerlFoldingSettingsImpl> implements CodeFoldingOptionsProvider {
  public PerlFoldingOptionsProvider() {
    super(PerlFoldingSettingsImpl.getInstance(), PerlLanguage.INSTANCE.getDisplayName());

    PerlFoldingSettingsImpl foldingSettings = PerlFoldingSettingsImpl.getInstance();
    checkBox(PerlBundle.message("perl.options.fold.comments"),
             () -> foldingSettings.COLLAPSE_COMMENTS, value -> foldingSettings.COLLAPSE_COMMENTS = value);
    checkBox(PerlBundle.message("perl.options.fold.constants"),
             () -> foldingSettings.COLLAPSE_CONSTANT_BLOCKS, value -> foldingSettings.COLLAPSE_CONSTANT_BLOCKS = value);
    checkBox(PerlBundle.message("perl.options.fold.arrays"),
             () -> foldingSettings.COLLAPSE_ANON_ARRAYS, value -> foldingSettings.COLLAPSE_ANON_ARRAYS = value);
    checkBox(PerlBundle.message("perl.options.fold.hashes"),
             () -> foldingSettings.COLLAPSE_ANON_HASHES, value -> foldingSettings.COLLAPSE_ANON_HASHES = value);
    checkBox(PerlBundle.message("perl.options.fold.parens"),
             () -> foldingSettings.COLLAPSE_PARENTHESISED, value -> foldingSettings.COLLAPSE_PARENTHESISED = value);
    checkBox(PerlBundle.message("perl.options.fold.heredocs"),
             () -> foldingSettings.COLLAPSE_HEREDOCS, value -> foldingSettings.COLLAPSE_HEREDOCS = value);
    checkBox(PerlBundle.message("perl.options.fold.templates"),
             () -> foldingSettings.COLLAPSE_TEMPLATES, value -> foldingSettings.COLLAPSE_TEMPLATES = value);
    checkBox(PerlBundle.message("perl.options.fold.qw"),
             () -> foldingSettings.COLLAPSE_QW, value -> foldingSettings.COLLAPSE_QW = value);
    checkBox(PerlBundle.message("perl.options.fold.char.substitutions"),
             () -> foldingSettings.COLLAPSE_CHAR_SUBSTITUTIONS, value -> foldingSettings.COLLAPSE_CHAR_SUBSTITUTIONS = value);
  }
}
