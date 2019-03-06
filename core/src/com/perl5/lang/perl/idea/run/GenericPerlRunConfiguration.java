/*
 * Copyright 2015-2018 Alexandr Evstigneev
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
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.LocatableConfigurationBase;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.net.NetUtils;
import com.intellij.util.xmlb.XmlSerializer;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptions;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugProcess;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugProfileState;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.debugger.DebuggableRunConfiguration;
import org.jetbrains.jps.model.serialization.PathMacroUtil;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.CONSOLE;
import static com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.NONE;

/**
 * @author VISTALL
 * @since 16-Sep-15
 */
public abstract class GenericPerlRunConfiguration extends LocatableConfigurationBase implements
                                                                     CommonProgramRunConfigurationParameters,
                                                                     DebuggableRunConfiguration,
                                                                     PerlDebugOptions {
  private String myScriptPath;
  private String myScriptParameters;    // these are script parameters

  private String myPerlParameters = "";
  private String myWorkingDirectory;
  private Map<String, String> myEnvs = new HashMap<>();
  private boolean myPassParentEnvs = true;
  private String myConsoleCharset;
  private boolean myUseAlternativeSdk;
  private String myAlternativeSdkName;

  // debugging-related options
  private String myScriptCharset = "utf8";
  private String myStartMode = "RUN";
  private boolean myIsNonInteractiveModeEnabled = false;
  private boolean myIsCompileTimeBreakpointsEnabled = false;
  private String myInitCode = "";

  private transient Integer myDebugPort;

  public GenericPerlRunConfiguration(Project project, @NotNull ConfigurationFactory factory, String name) {
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

  @Nullable
  @Override
  public PerlRunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) {
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
    return StringUtils.isEmpty(myScriptPath) ? null : LocalFileSystem.getInstance().findFileByPath(myScriptPath);
  }

  public String getConsoleCharset() {
    return myConsoleCharset;
  }

  public void setConsoleCharset(String charset) {
    myConsoleCharset = charset;
  }

  public String getScriptPath() {
    return myScriptPath;
  }

  public void setScriptPath(String scriptPath) {
    myScriptPath = scriptPath;
  }

  public String getAlternativeSdkName() {
    return myAlternativeSdkName;
  }

  public void setAlternativeSdkName(String name) {
    myAlternativeSdkName = name;
  }

  public boolean isUseAlternativeSdk() {
    return myUseAlternativeSdk;
  }

  public void setUseAlternativeSdk(boolean value) {
    myUseAlternativeSdk = value;
  }

  @Nullable
  @Override
  public String getProgramParameters() {
    return myScriptParameters;
  }

  @Override
  public void setProgramParameters(@Nullable String s) {
    myScriptParameters = s;
  }

  @Nullable
  @Override
  public String getWorkingDirectory() {
    return myWorkingDirectory;
  }

  @Override
  public void setWorkingDirectory(@Nullable String s) {
    myWorkingDirectory = s;
  }

  @NotNull
  @Override
  public Map<String, String> getEnvs() {
    return myEnvs;
  }

  @Override
  public void setEnvs(@NotNull Map<String, String> map) {
    myEnvs = map;
  }

  @Override
  public boolean isPassParentEnvs() {
    return myPassParentEnvs;
  }

  @Override
  public void setPassParentEnvs(boolean b) {
    myPassParentEnvs = b;
  }

  public String getPerlParameters() {
    return myPerlParameters;
  }

  @NotNull
  protected List<String> getPerlParametersList() {
    String perlParameters = getPerlParameters();
    return StringUtil.isEmpty(perlParameters) ? Collections.emptyList() : StringUtil.split(perlParameters, " ");
  }

  public void setPerlParameters(String PERL_PARAMETERS) {
    this.myPerlParameters = PERL_PARAMETERS;
  }

  @Override
  public String getStartMode() {
    return myStartMode;
  }

  public void setStartMode(String startMode) {
    this.myStartMode = startMode;
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
    if (myDebugPort == null) {
      myDebugPort = NetUtils.tryToFindAvailableSocketPort();
      if (myDebugPort == -1) {
        throw new ExecutionException("No free port to work on");
      }
    }

    return myDebugPort;
  }

  @Override
  public String getRemoteProjectRoot() {
    return getProject().getBasePath();
  }

  @Override
  public String getScriptCharset() {
    return myScriptCharset;
  }

  public void setScriptCharset(String scriptCharset) {
    this.myScriptCharset = scriptCharset;
  }

  @Override
  public boolean isNonInteractiveModeEnabled() {
    return myIsNonInteractiveModeEnabled;
  }

  public void setNonInteractiveModeEnabled(boolean nonInteractiveModeEnabled) {
    myIsNonInteractiveModeEnabled = nonInteractiveModeEnabled;
  }

  @Override
  public boolean isCompileTimeBreakpointsEnabled() {
    return myIsCompileTimeBreakpointsEnabled;
  }

  public void setCompileTimeBreakpointsEnabled(boolean compileTimeBreakpointsEnabled) {
    myIsCompileTimeBreakpointsEnabled = compileTimeBreakpointsEnabled;
  }


  @NotNull
  public PerlCommandLine createCommandLine(@NotNull Project project,
                                           @NotNull List<String> additionalPerlParameters,
                                           @NotNull Map<String, String> additionalEnvironmentVariables) throws ExecutionException {
    VirtualFile scriptFile = getScriptFile();
    if (scriptFile == null) {
      throw new ExecutionException(PerlBundle.message("perl.run.error.script.missing", getScriptPath()));
    }


    Sdk perlSdk = getEffectiveSdk();
    PerlCommandLine commandLine = PerlRunUtil.getPerlCommandLine(
      project, perlSdk, scriptFile, ContainerUtil.concat(getPerlParametersList(), additionalPerlParameters), getScriptParameters());

    if (commandLine == null) {
      throw new ExecutionException(PerlBundle.message("perl.run.error.sdk.corrupted", perlSdk));
    }

    String workDirectory = getWorkingDirectory();
    if (StringUtil.isEmpty(workDirectory)) {
      Module moduleForFile = ModuleUtilCore.findModuleForFile(scriptFile, project);
      if (moduleForFile != null) {
        workDirectory = PathMacroUtil.getModuleDir(moduleForFile.getModuleFilePath());
      }
      else {
        workDirectory = project.getBasePath();
      }
    }
    commandLine.withWorkDirectory(workDirectory);

    String charsetName = getConsoleCharset();
    Charset charset;
    if (!StringUtil.isEmpty(charsetName)) {
      try {
        charset = Charset.forName(charsetName);
      }
      catch (UnsupportedCharsetException e) {
        throw new ExecutionException(PerlBundle.message("perl.run.error.unknown.charset", charsetName));
      }
    }
    else {
      charset = scriptFile.getCharset();
    }
    commandLine.withCharset(charset);

    Map<String, String> environment = new HashMap<>(getEnvs());
    environment.putAll(additionalEnvironmentVariables);
    commandLine.withEnvironment(environment);
    commandLine.withParentEnvironmentType(isPassParentEnvs() ? CONSOLE : NONE);

    return commandLine.withSdk(perlSdk).withProject(project);
  }

  @NotNull
  protected List<String> getScriptParameters() {
    String programParameters = getProgramParameters();
    return StringUtil.isEmpty(programParameters) ? Collections.emptyList() : StringUtil.split(programParameters, " ");
  }

  @Override
  public String getInitCode() {
    return myInitCode;
  }

  public void setInitCode(String initCode) {
    this.myInitCode = initCode;
  }

  @NotNull
  @Override
  public XDebugProcess createDebugProcess(@NotNull InetSocketAddress socketAddress,
                                          @NotNull XDebugSession session,
                                          @Nullable ExecutionResult executionResult,
                                          @NotNull ExecutionEnvironment environment) throws ExecutionException {
    PerlRunProfileState runProfileState = getState(environment.getExecutor(), environment);
    if (!(runProfileState instanceof PerlDebugProfileState)) {
      throw new ExecutionException("Incorrect profile state");
    }
    return new PerlDebugProcess(session, (PerlDebugProfileState)runProfileState, runProfileState.execute(environment.getExecutor()));
  }
}
