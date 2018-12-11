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

package com.perl5.lang.perl.parser.moose.idea.inspections;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.inspections.PerlInspection;
import com.perl5.lang.perl.parser.moose.psi.impl.PerlAttributeDefinition;
import com.perl5.lang.perl.parser.moose.psi.impl.PerlMooseAttributeWrapper;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.light.PerlDelegatingLightNamedElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MooseMultiAttributeAccessorInspection extends PerlInspection {
  @NotNull
  @Override
  public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
    return new PerlVisitor() {
      @Override
      public void visitMooseAttributeWrapper(@NotNull PerlMooseAttributeWrapper o) {
        List<PerlDelegatingLightNamedElement> lightElements = o.getLightElements();
        if (lightElements.size() < 2) {
          return;
        }
        List<PerlDelegatingLightNamedElement> attributes = new ArrayList<>(lightElements.size());
        List<PerlDelegatingLightNamedElement> accessors = new ArrayList<>(lightElements.size() - 1);
        lightElements.forEach(it -> {
          if (it instanceof PerlAttributeDefinition) {
            attributes.add(it);
          }
          else {
            accessors.add(it);
          }
        });

        if (attributes.size() < 2 || accessors.isEmpty()) {
          return;
        }
        accessors.forEach(it -> registerProblem(holder, it.getNameIdentifier(), PerlBundle.message("perl.inspection.multiattr.accessor")));
        List<PsiElement> attributesIdentifiers = attributes.stream()
          .map(PerlDelegatingLightNamedElement::getNameIdentifier)
          .distinct().collect(Collectors.toList());
        if (attributesIdentifiers.size() == 1) {
          // same identifier looks like an accessor
          registerProblem(holder, attributesIdentifiers.get(0), PerlBundle.message("perl.inspection.multiattr.accessor"));
        }
      }
    };
  }
}
