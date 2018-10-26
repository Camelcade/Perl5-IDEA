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

package com.perl5.lang.perl.idea.inspections;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.adapters.CpanAdapter;
import com.perl5.lang.perl.adapters.CpanminusAdapter;
import com.perl5.lang.perl.adapters.PackageManagerAdapter;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 14.06.2015.
 */
public class PerlUnresolvedPackageFileInspection extends PerlInspection {
  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull final ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {

      @Override
      public void visitUseStatement(@NotNull PsiPerlUseStatement o) {
        if (o.getNamespaceElement() != null) {
          checkPackageFile(o.getNamespaceElement());
        }
      }

      @Override
      public void visitNoStatement(@NotNull PsiPerlNoStatement o) {
        if (o.getNamespaceElement() != null) {
          checkPackageFile(o.getNamespaceElement());
        }
      }

      @Override
      public void visitRequireExpr(@NotNull PsiPerlRequireExpr o) {
        if (o.getNamespaceElement() != null) {
          checkPackageFile(o.getNamespaceElement());
        }
      }

      public void checkPackageFile(PerlNamespaceElement o) {
        List<PerlFileImpl> namespaceFiles = o.getNamespaceFiles();
        String packageName = o.getCanonicalName();

        if (namespaceFiles.isEmpty() && StringUtil.isNotEmpty(packageName)) {
          List<LocalQuickFix> fixes = new ArrayList<>();
          Project project = o.getProject();
          Sdk perlSdk = PerlProjectManager.getSdk(project);
          if (perlSdk != null) {
            if (CpanminusAdapter.isAvailable(perlSdk)) {
              fixes.add(new InstallPackageQuickfix(new CpanminusAdapter(perlSdk, project), packageName));
            }
            else {
              fixes.add(new InstallPackageQuickfix(new CpanAdapter(perlSdk, project), packageName));
            }
          }

          registerProblem(holder, o,
                          PerlBundle.message("perl.inspection.missing.package.file", packageName),
                          fixes.toArray(LocalQuickFix.EMPTY_ARRAY));
        }
      }
    };
  }

  private static class InstallPackageQuickfix implements LocalQuickFix {
    @NotNull
    private final PackageManagerAdapter myAdapter;

    @NotNull
    private final String myPackageName;

    public InstallPackageQuickfix(@NotNull PackageManagerAdapter adapter, @NotNull String packageName) {
      myAdapter = adapter;
      myPackageName = packageName;
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getFamilyName() {
      return PerlBundle.message("perl.quickfix.install.family", myAdapter.getPresentableName());
    }

    @Nls(capitalization = Nls.Capitalization.Sentence)
    @NotNull
    @Override
    public String getName() {
      return PerlBundle.message("perl.quickfix.install.name", myPackageName, myAdapter.getPresentableName());
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
      myAdapter.install(myPackageName);
    }
  }
}
