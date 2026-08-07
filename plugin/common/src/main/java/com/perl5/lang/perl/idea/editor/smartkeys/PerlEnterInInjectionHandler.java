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

package com.perl5.lang.perl.idea.editor.smartkeys;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Restores smart indent inside a here-doc with an injected language.
 * <p>
 * When the caret is inside an injection, {@code EnterHandler} resolves the language from the data context and gets the
 * injected one, so the platform picks the {@code LineIndentProvider} of the injected language and never falls back to the
 * formatter. The host formatting model, the very one reformatting uses to lay the fragment out correctly, is not consulted
 * and the new line ends up in the first column. Re-running the formatter based indent adjustment for that single line
 * yields exactly what reformatting would produce.
 */
public class PerlEnterInInjectionHandler extends PerlEnterHandler {
  @Override
  protected Result doPostProcessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull DataContext dataContext) {
    Project project = file.getProject();
    InjectedLanguageManager injectedManager = InjectedLanguageManager.getInstance(project);
    PsiFile hostFile = injectedManager.getTopLevelFile(file);
    if (hostFile == file || !hostFile.getLanguage().is(PerlLanguage.INSTANCE)) {
      return Result.Continue;
    }

    Document hostDocument = PsiDocumentManager.getInstance(project).getDocument(hostFile);
    if (hostDocument == null) {
      return Result.Continue;
    }

    int hostOffset = injectedManager.injectedToHost(file, editor.getCaretModel().getOffset());
    PsiElement elementAtCaret = hostFile.findElementAt(hostOffset);
    PerlHeredocElementImpl heredoc = PsiTreeUtil.getParentOfType(elementAtCaret, PerlHeredocElementImpl.class, false);
    if (heredoc == null) {
      return Result.Continue;
    }

    PsiDocumentManager.getInstance(project).commitDocument(hostDocument);
    CodeStyleManager.getInstance(project).adjustLineIndent(hostFile, hostOffset);
    return Result.Continue;
  }
}
