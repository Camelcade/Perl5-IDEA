/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.pod.idea.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.psi.PerlSubElement;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.pod.parser.PodElementPatterns;
import com.perl5.lang.pod.parser.psi.PodRecursiveVisitor;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import com.perl5.lang.pod.parser.psi.references.PodSubReference;
import com.perl5.lang.pod.parser.psi.util.PodFileUtil;
import com.perl5.lang.pod.psi.PsiHead1Section;
import com.perl5.lang.pod.psi.PsiSectionTitle;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by hurricup on 24.04.2016.
 */
public class PodTitleCompletionProvider extends CompletionProvider<CompletionParameters> implements PodElementPatterns {
  public static final List<String> DEFAULT_POD_SECTIONS = Arrays.asList(
    "VERSION",
    "SYNOPSIS",
    "API",
    "DESCRIPTION",
    "INSTALLATION",
    "NAME",
    "AUTHORS",
    "CONTRIBUTORS",
    "COPYRIGHT AND LICENSE",
    "METHODS",
    "ATTRIBUTES",
    "FUNCTIONS"
  );

  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters,
                                @NotNull ProcessingContext context,
                                @NotNull CompletionResultSet result) {
    PsiElement element = parameters.getPosition();

    if (PsiUtilCore.getElementType(element) != POD_IDENTIFIER ||
        element.getPrevSibling() != null ||
        !(element.getParent() instanceof PsiSectionTitle)) {
      return;
    }
    if (element.getParent().getParent() instanceof PsiHead1Section) {
      DEFAULT_POD_SECTIONS.forEach(it -> result.addElement(LookupElementBuilder.create(it)));
    }

    final PsiFile elementFile = element.getContainingFile().getOriginalFile();
    final PsiFile perlFile = PodFileUtil.getTargetPerlFile(elementFile);
    if (perlFile == null) {
      return;
    }

    Set<PerlSubElement> possibleTargets = new HashSet<>();
    PerlPsiUtil.processSubElements(perlFile, possibleTargets::add);
    elementFile.accept(new PodRecursiveVisitor() {
      @Override
      public void visitTargetableSection(PodTitledSection o) {
        processSection(o);
        super.visitTargetableSection(o);
      }

      private void processSection(@NotNull PodTitledSection o) {
        PsiElement titleBlock = o.getTitleElement();
        if (titleBlock == null) {
          return;
        }
        PsiElement firstChild = titleBlock.getFirstChild();
        if (firstChild == null) {
          return;
        }
        //noinspection SuspiciousMethodCalls
        Arrays.stream(firstChild.getReferences())
          .filter(it -> it instanceof PodSubReference)
          .flatMap(it -> Arrays.stream(((PodSubReference)it).multiResolve(false)))
          .map(ResolveResult::getElement)
          .forEach(possibleTargets::remove);
      }
    });

    possibleTargets.forEach(it -> result.addElement(LookupElementBuilder
                                                      .create(it, StringUtil.notNullize(it.getPresentableName()))
                                                      .withIcon(it.getIcon(0))
    ));
  }
}
