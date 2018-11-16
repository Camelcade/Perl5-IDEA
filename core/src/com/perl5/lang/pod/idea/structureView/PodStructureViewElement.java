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

package com.perl5.lang.pod.idea.structureView;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.pod.parser.psi.*;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import com.perl5.lang.pod.psi.PsiCutSection;
import com.perl5.lang.pod.psi.PsiPodSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 03.04.2016.
 */
public class PodStructureViewElement implements StructureViewTreeElement {
  private final PsiElement myElement;

  public PodStructureViewElement(PsiElement element) {
    myElement = element;
  }

  @Override
  public Object getValue() {
    return myElement;
  }

  @Override
  public void navigate(boolean requestFocus) {
    if (myElement instanceof NavigationItem) {
      ((NavigationItem)myElement).navigate(requestFocus);
    }
  }

  @Override
  public boolean canNavigate() {
    return myElement instanceof NavigationItem &&
           ((NavigationItem)myElement).canNavigate();
  }

  @Override
  public boolean canNavigateToSource() {
    return myElement instanceof NavigationItem &&
           ((NavigationItem)myElement).canNavigateToSource();
  }

  @NotNull
  @Override
  public ItemPresentation getPresentation() {
    return new ItemPresentation() {
      @Nullable
      @Override
      public String getPresentableText() {
        if (myElement instanceof PodFile) {
          return PerlBundle.message("pod.structure.view.file.title");
        }
        else if (myElement instanceof PodTitledSection) {
          String title = null;
          if (myElement instanceof PodSectionItem && ((PodSectionItem)myElement).isBulleted()) {
            PsiElement itemContent = ((PodSectionItem)myElement).getContentBlock();
            if (itemContent != null) {
              title = PodRenderUtil.renderPsiElementAsText(itemContent);
              if (title.length() > 80) {
                title = title.substring(0, 80) + "...";
              }
            }
          }

          if (title == null) {
            title = ((PodTitledSection)myElement).getTitleText();
          }
          if (StringUtil.isNotEmpty(title)) {
            return title;
          }
        }

        PsiElement tag = myElement.getFirstChild();
        if (tag != null) {
          return tag.getText();
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
        return myElement instanceof PsiFile ? PerlIcons.POD_FILE : myElement.getIcon(0);
      }
    };
  }


  @NotNull
  @Override
  public TreeElement[] getChildren() {
    List<PodStructureViewElement> result = new ArrayList<>();

    PsiElement container = null;
    if (myElement instanceof PodSection) {
      container = ((PodSection)myElement).getContentBlock();
    }

    if (container == null) {
      container = myElement;
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
      return result.get(0).getChildren();
    }

    return result.toArray(new TreeElement[result.size()]);
  }
}
