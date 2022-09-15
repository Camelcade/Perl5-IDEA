package unit.perl;

import base.PerlLightTestCase;
import com.perl5.lang.perl.psi.PerlString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;

import java.util.ArrayList;

public class PerlStringMethodsTest extends PerlLightTestCase {
  @Test
  public void testGetQuoteCloseChar() {
    var openQuotes = "[({<\"'`1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*_|";
    var closeQuotes = "])}>\"'`1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*_|";
    assertEquals(openQuotes.length(), closeQuotes.length());
    var actual = new ArrayList<String>();
    var expected = new ArrayList<String>();
    for (int i = 0; i < openQuotes.length(); i++) {
      char open = openQuotes.charAt(i);
      char close = closeQuotes.charAt(i);
      expected.add(open + "-" + close);
      actual.add(open + "-" + PerlString.getQuoteCloseChar(open));
    }
    assertEquals(String.join("\n", expected), String.join("\n", actual));
  }

  @Test
  public void testSuggestOpenQuoteChar() {
    doTestSuggest("this is a test", '"', '"');
    doTestSuggest("this is a test", '`', '`');
    doTestSuggest("this is a test", '\'', '\'');
    doTestSuggest("this is a test", 't', '<');
    doTestSuggest("this i < s a test", 't', '[');
    doTestSuggest("this i > s a test", 't', '[');
    doTestSuggest("this i <[ s a test", 't', '{');
    doTestSuggest("this i <] s a test", 't', '{');
    doTestSuggest("this i >]} s a test", 't', '(');
    doTestSuggest("this i >]{ s a test", 't', '(');
    doTestSuggest("this i >]{) s a test", 't', '/');
    doTestSuggest("this i >]{)/ s a test", 't', '|');
    doTestSuggest("this i >]{)/| s a test", 't', '!');
    doTestSuggest("this i >]{)/|! s a test", 't', '~');
    doTestSuggest("this i >]{)/|!~ s a test", 't', '^');
    doTestSuggest("this i >]{)/|!~^ s a test", 't', '.');
    doTestSuggest("this i >]{)/|!~^. s a test", 't', '_');
    doTestSuggest("this i >]{)/|!~^._ s a test", 't', '=');
    doTestSuggest("this i >]{)/|!~^._= s a test", 't', '?');
    doTestSuggest("this i >]{)/|!~^._=? s a test", 't', '\\');
    doTestSuggest("this i >]{)/|!~^._=?\\ s a test", 't', ';');
    doTestSuggest("this i >]{)/|!~^._=?\\; s a test", 't', (char)0);
  }

  private static void doTestSuggest(@NotNull String text, char defaultQuote, char expectedQuote) {
    assertEquals("Unexpected quote returned for: '" + text + "' and default quote " + defaultQuote, expectedQuote,
                 PerlString.suggestOpenQuoteChar(text, defaultQuote));
  }

  @Test
  public void testLooksLikePackage() {
    assertPackage("::Foo");
    assertPackage("::'Foo");
    assertPackage("'Foo::Bar");
    assertPackage("Foo::Bar");
    assertPackage("Foo::::Bar");
    assertPackage("Foo::'Bar");
    assertPackage("Foo'Bar");
    assertPackage("Раз::'Два");
    assertPackage("Раз'Два");
    assertPackage("::Foo::Bar");
    assertPackage("Encode::KR::2022_KR");

    assertNotPackage("Foo:::Bar");
    assertNotPackage("Foo'::Bar");
    assertNotPackage("Foo:Bar");
    assertNotPackage("Foo:'Bar");
    assertNotPackage("!");
    assertNotPackage("Foo");
    assertNotPackage("Foo::");
    assertNotPackage("");
    assertNotPackage(null);
  }

  private void assertPackage(@NotNull String content) {
    assertTrue("Expected to look like a package: " + content, PerlString.looksLikePackage(content));
  }

  private void assertNotPackage(@Nullable String content) {
    assertFalse("Expected not to look like a package: " + content, PerlString.looksLikePackage(content));
  }

  @Test
  public void testContentFileNameLinux() {
    assertFileName(".gitignore", ".gitignore");
    assertFileName("./.gitignore", ".gitignore");
    assertFileName("/.gitignore", ".gitignore");
    assertFileName("c:/.gitignore", ".gitignore");
    assertFileName("../.gitignore", ".gitignore");
    assertFileName("./sub/pa-th/.gitignore", ".gitignore");
    assertFileName("/sub/pa-th/.gitignore", ".gitignore");
    assertFileName("../sub/pa-th/.gitignore", ".gitignore");
    assertFileName("c:/sub/pa-th/.gitignore", ".gitignore");

    assertFileName("gitignore", "gitignore");
    assertFileName("./gitignore", "gitignore");
    assertFileName("/gitignore", "gitignore");
    assertFileName("c:/gitignore", "gitignore");
    assertFileName("../gitignore", "gitignore");
    assertFileName("./sub/pa-th/gitignore", "gitignore");
    assertFileName("/sub/pa-th/gitignore", "gitignore");
    assertFileName("../sub/pa-th/gitignore", "gitignore");
    assertFileName("c:/sub/pa-th/gitignore", "gitignore");

    assertFileName("gitignore.foo", "gitignore.foo");
    assertFileName("./gitignore.foo", "gitignore.foo");
    assertFileName("/gitignore.foo", "gitignore.foo");
    assertFileName("c:/gitignore.foo", "gitignore.foo");
    assertFileName("../gitignore.foo", "gitignore.foo");
    assertFileName("./sub/pa-th/gitignore.foo", "gitignore.foo");
    assertFileName("/sub/pa-th/gitignore.foo", "gitignore.foo");
    assertFileName("../sub/pa-th/gitignore.foo", "gitignore.foo");
    assertFileName("c:/sub/pa-th/gitignore.foo", "gitignore.foo");

    assertFileName("gitignore.foo.bar", "gitignore.foo.bar");
    assertFileName("./gitignore.foo.bar", "gitignore.foo.bar");
    assertFileName("/gitignore.foo.bar", "gitignore.foo.bar");
    assertFileName("c:/gitignore.foo.bar", "gitignore.foo.bar");
    assertFileName("../gitignore.foo.bar", "gitignore.foo.bar");
    assertFileName("./sub/pa-th/gitignore.foo.bar", "gitignore.foo.bar");
    assertFileName("/sub/pa-th/gitignore.foo.bar", "gitignore.foo.bar");
    assertFileName("../sub/pa-th/gitignore.foo.bar", "gitignore.foo.bar");
    assertFileName("c:/sub/pa-th/gitignore.foo.bar", "gitignore.foo.bar");

    assertFileName(".gitignore/", ".gitignore");
    assertFileName("./.gitignore/", ".gitignore");
    assertFileName("/.gitignore/", ".gitignore");
    assertFileName("c:/.gitignore/", ".gitignore");
    assertFileName("../.gitignore/", ".gitignore");
    assertFileName("./sub/pa-th/.gitignore/", ".gitignore");
    assertFileName("/sub/pa-th/.gitignore/", ".gitignore");
    assertFileName("../sub/pa-th/.gitignore/", ".gitignore");
    assertFileName("c:/sub/pa-th/.gitignore/", ".gitignore");

    assertFileName("gitignore/", "gitignore");
    assertFileName("./gitignore/", "gitignore");
    assertFileName("/gitignore/", "gitignore");
    assertFileName("c:/gitignore/", "gitignore");
    assertFileName("../gitignore/", "gitignore");
    assertFileName("./sub/pa-th/gitignore/", "gitignore");
    assertFileName("/sub/pa-th/gitignore/", "gitignore");
    assertFileName("../sub/pa-th/gitignore/", "gitignore");
    assertFileName("c:/sub/pa-th/gitignore/", "gitignore");

    assertFileName("gitignore.foo/", "gitignore.foo");
    assertFileName("./gitignore.foo/", "gitignore.foo");
    assertFileName("/gitignore.foo/", "gitignore.foo");
    assertFileName("c:/gitignore.foo/", "gitignore.foo");
    assertFileName("../gitignore.foo/", "gitignore.foo");
    assertFileName("./sub/pa-th/gitignore.foo/", "gitignore.foo");
    assertFileName("/sub/pa-th/gitignore.foo/", "gitignore.foo");
    assertFileName("../sub/pa-th/gitignore.foo/", "gitignore.foo");
    assertFileName("c:/sub/pa-th/gitignore.foo/", "gitignore.foo");

    assertFileName("gitignore.foo.bar/", "gitignore.foo.bar");
    assertFileName("./gitignore.foo.bar/", "gitignore.foo.bar");
    assertFileName("/gitignore.foo.bar/", "gitignore.foo.bar");
    assertFileName("c:/gitignore.foo.bar/", "gitignore.foo.bar");
    assertFileName("../gitignore.foo.bar/", "gitignore.foo.bar");
    assertFileName("./sub/pa-th/gitignore.foo.bar/", "gitignore.foo.bar");
    assertFileName("/sub/pa-th/gitignore.foo.bar/", "gitignore.foo.bar");
    assertFileName("../sub/pa-th/gitignore.foo.bar/", "gitignore.foo.bar");
    assertFileName("c:/sub/pa-th/gitignore.foo.bar/", "gitignore.foo.bar");

    assertFileName(".gitignore", ".gitignore");
    assertFileName("./.gitignore", ".gitignore");
    assertFileName("/.gitignore", ".gitignore");
    assertFileName("c:/.gitignore", ".gitignore");
    assertFileName("../.gitignore", ".gitignore");
    assertFileName("./sub/pa-th/.gitignore", ".gitignore");
    assertFileName("/sub/pa-th/.gitignore", ".gitignore");
    assertFileName("../sub/pa-th/.gitignore", ".gitignore");
    assertFileName("c:/sub/pa-th/.gitignore", ".gitignore");

    assertFileName("git ignore", "git ignore");
    assertFileName("./git ignore", "git ignore");
    assertFileName("/git ignore", "git ignore");
    assertFileName("c:/git ignore", "git ignore");
    assertFileName("../git ignore", "git ignore");
    assertFileName("./sub/pa th/git ignore", "git ignore");
    assertFileName("/sub/pa th/git ignore", "git ignore");
    assertFileName("../sub/pa th/git ignore", "git ignore");
    assertFileName("c:/sub/pa th/git ignore", "git ignore");

    assertFileName("git ignore.foo", "git ignore.foo");
    assertFileName("./git ignore.foo", "git ignore.foo");
    assertFileName("/git ignore.foo", "git ignore.foo");
    assertFileName("c:/git ignore.foo", "git ignore.foo");
    assertFileName("../git ignore.foo", "git ignore.foo");
    assertFileName("./sub/pa th/git ignore.foo", "git ignore.foo");
    assertFileName("/sub/pa th/git ignore.foo", "git ignore.foo");
    assertFileName("../sub/pa th/git ignore.foo", "git ignore.foo");
    assertFileName("c:/sub/pa th/git ignore.foo", "git ignore.foo");

    assertFileName("git ignore.foo.bar", "git ignore.foo.bar");
    assertFileName("./git ignore.foo.bar", "git ignore.foo.bar");
    assertFileName("/git ignore.foo.bar", "git ignore.foo.bar");
    assertFileName("c:/git ignore.foo.bar", "git ignore.foo.bar");
    assertFileName("../git ignore.foo.bar", "git ignore.foo.bar");
    assertFileName("./sub/pa th/git ignore.foo.bar", "git ignore.foo.bar");
    assertFileName("/sub/pa th/git ignore.foo.bar", "git ignore.foo.bar");
    assertFileName("../sub/pa th/git ignore.foo.bar", "git ignore.foo.bar");
    assertFileName("c:/sub/pa th/git ignore.foo.bar", "git ignore.foo.bar");

    assertFileName(".git ignore/", ".git ignore");
    assertFileName("./.git ignore/", ".git ignore");
    assertFileName("/.git ignore/", ".git ignore");
    assertFileName("c:/.git ignore/", ".git ignore");
    assertFileName("../.git ignore/", ".git ignore");
    assertFileName("./sub/pa th/.git ignore/", ".git ignore");
    assertFileName("/sub/pa th/.git ignore/", ".git ignore");
    assertFileName("../sub/pa th/.git ignore/", ".git ignore");
    assertFileName("c:/sub/pa th/.git ignore/", ".git ignore");

    assertFileName("git ignore/", "git ignore");
    assertFileName("./git ignore/", "git ignore");
    assertFileName("/git ignore/", "git ignore");
    assertFileName("c:/git ignore/", "git ignore");
    assertFileName("../git ignore/", "git ignore");
    assertFileName("./sub/pa th/git ignore/", "git ignore");
    assertFileName("/sub/pa th/git ignore/", "git ignore");
    assertFileName("../sub/pa th/git ignore/", "git ignore");
    assertFileName("c:/sub/pa th/git ignore/", "git ignore");

    assertFileName("git ignore.foo/", "git ignore.foo");
    assertFileName("./git ignore.foo/", "git ignore.foo");
    assertFileName("/git ignore.foo/", "git ignore.foo");
    assertFileName("c:/git ignore.foo/", "git ignore.foo");
    assertFileName("../git ignore.foo/", "git ignore.foo");
    assertFileName("./sub/pa th/git ignore.foo/", "git ignore.foo");
    assertFileName("/sub/pa th/git ignore.foo/", "git ignore.foo");
    assertFileName("../sub/pa th/git ignore.foo/", "git ignore.foo");
    assertFileName("c:/sub/pa th/git ignore.foo/", "git ignore.foo");

    assertFileName("git ignore.foo.bar/", "git ignore.foo.bar");
    assertFileName("./git ignore.foo.bar/", "git ignore.foo.bar");
    assertFileName("/git ignore.foo.bar/", "git ignore.foo.bar");
    assertFileName("c:/git ignore.foo.bar/", "git ignore.foo.bar");
    assertFileName("../git ignore.foo.bar/", "git ignore.foo.bar");
    assertFileName("./sub/pa th/git ignore.foo.bar/", "git ignore.foo.bar");
    assertFileName("/sub/pa th/git ignore.foo.bar/", "git ignore.foo.bar");
    assertFileName("../sub/pa th/git ignore.foo.bar/", "git ignore.foo.bar");
    assertFileName("c:/sub/pa th/git ignore.foo.bar/", "git ignore.foo.bar");
  }

  @Test
  public void testContentFileNameWindows() {
    assertFileName(".gitignore", ".gitignore");
    assertFileName(".\\.gitignore", ".gitignore");
    assertFileName("c:\\.gitignore", ".gitignore");
    assertFileName("..\\.gitignore", ".gitignore");
    assertFileName(".\\sub\\pa-th\\.gitignore", ".gitignore");
    assertFileName("..\\sub\\pa-th\\.gitignore", ".gitignore");
    assertFileName("c:\\sub\\pa-th\\.gitignore", ".gitignore");

    assertFileName("gitignore", "gitignore");
    assertFileName(".\\gitignore", "gitignore");
    assertFileName("c:\\gitignore", "gitignore");
    assertFileName("..\\gitignore", "gitignore");
    assertFileName(".\\sub\\pa-th\\gitignore", "gitignore");
    assertFileName("..\\sub\\pa-th\\gitignore", "gitignore");
    assertFileName("c:\\sub\\pa-th\\gitignore", "gitignore");

    assertFileName("gitignore.foo", "gitignore.foo");
    assertFileName(".\\gitignore.foo", "gitignore.foo");
    assertFileName("c:\\gitignore.foo", "gitignore.foo");
    assertFileName("..\\gitignore.foo", "gitignore.foo");
    assertFileName(".\\sub\\pa-th\\gitignore.foo", "gitignore.foo");
    assertFileName("..\\sub\\pa-th\\gitignore.foo", "gitignore.foo");
    assertFileName("c:\\sub\\pa-th\\gitignore.foo", "gitignore.foo");

    assertFileName("gitignore.foo.bar", "gitignore.foo.bar");
    assertFileName(".\\gitignore.foo.bar", "gitignore.foo.bar");
    assertFileName("c:\\gitignore.foo.bar", "gitignore.foo.bar");
    assertFileName("..\\gitignore.foo.bar", "gitignore.foo.bar");
    assertFileName(".\\sub\\pa-th\\gitignore.foo.bar", "gitignore.foo.bar");
    assertFileName("..\\sub\\pa-th\\gitignore.foo.bar", "gitignore.foo.bar");
    assertFileName("c:\\sub\\pa-th\\gitignore.foo.bar", "gitignore.foo.bar");

    assertFileName(".gitignore\\\\", ".gitignore");
    assertFileName(".\\.gitignore\\\\", ".gitignore");
    assertFileName("c:\\.gitignore\\\\", ".gitignore");
    assertFileName("..\\.gitignore\\\\", ".gitignore");
    assertFileName(".\\sub\\pa-th\\.gitignore\\\\", ".gitignore");
    assertFileName("..\\sub\\pa-th\\.gitignore\\\\", ".gitignore");
    assertFileName("c:\\sub\\pa-th\\.gitignore\\\\", ".gitignore");

    assertFileName("gitignore\\\\", "gitignore");
    assertFileName(".\\gitignore\\\\", "gitignore");
    assertFileName("c:\\gitignore\\\\", "gitignore");
    assertFileName("..\\gitignore\\\\", "gitignore");
    assertFileName(".\\sub\\pa-th\\gitignore\\\\", "gitignore");
    assertFileName("..\\sub\\pa-th\\gitignore\\\\", "gitignore");
    assertFileName("c:\\sub\\pa-th\\gitignore\\\\", "gitignore");

    assertFileName("gitignore.foo\\\\", "gitignore.foo");
    assertFileName(".\\gitignore.foo\\\\", "gitignore.foo");
    assertFileName("c:\\gitignore.foo\\\\", "gitignore.foo");
    assertFileName("..\\gitignore.foo\\\\", "gitignore.foo");
    assertFileName(".\\sub\\pa-th\\gitignore.foo\\\\", "gitignore.foo");
    assertFileName("..\\sub\\pa-th\\gitignore.foo\\\\", "gitignore.foo");
    assertFileName("c:\\sub\\pa-th\\gitignore.foo\\\\", "gitignore.foo");

    assertFileName("gitignore.foo.bar\\\\", "gitignore.foo.bar");
    assertFileName(".\\gitignore.foo.bar\\\\", "gitignore.foo.bar");
    assertFileName("c:\\gitignore.foo.bar\\\\", "gitignore.foo.bar");
    assertFileName("..\\gitignore.foo.bar\\\\", "gitignore.foo.bar");
    assertFileName(".\\sub\\pa-th\\gitignore.foo.bar\\\\", "gitignore.foo.bar");
    assertFileName("..\\sub\\pa-th\\gitignore.foo.bar\\\\", "gitignore.foo.bar");
    assertFileName("c:\\sub\\pa-th\\gitignore.foo.bar\\\\", "gitignore.foo.bar");

    assertFileName(".git ignore", ".git ignore");
    assertFileName(".\\.git ignore", ".git ignore");
    assertFileName("c:\\.git ignore", ".git ignore");
    assertFileName("..\\.git ignore", ".git ignore");
    assertFileName(".\\sub\\pa th\\.git ignore", ".git ignore");
    assertFileName("..\\sub\\pa th\\.git ignore", ".git ignore");
    assertFileName("c:\\sub\\pa th\\.git ignore", ".git ignore");

    assertFileName("git ignore", "git ignore");
    assertFileName(".\\git ignore", "git ignore");
    assertFileName("c:\\git ignore", "git ignore");
    assertFileName("..\\git ignore", "git ignore");
    assertFileName(".\\sub\\pa th\\git ignore", "git ignore");
    assertFileName("..\\sub\\pa th\\git ignore", "git ignore");
    assertFileName("c:\\sub\\pa th\\git ignore", "git ignore");

    assertFileName("git ignore.foo", "git ignore.foo");
    assertFileName(".\\git ignore.foo", "git ignore.foo");
    assertFileName("c:\\git ignore.foo", "git ignore.foo");
    assertFileName("..\\git ignore.foo", "git ignore.foo");
    assertFileName(".\\sub\\pa th\\git ignore.foo", "git ignore.foo");
    assertFileName("..\\sub\\pa th\\git ignore.foo", "git ignore.foo");
    assertFileName("c:\\sub\\pa th\\git ignore.foo", "git ignore.foo");

    assertFileName("git ignore.foo.bar", "git ignore.foo.bar");
    assertFileName(".\\git ignore.foo.bar", "git ignore.foo.bar");
    assertFileName("c:\\git ignore.foo.bar", "git ignore.foo.bar");
    assertFileName("..\\git ignore.foo.bar", "git ignore.foo.bar");
    assertFileName(".\\sub\\pa th\\git ignore.foo.bar", "git ignore.foo.bar");
    assertFileName("..\\sub\\pa th\\git ignore.foo.bar", "git ignore.foo.bar");
    assertFileName("c:\\sub\\pa th\\git ignore.foo.bar", "git ignore.foo.bar");

    assertFileName(".git ignore\\\\", ".git ignore");
    assertFileName(".\\.git ignore\\\\", ".git ignore");
    assertFileName("c:\\.git ignore\\\\", ".git ignore");
    assertFileName("..\\.git ignore\\\\", ".git ignore");
    assertFileName(".\\sub\\pa th\\.git ignore\\\\", ".git ignore");
    assertFileName("..\\sub\\pa th\\.git ignore\\\\", ".git ignore");
    assertFileName("c:\\sub\\pa th\\.git ignore\\\\", ".git ignore");

    assertFileName("git ignore\\\\", "git ignore");
    assertFileName(".\\git ignore\\\\", "git ignore");
    assertFileName("c:\\git ignore\\\\", "git ignore");
    assertFileName("..\\git ignore\\\\", "git ignore");
    assertFileName(".\\sub\\pa th\\git ignore\\\\", "git ignore");
    assertFileName("..\\sub\\pa th\\git ignore\\\\", "git ignore");
    assertFileName("c:\\sub\\pa th\\git ignore\\\\", "git ignore");

    assertFileName("git ignore.foo\\\\", "git ignore.foo");
    assertFileName(".\\git ignore.foo\\\\", "git ignore.foo");
    assertFileName("c:\\git ignore.foo\\\\", "git ignore.foo");
    assertFileName("..\\git ignore.foo\\\\", "git ignore.foo");
    assertFileName(".\\sub\\pa th\\git ignore.foo\\\\", "git ignore.foo");
    assertFileName("..\\sub\\pa th\\git ignore.foo\\\\", "git ignore.foo");
    assertFileName("c:\\sub\\pa th\\git ignore.foo\\\\", "git ignore.foo");

    assertFileName("git ignore.foo.bar\\\\", "git ignore.foo.bar");
    assertFileName(".\\git ignore.foo.bar\\\\", "git ignore.foo.bar");
    assertFileName("c:\\git ignore.foo.bar\\\\", "git ignore.foo.bar");
    assertFileName("..\\git ignore.foo.bar\\\\", "git ignore.foo.bar");
    assertFileName(".\\sub\\pa th\\git ignore.foo.bar\\\\", "git ignore.foo.bar");
    assertFileName("..\\sub\\pa th\\git ignore.foo.bar\\\\", "git ignore.foo.bar");
    assertFileName("c:\\sub\\pa th\\git ignore.foo.bar\\\\", "git ignore.foo.bar");
  }

  private void assertFileName(@NotNull String content, @Nullable String expected) {
    assertEquals("Unexpected filename from: " + content, expected, PerlString.getContentFileName(content));
  }
}
