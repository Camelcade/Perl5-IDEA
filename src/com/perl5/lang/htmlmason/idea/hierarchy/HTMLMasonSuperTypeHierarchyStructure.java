/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.htmlmason.idea.hierarchy;

import com.intellij.psi.PsiElement;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 12.03.2016.
 */
public class HTMLMasonSuperTypeHierarchyStructure extends HTMLMasonSubTypeHierarchyStructure {
  public HTMLMasonSuperTypeHierarchyStructure(@NotNull PsiElement element) {
    super(element);
  }

  @Override
  protected Collection<PsiElement> getSubElements(PsiElement element) {
    assert element instanceof HTMLMasonFileImpl;
    PsiElement parent = ((HTMLMasonFileImpl)element).getParentComponent();
    if (parent != null) {
      List<PsiElement> result = new ArrayList<PsiElement>();
      result.add(parent);
      return result;
    }
    return Collections.emptyList();
  }
}
