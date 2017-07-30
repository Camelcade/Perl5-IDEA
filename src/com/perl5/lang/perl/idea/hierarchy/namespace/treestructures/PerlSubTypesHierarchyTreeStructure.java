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

import com.intellij.ide.hierarchy.HierarchyNodeDescriptor;
import com.intellij.ide.hierarchy.HierarchyTreeStructure;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.hierarchy.namespace.PerlHierarchyNodeDescriptor;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 16.08.2015.
 */
public class PerlSubTypesHierarchyTreeStructure extends HierarchyTreeStructure {
  public PerlSubTypesHierarchyTreeStructure(@NotNull PsiElement element) {
    this(element.getProject(), new PerlHierarchyNodeDescriptor(null, element, true));
  }

  public PerlSubTypesHierarchyTreeStructure(@NotNull Project project, HierarchyNodeDescriptor baseDescriptor) {
    super(project, baseDescriptor);
  }

  @NotNull
  @Override
  protected Object[] buildChildren(@NotNull HierarchyNodeDescriptor descriptor) {
    List<PerlHierarchyNodeDescriptor> result = new ArrayList<>();

    if (descriptor instanceof PerlHierarchyNodeDescriptor) {
      PsiElement element = ((PerlHierarchyNodeDescriptor)descriptor).getPerlElement();
      for (PsiElement childElement : getSubElements(element)) {
        result.add(createDescriptor(descriptor, childElement, false));
      }
    }

    return result.toArray();
  }

  protected Collection<PsiElement> getSubElements(PsiElement element) {
    assert element instanceof PerlNamespaceDefinitionElement;
    return new ArrayList<>(((PerlNamespaceDefinitionElement)element).getChildNamespaceDefinitions());
  }

  protected PerlHierarchyNodeDescriptor createDescriptor(NodeDescriptor parentDescriptor, PsiElement element, boolean isBase) {
    return new PerlHierarchyNodeDescriptor(parentDescriptor, element, isBase);
  }
}
