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

package com.perl5.lang.perl.psi.utils;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;
import gnu.trove.THashSet;

import java.util.Set;

/**
 * Created by hurricup on 04.09.2015.
 */
public class PerlNamesCache
{
	private static Set<String> KNOWN_SUBS = new THashSet<String>();
	private static Set<String> KNOWN_PACKAGES = new THashSet<String>();

	public static Set<String> getSubsNamesSet(Project project, boolean reuseCache)
	{
		if (!reuseCache && !isTestMode() && !DumbService.isDumb(project) && !PerlNamesCacheUpdateLock.isLocked() && areIndexesAvailable())
		{
			Set<String> newSet = new THashSet<String>();
			newSet.addAll(PerlSubUtil.getDeclaredSubsNames(project));
			newSet.addAll(PerlSubUtil.getDefinedSubsNames(project));
			newSet.addAll(PerlGlobUtil.getDefinedGlobsNames(project));
			return KNOWN_SUBS = newSet;
		}
		return KNOWN_SUBS;
	}

	// fixme this may be optimized using some hashset 
	private static boolean areIndexesAvailable()
	{
		return PerlSubUtil.isSubDeclarationsIndexAvailable() && PerlSubUtil.isSubDefinitionsIndexAvailable() && PerlGlobUtil.isGlobsDefinitionsIndexAvailable();
	}

	public static boolean isTestMode()
	{
		final Application application = ApplicationManager.getApplication();
		return application != null && (application.isUnitTestMode() || application.isHeadlessEnvironment());
	}

	public static Set<String> getPackagesNamesSet(Project project, boolean reuseCache)
	{
		if (!reuseCache && !isTestMode() && !DumbService.isDumb(project) && !PerlNamesCacheUpdateLock.isLocked() && PerlPackageUtil.isPackageIndexAvailable())
		{
			Set<String> newSet = new THashSet<String>();
			newSet.addAll(PerlPackageUtil.BUILT_IN_ALL);
			newSet.addAll(PerlPackageUtil.getDefinedPackageNames(project));
			return KNOWN_PACKAGES = newSet;
		}
		return KNOWN_PACKAGES;
	}
}
