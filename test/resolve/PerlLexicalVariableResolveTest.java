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

package resolve;

import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.PerlVariableDeclarationWrapper;
import com.perl5.lang.perl.psi.PerlVariableNameElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 18.02.2016.
 */
public class PerlLexicalVariableResolveTest extends LightCodeInsightFixtureTestCase
{
	public static final String DATA_PATH = "testData/resolve";

	@Override
	protected void setUp() throws Exception
	{
		VfsRootAccess.SHOULD_PERFORM_ACCESS_CHECK = false; // TODO: a workaround for v15
		super.setUp();
	}

	@Override
	protected String getTestDataPath()
	{
		return DATA_PATH;
	}

	public void testSimple()
	{
		doTest("variable_simple.pl", true);
	}

	public void testIfCondition()
	{
		doTest("variable_if_condition.pl", true);
	}

	public void testForIterator()
	{
		doTest("variable_for_iterator.pl", true);
	}

	public void testUseVars()
	{
		doTest("variable_use_vars.pl", true);
	}

	public void testIfElsifElse()
	{
		doTest("variable_if_elsif_else.pl", true);
	}

	public void testSubSignature()
	{
		doTest("variable_sub_signature.pl", true);
	}

	public void testMethodExplicitInvocant()
	{
		doTest("variable_method_explicit_invocant.pl", true);
	}

	public void testVariableInInvocation()
	{
		doTest("variable_in_call_expression.pl", true);
	}


	public void testMethodImplicitInvocant()
	{
		doTest("variable_method_implicit_invocant.pl", true);
	}

	public void testNegativeBlock()
	{
		doTest("negative_variable_block.pl", false);
	}

	public void testNegativeIfElse()
	{
		doTest("negative_if_else.pl", false);
	}

	public void doTest(String fileName, boolean shouldFind)
	{
		myFixture.configureByFile(fileName);
		PsiElement element = getElementAtCaret(PerlVariableNameElement.class);
		PsiReference reference = element.getReference();
		assertTrue(reference != null);
		PsiElement result = reference.resolve();

		if (shouldFind)
		{
			assertTrue(result != null);
			assertTrue(result instanceof PerlVariableDeclarationWrapper);
			PerlVariable variable = ((PerlVariableDeclarationWrapper) result).getVariable();
			assertEquals(variable.getName(), element.getText());
			assertEquals(variable.getActualType(), ((PerlVariable) element.getParent()).getActualType());
		}
		else
		{
			assertTrue(result == null);
		}
	}

	@NotNull
	protected <T extends PsiElement> T getElementAtCaret(@NotNull Class<T> clazz)
	{
		int offset = myFixture.getEditor().getCaretModel().getOffset();
		PsiElement focused = myFixture.getFile().findElementAt(offset);
		return ObjectUtils.assertNotNull(PsiTreeUtil.getParentOfType(focused, clazz, false));
	}

}
