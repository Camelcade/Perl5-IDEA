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

package com.perl5.lang.perl.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.extensions.parser.PerlReferencesProvider;
import com.perl5.lang.perl.psi.PerlString;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.references.PerlNamespaceReference;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 23.05.2015.
 */
public class PerlStringContentElementImpl extends PerlLeafPsiElementWithReferences implements PerlStringContentElement {
  public PerlStringContentElementImpl(@NotNull IElementType type, CharSequence text) {
    super(type, text);
  }

  @Override
  public PsiReference[] computeReferences() {
    List<PsiReference> result = new ArrayList<>();
    String continuosText = getContinuosText();
    if (PerlString.looksLikePackage(continuosText)) {
      result.add(new PerlNamespaceReference(PerlStringContentElementImpl.this));
    }
    else {
      PerlReferencesProvider referencesProvider =
        PsiTreeUtil.getParentOfType(PerlStringContentElementImpl.this, PerlReferencesProvider.class, true, PsiPerlStatement.class);

      if (referencesProvider != null) {
        PsiReference[] references = referencesProvider.getReferences(PerlStringContentElementImpl.this);
        if (references != null) {
          result.addAll(Arrays.asList(references));
        }
      }
    }
    result.addAll(Arrays.asList(ReferenceProvidersRegistry.getReferencesFromProviders(PerlStringContentElementImpl.this)));
    return result.toArray(new PsiReference[0]);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) {
      ((PerlVisitor)visitor).visitStringContentElement(this);
    }
    else {
      super.accept(visitor);
    }
  }

  @Override
  public String getContinuosText()    // fixme some caching here would be nice; Also we now could implement smarter logic
  {
    StringBuilder builder = new StringBuilder("");

    PsiElement currentElement = this;

    while (currentElement.getPrevSibling() instanceof PerlStringContentElement) {
      currentElement = currentElement.getPrevSibling();
    }

    while (currentElement instanceof PerlStringContentElement) {
      builder.append(currentElement.getNode().getText());
      currentElement = currentElement.getNextSibling();
    }

    return builder.toString();
  }
}


