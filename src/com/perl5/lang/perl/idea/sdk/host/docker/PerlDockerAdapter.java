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

package com.perl5.lang.perl.idea.sdk.host.docker;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.execution.util.ExecUtil;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
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

  @NotNull
  private final PerlDockerData myData;

  public PerlDockerAdapter(@NotNull PerlDockerData data) {
    myData = data;
  }

  /**
   * @return new container name, generated from {@code containerNameSeed}
   */
  @NotNull
  public String createContainer(@NotNull String containerNameSeed) throws ExecutionException {
    String containerName = createContainerName(containerNameSeed);
    runCommand(CONTAINER, CREATE, WITH_CONTAINER_NAME, containerName, myData.getImageName());
    return containerName;
  }

  /**
   * @return new container name, generated from {@code containerNameSeed}
   */
  @NotNull
  public String createRunningContainer(@NotNull String containerNameSeed) throws ExecutionException {
    String containerName = createContainerName(containerNameSeed);
    runCommand(RUN, AS_DAEMON, WITH_AUTOREMOVE, WITH_CONTAINER_NAME,
               containerName, myData.getImageName(), "bash", "-c", "while true;do sleep 1000000;done");
    return containerName;
  }

  public void copyRemote(@NotNull String remotePath, @NotNull String localPath) throws ExecutionException {
    String containerName = createContainer("copying_" + myData.getSafeImageName());

    try {
      File localPathFile = new File(localPath);
      FileUtil.createDirectory(localPathFile);
      runCommand(COPY, AS_ARCHIVE, FOLLOWING_LINKS, containerName + ':' + remotePath, localPathFile.getParent());
    }
    catch (PerlExecutionException e) {
      ProcessOutput processOutput = e.getProcessOutput();
      String stderr = processOutput.getStderr();
      if (!stderr.contains("no such file or directory") &&
          !stderr.contains("Could not find the file") &&
          !stderr.contains("No such container:path")) {
        throw e;
      }
    }
    finally {
      dropContainer(containerName);
    }
  }

  public void killContainer(@NotNull String... containers) throws ExecutionException {
    runCommand(ArrayUtil.mergeArrays(new String[]{KILL}, containers));
  }

  private void dropContainer(@NotNull String containerName) throws ExecutionException {
    runCommand(CONTAINER, REMOVE, containerName);
  }

  @NotNull
  private ProcessOutput checkOutput(@NotNull ProcessOutput output) throws ExecutionException {
    if (output.getExitCode() != 0) {
      throw new PerlExecutionException(output);
    }
    return output;
  }

  @NotNull
  public PerlDockerData getData() {
    return myData;
  }

  /**
   * @return contents of {@code path} in the container.
   */
  @NotNull
  public List<PerlFileDescriptor> listFiles(@NotNull String containerName, @NotNull String path) {
    try {
      ProcessOutput output = runCommand(EXEC, containerName, "ls", "-LAs", "--classify", path);
      return output.getStdoutLines().stream()
        .map(it -> PerlFileDescriptor.create(path, it))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    }
    catch (ExecutionException e) {
      LOG.error(e);
      return Collections.emptyList();
    }
  }

  private static String createContainerName(@NotNull String seed) {
    return CONTAINER_NAME_PREFIX + seed + "_" + System.currentTimeMillis();
  }

  @NotNull
  private ProcessOutput runCommand(@NotNull String... params) throws ExecutionException {
    return checkOutput(PerlHostData.execAndGetOutput(baseCommandLine().withParameters(params)));
  }

  private PerlCommandLine baseCommandLine() {
    return new PerlCommandLine("docker").withHostData(PerlHostHandler.getDefaultHandler().createData());
  }

  /**
   * @return list of images in {@code name[:tag]} format
   */
  List<String> listImages() throws ExecutionException {
    ProcessOutput output = runCommand(IMAGE, LIST_IMAGE, IN_FORMAT, "{{.Repository}}:{{.Tag}}");
    return output.getStdoutLines().stream().map(it -> StringUtil.replace(it, ":<none>", "")).sorted().collect(Collectors.toList());
  }

  /**
   * Wrapping a {@code commandLine} to a script and runs it using {@code docker} command, returning it's process
   */
  public Process createProcess(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    PerlCommandLine dockerCommandLine = baseCommandLine()
      .withParameters(RUN, WITH_AUTOREMOVE, INTERACTIVELY)
      .withParameters(WITH_ATTACHED, STDOUT, WITH_ATTACHED, STDERR, WITH_ATTACHED, STDIN)
      .withCharset(commandLine.getCharset());

    if (commandLine.isUsePty()) {
      dockerCommandLine.withParameters(WITH_TTY);
      dockerCommandLine.withPty(true);
    }

    // mounting helpers
    dockerCommandLine.withParameters(WITH_VOLUME, PerlPluginUtil.getPluginHelpersRoot() + ':' + myData.getHelpersRootPath());

    Project project = commandLine.getEffectiveProject();
    if (project != null) {
      // mounting modules roots
      Set<VirtualFile> roots = ContainerUtil.newHashSet();
      for (Module module : ModuleManager.getInstance(project).getModules()) {
        roots.addAll(Arrays.asList(ModuleRootManager.getInstance(module).getContentRoots()));
      }
      roots.addAll(PerlProjectManager.getInstance(project).getExternalLibraryRoots());
      for (VirtualFile rootToMount : VfsUtil.getCommonAncestors(roots.toArray(VirtualFile.EMPTY_ARRAY))) {
        String localPath = rootToMount.getPath();
        String remotePath = myData.getRemotePath(localPath);
        dockerCommandLine.withParameters(WITH_VOLUME, localPath + ':' + remotePath);
      }
    }

    // required by coverage, probably we should have a getter for this; Also contains a temp path
    String localSystemPath = PathManager.getSystemPath();
    dockerCommandLine.withParameters(WITH_VOLUME, localSystemPath + ':' + myData.getRemotePath(localSystemPath));

    // mapping ports
    commandLine.getPortMappings().forEach(
      it -> dockerCommandLine.withParameters(EXPOSE_PORT,
                                             String.valueOf(it.getRemote()),
                                             PUBLISH_PORT, it.getLocal() + ":" + it.getRemote()));

    // we sure that command script is under system dir
    File script = createCommandScript(commandLine);
    String dockerScriptPath = myData.getRemotePath(script.getPath());
    return dockerCommandLine.withParameters(myData.getImageName(), "sh", dockerScriptPath).createProcess();
  }


  @NotNull
  private File createCommandScript(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    StringBuilder sb = new StringBuilder();
    commandLine.getEnvironment().forEach((key, val) -> sb.append("export ").append(key).append('=').append(val).append("\n"));
    sb.append(commandLine.getCommandLineString());

    try {
      String command = sb.toString();
      LOG.info("Executing in " + myData.getImageName());
      StringUtil.split(command, "\n").forEach(it -> LOG.info("    " + it));
      return ExecUtil.createTempExecutableScript("dockerWrapper", "", command);
    }
    catch (IOException e) {
      throw new ExecutionException(e);
    }
  }
}
