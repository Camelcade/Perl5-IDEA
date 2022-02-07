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
public class MojoEnterHandlerTest extends MojoLightTestCase {
  private final String mySource;
  private final String myExpected;

  public MojoEnterHandlerTest(@NotNull String description, @NotNull String source, @NotNull String expected) {
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
      {"Empty file", "<caret>", "\n<caret>"},

      {"Empty file <%", "<%<caret>", "<%\n<caret>\n%>"},
      {"First of two <%", "<%<caret> somethng <% 42; %>", "<%\n<caret>\n%> somethng <% 42; %>"},
      {"Second of two <%", "somethng <% 42; %> text <%<caret>", "somethng <% 42; %> text <%\n<caret>\n%>"},
      {"Before text <%", "<%<caret>sometext", "<%\n<caret>\n%>sometext"},
      {"After text <%", "sometext<%<caret>", "sometext<%\n<caret>\n%>"},
      {"Open closed <%", "<%<caret>%>", "<%\n<caret>%>"},
      {"Inside text <%", "some <%<caret> text", "some <%\n<caret>\n%> text"},
      {"Inside perl <%", "some <% <%<caret> %> text", "some <% <%\n <caret>%> text"},

      {"Empty file <%=", "<%=<caret>", "<%=\n<caret>\n%>"},
      {"First of two <%=", "<%=<caret> somethng <%= 42; %>", "<%=\n<caret>\n%> somethng <%= 42; %>"},
      {"Second of two <%=", "somethng <%= 42; %> text <%=<caret>", "somethng <%= 42; %> text <%=\n<caret>\n%>"},
      {"Before text <%=", "<%=<caret>sometext", "<%=\n<caret>\n%>sometext"},
      {"After text <%=", "sometext<%=<caret>", "sometext<%=\n<caret>\n%>"},
      {"Open closed <%=", "<%=<caret>%>", "<%=\n<caret>%>"},
      {"Inside text <%=", "some <%=<caret> text", "some <%=\n<caret>\n%> text"},
      {"Inside perl <%=", "some <%= <%=<caret> %> text", "some <%= <%=\n <caret>%> text"},

      {"Empty file <%==", "<%==<caret>", "<%==\n<caret>\n%>"},
      {"First of two <%==", "<%==<caret> somethng <%== 42; %>", "<%==\n<caret>\n%> somethng <%== 42; %>"},
      {"Second of two <%==", "somethng <%== 42; %> text <%==<caret>", "somethng <%== 42; %> text <%==\n<caret>\n%>"},
      {"Before text <%==", "<%==<caret>sometext", "<%==\n<caret>\n%>sometext"},
      {"After text <%==", "sometext<%==<caret>", "sometext<%==\n<caret>\n%>"},
      {"Open closed <%==", "<%==<caret>%>", "<%==\n<caret>%>"},
      {"Inside text <%==", "some <%==<caret> text", "some <%==\n<caret>\n%> text"},
      {"Inside perl <%==", "some <%== <%==<caret> %> text", "some <%== <%==\n <caret>%> text"},

      {"Begin in block", "<% my $block = begin<caret> %>", "<% my $block = begin\n" +
                                                           "    <caret>\n" +
                                                           "% end\n" +
                                                           " %>"},
      {"Begin in block closed", "<% my $block = begin<caret> %>\n<% end %>", "<% my $block = begin\n" +
                                                                             "    <caret>%>\n" +
                                                                             "<% end %>"},
      {"Begin in line", "% my $block = begin<caret>", "% my $block = begin\n" +
                                                      "<caret>\n" +
                                                      "% end\n"},
      {"Begin in line closed", "% my $block = begin<caret>\n% end", "% my $block = begin\n" +
                                                                    "<caret>\n" +
                                                                    "% end\n" +
                                                                    "\n" +
                                                                    "% end"},

      {"Breaking the line", "% 42 +<caret> 42", "% 42 +\n" +
                                                "<caret>%  42"},
      {"End of the line", "% 42 + 42<caret>", "% 42 + 42\n" +
                                              "<caret>"},
    });
  }

  @Test
  public void testAutoClose() {
    doTestTypingWithoutFiles(mySource, "\n", myExpected);
  }
}
