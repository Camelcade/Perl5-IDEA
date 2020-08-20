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

package com.perl5.lang.perl.profiler.configuration;

import com.intellij.ui.EnumComboBoxModel;
import com.intellij.ui.SimpleListCellRenderer;
import com.intellij.util.ui.FormBuilder;
import com.perl5.lang.perl.profiler.run.PerlProfilerStartupMode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class PerlProfilerConfigurationEditor {
  final @NotNull JComboBox<PerlProfilerStartupMode> myStartModeComboBox = new JComboBox<>();
  final @NotNull JCheckBox myOptimizerCheckBox = new JCheckBox();
  final @NotNull JPanel myPanel = new JPanel(new BorderLayout());

  public PerlProfilerConfigurationEditor() {
    var formBuilder = new FormBuilder();
    myStartModeComboBox.setModel(new EnumComboBoxModel<>(PerlProfilerStartupMode.class));
    myStartModeComboBox.setRenderer(new SimpleListCellRenderer<>() {
      @Override
      public void customize(@NotNull JList<? extends PerlProfilerStartupMode> list,
                            PerlProfilerStartupMode value,
                            int index,
                            boolean selected,
                            boolean hasFocus) {
        setText(value.getExplanation());
      }
    });
    formBuilder.addLabeledComponent("Profiler startup mode:", myStartModeComboBox);
    formBuilder.addLabeledComponent("Disable perl optimizer:", myOptimizerCheckBox);
    myPanel.add(formBuilder.getPanel(), BorderLayout.NORTH);
  }
}
