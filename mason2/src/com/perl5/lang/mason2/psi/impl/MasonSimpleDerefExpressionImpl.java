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

package com.perl5.lang.mason2.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.mason2.psi.MasonNamespaceDefinition;
import com.perl5.lang.perl.psi.impl.PsiPerlDerefExprImpl;
import com.perl5.lang.perl.types.PerlType;
import com.perl5.lang.perl.types.PerlTypeNamespace;

/**
 * Created by hurricup on 08.01.2016.
 */
public class MasonSimpleDerefExpressionImpl extends PsiPerlDerefExprImpl {
  public MasonSimpleDerefExpressionImpl(ASTNode node) {
    super(node);
  }

  @Override
  public PerlType getCurrentElementNamespace(PsiElement currentElement) {
    MasonNamespaceDefinition namespaceDefinition = PsiTreeUtil.getParentOfType(this, MasonNamespaceDefinition.class);

    if (namespaceDefinition != null) {
      return PerlTypeNamespace.fromNamespace(namespaceDefinition.getPackageName());
    }

    return super.getCurrentElementNamespace(currentElement);
  }
}
