/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.profiler.parser.frames;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiDocumentManager;
import com.perl5.lang.perl.psi.PerlDelegatingFakeElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Wraps unusual element for better presentation, like try/catch block, named block.
 */
class PerlTargetElementWrapper extends PerlDelegatingFakeElement {
  public PerlTargetElementWrapper(@NotNull NavigatablePsiElement delegate) {
    super(delegate);
  }

  @Override
  public String getPresentableText() {
    return StringUtil.shortenTextWithEllipsis(StringUtil.notNullize(getText()), 80, 5, true);
  }

  @Override
  public @Nullable String getLocationString() {
    var containingFile = getContainingFile();
    if (containingFile == null) {
      return super.getLocationString();
    }
    var document = PsiDocumentManager.getInstance(getProject()).getDocument(containingFile);
    if (document == null) {
      return super.getLocationString();
    }
    return String
      .join(" ", containingFile.getName(), Integer.toString(document.getLineNumber(getTextOffset())));
  }
}
