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

package com.perl5.lang.perl.psi.utils;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.lexer.PerlLexer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 01.06.2015.
 */
public class PerlSubArgument {

  @NotNull
  private PerlVariableType myArgumentType;
  @NotNull
  private String myArgumentName;

  @NotNull
  private String myVariableClass;

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

  @NotNull
  public PerlVariableType getArgumentType() {
    return myArgumentType;
  }

  @NotNull
  public String getArgumentName() {
    return myArgumentName;
  }

  public Boolean isOptional() {
    return myIsOptional;
  }

  public void setOptional(Boolean optional) {
    myIsOptional = optional;
  }

  @NotNull
  public String getVariableClass() {
    return myVariableClass;
  }

  public String toStringShort() {
    return StringUtil.isNotEmpty(myArgumentName) ? myArgumentType.getSigil() + myArgumentName : PerlLexer.STRING_UNDEF;
  }

  public String toStringLong() {
    if (StringUtil.isEmpty(myArgumentName)) {
      return PerlLexer.STRING_UNDEF;
    }

    String shortName = myArgumentType.getSigil() + myArgumentName;
    return StringUtil.isNotEmpty(myVariableClass) ? myVariableClass + " " + shortName : shortName;
  }

  private void serialize(@NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(myArgumentType.toString());
    dataStream.writeName(myArgumentName);
    dataStream.writeName(myVariableClass);
    dataStream.writeBoolean(myIsOptional);
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
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PerlSubArgument argument = (PerlSubArgument)o;

    return myArgumentType == argument.myArgumentType &&
           myArgumentName.equals(argument.myArgumentName) &&
           myVariableClass.equals(argument.myVariableClass) &&
           (myIsOptional != null ? myIsOptional.equals(argument.myIsOptional) : argument.myIsOptional == null);
  }

  @Override
  public int hashCode() {
    int result = myArgumentType.hashCode();
    result = 31 * result + myArgumentName.hashCode();
    result = 31 * result + myVariableClass.hashCode();
    result = 31 * result + (myIsOptional != null ? myIsOptional.hashCode() : 0);
    return result;
  }

  public static PerlSubArgument optional(@NotNull PerlVariableType variableType, @NotNull String variableName) {
    return create(variableType, variableName, true);
  }

  public static PerlSubArgument mandatoryScalar(@NotNull String variableName) {
    return mandatory(PerlVariableType.SCALAR, variableName);
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

  @SuppressWarnings("ConstantConditions")
  private static PerlSubArgument deserialize(@NotNull StubInputStream dataStream) throws IOException {
    PerlVariableType argumentType = PerlVariableType.valueOf(dataStream.readName().toString());
    String argumentName = dataStream.readName().toString();
    String variableClass = dataStream.readName().toString();
    boolean isOptional = dataStream.readBoolean();
    return PerlSubArgument.create(argumentType, argumentName, variableClass, isOptional);
  }

  @NotNull
  public static List<PerlSubArgument> deserializeList(@NotNull StubInputStream dataStream) throws IOException {
    int argumentsNumber = dataStream.readInt();

    if (argumentsNumber > 0) {

      List<PerlSubArgument> arguments = new ArrayList<>(argumentsNumber);

      for (int i = 0; i < argumentsNumber; i++) {
        arguments.add(deserialize(dataStream));
      }
      return arguments;
    }
    else {
      return Collections.emptyList();
    }
  }

  public static PerlSubArgument empty() {
    return mandatoryScalar("");
  }

  public static PerlSubArgument self() {
    return mandatoryScalar("self");
  }

  public static void serializeList(@NotNull StubOutputStream dataStream, List<PerlSubArgument> arguments) throws IOException {
    dataStream.writeInt(arguments.size());
    for (PerlSubArgument argument : arguments) {
      argument.serialize(dataStream);
    }
  }
}
