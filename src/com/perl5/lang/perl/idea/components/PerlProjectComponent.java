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
import com.intellij.openapi.vfs.VirtualFileListener;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.perl5.lang.perl.idea.PerlFileListener;
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
//		System.out.println("Registered listener");
		if( myChangeListener == null )
		{
			myChangeListener = new PerlFileListener(myProject);
			VirtualFileManager.getInstance().addVirtualFileListener(myChangeListener);
		}
	}

	public void disposeComponent()
	{
		// TODO: insert component disposal logic here
//		System.out.println("Unregistered listener");
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

}
