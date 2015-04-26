// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.perl5.lang.perl.lexer.PerlElementTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class PerlParser implements PsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    if (t == CODE_LINE) {
      r = code_line(b, 0);
    }
    else if (t == CODE_LINE_INVALID_ELEMENT) {
      r = code_line_invalid_element(b, 0);
    }
    else if (t == FUNCTION_CALL) {
      r = function_call(b, 0);
    }
    else if (t == FUNCTION_CALL_ANY) {
      r = function_call_any(b, 0);
    }
    else if (t == FUNCTION_DEFINITION_ANON) {
      r = function_definition_anon(b, 0);
    }
    else if (t == FUNCTION_DEFINITION_NAMED) {
      r = function_definition_named(b, 0);
    }
    else if (t == IF_BLOCK) {
      r = if_block(b, 0);
    }
    else if (t == IF_BLOCK_ELSE) {
      r = if_block_else(b, 0);
    }
    else if (t == IF_BLOCK_ELSIF) {
      r = if_block_elsif(b, 0);
    }
    else if (t == IF_BRANCH) {
      r = if_branch(b, 0);
    }
    else if (t == IF_BRANCH_CONDITIONAL) {
      r = if_branch_conditional(b, 0);
    }
    else if (t == METHOD_CALL) {
      r = method_call(b, 0);
    }
    else if (t == OBJECT_CALL) {
      r = object_call(b, 0);
    }
    else if (t == PACKAGE_DEFINITION_INVALID) {
      r = package_definition_invalid(b, 0);
    }
    else if (t == PACKAGE_NAMESPACE) {
      r = package_namespace(b, 0);
    }
    else if (t == PACKAGE_NO) {
      r = package_no(b, 0);
    }
    else if (t == PACKAGE_NO_INVALID) {
      r = package_no_invalid(b, 0);
    }
    else if (t == PACKAGE_OBJECT_CALL) {
      r = package_object_call(b, 0);
    }
    else if (t == PACKAGE_REQUIRE) {
      r = package_require(b, 0);
    }
    else if (t == PACKAGE_REQUIRE_INVALID) {
      r = package_require_invalid(b, 0);
    }
    else if (t == PACKAGE_STATIC_CALL) {
      r = package_static_call(b, 0);
    }
    else if (t == PACKAGE_USE) {
      r = package_use(b, 0);
    }
    else if (t == PACKAGE_USE_ARGUMENTS) {
      r = package_use_arguments(b, 0);
    }
    else if (t == PACKAGE_USE_INVALID) {
      r = package_use_invalid(b, 0);
    }
    else if (t == PERL_ARRAY) {
      r = perl_array(b, 0);
    }
    else if (t == PERL_ARRAY_VALUE) {
      r = perl_array_value(b, 0);
    }
    else if (t == PERL_BLOCK) {
      r = perl_block(b, 0);
    }
    else if (t == PERL_CALL_PARAM) {
      r = perl_call_param(b, 0);
    }
    else if (t == PERL_CALL_PARAMS) {
      r = perl_call_params(b, 0);
    }
    else if (t == PERL_CALL_PARAMS_ANY) {
      r = perl_call_params_any(b, 0);
    }
    else if (t == PERL_CALL_PARAMS_STRICT) {
      r = perl_call_params_strict(b, 0);
    }
    else if (t == PERL_EVAL) {
      r = perl_eval(b, 0);
    }
    else if (t == PERL_EVAL_INVALID) {
      r = perl_eval_invalid(b, 0);
    }
    else if (t == PERL_EXPRESSION) {
      r = perl_expression(b, 0);
    }
    else if (t == PERL_FUNCTION_ALL) {
      r = perl_function_all(b, 0);
    }
    else if (t == PERL_GLOB) {
      r = perl_glob(b, 0);
    }
    else if (t == PERL_HASH) {
      r = perl_hash(b, 0);
    }
    else if (t == PERL_HASH_VALUE) {
      r = perl_hash_value(b, 0);
    }
    else if (t == PERL_SCALAR) {
      r = perl_scalar(b, 0);
    }
    else if (t == PERL_SCALAR_FUNCTION_RESULT) {
      r = perl_scalar_function_result(b, 0);
    }
    else if (t == PERL_SCALAR_VALUE) {
      r = perl_scalar_value(b, 0);
    }
    else if (t == PERL_SUBEXPRESSION) {
      r = perl_subexpression(b, 0);
    }
    else {
      r = parse_root_(t, b, 0);
    }
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return perlFile(b, l + 1);
  }

  /* ********************************************************** */
  // package_namespace | package_item | package_definition_invalid
  static boolean block_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_item")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_namespace(b, l + 1);
    if (!r) r = package_item(b, l + 1);
    if (!r) r = package_definition_invalid(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // code_line_element* ';' perl_multiline_string ?
  public static boolean code_line(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<code line>");
    r = code_line_0(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    r = r && code_line_2(b, l + 1);
    exit_section_(b, l, m, CODE_LINE, r, false, null);
    return r;
  }

  // code_line_element*
  private static boolean code_line_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_0")) return false;
    int c = current_position_(b);
    while (true) {
      if (!code_line_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "code_line_0", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // perl_multiline_string ?
  private static boolean code_line_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_2")) return false;
    perl_multiline_string(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // method_call | function_call| perl_function | perl_array_value | perl_hash_value | perl_scalar_value | perl_glob |  perl_multiline_string | PERL_MULTILINE_MARKER | PERL_OPERATOR
  static boolean code_line_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_element")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = method_call(b, l + 1);
    if (!r) r = function_call(b, l + 1);
    if (!r) r = perl_function(b, l + 1);
    if (!r) r = perl_array_value(b, l + 1);
    if (!r) r = perl_hash_value(b, l + 1);
    if (!r) r = perl_scalar_value(b, l + 1);
    if (!r) r = perl_glob(b, l + 1);
    if (!r) r = perl_multiline_string(b, l + 1);
    if (!r) r = consumeToken(b, PERL_MULTILINE_MARKER);
    if (!r) r = consumeToken(b, PERL_OPERATOR);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // code_line_element +
  static boolean code_line_elements(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_elements")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = code_line_element(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!code_line_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "code_line_elements", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // package_use_invalid
  //     | package_no_invalid
  //     | package_require_invalid
  //     | perl_eval_invalid
  static boolean code_line_invalid(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_invalid")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_use_invalid(b, l + 1);
    if (!r) r = package_no_invalid(b, l + 1);
    if (!r) r = package_require_invalid(b, l + 1);
    if (!r) r = perl_eval_invalid(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // code_line_element | perl_package | PERL_VERSION | perl_controls | perl_chars
  public static boolean code_line_invalid_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_invalid_element")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<code line invalid element>");
    r = code_line_element(b, l + 1);
    if (!r) r = perl_package(b, l + 1);
    if (!r) r = consumeToken(b, PERL_VERSION);
    if (!r) r = perl_controls(b, l + 1);
    if (!r) r = perl_chars(b, l + 1);
    exit_section_(b, l, m, CODE_LINE_INVALID_ELEMENT, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // perl_eval | package_use | package_no | package_require | code_line
  static boolean code_line_valid(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_valid")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_eval(b, l + 1);
    if (!r) r = package_use(b, l + 1);
    if (!r) r = package_no(b, l + 1);
    if (!r) r = package_require(b, l + 1);
    if (!r) r = code_line(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // perl_function perl_call_params_any ?
  public static boolean function_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call")) return false;
    if (!nextTokenIs(b, "<function call>", PERL_FUNCTION_BUILT_IN, PERL_FUNCTION_USER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<function call>");
    r = perl_function(b, l + 1);
    r = r && function_call_1(b, l + 1);
    exit_section_(b, l, m, FUNCTION_CALL, r, false, null);
    return r;
  }

  // perl_call_params_any ?
  private static boolean function_call_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_1")) return false;
    perl_call_params_any(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // perl_function_all perl_call_params_any ?
  public static boolean function_call_any(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_any")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<function call any>");
    r = perl_function_all(b, l + 1);
    r = r && function_call_any_1(b, l + 1);
    exit_section_(b, l, m, FUNCTION_CALL_ANY, r, false, null);
    return r;
  }

  // perl_call_params_any ?
  private static boolean function_call_any_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_any_1")) return false;
    perl_call_params_any(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // function_definition_named | function_definition_anon
  static boolean function_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_definition")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = function_definition_named(b, l + 1);
    if (!r) r = function_definition_anon(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'sub' perl_block ';'
  public static boolean function_definition_anon(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_definition_anon")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<function definition anon>");
    r = consumeToken(b, "sub");
    r = r && perl_block(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    exit_section_(b, l, m, FUNCTION_DEFINITION_ANON, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'sub' perl_function perl_block ';' ?
  public static boolean function_definition_named(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_definition_named")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<function definition named>");
    r = consumeToken(b, "sub");
    r = r && perl_function(b, l + 1);
    r = r && perl_block(b, l + 1);
    r = r && function_definition_named_3(b, l + 1);
    exit_section_(b, l, m, FUNCTION_DEFINITION_NAMED, r, false, null);
    return r;
  }

  // ';' ?
  private static boolean function_definition_named_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_definition_named_3")) return false;
    consumeToken(b, PERL_SEMI);
    return true;
  }

  /* ********************************************************** */
  // ('if' | 'unless') if_branch_conditional if_block_elsif * if_block_else ?
  public static boolean if_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_block")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<if block>");
    r = if_block_0(b, l + 1);
    r = r && if_branch_conditional(b, l + 1);
    r = r && if_block_2(b, l + 1);
    r = r && if_block_3(b, l + 1);
    exit_section_(b, l, m, IF_BLOCK, r, false, null);
    return r;
  }

  // 'if' | 'unless'
  private static boolean if_block_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_block_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "if");
    if (!r) r = consumeToken(b, "unless");
    exit_section_(b, m, null, r);
    return r;
  }

  // if_block_elsif *
  private static boolean if_block_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_block_2")) return false;
    int c = current_position_(b);
    while (true) {
      if (!if_block_elsif(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "if_block_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // if_block_else ?
  private static boolean if_block_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_block_3")) return false;
    if_block_else(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'else' if_branch
  public static boolean if_block_else(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_block_else")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<if block else>");
    r = consumeToken(b, "else");
    r = r && if_branch(b, l + 1);
    exit_section_(b, l, m, IF_BLOCK_ELSE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'elsif' if_branch_conditional
  public static boolean if_block_elsif(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_block_elsif")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<if block elsif>");
    r = consumeToken(b, "elsif");
    r = r && if_branch_conditional(b, l + 1);
    exit_section_(b, l, m, IF_BLOCK_ELSIF, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // code_line | perl_block
  public static boolean if_branch(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_branch")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<if branch>");
    r = code_line(b, l + 1);
    if (!r) r = perl_block(b, l + 1);
    exit_section_(b, l, m, IF_BRANCH, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '(' perl_expression ')' if_branch
  public static boolean if_branch_conditional(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_branch_conditional")) return false;
    if (!nextTokenIs(b, PERL_LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && perl_expression(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    r = r && if_branch(b, l + 1);
    exit_section_(b, m, IF_BRANCH_CONDITIONAL, r);
    return r;
  }

  /* ********************************************************** */
  // package_static_call | package_object_call | object_call
  public static boolean method_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_call")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<method call>");
    r = package_static_call(b, l + 1);
    if (!r) r = package_object_call(b, l + 1);
    if (!r) r = object_call(b, l + 1);
    exit_section_(b, l, m, METHOD_CALL, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // perl_scalar_value_var '->' function_call_any
  public static boolean object_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_call")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<object call>");
    r = perl_scalar_value_var(b, l + 1);
    r = r && consumeToken(b, PERL_DEREFERENCE);
    r = r && function_call_any(b, l + 1);
    exit_section_(b, l, m, OBJECT_CALL, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'package' code_line_invalid_element*';'
  public static boolean package_definition_invalid(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_definition_invalid")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package definition invalid>");
    r = consumeToken(b, "package");
    r = r && package_definition_invalid_1(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    exit_section_(b, l, m, PACKAGE_DEFINITION_INVALID, r, false, null);
    return r;
  }

  // code_line_invalid_element*
  private static boolean package_definition_invalid_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_definition_invalid_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!code_line_invalid_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "package_definition_invalid_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // function_definition | if_block | code_line_valid | PERL_POD | PERL_COMMENT | PERL_COMMENT_BLOCK | perl_block | code_line_invalid
  static boolean package_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_item")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = function_definition(b, l + 1);
    if (!r) r = if_block(b, l + 1);
    if (!r) r = code_line_valid(b, l + 1);
    if (!r) r = consumeToken(b, PERL_POD);
    if (!r) r = consumeToken(b, PERL_COMMENT);
    if (!r) r = consumeToken(b, PERL_COMMENT_BLOCK);
    if (!r) r = perl_block(b, l + 1);
    if (!r) r = code_line_invalid(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'package' perl_package PERL_VERSION ? (perl_block | ';' package_item * )
  public static boolean package_namespace(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package namespace>");
    r = consumeToken(b, "package");
    r = r && perl_package(b, l + 1);
    r = r && package_namespace_2(b, l + 1);
    r = r && package_namespace_3(b, l + 1);
    exit_section_(b, l, m, PACKAGE_NAMESPACE, r, false, null);
    return r;
  }

  // PERL_VERSION ?
  private static boolean package_namespace_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_2")) return false;
    consumeToken(b, PERL_VERSION);
    return true;
  }

  // perl_block | ';' package_item *
  private static boolean package_namespace_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_block(b, l + 1);
    if (!r) r = package_namespace_3_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ';' package_item *
  private static boolean package_namespace_3_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_3_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SEMI);
    r = r && package_namespace_3_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // package_item *
  private static boolean package_namespace_3_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_3_1_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!package_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "package_namespace_3_1_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // 'no' package_use_arguments ';'
  public static boolean package_no(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_no")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package no>");
    r = consumeToken(b, "no");
    r = r && package_use_arguments(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    exit_section_(b, l, m, PACKAGE_NO, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'no' code_line_invalid_element*';'
  public static boolean package_no_invalid(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_no_invalid")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package no invalid>");
    r = consumeToken(b, "no");
    r = r && package_no_invalid_1(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    exit_section_(b, l, m, PACKAGE_NO_INVALID, r, false, null);
    return r;
  }

  // code_line_invalid_element*
  private static boolean package_no_invalid_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_no_invalid_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!code_line_invalid_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "package_no_invalid_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // perl_package '->' function_call_any
  public static boolean package_object_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_object_call")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package object call>");
    r = perl_package(b, l + 1);
    r = r && consumeToken(b, PERL_DEREFERENCE);
    r = r && function_call_any(b, l + 1);
    exit_section_(b, l, m, PACKAGE_OBJECT_CALL, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'require' (perl_package | PERL_VERSION | perl_string) ';'
  public static boolean package_require(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_require")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package require>");
    r = consumeToken(b, "require");
    r = r && package_require_1(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    exit_section_(b, l, m, PACKAGE_REQUIRE, r, false, null);
    return r;
  }

  // perl_package | PERL_VERSION | perl_string
  private static boolean package_require_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_require_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_package(b, l + 1);
    if (!r) r = consumeToken(b, PERL_VERSION);
    if (!r) r = perl_string(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'require' code_line_invalid_element*';'
  public static boolean package_require_invalid(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_require_invalid")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package require invalid>");
    r = consumeToken(b, "require");
    r = r && package_require_invalid_1(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    exit_section_(b, l, m, PACKAGE_REQUIRE_INVALID, r, false, null);
    return r;
  }

  // code_line_invalid_element*
  private static boolean package_require_invalid_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_require_invalid_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!code_line_invalid_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "package_require_invalid_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // perl_package '::' function_call_any
  public static boolean package_static_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_static_call")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package static call>");
    r = perl_package(b, l + 1);
    r = r && consumeToken(b, PERL_DEPACKAGE);
    r = r && function_call_any(b, l + 1);
    exit_section_(b, l, m, PACKAGE_STATIC_CALL, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'use' package_use_arguments ';'
  public static boolean package_use(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package use>");
    r = consumeToken(b, "use");
    r = r && package_use_arguments(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    exit_section_(b, l, m, PACKAGE_USE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // perl_package PERL_VERSION perl_call_params ?
  //     | perl_package perl_call_params ?
  //     | PERL_VERSION
  public static boolean package_use_arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package use arguments>");
    r = package_use_arguments_0(b, l + 1);
    if (!r) r = package_use_arguments_1(b, l + 1);
    if (!r) r = consumeToken(b, PERL_VERSION);
    exit_section_(b, l, m, PACKAGE_USE_ARGUMENTS, r, false, null);
    return r;
  }

  // perl_package PERL_VERSION perl_call_params ?
  private static boolean package_use_arguments_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_package(b, l + 1);
    r = r && consumeToken(b, PERL_VERSION);
    r = r && package_use_arguments_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // perl_call_params ?
  private static boolean package_use_arguments_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments_0_2")) return false;
    perl_call_params(b, l + 1);
    return true;
  }

  // perl_package perl_call_params ?
  private static boolean package_use_arguments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_package(b, l + 1);
    r = r && package_use_arguments_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // perl_call_params ?
  private static boolean package_use_arguments_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments_1_1")) return false;
    perl_call_params(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'use' code_line_invalid_element*';'
  public static boolean package_use_invalid(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_invalid")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package use invalid>");
    r = consumeToken(b, "use");
    r = r && package_use_invalid_1(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    exit_section_(b, l, m, PACKAGE_USE_INVALID, r, false, null);
    return r;
  }

  // code_line_invalid_element*
  private static boolean package_use_invalid_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_invalid_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!code_line_invalid_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "package_use_invalid_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // block_item*
  static boolean perlFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perlFile")) return false;
    int c = current_position_(b);
    while (true) {
      if (!block_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "perlFile", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // PERL_VARIABLE_ARRAY | PERL_VARIABLE_ARRAY_BUILT_IN
  public static boolean perl_array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_array")) return false;
    if (!nextTokenIs(b, "<perl array>", PERL_VARIABLE_ARRAY, PERL_VARIABLE_ARRAY_BUILT_IN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<perl array>");
    r = consumeToken(b, PERL_VARIABLE_ARRAY);
    if (!r) r = consumeToken(b, PERL_VARIABLE_ARRAY_BUILT_IN);
    exit_section_(b, l, m, PERL_ARRAY, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PERL_SIGIL_ARRAY '{' perl_scalar_value '}'
  static boolean perl_array_dereference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_array_dereference")) return false;
    if (!nextTokenIs(b, PERL_SIGIL_ARRAY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SIGIL_ARRAY);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && perl_scalar_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // perl_array '[' (perl_array_value | perl_scalar_value) ']'
  static boolean perl_array_slice(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_array_slice")) return false;
    if (!nextTokenIs(b, "", PERL_VARIABLE_ARRAY, PERL_VARIABLE_ARRAY_BUILT_IN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_array(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACK);
    r = r && perl_array_slice_2(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACK);
    exit_section_(b, m, null, r);
    return r;
  }

  // perl_array_value | perl_scalar_value
  private static boolean perl_array_slice_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_array_slice_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_array_value(b, l + 1);
    if (!r) r = perl_scalar_value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // perl_array_value_item (',' (perl_array_value_item | perl_scalar_value  )) *
  public static boolean perl_array_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_array_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, "<perl array value>");
    r = perl_array_value_item(b, l + 1);
    r = r && perl_array_value_1(b, l + 1);
    exit_section_(b, l, m, PERL_ARRAY_VALUE, r, false, null);
    return r;
  }

  // (',' (perl_array_value_item | perl_scalar_value  )) *
  private static boolean perl_array_value_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_array_value_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!perl_array_value_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "perl_array_value_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ',' (perl_array_value_item | perl_scalar_value  )
  private static boolean perl_array_value_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_array_value_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_COMMA);
    r = r && perl_array_value_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // perl_array_value_item | perl_scalar_value
  private static boolean perl_array_value_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_array_value_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_array_value_item(b, l + 1);
    if (!r) r = perl_scalar_value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // perl_array_dereference | perl_array_slice | perl_hash_slice | perl_hash_value | perl_array
  static boolean perl_array_value_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_array_value_item")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_array_dereference(b, l + 1);
    if (!r) r = perl_array_slice(b, l + 1);
    if (!r) r = perl_hash_slice(b, l + 1);
    if (!r) r = perl_hash_value(b, l + 1);
    if (!r) r = perl_array(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '{' block_item* '}'
  public static boolean perl_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_block")) return false;
    if (!nextTokenIs(b, PERL_LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LBRACE);
    r = r && perl_block_1(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, PERL_BLOCK, r);
    return r;
  }

  // block_item*
  private static boolean perl_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_block_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!block_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "perl_block_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // perl_array_value | perl_scalar_value
  public static boolean perl_call_param(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_call_param")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<perl call param>");
    r = perl_array_value(b, l + 1);
    if (!r) r = perl_scalar_value(b, l + 1);
    exit_section_(b, l, m, PERL_CALL_PARAM, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // perl_call_param ? (',' perl_call_param ) *
  public static boolean perl_call_params(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_call_params")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<perl call params>");
    r = perl_call_params_0(b, l + 1);
    r = r && perl_call_params_1(b, l + 1);
    exit_section_(b, l, m, PERL_CALL_PARAMS, r, false, null);
    return r;
  }

  // perl_call_param ?
  private static boolean perl_call_params_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_call_params_0")) return false;
    perl_call_param(b, l + 1);
    return true;
  }

  // (',' perl_call_param ) *
  private static boolean perl_call_params_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_call_params_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!perl_call_params_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "perl_call_params_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ',' perl_call_param
  private static boolean perl_call_params_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_call_params_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_COMMA);
    r = r && perl_call_param(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // perl_call_params_strict | perl_call_params
  public static boolean perl_call_params_any(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_call_params_any")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<perl call params any>");
    r = perl_call_params_strict(b, l + 1);
    if (!r) r = perl_call_params(b, l + 1);
    exit_section_(b, l, m, PERL_CALL_PARAMS_ANY, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '(' perl_call_params ')'
  public static boolean perl_call_params_strict(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_call_params_strict")) return false;
    if (!nextTokenIs(b, PERL_LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && perl_call_params(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, PERL_CALL_PARAMS_STRICT, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_NEWLINE | PERL_BAD_CHARACTER
  static boolean perl_chars(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_chars")) return false;
    if (!nextTokenIs(b, "", PERL_BAD_CHARACTER, PERL_NEWLINE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_NEWLINE);
    if (!r) r = consumeToken(b, PERL_BAD_CHARACTER);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ',' | '[' | ']' | '(' | ')'
  static boolean perl_controls(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_controls")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_COMMA);
    if (!r) r = consumeToken(b, PERL_LBRACK);
    if (!r) r = consumeToken(b, PERL_RBRACK);
    if (!r) r = consumeToken(b, PERL_LPAREN);
    if (!r) r = consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'eval' (perl_block | perl_scalar_value) ';'
  public static boolean perl_eval(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_eval")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<perl eval>");
    r = consumeToken(b, "eval");
    r = r && perl_eval_1(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    exit_section_(b, l, m, PERL_EVAL, r, false, null);
    return r;
  }

  // perl_block | perl_scalar_value
  private static boolean perl_eval_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_eval_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_block(b, l + 1);
    if (!r) r = perl_scalar_value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'eval' code_line_invalid_element * ';'
  public static boolean perl_eval_invalid(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_eval_invalid")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<perl eval invalid>");
    r = consumeToken(b, "eval");
    r = r && perl_eval_invalid_1(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    exit_section_(b, l, m, PERL_EVAL_INVALID, r, false, null);
    return r;
  }

  // code_line_invalid_element *
  private static boolean perl_eval_invalid_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_eval_invalid_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!code_line_invalid_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "perl_eval_invalid_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // (code_line_elements | perl_subexpression) +
  public static boolean perl_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<perl expression>");
    r = perl_expression_0(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!perl_expression_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "perl_expression", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, l, m, PERL_EXPRESSION, r, false, null);
    return r;
  }

  // code_line_elements | perl_subexpression
  private static boolean perl_expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = code_line_elements(b, l + 1);
    if (!r) r = perl_subexpression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_FUNCTION_BUILT_IN | PERL_FUNCTION_USER
  static boolean perl_function(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_function")) return false;
    if (!nextTokenIs(b, "", PERL_FUNCTION_BUILT_IN, PERL_FUNCTION_USER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_FUNCTION_BUILT_IN);
    if (!r) r = consumeToken(b, PERL_FUNCTION_USER);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // perl_function | PERL_FUNCTION_BUILT_IN_IMPLEMENTED
  public static boolean perl_function_all(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_function_all")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<perl function all>");
    r = perl_function(b, l + 1);
    if (!r) r = consumeToken(b, PERL_FUNCTION_BUILT_IN_IMPLEMENTED);
    exit_section_(b, l, m, PERL_FUNCTION_ALL, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PERL_VARIABLE_GLOB | PERL_VARIABLE_GLOB_BUILT_IN
  public static boolean perl_glob(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_glob")) return false;
    if (!nextTokenIs(b, "<perl glob>", PERL_VARIABLE_GLOB, PERL_VARIABLE_GLOB_BUILT_IN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<perl glob>");
    r = consumeToken(b, PERL_VARIABLE_GLOB);
    if (!r) r = consumeToken(b, PERL_VARIABLE_GLOB_BUILT_IN);
    exit_section_(b, l, m, PERL_GLOB, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PERL_VARIABLE_HASH | PERL_VARIABLE_HASH_BUILT_IN
  public static boolean perl_hash(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_hash")) return false;
    if (!nextTokenIs(b, "<perl hash>", PERL_VARIABLE_HASH, PERL_VARIABLE_HASH_BUILT_IN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<perl hash>");
    r = consumeToken(b, PERL_VARIABLE_HASH);
    if (!r) r = consumeToken(b, PERL_VARIABLE_HASH_BUILT_IN);
    exit_section_(b, l, m, PERL_HASH, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PERL_SIGIL_HASH '{' perl_scalar_value '}'
  static boolean perl_hash_dereference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_hash_dereference")) return false;
    if (!nextTokenIs(b, PERL_SIGIL_HASH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SIGIL_HASH);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && perl_scalar_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // perl_array '{' (perl_array_value | perl_scalar_value) '{'
  static boolean perl_hash_slice(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_hash_slice")) return false;
    if (!nextTokenIs(b, "", PERL_VARIABLE_ARRAY, PERL_VARIABLE_ARRAY_BUILT_IN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_array(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && perl_hash_slice_2(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // perl_array_value | perl_scalar_value
  private static boolean perl_hash_slice_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_hash_slice_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_array_value(b, l + 1);
    if (!r) r = perl_scalar_value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // perl_hash_dereference | perl_hash
  public static boolean perl_hash_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_hash_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<perl hash value>");
    r = perl_hash_dereference(b, l + 1);
    if (!r) r = perl_hash(b, l + 1);
    exit_section_(b, l, m, PERL_HASH_VALUE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // (
  //         PERL_MULTILINE_SQ
  //         | PERL_MULTILINE_DQ
  //         | PERL_MULTILINE_DX
  //         | PERL_MULTILINE_XML
  //         | PERL_MULTILINE_HTML
  //     ) PERL_MULTILINE_MARKER
  static boolean perl_multiline_string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_multiline_string")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_multiline_string_0(b, l + 1);
    r = r && consumeToken(b, PERL_MULTILINE_MARKER);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_MULTILINE_SQ
  //         | PERL_MULTILINE_DQ
  //         | PERL_MULTILINE_DX
  //         | PERL_MULTILINE_XML
  //         | PERL_MULTILINE_HTML
  private static boolean perl_multiline_string_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_multiline_string_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_MULTILINE_SQ);
    if (!r) r = consumeToken(b, PERL_MULTILINE_DQ);
    if (!r) r = consumeToken(b, PERL_MULTILINE_DX);
    if (!r) r = consumeToken(b, PERL_MULTILINE_XML);
    if (!r) r = consumeToken(b, PERL_MULTILINE_HTML);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_PACKAGE_USER
  //     | PERL_PACKAGE_BUILT_IN
  //     | PERL_PACKAGE_BUILT_IN_DEPRECATED
  //     | PERL_PACKAGE_BUILT_IN_PRAGMA
  static boolean perl_package(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_package")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_PACKAGE_USER);
    if (!r) r = consumeToken(b, PERL_PACKAGE_BUILT_IN);
    if (!r) r = consumeToken(b, PERL_PACKAGE_BUILT_IN_DEPRECATED);
    if (!r) r = consumeToken(b, PERL_PACKAGE_BUILT_IN_PRAGMA);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_VARIABLE_SCALAR | PERL_VARIABLE_SCALAR_BUILT_IN
  public static boolean perl_scalar(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_scalar")) return false;
    if (!nextTokenIs(b, "<perl scalar>", PERL_VARIABLE_SCALAR, PERL_VARIABLE_SCALAR_BUILT_IN)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<perl scalar>");
    r = consumeToken(b, PERL_VARIABLE_SCALAR);
    if (!r) r = consumeToken(b, PERL_VARIABLE_SCALAR_BUILT_IN);
    exit_section_(b, l, m, PERL_SCALAR, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '[' perl_array_value ']'
  static boolean perl_scalar_anon_array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_scalar_anon_array")) return false;
    if (!nextTokenIs(b, PERL_LBRACK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LBRACK);
    r = r && perl_array_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACK);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '{' perl_array_value '}'
  static boolean perl_scalar_anon_hash(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_scalar_anon_hash")) return false;
    if (!nextTokenIs(b, PERL_LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LBRACE);
    r = r && perl_array_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // perl_scalar '[' perl_scalar_value ']'
  static boolean perl_scalar_array_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_scalar_array_element")) return false;
    if (!nextTokenIs(b, "", PERL_VARIABLE_SCALAR, PERL_VARIABLE_SCALAR_BUILT_IN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_scalar(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACK);
    r = r && perl_scalar_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACK);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_SIGIL_SCALAR '{' perl_scalar_value '}'
  static boolean perl_scalar_dereference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_scalar_dereference")) return false;
    if (!nextTokenIs(b, PERL_SIGIL_SCALAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SIGIL_SCALAR);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && perl_scalar_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // function_call
  public static boolean perl_scalar_function_result(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_scalar_function_result")) return false;
    if (!nextTokenIs(b, "<perl scalar function result>", PERL_FUNCTION_BUILT_IN, PERL_FUNCTION_USER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<perl scalar function result>");
    r = function_call(b, l + 1);
    exit_section_(b, l, m, PERL_SCALAR_FUNCTION_RESULT, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // perl_scalar '{' perl_scalar_value '}'
  static boolean perl_scalar_hash_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_scalar_hash_element")) return false;
    if (!nextTokenIs(b, "", PERL_VARIABLE_SCALAR, PERL_VARIABLE_SCALAR_BUILT_IN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_scalar(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && perl_scalar_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // perl_string | PERL_NUMBER | PERL_VERSION | PERL_MULTILINE_MARKER| perl_scalar_anon_array | perl_scalar_anon_hash | perl_scalar_value_var
  public static boolean perl_scalar_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_scalar_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, "<perl scalar value>");
    r = perl_string(b, l + 1);
    if (!r) r = consumeToken(b, PERL_NUMBER);
    if (!r) r = consumeToken(b, PERL_VERSION);
    if (!r) r = consumeToken(b, PERL_MULTILINE_MARKER);
    if (!r) r = perl_scalar_anon_array(b, l + 1);
    if (!r) r = perl_scalar_anon_hash(b, l + 1);
    if (!r) r = perl_scalar_value_var(b, l + 1);
    exit_section_(b, l, m, PERL_SCALAR_VALUE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // perl_scalar_dereference | perl_scalar_hash_element | perl_scalar_array_element | perl_scalar | perl_scalar_function_result
  static boolean perl_scalar_value_var(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_scalar_value_var")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_scalar_dereference(b, l + 1);
    if (!r) r = perl_scalar_hash_element(b, l + 1);
    if (!r) r = perl_scalar_array_element(b, l + 1);
    if (!r) r = perl_scalar(b, l + 1);
    if (!r) r = perl_scalar_function_result(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_DQ_STRING
  //     | PERL_SQ_STRING
  //     | PERL_DX_STRING
  static boolean perl_string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_string")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_DQ_STRING);
    if (!r) r = consumeToken(b, PERL_SQ_STRING);
    if (!r) r = consumeToken(b, PERL_DX_STRING);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '(' code_line_elements ')'
  public static boolean perl_subexpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_subexpression")) return false;
    if (!nextTokenIs(b, PERL_LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && code_line_elements(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, PERL_SUBEXPRESSION, r);
    return r;
  }

}
