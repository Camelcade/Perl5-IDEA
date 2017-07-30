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

package com.perl5.lang.perl.idea.hierarchy.namespace.treestructures;

import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by hurricup on 16.08.2015.
 */
public class PerlSuperTypesHierarchyTreeStructure extends PerlSubTypesHierarchyTreeStructure {
  public PerlSuperTypesHierarchyTreeStructure(@NotNull PsiElement element) {
    super(element);
  }

  @Override
  protected Collection<PsiElement> getSubElements(PsiElement element) {
    assert element instanceof PerlNamespaceDefinitionElement;
    return new ArrayList<>(((PerlNamespaceDefinitionElement)element).getParentNamespaceDefinitions());
  }
}
