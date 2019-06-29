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

package com.perl5.lang.perl.extensions.mojo;

import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.Processor;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlCallValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.PerlSub;
import com.perl5.lang.perl.psi.PerlSubDefinition;
import com.perl5.lang.perl.psi.PerlSubElement;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.light.PerlLightSubDefinitionElement;
import com.perl5.lang.perl.psi.stubs.PerlStubElementTypes;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MojoLightDelegatingSubDefinition extends PerlLightSubDefinitionElement<PerlUseStatementElement> {
  public MojoLightDelegatingSubDefinition(@NotNull PerlUseStatementElement useStatementElement,
                                          @Nullable String namespaceName,
                                          @NotNull String name,
                                          @NotNull AtomicNotNullLazyValue<PerlValue> returnValueFromCodeProvider) {
    super(useStatementElement, name, PerlStubElementTypes.LIGHT_METHOD_DEFINITION, null, namespaceName, null, Collections.emptyList(),
          returnValueFromCodeProvider,
          null);
    setImplicit(true);
  }

  @NotNull
  @Override
  public List<PerlSubArgument> getSubArgumentsList() {
    Ref<List<PerlSubArgument>> argumentsRef = Ref.create(super.getSubArgumentsList());
    processTargets(it -> {
      if (it instanceof PerlSubDefinition) {
        List<PerlSubArgument> list = new ArrayList<>(((PerlSubDefinition)it).getSubArgumentsList());
        if (!list.isEmpty()) {
          list.remove(0);
        }
        argumentsRef.set(list);
        return false;
      }
      return true;
    });
    return argumentsRef.get();
  }

  @Nullable
  @Override
  public PerlSubAnnotations getAnnotations() {
    Ref<PerlSubAnnotations> annotationsRef = Ref.create(super.getAnnotations());
    processTargetSubs(it -> {
      PerlSubAnnotations annotations = it.getAnnotations();
      if (annotations != null) {
        annotationsRef.set(annotations);
        return false;
      }
      return true;
    });
    return super.getAnnotations();
  }

  @NotNull
  @Override
  public PsiElement getNavigationElement() {
    Ref<PsiElement> navigationRef = Ref.create(super.getNavigationElement());
    processTargets(it -> {
      navigationRef.set(it);
      return false;
    });

    return navigationRef.get();
  }

  /**
   * @return target sub of this delegating sub or method.
   */
  @Nullable
  public PerlSubElement getTargetSubElement() {
    Ref<PerlSubElement> targetSubRef = Ref.create();
    processTargetSubs(it -> {
      if (it instanceof PerlSubElement) {
        targetSubRef.set((PerlSubElement)it);
        return false;
      }
      return false;
    });
    return targetSubRef.get();
  }


  /**
   * Processes this delegating sub target subs if possible
   */
  public boolean processTargetSubs(Processor<PerlSub> processor) {
    return processTargets(it -> !(it instanceof PerlSub) || processor.process((PerlSub)it));
  }

  /**
   * Processes this delegating sub targets if possible
   */
  public boolean processTargets(Processor<PsiNamedElement> processor) {
    PerlValue returnValue = getReturnValueFromCode();
    return !(returnValue instanceof PerlCallValue) ||
           ((PerlCallValue)returnValue).processCallTargets(getDelegate(), it -> it.equals(this) || processor.process(it));
  }
}
