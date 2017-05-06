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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PsiPerlUnconditionalBlock;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.perl.psi.properties.PerlLexicalScopeMember;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 27.05.2015.
 * Mixin for scope elements
 */
public abstract class PerlLexicalScopeMemberMixin extends PerlCompositeElementImpl implements PerlLexicalScopeMember {
  public PerlLexicalScopeMemberMixin(ASTNode node) {
    super(node);
  }


  @Override
  public PerlLexicalScope getLexicalScope() {
    return PsiTreeUtil.getParentOfType(this, PerlLexicalScope.class);
  }

  @Nullable
  public PsiPerlUnconditionalBlock getUnconditionalBlock() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlUnconditionalBlock.class);
  }
}
