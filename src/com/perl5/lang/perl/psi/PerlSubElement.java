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

import com.intellij.psi.StubBasedPsiElement;
import com.perl5.lang.perl.psi.properties.PerlIdentifierOwner;
import com.perl5.lang.perl.psi.properties.PerlLabelScope;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import com.perl5.lang.perl.psi.properties.PerlPackageMember;
import com.perl5.lang.perl.psi.stubs.PerlSubStub;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 05.06.2015.
 */
public interface PerlSubElement<Stub extends PerlSubStub> extends PerlSub,
                                                                  StubBasedPsiElement<Stub>,
                                                                  PerlPackageMember,
                                                                  PerlIdentifierOwner,
                                                                  PerlNamespaceElementContainer,
                                                                  PerlDeprecatable,
                                                                  PerlLabelScope {

  /**
   * Checks if sub defined as a method
   *
   * @return result
   */
  boolean isMethod();


  /**
   * Checks if sub defined as static, default implementation returns !isMethod(), but may be different for constants for example
   *
   * @return true if sub is static
   */
  boolean isStatic();

  /**
   * Checks if current declaration/definition is XSub
   *
   * @return true if sub located in deparsed file
   */
  boolean isXSub();

  /**
   * Returns local sub annotations if any
   *
   * @return annotations object or null
   */
  @Nullable
  PerlSubAnnotations getLocalAnnotations();
}
