/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.DocumentAdapter;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.run.PerlConfigurationEditorBase;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugOptionsSets;
import org.jdesktop.swingx.combobox.MapComboBoxModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;
import java.util.Objects;

import static com.perl5.lang.perl.idea.run.debugger.PerlDebugProfileState.*;


class PerlRemoteDebuggingConfigurationEditor extends PerlConfigurationEditorBase<PerlRemoteDebuggingConfiguration> {


  private JTextField myWorkingDirectoryComponent;
  private ComboBox myPerlRole;
  private JTextField myDebuggingHost;
  private JFormattedTextField myDebuggingPort;
  private JTextField myGeneratedCommandLine;
  private JCheckBox myIsReconnectCheckbox;

  public PerlRemoteDebuggingConfigurationEditor(Project project) {
    super(project);
  }

  @Override
  protected void resetEditorFrom(@NotNull PerlRemoteDebuggingConfiguration perlConfiguration) {
    myWorkingDirectoryComponent.setText(perlConfiguration.getRemoteProjectRoot());
    myPerlRole.setSelectedItem(perlConfiguration.getPerlRole());
    myDebuggingHost.setText(perlConfiguration.getDebugHost());
    myDebuggingPort.setText(String.valueOf(perlConfiguration.getDebugPort()));
    myIsReconnectCheckbox.setSelected(perlConfiguration.isReconnect());
    updateCommandLine();
    super.resetEditorFrom(perlConfiguration);
  }

  @Override
  protected void applyEditorTo(@NotNull PerlRemoteDebuggingConfiguration perlConfiguration) throws ConfigurationException {
    perlConfiguration.setRemoteProjectRoot(myWorkingDirectoryComponent.getText());
    Object selectedItem = myPerlRole.getSelectedItem();
    assert selectedItem != null;
    perlConfiguration.setPerlRole(selectedItem.toString());
    perlConfiguration.setDebugHost(myDebuggingHost.getText());
    String debuggingPort = myDebuggingPort.getText();
    if (StringUtil.isNotEmpty(debuggingPort)) {
      perlConfiguration.setDebugPort(Integer.parseInt(myDebuggingPort.getText()));
    }
    perlConfiguration.setReconnect(myIsReconnectCheckbox.isSelected());
    super.applyEditorTo(perlConfiguration);
  }

  @Nullable
  @Override
  protected JComponent getDebuggingComponent() {
    JComponent debugPanel = super.getDebuggingComponent();

    if (debugPanel == null) {
      return null;
    }

    myWorkingDirectoryComponent = new JTextField();
    LabeledComponent<JTextField> workingDirectory =
      LabeledComponent.create(myWorkingDirectoryComponent, PerlBundle.message("perl.run.option.remote.root"));
    workingDirectory.setLabelLocation(BorderLayout.WEST);
    debugPanel.add(workingDirectory);

    //noinspection Since15
    myPerlRole = new ComboBox(new MapComboBoxModel<>(PerlDebugOptionsSets.ROLE_OPTIONS)) {
      @Override
      public void setRenderer(ListCellRenderer renderer) {
        super.setRenderer(new ColoredListCellRenderer<String>() {
          @Override
          protected void customizeCellRenderer(@NotNull JList list, String value, int index, boolean selected, boolean hasFocus) {
            append(PerlDebugOptionsSets.ROLE_OPTIONS.get(value));
          }
        });
      }
    };
    myPerlRole.addActionListener(e -> updateCommandLine());

    LabeledComponent<?> perlRole = LabeledComponent.create(myPerlRole, PerlBundle.message("perl.run.option.debugger.connection.mode"));
    perlRole.setLabelLocation(BorderLayout.WEST);
    debugPanel.add(perlRole);

    DocumentAdapter documentAdapter = new DocumentAdapter() {
      @Override
      protected void textChanged(@NotNull DocumentEvent e) {
        updateCommandLine();
      }
    };

    myDebuggingHost = new JTextField();
    myDebuggingHost.getDocument().addDocumentListener(documentAdapter);
    LabeledComponent<JTextField> debuggingHost =
      LabeledComponent.create(myDebuggingHost, PerlBundle.message("perl.run.option.debugger.host"));
    debuggingHost.setLabelLocation(BorderLayout.WEST);
    debugPanel.add(debuggingHost);

    NumberFormat numberFormat = NumberFormat.getInstance();
    numberFormat.setMaximumIntegerDigits(6);
    numberFormat.setGroupingUsed(false);

    NumberFormatter formatter = new NumberFormatter(numberFormat);
    formatter.setAllowsInvalid(false);
    formatter.setMaximum(65535);
    formatter.setMinimum(0);

    myDebuggingPort = new JFormattedTextField(formatter);
    myDebuggingPort.getDocument().addDocumentListener(documentAdapter);
    LabeledComponent<JFormattedTextField> debuggingPort =
      LabeledComponent.create(myDebuggingPort, PerlBundle.message("perl.run.option.debugger.port"));
    debuggingPort.setLabelLocation(BorderLayout.WEST);
    debugPanel.add(debuggingPort);

    myIsReconnectCheckbox = new JCheckBox(PerlBundle.message("perl.run.option.debugger.reconnect"));
    debugPanel.add(myIsReconnectCheckbox);

    myGeneratedCommandLine = new JTextField();
    myGeneratedCommandLine.setEditable(false);
    myGeneratedCommandLine.setAlignmentX(JTextField.LEFT);
    myGeneratedCommandLine.addFocusListener(new FocusAdapter() {
      @Override
      public void focusGained(FocusEvent e) {
        myGeneratedCommandLine.selectAll();
      }
    });
    LabeledComponent<JTextField> labeledComponent =
      LabeledComponent.create(myGeneratedCommandLine, PerlBundle.message("perl.run.option.debugger.command.line"));
    labeledComponent.setLabelLocation(BorderLayout.WEST);
    debugPanel.add(labeledComponent);

    return debugPanel;
  }

  /**
   * Generates and sets command line according to selected settings
   */
  private void updateCommandLine() {
    String sb = PERL5_DEBUG_HOST + "=" + myDebuggingHost.getText() +
                " " + PERL5_DEBUG_PORT + "=" + myDebuggingPort.getText() +
                " " + PERL5_DEBUG_ROLE + "=" + Objects.requireNonNull(myPerlRole.getSelectedItem()).toString() +
                " perl " + DEBUG_ARGUMENT + " ./your_script.pl";
    myGeneratedCommandLine.setText(sb);
  }
}
