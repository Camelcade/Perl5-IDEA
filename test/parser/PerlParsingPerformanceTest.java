/*
 * Copyright 2016 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package parser;

import categories.Performance;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.testFramework.PlatformTestUtil;
import com.intellij.util.ThrowableRunnable;
import gnu.trove.THashMap;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.*;

/**
 * Created by hurricup on 02.10.2016.
 */
@Category(Performance.class)
public class PerlParsingPerformanceTest extends PerlParserTestBase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/parser/performance";
	}

	public void testPerlTidyParsing()
	{

		final String testData;
		try
		{
			testData = FileUtil.loadFile(new File("testData", "perlTidy.code"), CharsetToolkit.UTF8, true).trim();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}

		final int iterations = 30;

		PsiFile psiFile = null;
		for (int i = 0; i < iterations; i++)
		{
			psiFile = createPsiFile("test", testData);
			psiFile.getFirstChild();
		}

		PlatformTestUtil.startPerformanceTest("PerlTidy parsing", iterations * 400, new ThrowableRunnable()
		{
			@Override
			public void run() throws Throwable
			{
				long start = System.currentTimeMillis();
				for (int i = 0; i < iterations; i++)
				{
					PsiFile psiFile = createPsiFile("mytest", testData);
					psiFile.getFirstChild();
				}
				long length = System.currentTimeMillis() - start;
				System.err.println("Parsing done in " + length / iterations + " ms per iteration");
			}
		}).cpuBound().assertTiming();

//		analyzeFile(psiFile);
	}

	private static final TokenSet TERMINAL_TOKENS = TokenSet.create(
			// variable
			SCALAR_VARIABLE,
			SCALAR_CAST_EXPR,
			UNDEF_EXPR,
			SCALAR_INDEX_CAST_EXPR,
			ARRAY_INDEX_VARIABLE,

			ARRAY_VARIABLE,
			ARRAY_CAST_EXPR,
			STRING_LIST,
			ARRAY_ARRAY_SLICE,
			ARRAY_HASH_SLICE,

			HASH_VARIABLE,
			HASH_CAST_EXPR,

			GLOB_VARIABLE,

			// declarations
			VARIABLE_DECLARATION_GLOBAL,
			VARIABLE_DECLARATION_LEXICAL,
			VARIABLE_DECLARATION_LOCAL,

			// constants
			NUMBER_CONSTANT,
			TAG_SCALAR,
			STRING_BARE,
			STRING_SQ,
			STRING_DQ,
			STRING_XQ,
			HEREDOC_OPENER,

			// list or element
			PARENTHESISED_EXPR,
			ANON_ARRAY_ELEMENT,

			//regexps
			COMPILE_REGEX,
			REPLACEMENT_REGEX,
			TR_REGEX,
			MATCH_REGEX,

			// exprs
			DO_EXPR,
			EVAL_EXPR,
			GREP_EXPR,
			RETURN_EXPR,
			MAP_EXPR,
			SORT_EXPR,

			FILE_READ_EXPR,
			PRINT_EXPR,
			UNDEF_EXPR,
			PERL_HANDLE_EXPR,
			ANON_ARRAY,
			ANON_HASH,
			SUB_EXPR,

			//calls
			SUB_CALL_EXPR,
			NAMESPACE_EXPR
	);

	private void analyzeFile(PsiFile psiFile)
	{
		final Map<IElementType, Integer> tokensMap = new THashMap<IElementType, Integer>();
		final int[] totalTokens = new int[]{0};

		psiFile.accept(new PsiElementVisitor()
		{
			@Override
			public void visitElement(PsiElement element)
			{
				IElementType elementType = PsiUtilCore.getElementType(element);
				if (TERMINAL_TOKENS.contains(elementType))
				{
					Integer count = tokensMap.get(elementType);
					tokensMap.put(elementType, count == null ? 1 : count + 1);
					totalTokens[0]++;
				}
				element.acceptChildren(this);
				super.visitElement(element);
			}
		});

		List<Map.Entry<IElementType, Integer>> entries = new ArrayList<Map.Entry<IElementType, Integer>>(tokensMap.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<IElementType, Integer>>()
		{
			@Override
			public int compare(Map.Entry<IElementType, Integer> o1, Map.Entry<IElementType, Integer> o2)
			{
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		for (Map.Entry<IElementType, Integer> entry : entries)
		{
			Integer elementCount = entry.getValue();
			float percent = (float) elementCount / totalTokens[0];
			System.err.println(entry.getKey() + ": " + elementCount + " " + percent * 100 + "%");
		}
	}
}
