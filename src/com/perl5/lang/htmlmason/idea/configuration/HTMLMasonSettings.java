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

package com.perl5.lang.htmlmason.idea.configuration;

import com.intellij.lang.Language;
import com.intellij.openapi.components.*;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.LanguageSubstitutor;
import com.intellij.psi.LanguageSubstitutors;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.perl5.lang.htmlmason.idea.lang.HTMLMasonLanguageSubstitutor;
import com.perl5.lang.mason2.idea.configuration.VariableDescription;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 05.03.2016.
 */
@State(
		name = "HTMLMasonSettings",
		storages = {
				@Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
				@Storage(id = "dir", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/perl5.xml", scheme = StorageScheme.DIRECTORY_BASED)
		}
)

public class HTMLMasonSettings extends AbstractMasonSettings<HTMLMasonSettings>
{
	public List<String> substitutedExtensions = new ArrayList<String>();

	@Transient
	private Map<String, Pair<Language, LanguageSubstitutor>> substitutorMap = new THashMap<String, Pair<Language, LanguageSubstitutor>>();

	public HTMLMasonSettings()
	{
		globalVariables.add(new VariableDescription("$m", "HTML::Mason::Request"));
		globalVariables.add(new VariableDescription("$r", "Apache::Request"));
		changeCounter++;
	}

	public static HTMLMasonSettings getInstance(@NotNull Project project)
	{
		HTMLMasonSettings persisted = ServiceManager.getService(project, HTMLMasonSettings.class);
		if (persisted == null)
		{
			persisted = new HTMLMasonSettings();
		}

		persisted.setProject(project);
		persisted.updateSubstitutors();

		return persisted;
	}

	@Nullable
	@Override
	public HTMLMasonSettings getState()
	{
		return this;
	}

	@Override
	public void loadState(HTMLMasonSettings state)
	{
		XmlSerializerUtil.copyBean(state, this);
		changeCounter++;
	}

	protected void updateSubstitutors()
	{
		// unregister
		Iterator<Map.Entry<String, Pair<Language, LanguageSubstitutor>>> iterator = substitutorMap.entrySet().iterator();
		while (iterator.hasNext())
		{
			Map.Entry<String, Pair<Language, LanguageSubstitutor>> entry = iterator.next();
			if (!substitutedExtensions.contains(entry.getKey()))
			{
//				System.err.println("Unregistering " + entry.getKey());
				LanguageSubstitutors.INSTANCE.removeExplicitExtension(entry.getValue().first, entry.getValue().second);
				iterator.remove();
			}

		}

		// register
		FileTypeManager fileTypeManager = FileTypeManager.getInstance();
		for (FileType fileType : fileTypeManager.getRegisteredFileTypes())
		{
			if (fileType instanceof LanguageFileType)
			{
				Language language = ((LanguageFileType) fileType).getLanguage();
				for (FileNameMatcher matcher : fileTypeManager.getAssociations(fileType))
				{
					String presentableString = matcher.getPresentableString();
					if (substitutedExtensions.contains(presentableString) && !substitutorMap.containsKey(presentableString))
					{
//						System.err.println("Registering " + presentableString);
						LanguageSubstitutor substitutor = new HTMLMasonLanguageSubstitutor(myProject, matcher);

						LanguageSubstitutors.INSTANCE.addExplicitExtension(language, substitutor);
						substitutorMap.put(presentableString, Pair.create(language, substitutor));
					}
				}
			}
		}
	}
}
