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

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 25.05.2015.
 */
public class PerlVariableNameElementImpl extends PerlLeafPsiElementWithReferences implements PerlVariableNameElement {
  public PerlVariableNameElementImpl(@NotNull IElementType type, CharSequence text) {
    super(type, text);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PerlVisitor) {
      ((PerlVisitor)visitor).visitVariableNameElement(this);
    }
    else {
      super.accept(visitor);
    }
  }

  @Override
  public String getName() {
    return getText();
  }
}
