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

package com.perl5.lang.perl.idea.components;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.perl5.lang.perl.psi.impl.PerlFileListener;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 29.05.2015.
 */
public class PerlProjectComponent implements ProjectComponent
{
	Project myProject;
	VirtualFileListener myChangeListener;

	public PerlProjectComponent(Project project)
	{
		myProject = project;
	}


	public void initComponent()
	{
		// TODO: insert component initialization logic here
		System.out.println("Registered listener");
		if( myChangeListener == null )
		{
			myChangeListener = new PerlFileListener(myProject);
			VirtualFileManager.getInstance().addVirtualFileListener(myChangeListener);
		}
	}

	public void disposeComponent()
	{
		// TODO: insert component disposal logic here
		System.out.println("Unregistered listener");
		VirtualFileManager.getInstance().removeVirtualFileListener(myChangeListener);
	}

	@NotNull
	public String getComponentName()
	{
		return "PerlProjectComponent";
	}

	public void projectOpened()
	{
		// called when project is opened
	}

	public void projectClosed()
	{
		// called when project is being closed
	}


	//    // @todo this should be in app component
//	public synchronized void initLibPaths(Project project)
//	{
//		libPaths = new ArrayList<VirtualFile>();
//
//		for( VirtualFile file : ProjectRootManager.getInstance(project).getContentRoots())
//		{
//			VirtualFile libFile = file.findFileByRelativePath("lib");
//			if(libFile != null)
//				libPaths.add(libFile);
//			else
//				libPaths.add(file);
//		}
//
//
//		try
//		{
//			Process p = Runtime.getRuntime().exec("perl");
//			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
//
//			out.write("print join \"\\n\", @INC\n");
//			out.write(4);
//			out.write("\n");
//			out.flush();
//
//			String line;
//			while( (line = in.readLine()) != null )
//			{
//				if( !".".equals(line) )
//					libPaths.add(LocalFileSystem.getInstance().findFileByPath(line));
//			}
//
//			out.close();
//			in.close();
//		}
//		catch( Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//
//	public synchronized PsiFile getPackagePsiFile(Project project, PerlPackageFile file)
//	{
//		if( libPaths == null )
//			initLibPaths(project);
//
//		String relPath = file.getFilename();
//		VirtualFile packageFile = null;
//
//		for( VirtualFile dir: libPaths)
//		{
//			packageFile = dir.findFileByRelativePath(relPath);
//			if( packageFile != null )
//				break;
//		}
//
//		if( packageFile == null)
//			return null;
//
//		return PsiManager.getInstance(project).findFile(packageFile);
//	}
//
//	public ArrayList<VirtualFile> getLibPaths()
//	{
//		return libPaths;
//	}

}
