/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.pod.parser;

import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.perl5.lang.pod.parser.psi.PodSectionTitle;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.perl5.lang.pod.parser.PodElementTypesGenerated.FORMATTING_SECTION_CONTENT;
import static com.perl5.lang.pod.parser.PodElementTypesGenerated.POD_FORMAT_LINK;


public interface PodElementPatterns {
  PsiElementPattern.Capture<PsiElement> LINK_IDENTIFIER =
    psiElement().withSuperParent(2, psiElement(FORMATTING_SECTION_CONTENT).withParent(psiElement(POD_FORMAT_LINK)));

  PsiElementPattern.Capture<PsiElement> TITLE_IDENTIFIER =
    psiElement().withParent(psiElement(PodSectionTitle.class));
}
