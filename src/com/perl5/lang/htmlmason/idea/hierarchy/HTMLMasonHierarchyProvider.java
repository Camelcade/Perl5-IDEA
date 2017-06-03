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

package com.perl5.lang.htmlmason.idea.hierarchy;

import com.intellij.ide.hierarchy.HierarchyBrowser;
import com.intellij.ide.hierarchy.TypeHierarchyBrowserBase;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.perl5.lang.htmlmason.HTMLMasonLanguage;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.perl.idea.hierarchy.namespace.PerlHierarchyBrowser;
import com.perl5.lang.perl.idea.hierarchy.namespace.PerlPackageHierarchyProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 12.03.2016.
 */
public class HTMLMasonHierarchyProvider extends PerlPackageHierarchyProvider {
  @Override
  protected Language getLanguage() {
    return HTMLMasonLanguage.INSTANCE;
  }

  @Override
  protected PsiElement adjustTargetElement(PsiElement element) {
    if (element != null && !(element instanceof HTMLMasonFileImpl)) {
      return element.getContainingFile();
    }

    return element;
  }

  @NotNull
  @Override
  public HierarchyBrowser createHierarchyBrowser(PsiElement target) {
    return new HTMLMasonHierarchyBrowser(target);
  }

  @Override
  public void browserActivated(@NotNull HierarchyBrowser hierarchyBrowser) {
    ((PerlHierarchyBrowser)hierarchyBrowser).changeView(TypeHierarchyBrowserBase.TYPE_HIERARCHY_TYPE);
  }
}
