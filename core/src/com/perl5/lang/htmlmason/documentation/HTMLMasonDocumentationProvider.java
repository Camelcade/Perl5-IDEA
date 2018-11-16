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

package com.perl5.lang.htmlmason.documentation;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.psi.PsiElement;
import com.perl5.lang.htmlmason.HTMLMasonUtil;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 20.03.2016.
 */
public class HTMLMasonDocumentationProvider extends AbstractDocumentationProvider {
  @Nullable
  @Override
  public String getQuickNavigateInfo(PsiElement element, PsiElement originalElement) {
    if (element instanceof HTMLMasonFileImpl) {
      return "HTML::Mason component:<br>" +
             ((HTMLMasonFileImpl)element).getAbsoluteComponentPath() +
             HTMLMasonUtil.getArgumentsListAsString((HTMLMasonFileImpl)element);
    }
    return super.getQuickNavigateInfo(element, originalElement);
  }
}
