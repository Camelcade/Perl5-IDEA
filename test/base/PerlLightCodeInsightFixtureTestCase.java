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

import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;

/**
 * Created by hurricup on 04.03.2016.
 */
public abstract class PerlLightCodeInsightFixtureTestCase extends LightCodeInsightFixtureTestCase
{
	private static final String START_FOLD = "<fold\\stext=\'[^\']*\'(\\sexpand=\'[^\']*\')*>";
	private static final String END_FOLD = "</fold>";

	@Override
	protected void setUp() throws Exception
	{
		VfsRootAccess.SHOULD_PERFORM_ACCESS_CHECK = false; // TODO: a workaround for v15
		super.setUp();
	}

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

}
