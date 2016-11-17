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

package com.perl5.lang.perl.idea.project;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupManager;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Created by hurricup on 04.09.2015.
 */
public class PerlNamesCache implements ProjectComponent
{
	final Application myApplication = ApplicationManager.getApplication();
	final NamesCacheUpdater updaterRunner = new NamesCacheUpdater();
	final Thread updaterThread = new Thread(updaterRunner);
	private final Project myProject;
	private Set<String> KNOWN_SUBS = new THashSet<>();
	private Set<String> KNOWN_PACKAGES = new THashSet<>();
	final Runnable cacheUpdaterWorker = new Runnable()
	{
		@Override
		public void run()
		{
			if (isTestMode() || !DumbService.isDumb(myProject))
			{
				Set<String> newSet = new THashSet<>();
				newSet.addAll(PerlSubUtil.getDeclaredSubsNames(myProject));
				newSet.addAll(PerlSubUtil.getDefinedSubsNames(myProject));
				newSet.addAll(PerlGlobUtil.getDefinedGlobsNames(myProject));
				KNOWN_SUBS = newSet;

				newSet = new THashSet<>();
				newSet.addAll(PerlPackageUtil.BUILT_IN_ALL);
				newSet.addAll(PerlPackageUtil.getDefinedPackageNames(myProject));
				KNOWN_PACKAGES = newSet;
			}
		}
	};
	//	long notifyCounter = 0;
	private boolean isNotified = false;

	public PerlNamesCache(Project project)
	{
		this.myProject = project;
	}

	@Override
	public void projectOpened()
	{

	}

	@Override
	public void projectClosed()
	{

	}

	public void forceCacheUpdate()
	{
		cacheUpdaterWorker.run();
	}

	@Override
	public void initComponent()
	{
		StartupManager.getInstance(myProject).runWhenProjectIsInitialized(updaterThread::start);
	}

	@Override
	public void disposeComponent()
	{
		updaterRunner.stopUpdater();
	}

	@NotNull
	@Override
	public String getComponentName()
	{
		return "Perl5 names cache";
	}

	public Set<String> getSubsNamesSet()
	{
		updaterRunner.update();
		return KNOWN_SUBS;
	}

	public boolean isTestMode()
	{
		return myApplication != null && (myApplication.isUnitTestMode() || myApplication.isHeadlessEnvironment());
	}

	public Set<String> getPackagesNamesSet()
	{
		updaterRunner.update();
		return KNOWN_PACKAGES;
	}

	protected class NamesCacheUpdater implements Runnable
	{
		private static final long TTL = 1000;
		private boolean stopThis = false;
		private long lastUpdate = 0;

		@Override
		public void run()
		{

			while (!stopThis)
			{
				myApplication.runReadAction(cacheUpdaterWorker);
				lastUpdate = System.currentTimeMillis();
				isNotified = false;

				synchronized (this)
				{
					try
					{
						wait();
					}
					catch (Exception e)
					{
						break;
					}
				}
			}
		}

		public void update()
		{
			if (!isNotified && lastUpdate + TTL < System.currentTimeMillis())
			{
				synchronized (this)
				{
					isNotified = true;
					notify();
				}
			}
		}

		public void stopUpdater()
		{
			stopThis = true;
			synchronized (this)
			{
				notify();
			}
		}
	}

}