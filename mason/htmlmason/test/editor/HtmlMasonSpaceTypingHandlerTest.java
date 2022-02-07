package editor;

import base.HTMLMasonLightTestCase;
import com.intellij.testFramework.Parameterized;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("ALL")
@RunWith(Parameterized.class)
public class HtmlMasonSpaceTypingHandlerTest extends HTMLMasonLightTestCase {
  private final String mySource;
  private final String myExpected;

  public HtmlMasonSpaceTypingHandlerTest(@NotNull String description, @NotNull String source, @NotNull String expected) {
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

      {"Empty file <&", "<&<caret>", "<& <caret> &>"},
      {"First of two <&", "<&<caret> somethng <& 42; &>", "<& <caret> &> somethng <& 42; &>"},
      {"Second of two <&", "somethng <& 42; &> text <&<caret>", "somethng <& 42; &> text <& <caret> &>"},
      {"Before text <&", "<&<caret>sometext", "<& <caret> &>sometext"},
      {"After text <&", "sometext<&<caret>", "sometext<& <caret> &>"},
      {"Open closed <&", "<&<caret>&>", "<& <caret>&>"},
      {"Inside text <&", "some <&<caret> text", "some <& <caret> &> text"},
      {"Inside perl <&", "some <& <&<caret> &> text", "some <& <& <caret> &> text"},

      {"Empty file <&|", "<&|<caret>", "<&| <caret> &></&>"},
      {"Empty file with closer <&|", "<&|<caret></&>", "<&| <caret> &></&></&>"},
      {"First of two <&|", "<&|<caret> somethng <&| 42; &>", "<&| <caret> &></&> somethng <&| 42; &>"},
      {"Second of two <&|", "somethng <&| 42; &> text <&|<caret>", "somethng <&| 42; &> text <&| <caret> &></&>"},
      {"Before text <&|", "<&|<caret>sometext", "<&| <caret> &></&>sometext"},
      {"After text <&|", "sometext<&|<caret>", "sometext<&| <caret> &></&>"},
      {"Open closed <&|", "<&|<caret>&>", "<&| <caret> &></&>&>"},
      {"Open closed with closed tag <&|", "<&|<caret>&></&>", "<&| <caret> &></&>&></&>"},
      {"Inside text <&|", "some <&|<caret> text", "some <&| <caret> &></&> text"},
      {"Inside perl <&|", "some <&| <&|<caret> &> text", "some <&| <&| <caret> &> text"},

      {"Empty file <%def", "<%def<caret>", "<%def <caret>>\n" +
                                           "</%def>"},
      {"First of two <%def", "<%def<caret> somethng <%def testname >", "<%def <caret>>\n" +
                                                                       "</%def> somethng <%def testname >"},
      {"Second of two <%def", "somethng <%def testname > text <%def<caret>", "somethng <%def testname > text <%def <caret>>\n" +
                                                                             "</%def>"},
      {"Before text <%def", "<%def<caret>sometext", "<%def <caret>sometext"},
      {"After text <%def", "sometext<%def<caret>", "sometext<%def <caret>>\n" +
                                                   "</%def>"},
      {"Open closed <%def", "<%def<caret>>", "<%def <caret>>"},
      {"Inside text <%def", "some <%def<caret> text", "some <%def <caret>>\n" +
                                                      "</%def> text"},
      {"Inside perl <%def", "some <%def <%def<caret> > text", "some <%def <%def <caret> > text"},

      {"Empty file <%method", "<%method<caret>", "<%method <caret>>\n" +
                                                 "</%method>"},
      {"First of two <%method", "<%method<caret> somethng <%method testname >", "<%method <caret>>\n" +
                                                                                "</%method> somethng <%method testname >"},
      {"Second of two <%method", "somethng <%method testname > text <%method<caret>",
        "somethng <%method testname > text <%method <caret>>\n" +
        "</%method>"},
      {"Before text <%method", "<%method<caret>sometext", "<%method <caret>sometext"},
      {"After text <%method", "sometext<%method<caret>", "sometext<%method <caret>>\n" +
                                                         "</%method>"},
      {"Open closed <%method", "<%method<caret>>", "<%method <caret>>"},
      {"Inside text <%method", "some <%method<caret> text", "some <%method <caret>>\n" +
                                                            "</%method> text"},
      {"Inside perl <%method", "some <%method <%method<caret> > text", "some <%method <%method <caret> > text"},
    });
  }

  @Test
  public void testAutoClose() {
    doTestTypingWithoutFiles(mySource, " ", myExpected);
  }
}
