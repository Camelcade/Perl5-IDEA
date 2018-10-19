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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.PtyCommandLine;
import com.intellij.execution.process.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.serialization.PathMacroUtil;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.Map;

import static com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.CONSOLE;
import static com.intellij.execution.configurations.GeneralCommandLine.ParentEnvironmentType.NONE;

/**
 * @author VISTALL
 * @since 16-Sep-15
 */
public class PerlRunProfileState extends CommandLineState {
  public PerlRunProfileState(ExecutionEnvironment environment) {
    super(environment);
  }

  @NotNull
  @Override
  protected ProcessHandler startProcess() throws ExecutionException {
    PerlRunConfiguration runProfile = (PerlRunConfiguration)getEnvironment().getRunProfile();

    VirtualFile scriptFile = runProfile.getScriptFile();
    if (scriptFile == null) {
      throw new ExecutionException("Script file: " + runProfile.getScriptPath() + " is not exists");
    }

    Project project = getEnvironment().getProject();
    String perlSdkPath = PerlProjectManager.getInterpreterPath(project, scriptFile);

    String alternativeSdkPath = runProfile.getAlternativeSdkPath();
    if (runProfile.isUseAlternativeSdk() && !StringUtil.isEmpty(alternativeSdkPath)) {
      Sdk sdk = PerlSdkTable.getInstance().findJdk(alternativeSdkPath);
      if (sdk != null) {
        perlSdkPath = sdk.getHomePath();
      }
      else {
        perlSdkPath = alternativeSdkPath;
      }
    }

    if (perlSdkPath == null) {
      throw new ExecutionException("Unable to locate Perl Interpreter");
    }

    String homePath = runProfile.getWorkingDirectory();
    if (StringUtil.isEmpty(homePath)) {
      Module moduleForFile = ModuleUtilCore.findModuleForFile(scriptFile, project);
      if (moduleForFile != null) {
        homePath = PathMacroUtil.getModuleDir(moduleForFile.getModuleFilePath());
      }
      else {
        homePath = project.getBasePath();
      }
    }

    assert homePath != null;

    GeneralCommandLine commandLine = PerlRunUtil.getPerlCommandLine(project, perlSdkPath, scriptFile, getPerlParameters(runProfile));

    String programParameters = runProfile.getProgramParameters();

    if (programParameters != null) {
      commandLine.addParameters(StringUtil.split(programParameters, " "));
    }

    String charsetName = runProfile.getConsoleCharset();
    Charset charset;
    if (!StringUtil.isEmpty(charsetName)) {
      try {
        charset = Charset.forName(charsetName);
      }
      catch (UnsupportedCharsetException e) {
        throw new ExecutionException("Unknown charset: " + charsetName);
      }
    }
    else {
      charset = scriptFile.getCharset();
    }

    commandLine.setCharset(charset);
    commandLine.withWorkDirectory(homePath);
    Map<String, String> environment = calcEnv(runProfile);
    commandLine.withEnvironment(environment);
    commandLine.withParentEnvironmentType(runProfile.isPassParentEnvs() ? CONSOLE : NONE);
    PtyCommandLine ptyCommandLine = new PtyCommandLine(commandLine);
    OSProcessHandler handler = new ColoredProcessHandler(ptyCommandLine.createProcess(), ptyCommandLine.getCommandLineString(), charset) {
      @Override
      public void startNotify() {
        super.startNotify();
        String perl5Opt = environment.get(PerlRunUtil.PERL5OPT);
        if (StringUtil.isNotEmpty(perl5Opt)) {
          notifyTextAvailable(" - " + PerlRunUtil.PERL5OPT + "=" + perl5Opt + '\n', ProcessOutputTypes.SYSTEM);
        }
      }
    };
    ProcessTerminatedListener.attach(handler, project);
    return handler;
  }

  @NotNull
  protected String[] getPerlParameters(PerlRunConfiguration runProfile) {

    String perlParameters = runProfile.getPerlParameters();
    if (perlParameters == null) {
      return new String[0];
    }
    List<String> result = StringUtil.split(perlParameters, " ");
    return result.toArray(new String[0]);
  }

  protected Map<String, String> calcEnv(PerlRunConfiguration runProfile) throws ExecutionException {
    return runProfile.getEnvs();
  }
}
