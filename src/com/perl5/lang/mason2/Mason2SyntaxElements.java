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

package com.perl5.lang.mason2;

/**
 * Created by hurricup on 26.12.2015.
 */
public interface Mason2SyntaxElements {
  String KEYWORD_BLOCK_OPENER = "<% ";
  String KEYWORD_BLOCK_CLOSER = " %>";
  String KEYWORD_CALL_OPENER = "<& ";
  String KEYWORD_CALL_CLOSER = " &>";

  String KEYWORD_METHOD = "method";
  String KEYWORD_METHOD_OPENER = "<%" + KEYWORD_METHOD;
  String KEYWORD_METHOD_CLOSER = "</%" + KEYWORD_METHOD + ">";

  String KEYWORD_CLASS = "class";
  String KEYWORD_CLASS_OPENER_UNCLOSED = "<%" + KEYWORD_CLASS;
  String KEYWORD_CLASS_OPENER = KEYWORD_CLASS_OPENER_UNCLOSED + ">";
  String KEYWORD_CLASS_CLOSER = "</%" + KEYWORD_CLASS + ">";

  String KEYWORD_DOC = "doc";
  String KEYWORD_DOC_OPENER_UNCLOSED = "<%" + KEYWORD_DOC;
  String KEYWORD_DOC_OPENER = KEYWORD_DOC_OPENER_UNCLOSED + ">";
  String KEYWORD_DOC_CLOSER = "</%" + KEYWORD_DOC + ">";

  String KEYWORD_FLAGS = "flags";
  String KEYWORD_FLAGS_OPENER_UNCLOSED = "<%" + KEYWORD_FLAGS;
  String KEYWORD_FLAGS_OPENER = KEYWORD_FLAGS_OPENER_UNCLOSED + ">";
  String KEYWORD_FLAGS_CLOSER = "</%" + KEYWORD_FLAGS + ">";

  String KEYWORD_INIT = "init";
  String KEYWORD_INIT_OPENER_UNCLOSED = "<%" + KEYWORD_INIT;
  String KEYWORD_INIT_OPENER = KEYWORD_INIT_OPENER_UNCLOSED + ">";
  String KEYWORD_INIT_CLOSER = "</%" + KEYWORD_INIT + ">";

  String KEYWORD_PERL = "perl";
  String KEYWORD_PERL_OPENER_UNCLOSED = "<%" + KEYWORD_PERL;
  String KEYWORD_PERL_OPENER = KEYWORD_PERL_OPENER_UNCLOSED + ">";
  String KEYWORD_PERL_CLOSER = "</%" + KEYWORD_PERL + ">";

  String KEYWORD_TEXT = "text";
  String KEYWORD_TEXT_OPENER_UNCLOSED = "<%" + KEYWORD_TEXT;
  String KEYWORD_TEXT_OPENER = KEYWORD_TEXT_OPENER_UNCLOSED + ">";
  String KEYWORD_TEXT_CLOSER = "</%" + KEYWORD_TEXT + ">";

  String KEYWORD_FILTER = "filter";
  String KEYWORD_FILTER_OPENER = "<%" + KEYWORD_FILTER;
  String KEYWORD_FILTER_CLOSER = "</%" + KEYWORD_FILTER + ">";

  String KEYWORD_AFTER = "after";
  String KEYWORD_AFTER_OPENER = "<%" + KEYWORD_AFTER;
  String KEYWORD_AFTER_CLOSER = "</%" + KEYWORD_AFTER + ">";

  String KEYWORD_AUGMENT = "augment";
  String KEYWORD_AUGMENT_OPENER = "<%" + KEYWORD_AUGMENT;
  String KEYWORD_AUGMENT_CLOSER = "</%" + KEYWORD_AUGMENT + ">";

  String KEYWORD_AROUND = "around";
  String KEYWORD_AROUND_OPENER = "<%" + KEYWORD_AROUND;
  String KEYWORD_AROUND_CLOSER = "</%" + KEYWORD_AROUND + ">";

  String KEYWORD_BEFORE = "before";
  String KEYWORD_BEFORE_OPENER = "<%" + KEYWORD_BEFORE;
  String KEYWORD_BEFORE_CLOSER = "</%" + KEYWORD_BEFORE + ">";

  String KEYWORD_OVERRIDE = "override";
  String KEYWORD_OVERRIDE_OPENER = "<%" + KEYWORD_OVERRIDE;
  String KEYWORD_OVERRIDE_CLOSER = "</%" + KEYWORD_OVERRIDE + ">";

  String KEYWORD_FILTERED_BLOCK_OPENER = "{{";
  String KEYWORD_FILTERED_BLOCK_CLOSER = "}}";

  String KEYWORD_SELF_POINTER = "$.";
}
