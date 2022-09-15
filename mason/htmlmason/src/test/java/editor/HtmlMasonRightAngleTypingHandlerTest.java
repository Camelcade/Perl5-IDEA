package editor;

import base.HTMLMasonLightTestCase;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.testFramework.Parameterized;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("ALL")
@RunWith(Parameterized.class)
public class HtmlMasonRightAngleTypingHandlerTest extends HTMLMasonLightTestCase {
  private final String mySource;
  private final String myExpected;

  public HtmlMasonRightAngleTypingHandlerTest(@NotNull String description, @NotNull String source, @NotNull String expected) {
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

      {"Empty file <&|", "<&|<caret>", "<&|><caret>"},
      {"Empty file with closer <&|", "<&|<caret></&>", "<&|><caret></&>"},
      {"First of two <&|", "<&|<caret> somethng <&| 42; &>", "<&|><caret> somethng <&| 42; &>"},
      {"Second of two <&|", "somethng <&| 42; &> text <&|<caret>", "somethng <&| 42; &> text <&|><caret>"},
      {"Before text <&|", "<&|<caret>sometext", "<&|><caret>sometext"},
      {"After text <&|", "sometext<&|<caret>", "sometext<&|><caret>"},
      {"Open closed <&|", "<&|<caret>&>", "<&|><caret>&>"},
      {"Open closed with closed tag <&|", "<&|<caret>&></&>", "<&|><caret>&></&>"},
      {"Inside text <&|", "some <&|<caret> text", "some <&|><caret> text"},
      {"Inside perl <&|", "some <&| <&|<caret> &> text", "some <&| <&|><caret> &> text"},

      {"Empty file <%def", "<%def<caret>", "<%def><caret>"},
      {"First of two <%def", "<%def<caret> somethng <%def testname >", "<%def><caret> somethng <%def testname >"},
      {"Second of two <%def", "somethng <%def testname > text <%def<caret>", "somethng <%def testname > text <%def><caret>"},
      {"Before text <%def", "<%def<caret>sometext", "<%def><caret>sometext"},
      {"After text <%def", "sometext<%def<caret>", "sometext<%def><caret>"},
      {"Open closed <%def", "<%def<caret>>", "<%def><caret>>"},
      {"Inside text <%def", "some <%def<caret> text", "some <%def><caret> text"},
      {"Inside perl <%def", "some <%def <%def<caret> > text", "some <%def <%def><caret> > text"},

      {"Empty file <%method", "<%method<caret>", "<%method><caret>"},
      {"First of two <%method", "<%method<caret> somethng <%method testname >", "<%method><caret> somethng <%method testname >"},
      {"Second of two <%method", "somethng <%method testname > text <%method<caret>", "somethng <%method testname > text <%method><caret>"},
      {"Before text <%method", "<%method<caret>sometext", "<%method><caret>sometext"},
      {"After text <%method", "sometext<%method<caret>", "sometext<%method><caret>"},
      {"Open closed <%method", "<%method<caret>>", "<%method><caret>>"},
      {"Inside text <%method", "some <%method<caret> text", "some <%method><caret> text"},
      {"Inside perl <%method", "some <%method <%method<caret> > text", "some <%method <%method><caret> > text"},

      {"Empty file <%flags", "<%flags<caret>", "<%flags>\n" +
                                               "inherit => '<caret>'\n" +
                                               "</%flags>"},
      {"First of two <%flags", "<%flags<caret> somethng <%flags testname >", "<%flags>\n" +
                                                                             "inherit => '<caret>'\n" +
                                                                             "</%flags> somethng <%flags testname >"},
      {"Second of two <%flags", "somethng <%flags testname > text <%flags<caret>", "somethng <%flags testname > text <%flags><caret>"},
      {"Before text <%flags", "<%flags<caret>sometext", "<%flags><caret>sometext"},
      {"After text <%flags", "sometext<%flags<caret>", "sometext<%flags>\n" +
                                                       "inherit => '<caret>'\n" +
                                                       "</%flags>"},
      {"Open closed <%flags", "<%flags<caret>>", "<%flags><caret>>"},
      {"Inside text <%flags", "some <%flags<caret> text", "some <%flags>\n" +
                                                          "inherit => '<caret>'\n" +
                                                          "</%flags> text"},
      {"Inside perl <%flags", "some <%flags <%flags<caret> > text", "some <%flags <%flags><caret> > text"},

      {"Close %perl", "<%perl<caret>", "<%perl><caret></%perl>"},
      {"Close %init", "<%init<caret>", "<%init><caret></%init>"},
      {"Close %cleanup", "<%cleanup<caret>", "<%cleanup><caret></%cleanup>"},
      {"Close %once", "<%once<caret>", "<%once><caret></%once>"},
      {"Close %shared", "<%shared<caret>", "<%shared><caret></%shared>"},
      {"Close %attr", "<%attr<caret>", "<%attr><caret></%attr>"},
      {"Close %args", "<%args<caret>", "<%args><caret></%args>"},
      {"Close %filter", "<%filter<caret>", "<%filter><caret></%filter>"},
      {"Close %doc", "<%doc<caret>", "<%doc><caret></%doc>"},
      {"Close %text", "<%text<caret>", "<%text><caret></%text>"},
      {"Close %blabla", "<%blabla<caret>", "<%blabla><caret>"},
    });
  }

  @Test
  public void testAutoClose() {
    assertTrue(StringUtil.contains(myExpected, "<caret>"));
    doTestTypingWithoutFiles(mySource, ">", myExpected);
  }
}
