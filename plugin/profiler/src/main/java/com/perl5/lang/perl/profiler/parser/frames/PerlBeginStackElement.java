/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.perl.profiler.parser.frames;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElementBase;
import com.perl5.lang.perl.psi.impl.PsiPerlNamedBlockImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Frame created for use statements and BEGIN blocks. Looks like {@code Foo::Bar::BEGIN@123}
 *
 * @implNote it's not possible to deduce exact file. So, for now we are iterating all files up to {@link #MAX_FILE_TO_OPEN},
 * parsing them, traversing and collecting all matching statements and blocks.
 */
class PerlBeginStackElement extends PerlCallStackElement {
  private final @NotNull String myNamespaceName;
  private final int myLineNumber;

  PerlBeginStackElement(@NotNull String frameText) {
    super(frameText);
    var cleanedFrameText = getFrameText();
    var beginStartSuffix = cleanedFrameText.indexOf(BEGIN_BLOCK_SUFFIX);

    if (beginStartSuffix < 0) {
      LOG.error("Attempting to create try frame from non-try text: " + frameText);
    }
    myNamespaceName = PerlPackageUtil.getCanonicalName(cleanedFrameText.substring(0, beginStartSuffix));
    int lineNumber = 1;
    try {
      var lineNumberInfo = cleanedFrameText.substring(beginStartSuffix + BEGIN_BLOCK_SUFFIX.length());
      var lineNumberParts = StringUtil.split(lineNumberInfo, ".");
      lineNumber = Integer.parseInt(lineNumberParts.getFirst());
    }
    catch (NumberFormatException e) {
      LOG.warn("Unable to parse line number from: " + cleanedFrameText + "; " + e.getMessage());
    }
    myLineNumber = lineNumber;
  }

  @Override
  public @NotNull String fullName() {
    return "BEGIN in " + myNamespaceName + " " + myLineNumber;
  }

  @Override
  protected @NotNull List<NavigatablePsiElement> computeNavigatables(@NotNull Project project, @NotNull Sdk perlSdk) {
    if (PerlPackageUtil.MAIN_NAMESPACE_NAME.equals(myNamespaceName)) {
      return Collections.emptyList();
    }
    List<NavigatablePsiElement> result = new ArrayList<>();
    Set<PsiFile> processedFiles = new HashSet<>();
    PerlPackageUtil.processNamespaces(
      myNamespaceName, project, GlobalSearchScope.allScope(project),
      it -> {
        var psiFile = it.getContainingFile();
        if (processedFiles.add(psiFile)) {
          ProgressManager.checkCanceled();
          var documentManager = PsiDocumentManager.getInstance(project);
          var document = documentManager.getDocument(psiFile);
          if (document == null || document.getLineCount() < myLineNumber) {
            return true;
          }

          var startOffset = document.getLineStartOffset(myLineNumber - 1);
          var endOffset = document.getLineEndOffset(myLineNumber - 1);

          var useStatement = PerlPsiUtil.findElementOfClassAtRange(psiFile, startOffset, endOffset, PerlUseStatementElementBase.class);
          if (myNamespaceName.equals(PerlPackageUtil.getContextNamespaceName(useStatement))) {
            result.add(useStatement);
          }
          else {
            var namedBlockAtOffset = PerlPsiUtil.findElementOfClassAtRange(psiFile, startOffset, endOffset, PsiPerlNamedBlockImpl.class);
            if (myNamespaceName.equals(PerlPackageUtil.getContextNamespaceName(namedBlockAtOffset))) {
              result.add(new PerlTargetElementWrapper(namedBlockAtOffset) {
                @Override
                public void navigate(boolean requestFocus) {
                  new OpenFileDescriptor(getProject(), psiFile.getVirtualFile(), startOffset).navigate(requestFocus);
                }
              });
            }
          }
        }
        return processedFiles.size() < MAX_FILE_TO_OPEN;
      });
    return result;
  }
}
