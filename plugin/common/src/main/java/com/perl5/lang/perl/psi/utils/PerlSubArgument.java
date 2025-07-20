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

package com.perl5.lang.perl.psi.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlUndefValue;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.util.PerlScalarUtilCore;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;


public class PerlSubArgument {

  public static final @NonNls String NEW_VALUE_VALUE = "new_value";
  private final @NotNull PerlVariableType myArgumentType;
  private final @NotNull String myArgumentName;

  private final @NotNull String myVariableClass;

  private Boolean myIsOptional;

  private PerlSubArgument(@NotNull PerlVariableType variableType,
                          @NotNull String variableName,
                          @NotNull String variableClass,
                          boolean isOptional) {
    myArgumentType = variableType;
    myArgumentName = variableName;
    myIsOptional = isOptional;
    myVariableClass = variableClass;
  }

  public @NotNull PerlVariableType getArgumentType() {
    return myArgumentType;
  }

  public @NotNull String getArgumentName() {
    return myArgumentName;
  }

  public Boolean isOptional() {
    return myIsOptional;
  }

  public void setOptional(Boolean optional) {
    myIsOptional = optional;
  }

  public @NotNull String getVariableClass() {
    return myVariableClass;
  }

  public String toStringShort() {
    return StringUtil.isNotEmpty(myArgumentName) ? myArgumentType.getSigil() + myArgumentName : PerlUndefValue.STRING_UNDEF;
  }

  public String toStringLong() {
    if (StringUtil.isEmpty(myArgumentName)) {
      return PerlUndefValue.STRING_UNDEF;
    }

    String shortName = myArgumentType.getSigil() + myArgumentName;
    return StringUtil.isNotEmpty(myVariableClass) ? myVariableClass + " " + shortName : shortName;
  }

  @Override
  public String toString() {
    String toString = toStringLong();
    return myIsOptional ? "[" + toString + "]" : toString;
  }

  public boolean isSelf(Project project) {
    return getArgumentType() == PerlVariableType.SCALAR && PerlSharedSettings.getInstance(project).isSelfName(getArgumentName());
  }

  public boolean isEmpty() {
    return StringUtil.isEmpty(myArgumentName);
  }

  public PerlContextType getContextType() {
    if (myArgumentType == PerlVariableType.SCALAR || myArgumentType == PerlVariableType.CODE || myArgumentType == PerlVariableType.GLOB) {
      return PerlContextType.SCALAR;
    }
    return PerlContextType.LIST;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PerlSubArgument argument = (PerlSubArgument)o;

    return myArgumentType == argument.myArgumentType &&
           myArgumentName.equals(argument.myArgumentName) &&
           myVariableClass.equals(argument.myVariableClass) &&
           (Objects.equals(myIsOptional, argument.myIsOptional));
  }

  @Override
  public int hashCode() {
    int result = myArgumentType.hashCode();
    result = 31 * result + myArgumentName.hashCode();
    result = 31 * result + myVariableClass.hashCode();
    result = 31 * result + (myIsOptional != null ? myIsOptional.hashCode() : 0);
    return result;
  }

  public static PerlSubArgument optionalScalar(@NotNull String variableName) {
    return create(PerlVariableType.SCALAR, variableName, true);
  }

  public static PerlSubArgument optionalScalar(@NotNull String variableName, @Nullable String variableClass) {
    return create(PerlVariableType.SCALAR, variableName, variableClass == null ? "" : variableClass, true);
  }

  public static PerlSubArgument mandatoryScalar(@NotNull String variableName) {
    return mandatoryScalar(variableName, "");
  }

  public static @NotNull PerlSubArgument mandatoryScalar(@NotNull String variableName, @NotNull String variableClass) {
    return mandatory(PerlVariableType.SCALAR, variableName, variableClass);
  }

  public static PerlSubArgument mandatory(@NotNull PerlVariableType variableType, @NotNull String variableName) {
    return create(variableType, variableName, false);
  }

  public static PerlSubArgument mandatory(@NotNull PerlVariableType variableType,
                                          @NotNull String variableName,
                                          @NotNull String variableClass) {
    return create(variableType, variableName, variableClass, false);
  }

  public static PerlSubArgument create(@NotNull PerlVariableType variableType, @NotNull String variableName, boolean isOptional) {
    return create(variableType, variableName, "", isOptional);
  }

  public static PerlSubArgument create(@NotNull PerlVariableType variableType,
                                       @NotNull String variableName,
                                       @NotNull String variableClass,
                                       boolean isOptional) {
    return new PerlSubArgument(variableType, variableName, variableClass, isOptional);
  }

  public static PerlSubArgument empty() {
    return mandatoryScalar("");
  }

  public static PerlSubArgument self() {
    return mandatoryScalar(PerlScalarUtilCore.DEFAULT_SELF_NAME);
  }
}
