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

package com.perl5.lang.perl.idea.run.remote;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.RunConfigurationWithSuppressedDefaultRunAction;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.xmlb.XmlSerializer;
import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugSession;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugProcess;
import com.perl5.lang.perl.idea.run.debugger.PerlDebuggableRunConfiguration;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * Created by hurricup on 09.05.2016.
 */
public class PerlRemoteDebuggingConfiguration extends RunConfigurationBase
  implements RunConfigurationWithSuppressedDefaultRunAction, PerlDebuggableRunConfiguration {
  public String debugHost = "localhost";
  public int debugPort = 12345;
  public String remoteProjectRoot = "/home/";
  public String scriptCharset = "utf8";
  public String perlRole = "client";
  public String startMode = "RUN";
  public boolean isNonInteractiveModeEnabled = false;
  public boolean isCompileTimeBreakpointsEnabled = false;
  public String initCode = "";
  @Tag("auto-reconnect")
  private boolean myIsReconnect;

  public PerlRemoteDebuggingConfiguration(Project project, @NotNull ConfigurationFactory factory, String name) {
    super(project, factory, name);
  }

  @NotNull
  @Override
  public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    return new PerlRemoteDebuggingConfigurationEditor(getProject());
  }

  @Override
  public void checkConfiguration() {

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
  public PerlRemoteDebuggingRunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) {
    if (executor instanceof DefaultDebugExecutor) {
      return new PerlRemoteDebuggingRunProfileState(environment);
    }
    return null;
  }

  public String getDebugHost() {
    return debugHost;
  }

  public void setDebugHost(String debugHost) {
    this.debugHost = debugHost;
  }

  public int getDebugPort() {
    return debugPort;
  }

  public void setDebugPort(int debugPort) {
    this.debugPort = debugPort;
  }

  public String getRemoteProjectRoot() {
    return remoteProjectRoot;
  }

  public void setRemoteProjectRoot(String remoteWorkingDirectory) {
    this.remoteProjectRoot = remoteWorkingDirectory;
  }

  public String getScriptCharset() {
    return scriptCharset;
  }

  public void setScriptCharset(String scriptCharset) {
    this.scriptCharset = scriptCharset;
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
    return perlRole;
  }

  public void setPerlRole(String perlRole) {
    this.perlRole = perlRole;
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

  @Override
  public boolean isReconnect() {
    return myIsReconnect;
  }

  public void setReconnect(boolean reconnect) {
    myIsReconnect = reconnect;
  }

  @NotNull
  @Override
  public XDebugProcess createDebugProcess(@NotNull InetSocketAddress socketAddress,
                                          @NotNull XDebugSession session,
                                          @Nullable ExecutionResult executionResult,
                                          @NotNull ExecutionEnvironment environment) throws ExecutionException {
    PerlRemoteDebuggingRunProfileState runProfileState = Objects.requireNonNull(getState(environment.getExecutor(), environment));
    return new PerlDebugProcess(session, runProfileState, runProfileState.execute(environment.getExecutor()));
  }
}
