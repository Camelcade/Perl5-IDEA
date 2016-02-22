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

package oop;

import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.psi.mixins.PerlNamespaceDefinitionImplMixin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hurricup on 22.02.2016.
 */
public class NamespaceParentsDetectionTest extends NamespaceTestCase
{
	public static final String DATA_PATH = "testData/oop/parents";

	@Override
	protected String getTestDataPath()
	{
		return DATA_PATH;
	}

	public void testIsaAssign()
	{
		doTest("isa_assign.pl", "Foo", new String[]{"superfoo1", "superbar1"});
	}

	public void testIsaDeclare()
	{
		doTest("isa_declare.pl", "Foo", new String[]{"superfoo2", "superbar2"});
	}

	public void testIsaDeclareParens()
	{
		doTest("isa_declare_parens.pl", "Foo", new String[]{"superfoo3", "superbar3"});
	}

	public void testBase()
	{
		doTest("base.pl", "Foo", new String[]{"superbase", "superbase2"});
	}
	public void testParent()
	{
		doTest("parent.pl", "Foo", new String[]{"superparent", "superparent2"});
	}
	public void testMojoBase()
	{
		doTest("mojo_base.pl", "Foo", new String[]{"Mojo::Base"});
	}
	public void testMojoBaseNoBase()
	{
		doTest("mojo_base_no_base.pl", "Foo", new String[]{});
	}
	public void testMojoBaseSpecific()
	{
		doTest("mojo_base_specific.pl", "Foo", new String[]{"somebase1"});
	}
	public void testMoo()
	{
		doTest("moo.pl", "Foo", new String[]{"Moo::Object"});
	}
	public void testMoose()
	{
		doTest("moose.pl", "Foo", new String[]{"Moose::Object"});
	}

	public void testRuntimeExtends()
	{
		doTest("runtime_extends.pl", "Foo", new String[]{"someparent"});
	}
	public void testRuntimeWith()
	{
		doTest("runtime_with.pl", "Foo", new String[]{"someparent1", "someparent2"});
	}
	public void testRuntimeExtendsWith()
	{
		doTest("runtime_extends_with.pl", "Foo", new String[]{"someparent", "someotherparent2", "someotherparent3", "someotherparent4", "someotherparent5"});
	}
	public void testRuntimeExtendsVsAll()
	{
		doTest("runtime_extends_vs_all.pl", "Foo", new String[]{"someparent"});
	}
	public void testRuntimeISAVsAll()
	{
		doTest("runtime_isa_vs_all.pl", "Foo", new String[]{"someisa"});
	}
	public void testCompileVsRuntime()
	{
		doTest("compile_vs_runtime.pl", "Foo", new String[]{"someisa", "someotherparent4", "someotherparent5"});
	}
	public void testParentWith()
	{
		doTest("parent_with.pl", "Foo", new String[]{"someparents", "someotherparent4", "someotherparent5"});
	}

	public void doTest(String fileName, @NotNull String namespaceName, String[] parentsList)
	{
		PerlNamespaceDefinitionImplMixin namespaceDefinition = getNamespaceInFile(fileName, namespaceName);
		List<String> parents = namespaceDefinition.getParentNamespacesNamesFromPsi();
		assertEquals(new ArrayList<String>(Arrays.asList(parentsList)), parents);
	}

}
