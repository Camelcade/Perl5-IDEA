/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.formatter.settings;

import com.intellij.openapi.application.ApplicationBundle;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CustomCodeStyleSettings;
import com.perl5.PerlBundle;

import static com.intellij.psi.codeStyle.CommonCodeStyleSettings.DO_NOT_WRAP;
import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.SAME_LINE;
import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.WHATEVER;
import static com.perl5.lang.perl.util.PerlPackageUtil.MAIN_NAMESPACE_FULL;
import static com.perl5.lang.perl.util.PerlPackageUtil.PACKAGE_SEPARATOR;


public class PerlCodeStyleSettings extends CustomCodeStyleSettings {
  public int OPTIONAL_QUOTES = WHATEVER;
  public int OPTIONAL_QUOTES_HASH_INDEX = WHATEVER;
  public int OPTIONAL_QUOTES_HEREDOC_OPENER = WHATEVER;

  public int OPTIONAL_DEREFERENCE = WHATEVER;
  public int OPTIONAL_DEREFERENCE_HASHREF_ELEMENT = WHATEVER;
  public int OPTIONAL_DEREFERENCE_SIMPLE = WHATEVER;

  public int OPTIONAL_PARENTHESES = WHATEVER;

  public int MAIN_FORMAT = WHATEVER;

  public boolean SPACE_AFTER_VARIABLE_DECLARATION_KEYWORD = true;

  public boolean SPACES_WITHIN_ANON_HASH = true;
  public boolean SPACES_WITHIN_ANON_ARRAY = true;

  public boolean SPACE_AROUND_RANGE_OPERATORS = true;
  public boolean SPACE_AROUND_CONCAT_OPERATOR = true;
  public boolean SPACE_WITHIN_QW_QUOTES = false;

  public boolean SPACE_BEFORE_ATTRIBUTE = true;
  public int ATTRIBUTES_WRAP = DO_NOT_WRAP;
  public boolean ALIGN_ATTRIBUTES = false;
  public int ALIGN_CONSECUTIVE_ASSIGNMENTS = 0;

  public boolean ALIGN_FAT_COMMA = true;

  public int QW_LIST_WRAP = DO_NOT_WRAP;
  public boolean ALIGN_QW_ELEMENTS = false;

  public boolean ALIGN_COMMENTS_ON_CONSEQUENT_LINES = true;

  public int BRACE_STYLE_NAMESPACE = SAME_LINE;
  public int BRACE_STYLE_SUB = SAME_LINE;
  public int BRACE_STYLE_COMPOUND = SAME_LINE;

  public boolean ELSE_ON_NEW_LINE = true;

  public int VARIABLE_DECLARATION_WRAP = DO_NOT_WRAP;
  public boolean ALIGN_VARIABLE_DECLARATIONS = false;

  public PerlCodeStyleSettings(CodeStyleSettings container) {
    super("Perl5CodeStyleSettings", container);
  }

  public interface OptionalConstructions {
    int WHATEVER = 0x00;
    int FORCE = 0x01;
    int SUPPRESS = 0x02;
    int[] VALUES = {WHATEVER, FORCE, SUPPRESS};

    String AS_IS = PerlBundle.message("perl.formatting.option.asis");

    String[] LABELS_DEFAULT =
      {AS_IS, PerlBundle.message("perl.formatting.option.force"), PerlBundle.message("perl.formatting.option.suppress")};
    Object[] OPTIONS_DEFAULT = {LABELS_DEFAULT, VALUES};

    String[] LABELS_HASHREF_ELEMENT_STYLE =
      {AS_IS, PerlBundle.message("perl.formatting.option.hashref.short"), PerlBundle.message("perl.formatting.option.hashref.long")};
    Object[] OPTIONS_HASHREF_ELEMENT = {LABELS_HASHREF_ELEMENT_STYLE, VALUES};

    String[] LABELS_SIMPLE_DEREF_STYLE =
      {AS_IS, PerlBundle.message("perl.formatting.option.hashref.braced"), PerlBundle.message("perl.formatting.option.hashref.unbraced")};
    Object[] OPTIONS_SIMPLE_DEREF_STYLE = {LABELS_SIMPLE_DEREF_STYLE, VALUES};

    int NO_ALIGN = 0;
    int ALIGN_IN_STATEMENT = 1;
    int ALIGN_LINES = 2;
    int[] ALIGN_ASSIGNMENTS_VALUES = {
      NO_ALIGN, ALIGN_IN_STATEMENT, ALIGN_LINES
    };
    String[] ALIGN_ASSIGNMENTS_LABELS = {
      PerlBundle.message("perl.formatting.align.assignments.no"),
      PerlBundle.message("perl.formatting.align.assignments.statement"),
      PerlBundle.message("perl.formatting.align.assignments.lines")
    };
    Object[] ALIGN_ASSIGNMENTS_OPTIONS = {ALIGN_ASSIGNMENTS_LABELS, ALIGN_ASSIGNMENTS_VALUES};

    int SAME_LINE = 0;
    int NEXT_LINE = 1;
    int[] BRACE_PLACEMENT_VALUES = {SAME_LINE, NEXT_LINE};
    String[] BRACE_PLACEMENT_LABELS = {
      ApplicationBundle.message("wrapping.brace.placement.end.of.line"),
      ApplicationBundle.message("wrapping.brace.placement.next.line")
    };
    Object[] BRACE_PLACEMENT_OPTIONS = {BRACE_PLACEMENT_LABELS, BRACE_PLACEMENT_VALUES};

    String[] LABELS_MAIN_FORMAT = {AS_IS, MAIN_NAMESPACE_FULL, PACKAGE_SEPARATOR};
    Object[] OPTIONS_MAIN_FORMAT = {LABELS_MAIN_FORMAT, VALUES};
  }
}
