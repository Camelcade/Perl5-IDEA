package ui;

import base.PerlLightTestCase;
import com.intellij.execution.RunConfigurationConfigurableAdapter;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.options.SettingsEditor;
import com.perl5.lang.perl.debugger.run.run.debugger.remote.PerlRemoteDebuggingConfigurationType;
import com.perl5.lang.perl.idea.run.prove.PerlTestRunConfigurationType;
import com.perl5.lang.perl.idea.run.run.PerlRunConfigurationType;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

public class PerlRunConfigurationEditorTest extends PerlLightTestCase {
  @Test
  public void testPerlRunConfiguration() {
    doTest(PerlRunConfigurationType.getInstance());
  }

  @Test
  public void testPerlTestRunConfiguration() {
    doTest(PerlTestRunConfigurationType.getInstance());
  }

  @Test
  public void testPerlRemoteDebuggingConfiguration() {
    doTest(PerlRemoteDebuggingConfigurationType.getInstance());
  }

  private void doTest(@NotNull ConfigurationTypeBase configurationType) {
    var configurationFactories = configurationType.getConfigurationFactories();
    assertSize(1, configurationFactories);
    var configurationFactory = configurationFactories[0];
    assertNotNull("Null configuration for " + configurationType, configurationFactory);
    var templateConfiguration = configurationFactory.createTemplateConfiguration(getProject());
    assertNotNull("Null template configuration for " + configurationType, templateConfiguration);
    var testConfiguration = configurationFactory.createConfiguration("Test configuration", templateConfiguration);
    assertNotNull("No configuration created for", testConfiguration);
    SettingsEditor<? extends RunConfiguration> configurationEditor = templateConfiguration.getConfigurationEditor();
    assertNotNull("Empty editor for template settings for " + configurationType, configurationEditor);

    var configurableAdapter =
      new RunConfigurationConfigurableAdapter<>((SettingsEditor<RunConfiguration>)configurationEditor, testConfiguration);
    doTestConfigurable(configurableAdapter);
  }
}
