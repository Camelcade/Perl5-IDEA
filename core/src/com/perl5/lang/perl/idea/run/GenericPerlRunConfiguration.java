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
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Function;
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
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.debugger.DebuggableRunConfiguration;
import org.jetbrains.jps.model.serialization.PathMacroUtil;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.*;

import static com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.CONSOLE;
import static com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.NONE;

public abstract class GenericPerlRunConfiguration extends LocatableConfigurationBase implements
                                                                     CommonProgramRunConfigurationParameters,
                                                                     DebuggableRunConfiguration,
                                                                     PerlDebugOptions {
  public static final Function<String, List<String>> FILES_PARSER = text -> StringUtil.split(text.trim(), "||");
  public static final Function<List<String>, String> FILES_JOINER = strings ->
    StringUtil.join(ContainerUtil.filter(strings, StringUtil::isNotEmpty), "||");

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
    List<VirtualFile> targetFiles = computeTargetFiles();
    return targetFiles.isEmpty() ? null : targetFiles.get(0).getName();
  }

  @NotNull
  protected List<VirtualFile> computeTargetFiles() {
    return computeVirtualFilesFromPaths(getScriptPath());
  }

  @NotNull
  private VirtualFile computeNonNullScriptFile() throws ExecutionException {
    List<VirtualFile> targetFiles = computeTargetFiles();
    if (targetFiles.isEmpty()) {
      throw new ExecutionException(PerlBundle.message("perl.run.error.script.missing", getScriptPath()));
    }
    return targetFiles.get(0);
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

  /**
   * @return virtual file for a working directory explicitly set by user
   */
  @Nullable
  protected VirtualFile computeExplicitWorkingDirectory() {
    String workingDirectory = getWorkingDirectory();
    if (StringUtil.isEmpty(workingDirectory)) {
      return null;
    }
    return VfsUtil.findFileByIoFile(new File(workingDirectory), true);
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
    PerlCommandLine commandLine = createBaseCommandLine(project, additionalPerlParameters, additionalEnvironmentVariables);
    commandLine.withParentEnvironmentType(isPassParentEnvs() ? CONSOLE : NONE);
    commandLine.withWorkDirectory(computeWorkingDirectory(project));
    commandLine.withCharset(computeCharset());

    return commandLine;
  }

  @NotNull
  protected Charset computeCharset() throws ExecutionException {
    String charsetName = getConsoleCharset();
    if (!StringUtil.isEmpty(charsetName)) {
      try {
        return Charset.forName(charsetName);
      }
      catch (UnsupportedCharsetException e) {
        throw new ExecutionException(PerlBundle.message("perl.run.error.unknown.charset", charsetName));
      }
    }
    else {
      return computeNonNullScriptFile().getCharset();
    }
  }

  @Nullable
  protected String computeWorkingDirectory(@NotNull Project project) throws ExecutionException {
    String workDirectory = getWorkingDirectory();
    if (StringUtil.isNotEmpty(workDirectory)) {
      return workDirectory;
    }
    return computeWorkingDirectory(project, computeNonNullScriptFile());
  }

  @NotNull
  protected PerlCommandLine createBaseCommandLine(@NotNull Project project,
                                                  @NotNull List<String> additionalPerlParameters,
                                                  @NotNull Map<String, String> additionalEnvironmentVariables) throws ExecutionException {
    PerlCommandLine commandLine = PerlRunUtil.getPerlCommandLine(
      project, getEffectiveSdk(), computeNonNullScriptFile(), ContainerUtil.concat(getPerlParametersList(), additionalPerlParameters),
      getScriptParameters());

    if (commandLine == null) {
      throw new ExecutionException(PerlBundle.message("perl.run.error.sdk.corrupted", getEffectiveSdk()));
    }

    Map<String, String> environment = new HashMap<>(getEnvs());
    environment.putAll(additionalEnvironmentVariables);
    commandLine.withEnvironment(environment);
    return commandLine;
  }

  @Nullable
  protected static String computeWorkingDirectory(@NotNull Project project, @NotNull VirtualFile virtualFile) {
    Module moduleForFile = ModuleUtilCore.findModuleForFile(virtualFile, project);
    if (moduleForFile != null) {
      return PathMacroUtil.getModuleDir(moduleForFile.getModuleFilePath());
    }
    return project.getBasePath();
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

  /**
   * @return list of VirtualFiles pointed by  {@code paths} joined with pipe. Reverse of {@link #computePathsFromVirtualFiles(List)}
   */
  @NotNull
  public static List<VirtualFile> computeVirtualFilesFromPaths(@NotNull String paths) {
    List<String> pathNames = FILES_PARSER.fun(paths);
    if (pathNames.isEmpty()) {
      return Collections.emptyList();
    }
    List<VirtualFile> virtualFiles = new ArrayList<>(pathNames.size());
    for (String pathName : pathNames) {
      if (StringUtil.isEmpty(pathName)) {
        continue;
      }
      ContainerUtil.addIfNotNull(virtualFiles, VfsUtil.findFileByIoFile(new File(pathName), true));
    }
    return virtualFiles;
  }

  /**
   * @return paths of {@code virtualFiles} joined with pipe, reverse of {@link #computeVirtualFilesFromPaths(String)}
   */
  @NotNull
  public static String computePathsFromVirtualFiles(@NotNull List<VirtualFile> virtualFiles) {
    return FILES_JOINER.fun(ContainerUtil.map(virtualFiles, VirtualFile::getPath));
  }
}
