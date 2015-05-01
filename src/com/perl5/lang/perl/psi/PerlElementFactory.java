package com.perl5.lang.perl.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFileFactory;
import com.perl5.lang.perl.files.PerlFileTypePackage;

/**
 * Created by hurricup on 26.04.2015.
 */
public class PerlElementFactory
{
/*
	public static PerlPackageBare createPerlPackageBare(Project project, String name)
	{
		final PerlFile file = createFile(project, name);
		return (PerlPackageBare) file.getFirstChild();
	}
*/

	public static PerlFile createFile(Project project, String text)
	{
		String fileName = "package.dummy";
		return (PerlFile) PsiFileFactory.getInstance(project).
				createFileFromText(fileName, PerlFileTypePackage.INSTANCE, text);
	}

}
