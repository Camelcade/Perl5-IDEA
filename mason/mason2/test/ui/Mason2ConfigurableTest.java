package ui;

import base.Mason2PurePerlComponentTestCase;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SettingsConfigurable;
import org.junit.Test;

public class Mason2ConfigurableTest extends Mason2PurePerlComponentTestCase {
  @Test
  public void testSettingsConfigurable() {
    doTestConfigurable(new Perl5SettingsConfigurable(getProject()));
  }
}
