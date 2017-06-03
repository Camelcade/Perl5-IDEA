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

package com.perl5.lang.perl.psi.properties;

import com.perl5.lang.perl.psi.PerlDeprecatable;
import com.perl5.lang.perl.psi.PerlVariableNameElement;

/**
 * Created by hurricup on 02.06.2015.
 */
public interface PerlVariableNameElementContainer extends PerlDeprecatable {
  /**
   * Returns variable name element
   *
   * @return PsiElement
   */
  PerlVariableNameElement getVariableNameElement();

  /**
   * Checks if variable is built in
   *
   * @return result
   */
  boolean isBuiltIn();
}
