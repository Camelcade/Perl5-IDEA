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

package com.perl5.lang.perl.parser.Class.Accessor.psi;

import com.perl5.lang.perl.extensions.PerlCompletionElementsProvider;
import com.perl5.lang.perl.extensions.PerlHierarchyViewElementsProvider;
import com.perl5.lang.perl.extensions.PerlRenameUsagesSubstitutor;
import com.perl5.lang.perl.parser.Class.Accessor.ClassAccessorElementTypes;
import com.perl5.lang.perl.psi.PerlSubDefinitionWithTextIdentifier;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 21.01.2016.
 */
public interface PerlClassAccessorDeclaration extends
                                              PerlSubDefinitionWithTextIdentifier,
                                              ClassAccessorElementTypes,
                                              PerlCompletionElementsProvider,
                                              PerlHierarchyViewElementsProvider,
                                              PerlRenameUsagesSubstitutor {
  String ACCESSOR_PREFIX = "get_";
  String MUTATOR_PREFIX = "set_";


  /**
   * Checks if current declaration should follow best practice, declare get_ and set_
   *
   * @return check result
   */
  boolean isFollowsBestPractice();

  /**
   * Checks if current accessor readable
   *
   * @return check result
   */
  boolean isAccessorReadable();

  /**
   * Checks if current accessor writable
   *
   * @return check result
   */
  boolean isAccessorWritable();


  String getGetterName();

  @Nullable
  String getGetterCanonicalName();

  String getSetterName();

  @Nullable
  String getSetterCanonicalName();
}
