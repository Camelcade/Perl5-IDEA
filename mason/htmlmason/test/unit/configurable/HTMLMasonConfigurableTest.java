package unit.configurable;

import base.HTMLMasonLightTestCase;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonCustomTag;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonCustomTagRole;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettingsConfigurable;
import org.junit.Test;

public class HTMLMasonConfigurableTest extends HTMLMasonLightTestCase {
  @Test
  public void testMasonSettingsConfigurable() {
    var settings = HTMLMasonSettings.getInstance(getProject());
    settings.substitutedExtensions.add("*.superduper");
    settings.substitutedExtensions.add("*.otherpuper");
    settings.customTags.add(new HTMLMasonCustomTag("testPerl", HTMLMasonCustomTagRole.PERL));
    settings.customTags.add(new HTMLMasonCustomTag("testMethod", HTMLMasonCustomTagRole.METHOD));
    settings.customTags.add(new HTMLMasonCustomTag("testDef", HTMLMasonCustomTagRole.DEF));
    settings.customTags.add(new HTMLMasonCustomTag("testArgs", HTMLMasonCustomTagRole.ARGS));
    var configurable = new HTMLMasonSettingsConfigurable(getProject());
    configurable.createComponent();
    assertTrue(configurable.isModified());
    configurable.reset();
    assertFalse(configurable.isModified());
    configurable.apply();
    assertFalse(configurable.isModified());
  }
}
