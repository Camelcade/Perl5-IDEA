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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.stubs.IStubElementType;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.presentations.PerlItemPresentationSimple;
import com.perl5.lang.perl.psi.PerlSubDeclarationElement;
import com.perl5.lang.perl.psi.PsiPerlExpr;
import com.perl5.lang.perl.psi.stubs.subsdeclarations.PerlSubDeclarationStub;
import com.perl5.lang.perl.xsubs.PerlXSubsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public abstract class PerlSubDeclarationBase extends PerlSubBase<PerlSubDeclarationStub> implements PerlSubDeclarationElement {
  public PerlSubDeclarationBase(@NotNull ASTNode node) {
    super(node);
  }

  public PerlSubDeclarationBase(@NotNull PerlSubDeclarationStub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Nullable
  @Override
  public Icon getIcon(int flags) {
    return isXSub()
           ? PerlIcons.XSUB_GUTTER_ICON
           : PerlIcons.SUB_DECLARATION_GUTTER_ICON;
  }

  @Override
  public ItemPresentation getPresentation() {
    return new PerlItemPresentationSimple(this, getPresentableName());
  }

  @Override
  public boolean isMethod() {
    return true;
  }

  @Override
  public boolean isStatic() {
    return true;
  }

  @Override
  public boolean isXSub() {
    return StringUtil.equals(getContainingFile().getName(), PerlXSubsState.DEPARSED_FILE_NAME);
  }

  @Nullable
  public PsiPerlExpr getExpr() {return null;}
}
