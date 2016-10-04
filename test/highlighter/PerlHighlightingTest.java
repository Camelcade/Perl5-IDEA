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

package highlighter;

import base.PerlLightCodeInsightFixtureTestCase;

/**
 * Created by hurricup on 04.10.2016.
 */
public class PerlHighlightingTest extends PerlLightCodeInsightFixtureTestCase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/highlighting";
	}

	public void testVariables()
	{
		doTest();
	}

	private void doTest()
	{
//		myFixture.testHighlighting(true, true, true, getTestName(true) + ".pl");
	}

/*
	@Override
	protected String getTestDataPath()
	{
		return "testData/parser/performance";
	}

	// manual performance test
	public void testPerformance() throws IOException
	{
		final Project project = getProject();
		// 35 seconds
		for (int i = 0; i < 5; i++)
		{
			initWithFile("perltidy" + i, "pl", "perltidy.code");
			PsiDocumentManager.getInstance(project).commitAllDocuments();
			CodeInsightTestFixtureImpl.ensureIndexesUpToDate(project);
			long startTime = System.currentTimeMillis();
			List<HighlightInfo> highlightInfos = doHighlight();
			long endTime = System.currentTimeMillis();
			System.err.println("Pass " + i  + " done in " + (endTime - startTime));
		}
	}

	private List<HighlightInfo> doHighlight()
	{
		PsiFile file = getFile();
		Editor editor = getEditor();
		Project project = getProject();
		DaemonCodeAnalyzerImpl codeAnalyzer = (DaemonCodeAnalyzerImpl) DaemonCodeAnalyzer.getInstance(project);
		TextEditor textEditor = TextEditorProvider.getInstance().getTextEditor(editor);

		List<HighlightInfo> infos = new ArrayList<HighlightInfo>();
		try
		{
			infos.addAll(codeAnalyzer.runPasses(file, editor.getDocument(), textEditor, ArrayUtil.EMPTY_INT_ARRAY, false, null));
			infos.addAll(DaemonCodeAnalyzerEx.getInstanceEx(project).getFileLevelHighlights(project, file));
		}
		catch (ProcessCanceledException e)
		{
			PsiDocumentManager.getInstance(project).commitAllDocuments();
			UIUtil.dispatchAllInvocationEvents();
			throw e;
		}
		return infos;
	}
*/
}
