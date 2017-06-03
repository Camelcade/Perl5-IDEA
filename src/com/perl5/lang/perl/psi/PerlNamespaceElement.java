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

import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;

import java.util.List;

/**
 * Created by hurricup on 31.05.2015.
 */
public interface PerlNamespaceElement extends PsiElement, PerlDeprecatable {
  /**
   * Checks if package is from Core list
   *
   * @return result
   */
  boolean isBuiltin();

  /**
   * Checks if package is pragma
   *
   * @return result
   */
  boolean isPragma();

  /**
   * Returns list of definitions of current namespace
   *
   * @return list of PerlNameSpaceDefitions
   */
  List<PerlNamespaceDefinition> getNamespaceDefinitions();

  /**
   * Returns list of files suitable for this namespace, works only if namespace is in use or require statement
   *
   * @return list of PerlNameSpaceDefitions
   */
  List<PerlFileImpl> getNamespaceFiles();

  /**
   * Returns canonical namespace name
   *
   * @return canonical name
   */
  String getCanonicalName();

  /**
   * Checks if namespace is SUPER::
   *
   * @return check result
   */
  boolean isSUPER();

  /**
   * Checks if namespace is SUPER::
   *
   * @return check result
   */
  boolean isMain();

  /**
   * Checks if namespace is SUPER::
   *
   * @return check result
   */
  boolean isCORE();

  /**
   * Checks if namespace is UNIVERSAL::
   *
   * @return check result
   */
  boolean isUNIVERSAL();
}
