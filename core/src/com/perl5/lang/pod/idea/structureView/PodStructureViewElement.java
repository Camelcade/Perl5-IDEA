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

package com.perl5.lang.pod.idea.structureView;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.pod.parser.psi.PodFile;
import com.perl5.lang.pod.parser.psi.PodSection;
import com.perl5.lang.pod.parser.psi.PodSectionOver;
import com.perl5.lang.pod.parser.psi.PodStructureElement;
import com.perl5.lang.pod.psi.PsiCutSection;
import com.perl5.lang.pod.psi.PsiPodSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PodStructureViewElement extends PsiTreeElementBase<PsiElement> {

  public PodStructureViewElement(PsiElement element) {
    super(element);
  }

  @Nullable
  @Override
  public String getPresentableText() {
    PsiElement element = getElement();
    if (element == null) {
      return null;
    }
    if (element instanceof PodFile) {
      return PerlBundle.message("pod.structure.view.file.title");
    }
    else if (element instanceof ItemPresentation) {
      return ((ItemPresentation)element).getPresentableText();
    }
    return null;
  }

  @Nullable
  @Override
  public String getLocationString() {
    return null;
  }

  @Nullable
  @Override
  public Icon getIcon(boolean unused) {
    PsiElement element = getElement();
    if (element == null) {
      return null;
    }
    return element instanceof PsiFile ? PerlIcons.POD_FILE : element.getIcon(0);
  }

  @NotNull
  @Override
  public Collection<StructureViewTreeElement> getChildrenBase() {
    PsiElement psiElement = getElement();
    if (psiElement == null) {
      return Collections.emptyList();
    }
    List<StructureViewTreeElement> result = new ArrayList<>();

    PsiElement container = null;
    if (psiElement instanceof PodSection) {
      container = ((PodSection)psiElement).getContentBlock();
    }

    if (container == null) {
      container = psiElement;
    }


    for (PsiElement element : container.getChildren()) {
      if (element instanceof PodStructureElement) {
        if (!(element instanceof PsiCutSection || element instanceof PsiPodSection)) {
          result.add(new PodStructureViewElement(element));
        }
      }
    }

    if (result.size() == 1 && result.get(0).getValue() instanceof PodSectionOver) {
      // expanding over
      StructureViewTreeElement childElement = result.get(0);
      if (childElement instanceof PodStructureViewElement) {
        return ((PodStructureViewElement)childElement).getChildrenBase();
      }
    }

    return result;
  }
}
