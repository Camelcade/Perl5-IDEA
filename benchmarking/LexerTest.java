package benchmarking;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.IdeaTestCase;
import com.intellij.testFramework.LexerTestCase;
import com.perl5.lang.perl.lexer.PerlLexerAdapter;
import junit.framework.TestCase;
import net.sf.cglib.core.Local;

import java.io.File;

/**
 * Created by hurricup on 03.06.2015.
 */
public class LexerTest extends LexerTestCase
{
	public void testLexer() throws Exception
	{
		doFileTest("pm");
	}

	@Override
	protected Lexer createLexer()
	{
		return new PerlLexerAdapter(null);
	}

	@Override
	protected void doTest(String text, String expected, Lexer lexer)
	{
		System.err.println("Printing tokens 1000 times " + text.length());
		long startTime = System.currentTimeMillis();
		int tokensNumber = 0;

		for( int i = 0; i < 1000; i++)
		{
			lexer.start(text, 0, text.length());
			while( lexer.getTokenType() != null)
			{
				lexer.advance();
				lexer.getTokenText();
				tokensNumber++;
			}
		}
		long stopTime = System.currentTimeMillis();
		System.out.println(" Time :" + ( stopTime - startTime )/1000 + ", total tokens: " + tokensNumber);
		super.doTest(text, expected, lexer);
	}

	@Override
	protected String getDirPath()
	{
		return "superdir";
	}
}
