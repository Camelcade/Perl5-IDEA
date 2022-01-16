// This is a generated file. Not intended for manual editing.
package com.perl5.lang.tt2.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.perl5.lang.tt2.lexer.TemplateToolkitElementTypesGenerated.*;
import static com.perl5.lang.tt2.parser.TemplateToolkitParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class TemplateToolkitParserGenerated implements PsiParser, LightPsiParser {

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
    return root(builder_, level_ + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(ADD_EXPR, AND_EXPR, ARRAY_EXPR, ASSIGN_EXPR,
      CALL_EXPR, COMPARE_EXPR, DQ_STRING_EXPR, EQUAL_EXPR,
      EXPR, FILTER_ELEMENT_EXPR, HASH_EXPR, IDENTIFIER_EXPR,
      MUL_EXPR, OR_EXPR, PAIR_EXPR, PARENTHESISED_EXPR,
      RANGE_EXPR, SQ_STRING_EXPR, TERM_EXPR, TERNAR_EXPR,
      UNARY_EXPR, VARIABLE_EXPR),
  };

  /* ********************************************************** */
  // identifier_expr TT2_ASSIGN !TT2_CLOSE_TAG TT2_BLOCK
  public static boolean anon_block_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "anon_block_directive")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ANON_BLOCK_DIRECTIVE, "<anon block directive>");
    result_ = identifier_expr(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, TT2_ASSIGN);
    result_ = result_ && anon_block_directive_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, TT2_BLOCK);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // !TT2_CLOSE_TAG
  private static boolean anon_block_directive_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "anon_block_directive_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, TT2_CLOSE_TAG);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // TT2_LEFT_BRACKET array_expr_content TT2_RIGHT_BRACKET
  public static boolean array_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_expr")) return false;
    if (!nextTokenIs(builder_, TT2_LEFT_BRACKET)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ARRAY_EXPR, null);
    result_ = consumeToken(builder_, TT2_LEFT_BRACKET);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, array_expr_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, TT2_RIGHT_BRACKET) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // (expr [TT2_COMMA]) *
  static boolean array_expr_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_expr_content")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    while (true) {
      int pos_ = current_position_(builder_);
      if (!array_expr_content_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "array_expr_content", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, TemplateToolkitParserGenerated::recover_array_content);
    return true;
  }

  // expr [TT2_COMMA]
  private static boolean array_expr_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_expr_content_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = expr(builder_, level_ + 1, -1);
    result_ = result_ && array_expr_content_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [TT2_COMMA]
  private static boolean array_expr_content_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "array_expr_content_0_1")) return false;
    consumeToken(builder_, TT2_COMMA);
    return true;
  }

  /* ********************************************************** */
  // filter_directive |
  // 	wrapper_directive |
  // 	debug_directive |
  // 	next_directive |
  // 	last_directive |
  // 	return_directive |
  // 	stop_directive |
  // 	clear_directive |
  // 	default_directive |
  // 	insert_directive |
  // 	include_directive |
  // 	process_directive |
  // 	call_directive |
  // 	throw_directive |
  // 	set_directive |
  // 	get_directive
  static boolean atom_dirictive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "atom_dirictive")) return false;
    boolean result_;
    result_ = filter_directive(builder_, level_ + 1);
    if (!result_) result_ = wrapper_directive(builder_, level_ + 1);
    if (!result_) result_ = debug_directive(builder_, level_ + 1);
    if (!result_) result_ = next_directive(builder_, level_ + 1);
    if (!result_) result_ = last_directive(builder_, level_ + 1);
    if (!result_) result_ = return_directive(builder_, level_ + 1);
    if (!result_) result_ = stop_directive(builder_, level_ + 1);
    if (!result_) result_ = clear_directive(builder_, level_ + 1);
    if (!result_) result_ = default_directive(builder_, level_ + 1);
    if (!result_) result_ = insert_directive(builder_, level_ + 1);
    if (!result_) result_ = include_directive(builder_, level_ + 1);
    if (!result_) result_ = process_directive(builder_, level_ + 1);
    if (!result_) result_ = call_directive(builder_, level_ + 1);
    if (!result_) result_ = throw_directive(builder_, level_ + 1);
    if (!result_) result_ = set_directive(builder_, level_ + 1);
    if (!result_) result_ = get_directive(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // TT2_BLOCK block_name
  public static boolean block_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_directive")) return false;
    if (!nextTokenIs(builder_, TT2_BLOCK)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLOCK_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_BLOCK);
    pinned_ = result_; // pin = 1
    result_ = result_ && block_name(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // if_directive |
  // 	unless_directive |
  // 	foreach_directive |
  // 	while_directive |
  // 	perl_directive |
  // 	rawperl_directive |
  // 	anon_block_directive |
  // 	try_directive
  static boolean block_directives(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_directives")) return false;
    boolean result_;
    result_ = if_directive(builder_, level_ + 1);
    if (!result_) result_ = unless_directive(builder_, level_ + 1);
    if (!result_) result_ = foreach_directive(builder_, level_ + 1);
    if (!result_) result_ = while_directive(builder_, level_ + 1);
    if (!result_) result_ = perl_directive(builder_, level_ + 1);
    if (!result_) result_ = rawperl_directive(builder_, level_ + 1);
    if (!result_) result_ = anon_block_directive(builder_, level_ + 1);
    if (!result_) result_ = try_directive(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // keyword_or_identifier_term
  public static boolean block_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "block_name")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, BLOCK_NAME, "<block name>");
    result_ = keyword_or_identifier_term(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // call_arguments_
  public static boolean call_arguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_arguments")) return false;
    if (!nextTokenIs(builder_, TT2_LEFT_PAREN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = call_arguments_(builder_, level_ + 1);
    exit_section_(builder_, marker_, CALL_ARGUMENTS, result_);
    return result_;
  }

  /* ********************************************************** */
  // TT2_LEFT_PAREN call_arguments_content ? TT2_RIGHT_PAREN
  static boolean call_arguments_(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_arguments_")) return false;
    if (!nextTokenIs(builder_, TT2_LEFT_PAREN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, TT2_LEFT_PAREN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, call_arguments__1(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, TT2_RIGHT_PAREN) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // call_arguments_content ?
  private static boolean call_arguments__1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_arguments__1")) return false;
    call_arguments_content(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // call_arguments_item (TT2_COMMA call_arguments_item)*
  static boolean call_arguments_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_arguments_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = call_arguments_item(builder_, level_ + 1);
    result_ = result_ && call_arguments_content_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, TemplateToolkitParserGenerated::recover_parenthesised_item);
    return result_;
  }

  // (TT2_COMMA call_arguments_item)*
  private static boolean call_arguments_content_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_arguments_content_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!call_arguments_content_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "call_arguments_content_1", pos_)) break;
    }
    return true;
  }

  // TT2_COMMA call_arguments_item
  private static boolean call_arguments_content_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_arguments_content_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_COMMA);
    result_ = result_ && call_arguments_item(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // pair_expr | expr
  static boolean call_arguments_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_arguments_item")) return false;
    boolean result_;
    result_ = pair_expr(builder_, level_ + 1);
    if (!result_) result_ = expr(builder_, level_ + 1, -1);
    return result_;
  }

  /* ********************************************************** */
  // TT2_CALL expr [directive_postfix]
  public static boolean call_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_directive")) return false;
    if (!nextTokenIs(builder_, TT2_CALL)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CALL_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_CALL);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, expr(builder_, level_ + 1, -1));
    result_ = pinned_ && call_directive_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [directive_postfix]
  private static boolean call_directive_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_directive_2")) return false;
    directive_postfix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // keyword_or_identifier_term call_arguments
  public static boolean call_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "call_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CALL_EXPR, "<call expr>");
    result_ = keyword_or_identifier_term(builder_, level_ + 1);
    result_ = result_ && call_arguments(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // TT2_CASE [TT2_DEFAULT|expr]
  public static boolean case_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "case_directive")) return false;
    if (!nextTokenIs(builder_, TT2_CASE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CASE_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_CASE);
    pinned_ = result_; // pin = 1
    result_ = result_ && case_directive_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [TT2_DEFAULT|expr]
  private static boolean case_directive_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "case_directive_1")) return false;
    case_directive_1_0(builder_, level_ + 1);
    return true;
  }

  // TT2_DEFAULT|expr
  private static boolean case_directive_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "case_directive_1_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, TT2_DEFAULT);
    if (!result_) result_ = expr(builder_, level_ + 1, -1);
    return result_;
  }

  /* ********************************************************** */
  // TT2_CATCH [exception_type]
  public static boolean catch_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_directive")) return false;
    if (!nextTokenIs(builder_, TT2_CATCH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CATCH_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_CATCH);
    pinned_ = result_; // pin = 1
    result_ = result_ && catch_directive_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [exception_type]
  private static boolean catch_directive_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "catch_directive_1")) return false;
    exception_type(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // TT2_CLEAR [directive_postfix]
  public static boolean clear_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "clear_directive")) return false;
    if (!nextTokenIs(builder_, TT2_CLEAR)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CLEAR_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_CLEAR);
    pinned_ = result_; // pin = 1
    result_ = result_ && clear_directive_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [directive_postfix]
  private static boolean clear_directive_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "clear_directive_1")) return false;
    directive_postfix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // <<parseFileAsString>>
  static boolean convert_to_string(PsiBuilder builder_, int level_) {
    return parseFileAsString(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // TT2_DEBUG {TT2_ON|TT2_OFF|debug_format} [directive_postfix]
  public static boolean debug_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "debug_directive")) return false;
    if (!nextTokenIs(builder_, TT2_DEBUG)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DEBUG_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_DEBUG);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, debug_directive_1(builder_, level_ + 1));
    result_ = pinned_ && debug_directive_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // TT2_ON|TT2_OFF|debug_format
  private static boolean debug_directive_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "debug_directive_1")) return false;
    boolean result_;
    result_ = consumeToken(builder_, TT2_ON);
    if (!result_) result_ = consumeToken(builder_, TT2_OFF);
    if (!result_) result_ = debug_format(builder_, level_ + 1);
    return result_;
  }

  // [directive_postfix]
  private static boolean debug_directive_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "debug_directive_2")) return false;
    directive_postfix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // TT2_FORMAT format_elements
  public static boolean debug_format(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "debug_format")) return false;
    if (!nextTokenIs(builder_, TT2_FORMAT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DEBUG_FORMAT, null);
    result_ = consumeToken(builder_, TT2_FORMAT);
    pinned_ = result_; // pin = 1
    result_ = result_ && format_elements(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // TT2_DEFAULT assign_expr + [directive_postfix]
  public static boolean default_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "default_directive")) return false;
    if (!nextTokenIs(builder_, TT2_DEFAULT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DEFAULT_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_DEFAULT);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, default_directive_1(builder_, level_ + 1));
    result_ = pinned_ && default_directive_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // assign_expr +
  private static boolean default_directive_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "default_directive_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = expr(builder_, level_ + 1, -1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!expr(builder_, level_ + 1, -1)) break;
      if (!empty_element_parsed_guard_(builder_, "default_directive_1", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [directive_postfix]
  private static boolean default_directive_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "default_directive_2")) return false;
    directive_postfix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // call_expr |
  // 	variable_expr |
  // 	TT2_NUMBER_SIMPLE |
  // 	keyword_or_identifier_term |
  // 	<<parseKeywordFallback>>
  static boolean deref_part(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "deref_part")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = call_expr(builder_, level_ + 1);
    if (!result_) result_ = variable_expr(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, TT2_NUMBER_SIMPLE);
    if (!result_) result_ = keyword_or_identifier_term(builder_, level_ + 1);
    if (!result_) result_ = parseKeywordFallback(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // block_directive |
  // 	switch_directive |
  // 	use_directive |
  // 	macro_directive |
  // 	tags_directive |
  // 	meta_directive |
  // 	block_directives |
  // 	reply_directives |
  // 	atom_dirictive |
  // 	empty_directive
  static boolean directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "directive")) return false;
    boolean result_;
    result_ = block_directive(builder_, level_ + 1);
    if (!result_) result_ = switch_directive(builder_, level_ + 1);
    if (!result_) result_ = use_directive(builder_, level_ + 1);
    if (!result_) result_ = macro_directive(builder_, level_ + 1);
    if (!result_) result_ = tags_directive(builder_, level_ + 1);
    if (!result_) result_ = meta_directive(builder_, level_ + 1);
    if (!result_) result_ = block_directives(builder_, level_ + 1);
    if (!result_) result_ = reply_directives(builder_, level_ + 1);
    if (!result_) result_ = atom_dirictive(builder_, level_ + 1);
    if (!result_) result_ = empty_directive(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // if_directive |
  // 	unless_directive |
  // 	filters_directives |
  // 	foreach_directive |
  // 	while_directive |
  // 	wrapper_directive
  public static boolean directive_postfix(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "directive_postfix")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DIRECTIVE_POSTFIX, "<directive postfix>");
    result_ = if_directive(builder_, level_ + 1);
    if (!result_) result_ = unless_directive(builder_, level_ + 1);
    if (!result_) result_ = filters_directives(builder_, level_ + 1);
    if (!result_) result_ = foreach_directive(builder_, level_ + 1);
    if (!result_) result_ = while_directive(builder_, level_ + 1);
    if (!result_) result_ = wrapper_directive(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // TT2_STRING_CONTENT*
  static boolean dq_string_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dq_string_content")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    while (true) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, TT2_STRING_CONTENT)) break;
      if (!empty_element_parsed_guard_(builder_, "dq_string_content", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, TemplateToolkitParserGenerated::recover_dq_string);
    return true;
  }

  /* ********************************************************** */
  // TT2_DQ_OPEN dq_string_content TT2_DQ_CLOSE
  public static boolean dq_string_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "dq_string_expr")) return false;
    if (!nextTokenIs(builder_, TT2_DQ_OPEN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, DQ_STRING_EXPR, null);
    result_ = consumeToken(builder_, TT2_DQ_OPEN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, dq_string_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, TT2_DQ_CLOSE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // <<parseBlockComment>> | <<parseDirective directive>>
  static boolean element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "element")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parseBlockComment(builder_, level_ + 1);
    if (!result_) result_ = parseDirective(builder_, level_ + 1, TemplateToolkitParserGenerated::directive);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // TT2_ELSE
  public static boolean else_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "else_directive")) return false;
    if (!nextTokenIs(builder_, TT2_ELSE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_ELSE);
    exit_section_(builder_, marker_, ELSE_DIRECTIVE, result_);
    return result_;
  }

  /* ********************************************************** */
  // TT2_ELSIF expr
  public static boolean elsif_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "elsif_directive")) return false;
    if (!nextTokenIs(builder_, TT2_ELSIF)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ELSIF_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_ELSIF);
    pinned_ = result_; // pin = 1
    result_ = result_ && expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // [TT2_GET]
  public static boolean empty_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "empty_directive")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EMPTY_DIRECTIVE, "<empty directive>");
    consumeToken(builder_, TT2_GET);
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // TT2_END
  public static boolean end_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "end_directive")) return false;
    if (!nextTokenIs(builder_, TT2_END)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_END);
    exit_section_(builder_, marker_, END_DIRECTIVE, result_);
    return result_;
  }

  /* ********************************************************** */
  // {assign_expr|expr}+
  public static boolean exception_args(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "exception_args")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXCEPTION_ARGS, "<exception args>");
    result_ = exception_args_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!exception_args_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "exception_args", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // assign_expr|expr
  private static boolean exception_args_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "exception_args_0")) return false;
    boolean result_;
    result_ = expr(builder_, level_ + 1, -1);
    if (!result_) result_ = expr(builder_, level_ + 1, -1);
    return result_;
  }

  /* ********************************************************** */
  // expr
  public static boolean exception_message(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "exception_message")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXCEPTION_MESSAGE, "<exception message>");
    result_ = expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // variable_strings_or_stringify
  public static boolean exception_type(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "exception_type")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, EXCEPTION_TYPE, "<exception type>");
    result_ = variable_strings_or_stringify(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // variable_expr | assign_expr | identifier_expr
  static boolean filter_arguments(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filter_arguments")) return false;
    boolean result_;
    result_ = variable_expr(builder_, level_ + 1);
    if (!result_) result_ = expr(builder_, level_ + 1, -1);
    if (!result_) result_ = identifier_expr(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // TT2_FILTER filter_arguments [directive_postfix]
  public static boolean filter_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filter_directive")) return false;
    if (!nextTokenIs(builder_, TT2_FILTER)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FILTER_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_FILTER);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, filter_arguments(builder_, level_ + 1));
    result_ = pinned_ && filter_directive_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [directive_postfix]
  private static boolean filter_directive_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filter_directive_2")) return false;
    directive_postfix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // TT2_FILTER filter_arguments
  public static boolean filter_element_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filter_element_expr")) return false;
    if (!nextTokenIs(builder_, TT2_FILTER)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FILTER_ELEMENT_EXPR, null);
    result_ = consumeToken(builder_, TT2_FILTER);
    pinned_ = result_; // pin = 1
    result_ = result_ && filter_arguments(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // filter_element_expr +
  static boolean filters_directives(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "filters_directives")) return false;
    if (!nextTokenIs(builder_, TT2_FILTER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = filter_element_expr(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!filter_element_expr(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "filters_directives", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // TT2_FINAL
  public static boolean final_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "final_directive")) return false;
    if (!nextTokenIs(builder_, TT2_FINAL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_FINAL);
    exit_section_(builder_, marker_, FINAL_DIRECTIVE, result_);
    return result_;
  }

  /* ********************************************************** */
  // TT2_FOREACH [foreach_iterator_] foreach_iterable
  public static boolean foreach_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_directive")) return false;
    if (!nextTokenIs(builder_, TT2_FOREACH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FOREACH_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_FOREACH);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, foreach_directive_1(builder_, level_ + 1));
    result_ = pinned_ && foreach_iterable(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [foreach_iterator_]
  private static boolean foreach_directive_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_directive_1")) return false;
    foreach_iterator_(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // expr
  public static boolean foreach_iterable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_iterable")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FOREACH_ITERABLE, "<foreach iterable>");
    result_ = expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // keyword_or_identifier_term
  public static boolean foreach_iterator(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_iterator")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FOREACH_ITERATOR, "<foreach iterator>");
    result_ = keyword_or_identifier_term(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // foreach_iterator {TT2_ASSIGN !TT2_CLOSE_TAG|TT2_IN}
  static boolean foreach_iterator_(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_iterator_")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = foreach_iterator(builder_, level_ + 1);
    result_ = result_ && foreach_iterator__1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // TT2_ASSIGN !TT2_CLOSE_TAG|TT2_IN
  private static boolean foreach_iterator__1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_iterator__1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = foreach_iterator__1_0(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, TT2_IN);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // TT2_ASSIGN !TT2_CLOSE_TAG
  private static boolean foreach_iterator__1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_iterator__1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_ASSIGN);
    result_ = result_ && foreach_iterator__1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !TT2_CLOSE_TAG
  private static boolean foreach_iterator__1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "foreach_iterator__1_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, TT2_CLOSE_TAG);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // variable_expr |
  // 	identifier_expr |
  // 	dq_string_expr |
  // 	sq_string_expr
  static boolean format_elements(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "format_elements")) return false;
    boolean result_;
    result_ = variable_expr(builder_, level_ + 1);
    if (!result_) result_ = identifier_expr(builder_, level_ + 1);
    if (!result_) result_ = dq_string_expr(builder_, level_ + 1);
    if (!result_) result_ = sq_string_expr(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // [TT2_GET] expr [directive_postfix]
  public static boolean get_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "get_directive")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, GET_DIRECTIVE, "<get directive>");
    result_ = get_directive_0(builder_, level_ + 1);
    result_ = result_ && expr(builder_, level_ + 1, -1);
    result_ = result_ && get_directive_2(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [TT2_GET]
  private static boolean get_directive_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "get_directive_0")) return false;
    consumeToken(builder_, TT2_GET);
    return true;
  }

  // [directive_postfix]
  private static boolean get_directive_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "get_directive_2")) return false;
    directive_postfix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // TT2_LEFT_BRACE hash_expr_content TT2_RIGHT_BRACE
  public static boolean hash_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_expr")) return false;
    if (!nextTokenIs(builder_, TT2_LEFT_BRACE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HASH_EXPR, null);
    result_ = consumeToken(builder_, TT2_LEFT_BRACE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, hash_expr_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, TT2_RIGHT_BRACE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // {hash_expr_item [TT2_COMMA]} *
  static boolean hash_expr_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_expr_content")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!hash_expr_content_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "hash_expr_content", pos_)) break;
    }
    return true;
  }

  // hash_expr_item [TT2_COMMA]
  private static boolean hash_expr_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_expr_content_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = hash_expr_item(builder_, level_ + 1);
    result_ = result_ && hash_expr_content_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [TT2_COMMA]
  private static boolean hash_expr_content_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_expr_content_0_1")) return false;
    consumeToken(builder_, TT2_COMMA);
    return true;
  }

  /* ********************************************************** */
  // pair_expr
  static boolean hash_expr_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "hash_expr_item")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = pair_expr(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, TemplateToolkitParserGenerated::recover_hash_expr_item);
    return result_;
  }

  /* ********************************************************** */
  // identifier_expr_content
  public static boolean identifier_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "identifier_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, IDENTIFIER_EXPR, "<identifier expr>");
    result_ = identifier_expr_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // keyword_or_identifier_term (TT2_PERIOD deref_part)*
  static boolean identifier_expr_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "identifier_expr_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = keyword_or_identifier_term(builder_, level_ + 1);
    result_ = result_ && identifier_expr_content_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (TT2_PERIOD deref_part)*
  private static boolean identifier_expr_content_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "identifier_expr_content_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!identifier_expr_content_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "identifier_expr_content_1", pos_)) break;
    }
    return true;
  }

  // TT2_PERIOD deref_part
  private static boolean identifier_expr_content_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "identifier_expr_content_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_PERIOD);
    result_ = result_ && deref_part(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // TT2_IF expr
  public static boolean if_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "if_directive")) return false;
    if (!nextTokenIs(builder_, TT2_IF)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, IF_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_IF);
    pinned_ = result_; // pin = 1
    result_ = result_ && expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // TT2_INCLUDE variable_strings_or_files assign_expr * [directive_postfix]
  public static boolean include_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "include_directive")) return false;
    if (!nextTokenIs(builder_, TT2_INCLUDE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, INCLUDE_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_INCLUDE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, variable_strings_or_files(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, include_directive_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && include_directive_3(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // assign_expr *
  private static boolean include_directive_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "include_directive_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!expr(builder_, level_ + 1, -1)) break;
      if (!empty_element_parsed_guard_(builder_, "include_directive_2", pos_)) break;
    }
    return true;
  }

  // [directive_postfix]
  private static boolean include_directive_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "include_directive_3")) return false;
    directive_postfix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // TT2_INSERT variable_strings_or_files [directive_postfix]
  public static boolean insert_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "insert_directive")) return false;
    if (!nextTokenIs(builder_, TT2_INSERT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, INSERT_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_INSERT);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, variable_strings_or_files(builder_, level_ + 1));
    result_ = pinned_ && insert_directive_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [directive_postfix]
  private static boolean insert_directive_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "insert_directive_2")) return false;
    directive_postfix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // convert_to_string (TT2_PLUS !TT2_CLOSE_TAG convert_to_string) *
  static boolean insert_files(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "insert_files")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = convert_to_string(builder_, level_ + 1);
    result_ = result_ && insert_files_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (TT2_PLUS !TT2_CLOSE_TAG convert_to_string) *
  private static boolean insert_files_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "insert_files_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!insert_files_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "insert_files_1", pos_)) break;
    }
    return true;
  }

  // TT2_PLUS !TT2_CLOSE_TAG convert_to_string
  private static boolean insert_files_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "insert_files_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_PLUS);
    result_ = result_ && insert_files_1_0_1(builder_, level_ + 1);
    result_ = result_ && convert_to_string(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !TT2_CLOSE_TAG
  private static boolean insert_files_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "insert_files_1_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, TT2_CLOSE_TAG);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // <<parseIdentifier>>
  static boolean keyword_or_identifier_term(PsiBuilder builder_, int level_) {
    return parseIdentifier(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // TT2_LAST [directive_postfix]
  public static boolean last_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "last_directive")) return false;
    if (!nextTokenIs(builder_, TT2_LAST)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LAST_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_LAST);
    pinned_ = result_; // pin = 1
    result_ = result_ && last_directive_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [directive_postfix]
  private static boolean last_directive_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "last_directive_1")) return false;
    directive_postfix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // !(TT2_MACRO) <<parseMacroBody directive>>
  public static boolean macro_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "macro_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MACRO_CONTENT, "<macro content>");
    result_ = macro_content_0(builder_, level_ + 1);
    result_ = result_ && parseMacroBody(builder_, level_ + 1, TemplateToolkitParserGenerated::directive);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // !(TT2_MACRO)
  private static boolean macro_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "macro_content_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, TT2_MACRO);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // TT2_MACRO macro_name macro_content
  public static boolean macro_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "macro_directive")) return false;
    if (!nextTokenIs(builder_, TT2_MACRO)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MACRO_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_MACRO);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, macro_name(builder_, level_ + 1));
    result_ = pinned_ && macro_content(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // keyword_or_identifier_term [call_arguments]
  public static boolean macro_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "macro_name")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MACRO_NAME, "<macro name>");
    result_ = keyword_or_identifier_term(builder_, level_ + 1);
    result_ = result_ && macro_name_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [call_arguments]
  private static boolean macro_name_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "macro_name_1")) return false;
    call_arguments(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // TT2_META pair_expr +
  public static boolean meta_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "meta_directive")) return false;
    if (!nextTokenIs(builder_, TT2_META)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, META_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_META);
    pinned_ = result_; // pin = 1
    result_ = result_ && meta_directive_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // pair_expr +
  private static boolean meta_directive_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "meta_directive_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = pair_expr(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!pair_expr(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "meta_directive_1", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // identifier_expr
  public static boolean module_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "module_name")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, MODULE_NAME, "<module name>");
    result_ = identifier_expr(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // call_arguments_
  public static boolean module_params(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "module_params")) return false;
    if (!nextTokenIs(builder_, TT2_LEFT_PAREN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = call_arguments_(builder_, level_ + 1);
    exit_section_(builder_, marker_, MODULE_PARAMS, result_);
    return result_;
  }

  /* ********************************************************** */
  // TT2_NEXT [directive_postfix]
  public static boolean next_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "next_directive")) return false;
    if (!nextTokenIs(builder_, TT2_NEXT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, NEXT_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_NEXT);
    pinned_ = result_; // pin = 1
    result_ = result_ && next_directive_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [directive_postfix]
  private static boolean next_directive_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "next_directive_1")) return false;
    directive_postfix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // {string_argument | <<parseHashKey keyword_or_identifier_term>>} TT2_ASSIGN !TT2_CLOSE_TAG expr
  public static boolean pair_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pair_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, PAIR_EXPR, "<pair expr>");
    result_ = pair_expr_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, TT2_ASSIGN);
    result_ = result_ && pair_expr_2(builder_, level_ + 1);
    result_ = result_ && expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // string_argument | <<parseHashKey keyword_or_identifier_term>>
  private static boolean pair_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pair_expr_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = string_argument(builder_, level_ + 1);
    if (!result_) result_ = parseHashKey(builder_, level_ + 1, TemplateToolkitParserGenerated::keyword_or_identifier_term);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !TT2_CLOSE_TAG
  private static boolean pair_expr_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pair_expr_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, TT2_CLOSE_TAG);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // TT2_LEFT_PAREN parenthesised_item_content TT2_RIGHT_PAREN
  public static boolean parenthesised_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesised_expr")) return false;
    if (!nextTokenIs(builder_, TT2_LEFT_PAREN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PARENTHESISED_EXPR, null);
    result_ = consumeToken(builder_, TT2_LEFT_PAREN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, parenthesised_item_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, TT2_RIGHT_PAREN) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // expr
  static boolean parenthesised_item_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parenthesised_item_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, false, TemplateToolkitParserGenerated::recover_parenthesised_item);
    return result_;
  }

  /* ********************************************************** */
  // identifier_expr TT2_ASSIGN !TT2_CLOSE_TAG {process_directive|expr}
  static boolean parse_set_element(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_set_element")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = identifier_expr(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, TT2_ASSIGN);
    result_ = result_ && parse_set_element_2(builder_, level_ + 1);
    result_ = result_ && parse_set_element_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !TT2_CLOSE_TAG
  private static boolean parse_set_element_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_set_element_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, TT2_CLOSE_TAG);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // process_directive|expr
  private static boolean parse_set_element_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_set_element_3")) return false;
    boolean result_;
    result_ = process_directive(builder_, level_ + 1);
    if (!result_) result_ = expr(builder_, level_ + 1, -1);
    return result_;
  }

  /* ********************************************************** */
  // TT2_PERL
  public static boolean perl_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "perl_directive")) return false;
    if (!nextTokenIs(builder_, TT2_PERL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_PERL);
    exit_section_(builder_, marker_, PERL_DIRECTIVE, result_);
    return result_;
  }

  /* ********************************************************** */
  // TT2_PROCESS variable_strings_or_files assign_expr * [directive_postfix]
  public static boolean process_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "process_directive")) return false;
    if (!nextTokenIs(builder_, TT2_PROCESS)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, PROCESS_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_PROCESS);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, variable_strings_or_files(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, process_directive_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && process_directive_3(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // assign_expr *
  private static boolean process_directive_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "process_directive_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!expr(builder_, level_ + 1, -1)) break;
      if (!empty_element_parsed_guard_(builder_, "process_directive_2", pos_)) break;
    }
    return true;
  }

  // [directive_postfix]
  private static boolean process_directive_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "process_directive_3")) return false;
    directive_postfix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // TT2_RAWPERL
  public static boolean rawperl_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "rawperl_directive")) return false;
    if (!nextTokenIs(builder_, TT2_RAWPERL)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_RAWPERL);
    exit_section_(builder_, marker_, RAWPERL_DIRECTIVE, result_);
    return result_;
  }

  /* ********************************************************** */
  // !(TT2_RIGHT_BRACKET|TT2_HARD_NEWLINE|[TT2_MINUS]TT2_CLOSE_TAG|TT2_SEMI)
  static boolean recover_array_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_array_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !recover_array_content_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // TT2_RIGHT_BRACKET|TT2_HARD_NEWLINE|[TT2_MINUS]TT2_CLOSE_TAG|TT2_SEMI
  private static boolean recover_array_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_array_content_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_RIGHT_BRACKET);
    if (!result_) result_ = consumeToken(builder_, TT2_HARD_NEWLINE);
    if (!result_) result_ = recover_array_content_0_2(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, TT2_SEMI);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [TT2_MINUS]TT2_CLOSE_TAG
  private static boolean recover_array_content_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_array_content_0_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = recover_array_content_0_2_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, TT2_CLOSE_TAG);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [TT2_MINUS]
  private static boolean recover_array_content_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_array_content_0_2_0")) return false;
    consumeToken(builder_, TT2_MINUS);
    return true;
  }

  /* ********************************************************** */
  // !(TT2_RIGHT_BRACE|TT2_HARD_NEWLINE|[TT2_MINUS]TT2_CLOSE_TAG|TT2_SEMI)
  static boolean recover_braced_variable(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_braced_variable")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !recover_braced_variable_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // TT2_RIGHT_BRACE|TT2_HARD_NEWLINE|[TT2_MINUS]TT2_CLOSE_TAG|TT2_SEMI
  private static boolean recover_braced_variable_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_braced_variable_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_RIGHT_BRACE);
    if (!result_) result_ = consumeToken(builder_, TT2_HARD_NEWLINE);
    if (!result_) result_ = recover_braced_variable_0_2(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, TT2_SEMI);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [TT2_MINUS]TT2_CLOSE_TAG
  private static boolean recover_braced_variable_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_braced_variable_0_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = recover_braced_variable_0_2_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, TT2_CLOSE_TAG);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [TT2_MINUS]
  private static boolean recover_braced_variable_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_braced_variable_0_2_0")) return false;
    consumeToken(builder_, TT2_MINUS);
    return true;
  }

  /* ********************************************************** */
  // !(TT2_DQ_CLOSE|TT2_HARD_NEWLINE|[TT2_MINUS]TT2_CLOSE_TAG|TT2_SEMI)
  static boolean recover_dq_string(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_dq_string")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !recover_dq_string_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // TT2_DQ_CLOSE|TT2_HARD_NEWLINE|[TT2_MINUS]TT2_CLOSE_TAG|TT2_SEMI
  private static boolean recover_dq_string_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_dq_string_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_DQ_CLOSE);
    if (!result_) result_ = consumeToken(builder_, TT2_HARD_NEWLINE);
    if (!result_) result_ = recover_dq_string_0_2(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, TT2_SEMI);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [TT2_MINUS]TT2_CLOSE_TAG
  private static boolean recover_dq_string_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_dq_string_0_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = recover_dq_string_0_2_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, TT2_CLOSE_TAG);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [TT2_MINUS]
  private static boolean recover_dq_string_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_dq_string_0_2_0")) return false;
    consumeToken(builder_, TT2_MINUS);
    return true;
  }

  /* ********************************************************** */
  // !(TT2_DQ_OPEN|TT2_SQ_OPEN|TT2_IDENTIFIER|TT2_FORMAT|TT2_ON|TT2|OFF|TT2_RIGHT_BRACE|TT2_COMMA|TT2_HARD_NEWLINE|[TT2_MINUS]TT2_CLOSE_TAG|TT2_SEMI)
  static boolean recover_hash_expr_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_hash_expr_item")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !recover_hash_expr_item_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // TT2_DQ_OPEN|TT2_SQ_OPEN|TT2_IDENTIFIER|TT2_FORMAT|TT2_ON|TT2|OFF|TT2_RIGHT_BRACE|TT2_COMMA|TT2_HARD_NEWLINE|[TT2_MINUS]TT2_CLOSE_TAG|TT2_SEMI
  private static boolean recover_hash_expr_item_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_hash_expr_item_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_DQ_OPEN);
    if (!result_) result_ = consumeToken(builder_, TT2_SQ_OPEN);
    if (!result_) result_ = consumeToken(builder_, TT2_IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, TT2_FORMAT);
    if (!result_) result_ = consumeToken(builder_, TT2_ON);
    if (!result_) result_ = consumeToken(builder_, TT2);
    if (!result_) result_ = consumeToken(builder_, OFF);
    if (!result_) result_ = consumeToken(builder_, TT2_RIGHT_BRACE);
    if (!result_) result_ = consumeToken(builder_, TT2_COMMA);
    if (!result_) result_ = consumeToken(builder_, TT2_HARD_NEWLINE);
    if (!result_) result_ = recover_hash_expr_item_0_10(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, TT2_SEMI);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [TT2_MINUS]TT2_CLOSE_TAG
  private static boolean recover_hash_expr_item_0_10(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_hash_expr_item_0_10")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = recover_hash_expr_item_0_10_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, TT2_CLOSE_TAG);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [TT2_MINUS]
  private static boolean recover_hash_expr_item_0_10_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_hash_expr_item_0_10_0")) return false;
    consumeToken(builder_, TT2_MINUS);
    return true;
  }

  /* ********************************************************** */
  // !(TT2_RIGHT_PAREN|TT2_HARD_NEWLINE|[TT2_MINUS]TT2_CLOSE_TAG|TT2_SEMI)
  static boolean recover_parenthesised_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_parenthesised_item")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !recover_parenthesised_item_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // TT2_RIGHT_PAREN|TT2_HARD_NEWLINE|[TT2_MINUS]TT2_CLOSE_TAG|TT2_SEMI
  private static boolean recover_parenthesised_item_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_parenthesised_item_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_RIGHT_PAREN);
    if (!result_) result_ = consumeToken(builder_, TT2_HARD_NEWLINE);
    if (!result_) result_ = recover_parenthesised_item_0_2(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, TT2_SEMI);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [TT2_MINUS]TT2_CLOSE_TAG
  private static boolean recover_parenthesised_item_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_parenthesised_item_0_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = recover_parenthesised_item_0_2_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, TT2_CLOSE_TAG);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [TT2_MINUS]
  private static boolean recover_parenthesised_item_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_parenthesised_item_0_2_0")) return false;
    consumeToken(builder_, TT2_MINUS);
    return true;
  }

  /* ********************************************************** */
  // !(TT2_SQ_CLOSE|TT2_HARD_NEWLINE|[TT2_MINUS]TT2_CLOSE_TAG|TT2_SEMI)
  static boolean recover_sq_string(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_sq_string")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !recover_sq_string_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // TT2_SQ_CLOSE|TT2_HARD_NEWLINE|[TT2_MINUS]TT2_CLOSE_TAG|TT2_SEMI
  private static boolean recover_sq_string_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_sq_string_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_SQ_CLOSE);
    if (!result_) result_ = consumeToken(builder_, TT2_HARD_NEWLINE);
    if (!result_) result_ = recover_sq_string_0_2(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, TT2_SEMI);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [TT2_MINUS]TT2_CLOSE_TAG
  private static boolean recover_sq_string_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_sq_string_0_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = recover_sq_string_0_2_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, TT2_CLOSE_TAG);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [TT2_MINUS]
  private static boolean recover_sq_string_0_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_sq_string_0_2_0")) return false;
    consumeToken(builder_, TT2_MINUS);
    return true;
  }

  /* ********************************************************** */
  // end_directive |
  // 	elsif_directive |
  // 	case_directive |
  // 	else_directive |
  // 	catch_directive |
  // 	final_directive
  static boolean reply_directives(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "reply_directives")) return false;
    boolean result_;
    result_ = end_directive(builder_, level_ + 1);
    if (!result_) result_ = elsif_directive(builder_, level_ + 1);
    if (!result_) result_ = case_directive(builder_, level_ + 1);
    if (!result_) result_ = else_directive(builder_, level_ + 1);
    if (!result_) result_ = catch_directive(builder_, level_ + 1);
    if (!result_) result_ = final_directive(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // TT2_RETURN [directive_postfix]
  public static boolean return_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "return_directive")) return false;
    if (!nextTokenIs(builder_, TT2_RETURN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, RETURN_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_RETURN);
    pinned_ = result_; // pin = 1
    result_ = result_ && return_directive_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [directive_postfix]
  private static boolean return_directive_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "return_directive_1")) return false;
    directive_postfix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // element *
  static boolean root(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "root")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!element(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "root", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // [TT2_SET] <<parseSetElement parse_set_element>> + [directive_postfix]
  public static boolean set_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "set_directive")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SET_DIRECTIVE, "<set directive>");
    result_ = set_directive_0(builder_, level_ + 1);
    result_ = result_ && set_directive_1(builder_, level_ + 1);
    result_ = result_ && set_directive_2(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [TT2_SET]
  private static boolean set_directive_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "set_directive_0")) return false;
    consumeToken(builder_, TT2_SET);
    return true;
  }

  // <<parseSetElement parse_set_element>> +
  private static boolean set_directive_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "set_directive_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parseSetElement(builder_, level_ + 1, TemplateToolkitParserGenerated::parse_set_element);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!parseSetElement(builder_, level_ + 1, TemplateToolkitParserGenerated::parse_set_element)) break;
      if (!empty_element_parsed_guard_(builder_, "set_directive_1", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [directive_postfix]
  private static boolean set_directive_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "set_directive_2")) return false;
    directive_postfix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // TT2_STRING_CONTENT*
  static boolean sq_string_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sq_string_content")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    while (true) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, TT2_STRING_CONTENT)) break;
      if (!empty_element_parsed_guard_(builder_, "sq_string_content", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, TemplateToolkitParserGenerated::recover_sq_string);
    return true;
  }

  /* ********************************************************** */
  // TT2_SQ_OPEN sq_string_content TT2_SQ_CLOSE
  public static boolean sq_string_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "sq_string_expr")) return false;
    if (!nextTokenIs(builder_, TT2_SQ_OPEN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SQ_STRING_EXPR, null);
    result_ = consumeToken(builder_, TT2_SQ_OPEN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, sq_string_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, TT2_SQ_CLOSE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // TT2_STOP [directive_postfix]
  public static boolean stop_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "stop_directive")) return false;
    if (!nextTokenIs(builder_, TT2_STOP)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, STOP_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_STOP);
    pinned_ = result_; // pin = 1
    result_ = result_ && stop_directive_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [directive_postfix]
  private static boolean stop_directive_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "stop_directive_1")) return false;
    directive_postfix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // dq_string_expr | sq_string_expr
  static boolean string_argument(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "string_argument")) return false;
    if (!nextTokenIs(builder_, "", TT2_DQ_OPEN, TT2_SQ_OPEN)) return false;
    boolean result_;
    result_ = dq_string_expr(builder_, level_ + 1);
    if (!result_) result_ = sq_string_expr(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // string_argument (TT2_PLUS !TT2_CLOSE_TAG string_argument) *
  static boolean strings_argument(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "strings_argument")) return false;
    if (!nextTokenIs(builder_, "", TT2_DQ_OPEN, TT2_SQ_OPEN)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = string_argument(builder_, level_ + 1);
    result_ = result_ && strings_argument_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // (TT2_PLUS !TT2_CLOSE_TAG string_argument) *
  private static boolean strings_argument_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "strings_argument_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!strings_argument_1_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "strings_argument_1", pos_)) break;
    }
    return true;
  }

  // TT2_PLUS !TT2_CLOSE_TAG string_argument
  private static boolean strings_argument_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "strings_argument_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_PLUS);
    result_ = result_ && strings_argument_1_0_1(builder_, level_ + 1);
    result_ = result_ && string_argument(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !TT2_CLOSE_TAG
  private static boolean strings_argument_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "strings_argument_1_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, TT2_CLOSE_TAG);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // TT2_SWITCH expr
  public static boolean switch_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "switch_directive")) return false;
    if (!nextTokenIs(builder_, TT2_SWITCH)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SWITCH_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_SWITCH);
    pinned_ = result_; // pin = 1
    result_ = result_ && expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // TT2_TAGS <<parseTags>>
  public static boolean tags_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "tags_directive")) return false;
    if (!nextTokenIs(builder_, TT2_TAGS)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, TAGS_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_TAGS);
    pinned_ = result_; // pin = 1
    result_ = result_ && parseTags(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // TT2_THROW exception_type exception_message [exception_args] [directive_postfix]
  public static boolean throw_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "throw_directive")) return false;
    if (!nextTokenIs(builder_, TT2_THROW)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, THROW_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_THROW);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, exception_type(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, exception_message(builder_, level_ + 1)) && result_;
    result_ = pinned_ && report_error_(builder_, throw_directive_3(builder_, level_ + 1)) && result_;
    result_ = pinned_ && throw_directive_4(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [exception_args]
  private static boolean throw_directive_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "throw_directive_3")) return false;
    exception_args(builder_, level_ + 1);
    return true;
  }

  // [directive_postfix]
  private static boolean throw_directive_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "throw_directive_4")) return false;
    directive_postfix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // TT2_TRY
  public static boolean try_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "try_directive")) return false;
    if (!nextTokenIs(builder_, TT2_TRY)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_TRY);
    exit_section_(builder_, marker_, TRY_DIRECTIVE, result_);
    return result_;
  }

  /* ********************************************************** */
  // TT2_UNLESS expr
  public static boolean unless_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unless_directive")) return false;
    if (!nextTokenIs(builder_, TT2_UNLESS)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, UNLESS_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_UNLESS);
    pinned_ = result_; // pin = 1
    result_ = result_ && expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // TT2_USE [use_instantiation] use_module
  public static boolean use_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "use_directive")) return false;
    if (!nextTokenIs(builder_, TT2_USE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, USE_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_USE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, use_directive_1(builder_, level_ + 1));
    result_ = pinned_ && use_module(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [use_instantiation]
  private static boolean use_directive_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "use_directive_1")) return false;
    use_instantiation(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // keyword_or_identifier_term
  public static boolean use_instance(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "use_instance")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, USE_INSTANCE, "<use instance>");
    result_ = keyword_or_identifier_term(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // use_instance TT2_ASSIGN !TT2_CLOSE_TAG
  static boolean use_instantiation(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "use_instantiation")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = use_instance(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, TT2_ASSIGN);
    result_ = result_ && use_instantiation_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !TT2_CLOSE_TAG
  private static boolean use_instantiation_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "use_instantiation_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeToken(builder_, TT2_CLOSE_TAG);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // module_name [module_params]
  static boolean use_module(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "use_module")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = module_name(builder_, level_ + 1);
    result_ = result_ && use_module_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [module_params]
  private static boolean use_module_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "use_module_1")) return false;
    module_params(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // TT2_LEFT_BRACE variable_braced_content TT2_RIGHT_BRACE
  static boolean variable_braced(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_braced")) return false;
    if (!nextTokenIs(builder_, TT2_LEFT_BRACE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, TT2_LEFT_BRACE);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, variable_braced_content(builder_, level_ + 1));
    result_ = pinned_ && consumeToken(builder_, TT2_RIGHT_BRACE) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // identifier_expr_content
  static boolean variable_braced_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_braced_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = identifier_expr_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, TemplateToolkitParserGenerated::recover_braced_variable);
    return result_;
  }

  /* ********************************************************** */
  // TT2_SIGIL_SCALAR {variable_braced|identifier_expr_content}
  public static boolean variable_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_expr")) return false;
    if (!nextTokenIs(builder_, TT2_SIGIL_SCALAR)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_SIGIL_SCALAR);
    result_ = result_ && variable_expr_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, VARIABLE_EXPR, result_);
    return result_;
  }

  // variable_braced|identifier_expr_content
  private static boolean variable_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_expr_1")) return false;
    boolean result_;
    result_ = variable_braced(builder_, level_ + 1);
    if (!result_) result_ = identifier_expr_content(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // variable_expr | strings_argument
  static boolean variable_or_strings(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_or_strings")) return false;
    boolean result_;
    result_ = variable_expr(builder_, level_ + 1);
    if (!result_) result_ = strings_argument(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // variable_or_strings | insert_files
  static boolean variable_strings_or_files(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_strings_or_files")) return false;
    boolean result_;
    result_ = variable_or_strings(builder_, level_ + 1);
    if (!result_) result_ = insert_files(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // variable_or_strings | convert_to_string
  static boolean variable_strings_or_stringify(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "variable_strings_or_stringify")) return false;
    boolean result_;
    result_ = variable_or_strings(builder_, level_ + 1);
    if (!result_) result_ = convert_to_string(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // TT2_WHILE expr
  public static boolean while_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "while_directive")) return false;
    if (!nextTokenIs(builder_, TT2_WHILE)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, WHILE_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_WHILE);
    pinned_ = result_; // pin = 1
    result_ = result_ && expr(builder_, level_ + 1, -1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // TT2_WRAPPER variable_strings_or_files assign_expr * [directive_postfix]
  public static boolean wrapper_directive(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "wrapper_directive")) return false;
    if (!nextTokenIs(builder_, TT2_WRAPPER)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, WRAPPER_DIRECTIVE, null);
    result_ = consumeToken(builder_, TT2_WRAPPER);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, variable_strings_or_files(builder_, level_ + 1));
    result_ = pinned_ && report_error_(builder_, wrapper_directive_2(builder_, level_ + 1)) && result_;
    result_ = pinned_ && wrapper_directive_3(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // assign_expr *
  private static boolean wrapper_directive_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "wrapper_directive_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!expr(builder_, level_ + 1, -1)) break;
      if (!empty_element_parsed_guard_(builder_, "wrapper_directive_2", pos_)) break;
    }
    return true;
  }

  // [directive_postfix]
  private static boolean wrapper_directive_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "wrapper_directive_3")) return false;
    directive_postfix(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // Expression root: expr
  // Operator priority table:
  // 0: BINARY(assign_expr)
  // 1: BINARY(ternar_expr)
  // 2: BINARY(range_expr)
  // 3: N_ARY(or_expr)
  // 4: N_ARY(and_expr)
  // 5: BINARY(equal_expr)
  // 6: BINARY(compare_expr)
  // 7: N_ARY(add_expr)
  // 8: N_ARY(mul_expr)
  // 9: PREFIX(unary_expr)
  // 10: ATOM(term_expr)
  public static boolean expr(PsiBuilder builder_, int level_, int priority_) {
    if (!recursion_guard_(builder_, level_, "expr")) return false;
    addVariant(builder_, "<expr>");
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, "<expr>");
    result_ = unary_expr(builder_, level_ + 1);
    if (!result_) result_ = term_expr(builder_, level_ + 1);
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
      if (priority_ < 0 && leftMarkerIs(builder_, IDENTIFIER_EXPR) && assign_expr_0(builder_, level_ + 1)) {
        result_ = expr(builder_, level_, -1);
        exit_section_(builder_, level_, marker_, ASSIGN_EXPR, result_, true, null);
      }
      else if (priority_ < 1 && consumeTokenSmart(builder_, TT2_QUESTION)) {
        result_ = report_error_(builder_, expr(builder_, level_, 0));
        result_ = ternar_expr_1(builder_, level_ + 1) && result_;
        exit_section_(builder_, level_, marker_, TERNAR_EXPR, result_, true, null);
      }
      else if (priority_ < 2 && consumeTokenSmart(builder_, TT2_RANGE)) {
        result_ = expr(builder_, level_, 2);
        exit_section_(builder_, level_, marker_, RANGE_EXPR, result_, true, null);
      }
      else if (priority_ < 3 && or_expr_0(builder_, level_ + 1)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 3));
          if (!or_expr_0(builder_, level_ + 1)) break;
        }
        exit_section_(builder_, level_, marker_, OR_EXPR, result_, true, null);
      }
      else if (priority_ < 4 && and_expr_0(builder_, level_ + 1)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 4));
          if (!and_expr_0(builder_, level_ + 1)) break;
        }
        exit_section_(builder_, level_, marker_, AND_EXPR, result_, true, null);
      }
      else if (priority_ < 5 && equal_expr_0(builder_, level_ + 1)) {
        result_ = expr(builder_, level_, 5);
        exit_section_(builder_, level_, marker_, EQUAL_EXPR, result_, true, null);
      }
      else if (priority_ < 6 && compare_expr_0(builder_, level_ + 1)) {
        result_ = expr(builder_, level_, 6);
        exit_section_(builder_, level_, marker_, COMPARE_EXPR, result_, true, null);
      }
      else if (priority_ < 7 && add_expr_0(builder_, level_ + 1)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 7));
          if (!add_expr_0(builder_, level_ + 1)) break;
        }
        exit_section_(builder_, level_, marker_, ADD_EXPR, result_, true, null);
      }
      else if (priority_ < 8 && mul_expr_0(builder_, level_ + 1)) {
        while (true) {
          result_ = report_error_(builder_, expr(builder_, level_, 8));
          if (!mul_expr_0(builder_, level_ + 1)) break;
        }
        exit_section_(builder_, level_, marker_, MUL_EXPR, result_, true, null);
      }
      else {
        exit_section_(builder_, level_, marker_, null, false, false, null);
        break;
      }
    }
    return result_;
  }

  // TT2_ASSIGN !TT2_CLOSE_TAG
  private static boolean assign_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assign_expr_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenSmart(builder_, TT2_ASSIGN);
    result_ = result_ && assign_expr_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !TT2_CLOSE_TAG
  private static boolean assign_expr_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "assign_expr_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeTokenSmart(builder_, TT2_CLOSE_TAG);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // TT2_COLON expr
  private static boolean ternar_expr_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ternar_expr_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, TT2_COLON);
    result_ = result_ && expr(builder_, level_ + 1, -1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // TT2_OR|TT2_OR_TEXT
  private static boolean or_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "or_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, TT2_OR);
    if (!result_) result_ = consumeTokenSmart(builder_, TT2_OR_TEXT);
    return result_;
  }

  // TT2_AND|TT2_AND_TEXT
  private static boolean and_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "and_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, TT2_AND);
    if (!result_) result_ = consumeTokenSmart(builder_, TT2_AND_TEXT);
    return result_;
  }

  // TT2_EQUAL|TT2_NOT_EQUAL
  private static boolean equal_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "equal_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, TT2_EQUAL);
    if (!result_) result_ = consumeTokenSmart(builder_, TT2_NOT_EQUAL);
    return result_;
  }

  // TT2_LE|TT2_LT|TT2_GT|TT2_GE
  private static boolean compare_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "compare_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, TT2_LE);
    if (!result_) result_ = consumeTokenSmart(builder_, TT2_LT);
    if (!result_) result_ = consumeTokenSmart(builder_, TT2_GT);
    if (!result_) result_ = consumeTokenSmart(builder_, TT2_GE);
    return result_;
  }

  // TT2_PLUS !TT2_CLOSE_TAG|TT2_MINUS !(TT2_CLOSE_TAG)|TT2_CONCAT
  private static boolean add_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "add_expr_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = add_expr_0_0(builder_, level_ + 1);
    if (!result_) result_ = add_expr_0_1(builder_, level_ + 1);
    if (!result_) result_ = consumeTokenSmart(builder_, TT2_CONCAT);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // TT2_PLUS !TT2_CLOSE_TAG
  private static boolean add_expr_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "add_expr_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenSmart(builder_, TT2_PLUS);
    result_ = result_ && add_expr_0_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !TT2_CLOSE_TAG
  private static boolean add_expr_0_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "add_expr_0_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeTokenSmart(builder_, TT2_CLOSE_TAG);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // TT2_MINUS !(TT2_CLOSE_TAG)
  private static boolean add_expr_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "add_expr_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokenSmart(builder_, TT2_MINUS);
    result_ = result_ && add_expr_0_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !(TT2_CLOSE_TAG)
  private static boolean add_expr_0_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "add_expr_0_1_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !consumeTokenSmart(builder_, TT2_CLOSE_TAG);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // TT2_MUL|TT2_DIV|TT2_MOD|TT2_DIV_TEXT|TT2_MOD_TEXT
  private static boolean mul_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "mul_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, TT2_MUL);
    if (!result_) result_ = consumeTokenSmart(builder_, TT2_DIV);
    if (!result_) result_ = consumeTokenSmart(builder_, TT2_MOD);
    if (!result_) result_ = consumeTokenSmart(builder_, TT2_DIV_TEXT);
    if (!result_) result_ = consumeTokenSmart(builder_, TT2_MOD_TEXT);
    return result_;
  }

  public static boolean unary_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_expr")) return false;
    if (!nextTokenIsSmart(builder_, TT2_NOT, TT2_NOT_TEXT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, null);
    result_ = unary_expr_0(builder_, level_ + 1);
    pinned_ = result_;
    result_ = pinned_ && expr(builder_, level_, 9);
    exit_section_(builder_, level_, marker_, UNARY_EXPR, result_, pinned_, null);
    return result_ || pinned_;
  }

  // TT2_NOT|TT2_NOT_TEXT
  private static boolean unary_expr_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unary_expr_0")) return false;
    boolean result_;
    result_ = consumeTokenSmart(builder_, TT2_NOT);
    if (!result_) result_ = consumeTokenSmart(builder_, TT2_NOT_TEXT);
    return result_;
  }

  // parenthesised_expr |
  // 	hash_expr |
  // 	array_expr |
  // 	variable_expr |
  // 	call_expr |
  // 	identifier_expr |
  // 	sq_string_expr |
  // 	dq_string_expr |
  // 	TT2_SYMBOL |
  // 	[<<parseUnaryMinus>>] TT2_NUMBER |
  // 	[<<parseUnaryMinus>>] TT2_NUMBER_SIMPLE |
  // 	keyword_or_identifier_term
  public static boolean term_expr(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_expr")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _COLLAPSE_, TERM_EXPR, "<term expr>");
    result_ = parenthesised_expr(builder_, level_ + 1);
    if (!result_) result_ = hash_expr(builder_, level_ + 1);
    if (!result_) result_ = array_expr(builder_, level_ + 1);
    if (!result_) result_ = variable_expr(builder_, level_ + 1);
    if (!result_) result_ = call_expr(builder_, level_ + 1);
    if (!result_) result_ = identifier_expr(builder_, level_ + 1);
    if (!result_) result_ = sq_string_expr(builder_, level_ + 1);
    if (!result_) result_ = dq_string_expr(builder_, level_ + 1);
    if (!result_) result_ = consumeTokenSmart(builder_, TT2_SYMBOL);
    if (!result_) result_ = term_expr_9(builder_, level_ + 1);
    if (!result_) result_ = term_expr_10(builder_, level_ + 1);
    if (!result_) result_ = keyword_or_identifier_term(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // [<<parseUnaryMinus>>] TT2_NUMBER
  private static boolean term_expr_9(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_expr_9")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = term_expr_9_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, TT2_NUMBER);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [<<parseUnaryMinus>>]
  private static boolean term_expr_9_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_expr_9_0")) return false;
    parseUnaryMinus(builder_, level_ + 1);
    return true;
  }

  // [<<parseUnaryMinus>>] TT2_NUMBER_SIMPLE
  private static boolean term_expr_10(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_expr_10")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = term_expr_10_0(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, TT2_NUMBER_SIMPLE);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [<<parseUnaryMinus>>]
  private static boolean term_expr_10_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "term_expr_10_0")) return false;
    parseUnaryMinus(builder_, level_ + 1);
    return true;
  }

}
