/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.sdk.host.docker;

import com.intellij.execution.CommandLineUtil;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.execution.util.ExecUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.ArrayUtil;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlExecutionException;
import com.perl5.lang.perl.idea.sdk.host.PerlFileDescriptor;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.util.PerlPluginUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Adapter running command using docker cli
 */
class PerlDockerAdapter {
  private static final Logger LOG = Logger.getInstance(PerlDockerAdapter.class);
  private static final String CONTAINER_NAME_PREFIX = "intellijPerl_";
  private static final String KILL = "kill";
  private static final String RUN = "run";
  private static final String EXEC = "exec";
  private static final String WITH_AUTOREMOVE = "--rm";
  private static final String WITH_ENTRYPOINT = "--entrypoint";
  private static final String AS_DAEMON = "-d";
  private static final String CONTAINER = "container";
  private static final String REMOVE = "rm";
  private static final String CREATE = "create";
  private static final String WITH_CONTAINER_NAME = "--name";
  private static final String COPY = "cp";
  private static final String AS_ARCHIVE = "--archive";
  private static final String FOLLOWING_LINKS = "--follow-link";
  private static final String IMAGE = "image";
  private static final String LIST_IMAGE = "ls";
  private static final String IN_FORMAT = "--format";
  private static final String INTERACTIVELY = "-i";
  private static final String WITH_ATTACHED = "-a";
  private static final String STDOUT = "stdout";
  private static final String STDERR = "stderr";
  private static final String STDIN = "stdin";
  private static final String WITH_TTY = "-t";
  private static final String WITH_VOLUME = "-v";
  private static final String EXPOSE_PORT = "--expose";
  private static final String PUBLISH_PORT = "-p";
  private static final String WORKING_DIRECTORY = "-w";
  static final String DOCKER_EXECUTABLE = SystemInfo.isWindows ? "docker.exe" : "docker";

  private final @NotNull PerlDockerData myData;

  public PerlDockerAdapter(@NotNull PerlDockerData data) {
    myData = data;
  }

  /**
   * @return new container name, generated from {@code containerNameSeed}
   */
  public @NotNull String createContainer(@NotNull String containerNameSeed) throws ExecutionException {
    String containerName = createContainerName(containerNameSeed);
    runCommand(CONTAINER, CREATE, WITH_CONTAINER_NAME, containerName, myData.getImageName());
    return containerName;
  }

  /**
   * @return new container name, generated from {@code containerNameSeed}
   */
  public @NotNull String createRunningContainer(@NotNull String containerNameSeed) throws ExecutionException {
    String containerName = createContainerName(containerNameSeed);
    runCommand(RUN, AS_DAEMON, WITH_AUTOREMOVE,
               WITH_ENTRYPOINT, "",
               WITH_CONTAINER_NAME, containerName,
               myData.getImageName(), "bash", "-c", "while true;do sleep 1000000;done");
    return containerName;
  }

  private boolean tryCopyWithTar(@NotNull String containerName, @NotNull String remotePath, @NotNull String localPath)
    throws ExecutionException {
    File localPathFile = new File(localPath);
    File remotePathFile = new File(remotePath);
    String command = String.join(" ", DOCKER_EXECUTABLE, EXEC, INTERACTIVELY, containerName,
                                 "tar", "hCcF", remotePathFile.getParent(), "-", remotePathFile.getName(),
                                 "|", "tar", "Cxf", localPathFile.getParent(), "-");
    if (SystemInfo.isLinux) {
      try {
        checkOutput(PerlHostData.execAndGetOutput(
          new PerlCommandLine("sh").withHostData(PerlHostHandler.getDefaultHandler().createData())
            .withParameters("-c", command)));
        return true;
      }
      catch (PerlExecutionException e) {
        LOG.error(e);
      }
    }
    else {
      LOG.error("Try execute command: " + command);
    }
    return false;
  }

  public void copyRemote(@NotNull String containerName, @NotNull String remotePath, @NotNull String localPath) throws ExecutionException {
    try {
      File localPathFile = new File(localPath);
      FileUtil.createDirectory(localPathFile);
      runCommand(COPY, AS_ARCHIVE, FOLLOWING_LINKS, containerName + ':' + remotePath, localPathFile.getParent());
    }
    catch (PerlExecutionException e) {
      ProcessOutput processOutput = e.getProcessOutput();
      String stderr = processOutput.getStderr();
      if (!(stderr.contains("invalid symlink") && tryCopyWithTar(containerName, remotePath, localPath)) &&
          !stderr.contains("no such file or directory") &&
          !stderr.contains("Could not find the file") &&
          !stderr.contains("No such container:path")) {
        throw e;
      }
    }
  }

  public void killContainer(@NotNull String... containers) throws ExecutionException {
    runCommand(ArrayUtil.mergeArrays(new String[]{KILL}, containers));
  }

  private void dropContainer(@NotNull String containerName) throws ExecutionException {
    runCommand(CONTAINER, REMOVE, containerName);
  }

  private @NotNull ProcessOutput checkOutput(@NotNull ProcessOutput output) throws ExecutionException {
    if (output.getExitCode() != 0) {
      throw new PerlExecutionException(output);
    }
    return output;
  }

  public @NotNull PerlDockerData getData() {
    return myData;
  }

  /**
   * @return contents of {@code path} in the container.
   */
  public @NotNull List<PerlFileDescriptor> listFiles(@NotNull String containerName, @NotNull String path) {
    try {
      if (ApplicationManager.getApplication().isDispatchThread()) {
        return ProgressManager.getInstance().runProcessWithProgressSynchronously(
          () -> doListFiles(containerName, path),
          PerlDockerBundle.message("docker.adapter.listing.files.in", path),
          true,
          null
        );
      }
      else {
        return doListFiles(containerName, path);
      }
    }
    catch (ExecutionException e) {
      LOG.error(e);
      return Collections.emptyList();
    }
  }

  private @NotNull List<PerlFileDescriptor> doListFiles(@NotNull String containerName, @NotNull String path) throws ExecutionException {
    try {
      ProcessOutput output = runCommand(EXEC, INTERACTIVELY, containerName, "ls", "-LAs", "--classify", path);
      return output.getStdoutLines().stream()
        .map(it -> PerlFileDescriptor.create(path, it))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    }
    catch (ExecutionException e) {
      if (StringUtil.contains(e.getMessage(), "cannot access")) {
        LOG.warn("Could not access " + path + " " + e.getMessage());
        return Collections.emptyList();
      }
      throw e;
    }
  }

  private static String createContainerName(@NotNull String seed) {
    return CONTAINER_NAME_PREFIX + seed + "_" + System.currentTimeMillis();
  }

  private @NotNull ProcessOutput runCommand(@NotNull String... params) throws ExecutionException {
    return checkOutput(PerlHostData.execAndGetOutput(baseCommandLine().withParameters(params)));
  }

  /**
   * Wrapping a {@code commandLine} to a script and runs it using {@code docker} command, returning it's process
   */
  public Process createProcess(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    PerlCommandLine dockerCommandLine = buildBaseProcessCommandLine(commandLine);

    // mounting helpers
    dockerCommandLine.withParameters(WITH_VOLUME, PerlPluginUtil.getPluginHelpersRoot() + ':' + myData.getHelpersRootPath());

    if (!commandLine.isUserCommandLine()) {
      dockerCommandLine.withParameters(WITH_ENTRYPOINT, "");
    }

    Project project = commandLine.getEffectiveProject();
    if (project != null) {
      // mounting modules roots
      Set<VirtualFile> roots = new HashSet<>();
      for (Module module : ModuleManager.getInstance(project).getModules()) {
        roots.addAll(Arrays.asList(ModuleRootManager.getInstance(module).getContentRoots()));
      }
      roots.addAll(PerlProjectManager.getInstance(project).getExternalLibraryRoots());
      for (VirtualFile rootToMount : VfsUtil.getCommonAncestors(roots.toArray(VirtualFile.EMPTY_ARRAY))) {
        String localPath = rootToMount.getPath();
        String remotePath = myData.getRemotePath(localPath);
        dockerCommandLine.withParameters(WITH_VOLUME, localPath + ':' + remotePath);
      }

      // adding project settings if possible
      dockerCommandLine
        .withParameters(StringUtil.split(PerlDockerProjectSettings.getInstance(project).getAdditionalDockerParameters(), " "));
    }

    // working directory
    File remoteWorkingDirectory = myData.getRemotePath(commandLine.getWorkDirectory());
    if (remoteWorkingDirectory != null) {
      dockerCommandLine.withParameters(WORKING_DIRECTORY + "=" + StringUtil.escapeChar(
        FileUtil.toSystemIndependentName(remoteWorkingDirectory.toString()), ' '));
    }

    // required by coverage, probably we should have a getter for this; Also contains a temp path
    String localSystemPath = PathManager.getSystemPath();
    dockerCommandLine.withParameters(WITH_VOLUME, localSystemPath + ':' + myData.getRemotePath(localSystemPath));

    // we sure that command script is under system dir
    File script = createCommandScript(commandLine);
    String dockerScriptPath = myData.getRemotePath(script.getPath());
    if (StringUtil.isEmpty(dockerScriptPath)) {
      throw new ExecutionException("Unable to map path for " + script.getPath() + " in " + myData);
    }

    return dockerCommandLine.withParameters(myData.getImageName(), "sh", dockerScriptPath).createProcess();
  }

  /**
   * @return list of images in {@code name[:tag]} format
   */
  List<String> listImages() throws ExecutionException {
    ProcessOutput output = runCommand(IMAGE, LIST_IMAGE, IN_FORMAT, "{{.Repository}}:{{.Tag}}");
    return output.getStdoutLines().stream()
      .map(it -> StringUtil.replace(it, ":<none>", ""))
      .filter(it -> !StringUtil.equals(it, "<none>"))
      .sorted().collect(Collectors.toList());
  }

  private static PerlCommandLine baseCommandLine() {
    return new PerlCommandLine(DOCKER_EXECUTABLE).withHostData(PerlHostHandler.getDefaultHandler().createData());
  }

  static @NotNull PerlCommandLine buildBaseProcessCommandLine(@NotNull PerlCommandLine commandLine) {
    PerlCommandLine dockerCommandLine = baseCommandLine()
      .withParameters(RUN, WITH_AUTOREMOVE, INTERACTIVELY)
      .withParameters(WITH_ATTACHED, STDOUT, WITH_ATTACHED, STDERR, WITH_ATTACHED, STDIN)
      .withCharset(commandLine.getCharset());

    if (commandLine.isUsePty()) {
      dockerCommandLine.withParameters(WITH_TTY);
      dockerCommandLine.withPty(true);
    }

    // mapping ports
    commandLine.getPortMappings().forEach(
      it -> dockerCommandLine.withParameters(EXPOSE_PORT,
                                             String.valueOf(it.getRemote()),
                                             PUBLISH_PORT, it.getLocal() + ":" + it.getRemote()));
    return dockerCommandLine;
  }

  private @NotNull File createCommandScript(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    StringBuilder sb = new StringBuilder();
    commandLine.getEnvironment().forEach((key, val) -> sb.append("export ").append(key).append('=')
      .append(CommandLineUtil.posixQuote(val)).append("\n"));
    sb.append(String.join(" ", ContainerUtil.map(commandLine.getCommandLineList(null), CommandLineUtil::posixQuote)));

    try {
      String command = sb.toString();
      LOG.debug("Executing in ", myData.getImageName());
      StringUtil.split(command, "\n").forEach(it -> LOG.debug("    ", it));
      var dockerWrapper = ExecUtil.createTempExecutableScript("dockerWrapper", "", command);
      LOG.debug("Created docker wrapper: ", dockerWrapper);
      return dockerWrapper;
    }
    catch (IOException e) {
      throw new ExecutionException(e);
    }
  }
}
