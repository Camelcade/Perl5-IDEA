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

package com.perl5.lang.mason2.idea.generation;

import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.perl5.lang.mason2.Mason2TemplatingLanguage;
import com.perl5.lang.perl.extensions.PerlCodeGenerator;
import com.perl5.lang.perl.extensions.generation.PerlCodeGeneratorImpl;


public class Mason2TemplatingCodeGeneratorImpl extends PerlCodeGeneratorImpl {
  public static PerlCodeGenerator INSTANCE = new Mason2TemplatingCodeGeneratorImpl();


  @Override
  protected void insertCodeAfterElement(PsiElement anchor, String code, Editor editor) {
    if (code == null) {
      return;
    }

    if (anchor.getLanguage() == Mason2TemplatingLanguage.INSTANCE) {
      code = "<%perl>\n" + code + "\n</%perl>\n";
    }

    super.insertCodeAfterElement(anchor, code, editor);
  }
}
