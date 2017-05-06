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

package com.perl5.lang.perl.parser.moose.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseAttribute;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseHasStatement;
import com.perl5.lang.perl.psi.PerlAnnotation;
import com.perl5.lang.perl.psi.PsiPerlStatement;
import com.perl5.lang.perl.psi.impl.PerlSubDefinitionWithTextIdentifierImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 29.11.2015.
 */
public class PerlMooseAttributeImpl extends PerlSubDefinitionWithTextIdentifierImpl implements PerlMooseAttribute {
  public PerlMooseAttributeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public PerlMooseAttributeImpl(@NotNull PerlSubDefinitionStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Nullable
  protected PerlMooseHasStatement getHasStatement() {
    //noinspection unchecked
    return PsiTreeUtil.getParentOfType(this, PerlMooseHasStatement.class, true, PsiPerlStatement.class);
  }

  @Override
  public boolean isExtension() {
    PsiElement prevElement = getPrevSibling();
    // fixme adjust
    return false; //prevElement instanceof PerlStringContentElement && prevElement.getNode().getElementType() == PerlElementTypes.STRING_PLUS;
  }


  @NotNull
  @Override
  public List<PerlAnnotation> getAnnotationList() {
    PerlMooseHasStatement hasStatement = getHasStatement();
    if (hasStatement != null) {
      return PerlPsiUtil.collectAnnotations(hasStatement);
    }
    return super.getAnnotationList();
  }
}
