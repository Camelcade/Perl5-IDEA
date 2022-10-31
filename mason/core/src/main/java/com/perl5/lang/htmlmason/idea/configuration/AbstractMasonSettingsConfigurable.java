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

package com.perl5.lang.htmlmason.idea.configuration;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.TableUtil;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.ListTableModel;
import com.perl5.lang.mason2.idea.configuration.VariableDescription;
import com.perl5.lang.perl.parser.PerlParserUtil;
import com.perl5.lang.perl.util.PerlConfigurationUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;


public abstract class AbstractMasonSettingsConfigurable implements Configurable {

  protected final Project myProject;
  protected final String myWindowTitle;

  protected ListTableModel<VariableDescription> myGlobalsModel;
  protected JBTable myGlobalsTable;

  public AbstractMasonSettingsConfigurable(Project project, String windowTitle) {
    myProject = project;
    myWindowTitle = windowTitle;
  }

  @Override
  public @Nls String getDisplayName() {
    return myWindowTitle;
  }

  @Override
  public @Nullable String getHelpTopic() {
    return null;
  }

  public void createGlobalsComponent(FormBuilder builder) {
    myGlobalsModel = new ListTableModel<>(
      new MyVariableNameColumnInfo(),
      new MyVariableTypeColumnInfo()
    );
    myGlobalsTable = new JBTable(myGlobalsModel);

    builder.addLabeledComponent(new JLabel("Components global variables (allow_globals option):"), ToolbarDecorator
      .createDecorator(myGlobalsTable)
      .setAddAction(anActionButton -> {
        final TableCellEditor cellEditor = myGlobalsTable.getCellEditor();
        if (cellEditor != null) {
          cellEditor.stopCellEditing();
        }
        final TableModel model = myGlobalsTable.getModel();

        int indexToEdit = -1;

        for (VariableDescription variableDescription : myGlobalsModel.getItems()) {
          if (StringUtil.isEmpty(variableDescription.variableName)) {
            indexToEdit = myGlobalsModel.indexOf(variableDescription);
            break;
          }
        }

        if (indexToEdit == -1) {
          myGlobalsModel.addRow(new VariableDescription());
          indexToEdit = model.getRowCount() - 1;
        }

        TableUtil.editCellAt(myGlobalsTable, indexToEdit, 0);
      })
      .disableDownAction()
      .disableUpAction()
      .setPreferredSize(JBUI.size(0, PerlConfigurationUtil.WIDGET_HEIGHT))
      .createPanel()
    )
    ;
  }

  @Override
  public void disposeUIResources() {
  }

  private abstract static class MyStringColumnInfo extends ColumnInfo<VariableDescription, String> {
    public MyStringColumnInfo(String name) {
      super(name);
    }

    @Override
    public boolean isCellEditable(VariableDescription variableDescription) {
      return true;
    }
  }

  private final class MyVariableNameColumnInfo extends MyStringColumnInfo {
    public MyVariableNameColumnInfo() {
      super("Variable name");
    }

    @Override
    public @Nullable String valueOf(VariableDescription variableDescription) {
      return variableDescription.variableName;
    }

    @Override
    public void setValue(VariableDescription variableDescription, String value) {
      if (StringUtil.isNotEmpty(value) && !containsVariableName(value)) {
        if (PerlParserUtil.isVariableWithSigil(value)) {
          variableDescription.variableName = value;
          if (value.charAt(0) != '$') {
            variableDescription.variableType = "";
          }
        }
        else {
          Messages.showErrorDialog("Incorrect variable name: " + value, "Incorrect Variable Name");
        }
      }
    }

    private boolean containsVariableName(String variableName) {
      for (VariableDescription variableDescription : myGlobalsModel.getItems()) {
        if (variableName.equals(variableDescription.variableName)) {
          return true;
        }
      }
      return false;
    }
  }

  private static final class MyVariableTypeColumnInfo extends MyStringColumnInfo {
    public MyVariableTypeColumnInfo() {
      super("Variable type");
    }

    @Override
    public @Nullable String valueOf(VariableDescription variableDescription) {
      return variableDescription.variableType;
    }

    @Override
    public boolean isCellEditable(VariableDescription variableDescription) {
      return StringUtil.isNotEmpty(variableDescription.variableName) && variableDescription.variableName.charAt(0) == '$';
    }

    @Override
    public void setValue(VariableDescription variableDescription, String value) {
      if (StringUtil.isNotEmpty(value)) {
        if (PerlParserUtil.isAmbiguousPackage(value)) {
          variableDescription.variableType = value;
        }
        else {
          Messages.showErrorDialog("Incorrect package name: " + value, "Incorrect Package Name");
        }
      }
    }
  }
}
