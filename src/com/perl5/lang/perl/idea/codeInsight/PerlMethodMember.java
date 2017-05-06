/*
 * Copyright 2016 Alexandr Evstigneev
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
import com.perl5.lang.perl.idea.stubs.namespaces.PerlNamespaceDefinitionStub;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlSubDefinitionBase;
import com.perl5.lang.perl.psi.properties.PerlNamedElement;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 29.01.2016.
 */
public class PerlMethodMember extends PsiElementMemberChooserObject implements ClassMember {
  public PerlMethodMember(@NotNull PsiElement psiElement) {
    super(psiElement, ((PerlNamedElement)psiElement).getPresentableName(), psiElement.getIcon(0));
  }

  @Override
  public MemberChooserObject getParentNodeDelegate() {
    final PerlSubDefinitionBase subDefinitionBase = (PerlSubDefinitionBase)getPsiElement();
    final PerlNamespaceDefinition parent = (PerlNamespaceDefinition)PerlPsiUtil
      .getParentElementOrStub(subDefinitionBase, PerlNamespaceDefinitionStub.class, PerlNamespaceDefinition.class);
    assert (parent != null);
    return new PerlMethodMember(parent);
  }

  public static String trimUnderscores(String s) {
    return StringUtil.trimStart(StringUtil.trimStart(s, "_"), "_");
  }
}
