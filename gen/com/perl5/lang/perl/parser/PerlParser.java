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
    if (t == ARRAY) {
      r = array(b, 0);
    }
    else if (t == ARRAY_VALUE) {
      r = array_value(b, 0);
    }
    else if (t == BLOCK) {
      r = block(b, 0);
    }
    else if (t == CALL_PARAM) {
      r = call_param(b, 0);
    }
    else if (t == CALL_PARAMS) {
      r = call_params(b, 0);
    }
    else if (t == CALL_PARAMS_ANY) {
      r = call_params_any(b, 0);
    }
    else if (t == CALL_PARAMS_STRICT) {
      r = call_params_strict(b, 0);
    }
    else if (t == CODE_LINE) {
      r = code_line(b, 0);
    }
    else if (t == CODE_LINE_INVALID_ELEMENT) {
      r = code_line_invalid_element(b, 0);
    }
    else if (t == EVAL) {
      r = eval(b, 0);
    }
    else if (t == EVAL_INVALID) {
      r = eval_invalid(b, 0);
    }
    else if (t == EXPRESSION) {
      r = expression(b, 0);
    }
    else if (t == FUNCTION) {
      r = function(b, 0);
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
    else if (t == GLOB) {
      r = glob(b, 0);
    }
    else if (t == HASH) {
      r = hash(b, 0);
    }
    else if (t == HASH_VALUE) {
      r = hash_value(b, 0);
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
    else if (t == PACKAGE_BARE) {
      r = package_bare(b, 0);
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
    else if (t == PACKAGE_USE_INVALID) {
      r = package_use_invalid(b, 0);
    }
    else if (t == SCALAR) {
      r = scalar(b, 0);
    }
    else if (t == SCALAR_VALUE) {
      r = scalar_value(b, 0);
    }
    else if (t == STRING) {
      r = string(b, 0);
    }
    else if (t == SUBEXPRESSION) {
      r = subexpression(b, 0);
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
  // PERL_ARRAY
  public static boolean array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array")) return false;
    if (!nextTokenIs(b, PERL_ARRAY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_ARRAY);
    exit_section_(b, m, ARRAY, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_SIGIL_ARRAY '{' scalar_value '}'
  static boolean array_dereference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_dereference")) return false;
    if (!nextTokenIs(b, PERL_SIGIL_ARRAY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SIGIL_ARRAY);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && scalar_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // array '[' (array_value | scalar_value) ']'
  static boolean array_slice(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_slice")) return false;
    if (!nextTokenIs(b, PERL_ARRAY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = array(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACK);
    r = r && array_slice_2(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACK);
    exit_section_(b, m, null, r);
    return r;
  }

  // array_value | scalar_value
  private static boolean array_slice_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_slice_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = array_value(b, l + 1);
    if (!r) r = scalar_value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // array_value_item (',' (array_value_item | scalar_value  )) *
  public static boolean array_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, "<array value>");
    r = array_value_item(b, l + 1);
    r = r && array_value_1(b, l + 1);
    exit_section_(b, l, m, ARRAY_VALUE, r, false, null);
    return r;
  }

  // (',' (array_value_item | scalar_value  )) *
  private static boolean array_value_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_value_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!array_value_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_value_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ',' (array_value_item | scalar_value  )
  private static boolean array_value_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_value_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_COMMA);
    r = r && array_value_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // array_value_item | scalar_value
  private static boolean array_value_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_value_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = array_value_item(b, l + 1);
    if (!r) r = scalar_value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // array_dereference | array_slice | hash_slice | hash_value | array
  static boolean array_value_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_value_item")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = array_dereference(b, l + 1);
    if (!r) r = array_slice(b, l + 1);
    if (!r) r = hash_slice(b, l + 1);
    if (!r) r = hash_value(b, l + 1);
    if (!r) r = array(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '{' block_item* '}'
  public static boolean block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block")) return false;
    if (!nextTokenIs(b, PERL_LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LBRACE);
    r = r && block_1(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, BLOCK, r);
    return r;
  }

  // block_item*
  private static boolean block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!block_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "block_1", c)) break;
      c = current_position_(b);
    }
    return true;
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
  // array_value | scalar_value
  public static boolean call_param(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_param")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<call param>");
    r = array_value(b, l + 1);
    if (!r) r = scalar_value(b, l + 1);
    exit_section_(b, l, m, CALL_PARAM, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // call_param ? (',' call_param ) *
  public static boolean call_params(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_params")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<call params>");
    r = call_params_0(b, l + 1);
    r = r && call_params_1(b, l + 1);
    exit_section_(b, l, m, CALL_PARAMS, r, false, null);
    return r;
  }

  // call_param ?
  private static boolean call_params_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_params_0")) return false;
    call_param(b, l + 1);
    return true;
  }

  // (',' call_param ) *
  private static boolean call_params_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_params_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!call_params_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "call_params_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ',' call_param
  private static boolean call_params_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_params_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_COMMA);
    r = r && call_param(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // call_params_strict | call_params
  public static boolean call_params_any(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_params_any")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<call params any>");
    r = call_params_strict(b, l + 1);
    if (!r) r = call_params(b, l + 1);
    exit_section_(b, l, m, CALL_PARAMS_ANY, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '(' call_params ')'
  public static boolean call_params_strict(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_params_strict")) return false;
    if (!nextTokenIs(b, PERL_LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && call_params(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, CALL_PARAMS_STRICT, r);
    return r;
  }

  /* ********************************************************** */
  // code_line_element* ';' multiline_string ?
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

  // multiline_string ?
  private static boolean code_line_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_2")) return false;
    multiline_string(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // method_call | function_call| function | array_value | hash_value | scalar_value | glob |  multiline_string | PERL_MULTILINE_MARKER | PERL_OPERATOR
  static boolean code_line_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_element")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = method_call(b, l + 1);
    if (!r) r = function_call(b, l + 1);
    if (!r) r = function(b, l + 1);
    if (!r) r = array_value(b, l + 1);
    if (!r) r = hash_value(b, l + 1);
    if (!r) r = scalar_value(b, l + 1);
    if (!r) r = glob(b, l + 1);
    if (!r) r = multiline_string(b, l + 1);
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
  //     | eval_invalid
  static boolean code_line_invalid(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_invalid")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_use_invalid(b, l + 1);
    if (!r) r = package_no_invalid(b, l + 1);
    if (!r) r = package_require_invalid(b, l + 1);
    if (!r) r = eval_invalid(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // code_line_element | package_bare | PERL_VERSION | controls | chars
  public static boolean code_line_invalid_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_invalid_element")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<code line invalid element>");
    r = code_line_element(b, l + 1);
    if (!r) r = package_bare(b, l + 1);
    if (!r) r = consumeToken(b, PERL_VERSION);
    if (!r) r = controls(b, l + 1);
    if (!r) r = consumeToken(b, CHARS);
    exit_section_(b, l, m, CODE_LINE_INVALID_ELEMENT, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // eval | package_use | package_no | package_require | code_line
  static boolean code_line_valid(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_valid")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = eval(b, l + 1);
    if (!r) r = package_use(b, l + 1);
    if (!r) r = package_no(b, l + 1);
    if (!r) r = package_require(b, l + 1);
    if (!r) r = code_line(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ',' | '[' | ']' | '(' | ')'
  static boolean controls(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "controls")) return false;
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
  // 'eval' (block | scalar_value) ';'
  public static boolean eval(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "eval")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<eval>");
    r = consumeToken(b, "eval");
    r = r && eval_1(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    exit_section_(b, l, m, EVAL, r, false, null);
    return r;
  }

  // block | scalar_value
  private static boolean eval_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "eval_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = block(b, l + 1);
    if (!r) r = scalar_value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'eval' code_line_invalid_element * ';'
  public static boolean eval_invalid(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "eval_invalid")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<eval invalid>");
    r = consumeToken(b, "eval");
    r = r && eval_invalid_1(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    exit_section_(b, l, m, EVAL_INVALID, r, false, null);
    return r;
  }

  // code_line_invalid_element *
  private static boolean eval_invalid_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "eval_invalid_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!code_line_invalid_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "eval_invalid_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // (code_line_elements | subexpression) +
  public static boolean expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<expression>");
    r = expression_0(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!expression_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expression", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, l, m, EXPRESSION, r, false, null);
    return r;
  }

  // code_line_elements | subexpression
  private static boolean expression_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = code_line_elements(b, l + 1);
    if (!r) r = subexpression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_FUNCTION
  public static boolean function(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function")) return false;
    if (!nextTokenIs(b, PERL_FUNCTION)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_FUNCTION);
    exit_section_(b, m, FUNCTION, r);
    return r;
  }

  /* ********************************************************** */
  // function call_params_any ?
  public static boolean function_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call")) return false;
    if (!nextTokenIs(b, PERL_FUNCTION)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = function(b, l + 1);
    r = r && function_call_1(b, l + 1);
    exit_section_(b, m, FUNCTION_CALL, r);
    return r;
  }

  // call_params_any ?
  private static boolean function_call_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_1")) return false;
    call_params_any(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // PERL_FUNCTION call_params_any ?
  public static boolean function_call_any(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_any")) return false;
    if (!nextTokenIs(b, PERL_FUNCTION)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_FUNCTION);
    r = r && function_call_any_1(b, l + 1);
    exit_section_(b, m, FUNCTION_CALL_ANY, r);
    return r;
  }

  // call_params_any ?
  private static boolean function_call_any_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_any_1")) return false;
    call_params_any(b, l + 1);
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
  // 'sub' block ';'
  public static boolean function_definition_anon(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_definition_anon")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<function definition anon>");
    r = consumeToken(b, "sub");
    r = r && block(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    exit_section_(b, l, m, FUNCTION_DEFINITION_ANON, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'sub' function block ';' ?
  public static boolean function_definition_named(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_definition_named")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<function definition named>");
    r = consumeToken(b, "sub");
    r = r && function(b, l + 1);
    r = r && block(b, l + 1);
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
  // PERL_GLOB
  public static boolean glob(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "glob")) return false;
    if (!nextTokenIs(b, PERL_GLOB)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_GLOB);
    exit_section_(b, m, GLOB, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_HASH
  public static boolean hash(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash")) return false;
    if (!nextTokenIs(b, PERL_HASH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_HASH);
    exit_section_(b, m, HASH, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_SIGIL_HASH '{' scalar_value '}'
  static boolean hash_dereference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_dereference")) return false;
    if (!nextTokenIs(b, PERL_SIGIL_HASH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SIGIL_HASH);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && scalar_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // array '{' (array_value | scalar_value) '{'
  static boolean hash_slice(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_slice")) return false;
    if (!nextTokenIs(b, PERL_ARRAY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = array(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && hash_slice_2(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // array_value | scalar_value
  private static boolean hash_slice_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_slice_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = array_value(b, l + 1);
    if (!r) r = scalar_value(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // hash_dereference | hash
  public static boolean hash_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_value")) return false;
    if (!nextTokenIs(b, "<hash value>", PERL_HASH, PERL_SIGIL_HASH)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<hash value>");
    r = hash_dereference(b, l + 1);
    if (!r) r = hash(b, l + 1);
    exit_section_(b, l, m, HASH_VALUE, r, false, null);
    return r;
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
  // code_line | block
  public static boolean if_branch(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_branch")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<if branch>");
    r = code_line(b, l + 1);
    if (!r) r = block(b, l + 1);
    exit_section_(b, l, m, IF_BRANCH, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '(' expression ')' if_branch
  public static boolean if_branch_conditional(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_branch_conditional")) return false;
    if (!nextTokenIs(b, PERL_LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && expression(b, l + 1);
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
  // PERL_STRING_MULTILINE PERL_MULTILINE_MARKER
  static boolean multiline_string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiline_string")) return false;
    if (!nextTokenIs(b, PERL_STRING_MULTILINE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, PERL_STRING_MULTILINE, PERL_MULTILINE_MARKER);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // scalar_value_var '->' function_call_any
  public static boolean object_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_call")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<object call>");
    r = scalar_value_var(b, l + 1);
    r = r && consumeToken(b, PERL_DEREFERENCE);
    r = r && function_call_any(b, l + 1);
    exit_section_(b, l, m, OBJECT_CALL, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PERL_PACKAGE
  public static boolean package_bare(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_bare")) return false;
    if (!nextTokenIs(b, PERL_PACKAGE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_PACKAGE);
    exit_section_(b, m, PACKAGE_BARE, r);
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
  // function_definition | if_block | code_line_valid | PERL_POD | PERL_COMMENT | PERL_COMMENT_BLOCK | block | code_line_invalid
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
    if (!r) r = block(b, l + 1);
    if (!r) r = code_line_invalid(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'package' package_bare PERL_VERSION ? (block | ';' package_item * )
  public static boolean package_namespace(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package namespace>");
    r = consumeToken(b, "package");
    r = r && package_bare(b, l + 1);
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

  // block | ';' package_item *
  private static boolean package_namespace_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = block(b, l + 1);
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
  // package_bare '->' function_call_any
  public static boolean package_object_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_object_call")) return false;
    if (!nextTokenIs(b, PERL_PACKAGE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_bare(b, l + 1);
    r = r && consumeToken(b, PERL_DEREFERENCE);
    r = r && function_call_any(b, l + 1);
    exit_section_(b, m, PACKAGE_OBJECT_CALL, r);
    return r;
  }

  /* ********************************************************** */
  // 'require' (package_bare | PERL_VERSION | string) ';'
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

  // package_bare | PERL_VERSION | string
  private static boolean package_require_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_require_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_bare(b, l + 1);
    if (!r) r = consumeToken(b, PERL_VERSION);
    if (!r) r = string(b, l + 1);
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
  // package_bare '::' function_call_any
  public static boolean package_static_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_static_call")) return false;
    if (!nextTokenIs(b, PERL_PACKAGE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_bare(b, l + 1);
    r = r && consumeToken(b, PERL_DEPACKAGE);
    r = r && function_call_any(b, l + 1);
    exit_section_(b, m, PACKAGE_STATIC_CALL, r);
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
  // package_bare PERL_VERSION call_params ?
  //     | package_bare call_params ?
  //     | PERL_VERSION
  static boolean package_use_arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments")) return false;
    if (!nextTokenIs(b, "", PERL_PACKAGE, PERL_VERSION)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_use_arguments_0(b, l + 1);
    if (!r) r = package_use_arguments_1(b, l + 1);
    if (!r) r = consumeToken(b, PERL_VERSION);
    exit_section_(b, m, null, r);
    return r;
  }

  // package_bare PERL_VERSION call_params ?
  private static boolean package_use_arguments_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_bare(b, l + 1);
    r = r && consumeToken(b, PERL_VERSION);
    r = r && package_use_arguments_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // call_params ?
  private static boolean package_use_arguments_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments_0_2")) return false;
    call_params(b, l + 1);
    return true;
  }

  // package_bare call_params ?
  private static boolean package_use_arguments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_bare(b, l + 1);
    r = r && package_use_arguments_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // call_params ?
  private static boolean package_use_arguments_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments_1_1")) return false;
    call_params(b, l + 1);
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
  // PERL_SCALAR
  public static boolean scalar(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar")) return false;
    if (!nextTokenIs(b, PERL_SCALAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SCALAR);
    exit_section_(b, m, SCALAR, r);
    return r;
  }

  /* ********************************************************** */
  // '[' array_value ']'
  static boolean scalar_anon_array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_anon_array")) return false;
    if (!nextTokenIs(b, PERL_LBRACK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LBRACK);
    r = r && array_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACK);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '{' array_value '}'
  static boolean scalar_anon_hash(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_anon_hash")) return false;
    if (!nextTokenIs(b, PERL_LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LBRACE);
    r = r && array_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // scalar '[' scalar_value ']'
  static boolean scalar_array_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_array_element")) return false;
    if (!nextTokenIs(b, PERL_SCALAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scalar(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACK);
    r = r && scalar_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACK);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_SIGIL_SCALAR '{' scalar_value '}'
  static boolean scalar_dereference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_dereference")) return false;
    if (!nextTokenIs(b, PERL_SIGIL_SCALAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SIGIL_SCALAR);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && scalar_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // function_call
  static boolean scalar_function_result(PsiBuilder b, int l) {
    return function_call(b, l + 1);
  }

  /* ********************************************************** */
  // scalar '{' scalar_value '}'
  static boolean scalar_hash_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_hash_element")) return false;
    if (!nextTokenIs(b, PERL_SCALAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scalar(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && scalar_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // string | PERL_NUMBER | PERL_VERSION | PERL_MULTILINE_MARKER| scalar_anon_array | scalar_anon_hash | scalar_value_var
  public static boolean scalar_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, "<scalar value>");
    r = string(b, l + 1);
    if (!r) r = consumeToken(b, PERL_NUMBER);
    if (!r) r = consumeToken(b, PERL_VERSION);
    if (!r) r = consumeToken(b, PERL_MULTILINE_MARKER);
    if (!r) r = scalar_anon_array(b, l + 1);
    if (!r) r = scalar_anon_hash(b, l + 1);
    if (!r) r = scalar_value_var(b, l + 1);
    exit_section_(b, l, m, SCALAR_VALUE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // scalar_dereference | scalar_hash_element | scalar_array_element | scalar | scalar_function_result
  static boolean scalar_value_var(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_value_var")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scalar_dereference(b, l + 1);
    if (!r) r = scalar_hash_element(b, l + 1);
    if (!r) r = scalar_array_element(b, l + 1);
    if (!r) r = scalar(b, l + 1);
    if (!r) r = scalar_function_result(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_STRING
  public static boolean string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string")) return false;
    if (!nextTokenIs(b, PERL_STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_STRING);
    exit_section_(b, m, STRING, r);
    return r;
  }

  /* ********************************************************** */
  // '(' code_line_elements ')'
  public static boolean subexpression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "subexpression")) return false;
    if (!nextTokenIs(b, PERL_LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && code_line_elements(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, SUBEXPRESSION, r);
    return r;
  }

}
