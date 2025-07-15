// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.perl5.lang.perl.parser.PerlElementTypesGenerated.*;
import static com.perl5.lang.perl.parser.PerlParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;
import static com.intellij.lang.WhitespacesBinders.*;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class PerlParserGenerated implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    parseLight(root_, builder_);
    return builder_.getTreeBuilt();
  }

  public void parseLight(IElementType root_, PsiBuilder builder_) {
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this, EXTENDS_SETS_);
    Marker marker_ = enter_section_(builder_, 0, _COLLAPSE_, null);
    result_ = parse_root_(root_, builder_);
    exit_section_(builder_, 0, marker_, root_, result_, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType root_, PsiBuilder builder_) {
    return parse_root_(root_, builder_, 0);
  }

  static boolean parse_root_(IElementType root_, PsiBuilder builder_, int level_) {
    boolean result_;
    if (root_ == ARRAY_CAST_EXPR) {
      result_ = array_cast_expr(builder_, level_ + 1);
    }
    else if (root_ == ARRAY_INDEX) {
      result_ = array_index(builder_, level_ + 1);
    }
    else if (root_ == BLOCK) {
      result_ = block(builder_, level_ + 1);
    }
    else if (root_ == CODE_CAST_EXPR) {
      result_ = code_cast_expr(builder_, level_ + 1);
    }
    else if (root_ == COMMENT_ANNOTATION) {
      result_ = comment_annotation(builder_, level_ + 1);
    }
    else if (root_ == CONDITION_EXPR) {
      result_ = condition_expr(builder_, level_ + 1);
    }
    else if (root_ == GLOB_CAST_EXPR) {
      result_ = glob_cast_expr(builder_, level_ + 1);
    }
    else if (root_ == HASH_CAST_EXPR) {
      result_ = hash_cast_expr(builder_, level_ + 1);
    }
    else if (root_ == HASH_INDEX) {
      result_ = hash_index(builder_, level_ + 1);
    }
    else if (root_ == HEREDOC) {
      result_ = heredoc(builder_, level_ + 1);
    }
    else if (root_ == HEREDOC_QQ) {
      result_ = heredoc_qq(builder_, level_ + 1);
    }
    else if (root_ == HEREDOC_QX) {
      result_ = heredoc_qx(builder_, level_ + 1);
    }
    else if (root_ == PARENTHESISED_CALL_ARGUMENTS) {
      result_ = parenthesised_call_arguments(builder_, level_ + 1);
    }
    else if (root_ == PARENTHESISED_EXPR) {
      result_ = parenthesised_expr(builder_, level_ + 1);
    }
    else if (root_ == PARSABLE_STRING_USE_VARS) {
      result_ = parsable_string_use_vars(builder_, level_ + 1);
    }
    else if (root_ == SCALAR_CAST_EXPR) {
      result_ = scalar_cast_expr(builder_, level_ + 1);
    }
    else if (root_ == STRING_LIST) {
      result_ = string_list(builder_, level_ + 1);
    }
    else {
      result_ = root(builder_, level_ + 1);
    }
    return result_;
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(STATEMENT, SUB_DECLARATION),
    create_token_set_(CALL_ARGUMENTS, PARENTHESISED_CALL_ARGUMENTS),
    create_token_set_(BLOCK, BLOCK_ARRAY, BLOCK_BRACELESS, BLOCK_CODE,
      BLOCK_GLOB, BLOCK_HASH, BLOCK_SCALAR),
    create_token_set_(FOR_STATEMENT_MODIFIER, IF_STATEMENT_MODIFIER, STATEMENT_MODIFIER, UNLESS_STATEMENT_MODIFIER,
      UNTIL_STATEMENT_MODIFIER, WHEN_STATEMENT_MODIFIER, WHILE_STATEMENT_MODIFIER),
    create_token_set_(ADD_EXPR, AND_EXPR, ANON_ARRAY, ANON_HASH,
      ARRAY_CAST_EXPR, ARRAY_ELEMENT, ARRAY_INDEX_VARIABLE, ARRAY_POP_EXPR,
      ARRAY_PUSH_EXPR, ARRAY_SHIFT_EXPR, ARRAY_SLICE, ARRAY_UNSHIFT_EXPR,
      ARRAY_VARIABLE, ASSIGN_EXPR, BITWISE_AND_EXPR, BITWISE_OR_XOR_EXPR,
      BLESS_EXPR, CATCH_EXPR, CODE_CAST_EXPR, CODE_VARIABLE,
      COMMA_SEQUENCE_EXPR, COMPARE_EXPR, COMPILE_REGEX, COMPOSITE_ATOM_EXPR,
      CONDITION_EXPR, CONTINUATION_EXPR, CONTINUE_EXPR, CUSTOM_ATOM_EXPR,
      DEFINED_EXPR, DELETE_EXPR, DEREF_EXPR, DO_BLOCK_EXPR,
      DO_EXPR, EACH_EXPR, EQUAL_EXPR, EVAL_EXPR,
      EXCEPT_EXPR, EXIT_EXPR, EXPR, FILE_GLOB_EXPR,
      FILE_READ_EXPR, FINALLY_EXPR, FLIPFLOP_EXPR, FUN_EXPR,
      GLOB_CAST_EXPR, GLOB_SLOT, GLOB_VARIABLE, GOTO_EXPR,
      GREP_EXPR, HASH_ARRAY_SLICE, HASH_CAST_EXPR, HASH_ELEMENT,
      HASH_HASH_SLICE, HASH_SLICE, HASH_VARIABLE, HEREDOC_OPENER,
      ISA_EXPR, KEYS_EXPR, LABEL_EXPR, LAST_EXPR,
      LP_AND_EXPR, LP_NOT_EXPR, LP_OR_XOR_EXPR, MAP_EXPR,
      MATCH_REGEX, METHOD_EXPR, MUL_EXPR, NEXT_EXPR,
      NUMBER_CONSTANT, OR_EXPR, OTHERWISE_EXPR, PACKAGE_EXPR,
      PARENTHESISED_EXPR, PERL_HANDLE_EXPR, POST_DEREF_ARRAY_SLICE_EXPR, POST_DEREF_EXPR,
      POST_DEREF_GLOB_EXPR, POST_DEREF_HASH_SLICE_EXPR, POW_EXPR, PREFIX_UNARY_EXPR,
      PREF_PP_EXPR, PRINT_EXPR, REDO_EXPR, REF_EXPR,
      REGEX_EXPR, REPLACEMENT_REGEX, REQUIRE_EXPR, RETURN_EXPR,
      SCALAR_CAST_EXPR, SCALAR_EXPR, SCALAR_INDEX_CAST_EXPR, SCALAR_VARIABLE,
      SHIFT_EXPR, SORT_EXPR, SPLICE_EXPR, STRING_BARE,
      STRING_DQ, STRING_LIST, STRING_SQ, STRING_XQ,
      SUB_CALL, SUB_EXPR, SUFF_PP_EXPR, TAG_SCALAR,
      TERNARY_EXPR, TRYCATCH_EXPR, TRY_EXPR, TR_REGEX,
      UNDEF_EXPR, VALUES_EXPR, VARIABLE_DECLARATION_GLOBAL, VARIABLE_DECLARATION_LEXICAL,
      VARIABLE_DECLARATION_LOCAL, WANTARRAY_EXPR),
  };

  /* ********************************************************** */
  // 'fp_after' fp_modifier_named_body
  public static boolean after_modifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "after_modifier")) return false;
    if (!nextTokenIs(builder_, "<after modifier>", RESERVED_AFTER_FP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, AFTER_MODIFIER, "<after modifier>");
    result_ = consumeToken(builder_, RESERVED_AFTER_FP);
    result_ = result_ && fp_modifier_named_body(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // annotation_abstract
  // 	| annotation_deprecated
  // 	| annotation_method
  // 	| annotation_override
  // 	| annotation_returns
  // 	| annotation_type
  // 	| annotation_inject
  // 	| annotation_no_inject
  // 	| annotation_noinspection
  // 	| '#@unknown'
  static boolean annotation(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "annotation")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<perl annotation>");
    result_ = annotation_abstract(builder_, level_ + 1);
    if (!result_) result_ = annotation_deprecated(builder_, level_ + 1);
    if (!result_) result_ = annotation_method(builder_, level_ + 1);
    if (!result_) result_ = annotation_override(builder_, level_ + 1);
    if (!result_) result_ = annotation_returns(builder_, level_ + 1);
    if (!result_) result_ = annotation_type(builder_, level_ + 1);
    if (!result_) result_ = annotation_inject(builder_, level_ + 1);
    if (!result_) result_ = annotation_no_inject(builder_, level_ + 1);
    if (!result_) result_ = annotation_noinspection(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, ANNOTATION_UNKNOWN_KEY);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '#@abstract'
  public static boolean annotation_abstract(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "annotation_abstract")) return false;
    if (!nextTokenIs(builder_, ANNOTATION_ABSTRACT_KEY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, ANNOTATION_ABSTRACT_KEY);
    exit_section_(builder_, marker_, ANNOTATION_ABSTRACT, result_);
    return result_;
  }

  /* ********************************************************** */
  // '#@deprecated'
  public static boolean annotation_deprecated(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "annotation_deprecated")) return false;
    if (!nextTokenIs(builder_, ANNOTATION_DEPRECATED_KEY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, ANNOTATION_DEPRECATED_KEY);
    exit_section_(builder_, marker_, ANNOTATION_DEPRECATED, result_);
    return result_;
  }

  /* ********************************************************** */
  // '#@inject' string_bare
  public static boolean annotation_inject(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "annotation_inject")) return false;
    if (!nextTokenIs(builder_, ANNOTATION_INJECT_KEY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ANNOTATION_INJECT, null);
    result_ = consumeToken(builder_, ANNOTATION_INJECT_KEY);
    pinned_ = result_; // pin = 1
    result_ = result_ && string_bare(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '#@method'
  public static boolean annotation_method(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "annotation_method")) return false;
    if (!nextTokenIs(builder_, ANNOTATION_METHOD_KEY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, ANNOTATION_METHOD_KEY);
    exit_section_(builder_, marker_, ANNOTATION_METHOD, result_);
    return result_;
  }

  /* ********************************************************** */
  // '#@noinject'
  public static boolean annotation_no_inject(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "annotation_no_inject")) return false;
    if (!nextTokenIs(builder_, ANNOTATION_NO_INJECT_KEY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, ANNOTATION_NO_INJECT_KEY);
    exit_section_(builder_, marker_, ANNOTATION_NO_INJECT, result_);
    return result_;
  }

  /* ********************************************************** */
  // '#@noinspection' string_bare
  public static boolean annotation_noinspection(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "annotation_noinspection")) return false;
    if (!nextTokenIs(builder_, ANNOTATION_NOINSPECTION_KEY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ANNOTATION_NOINSPECTION, null);
    result_ = consumeToken(builder_, ANNOTATION_NOINSPECTION_KEY);
    pinned_ = result_; // pin = 1
    result_ = result_ && string_bare(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '#@override'
  public static boolean annotation_override(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "annotation_override")) return false;
    if (!nextTokenIs(builder_, ANNOTATION_OVERRIDE_KEY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, ANNOTATION_OVERRIDE_KEY);
    exit_section_(builder_, marker_, ANNOTATION_OVERRIDE, result_);
    return result_;
  }

  /* ********************************************************** */
  // '#@returns' annotation_type_param
  public static boolean annotation_returns(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "annotation_returns")) return false;
    if (!nextTokenIs(builder_, ANNOTATION_RETURNS_KEY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ANNOTATION_RETURNS, null);
    result_ = consumeToken(builder_, ANNOTATION_RETURNS_KEY);
    pinned_ = result_; // pin = 1
    result_ = result_ && annotation_type_param(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '#@type' [annotation_variable] annotation_type_param
  public static boolean annotation_type(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "annotation_type")) return false;
    if (!nextTokenIs(builder_, ANNOTATION_TYPE_KEY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ANNOTATION_TYPE, null);
    result_ = consumeToken(builder_, ANNOTATION_TYPE_KEY);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, annotation_type_1(builder_, level_ + 1));
    result_ = pinned_ && annotation_type_param(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [annotation_variable]
  private static boolean annotation_type_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "annotation_type_1")) return false;
    annotation_variable(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // '*' |
  //   arrayref_type |
  //   hashref_type |
  //   any_package
  static boolean annotation_type_param(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "annotation_type_param")) return false;
    boolean result_;
    result_ = consumeToken(builder_, OPERATOR_MUL);
    if (!result_) result_ = arrayref_type(builder_, level_ + 1);
    if (!result_) result_ = hashref_type(builder_, level_ + 1);
    if (!result_) result_ = any_package(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '$annotated'|'@annotated'|'%annotated'
  public static boolean annotation_variable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "annotation_variable")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ANNOTATION_VARIABLE, "<annotation variable>");
    result_ = consumeToken(builder_, ANNOTATION_SCALAR);
    if (!result_) result_ = consumeToken(builder_, ANNOTATION_ARRAY);
    if (!result_) result_ = consumeToken(builder_, ANNOTATION_HASH);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '{' anon_hash_lookahead_body '}'
  static boolean anon_hash_lookahead(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "anon_hash_lookahead")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_BRACE);
    result_ = result_ && anon_hash_lookahead_body(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_BRACE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // array_or_slice  |
  //   hash_or_slice  |
  //   // tried to optimize this, but technically not possible. E.g. if first line contains some comma sequence, see heredocWrappingTest
  //   parse_scalar_expr {comma parse_scalar_expr} +
  static boolean anon_hash_lookahead_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "anon_hash_lookahead_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = array_or_slice(builder_, level_ + 1);
    if (!result_) result_ = hash_or_slice(builder_, level_ + 1);
    if (!result_) result_ = anon_hash_lookahead_body_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // parse_scalar_expr {comma parse_scalar_expr} +
  private static boolean anon_hash_lookahead_body_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "anon_hash_lookahead_body_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_scalar_expr(builder_, level_ + 1);
    result_ = result_ && anon_hash_lookahead_body_2_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // {comma parse_scalar_expr} +
  private static boolean anon_hash_lookahead_body_2_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "anon_hash_lookahead_body_2_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = anon_hash_lookahead_body_2_1_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!anon_hash_lookahead_body_2_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "anon_hash_lookahead_body_2_1", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // comma parse_scalar_expr
  private static boolean anon_hash_lookahead_body_2_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "anon_hash_lookahead_body_2_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = comma(builder_, level_ + 1);
    result_ = result_ && parse_scalar_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // parenthesised_call_arguments | [call_arguments]
  static boolean any_call_arguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "any_call_arguments")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parenthesised_call_arguments(builder_, level_ + 1);
    if (!result_) result_ = any_call_arguments_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [call_arguments]
  private static boolean any_call_arguments_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "any_call_arguments_1")) return false;
    call_arguments(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // 'package::name' | '__PACKAGE__'
  static boolean any_package(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "any_package")) return false;
    if (!nextTokenIs(builder_, "", PACKAGE, TAG_PACKAGE)) return false;
    boolean result_;
    result_ = consumeToken(builder_, PACKAGE);
    if (!result_) result_ = consumeToken(builder_, TAG_PACKAGE);
    return result_;
  }

  /* ********************************************************** */
  // parenthesised_call_arguments | unary_call_arguments
  static boolean any_unary_call_arguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "any_unary_call_arguments")) return false;
    boolean result_;
    result_ = parenthesised_call_arguments(builder_, level_ + 1);
    if (!result_) result_ = unary_call_arguments(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // argumentless_method
  static boolean argumentless_call(PsiBuilder builder_, int level_) {
    return argumentless_method(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // 'argumentless'
  public static boolean argumentless_method(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "argumentless_method")) return false;
    if (!nextTokenIs(builder_, BUILTIN_ARGUMENTLESS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, BUILTIN_ARGUMENTLESS);
    exit_section_(builder_, marker_, METHOD, result_);
    return result_;
  }

  /* ********************************************************** */
  // sub_expr_simple [[comma] parse_scalar_expr {comma parse_scalar_expr}*]
  public static boolean arguments_list_with_codeblock(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "arguments_list_with_codeblock")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, COMMA_SEQUENCE_EXPR, null);
    result_ = sub_expr_simple(builder_, level_ + 1);
    result_ = result_ && arguments_list_with_codeblock_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [[comma] parse_scalar_expr {comma parse_scalar_expr}*]
  private static boolean arguments_list_with_codeblock_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "arguments_list_with_codeblock_1")) return false;
    arguments_list_with_codeblock_1_0(builder_, level_ + 1);
    return true;
  }

  // [comma] parse_scalar_expr {comma parse_scalar_expr}*
  private static boolean arguments_list_with_codeblock_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "arguments_list_with_codeblock_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = arguments_list_with_codeblock_1_0_0(builder_, level_ + 1);
    result_ = result_ && parse_scalar_expr(builder_, level_ + 1);
    result_ = result_ && arguments_list_with_codeblock_1_0_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [comma]
  private static boolean arguments_list_with_codeblock_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "arguments_list_with_codeblock_1_0_0")) return false;
    comma(builder_, level_ + 1);
    return true;
  }

  // {comma parse_scalar_expr}*
  private static boolean arguments_list_with_codeblock_1_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "arguments_list_with_codeblock_1_0_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!arguments_list_with_codeblock_1_0_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "arguments_list_with_codeblock_1_0_2", pos_)) break;
    }
    return true;
  }

  // comma parse_scalar_expr
  private static boolean arguments_list_with_codeblock_1_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "arguments_list_with_codeblock_1_0_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = comma(builder_, level_ + 1);
    result_ = result_ && parse_scalar_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // 'fp_around' sub_names_token around_signature func_or_method_body
  public static boolean around_modifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "around_modifier")) return false;
    if (!nextTokenIs(builder_, "<around modifier>", RESERVED_AROUND_FP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, AROUND_MODIFIER, "<around modifier>");
    result_ = consumeToken(builder_, RESERVED_AROUND_FP);
    result_ = result_ && sub_names_token(builder_, level_ + 1);
    result_ = result_ && around_signature(builder_, level_ + 1);
    result_ = result_ && func_or_method_body(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // <<parse_signature_content parse_around_signature_content>>
  static boolean around_signature(PsiBuilder builder_, int level_) {
    return parse_signature_content(builder_, level_ + 1, PerlParserGenerated::parse_around_signature_content);
  }

  /* ********************************************************** */
  // <<scalarDeclarationWrapper>> ',' <<scalarDeclarationWrapper>>  ':'
  public static boolean around_signature_invocants(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "around_signature_invocants")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, AROUND_SIGNATURE_INVOCANTS, "<around signature invocants>");
    result_ = scalarDeclarationWrapper(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COMMA);
    result_ = result_ && scalarDeclarationWrapper(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '$@' array_cast_target
  public static boolean array_cast_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_cast_expr")) return false;
    if (!nextTokenIs(builder_, "<array dereference>", SIGIL_ARRAY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARRAY_CAST_EXPR, "<array dereference>");
    result_ = consumeToken(builder_, SIGIL_ARRAY);
    result_ = result_ && array_cast_target(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // block_array | scalar_primitive
  static boolean array_cast_target(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_cast_target")) return false;
    boolean result_;
    result_ = block_array(builder_, level_ + 1);
    if (!result_) result_ = scalar_primitive(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // array_index
  public static boolean array_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_element")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACKET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, ARRAY_ELEMENT, null);
    result_ = array_index(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '[' array_index_content ']'
  public static boolean array_index(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_index")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACKET)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARRAY_INDEX, null);
    result_ = consumeToken(builder_, LEFT_BRACKET);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, array_index_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_BRACKET) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // expr
  static boolean array_index_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_index_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, false, PerlParserGenerated::recover_bracketed_expression);
    return result_;
  }

  /* ********************************************************** */
  // array_primitive [array_slice | hash_slice]
  static boolean array_or_slice(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_or_slice")) return false;
    if (!nextTokenIs(builder_, "", RESERVED_QW, SIGIL_ARRAY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = array_primitive(builder_, level_ + 1);
    result_ = result_ && array_or_slice_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [array_slice | hash_slice]
  private static boolean array_or_slice_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_or_slice_1")) return false;
    array_or_slice_1_0(builder_, level_ + 1);
    return true;
  }

  // array_slice | hash_slice
  private static boolean array_or_slice_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_or_slice_1_0")) return false;
    boolean result_;
    result_ = array_slice(builder_, level_ + 1);
    if (!result_) result_ = hash_slice(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // array_variable | array_cast_expr | string_list
  static boolean array_primitive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_primitive")) return false;
    if (!nextTokenIs(builder_, "", RESERVED_QW, SIGIL_ARRAY)) return false;
    boolean result_;
    result_ = array_variable(builder_, level_ + 1);
    if (!result_) result_ = array_cast_expr(builder_, level_ + 1);
    if (!result_) result_ = string_list(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // array_index
  public static boolean array_slice(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_slice")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACKET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, ARRAY_SLICE, null);
    result_ = array_index(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '$@' {ARRAY_NAME | '@{' ARRAY_NAME '@}'}
  public static boolean array_variable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_variable")) return false;
    if (!nextTokenIs(builder_, "<array>", SIGIL_ARRAY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARRAY_VARIABLE, "<array>");
    result_ = consumeToken(builder_, SIGIL_ARRAY);
    result_ = result_ && array_variable_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ARRAY_NAME | '@{' ARRAY_NAME '@}'
  private static boolean array_variable_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_variable_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, ARRAY_NAME);
    if (!result_) result_ = parseTokens(builder_, 0, LEFT_BRACE_ARRAY, ARRAY_NAME, RIGHT_BRACE_ARRAY);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // 'ArrayRef' '[' annotation_type_param ']'
  public static boolean arrayref_type(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "arrayref_type")) return false;
    if (!nextTokenIs(builder_, TYPE_ARRAYREF)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARRAYREF_TYPE, null);
    result_ = consumeTokens(builder_, 1, TYPE_ARRAYREF, LEFT_BRACKET);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, annotation_type_param(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_BRACKET) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // ATTRIBUTE_IDENTIFIER [quoted_sq_string]
  public static boolean attribute(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute")) return false;
    if (!nextTokenIs(builder_, ATTRIBUTE_IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, ATTRIBUTE_IDENTIFIER);
    result_ = result_ && attribute_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, ATTRIBUTE, result_);
    return result_;
  }

  // [quoted_sq_string]
  private static boolean attribute_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "attribute_1")) return false;
    quoted_sq_string(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // <<x1>>
  public static boolean attributes(PsiBuilder builder_, int level_, Parser x1) {
    if (!recursion_guard_(builder_, level_, "attributes")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = x1.parse(builder_, level_);
    exit_section_(builder_, marker_, ATTRIBUTES, result_);
    return result_;
  }

  /* ********************************************************** */
  // 'fp_augment' fp_modifier_named_body
  public static boolean augment_modifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "augment_modifier")) return false;
    if (!nextTokenIs(builder_, "<augment modifier>", RESERVED_AUGMENT_FP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, AUGMENT_MODIFIER, "<augment modifier>");
    result_ = consumeToken(builder_, RESERVED_AUGMENT_FP);
    result_ = result_ && fp_modifier_named_body(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // 'fp_before' fp_modifier_named_body
  public static boolean before_modifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "before_modifier")) return false;
    if (!nextTokenIs(builder_, "<before modifier>", RESERVED_BEFORE_FP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BEFORE_MODIFIER, "<before modifier>");
    result_ = consumeToken(builder_, RESERVED_BEFORE_FP);
    result_ = result_ && fp_modifier_named_body(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '{' block_content '}'
  public static boolean block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLOCK, null);
    result_ = consumeToken(builder_, LEFT_BRACE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, block_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_BRACE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '@{' block_content '@}'
  public static boolean block_array(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_array")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE_ARRAY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLOCK_ARRAY, null);
    result_ = consumeToken(builder_, LEFT_BRACE_ARRAY);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, block_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_BRACE_ARRAY) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // file_items
  public static boolean block_braceless(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_braceless")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, BLOCK_BRACELESS, "<block braceless>");
    result_ = file_items(builder_, level_ + 1);
    register_hook_(builder_, RIGHT_BINDER, GREEDY_RIGHT_BINDER);
    register_hook_(builder_, LEFT_BINDER, GREEDY_LEFT_BINDER);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '&{' block_content '&}'
  public static boolean block_code(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_code")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE_CODE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLOCK_CODE, null);
    result_ = consumeToken(builder_, LEFT_BRACE_CODE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, block_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_BRACE_CODE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // parse_block_compound
  public static boolean block_compound(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_compound")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLOCK_COMPOUND, "<block compound>");
    result_ = parse_block_compound(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // file_item *
  static boolean block_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_content")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    while (true) {
      int pos_ = current_position_(builder_);
      if (!file_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "block_content", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, PerlParserGenerated::recover_statement);
    return true;
  }

  /* ********************************************************** */
  // '*{' block_content '*}'
  public static boolean block_glob(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_glob")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE_GLOB)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLOCK_GLOB, null);
    result_ = consumeToken(builder_, LEFT_BRACE_GLOB);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, block_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_BRACE_GLOB) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '%{' block_content '%}'
  public static boolean block_hash(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_hash")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE_HASH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLOCK_HASH, null);
    result_ = consumeToken(builder_, LEFT_BRACE_HASH);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, block_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_BRACE_HASH) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '${' block_content '$}'
  public static boolean block_scalar(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_scalar")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE_SCALAR)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLOCK_SCALAR, null);
    result_ = consumeToken(builder_, LEFT_BRACE_SCALAR);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, block_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_BRACE_SCALAR) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // parse_call_arguments
  public static boolean call_arguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_arguments")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CALL_ARGUMENTS, "<call arguments>");
    result_ = parse_call_arguments(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // 'case' case_condition block
  public static boolean case_compound(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "case_compound")) return false;
    if (!nextTokenIs(builder_, RESERVED_CASE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CASE_COMPOUND, null);
    result_ = consumeToken(builder_, RESERVED_CASE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, case_condition(builder_, level_ + 1));
    result_ = pinned_ && block(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '(' parse_scalar_expr ')' | block | string | number_constant | anon_array | match_regex | compile_regex
  public static boolean case_condition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "case_condition")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CASE_CONDITION, "<case condition>");
    result_ = case_condition_0(builder_, level_ + 1);
    if (!result_) result_ = block(builder_, level_ + 1);
    if (!result_) result_ = expr(builder_, level_ + 1, 21);
    if (!result_) result_ = number_constant(builder_, level_ + 1);
    if (!result_) result_ = anon_array(builder_, level_ + 1);
    if (!result_) result_ = match_regex(builder_, level_ + 1);
    if (!result_) result_ = compile_regex(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // '(' parse_scalar_expr ')'
  private static boolean case_condition_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "case_condition_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    result_ = result_ && parse_scalar_expr(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_PAREN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // if_compound_else
  public static boolean case_default(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "case_default")) return false;
    if (!nextTokenIs(builder_, "<case default>", POD, RESERVED_ELSE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CASE_DEFAULT, "<case default>");
    result_ = if_compound_else(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // case_compound + [case_default]
  static boolean cases_sequence(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "cases_sequence")) return false;
    if (!nextTokenIs(builder_, RESERVED_CASE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = cases_sequence_0(builder_, level_ + 1);
    result_ = result_ && cases_sequence_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // case_compound +
  private static boolean cases_sequence_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "cases_sequence_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = case_compound(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!case_compound(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "cases_sequence_0", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [case_default]
  private static boolean cases_sequence_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "cases_sequence_1")) return false;
    case_default(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // catch_condition_parenthesised | catch_condition_with
  public static boolean catch_condition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_condition")) return false;
    if (!nextTokenIs(builder_, "<catch condition>", LEFT_PAREN, PACKAGE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CATCH_CONDITION, "<catch condition>");
    result_ = catch_condition_parenthesised(builder_, level_ + 1);
    if (!result_) result_ = catch_condition_with(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // [catch_condition_type] variable_declaration_element [where_clause]
  static boolean catch_condition_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_condition_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = catch_condition_content_0(builder_, level_ + 1);
    result_ = result_ && variable_declaration_element(builder_, level_ + 1);
    result_ = result_ && catch_condition_content_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [catch_condition_type]
  private static boolean catch_condition_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_condition_content_0")) return false;
    catch_condition_type(builder_, level_ + 1);
    return true;
  }

  // [where_clause]
  private static boolean catch_condition_content_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_condition_content_2")) return false;
    where_clause(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // '(' catch_condition_content ')'
  static boolean catch_condition_parenthesised(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_condition_parenthesised")) return false;
    if (!nextTokenIs(builder_, LEFT_PAREN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, catch_condition_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_PAREN) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // [type_constraints]
  static boolean catch_condition_type(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_condition_type")) return false;
    type_constraints(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // 'package::name' 'catch_with'
  static boolean catch_condition_with(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_condition_with")) return false;
    if (!nextTokenIs(builder_, PACKAGE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 2, PACKAGE, RESERVED_CATCH_WITH);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // 'catch' [catch_condition] <<x1>>
  public static boolean catch_expr(PsiBuilder builder_, int level_, Parser x1) {
    if (!recursion_guard_(builder_, level_, "catch_expr")) return false;
    if (!nextTokenIs(builder_, "", RESERVED_CATCH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CATCH_EXPR, null);
    result_ = consumeToken(builder_, RESERVED_CATCH);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, catch_expr_1(builder_, level_ + 1));
    result_ = pinned_ && x1.parse(builder_, level_) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [catch_condition]
  private static boolean catch_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_expr_1")) return false;
    catch_condition(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // NUMBER_HEX
  static boolean char_code_hex(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "char_code_hex")) return false;
    if (!nextTokenIs(builder_, "<hex character code>", NUMBER_HEX)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<hex character code>");
    result_ = consumeToken(builder_, NUMBER_HEX);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // NUMBER_OCT
  static boolean char_code_oct(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "char_code_oct")) return false;
    if (!nextTokenIs(builder_, "<octal character code>", NUMBER_OCT)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<octal character code>");
    result_ = consumeToken(builder_, NUMBER_OCT);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // 'class' any_package [perl_version] [sub_attributes]
  static boolean class_definition_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_definition_name")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<class definition>");
    result_ = consumeToken(builder_, RESERVED_CLASS);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, any_package(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, class_definition_name_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && class_definition_name_3(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, PerlParserGenerated::recover_statement);
    return result_ || pinned_;
  }

  // [perl_version]
  private static boolean class_definition_name_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_definition_name_2")) return false;
    perl_version(builder_, level_ + 1);
    return true;
  }

  // [sub_attributes]
  private static boolean class_definition_name_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "class_definition_name_3")) return false;
    sub_attributes(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // '$&' code_cast_target
  public static boolean code_cast_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "code_cast_expr")) return false;
    if (!nextTokenIs(builder_, "<expression>", SIGIL_CODE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CODE_CAST_EXPR, "<expression>");
    result_ = consumeToken(builder_, SIGIL_CODE);
    result_ = result_ && code_cast_target(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // block_code | scalar_primitive
  static boolean code_cast_target(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "code_cast_target")) return false;
    boolean result_;
    result_ = block_code(builder_, level_ + 1);
    if (!result_) result_ = scalar_primitive(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // code_primitive_variation [primitive_call]
  static boolean code_primitive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "code_primitive")) return false;
    if (!nextTokenIs(builder_, SIGIL_CODE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = code_primitive_variation(builder_, level_ + 1);
    result_ = result_ && code_primitive_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [primitive_call]
  private static boolean code_primitive_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "code_primitive_1")) return false;
    primitive_call(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // code_variable | code_cast_expr
  static boolean code_primitive_variation(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "code_primitive_variation")) return false;
    if (!nextTokenIs(builder_, SIGIL_CODE)) return false;
    boolean result_;
    result_ = code_variable(builder_, level_ + 1);
    if (!result_) result_ = code_cast_expr(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '$&' {method | '&{' method '&}'}
  public static boolean code_variable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "code_variable")) return false;
    if (!nextTokenIs(builder_, "<code>", SIGIL_CODE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CODE_VARIABLE, "<code>");
    result_ = consumeToken(builder_, SIGIL_CODE);
    result_ = result_ && code_variable_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // method | '&{' method '&}'
  private static boolean code_variable_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "code_variable_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = method(builder_, level_ + 1);
    if (!result_) result_ = code_variable_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // '&{' method '&}'
  private static boolean code_variable_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "code_variable_1_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_BRACE_CODE);
    result_ = result_ && method(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_BRACE_CODE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // ',' | '=>'
  static boolean comma(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "comma")) return false;
    if (!nextTokenIs(builder_, "<comma>", COMMA, FAT_COMMA)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<comma>");
    result_ = consumeToken(builder_, COMMA);
    if (!result_) result_ = consumeToken(builder_, FAT_COMMA);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // annotation
  public static boolean comment_annotation(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "comment_annotation")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, COMMENT_ANNOTATION, "<comment annotation>");
    result_ = annotation(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // if_compound
  //     | unless_compound
  //     | given_compound
  //     | while_compound
  //     | until_compound
  //     | for_or_foreach
  //     | when_compound
  //     | default_compound
  //     | trycatch_compound
  //     | switch_compound
  //     | cases_sequence
  static boolean compound_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "compound_statement")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<compound statement>");
    result_ = if_compound(builder_, level_ + 1);
    if (!result_) result_ = unless_compound(builder_, level_ + 1);
    if (!result_) result_ = given_compound(builder_, level_ + 1);
    if (!result_) result_ = while_compound(builder_, level_ + 1);
    if (!result_) result_ = until_compound(builder_, level_ + 1);
    if (!result_) result_ = for_or_foreach(builder_, level_ + 1);
    if (!result_) result_ = when_compound(builder_, level_ + 1);
    if (!result_) result_ = default_compound(builder_, level_ + 1);
    if (!result_) result_ = trycatch_compound(builder_, level_ + 1);
    if (!result_) result_ = switch_compound(builder_, level_ + 1);
    if (!result_) result_ = cases_sequence(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // parse_parenthesized_expression
  public static boolean condition_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "condition_expr")) return false;
    if (!nextTokenIs(builder_, "<expression>", LEFT_PAREN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONDITION_EXPR, "<expression>");
    result_ = parse_parenthesized_expression(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // parse_conditional_block
  public static boolean conditional_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "conditional_block")) return false;
    if (!nextTokenIs(builder_, LEFT_PAREN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_conditional_block(builder_, level_ + 1);
    exit_section_(builder_, marker_, CONDITIONAL_BLOCK, result_);
    return result_;
  }

  /* ********************************************************** */
  // 'continuation' sub_expr_simple_ensured
  public static boolean continuation_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "continuation_expr")) return false;
    if (!nextTokenIs(builder_, "<expression>", RESERVED_CONTINUATION)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONTINUATION_EXPR, "<expression>");
    result_ = consumeToken(builder_, RESERVED_CONTINUATION);
    pinned_ = result_; // pin = 1
    result_ = result_ && sub_expr_simple_ensured(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // continue_block_opener block
  public static boolean continue_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "continue_block")) return false;
    if (!nextTokenIs(builder_, RESERVED_CONTINUE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONTINUE_BLOCK, null);
    result_ = continue_block_opener(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'continue' &'{'
  static boolean continue_block_opener(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "continue_block_opener")) return false;
    if (!nextTokenIs(builder_, RESERVED_CONTINUE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, RESERVED_CONTINUE);
    result_ = result_ && continue_block_opener_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // &'{'
  private static boolean continue_block_opener_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "continue_block_opener_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = consumeToken(builder_, LEFT_BRACE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '(' <<x1>> ')' !'[' | <<x1>>
  static boolean custom_expr_arguments(PsiBuilder builder_, int level_, Parser x1) {
    if (!recursion_guard_(builder_, level_, "custom_expr_arguments")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = custom_expr_arguments_0(builder_, level_ + 1, x1);
    if (!result_) result_ = x1.parse(builder_, level_);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // '(' <<x1>> ')' !'['
  private static boolean custom_expr_arguments_0(PsiBuilder builder_, int level_, Parser x1) {
    if (!recursion_guard_(builder_, level_, "custom_expr_arguments_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    result_ = result_ && x1.parse(builder_, level_);
    result_ = result_ && consumeToken(builder_, RIGHT_PAREN);
    result_ = result_ && custom_expr_arguments_0_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !'['
  private static boolean custom_expr_arguments_0_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "custom_expr_arguments_0_3")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, LEFT_BRACKET);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // <<custom_expr_arguments single_argument_expr>>
  static boolean custom_single_expr_argument(PsiBuilder builder_, int level_) {
    return custom_expr_arguments(builder_, level_ + 1, PerlParserGenerated::single_argument_expr);
  }

  /* ********************************************************** */
  // 'default' block
  public static boolean default_compound(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "default_compound")) return false;
    if (!nextTokenIs(builder_, RESERVED_DEFAULT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DEFAULT_COMPOUND, null);
    result_ = consumeToken(builder_, RESERVED_DEFAULT);
    pinned_ = result_; // pin = 1
    result_ = result_ && block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '__DATA__' | '__END__'
  static boolean end_or_data(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "end_or_data")) return false;
    if (!nextTokenIs(builder_, "<__END__ or __DATA__>", TAG_DATA, TAG_END)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<__END__ or __DATA__>");
    result_ = consumeToken(builder_, TAG_DATA);
    if (!result_) result_ = consumeToken(builder_, TAG_END);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '\c*'
  public static boolean esc_char(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "esc_char")) return false;
    if (!nextTokenIs(builder_, STRING_SPECIAL_SUBST)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, STRING_SPECIAL_SUBST);
    exit_section_(builder_, marker_, ESC_CHAR, result_);
    return result_;
  }

  /* ********************************************************** */
  // parenthesised_expr | block | expr
  static boolean eval_argument(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "eval_argument")) return false;
    boolean result_;
    result_ = parenthesised_expr(builder_, level_ + 1);
    if (!result_) result_ = block(builder_, level_ + 1);
    if (!result_) result_ = expr(builder_, level_ + 1, -1);
    return result_;
  }

  /* ********************************************************** */
  // 'except' sub_expr_simple_ensured
  public static boolean except_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "except_expr")) return false;
    if (!nextTokenIs(builder_, "<expression>", RESERVED_EXCEPT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXCEPT_EXPR, "<expression>");
    result_ = consumeToken(builder_, RESERVED_EXCEPT);
    pinned_ = result_; // pin = 1
    result_ = result_ && sub_expr_simple_ensured(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // {'field' !'('} variable_declaration_element [var_attributes]
  static boolean field_lexical_declaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "field_lexical_declaration")) return false;
    if (!nextTokenIs(builder_, "<field declaration>", RESERVED_FIELD)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<field declaration>");
    result_ = field_lexical_declaration_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, variable_declaration_element(builder_, level_ + 1));
    result_ = pinned_ && field_lexical_declaration_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'field' !'('
  private static boolean field_lexical_declaration_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "field_lexical_declaration_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, RESERVED_FIELD);
    result_ = result_ && field_lexical_declaration_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !'('
  private static boolean field_lexical_declaration_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "field_lexical_declaration_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, LEFT_PAREN);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [var_attributes]
  private static boolean field_lexical_declaration_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "field_lexical_declaration_2")) return false;
    var_attributes(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // !<<eof>>
  // {
  // 	namespace_definition |
  // 	label_declaration [statement_item]
  //     | statement_item
  // }
  static boolean file_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "file_item")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = file_item_0(builder_, level_ + 1);
    result_ = result_ && file_item_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !<<eof>>
  private static boolean file_item_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "file_item_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !eof(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // namespace_definition |
  // 	label_declaration [statement_item]
  //     | statement_item
  private static boolean file_item_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "file_item_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = namespace_definition(builder_, level_ + 1);
    if (!result_) result_ = file_item_1_1(builder_, level_ + 1);
    if (!result_) result_ = statement_item(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // label_declaration [statement_item]
  private static boolean file_item_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "file_item_1_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = label_declaration(builder_, level_ + 1);
    result_ = result_ && file_item_1_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [statement_item]
  private static boolean file_item_1_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "file_item_1_1_1")) return false;
    statement_item(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // file_item*
  static boolean file_items(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "file_items")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!file_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "file_items", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // 'finally' sub_expr_simple_ensured
  public static boolean finally_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "finally_expr")) return false;
    if (!nextTokenIs(builder_, "<expression>", RESERVED_FINALLY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FINALLY_EXPR, "<expression>");
    result_ = consumeToken(builder_, RESERVED_FINALLY);
    pinned_ = result_; // pin = 1
    result_ = result_ && sub_expr_simple_ensured(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // {'for'|'foreach'} for_iterator block
  public static boolean for_compound(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_compound")) return false;
    if (!nextTokenIs(builder_, "<for compound>", RESERVED_FOR, RESERVED_FOREACH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FOR_COMPOUND, "<for compound>");
    result_ = for_compound_0(builder_, level_ + 1);
    result_ = result_ && for_iterator(builder_, level_ + 1);
    pinned_ = result_; // pin = 2
    result_ = result_ && block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'for'|'foreach'
  private static boolean for_compound_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_compound_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, RESERVED_FOR);
    if (!result_) result_ = consumeToken(builder_, RESERVED_FOREACH);
    return result_;
  }

  /* ********************************************************** */
  // expr
  public static boolean for_condition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_condition")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FOR_CONDITION, "<for condition>");
    result_ = expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, false, PerlParserGenerated::recover_parenthesised);
    return result_;
  }

  /* ********************************************************** */
  // expr
  public static boolean for_init(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_init")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FOR_INIT, "<for init>");
    result_ = expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, false, PerlParserGenerated::recover_parenthesised);
    return result_;
  }

  /* ********************************************************** */
  // '(' [for_init]  ';' [for_condition] ';' [for_mutator] ')'
  static boolean for_iterator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_iterator")) return false;
    if (!nextTokenIs(builder_, LEFT_PAREN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    result_ = result_ && for_iterator_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    pinned_ = result_; // pin = 3
    result_ = result_ && report_error_(builder_, for_iterator_3(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, SEMICOLON)) && result_;
    result_ = pinned_ && report_error_(builder_, for_iterator_5(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, RIGHT_PAREN) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [for_init]
  private static boolean for_iterator_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_iterator_1")) return false;
    for_init(builder_, level_ + 1);
    return true;
  }

  // [for_condition]
  private static boolean for_iterator_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_iterator_3")) return false;
    for_condition(builder_, level_ + 1);
    return true;
  }

  // [for_mutator]
  private static boolean for_iterator_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_iterator_5")) return false;
    for_mutator(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // expr
  public static boolean for_mutator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_mutator")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FOR_MUTATOR, "<for mutator>");
    result_ = expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, false, PerlParserGenerated::recover_parenthesised);
    return result_;
  }

  /* ********************************************************** */
  // for_compound|foreach_compound
  static boolean for_or_foreach(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_or_foreach")) return false;
    if (!nextTokenIs(builder_, "", RESERVED_FOR, RESERVED_FOREACH)) return false;
    boolean result_;
    result_ = for_compound(builder_, level_ + 1);
    if (!result_) result_ = foreach_compound(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // {{'for'|'foreach'} !(for_iterator)} expr
  public static boolean for_statement_modifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_statement_modifier")) return false;
    if (!nextTokenIs(builder_, "<Postfix for>", RESERVED_FOR, RESERVED_FOREACH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FOR_STATEMENT_MODIFIER, "<Postfix for>");
    result_ = for_statement_modifier_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // {'for'|'foreach'} !(for_iterator)
  private static boolean for_statement_modifier_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_statement_modifier_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = for_statement_modifier_0_0(builder_, level_ + 1);
    result_ = result_ && for_statement_modifier_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // 'for'|'foreach'
  private static boolean for_statement_modifier_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_statement_modifier_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, RESERVED_FOR);
    if (!result_) result_ = consumeToken(builder_, RESERVED_FOREACH);
    return result_;
  }

  // !(for_iterator)
  private static boolean for_statement_modifier_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_statement_modifier_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !for_statement_modifier_0_1_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (for_iterator)
  private static boolean for_statement_modifier_0_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_statement_modifier_0_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = for_iterator(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // {'for'|'foreach'} [ foreach_iterator ] condition_expr parse_block_compound
  public static boolean foreach_compound(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_compound")) return false;
    if (!nextTokenIs(builder_, "<foreach compound>", RESERVED_FOR, RESERVED_FOREACH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FOREACH_COMPOUND, "<foreach compound>");
    result_ = foreach_compound_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, foreach_compound_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, condition_expr(builder_, level_ + 1)) && result_;
    result_ = pinned_ && parse_block_compound(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'for'|'foreach'
  private static boolean foreach_compound_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_compound_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, RESERVED_FOR);
    if (!result_) result_ = consumeToken(builder_, RESERVED_FOREACH);
    return result_;
  }

  // [ foreach_iterator ]
  private static boolean foreach_compound_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_compound_1")) return false;
    foreach_iterator(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // variable_declaration | variable
  public static boolean foreach_iterator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_iterator")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FOREACH_ITERATOR, "<foreach iterator>");
    result_ = variable_declaration(builder_, level_ + 1);
    if (!result_) result_ = variable(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // 'format' ['subname'] '=' [FORMAT] FORMAT_TERMINATOR
  public static boolean format_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "format_definition")) return false;
    if (!nextTokenIs(builder_, "<format definition>", RESERVED_FORMAT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FORMAT_DEFINITION, "<format definition>");
    result_ = consumeToken(builder_, RESERVED_FORMAT);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, format_definition_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, OPERATOR_ASSIGN)) && result_;
    result_ = pinned_ && report_error_(builder_, format_definition_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, FORMAT_TERMINATOR) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // ['subname']
  private static boolean format_definition_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "format_definition_1")) return false;
    consumeToken(builder_, SUB_NAME);
    return true;
  }

  // [FORMAT]
  private static boolean format_definition_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "format_definition_3")) return false;
    consumeToken(builder_, FORMAT);
    return true;
  }

  /* ********************************************************** */
  // sub_names_token method_signature func_or_method_body
  static boolean fp_modifier_named_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fp_modifier_named_body")) return false;
    if (!nextTokenIs(builder_, "", QUALIFYING_PACKAGE, SUB_NAME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = sub_names_token(builder_, level_ + 1);
    result_ = result_ && method_signature(builder_, level_ + 1);
    result_ = result_ && func_or_method_body(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // [func_signature] func_or_method_body
  static boolean func_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "func_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = func_body_0(builder_, level_ + 1);
    result_ = result_ && func_or_method_body(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [func_signature]
  private static boolean func_body_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "func_body_0")) return false;
    func_signature(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // {'func'|'fun'} sub_names_token func_body
  public static boolean func_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "func_definition")) return false;
    if (!nextTokenIs(builder_, "<func definition>", RESERVED_FUN, RESERVED_FUNC)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUNC_DEFINITION, "<func definition>");
    result_ = func_definition_0(builder_, level_ + 1);
    result_ = result_ && sub_names_token(builder_, level_ + 1);
    result_ = result_ && func_body(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // 'func'|'fun'
  private static boolean func_definition_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "func_definition_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, RESERVED_FUNC);
    if (!result_) result_ = consumeToken(builder_, RESERVED_FUN);
    return result_;
  }

  /* ********************************************************** */
  // [sub_attributes] block
  static boolean func_or_method_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "func_or_method_body")) return false;
    if (!nextTokenIs(builder_, "", COLON, LEFT_BRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = func_or_method_body_0(builder_, level_ + 1);
    result_ = result_ && block(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [sub_attributes]
  private static boolean func_or_method_body_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "func_or_method_body_0")) return false;
    sub_attributes(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // <<parse_signature_content parse_func_signature_content>>
  static boolean func_signature(PsiBuilder builder_, int level_) {
    return parse_signature_content(builder_, level_ + 1, PerlParserGenerated::parse_func_signature_content);
  }

  /* ********************************************************** */
  // <<signature_element parse_func_signature_element>>
  static boolean func_signature_element(PsiBuilder builder_, int level_) {
    return signature_element(builder_, level_ + 1, PerlParserGenerated::parse_func_signature_element);
  }

  /* ********************************************************** */
  // func_signature_element (comma + func_signature_element ) * comma*
  static boolean func_signature_elements(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "func_signature_elements")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = func_signature_element(builder_, level_ + 1);
    result_ = result_ && func_signature_elements_1(builder_, level_ + 1);
    result_ = result_ && func_signature_elements_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (comma + func_signature_element ) *
  private static boolean func_signature_elements_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "func_signature_elements_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!func_signature_elements_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "func_signature_elements_1", pos_)) break;
    }
    return true;
  }

  // comma + func_signature_element
  private static boolean func_signature_elements_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "func_signature_elements_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = func_signature_elements_1_0_0(builder_, level_ + 1);
    result_ = result_ && func_signature_element(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // comma +
  private static boolean func_signature_elements_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "func_signature_elements_1_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = comma(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!comma(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "func_signature_elements_1_0_0", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // comma*
  private static boolean func_signature_elements_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "func_signature_elements_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!comma(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "func_signature_elements_2", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // 'given' parse_conditional_block
  public static boolean given_compound(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "given_compound")) return false;
    if (!nextTokenIs(builder_, RESERVED_GIVEN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, GIVEN_COMPOUND, null);
    result_ = consumeToken(builder_, RESERVED_GIVEN);
    pinned_ = result_; // pin = 1
    result_ = result_ && parse_conditional_block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '$*' glob_cast_target
  public static boolean glob_cast_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "glob_cast_expr")) return false;
    if (!nextTokenIs(builder_, "<typeglob dereference>", SIGIL_GLOB)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, GLOB_CAST_EXPR, "<typeglob dereference>");
    result_ = consumeToken(builder_, SIGIL_GLOB);
    result_ = result_ && glob_cast_target(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // block_glob | scalar_primitive
  static boolean glob_cast_target(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "glob_cast_target")) return false;
    boolean result_;
    result_ = block_glob(builder_, level_ + 1);
    if (!result_) result_ = scalar_primitive(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // glob_primitive [glob_slot]
  static boolean glob_or_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "glob_or_element")) return false;
    if (!nextTokenIs(builder_, SIGIL_GLOB)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = glob_primitive(builder_, level_ + 1);
    result_ = result_ && glob_or_element_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [glob_slot]
  private static boolean glob_or_element_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "glob_or_element_1")) return false;
    glob_slot(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // glob_variable | glob_cast_expr
  static boolean glob_primitive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "glob_primitive")) return false;
    if (!nextTokenIs(builder_, SIGIL_GLOB)) return false;
    boolean result_;
    result_ = glob_variable(builder_, level_ + 1);
    if (!result_) result_ = glob_cast_expr(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // hash_index
  public static boolean glob_slot(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "glob_slot")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, GLOB_SLOT, null);
    result_ = hash_index(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '$*' {GLOB_NAME | '*{' GLOB_NAME '*}'}
  public static boolean glob_variable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "glob_variable")) return false;
    if (!nextTokenIs(builder_, "<typeglob>", SIGIL_GLOB)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, GLOB_VARIABLE, "<typeglob>");
    result_ = consumeToken(builder_, SIGIL_GLOB);
    result_ = result_ && glob_variable_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // GLOB_NAME | '*{' GLOB_NAME '*}'
  private static boolean glob_variable_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "glob_variable_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, GLOB_NAME);
    if (!result_) result_ = parseTokens(builder_, 0, LEFT_BRACE_GLOB, GLOB_NAME, RIGHT_BRACE_GLOB);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // label_expr | code_primitive  !'(' | expr
  static boolean goto_param(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "goto_param")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = label_expr(builder_, level_ + 1);
    if (!result_) result_ = goto_param_1(builder_, level_ + 1);
    if (!result_) result_ = expr(builder_, level_ + 1, -1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // code_primitive  !'('
  private static boolean goto_param_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "goto_param_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = code_primitive(builder_, level_ + 1);
    result_ = result_ && goto_param_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !'('
  private static boolean goto_param_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "goto_param_1_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, LEFT_PAREN);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // grep_map_sort_with_block |
  //     parse_scalar_expr comma grep_map_sort_tail |
  //     expr
  static boolean grep_map_arguments_variants(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "grep_map_arguments_variants")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = grep_map_sort_with_block(builder_, level_ + 1);
    if (!result_) result_ = grep_map_arguments_variants_1(builder_, level_ + 1);
    if (!result_) result_ = expr(builder_, level_ + 1, -1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // parse_scalar_expr comma grep_map_sort_tail
  private static boolean grep_map_arguments_variants_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "grep_map_arguments_variants_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_scalar_expr(builder_, level_ + 1);
    result_ = result_ && comma(builder_, level_ + 1);
    result_ = result_ && grep_map_sort_tail(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // parse_list_expr
  static boolean grep_map_sort_tail(PsiBuilder builder_, int level_) {
    return parse_list_expr(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // block [comma] grep_map_sort_tail
  static boolean grep_map_sort_with_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "grep_map_sort_with_block")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = block(builder_, level_ + 1);
    result_ = result_ && grep_map_sort_with_block_1(builder_, level_ + 1);
    result_ = result_ && grep_map_sort_tail(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [comma]
  private static boolean grep_map_sort_with_block_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "grep_map_sort_with_block_1")) return false;
    comma(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // array_index
  public static boolean hash_array_slice(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_array_slice")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACKET)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, HASH_ARRAY_SLICE, null);
    result_ = array_index(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '$%' hash_cast_target
  public static boolean hash_cast_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_cast_expr")) return false;
    if (!nextTokenIs(builder_, "<hash dereference>", SIGIL_HASH)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HASH_CAST_EXPR, "<hash dereference>");
    result_ = consumeToken(builder_, SIGIL_HASH);
    result_ = result_ && hash_cast_target(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // block_hash  | scalar_primitive
  static boolean hash_cast_target(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_cast_target")) return false;
    boolean result_;
    result_ = block_hash(builder_, level_ + 1);
    if (!result_) result_ = scalar_primitive(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // hash_index
  public static boolean hash_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_element")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, HASH_ELEMENT, null);
    result_ = hash_index(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // hash_index
  public static boolean hash_hash_slice(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_hash_slice")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, HASH_HASH_SLICE, null);
    result_ = hash_index(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '{' hash_index_content '}'
  public static boolean hash_index(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_index")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HASH_INDEX, null);
    result_ = consumeToken(builder_, LEFT_BRACE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, hash_index_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_BRACE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // expr
  static boolean hash_index_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_index_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, false, PerlParserGenerated::recover_braced_expression);
    return result_;
  }

  /* ********************************************************** */
  // hash_primitive [hash_array_slice| hash_hash_slice]
  static boolean hash_or_slice(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_or_slice")) return false;
    if (!nextTokenIs(builder_, SIGIL_HASH)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = hash_primitive(builder_, level_ + 1);
    result_ = result_ && hash_or_slice_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [hash_array_slice| hash_hash_slice]
  private static boolean hash_or_slice_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_or_slice_1")) return false;
    hash_or_slice_1_0(builder_, level_ + 1);
    return true;
  }

  // hash_array_slice| hash_hash_slice
  private static boolean hash_or_slice_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_or_slice_1_0")) return false;
    boolean result_;
    result_ = hash_array_slice(builder_, level_ + 1);
    if (!result_) result_ = hash_hash_slice(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // hash_variable | hash_cast_expr
  static boolean hash_primitive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_primitive")) return false;
    if (!nextTokenIs(builder_, SIGIL_HASH)) return false;
    boolean result_;
    result_ = hash_variable(builder_, level_ + 1);
    if (!result_) result_ = hash_cast_expr(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // hash_index
  public static boolean hash_slice(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_slice")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, HASH_SLICE, null);
    result_ = hash_index(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '$%' {HASH_NAME | '%{' HASH_NAME '%}'}
  public static boolean hash_variable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_variable")) return false;
    if (!nextTokenIs(builder_, "<hash>", SIGIL_HASH)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HASH_VARIABLE, "<hash>");
    result_ = consumeToken(builder_, SIGIL_HASH);
    result_ = result_ && hash_variable_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // HASH_NAME | '%{' HASH_NAME '%}'
  private static boolean hash_variable_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_variable_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, HASH_NAME);
    if (!result_) result_ = parseTokens(builder_, 0, LEFT_BRACE_HASH, HASH_NAME, RIGHT_BRACE_HASH);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // 'HashRef' '[' annotation_type_param ']'
  public static boolean hashref_type(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hashref_type")) return false;
    if (!nextTokenIs(builder_, TYPE_HASHREF)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HASHREF_TYPE, null);
    result_ = consumeTokens(builder_, 1, TYPE_HASHREF, LEFT_BRACKET);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, annotation_type_param(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_BRACKET) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // sq_string_content
  public static boolean heredoc(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "heredoc")) return false;
    if (!nextTokenIs(builder_, "<heredoc>", STRING_CONTENT, STRING_SPECIAL_ESCAPE_CHAR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HEREDOC, "<heredoc>");
    result_ = sq_string_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // qq_string_content
  public static boolean heredoc_qq(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "heredoc_qq")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HEREDOC_QQ, "<heredoc qq>");
    result_ = qq_string_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // qx_string_content
  public static boolean heredoc_qx(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "heredoc_qx")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HEREDOC_QX, "<heredoc qx>");
    result_ = qx_string_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '\x' hex_char_body
  public static boolean hex_char(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hex_char")) return false;
    if (!nextTokenIs(builder_, STRING_SPECIAL_HEX)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HEX_CHAR, null);
    result_ = consumeToken(builder_, STRING_SPECIAL_HEX);
    pinned_ = result_; // pin = 1
    result_ = result_ && hex_char_body(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // hex_char_body_braced | [char_code_hex]
  static boolean hex_char_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hex_char_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = hex_char_body_braced(builder_, level_ + 1);
    if (!result_) result_ = hex_char_body_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [char_code_hex]
  private static boolean hex_char_body_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hex_char_body_1")) return false;
    char_code_hex(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // '"{' [hex_char_body_content_in_brace] '}"'
  static boolean hex_char_body_braced(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hex_char_body_braced")) return false;
    if (!nextTokenIs(builder_, "<braced character code>", STRING_SPECIAL_LEFT_BRACE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<braced character code>");
    result_ = consumeToken(builder_, STRING_SPECIAL_LEFT_BRACE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, hex_char_body_braced_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, STRING_SPECIAL_RIGHT_BRACE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [hex_char_body_content_in_brace]
  private static boolean hex_char_body_braced_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hex_char_body_braced_1")) return false;
    hex_char_body_content_in_brace(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // char_code_hex
  static boolean hex_char_body_content_in_brace(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hex_char_body_content_in_brace")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = char_code_hex(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PerlParserGenerated::recover_string_braces);
    return result_;
  }

  /* ********************************************************** */
  // 'if' conditional_block if_compound_elsif * [if_compound_else]
  public static boolean if_compound(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_compound")) return false;
    if (!nextTokenIs(builder_, RESERVED_IF)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IF_COMPOUND, null);
    result_ = consumeToken(builder_, RESERVED_IF);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, conditional_block(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, if_compound_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && if_compound_3(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // if_compound_elsif *
  private static boolean if_compound_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_compound_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!if_compound_elsif(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "if_compound_2", pos_)) break;
    }
    return true;
  }

  // [if_compound_else]
  private static boolean if_compound_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_compound_3")) return false;
    if_compound_else(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // [POD]  'else' unconditional_block
  static boolean if_compound_else(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_compound_else")) return false;
    if (!nextTokenIs(builder_, "", POD, RESERVED_ELSE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = if_compound_else_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RESERVED_ELSE);
    pinned_ = result_; // pin = 2
    result_ = result_ && unconditional_block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [POD]
  private static boolean if_compound_else_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_compound_else_0")) return false;
    consumeToken(builder_, POD);
    return true;
  }

  /* ********************************************************** */
  // [POD]  'elsif' conditional_block
  static boolean if_compound_elsif(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_compound_elsif")) return false;
    if (!nextTokenIs(builder_, "", POD, RESERVED_ELSIF)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = if_compound_elsif_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RESERVED_ELSIF);
    pinned_ = result_; // pin = 2
    result_ = result_ && conditional_block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [POD]
  private static boolean if_compound_elsif_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_compound_elsif_0")) return false;
    consumeToken(builder_, POD);
    return true;
  }

  /* ********************************************************** */
  // 'if' expr
  public static boolean if_statement_modifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_statement_modifier")) return false;
    if (!nextTokenIs(builder_, "<Postfix if>", RESERVED_IF)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IF_STATEMENT_MODIFIER, "<Postfix if>");
    result_ = consumeToken(builder_, RESERVED_IF);
    pinned_ = result_; // pin = 1
    result_ = result_ && expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // deref_expr
  static boolean interpolated_constructs(PsiBuilder builder_, int level_) {
    return expr(builder_, level_ + 1, 20);
  }

  /* ********************************************************** */
  // <<parseLabelDeclaration>>
  public static boolean label_declaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "label_declaration")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LABEL_DECLARATION, "<label declaration>");
    result_ = parseLabelDeclaration(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // IDENTIFIER|'subname' !'('
  public static boolean label_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "label_expr")) return false;
    if (!nextTokenIs(builder_, "<expression>", IDENTIFIER, SUB_NAME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LABEL_EXPR, "<expression>");
    result_ = consumeToken(builder_, IDENTIFIER);
    if (!result_) result_ = label_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // 'subname' !'('
  private static boolean label_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "label_expr_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SUB_NAME);
    result_ = result_ && label_expr_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !'('
  private static boolean label_expr_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "label_expr_1_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, LEFT_PAREN);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // {method|code_primitive_variation} parenthesised_call_arguments
  static boolean leftward_call(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "leftward_call")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = leftward_call_0(builder_, level_ + 1);
    result_ = result_ && parenthesised_call_arguments(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // method|code_primitive_variation
  private static boolean leftward_call_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "leftward_call_0")) return false;
    boolean result_;
    result_ = method(builder_, level_ + 1);
    if (!result_) result_ = code_primitive_variation(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // scalar_variable | array_variable | hash_variable
  static boolean lexical_variable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "lexical_variable")) return false;
    boolean result_;
    result_ = scalar_variable(builder_, level_ + 1);
    if (!result_) result_ = array_variable(builder_, level_ + 1);
    if (!result_) result_ = hash_variable(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // <<custom_expr_arguments parse_list_expr>>
  static boolean list_expr_arguments(PsiBuilder builder_, int level_) {
    return custom_expr_arguments(builder_, level_ + 1, PerlParserGenerated::parse_list_expr);
  }

  /* ********************************************************** */
  // label_expr | expr
  static boolean lnr_param(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "lnr_param")) return false;
    boolean result_;
    result_ = label_expr(builder_, level_ + 1);
    if (!result_) result_ = expr(builder_, level_ + 1, -1);
    return result_;
  }

  /* ********************************************************** */
  // '(' local_variable_declaration_argument (comma + local_variable_declaration_argument ) * comma * ')'
  static boolean local_parenthesised_declaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "local_parenthesised_declaration")) return false;
    if (!nextTokenIs(builder_, LEFT_PAREN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, local_variable_declaration_argument(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, local_parenthesised_declaration_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, local_parenthesised_declaration_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && consumeToken(builder_, RIGHT_PAREN) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // (comma + local_variable_declaration_argument ) *
  private static boolean local_parenthesised_declaration_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "local_parenthesised_declaration_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!local_parenthesised_declaration_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "local_parenthesised_declaration_2", pos_)) break;
    }
    return true;
  }

  // comma + local_variable_declaration_argument
  private static boolean local_parenthesised_declaration_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "local_parenthesised_declaration_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = local_parenthesised_declaration_2_0_0(builder_, level_ + 1);
    result_ = result_ && local_variable_declaration_argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // comma +
  private static boolean local_parenthesised_declaration_2_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "local_parenthesised_declaration_2_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = comma(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!comma(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "local_parenthesised_declaration_2_0_0", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // comma *
  private static boolean local_parenthesised_declaration_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "local_parenthesised_declaration_3")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!comma(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "local_parenthesised_declaration_3", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // strict_variable_declaration_argument | parse_scalar_expr
  static boolean local_variable_declaration_argument(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "local_variable_declaration_argument")) return false;
    boolean result_;
    result_ = strict_variable_declaration_argument(builder_, level_ + 1);
    if (!result_) result_ = parse_scalar_expr(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // local_parenthesised_declaration | local_variable_declaration_argument
  static boolean local_variable_declaration_variation(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "local_variable_declaration_variation")) return false;
    boolean result_;
    result_ = local_parenthesised_declaration(builder_, level_ + 1);
    if (!result_) result_ = local_variable_declaration_argument(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // regex_match REGEX_QUOTE_CLOSE [perl_regex_modifiers]
  static boolean match_regex_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "match_regex_body")) return false;
    if (!nextTokenIs(builder_, REGEX_QUOTE_OPEN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = regex_match(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, REGEX_QUOTE_CLOSE);
    result_ = result_ && match_regex_body_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [perl_regex_modifiers]
  private static boolean match_regex_body_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "match_regex_body_2")) return false;
    perl_regex_modifiers(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // method_tokens
  public static boolean method(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, METHOD, "<method>");
    result_ = method_tokens(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // [method_signature] func_or_method_body
  static boolean method_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = method_body_0(builder_, level_ + 1);
    result_ = result_ && func_or_method_body(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [method_signature]
  private static boolean method_body_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_body_0")) return false;
    method_signature(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // {['async'] 'method'|'fp_override'} sub_names_token method_body
  public static boolean method_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_definition")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, METHOD_DEFINITION, "<method definition>");
    result_ = method_definition_0(builder_, level_ + 1);
    result_ = result_ && sub_names_token(builder_, level_ + 1);
    result_ = result_ && method_body(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ['async'] 'method'|'fp_override'
  private static boolean method_definition_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_definition_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = method_definition_0_0(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, RESERVED_OVERRIDE_FP);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // ['async'] 'method'
  private static boolean method_definition_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_definition_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = method_definition_0_0_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RESERVED_METHOD);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // ['async']
  private static boolean method_definition_0_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_definition_0_0_0")) return false;
    consumeToken(builder_, RESERVED_ASYNC);
    return true;
  }

  /* ********************************************************** */
  // <<parse_signature_content parse_method_signature_content>>
  static boolean method_signature(PsiBuilder builder_, int level_) {
    return parse_signature_content(builder_, level_ + 1, PerlParserGenerated::parse_method_signature_content);
  }

  /* ********************************************************** */
  // <<scalarDeclarationWrapper>> ':'
  public static boolean method_signature_invocant(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_signature_invocant")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, METHOD_SIGNATURE_INVOCANT, "<method signature invocant>");
    result_ = scalarDeclarationWrapper(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // 'list' | 'unary' | 'unary_custom'| 'argumentless' |
  //   'package::name::' 'subname' |
  //   'subname' ['package::name'] |
  //   'method' | 'func' | 'default' | 'fun' |
  //   'finally' | 'try' | 'catch' |
  //   'switch' | 'case' |
  //   'fp_override' | 'fp_after' | 'fp_before' | 'fp_around' | 'fp_augment' | 'class' | 'field'
  static boolean method_tokens(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_tokens")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, BUILTIN_LIST);
    if (!result_) result_ = consumeToken(builder_, BUILTIN_UNARY);
    if (!result_) result_ = consumeToken(builder_, CUSTOM_UNARY);
    if (!result_) result_ = consumeToken(builder_, BUILTIN_ARGUMENTLESS);
    if (!result_) result_ = parseTokens(builder_, 0, QUALIFYING_PACKAGE, SUB_NAME);
    if (!result_) result_ = method_tokens_5(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, RESERVED_METHOD);
    if (!result_) result_ = consumeToken(builder_, RESERVED_FUNC);
    if (!result_) result_ = consumeToken(builder_, RESERVED_DEFAULT);
    if (!result_) result_ = consumeToken(builder_, RESERVED_FUN);
    if (!result_) result_ = consumeToken(builder_, RESERVED_FINALLY);
    if (!result_) result_ = consumeToken(builder_, RESERVED_TRY);
    if (!result_) result_ = consumeToken(builder_, RESERVED_CATCH);
    if (!result_) result_ = consumeToken(builder_, RESERVED_SWITCH);
    if (!result_) result_ = consumeToken(builder_, RESERVED_CASE);
    if (!result_) result_ = consumeToken(builder_, RESERVED_OVERRIDE_FP);
    if (!result_) result_ = consumeToken(builder_, RESERVED_AFTER_FP);
    if (!result_) result_ = consumeToken(builder_, RESERVED_BEFORE_FP);
    if (!result_) result_ = consumeToken(builder_, RESERVED_AROUND_FP);
    if (!result_) result_ = consumeToken(builder_, RESERVED_AUGMENT_FP);
    if (!result_) result_ = consumeToken(builder_, RESERVED_CLASS);
    if (!result_) result_ = consumeToken(builder_, RESERVED_FIELD);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // 'subname' ['package::name']
  private static boolean method_tokens_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_tokens_5")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SUB_NAME);
    result_ = result_ && method_tokens_5_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // ['package::name']
  private static boolean method_tokens_5_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_tokens_5_1")) return false;
    consumeToken(builder_, PACKAGE);
    return true;
  }

  /* ********************************************************** */
  // BLOCK_NAME block
  public static boolean named_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_block")) return false;
    if (!nextTokenIs(builder_, "<named block>", BLOCK_NAME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NAMED_BLOCK, "<named block>");
    result_ = consumeToken(builder_, BLOCK_NAME);
    result_ = result_ && block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // sub_definition
  //     | named_block
  //     | method_definition
  //     | func_definition
  //     | before_modifier
  //     | after_modifier
  //     | around_modifier
  //     | augment_modifier
  static boolean named_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_definition")) return false;
    boolean result_;
    result_ = sub_definition(builder_, level_ + 1);
    if (!result_) result_ = named_block(builder_, level_ + 1);
    if (!result_) result_ = method_definition(builder_, level_ + 1);
    if (!result_) result_ = func_definition(builder_, level_ + 1);
    if (!result_) result_ = before_modifier(builder_, level_ + 1);
    if (!result_) result_ = after_modifier(builder_, level_ + 1);
    if (!result_) result_ = around_modifier(builder_, level_ + 1);
    if (!result_) result_ = augment_modifier(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // unary_method [unary_call_arguments]
  static boolean named_unary_call(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_unary_call")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = unary_method(builder_, level_ + 1);
    result_ = result_ && named_unary_call_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [unary_call_arguments]
  private static boolean named_unary_call_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "named_unary_call_1")) return false;
    unary_call_arguments(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // real_namespace_content
  public static boolean namespace_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namespace_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NAMESPACE_CONTENT, "<namespace content>");
    result_ = real_namespace_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // {namespace_definition_name|class_definition_name} (block | <<parseSemicolon>> <<parseNamespaceContent>>)
  public static boolean namespace_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namespace_definition")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NAMESPACE_DEFINITION, "<namespace definition>");
    result_ = namespace_definition_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && namespace_definition_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, PerlParserGenerated::recover_statement);
    return result_ || pinned_;
  }

  // namespace_definition_name|class_definition_name
  private static boolean namespace_definition_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namespace_definition_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = namespace_definition_name(builder_, level_ + 1);
    if (!result_) result_ = class_definition_name(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // block | <<parseSemicolon>> <<parseNamespaceContent>>
  private static boolean namespace_definition_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namespace_definition_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = block(builder_, level_ + 1);
    if (!result_) result_ = namespace_definition_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // <<parseSemicolon>> <<parseNamespaceContent>>
  private static boolean namespace_definition_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namespace_definition_1_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parseSemicolon(builder_, level_ + 1);
    result_ = result_ && parseNamespaceContent(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // 'package' any_package [perl_version]
  static boolean namespace_definition_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namespace_definition_name")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<namespace definition>");
    result_ = consumeToken(builder_, RESERVED_PACKAGE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, any_package(builder_, level_ + 1));
    result_ = pinned_ && namespace_definition_name_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, PerlParserGenerated::recover_statement);
    return result_ || pinned_;
  }

  // [perl_version]
  private static boolean namespace_definition_name_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "namespace_definition_name_2")) return false;
    perl_version(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // hash_index
  //     | array_index
  //     | regular_nested_call
  //     | parenthesised_call_arguments
  //     | scalar_call
  //     | post_deref_expr
  //     | post_deref_glob_expr
  //     | post_deref_array_slice_expr
  //     | post_deref_hash_slice_expr
  static boolean nested_element_variation(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nested_element_variation")) return false;
    boolean result_;
    result_ = hash_index(builder_, level_ + 1);
    if (!result_) result_ = array_index(builder_, level_ + 1);
    if (!result_) result_ = regular_nested_call(builder_, level_ + 1);
    if (!result_) result_ = parenthesised_call_arguments(builder_, level_ + 1);
    if (!result_) result_ = scalar_call(builder_, level_ + 1);
    if (!result_) result_ = post_deref_expr(builder_, level_ + 1);
    if (!result_) result_ = post_deref_glob_expr(builder_, level_ + 1);
    if (!result_) result_ = post_deref_array_slice_expr(builder_, level_ + 1);
    if (!result_) result_ = post_deref_hash_slice_expr(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // ('my' | 'state' ) [any_package] variable_declaration_variation [var_attributes]
  static boolean normal_lexical_declaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "normal_lexical_declaration")) return false;
    if (!nextTokenIs(builder_, "<lexical variable declaration>", RESERVED_MY, RESERVED_STATE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<lexical variable declaration>");
    result_ = normal_lexical_declaration_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, normal_lexical_declaration_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, variable_declaration_variation(builder_, level_ + 1)) && result_;
    result_ = pinned_ && normal_lexical_declaration_3(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'my' | 'state'
  private static boolean normal_lexical_declaration_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "normal_lexical_declaration_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, RESERVED_MY);
    if (!result_) result_ = consumeToken(builder_, RESERVED_STATE);
    return result_;
  }

  // [any_package]
  private static boolean normal_lexical_declaration_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "normal_lexical_declaration_1")) return false;
    any_package(builder_, level_ + 1);
    return true;
  }

  // [var_attributes]
  private static boolean normal_lexical_declaration_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "normal_lexical_declaration_3")) return false;
    var_attributes(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // expr [statement_modifier | <<parseStatementModifier>>]
  static boolean normal_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "normal_statement")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = expr(builder_, level_ + 1, -1);
    pinned_ = result_; // pin = 1
    result_ = result_ && normal_statement_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [statement_modifier | <<parseStatementModifier>>]
  private static boolean normal_statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "normal_statement_1")) return false;
    normal_statement_1_0(builder_, level_ + 1);
    return true;
  }

  // statement_modifier | <<parseStatementModifier>>
  private static boolean normal_statement_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "normal_statement_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = statement_modifier(builder_, level_ + 1);
    if (!result_) result_ = parseStatementModifier(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // 'nyi'
  public static boolean nyi_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nyi_statement")) return false;
    if (!nextTokenIs(builder_, "<statement>", OPERATOR_NYI)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NYI_STATEMENT, "<statement>");
    result_ = consumeToken(builder_, OPERATOR_NYI);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // oct_char_ambiguous | oct_char_unambiguous
  public static boolean oct_char(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "oct_char")) return false;
    if (!nextTokenIs(builder_, "<oct char>", STRING_SPECIAL_OCT, STRING_SPECIAL_OCT_AMBIGUOUS)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, OCT_CHAR, "<oct char>");
    result_ = oct_char_ambiguous(builder_, level_ + 1);
    if (!result_) result_ = oct_char_unambiguous(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '\0' [char_code_oct]
  static boolean oct_char_ambiguous(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "oct_char_ambiguous")) return false;
    if (!nextTokenIs(builder_, STRING_SPECIAL_OCT_AMBIGUOUS)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, STRING_SPECIAL_OCT_AMBIGUOUS);
    pinned_ = result_; // pin = 1
    result_ = result_ && oct_char_ambiguous_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [char_code_oct]
  private static boolean oct_char_ambiguous_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "oct_char_ambiguous_1")) return false;
    char_code_oct(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // '"{' oct_char_body_content_braced '}"'
  static boolean oct_char_body_braced(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "oct_char_body_braced")) return false;
    if (!nextTokenIs(builder_, STRING_SPECIAL_LEFT_BRACE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, STRING_SPECIAL_LEFT_BRACE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, oct_char_body_content_braced(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, STRING_SPECIAL_RIGHT_BRACE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // char_code_oct
  static boolean oct_char_body_content_braced(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "oct_char_body_content_braced")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = char_code_oct(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PerlParserGenerated::recover_string_braces);
    return result_;
  }

  /* ********************************************************** */
  // '\o' oct_char_body_braced
  static boolean oct_char_unambiguous(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "oct_char_unambiguous")) return false;
    if (!nextTokenIs(builder_, STRING_SPECIAL_OCT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, STRING_SPECIAL_OCT);
    pinned_ = result_; // pin = 1
    result_ = result_ && oct_char_body_braced(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // [expr]
  static boolean optional_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "optional_expression")) return false;
    expr(builder_, level_ + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // [parse_list_expr]
  static boolean optional_list_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "optional_list_expr")) return false;
    parse_list_expr(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // [parse_scalar_expr]
  static boolean optional_scalar_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "optional_scalar_expr")) return false;
    parse_scalar_expr(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // <<custom_expr_arguments optional_scalar_expr>>
  static boolean optional_scalar_expr_arguments(PsiBuilder builder_, int level_) {
    return custom_expr_arguments(builder_, level_ + 1, PerlParserGenerated::optional_scalar_expr);
  }

  /* ********************************************************** */
  // [unary_expr]
  static boolean optional_unary_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "optional_unary_expr")) return false;
    unary_expr(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // <<custom_expr_arguments optional_unary_expr>>
  static boolean optional_unary_expr_arguments(PsiBuilder builder_, int level_) {
    return custom_expr_arguments(builder_, level_ + 1, PerlParserGenerated::optional_unary_expr);
  }

  /* ********************************************************** */
  // 'otherwise' sub_expr_simple_ensured
  public static boolean otherwise_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "otherwise_expr")) return false;
    if (!nextTokenIs(builder_, "<expression>", RESERVED_OTHERWISE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, OTHERWISE_EXPR, "<expression>");
    result_ = consumeToken(builder_, RESERVED_OTHERWISE);
    pinned_ = result_; // pin = 1
    result_ = result_ && sub_expr_simple_ensured(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // parenthesised_call_arguments_body !'['
  public static boolean parenthesised_call_arguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesised_call_arguments")) return false;
    if (!nextTokenIs(builder_, LEFT_PAREN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parenthesised_call_arguments_body(builder_, level_ + 1);
    result_ = result_ && parenthesised_call_arguments_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, PARENTHESISED_CALL_ARGUMENTS, result_);
    return result_;
  }

  // !'['
  private static boolean parenthesised_call_arguments_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesised_call_arguments_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, LEFT_BRACKET);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '(' optional_expression ')'
  static boolean parenthesised_call_arguments_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesised_call_arguments_body")) return false;
    if (!nextTokenIs(builder_, LEFT_PAREN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, optional_expression(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_PAREN) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // parse_parenthesized_expression
  public static boolean parenthesised_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesised_expr")) return false;
    if (!nextTokenIs(builder_, "<expression>", LEFT_PAREN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PARENTHESISED_EXPR, "<expression>");
    result_ = parse_parenthesized_expression(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // [expr]
  static boolean parenthesised_expr_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesised_expr_content")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, true, false, PerlParserGenerated::recover_parenthesised);
    return true;
  }

  /* ********************************************************** */
  // use_vars_declarations
  public static boolean parsable_string_use_vars(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parsable_string_use_vars")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PARSABLE_STRING_USE_VARS, "<parsable string use vars>");
    result_ = use_vars_declarations(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // [around_signature_invocants] [func_signature_elements]
  static boolean parse_around_signature_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_around_signature_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_around_signature_content_0(builder_, level_ + 1);
    result_ = result_ && parse_around_signature_content_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [around_signature_invocants]
  private static boolean parse_around_signature_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_around_signature_content_0")) return false;
    around_signature_invocants(builder_, level_ + 1);
    return true;
  }

  // [func_signature_elements]
  private static boolean parse_around_signature_content_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_around_signature_content_1")) return false;
    func_signature_elements(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // &('{') !(anon_hash_lookahead) block [[POD] continue_block] !('->')
  static boolean parse_block_compound(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_block_compound")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_block_compound_0(builder_, level_ + 1);
    result_ = result_ && parse_block_compound_1(builder_, level_ + 1);
    result_ = result_ && block(builder_, level_ + 1);
    result_ = result_ && parse_block_compound_3(builder_, level_ + 1);
    result_ = result_ && parse_block_compound_4(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // &('{')
  private static boolean parse_block_compound_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_block_compound_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = consumeToken(builder_, LEFT_BRACE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // !(anon_hash_lookahead)
  private static boolean parse_block_compound_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_block_compound_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !parse_block_compound_1_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (anon_hash_lookahead)
  private static boolean parse_block_compound_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_block_compound_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = anon_hash_lookahead(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [[POD] continue_block]
  private static boolean parse_block_compound_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_block_compound_3")) return false;
    parse_block_compound_3_0(builder_, level_ + 1);
    return true;
  }

  // [POD] continue_block
  private static boolean parse_block_compound_3_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_block_compound_3_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_block_compound_3_0_0(builder_, level_ + 1);
    result_ = result_ && continue_block(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [POD]
  private static boolean parse_block_compound_3_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_block_compound_3_0_0")) return false;
    consumeToken(builder_, POD);
    return true;
  }

  // !('->')
  private static boolean parse_block_compound_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_block_compound_4")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, OPERATOR_DEREFERENCE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // &('+'|anon_hash_lookahead) parse_list_expr
  //     | arguments_list_with_codeblock
  //     | parse_list_expr
  static boolean parse_call_arguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_call_arguments")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_call_arguments_0(builder_, level_ + 1);
    if (!result_) result_ = arguments_list_with_codeblock(builder_, level_ + 1);
    if (!result_) result_ = parse_list_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // &('+'|anon_hash_lookahead) parse_list_expr
  private static boolean parse_call_arguments_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_call_arguments_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_call_arguments_0_0(builder_, level_ + 1);
    result_ = result_ && parse_list_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // &('+'|anon_hash_lookahead)
  private static boolean parse_call_arguments_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_call_arguments_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = parse_call_arguments_0_0_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // '+'|anon_hash_lookahead
  private static boolean parse_call_arguments_0_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_call_arguments_0_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, OPERATOR_PLUS);
    if (!result_) result_ = anon_hash_lookahead(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // condition_expr block
  static boolean parse_conditional_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_conditional_block")) return false;
    if (!nextTokenIs(builder_, LEFT_PAREN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = condition_expr(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '=' [parse_scalar_expr]
  static boolean parse_func_initializer(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_func_initializer")) return false;
    if (!nextTokenIs(builder_, OPERATOR_ASSIGN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, OPERATOR_ASSIGN);
    result_ = result_ && parse_func_initializer_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [parse_scalar_expr]
  private static boolean parse_func_initializer_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_func_initializer_1")) return false;
    parse_scalar_expr(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // [func_signature_elements]
  static boolean parse_func_signature_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_func_signature_content")) return false;
    func_signature_elements(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // [type_specifier]':' ? strict_variable_declaration_wrapper [parse_func_initializer] |  undef_expr | sub_signature_element_ignore
  static boolean parse_func_signature_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_func_signature_element")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_func_signature_element_0(builder_, level_ + 1);
    if (!result_) result_ = undef_expr(builder_, level_ + 1);
    if (!result_) result_ = sub_signature_element_ignore(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [type_specifier]':' ? strict_variable_declaration_wrapper [parse_func_initializer]
  private static boolean parse_func_signature_element_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_func_signature_element_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_func_signature_element_0_0(builder_, level_ + 1);
    result_ = result_ && parse_func_signature_element_0_1(builder_, level_ + 1);
    result_ = result_ && strict_variable_declaration_wrapper(builder_, level_ + 1);
    result_ = result_ && parse_func_signature_element_0_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [type_specifier]
  private static boolean parse_func_signature_element_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_func_signature_element_0_0")) return false;
    type_specifier(builder_, level_ + 1);
    return true;
  }

  // ':' ?
  private static boolean parse_func_signature_element_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_func_signature_element_0_1")) return false;
    consumeToken(builder_, COLON);
    return true;
  }

  // [parse_func_initializer]
  private static boolean parse_func_signature_element_0_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_func_signature_element_0_3")) return false;
    parse_func_initializer(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // <<custom_expr_arguments grep_map_arguments_variants>>
  static boolean parse_grep_map_arguments(PsiBuilder builder_, int level_) {
    return custom_expr_arguments(builder_, level_ + 1, PerlParserGenerated::grep_map_arguments_variants);
  }

  /* ********************************************************** */
  // <<parseExpressionLevel 2>>
  public static boolean parse_list_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_list_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, EXPR, "<expression>");
    result_ = parseExpressionLevel(builder_, level_ + 1, 2);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // [method_signature_invocant] [func_signature_elements]
  static boolean parse_method_signature_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_method_signature_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_method_signature_content_0(builder_, level_ + 1);
    result_ = result_ && parse_method_signature_content_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [method_signature_invocant]
  private static boolean parse_method_signature_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_method_signature_content_0")) return false;
    method_signature_invocant(builder_, level_ + 1);
    return true;
  }

  // [func_signature_elements]
  private static boolean parse_method_signature_content_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_method_signature_content_1")) return false;
    func_signature_elements(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // 'no' use_no_parameters <<statementSemi>>
  static boolean parse_no_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_no_statement")) return false;
    if (!nextTokenIs(builder_, RESERVED_NO)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, RESERVED_NO);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, use_no_parameters(builder_, level_ + 1));
    result_ = pinned_ && statementSemi(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '(' parenthesised_expr_content ')'
  static boolean parse_parenthesized_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_parenthesized_expression")) return false;
    if (!nextTokenIs(builder_, "<Parenthesised expression>", LEFT_PAREN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<Parenthesised expression>");
    result_ = consumeToken(builder_, LEFT_PAREN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, parenthesised_expr_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_PAREN) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // <<isUseVars>> <<mapUseVars qw_string_content>>+| qw_string_content
  static boolean parse_qw_string_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_qw_string_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_qw_string_content_0(builder_, level_ + 1);
    if (!result_) result_ = qw_string_content(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // <<isUseVars>> <<mapUseVars qw_string_content>>+
  private static boolean parse_qw_string_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_qw_string_content_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = isUseVars(builder_, level_ + 1);
    result_ = result_ && parse_qw_string_content_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // <<mapUseVars qw_string_content>>+
  private static boolean parse_qw_string_content_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_qw_string_content_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = mapUseVars(builder_, level_ + 1, PerlParserGenerated::qw_string_content);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!mapUseVars(builder_, level_ + 1, PerlParserGenerated::qw_string_content)) break;
      if (!empty_element_parsed_guard_(builder_, "parse_qw_string_content_0_1", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // <<parseExpressionLevel 3>>
  static boolean parse_scalar_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_scalar_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<expression>");
    result_ = parseExpressionLevel(builder_, level_ + 1, 3);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '(' <<signature_content <<x1>>>> ')'
  static boolean parse_signature_content(PsiBuilder builder_, int level_, Parser x1) {
    if (!recursion_guard_(builder_, level_, "parse_signature_content")) return false;
    if (!nextTokenIs(builder_, LEFT_PAREN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, signature_content(builder_, level_ + 1, x1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_PAREN) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // <<custom_expr_arguments  sort_arguments_variants>>
  static boolean parse_sort_arguments(PsiBuilder builder_, int level_) {
    return custom_expr_arguments(builder_, level_ + 1, PerlParserGenerated::sort_arguments_variants);
  }

  /* ********************************************************** */
  // ':' attribute ([':'] attribute)*
  static boolean parse_sub_attributes(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_sub_attributes")) return false;
    if (!nextTokenIs(builder_, COLON)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, COLON);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, attribute(builder_, level_ + 1));
    result_ = pinned_ && parse_sub_attributes_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // ([':'] attribute)*
  private static boolean parse_sub_attributes_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_sub_attributes_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!parse_sub_attributes_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "parse_sub_attributes_2", pos_)) break;
    }
    return true;
  }

  // [':'] attribute
  private static boolean parse_sub_attributes_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_sub_attributes_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_sub_attributes_2_0_0(builder_, level_ + 1);
    result_ = result_ && attribute(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [':']
  private static boolean parse_sub_attributes_2_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_sub_attributes_2_0_0")) return false;
    consumeToken(builder_, COLON);
    return true;
  }

  /* ********************************************************** */
  // leftward_call |
  //  	named_unary_call |
  //  	argumentless_call |
  //  	rightward_call
  static boolean parse_sub_call(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_sub_call")) return false;
    boolean result_;
    result_ = leftward_call(builder_, level_ + 1);
    if (!result_) result_ = named_unary_call(builder_, level_ + 1);
    if (!result_) result_ = argumentless_call(builder_, level_ + 1);
    if (!result_) result_ = rightward_call(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // sub_signature_element (',' sub_signature_element) * ','*
  static boolean parse_sub_signature(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_sub_signature")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = sub_signature_element(builder_, level_ + 1);
    result_ = result_ && parse_sub_signature_1(builder_, level_ + 1);
    result_ = result_ && parse_sub_signature_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (',' sub_signature_element) *
  private static boolean parse_sub_signature_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_sub_signature_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!parse_sub_signature_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "parse_sub_signature_1", pos_)) break;
    }
    return true;
  }

  // ',' sub_signature_element
  private static boolean parse_sub_signature_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_sub_signature_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COMMA);
    result_ = result_ && sub_signature_element(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // ','*
  private static boolean parse_sub_signature_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_sub_signature_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, COMMA)) break;
      if (!empty_element_parsed_guard_(builder_, "parse_sub_signature_2", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // signature_left_side ['=' [parse_scalar_expr]]
  static boolean parse_sub_signature_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_sub_signature_element")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = signature_left_side(builder_, level_ + 1);
    result_ = result_ && parse_sub_signature_element_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // ['=' [parse_scalar_expr]]
  private static boolean parse_sub_signature_element_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_sub_signature_element_1")) return false;
    parse_sub_signature_element_1_0(builder_, level_ + 1);
    return true;
  }

  // '=' [parse_scalar_expr]
  private static boolean parse_sub_signature_element_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_sub_signature_element_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, OPERATOR_ASSIGN);
    result_ = result_ && parse_sub_signature_element_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [parse_scalar_expr]
  private static boolean parse_sub_signature_element_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_sub_signature_element_1_0_1")) return false;
    parse_scalar_expr(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // 'use' <<parseUseParameters use_no_parameters>> <<statementSemi>>
  static boolean parse_use_statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_use_statement")) return false;
    if (!nextTokenIs(builder_, RESERVED_USE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, RESERVED_USE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, parseUseParameters(builder_, level_ + 1, PerlParserGenerated::use_no_parameters));
    result_ = pinned_ && statementSemi(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // ':' attribute ([':'] attribute)*
  static boolean parse_var_attributes(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_var_attributes")) return false;
    if (!nextTokenIs(builder_, COLON)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, COLON);
    result_ = result_ && attribute(builder_, level_ + 1);
    result_ = result_ && parse_var_attributes_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // ([':'] attribute)*
  private static boolean parse_var_attributes_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_var_attributes_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!parse_var_attributes_2_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "parse_var_attributes_2", pos_)) break;
    }
    return true;
  }

  // [':'] attribute
  private static boolean parse_var_attributes_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_var_attributes_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_var_attributes_2_0_0(builder_, level_ + 1);
    result_ = result_ && attribute(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [':']
  private static boolean parse_var_attributes_2_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_var_attributes_2_0_0")) return false;
    consumeToken(builder_, COLON);
    return true;
  }

  /* ********************************************************** */
  // perl_handle_expr | block | scalar_variable !('{'|'['|<<isOperatorToken>>)
  static boolean perl_handle(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "perl_handle")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = perl_handle_expr(builder_, level_ + 1);
    if (!result_) result_ = block(builder_, level_ + 1);
    if (!result_) result_ = perl_handle_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // scalar_variable !('{'|'['|<<isOperatorToken>>)
  private static boolean perl_handle_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "perl_handle_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = scalar_variable(builder_, level_ + 1);
    result_ = result_ && perl_handle_2_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !('{'|'['|<<isOperatorToken>>)
  private static boolean perl_handle_2_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "perl_handle_2_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !perl_handle_2_1_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // '{'|'['|<<isOperatorToken>>
  private static boolean perl_handle_2_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "perl_handle_2_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_BRACE);
    if (!result_) result_ = consumeToken(builder_, LEFT_BRACKET);
    if (!result_) result_ = isOperatorToken(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // perl_regex_item *
  public static boolean perl_regex(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "perl_regex")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PERL_REGEX, "<perl regex>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!perl_regex_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "perl_regex", pos_)) break;
    }
    register_hook_(builder_, RIGHT_BINDER, GREEDY_RIGHT_BINDER);
    register_hook_(builder_, LEFT_BINDER, GREEDY_LEFT_BINDER);
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // 'regex' |
  //         block_compound |
  // 	interpolated_constructs
  static boolean perl_regex_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "perl_regex_item")) return false;
    boolean result_;
    result_ = consumeToken(builder_, REGEX_TOKEN);
    if (!result_) result_ = block_compound(builder_, level_ + 1);
    if (!result_) result_ = interpolated_constructs(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '/m' +
  public static boolean perl_regex_modifiers(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "perl_regex_modifiers")) return false;
    if (!nextTokenIs(builder_, REGEX_MODIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, REGEX_MODIFIER);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, REGEX_MODIFIER)) break;
      if (!empty_element_parsed_guard_(builder_, "perl_regex_modifiers", pos_)) break;
    }
    exit_section_(builder_, marker_, PERL_REGEX_MODIFIERS, result_);
    return result_;
  }

  /* ********************************************************** */
  // <<parsePerlVersion>>
  static boolean perl_version(PsiBuilder builder_, int level_) {
    return parsePerlVersion(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // POD
  static boolean pod_section(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_section")) return false;
    if (!nextTokenIs(builder_, "<pod section>", POD)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<pod section>");
    result_ = consumeToken(builder_, POD);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '$@' {hash_index|array_index}
  public static boolean post_deref_array_slice_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "post_deref_array_slice_expr")) return false;
    if (!nextTokenIs(builder_, "<Array slice>", SIGIL_ARRAY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POST_DEREF_ARRAY_SLICE_EXPR, "<Array slice>");
    result_ = consumeToken(builder_, SIGIL_ARRAY);
    result_ = result_ && post_deref_array_slice_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // hash_index|array_index
  private static boolean post_deref_array_slice_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "post_deref_array_slice_expr_1")) return false;
    boolean result_;
    result_ = hash_index(builder_, level_ + 1);
    if (!result_) result_ = array_index(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '->$*'|'->$#*'|'->@*'|'->%*'|'->**'|'->&*'
  public static boolean post_deref_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "post_deref_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POST_DEREF_EXPR, "<Postderef>");
    result_ = consumeToken(builder_, DEREF_SCALAR);
    if (!result_) result_ = consumeToken(builder_, DEREF_SCALAR_INDEX);
    if (!result_) result_ = consumeToken(builder_, DEREF_ARRAY);
    if (!result_) result_ = consumeToken(builder_, DEREF_HASH);
    if (!result_) result_ = consumeToken(builder_, DEREF_GLOB);
    if (!result_) result_ = consumeToken(builder_, DEREF_CODE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '$*' hash_index
  public static boolean post_deref_glob_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "post_deref_glob_expr")) return false;
    if (!nextTokenIs(builder_, "<Glob expr>", SIGIL_GLOB)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POST_DEREF_GLOB_EXPR, "<Glob expr>");
    result_ = consumeToken(builder_, SIGIL_GLOB);
    result_ = result_ && hash_index(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '$%' {hash_index|array_index}
  public static boolean post_deref_hash_slice_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "post_deref_hash_slice_expr")) return false;
    if (!nextTokenIs(builder_, "<Hash slice>", SIGIL_HASH)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POST_DEREF_HASH_SLICE_EXPR, "<Hash slice>");
    result_ = consumeToken(builder_, SIGIL_HASH);
    result_ = result_ && post_deref_hash_slice_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // hash_index|array_index
  private static boolean post_deref_hash_slice_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "post_deref_hash_slice_expr_1")) return false;
    boolean result_;
    result_ = hash_index(builder_, level_ + 1);
    if (!result_) result_ = array_index(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // parenthesised_call_arguments
  public static boolean primitive_call(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "primitive_call")) return false;
    if (!nextTokenIs(builder_, LEFT_PAREN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _LEFT_, SUB_CALL, null);
    result_ = parenthesised_call_arguments(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // print_arguments_contents
  public static boolean print_arguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "print_arguments")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CALL_ARGUMENTS, "<print arguments>");
    result_ = print_arguments_contents(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // [perl_handle] [print_arguments_contents_tail]
  public static boolean print_arguments_contents(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "print_arguments_contents")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, COMMA_SEQUENCE_EXPR, "<print arguments contents>");
    result_ = print_arguments_contents_0(builder_, level_ + 1);
    result_ = result_ && print_arguments_contents_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [perl_handle]
  private static boolean print_arguments_contents_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "print_arguments_contents_0")) return false;
    perl_handle(builder_, level_ + 1);
    return true;
  }

  // [print_arguments_contents_tail]
  private static boolean print_arguments_contents_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "print_arguments_contents_1")) return false;
    print_arguments_contents_tail(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // parse_scalar_expr {comma [parse_scalar_expr]}*
  static boolean print_arguments_contents_tail(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "print_arguments_contents_tail")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = parse_scalar_expr(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && print_arguments_contents_tail_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // {comma [parse_scalar_expr]}*
  private static boolean print_arguments_contents_tail_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "print_arguments_contents_tail_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!print_arguments_contents_tail_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "print_arguments_contents_tail_1", pos_)) break;
    }
    return true;
  }

  // comma [parse_scalar_expr]
  private static boolean print_arguments_contents_tail_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "print_arguments_contents_tail_1_0")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = comma(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && print_arguments_contents_tail_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [parse_scalar_expr]
  private static boolean print_arguments_contents_tail_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "print_arguments_contents_tail_1_0_1")) return false;
    parse_scalar_expr(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // print_parenthesized_call_arguments_body !('[')
  public static boolean print_parenthesized_call_arguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "print_parenthesized_call_arguments")) return false;
    if (!nextTokenIs(builder_, LEFT_PAREN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = print_parenthesized_call_arguments_body(builder_, level_ + 1);
    result_ = result_ && print_parenthesized_call_arguments_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, PARENTHESISED_CALL_ARGUMENTS, result_);
    return result_;
  }

  // !('[')
  private static boolean print_parenthesized_call_arguments_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "print_parenthesized_call_arguments_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, LEFT_BRACKET);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '(' [print_arguments_contents] ')'
  static boolean print_parenthesized_call_arguments_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "print_parenthesized_call_arguments_body")) return false;
    if (!nextTokenIs(builder_, LEFT_PAREN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, print_parenthesized_call_arguments_body_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_PAREN) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [print_arguments_contents]
  private static boolean print_parenthesized_call_arguments_body_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "print_parenthesized_call_arguments_body_1")) return false;
    print_arguments_contents(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // qq_string_element+
  static boolean qq_string_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qq_string_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = qq_string_element(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!qq_string_element(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "qq_string_content", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // qq_string_content
  static boolean qq_string_content_with_lp(PsiBuilder builder_, int level_) {
    return qq_string_content(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // STRING_CONTENT_QQ | special_constructs | interpolated_constructs
  static boolean qq_string_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qq_string_element")) return false;
    boolean result_;
    result_ = consumeToken(builder_, STRING_CONTENT_QQ);
    if (!result_) result_ = special_constructs(builder_, level_ + 1);
    if (!result_) result_ = interpolated_constructs(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // QUOTE_DOUBLE_OPEN [qq_string_content_with_lp] QUOTE_DOUBLE_CLOSE
  static boolean quoted_qq_string(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "quoted_qq_string")) return false;
    if (!nextTokenIs(builder_, QUOTE_DOUBLE_OPEN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, QUOTE_DOUBLE_OPEN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, quoted_qq_string_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, QUOTE_DOUBLE_CLOSE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [qq_string_content_with_lp]
  private static boolean quoted_qq_string_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "quoted_qq_string_1")) return false;
    qq_string_content_with_lp(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // QUOTE_SINGLE_OPEN [smart_sq_string_content] QUOTE_SINGLE_CLOSE
  static boolean quoted_sq_string(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "quoted_sq_string")) return false;
    if (!nextTokenIs(builder_, QUOTE_SINGLE_OPEN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, QUOTE_SINGLE_OPEN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, quoted_sq_string_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, QUOTE_SINGLE_CLOSE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [smart_sq_string_content]
  private static boolean quoted_sq_string_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "quoted_sq_string_1")) return false;
    smart_sq_string_content(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // QUOTE_TICK_OPEN [qx_string_content] QUOTE_TICK_CLOSE
  static boolean quoted_xq_string(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "quoted_xq_string")) return false;
    if (!nextTokenIs(builder_, QUOTE_TICK_OPEN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, QUOTE_TICK_OPEN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, quoted_xq_string_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, QUOTE_TICK_CLOSE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [qx_string_content]
  private static boolean quoted_xq_string_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "quoted_xq_string_1")) return false;
    qx_string_content(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // QUOTE_SINGLE_OPEN [parse_qw_string_content] QUOTE_SINGLE_CLOSE
  static boolean qw_string(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qw_string")) return false;
    if (!nextTokenIs(builder_, QUOTE_SINGLE_OPEN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, QUOTE_SINGLE_OPEN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, qw_string_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, QUOTE_SINGLE_CLOSE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [parse_qw_string_content]
  private static boolean qw_string_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qw_string_1")) return false;
    parse_qw_string_content(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // string_bare+
  static boolean qw_string_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qw_string_content")) return false;
    if (!nextTokenIs(builder_, "", STRING_CONTENT, STRING_SPECIAL_ESCAPE_CHAR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = string_bare(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!string_bare(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "qw_string_content", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // qx_string_element+
  static boolean qx_string_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qx_string_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = qx_string_element(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!qx_string_element(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "qx_string_content", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // STRING_CONTENT_XQ | special_constructs |interpolated_constructs
  static boolean qx_string_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "qx_string_element")) return false;
    boolean result_;
    result_ = consumeToken(builder_, STRING_CONTENT_XQ);
    if (!result_) result_ = special_constructs(builder_, level_ + 1);
    if (!result_) result_ = interpolated_constructs(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // {!('package'|'class') file_item} *
  static boolean real_namespace_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "real_namespace_content")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    while (true) {
      int pos_ = current_position_(builder_);
      if (!real_namespace_content_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "real_namespace_content", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, PerlParserGenerated::recover_statement);
    return true;
  }

  // !('package'|'class') file_item
  private static boolean real_namespace_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "real_namespace_content_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = real_namespace_content_0_0(builder_, level_ + 1);
    result_ = result_ && file_item(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !('package'|'class')
  private static boolean real_namespace_content_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "real_namespace_content_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !real_namespace_content_0_0_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // 'package'|'class'
  private static boolean real_namespace_content_0_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "real_namespace_content_0_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, RESERVED_PACKAGE);
    if (!result_) result_ = consumeToken(builder_, RESERVED_CLASS);
    return result_;
  }

  /* ********************************************************** */
  // !'}'
  static boolean recover_braced_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_braced_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, RIGHT_BRACE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // !']'
  static boolean recover_bracketed_expression(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_bracketed_expression")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, RIGHT_BRACKET);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // !(')' | '{' | '}' | <<checkSemicolon>> )
  static boolean recover_parenthesised(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_parenthesised")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !recover_parenthesised_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ')' | '{' | '}' | <<checkSemicolon>>
  private static boolean recover_parenthesised_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_parenthesised_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, RIGHT_PAREN);
    if (!result_) result_ = consumeToken(builder_, LEFT_BRACE);
    if (!result_) result_ = consumeToken(builder_, RIGHT_BRACE);
    if (!result_) result_ = checkSemicolon(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // !('r}')
  static boolean recover_regex_replacement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_regex_replacement")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, REGEX_QUOTE_CLOSE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // !(')'|'{'|SUB_PROTOTYPE_TOKEN)
  static boolean recover_signature_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_signature_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !recover_signature_content_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ')'|'{'|SUB_PROTOTYPE_TOKEN
  private static boolean recover_signature_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_signature_content_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, RIGHT_PAREN);
    if (!result_) result_ = consumeToken(builder_, LEFT_BRACE);
    if (!result_) result_ = consumeToken(builder_, SUB_PROTOTYPE_TOKEN);
    return result_;
  }

  /* ********************************************************** */
  // <<recoverStatement>>
  static boolean recover_statement(PsiBuilder builder_, int level_) {
    return recoverStatement(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // !('}"')
  static boolean recover_string_braces(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_string_braces")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, STRING_SPECIAL_RIGHT_BRACE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // block_braceless
  static boolean regex_code(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "regex_code")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = block_braceless(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PerlParserGenerated::recover_regex_replacement);
    return result_;
  }

  /* ********************************************************** */
  // 'r{' [perl_regex]
  static boolean regex_match(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "regex_match")) return false;
    if (!nextTokenIs(builder_, REGEX_QUOTE_OPEN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, REGEX_QUOTE_OPEN);
    result_ = result_ && regex_match_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [perl_regex]
  private static boolean regex_match_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "regex_match_1")) return false;
    perl_regex(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // regex_replace_regex | regex_replace_code
  static boolean regex_replace(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "regex_replace")) return false;
    boolean result_;
    result_ = regex_replace_regex(builder_, level_ + 1);
    if (!result_) result_ = regex_replace_code(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // {'re/' | 'r}' 're{' } [regex_code]
  static boolean regex_replace_code(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "regex_replace_code")) return false;
    if (!nextTokenIs(builder_, "", REGEX_QUOTE_CLOSE, REGEX_QUOTE_E)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = regex_replace_code_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && regex_replace_code_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 're/' | 'r}' 're{'
  private static boolean regex_replace_code_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "regex_replace_code_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, REGEX_QUOTE_E);
    if (!result_) result_ = parseTokens(builder_, 0, REGEX_QUOTE_CLOSE, REGEX_QUOTE_OPEN_E);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [regex_code]
  private static boolean regex_replace_code_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "regex_replace_code_1")) return false;
    regex_code(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // {'r/' | 'r}' 'r{' } regex_replacement
  static boolean regex_replace_regex(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "regex_replace_regex")) return false;
    if (!nextTokenIs(builder_, "", REGEX_QUOTE, REGEX_QUOTE_CLOSE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = regex_replace_regex_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && regex_replacement(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'r/' | 'r}' 'r{'
  private static boolean regex_replace_regex_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "regex_replace_regex_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, REGEX_QUOTE);
    if (!result_) result_ = parseTokens(builder_, 0, REGEX_QUOTE_CLOSE, REGEX_QUOTE_OPEN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // regex_replacement_content
  public static boolean regex_replacement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "regex_replacement")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, REGEX_REPLACEMENT, "<regex replacement>");
    result_ = regex_replacement_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PerlParserGenerated::recover_regex_replacement);
    return result_;
  }

  /* ********************************************************** */
  // [qq_string_content]
  static boolean regex_replacement_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "regex_replacement_content")) return false;
    qq_string_content(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // regular_nested_call_variations
  public static boolean regular_nested_call(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "regular_nested_call")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, SUB_CALL, "<regular nested call>");
    result_ = regular_nested_call_variations(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // leftward_call | method
  static boolean regular_nested_call_variations(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "regular_nested_call_variations")) return false;
    boolean result_;
    result_ = leftward_call(builder_, level_ + 1);
    if (!result_) result_ = method(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // method  [call_arguments]
  static boolean rightward_call(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "rightward_call")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = method(builder_, level_ + 1);
    result_ = result_ && rightward_call_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [call_arguments]
  private static boolean rightward_call_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "rightward_call_1")) return false;
    call_arguments(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // <<parseFileContent>> file_items
  static boolean root(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "root")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parseFileContent(builder_, level_ + 1);
    result_ = result_ && file_items(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // scalar_or_element [parenthesised_call_arguments]
  public static boolean scalar_call(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "scalar_call")) return false;
    if (!nextTokenIs(builder_, "<scalar call>", RESERVED_UNDEF, SIGIL_SCALAR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SCALAR_CALL, "<scalar call>");
    result_ = scalar_or_element(builder_, level_ + 1);
    result_ = result_ && scalar_call_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [parenthesised_call_arguments]
  private static boolean scalar_call_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "scalar_call_1")) return false;
    parenthesised_call_arguments(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // '$$' scalar_cast_target
  public static boolean scalar_cast_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "scalar_cast_expr")) return false;
    if (!nextTokenIs(builder_, "<scalar dereference>", SIGIL_SCALAR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SCALAR_CAST_EXPR, "<scalar dereference>");
    result_ = consumeToken(builder_, SIGIL_SCALAR);
    result_ = result_ && scalar_cast_target(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // block_scalar | scalar_primitive
  static boolean scalar_cast_target(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "scalar_cast_target")) return false;
    boolean result_;
    result_ = block_scalar(builder_, level_ + 1);
    if (!result_) result_ = scalar_primitive(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // scalar_primitive [array_element | hash_element]
  static boolean scalar_or_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "scalar_or_element")) return false;
    if (!nextTokenIs(builder_, "", RESERVED_UNDEF, SIGIL_SCALAR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = scalar_primitive(builder_, level_ + 1);
    result_ = result_ && scalar_or_element_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [array_element | hash_element]
  private static boolean scalar_or_element_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "scalar_or_element_1")) return false;
    scalar_or_element_1_0(builder_, level_ + 1);
    return true;
  }

  // array_element | hash_element
  private static boolean scalar_or_element_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "scalar_or_element_1_0")) return false;
    boolean result_;
    result_ = array_element(builder_, level_ + 1);
    if (!result_) result_ = hash_element(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // scalar_variable | scalar_cast_expr | undef_expr
  static boolean scalar_primitive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "scalar_primitive")) return false;
    if (!nextTokenIs(builder_, "", RESERVED_UNDEF, SIGIL_SCALAR)) return false;
    boolean result_;
    result_ = scalar_variable(builder_, level_ + 1);
    if (!result_) result_ = scalar_cast_expr(builder_, level_ + 1);
    if (!result_) result_ = undef_expr(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '$$' {SCALAR_NAME | '${' SCALAR_NAME '$}'}
  public static boolean scalar_variable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "scalar_variable")) return false;
    if (!nextTokenIs(builder_, "<scalar>", SIGIL_SCALAR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SCALAR_VARIABLE, "<scalar>");
    result_ = consumeToken(builder_, SIGIL_SCALAR);
    result_ = result_ && scalar_variable_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // SCALAR_NAME | '${' SCALAR_NAME '$}'
  private static boolean scalar_variable_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "scalar_variable_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, SCALAR_NAME);
    if (!result_) result_ = parseTokens(builder_, 0, LEFT_BRACE_SCALAR, SCALAR_NAME, RIGHT_BRACE_SCALAR);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // <<x1>>
  public static boolean signature_content(PsiBuilder builder_, int level_, Parser x1) {
    if (!recursion_guard_(builder_, level_, "signature_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SIGNATURE_CONTENT, null);
    result_ = x1.parse(builder_, level_);
    register_hook_(builder_, LEFT_BINDER, PERL_LEADING_COMMENTS_BINDER);
    exit_section_(builder_, level_, marker_, result_, false, PerlParserGenerated::recover_signature_content);
    return result_;
  }

  /* ********************************************************** */
  // <<x1>>
  public static boolean signature_element(PsiBuilder builder_, int level_, Parser x1) {
    if (!recursion_guard_(builder_, level_, "signature_element")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = x1.parse(builder_, level_);
    exit_section_(builder_, marker_, SIGNATURE_ELEMENT, result_);
    return result_;
  }

  /* ********************************************************** */
  // variable_declaration_element | sub_signature_element_ignore
  static boolean signature_left_side(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "signature_left_side")) return false;
    boolean result_;
    result_ = variable_declaration_element(builder_, level_ + 1);
    if (!result_) result_ = sub_signature_element_ignore(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // <<parseExpressionLevel 20>>
  static boolean single_argument_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "single_argument_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<expression>");
    result_ = parseExpressionLevel(builder_, level_ + 1, 20);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // <<isUseVars>> <<mapUseVars sq_string_content_element>>+ | sq_string_content
  static boolean smart_sq_string_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "smart_sq_string_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = smart_sq_string_content_0(builder_, level_ + 1);
    if (!result_) result_ = sq_string_content(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // <<isUseVars>> <<mapUseVars sq_string_content_element>>+
  private static boolean smart_sq_string_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "smart_sq_string_content_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = isUseVars(builder_, level_ + 1);
    result_ = result_ && smart_sq_string_content_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // <<mapUseVars sq_string_content_element>>+
  private static boolean smart_sq_string_content_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "smart_sq_string_content_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = mapUseVars(builder_, level_ + 1, PerlParserGenerated::sq_string_content_element);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!mapUseVars(builder_, level_ + 1, PerlParserGenerated::sq_string_content_element)) break;
      if (!empty_element_parsed_guard_(builder_, "smart_sq_string_content_0_1", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // grep_map_sort_with_block |
  //     sorter grep_map_sort_tail |
  //     grep_map_sort_tail
  static boolean sort_arguments_variants(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sort_arguments_variants")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = grep_map_sort_with_block(builder_, level_ + 1);
    if (!result_) result_ = sort_arguments_variants_1(builder_, level_ + 1);
    if (!result_) result_ = grep_map_sort_tail(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // sorter grep_map_sort_tail
  private static boolean sort_arguments_variants_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sort_arguments_variants_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = sorter(builder_, level_ + 1);
    result_ = result_ && grep_map_sort_tail(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // scalar_variable | method
  static boolean sorter(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sorter")) return false;
    boolean result_;
    result_ = scalar_variable(builder_, level_ + 1);
    if (!result_) result_ = method(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '\t'|'\n'|'\r'|'\f'|'\b'|'\a'|'\e'|'\l'|'\\u'| '\1' |
  //                                '\\"'|
  //                                '\L'|'\U'|'\F'|'\Q'|'\E'|
  //                                unicode_char | hex_char | oct_char | esc_char
  static boolean special_constructs(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "special_constructs")) return false;
    boolean result_;
    result_ = consumeToken(builder_, STRING_SPECIAL_TAB);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_NEWLINE);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_RETURN);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_FORMFEED);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_BACKSPACE);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_ALARM);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_ESCAPE);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_LCFIRST);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_TCFIRST);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_BACKREF);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_ESCAPE_CHAR);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_LOWERCASE_START);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_UPPERCASE_START);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_FOLDCASE_START);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_QUOTE_START);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_MODIFIER_END);
    if (!result_) result_ = unicode_char(builder_, level_ + 1);
    if (!result_) result_ = hex_char(builder_, level_ + 1);
    if (!result_) result_ = oct_char(builder_, level_ + 1);
    if (!result_) result_ = esc_char(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // sq_string_content_element +
  static boolean sq_string_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sq_string_content")) return false;
    if (!nextTokenIs(builder_, "", STRING_CONTENT, STRING_SPECIAL_ESCAPE_CHAR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = sq_string_content_element(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!sq_string_content_element(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "sq_string_content", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // STRING_CONTENT | '\\"'
  static boolean sq_string_content_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sq_string_content_element")) return false;
    if (!nextTokenIs(builder_, "", STRING_CONTENT, STRING_SPECIAL_ESCAPE_CHAR)) return false;
    boolean result_;
    result_ = consumeToken(builder_, STRING_CONTENT);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_ESCAPE_CHAR);
    return result_;
  }

  /* ********************************************************** */
  // sub_declaration | statement_body <<statementSemi>>
  public static boolean statement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, STATEMENT, "<statement>");
    result_ = sub_declaration(builder_, level_ + 1);
    if (!result_) result_ = statement_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // statement_body <<statementSemi>>
  private static boolean statement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = statement_body(builder_, level_ + 1);
    result_ = result_ && statementSemi(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // normal_statement
  static boolean statement_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_body")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = normal_statement(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PerlParserGenerated::recover_statement);
    return result_;
  }

  /* ********************************************************** */
  // <<parseSemicolon>> +
  //         | nyi_statement
  //         | <<parseParserExtensionStatement>>
  //         | named_definition
  //         | compound_statement
  //         | format_definition
  //         | <<parseUse>>
  //         | <<parseNo>>
  //         | block_compound
  //         | statement
  //         | annotation
  //         | pod_section
  //         | end_or_data
  //         | <<parseBadCharacters>>
  static boolean statement_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_item")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = statement_item_0(builder_, level_ + 1);
    if (!result_) result_ = nyi_statement(builder_, level_ + 1);
    if (!result_) result_ = parseParserExtensionStatement(builder_, level_ + 1);
    if (!result_) result_ = named_definition(builder_, level_ + 1);
    if (!result_) result_ = compound_statement(builder_, level_ + 1);
    if (!result_) result_ = format_definition(builder_, level_ + 1);
    if (!result_) result_ = parseUse(builder_, level_ + 1);
    if (!result_) result_ = parseNo(builder_, level_ + 1);
    if (!result_) result_ = block_compound(builder_, level_ + 1);
    if (!result_) result_ = statement(builder_, level_ + 1);
    if (!result_) result_ = annotation(builder_, level_ + 1);
    if (!result_) result_ = pod_section(builder_, level_ + 1);
    if (!result_) result_ = end_or_data(builder_, level_ + 1);
    if (!result_) result_ = parseBadCharacters(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // <<parseSemicolon>> +
  private static boolean statement_item_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_item_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parseSemicolon(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!parseSemicolon(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "statement_item_0", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // statement_modifier_variant !('{'|'(')
  public static boolean statement_modifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_modifier")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, STATEMENT_MODIFIER, "<statement modifier>");
    result_ = statement_modifier_variant(builder_, level_ + 1);
    result_ = result_ && statement_modifier_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // !('{'|'(')
  private static boolean statement_modifier_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_modifier_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !statement_modifier_1_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // '{'|'('
  private static boolean statement_modifier_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_modifier_1_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, LEFT_BRACE);
    if (!result_) result_ = consumeToken(builder_, LEFT_PAREN);
    return result_;
  }

  /* ********************************************************** */
  // if_statement_modifier
  //     | unless_statement_modifier
  //     | while_statement_modifier
  //     | until_statement_modifier
  //     | for_statement_modifier
  //     | when_statement_modifier
  static boolean statement_modifier_variant(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "statement_modifier_variant")) return false;
    boolean result_;
    result_ = if_statement_modifier(builder_, level_ + 1);
    if (!result_) result_ = unless_statement_modifier(builder_, level_ + 1);
    if (!result_) result_ = while_statement_modifier(builder_, level_ + 1);
    if (!result_) result_ = until_statement_modifier(builder_, level_ + 1);
    if (!result_) result_ = for_statement_modifier(builder_, level_ + 1);
    if (!result_) result_ = when_statement_modifier(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // strict_variable_declaration_wrapper |  undef_expr
  static boolean strict_variable_declaration_argument(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "strict_variable_declaration_argument")) return false;
    boolean result_;
    result_ = strict_variable_declaration_wrapper(builder_, level_ + 1);
    if (!result_) result_ = undef_expr(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // variable_declaration_element !('{' | '[' | '->' )
  static boolean strict_variable_declaration_wrapper(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "strict_variable_declaration_wrapper")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = variable_declaration_element(builder_, level_ + 1);
    result_ = result_ && strict_variable_declaration_wrapper_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !('{' | '[' | '->' )
  private static boolean strict_variable_declaration_wrapper_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "strict_variable_declaration_wrapper_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !strict_variable_declaration_wrapper_1_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // '{' | '[' | '->'
  private static boolean strict_variable_declaration_wrapper_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "strict_variable_declaration_wrapper_1_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, LEFT_BRACE);
    if (!result_) result_ = consumeToken(builder_, LEFT_BRACKET);
    if (!result_) result_ = consumeToken(builder_, OPERATOR_DEREFERENCE);
    return result_;
  }

  /* ********************************************************** */
  // 'qw' qw_string
  public static boolean string_list(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "string_list")) return false;
    if (!nextTokenIs(builder_, RESERVED_QW)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, STRING_LIST, null);
    result_ = consumeToken(builder_, RESERVED_QW);
    pinned_ = result_; // pin = 1
    result_ = result_ && qw_string(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // <<attributes <<parse_sub_attributes>>>>
  static boolean sub_attributes(PsiBuilder builder_, int level_) {
    return attributes(builder_, level_ + 1, sub_attributes_0_0_parser_);
  }

  /* ********************************************************** */
  // ['my'|'our'|'state'] 'sub' sub_names_token sub_declaration_parameters <<statementSemi>>
  public static boolean sub_declaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_declaration")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SUB_DECLARATION, "<sub declaration>");
    result_ = sub_declaration_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RESERVED_SUB);
    result_ = result_ && sub_names_token(builder_, level_ + 1);
    result_ = result_ && sub_declaration_parameters(builder_, level_ + 1);
    result_ = result_ && statementSemi(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ['my'|'our'|'state']
  private static boolean sub_declaration_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_declaration_0")) return false;
    sub_declaration_0_0(builder_, level_ + 1);
    return true;
  }

  // 'my'|'our'|'state'
  private static boolean sub_declaration_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_declaration_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, RESERVED_MY);
    if (!result_) result_ = consumeToken(builder_, RESERVED_OUR);
    if (!result_) result_ = consumeToken(builder_, RESERVED_STATE);
    return result_;
  }

  /* ********************************************************** */
  // sub_definition_parameters
  static boolean sub_declaration_parameters(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_declaration_parameters")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = sub_definition_parameters(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PerlParserGenerated::recover_statement);
    return result_;
  }

  /* ********************************************************** */
  // ['my'|'our'|'state'|'async'] 'sub' sub_names_token sub_definition_parameters block
  public static boolean sub_definition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_definition")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SUB_DEFINITION, "<sub definition>");
    result_ = sub_definition_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RESERVED_SUB);
    result_ = result_ && sub_names_token(builder_, level_ + 1);
    result_ = result_ && sub_definition_parameters(builder_, level_ + 1);
    result_ = result_ && block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ['my'|'our'|'state'|'async']
  private static boolean sub_definition_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_definition_0")) return false;
    sub_definition_0_0(builder_, level_ + 1);
    return true;
  }

  // 'my'|'our'|'state'|'async'
  private static boolean sub_definition_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_definition_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, RESERVED_MY);
    if (!result_) result_ = consumeToken(builder_, RESERVED_OUR);
    if (!result_) result_ = consumeToken(builder_, RESERVED_STATE);
    if (!result_) result_ = consumeToken(builder_, RESERVED_ASYNC);
    return result_;
  }

  /* ********************************************************** */
  // sub_attributes [sub_signature_in_parens] |
  //   [sub_prototype_or_signature] [sub_attributes]
  static boolean sub_definition_parameters(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_definition_parameters")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = sub_definition_parameters_0(builder_, level_ + 1);
    if (!result_) result_ = sub_definition_parameters_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // sub_attributes [sub_signature_in_parens]
  private static boolean sub_definition_parameters_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_definition_parameters_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = sub_attributes(builder_, level_ + 1);
    result_ = result_ && sub_definition_parameters_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [sub_signature_in_parens]
  private static boolean sub_definition_parameters_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_definition_parameters_0_1")) return false;
    sub_signature_in_parens(builder_, level_ + 1);
    return true;
  }

  // [sub_prototype_or_signature] [sub_attributes]
  private static boolean sub_definition_parameters_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_definition_parameters_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = sub_definition_parameters_1_0(builder_, level_ + 1);
    result_ = result_ && sub_definition_parameters_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [sub_prototype_or_signature]
  private static boolean sub_definition_parameters_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_definition_parameters_1_0")) return false;
    sub_prototype_or_signature(builder_, level_ + 1);
    return true;
  }

  // [sub_attributes]
  private static boolean sub_definition_parameters_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_definition_parameters_1_1")) return false;
    sub_attributes(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // block !('->')
  public static boolean sub_expr_simple(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_expr_simple")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = block(builder_, level_ + 1);
    result_ = result_ && sub_expr_simple_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, SUB_EXPR, result_);
    return result_;
  }

  // !('->')
  private static boolean sub_expr_simple_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_expr_simple_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, OPERATOR_DEREFERENCE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // block
  public static boolean sub_expr_simple_ensured(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_expr_simple_ensured")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = block(builder_, level_ + 1);
    exit_section_(builder_, marker_, SUB_EXPR, result_);
    return result_;
  }

  /* ********************************************************** */
  // ['package::name::'] 'subname'
  static boolean sub_names_token(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_names_token")) return false;
    if (!nextTokenIs(builder_, "", QUALIFYING_PACKAGE, SUB_NAME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = sub_names_token_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SUB_NAME);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // ['package::name::']
  private static boolean sub_names_token_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_names_token_0")) return false;
    consumeToken(builder_, QUALIFYING_PACKAGE);
    return true;
  }

  /* ********************************************************** */
  // SUB_PROTOTYPE_TOKEN*
  static boolean sub_prototype(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_prototype")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, SUB_PROTOTYPE_TOKEN)) break;
      if (!empty_element_parsed_guard_(builder_, "sub_prototype", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // '(' sub_prototype_or_signature_content ')'
  static boolean sub_prototype_or_signature(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_prototype_or_signature")) return false;
    if (!nextTokenIs(builder_, LEFT_PAREN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, sub_prototype_or_signature_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_PAREN) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // sub_signature | sub_prototype
  static boolean sub_prototype_or_signature_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_prototype_or_signature_content")) return false;
    boolean result_;
    result_ = sub_signature(builder_, level_ + 1);
    if (!result_) result_ = sub_prototype(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // <<signature_content parse_sub_signature>>
  static boolean sub_signature(PsiBuilder builder_, int level_) {
    return signature_content(builder_, level_ + 1, PerlParserGenerated::parse_sub_signature);
  }

  /* ********************************************************** */
  // <<signature_element parse_sub_signature_element>>
  static boolean sub_signature_element(PsiBuilder builder_, int level_) {
    return signature_element(builder_, level_ + 1, PerlParserGenerated::parse_sub_signature_element);
  }

  /* ********************************************************** */
  // '$$' | '$@' | '$%'
  public static boolean sub_signature_element_ignore(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_signature_element_ignore")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SUB_SIGNATURE_ELEMENT_IGNORE, "<sub signature element ignore>");
    result_ = consumeToken(builder_, SIGIL_SCALAR);
    if (!result_) result_ = consumeToken(builder_, SIGIL_ARRAY);
    if (!result_) result_ = consumeToken(builder_, SIGIL_HASH);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '(' [sub_signature] ')'
  static boolean sub_signature_in_parens(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_signature_in_parens")) return false;
    if (!nextTokenIs(builder_, LEFT_PAREN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, sub_signature_in_parens_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_PAREN) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [sub_signature]
  private static boolean sub_signature_in_parens_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_signature_in_parens_1")) return false;
    sub_signature(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // 'switch' switch_condition block
  public static boolean switch_compound(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_compound")) return false;
    if (!nextTokenIs(builder_, RESERVED_SWITCH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SWITCH_COMPOUND, null);
    result_ = consumeToken(builder_, RESERVED_SWITCH);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, switch_condition(builder_, level_ + 1));
    result_ = pinned_ && block(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '(' parse_scalar_expr ')'
  public static boolean switch_condition(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_condition")) return false;
    if (!nextTokenIs(builder_, LEFT_PAREN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    result_ = result_ && parse_scalar_expr(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_PAREN);
    exit_section_(builder_, marker_, SWITCH_CONDITION, result_);
    return result_;
  }

  /* ********************************************************** */
  // {qq_string_element | '"-'}+
  static boolean tr_block_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tr_block_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = tr_block_content_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!tr_block_content_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "tr_block_content", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // qq_string_element | '"-'
  private static boolean tr_block_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tr_block_content_0")) return false;
    boolean result_;
    result_ = qq_string_element(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, STRING_SPECIAL_RANGE);
    return result_;
  }

  /* ********************************************************** */
  // '/m' +
  public static boolean tr_modifiers(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tr_modifiers")) return false;
    if (!nextTokenIs(builder_, REGEX_MODIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, REGEX_MODIFIER);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, REGEX_MODIFIER)) break;
      if (!empty_element_parsed_guard_(builder_, "tr_modifiers", pos_)) break;
    }
    exit_section_(builder_, marker_, TR_MODIFIERS, result_);
    return result_;
  }

  /* ********************************************************** */
  // {'r/' | 'r}' 'r{'} [tr_replacementlist] 'r}'
  static boolean tr_replacement(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tr_replacement")) return false;
    if (!nextTokenIs(builder_, "", REGEX_QUOTE, REGEX_QUOTE_CLOSE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = tr_replacement_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, tr_replacement_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, REGEX_QUOTE_CLOSE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'r/' | 'r}' 'r{'
  private static boolean tr_replacement_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tr_replacement_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, REGEX_QUOTE);
    if (!result_) result_ = parseTokens(builder_, 0, REGEX_QUOTE_CLOSE, REGEX_QUOTE_OPEN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [tr_replacementlist]
  private static boolean tr_replacement_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tr_replacement_1")) return false;
    tr_replacementlist(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // [tr_block_content]
  public static boolean tr_replacementlist(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tr_replacementlist")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TR_REPLACEMENTLIST, "<tr replacementlist>");
    tr_block_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // 'r{' [tr_searchlist]
  static boolean tr_search(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tr_search")) return false;
    if (!nextTokenIs(builder_, REGEX_QUOTE_OPEN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, REGEX_QUOTE_OPEN);
    pinned_ = result_; // pin = 1
    result_ = result_ && tr_search_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [tr_searchlist]
  private static boolean tr_search_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tr_search_1")) return false;
    tr_searchlist(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // [tr_block_content]
  public static boolean tr_searchlist(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tr_searchlist")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TR_SEARCHLIST, "<tr searchlist>");
    tr_block_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // 'try' <<x1>>
  public static boolean try_expr(PsiBuilder builder_, int level_, Parser x1) {
    if (!recursion_guard_(builder_, level_, "try_expr")) return false;
    if (!nextTokenIs(builder_, "", RESERVED_TRY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TRY_EXPR, null);
    result_ = consumeToken(builder_, RESERVED_TRY);
    pinned_ = result_; // pin = 1
    result_ = result_ && x1.parse(builder_, level_);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'TryCatch::' <<try_expr block>> [<<catch_expr block>>]
  public static boolean trycatch_compound(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "trycatch_compound")) return false;
    if (!nextTokenIs(builder_, RESERVED_TRYCATCH)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, RESERVED_TRYCATCH);
    result_ = result_ && try_expr(builder_, level_ + 1, PerlParserGenerated::block);
    result_ = result_ && trycatch_compound_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, TRYCATCH_COMPOUND, result_);
    return result_;
  }

  // [<<catch_expr block>>]
  private static boolean trycatch_compound_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "trycatch_compound_2")) return false;
    catch_expr(builder_, level_ + 1, PerlParserGenerated::block);
    return true;
  }

  /* ********************************************************** */
  // any_package ['[' expr ']']
  public static boolean type_constraints(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_constraints")) return false;
    if (!nextTokenIs(builder_, "<type constraints>", PACKAGE, TAG_PACKAGE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TYPE_CONSTRAINTS, "<type constraints>");
    result_ = any_package(builder_, level_ + 1);
    result_ = result_ && type_constraints_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ['[' expr ']']
  private static boolean type_constraints_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_constraints_1")) return false;
    type_constraints_1_0(builder_, level_ + 1);
    return true;
  }

  // '[' expr ']'
  private static boolean type_constraints_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_constraints_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, LEFT_BRACKET);
    result_ = result_ && expr(builder_, level_ + 1, -1);
    result_ = result_ && consumeToken(builder_, RIGHT_BRACKET);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // parenthesised_expr | type_specifier_call
  public static boolean type_specifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_specifier")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TYPE_SPECIFIER, "<type specifier>");
    result_ = parenthesised_expr(builder_, level_ + 1);
    if (!result_) result_ = type_specifier_call(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // type_specifier_method  [!('$$'|'$@'|'$%')unary_call_arguments]
  public static boolean type_specifier_call(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_specifier_call")) return false;
    if (!nextTokenIs(builder_, "<type specifier call>", CUSTOM_UNARY, SUB_NAME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SUB_CALL, "<type specifier call>");
    result_ = type_specifier_method(builder_, level_ + 1);
    result_ = result_ && type_specifier_call_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [!('$$'|'$@'|'$%')unary_call_arguments]
  private static boolean type_specifier_call_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_specifier_call_1")) return false;
    type_specifier_call_1_0(builder_, level_ + 1);
    return true;
  }

  // !('$$'|'$@'|'$%')unary_call_arguments
  private static boolean type_specifier_call_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_specifier_call_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = type_specifier_call_1_0_0(builder_, level_ + 1);
    result_ = result_ && unary_call_arguments(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !('$$'|'$@'|'$%')
  private static boolean type_specifier_call_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_specifier_call_1_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !type_specifier_call_1_0_0_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // '$$'|'$@'|'$%'
  private static boolean type_specifier_call_1_0_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_specifier_call_1_0_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, SIGIL_SCALAR);
    if (!result_) result_ = consumeToken(builder_, SIGIL_ARRAY);
    if (!result_) result_ = consumeToken(builder_, SIGIL_HASH);
    return result_;
  }

  /* ********************************************************** */
  // type_specifier_tokens
  public static boolean type_specifier_method(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_specifier_method")) return false;
    if (!nextTokenIs(builder_, "<type specifier method>", CUSTOM_UNARY, SUB_NAME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, METHOD, "<type specifier method>");
    result_ = type_specifier_tokens(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // 'unary_custom'|'subname'
  static boolean type_specifier_tokens(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "type_specifier_tokens")) return false;
    if (!nextTokenIs(builder_, "", CUSTOM_UNARY, SUB_NAME)) return false;
    boolean result_;
    result_ = consumeToken(builder_, CUSTOM_UNARY);
    if (!result_) result_ = consumeToken(builder_, SUB_NAME);
    return result_;
  }

  /* ********************************************************** */
  // unary_expr
  public static boolean unary_call_arguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_call_arguments")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, CALL_ARGUMENTS, "<unary call arguments>");
    result_ = unary_expr(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // <<parseExpressionLevel 13>>
  static boolean unary_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<expression>");
    result_ = parseExpressionLevel(builder_, level_ + 1, 13);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // <<custom_expr_arguments unary_expr>>
  static boolean unary_expr_arguments(PsiBuilder builder_, int level_) {
    return custom_expr_arguments(builder_, level_ + 1, PerlParserGenerated::unary_expr);
  }

  /* ********************************************************** */
  // 'unary' | 'unary_custom' | '-t'
  public static boolean unary_method(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_method")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, METHOD, "<unary method>");
    result_ = consumeToken(builder_, BUILTIN_UNARY);
    if (!result_) result_ = consumeToken(builder_, CUSTOM_UNARY);
    if (!result_) result_ = consumeToken(builder_, OPERATOR_FILETEST);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // block
  public static boolean unconditional_block(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unconditional_block")) return false;
    if (!nextTokenIs(builder_, LEFT_BRACE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = block(builder_, level_ + 1);
    exit_section_(builder_, marker_, UNCONDITIONAL_BLOCK, result_);
    return result_;
  }

  /* ********************************************************** */
  // deref_expr | variable
  static boolean undef_params(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "undef_params")) return false;
    boolean result_;
    result_ = expr(builder_, level_ + 1, 20);
    if (!result_) result_ = variable(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '\N' unicode_char_body
  public static boolean unicode_char(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unicode_char")) return false;
    if (!nextTokenIs(builder_, STRING_SPECIAL_UNICODE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, UNICODE_CHAR, null);
    result_ = consumeToken(builder_, STRING_SPECIAL_UNICODE);
    pinned_ = result_; // pin = 1
    result_ = result_ && unicode_char_body(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '"{' unicode_char_body_content '}"'
  static boolean unicode_char_body(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unicode_char_body")) return false;
    if (!nextTokenIs(builder_, STRING_SPECIAL_LEFT_BRACE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, STRING_SPECIAL_LEFT_BRACE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, unicode_char_body_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, STRING_SPECIAL_RIGHT_BRACE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // unicode_char_body_numbered | unicode_char_name
  static boolean unicode_char_body_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unicode_char_body_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = unicode_char_body_numbered(builder_, level_ + 1);
    if (!result_) result_ = unicode_char_name(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PerlParserGenerated::recover_string_braces);
    return result_;
  }

  /* ********************************************************** */
  // 'U+' char_code_hex
  static boolean unicode_char_body_numbered(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unicode_char_body_numbered")) return false;
    if (!nextTokenIs(builder_, "<character code with U+ prefix>", STRING_SPECIAL_UNICODE_CODE_PREFIX)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<character code with U+ prefix>");
    result_ = consumeToken(builder_, STRING_SPECIAL_UNICODE_CODE_PREFIX);
    pinned_ = result_; // pin = 1
    result_ = result_ && char_code_hex(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'charname'
  static boolean unicode_char_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unicode_char_name")) return false;
    if (!nextTokenIs(builder_, "<character name>", STRING_CHAR_NAME)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null, "<character name>");
    result_ = consumeToken(builder_, STRING_CHAR_NAME);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // 'unless' conditional_block if_compound_elsif * [if_compound_else]
  public static boolean unless_compound(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unless_compound")) return false;
    if (!nextTokenIs(builder_, RESERVED_UNLESS)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, UNLESS_COMPOUND, null);
    result_ = consumeToken(builder_, RESERVED_UNLESS);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, conditional_block(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, unless_compound_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && unless_compound_3(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // if_compound_elsif *
  private static boolean unless_compound_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unless_compound_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!if_compound_elsif(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "unless_compound_2", pos_)) break;
    }
    return true;
  }

  // [if_compound_else]
  private static boolean unless_compound_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unless_compound_3")) return false;
    if_compound_else(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // 'unless' expr
  public static boolean unless_statement_modifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unless_statement_modifier")) return false;
    if (!nextTokenIs(builder_, "<Postfix unless>", RESERVED_UNLESS)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, UNLESS_STATEMENT_MODIFIER, "<Postfix unless>");
    result_ = consumeToken(builder_, RESERVED_UNLESS);
    pinned_ = result_; // pin = 1
    result_ = result_ && expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'until' parse_conditional_block [[POD] continue_block]
  public static boolean until_compound(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "until_compound")) return false;
    if (!nextTokenIs(builder_, RESERVED_UNTIL)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, UNTIL_COMPOUND, null);
    result_ = consumeToken(builder_, RESERVED_UNTIL);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, parse_conditional_block(builder_, level_ + 1));
    result_ = pinned_ && until_compound_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [[POD] continue_block]
  private static boolean until_compound_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "until_compound_2")) return false;
    until_compound_2_0(builder_, level_ + 1);
    return true;
  }

  // [POD] continue_block
  private static boolean until_compound_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "until_compound_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = until_compound_2_0_0(builder_, level_ + 1);
    result_ = result_ && continue_block(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [POD]
  private static boolean until_compound_2_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "until_compound_2_0_0")) return false;
    consumeToken(builder_, POD);
    return true;
  }

  /* ********************************************************** */
  // 'until' expr
  public static boolean until_statement_modifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "until_statement_modifier")) return false;
    if (!nextTokenIs(builder_, "<Postfix until>", RESERVED_UNTIL)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, UNTIL_STATEMENT_MODIFIER, "<Postfix until>");
    result_ = consumeToken(builder_, RESERVED_UNTIL);
    pinned_ = result_; // pin = 1
    result_ = result_ && expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // any_package [perl_version [comma]] [expr]
  static boolean use_module_parameters(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "use_module_parameters")) return false;
    if (!nextTokenIs(builder_, "", PACKAGE, TAG_PACKAGE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = any_package(builder_, level_ + 1);
    result_ = result_ && use_module_parameters_1(builder_, level_ + 1);
    result_ = result_ && use_module_parameters_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [perl_version [comma]]
  private static boolean use_module_parameters_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "use_module_parameters_1")) return false;
    use_module_parameters_1_0(builder_, level_ + 1);
    return true;
  }

  // perl_version [comma]
  private static boolean use_module_parameters_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "use_module_parameters_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = perl_version(builder_, level_ + 1);
    result_ = result_ && use_module_parameters_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [comma]
  private static boolean use_module_parameters_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "use_module_parameters_1_0_1")) return false;
    comma(builder_, level_ + 1);
    return true;
  }

  // [expr]
  private static boolean use_module_parameters_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "use_module_parameters_2")) return false;
    expr(builder_, level_ + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // use_module_parameters | use_version_parameters
  static boolean use_no_parameters(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "use_no_parameters")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = use_module_parameters(builder_, level_ + 1);
    if (!result_) result_ = use_version_parameters(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, PerlParserGenerated::recover_statement);
    return result_;
  }

  /* ********************************************************** */
  // {variable_declaration_element|glob_variable|code_variable}*
  static boolean use_vars_declarations(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "use_vars_declarations")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!use_vars_declarations_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "use_vars_declarations", pos_)) break;
    }
    return true;
  }

  // variable_declaration_element|glob_variable|code_variable
  private static boolean use_vars_declarations_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "use_vars_declarations_0")) return false;
    boolean result_;
    result_ = variable_declaration_element(builder_, level_ + 1);
    if (!result_) result_ = glob_variable(builder_, level_ + 1);
    if (!result_) result_ = code_variable(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // perl_version
  static boolean use_version_parameters(PsiBuilder builder_, int level_) {
    return perl_version(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // <<attributes <<parse_var_attributes>>>>
  static boolean var_attributes(PsiBuilder builder_, int level_) {
    return attributes(builder_, level_ + 1, var_attributes_0_0_parser_);
  }

  /* ********************************************************** */
  // scalar_or_element | array_or_slice | hash_variable | hash_cast_expr | glob_or_element
  static boolean variable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable")) return false;
    boolean result_;
    result_ = scalar_or_element(builder_, level_ + 1);
    if (!result_) result_ = array_or_slice(builder_, level_ + 1);
    if (!result_) result_ = hash_variable(builder_, level_ + 1);
    if (!result_) result_ = hash_cast_expr(builder_, level_ + 1);
    if (!result_) result_ = glob_or_element(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // variable_declaration_global
  //     | variable_declaration_lexical
  //     | variable_declaration_local
  static boolean variable_declaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_declaration")) return false;
    boolean result_;
    result_ = variable_declaration_global(builder_, level_ + 1);
    if (!result_) result_ = variable_declaration_lexical(builder_, level_ + 1);
    if (!result_) result_ = variable_declaration_local(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // variable_declaration_element |  undef_expr
  static boolean variable_declaration_argument(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_declaration_argument")) return false;
    boolean result_;
    result_ = variable_declaration_element(builder_, level_ + 1);
    if (!result_) result_ = undef_expr(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '\\'? lexical_variable
  public static boolean variable_declaration_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_declaration_element")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, VARIABLE_DECLARATION_ELEMENT, "<variable declaration element>");
    result_ = variable_declaration_element_0(builder_, level_ + 1);
    result_ = result_ && lexical_variable(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // '\\'?
  private static boolean variable_declaration_element_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_declaration_element_0")) return false;
    consumeToken(builder_, OPERATOR_REFERENCE);
    return true;
  }

  /* ********************************************************** */
  // variable_parenthesised_declaration | variable_declaration_argument
  static boolean variable_declaration_variation(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_declaration_variation")) return false;
    boolean result_;
    result_ = variable_parenthesised_declaration(builder_, level_ + 1);
    if (!result_) result_ = variable_declaration_argument(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '(' variable_parenthesised_declaration_contents ')'
  static boolean variable_parenthesised_declaration(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_parenthesised_declaration")) return false;
    if (!nextTokenIs(builder_, LEFT_PAREN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, LEFT_PAREN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, variable_parenthesised_declaration_contents(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_PAREN) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // strict_variable_declaration_argument (comma + strict_variable_declaration_argument ) * comma*
  static boolean variable_parenthesised_declaration_contents(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_parenthesised_declaration_contents")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = strict_variable_declaration_argument(builder_, level_ + 1);
    result_ = result_ && variable_parenthesised_declaration_contents_1(builder_, level_ + 1);
    result_ = result_ && variable_parenthesised_declaration_contents_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (comma + strict_variable_declaration_argument ) *
  private static boolean variable_parenthesised_declaration_contents_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_parenthesised_declaration_contents_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!variable_parenthesised_declaration_contents_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "variable_parenthesised_declaration_contents_1", pos_)) break;
    }
    return true;
  }

  // comma + strict_variable_declaration_argument
  private static boolean variable_parenthesised_declaration_contents_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_parenthesised_declaration_contents_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = variable_parenthesised_declaration_contents_1_0_0(builder_, level_ + 1);
    result_ = result_ && strict_variable_declaration_argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // comma +
  private static boolean variable_parenthesised_declaration_contents_1_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_parenthesised_declaration_contents_1_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = comma(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!comma(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "variable_parenthesised_declaration_contents_1_0_0", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // comma*
  private static boolean variable_parenthesised_declaration_contents_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_parenthesised_declaration_contents_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!comma(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "variable_parenthesised_declaration_contents_2", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // 'when' parse_conditional_block
  public static boolean when_compound(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "when_compound")) return false;
    if (!nextTokenIs(builder_, RESERVED_WHEN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, WHEN_COMPOUND, null);
    result_ = consumeToken(builder_, RESERVED_WHEN);
    pinned_ = result_; // pin = 1
    result_ = result_ && parse_conditional_block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'when' expr
  public static boolean when_statement_modifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "when_statement_modifier")) return false;
    if (!nextTokenIs(builder_, "<Postfix when>", RESERVED_WHEN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, WHEN_STATEMENT_MODIFIER, "<Postfix when>");
    result_ = consumeToken(builder_, RESERVED_WHEN);
    pinned_ = result_; // pin = 1
    result_ = result_ && expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // expr
  static boolean where_clause(PsiBuilder builder_, int level_) {
    return expr(builder_, level_ + 1, -1);
  }

  /* ********************************************************** */
  // 'while' parse_conditional_block [[POD] continue_block]
  public static boolean while_compound(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "while_compound")) return false;
    if (!nextTokenIs(builder_, RESERVED_WHILE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, WHILE_COMPOUND, null);
    result_ = consumeToken(builder_, RESERVED_WHILE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, parse_conditional_block(builder_, level_ + 1));
    result_ = pinned_ && while_compound_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [[POD] continue_block]
  private static boolean while_compound_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "while_compound_2")) return false;
    while_compound_2_0(builder_, level_ + 1);
    return true;
  }

  // [POD] continue_block
  private static boolean while_compound_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "while_compound_2_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = while_compound_2_0_0(builder_, level_ + 1);
    result_ = result_ && continue_block(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [POD]
  private static boolean while_compound_2_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "while_compound_2_0_0")) return false;
    consumeToken(builder_, POD);
    return true;
  }

  /* ********************************************************** */
  // 'while' expr
  public static boolean while_statement_modifier(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "while_statement_modifier")) return false;
    if (!nextTokenIs(builder_, "<Postfix while>", RESERVED_WHILE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, WHILE_STATEMENT_MODIFIER, "<Postfix while>");
    result_ = consumeToken(builder_, RESERVED_WHILE);
    pinned_ = result_; // pin = 1
    result_ = result_ && expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // Expression root: expr
  // Operator priority table:
  // 0: N_ARY(lp_or_xor_expr)
  // 1: N_ARY(lp_and_expr)
  // 2: PREFIX(lp_not_expr)
  // 3: POSTFIX(comma_sequence_expr)
  // 4: N_ARY(assign_expr) ATOM(last_expr) ATOM(next_expr) ATOM(goto_expr)
  //    ATOM(redo_expr)
  // 5: POSTFIX(ternary_expr)
  // 6: BINARY(flipflop_expr)
  // 7: N_ARY(or_expr)
  // 8: N_ARY(and_expr)
  // 9: N_ARY(bitwise_or_xor_expr)
  // 10: N_ARY(bitwise_and_expr)
  // 11: BINARY(isa_expr)
  // 12: N_ARY(equal_expr)
  // 13: N_ARY(compare_expr)
  // 14: N_ARY(shift_expr)
  // 15: N_ARY(add_expr)
  // 16: N_ARY(mul_expr)
  // 17: BINARY(regex_expr)
  // 18: PREFIX(ref_expr) PREFIX(prefix_unary_expr)
  // 19: N_ARY(pow_expr)
  // 20: PREFIX(pref_pp_expr) POSTFIX(suff_pp_expr)
  // 21: POSTFIX(deref_expr)
  // 22: ATOM(composite_atom_expr) ATOM(string_bare) ATOM(string_sq) ATOM(string_dq)
  //    ATOM(string_xq) ATOM(heredoc_opener) ATOM(number_constant) ATOM(variable_declaration_lexical)
  //    ATOM(match_regex) ATOM(return_expr) ATOM(scalar_expr) ATOM(keys_expr)
  //    ATOM(values_expr) ATOM(each_expr) ATOM(defined_expr) ATOM(delete_expr)
  //    ATOM(splice_expr) ATOM(bless_expr) ATOM(array_shift_expr) ATOM(array_unshift_expr)
  //    ATOM(array_push_expr) ATOM(array_pop_expr) ATOM(wantarray_expr) ATOM(exit_expr)
  //    ATOM(array_index_variable) ATOM(scalar_index_cast_expr) ATOM(anon_array) ATOM(undef_expr)
  //    ATOM(print_expr) ATOM(replacement_regex) ATOM(sub_expr) ATOM(fun_expr)
  //    ATOM(method_expr) ATOM(eval_expr) ATOM(do_block_expr) ATOM(do_expr)
  //    ATOM(anon_hash) ATOM(variable_declaration_local) ATOM(sort_expr) ATOM(grep_expr)
  //    ATOM(map_expr) ATOM(continue_expr) ATOM(tag_scalar) ATOM(variable_declaration_global)
  //    ATOM(compile_regex) ATOM(tr_regex) ATOM(file_read_expr) ATOM(file_glob_expr)
  //    ATOM(require_expr) ATOM(perl_handle_expr) ATOM(custom_atom_expr) ATOM(trycatch_expr)
  //    ATOM(sub_call) ATOM(package_expr)
  public static boolean expr(PsiBuilder builder_, int level_, int priority_) {
    if (!recursion_guard_(builder_, level_, "expr")) return false;
    addVariant(builder_, "<expr>");
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, "<expr>");
    result_ = lp_not_expr(builder_, level_ + 1);
    if (!result_) result_ = last_expr(builder_, level_ + 1);
    if (!result_) result_ = next_expr(builder_, level_ + 1);
    if (!result_) result_ = goto_expr(builder_, level_ + 1);
    if (!result_) result_ = redo_expr(builder_, level_ + 1);
    if (!result_) result_ = ref_expr(builder_, level_ + 1);
    if (!result_) result_ = prefix_unary_expr(builder_, level_ + 1);
    if (!result_) result_ = pref_pp_expr(builder_, level_ + 1);
    if (!result_) result_ = composite_atom_expr(builder_, level_ + 1);
    if (!result_) result_ = string_bare(builder_, level_ + 1);
    if (!result_) result_ = string_sq(builder_, level_ + 1);
    if (!result_) result_ = string_dq(builder_, level_ + 1);
    if (!result_) result_ = string_xq(builder_, level_ + 1);
    if (!result_) result_ = heredoc_opener(builder_, level_ + 1);
    if (!result_) result_ = number_constant(builder_, level_ + 1);
    if (!result_) result_ = variable_declaration_lexical(builder_, level_ + 1);
    if (!result_) result_ = match_regex(builder_, level_ + 1);
    if (!result_) result_ = return_expr(builder_, level_ + 1);
    if (!result_) result_ = scalar_expr(builder_, level_ + 1);
    if (!result_) result_ = keys_expr(builder_, level_ + 1);
    if (!result_) result_ = values_expr(builder_, level_ + 1);
    if (!result_) result_ = each_expr(builder_, level_ + 1);
    if (!result_) result_ = defined_expr(builder_, level_ + 1);
    if (!result_) result_ = delete_expr(builder_, level_ + 1);
    if (!result_) result_ = splice_expr(builder_, level_ + 1);
    if (!result_) result_ = bless_expr(builder_, level_ + 1);
    if (!result_) result_ = array_shift_expr(builder_, level_ + 1);
    if (!result_) result_ = array_unshift_expr(builder_, level_ + 1);
    if (!result_) result_ = array_push_expr(builder_, level_ + 1);
    if (!result_) result_ = array_pop_expr(builder_, level_ + 1);
    if (!result_) result_ = wantarray_expr(builder_, level_ + 1);
    if (!result_) result_ = exit_expr(builder_, level_ + 1);
    if (!result_) result_ = array_index_variable(builder_, level_ + 1);
    if (!result_) result_ = scalar_index_cast_expr(builder_, level_ + 1);
    if (!result_) result_ = anon_array(builder_, level_ + 1);
    if (!result_) result_ = undef_expr(builder_, level_ + 1);
    if (!result_) result_ = print_expr(builder_, level_ + 1);
    if (!result_) result_ = replacement_regex(builder_, level_ + 1);
    if (!result_) result_ = sub_expr(builder_, level_ + 1);
    if (!result_) result_ = fun_expr(builder_, level_ + 1);
    if (!result_) result_ = method_expr(builder_, level_ + 1);
    if (!result_) result_ = eval_expr(builder_, level_ + 1);
    if (!result_) result_ = do_block_expr(builder_, level_ + 1);
    if (!result_) result_ = do_expr(builder_, level_ + 1);
    if (!result_) result_ = anon_hash(builder_, level_ + 1);
    if (!result_) result_ = variable_declaration_local(builder_, level_ + 1);
    if (!result_) result_ = sort_expr(builder_, level_ + 1);
    if (!result_) result_ = grep_expr(builder_, level_ + 1);
    if (!result_) result_ = map_expr(builder_, level_ + 1);
    if (!result_) result_ = continue_expr(builder_, level_ + 1);
    if (!result_) result_ = tag_scalar(builder_, level_ + 1);
    if (!result_) result_ = variable_declaration_global(builder_, level_ + 1);
    if (!result_) result_ = compile_regex(builder_, level_ + 1);
    if (!result_) result_ = tr_regex(builder_, level_ + 1);
    if (!result_) result_ = file_read_expr(builder_, level_ + 1);
    if (!result_) result_ = file_glob_expr(builder_, level_ + 1);
    if (!result_) result_ = require_expr(builder_, level_ + 1);
    if (!result_) result_ = perl_handle_expr(builder_, level_ + 1);
    if (!result_) result_ = custom_atom_expr(builder_, level_ + 1);
    if (!result_) result_ = trycatch_expr(builder_, level_ + 1);
    if (!result_) result_ = sub_call(builder_, level_ + 1);
    if (!result_) result_ = package_expr(builder_, level_ + 1);
    pinned_ = result_;
    result_ = result_ && expr_0(builder_, level_ + 1, priority_);
    exit_section_(builder_, level_, marker_, null, result_, pinned_, null);
    return result_ || pinned_;
  }

  public static boolean expr_0(PsiBuilder builder_, int level_, int priority_) {
    if (!recursion_guard_(builder_, level_, "expr_0")) return false;
    boolean result_ = true;
    while (true) {
      Marker marker_ = enter_section_(builder_, level_, _LEFT_, null);
      if (priority_ < 0 && lp_or_xor_expr_0(builder_, level_ + 1)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 0));
          if (!lp_or_xor_expr_0(builder_, level_ + 1)) break;
        }
        exit_section_(builder_, level_, marker_, LP_OR_XOR_EXPR, result_, true, null);
      }
      else if (priority_ < 1 && consumeTokenSmart(builder_, OPERATOR_AND_LP)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 1));
          if (!consumeTokenSmart(builder_, OPERATOR_AND_LP)) break;
        }
        exit_section_(builder_, level_, marker_, LP_AND_EXPR, result_, true, null);
      }
      else if (priority_ < 3 && comma_sequence_expr_0(builder_, level_ + 1)) {
        result_ = true;
        exit_section_(builder_, level_, marker_, COMMA_SEQUENCE_EXPR, result_, true, null);
      }
      else if (priority_ < 4 && assign_expr_0(builder_, level_ + 1)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 4));
          if (!assign_expr_0(builder_, level_ + 1)) break;
        }
        exit_section_(builder_, level_, marker_, ASSIGN_EXPR, result_, true, null);
      }
      else if (priority_ < 5 && ternary_expr_0(builder_, level_ + 1)) {
        result_ = true;
        exit_section_(builder_, level_, marker_, TERNARY_EXPR, result_, true, null);
      }
      else if (priority_ < 6 && flipflop_expr_0(builder_, level_ + 1)) {
        result_ = expr(builder_, level_, 6);
        exit_section_(builder_, level_, marker_, FLIPFLOP_EXPR, result_, true, null);
      }
      else if (priority_ < 7 && or_expr_0(builder_, level_ + 1)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 7));
          if (!or_expr_0(builder_, level_ + 1)) break;
        }
        exit_section_(builder_, level_, marker_, OR_EXPR, result_, true, null);
      }
      else if (priority_ < 8 && consumeTokenSmart(builder_, OPERATOR_AND)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 8));
          if (!consumeTokenSmart(builder_, OPERATOR_AND)) break;
        }
        exit_section_(builder_, level_, marker_, AND_EXPR, result_, true, null);
      }
      else if (priority_ < 9 && bitwise_or_xor_expr_0(builder_, level_ + 1)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 9));
          if (!bitwise_or_xor_expr_0(builder_, level_ + 1)) break;
        }
        exit_section_(builder_, level_, marker_, BITWISE_OR_XOR_EXPR, result_, true, null);
      }
      else if (priority_ < 10 && consumeTokenSmart(builder_, OPERATOR_BITWISE_AND)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 10));
          if (!consumeTokenSmart(builder_, OPERATOR_BITWISE_AND)) break;
        }
        exit_section_(builder_, level_, marker_, BITWISE_AND_EXPR, result_, true, null);
      }
      else if (priority_ < 11 && consumeTokenSmart(builder_, OPERATOR_ISA)) {
        result_ = expr(builder_, level_, 11);
        exit_section_(builder_, level_, marker_, ISA_EXPR, result_, true, null);
      }
      else if (priority_ < 12 && equal_expr_0(builder_, level_ + 1)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 12));
          if (!equal_expr_0(builder_, level_ + 1)) break;
        }
        exit_section_(builder_, level_, marker_, EQUAL_EXPR, result_, true, null);
      }
      else if (priority_ < 13 && compare_expr_0(builder_, level_ + 1)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 13));
          if (!compare_expr_0(builder_, level_ + 1)) break;
        }
        exit_section_(builder_, level_, marker_, COMPARE_EXPR, result_, true, null);
      }
      else if (priority_ < 14 && shift_expr_0(builder_, level_ + 1)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 14));
          if (!shift_expr_0(builder_, level_ + 1)) break;
        }
        exit_section_(builder_, level_, marker_, SHIFT_EXPR, result_, true, null);
      }
      else if (priority_ < 15 && add_expr_0(builder_, level_ + 1)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 15));
          if (!add_expr_0(builder_, level_ + 1)) break;
        }
        exit_section_(builder_, level_, marker_, ADD_EXPR, result_, true, null);
      }
      else if (priority_ < 16 && mul_expr_0(builder_, level_ + 1)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 16));
          if (!mul_expr_0(builder_, level_ + 1)) break;
        }
        exit_section_(builder_, level_, marker_, MUL_EXPR, result_, true, null);
      }
      else if (priority_ < 17 && regex_expr_0(builder_, level_ + 1)) {
        result_ = expr(builder_, level_, 17);
        exit_section_(builder_, level_, marker_, REGEX_EXPR, result_, true, null);
      }
      else if (priority_ < 19 && consumeTokenSmart(builder_, OPERATOR_POW)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 19));
          if (!consumeTokenSmart(builder_, OPERATOR_POW)) break;
        }
        exit_section_(builder_, level_, marker_, POW_EXPR, result_, true, null);
      }
      else if (priority_ < 20 && suff_pp_expr_0(builder_, level_ + 1)) {
        result_ = true;
        exit_section_(builder_, level_, marker_, SUFF_PP_EXPR, result_, true, null);
      }
      else if (priority_ < 21 && deref_expr_0(builder_, level_ + 1)) {
        result_ = true;
        exit_section_(builder_, level_, marker_, DEREF_EXPR, result_, true, null);
      }
      else {
        exit_section_(builder_, level_, marker_, null, false, false, null);
        break;
      }
    }
    return result_;
  }

  // 'or'|'xor'
  private static boolean lp_or_xor_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "lp_or_xor_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, OPERATOR_OR_LP);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_XOR_LP);
    return result_;
  }

  public static boolean lp_not_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "lp_not_expr")) return false;
    if (!nextTokenIsSmart(builder_, OPERATOR_NOT_LP)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null);
    result_ = consumeTokenSmart(builder_, OPERATOR_NOT_LP);
    pinned_ = result_;
    result_ = pinned_ && expr(builder_, level_, 2);
    exit_section_(builder_, level_, marker_, LP_NOT_EXPR, result_, pinned_, null);
    return result_ || pinned_;
  }

  // {comma [parse_scalar_expr]}+
  private static boolean comma_sequence_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "comma_sequence_expr_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = comma_sequence_expr_0_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!comma_sequence_expr_0_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "comma_sequence_expr_0", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // comma [parse_scalar_expr]
  private static boolean comma_sequence_expr_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "comma_sequence_expr_0_0")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = comma(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && comma_sequence_expr_0_0_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [parse_scalar_expr]
  private static boolean comma_sequence_expr_0_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "comma_sequence_expr_0_0_1")) return false;
    parse_scalar_expr(builder_, level_ + 1);
    return true;
  }

  // '**='|'+='|'-='|'*='|'/='|'%='|'.='|'x='|'&='|'|='|'^='|'<<='|'>>='|'&&='|'||='|'//='|'='
  private static boolean assign_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assign_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, OPERATOR_POW_ASSIGN);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_PLUS_ASSIGN);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_MINUS_ASSIGN);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_MUL_ASSIGN);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_DIV_ASSIGN);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_MOD_ASSIGN);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_CONCAT_ASSIGN);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_X_ASSIGN);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_BITWISE_AND_ASSIGN);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_BITWISE_OR_ASSIGN);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_BITWISE_XOR_ASSIGN);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_SHIFT_LEFT_ASSIGN);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_SHIFT_RIGHT_ASSIGN);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_AND_ASSIGN);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_OR_ASSIGN);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_OR_DEFINED_ASSIGN);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_ASSIGN);
    return result_;
  }

  // 'last' [lnr_param]
  public static boolean last_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "last_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_LAST)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LAST_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_LAST);
    pinned_ = result_; // pin = 1
    result_ = result_ && last_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [lnr_param]
  private static boolean last_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "last_expr_1")) return false;
    lnr_param(builder_, level_ + 1);
    return true;
  }

  // 'next' [lnr_param]
  public static boolean next_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "next_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_NEXT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NEXT_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_NEXT);
    pinned_ = result_; // pin = 1
    result_ = result_ && next_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [lnr_param]
  private static boolean next_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "next_expr_1")) return false;
    lnr_param(builder_, level_ + 1);
    return true;
  }

  // 'goto' [goto_param]
  public static boolean goto_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "goto_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_GOTO)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, GOTO_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_GOTO);
    pinned_ = result_; // pin = 1
    result_ = result_ && goto_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [goto_param]
  private static boolean goto_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "goto_expr_1")) return false;
    goto_param(builder_, level_ + 1);
    return true;
  }

  // 'redo' [lnr_param]
  public static boolean redo_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "redo_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_REDO)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, REDO_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_REDO);
    pinned_ = result_; // pin = 1
    result_ = result_ && redo_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [lnr_param]
  private static boolean redo_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "redo_expr_1")) return false;
    lnr_param(builder_, level_ + 1);
    return true;
  }

  // '?' parse_scalar_expr ':' parse_scalar_expr
  private static boolean ternary_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ternary_expr_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenSmart(builder_, QUESTION);
    result_ = result_ && parse_scalar_expr(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, COLON);
    result_ = result_ && parse_scalar_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // '..'|'...'
  private static boolean flipflop_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "flipflop_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, OPERATOR_FLIP_FLOP);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_HELLIP);
    return result_;
  }

  // '||'|'//'
  private static boolean or_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "or_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, OPERATOR_OR);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_OR_DEFINED);
    return result_;
  }

  // '|'|'^'
  private static boolean bitwise_or_xor_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bitwise_or_xor_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, OPERATOR_BITWISE_OR);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_BITWISE_XOR);
    return result_;
  }

  // '<=>'|'cmp'|'~~'|'=='|'!='|'eq'|'ne'
  private static boolean equal_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "equal_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, OPERATOR_CMP_NUMERIC);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_CMP_STR);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_SMARTMATCH);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_EQ_NUMERIC);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_NE_NUMERIC);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_EQ_STR);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_NE_STR);
    return result_;
  }

  // '>='|'<='|'>'|'<'|'ge'|'le'|'gt'|'lt'
  private static boolean compare_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "compare_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, OPERATOR_GE_NUMERIC);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_LE_NUMERIC);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_GT_NUMERIC);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_LT_NUMERIC);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_GE_STR);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_LE_STR);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_GT_STR);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_LT_STR);
    return result_;
  }

  // '<<'|'>>'
  private static boolean shift_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "shift_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, OPERATOR_SHIFT_LEFT);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_SHIFT_RIGHT);
    return result_;
  }

  // '+'|'-'|'.'
  private static boolean add_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "add_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, OPERATOR_PLUS);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_MINUS);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_CONCAT);
    return result_;
  }

  // '*'|'/'|'%'|'x'
  private static boolean mul_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "mul_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, OPERATOR_MUL);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_DIV);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_MOD);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_X);
    return result_;
  }

  // '=~'|'!~'
  private static boolean regex_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "regex_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, OPERATOR_RE);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_NOT_RE);
    return result_;
  }

  public static boolean ref_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ref_expr")) return false;
    if (!nextTokenIsSmart(builder_, OPERATOR_REFERENCE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null);
    result_ = consumeTokenSmart(builder_, OPERATOR_REFERENCE);
    pinned_ = result_;
    result_ = pinned_ && expr(builder_, level_, 18);
    exit_section_(builder_, level_, marker_, REF_EXPR, result_, pinned_, null);
    return result_ || pinned_;
  }

  public static boolean prefix_unary_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "prefix_unary_expr")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null);
    result_ = prefix_unary_expr_0(builder_, level_ + 1);
    pinned_ = result_;
    result_ = pinned_ && expr(builder_, level_, 18);
    exit_section_(builder_, level_, marker_, PREFIX_UNARY_EXPR, result_, pinned_, null);
    return result_ || pinned_;
  }

  // '~' | '!' | '+' | '-'
  private static boolean prefix_unary_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "prefix_unary_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, OPERATOR_BITWISE_NOT);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_NOT);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_PLUS);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_MINUS);
    return result_;
  }

  public static boolean pref_pp_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pref_pp_expr")) return false;
    if (!nextTokenIsSmart(builder_, OPERATOR_MINUS_MINUS, OPERATOR_PLUS_PLUS)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null);
    result_ = pref_pp_expr_0(builder_, level_ + 1);
    pinned_ = result_;
    result_ = pinned_ && expr(builder_, level_, 20);
    exit_section_(builder_, level_, marker_, PREF_PP_EXPR, result_, pinned_, null);
    return result_ || pinned_;
  }

  // '++'|'--'
  private static boolean pref_pp_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pref_pp_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, OPERATOR_PLUS_PLUS);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_MINUS_MINUS);
    return result_;
  }

  // '++'|'--'
  private static boolean suff_pp_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "suff_pp_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, OPERATOR_PLUS_PLUS);
    if (!result_) result_ = consumeTokenSmart(builder_, OPERATOR_MINUS_MINUS);
    return result_;
  }

  // (<<parseArrowSmart>> nested_element_variation) +
  private static boolean deref_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "deref_expr_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = deref_expr_0_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!deref_expr_0_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "deref_expr_0", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // <<parseArrowSmart>> nested_element_variation
  private static boolean deref_expr_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "deref_expr_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parseArrowSmart(builder_, level_ + 1);
    result_ = result_ && nested_element_variation(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // scalar_or_element
  //     | parenthesised_expr [array_element]
  //     | array_or_slice
  //     | hash_or_slice
  //     | glob_or_element
  //     | code_primitive
  public static boolean composite_atom_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "composite_atom_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, COMPOSITE_ATOM_EXPR, "<expression>");
    result_ = scalar_or_element(builder_, level_ + 1);
    if (!result_) result_ = composite_atom_expr_1(builder_, level_ + 1);
    if (!result_) result_ = array_or_slice(builder_, level_ + 1);
    if (!result_) result_ = hash_or_slice(builder_, level_ + 1);
    if (!result_) result_ = glob_or_element(builder_, level_ + 1);
    if (!result_) result_ = code_primitive(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // parenthesised_expr [array_element]
  private static boolean composite_atom_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "composite_atom_expr_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parenthesised_expr(builder_, level_ + 1);
    result_ = result_ && composite_atom_expr_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [array_element]
  private static boolean composite_atom_expr_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "composite_atom_expr_1_1")) return false;
    array_element(builder_, level_ + 1);
    return true;
  }

  // &(STRING_CONTENT|'\\"')<<parseBareString>>
  public static boolean string_bare(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "string_bare")) return false;
    if (!nextTokenIsSmart(builder_, STRING_CONTENT, STRING_SPECIAL_ESCAPE_CHAR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, STRING_BARE, "<string bare>");
    result_ = string_bare_0(builder_, level_ + 1);
    result_ = result_ && parseBareString(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // &(STRING_CONTENT|'\\"')
  private static boolean string_bare_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "string_bare_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = string_bare_0_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // STRING_CONTENT|'\\"'
  private static boolean string_bare_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "string_bare_0_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, STRING_CONTENT);
    if (!result_) result_ = consumeTokenSmart(builder_, STRING_SPECIAL_ESCAPE_CHAR);
    return result_;
  }

  // [ 'q'] quoted_sq_string
  public static boolean string_sq(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "string_sq")) return false;
    if (!nextTokenIsSmart(builder_, QUOTE_SINGLE_OPEN, RESERVED_Q)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, STRING_SQ, "<string sq>");
    result_ = string_sq_0(builder_, level_ + 1);
    result_ = result_ && quoted_sq_string(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [ 'q']
  private static boolean string_sq_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "string_sq_0")) return false;
    consumeTokenSmart(builder_, RESERVED_Q);
    return true;
  }

  // [ 'qq'] quoted_qq_string
  public static boolean string_dq(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "string_dq")) return false;
    if (!nextTokenIsSmart(builder_, QUOTE_DOUBLE_OPEN, RESERVED_QQ)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, STRING_DQ, "<string dq>");
    result_ = string_dq_0(builder_, level_ + 1);
    result_ = result_ && quoted_qq_string(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [ 'qq']
  private static boolean string_dq_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "string_dq_0")) return false;
    consumeTokenSmart(builder_, RESERVED_QQ);
    return true;
  }

  // [ 'qx'] quoted_xq_string
  public static boolean string_xq(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "string_xq")) return false;
    if (!nextTokenIsSmart(builder_, QUOTE_TICK_OPEN, RESERVED_QX)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, STRING_XQ, "<string xq>");
    result_ = string_xq_0(builder_, level_ + 1);
    result_ = result_ && quoted_xq_string(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [ 'qx']
  private static boolean string_xq_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "string_xq_0")) return false;
    consumeTokenSmart(builder_, RESERVED_QX);
    return true;
  }

  // 'heredoc<<' ( '\\' string_bare | string )
  public static boolean heredoc_opener(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "heredoc_opener")) return false;
    if (!nextTokenIsSmart(builder_, OPERATOR_HEREDOC)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HEREDOC_OPENER, null);
    result_ = consumeTokenSmart(builder_, OPERATOR_HEREDOC);
    pinned_ = result_; // pin = 1
    result_ = result_ && heredoc_opener_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // '\\' string_bare | string
  private static boolean heredoc_opener_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "heredoc_opener_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = heredoc_opener_1_0(builder_, level_ + 1);
    if (!result_) result_ = expr(builder_, level_ + 1, 21);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // '\\' string_bare
  private static boolean heredoc_opener_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "heredoc_opener_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenSmart(builder_, OPERATOR_REFERENCE);
    result_ = result_ && string_bare(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // NUMBER | NUMBER_VERSION | NUMBER_HEX | NUMBER_OCT | NUMBER_BIN
  public static boolean number_constant(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "number_constant")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NUMBER_CONSTANT, "<number constant>");
    result_ = consumeTokenSmart(builder_, NUMBER);
    if (!result_) result_ = consumeTokenSmart(builder_, NUMBER_VERSION);
    if (!result_) result_ = consumeTokenSmart(builder_, NUMBER_HEX);
    if (!result_) result_ = consumeTokenSmart(builder_, NUMBER_OCT);
    if (!result_) result_ = consumeTokenSmart(builder_, NUMBER_BIN);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // normal_lexical_declaration | field_lexical_declaration
  public static boolean variable_declaration_lexical(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_declaration_lexical")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, VARIABLE_DECLARATION_LEXICAL, "<variable declaration lexical>");
    result_ = normal_lexical_declaration(builder_, level_ + 1);
    if (!result_) result_ = field_lexical_declaration(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ['m'] match_regex_body
  public static boolean match_regex(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "match_regex")) return false;
    if (!nextTokenIsSmart(builder_, REGEX_QUOTE_OPEN, RESERVED_M)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MATCH_REGEX, "<match regex>");
    result_ = match_regex_0(builder_, level_ + 1);
    result_ = result_ && match_regex_body(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ['m']
  private static boolean match_regex_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "match_regex_0")) return false;
    consumeTokenSmart(builder_, RESERVED_M);
    return true;
  }

  // 'return' [parse_list_expr]
  public static boolean return_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "return_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_RETURN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, RETURN_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_RETURN);
    pinned_ = result_; // pin = 1
    result_ = result_ && return_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [parse_list_expr]
  private static boolean return_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "return_expr_1")) return false;
    parse_list_expr(builder_, level_ + 1);
    return true;
  }

  // 'scalar' custom_single_expr_argument
  public static boolean scalar_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "scalar_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_SCALAR)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SCALAR_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_SCALAR);
    pinned_ = result_; // pin = 1
    result_ = result_ && custom_single_expr_argument(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'keys' custom_single_expr_argument
  public static boolean keys_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "keys_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_KEYS)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, KEYS_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_KEYS);
    pinned_ = result_; // pin = 1
    result_ = result_ && custom_single_expr_argument(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'values' custom_single_expr_argument
  public static boolean values_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "values_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_VALUES)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, VALUES_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_VALUES);
    pinned_ = result_; // pin = 1
    result_ = result_ && custom_single_expr_argument(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'each' custom_single_expr_argument
  public static boolean each_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "each_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_EACH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EACH_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_EACH);
    pinned_ = result_; // pin = 1
    result_ = result_ && custom_single_expr_argument(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'defined' [optional_unary_expr_arguments]
  public static boolean defined_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "defined_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_DEFINED)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DEFINED_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_DEFINED);
    pinned_ = result_; // pin = 1
    result_ = result_ && defined_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [optional_unary_expr_arguments]
  private static boolean defined_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "defined_expr_1")) return false;
    optional_unary_expr_arguments(builder_, level_ + 1);
    return true;
  }

  // 'delete' unary_expr_arguments
  public static boolean delete_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "delete_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_DELETE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DELETE_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_DELETE);
    pinned_ = result_; // pin = 1
    result_ = result_ && unary_expr_arguments(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'splice' list_expr_arguments
  public static boolean splice_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "splice_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_SPLICE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SPLICE_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_SPLICE);
    pinned_ = result_; // pin = 1
    result_ = result_ && list_expr_arguments(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'bless' list_expr_arguments
  public static boolean bless_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "bless_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_BLESS)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLESS_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_BLESS);
    pinned_ = result_; // pin = 1
    result_ = result_ && list_expr_arguments(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'shift' [any_unary_call_arguments]
  public static boolean array_shift_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_shift_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_SHIFT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARRAY_SHIFT_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_SHIFT);
    pinned_ = result_; // pin = 1
    result_ = result_ && array_shift_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [any_unary_call_arguments]
  private static boolean array_shift_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_shift_expr_1")) return false;
    any_unary_call_arguments(builder_, level_ + 1);
    return true;
  }

  // 'unshift' any_call_arguments
  public static boolean array_unshift_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_unshift_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_UNSHIFT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARRAY_UNSHIFT_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_UNSHIFT);
    pinned_ = result_; // pin = 1
    result_ = result_ && any_call_arguments(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'push' any_call_arguments
  public static boolean array_push_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_push_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_PUSH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARRAY_PUSH_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_PUSH);
    pinned_ = result_; // pin = 1
    result_ = result_ && any_call_arguments(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'pop' [any_unary_call_arguments]
  public static boolean array_pop_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_pop_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_POP)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARRAY_POP_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_POP);
    pinned_ = result_; // pin = 1
    result_ = result_ && array_pop_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [any_unary_call_arguments]
  private static boolean array_pop_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_pop_expr_1")) return false;
    any_unary_call_arguments(builder_, level_ + 1);
    return true;
  }

  // 'wantarray' [parenthesised_call_arguments]
  public static boolean wantarray_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "wantarray_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_WANTARRAY)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, WANTARRAY_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_WANTARRAY);
    pinned_ = result_; // pin = 1
    result_ = result_ && wantarray_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [parenthesised_call_arguments]
  private static boolean wantarray_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "wantarray_expr_1")) return false;
    parenthesised_call_arguments(builder_, level_ + 1);
    return true;
  }

  // 'exit' [optional_scalar_expr_arguments]
  public static boolean exit_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "exit_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_EXIT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXIT_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_EXIT);
    pinned_ = result_; // pin = 1
    result_ = result_ && exit_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [optional_scalar_expr_arguments]
  private static boolean exit_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "exit_expr_1")) return false;
    optional_scalar_expr_arguments(builder_, level_ + 1);
    return true;
  }

  // '$#' {SCALAR_NAME | '${' SCALAR_NAME '$}'}
  public static boolean array_index_variable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_index_variable")) return false;
    if (!nextTokenIsSmart(builder_, SIGIL_SCALAR_INDEX)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARRAY_INDEX_VARIABLE, "<array last index>");
    result_ = consumeTokenSmart(builder_, SIGIL_SCALAR_INDEX);
    result_ = result_ && array_index_variable_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // SCALAR_NAME | '${' SCALAR_NAME '$}'
  private static boolean array_index_variable_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_index_variable_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenSmart(builder_, SCALAR_NAME);
    if (!result_) result_ = parseTokensSmart(builder_, 0, LEFT_BRACE_SCALAR, SCALAR_NAME, RIGHT_BRACE_SCALAR);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // '$#' scalar_cast_target
  public static boolean scalar_index_cast_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "scalar_index_cast_expr")) return false;
    if (!nextTokenIsSmart(builder_, SIGIL_SCALAR_INDEX)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SCALAR_INDEX_CAST_EXPR, "<array last index dereference>");
    result_ = consumeTokenSmart(builder_, SIGIL_SCALAR_INDEX);
    result_ = result_ && scalar_cast_target(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // '[' [expr] ']'
  public static boolean anon_array(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "anon_array")) return false;
    if (!nextTokenIsSmart(builder_, LEFT_BRACKET)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ANON_ARRAY, "<anonymous array>");
    result_ = consumeTokenSmart(builder_, LEFT_BRACKET);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, anon_array_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_BRACKET) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [expr]
  private static boolean anon_array_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "anon_array_1")) return false;
    expr(builder_, level_ + 1, -1);
    return true;
  }

  // 'undef' (undef_params | '(' undef_params ')') ?
  public static boolean undef_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "undef_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_UNDEF)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, UNDEF_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_UNDEF);
    pinned_ = result_; // pin = 1
    result_ = result_ && undef_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // (undef_params | '(' undef_params ')') ?
  private static boolean undef_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "undef_expr_1")) return false;
    undef_expr_1_0(builder_, level_ + 1);
    return true;
  }

  // undef_params | '(' undef_params ')'
  private static boolean undef_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "undef_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = undef_params(builder_, level_ + 1);
    if (!result_) result_ = undef_expr_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // '(' undef_params ')'
  private static boolean undef_expr_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "undef_expr_1_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenSmart(builder_, LEFT_PAREN);
    result_ = result_ && undef_params(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_PAREN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // ('print'|'printf'|'say') ( print_parenthesized_call_arguments | [print_arguments] )
  public static boolean print_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "print_expr")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PRINT_EXPR, "<expression>");
    result_ = print_expr_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && print_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'print'|'printf'|'say'
  private static boolean print_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "print_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, RESERVED_PRINT);
    if (!result_) result_ = consumeTokenSmart(builder_, RESERVED_PRINTF);
    if (!result_) result_ = consumeTokenSmart(builder_, RESERVED_SAY);
    return result_;
  }

  // print_parenthesized_call_arguments | [print_arguments]
  private static boolean print_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "print_expr_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = print_parenthesized_call_arguments(builder_, level_ + 1);
    if (!result_) result_ = print_expr_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [print_arguments]
  private static boolean print_expr_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "print_expr_1_1")) return false;
    print_arguments(builder_, level_ + 1);
    return true;
  }

  // 's'
  //     regex_match
  //     regex_replace
  //     'r}'
  //     [perl_regex_modifiers]
  public static boolean replacement_regex(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "replacement_regex")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_S)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, REPLACEMENT_REGEX, null);
    result_ = consumeTokenSmart(builder_, RESERVED_S);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, regex_match(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, regex_replace(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, consumeToken(builder_, REGEX_QUOTE_CLOSE)) && result_;
    result_ = pinned_ && replacement_regex_4(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [perl_regex_modifiers]
  private static boolean replacement_regex_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "replacement_regex_4")) return false;
    perl_regex_modifiers(builder_, level_ + 1);
    return true;
  }

  // ['async'] 'sub' sub_definition_parameters block
  public static boolean sub_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_ASYNC, RESERVED_SUB)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SUB_EXPR, "<expression>");
    result_ = sub_expr_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RESERVED_SUB);
    result_ = result_ && sub_definition_parameters(builder_, level_ + 1);
    result_ = result_ && block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ['async']
  private static boolean sub_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_expr_0")) return false;
    consumeTokenSmart(builder_, RESERVED_ASYNC);
    return true;
  }

  // 'fun' func_body
  public static boolean fun_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "fun_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_FUN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FUN_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_FUN);
    result_ = result_ && func_body(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // 'fp_method' method_body
  public static boolean method_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "method_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_METHOD_FP)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, METHOD_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_METHOD_FP);
    result_ = result_ && method_body(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // 'eval' [eval_argument]
  public static boolean eval_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "eval_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_EVAL)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EVAL_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_EVAL);
    pinned_ = result_; // pin = 1
    result_ = result_ && eval_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [eval_argument]
  private static boolean eval_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "eval_expr_1")) return false;
    eval_argument(builder_, level_ + 1);
    return true;
  }

  // 'do' block
  public static boolean do_block_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "do_block_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_DO)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DO_BLOCK_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_DO);
    result_ = result_ && block(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // 'do' expr
  public static boolean do_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "do_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_DO)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DO_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_DO);
    pinned_ = result_; // pin = 1
    result_ = result_ && expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // '{' [expr] '}'
  public static boolean anon_hash(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "anon_hash")) return false;
    if (!nextTokenIsSmart(builder_, LEFT_BRACE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ANON_HASH, "<anonymous hash>");
    result_ = consumeTokenSmart(builder_, LEFT_BRACE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, anon_hash_1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, RIGHT_BRACE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [expr]
  private static boolean anon_hash_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "anon_hash_1")) return false;
    expr(builder_, level_ + 1, -1);
    return true;
  }

  // 'local' local_variable_declaration_variation
  public static boolean variable_declaration_local(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_declaration_local")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_LOCAL)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, VARIABLE_DECLARATION_LOCAL, null);
    result_ = consumeTokenSmart(builder_, RESERVED_LOCAL);
    pinned_ = result_; // pin = 1
    result_ = result_ && local_variable_declaration_variation(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'sort' parse_sort_arguments
  public static boolean sort_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sort_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_SORT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SORT_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_SORT);
    pinned_ = result_; // pin = 1
    result_ = result_ && parse_sort_arguments(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'grep' parse_grep_map_arguments
  public static boolean grep_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "grep_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_GREP)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, GREP_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_GREP);
    pinned_ = result_; // pin = 1
    result_ = result_ && parse_grep_map_arguments(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'map' parse_grep_map_arguments
  public static boolean map_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "map_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_MAP)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MAP_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_MAP);
    pinned_ = result_; // pin = 1
    result_ = result_ && parse_grep_map_arguments(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'continue' ['(' ')']
  public static boolean continue_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "continue_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_CONTINUE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CONTINUE_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_CONTINUE);
    pinned_ = result_; // pin = 1
    result_ = result_ && continue_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // ['(' ')']
  private static boolean continue_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "continue_expr_1")) return false;
    parseTokensSmart(builder_, 0, LEFT_PAREN, RIGHT_PAREN);
    return true;
  }

  // TAG
  public static boolean tag_scalar(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tag_scalar")) return false;
    if (!nextTokenIsSmart(builder_, TAG)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenSmart(builder_, TAG);
    exit_section_(builder_, marker_, TAG_SCALAR, result_);
    return result_;
  }

  // 'our' [any_package] variable_declaration_variation [var_attributes]
  public static boolean variable_declaration_global(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_declaration_global")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_OUR)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, VARIABLE_DECLARATION_GLOBAL, null);
    result_ = consumeTokenSmart(builder_, RESERVED_OUR);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, variable_declaration_global_1(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, variable_declaration_variation(builder_, level_ + 1)) && result_;
    result_ = pinned_ && variable_declaration_global_3(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [any_package]
  private static boolean variable_declaration_global_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_declaration_global_1")) return false;
    any_package(builder_, level_ + 1);
    return true;
  }

  // [var_attributes]
  private static boolean variable_declaration_global_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_declaration_global_3")) return false;
    var_attributes(builder_, level_ + 1);
    return true;
  }

  // 'qr' match_regex_body
  public static boolean compile_regex(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "compile_regex")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_QR)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, COMPILE_REGEX, null);
    result_ = consumeTokenSmart(builder_, RESERVED_QR);
    pinned_ = result_; // pin = 1
    result_ = result_ && match_regex_body(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // ('tr'|'y') tr_search tr_replacement [tr_modifiers]
  public static boolean tr_regex(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tr_regex")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_TR, RESERVED_Y)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TR_REGEX, "<tr regex>");
    result_ = tr_regex_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, tr_search(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, tr_replacement(builder_, level_ + 1)) && result_;
    result_ = pinned_ && tr_regex_3(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'tr'|'y'
  private static boolean tr_regex_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tr_regex_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, RESERVED_TR);
    if (!result_) result_ = consumeTokenSmart(builder_, RESERVED_Y);
    return result_;
  }

  // [tr_modifiers]
  private static boolean tr_regex_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tr_regex_3")) return false;
    tr_modifiers(builder_, level_ + 1);
    return true;
  }

  // LEFT_ANGLE [perl_handle_expr|scalar_variable] RIGHT_ANGLE
  public static boolean file_read_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "file_read_expr")) return false;
    if (!nextTokenIsSmart(builder_, LEFT_ANGLE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FILE_READ_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, LEFT_ANGLE);
    result_ = result_ && file_read_expr_1(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_ANGLE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [perl_handle_expr|scalar_variable]
  private static boolean file_read_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "file_read_expr_1")) return false;
    file_read_expr_1_0(builder_, level_ + 1);
    return true;
  }

  // perl_handle_expr|scalar_variable
  private static boolean file_read_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "file_read_expr_1_0")) return false;
    boolean result_;
    result_ = perl_handle_expr(builder_, level_ + 1);
    if (!result_) result_ = scalar_variable(builder_, level_ + 1);
    return result_;
  }

  // LEFT_ANGLE qq_string_content_with_lp RIGHT_ANGLE
  public static boolean file_glob_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "file_glob_expr")) return false;
    if (!nextTokenIsSmart(builder_, LEFT_ANGLE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FILE_GLOB_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, LEFT_ANGLE);
    result_ = result_ && qq_string_content_with_lp(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHT_ANGLE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // 'require' (any_package| perl_version | parse_scalar_expr)
  public static boolean require_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "require_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_REQUIRE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, REQUIRE_EXPR, "<expression>");
    result_ = consumeTokenSmart(builder_, RESERVED_REQUIRE);
    pinned_ = result_; // pin = 1
    result_ = result_ && require_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // any_package| perl_version | parse_scalar_expr
  private static boolean require_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "require_expr_1")) return false;
    boolean result_;
    result_ = any_package(builder_, level_ + 1);
    if (!result_) result_ = perl_version(builder_, level_ + 1);
    if (!result_) result_ = parse_scalar_expr(builder_, level_ + 1);
    return result_;
  }

  // [QUALIFYING_PACKAGE] HANDLE
  public static boolean perl_handle_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "perl_handle_expr")) return false;
    if (!nextTokenIsSmart(builder_, HANDLE, QUALIFYING_PACKAGE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PERL_HANDLE_EXPR, "<expression>");
    result_ = perl_handle_expr_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, HANDLE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [QUALIFYING_PACKAGE]
  private static boolean perl_handle_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "perl_handle_expr_0")) return false;
    consumeTokenSmart(builder_, QUALIFYING_PACKAGE);
    return true;
  }

  // <<parseParserExtensionTerm>>
  public static boolean custom_atom_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "custom_atom_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, CUSTOM_ATOM_EXPR, "<expression>");
    result_ = parseParserExtensionTerm(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // <<try_expr sub_expr_simple_ensured>> (<<catch_expr sub_expr_simple_ensured>>|finally_expr|except_expr|otherwise_expr|continuation_expr)*
  public static boolean trycatch_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "trycatch_expr")) return false;
    if (!nextTokenIsSmart(builder_, RESERVED_TRY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, TRYCATCH_EXPR, "<expression>");
    result_ = try_expr(builder_, level_ + 1, PerlParserGenerated::sub_expr_simple_ensured);
    result_ = result_ && trycatch_expr_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // (<<catch_expr sub_expr_simple_ensured>>|finally_expr|except_expr|otherwise_expr|continuation_expr)*
  private static boolean trycatch_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "trycatch_expr_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!trycatch_expr_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "trycatch_expr_1", pos_)) break;
    }
    return true;
  }

  // <<catch_expr sub_expr_simple_ensured>>|finally_expr|except_expr|otherwise_expr|continuation_expr
  private static boolean trycatch_expr_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "trycatch_expr_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = catch_expr(builder_, level_ + 1, PerlParserGenerated::sub_expr_simple_ensured);
    if (!result_) result_ = finally_expr(builder_, level_ + 1);
    if (!result_) result_ = except_expr(builder_, level_ + 1);
    if (!result_) result_ = otherwise_expr(builder_, level_ + 1);
    if (!result_) result_ = continuation_expr(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // parse_sub_call
  public static boolean sub_call(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sub_call")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, SUB_CALL, "<sub call>");
    result_ = parse_sub_call(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // any_package
  public static boolean package_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "package_expr")) return false;
    if (!nextTokenIsSmart(builder_, PACKAGE, TAG_PACKAGE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PACKAGE_EXPR, "<expression>");
    result_ = any_package(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  static final Parser sub_attributes_0_0_parser_ = (builder_, level_) -> parse_sub_attributes(builder_, level_ + 1);
  static final Parser var_attributes_0_0_parser_ = (builder_, level_) -> parse_var_attributes(builder_, level_ + 1);
}
