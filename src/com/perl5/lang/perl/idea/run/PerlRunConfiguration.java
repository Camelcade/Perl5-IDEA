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

package com.perl5.lang.perl.idea.run;

import com.intellij.execution.CommonProgramRunConfigurationParameters;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.LocatableConfigurationBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.net.NetUtils;
import com.intellij.util.xmlb.XmlSerializer;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptions;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugProfileState;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author VISTALL
 * @since 16-Sep-15
 */
public class PerlRunConfiguration extends LocatableConfigurationBase implements CommonProgramRunConfigurationParameters, PerlDebugOptions {
  public String SCRIPT_PATH;
  public String PROGRAM_PARAMETERS;    // these are script parameters

  public String PERL_PARAMETERS = "";
  public String WORKING_DIRECTORY;
  public Map<String, String> ENVS = new HashMap<>();
  public boolean PASS_PARENT_ENVS = true;
  public String CHARSET;
  public boolean USE_ALTERNATIVE_SDK;
  public String ALTERNATIVE_SDK_NAME;

  // debugging-related options
  public String scriptCharset = "utf8";
  public String startMode = "RUN";
  public boolean isNonInteractiveModeEnabled = false;
  public boolean isCompileTimeBreakpointsEnabled = false;
  public String initCode = "";

  private transient Integer debugPort;

  public PerlRunConfiguration(Project project, @NotNull ConfigurationFactory factory, String name) {
    super(project, factory, name);
  }

  @Override
  public void readExternal(@NotNull Element element) throws InvalidDataException {
    super.readExternal(element);
    XmlSerializer.deserializeInto(this, element);
  }

  @Override
  public void writeExternal(@NotNull Element element) throws WriteExternalException {
    super.writeExternal(element);
    XmlSerializer.serializeInto(this, element);
  }

  @NotNull
  @Override
  public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    return new PerlConfigurationEditor(getProject());
  }

  @Nullable
  @Override
  public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) {
    if (executor instanceof DefaultDebugExecutor) {
      return new PerlDebugProfileState(executionEnvironment);
    }
    return new PerlRunProfileState(executionEnvironment);
  }

  @NotNull
  public Sdk getEffectiveSdk() throws ExecutionException {
    Sdk perlSdk;
    if (isUseAlternativeSdk()) {
      String alternativeSdkName = getAlternativeSdkName();
      perlSdk = PerlSdkTable.getInstance().findJdk(alternativeSdkName);
      if (perlSdk == null) {
        throw new ExecutionException(PerlBundle.message("perl.run.error.no.alternative.sdk", alternativeSdkName));
      }
      return perlSdk;
    }
    else {
      perlSdk = PerlProjectManager.getSdk(getProject());
      if (perlSdk == null) {
        throw new ExecutionException(PerlBundle.message("perl.run.error.no.sdk", getProject()));
      }
    }
    return perlSdk;
  }

  @Override
  public String suggestedName() {
    VirtualFile scriptFile = getScriptFile();
    return scriptFile == null ? null : scriptFile.getName();
  }

  @Nullable
  public VirtualFile getScriptFile() {
    return StringUtils.isEmpty(SCRIPT_PATH) ? null : LocalFileSystem.getInstance().findFileByPath(SCRIPT_PATH);
  }

  public String getConsoleCharset() {
    return CHARSET;
  }

  public void setConsoleCharset(String charset) {
    CHARSET = charset;
  }

  public String getScriptPath() {
    return SCRIPT_PATH;
  }

  public void setScriptPath(String scriptPath) {
    SCRIPT_PATH = scriptPath;
  }

  public String getAlternativeSdkName() {
    return ALTERNATIVE_SDK_NAME;
  }

  public void setAlternativeSdkName(String path) {
    this.ALTERNATIVE_SDK_NAME = path;
  }

  public boolean isUseAlternativeSdk() {
    return USE_ALTERNATIVE_SDK;
  }

  public void setUseAlternativeSdk(boolean value) {
    this.USE_ALTERNATIVE_SDK = value;
  }

  @Nullable
  @Override
  public String getProgramParameters() {
    return PROGRAM_PARAMETERS;
  }

  @Override
  public void setProgramParameters(@Nullable String s) {
    PROGRAM_PARAMETERS = s;
  }

  @Nullable
  @Override
  public String getWorkingDirectory() {
    return WORKING_DIRECTORY;
  }

  @Override
  public void setWorkingDirectory(@Nullable String s) {
    WORKING_DIRECTORY = s;
  }

  @NotNull
  @Override
  public Map<String, String> getEnvs() {
    return ENVS;
  }

  @Override
  public void setEnvs(@NotNull Map<String, String> map) {
    ENVS = map;
  }

  @Override
  public boolean isPassParentEnvs() {
    return PASS_PARENT_ENVS;
  }

  @Override
  public void setPassParentEnvs(boolean b) {
    PASS_PARENT_ENVS = b;
  }

  public String getPerlParameters() {
    return PERL_PARAMETERS;
  }

  public void setPerlParameters(String PERL_PARAMETERS) {
    this.PERL_PARAMETERS = PERL_PARAMETERS;
  }

  @Override
  public String getStartMode() {
    return startMode;
  }

  public void setStartMode(String startMode) {
    this.startMode = startMode;
  }

  @Override
  public String getPerlRole() {
    return PerlDebugOptions.ROLE_SERVER;
  }

  @Override
  public String getDebugHost() {
    return "0.0.0.0";
  }

  @Override
  public int getDebugPort() throws ExecutionException {
    if (debugPort == null) {
      debugPort = NetUtils.tryToFindAvailableSocketPort();
      if (debugPort == -1) {
        throw new ExecutionException("No free port to work on");
      }
    }

    return debugPort;
  }

  @Override
  public String getRemoteProjectRoot() {
    return getProject().getBasePath();
  }

  @Override
  public String getScriptCharset() {
    return scriptCharset;
  }

  public void setScriptCharset(String scriptCharset) {
    this.scriptCharset = scriptCharset;
  }

  @Override
  public boolean isNonInteractiveModeEnabled() {
    return isNonInteractiveModeEnabled;
  }

  public void setNonInteractiveModeEnabled(boolean nonInteractiveModeEnabled) {
    isNonInteractiveModeEnabled = nonInteractiveModeEnabled;
  }

  @Override
  public boolean isCompileTimeBreakpointsEnabled() {
    return isCompileTimeBreakpointsEnabled;
  }

  public void setCompileTimeBreakpointsEnabled(boolean compileTimeBreakpointsEnabled) {
    isCompileTimeBreakpointsEnabled = compileTimeBreakpointsEnabled;
  }

  @Override
  public String getInitCode() {
    return initCode;
  }

  public void setInitCode(String initCode) {
    this.initCode = initCode;
  }
}
