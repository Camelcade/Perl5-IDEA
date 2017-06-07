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

package com.perl5.lang.perl.psi;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.StubBasedPsiElement;
import com.perl5.lang.perl.psi.properties.PerlIdentifierOwner;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariableStub;
import com.perl5.lang.perl.psi.utils.PerlVariableAnnotations;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.util.PerlPackageUtil.PACKAGE_SEPARATOR;

/**
 * Created by hurricup on 29.09.2015.
 */
public interface PerlVariableDeclarationWrapper
  extends StubBasedPsiElement<PerlVariableStub>, PerlIdentifierOwner, PerlCompositeElement, PerlDeprecatable {
  /**
   * Returns declared variable object
   *
   * @return variable object
   */
  PerlVariable getVariable();

  /**
   * Returns declaration type in annotation or declaration
   *
   * @return type string or null
   */
  @Nullable
  String getDeclaredType();

  /**
   * Returns declaration type if variable is in declaration
   *
   * @return type string or null
   */
  @Nullable
  String getLocallyDeclaredType();

  /**
   * Trying to get the package name from explicit specification or by traversing
   *
   * @return package name for current element
   */
  String getPackageName();

  /**
   * returns proper fqn
   *
   * @return fqn or null if name is missing
   */
  @Nullable
  default String getFullQualifiedName() {
    String name = getName();
    if (StringUtil.isEmpty(name)) {
      return null;
    }

    String packageName = getPackageName();
    if (StringUtil.isEmpty(packageName)) {
      return name;
    }
    return packageName + PACKAGE_SEPARATOR + name;
  }

  /**
   * Guessing actual variable type from context
   *
   * @return variable type
   */
  PerlVariableType getActualType();

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
   * Returns stubbed, local or external variable annotations
   *
   * @return annotations or null
   */
  @Nullable
  PerlVariableAnnotations getVariableAnnotations();

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
}
