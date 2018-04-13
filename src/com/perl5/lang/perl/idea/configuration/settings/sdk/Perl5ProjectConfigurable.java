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

package com.perl5.lang.perl.idea.configuration.settings.sdk;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.util.FileContentUtil;
import com.intellij.util.ui.FormBuilder;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.actions.PerlFormatWithPerlTidyAction;
import com.perl5.lang.perl.idea.annotators.PerlCriticAnnotator;
import com.perl5.lang.perl.idea.configuration.settings.PerlLocalSettings;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5RealSdkWrapper;
import com.perl5.lang.perl.idea.configuration.settings.sdk.wrappers.Perl5SdkWrapper;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.internals.PerlVersion;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SdkConfigurable.DISABLE_PERL_ITEM;

public class Perl5ProjectConfigurable implements Configurable, Perl5SdkManipulator {
  private static final int ourRowsCount = 5;

  @NotNull
  private final Project myProject;
  private final PerlSharedSettings mySharedSettings;
  private final PerlLocalSettings myLocalSettings;

  @NotNull
  private Perl5SdkConfigurable myPerl5SdkConfigurable;

  protected JBList<VirtualFile> myLibsList;
  private CollectionListModel<VirtualFile> myLibsModel;

  private TextFieldWithBrowseButton perlCriticPathInputField;
  private RawCommandLineEditor perlCriticArgsInputField;
  private TextFieldWithBrowseButton perlTidyPathInputField;
  private RawCommandLineEditor perlTidyArgsInputField;
  private JTextField deparseArgumentsTextField;
  private JCheckBox simpleMainCheckbox;
  private JCheckBox autoInjectionCheckbox;
  private JCheckBox perlCriticCheckBox;
  private JCheckBox perlAnnotatorCheckBox;
  private JCheckBox allowInjectionWithInterpolation;
  private JCheckBox allowRegexpInjections;
  private JCheckBox enablePerlSwitchCheckbox;
  private CollectionListModel<String> selfNamesModel;
  private ComboBox<PerlVersion> myTargetPerlVersionComboBox;


  public Perl5ProjectConfigurable(@NotNull Project project) {
    myProject = project;
    mySharedSettings = PerlSharedSettings.getInstance(myProject);
    myLocalSettings = PerlLocalSettings.getInstance(myProject);
    myPerl5SdkConfigurable = new Perl5SdkConfigurable(this);
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    FormBuilder builder = FormBuilder.createFormBuilder();
    builder.getPanel().setLayout(new VerticalFlowLayout());

    builder.addComponent(myPerl5SdkConfigurable.createComponent());

    FormBuilder versionBuilder = FormBuilder.createFormBuilder();
    ComboBoxModel<PerlVersion> versionModel = new CollectionComboBoxModel<>(PerlVersion.ALL_VERSIONS);
    myTargetPerlVersionComboBox = new ComboBox<>(versionModel);
    myTargetPerlVersionComboBox.setRenderer(new ColoredListCellRenderer<PerlVersion>() {
      @Override
      protected void customizeCellRenderer(@NotNull JList<? extends PerlVersion> list,
                                           PerlVersion value,
                                           int index,
                                           boolean selected,
                                           boolean hasFocus) {
        append(value.getStrictDottedVersion());
        String versionDescription = PerlVersion.PERL_VERSION_DESCRIPTIONS.get(value);
        if (StringUtil.isNotEmpty(versionDescription)) {
          append(" (" + versionDescription + ")");
        }
      }
    });
    versionBuilder.addLabeledComponent(PerlBundle.message("perl.config.language.level"), myTargetPerlVersionComboBox);
    builder.addComponent(versionBuilder.getPanel());

    myLibsModel = new CollectionListModel<>();
    myLibsList = new JBList<>(myLibsModel);
    myLibsList.setVisibleRowCount(ourRowsCount);
    myLibsList.setCellRenderer(new ColoredListCellRenderer<VirtualFile>() {
      @Override
      protected void customizeCellRenderer(@NotNull JList<? extends VirtualFile> list,
                                           VirtualFile value,
                                           int index,
                                           boolean selected,
                                           boolean hasFocus) {
        setIcon(PerlIcons.PERL_LANGUAGE_ICON);
        append(FileUtil.toSystemDependentName(value.getPath()));
      }
    });
    builder.addLabeledComponent(
      PerlBundle.message("perl.settings.external.libs"),
      ToolbarDecorator.createDecorator(myLibsList)
        .setAddAction(this::doAddExternalLibrary)
        .createPanel()
    );

    simpleMainCheckbox = new JCheckBox(PerlBundle.message("perl.config.simple.main"));
    builder.addComponent(simpleMainCheckbox);

    autoInjectionCheckbox = new JCheckBox(PerlBundle.message("perl.config.heredoc.injections"));
    builder.addComponent(autoInjectionCheckbox);

    allowInjectionWithInterpolation = new JCheckBox(PerlBundle.message("perl.config.heredoc.injections.qq"));
    builder.addComponent(allowInjectionWithInterpolation);

    allowRegexpInjections = new JCheckBox(PerlBundle.message("perl.config.regex.injections"));
    builder.addComponent(allowRegexpInjections);
    allowRegexpInjections.setEnabled(false);
    allowRegexpInjections.setVisible(false);

    perlAnnotatorCheckBox = new JCheckBox(PerlBundle.message("perl.config.annotations.cw"));
    //		builder.addComponent(perlAnnotatorCheckBox);

    perlCriticCheckBox = new JCheckBox(PerlBundle.message("perl.config.annotations.critic"));
    builder.addComponent(perlCriticCheckBox);

    enablePerlSwitchCheckbox = new JCheckBox(PerlBundle.message("perl.config.enable.switch"));
    builder.addComponent(enablePerlSwitchCheckbox);

    perlCriticPathInputField = new TextFieldWithBrowseButton();
    perlCriticPathInputField.setEditable(false);
    FileChooserDescriptor perlCriticDescriptor = new FileChooserDescriptor(true, false, false, false, false, false) {
      @Override
      public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles) {
        return super.isFileVisible(file, showHiddenFiles) &&
               (file.isDirectory() || StringUtil.equals(file.getNameWithoutExtension(), PerlCriticAnnotator.PERL_CRITIC_LINUX_NAME));
      }
    };

    //noinspection DialogTitleCapitalization
    perlCriticPathInputField.addBrowseFolderListener(
      PerlBundle.message("perl.config.select.file.title"),
      PerlBundle.message("perl.config.select.critic"),
      null, // project
      perlCriticDescriptor
    );
    builder.addLabeledComponent(new JLabel(PerlBundle.message("perl.config.path.critic")), perlCriticPathInputField);
    perlCriticArgsInputField = new RawCommandLineEditor();
    builder.addComponent(
      copyDialogCaption(
        LabeledComponent.create(perlCriticArgsInputField, PerlBundle.message("perl.config.critic.cmd.arguments")),
        PerlBundle.message("perl.config.critic.cmd.arguments")
      )
    );

    perlTidyPathInputField = new TextFieldWithBrowseButton();
    perlTidyPathInputField.setEditable(false);
    FileChooserDescriptor perlTidyDescriptor = new FileChooserDescriptor(true, false, false, false, false, false) {
      @Override
      public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles) {
        return super.isFileVisible(file, showHiddenFiles) &&
               (file.isDirectory() || StringUtil.equals(file.getNameWithoutExtension(), PerlFormatWithPerlTidyAction.PERL_TIDY_LINUX_NAME));
      }
    };

    //noinspection DialogTitleCapitalization
    perlTidyPathInputField.addBrowseFolderListener(
      PerlBundle.message("perl.config.select.file.title"),
      PerlBundle.message("perl.config.select.tidy"),
      null, // project
      perlTidyDescriptor
    );
    builder.addLabeledComponent(new JLabel(PerlBundle.message("perl.config.path.tidy")), perlTidyPathInputField);
    perlTidyArgsInputField = new RawCommandLineEditor();
    builder.addComponent(
      copyDialogCaption(
        LabeledComponent.create(perlTidyArgsInputField, PerlBundle.message("perl.config.tidy.options.label")),
        PerlBundle.message("perl.config.tidy.options.label.short")
      ));

    deparseArgumentsTextField = new JTextField();
    builder.addLabeledComponent(PerlBundle.message("perl.config.deparse.options.label"), deparseArgumentsTextField);

    selfNamesModel = new CollectionListModel<>();
    JBList selfNamesList = new JBList<>(selfNamesModel);
    selfNamesList.setVisibleRowCount(ourRowsCount);
    builder.addLabeledComponent(new JLabel(PerlBundle.message("perl.config.self.names.label")), ToolbarDecorator
      .createDecorator(selfNamesList)
      .setAddAction(
        anActionButton -> {
          String variableName = Messages.showInputDialog(
            myProject,
            PerlBundle.message("perl.config.self.add.text"),
            PerlBundle.message("perl.config.self.add.title"),
            Messages.getQuestionIcon(),
            "",
            null);
          if (StringUtil.isNotEmpty(variableName)) {
            while (variableName.startsWith("$")) {
              variableName = variableName.substring(1);
            }

            if (StringUtil.isNotEmpty(variableName) && !selfNamesModel.getItems().contains(variableName)) {
              selfNamesModel.add(variableName);
            }
          }
        }).createPanel());

    return builder.getPanel();
  }

  @Nls
  @Override
  public String getDisplayName() {
    return PerlBundle.message("perl.perl5");
  }

  private void doAddExternalLibrary(AnActionButton button) {
    FileChooserFactory.getInstance().createPathChooser(
      FileChooserDescriptorFactory.
        createMultipleFoldersDescriptor().
        withTreeRootVisible(true).
        withTitle(PerlBundle.message("perl.settings.select.libs")),
      myProject,
      myLibsList
    ).choose(null, virtualFiles -> {
      Ref<Boolean> notifyInternals = new Ref<>(false);

      List<VirtualFile> rootsToAdd = new ArrayList<>();

      for (VirtualFile virtualFile : virtualFiles) {
        if (!virtualFile.isValid()) {
          continue;
        }
        if (!virtualFile.isDirectory()) {
          virtualFile = virtualFile.getParent();
          if (virtualFile == null || !virtualFile.isValid()) {
            continue;
          }
        }

        if (ModuleUtilCore.findModuleForFile(virtualFile, myProject) != null) {
          notifyInternals.set(true);
          continue;
        }
        rootsToAdd.add(virtualFile);
      }

      myLibsModel.add(rootsToAdd);

      if (notifyInternals.get()) {
        Messages.showErrorDialog(
          myLibsList,
          PerlBundle.message("perl.settings.external.libs.internal.text"),
          PerlBundle.message("perl.settings.external.libs.internal.title")
        );
      }
    });
  }

  @Override
  public boolean isModified() {
    return myPerl5SdkConfigurable.isModified() || isLibsModified() ||
           mySharedSettings.SIMPLE_MAIN_RESOLUTION != simpleMainCheckbox.isSelected() ||
           mySharedSettings.AUTOMATIC_HEREDOC_INJECTIONS != autoInjectionCheckbox.isSelected() ||
           mySharedSettings.ALLOW_INJECTIONS_WITH_INTERPOLATION != allowInjectionWithInterpolation.isSelected() ||
           myLocalSettings.ENABLE_REGEX_INJECTIONS != allowRegexpInjections.isSelected() ||
           mySharedSettings.PERL_ANNOTATOR_ENABLED != perlAnnotatorCheckBox.isSelected() ||
           mySharedSettings.PERL_CRITIC_ENABLED != perlCriticCheckBox.isSelected() ||
           mySharedSettings.PERL_SWITCH_ENABLED != enablePerlSwitchCheckbox.isSelected() ||
           !mySharedSettings.getTargetPerlVersion().equals(myTargetPerlVersionComboBox.getSelectedItem()) ||
           !StringUtil.equals(mySharedSettings.PERL_DEPARSE_ARGUMENTS, deparseArgumentsTextField.getText()) ||
           !StringUtil.equals(myLocalSettings.PERL_CRITIC_PATH, perlCriticPathInputField.getText()) ||
           !StringUtil.equals(mySharedSettings.PERL_CRITIC_ARGS, perlCriticArgsInputField.getText()) ||
           !StringUtil.equals(myLocalSettings.PERL_TIDY_PATH, perlTidyPathInputField.getText()) ||
           !StringUtil.equals(mySharedSettings.PERL_TIDY_ARGS, perlTidyArgsInputField.getText()) ||
           !mySharedSettings.selfNames.equals(selfNamesModel.getItems());
  }

  private boolean isLibsModified() {
    return !myLibsModel.getItems().equals(PerlProjectManager.getInstance(myProject).getExternalLibraryRoots());
  }

  @Override
  public void reset() {
    myPerl5SdkConfigurable.reset();
    myLibsModel.removeAll();
    myLibsModel.add(PerlProjectManager.getInstance(myProject).getExternalLibraryRoots());
    selfNamesModel.removeAll();
    selfNamesModel.add(mySharedSettings.selfNames);

    myTargetPerlVersionComboBox.setSelectedItem(mySharedSettings.getTargetPerlVersion());
    simpleMainCheckbox.setSelected(mySharedSettings.SIMPLE_MAIN_RESOLUTION);
    autoInjectionCheckbox.setSelected(mySharedSettings.AUTOMATIC_HEREDOC_INJECTIONS);
    allowInjectionWithInterpolation.setSelected(mySharedSettings.ALLOW_INJECTIONS_WITH_INTERPOLATION);
    allowRegexpInjections.setSelected(myLocalSettings.ENABLE_REGEX_INJECTIONS);
    perlAnnotatorCheckBox.setSelected(mySharedSettings.PERL_ANNOTATOR_ENABLED);
    deparseArgumentsTextField.setText(mySharedSettings.PERL_DEPARSE_ARGUMENTS);
    enablePerlSwitchCheckbox.setSelected(mySharedSettings.PERL_SWITCH_ENABLED);

    perlCriticCheckBox.setSelected(mySharedSettings.PERL_CRITIC_ENABLED);
    perlCriticPathInputField.setText(myLocalSettings.PERL_CRITIC_PATH);
    perlCriticArgsInputField.setText(mySharedSettings.PERL_CRITIC_ARGS);

    perlTidyPathInputField.setText(myLocalSettings.PERL_TIDY_PATH);
    perlTidyArgsInputField.setText(mySharedSettings.PERL_TIDY_ARGS);
  }

  @Override
  public void apply() throws ConfigurationException {
    myPerl5SdkConfigurable.apply();
    if (isLibsModified()) {
      PerlProjectManager.getInstance(myProject).setExternalLibraries(myLibsModel.getItems());
    }
    boolean reparseOpenFiles = false;
    mySharedSettings.SIMPLE_MAIN_RESOLUTION = simpleMainCheckbox.isSelected();
    mySharedSettings.AUTOMATIC_HEREDOC_INJECTIONS = autoInjectionCheckbox.isSelected();
    mySharedSettings.ALLOW_INJECTIONS_WITH_INTERPOLATION = allowInjectionWithInterpolation.isSelected();
    mySharedSettings.PERL_ANNOTATOR_ENABLED = perlAnnotatorCheckBox.isSelected();
    mySharedSettings.setDeparseOptions(deparseArgumentsTextField.getText());
    //noinspection ConstantConditions
    mySharedSettings.setTargetPerlVersion((PerlVersion)myTargetPerlVersionComboBox.getSelectedItem());

    mySharedSettings.PERL_CRITIC_ENABLED = perlCriticCheckBox.isSelected();
    myLocalSettings.PERL_CRITIC_PATH = perlCriticPathInputField.getText();
    mySharedSettings.PERL_CRITIC_ARGS = perlCriticArgsInputField.getText();

    if (mySharedSettings.PERL_SWITCH_ENABLED != enablePerlSwitchCheckbox.isSelected()) {
      mySharedSettings.PERL_SWITCH_ENABLED = enablePerlSwitchCheckbox.isSelected();
      reparseOpenFiles = true;
    }

    if (myLocalSettings.ENABLE_REGEX_INJECTIONS != allowRegexpInjections.isSelected()) {
      myLocalSettings.ENABLE_REGEX_INJECTIONS = allowRegexpInjections.isSelected();
      reparseOpenFiles = true;
    }

    myLocalSettings.PERL_TIDY_PATH = perlTidyPathInputField.getText();
    mySharedSettings.PERL_TIDY_ARGS = perlTidyArgsInputField.getText();

    mySharedSettings.selfNames.clear();
    mySharedSettings.selfNames.addAll(selfNamesModel.getItems());

    mySharedSettings.settingsUpdated();
    if (reparseOpenFiles) {
      FileContentUtil.reparseOpenedFiles();
    }
  }

  @Override
  public void setSdk(@Nullable Sdk sdk) {
    PerlProjectManager.getInstance(myProject).setProjectSdk(sdk);
  }

  @Override
  public List<Perl5SdkWrapper> getAllSdkWrappers() {
    List<Perl5SdkWrapper> defaultItems = new ArrayList<>(Perl5SdkManipulator.super.getAllSdkWrappers());
    defaultItems.add(0, DISABLE_PERL_ITEM);
    return defaultItems;
  }

  @Override
  public void disposeUIResources() {
    myLibsList = null;
    myPerl5SdkConfigurable.disposeUIResources();
  }

  @NotNull
  @Override
  public Perl5SdkWrapper getCurrentSdkWrapper() {
    Sdk projectSdk = PerlProjectManager.getInstance(myProject).getProjectSdk();
    return projectSdk == null ? DISABLE_PERL_ITEM : new Perl5RealSdkWrapper(projectSdk);
  }

  private static LabeledComponent<RawCommandLineEditor> copyDialogCaption(final LabeledComponent<RawCommandLineEditor> component,
                                                                          String text) {
    final RawCommandLineEditor rawCommandLineEditor = component.getComponent();
    rawCommandLineEditor.setDialogCaption(text);
    component.getLabel().setLabelFor(rawCommandLineEditor.getTextField());
    return component;
  }
}
