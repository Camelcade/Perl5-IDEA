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

package com.perl5.lang.perl.documentation;

import com.intellij.lang.documentation.AbstractDocumentationProvider;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.pod.filetypes.PodFileType;

public abstract class PerlDocumentationProviderBase extends AbstractDocumentationProvider {
  @Override
  public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object object, PsiElement element) {
    if (object instanceof VirtualFile) {
      PsiFile psiFile = psiManager.findFile((VirtualFile)object);
      if (psiFile == null) {
        return null;
      }
      if (psiFile.getFileType() == PodFileType.INSTANCE) {
        return psiFile;
      }
      if (psiFile instanceof PerlFileImpl) {
        String filePackageName = ((PerlFileImpl)psiFile).getFilePackageName();
        if (filePackageName != null) {
          return PerlDocUtil.resolveDoc(filePackageName, null, psiFile, true);
        }
      }
    }
    return super.getDocumentationElementForLookupItem(psiManager, object, element);
  }
}
