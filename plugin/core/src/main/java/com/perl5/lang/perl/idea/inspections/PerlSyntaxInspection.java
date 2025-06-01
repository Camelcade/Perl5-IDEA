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

package com.perl5.lang.perl.idea.inspections;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.codeInsight.intention.FileModifier;
import com.intellij.codeInsight.intention.HighPriorityAction;
import com.intellij.codeInsight.intention.preview.IntentionPreviewInfo;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ArrayUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SettingsConfigurable;
import com.perl5.lang.perl.idea.quickfixes.PerlFancyMethodQuickFix;
import com.perl5.lang.perl.internals.PerlVersion;
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PsiPerlSubDefinitionImpl;
import com.perl5.lang.perl.psi.impl.PsiPerlSubExprImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.perl5.lang.perl.internals.PerlVersion.*;
import static com.perl5.lang.perl.lexer.PerlTokenSets.BITWISE_ASSIGN_OPERATORS_TOKENSET;
import static com.perl5.lang.perl.lexer.PerlTokenSets.BITWISE_OPERATORS_TOKENSET;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;

public class PerlSyntaxInspection extends PerlInspection {
  @Override
  public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    PerlVersion selectedVersion = PerlSharedSettings.getInstance(holder.getProject()).getTargetPerlVersion();
    return new PerlVisitor() {
      @Override
      public void visitMethod(@NotNull PsiPerlMethod o) {
        if (o.isFancySyntax()) {
          String namespaceName = o.getExplicitNamespaceName();
          if (namespaceName == null) {
            return;
          }
          PerlSubNameElement subNameElement = o.getSubNameElement();
          if (subNameElement == null) {
            return;
          }
          String properForm = String.format("%s->%s", namespaceName, subNameElement.getName());
          holder.registerProblem(
            o,
            selectedVersion.lesserThan(V5_36)
            ? PerlBundle.message("perl.inspection.fancy.call", properForm)
            : PerlBundle.message("perl.inspection.fancy.call.unsupported", properForm),
            selectedVersion.lesserThan(V5_36) ? ProblemHighlightType.WARNING : ProblemHighlightType.ERROR,
            new PerlFancyMethodQuickFix(properForm)
          );
        }
        super.visitMethod(o);
      }

      @Override
      public void visitSortExpr(@NotNull PsiPerlSortExpr o) {
        super.visitSortExpr(o);

        if (selectedVersion.lesserThan(V5_36) || o.getBlock() != null || o.getMethod() != null) {
          return;
        }
        var sortableList = o.getExprList();
        if (sortableList.size() != 1 ||
            !(sortableList.getFirst() instanceof PsiPerlParenthesisedExpr parenthesizedExpression) ||
            parenthesizedExpression.getExpr() != null) {
          return;
        }
        holder.registerProblem(o, PerlBundle.message("perl.inspection.wrong.sort.syntax"), ProblemHighlightType.ERROR);
      }

      @Override
      public void visitAssignExpr(@NotNull PsiPerlAssignExpr o) {
        if (selectedVersion.lesserThan(V5_22)) {
          PsiElement run = o.getFirstChild();
          while (run != null) {
            if (BITWISE_ASSIGN_OPERATORS_TOKENSET.contains(PsiUtilCore.getElementType(run)) && run.getTextLength() > 2) {
              registerProblem(
                holder, o,
                PerlBundle.message("perl.inspection.string.binary.unavailable"),
                buildChangePerlVersionQuickFixes(GREATER_OR_EQUAL_V522));
              break;
            }
            run = run.getNextSibling();
          }
        }
        super.visitAssignExpr(o);
      }

      @Override
      public void visitFileReadExpr(@NotNull PsiPerlFileReadExpr o) {
        if (selectedVersion.lesserThan(V5_22)) {
          PsiElement firstChild = o.getFirstChild();
          PsiElement lastChild = o.getLastChild();
          if (PsiUtilCore.getElementType(firstChild) == LEFT_ANGLE && firstChild.getTextLength() > 1 ||
              PsiUtilCore.getElementType(lastChild) == RIGHT_ANGLE && lastChild.getTextLength() > 1) {
            registerProblem(
              holder, o,
              PerlBundle.message("perl.inspection.double.diamond.unavailable"),
              buildChangePerlVersionQuickFixes(GREATER_OR_EQUAL_V522));
          }
        }
        super.visitFileReadExpr(o);
      }

      @Override
      public void visitBitwiseAndExpr(@NotNull PsiPerlBitwiseAndExpr o) {
        processBinaryExpression(o);
        super.visitBitwiseAndExpr(o);
      }

      private void processBinaryExpression(@NotNull PsiPerlExpr o) {
        if (!selectedVersion.lesserThan(V5_22)) {
          return;
        }
        PsiElement run = o.getFirstChild();
        while (run != null) {
          if (BITWISE_OPERATORS_TOKENSET.contains(PsiUtilCore.getElementType(run)) && run.getTextLength() > 1) {
            registerProblem(
              holder, o,
              PerlBundle.message("perl.inspection.string.binary.unavailable"),
              buildChangePerlVersionQuickFixes(GREATER_OR_EQUAL_V522));
            break;
          }
          run = run.getNextSibling();
        }
      }

      @Override
      public void visitBitwiseOrXorExpr(@NotNull PsiPerlBitwiseOrXorExpr o) {
        processBinaryExpression(o);
        super.visitBitwiseOrXorExpr(o);
      }

      @Override
      public void visitEqualExpr(@NotNull PsiPerlEqualExpr o) {
        if (o.getChildren().length > 2) {
          reportOperatorsChainingAvailability(o);
          PsiElement run = o.getFirstChild();
          while (run != null) {
            if (PerlTokenSets.UNCHAINABLE_OPERATORS.contains(PsiUtilCore.getElementType(run))) {
              registerProblem(
                holder, o,
                PerlBundle.message("perl.inspection.chained.expr.invalid"));
              break;
            }
            run = run.getNextSibling();
          }
        }
        super.visitEqualExpr(o);
      }

      private void reportOperatorsChainingAvailability(@NotNull PsiElement o) {
        if (selectedVersion.lesserThan(V5_32)) {
          registerProblem(
            holder, o,
            PerlBundle.message("perl.inspection.chained.expr.unavailable"),
            buildChangePerlVersionQuickFixes(GREATER_OR_EQUAL_V532));
        }
      }

      @Override
      public void visitCompareExpr(@NotNull PsiPerlCompareExpr o) {
        if (o.getChildren().length > 2) {
          reportOperatorsChainingAvailability(o);
        }
        super.visitCompareExpr(o);
      }

      @Override
      public void visitIsaExpr(@NotNull PsiPerlIsaExpr o) {
        if (selectedVersion.lesserThan(V5_32)) {
          registerProblem(
            holder, o,
            PerlBundle.message("perl.inspection.isa.expr.unavailable"),
            buildChangePerlVersionQuickFixes(GREATER_OR_EQUAL_V532));
        }
        super.visitIsaExpr(o);
      }

      @Override
      public void visitHashHashSlice(@NotNull PsiPerlHashHashSlice o) {
        if (selectedVersion.lesserThan(V5_20)) {
          registerProblem(
            holder, o,
            PerlBundle.message("perl.inspection.hash.hash.slice.unavailable"),
            buildChangePerlVersionQuickFixes(PerlVersion.GREATER_OR_EQUAL_V520));
        }
        super.visitHashHashSlice(o);
      }

      @Override
      public void visitHashArraySlice(@NotNull PsiPerlHashArraySlice o) {
        if (selectedVersion.lesserThan(V5_20)) {
          registerProblem(
            holder, o,
            PerlBundle.message("perl.inspection.hash.array.slice.unavailable"),
            buildChangePerlVersionQuickFixes(PerlVersion.GREATER_OR_EQUAL_V520));
        }
        super.visitHashArraySlice(o);
      }

      @Override
      public void visitSignatureContent(@NotNull PsiPerlSignatureContent o) {
        PsiElement signatureOwner = o.getParent();
        if (!(signatureOwner instanceof PsiPerlSubDefinitionImpl) &&
            !(signatureOwner instanceof PsiPerlSubExprImpl)) {
          return;
        }

        if (selectedVersion.lesserThan(V5_20)) {
          registerProblem(
            holder, o,
            PerlBundle.message("perl.inspection.sub.signatures", selectedVersion.getStrictDottedVersion()),
            buildChangePerlVersionQuickFixes(PerlVersion.GREATER_OR_EQUAL_V520)
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

      private @NotNull LocalQuickFix getFlipElementsQuickFix(@NotNull PsiElement attributes) {
        SmartPsiElementPointer<?> attributesPointer =
          SmartPointerManager.getInstance(attributes.getProject()).createSmartPsiElementPointer(attributes);
        return new MyTopQuickFix() {
          @Override
          public @Nls @NotNull String getFamilyName() {
            return PerlBundle.message("perl.quickfix.switch.signature.with.attributes");
          }

          @Override
          public @Nullable FileModifier getFileModifierForPreview(@NotNull PsiFile target) {
            PsiElement originalAttributes = attributesPointer.getElement();
            return originalAttributes == null
                   ? null
                   : getFlipElementsQuickFix(PsiTreeUtil.findSameElementInCopy(originalAttributes, target));
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

      private @NotNull LocalQuickFix[] buildChangePerlVersionQuickFixes(@NotNull Predicate<? super PerlVersion> versionPredicate) {
        List<LocalQuickFix> result = new ArrayList<>();
        result.add(new LocalQuickFix() {
          @Override
          public @Nls @NotNull String getFamilyName() {
            return PerlBundle.message("perl.quickfix.change.language.level");
          }

          @Override
          public @NotNull IntentionPreviewInfo generatePreview(@NotNull Project project, @NotNull ProblemDescriptor previewDescriptor) {
            return IntentionPreviewInfo.EMPTY;
          }

          @Override
          public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            Perl5SettingsConfigurable.open(project);
          }
        });

        PerlVersion.ALL_VERSIONS.stream().filter(versionPredicate).forEach(it -> result.add(new LocalQuickFix() {
          @Override
          public @Nls @NotNull String getFamilyName() {
            return PerlBundle.message("perl.quickfix.set.language.level", it.getStrictDottedVersion());
          }

          @Override
          public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
            PerlSharedSettings.getInstance(project).setTargetPerlVersion(it);
            DaemonCodeAnalyzer.getInstance(project).restart();
          }
        }));

        return result.toArray(LocalQuickFix.EMPTY_ARRAY);
      }
    };
  }

  private interface MyTopQuickFix extends LocalQuickFix, HighPriorityAction {
  }
}
