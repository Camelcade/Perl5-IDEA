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

package com.perl5.lang.perl.idea.sdk.host;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.AbstractPerlData;
import com.perl5.lang.perl.idea.sdk.PerlSdkAdditionalData;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import com.perl5.lang.perl.idea.sdk.versionManager.PerlVersionManagerData;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Contains data necessary to access a perl host. E.g. local, wsl, ssh, docker
 */
public abstract class PerlHostData<Data extends PerlHostData<Data, Handler>, Handler extends PerlHostHandler<Data, Handler>>
  extends AbstractPerlData<Data, Handler> {
  private static final Logger LOG = Logger.getInstance(PerlHostData.class);

  public PerlHostData(@NotNull Handler handler) {
    super(handler);
  }

  /**
   * @return an OS handler for this host
   */
  @NotNull
  public abstract PerlOsHandler getOsHandler();

  /**
   * Returns a recommended starting path for a file chooser (where SDKs of this type are usually may be found),
   * or {@code null} if not applicable/no SDKs found.
   * <p/>
   * E.g. for Python SDK on Unix the method may return either {@code "/usr/bin"} or {@code "/usr/bin/python"}
   * (if there is only one Python interpreter installed on a host).
   */
  @Nullable
  public abstract Path suggestHomePath();

  /**
   * @return short lowercased name, for interpreters list
   */
  @NotNull
  public String getShortName() {
    return getHandler().getShortName();
  }

  /**
   * Attempts to find a file at host
   */
  @Nullable
  public abstract Path findFile(@NotNull String fileName);

  /**
   * Creates a process and process handler to be run in console.
   */
  @NotNull
  protected abstract ProcessHandler doCreateConsoleProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException;

  @NotNull
  protected abstract ProcessOutput doExecAndGetOutput(@NotNull PerlCommandLine commandLine) throws ExecutionException;

  @NotNull
  public static ProcessHandler createConsoleProcessHandler(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    PerlHostData hostData = commandLine.getEffectiveHostData();
    if (hostData == null) {
      throw new ExecutionException("No host data in the command line " + commandLine);
    }
    PerlVersionManagerData versionManagerData = commandLine.getEffectiveVersionManagerData();
    if (versionManagerData != null) {
      commandLine = versionManagerData.patchCommandLine(commandLine);
    }
    final Map<String, String> environment = commandLine.getEnvironment();
    ProcessHandler handler = hostData.doCreateConsoleProcessHandler(commandLine);
    handler.addProcessListener(new ProcessAdapter() {
      @Override
      public void startNotified(@NotNull ProcessEvent event) {
        String perl5Opt = environment.get(PerlRunUtil.PERL5OPT);
        if (StringUtil.isNotEmpty(perl5Opt)) {
          handler.notifyTextAvailable(" - " + PerlRunUtil.PERL5OPT + "=" + perl5Opt + '\n', ProcessOutputTypes.SYSTEM);
        }
      }
    });
    return handler;
  }

  @NotNull
  public static ProcessOutput execAndGetOutput(@NotNull PerlCommandLine commandLine)
    throws ExecutionException {
    PerlVersionManagerData versionManagerData = commandLine.getEffectiveVersionManagerData();
    if (versionManagerData != null) {
      commandLine = versionManagerData.patchCommandLine(commandLine);
    }

    PerlHostData perlHostData = commandLine.getEffectiveHostData();
    if (perlHostData == null) {
      throw new ExecutionException("No host data in " + commandLine);
    }
    assertNotEdt();
    return perlHostData.doExecAndGetOutput(commandLine);
  }

  /**
   * @return stream of existing sdks from this host
   */
  @NotNull
  public final Stream<Sdk> getHostSdkStream() {
    return PerlSdkTable.getInstance().getInterpreters().stream().filter(it -> this.equals(PerlHostData.from(it)));
  }

  @Contract("null->null")
  @Nullable
  public static PerlHostData from(@Nullable Sdk sdk) {
    return ObjectUtils.doIfNotNull(PerlSdkAdditionalData.from(sdk), PerlSdkAdditionalData::getHostData);
  }

  protected static void assertNotEdt() {
    if (ApplicationManager.getApplication().isDispatchThread()) {
      throw new RuntimeException("Executions should not be performed on EDT");
    }
  }

  @NotNull
  public static PerlHostData notNullFrom(@NotNull Sdk sdk) {
    return Objects.requireNonNull(from(sdk), () -> "No host data in " + sdk);
  }
}

