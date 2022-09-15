package ui;

import base.PerlLightTestCase;
import com.intellij.openapi.options.Configurable;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.xdebugger.settings.DebuggerSettingsCategory;
import com.perl5.lang.perl.debugger.PerlDebuggerSettings;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PerlDebugSettingsConfigurableTest extends PerlLightTestCase {
  @Override
  protected void tearDown() throws Exception {
    PerlDebuggerSettings.getInstance().setDataRenderers(Collections.emptyList());
    super.tearDown();
  }

  @Test
  public void testCreation() {
    var debuggerSettings = PerlDebuggerSettings.getInstance();
    Collection<? extends Configurable> configurables = debuggerSettings.createConfigurables(DebuggerSettingsCategory.DATA_VIEWS);
    assertSize(1, configurables);
    debuggerSettings.setDataRenderers(List.of(new PerlDebuggerSettings.Item("Foo::Bar", "blabla()")));
    doTestConfigurable(ContainerUtil.getFirstItem(configurables));
  }
}
