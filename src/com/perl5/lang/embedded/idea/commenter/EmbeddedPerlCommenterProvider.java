/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.embedded.idea.commenter;

import com.intellij.lang.Commenter;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageCommenters;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.templateLanguages.MultipleLangCommentProvider;
import com.perl5.lang.embedded.psi.EmbeddedPerlFileViewProvider;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 04.09.2015.
 * This provider is a temporary solution, allows to comment html parts of the template, see #414
 */
public class EmbeddedPerlCommenterProvider implements MultipleLangCommentProvider {
  @Nullable
  @Override
  public Commenter getLineCommenter(PsiFile file, Editor editor, Language lineStartLanguage, Language lineEndLanguage) {
    return LanguageCommenters.INSTANCE.forLanguage(lineStartLanguage);
  }

  @Override
  public boolean canProcess(PsiFile file, FileViewProvider viewProvider) {
    return viewProvider instanceof EmbeddedPerlFileViewProvider;
  }
}
