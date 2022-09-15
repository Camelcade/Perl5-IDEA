package ui;

import base.PerlLightTestCase;
import com.perl5.lang.perl.idea.configuration.settings.sdk.PerlContentEntriesTreeEditor;
import org.junit.Test;

public class PerlContentEntriesTreeEditorConfigurableTest extends PerlLightTestCase {
  @Test
  public void testCreation() {
    doTestConfigurable(new PerlContentEntriesTreeEditor(getModule(), getTestRootDisposable()));
  }
}
