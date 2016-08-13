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

package com.perl5.lang.perl.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hurricup on 13.08.2016.
 */
public abstract class PsiModificationCounterCache<K extends PsiElement, V>
{
	private static final Object NULL_TYPE = new Object();

	private final Map<K, Object> myCache = new ConcurrentHashMap<K, Object>();
	private long myModificationCount = 0;

	private Map<K, Object> getCache(@NotNull Project project)
	{
		long modificationCount = PsiManager.getInstance(project).getModificationTracker().getModificationCount();
		if (modificationCount != myModificationCount)
		{
			myModificationCount = modificationCount;
			myCache.clear();
		}
		return myCache;
	}

	@Nullable
	public V getValue(K key)
	{
		Map<K, Object> cacheMap = getCache(key.getProject());
		Object result = cacheMap.get(key);

		if (result == null)
		{
			result = compute(key);
			if (result == null)
			{
				result = NULL_TYPE;
			}
			cacheMap.put(key, result);
		}
		//noinspection unchecked
		return result == NULL_TYPE ? null : (V) result;
	}

	@Nullable
	protected abstract V compute(K key);
}
