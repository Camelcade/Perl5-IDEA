/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.application.options.SmartIndentOptionsEditor;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.PerlLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable.*;
import static com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider.SettingsType.*;
import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.BRACE_PLACEMENT_OPTIONS;
import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.*;

/**
 * Created by hurricup on 03.09.2015.
 */
public class PerlLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {
  private static final String GROUP_ALIGNMENT = PerlBundle.message("perl.formatting.group.alignment");
  private static final String GROUP_QUOTATION = PerlBundle.message("perl.formatting.group.optional.quotation");
  private static final String GROUP_DEREFERENCE = PerlBundle.message("perl.formatting.group.dereferencing");
  private static final String GROUP_PARENTHESES = PerlBundle.message("perl.formatting.group.optional.parentheses");
  private static final String GROUP_NEW_LINE_BEFORE = PerlBundle.message("perl.formatting.group.new.line.before");

  private static final String DEFAULT_CODE_SAMPLE = PerlBundle.message("perl.code.sample.nyi");
  private static final String SPACING_CODE_SAMPLE = readCodeSample("spaces");
  private static final String INDENT_CODE_SAMPLE = readCodeSample("indents");
  private static final String WRAPPING_CODES_SAMPLE = readCodeSample("wrapping");
  private static final String LANGUAGE_SPECIFIC_CODE_SAMPLE = readCodeSample("perl5");

  @Override
  public void customizeSettings(@NotNull CodeStyleSettingsCustomizable consumer, @NotNull SettingsType settingsType) {
    if (settingsType == SPACING_SETTINGS) {
      consumer.showStandardOptions(
        "SPACE_AROUND_ASSIGNMENT_OPERATORS",    // implemented
        "SPACE_AROUND_LOGICAL_OPERATORS",        // implemented
        "SPACE_AROUND_EQUALITY_OPERATORS",        // implemented
        "SPACE_AROUND_RELATIONAL_OPERATORS",    // implemented
        "SPACE_AROUND_BITWISE_OPERATORS",        // implemented
        "SPACE_AROUND_ADDITIVE_OPERATORS",        // implemented
        "SPACE_AROUND_MULTIPLICATIVE_OPERATORS",// implemented
        "SPACE_AROUND_SHIFT_OPERATORS",            // implemented
        "SPACE_AROUND_UNARY_OPERATOR",            // implemented

        "SPACE_BEFORE_METHOD_PARENTHESES",
        "SPACE_WITHIN_METHOD_PARENTHESES",
        "SPACE_WITHIN_EMPTY_METHOD_PARENTHESES",

        "SPACE_WITHIN_METHOD_CALL_PARENTHESES", // method()

        "SPACE_AFTER_COMMA",    // implemented
        "SPACE_BEFORE_COMMA",   // implemented

        "SPACE_BEFORE_QUEST",
        "SPACE_AFTER_QUEST",
        "SPACE_BEFORE_COLON",
        "SPACE_AFTER_COLON",    // implemented

        "SPACE_AFTER_SEMICOLON",    // implemented
        "SPACE_BEFORE_SEMICOLON",    // implemented

        "SPACE_BEFORE_IF_PARENTHESES",    // implemented, any conditional block, for and iterator

        "SPACE_WITHIN_BRACES",            // for code blocks
        "SPACE_WITHIN_IF_PARENTHESES",    // condition, for iterator
        "SPACE_WITHIN_PARENTHESES",       // @a = (something)

        "SPACE_BEFORE_IF_LBRACE",        // implemented, any or undonditional conditional block, for,

        "SPACE_BEFORE_ELSE_KEYWORD",    // implemented, else,elsif,continue,default

        "SPACE_BEFORE_DO_LBRACE"        // implemented, sub_{}, do_{}, eval_{}
      );
      consumer.renameStandardOption("SPACE_BEFORE_IF_PARENTHESES", PerlBundle.message("perl.formatting.condition"));
      consumer.renameStandardOption("SPACE_BEFORE_IF_LBRACE", PerlBundle.message("perl.formatting.compound.block"));
      consumer.renameStandardOption("SPACE_BEFORE_ELSE_KEYWORD", PerlBundle.message("perl.formatting.compound.secondary"));
      consumer.renameStandardOption("SPACE_BEFORE_DO_LBRACE", PerlBundle.message("perl.formatting.term.block"));
      consumer.renameStandardOption("SPACE_WITHIN_IF_PARENTHESES", PerlBundle.message("perl.formatting.condition"));
      consumer.renameStandardOption("SPACE_WITHIN_BRACES", PerlBundle.message("perl.formatting.within.code.block"));
      consumer.renameStandardOption("SPACE_BEFORE_METHOD_PARENTHESES", PerlBundle.message("perl.formatting.sub.signature"));
      consumer.renameStandardOption("SPACE_WITHIN_METHOD_PARENTHESES", PerlBundle.message("perl.formatting.signature.parentheses"));
      consumer
        .renameStandardOption("SPACE_WITHIN_EMPTY_METHOD_PARENTHESES", PerlBundle.message("perl.formatting.empty.signature.parentheses"));

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "SPACE_BEFORE_ATTRIBUTE",
                                PerlBundle.message("perl.formatting.attribute"),
                                SPACES_OTHER
      );

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "SPACE_AROUND_CONCAT_OPERATOR",
                                PerlBundle.message("perl.formatting.concatenation"),
                                SPACES_AROUND_OPERATORS);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "SPACE_AROUND_RANGE_OPERATORS",
                                PerlBundle.message("perl.formatting.range.operators"),
                                SPACES_AROUND_OPERATORS);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "SPACE_AFTER_VARIABLE_DECLARATION_KEYWORD",
                                PerlBundle.message("perl.formatting.after.my"),
                                SPACES_OTHER);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "SPACES_WITHIN_ANON_HASH",
                                PerlBundle.message("perl.formatting.within.hash"),
                                SPACES_WITHIN);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "SPACES_WITHIN_ANON_ARRAY",
                                PerlBundle.message("perl.formatting.within.array"),
                                SPACES_WITHIN);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "SPACE_WITHIN_QW_QUOTES",
                                PerlBundle.message("perl.formatting.space.inside.qw"),
                                SPACES_WITHIN);
    }
    else if (settingsType == WRAPPING_AND_BRACES_SETTINGS) {

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "ELSE_ON_NEW_LINE",
                                PerlBundle.message("perl.formatting.compound.secondary"),
                                GROUP_NEW_LINE_BEFORE
      );

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "BRACE_STYLE_NAMESPACE",
                                PerlBundle.message("perl.formatting.brace.style.namespace"),
                                WRAPPING_BRACES,
                                BRACE_PLACEMENT_OPTIONS
      );

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "BRACE_STYLE_SUB",
                                PerlBundle.message("perl.formatting.brace.style.sub"),
                                WRAPPING_BRACES,
                                BRACE_PLACEMENT_OPTIONS
      );

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "BRACE_STYLE_COMPOUND",
                                PerlBundle.message("perl.formatting.brace.style.compound"),
                                WRAPPING_BRACES,
                                BRACE_PLACEMENT_OPTIONS
      );

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "ALIGN_FAT_COMMA",
                                PerlBundle.message("perl.formatting.align.fat.comma"),
                                GROUP_ALIGNMENT);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "ALIGN_QW_ELEMENTS",
                                PerlBundle.message("perl.formatting.align.qw.elements"),
                                GROUP_ALIGNMENT);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "ALIGN_TERNARY",
                                PerlBundle.message("perl.formatting.align.ternary"),
                                GROUP_ALIGNMENT);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "ALIGN_DEREFERENCE_IN_CHAIN",
                                PerlBundle.message("perl.formatting.align.dereference"),
                                GROUP_ALIGNMENT);
    }
    else if (settingsType == LANGUAGE_SPECIFIC) {

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "OPTIONAL_QUOTES",
                                PerlBundle.message("perl.formatting.quotation.before.fatcomma"),
                                GROUP_QUOTATION,
                                OPTIONS_DEFAULT);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "OPTIONAL_QUOTES_HASH_INDEX",
                                PerlBundle.message("perl.formatting.quotation.hash.index"),
                                GROUP_QUOTATION,
                                OPTIONS_DEFAULT);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "OPTIONAL_QUOTES_HEREDOC_OPENER",
                                PerlBundle.message("perl.formatting.quotation.heredoc.opener"),
                                GROUP_QUOTATION,
                                OPTIONS_DEFAULT);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "OPTIONAL_DEREFERENCE",
                                PerlBundle.message("perl.formatting.deref.indexes"),
                                GROUP_DEREFERENCE,
                                OPTIONS_DEFAULT);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "OPTIONAL_DEREFERENCE_HASHREF_ELEMENT",
                                PerlBundle.message("perl.formatting.hashref.element"),
                                GROUP_DEREFERENCE,
                                OPTIONS_HASHREF_ELEMENT);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "OPTIONAL_DEREFERENCE_SIMPLE",
                                PerlBundle.message("perl.formatting.simple.dereference"),
                                GROUP_DEREFERENCE,
                                OPTIONS_SIMPLE_DEREF_STYLE);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "OPTIONAL_PARENTHESES",
                                PerlBundle.message("perl.formatting.statement.modifiers"),
                                GROUP_PARENTHESES,
                                OPTIONS_DEFAULT);

      //			consumer.showCustomOption(PerlCodeStyleSettings.class, "OPTIONAL_SEMI", PERL_OPTION_OPTIONAL_SEMI, OPTIONAL_ELEMENTS_GROUP, PerlCodeStyleSettings.OptionalConstructions.OPTIONS_DEFAULT);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "MAIN_FORMAT",
                                PerlBundle.message("perl.formatting.main.format"),
                                SPACES_OTHER,
                                OPTIONS_MAIN_FORMAT);
    }
  }

  @Override
  public IndentOptionsEditor getIndentOptionsEditor() {
    return new SmartIndentOptionsEditor();
  }

  @Nullable
  @Override
  public CommonCodeStyleSettings getDefaultCommonSettings() {
    CommonCodeStyleSettings defaultSettings = new CommonCodeStyleSettings(getLanguage());
    CommonCodeStyleSettings.IndentOptions indentOptions = defaultSettings.initIndentOptions();
    indentOptions.INDENT_SIZE = 4;
    indentOptions.CONTINUATION_INDENT_SIZE = 4;
    indentOptions.TAB_SIZE = 4;

    return defaultSettings;
  }

  @NotNull
  @Override
  public Language getLanguage() {
    return PerlLanguage.INSTANCE;
  }

  @Override
  public String getCodeSample(@NotNull SettingsType settingsType) {
    if (settingsType == SPACING_SETTINGS) {
      return SPACING_CODE_SAMPLE;
    }
    else if (settingsType == INDENT_SETTINGS) {
      return INDENT_CODE_SAMPLE;
    }
    else if (settingsType == WRAPPING_AND_BRACES_SETTINGS) {
      return WRAPPING_CODES_SAMPLE;
    }
    else if (settingsType == LANGUAGE_SPECIFIC) {
      return LANGUAGE_SPECIFIC_CODE_SAMPLE;
    }
    return DEFAULT_CODE_SAMPLE;
  }

  @NotNull
  private static String readCodeSample(@NotNull String name) {
    return CodeStyleAbstractPanel.readFromFile(PerlLanguageCodeStyleSettingsProvider.class, name + ".code");
  }
}
