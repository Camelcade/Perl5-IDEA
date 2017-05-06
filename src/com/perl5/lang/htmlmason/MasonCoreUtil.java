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

package com.perl5.lang.htmlmason;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.impl.FakeVirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.indexing.IndexingDataKeys;
import com.perl5.lang.htmlmason.idea.configuration.AbstractMasonSettings;
import com.perl5.lang.mason2.idea.configuration.VariableDescription;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.PerlVariableDeclarationWrapper;
import com.perl5.lang.perl.psi.impl.PerlVariableLightImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 05.03.2016.
 */
public class MasonCoreUtil {
  @Nullable
  public static VirtualFile getComponentRoot(@NotNull AbstractMasonSettings masonSettings, @Nullable VirtualFile file) {
    if (file != null) {
      if (file instanceof FakeVirtualFile) {
        file = file.getParent();
      }

      if (file != null) {
        //noinspection unchecked
        for (VirtualFile componentRoot : (List<VirtualFile>)masonSettings.getComponentsRootsVirtualFiles()) {
          if (VfsUtil.isAncestor(componentRoot, file, false)) {
            return componentRoot;
          }
        }
      }
    }
    return null;
  }

  /**
   * Returns real containing virtual file, not the Light one
   *
   * @return virtual file or null
   */
  @Nullable
  public static VirtualFile getContainingVirtualFile(PsiFile psiFile) {
    VirtualFile originalFile = psiFile.getViewProvider().getVirtualFile();

    if (originalFile instanceof LightVirtualFile) {
      if (psiFile.getUserData(IndexingDataKeys.VIRTUAL_FILE) != null) {
        originalFile = psiFile.getUserData(IndexingDataKeys.VIRTUAL_FILE);
      }
      else if (((LightVirtualFile)originalFile).getOriginalFile() != null) {
        originalFile = ((LightVirtualFile)originalFile).getOriginalFile();
      }
    }
    return originalFile instanceof LightVirtualFile || originalFile == null || !originalFile.exists() ? null : originalFile;
  }

  public static void fillVariablesList(PsiElement parent,
                                       List<PerlVariableDeclarationWrapper> targetList,
                                       List<VariableDescription> sourceList) {
    for (VariableDescription variableDescription : sourceList) {
      String variableType = variableDescription.variableType;
      if (StringUtil.isEmpty(variableType)) {
        variableType = null;
      }
      targetList.add(
        new PerlVariableLightImpl(
          parent.getManager(),
          PerlLanguage.INSTANCE,
          variableDescription.variableName,
          variableType,
          false,
          false,
          false,
          parent
        ));
    }
  }
}
