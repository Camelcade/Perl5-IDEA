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

package com.perl5.lang.perl.idea.structureView;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.Filter;
import com.intellij.ide.util.treeView.smartTree.Grouper;
import com.intellij.ide.util.treeView.smartTree.Sorter;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.idea.structureView.elements.PerlLeafStructureViewElement;
import com.perl5.lang.perl.idea.structureView.elements.PerlStructureViewElement;
import com.perl5.lang.perl.idea.structureView.filters.*;
import com.perl5.lang.perl.idea.structureView.groupers.PerlAttributeGrouper;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 15.08.2015.
 */
public class PerlStructureViewModel extends StructureViewModelBase implements StructureViewModel.ElementInfoProvider {
  private static final Filter[] FILTERS = new Filter[]{
    PerlPodFilter.INSTANCE,
    PerlVariableFilter.INSTANCE,
    PerlGlobFilter.INSTANCE,
    PerlConstantFilter.INSTANCE,
    PerlMethodFilter.INSTANCE,
    PerlDeclarationFilter.INSTANCE,
    PerlInheritedFilter.INSTANCE,
    PerlImportedFilter.INSTANCE
  };

  private static final Grouper[] GROUPERS = new Grouper[]{
    new PerlAttributeGrouper()
  };

  public PerlStructureViewModel(PsiFile psiFile, Editor editor) {
    super(psiFile, editor, new PerlStructureViewElement(psiFile) {
    });
  }

  @NotNull
  @Override
  public Sorter[] getSorters() {
    return new Sorter[]{Sorter.ALPHA_SORTER};
  }

  @NotNull
  @Override
  public Grouper[] getGroupers() {
    return GROUPERS;
  }

  @NotNull
  @Override
  public Filter[] getFilters() {
    return FILTERS;
  }

  @Override
  public boolean isAlwaysShowsPlus(StructureViewTreeElement structureViewTreeElement) {
    return false;
  }

  @Override
  public boolean isAlwaysLeaf(StructureViewTreeElement structureViewTreeElement) {
    return structureViewTreeElement instanceof PerlLeafStructureViewElement;
  }
}
