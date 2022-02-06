package ui;

import base.HTMLMasonLightTestCase;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SettingsConfigurable;
import org.junit.Test;

public class HtmlMasonConfigurableTest extends HTMLMasonLightTestCase {
  @Test
  public void testSettingsConfigurable() {
    doTestConfigurable(new Perl5SettingsConfigurable(getProject()));
  }
}
