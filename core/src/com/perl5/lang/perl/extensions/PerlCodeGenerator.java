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

package com.perl5.lang.perl.extensions;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;


public interface PerlCodeGenerator {
  /**
   * Returns code for overriding provided sub definition
   *
   * @param subBase sub to override
   * @return generated code
   */
  @Nullable
  String getOverrideCodeText(PsiElement subBase);

  /**
   * Returns code for Moose method modifier
   *
   * @param subBase method to modify
   * @return generated code
   */
  @Nullable
  String getMethodModifierCodeText(PsiElement subBase, String modifierType);


  /**
   * Generating and inserting overriden method
   *
   * @param anchor element to insert method after
   */
  void generateOverrideMethod(PsiElement anchor, Editor editor);

  void generateSetters(PsiElement anchor, Editor editor);

  void generateGetters(PsiElement anchor, Editor editor);

  void generateGettersAndSetters(PsiElement anchor, Editor editor);

  void generateConstructor(PsiElement anchor, Editor editor);
}
