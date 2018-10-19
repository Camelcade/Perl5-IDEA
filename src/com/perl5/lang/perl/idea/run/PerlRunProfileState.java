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
import com.intellij.execution.process.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.PerlSdkAdditionalData;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.serialization.PathMacroUtil;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;
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
    Sdk perlSdk = PerlProjectManager.getSdk(project);
    if (perlSdk == null) {
      throw new ExecutionException("Unable to detect perl sdk for a project " + project);
    }

    String perlInterpreterPath = perlSdk.getHomePath();
    if (perlInterpreterPath == null) {
      throw new ExecutionException("Perl sdk is corrupted: " + perlSdk);
    }

    String workDirectory = runProfile.getWorkingDirectory();
    if (StringUtil.isEmpty(workDirectory)) {
      Module moduleForFile = ModuleUtilCore.findModuleForFile(scriptFile, project);
      if (moduleForFile != null) {
        workDirectory = PathMacroUtil.getModuleDir(moduleForFile.getModuleFilePath());
      }
      else {
        workDirectory = project.getBasePath();
      }
    }

    GeneralCommandLine commandLine = PerlRunUtil.getPerlCommandLine(
      project, perlInterpreterPath, scriptFile, getPerlParameters(runProfile), getScriptParameters(runProfile));

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
    commandLine.withWorkDirectory(workDirectory);
    Map<String, String> environment = calcEnv(runProfile);
    commandLine.withEnvironment(environment);
    commandLine.withParentEnvironmentType(runProfile.isPassParentEnvs() ? CONSOLE : NONE);

    ProcessHandler handler = PerlRunUtil.createConsoleProcessHandler(PerlSdkAdditionalData.notNullFrom(perlSdk), commandLine, charset);
    handler.addProcessListener(new ProcessAdapter() {
      @Override
      public void startNotified(@NotNull ProcessEvent event) {
        String perl5Opt = environment.get(PerlRunUtil.PERL5OPT);
        if (StringUtil.isNotEmpty(perl5Opt)) {
          handler.notifyTextAvailable(" - " + PerlRunUtil.PERL5OPT + "=" + perl5Opt + '\n', ProcessOutputTypes.SYSTEM);
        }
      }
    });
    ProcessTerminatedListener.attach(handler, project);
    return handler;
  }

  @NotNull
  protected List<String> getPerlParameters(PerlRunConfiguration runProfile) {

    String perlParameters = runProfile.getPerlParameters();
    return StringUtil.isEmpty(perlParameters) ? Collections.emptyList() : StringUtil.split(perlParameters, " ");
  }

  @NotNull
  protected List<String> getScriptParameters(@NotNull PerlRunConfiguration runProfile) {
    String programParameters = runProfile.getProgramParameters();
    return StringUtil.isEmpty(programParameters) ? Collections.emptyList() : StringUtil.split(programParameters, " ");
  }

  protected Map<String, String> calcEnv(PerlRunConfiguration runProfile) throws ExecutionException {
    return runProfile.getEnvs();
  }
}
