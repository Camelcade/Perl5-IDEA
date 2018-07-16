/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.codeInsight.intention.HighPriorityAction;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.SmartPointerManager;
import com.intellij.psi.SmartPsiElementPointer;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ArrayUtil;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SettingsConfigurable;
import com.perl5.lang.perl.internals.PerlVersion;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlAttributes;
import com.perl5.lang.perl.psi.PsiPerlSubSignature;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Predicate;

import static com.perl5.lang.perl.internals.PerlVersion.V5_20;
import static com.perl5.lang.perl.internals.PerlVersion.V5_28;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.LEFT_PAREN;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.RIGHT_PAREN;

public class PerlSubSignaturesInspection extends PerlInspection {
  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {
      @Override
      public void visitSubSignature(@NotNull PsiPerlSubSignature o) {
        PerlVersion selectedVersion = PerlSharedSettings.getInstance(holder.getProject()).getTargetPerlVersion();
        if (selectedVersion.lesserThan(V5_20)) {
          registerProblem(
            holder, o,
            PerlBundle.message("perl.inspection.sub.signatures", selectedVersion.getStrictDottedVersion()),
            buildChangePerlVersionQuickFixes(version -> !version.lesserThan(V5_20))
          );
        }
        else if (selectedVersion.greaterThan(V5_20) && selectedVersion.lesserThan(V5_28)) {
          PsiElement run = o.getPrevSibling();
          while (run != null) {
            if (run instanceof PsiPerlAttributes) {
              LocalQuickFix flipQuickFix = getFlipElementsQuickFix(run);
              registerProblem(
                holder, o,
                PerlBundle.message("perl.inspection.sub.signatures.after.attributes", selectedVersion.getStrictDottedVersion()),
                isOnTheFly ? ArrayUtil.prepend(flipQuickFix,
                                               buildChangePerlVersionQuickFixes(
                                                 version -> version.equals(V5_20) || version.compareTo(V5_28) >= 0))
                           : new LocalQuickFix[]{flipQuickFix}
              );
              return;
            }
            run = run.getPrevSibling();
          }
        }
        else {
          PsiElement run = o.getPrevSibling();
          while (run != null) {
            if (run instanceof PsiPerlAttributes) {
              LocalQuickFix flipQuickFix = getFlipElementsQuickFix(run);
              registerProblem(
                holder, o,
                PerlBundle.message("perl.inspection.sub.signatures.before.attributes", selectedVersion.getStrictDottedVersion()),
                isOnTheFly ? ArrayUtil.prepend(flipQuickFix,
                                               buildChangePerlVersionQuickFixes(
                                                 version -> version.greaterThan(V5_20) && version.lesserThan(V5_28)))
                           : new LocalQuickFix[]{flipQuickFix}
              );
              return;
            }
            run = run.getNextSibling();
          }
        }
      }

      @NotNull
      private LocalQuickFix getFlipElementsQuickFix(@NotNull PsiElement attributes) {
        SmartPsiElementPointer attributesPointer =
          SmartPointerManager.getInstance(attributes.getProject()).createSmartPsiElementPointer(attributes);
        return new MyTopQuickFix() {
          @Nls
          @NotNull
          @Override
          public String getFamilyName() {
            return PerlBundle.message("perl.quickfix.switch.signature.with.attributes");
          }

          @Override
          public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            PsiElement attributesElement = attributesPointer.getElement();
            PsiElement signatureElement = descriptor.getPsiElement();
            if (attributesElement == null || !attributesElement.isValid() || !signatureElement.isValid()) {
              return;
            }

            PsiElement subDefinition = signatureElement.getParent();

            if (attributesElement.getNode().getStartOffset() < signatureElement.getNode().getStartOffset()) {
              PsiElement closeParen = PerlPsiUtil.getNextSignificantSibling(signatureElement);
              if (PsiUtilCore.getElementType(closeParen) != RIGHT_PAREN) {
                return;
              }
              subDefinition.addAfter(attributesElement, closeParen);
            }
            else {
              PsiElement openParen = PerlPsiUtil.getPrevSignificantSibling(signatureElement);
              if (PsiUtilCore.getElementType(openParen) != LEFT_PAREN) {
                return;
              }
              subDefinition.addBefore(attributesElement, openParen);
              subDefinition.addBefore(PerlElementFactory.createSpace(project), openParen);
            }
            attributesElement.delete();
          }
        };
      }

      @NotNull
      private LocalQuickFix[] buildChangePerlVersionQuickFixes(@NotNull Predicate<PerlVersion> versionPredicate) {
        List<LocalQuickFix> result = ContainerUtil.newArrayList();
        result.add(new LocalQuickFix() {
          @Nls
          @NotNull
          @Override
          public String getFamilyName() {
            return PerlBundle.message("perl.quickfix.change.language.level");
          }

          @Override
          public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            Perl5SettingsConfigurable.open(project);
          }
        });

        PerlVersion.ALL_VERSIONS.stream().filter(versionPredicate).forEach(it -> result.add(new LocalQuickFix() {
          @Nls
          @NotNull
          @Override
          public String getFamilyName() {
            return PerlBundle.message("perl.quickfix.set.language.level", it.getStrictDottedVersion());
          }

          @Override
          public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            PerlSharedSettings.getInstance(project).setTargetPerlVersion(it);
            DaemonCodeAnalyzer.getInstance(project).restart();
          }
        }));

        return result.toArray(new LocalQuickFix[0]);
      }
    };
  }

  private interface MyTopQuickFix extends LocalQuickFix, HighPriorityAction {
  }
}
