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

package com.perl5.lang.pod.parser.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiFile;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.psi.references.PerlCachingReference;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.pod.filetypes.PodFileType;
import com.perl5.lang.pod.parser.psi.PodLinkDescriptor;
import com.perl5.lang.pod.parser.psi.mixin.PodFormatterL;
import com.perl5.lang.pod.parser.psi.util.PodFileUtil;
import org.jetbrains.annotations.NotNull;

public class PodLinkToFileReference extends PerlCachingReference<PodFormatterL> {
  public PodLinkToFileReference(@NotNull PodFormatterL element, @NotNull TextRange range) {
    super(element, range);
  }

  @Override
  protected @NotNull ResolveResult[] resolveInner(boolean incompleteCode) {
    PodFormatterL podLink = getElement();
    PodLinkDescriptor descriptor = podLink.getLinkDescriptor();

    if (descriptor != null && !descriptor.isUrl() && descriptor.getName() != null) {
      PsiFile targetFile = PodFileUtil.getPodOrPackagePsiByDescriptor(podLink.getProject(), descriptor);
      if (targetFile != null) {
        return new ResolveResult[]{new PsiElementResolveResult(targetFile)};
      }
    }

    return ResolveResult.EMPTY_ARRAY;
  }

  @Override
  public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
    PodLinkDescriptor descriptor = myElement.getLinkDescriptor();
    if (descriptor != null) {
      String currentName = descriptor.getName();

      if (StringUtil.isNotEmpty(currentName) &&
          (newElementName.endsWith("." + PerlFileTypePackage.EXTENSION) || newElementName.endsWith("." + PodFileType.EXTENSION))) {
        String[] nameChunks = currentName.split(PerlPackageUtil.NAMESPACE_SEPARATOR);
        nameChunks[nameChunks.length - 1] = newElementName.replaceFirst(PodFileUtil.PM_OR_POD_EXTENSION_PATTERN, "");
        newElementName = StringUtil.join(nameChunks, PerlPackageUtil.NAMESPACE_SEPARATOR);

        return super.handleElementRename(newElementName);
      }
    }
    return myElement;
  }

  @Override
  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
    if (element instanceof PsiFile) {
      String newName = PodFileUtil.getPackageName((PsiFile)element);
      if (StringUtil.isNotEmpty(newName)) {
        return super.handleElementRename(newName);
      }
    }
    return myElement;
  }
}
