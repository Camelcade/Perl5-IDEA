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
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.roots.ui.util.CompositeAppearance;
import com.intellij.psi.PsiElement;
import com.perl5.lang.htmlmason.parser.psi.impl.HTMLMasonFileImpl;
import com.perl5.lang.perl.idea.hierarchy.namespace.PerlHierarchyNodeDescriptor;


public class HTMLMasonHierarchyNodeDescriptor extends PerlHierarchyNodeDescriptor {
  public HTMLMasonHierarchyNodeDescriptor(NodeDescriptor parentDescriptor, PsiElement element, boolean isBase) {
    super(parentDescriptor, element, isBase);
  }

  @Override
  protected void adjustAppearance(CompositeAppearance appearance, ItemPresentation presentation) {
    String absoluteComponentPath = ((HTMLMasonFileImpl)getPerlElement()).getAbsoluteComponentContainerPath();

    if (absoluteComponentPath != null) {
      appearance.getEnding().addText(
        " in " + absoluteComponentPath,
        HierarchyNodeDescriptor.getPackageNameAttributes()
        //					UsageTreeColorsScheme.getInstance().getScheme().getAttributes(UsageTreeColors.USAGE_LOCATION)
      );
    }
  }
}
