package editor;

import base.Mason2TopLevelComponentTestCase;
import com.intellij.testFramework.Parameterized;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("ALL")
@RunWith(Parameterized.class)
public class Mason2LeftBraceTypingHandlerTest extends Mason2TopLevelComponentTestCase {
  private final String mySource;
  private final String myExpected;

  public Mason2LeftBraceTypingHandlerTest(@NotNull String description, @NotNull String source, @NotNull String expected) {
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
      {"Empty file", "<caret>", "{<caret>"},
      {"Single brace", "{<caret>", "{{<caret>"},
      {"Outlined empty line", "% <caret>", "% {<caret>}"},
      {"Outlined single brace", "% {<caret>", "% {{<caret>}"},
      {"Doc sample", "% $.Row('std') {<caret>", "% $.Row('std') {{<caret>}"},
    });
  }

  @Test
  public void testAutoClose() {
    doTestTypingWithoutFiles(mySource, "{", myExpected);
  }
}
