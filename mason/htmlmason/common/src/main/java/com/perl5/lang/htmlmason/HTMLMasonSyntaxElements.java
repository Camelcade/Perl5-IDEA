/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.htmlmason;

import java.util.Set;


public final class HTMLMasonSyntaxElements {
  private HTMLMasonSyntaxElements() {
  }

  public static final String COMPONENT_SLUG_SELF = "SELF";
  public static final String COMPONENT_SLUG_PARENT = "PARENT";
  public static final String COMPONENT_SLUG_REQUEST = "REQUEST";

  public static final String KEYWORD_BLOCK_OPENER = "<%";
  public static final String KEYWORD_BLOCK_CLOSER = "%>";

  public static final String KEYWORD_CALL_OPENER = "<&";
  public static final String KEYWORD_CALL_OPENER_FILTER = "<&|";

  public static final String KEYWORD_CALL_CLOSER = "&>";

  public static final String KEYWORD_CALL_CLOSE_TAG_START = "</&";
  public static final String KEYWORD_TAG_CLOSER = ">";

  public static final String KEYWORD_PERL = "perl";
  public static final String KEYWORD_PERL_OPENER_UNCLOSED = "<%" + KEYWORD_PERL;
  public static final String KEYWORD_PERL_OPENER = KEYWORD_PERL_OPENER_UNCLOSED + ">";
  public static final String KEYWORD_PERL_CLOSER = "</%" + KEYWORD_PERL + ">";

  public static final String KEYWORD_INIT = "init";
  public static final String KEYWORD_INIT_OPENER_UNCLOSED = "<%" + KEYWORD_INIT;
  public static final String KEYWORD_INIT_OPENER = KEYWORD_INIT_OPENER_UNCLOSED + ">";
  public static final String KEYWORD_INIT_CLOSER = "</%" + KEYWORD_INIT + ">";

  public static final String KEYWORD_CLEANUP = "cleanup";
  public static final String KEYWORD_CLEANUP_OPENER_UNCLOSED = "<%" + KEYWORD_CLEANUP;
  public static final String KEYWORD_CLEANUP_OPENER = KEYWORD_CLEANUP_OPENER_UNCLOSED + ">";
  public static final String KEYWORD_CLEANUP_CLOSER = "</%" + KEYWORD_CLEANUP + ">";

  public static final String KEYWORD_ONCE = "once";
  public static final String KEYWORD_ONCE_OPENER_UNCLOSED = "<%" + KEYWORD_ONCE;
  public static final String KEYWORD_ONCE_OPENER = KEYWORD_ONCE_OPENER_UNCLOSED + ">";
  public static final String KEYWORD_ONCE_CLOSER = "</%" + KEYWORD_ONCE + ">";

  public static final String KEYWORD_SHARED = "shared";
  public static final String KEYWORD_SHARED_OPENER_UNCLOSED = "<%" + KEYWORD_SHARED;
  public static final String KEYWORD_SHARED_OPENER = KEYWORD_SHARED_OPENER_UNCLOSED + ">";
  public static final String KEYWORD_SHARED_CLOSER = "</%" + KEYWORD_SHARED + ">";

  public static final String KEYWORD_METHOD = "method";
  public static final String KEYWORD_METHOD_OPENER = "<%" + KEYWORD_METHOD;
  public static final String KEYWORD_METHOD_CLOSER = "</%" + KEYWORD_METHOD + ">";

  public static final String KEYWORD_DEF = "def";
  public static final String KEYWORD_DEF_OPENER = "<%" + KEYWORD_DEF;
  public static final String KEYWORD_DEF_CLOSER = "</%" + KEYWORD_DEF + ">";

  public static final String KEYWORD_FLAGS = "flags";
  public static final String KEYWORD_FLAGS_OPENER_UNCLOSED = "<%" + KEYWORD_FLAGS;
  public static final String KEYWORD_FLAGS_OPENER = KEYWORD_FLAGS_OPENER_UNCLOSED + ">";
  public static final String KEYWORD_FLAGS_CLOSER = "</%" + KEYWORD_FLAGS + ">";

  public static final String KEYWORD_ATTR = "attr";
  public static final String KEYWORD_ATTR_OPENER_UNCLOSED = "<%" + KEYWORD_ATTR;
  public static final String KEYWORD_ATTR_OPENER = KEYWORD_ATTR_OPENER_UNCLOSED + ">";
  public static final String KEYWORD_ATTR_CLOSER = "</%" + KEYWORD_ATTR + ">";

  public static final String KEYWORD_ARGS = "args";
  public static final String KEYWORD_ARGS_OPENER_UNCLOSED = "<%" + KEYWORD_ARGS;
  public static final String KEYWORD_ARGS_OPENER = KEYWORD_ARGS_OPENER_UNCLOSED + ">";
  public static final String KEYWORD_ARGS_CLOSER = "</%" + KEYWORD_ARGS + ">";

  public static final String KEYWORD_FILTER = "filter";
  public static final String KEYWORD_FILTER_OPENER_UNCLOSED = "<%" + KEYWORD_FILTER;
  public static final String KEYWORD_FILTER_OPENER = KEYWORD_FILTER_OPENER_UNCLOSED + ">";
  public static final String KEYWORD_FILTER_CLOSER = "</%" + KEYWORD_FILTER + ">";

  public static final String KEYWORD_TEXT = "text";
  public static final String KEYWORD_TEXT_OPENER_UNCLOSED = "<%" + KEYWORD_TEXT;
  public static final String KEYWORD_TEXT_OPENER = KEYWORD_TEXT_OPENER_UNCLOSED + ">";
  public static final String KEYWORD_TEXT_CLOSER = "</%" + KEYWORD_TEXT + ">";

  public static final String KEYWORD_DOC = "doc";
  public static final String KEYWORD_DOC_OPENER_UNCLOSED = "<%" + KEYWORD_DOC;
  public static final String KEYWORD_DOC_OPENER = KEYWORD_DOC_OPENER_UNCLOSED + ">";
  public static final String KEYWORD_DOC_CLOSER = "</%" + KEYWORD_DOC + ">";

  public static final Set<String> BUILTIN_TAGS_SIMPLE = Set.of(
    KEYWORD_ARGS,
    KEYWORD_ATTR,
    KEYWORD_PERL,
    KEYWORD_INIT,
    KEYWORD_CLEANUP,
    KEYWORD_ONCE,
    KEYWORD_SHARED,
    KEYWORD_FLAGS,
    KEYWORD_FILTER,
    KEYWORD_TEXT,
    KEYWORD_DOC
  );

  public static final Set<String> BUILTIN_TAGS_COMPLEX = Set.of(
    KEYWORD_METHOD,
    KEYWORD_DEF
  );
}
