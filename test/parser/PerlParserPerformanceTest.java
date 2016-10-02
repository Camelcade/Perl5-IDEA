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

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiUtilCore;
import gnu.trove.THashMap;

import java.io.IOException;
import java.util.*;

/**
 * Created by hurricup on 02.10.2016.
 */
public class PerlParserPerformanceTest extends PerlParserTestBase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/parser/performance";
	}

	public void testlabels_parsing()
	{
		myFileName = "perltidy";
		try
		{
			String testName = getTestName(false);
			String text = loadFile(testName + "." + myFileExt);
			long startTime = System.currentTimeMillis();

			PsiFile psiFile;

/*
			for (int i = 0; i < 50; i++)
			{
				psiFile = createPsiFile(testName, text);
				psiFile.getFirstChild();
			}
*/

			long endTime = System.currentTimeMillis();
			System.err.println("Done in " + (endTime - startTime) + " ms");

//			analyzeFile(psiFile);

		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void analyzeFile(PsiFile psiFile)
	{
		final Map<IElementType, Integer> tokensMap = new THashMap<IElementType, Integer>();

		psiFile.accept(new PsiElementVisitor()
		{
			@Override
			public void visitElement(PsiElement element)
			{
				IElementType elementType = PsiUtilCore.getElementType(element);
				Integer count = tokensMap.get(elementType);
				tokensMap.put(elementType, count == null ? 1 : count + 1);
				super.visitElement(element);
				element.acceptChildren(this);
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
			System.err.println(entry.getKey() + ": " + entry.getValue());
		}
	}
}
