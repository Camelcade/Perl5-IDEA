package ui;

import base.PerlLightTestCase;
import com.intellij.openapi.options.ConfigurationException;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SettingsConfigurable;
import org.junit.Test;

public class Perl5SettingsConfigurableTest extends PerlLightTestCase {
  @Test
  public void testCreation() {
    var topConfigurable = new Perl5SettingsConfigurable(getProject());
    var component = topConfigurable.createComponent();
    assertNotNull(component);
    assertTrue(topConfigurable.isModified());
    topConfigurable.reset();
    assertFalse(topConfigurable.isModified());
    try {
      topConfigurable.apply();
    }
    catch (ConfigurationException e) {
      fail(e.getMessage());
    }
    topConfigurable.disposeUIResources();
  }
}
