package editor;

import base.MojoLightTestCase;
import com.intellij.testFramework.Parameterized;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("ALL")
@RunWith(Parameterized.class)
public class MojoSpaceTypingHandlerTest extends MojoLightTestCase {
  private final String mySource;
  private final String myExpected;

  public MojoSpaceTypingHandlerTest(@NotNull String description, @NotNull String source, @NotNull String expected) {
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
      {"Empty file", "<caret>", " <caret>"},

      {"Empty file <%", "<%<caret>", "<% <caret> %>"},
      {"First of two <%", "<%<caret> somethng <% 42; %>", "<% <caret> %> somethng <% 42; %>"},
      {"Second of two <%", "somethng <% 42; %> text <%<caret>", "somethng <% 42; %> text <% <caret> %>"},
      {"Before text <%", "<%<caret>sometext", "<% <caret> %>sometext"},
      {"After text <%", "sometext<%<caret>", "sometext<% <caret> %>"},
      {"Open closed <%", "<%<caret>%>", "<% <caret>%>"},
      {"Inside text <%", "some <%<caret> text", "some <% <caret> %> text"},
      {"Inside perl <%", "some <% <%<caret> %> text", "some <% <% <caret> %> text"},

      {"Empty file <%=", "<%=<caret>", "<%= <caret> %>"},
      {"First of two <%=", "<%=<caret> somethng <%= 42; %>", "<%= <caret> %> somethng <%= 42; %>"},
      {"Second of two <%=", "somethng <%= 42; %> text <%=<caret>", "somethng <%= 42; %> text <%= <caret> %>"},
      {"Before text <%=", "<%=<caret>sometext", "<%= <caret> %>sometext"},
      {"After text <%=", "sometext<%=<caret>", "sometext<%= <caret> %>"},
      {"Open closed <%=", "<%=<caret>%>", "<%= <caret>%>"},
      {"Inside text <%=", "some <%=<caret> text", "some <%= <caret> %> text"},
      {"Inside perl <%=", "some <%= <%=<caret> %> text", "some <%= <%= <caret> %> text"},

      {"Empty file <%==", "<%==<caret>", "<%== <caret> %>"},
      {"First of two <%==", "<%==<caret> somethng <%== 42; %>", "<%== <caret> %> somethng <%== 42; %>"},
      {"Second of two <%==", "somethng <%== 42; %> text <%==<caret>", "somethng <%== 42; %> text <%== <caret> %>"},
      {"Before text <%==", "<%==<caret>sometext", "<%== <caret> %>sometext"},
      {"After text <%==", "sometext<%==<caret>", "sometext<%== <caret> %>"},
      {"Open closed <%==", "<%==<caret>%>", "<%== <caret>%>"},
      {"Inside text <%==", "some <%==<caret> text", "some <%== <caret> %> text"},
      {"Inside perl <%==", "some <%== <%==<caret> %> text", "some <%== <%== <caret> %> text"},
    });
  }

  @Test
  public void testAutoClose() {
    doTestTypingWithoutFiles(mySource, " ", myExpected);
  }
}
