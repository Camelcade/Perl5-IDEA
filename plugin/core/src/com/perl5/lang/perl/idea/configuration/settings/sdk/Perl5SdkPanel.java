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

package com.perl5.lang.perl.idea.configuration.settings.sdk;

import com.intellij.icons.AllIcons;
import com.intellij.ide.HelpTooltip;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5SdkWrapper;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

import static java.awt.BorderLayout.WEST;
import static java.awt.FlowLayout.CENTER;

public class Perl5SdkPanel extends LabeledComponent<JPanel> {
  private final DefaultActionGroup myActionGroup = new DefaultActionGroup();
  private JPanel myToolBar;
  private final ComboBox<Perl5SdkWrapper> mySdkComboBox = new ComboBox<>();

  /**
   * popup taken from {@link com.intellij.application.options.schemes.AbstractSchemesPanel}
   */
  public Perl5SdkPanel() {
    DefaultActionGroup toolbarActionGroup = new DefaultActionGroup();
    ActionToolbarImpl toolbar =
      (ActionToolbarImpl)ActionManager.getInstance().createActionToolbar(ActionPlaces.NAVIGATION_BAR_TOOLBAR, toolbarActionGroup, true);
    toolbar.setReservePlaceAutoPopupIcon(false);
    toolbar.setLayoutPolicy(ActionToolbar.NOWRAP_LAYOUT_POLICY);
    toolbar.getComponent().setBorder(JBUI.Borders.empty());

    toolbarActionGroup.add(new DumbAwareAction() {
      @Override
      public void update(@NotNull AnActionEvent e) {
        Presentation p = e.getPresentation();
        p.setIcon(AllIcons.General.GearPlain);
        p.setVisible(true);
        p.setEnabled(toolbar.isEnabled());
      }

      @Override
      public void actionPerformed(@NotNull AnActionEvent e) {
        ListPopup actionGroupPopup = JBPopupFactory.getInstance().
          createActionGroupPopup(null, myActionGroup, e.getDataContext(), true, null, Integer.MAX_VALUE);

        HelpTooltip.setMasterPopup(e.getInputEvent().getComponent(), actionGroupPopup);
        actionGroupPopup.show(new RelativePoint(toolbar, getPopupPoint()));
      }

      private Point getPopupPoint() {
        int dH = UIUtil.isUnderWin10LookAndFeel() ? JBUI.scale(1) : 0;
        return new Point(JBUI.scale(2), toolbar.getHeight() - dH);
      }
    });
    myToolBar = toolbar;

    setText(PerlBundle.message("perl.config.interpreter.label"));
    setLabelLocation(WEST);
    JPanel comboboxAndToolbarPanel = new JPanel(new BorderLayout());
    comboboxAndToolbarPanel.add(mySdkComboBox, BorderLayout.CENTER);
    JPanel toolbarPanel = new JPanel(new FlowLayout(CENTER, 0, 4));
    toolbarPanel.add(myToolBar);
    comboboxAndToolbarPanel.add(toolbarPanel, BorderLayout.EAST);
    setComponent(comboboxAndToolbarPanel);
    getLabel().setLabelFor(mySdkComboBox);

    //add(toolbarPanel, BorderLayout.EAST);
  }

  public ComboBox<Perl5SdkWrapper> getSdkComboBox() {
    return mySdkComboBox;
  }

  @NotNull
  public DefaultActionGroup getActionGroup() {
    return myActionGroup;
  }

  public void setEnabled(boolean isEnabled) {
    super.setEnabled(isEnabled);
    mySdkComboBox.setEnabled(isEnabled);
    myToolBar.setEnabled(isEnabled);
  }
}
