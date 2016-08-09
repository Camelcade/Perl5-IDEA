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
import com.intellij.psi.tree.IElementType;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.htmlmason.idea.lang.HTMLMasonLanguageSubstitutor;
import com.perl5.lang.htmlmason.lexer.HTMLMasonLexer;
import com.perl5.lang.mason2.idea.configuration.VariableDescription;
import com.perl5.lang.perl.idea.PerlPathMacros;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 05.03.2016.
 */
@State(
		name = "HTMLMasonSettings",
		storages = {
				@Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
				@Storage(id = "dir", file = PerlPathMacros.PERL5_PROJECT_SHARED_SETTINGS_FILE, scheme = StorageScheme.DIRECTORY_BASED)
		}
)

public class HTMLMasonSettings extends AbstractMasonSettings implements PersistentStateComponent<HTMLMasonSettings>, HTMLMasonElementTypes
{

	public String autoHandlerName = "autohandler";
	public String defaultHandlerName = "dhandler";
	public List<String> substitutedExtensions = new ArrayList<String>();
	public List<HTMLMasonCustomTag> customTags = new ArrayList<HTMLMasonCustomTag>();
	@Transient
	protected Pattern mySimpleOpenersPattern;
	@Transient
	protected Pattern myOpenersPattern;
	@Transient
	protected Pattern myClosersPattern;
	@Transient
	private Map<String, Pair<Language, LanguageSubstitutor>> substitutorMap = new THashMap<String, Pair<Language, LanguageSubstitutor>>();
	@Transient
	private Map<String, IElementType> myOpenTokensMap;
	@Transient
	private Map<String, String> myOpenCloseMap;
	@Transient
	private Map<String, IElementType> myCloseTokensMap;

	public HTMLMasonSettings(Project project)
	{
		this();
		myProject = project;
	}

	private HTMLMasonSettings()
	{
		globalVariables.add(new VariableDescription("$m", "HTML::Mason::Request"));
		globalVariables.add(new VariableDescription("$r", "Apache::Request"));
		changeCounter++;
	}

	public static HTMLMasonSettings getInstance(@NotNull Project project)
	{
		HTMLMasonSettings persisted = ServiceManager.getService(project, HTMLMasonSettings.class);
		return persisted == null ? new HTMLMasonSettings(project) : persisted;
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

	public void removeSubstitutors()
	{
		for (Map.Entry<String, Pair<Language, LanguageSubstitutor>> entry : substitutorMap.entrySet())
		{
			LanguageSubstitutors.INSTANCE.removeExplicitExtension(entry.getValue().first, entry.getValue().second);
		}
		substitutorMap.clear();
	}

	public void updateSubstitutors()
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

	@Override
	public void settingsUpdated()
	{
		super.settingsUpdated();
		mySimpleOpenersPattern = null;
		myOpenersPattern = null;
		myClosersPattern = null;
		myOpenTokensMap = null;
		myOpenCloseMap = null;
		myCloseTokensMap = null;
	}

	public void prepareLexerConfiguration()
	{
		// regexp for simple openers
		StringBuilder simpleBuilder = new StringBuilder("<%(");
		boolean addOr = false;

		for (String token : HTMLMasonLexer.BUILTIN_TAGS_SIMPLE)
		{
			if (addOr)
			{
				simpleBuilder.append('|');
			}
			else
			{
				addOr = true;
			}

			simpleBuilder.append(token);
		}

		// regex for complex tags
		StringBuilder complexBuilder = new StringBuilder("%(");
		addOr = false;

		for (String token : HTMLMasonLexer.BUILTIN_TAGS_COMPLEX)
		{
			if (addOr)
			{
				complexBuilder.append('|');
			}
			else
			{
				addOr = true;
			}

			complexBuilder.append(token);
		}

		// map for open token keyword => open token token
		myOpenTokensMap = new THashMap<String, IElementType>();
		myOpenTokensMap.put(KEYWORD_PERL_OPENER, HTML_MASON_PERL_OPENER);
		myOpenTokensMap.put(KEYWORD_INIT_OPENER, HTML_MASON_INIT_OPENER);
		myOpenTokensMap.put(KEYWORD_CLEANUP_OPENER, HTML_MASON_CLEANUP_OPENER);
		myOpenTokensMap.put(KEYWORD_ONCE_OPENER, HTML_MASON_ONCE_OPENER);
		myOpenTokensMap.put(KEYWORD_SHARED_OPENER, HTML_MASON_SHARED_OPENER);
		myOpenTokensMap.put(KEYWORD_FLAGS_OPENER, HTML_MASON_FLAGS_OPENER);
		myOpenTokensMap.put(KEYWORD_ATTR_OPENER, HTML_MASON_ATTR_OPENER);
		myOpenTokensMap.put(KEYWORD_ARGS_OPENER, HTML_MASON_ARGS_OPENER);
		myOpenTokensMap.put(KEYWORD_FILTER_OPENER, HTML_MASON_FILTER_OPENER);
		myOpenTokensMap.put(KEYWORD_DOC_OPENER, HTML_MASON_DOC_OPENER);
		myOpenTokensMap.put(KEYWORD_TEXT_OPENER, HTML_MASON_TEXT_OPENER);

		// parametrized
		myOpenTokensMap.put(KEYWORD_METHOD_OPENER, HTML_MASON_METHOD_OPENER);
		myOpenTokensMap.put(KEYWORD_DEF_OPENER, HTML_MASON_DEF_OPENER);

		myOpenCloseMap = new THashMap<String, String>();
		myOpenCloseMap.put(KEYWORD_PERL_OPENER, KEYWORD_PERL_CLOSER);
		myOpenCloseMap.put(KEYWORD_INIT_OPENER, KEYWORD_INIT_CLOSER);
		myOpenCloseMap.put(KEYWORD_CLEANUP_OPENER, KEYWORD_CLEANUP_CLOSER);
		myOpenCloseMap.put(KEYWORD_ONCE_OPENER, KEYWORD_ONCE_CLOSER);
		myOpenCloseMap.put(KEYWORD_SHARED_OPENER, KEYWORD_SHARED_CLOSER);
		myOpenCloseMap.put(KEYWORD_FLAGS_OPENER, KEYWORD_FLAGS_CLOSER);
		myOpenCloseMap.put(KEYWORD_ATTR_OPENER, KEYWORD_ATTR_CLOSER);
		myOpenCloseMap.put(KEYWORD_ARGS_OPENER, KEYWORD_ARGS_CLOSER);
		myOpenCloseMap.put(KEYWORD_FILTER_OPENER, KEYWORD_FILTER_CLOSER);
		myOpenCloseMap.put(KEYWORD_DOC_OPENER, KEYWORD_DOC_CLOSER);
		myOpenCloseMap.put(KEYWORD_TEXT_OPENER, KEYWORD_TEXT_CLOSER);

		// parametrized
		myOpenCloseMap.put(KEYWORD_METHOD_OPENER, KEYWORD_METHOD_CLOSER);
		myOpenCloseMap.put(KEYWORD_DEF_OPENER, KEYWORD_DEF_CLOSER);

		myCloseTokensMap = new THashMap<String, IElementType>();
		myCloseTokensMap.put(KEYWORD_PERL_CLOSER, HTML_MASON_PERL_CLOSER);
		myCloseTokensMap.put(KEYWORD_INIT_CLOSER, HTML_MASON_INIT_CLOSER);
		myCloseTokensMap.put(KEYWORD_CLEANUP_CLOSER, HTML_MASON_CLEANUP_CLOSER);
		myCloseTokensMap.put(KEYWORD_ONCE_CLOSER, HTML_MASON_ONCE_CLOSER);
		myCloseTokensMap.put(KEYWORD_SHARED_CLOSER, HTML_MASON_SHARED_CLOSER);
		myCloseTokensMap.put(KEYWORD_FLAGS_CLOSER, HTML_MASON_FLAGS_CLOSER);
		myCloseTokensMap.put(KEYWORD_ATTR_CLOSER, HTML_MASON_ATTR_CLOSER);
		myCloseTokensMap.put(KEYWORD_ARGS_CLOSER, HTML_MASON_ARGS_CLOSER);
		myCloseTokensMap.put(KEYWORD_FILTER_CLOSER, HTML_MASON_FILTER_CLOSER);
		myCloseTokensMap.put(KEYWORD_DOC_CLOSER, HTML_MASON_DOC_CLOSER);
		myCloseTokensMap.put(KEYWORD_TEXT_CLOSER, HTML_MASON_TEXT_CLOSER);

		// parametrized
		myCloseTokensMap.put(KEYWORD_METHOD_CLOSER, HTML_MASON_METHOD_CLOSER);
		myCloseTokensMap.put(KEYWORD_DEF_CLOSER, HTML_MASON_DEF_CLOSER);

		// iterate custom tags
		for (HTMLMasonCustomTag customTag : customTags)
		{
			HTMLMasonCustomTagRole role = customTag.getRole();

			myOpenTokensMap.put(customTag.getOpenTagText(), role.getOpenToken());
			myCloseTokensMap.put(customTag.getCloseTagText(), role.getCloseToken());
			myOpenCloseMap.put(customTag.getOpenTagText(), customTag.getCloseTagText());

			if (role.isSimple()) // simple tag
			{
				simpleBuilder.append('|');
				simpleBuilder.append(customTag.getText());
			}
			else // custom tag
			{
				complexBuilder.append('|');
				complexBuilder.append(customTag.getText());
			}
		}

		simpleBuilder.append(")>");
		complexBuilder.append(")");

		// compiling patterns
		mySimpleOpenersPattern = Pattern.compile(simpleBuilder.toString());
		myOpenersPattern = Pattern.compile("<" + complexBuilder.toString());
		myClosersPattern = Pattern.compile("</" + complexBuilder.toString() + ">");

//		System.err.println("HTML::Mason lexer settings prepared");
	}

	public Map<String, IElementType> getCloseTokensMap()
	{
		if (myCloseTokensMap == null)
		{
			prepareLexerConfiguration();
		}
		return myCloseTokensMap;
	}

	public Map<String, String> getOpenCloseMap()
	{
		if (myOpenCloseMap == null)
		{
			prepareLexerConfiguration();
		}
		return myOpenCloseMap;
	}

	public Map<String, IElementType> getOpenTokensMap()
	{
		if (myOpenTokensMap == null)
		{
			prepareLexerConfiguration();
		}
		return myOpenTokensMap;
	}

	public Pattern getClosersPattern()
	{
		if (myClosersPattern == null)
		{
			prepareLexerConfiguration();
		}
		return myClosersPattern;
	}

	public Pattern getOpenersPattern()
	{
		if (myOpenersPattern == null)
		{
			prepareLexerConfiguration();
		}
		return myOpenersPattern;
	}

	public Pattern getSimpleOpenersPattern()
	{
		if (mySimpleOpenersPattern == null)
		{
			prepareLexerConfiguration();
		}
		return mySimpleOpenersPattern;
	}
}
