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

/**
 * Created by hurricup on 09.05.2016.
 */
public class PerlRemoteDebuggingConfigurationEditor extends PerlConfigurationEditorBase<PerlRemoteDebuggingConfiguration> {
  private JTextField myWorkingDirectoryComponent;
  private ComboBox myPerlRole;
  private JTextField myDebuggingHost;
  private JFormattedTextField myDebuggingPort;
  private JTextField myGeneratedCommandLine;

  public PerlRemoteDebuggingConfigurationEditor(Project project) {
    super(project);
  }

  @Override
  protected void resetEditorFrom(@NotNull PerlRemoteDebuggingConfiguration perlConfiguration) {
    myWorkingDirectoryComponent.setText(perlConfiguration.getRemoteProjectRoot());
    myPerlRole.setSelectedItem(perlConfiguration.getPerlRole());
    myDebuggingHost.setText(perlConfiguration.getDebugHost());
    myDebuggingPort.setText(String.valueOf(perlConfiguration.getDebugPort()));
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
    StringBuilder sb = new StringBuilder();
    sb.append("PERL5_DEBUG_HOST=")
      .append(myDebuggingHost.getText())
      .append(" PERL5_DEBUG_PORT=")
      .append(myDebuggingPort.getText())
      .append(" PERL5_DEBUG_ROLE=")
      .append(myPerlRole.getSelectedItem().toString())
      .append(" perl -d:Camelcadedb ./your_script.pl");
    myGeneratedCommandLine.setText(sb.toString());
  }
}
