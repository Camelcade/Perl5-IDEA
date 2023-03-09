/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

import com.google.common.collect.Sets;
import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.fileTypes.ex.FileTypeManagerEx;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBoxTableRenderer;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.CollectionListModel;
import com.intellij.ui.TableUtil;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.components.JBList;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.ListTableModel;
import com.perl5.lang.htmlmason.HtmlMasonBundle;
import com.perl5.lang.mason2.idea.configuration.VariableDescription;
import com.perl5.lang.perl.parser.PerlParserUtil;
import com.perl5.lang.perl.util.PerlConfigurationUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.event.MouseEvent;
import java.util.*;

import static com.perl5.lang.htmlmason.HTMLMasonSyntaxElements.BUILTIN_TAGS_COMPLEX;
import static com.perl5.lang.htmlmason.HTMLMasonSyntaxElements.BUILTIN_TAGS_SIMPLE;


public class HTMLMasonSettingsConfigurable extends AbstractMasonSettingsConfigurable {
  final HTMLMasonSettings mySettings;

  protected CollectionListModel<String> mySubstitutedExtensionsModel;
  protected JBList<String> mySubstitutedExtensionsList;
  protected JPanel mySubstitutedExtensionsPanel;

  protected JTextField myAutohandlerName;
  protected JTextField myDefaulthandlerName;

  protected ListTableModel<HTMLMasonCustomTag> myCustomTagsModel;
  protected JBTable myCustomTagsTable;

  public HTMLMasonSettingsConfigurable(Project myProject) {
    this(myProject, "HTML::Mason");
  }

  public HTMLMasonSettingsConfigurable(Project myProject, String windowTitile) {
    super(myProject, windowTitile);
    mySettings = HTMLMasonSettings.getInstance(myProject);
  }

  @Override
  public @Nullable JComponent createComponent() {
    FormBuilder builder = FormBuilder.createFormBuilder();
    builder.getPanel().setLayout(new VerticalFlowLayout());

    myDefaulthandlerName = new JTextField();
    builder.addLabeledComponent(new JLabel(HtmlMasonBundle.message("default.handler.name")), myDefaulthandlerName);

    myAutohandlerName = new JTextField();
    builder.addLabeledComponent(new JLabel(HtmlMasonBundle.message("auto.handler.name")), myAutohandlerName);

    createGlobalsComponent(builder);
    createSubstitutedExtensionsComponent(builder);
    createCustomTagsComponent(builder);

    return builder.getPanel();
  }

  @Override
  public boolean isModified() {
    return
      !mySettings.globalVariables.equals(myGlobalsModel.getItems()) ||
      !mySettings.substitutedExtensions.equals(mySubstitutedExtensionsModel.getItems()) ||
      isStructureModified()
      ;
  }

  protected boolean isStructureModified() {
    return !Objects.equals(mySettings.autoHandlerName, myAutohandlerName.getText()) ||
           !Objects.equals(mySettings.defaultHandlerName, myDefaulthandlerName.getText()) ||
           !mySettings.customTags.equals(myCustomTagsModel.getItems())
      ;
  }

  @Override
  public void apply() {
    boolean forceReparse = isStructureModified();

    Set<String> extDiff = getDiff(mySettings.substitutedExtensions, mySubstitutedExtensionsModel.getItems());
    mySettings.substitutedExtensions.clear();
    mySettings.substitutedExtensions.addAll(mySubstitutedExtensionsModel.getItems());

    mySettings.autoHandlerName = myAutohandlerName.getText();
    mySettings.defaultHandlerName = myDefaulthandlerName.getText();

    mySettings.globalVariables.clear();
    for (VariableDescription variableDescription : new ArrayList<>(myGlobalsModel.getItems())) {
      if (StringUtil.isNotEmpty(variableDescription.variableName)) {
        mySettings.globalVariables.add(variableDescription);
      }
      else {
        myGlobalsModel.removeRow(myGlobalsModel.indexOf(variableDescription));
      }
    }

    mySettings.customTags.clear();
    mySettings.customTags.addAll(myCustomTagsModel.getItems());

    mySettings.settingsUpdated();

    if (!extDiff.isEmpty() || forceReparse) {
      // fixme move this logic to the settings
      WriteAction.run(() -> FileTypeManagerEx.getInstanceEx().fireFileTypesChanged());
    }
  }


  protected Set<String> getDiff(List<String> first, List<String> second) {
    Set<String> secondSet = Set.copyOf(second);
    Set<String> firstSet = Set.copyOf(first);
    return Sets.union(
      Sets.difference(firstSet, secondSet),
      Sets.difference(secondSet, firstSet)
    );
  }

  @Override
  public void reset() {
    mySubstitutedExtensionsModel.removeAll();
    mySubstitutedExtensionsModel.add(mySettings.substitutedExtensions);

    myAutohandlerName.setText(mySettings.autoHandlerName);
    myDefaulthandlerName.setText(mySettings.defaultHandlerName);

    myCustomTagsModel.setItems(new ArrayList<>());
    for (HTMLMasonCustomTag htmlMasonCustomTag : mySettings.customTags) {
      myCustomTagsModel.addRow(htmlMasonCustomTag.clone());
    }

    myGlobalsModel.setItems(new ArrayList<>());
    for (VariableDescription variableDescription : mySettings.globalVariables) {
      myGlobalsModel.addRow(variableDescription.clone());
    }
  }

  protected void createSubstitutedExtensionsComponent(FormBuilder builder) {
    mySubstitutedExtensionsModel = new CollectionListModel<>();
    mySubstitutedExtensionsList = new JBList<>(mySubstitutedExtensionsModel);
    mySubstitutedExtensionsPanel =
      PerlConfigurationUtil.createSubstituteExtensionPanel(mySubstitutedExtensionsModel, mySubstitutedExtensionsList);
    builder.addLabeledComponent(
      new JLabel(HtmlMasonBundle.message("extensions.that.should.be.handled.as.html.mason.components.except.mas.only.in.component.roots")),
      mySubstitutedExtensionsPanel);
  }

  protected void createCustomTagsComponent(FormBuilder builder) {
    MyTagNameColumnInfo tagNameColumnInfo = new MyTagNameColumnInfo();
    myCustomTagsModel = new ListTableModel<>(
      tagNameColumnInfo,
      new MyTagRoleColumInfo()
    );

    tagNameColumnInfo.setCustomTagsModel(myCustomTagsModel);

    myCustomTagsTable = new JBTable(myCustomTagsModel);

    final TableColumn secondColumn = myCustomTagsTable.getColumnModel().getColumn(1);

    ComboBoxTableRenderer<HTMLMasonCustomTagRole> roleComboBoxTableRenderer =
      new ComboBoxTableRenderer<>(HTMLMasonCustomTagRole.values()) {
        @Override
        protected @NlsSafe String getTextFor(@NotNull HTMLMasonCustomTagRole value) {
          return value.getTitle();
        }

        @Override
        public boolean isCellEditable(EventObject event) {
          if (event instanceof MouseEvent) {
            return ((MouseEvent)event).getClickCount() >= 1;
          }

          return true;
        }
      };
    secondColumn.setCellRenderer(roleComboBoxTableRenderer);
    secondColumn.setCellEditor(roleComboBoxTableRenderer);


    builder.addLabeledComponent(new JLabel(HtmlMasonBundle.message("custom.tags.that.mimics.built.in.html.mason.tags")), ToolbarDecorator
      .createDecorator(myCustomTagsTable)
      .setAddAction(anActionButton -> {
        final TableCellEditor cellEditor = myCustomTagsTable.getCellEditor();
        if (cellEditor != null) {
          cellEditor.stopCellEditing();
        }
        final TableModel model = myCustomTagsTable.getModel();

        int indexToEdit = -1;

        for (HTMLMasonCustomTag entry : myCustomTagsModel.getItems()) {
          if (StringUtil.isEmpty(entry.getText())) {
            indexToEdit = myCustomTagsModel.indexOf(entry);
            break;
          }
        }

        if (indexToEdit == -1) {
          myCustomTagsModel.addRow(new HTMLMasonCustomTag("customTag" + myCustomTagsModel.getItems().size(), HTMLMasonCustomTagRole.PERL));
          indexToEdit = model.getRowCount() - 1;
        }

        TableUtil.editCellAt(myCustomTagsTable, indexToEdit, 0);
      })
      .disableDownAction()
      .disableUpAction()
      .setPreferredSize(JBUI.size(0, PerlConfigurationUtil.WIDGET_HEIGHT))
      .createPanel()
    );
  }

  private final class MyTagNameColumnInfo extends ColumnInfo<HTMLMasonCustomTag, String> {
    private static final Set<String> BUILTIN_TAGS = new HashSet<>();

    static {
      BUILTIN_TAGS.addAll(BUILTIN_TAGS_SIMPLE);
      BUILTIN_TAGS.addAll(BUILTIN_TAGS_COMPLEX);
    }

    public MyTagNameColumnInfo() {
      super(HtmlMasonBundle.message("column.name.tag.text.without.surrounding"));
    }

    public void setCustomTagsModel(ListTableModel<HTMLMasonCustomTag> customTagsModel) {
      myCustomTagsModel = customTagsModel;
    }

    @Override
    public @Nullable String valueOf(HTMLMasonCustomTag customTag) {
      return customTag.getText();
    }

    @Override
    public boolean isCellEditable(HTMLMasonCustomTag customTag) {
      return true;
    }

    @Override
    public void setValue(HTMLMasonCustomTag customTag, String value) {
      if (!StringUtil.equals(customTag.getText(), value)) {
        if (!PerlParserUtil.isIdentifier(value)) {
          Messages.showErrorDialog(HtmlMasonBundle.message("dialog.message.tag.text.should.be.valid.identifier"),
                                   HtmlMasonBundle.message("dialog.title.incorrect.tag.text"));
        }
        else if (BUILTIN_TAGS.contains(value)) {
          Messages.showErrorDialog(HtmlMasonBundle.message("dialog.message.tag.already.defined.in.html.mason", value),
                                   HtmlMasonBundle.message("dialog.title.predefined.tag.text"));
        }
        else if (myCustomTagsModel.getItems().contains(new HTMLMasonCustomTag(value, HTMLMasonCustomTagRole.PERL))) {
          Messages.showErrorDialog(HtmlMasonBundle.message("dialog.message.tag.with.such.text.already.exists"),
                                   HtmlMasonBundle.message("dialog.title.duplicate.tag.text"));
        }
        else {
          customTag.setText(value);
        }
      }
    }
  }

  private static class MyTagRoleColumInfo extends ColumnInfo<HTMLMasonCustomTag, HTMLMasonCustomTagRole> {
    public MyTagRoleColumInfo() {
      super(HtmlMasonBundle.message("column.name.parse"));
    }

    @Override
    public @Nullable HTMLMasonCustomTagRole valueOf(HTMLMasonCustomTag customTag) {
      return customTag.getRole();
    }

    @Override
    public Class<?> getColumnClass() {
      return HTMLMasonCustomTagRole.class;
    }

    @Override
    public boolean isCellEditable(HTMLMasonCustomTag customTag) {
      return true;
    }

    @Override
    public void setValue(HTMLMasonCustomTag customTag, HTMLMasonCustomTagRole value) {
      customTag.setRole(value);
    }
  }
}
