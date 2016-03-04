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

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.perl5.lang.perl.psi.PerlLabel;

/**
 * Created by hurricup on 04.03.2016.
 */
public class PerlLabelResolveTest extends PerlLightCodeInsightFixtureTestCase
{
	@Override
	protected String getTestDataPath()
	{
		return "testData/resolve/labels";
	}

	public void testNextOutAnonSub()
	{
		doTest("next_label_out_anon_sub", false);
	}

	public void testNextOutDo()
	{
		doTest("next_label_out_do", false);
	}

	public void testNextOutEval()
	{
		doTest("next_label_out_eval", false);
	}

	public void testNextOutGrep()
	{
		doTest("next_label_out_grep", false);
	}

	public void testNextOutMap()
	{
		doTest("next_label_out_map", false);
	}

	public void testNextOutSort()
	{
		doTest("next_label_out_sort", false);
	}

	public void testNextOutSub()
	{
		doTest("next_label_out_sub", false);
	}

	public void testNextInAnonSub()
	{
		doTest("next_label_in_anon_sub", true);
	}

	public void testNextInGrep()
	{
		doTest("next_label_in_grep", true);
	}

	public void testNextInMap()
	{
		doTest("next_label_in_map", true);
	}

	public void testNextInSort()
	{
		doTest("next_label_in_sort", true);
	}

	public void testNextInSub()
	{
		doTest("next_label_in_sub", true);
	}

	public void testNextLabeledBlock()
	{
		doTest("next_labeled_block", true);
	}

	public void testRedoLabeledBlock()
	{
		doTest("redo_labeled_block", true);
	}

	public void testLastLabeledBlock()
	{
		doTest("last_labeled_block", true);
	}

	public void testNextLabelBeforePod()
	{
		doTest("next_label_before_pod", true);
	}

	public void testNextLabelBeforeComment()
	{
		doTest("next_label_before_comment", true);
	}

	public void testNextLabelOtherStatement()
	{
		doTest("next_label_other_statement", false);
	}

	public void doTest(String filename, boolean success)
	{
		initWithFileAsScript(filename);
		PsiElement element = getElementAtCaret(PerlLabel.class);
		assertNotNull(element);
		PsiReference reference = element.getReference();
		assertNotNull(reference);
		if (success)
		{
			assertNotNull(reference.resolve());
		}
		else
		{
			assertNull(reference.resolve());
		}
	}
}
