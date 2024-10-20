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

package com.perl5.lang.pod.idea.inspections;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import com.perl5.PerlBundle;
import com.perl5.lang.pod.parser.psi.PodLinkDescriptor;
import com.perl5.lang.pod.parser.psi.PodVisitor;
import com.perl5.lang.pod.parser.psi.mixin.PodFormatterL;
import com.perl5.lang.pod.parser.psi.references.PodLinkToFileReference;
import com.perl5.lang.pod.parser.psi.references.PodLinkToSectionReference;
import com.perl5.lang.pod.psi.PsiPodFormatLink;
import org.jetbrains.annotations.NotNull;


public class PodUnresolvableLinkInspection extends LocalInspectionTool {
  @Override
  public @NotNull PsiElementVisitor buildVisitor(final @NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new PodVisitor() {
      @Override
      public void visitPodFormatLink(@NotNull PsiPodFormatLink o) {
        assert o instanceof PodFormatterL;
        PodFormatterL link = (PodFormatterL)o;
        for (PsiReference reference : link.getReferences()) {
          if (reference instanceof PsiPolyVariantReference && ((PsiPolyVariantReference)reference).multiResolve(false).length == 0) {
            String error;

            if (reference instanceof PodLinkToFileReference) {
              String fileName = "UNKNOWN";
              PodLinkDescriptor descriptor = link.getLinkDescriptor();

              if (descriptor != null && descriptor.getName() != null) {
                fileName = descriptor.getName();
              }

              error = PerlBundle.message("inspection.message.can.t.find.pod.or.pm.file.by", fileName);
            }
            else if (reference instanceof PodLinkToSectionReference) {
              String fileName = "UNKNONW";
              PodLinkDescriptor descriptor = link.getLinkDescriptor();

              if (descriptor != null && descriptor.getSection() != null) {
                fileName = descriptor.getSection();
              }

              error = PerlBundle.message("inspection.message.can.t.find.pod.section", fileName);
            }
            else {
              error = PerlBundle.message("inspection.message.can.t.find.reference.target");
            }

            holder.registerProblem(reference, error, ProblemHighlightType.GENERIC_ERROR_OR_WARNING);
          }
        }
        super.visitPodFormatLink(o);
      }
    };
  }
}
