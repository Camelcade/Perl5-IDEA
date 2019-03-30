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

package com.perl5.lang.perl.idea.refactoring.introduce.target;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.refactoring.introduce.PerlIntroduceTarget;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.perl5.lang.perl.psi.utils.PerlPsiUtil.DOUBLE_QUOTE;
import static com.perl5.lang.perl.psi.utils.PerlPsiUtil.DOUBLE_QUOTE_CHAR;

public class PerlHeredocTargetsHandler extends PerlGenericStringTargetsHandler {
  static final PerlHeredocTargetsHandler INSTANCE = new PerlHeredocTargetsHandler();
  private static final Logger LOG = Logger.getInstance(PerlHeredocTargetsHandler.class);

  private PerlHeredocTargetsHandler() {
  }

  @Override
  protected boolean shouldAddElementAsTarget() {
    return false;
  }

  @NotNull
  @Override
  protected List<PsiElement> getChildren(@NotNull PsiElement element) {
    return ((PerlHeredocElementImpl)element).getAllChildrenList();
  }

  @Override
  protected boolean isApplicable(@Nullable PsiElement element) {
    return isTargetableHeredocElement(element);
  }

  @NotNull
  @Override
  protected String createDeclarationStatementText(@NotNull String variableName, @NotNull PerlIntroduceTarget target) {
    return "my " + computeVariableType(target).getSigil() + variableName + " = " + createTargetExpressionText(target);
  }

  @NotNull
  @Override
  protected String createTargetExpressionText(@NotNull PerlIntroduceTarget target) {
    PsiElement targetPlace = target.getPlace();
    if (!(targetPlace instanceof PerlHeredocElementImpl)) {
      LOG.error("Got wrong target element: " + target);
      return "'INTERNAL ERROR, REPORT TO DEVS';";
    }
    CharSequence subSequence = target.getTextRangeInElement().subSequence(targetPlace.getNode().getChars());
    char openQuote = PerlString.suggestOpenQuoteChar(subSequence, '"');
    if (openQuote == DOUBLE_QUOTE_CHAR) {
      return DOUBLE_QUOTE + subSequence + DOUBLE_QUOTE + ";";
    }
    else if (openQuote != 0) {
      return PerlPsiUtil.QUOTE_QQ + " " + openQuote + subSequence + PerlString.getQuoteCloseChar(openQuote) + ";";
    }
    return "substr <<EOM, 0, -1;\n" + subSequence + "\nEOM\n";
  }

  @NotNull
  @Override
  protected List<PsiElement> replaceTarget(@NotNull List<PerlIntroduceTarget> occurrences, @NotNull PsiElement replacement) {
    CharSequence replacementChars = replacement.getNode().getChars();
    assert replacement instanceof PerlVariable : "Got " + replacement;

    PsiElement psiElement = Objects.requireNonNull(occurrences.get(0).getPlace());
    Set<TextRange> replacementRanges = new HashSet<>();

    PsiElement replacedString = replaceWithInterpolation(occurrences, replacementChars, psiElement, replacementRanges);
    return ContainerUtil.filter(replacedString.getChildren(), it -> replacementRanges.contains(it.getTextRangeInParent()));
  }

  @NotNull
  @Override
  PsiElement createReplacementFromText(@NotNull PsiElement originalElement, @NotNull String text) {
    return PerlElementFactory.createHeredocBodyReplacement((PerlHeredocElementImpl)originalElement, text);
  }
}
