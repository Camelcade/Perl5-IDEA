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

import com.intellij.execution.Executor;
import com.intellij.execution.actions.StopProcessAction;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.actions.CloseAction;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class PerlRunConsole extends ConsoleViewImpl {
  private final DefaultActionGroup myActionGroup = new DefaultActionGroup();
  @Nullable
  private final PerlHostData myHostData;

  public PerlRunConsole(@NotNull Project project, @Nullable PerlHostData hostData) {
    super(project, false);
    myHostData = hostData;
  }

  public JPanel buildPanel() {

    ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(getClass().getSimpleName(), myActionGroup, false);
    JPanel consolePanel = new JPanel(new BorderLayout());
    consolePanel.add(getComponent(), BorderLayout.CENTER);
    consolePanel.add(toolbar.getComponent(), BorderLayout.WEST);
    return consolePanel;
  }

  @Nullable
  public PerlHostData getHostData() {
    return myHostData;
  }

  @Override
  public void attachToProcess(@NotNull ProcessHandler processHandler) {
    super.attachToProcess(processHandler);
    addStopAction(processHandler);
  }

  private void addStopAction(@NotNull ProcessHandler processHandler) {
    StopProcessAction stopAction = new StopProcessAction("Stop", "Stop", processHandler);
    AnAction generalStopAction = ActionManager.getInstance().getAction(IdeActions.ACTION_STOP_PROGRAM);
    if (generalStopAction != null) {
      stopAction.copyFrom(generalStopAction);
      stopAction.registerCustomShortcutSet(generalStopAction.getShortcutSet(), this);
    }
    myActionGroup.add(stopAction);
  }

  public void addCloseAction(@NotNull Executor executor, @NotNull RunContentDescriptor contentDescriptor) {
    myActionGroup.add(new CloseAction(executor, contentDescriptor, getProject()));
  }
}
