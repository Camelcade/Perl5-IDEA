/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

public interface PerlDefaultInjectionMarkers {
  String PERL5_MARKER = "PERL5";

  Map<String, String> DEFAULT_MARKERS = ContainerUtil.<String, String>immutableMapBuilder()
    .put("SQL", "SQL")
    .put("MYSQL", "MySQL")
    .put("PGSQL", "PostgreSQL")
    .put("TSQL", "TSQL")
    .put("OSQLP", "OracleSqlPlus")
    .put("DB2", "DB2")
    .put("SQL92", "SQL92")
    .put("SQLITE", "SQLite")
    .put("SYBASE", "Sybase")
    .put("HSQLDB", "HSQLDB")
    .put("GSQL", "GenericSQL")
    .put("OSQL", "Oracle")

    // database navigator
    .put("PGSQL", "POSTGRES-SQL")
    .put("SQL92", "ISO92-SQL")
    .put("OSQL", "ORACLE-SQL")
    .put("MYSQL", "MYSQL-SQL")

    .put("JS", "JavaScript")
    .put("JS15", "JavaScript 1.5")
    .put("JS16", "JavaScript 1.6")
    .put("JS17", "JavaScript 1.7")
    .put("JS18", "JavaScript 1.8")
    .put("APPLEJS", "Apple JS")
    .put("JSON", "JSON")

    .put("CSS", "CSS")
    .put("DTD", "DTD")
    .put("XHTML", "XHTML")
    .put("XML", "XML")
    .put("HTML", "HTML")

    .put("JAVA", "JAVA")
    .put("YAML", "yaml")
    .put("MANIFEST", "Manifest")
    .put("MARKDOWN", "Markdown")
    .put("PHP", "PHP")
    .put("PYTHON", "Python")

    .put(PERL5_MARKER, PerlLanguage.INSTANCE.getID())
    .build();
}
