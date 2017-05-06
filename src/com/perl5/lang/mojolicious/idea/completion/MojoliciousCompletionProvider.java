/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.mojolicious.idea.completion;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionProvider;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.mojolicious.psi.MojoliciousHelperDeclaration;
import com.perl5.lang.mojolicious.psi.stubs.MojoliciousHelpersStubIndex;
import com.perl5.lang.mojolicious.util.MojoliciousSubUtil;
import com.perl5.lang.perl.PerlScopes;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

/**
 * Created by hurricup on 02.08.2015.
 */
public class MojoliciousCompletionProvider extends CompletionProvider<CompletionParameters> {
  public static final HashSet<LookupElementBuilder> BUILT_IN_SUB_LOOKUP_ELEMENTS = new HashSet<LookupElementBuilder>();

  static {
    for (String subName : MojoliciousSubUtil.MOJO_DEFAULT_HELPERS) {
      BUILT_IN_SUB_LOOKUP_ELEMENTS.add(LookupElementBuilder
                                         .create(subName)
                                         .withIcon(PerlIcons.MOJO_FILE)
                                         .withBoldness(true)
      );
    }
    for (String subName : MojoliciousSubUtil.MOJO_TAG_HELPERS) {
      BUILT_IN_SUB_LOOKUP_ELEMENTS.add(LookupElementBuilder
                                         .create(subName)
                                         .withIcon(PerlIcons.MOJO_FILE)
                                         .withBoldness(true)
      );
    }
  }

  public void addCompletions(@NotNull CompletionParameters parameters,
                             ProcessingContext context,
                             @NotNull CompletionResultSet resultSet) {
    PsiElement method = parameters.getPosition().getParent();
    assert method instanceof PsiPerlMethod;

    if (!((PsiPerlMethod)method).hasExplicitNamespace() && !((PsiPerlMethod)method).isObjectMethod()) {
      resultSet.addAllElements(BUILT_IN_SUB_LOOKUP_ELEMENTS);

      StubIndex stubIndex = StubIndex.getInstance();
      final Project project = method.getProject();
      final GlobalSearchScope scope = PerlScopes.getProjectAndLibrariesScope(project);

      for (String helperName : stubIndex.getAllKeys(MojoliciousHelpersStubIndex.KEY, method.getProject())) {
        for (MojoliciousHelperDeclaration helper : StubIndex
          .getElements(MojoliciousHelpersStubIndex.KEY, helperName, project, scope, MojoliciousHelperDeclaration.class)) {
          if (helper != null) {
            LookupElementBuilder newElement = LookupElementBuilder
              .create(helperName)
              .withIcon(PerlIcons.MOJO_FILE)
              .withTailText(helper.getSubArgumentsListAsString()
              );

            PsiFile file = helper.getContainingFile();
            if (file != null) {
              ItemPresentation presentation = file.getPresentation();
              if (presentation != null) {
                newElement = newElement.withTypeText(file.getName(), presentation.getIcon(false), false);
              }
            }

            resultSet.addElement(newElement);
          }
        }
      }
    }
  }
}
