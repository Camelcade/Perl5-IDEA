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

package base;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.picocontainer.MutablePicoContainer;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by hurricup on 04.03.2016.
 */
public abstract class PerlLightCodeInsightFixtureTestCase extends LightCodeInsightFixtureTestCase
{
	private static final String START_FOLD = "<fold\\stext=\'[^\']*\'(\\sexpand=\'[^\']*\')*>";
	private static final String END_FOLD = "</fold>";

	public static Application getApplication()
	{
		return ApplicationManager.getApplication();
	}

	public String getFileExtension()
	{
		return "pl";
	}

	@Override
	protected void setUp() throws Exception
	{
		VfsRootAccess.SHOULD_PERFORM_ACCESS_CHECK = false; // TODO: a workaround for v15
		super.setUp();
		registerApplicationService(PerlSharedSettings.class, new PerlSharedSettings());
	}

	protected <T> void registerApplicationService(final Class<T> aClass, T object)
	{
		final MutablePicoContainer container = ((MutablePicoContainer) getApplication().getPicoContainer());
		container.registerComponentImplementation(object, aClass);
//		container.registerComponentInDisposer(serviceImplementation);

		Disposer.register(getProject(), new Disposable()
		{
			@Override
			public void dispose()
			{
				container.unregisterComponent(aClass.getName());
			}
		});
	}

	public void initWithFileSmart(String filename)
	{
		try
		{
			initWithFile(filename, getFileExtension());
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void initWithTextSmart(String content)
	{
		try
		{
			initWithFileContent("test", getFileExtension(), content);
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Deprecated // use initWithFileSmart
	public void initWithFileAsScript(String filename)
	{
		try
		{
			initWithFile(filename, "pl");
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Deprecated  // use initWithFileSmart
	public void initWithFileAsHTMLMason(String filename)
	{
		try
		{
			initWithFile(filename, "mas");
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Deprecated  // use initWithFileSmart
	public void initWithFileAsPackage(String filename)
	{
		try
		{
			initWithFile(filename, "pm");
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Deprecated // use initWIthTextSmart
	public void initWithTextAsScript(String content)
	{
		try
		{
			initWithFileContent("test", "pl", content);
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	public void initWithFile(String filename, String extension) throws IOException
	{
		initWithFileContent(filename, extension, FileUtil.loadFile(new File(getTestDataPath(), filename + ".code"), CharsetToolkit.UTF8, true).trim());
	}

	public void initWithFileContent(String filename, String extension, String content) throws IOException
	{
		myFixture.configureByText(filename + "." + extension, content);
	}

	@NotNull
	protected <T extends PsiElement> T getElementAtCaret(@NotNull Class<T> clazz)
	{
		int offset = myFixture.getEditor().getCaretModel().getOffset();
		PsiElement focused = myFixture.getFile().findElementAt(offset);
		return ObjectUtils.assertNotNull(PsiTreeUtil.getParentOfType(focused, clazz, false));
	}

	@NotNull
	protected <T extends PsiElement> T getElementAtCaret(int caretIndex, @NotNull Class<T> clazz)
	{
		CaretModel caretModel = myFixture.getEditor().getCaretModel();
		assertTrue(caretModel.getCaretCount() > caretIndex);
		Caret caret = caretModel.getAllCarets().get(caretIndex);
		int offset = caret.getOffset();
		PsiElement focused = myFixture.getFile().findElementAt(offset);
		return ObjectUtils.assertNotNull(PsiTreeUtil.getParentOfType(focused, clazz, false));
	}

	protected void testFoldingRegions(@NotNull String verificationFileName, LanguageFileType fileType)
	{
		testFoldingRegions(verificationFileName, false, fileType);
	}

	protected void testFoldingRegions(@NotNull String verificationFileName, boolean doCheckCollapseStatus, LanguageFileType fileType)
	{
		String expectedContent;
		try
		{
			expectedContent = FileUtil.loadFile(new File(getTestDataPath() + "/" + verificationFileName + ".code"));
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		Assert.assertNotNull(expectedContent);

		expectedContent = StringUtil.replace(expectedContent, "\r", "");
		final String cleanContent = expectedContent.replaceAll(START_FOLD, "").replaceAll(END_FOLD, "");

		myFixture.configureByText(fileType, cleanContent);
		final String actual = ((CodeInsightTestFixtureImpl) myFixture).getFoldingDescription(doCheckCollapseStatus);

		Assert.assertEquals(expectedContent, actual);
	}

	protected void testSmartKeyFile(String filename, char typed)
	{
		initWithFileSmart(filename);
		myFixture.type(typed);
		checkResultByFile(filename);
	}

	protected void testSmartKey(String original, char typed, String expected)
	{
		initWithTextSmart(original);
		myFixture.type(typed);
		myFixture.checkResult(expected);
	}

	protected void testLiveTemplateFile(String filename)
	{
		initWithFileSmart(filename);
		myFixture.performEditorAction(IdeActions.ACTION_EXPAND_LIVE_TEMPLATE_BY_TAB);
		checkResultByFile(filename);
	}


	protected void checkResultByFile(String filenameWithoutExtension)
	{
		String checkFileName = filenameWithoutExtension + ".txt";


		myFixture.checkResultByFile(checkFileName);
	}

	public void assertContainsLookupElements(String... pattern)
	{
		assertContainsLookupElements(Arrays.asList(pattern));
	}

	public void assertContainsLookupElements(List<String> pattern)
	{
		List<String> strings = myFixture.getLookupElementStrings();
		assertNotNull(strings);
		assertContainsElements(new HashSet<String>(strings), pattern);
	}


	public void assertNotContainsLookupElements(String... pattern)
	{
		assertNotContainsLookupElements(Arrays.asList(pattern));
	}

	public void assertNotContainsLookupElements(List<String> pattern)
	{
		List<String> strings = myFixture.getLookupElementStrings();
		assertNotNull(strings);
		assertDoesntContain(new HashSet<Object>(strings), pattern);
	}

	protected void doFormatTest()
	{
		doFormatTest(getTestName(false));
	}

	protected void doFormatTest(String filename)
	{
		doFormatTest(filename, "");
	}

	protected void doFormatTest(String filename, String resultSuffix)
	{
		initWithFileSmart(filename);
		new WriteCommandAction.Simple(getProject())
		{
			@Override
			protected void run() throws Throwable
			{
				PsiFile file = myFixture.getFile();
				TextRange rangeToUse = file.getTextRange();
				CodeStyleManager.getInstance(getProject()).reformatText(file, rangeToUse.getStartOffset(), rangeToUse.getEndOffset());
				// 	CodeStyleManager.getInstance(getProject()).reformat(myFixture.getFile());
			}
		}.execute();

		String resultFileName = getTestDataPath() + "/" + filename + resultSuffix + ".txt";
		UsefulTestCase.assertSameLinesWithFile(resultFileName, myFixture.getFile().getText());
//		myFixture.checkResultByFile(resultFileName);
	}

}
