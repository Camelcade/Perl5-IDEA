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

package com.perl5.lang.htmlmason;

import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonFlagsStatement;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.perl.idea.PerlElementPatterns;
import com.perl5.lang.perl.psi.*;

import static com.intellij.patterns.PlatformPatterns.psiElement;
import static com.intellij.patterns.PlatformPatterns.psiFile;
import static com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes.*;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;


public final class HTMLMasonElementPatterns {
  private HTMLMasonElementPatterns() {
  }

  public static final PsiElementPattern.Capture<PsiElement> ATTR_OR_ARG_ELEMENT_PATTERN = psiElement().andOr(
    psiElement().inside(psiElement(HTML_MASON_ATTR_BLOCK)),
    psiElement().inside(psiElement(HTML_MASON_ARGS_BLOCK))
  );

  public static final PsiElementPattern.Capture<PerlSubNameElement> HTML_MASON_TEMPLATE_CONTEXT_PATTERN =
    psiElement(PerlSubNameElement.class).withParent(
      psiElement(PsiPerlMethod.class)
        .withParent(
          psiElement(SUB_CALL).afterLeaf(psiElement(HTML_MASON_BLOCK_OPENER))
        )
    );

  // this is for corrupted <%identifier
  public static final PsiElementPattern.Capture<PsiElement> HTML_MASON_TEMPLATE_CONTEXT_PATTERN_BROKEN =
    psiElement(IDENTIFIER).afterLeaf(psiElement(HTML_MASON_BLOCK_OPENER)
    );

  public static final PsiElementPattern.Capture<PerlString> HTML_MASON_COMPONENT_CALLEE =
    psiElement(PerlString.class)
      .inFile(psiFile(HTMLMasonFileImpl.class))
      .withParent(psiElement(HTML_MASON_CALL_STATEMENT))
      .afterLeafSkipping(PerlElementPatterns.WHITE_SPACE_AND_COMMENTS, psiElement().andOr(
        psiElement(HTML_MASON_CALL_OPENER),
        psiElement(HTML_MASON_CALL_FILTERING_OPENER)
      ));


  public static final PsiElementPattern.Capture<PerlString> HTML_MASON_FLAGS_PARENT =
    psiElement(PerlString.class)
      .inFile(psiFile(HTMLMasonFileImpl.class))
      .withParent(
        psiElement(PsiPerlCommaSequenceExpr.class).withParent(psiElement(HTMLMasonFlagsStatement.class))
      )
      .afterLeafSkipping(PerlElementPatterns.WHITE_SPACE_AND_COMMENTS, psiElement(FAT_COMMA));


  public static final PsiElementPattern.Capture<PerlStringContentElement> HTML_MASON_COMPONENT_COMPLETION =
    psiElement(PerlStringContentElement.class).andOr(
      psiElement().withParent(HTML_MASON_COMPONENT_CALLEE),
      psiElement().withParent(HTML_MASON_FLAGS_PARENT)
    );
}
