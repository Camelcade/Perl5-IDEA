/*
 * Copyright 2015-2021 Alexandr Evstigneev
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
import com.intellij.openapi.application.ApplicationBundle;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable;
import com.intellij.psi.codeStyle.CodeStyleSettingsCustomizableOptions;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.PerlLanguage;
import org.jetbrains.annotations.NotNull;

import static com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable.OptionAnchor.AFTER;
import static com.intellij.psi.codeStyle.CodeStyleSettingsCustomizable.WRAP_VALUES;
import static com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider.SettingsType.*;
import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.*;


public class PerlLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {
  private static final String GROUP_QW = PerlBundle.message("perl.qw.list");
  private static final String GROUP_QUOTATION = PerlBundle.message("perl.formatting.group.optional.quotation");
  private static final String GROUP_DEREFERENCE = PerlBundle.message("perl.formatting.group.dereferencing");
  private static final String GROUP_PARENTHESES = PerlBundle.message("perl.formatting.group.optional.parentheses");
  private static final String GROUP_COMPOUND = PerlBundle.message("perl.formatting.group.compound");
  private static final String GROUP_NAMESPACE = PerlBundle.message("perl.formatting.brace.style.namespace");
  private static final String GROUP_SUB = PerlBundle.message("perl.formatting.brace.style.sub");
  private static final String GROUP_VARIABLE_DECLARATION = PerlBundle.message("perl.formatting.wrap.variable.declarations");
  private static final String GROUP_COMMENT = CodeStyleSettingsCustomizableOptions.getInstance().WRAPPING_COMMENTS;
  private static final String GROUP_LIST = CodeStyleSettingsCustomizableOptions.getInstance().WRAPPING_ARRAY_INITIALIZER;
  private static final String GROUP_ATTRIBUTES_WRAP = PerlBundle.message("perl.formatting.wrap.attributes");

  private static final String DEFAULT_CODE_SAMPLE = PerlBundle.message("perl.code.sample.nyi");
  private static final String SPACING_CODE_SAMPLE = readCodeSample("spaces");
  private static final String INDENT_CODE_SAMPLE = readCodeSample("indents");
  private static final String WRAPPING_CODES_SAMPLE = readCodeSample("wrapping");
  private static final String LANGUAGE_SPECIFIC_CODE_SAMPLE = readCodeSample("perl5");

  @Override
  public void customizeSettings(@NotNull CodeStyleSettingsCustomizable consumer, @NotNull SettingsType settingsType) {
    var customizableOptions = CodeStyleSettingsCustomizableOptions.getInstance();
    if (settingsType == SPACING_SETTINGS) {
      consumer.showStandardOptions(
        "SPACE_AROUND_ASSIGNMENT_OPERATORS",
        "SPACE_AROUND_LOGICAL_OPERATORS",
        "SPACE_AROUND_EQUALITY_OPERATORS",
        "SPACE_AROUND_RELATIONAL_OPERATORS",
        "SPACE_AROUND_BITWISE_OPERATORS",
        "SPACE_AROUND_ADDITIVE_OPERATORS",
        "SPACE_AROUND_MULTIPLICATIVE_OPERATORS",
        "SPACE_AROUND_SHIFT_OPERATORS",
        "SPACE_AROUND_UNARY_OPERATOR",

        "SPACE_BEFORE_METHOD_PARENTHESES",
        "SPACE_WITHIN_METHOD_PARENTHESES",
        "SPACE_WITHIN_EMPTY_METHOD_PARENTHESES",

        "SPACE_WITHIN_METHOD_CALL_PARENTHESES", // method()

        "SPACE_AFTER_COMMA",
        "SPACE_BEFORE_COMMA",

        "SPACE_BEFORE_QUEST",
        "SPACE_AFTER_QUEST",
        "SPACE_BEFORE_COLON",
        "SPACE_AFTER_COLON",

        "SPACE_AFTER_SEMICOLON",
        "SPACE_BEFORE_SEMICOLON",

        "SPACE_BEFORE_IF_PARENTHESES",    // any conditional block, for and iterator

        "SPACE_WITHIN_BRACES",            // code blocks
        "SPACE_WITHIN_CAST_PARENTHESES",  // dereference expressions ${...}
        "SPACE_WITHIN_IF_PARENTHESES",    // condition, for iterator
        "SPACE_WITHIN_PARENTHESES",       // @a = (something)

        "SPACE_BEFORE_IF_LBRACE",        //  any or unconditional conditional block, for,

        "SPACE_BEFORE_ELSE_KEYWORD",    // implemented, else,elsif,continue,default

        "SPACE_BEFORE_DO_LBRACE"        // implemented, sub_{}, do_{}, eval_{}
      );
      consumer.renameStandardOption("SPACE_BEFORE_IF_PARENTHESES", PerlBundle.message("perl.formatting.condition"));
      consumer.renameStandardOption("SPACE_BEFORE_IF_LBRACE", PerlBundle.message("perl.formatting.compound.block"));
      consumer.renameStandardOption("SPACE_BEFORE_ELSE_KEYWORD", PerlBundle.message("perl.formatting.compound.secondary"));
      consumer.renameStandardOption("SPACE_BEFORE_DO_LBRACE", PerlBundle.message("perl.formatting.term.block"));
      consumer.renameStandardOption("SPACE_WITHIN_IF_PARENTHESES", PerlBundle.message("perl.formatting.condition"));
      consumer.renameStandardOption("SPACE_WITHIN_BRACES", PerlBundle.message("perl.formatting.within.code.block"));
      consumer.renameStandardOption("SPACE_WITHIN_CAST_PARENTHESES", PerlBundle.message("perl.formatting.within.braced.dereference"));
      consumer.renameStandardOption("SPACE_BEFORE_METHOD_PARENTHESES", PerlBundle.message("perl.formatting.sub.signature"));
      consumer.renameStandardOption("SPACE_WITHIN_METHOD_PARENTHESES", PerlBundle.message("perl.formatting.signature.parentheses"));
      consumer
        .renameStandardOption("SPACE_WITHIN_EMPTY_METHOD_PARENTHESES", PerlBundle.message("perl.formatting.empty.signature.parentheses"));

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "SPACE_BEFORE_ATTRIBUTE",
                                PerlBundle.message("perl.formatting.attribute"),
                                customizableOptions.SPACES_OTHER
      );

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "SPACE_AROUND_CONCAT_OPERATOR",
                                PerlBundle.message("perl.formatting.concatenation"),
                                customizableOptions.SPACES_AROUND_OPERATORS);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "SPACE_AROUND_RANGE_OPERATORS",
                                PerlBundle.message("perl.formatting.range.operators"),
                                customizableOptions.SPACES_AROUND_OPERATORS);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "SPACE_AFTER_VARIABLE_DECLARATION_KEYWORD",
                                PerlBundle.message("perl.formatting.after.my"),
                                customizableOptions.SPACES_OTHER);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "SPACES_WITHIN_ANON_HASH",
                                PerlBundle.message("perl.formatting.within.hash"),
                                customizableOptions.SPACES_WITHIN);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "SPACES_WITHIN_ANON_ARRAY",
                                PerlBundle.message("perl.formatting.within.array"),
                                customizableOptions.SPACES_WITHIN);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "SPACE_WITHIN_QW_QUOTES",
                                PerlBundle.message("perl.qw.list"),
                                customizableOptions.SPACES_WITHIN);
    }
    else if (settingsType == WRAPPING_AND_BRACES_SETTINGS) {
      consumer.showStandardOptions(
        "BINARY_OPERATION_WRAP",
        "BINARY_OPERATION_SIGN_ON_NEXT_LINE",
        "ALIGN_MULTILINE_BINARY_OPERATION",

        "ASSIGNMENT_WRAP",
        "PLACE_ASSIGNMENT_SIGN_ON_NEXT_LINE",

        "WRAP_COMMENTS",

        "ARRAY_INITIALIZER_WRAP",
        "ALIGN_MULTILINE_ARRAY_INITIALIZER_EXPRESSION",

        "CALL_PARAMETERS_WRAP",
        "ALIGN_MULTILINE_PARAMETERS_IN_CALLS",

        "METHOD_PARAMETERS_WRAP",
        "ALIGN_MULTILINE_PARAMETERS",

        "METHOD_CALL_CHAIN_WRAP",
        "ALIGN_MULTILINE_CHAINED_METHODS",

        "TERNARY_OPERATION_WRAP",
        "TERNARY_OPERATION_SIGNS_ON_NEXT_LINE",
        "ALIGN_MULTILINE_TERNARY_OPERATION"
      );

      consumer.renameStandardOption("ASSIGNMENT_WRAP", PerlBundle.message("perl.formatting.wrap.assignment.expression"));
      consumer.renameStandardOption("METHOD_PARAMETERS_WRAP", PerlBundle.message("perl.formatting.wrap.declarations.parameters"));
      consumer.renameStandardOption("METHOD_CALL_CHAIN_WRAP", PerlBundle.message("perl.formatting.wrap.dereference"));

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "METHOD_CALL_CHAIN_SIGN_NEXT_LINE",
                                PerlBundle.message("perl.formatting.dereference.next.line"),
                                customizableOptions.WRAPPING_CALL_CHAIN
      );

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "ALIGN_CONSECUTIVE_ASSIGNMENTS",
                                PerlBundle.message("perl.formatting.align.consecutive.assignments"),
                                customizableOptions.WRAPPING_ASSIGNMENT,
                                ALIGN_ASSIGNMENTS_OPTIONS
      );

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "ALIGN_COMMENTS_ON_CONSEQUENT_LINES",
                                PerlBundle.message("perl.formatting.align.comments.in.list"),
                                GROUP_COMMENT);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "VARIABLE_DECLARATION_WRAP",
                                GROUP_VARIABLE_DECLARATION,
                                null,
                                AFTER, "METHOD_PARAMETERS_WRAP",
                                customizableOptions.WRAP_OPTIONS, WRAP_VALUES);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "ALIGN_VARIABLE_DECLARATIONS",
                                ApplicationBundle.message("wrapping.align.when.multiline"),
                                GROUP_VARIABLE_DECLARATION
      );

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "BRACE_STYLE_NAMESPACE",
                                customizableOptions.WRAPPING_BRACES,
                                GROUP_NAMESPACE,
                                BRACE_PLACEMENT_OPTIONS
      );

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "BRACE_STYLE_SUB",
                                customizableOptions.WRAPPING_BRACES,
                                GROUP_SUB,
                                BRACE_PLACEMENT_OPTIONS
      );

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "ELSE_ON_NEW_LINE",
                                PerlBundle.message("perl.formatting.compound.secondary"),
                                GROUP_COMPOUND
      );

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "BRACE_STYLE_COMPOUND",
                                customizableOptions.WRAPPING_BRACES,
                                GROUP_COMPOUND,
                                BRACE_PLACEMENT_OPTIONS
      );

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "ATTRIBUTES_WRAP",
                                GROUP_ATTRIBUTES_WRAP,
                                null,
                                customizableOptions.WRAP_OPTIONS, WRAP_VALUES);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "ALIGN_ATTRIBUTES",
                                ApplicationBundle.message("wrapping.align.when.multiline"),
                                GROUP_ATTRIBUTES_WRAP
      );

      consumer.renameStandardOption("ARRAY_INITIALIZER_WRAP", PerlBundle.message("perl.formatting.align.list.elements"));
      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "ALIGN_FAT_COMMA",
                                PerlBundle.message("perl.formatting.align.fat.comma"),
                                GROUP_LIST);

      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "QW_LIST_WRAP",
                                GROUP_QW,
                                null,
                                AFTER, "ARRAY_INITIALIZER_WRAP",
                                customizableOptions.WRAP_OPTIONS, WRAP_VALUES
      );
      consumer.showCustomOption(PerlCodeStyleSettings.class,
                                "ALIGN_QW_ELEMENTS",
                                PerlBundle.message("perl.formatting.align.qw.elements"),
                                GROUP_QW);

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
                                customizableOptions.SPACES_OTHER,
                                OPTIONS_MAIN_FORMAT);
    }
  }

  @Override
  public IndentOptionsEditor getIndentOptionsEditor() {
    return new SmartIndentOptionsEditor();
  }

  @Override
  protected void customizeDefaults(@NotNull CommonCodeStyleSettings commonSettings,
                                   @NotNull CommonCodeStyleSettings.IndentOptions indentOptions) {
    indentOptions.INDENT_SIZE = 4;
    indentOptions.CONTINUATION_INDENT_SIZE = 4;
    indentOptions.TAB_SIZE = 4;
  }

  @Override
  public @NotNull Language getLanguage() {
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

  @Override
  public int getRightMargin(@NotNull SettingsType settingsType) {
    return settingsType == SettingsType.WRAPPING_AND_BRACES_SETTINGS ? 45 : super.getRightMargin(settingsType);
  }

  private static @NotNull String readCodeSample(@NotNull String name) {
    return CodeStyleAbstractPanel.readFromFile(PerlLanguageCodeStyleSettingsProvider.class, name + ".code");
  }
}
