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
	public static PerlNamespace createPackageName(Project project, String name)
	{
		PerlFileImpl file = createFile(project, "package " + name + ";");
		return PsiTreeUtil.findChildOfType(file, PerlNamespace.class);
	}

	public static PerlUserFunctionImpl createUserFunction(Project project, String name)
	{
		PerlFileImpl file = createFile(project, "sub " + name + ";");
		return PsiTreeUtil.findChildOfType(file, PerlUserFunctionImpl.class);
	}

	public static PerlVariableName createVariableName(Project project, String name)
	{
		PerlFileImpl file = createFile(project, "$" + name + ";");
		return PsiTreeUtil.findChildOfType(file, PerlVariableName.class);
	}

	public static PerlHeredocTerminatorImpl createHereDocTerminator(Project project, String name)
	{
		PerlFileImpl file = createFile(project, "<<'" + name + "';\n"+name+"\n");
		return (PerlHeredocTerminatorImpl)file.getChildren()[3];
	}

	public static PerlStringContentImpl createStringContent(Project project, String name)
	{
		PerlFileImpl file = createFile(project, "'"+name+"';");
		return (PerlStringContentImpl)file.getFirstChild().getFirstChild().getFirstChild().getNextSibling();
	}

	public static PerlFileImpl createFile(Project project, String text)
	{
		String fileName = "file.dummy";
		return (PerlFileImpl) PsiFileFactory.getInstance(project).
				createFileFromText(fileName, PerlFileTypePackage.INSTANCE, text);
	}

}
