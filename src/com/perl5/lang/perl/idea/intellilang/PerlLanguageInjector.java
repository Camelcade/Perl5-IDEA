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

package com.perl5.lang.perl.idea.intellilang;

import com.intellij.lang.Language;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.InjectedLanguagePlaces;
import com.intellij.psi.LanguageInjector;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PerlHeredocTerminatorElementImpl;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hurricup on 12.06.2015.
 */
public class PerlLanguageInjector implements LanguageInjector
{
	public static final Map<String, Language> LANGUAGE_MAP = new HashMap<String, Language>();

	protected static final Map<String, String> ACCEPTABLE_LANGUAGES = new HashMap<String, String>();

	static
	{
		// idea database tools
		ACCEPTABLE_LANGUAGES.put("SQL", "SQL");
		ACCEPTABLE_LANGUAGES.put("MySQL", "MYSQL");
		ACCEPTABLE_LANGUAGES.put("PostgreSQL", "PGSQL");
		ACCEPTABLE_LANGUAGES.put("TSQL", "TSQL");
		ACCEPTABLE_LANGUAGES.put("OracleSqlPlus", "OSQLP");
		ACCEPTABLE_LANGUAGES.put("DB2", "DB2");
		ACCEPTABLE_LANGUAGES.put("SQL92", "SQL92");
		ACCEPTABLE_LANGUAGES.put("SQLite", "SQLITE");
		ACCEPTABLE_LANGUAGES.put("Sybase", "SYBASE");
		ACCEPTABLE_LANGUAGES.put("HSQLDB", "HSQLDB");
		ACCEPTABLE_LANGUAGES.put("GenericSQL", "GSQL");
		ACCEPTABLE_LANGUAGES.put("Oracle", "OSQL");

		// database navigator
		ACCEPTABLE_LANGUAGES.put("POSTGRES-SQL", "PGSQL");
		ACCEPTABLE_LANGUAGES.put("ISO92-SQL", "SQL92");
		ACCEPTABLE_LANGUAGES.put("ORACLE-SQL", "OSQL");
		ACCEPTABLE_LANGUAGES.put("MYSQL-SQL", "MYSQL");

		ACCEPTABLE_LANGUAGES.put("JavaScript", "JS");
		ACCEPTABLE_LANGUAGES.put("JavaScript 1.5", "JS15");
		ACCEPTABLE_LANGUAGES.put("JavaScript 1.6", "JS16");
		ACCEPTABLE_LANGUAGES.put("JavaScript 1.7", "JS17");
		ACCEPTABLE_LANGUAGES.put("JavaScript 1.8", "JS18");
		ACCEPTABLE_LANGUAGES.put("Apple JS", "APPLEJS");
		ACCEPTABLE_LANGUAGES.put("JSON", "JSON");

		ACCEPTABLE_LANGUAGES.put("CSS", "CSS");
		ACCEPTABLE_LANGUAGES.put("DTD", "DTD");
		ACCEPTABLE_LANGUAGES.put("XHTML", "XHTML");
		ACCEPTABLE_LANGUAGES.put("XML", "XML");
		ACCEPTABLE_LANGUAGES.put("HTML", "HTML");

		ACCEPTABLE_LANGUAGES.put("JAVA", "JAVA");
		ACCEPTABLE_LANGUAGES.put("yaml", "YAML");
		ACCEPTABLE_LANGUAGES.put("Manifest", "MANIFEST");
		ACCEPTABLE_LANGUAGES.put("Markdown", "MARKDOWN");
		ACCEPTABLE_LANGUAGES.put("PHP", "PHP");
		ACCEPTABLE_LANGUAGES.put("Python", "PYTHON");

		ACCEPTABLE_LANGUAGES.put("Perl5", "PERL5");
		ACCEPTABLE_LANGUAGES.put("Embedded Perl", "EPERL5");
		ACCEPTABLE_LANGUAGES.put("Mojolicious Perl", "MOJO");

//		Collection<Language> languages = Language.getRegisteredLanguages();

		for (Language language : Language.getRegisteredLanguages())
		{
			String languageKey = ACCEPTABLE_LANGUAGES.get(language.getID());

			if (languageKey != null && LANGUAGE_MAP.get(languageKey) == null)
				LANGUAGE_MAP.put(languageKey, language);
		}

	}


	@Override
	public void getLanguagesToInject(@NotNull PsiLanguageInjectionHost host, @NotNull InjectedLanguagePlaces injectionPlacesRegistrar)
	{

		if (host instanceof PerlHeredocElementImpl)
		{
			PsiElement terminator = host.getNextSibling();
			if (terminator instanceof PerlHeredocTerminatorElementImpl)
			{
				String terminatorText = terminator.getText();
				Language mappedLanguage = LANGUAGE_MAP.get(terminatorText);

				if (mappedLanguage != null)
					injectionPlacesRegistrar.addPlace(mappedLanguage, new TextRange(0, host.getTextLength()), null, null);
			}
		}
	}
}
