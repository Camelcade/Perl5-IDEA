/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.util;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;

import java.util.ArrayList;
import java.util.List;

public final class PerlSubUtilCore {
  public static final String SUB_AUTOLOAD = "AUTOLOAD";
  public static final String SUB_DESTROY = "DESTROY";
  @Transient
  public static final String DEPARSED_FILE_NAME = "_Deparsed_XSubs.pm";

  private PerlSubUtilCore() {
  }

  /**
   * Builds arguments string for presentation
   *
   * @param subArguments list of arguments
   * @return stringified prototype
   */
  public static String getArgumentsListAsString(List<? extends PerlSubArgument> subArguments) {
    int argumentsNumber = subArguments.size();

    List<String> argumentsList = new ArrayList<>();
    List<String> optionalAargumentsList = new ArrayList<>();

    for (PerlSubArgument argument : subArguments) {
      if (!optionalAargumentsList.isEmpty() || argument.isOptional()) {
        optionalAargumentsList.add(argument.toStringShort());
      }
      else {
        argumentsList.add(argument.toStringShort());
      }

      int compiledListSize = argumentsList.size() + optionalAargumentsList.size();
      if (compiledListSize > 5 && argumentsNumber > compiledListSize) {
        if (!optionalAargumentsList.isEmpty()) {
          optionalAargumentsList.add("...");
        }
        else {
          argumentsList.add("...");
        }
        break;
      }
    }

    if (argumentsList.isEmpty() && optionalAargumentsList.isEmpty()) {
      return "";
    }

    String argumentListString = StringUtil.join(argumentsList, ", ");
    String optionalArgumentsString = StringUtil.join(optionalAargumentsList, ", ");

    if (argumentListString.isEmpty()) {
      return "([" + optionalArgumentsString + "])";
    }
    if (optionalAargumentsList.isEmpty()) {
      return "(" + argumentListString + ")";
    }
    else {
      return "(" + argumentListString + " [, " + optionalArgumentsString + "])";
    }
  }
}
