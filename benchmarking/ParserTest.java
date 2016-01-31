package benchmarking;

import com.intellij.psi.PsiFile;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.testFramework.ParsingTestCase;
import com.perl5.lang.perl.PerlParserDefinition;

/**
 * Created by hurricup on 03.06.2015.
 */
public class ParserTest extends ParsingTestCase
{
	public ParserTest()
	{
		super("", "pm", new PerlParserDefinition());
	}

	public void testParser() throws Exception
	{
		doTest(false);
	}

	@Override
	protected PsiFile createFile(LightVirtualFile virtualFile)
	{
		System.err.println("Parsing tree 1000 times ");
		long startTime = System.currentTimeMillis();

		for( int i = 0; i < 1000; i++)
		{
			PsiFile file = super.createFile(virtualFile);
		}
		long stopTime = System.currentTimeMillis();
		System.out.println(" Time :" + ( stopTime - startTime )/1000 );


		return super.createFile(virtualFile);
	}

	@Override
	protected String getTestDataPath()
	{
		return "C:\\Repository\\superdir\\";
	}
}
