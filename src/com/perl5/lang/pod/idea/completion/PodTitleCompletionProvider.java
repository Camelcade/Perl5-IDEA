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

package com.perl5.lang.pod.idea.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.perl5.lang.perl.psi.PerlSubBase;
import com.perl5.lang.pod.parser.PodElementPatterns;
import com.perl5.lang.pod.parser.psi.PodRecursiveVisitor;
import com.perl5.lang.pod.parser.psi.PodSectionTitle;
import com.perl5.lang.pod.parser.psi.PodTitledSection;
import com.perl5.lang.pod.parser.psi.references.PodSubReference;
import com.perl5.lang.pod.parser.psi.util.PodFileUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 24.04.2016.
 */
public class PodTitleCompletionProvider extends CompletionProvider<CompletionParameters> implements PodElementPatterns {
  public static final List<String> DEFAULT_POD_SECTIONS = new ArrayList<String>(
    Arrays.asList(
      "VERSION",
      "SYNOPSIS",
      "API",
      "DESCRIPTION",
      "INSTALLATION",
      "NAME",
      "AUTHORS",
      "CONTRIBUTORS",
      "COPYRIGHT AND LICENSE"
    )
  );

  @Override
  protected void addCompletions(@NotNull CompletionParameters parameters, ProcessingContext context, @NotNull CompletionResultSet result) {
    final PsiElement element = parameters.getOriginalPosition();
    if (element == null) {
      return;
    }

    PsiElement prevElement = element.getPrevSibling();

    if (prevElement == null || prevElement instanceof PodSectionTitle) {
      if (HEADER1_ELEMENT.accepts(element)) {
        for (String title : DEFAULT_POD_SECTIONS) {
          result.addElement(LookupElementBuilder.create(title));
        }
      }


      final PsiFile elementFile = element.getContainingFile();
      final PsiFile perlFile = PodFileUtil.getTargetPerlFile(elementFile);
      if (perlFile != null) {
        final Collection<PerlSubBase> possibleTargets = PsiTreeUtil.findChildrenOfType(perlFile, PerlSubBase.class);
        element.getContainingFile().accept(new PodRecursiveVisitor() {
          @Override
          public void visitTargetableSection(PodTitledSection o) {
            PsiElement titleBlock = o.getTitleBlock();
            if (titleBlock != null) {
              PsiElement firstChild = titleBlock.getFirstChild();
              if (firstChild != null) {
                for (PsiReference reference : firstChild.getReferences()) {
                  if (reference instanceof PodSubReference) {
                    for (ResolveResult resolveResult : ((PodSubReference)reference).multiResolve(false)) {
                      PsiElement targetElement = resolveResult.getElement();

                      if (targetElement instanceof PerlSubBase) {
                        possibleTargets.remove(targetElement);
                      }
                    }
                  }
                }
              }
            }
            super.visitTargetableSection(o);
          }
        });

        for (PerlSubBase untargetedSub : possibleTargets) {
          result.addElement(LookupElementBuilder
                              .create(untargetedSub.getPresentableName())
                              .withIcon(untargetedSub.getIcon(0))
          );
        }
      }
    }
  }
}
