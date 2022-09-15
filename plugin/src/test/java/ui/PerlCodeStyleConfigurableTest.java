package ui;

import base.PerlLightTestCase;
import com.intellij.psi.codeStyle.CodeStyleSettingsManager;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleConfigurable;
import org.junit.Test;

public class PerlCodeStyleConfigurableTest extends PerlLightTestCase {
  @Test
  public void testCreation() {
    var projectSettings = CodeStyleSettingsManager.getInstance(getProject()).getMainProjectCodeStyle();
    doTestConfigurable(new PerlCodeStyleConfigurable(projectSettings, projectSettings));
  }
}
