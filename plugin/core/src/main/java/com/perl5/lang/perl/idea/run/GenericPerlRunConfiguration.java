/*
 * Copyright 2015-2023 Alexandr Evstigneev
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
import com.intellij.execution.configurations.*;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.RunConfigurationWithSuppressedDefaultRunAction;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
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
import com.intellij.util.execution.ParametersListUtil;
import com.intellij.util.net.NetUtils;
import com.intellij.util.xmlb.XmlSerializer;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.execution.PerlTerminalExecutionConsole;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptions;
import com.perl5.lang.perl.idea.sdk.host.PerlConsoleView;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.util.PerlFileUtil;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jdom.Element;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.*;

import static com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.CONSOLE;
import static com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.NONE;

public abstract class GenericPerlRunConfiguration extends LocatableConfigurationBase<LocatableRunConfigurationOptions>
  implements CommonProgramRunConfigurationParameters,
             RunConfigurationWithSuppressedDefaultRunAction,
             PerlDebugOptions {
  public static final Function<String, List<String>> FILES_PARSER = text -> StringUtil.split(text.trim(), "||");
  public static final Function<List<String>, String> FILES_JOINER = strings ->
    StringUtil.join(ContainerUtil.filter(strings, StringUtil::isNotEmpty), "||");
  private static final Logger LOG = Logger.getInstance(GenericPerlRunConfiguration.class);

  private String myScriptPath;
  private String myScriptParameters;    // these are script parameters

  private String myPerlParameters = "";
  private String myWorkingDirectory;
  private Map<String, String> myEnvironments = new HashMap<>();
  private boolean myPassParentEnvironments = true;
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

  @Override
  public final @Nullable PerlRunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment)
    throws ExecutionException {
    var runner = executionEnvironment.getRunner();
    if (!(runner instanceof GenericPerlProgramRunner)) {
      LOG.error("GenericPerlProgramRunner expected, got " + runner);
      throw new ExecutionException("Wrong runner used to run perl configuration, please report to Perl plugin developers");
    }
    return ((GenericPerlProgramRunner)runner).createState(executionEnvironment);
  }

  /**
   * @return local path to the perl interpreter for this run configuration
   * @throws ExecutionException with human readable error message if interpreter path is empty
   */
  protected @NotNull String getEffectiveInterpreterPath() throws ExecutionException {
    Sdk effectiveSdk = getEffectiveSdk();
    String interpreterPath = PerlProjectManager.getInterpreterPath(effectiveSdk);
    if (StringUtil.isEmpty(interpreterPath)) {
      throw new ExecutionException(PerlBundle.message("perl.run.error.no.interpreter.path", effectiveSdk));
    }
    return interpreterPath;
  }

  public @NotNull Sdk getEffectiveSdk() throws ExecutionException {
    Sdk perlSdk;
    if (isUseAlternativeSdk()) {
      String alternativeSdkName = getAlternativeSdkName();
      if (StringUtil.isEmpty(alternativeSdkName)) {
        throw new ExecutionException(PerlBundle.message("perl.run.error.no.alternative.sdk.selected"));
      }
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

  protected @NotNull List<VirtualFile> computeTargetFiles() {
    return computeVirtualFilesFromPaths(getScriptPath());
  }

  private @NotNull VirtualFile computeNonNullScriptFile() throws ExecutionException {
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

  @Override
  public @Nullable String getProgramParameters() {
    return myScriptParameters;
  }

  @Override
  public void setProgramParameters(@Nullable String s) {
    myScriptParameters = s;
  }

  @Override
  public @Nullable String getWorkingDirectory() {
    return myWorkingDirectory;
  }

  /**
   * @return virtual file for a working directory explicitly set by user
   */
  protected @Nullable VirtualFile computeExplicitWorkingDirectory() {
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

  @Override
  public @NotNull Map<String, String> getEnvs() {
    return myEnvironments;
  }

  @Override
  public void setEnvs(@NotNull Map<String, String> map) {
    myEnvironments = map;
  }

  @Override
  public boolean isPassParentEnvs() {
    return myPassParentEnvironments;
  }

  @Override
  public void setPassParentEnvs(boolean b) {
    myPassParentEnvironments = b;
  }

  public String getPerlParameters() {
    return myPerlParameters;
  }

  protected @NotNull List<String> getPerlParametersList() {
    String perlParameters = getPerlParameters();
    return StringUtil.isEmpty(perlParameters) ? Collections.emptyList() : ParametersListUtil.parse(perlParameters);
  }

  public void setPerlParameters(String PERL_PARAMETERS) {
    this.myPerlParameters = PERL_PARAMETERS;
  }

  @Override
  public String getStartMode() {
    return myStartMode;
  }

  @Override
  public void setStartMode(String startMode) {
    this.myStartMode = startMode;
  }

  @Override
  public String getPerlRole() {
    return PerlDebugOptions.ROLE_SERVER;
  }

  @Override
  public String getHostToBind() {
    return LOCAL_DEBUG_HOST;
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

  @Override
  public void setScriptCharset(String scriptCharset) {
    this.myScriptCharset = scriptCharset;
  }

  @Override
  public boolean isNonInteractiveModeEnabled() {
    return myIsNonInteractiveModeEnabled;
  }

  @Override
  public void setNonInteractiveModeEnabled(boolean nonInteractiveModeEnabled) {
    myIsNonInteractiveModeEnabled = nonInteractiveModeEnabled;
  }

  @Override
  public boolean isCompileTimeBreakpointsEnabled() {
    return myIsCompileTimeBreakpointsEnabled;
  }

  @Override
  public void setCompileTimeBreakpointsEnabled(boolean compileTimeBreakpointsEnabled) {
    myIsCompileTimeBreakpointsEnabled = compileTimeBreakpointsEnabled;
  }

  public @NotNull PerlCommandLine createCommandLine(@NotNull PerlRunProfileState perlRunProfileState) throws ExecutionException {
    PerlCommandLine commandLine = createBaseCommandLine(perlRunProfileState);
    commandLine.withParentEnvironmentType(isPassParentEnvs() ? CONSOLE : NONE);
    commandLine.withWorkDirectory(computeWorkingDirectory(perlRunProfileState.getEnvironment().getProject()));
    commandLine.withCharset(computeCharset());

    return commandLine.withPty(isUsePty());
  }

  protected boolean isUsePty() {
    return true;
  }

  protected @NotNull Charset computeCharset() throws ExecutionException {
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

  /**
   * @see PerlRemoteDebuggingRunProfileState#computeLocalProjectPath(PerlRemoteDebuggingConfiguration)
   */
  protected @Nullable String computeWorkingDirectory(@NotNull Project project) throws ExecutionException {
    String workDirectory = getWorkingDirectory();
    if (StringUtil.isNotEmpty(workDirectory)) {
      return workDirectory;
    }
    return computeWorkingDirectory(project, computeNonNullScriptFile());
  }

  protected @NotNull PerlCommandLine createBaseCommandLine(@NotNull PerlRunProfileState perlRunProfileState) throws ExecutionException {
    ExecutionEnvironment executionEnvironment = perlRunProfileState.getEnvironment();
    Project project = executionEnvironment.getProject();
    List<String> additionalPerlParameters = perlRunProfileState.getAdditionalPerlParameters(this);
    Map<String, String> additionalEnvironmentVariables = perlRunProfileState.getAdditionalEnvironmentVariables();

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

  protected static @Nullable String computeWorkingDirectory(@NotNull Project project, @NotNull VirtualFile virtualFile) {
    var fileContentRoot = PerlFileUtil.getContentRoot(project, virtualFile);
    if (fileContentRoot != null) {
      return fileContentRoot.getPath();
    }
    return project.getBasePath();
  }

  protected @NotNull List<String> getScriptParameters() {
    String programParameters = getProgramParameters();
    return StringUtil.isEmpty(programParameters) ? Collections.emptyList() : ParametersListUtil.parse(programParameters);
  }

  @Override
  public String getInitCode() {
    return myInitCode;
  }

  @Override
  public void setInitCode(String initCode) {
    this.myInitCode = initCode;
  }

  /**
   * @return list of VirtualFiles pointed by  {@code paths} joined with pipe. Reverse of {@link #computePathsFromVirtualFiles(List)}
   */
  public static @NotNull List<VirtualFile> computeVirtualFilesFromPaths(@Nullable String paths) {
    if (StringUtil.isEmpty(paths)) {
      return Collections.emptyList();
    }
    List<String> pathNames = FILES_PARSER.fun(paths);
    if (pathNames.isEmpty()) {
      return Collections.emptyList();
    }
    List<VirtualFile> virtualFiles = new ArrayList<>(pathNames.size());
    for (String pathName : pathNames) {
      if (StringUtil.isEmpty(pathName)) {
        continue;
      }
      ContainerUtil.addIfNotNull(virtualFiles, VfsUtil.findFileByIoFile(new File(pathName), false));
    }
    return virtualFiles;
  }

  /**
   * @return paths of {@code virtualFiles} joined with pipe, reverse of {@link #computeVirtualFilesFromPaths(String)}
   */
  public static @NotNull String computePathsFromVirtualFiles(@NotNull List<VirtualFile> virtualFiles) {
    return FILES_JOINER.fun(ContainerUtil.map(virtualFiles, VirtualFile::getPath));
  }

  public @NotNull ConsoleView createConsole(@NotNull PerlRunProfileState runProfileState) throws ExecutionException {
    ExecutionEnvironment executionEnvironment = runProfileState.getEnvironment();
    PerlConsoleView console = ApplicationManager.getApplication().isUnitTestMode() ?
                              new PerlRunConsole(runProfileState.getEnvironment().getProject()) :
                              new PerlTerminalExecutionConsole(executionEnvironment.getProject());
    return console.withHostData(PerlHostData.from(getEffectiveSdk()));
  }

  /**
   * May patch {@code processHandler} created with {@code runProfileState} from current run configuration.
   *
   * @return patched process handler
   */
  protected @NotNull ProcessHandler doPatchProcessHandler(@NotNull ProcessHandler processHandler) {
    return processHandler;
  }

  /**
   * Method checks if specified script(s) path is ok.
   *
   * @throws RuntimeConfigurationException with human-readable error message
   */
  protected void checkConfigurationScriptPath() throws RuntimeConfigurationException {
    if (StringUtil.isEmptyOrSpaces(getScriptPath())) {
      throw new RuntimeConfigurationException(PerlBundle.message("perl.run.error.no.script.set"));
    }
    if (computeTargetFiles().isEmpty()) {
      throw new RuntimeConfigurationException(PerlBundle.message("perl.run.error.no.script.found", getScriptPath()));
    }
  }

  @Override
  public void checkConfiguration() throws RuntimeConfigurationException {
    checkConfigurationScriptPath();
    try {
      getEffectiveInterpreterPath();
    }
    catch (ExecutionException e) {
      throw new RuntimeConfigurationException(e.getMessage());
    }
  }

  /**
   * If profileState was build with {@link GenericPerlRunConfiguration}, patches {@code processHandler} using
   * {@link #doPatchProcessHandler(ProcessHandler)}
   *
   * @return patched process handler
   */
  @Contract("null,_->null; !null,_->!null")
  static @Nullable ProcessHandler patchProcessHandler(@Nullable ProcessHandler processHandler,
                                                      @NotNull PerlRunProfileState runProfileState) {
    if (processHandler == null) {
      return null;
    }
    RunProfile runConfiguration = runProfileState.getEnvironment().getRunProfile();
    return runConfiguration instanceof GenericPerlRunConfiguration ?
           ((GenericPerlRunConfiguration)runConfiguration).doPatchProcessHandler(processHandler) :
           processHandler;
  }
}
