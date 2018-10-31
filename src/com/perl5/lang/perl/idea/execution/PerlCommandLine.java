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

package com.perl5.lang.perl.idea.execution;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.execution.configurations.PtyCommandLine;
import com.intellij.execution.process.ProcessListener;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

public class PerlCommandLine extends GeneralCommandLine {
  private static final Logger LOG = Logger.getInstance(PerlCommandLine.class);
  @Nullable
  private Sdk mySdk;

  @Nullable
  private PerlHostData myHostData;

  @Nullable
  private PerlVersionManagerData myVersionManagerData;

  private boolean myUsePty = false;

  @Nullable
  private Project myProject;

  @Nullable
  private Module myModule;

  @Nullable
  private String myConsoleTitle;

  @Nullable
  private Icon myConsoleIcon;

  @NotNull
  private List<ProcessListener> myProcessListeners = Collections.emptyList();

  public PerlCommandLine() {
  }

  public PerlCommandLine(@NotNull Sdk sdk) {
    mySdk = sdk;
  }

  public PerlCommandLine(@NotNull String... command) {
    super(command);
  }

  public PerlCommandLine(@NotNull List<String> command) {
    super(command);
  }

  public PerlCommandLine(@NotNull GeneralCommandLine original) {
    super(original);
    if (original instanceof PerlCommandLine) {
      mySdk = ((PerlCommandLine)original).mySdk;
      myHostData = ((PerlCommandLine)original).myHostData;
      myVersionManagerData = ((PerlCommandLine)original).myVersionManagerData;
      myUsePty = ((PerlCommandLine)original).myUsePty;
      myConsoleTitle = ((PerlCommandLine)original).myConsoleTitle;
      myConsoleIcon = ((PerlCommandLine)original).myConsoleIcon;
      myProcessListeners = new ArrayList<>(((PerlCommandLine)original).myProcessListeners);
      myProject = ((PerlCommandLine)original).myProject;
      myModule = ((PerlCommandLine)original).myModule;
    }
  }

  @NotNull
  @Override
  public PerlCommandLine withEnvironment(@Nullable Map<String, String> environment) {
    return (PerlCommandLine)super.withEnvironment(environment);
  }

  @NotNull
  @Override
  public PerlCommandLine withEnvironment(@NotNull String key, @NotNull String value) {
    return (PerlCommandLine)super.withEnvironment(key, value);
  }

  @NotNull
  @Override
  public PerlCommandLine withParameters(@NotNull String... parameters) {
    return (PerlCommandLine)super.withParameters(parameters);
  }

  @NotNull
  @Override
  public PerlCommandLine withWorkDirectory(@Nullable String path) {
    return (PerlCommandLine)super.withWorkDirectory(path);
  }

  @NotNull
  @Override
  public PerlCommandLine withWorkDirectory(@Nullable File workDirectory) {
    return (PerlCommandLine)super.withWorkDirectory(workDirectory);
  }

  @NotNull
  @Override
  public PerlCommandLine withParameters(@NotNull List<String> parameters) {
    return (PerlCommandLine)super.withParameters(parameters);
  }

  @Nullable
  public Sdk getEffectiveSdk() {
    return mySdk != null ? mySdk : PerlProjectManager.getSdk(getEffectiveProject());
  }

  @NotNull
  public PerlCommandLine withSdk(@Nullable Sdk sdk) {
    mySdk = sdk;
    return this;
  }

  /**
   * @return explicit data or data from sdk if any, null otherwise
   */
  @Nullable
  public PerlHostData getEffectiveHostData() {
    return myHostData == null ? PerlHostData.from(getEffectiveSdk()) : myHostData;
  }

  @NotNull
  public PerlCommandLine withHostData(@Nullable PerlHostData hostData) {
    myHostData = hostData;
    return this;
  }

  /**
   * @return explicit data or data from sdk if any, null otherwise
   */
  @Nullable
  public PerlVersionManagerData getEffectiveVersionManagerData() {
    return myVersionManagerData == null ? PerlVersionManagerData.from(getEffectiveSdk()) : myVersionManagerData;
  }

  @NotNull
  public PerlCommandLine withVersionManagerData(@Nullable PerlVersionManagerData versionManagerData) {
    myVersionManagerData = versionManagerData;
    return this;
  }

  @Nullable
  public String getConsoleTitle() {
    return myConsoleTitle;
  }

  @NotNull
  public PerlCommandLine withConsoleTitle(@Nullable String consoleTitle) {
    myConsoleTitle = consoleTitle;
    return this;
  }

  @NotNull
  public PerlCommandLine prependLineWith(@NotNull String... commands) {
    ArrayList<String> commandsList = ContainerUtil.newArrayList(commands);
    commandsList.add(getExePath());
    setExePath(commandsList.remove(0));
    if (!commandsList.isEmpty()) {
      ParametersList parametersList = getParametersList();
      ContainerUtil.reverse(commandsList).forEach(it -> parametersList.addAt(0, it));
    }
    return this;
  }

  @NotNull
  public List<ProcessListener> getProcessListeners() {
    return Collections.unmodifiableList(myProcessListeners);
  }

  @NotNull
  public PerlCommandLine withProcessListener(@NotNull ProcessListener... listeners) {
    myProcessListeners = new ArrayList<>(myProcessListeners);
    myProcessListeners.addAll(ContainerUtil.filter(listeners, Objects::nonNull));
    return this;
  }

  @NotNull
  @Override
  public PerlCommandLine withCharset(@NotNull Charset charset) {
    return (PerlCommandLine)super.withCharset(charset);
  }

  public boolean isUsePty() {
    return myUsePty;
  }

  @NotNull
  public PerlCommandLine withPty(boolean usePty) {
    myUsePty = usePty;
    return this;
  }

  @Nullable
  public Project getEffectiveProject() {
    return myProject != null ? myProject : ObjectUtils.doIfNotNull(getModule(), Module::getProject);
  }

  @NotNull
  public Project getNonNullEffectiveProject() {
    return ObjectUtils.notNull(getEffectiveProject());
  }

  @NotNull
  public PerlCommandLine withProject(@Nullable Project project) {
    myProject = project;
    return this;
  }

  @Nullable
  public Module getModule() {
    return myModule;
  }

  @NotNull
  public PerlCommandLine withModule(@Nullable Module module) {
    myModule = module;
    return this;
  }

  @Nullable
  public Icon getConsoleIcon() {
    return myConsoleIcon;
  }

  @NotNull
  public PerlCommandLine withConsoleIcon(@Nullable Icon consoleIcon) {
    myConsoleIcon = consoleIcon;
    return this;
  }

  @NotNull
  @Override
  public Process createProcess() throws ExecutionException {
    if (!myUsePty && ApplicationManager.getApplication().isDispatchThread()) {
      throw new RuntimeException("Non-console executions should not be performed on EDT");
    }
    LOG.info("Executing: " + getCommandLineString());
    LOG.info("  environment: " + getEnvironment() + " (+" + getParentEnvironmentType() + ")");
    LOG.info("  host = " + getEffectiveHostData() +
             "; vm = " + getEffectiveVersionManagerData() +
             "; pty = " + isUsePty() +
             "; charset: " + getCharset());
    return myUsePty ?
           new PtyCommandLine(this).withInitialColumns(PtyCommandLine.MAX_COLUMNS).withEnvironment("TERM", "xterm-256color").createProcess()
                    :
           super.createProcess();
  }
}
