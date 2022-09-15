package ui;

import base.PerlLightTestCase;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SettingsConfigurable;
import org.junit.Test;

public class Perl5SettingsConfigurableTest extends PerlLightTestCase {
  @Test
  public void testCreation() {
    doTestConfigurable(new Perl5SettingsConfigurable(getProject()));
  }
}
