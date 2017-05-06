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

import com.intellij.ide.hierarchy.HierarchyNodeDescriptor;
import com.intellij.psi.PsiElement;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by hurricup on 12.03.2016.
 */
public class HTMLMasonTypeHierarchyTreeStructure extends HTMLMasonSubTypeHierarchyStructure {
  public HTMLMasonTypeHierarchyTreeStructure(@NotNull PsiElement element) {
    super(element.getProject(), buildHierarchyTree(element));
    setBaseElement(myBaseDescriptor);
  }

  protected static HierarchyNodeDescriptor buildHierarchyTree(PsiElement element) {
    HierarchyNodeDescriptor lastParent = null;

    List<PsiElement> linearParents = getLinearParents(element);

    for (int i = 0; i < linearParents.size(); i++) {
      HierarchyNodeDescriptor newDescriptor =
        new HTMLMasonHierarchyNodeDescriptor(lastParent, linearParents.get(i), i == linearParents.size() - 1);
      if (lastParent != null) {
        lastParent.setCachedChildren(new HierarchyNodeDescriptor[]{newDescriptor});
      }
      lastParent = newDescriptor;
    }

    return lastParent;
  }

  protected static List<PsiElement> getLinearParents(PsiElement element) {
    assert element instanceof HTMLMasonFileImpl;
    List<PsiElement> result = new ArrayList<PsiElement>();
    Set<PsiElement> recursionMap = new THashSet<PsiElement>();
    HTMLMasonFileImpl run = (HTMLMasonFileImpl)element;

    while (run != null) {
      if (recursionMap.contains(run)) {
        break;
      }
      result.add(0, run);
      recursionMap.add(run);

      run = run.getParentComponent();
    }

    return result;
  }
}
