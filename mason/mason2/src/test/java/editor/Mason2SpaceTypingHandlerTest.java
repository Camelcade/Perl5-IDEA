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
public class Mason2SpaceTypingHandlerTest extends Mason2TopLevelComponentTestCase {
  private final String mySource;
  private final String myExpected;

  public Mason2SpaceTypingHandlerTest(@NotNull String description, @NotNull String source, @NotNull String expected) {
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

      {"Empty file <%", "<%<caret>", "<% <caret>"},
      {"First of two <%", "<%<caret> somethng <% 42; %>", "<% <caret> %> somethng <% 42; %>"},
      {"Second of two <%", "somethng <% 42; %> text <%<caret>", "somethng <% 42; %> text <% <caret>"},
      {"Before text <%", "<%<caret>sometext", "<% <caret>sometext"},
      {"After text <%", "sometext<%<caret>", "sometext<% <caret>"},
      {"Open closed <%", "<%<caret>%>", "<% <caret>%>"},
      {"Inside text <%", "some <%<caret> text", "some <% <caret> %> text"},
      {"Inside perl <%", "some <% <%<caret> %> text", "some <% <% <caret> %> text"},

      {"Empty file <&", "<&<caret>", "<& <caret>"},
      {"First of two <&", "<&<caret> somethng <& 42; &>", "<& <caret> &> somethng <& 42; &>"},
      {"Second of two <&", "somethng <& 42; &> text <&<caret>", "somethng <& 42; &> text <& <caret>"},
      {"Before text <&", "<&<caret>sometext", "<& <caret>sometext"},
      {"After text <&", "sometext<&<caret>", "sometext<& <caret>"},
      {"Open closed <&", "<&<caret>&>", "<& <caret>&>"},
      {"Inside text <&", "some <&<caret> text", "some <& <caret> &> text"},
      {"Inside perl <&", "some <& <&<caret> &> text", "some <& <& <caret> &> text"},

      {"Empty file <%method", "<%method<caret>", "<%method <caret>>\n" +
                                                 "</%method>"},
      {"First of two <%method", "<%method<caret> somethng <%method testname >", "<%method <caret>>\n" +
                                                                                "</%method> somethng <%method testname >"},
      {"Second of two <%method", "somethng <%method testname > text <%method<caret>",
        "somethng <%method testname > text <%method <caret>>\n" +
        "</%method>"},
      {"Before text <%method", "<%method<caret>sometext", "<%method <caret>>\n" +
                                                          "</%method>sometext"},
      {"After text <%method", "sometext<%method<caret>", "sometext<%method <caret>>\n" +
                                                         "</%method>"},
      {"Open closed <%method", "<%method<caret>>", "<%method <caret>>\n" +
                                                   "</%method>>"},
      {"Inside text <%method", "some <%method<caret> text", "some <%method <caret>>\n" +
                                                            "</%method> text"},
      {"Inside perl <%method", "some <%method <%method<caret> > text", "some <%method <%method <caret> > text"},


      {"Empty file <%filter", "<%filter<caret>", "<%filter <caret>>\n" +
                                                 "</%filter>"},
      {"First of two <%filter", "<%filter<caret> somethng <%filter testname >", "<%filter <caret>>\n" +
                                                                                "</%filter> somethng <%filter testname >"},
      {"Second of two <%filter", "somethng <%filter testname > text <%filter<caret>",
        "somethng <%filter testname > text <%filter <caret>>\n" +
        "</%filter>"},
      {"Before text <%filter", "<%filter<caret>sometext", "<%filter <caret>>\n" +
                                                          "</%filter>sometext"},
      {"After text <%filter", "sometext<%filter<caret>", "sometext<%filter <caret>>\n" +
                                                         "</%filter>"},
      {"Open closed <%filter", "<%filter<caret>>", "<%filter <caret>>\n" +
                                                   "</%filter>>"},
      {"Inside text <%filter", "some <%filter<caret> text", "some <%filter <caret>>\n" +
                                                            "</%filter> text"},
      {"Inside perl <%filter", "some <%filter <%filter<caret> > text", "some <%filter <%filter <caret> > text"},

      {"Empty file <%override", "<%override<caret>", "<%override <caret>>\n" +
                                                     "</%override>"},
      {"First of two <%override", "<%override<caret> somethng <%override testname >", "<%override <caret>>\n" +
                                                                                      "</%override> somethng <%override testname >"},
      {"Second of two <%override", "somethng <%override testname > text <%override<caret>",
        "somethng <%override testname > text <%override <caret>>\n" +
        "</%override>"},
      {"Before text <%override", "<%override<caret>sometext", "<%override <caret>>\n" +
                                                              "</%override>sometext"},
      {"After text <%override", "sometext<%override<caret>", "sometext<%override <caret>>\n" +
                                                             "</%override>"},
      {"Open closed <%override", "<%override<caret>>", "<%override <caret>>\n" +
                                                       "</%override>>"},
      {"Inside text <%override", "some <%override<caret> text", "some <%override <caret>>\n" +
                                                                "</%override> text"},
      {"Inside perl <%override", "some <%override <%override<caret> > text", "some <%override <%override <caret> > text"},
    });
  }

  @Test
  public void testAutoClose() {
    doTestTypingWithoutFiles(mySource, " ", myExpected);
  }
}
