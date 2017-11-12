/*
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

package com.perl5.lang.perl.idea.refactoring;

import com.intellij.refactoring.util.AbstractVariableData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PerlRefactor {
  private final String subName;
  @SuppressWarnings("unused") private String newMethodCall;
  @SuppressWarnings("unused") private String newMethod;

  private String codeSelection;
  private String className = "";

  private final Set<String> hashVariables = new HashSet<>();
  private final Set<String> arrayVariables = new HashSet<>();
  private final Set<String> scalarVariables = new HashSet<>();
  private final Set<String> loopScalarVariables = new HashSet<>();
  private final Set<String> localScalarVariables = new HashSet<>();
  private final Set<String> localArrayVariables = new HashSet<>();
  private final Set<String> localHashVariables = new HashSet<>();


  private final ArrayList<String> parameters = new ArrayList<>();
  private final ArrayList<String> innerVariables = new ArrayList<>();
  private final ArrayList<String> outerVariables = new ArrayList<>();
  private String methodParameters;
  private String methodReturn;

  /**
   * @return The code selection passed in and modified from refactoring
   */
  public String getCodeSelection() {
    return codeSelection;
  }

  /**
   * @return An array of parameters to be passed in to a method
   */
  public ArrayList<String> getParameters() {
    return parameters;
  }

  /**
   * @return An array of parameters to be returned from a method
   */
  public ArrayList<String> getInnerVariables() {
    return innerVariables;
  }

  /**
   * @return returns an array of parameters with arrays and hashes as scalars.
   */
  public ArrayList<String> getOuterVariablesModified() {
    String reg = "[@%](.*)";
    Pattern regex = Pattern.compile(reg);
    ArrayList<String> ovs = new ArrayList<>();

    for (String s : this.outerVariables) {
      Matcher matcher = regex.matcher(s);
      if (matcher.find()) {
        s = s.replaceAll(reg, "\\$" + matcher.group(1));
      }
      ovs.add(s);
    }

    return ovs;
  }

  public PerlRefactor(String codeSelection, String subName) {
    this.codeSelection = codeSelection;
    this.subName = subName;

    parseVariables();
    parseLocalVariables();
    buildMethod();
  }

  /**
   * @param methodName       Name of method to generate
   * @param methodParameters parameters to pass into method
   * @param methodBody       body of the generated method
   * @param methodReturn     return string ie "return ($foo);"
   * @return returns a built method
   */
  public String buildMethod(@NotNull String methodName,
                            @Nullable AbstractVariableData[] methodParameters,
                            @NotNull String methodBody,
                            @Nullable String methodReturn) {
    StringBuilder builder = new StringBuilder("sub ");
    builder.append(methodName).append(" {\n");

    if (methodParameters != null) {
      builder.append(this.buildMethodParameters(methodParameters)).append("\n\n");
    }

    builder.append(methodBody).append("\n");

    builder.append(methodReturn).append("\n");
    builder.append("}\n");

    return builder.toString();
  }

  // TODO: fix formatting ?
  private void buildMethod() {
    extractMethodVariables();

    this.newMethod= "sub " + this.subName + " {\n" +
                     buildMethodParameters(this.parameters) + "\n" +
                     this.codeSelection +
                     buildMethodReturn() + "\n" +
                     "}\n";

    this.newMethodCall = buildMethodCall();
  }

  @NotNull
  private String buildMethodCall() {
    StringBuilder builder = new StringBuilder();

    if (builder.length() > 0) {
      builder.insert(0, "my (");
      builder.append(") = ");
    }

    if (!this.className.isEmpty()) {
      builder.append(this.className).append("->");
    }
    builder.append(this.subName).append("(");

    String pattern = "([%@])(.*)";
    Pattern regex = Pattern.compile(pattern);
    boolean first = true;
    for (String s : this.parameters) {
      if (first) {
        first = false;
      }
      else {
        builder.append(", ");
      }
      Matcher matcher = regex.matcher(s);
      if (matcher.find()) {
        s = s.replaceAll(pattern, "\\\\" + matcher.group(1) + matcher.group(2));
      }
      builder.append(s);
    }
    builder.append(");\n");

    return builder.toString();
  }

  private void extractMethodVariables() {
    String reg1;
    String reg2;
    String arrayRef;

    for (String param : this.scalarVariables) {
      if (!this.localScalarVariables.contains(param)) {
        this.parameters.add(param);
      }
      else {
        if (this.loopScalarVariables.contains(param)) {
          continue;
        }

        if (!param.matches("\\$\\d+$")) {
          this.innerVariables.add(param);
          this.outerVariables.add(param);
        }
      }
    }

    for (String param : this.arrayVariables) {
      param = param.replace("$", "@");

      if (!this.localArrayVariables.contains(param)) {
        this.parameters.add(param);
        reg2 = String.format("\\%s", param);
        arrayRef = param;
        arrayRef = arrayRef.replace("@", "$");
        this.codeSelection = this.codeSelection.replaceAll(reg2, "@" + arrayRef);
        param = param.replace("@", "$");
        reg1 = String.format("\\%s\\[", param);
        this.codeSelection = this.codeSelection.replaceAll(reg1, param + "->[");
      }
      else {
        this.innerVariables.add("\\" + param);
        this.outerVariables.add(param);
      }
    }

    for (String param : this.hashVariables) {
      param = param.replace("$", "%");

      if (!this.localHashVariables.contains(param)) {
        this.parameters.add(param);
        reg1 = String.format("\\%s", param);
        String hashRef = param.replace("%", "$");
        this.codeSelection = this.codeSelection.replaceAll(reg1, String.format("%%\\%s", hashRef));

        param = param.replace("%", "$");
        reg2 = String.format("\\%s\\{", param);
        this.codeSelection = this.codeSelection.replaceAll(reg2, "\\" + param + "->{");
      }
      else {
        this.innerVariables.add("\\" + param);
        this.outerVariables.add(param);
      }
    }
  }


  /**
   * @return method return string
   */
  public String buildMethodReturn() {
    StringBuilder builder = new StringBuilder();
    if (this.innerVariables.size() > 0) {
      builder.append("\n    return (");
      boolean first = true;
      for (String innerVariable : this.innerVariables) {
        if (first) {
          first = false;
        }
        else {
          builder.append(", ");
        }
        builder.append(innerVariable);
      }
      builder.append(");");
    }
    else {
      builder.append("\n    return;");
    }
    this.methodReturn = builder.toString();
    return this.methodReturn;
  }

  /**
   * @param variableData
   * @return built string of method parameters
   */
  public String buildMethodParameters(@NotNull AbstractVariableData[] variableData) {
    StringBuilder builder = new StringBuilder();

    if (!this.className.isEmpty()) {
      builder.append("    my ").append(this.className).append(" = shift;\n");
    }

    builder.append("    my (");
    boolean first = true;
    for (AbstractVariableData vd : variableData) {
      if (!vd.passAsParameter) {
        continue;
      }

      if (first) {
        first = false;
      }
      else {
        builder.append(", ");
      }
      builder.append(vd.name.replaceAll("[%@]", "\\$"));
    }
    builder.append(") = @_;");
    return builder.toString();
  }

  /**
   * @param newParameters
   * @return built string of method parameters
   */
  public String buildMethodParameters(@Nullable ArrayList<String> newParameters) {
    StringBuilder builder = new StringBuilder();
    if (!this.className.isEmpty()) {
      builder.append("    my ").append(this.className).append(" = shift;\n");
    }
    ArrayList<String> parameters;

    if (newParameters != null) {
      parameters = newParameters;
    }
    else {
      parameters = this.parameters;
    }
    builder.append("    my (");
    boolean first = true;
    for (String s : parameters) {
      if (first) {
        first = false;
      }
      else {
        builder.append(", ");
      }
      builder.append(s.replaceAll("[%@]", "\\$"));
    }
    builder.append(") = @_;");

    this.methodParameters = builder.toString();
    return this.methodParameters;
  }

  private void parseLocalVariables() {

    for (String var : this.scalarVariables) {
      String reg1 = String.format("\\s*my\\s*\\%s\\s*[=;(]", var);
      String reg2 = String.format("\\s*my\\s*\\(.*?\\%s.*?\\)", var);
      String reg3 = String.format("(?:for|foreach)\\s+my\\s*\\%s\\s*\\(", var);

      Pattern pattern1 = Pattern.compile(reg1);
      Pattern pattern2 = Pattern.compile(reg2);
      Pattern pattern3 = Pattern.compile(reg3);

      if (var.matches("(?:\\$\\d+$|\\$[ab]$)")) {
        this.localScalarVariables.add(var);
      }
      else if (pattern1.matcher(codeSelection).find() || pattern2.matcher(codeSelection).find()) {
        this.localScalarVariables.add(var);

        // Skip loop variables
        if (pattern3.matcher(codeSelection).find()) {
          this.loopScalarVariables.add(var);
        }
      }
    }

    for (String var : this.arrayVariables) {
      var = var.replace("$", "@");

      String reg1 = String.format("\\s*my\\s*\\%s\\s*[=;(]", var);
      String reg2 = String.format("\\s*my\\s*\\(.*?\\%s.*?\\)", var);
      Pattern pattern1 = Pattern.compile(reg1);
      Pattern pattern2 = Pattern.compile(reg2);

      if (pattern1.matcher(codeSelection).find() || pattern2.matcher(codeSelection).find()) {
        localArrayVariables.add(var);
      }
    }

    for (String var : this.hashVariables) {
      var = var.replace("$", "%");
      String reg1 = String.format("\\s*my\\s*\\%s\\s*[=;(]", var);
      String reg2 = String.format("\\s*my\\s*\\(.*?\\%s.*?\\)", var);
      Pattern pattern1 = Pattern.compile(reg1);
      Pattern pattern2 = Pattern.compile(reg2);

      if (pattern1.matcher(codeSelection).find() || pattern2.matcher(codeSelection).find()) {
        this.localHashVariables.add(var);
      }
    }
  }

  private void parseVariables() {
    Pattern regex = Pattern.compile("([$@]\\w+?)(\\W)");
    Pattern classNameRegex = Pattern.compile("^(\\$self|\\$this|\\$class)$");

    String[] codeArray = this.codeSelection.split("\n");
    for (String code : codeArray) {
      // Strip out comments
      code = code.replaceAll("#.*", "");

      Matcher matcher = regex.matcher(code);
      while (matcher.find()) {
        String variable = matcher.group(1);
        String hint = matcher.group(2);

        // TODO: This might need to be .* or pattern/matcher
        // Skip $_
        if (variable.matches("^\\$_$")) {
          continue;
        }

        // Save class naming
        Matcher classMatcher = classNameRegex.matcher(variable);
        if (classMatcher.find()) {
          this.className = classMatcher.group(1);
          continue;
        }

        if (hint.matches("^\\{")) {
          variable = variable.replace("$", "%");
          this.hashVariables.add(variable);
        }
        else if (hint.matches("^\\[")) {
          variable = variable.replace("$", "@");
          this.arrayVariables.add(variable);
        }
        else if (hint.matches("^@")) {
          this.arrayVariables.add(variable);
        }
        else if (hint.matches("^%")) {
          this.hashVariables.add(variable);
        }
        else {
          this.scalarVariables.add(variable);
        }
      }
    }
  }
}