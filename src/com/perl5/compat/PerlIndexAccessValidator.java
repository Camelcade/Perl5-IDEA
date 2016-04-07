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
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.indexing.FileBasedIndexImpl;
import com.intellij.util.indexing.ID;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;

/**
 * Created by hurricup on 07.04.2016.
 * Copy-paste from Idea 145.844 IndexAcessValidator
 */
public class PerlIndexAccessValidator
{
	private final ThreadLocal<ID<?, ?>> ourAlreadyProcessingIndices = new ThreadLocal<ID<?, ?>>();

	public void checkAccessingIndexDuringOtherIndexProcessing(@NotNull ID<?, ?> indexKey)
	{
		final ID<?, ?> alreadyProcessingIndex = ourAlreadyProcessingIndices.get();
		if (alreadyProcessingIndex != null && alreadyProcessingIndex != indexKey)
		{
			final String message = MessageFormat.format("Accessing ''{0}'' during processing ''{1}''. Nested different indices processing may cause deadlock",
					indexKey.toString(),
					alreadyProcessingIndex.toString());
			if (ApplicationManager.getApplication().isUnitTestMode()) throw new RuntimeException(message);
			Logger.getInstance(FileBasedIndexImpl.class).error(message); // RuntimeException to skip rebuild
		}
	}

	public void startedProcessingActivityForIndex(ID<?, ?> indexId)
	{
		ourAlreadyProcessingIndices.set(indexId);
	}

	public void stoppedProcessingActivityForIndex(ID<?, ?> indexId)
	{
		ourAlreadyProcessingIndices.set(null);
	}

}
