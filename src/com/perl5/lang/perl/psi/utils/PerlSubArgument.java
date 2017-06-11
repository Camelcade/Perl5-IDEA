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

  PerlVariableType argumentType;
  String argumentName;
  String variableClass;
  Boolean isOptional;

  public PerlSubArgument(PerlVariableType variableType, String variableName, String variableClass, boolean isOptional) {
    this.argumentType = variableType;
    this.argumentName = variableName;
    this.isOptional = isOptional;
    this.variableClass = variableClass;
  }

  public PerlVariableType getArgumentType() {
    return argumentType;
  }

  public String getArgumentName() {
    return argumentName;
  }

  public Boolean isOptional() {
    return isOptional;
  }

  public void setOptional(Boolean optional) {
    isOptional = optional;
  }

  public String getVariableClass() {
    return variableClass;
  }

  public String toStringShort() {
    return StringUtil.isNotEmpty(argumentName) ? argumentType.getSigil() + argumentName : PerlLexer.STRING_UNDEF;
  }

  public String toStringLong() {
    if (StringUtil.isEmpty(argumentName)) {
      return PerlLexer.STRING_UNDEF;
    }

    String shortName = argumentType.getSigil() + argumentName;
    return StringUtil.isNotEmpty(variableClass) ? variableClass + " " + shortName : shortName;
  }

  private void serialize(@NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(argumentType.toString());
    dataStream.writeName(argumentName);
    dataStream.writeName(variableClass);
    dataStream.writeBoolean(isOptional);
  }

  public boolean isSelf(Project project) {
    return getArgumentType() == PerlVariableType.SCALAR && PerlSharedSettings.getInstance(project).isSelfName(getArgumentName());
  }

  public boolean isEmpty() {
    return StringUtil.isEmpty(argumentName);
  }

  public PerlContextType getContextType() {
    if (argumentType == PerlVariableType.SCALAR || argumentType == PerlVariableType.CODE || argumentType == PerlVariableType.GLOB) {
      return PerlContextType.SCALAR;
    }
    return PerlContextType.LIST;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PerlSubArgument)) return false;

    PerlSubArgument argument = (PerlSubArgument)o;

    if (getArgumentType() != argument.getArgumentType()) return false;
    if (getArgumentName() != null ? !getArgumentName().equals(argument.getArgumentName()) : argument.getArgumentName() != null) {
      return false;
    }
    if (getVariableClass() != null ? !getVariableClass().equals(argument.getVariableClass()) : argument.getVariableClass() != null) {
      return false;
    }
    return isOptional != null ? isOptional.equals(argument.isOptional) : argument.isOptional == null;
  }

  @Override
  public int hashCode() {
    int result = getArgumentType() != null ? getArgumentType().hashCode() : 0;
    result = 31 * result + (getArgumentName() != null ? getArgumentName().hashCode() : 0);
    result = 31 * result + (getVariableClass() != null ? getVariableClass().hashCode() : 0);
    result = 31 * result + (isOptional != null ? isOptional.hashCode() : 0);
    return result;
  }

  private static PerlSubArgument deserialize(@NotNull StubInputStream dataStream) throws IOException {
    PerlVariableType argumentType = PerlVariableType.valueOf(dataStream.readName().toString());
    String argumentName = dataStream.readName().toString();
    String variableClass = dataStream.readName().toString();
    boolean isOptional = dataStream.readBoolean();
    return new PerlSubArgument(argumentType, argumentName, variableClass, isOptional);
  }

  @NotNull
  public static List<PerlSubArgument> deserializeList(@NotNull StubInputStream dataStream) throws IOException {
    int argumentsNumber = dataStream.readInt();

    if (argumentsNumber > 0) {

      List<PerlSubArgument> arguments = new ArrayList<PerlSubArgument>(argumentsNumber);

      for (int i = 0; i < argumentsNumber; i++) {
        arguments.add(deserialize(dataStream));
      }
      return arguments;
    }
    else {
      return Collections.emptyList();
    }
  }

  public static PerlSubArgument getEmptyArgument() {
    return new PerlSubArgument(
      PerlVariableType.SCALAR,
      "",
      "",
      false
    );
  }

  public static PerlSubArgument getSelfArgument() {
    return new PerlSubArgument(
      PerlVariableType.SCALAR,
      "self",
      "",
      false
    );
  }

  public static void serializeList(@NotNull StubOutputStream dataStream, List<PerlSubArgument> arguments) throws IOException {
    dataStream.writeInt(arguments.size());
    for (PerlSubArgument argument : arguments) {
      argument.serialize(dataStream);
    }
  }
}
