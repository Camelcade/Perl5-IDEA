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

package com.perl5.lang.perl.parser.Exception.Class.ide.refactoring;

import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.extensions.packageprocessor.impl.ExceptionClassProcessor;
import com.perl5.lang.perl.psi.impl.PerlImplicitElement;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.light.PerlLightMethodDefinitionElement;

public class PerlRenamingVetoCondition implements Condition<PsiElement> {
  @Override
  public boolean value(PsiElement element) {
    return isVetoed(element);
  }

  public static boolean isVetoed(PsiElement element) {
    if (element instanceof PerlImplicitElement) {
      return true;
    }

    if (!(element instanceof PerlLightMethodDefinitionElement<?> lightMethodDefinitionElement)) {
      return false;
    }

    PsiElement delegate = lightMethodDefinitionElement.getDelegate();
    if (!(delegate instanceof PerlUseStatementElement useStatementElement)) {
      return false;
    }

    PerlPackageProcessor packageProcessor = useStatementElement.getPackageProcessor();
    return packageProcessor instanceof ExceptionClassProcessor &&
           ExceptionClassProcessor.FIELDS_METHOD_NAME.equals(lightMethodDefinitionElement.getName());
  }
}
