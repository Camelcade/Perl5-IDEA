package editor;

import base.Mason2TopLevelComponentTestCase;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.testFramework.Parameterized;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("ALL")
@RunWith(Parameterized.class)
public class Mason2RightAngleTypingHandlerTest extends Mason2TopLevelComponentTestCase {
  private final String mySource;
  private final String myExpected;

  public Mason2RightAngleTypingHandlerTest(@NotNull String description, @NotNull String source, @NotNull String expected) {
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
      {"Empty file", "<caret>", "><caret>"},

      {"Empty file <%", "<%<caret>", "<%><caret>"},
      {"First of two <%", "<%<caret> somethng <% 42; %>", "<%><caret> somethng <% 42; %>"},
      {"Second of two <%", "somethng <% 42; %> text <%<caret>", "somethng <% 42; %> text <%><caret>"},
      {"Before text <%", "<%<caret>sometext", "<%><caret>sometext"},
      {"After text <%", "sometext<%<caret>", "sometext<%><caret>"},
      {"Open closed <%", "<%<caret>%>", "<%><caret>%>"},
      {"Inside text <%", "some <%<caret> text", "some <%><caret> text"},
      {"Inside perl <%", "some <% <%<caret> %> text", "some <% <%><caret> %> text"},

      {"Empty file <&", "<&<caret>", "<&><caret>"},
      {"First of two <&", "<&<caret> somethng <& 42; &>", "<&><caret> somethng <& 42; &>"},
      {"Second of two <&", "somethng <& 42; &> text <&<caret>", "somethng <& 42; &> text <&><caret>"},
      {"Before text <&", "<&<caret>sometext", "<&><caret>sometext"},
      {"After text <&", "sometext<&<caret>", "sometext<&><caret>"},
      {"Open closed <&", "<&<caret>&>", "<&><caret>&>"},
      {"Inside text <&", "some <&<caret> text", "some <&><caret> text"},
      {"Inside perl <&", "some <& <&<caret> &> text", "some <& <&><caret> &> text"},

      {"Empty file <%doc", "<%doc<caret>", "<%doc><caret></%doc>"},
      {"First of two <%doc", "<%doc<caret> somethng <%doc testname >", "<%doc><caret></%doc> somethng <%doc testname >"},
      {"Second of two <%doc", "somethng <%doc testname > text <%doc<caret>", "somethng <%doc testname > text <%doc><caret></%doc>"},
      {"Before text <%doc", "<%doc<caret>sometext", "<%doc><caret>sometext"},
      {"After text <%doc", "sometext<%doc<caret>", "sometext<%doc><caret>"},
      {"Open closed <%doc", "<%doc<caret>>", "<%doc><caret>>"},
      {"Inside text <%doc", "some <%doc<caret> text", "some <%doc><caret></%doc> text"},
      {"Inside perl <%doc", "some <%doc <%doc<caret> > text", "some <%doc <%doc><caret></%doc> > text"},

      {"Empty file <%class", "<%class<caret>", "<%class><caret></%class>"},
      {"First of two <%class", "<%class<caret> somethng <%class testname >", "<%class><caret></%class> somethng <%class testname >"},
      {"Second of two <%class", "somethng <%class testname > text <%class<caret>",
        "somethng <%class testname > text <%class><caret></%class>"},
      {"Before text <%class", "<%class<caret>sometext", "<%class><caret>sometext"},
      {"After text <%class", "sometext<%class<caret>", "sometext<%class><caret>"},
      {"Open closed <%class", "<%class<caret>>", "<%class><caret>>"},
      {"Inside text <%class", "some <%class<caret> text", "some <%class><caret></%class> text"},
      {"Inside perl <%class", "some <%class <%class<caret> > text", "some <%class <%class><caret></%class> > text"},

      {"Empty file <%init", "<%init<caret>", "<%init><caret></%init>"},
      {"First of two <%init", "<%init<caret> somethng <%init testname >", "<%init><caret></%init> somethng <%init testname >"},
      {"Second of two <%init", "somethng <%init testname > text <%init<caret>", "somethng <%init testname > text <%init><caret></%init>"},
      {"Before text <%init", "<%init<caret>sometext", "<%init><caret>sometext"},
      {"After text <%init", "sometext<%init<caret>", "sometext<%init><caret>"},
      {"Open closed <%init", "<%init<caret>>", "<%init><caret>>"},
      {"Inside text <%init", "some <%init<caret> text", "some <%init><caret></%init> text"},
      {"Inside perl <%init", "some <%init <%init<caret> > text", "some <%init <%init><caret></%init> > text"},

      {"Empty file <%perl", "<%perl<caret>", "<%perl><caret></%perl>"},
      {"First of two <%perl", "<%perl<caret> somethng <%perl testname >", "<%perl><caret></%perl> somethng <%perl testname >"},
      {"Second of two <%perl", "somethng <%perl testname > text <%perl<caret>", "somethng <%perl testname > text <%perl><caret></%perl>"},
      {"Before text <%perl", "<%perl<caret>sometext", "<%perl><caret>sometext"},
      {"After text <%perl", "sometext<%perl<caret>", "sometext<%perl><caret>"},
      {"Open closed <%perl", "<%perl<caret>>", "<%perl><caret>>"},
      {"Inside text <%perl", "some <%perl<caret> text", "some <%perl><caret></%perl> text"},
      {"Inside perl <%perl", "some <%perl <%perl<caret> > text", "some <%perl <%perl><caret></%perl> > text"},

      {"Empty file <%text", "<%text<caret>", "<%text><caret></%text>"},
      {"First of two <%text", "<%text<caret> somethng <%text testname >", "<%text><caret></%text> somethng <%text testname >"},
      {"Second of two <%text", "somethng <%text testname > text <%text<caret>", "somethng <%text testname > text <%text><caret></%text>"},
      {"Before text <%text", "<%text<caret>sometext", "<%text><caret>sometext"},
      {"After text <%text", "sometext<%text<caret>", "sometext<%text><caret>"},
      {"Open closed <%text", "<%text<caret>>", "<%text><caret>>"},
      {"Inside text <%text", "some <%text<caret> text", "some <%text><caret></%text> text"},
      {"Inside perl <%text", "some <%text <%text<caret> > text", "some <%text <%text><caret></%text> > text"},

      {"Empty file <%flags", "<%flags<caret>", "<%flags> extends => '<caret>' </%flags>"},
      {"First of two <%flags", "<%flags<caret> somethng <%flags testname >",
        "<%flags> extends => '<caret>' </%flags> somethng <%flags testname >"},
      {"Second of two <%flags", "somethng <%flags testname > text <%flags<caret>",
        "somethng <%flags testname > text <%flags> extends => '<caret>' </%flags>"},
      {"Before text <%flags", "<%flags<caret>sometext", "<%flags><caret>sometext"},
      {"After text <%flags", "sometext<%flags<caret>", "sometext<%flags><caret>"},
      {"Open closed <%flags", "<%flags<caret>>", "<%flags><caret>>"},
      {"Inside text <%flags", "some <%flags<caret> text", "some <%flags> extends => '<caret>' </%flags> text"},
      {"Inside perl <%flags", "some <%flags <%flags<caret> > text", "some <%flags <%flags> extends => '<caret>' </%flags> > text"},
    });
  }

  @Test
  public void testAutoClose() {
    assertTrue(StringUtil.contains(myExpected, "<caret>"));
    doTestTypingWithoutFiles(mySource, ">", myExpected);
  }
}
