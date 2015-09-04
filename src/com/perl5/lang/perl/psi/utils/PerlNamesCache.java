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

import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlSubUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hurricup on 04.09.2015.
 */
public class PerlNamesCache
{
	private static final Boolean mapValue = true;
	private static final Map<String, Boolean> KNOWN_SUBS = new ConcurrentHashMap<String, Boolean>();
	private static final Map<String, Boolean> KNOWN_PACKAGES = new ConcurrentHashMap<String, Boolean>();

	public static boolean initCaches(Project project)
	{
		if (!DumbService.isDumb(project))
		{
			for (String name : PerlSubUtil.getDeclaredSubsNames(project))
				KNOWN_SUBS.put(name, mapValue);
			for (String name : PerlSubUtil.getDefinedSubsNames(project))
				KNOWN_SUBS.put(name, mapValue);
			for (String name : PerlGlobUtil.getDefinedGlobsNames(project))
				KNOWN_SUBS.put(name, mapValue);

			for (String name : PerlPackageUtil.getDefinedPackageNames(project))
				KNOWN_PACKAGES.put(name, mapValue);
			return true;
		}
		return false;
	}

	public static boolean isPackageExists(String name)
	{
		return KNOWN_PACKAGES.containsKey(PerlPackageUtil.getCanonicalPackageName(name));
	}

	public static boolean isSubExists(String name)
	{
		return KNOWN_SUBS.containsKey(name);
	}

	public void resisterPackage(String name)
	{
		KNOWN_PACKAGES.put(PerlPackageUtil.getCanonicalPackageName(name), mapValue);
	}

	public void resisterSub(String name)
	{
		KNOWN_SUBS.put(name, mapValue);
	}
}
