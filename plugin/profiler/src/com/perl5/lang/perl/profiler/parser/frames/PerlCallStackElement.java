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

import com.google.common.annotations.VisibleForTesting;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.profiler.api.BaseCallStackElement;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.psi.PerlDelegatingFakeElement;
import com.perl5.lang.perl.psi.PerlRecursiveVisitor;
import com.perl5.lang.perl.psi.PsiPerlTryExpr;
import com.perl5.lang.perl.psi.PsiPerlTrycatchCompound;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@VisibleForTesting
public class PerlCallStackElement extends BaseCallStackElement {
  private static final String TRY_TINY_SUFFIX = "::try {...}";
  private static final int MAX_FILE_TO_OPEN = 10;

  private final @NotNull String myName;
  private final @NotNull String myFullName;

  public PerlCallStackElement(@NotNull String name) {
    myName = name.trim();

    var tryTinySuffixIndex = myName.indexOf(TRY_TINY_SUFFIX);
    if (tryTinySuffixIndex > -1) {
      myFullName = "try in " + PerlPackageUtil.getCanonicalName(myName.substring(0, tryTinySuffixIndex));
      return;
    }
    myFullName = myName;
  }

  @Override
  public @NotNull String fullName() {
    return myFullName;
  }

  @Override
  public boolean isNavigatable() {
    return true;
  }

  @Override
  public @NotNull NavigatablePsiElement[] calcNavigatables(@NotNull Project project) {
    var perlSdk = PerlProjectManager.getSdk(project);
    if (perlSdk == null) {
      return NavigatablePsiElement.EMPTY_NAVIGATABLE_ELEMENT_ARRAY;
    }
    List<NavigatablePsiElement> result;

    if (myName.contains(TRY_TINY_SUFFIX)) {
      if (PerlPackageUtil.MAIN_NAMESPACE_NAME.equals(myFullName)) {
        return NavigatablePsiElement.EMPTY_NAVIGATABLE_ELEMENT_ARRAY;
      }
      result = ReadAction.compute(() -> computeTryNavigatables(project));
    }
    else {
      result = ReadAction.compute(() -> computeFqnNavigatables(project));
    }

    return result.toArray(NavigatablePsiElement.EMPTY_NAVIGATABLE_ELEMENT_ARRAY);
  }

  private @NotNull List<NavigatablePsiElement> computeFqnNavigatables(@NotNull Project project) {
    List<NavigatablePsiElement> result = new ArrayList<>();
    PerlSubUtil.processRelatedItems(project, GlobalSearchScope.allScope(project), myName, it -> {
      if (it instanceof NavigatablePsiElement) {
        result.add((NavigatablePsiElement)it);
      }
      return true;
    });
    return result;
  }

  private @NotNull List<NavigatablePsiElement> computeTryNavigatables(@NotNull Project project) {
    List<NavigatablePsiElement> result = new ArrayList<>();
    Set<PsiFile> processedFiles = new HashSet<>();
    PerlPackageUtil.processNamespaces(
      myName.substring(0, myName.length() - TRY_TINY_SUFFIX.length()), project, GlobalSearchScope.allScope(project),
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

  @Override
  public String toString() {
    return myName;
  }
}
