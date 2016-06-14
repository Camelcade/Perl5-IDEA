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

package com.perl5.compat;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.psi.stubs.StubIndexKey;
import com.intellij.util.Processor;
import com.intellij.util.indexing.IdFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Created by hurricup on 07.04.2016.
 * Proxy class to implement restrictions from IDEA2016.2
 */
public class PerlStubIndex extends StubIndex implements ApplicationComponent
{
	private final PerlIndexAccessValidator myAccessValidator = new PerlIndexAccessValidator();

	public static PerlStubIndex getInstance()
	{
		return StubIndexHolder.ourInstance;
	}

	public static <Key, Psi extends PsiElement> Collection<Psi> getElements(@NotNull StubIndexKey<Key, Psi> indexKey,
																			@NotNull Key key,
																			@NotNull final Project project,
																			@Nullable final GlobalSearchScope scope,
																			@NotNull Class<Psi> requiredClass)
	{
		return getElements(indexKey, key, project, scope, null, requiredClass);
	}

	public static <Key, Psi extends PsiElement> Collection<Psi> getElements(@NotNull StubIndexKey<Key, Psi> indexKey,
																			@NotNull Key key,
																			@NotNull final Project project,
																			@Nullable final GlobalSearchScope scope,
																			@Nullable IdFilter idFilter,
																			@NotNull Class<Psi> requiredClass)
	{
		getInstance().myAccessValidator.checkAndStartProcessingActivityForIndex(indexKey);
		try
		{
			return StubIndex.getElements(indexKey, key, project, scope, idFilter, requiredClass);
		}
		finally
		{
			getInstance().myAccessValidator.stoppedProcessingActivityForIndex(indexKey);
		}
	}

	@Override
	public void initComponent()
	{

	}

	@Override
	public void disposeComponent()
	{

	}

	public <Key, Psi extends PsiElement> boolean isIndexAvailable(@NotNull StubIndexKey<Key, Psi> indexKey)
	{
		return myAccessValidator.isIndexAvailable(indexKey);
	}

	@NotNull
	@Override
	public String getComponentName()
	{
		return "Stub indexes proxy for Perl5 plugin";
	}

	@Override
	public <Key, Psi extends PsiElement> Collection<Psi> get(@NotNull StubIndexKey<Key, Psi> indexKey, @NotNull Key key, @NotNull Project project, @Nullable GlobalSearchScope scope)
	{
		myAccessValidator.checkAndStartProcessingActivityForIndex(indexKey);
		try
		{
			return StubIndex.getInstance().get(indexKey, key, project, scope);
		}
		finally
		{
			myAccessValidator.stoppedProcessingActivityForIndex(indexKey);
		}
	}

	@Override
	public <Key, Psi extends PsiElement> boolean processElements(@NotNull StubIndexKey<Key, Psi> indexKey, @NotNull Key key, @NotNull Project project, @Nullable GlobalSearchScope scope, Class<Psi> requiredClass, @NotNull Processor<? super Psi> processor)
	{
		myAccessValidator.checkAndStartProcessingActivityForIndex(indexKey);
		try
		{
			return StubIndex.getInstance().processElements(indexKey, key, project, scope, requiredClass, processor);
		}
		finally
		{
			myAccessValidator.stoppedProcessingActivityForIndex(indexKey);
		}
	}

	@NotNull
	@Override
	public <Key> Collection<Key> getAllKeys(@NotNull StubIndexKey<Key, ?> indexKey, @NotNull Project project)
	{
		myAccessValidator.checkAndStartProcessingActivityForIndex(indexKey);
		try
		{
			return StubIndex.getInstance().getAllKeys(indexKey, project);
		}
		finally
		{
			myAccessValidator.stoppedProcessingActivityForIndex(indexKey);
		}
	}

	@Override
	public <K> boolean processAllKeys(@NotNull StubIndexKey<K, ?> indexKey, @NotNull Project project, Processor<K> processor)
	{
		myAccessValidator.checkAndStartProcessingActivityForIndex(indexKey);
		try
		{
			return StubIndex.getInstance().processAllKeys(indexKey, project, processor);
		}
		finally
		{
			myAccessValidator.stoppedProcessingActivityForIndex(indexKey);
		}
	}

	@Override
	public void forceRebuild(@NotNull Throwable e)
	{
		StubIndex.getInstance().forceRebuild(e);
	}

	private static class StubIndexHolder
	{
		private static final PerlStubIndex ourInstance = ApplicationManager.getApplication().getComponent(PerlStubIndex.class);
	}

}
