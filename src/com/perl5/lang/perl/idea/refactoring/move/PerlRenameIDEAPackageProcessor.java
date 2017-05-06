/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.refactoring.move;

import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiDirectoryContainer;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 03.10.2015.
 */
public class PerlRenameIDEAPackageProcessor extends PerlRenameDirectoryProcessor {
  @Override
  public boolean canProcessElement(@NotNull PsiElement element) {
    if (element instanceof PsiDirectoryContainer)    // package
    {
      for (PsiDirectory dir : ((PsiDirectoryContainer)element).getDirectories()) {
        if (!canProcessDir(dir)) {
          return false;
        }
      }
      return true;
    }

    return false;
  }

  protected void renamePsiElement(PsiElement element, String newName) {
    if (element instanceof PsiDirectoryContainer)    // package
    {
      for (PsiDirectory dir : ((PsiDirectoryContainer)element).getDirectories()) {
        super.renamePsiElement(dir, newName);
      }
    }
  }
}
