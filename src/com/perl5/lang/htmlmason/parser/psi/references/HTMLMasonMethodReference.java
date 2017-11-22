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

package com.perl5.lang.htmlmason.parser.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonMethodDefinition;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonNamedElement;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.perl.psi.PerlString;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 19.03.2016.
 */
public class HTMLMasonMethodReference extends HTMLMasonStringReference {
  public HTMLMasonMethodReference(@NotNull PerlString element, TextRange textRange) {
    super(element, textRange);
  }


  @Override
  public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
    if (HTMLMasonNamedElement.HTML_MASON_IDENTIFIER_PATTERN.matcher(newElementName).matches()) { // fixme should be in other place
      return ElementManipulators.handleContentChange(myElement, newElementName);
    }
    return myElement;
  }

  @NotNull
  @Override
  protected ResolveResult[] resolveInner(boolean incompleteCode) {
    PerlString element = getElement();
    String methodName = getRangeInElement().substring(element.getText());

    if (StringUtil.isNotEmpty(methodName)) {
      PsiReference[] references = element.getReferences();
      if (references.length == 2) {
        PsiReference componentReference = references[0];
        PsiElement startComponent = componentReference.resolve();
        if (startComponent instanceof HTMLMasonFileImpl) {
          HTMLMasonMethodDefinition methodDefinition =
            ((HTMLMasonFileImpl)startComponent).findMethodDefinitionByNameInThisOrParents(methodName);
          if (methodDefinition != null) {
            return new ResolveResult[]{new PsiElementResolveResult(methodDefinition)};
          }
        }
      }
    }
    return ResolveResult.EMPTY_ARRAY;
  }
}
