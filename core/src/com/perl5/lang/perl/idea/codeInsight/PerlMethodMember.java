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

package com.perl5.lang.perl.idea.codeInsight;

import com.intellij.codeInsight.generation.ClassMember;
import com.intellij.codeInsight.generation.MemberChooserObject;
import com.intellij.codeInsight.generation.PsiElementMemberChooserObject;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.properties.PerlIdentifierOwner;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;


public class PerlMethodMember extends PsiElementMemberChooserObject implements ClassMember {
  public PerlMethodMember(@NotNull PsiElement psiElement) {
    super(psiElement, ((PerlIdentifierOwner)psiElement).getPresentableName(), psiElement.getIcon(0));
  }

  @Override
  public MemberChooserObject getParentNodeDelegate() {
    final PsiElement subDefinitionBase = getPsiElement();
    final PerlNamespaceDefinitionElement parent = (PerlNamespaceDefinitionElement)PerlPsiUtil
      .getParentElementOrStub(subDefinitionBase, PerlNamespaceDefinitionStub.class, PerlNamespaceDefinitionElement.class);
    assert (parent != null);
    return new PerlMethodMember(parent);
  }

  public static String trimUnderscores(String s) {
    return StringUtil.trimStart(StringUtil.trimStart(s, "_"), "_");
  }
}
