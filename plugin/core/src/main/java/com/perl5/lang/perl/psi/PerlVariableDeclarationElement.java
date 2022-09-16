/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi;

import com.intellij.navigation.NavigationItem;
import com.intellij.psi.PsiElement;
import com.intellij.psi.StubBasedPsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.psi.properties.PerlIdentifierOwner;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariableDeclarationStub;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import com.perl5.lang.perl.psi.utils.PerlVariableAnnotations;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.COLON;
import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.SIGNATURE_ELEMENT;


public interface PerlVariableDeclarationElement extends StubBasedPsiElement<PerlVariableDeclarationStub>,
                                                        PerlIdentifierOwner,
                                                        PerlCompositeElement,
                                                        PerlVariableDeclaration,
                                                        NavigationItem {
  /**
   * @return Returns declared variable object
   */
  @NotNull
  PerlVariable getVariable();

  default @Nullable Icon getIconByType(@NotNull PerlVariableType type) {
    if (type == PerlVariableType.ARRAY) {
      return PerlIcons.ARRAY_GUTTER_ICON;
    }
    if (type == PerlVariableType.HASH) {
      return PerlIcons.HASH_GUTTER_ICON;
    }
    if (type == PerlVariableType.SCALAR) {
      return PerlIcons.SCALAR_GUTTER_ICON;
    }
    return null;
  }

  /**
   * Checks if this declaration is lexical. IMPORTANT: builds PSI
   *
   * @return checking result
   */
  boolean isLexicalDeclaration();

  /**
   * Checks if this declaration is local. IMPORTANT: builds PSI
   *
   * @return checking result
   */
  boolean isLocalDeclaration();

  /**
   * Checks if this declaration is global. IMPORTANT: builds PSI
   *
   * @return checking result
   */
  boolean isGlobalDeclaration();

  /**
   * Checks if this is method's invocant declaration. IMPORTANT: builds PSI
   *
   * @return checking result
   */
  boolean isInvocantDeclaration();

  /**
   * Checks if variable is configured $self
   *
   * @return true of false
   */
  default boolean isSelf() {
    return getActualType() == PerlVariableType.SCALAR && PerlSharedSettings.getInstance(getProject()).isSelfName(getName());
  }

  /**
   * Returns local variable annotations if any
   *
   * @return annotations object or null
   */
  @Nullable
  PerlVariableAnnotations getLocalVariableAnnotations();


  /**
   * Returns external variable annotations if any
   *
   * @return annotations object or null
   */
  @Nullable
  PerlVariableAnnotations getExternalVariableAnnotations();

  /**
   * @return true iff {@code psiElement} is variable declaration element preceding by colon. See {@code Function::Parameters} named
   * parameters syntax.
   */
  @Contract("null -> false")
  static boolean isNamedParameter(@Nullable PsiElement psiElement) {
    return psiElement instanceof PerlVariableDeclarationElement &&
           PsiUtilCore.getElementType(psiElement.getParent()) == SIGNATURE_ELEMENT &&
           PsiUtilCore.getElementType(PerlPsiUtil.getPrevSignificantSibling(psiElement)) == COLON;
  }
}
