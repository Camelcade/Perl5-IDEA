/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.PsiCommentImpl;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.psi.PerlHeredocTerminatorElement;
import com.perl5.lang.perl.psi.PerlReferenceOwner;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.references.PerlHeredocReference;
import org.jetbrains.annotations.NotNull;

public class PerlHeredocTerminatorElementImpl extends PsiCommentImpl implements PerlHeredocTerminatorElement {
  public PerlHeredocTerminatorElementImpl(IElementType type, CharSequence text) {
    super(type, text);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor perlVisitor) {
      perlVisitor.visitHeredocTeminator(this);
    }
    else {
      super.accept(visitor);
    }
  }

  @Override
  public final PsiReference @NotNull [] getReferences() {
    return PerlReferenceOwner.getReferencesWithCache(this);
  }

  @Override
  public final PsiReference getReference() {
    PsiReference[] references = getReferences();
    return references.length == 0 ? null : references[0];
  }

  @Override
  public PsiReference @NotNull [] computeReferences() {
    return new PsiReference[]{new PerlHeredocReference(this)};
  }

  @Override
  public boolean hasReferences() {
    return true;
  }

  @Override
  public boolean isValidHost() {
    return false;
  }
}
