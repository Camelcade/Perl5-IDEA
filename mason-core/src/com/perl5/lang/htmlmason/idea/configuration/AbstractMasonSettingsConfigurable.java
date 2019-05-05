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
import java.util.regex.Pattern;


public abstract class AbstractMasonSettingsConfigurable implements Configurable {
  protected static final Pattern VARIABLE_CHECK_PATTERN = Pattern.compile(
    "[$@%]" + PerlParserUtil.IDENTIFIER_PATTERN
  );

  protected final Project myProject;
  protected final String windowTitile;

  protected ListTableModel<VariableDescription> globalsModel;
  protected JBTable globalsTable;

  public AbstractMasonSettingsConfigurable(Project myProject, String windowTitile) {
    this.myProject = myProject;
    this.windowTitile = windowTitile;
  }

  @Nls
  @Override
  public String getDisplayName() {
    return windowTitile;
  }

  @Nullable
  @Override
  public String getHelpTopic() {
    return null;
  }

  public void createGlobalsComponent(FormBuilder builder) {
    globalsModel = new ListTableModel<>(
      new myVariableNameColumnInfo(),
      new myVariableTypeColumnInfo()
    );
    globalsTable = new JBTable(globalsModel);

    builder.addLabeledComponent(new JLabel("Components global variables (allow_globals option):"), ToolbarDecorator
      .createDecorator(globalsTable)
      .setAddAction(anActionButton -> {
        final TableCellEditor cellEditor = globalsTable.getCellEditor();
        if (cellEditor != null) {
          cellEditor.stopCellEditing();
        }
        final TableModel model = globalsTable.getModel();

        int indexToEdit = -1;

        for (VariableDescription variableDescription : globalsModel.getItems()) {
          if (StringUtil.isEmpty(variableDescription.variableName)) {
            indexToEdit = globalsModel.indexOf(variableDescription);
            break;
          }
        }

        if (indexToEdit == -1) {
          globalsModel.addRow(new VariableDescription());
          indexToEdit = model.getRowCount() - 1;
        }

        TableUtil.editCellAt(globalsTable, indexToEdit, 0);
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

  public static abstract class myStringColumnInfo extends ColumnInfo<VariableDescription, String> {
    public myStringColumnInfo(String name) {
      super(name);
    }

    @Override
    public boolean isCellEditable(VariableDescription variableDescription) {
      return true;
    }
  }

  public class myVariableNameColumnInfo extends myStringColumnInfo {
    public myVariableNameColumnInfo() {
      super("Variable name");
    }

    @Nullable
    @Override
    public String valueOf(VariableDescription variableDescription) {
      return variableDescription.variableName;
    }

    @Override
    public void setValue(VariableDescription variableDescription, String value) {
      if (StringUtil.isNotEmpty(value) && !containsVariableName(value)) {
        if (VARIABLE_CHECK_PATTERN.matcher(value).matches()) {
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

    protected boolean containsVariableName(String variableName) {
      for (VariableDescription variableDescription : globalsModel.getItems()) {
        if (variableName.equals(variableDescription.variableName)) {
          return true;
        }
      }
      return false;
    }
  }

  public class myVariableTypeColumnInfo extends myStringColumnInfo {
    public myVariableTypeColumnInfo() {
      super("Variable type");
    }

    @Nullable
    @Override
    public String valueOf(VariableDescription variableDescription) {
      return variableDescription.variableType;
    }

    @Override
    public boolean isCellEditable(VariableDescription variableDescription) {
      return StringUtil.isNotEmpty(variableDescription.variableName) && variableDescription.variableName.charAt(0) == '$';
    }

    @Override
    public void setValue(VariableDescription variableDescription, String value) {
      if (StringUtil.isNotEmpty(value)) {
        if (PerlParserUtil.AMBIGUOUS_PACKAGE_PATTERN.matcher(value).matches()) {
          variableDescription.variableType = value;
        }
        else {
          Messages.showErrorDialog("Incorrect package name: " + value, "Incorrect Package Name");
        }
      }
    }
  }
}
