package ui;

import base.TemplateToolkitLightTestCase;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SettingsConfigurable;
import org.junit.Test;

public class TemplateToolkitConfigurableTest extends TemplateToolkitLightTestCase {
  @Test
  public void testSettingsConfigurable() {
    doTestConfigurable(new Perl5SettingsConfigurable(getProject()));
  }
}
