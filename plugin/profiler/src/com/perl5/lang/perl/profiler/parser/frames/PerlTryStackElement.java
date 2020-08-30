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

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.lang.perl.psi.PerlDelegatingFakeElement;
import com.perl5.lang.perl.psi.PerlRecursiveVisitor;
import com.perl5.lang.perl.psi.PsiPerlTryExpr;
import com.perl5.lang.perl.psi.PsiPerlTrycatchCompound;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Frame created by Try::Tiny {@code Foo::Bar::try {...} }
 *
 * @implNote it's not possible to deduce exact try in the namespace. So, for now we are iterating all files up to {@link #MAX_FILE_TO_OPEN},
 * parsing them, traversing and collecting all try expressions and compounds.
 */
class PerlTryStackElement extends PerlCallStackElement {
  private final @NotNull String myNamespaceName;

  PerlTryStackElement(@NotNull String frameText) {
    super(frameText);
    var cleanedFrameText = getFrameText();
    var tryTinySuffixIndex = cleanedFrameText.indexOf(TRY_TINY_SUFFIX);

    if (tryTinySuffixIndex < 0) {
      LOG.error("Attempting to create try frame from non-try text: " + frameText);
    }
    myNamespaceName = PerlPackageUtil.getCanonicalName(cleanedFrameText.substring(0, tryTinySuffixIndex));
  }

  @Override
  public @NotNull String fullName() {
    return "try in " + myNamespaceName;
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
          psiFile.accept(new PerlRecursiveVisitor() {
            @Override
            public void visitTrycatchCompound(@NotNull PsiPerlTrycatchCompound o) {
              if (o instanceof NavigatablePsiElement) {
                result.add(createDelegate((NavigatablePsiElement)o));
              }
              super.visitTrycatchCompound(o);
            }

            @Override
            public void visitTryExpr(@NotNull PsiPerlTryExpr o) {
              if (o instanceof NavigatablePsiElement) {
                result.add(createDelegate((NavigatablePsiElement)o));
              }
              super.visitTryExpr(o);
            }

            private PerlDelegatingFakeElement createDelegate(@NotNull NavigatablePsiElement originalElement) {
              return new PerlDelegatingFakeElement(originalElement) {
                @Override
                public String getPresentableText() {
                  return StringUtil.shortenTextWithEllipsis(StringUtil.notNullize(getText()), 80, 5, true);
                }

                @Override
                public @Nullable String getLocationString() {
                  var containingFile = getContainingFile();
                  if (containingFile == null) {
                    return super.getLocationString();
                  }
                  var document = PsiDocumentManager.getInstance(getProject()).getDocument(containingFile);
                  if (document == null) {
                    return super.getLocationString();
                  }
                  return String
                    .join(" ", containingFile.getName(), Integer.toString(document.getLineNumber(getTextOffset())));
                }
              };
            }
          });
        }
        return processedFiles.size() < MAX_FILE_TO_OPEN;
      });
    return result;
  }
}
