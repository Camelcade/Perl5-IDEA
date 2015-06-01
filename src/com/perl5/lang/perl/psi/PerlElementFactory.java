/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.PerlFileTypePackage;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.impl.*;

public class PerlElementFactory
{
	public static PerlNamespaceImpl createPackageName(Project project, String name)
	{
		PerlFileImpl file = createFile(project, "package " + name + ";");
		PerlNamespaceDefinition def = PsiTreeUtil.findChildOfType(file, PerlNamespaceDefinition.class);
		assert def != null;
		return (PerlNamespaceImpl)def.getNamespace();
	}

	public static PerlFunction createUserFunction(Project project, String name)
	{
		PerlFileImpl file = createFile(project, "sub " + name + ";");
		PerlSubDeclaration decl = PsiTreeUtil.findChildOfType(file, PerlSubDeclaration.class);
		assert decl != null;
		return decl.getUserFunction();
	}

	public static PerlVariableName createVariableName(Project project, String name)
	{
		PerlFileImpl file = createFile(project, "$" + name + ";");
		PerlPerlScalar scalar = PsiTreeUtil.findChildOfType(file, PerlPerlScalar.class);
		assert scalar != null;
		return scalar.getVariableName();
	}

	public static PerlHeredocTerminatorImpl createHereDocTerminator(Project project, String name)
	{
		PerlFileImpl file = createFile(project, "<<'" + name + "';\n"+name+"\n");
		return PsiTreeUtil.findChildOfType(file, PerlHeredocTerminatorImpl.class);
	}

	public static PerlStringContentImpl createStringContent(Project project, String name)
	{
		PerlFileImpl file = createFile(project, "'"+name+"';");
		PerlStringSq string = PsiTreeUtil.findChildOfType(file, PerlStringSq.class);
		assert string != null;
		return (PerlStringContentImpl) string.getFirstChild().getNextSibling();
	}

	public static PerlFileImpl createFile(Project project, String text)
	{
		String fileName = "file.dummy";
		return (PerlFileImpl) PsiFileFactory.getInstance(project).
				createFileFromText(fileName, PerlFileTypePackage.INSTANCE, text);
	}

}
