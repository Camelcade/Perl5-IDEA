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

package com.perl5.lang.perl.xsubs;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 01.05.2016.
 */
public class PerlXSubsProjectComponent implements ProjectComponent
{

	private final Project myProject;
	//	private final VirtualFileListener myFileListener;
	private final PerlXSubsState xSubsState;

	public PerlXSubsProjectComponent(Project myProject)
	{
		this.myProject = myProject;
//		myFileListener = new PerlXSubsFileListener();
		xSubsState = PerlXSubsState.getInstance(myProject);
	}

	@Override
	public void projectOpened()
	{
		xSubsState.rescanFiles();

		if (!xSubsState.isActual)
		{
			xSubsState.notifyUser();
		}

//		VirtualFileManager.getInstance().addVirtualFileListener(myFileListener);
	}

	@Override
	public void projectClosed()
	{
//		VirtualFileManager.getInstance().removeVirtualFileListener(myFileListener);
	}

	@Override
	public void initComponent()
	{

	}

	@Override
	public void disposeComponent()
	{

	}

	@NotNull
	@Override
	public String getComponentName()
	{
		return "PerlXSubsProjectComponent";
	}
}
