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

package com.perl5.lang.perl.idea.regexp;

import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.psi.impl.PsiPerlPerlRegexImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.REGEX_TOKEN;

/**
 * Created by hurricup on 30.11.2016.
 */
public class Perl5RegexpInjector implements MultiHostInjector {
  private static final List<? extends Class<? extends PsiElement>> ELEMENTS_TO_INJECT =
    Collections.singletonList(PsiPerlPerlRegexImpl.class);

  @Override
  public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement context) {
    assert context instanceof PsiPerlPerlRegexImpl : "Got " + context;
    if (!((PsiPerlPerlRegexImpl)context).isValidHost() || context.getTextLength() == 0) {
      return;
    }

    int[] sourceOffset = new int[]{0};
    boolean[] hasStarted = new boolean[]{false};
    context.acceptChildren(new PsiElementVisitor() {
      @Override
      public void visitElement(PsiElement element) {
        if (PsiUtilCore.getElementType(element) == REGEX_TOKEN) {
          if (!hasStarted[0]) {
            registrar.startInjecting(Perl5RegexpLanguage.INSTANCE);
            hasStarted[0] = true;
          }
          registrar.addPlace(null, null, (PsiPerlPerlRegexImpl)context, TextRange.from(sourceOffset[0], element.getTextLength()));
        }
        sourceOffset[0] += element.getTextLength();
      }
    });
    if (hasStarted[0]) {
      registrar.doneInjecting();
    }
  }

  @NotNull
  @Override
  public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
    return ELEMENTS_TO_INJECT;
  }
}
