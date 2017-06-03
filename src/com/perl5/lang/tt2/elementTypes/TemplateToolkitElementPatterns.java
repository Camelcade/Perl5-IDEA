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

package com.perl5.lang.tt2.elementTypes;

import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * Created by hurricup on 12.06.2016.
 */
public interface TemplateToolkitElementPatterns extends TemplateToolkitElementTypes {
  PsiElementPattern.Capture<PsiElement> FILTER_NAME_PATTERN =
    psiElement(TT2_IDENTIFIER).withParent(
      psiElement(IDENTIFIER_EXPR).afterLeafSkipping(
        psiElement().andOr(psiElement().whitespace(), psiElement(PsiComment.class)),
        psiElement(TT2_FILTER)
      )
    );

  // fixme add controlling tokenset
  PsiElementPattern.Capture<PsiElement> BLOCK_NAME_USAGE_PATTERN = psiElement(TT2_STRING_CONTENT);
}
