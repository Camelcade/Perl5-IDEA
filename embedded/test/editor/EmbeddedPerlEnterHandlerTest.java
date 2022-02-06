package editor;

import base.EmbeddedPerlLightTestCase;
import com.intellij.testFramework.Parameterized;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("ALL")
@RunWith(Parameterized.class)
public class EmbeddedPerlEnterHandlerTest extends EmbeddedPerlLightTestCase {
  private final String mySource;
  private final String myExpected;

  public EmbeddedPerlEnterHandlerTest(@NotNull String description, @NotNull String source, @NotNull String expected) {
    mySource = source;
    myExpected = expected;
  }

  @org.junit.runners.Parameterized.Parameters(name = "{0}")
  public static Iterable<Object[]> fakeData() {
    return Collections.emptyList();
  }

  @Parameterized.Parameters(name = "{0}")
  public static Iterable<Object[]> realData(Class<?> clazz) {
    return Arrays.asList(new Object[][]{
      {"Empty file", "<caret>", "<?\n<caret>\n?>"},
      {"First of two", "<caret> somethng <? 42; ?>", "<?\n<caret>\n?>somethng <? 42; ?>"},
      {"Second of two", "somethng <? 42; ?> text <caret>", "somethng <? 42; ?> text <?\n<caret>\n?>"},
      {"Before text", "<caret>sometext", "<?\n<caret>\n?>sometext"},
      {"After text", "sometext<caret>", "sometext<?\n<caret>\n?>"},
      {"Open closed", "<caret>?>", "<?\n<caret>?>"},
      {"Inside text", "some <caret> text", "some <?\n<caret>\n?>text"},
      {"Inside perl", "some <? <caret> ?> text", "some <? <?\n <caret>?> text"},
    });
  }

  @Test
  public void testAutoClose() {
    doTestTypingWithoutFiles(mySource, "<?\n", myExpected);
  }
}
