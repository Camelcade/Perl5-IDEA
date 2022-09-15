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

package com.perl5.lang.perl.profiler.parser.frames;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.psi.PerlDelegatingFakeElement;
import com.perl5.lang.perl.psi.impl.PsiPerlExprImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collections;
import java.util.List;

class PerlAnonStubStackElement extends PerlCallStackElement {
  private final @NotNull String myFilePath;
  private final int myLineNumber;
  private final @NotNull String myDisplayName;

  PerlAnonStubStackElement(@NotNull String frameText) {
    super(frameText);
    var anonSubSuffixOffset = frameText.indexOf(ANON_SUB_WITH_LINE_SUFFIX);
    var pathStartOffset = anonSubSuffixOffset + ANON_SUB_WITH_LINE_SUFFIX.length();
    var pathEndOffset = frameText.length() - 1;
    var evaled = false;
    if (anonSubSuffixOffset < 0 || pathStartOffset >= pathEndOffset) {
      LOG.error("Attempting to create anon suffix with wrong data: " + frameText);
      myFilePath = "";
      myLineNumber = 0;
    }
    else {
      var pathWithLineNumber = frameText.substring(pathStartOffset, pathEndOffset);
      var evalOffset = pathWithLineNumber.lastIndexOf(")[");
      if (evalOffset > -1) {
        var evalEndOffset = pathWithLineNumber.indexOf(']', evalOffset);
        if (evalEndOffset < 0) {
          evalEndOffset = pathWithLineNumber.length();
        }
        pathWithLineNumber = pathWithLineNumber.substring(evalOffset + 2, evalEndOffset);
        evaled = true;
      }

      var lastColonOffset = pathWithLineNumber.lastIndexOf(':');
      if (lastColonOffset > 0) {
        myFilePath = pathWithLineNumber.substring(0, lastColonOffset);
        var lineNumberOffset = lastColonOffset + 1;
        var lineNumber = 0;
        if (lineNumberOffset < pathWithLineNumber.length()) {
          try {
            lineNumber = Integer.parseInt(pathWithLineNumber.substring(lineNumberOffset));
          }
          catch (NumberFormatException ignore) {
          }
        }
        myLineNumber = lineNumber;
      }
      else {
        myFilePath = pathWithLineNumber;
        myLineNumber = 0;
      }
    }

    myDisplayName = "sub {...}" +
                    (evaled ? " (evaled)" : "") +
                    " in " +
                    (myFilePath.isEmpty() ? "unknown file" : new File(myFilePath).getName() + " " + myLineNumber);
  }

  @Override
  public @NotNull String fullName() {
    return myDisplayName;
  }

  @Override
  protected @NotNull List<NavigatablePsiElement> computeNavigatables(@NotNull Project project, @NotNull Sdk perlSdk) {
    PerlHostData<?, ?> hostData = PerlHostData.notNullFrom(perlSdk);
    var localPath = hostData.getLocalPath(myFilePath);
    if (localPath == null) {
      return Collections.emptyList();
    }
    var virtualFile = VfsUtil.findFileByIoFile(new File(localPath), false);
    if (virtualFile == null) {
      return Collections.emptyList();
    }

    var psiFile = PsiUtilCore.getPsiFile(project, virtualFile);
    return Collections.singletonList(new PerlDelegatingFakeElement(psiFile) {
      @Override
      public String getPresentableText() {
        return "sub {...}";
      }

      @Override
      public @Nullable String getLocationString() {
        var filePresentation = psiFile.getPresentation();
        return filePresentation == null ? null : filePresentation.getLocationString();
      }

      @Override
      public void navigate(boolean requestFocus) {
        var documentManager = PsiDocumentManager.getInstance(project);
        var document = documentManager.getDocument(psiFile);
        if (document == null) {
          return;
        }
        var startOffset = document.getLineStartOffset(myLineNumber - 1);
        var subExpr = PsiTreeUtil.findElementOfClassAtOffset(psiFile, startOffset, PsiPerlExprImpl.class, false);
        if (subExpr != null) {
          subExpr.navigate(requestFocus);
        }
        else {
          new OpenFileDescriptor(project, virtualFile, startOffset).navigate(requestFocus);
        }
      }
    });
  }
}
