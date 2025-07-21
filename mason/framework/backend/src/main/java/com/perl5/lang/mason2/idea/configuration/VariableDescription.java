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

package com.perl5.lang.mason2.idea.configuration;


import java.util.Objects;

public class VariableDescription {
  public String variableName;
  public String variableType;

  public VariableDescription() {
  }

  public VariableDescription(String variableName, String variableType) {
    this.variableName = variableName;
    this.variableType = variableType;
  }

  public VariableDescription copy() {
    return new VariableDescription(variableName, variableType);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof VariableDescription that)) {
      return false;
    }

    if (!Objects.equals(variableName, that.variableName)) {
      return false;
    }
    return Objects.equals(variableType, that.variableType);
  }

  @Override
  public int hashCode() {
    int result = variableName != null ? variableName.hashCode() : 0;
    result = 31 * result + (variableType != null ? variableType.hashCode() : 0);
    return result;
  }
}
