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

package com.perl5.lang.tt2.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.impl.providers.FileReferenceSet;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.tt2.psi.TemplateToolkitString;
import com.perl5.lang.tt2.psi.impl.TemplateToolkitCompositeElementImpl;
import com.perl5.lang.tt2.psi.references.TemplateToolkitBlockReference;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class TemplateToolkitStringMixin extends TemplateToolkitCompositeElementImpl implements TemplateToolkitString {
  public static final TokenSet BLOCK_NAME_TARGETED_CONTAINERS = TokenSet.create(
    INCLUDE_DIRECTIVE,
    PROCESS_DIRECTIVE,
    WRAPPER_DIRECTIVE
  );
  protected static final TokenSet FILES_TARGETED_CONTAINERS = TokenSet.create(
    INSERT_DIRECTIVE,
    INCLUDE_DIRECTIVE,
    PROCESS_DIRECTIVE,
    WRAPPER_DIRECTIVE
  );

  public TemplateToolkitStringMixin(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiReference[] computeReferences() {
    List<PsiReference> references = new ArrayList<>();
    IElementType parentElementType = PsiUtilCore.getElementType(getParent());

    if (FILES_TARGETED_CONTAINERS.contains(parentElementType)) {
      references.addAll(Arrays.asList(new FileReferenceSet(TemplateToolkitStringMixin.this) {
        @NotNull
        @Override
        public Collection<PsiFileSystemItem> computeDefaultContexts() {
          String path = getPathString();
          PsiFile containingFile = getContainingFile();
          if (StringUtil.startsWith(path, ".") && containingFile != null && containingFile.getParent() != null) {
            return Collections.singletonList(containingFile.getParent());
          }
          return super.computeDefaultContexts();
        }
      }.getAllReferences()));
    }

    if (BLOCK_NAME_TARGETED_CONTAINERS.contains(parentElementType)) {
      references.add(new TemplateToolkitBlockReference(TemplateToolkitStringMixin.this));
    }

    return references.toArray(new PsiReference[references.size()]);
  }

  @Override
  public boolean hasReferences() {
    return true;
  }
}
