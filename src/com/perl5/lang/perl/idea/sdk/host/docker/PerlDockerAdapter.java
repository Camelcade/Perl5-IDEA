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
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.idea.sdk.host.PerlExecutionException;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.util.PerlPluginUtil;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adapter running command using docker cli
 */
class PerlDockerAdapter {
  private static final Logger LOG = Logger.getInstance(PerlDockerAdapter.class);
  @NotNull
  private final PerlDockerData myData;

  public PerlDockerAdapter(@NotNull PerlDockerData data) {
    myData = data;
  }

  private void createContainer(@NotNull String containerName) throws ExecutionException {
    checkOutput(PerlHostData.execAndGetOutput(
      baseCommandLine().withParameters("container", "create", "--name", containerName, myData.getImageName())));
  }

  private void dropContainer(@NotNull String containerName) throws ExecutionException {
    checkOutput(PerlHostData.execAndGetOutput(baseCommandLine().withParameters("container", "rm", containerName)));
  }

  @NotNull
  private ProcessOutput checkOutput(@NotNull ProcessOutput output) throws ExecutionException {
    if (output.getExitCode() != 0) {
      throw new PerlExecutionException(output);
    }
    return output;
  }

  public void copyRemote(@NotNull String remotePath, @NotNull String localPath) throws ExecutionException {
    String containerName = "copying_" + myData.getSafeImageName() + Math.random();
    createContainer(containerName);

    try {
      File localPathFile = new File(localPath);
      FileUtil.createDirectory(localPathFile);
      checkOutput(PerlHostData.execAndGetOutput(
        baseCommandLine().withParameters("cp", "--archive", containerName + ':' + remotePath, localPathFile.getParent()) // "--follow-link"
      ));
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

  private PerlCommandLine baseCommandLine() {
    return new PerlCommandLine("docker").withHostData(PerlHostHandler.getDefaultHandler().createData());
  }

  /**
   * @return list of images in {@code name[:tag]} format
   */
  List<String> listImages() throws ExecutionException {
    ProcessOutput output =
      checkOutput(PerlHostData.execAndGetOutput(baseCommandLine().withParameters("image", "ls", "--format", "{{.Repository}}:{{.Tag}}")));
    return output.getStdoutLines().stream().map(it -> StringUtil.replace(it, ":<none>", "")).sorted().collect(Collectors.toList());
  }

  /**
   * Wrapping a {@code commandLine} to a script and runs it using {@code docker} command, returning it's process
   */
  public Process createProcess(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    File script = createCommandScript(commandLine);
    PerlCommandLine dockerCommandLine = baseCommandLine()
      .withParameters("run", "--rm", "-i")
      .withParameters("-a", "stdout", "-a", "stderr", "-a", "stdin")
      .withCharset(commandLine.getCharset());
    if (commandLine.isUsePty()) {
      dockerCommandLine.withParameters("-t");
      dockerCommandLine.withPty(true);
    }

    // mounting script
    String dockerScriptPath = PerlDockerData.CONTAINER_ROOT + '/' + script.getName();
    dockerCommandLine.withParameters("-v=" + script.getPath() + ':' + dockerScriptPath + ":ro");

    // mounting helpers
    dockerCommandLine.withParameters("-v=" + PerlPluginUtil.getPluginHelpersRoot() + ':' + myData.getHelpersRootPath() + ":ro");

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
        dockerCommandLine.withParameters("-v=" + localPath + ':' + remotePath + ":rw");
      }
    }

    // required by coverage, probably we should have a getter for this
    String localSystemPath = PathManager.getSystemPath();
    dockerCommandLine.withParameters("-v=" + localSystemPath + ':' + myData.getRemotePath(localSystemPath) + ":rw");

    // mapping ports
    commandLine.getPortMappings().forEach(
      it -> dockerCommandLine.withParameters("--expose=" + it.getRemote(), "-p=" + it.getLocal() + ":" + it.getRemote()));

    return dockerCommandLine.withParameters(myData.getImageName(), "sh", dockerScriptPath).createProcess();
  }


  @NotNull
  private File createCommandScript(@NotNull PerlCommandLine commandLine) throws ExecutionException {
    StringBuilder sb = new StringBuilder();
    commandLine.getEnvironment().forEach((key, val) -> {
      sb.append("export ").append(key).append('=').append(val).append("\n");
    });
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
