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

package com.perl5.lang.pod.parser.psi.impl;

import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.psi.impl.PerlLeafPsiElementWithReferences;
import com.perl5.lang.pod.parser.psi.PodSectionTitle;
import com.perl5.lang.pod.parser.psi.references.PodSubReference;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PodIdentifierImpl extends PerlLeafPsiElementWithReferences {
  public PodIdentifierImpl(@NotNull IElementType type, CharSequence text) {
    super(type, text);
  }

  @Override
  public PsiReference[] computeReferences() {
    final PodIdentifierImpl element = PodIdentifierImpl.this;
    List<PsiReference> references = new ArrayList<>();

    if (element.getParent() instanceof PodSectionTitle && element.getPrevSibling() == null) {
      references.add(new PodSubReference(element));
    }

    references.addAll(Arrays.asList(ReferenceProvidersRegistry.getReferencesFromProviders(element)));

    return references.toArray(new PsiReference[references.size()]);
  }
}
