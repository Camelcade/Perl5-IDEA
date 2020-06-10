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

package com.perl5.lang.htmlmason.idea.hierarchy;

import com.intellij.ide.hierarchy.HierarchyTreeStructure;
import com.intellij.ide.hierarchy.ViewClassHierarchyAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.psi.PsiElement;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.perl.idea.hierarchy.namespace.PerlHierarchyBrowser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class HTMLMasonHierarchyBrowser extends PerlHierarchyBrowser {
  public HTMLMasonHierarchyBrowser(PsiElement element) {
    super(element);
  }

  @Override
  protected boolean isApplicableElement(@NotNull PsiElement element) {
    return element instanceof HTMLMasonFileImpl;
  }

  @Override
  protected void prependActions(@NotNull DefaultActionGroup actionGroup) {
    actionGroup.add(new ViewClassHierarchyAction());
    super.prependActions(actionGroup);
  }

  @Override
  protected @Nullable HierarchyTreeStructure getTypesHierarchyStructure(PsiElement psiElement) {
    return new HTMLMasonTypeHierarchyTreeStructure(psiElement);
  }

  @Override
  protected @Nullable HierarchyTreeStructure getSuperTypesHierarchyStructure(PsiElement psiElement) {
    return new HTMLMasonSuperTypeHierarchyStructure(psiElement);
  }

  @Override
  protected @Nullable HierarchyTreeStructure getSubTypesHierarchyStructure(PsiElement psiElement) {
    return new HTMLMasonSubTypeHierarchyStructure(psiElement);
  }
}
