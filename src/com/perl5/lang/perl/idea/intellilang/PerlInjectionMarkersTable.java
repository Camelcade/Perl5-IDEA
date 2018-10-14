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

package com.perl5.lang.perl.idea.intellilang;

import com.intellij.lang.Language;
import com.intellij.lang.html.HTMLLanguage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBoxTableRenderer;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.TableUtil;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ArrayUtil;
import com.intellij.util.FileContentUtil;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.ListTableModel;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.EventObject;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class PerlInjectionMarkersTable extends JBTable implements Configurable {
  @NotNull
  private final Project myProject;

  public PerlInjectionMarkersTable(@NotNull Project project) {
    super(new MyModel());
    myProject = project;

    final TableColumn languageColumn = getColumnModel().getColumn(1);

    List<String> languageIds = Language.getRegisteredLanguages().stream()
      .map(Language::getID)
      .filter(it -> !it.isEmpty())
      .sorted()
      .collect(Collectors.toList());
    ComboBoxTableRenderer<String> roleComboBoxTableRenderer =
      new ComboBoxTableRenderer<String>(ArrayUtil.toStringArray(languageIds)) {
        @Override
        public boolean isCellEditable(EventObject event) {
          if (event instanceof MouseEvent) {
            return ((MouseEvent)event).getClickCount() >= 1;
          }

          return true;
        }
      };
    languageColumn.setCellRenderer(roleComboBoxTableRenderer);
    languageColumn.setCellEditor(roleComboBoxTableRenderer);
    getColumnModel().getColumn(0).setPreferredWidth(200);
    languageColumn.setPreferredWidth(600);
  }

  @Nls(capitalization = Nls.Capitalization.Title)
  @Override
  public String getDisplayName() {
    return PerlBundle.message("perl.settings.markers.label.text");
  }

  @NotNull
  @Override
  public JComponent createComponent() {
    JTextPane textPane = new JTextPane();
    textPane.setText(PerlBundle.message("perl.settings.markers.language.description"));
    JPanel panel = FormBuilder.createFormBuilder()
      .addComponent(textPane)
      .addComponent(
        ToolbarDecorator.createDecorator(this).setAddAction(action -> {
          final TableCellEditor cellEditor = getCellEditor();
          if (cellEditor != null) {
            cellEditor.stopCellEditing();
          }
          final MyModel model = getModel();

          int indexToEdit = -1;

          for (Item item : model.getItems()) {
            if (StringUtil.isEmpty(item.marker)) {
              indexToEdit = model.indexOf(item);
              break;
            }
          }

          if (indexToEdit == -1) {
            model.addRow(new Item("marker" + model.getItems().size(), HTMLLanguage.INSTANCE.getID()));
            indexToEdit = model.getRowCount() - 1;
          }

          TableUtil.editCellAt(this, indexToEdit, 0);
        })
          .disableDownAction()
          .disableUpAction()
          .createPanel()
      ).getPanel();
    panel.setLayout(new VerticalFlowLayout());
    return panel;
  }

  @Override
  public MyModel getModel() {
    return (MyModel)super.getModel();
  }

  @Override
  public boolean isModified() {
    return !ContainerUtil.newHashSet(computeItems()).equals(ContainerUtil.newHashSet(getModel().getItems()));
  }

  private List<Item> computeItems() {
    return PerlInjectionMarkersService.getInstance(myProject).computeMergedMarkersMap().entrySet().stream()
      .filter(it -> Language.findLanguageByID(it.getValue()) != null)
      .map(it -> new Item(it.getKey(), it.getValue()))
      .collect(Collectors.toList());
  }

  @Override
  public void reset() {
    getModel().setItems(computeItems());
  }

  @Override
  public void apply() throws ConfigurationException {
    if (!isModified()) {
      return;
    }

    Map<String, String> result = ContainerUtil.newHashMap();
    getModel().getItems().forEach(item -> result.putIfAbsent(item.marker, item.languageId));
    PerlInjectionMarkersService.getInstance(myProject).setCustomMarkersMap(result);
    FileContentUtil.reparseOpenedFiles();
  }

  private static class Item {
    @NotNull
    String marker;
    @NotNull
    String languageId;

    public Item(@NotNull Map.Entry<String, String> entry) {
      this(entry.getKey(), entry.getValue());
    }

    public Item(@NotNull String marker, @NotNull String languageId) {
      this.marker = marker;
      this.languageId = languageId;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Item item = (Item)o;

      if (!marker.equals(item.marker)) {
        return false;
      }
      return languageId.equals(item.languageId);
    }

    @Override
    public int hashCode() {
      int result = marker.hashCode();
      result = 31 * result + languageId.hashCode();
      return result;
    }
  }

  private static class MyModel extends ListTableModel<Item> {
    public MyModel() {
      super(new MarkerColumnInfo(), new LanguageColumnInfo());
      setSortable(true);
    }
  }

  private static class MarkerColumnInfo extends ColumnInfo<Item, String> {
    public MarkerColumnInfo() {
      super(PerlBundle.message("perl.settings.markers.column.marker.title"));
    }

    @Nullable
    @Override
    public Comparator<Item> getComparator() {
      return Comparator.comparing(it -> it.marker);
    }

    @Override
    public boolean isCellEditable(Item item) {
      return true;
    }

    @Nullable
    @Override
    public String valueOf(Item item) {
      return item.marker;
    }

    @Override
    public void setValue(Item item, String value) {
      item.marker = value;
    }
  }

  private static class LanguageColumnInfo extends ColumnInfo<Item, String> {
    public LanguageColumnInfo() {
      super(PerlBundle.message("perl.settings.markers.column.language.title"));
    }

    @Override
    public boolean isCellEditable(Item item) {
      return true;
    }

    @Nullable
    @Override
    public Comparator<Item> getComparator() {
      return Comparator.comparing(this::valueOf);
    }

    @Override
    public void setValue(Item item, String value) {
      item.languageId = value;
    }

    @Nullable
    @Override
    public String valueOf(Item item) {
      Language language = Language.findLanguageByID(item.languageId);
      return language == null ?
             PerlBundle.message("perl.settings.markers.language.unsupported", item.languageId) :
             language.getDisplayName();
    }
  }
}
