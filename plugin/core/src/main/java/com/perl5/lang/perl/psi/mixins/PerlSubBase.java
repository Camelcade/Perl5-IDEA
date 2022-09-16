/*
 * Copyright 2015-2020 Alexandr Evstigneev
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
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.util.IncorrectOperationException;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.psi.PerlDeprecatable;
import com.perl5.lang.perl.psi.PerlNamespaceElement;
import com.perl5.lang.perl.psi.PerlStubBasedPsiElementBase;
import com.perl5.lang.perl.psi.PerlSubElement;
import com.perl5.lang.perl.psi.properties.PerlLabelScope;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import com.perl5.lang.perl.psi.stubs.PerlSubStub;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.SUB_NAME;


public abstract class PerlSubBase<Stub extends PerlSubStub<?>> extends PerlStubBasedPsiElementBase<Stub>
  implements PerlSubElement,
             StubBasedPsiElement<Stub>,
             PerlNamespaceElementContainer,
             PerlDeprecatable,
             PerlLabelScope {
  public PerlSubBase(@NotNull ASTNode node) {
    super(node);
  }

  public PerlSubBase(@NotNull Stub stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  @Override
  public @Nullable String getNamespaceName() {
    Stub stub = getGreenStub();
    if (stub != null) {
      return stub.getNamespaceName();
    }

    String namespace = getExplicitNamespaceName();
    if (namespace == null) {
      namespace = PerlPackageUtil.getContextNamespaceName(this);
    }

    return namespace;
  }


  @Override
  public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
    return PerlPsiUtil.renameNamedElement(this, name);
  }

  @Override
  public String getName() {
    return getSubName();
  }

  @Override
  public String getSubName() {
    Stub stub = getGreenStub();
    if (stub != null) {
      return stub.getSubName();
    }

    return getSubNameHeavy();
  }

  protected String getSubNameHeavy() {
    PsiElement subNameElement = getNameIdentifier();
    // fixme manipulator?
    return subNameElement == null ? null : subNameElement.getText();
  }

  @Override
  public @Nullable String getExplicitNamespaceName() {
    PerlNamespaceElement namespaceElement = getNamespaceElement();
    return namespaceElement != null ? namespaceElement.getCanonicalName() : null;
  }

  @Override
  public @Nullable PsiElement getNameIdentifier() {
    return findChildByType(SUB_NAME);
  }

  @Override
  public @Nullable PerlNamespaceElement getNamespaceElement() {
    return findChildByClass(PerlNamespaceElement.class);
  }

  @Override
  public @Nullable PerlSubAnnotations getAnnotations() {
    Stub stub = getGreenStub();
    if (stub != null) {
      return stub.getAnnotations();
    }
    return PerlSubAnnotations.createFromAnnotationsList(PerlPsiUtil.collectAnnotations(this));
  }

  @Override
  public @Nullable Icon getIcon(int flags) {
    if (isMethod()) {
      return PerlIcons.METHOD_GUTTER_ICON;
    }
    else {
      return PerlIcons.SUB_GUTTER_ICON;
    }
  }

  @Override
  public int getTextOffset() {
    PsiElement nameIdentifier = getNameIdentifier();

    return nameIdentifier == null
           ? super.getTextOffset()
           : nameIdentifier.getTextOffset();
  }

  @Override
  public String toString() {
    return super.toString() + "@" + (isValid() ? getCanonicalName() : "!INVALID!");
  }
}
