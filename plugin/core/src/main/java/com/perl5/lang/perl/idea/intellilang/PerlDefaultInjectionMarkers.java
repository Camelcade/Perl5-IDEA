/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.PerlLanguage;

import java.util.Map;

public final class PerlDefaultInjectionMarkers {
  private PerlDefaultInjectionMarkers() {
  }

  public static final String PERL5_MARKER = "PERL5";

  public static final Map<String, String> DEFAULT_MARKERS = ContainerUtil.<String, String>immutableMapBuilder()
    .put("APPLEJS", "Apple JS")
    .put("CSS", "CSS")
    .put("DB2", "DB2")
    .put("DTD", "DTD")
    .put("GSQL", "GenericSQL")
    .put("HSQLDB", "HSQLDB")
    .put("HTML", "HTML")
    .put("JAVA", "JAVA")
    .put("JS", "JavaScript")
    .put("JS15", "JavaScript 1.5")
    .put("JS16", "JavaScript 1.6")
    .put("JS17", "JavaScript 1.7")
    .put("JS18", "JavaScript 1.8")
    .put("JSON", "JSON")
    .put("MANIFEST", "Manifest")
    .put("MARKDOWN", "Markdown")
    .put("MYSQL", "MYSQL-SQL")
    .put("MYSQL", "MySQL")
    .put("OSQL", "ORACLE-SQL")
    .put("OSQL", "Oracle")
    .put("OSQLP", "OracleSqlPlus")
    .put("PERL5", PerlLanguage.INSTANCE.getID())
    .put("PGSQL", "POSTGRES-SQL")
    .put("PGSQL", "PostgreSQL")
    .put("PHP", "PHP")
    .put("PYTHON", "Python")
    .put("SHELL", "Shell Script")
    .put("SQL", "SQL")
    .put("SQL92", "ISO92-SQL")
    .put("SQL92", "SQL92")
    .put("SQLITE", "SQLite")
    .put("SYBASE", "Sybase")
    .put("TSQL", "TSQL")
    .put("XHTML", "XHTML")
    .put("XML", "XML")
    .put("YAML", "yaml")
    .build();
}
