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

package com.perl5.lang.mojolicious.idea.editor.smartkeys;

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.perl5.lang.mojolicious.MojoliciousElementTypes;
import com.perl5.lang.mojolicious.psi.MojoliciousFileViewProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 08.01.2016.
 */
public class MojoliciousTypedHandler extends TypedHandlerDelegate implements MojoliciousElementTypes {
  @Override
  public Result charTyped(char c, final Project project, @NotNull final Editor editor, @NotNull PsiFile file) {
    if (file.getViewProvider() instanceof MojoliciousFileViewProvider) {
      if (c == ' ') {
        MojoliciousSmartKeysUtil.addCloseMarker(editor, file, " " + KEYWORD_MOJO_BLOCK_CLOSER);
      }
    }
    return super.charTyped(c, project, editor, file);
  }
}
