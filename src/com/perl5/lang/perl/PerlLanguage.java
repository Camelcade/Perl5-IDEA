package com.perl5.lang.perl;

/**
 * Created by hurricup on 12.04.2015.
 */

import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.packaging.ui.PackagingSourceItemFilter;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.perl5.lang.perl.idea.highlighter.PerlSyntaxHighlighter;
import com.perl5.lang.perl.parser.PerlPackage;
import com.perl5.lang.perl.parser.PerlPackageFile;

import java.io.*;
import java.util.ArrayList;

public class PerlLanguage extends Language
{
	public static final PerlLanguage INSTANCE = new PerlLanguage();

	protected ArrayList<VirtualFile> libPaths = null;

	public PerlLanguage() {
		super("Perl5");
	}

	@Override
	public boolean isCaseSensitive() {
		return true;
	}

	public void initLibPaths(Project project)
	{
		libPaths = new ArrayList<VirtualFile>();

		for( VirtualFile file : ProjectRootManager.getInstance(project).getContentRoots())
		{
			VirtualFile libFile = file.findFileByRelativePath("lib");
			if(libFile != null)
				libPaths.add(libFile);
			else
				libPaths.add(file);
		}


		try
		{
			Process p = Runtime.getRuntime().exec("perl");
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));

			out.write("print join \"\\n\", @INC\n");
			out.write(4);
			out.write("\n");
			out.flush();

			String line;
			while( (line = in.readLine()) != null )
			{
				if( !".".equals(line) )
					libPaths.add(LocalFileSystem.getInstance().findFileByPath(line));
			}

			out.close();
			in.close();
		}
		catch( Exception e)
		{
			e.printStackTrace();
		}
	}

	public PsiFile getPackagePsiFile(Project project, PerlPackageFile file)
	{
		if( libPaths == null )
			initLibPaths(project);

		String relPath = file.getFilename();
		VirtualFile packageFile = null;

		for( VirtualFile dir: libPaths)
		{
			packageFile = dir.findFileByRelativePath(relPath);
			if( packageFile != null )
				break;
		}

		if( packageFile == null)
			return null;

		return PsiManager.getInstance(project).findFile(packageFile);
	}

	public ArrayList<VirtualFile> getLibPaths()
	{
		return libPaths;
	}
}

