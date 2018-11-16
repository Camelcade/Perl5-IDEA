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

import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public interface PerlSubDefinition extends PerlSub {
  /**
   * Returns list of accepted arguments
   *
   * @return list of accepted arguments
   */
  @NotNull
  List<PerlSubArgument> getSubArgumentsList();

  default List<PerlSubArgument> getSubArgumentsListWithoutSelf() {
    List<PerlSubArgument> subArguments = getSubArgumentsList();

    if (isMethod() && !subArguments.isEmpty()) {
      return subArguments.size() > 1 ? subArguments.subList(1, subArguments.size()) : Collections.emptyList();
    }
    return subArguments;
  }

  default String getSubArgumentsListAsString() {
    return PerlSubUtil.getArgumentsListAsString(getSubArgumentsListWithoutSelf());
  }
}
