/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.debugger;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.TableUtil;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.ListTableModel;
import com.intellij.util.xmlb.annotations.Attribute;
import com.intellij.util.xmlb.annotations.Tag;
import com.intellij.xdebugger.settings.DebuggerSettingsCategory;
import com.intellij.xdebugger.settings.XDebuggerSettings;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.util.*;

public class PerlDebuggerSettings extends XDebuggerSettings<PerlDebuggerSettings> {
  @Tag("dataRenderers")
  private List<Item> myDataRenderers = new ArrayList<>();

  public PerlDebuggerSettings() {
    super("perl5");
  }

  @Override
  public @NotNull Collection<? extends Configurable> createConfigurables(@NotNull DebuggerSettingsCategory category) {
    if (category == DebuggerSettingsCategory.DATA_VIEWS) {
      return Collections.singletonList(new DataViewsConfigurable());
    }
    return super.createConfigurables(category);
  }

  @Override
  public @Nullable PerlDebuggerSettings getState() {
    return this;
  }

  public List<Item> getDataRenderers() {
    return new ArrayList<>(ContainerUtil.map(myDataRenderers, Item::new));
  }

  @Override
  public void loadState(@NotNull PerlDebuggerSettings state) {
    myDataRenderers = new ArrayList<>(ContainerUtil.filter(state.myDataRenderers, Item::isValid));
  }

  @TestOnly
  public void setDataRenderers(@NotNull List<? extends Item> renderers) {
    myDataRenderers = new ArrayList<>(renderers);
  }

  public static @NotNull PerlDebuggerSettings getInstance() {
    return getInstance(PerlDebuggerSettings.class);
  }

  @Tag("entry")
  public static class Item {
    @Attribute("package")
    String namespaceName;
    @Attribute("expression")
    String renderExpression;

    @SuppressWarnings("unused")
    public Item() {
    }

    public Item(@NotNull String namespaceName, @NotNull String renderExpression) {
      this.namespaceName = namespaceName;
      this.renderExpression = renderExpression;
    }

    public Item(Item copy) {
      this.namespaceName = copy.namespaceName;
      this.renderExpression = copy.renderExpression;
    }

    public boolean isValid() {
      return StringUtil.isNotEmpty(namespaceName) && StringUtil.isNotEmpty(renderExpression);
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

      if (!Objects.equals(namespaceName, item.namespaceName)) {
        return false;
      }
      return Objects.equals(renderExpression, item.renderExpression);
    }

    @Override
    public int hashCode() {
      int result = namespaceName != null ? namespaceName.hashCode() : 0;
      result = 31 * result + (renderExpression != null ? renderExpression.hashCode() : 0);
      return result;
    }
  }

  private static class MyModel extends ListTableModel<Item> {
    public MyModel() {
      super(new ClassColumnInfo(), new RendererColumnInfo());
      setSortable(true);
    }
  }

  private static class ClassColumnInfo extends ColumnInfo<Item, String> {
    public ClassColumnInfo() {
      super(PerlDebuggerBundle.message("perl.debugger.settings.class.column.title"));
    }

    @Override
    public @Nullable String valueOf(Item item) {
      return item.namespaceName;
    }

    @Override
    public void setValue(Item item, String value) {
      item.namespaceName = value;
    }

    @Override
    public boolean isCellEditable(Item item) {
      return true;
    }
  }

  private static class RendererColumnInfo extends ColumnInfo<Item, String> {
    @SuppressWarnings("DialogTitleCapitalization")
    public RendererColumnInfo() {
      super(PerlDebuggerBundle.message("perl.debugger.settings.renderer.column.title"));
    }

    @Override
    public @Nullable String valueOf(Item entry) {
      return entry.renderExpression;
    }

    @Override
    public void setValue(Item item, String value) {
      item.renderExpression = value;
    }

    @Override
    public boolean isCellEditable(Item item) {
      return true;
    }
  }

  private class DataViewsConfigurable extends JBTable implements SearchableConfigurable {
    public DataViewsConfigurable() {
      super(new MyModel());
    }

    @Override
    public @NotNull String getId() {
      return "perl5.debugger.type.renderers";
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
      return PerlDebuggerBundle.message("perl.debugger.settings.type.renderers.title");
    }

    @Override
    public @Nullable JComponent createComponent() {
      JTextPane textPane = new JTextPane();
      textPane.setText(PerlDebuggerBundle.message("perl.debugger.settings.type.renderers.explanation"));
      JPanel panel = FormBuilder.createFormBuilder()
        .addComponent(textPane)
        .addComponent(ToolbarDecorator.createDecorator(this).setAddAction(action -> {
          final TableCellEditor cellEditor = getCellEditor();
          if (cellEditor != null) {
            cellEditor.stopCellEditing();
          }
          MyModel model = getModel();

          int indexToEdit = -1;

          for (Item item : model.getItems()) {
            if (StringUtil.isEmpty(item.namespaceName)) {
              indexToEdit = model.indexOf(item);
              break;
            }
          }

          if (indexToEdit == -1) {
            model.addRow(new Item("Foo::Bar", "$it->to_string()"));
            indexToEdit = model.getRowCount() - 1;
          }

          TableUtil.editCellAt(this, indexToEdit, 0);
        }).createPanel())
        .getPanel();
      panel.setLayout(new VerticalFlowLayout());

      getColumnModel().getColumn(0).setPreferredWidth(200);
      getColumnModel().getColumn(1).setPreferredWidth(600);

      return panel;
    }

    @Override
    public boolean isModified() {
      return !myDataRenderers.equals(getModelState());
    }

    @Override
    public void apply() {
      myDataRenderers = new ArrayList<>(ContainerUtil.map(getModelState(), Item::new));
    }

    private List<Item> getModelState() {
      return new ArrayList<>(ContainerUtil.filter(getModel().getItems(), Item::isValid));
    }

    @Override
    public MyModel getModel() {
      return (MyModel)super.getModel();
    }

    @Override
    public void reset() {
      getModel().setItems(getDataRenderers());
    }
  }
}
