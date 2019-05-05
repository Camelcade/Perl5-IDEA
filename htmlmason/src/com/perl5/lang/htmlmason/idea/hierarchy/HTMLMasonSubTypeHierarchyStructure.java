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

package com.perl5.lang.htmlmason.idea.hierarchy;

import com.intellij.ide.hierarchy.HierarchyNodeDescriptor;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.perl.idea.hierarchy.namespace.PerlHierarchyNodeDescriptor;
import com.perl5.lang.perl.idea.hierarchy.namespace.treestructures.PerlSubTypesHierarchyTreeStructure;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;


public class HTMLMasonSubTypeHierarchyStructure extends PerlSubTypesHierarchyTreeStructure {
  public HTMLMasonSubTypeHierarchyStructure(@NotNull PsiElement element) {
    this(element.getProject(), new HTMLMasonHierarchyNodeDescriptor(null, element, true));
  }

  public HTMLMasonSubTypeHierarchyStructure(@NotNull Project project, HierarchyNodeDescriptor baseDescriptor) {
    super(project, baseDescriptor);
  }

  @Override
  protected PerlHierarchyNodeDescriptor createDescriptor(NodeDescriptor parentDescriptor, PsiElement element, boolean isBase) {
    return new HTMLMasonHierarchyNodeDescriptor(parentDescriptor, element, isBase);
  }

  @Override
  protected Collection<PsiElement> getSubElements(PsiElement element) {
    assert element instanceof HTMLMasonFileImpl;
    return new ArrayList<>(((HTMLMasonFileImpl)element).getChildComponents());
  }
}
