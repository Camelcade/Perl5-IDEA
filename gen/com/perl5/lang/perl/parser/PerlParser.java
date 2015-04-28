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
    else if (t == ARRAY_DEREFERENCE) {
      r = array_dereference(b, 0);
    }
    else if (t == ARRAY_ELEMENT) {
      r = array_element(b, 0);
    }
    else if (t == ARRAY_ELEMENTS) {
      r = array_elements(b, 0);
    }
    else if (t == ARRAY_SLICE) {
      r = array_slice(b, 0);
    }
    else if (t == ARRAY_VALUE) {
      r = array_value(b, 0);
    }
    else if (t == BLOCK) {
      r = block(b, 0);
    }
    else if (t == BLOCK_ITEM) {
      r = block_item(b, 0);
    }
    else if (t == CODE_CHUNK_INVALID) {
      r = code_chunk_invalid(b, 0);
    }
    else if (t == CODE_CHUNK_VALID) {
      r = code_chunk_valid(b, 0);
    }
    else if (t == CODE_LINE) {
      r = code_line(b, 0);
    }
    else if (t == CODE_LINE_ELEMENT) {
      r = code_line_element(b, 0);
    }
    else if (t == CODE_LINE_ELEMENTS) {
      r = code_line_elements(b, 0);
    }
    else if (t == CODE_LINE_INVALID_ELEMENT) {
      r = code_line_invalid_element(b, 0);
    }
    else if (t == CONTROLS) {
      r = controls(b, 0);
    }
    else if (t == EVAL) {
      r = eval(b, 0);
    }
    else if (t == EVAL_INVALID) {
      r = eval_invalid(b, 0);
    }
    else if (t == EXPR) {
      r = expr(b, 0);
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
    else if (t == FUNCTION_DEFINITION) {
      r = function_definition(b, 0);
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
    else if (t == HASH_DEREFERENCE) {
      r = hash_dereference(b, 0);
    }
    else if (t == HASH_SLICE) {
      r = hash_slice(b, 0);
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
    else if (t == LOCAL_DEFINITION) {
      r = local_definition(b, 0);
    }
    else if (t == METHOD_CALL) {
      r = method_call(b, 0);
    }
    else if (t == MULTILINE_MARKER) {
      r = multiline_marker(b, 0);
    }
    else if (t == MULTILINE_STRING) {
      r = multiline_string(b, 0);
    }
    else if (t == MY_DEFINITION) {
      r = my_definition(b, 0);
    }
    else if (t == OBJECT_METHOD_CALL) {
      r = object_method_call(b, 0);
    }
    else if (t == OP_10) {
      r = op_10(b, 0);
    }
    else if (t == OP_11) {
      r = op_11(b, 0);
    }
    else if (t == OP_12) {
      r = op_12(b, 0);
    }
    else if (t == OP_13) {
      r = op_13(b, 0);
    }
    else if (t == OP_14) {
      r = op_14(b, 0);
    }
    else if (t == OP_15) {
      r = op_15(b, 0);
    }
    else if (t == OP_16) {
      r = op_16(b, 0);
    }
    else if (t == OP_17) {
      r = op_17(b, 0);
    }
    else if (t == OP_18) {
      r = op_18(b, 0);
    }
    else if (t == OP_19) {
      r = op_19(b, 0);
    }
    else if (t == OP_20) {
      r = op_20(b, 0);
    }
    else if (t == OP_21) {
      r = op_21(b, 0);
    }
    else if (t == OP_22) {
      r = op_22(b, 0);
    }
    else if (t == OP_23) {
      r = op_23(b, 0);
    }
    else if (t == OP_24) {
      r = op_24(b, 0);
    }
    else if (t == OP_3) {
      r = op_3(b, 0);
    }
    else if (t == OP_4) {
      r = op_4(b, 0);
    }
    else if (t == OP_5) {
      r = op_5(b, 0);
    }
    else if (t == OP_6) {
      r = op_6(b, 0);
    }
    else if (t == OP_7) {
      r = op_7(b, 0);
    }
    else if (t == OP_8) {
      r = op_8(b, 0);
    }
    else if (t == OP_9) {
      r = op_9(b, 0);
    }
    else if (t == OP_RIGHT_ARRAY_OPERAND) {
      r = op_right_array_operand(b, 0);
    }
    else if (t == OP_RIGHT_SCALAR_OPERAND) {
      r = op_right_scalar_operand(b, 0);
    }
    else if (t == OUR_DEFINITION) {
      r = our_definition(b, 0);
    }
    else if (t == PACKAGE_BARE) {
      r = package_bare(b, 0);
    }
    else if (t == PACKAGE_DEFINITION) {
      r = package_definition(b, 0);
    }
    else if (t == PACKAGE_DEFINITION_INVALID) {
      r = package_definition_invalid(b, 0);
    }
    else if (t == PACKAGE_FUNCTION_CALL) {
      r = package_function_call(b, 0);
    }
    else if (t == PACKAGE_ITEM) {
      r = package_item(b, 0);
    }
    else if (t == PACKAGE_METHOD_CALL) {
      r = package_method_call(b, 0);
    }
    else if (t == PACKAGE_NO) {
      r = package_no(b, 0);
    }
    else if (t == PACKAGE_NO_INVALID) {
      r = package_no_invalid(b, 0);
    }
    else if (t == PACKAGE_REQUIRE) {
      r = package_require(b, 0);
    }
    else if (t == PACKAGE_REQUIRE_INVALID) {
      r = package_require_invalid(b, 0);
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
    else if (t == SCALAR) {
      r = scalar(b, 0);
    }
    else if (t == SCALAR_ANON_ARRAY) {
      r = scalar_anon_array(b, 0);
    }
    else if (t == SCALAR_ANON_HASH) {
      r = scalar_anon_hash(b, 0);
    }
    else if (t == SCALAR_ARRAY_ELEMENT) {
      r = scalar_array_element(b, 0);
    }
    else if (t == SCALAR_DEREFERENCE) {
      r = scalar_dereference(b, 0);
    }
    else if (t == SCALAR_FUNCTION_RESULT) {
      r = scalar_function_result(b, 0);
    }
    else if (t == SCALAR_HASH_ELEMENT) {
      r = scalar_hash_element(b, 0);
    }
    else if (t == SCALAR_VALUE) {
      r = scalar_value(b, 0);
    }
    else if (t == SCALAR_VALUE_DETERMINED) {
      r = scalar_value_determined(b, 0);
    }
    else if (t == SCALAR_VALUE_MUTABLE) {
      r = scalar_value_mutable(b, 0);
    }
    else if (t == STRING) {
      r = string(b, 0);
    }
    else if (t == SUBEXPRESSION) {
      r = subexpression(b, 0);
    }
    else if (t == VARIABLE) {
      r = variable(b, 0);
    }
    else if (t == VARIABLE_DEFINITION) {
      r = variable_definition(b, 0);
    }
    else if (t == VARIABLE_DEFINITION_ARGUMENTS) {
      r = variable_definition_arguments(b, 0);
    }
    else if (t == VARIABLES) {
      r = variables(b, 0);
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
  public static boolean array_dereference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_dereference")) return false;
    if (!nextTokenIs(b, PERL_SIGIL_ARRAY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SIGIL_ARRAY);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && scalar_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, ARRAY_DEREFERENCE, r);
    return r;
  }

  /* ********************************************************** */
  // array_dereference
  //     | array_slice
  //     | hash_slice
  //     | hash_value
  //     | array
  //     | scalar_value
  public static boolean array_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_element")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<array element>");
    r = array_dereference(b, l + 1);
    if (!r) r = array_slice(b, l + 1);
    if (!r) r = hash_slice(b, l + 1);
    if (!r) r = hash_value(b, l + 1);
    if (!r) r = array(b, l + 1);
    if (!r) r = scalar_value(b, l + 1);
    exit_section_(b, l, m, ARRAY_ELEMENT, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // array_element (PERL_COMMA array_element )*
  public static boolean array_elements(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_elements")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<array elements>");
    r = array_element(b, l + 1);
    r = r && array_elements_1(b, l + 1);
    exit_section_(b, l, m, ARRAY_ELEMENTS, r, false, null);
    return r;
  }

  // (PERL_COMMA array_element )*
  private static boolean array_elements_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_elements_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!array_elements_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_elements_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // PERL_COMMA array_element
  private static boolean array_elements_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_elements_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_COMMA);
    r = r && array_element(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // array '[' (array_value | scalar_value) ']'
  public static boolean array_slice(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_slice")) return false;
    if (!nextTokenIs(b, PERL_ARRAY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = array(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACK);
    r = r && array_slice_2(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACK);
    exit_section_(b, m, ARRAY_SLICE, r);
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
  // array_elements
  //     | '(' array_elements* ')'
  public static boolean array_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<array value>");
    r = array_elements(b, l + 1);
    if (!r) r = array_value_1(b, l + 1);
    exit_section_(b, l, m, ARRAY_VALUE, r, false, null);
    return r;
  }

  // '(' array_elements* ')'
  private static boolean array_value_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_value_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && array_value_1_1(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // array_elements*
  private static boolean array_value_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_value_1_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!array_elements(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_value_1_1", c)) break;
      c = current_position_(b);
    }
    return true;
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
  // package_definition | package_item | package_definition_invalid
  public static boolean block_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_item")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<block item>");
    r = package_definition(b, l + 1);
    if (!r) r = package_item(b, l + 1);
    if (!r) r = package_definition_invalid(b, l + 1);
    exit_section_(b, l, m, BLOCK_ITEM, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // package_use_invalid
  //     | package_no_invalid
  //     | package_require_invalid
  //     | eval_invalid
  public static boolean code_chunk_invalid(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_chunk_invalid")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<code chunk invalid>");
    r = package_use_invalid(b, l + 1);
    if (!r) r = package_no_invalid(b, l + 1);
    if (!r) r = package_require_invalid(b, l + 1);
    if (!r) r = eval_invalid(b, l + 1);
    exit_section_(b, l, m, CODE_CHUNK_INVALID, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // eval
  //     | function_definition
  //     | package_use
  //     | package_no
  //     | package_require
  //     | if_block
  //     | block
  //     | code_line
  public static boolean code_chunk_valid(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_chunk_valid")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<code chunk valid>");
    r = eval(b, l + 1);
    if (!r) r = function_definition(b, l + 1);
    if (!r) r = package_use(b, l + 1);
    if (!r) r = package_no(b, l + 1);
    if (!r) r = package_require(b, l + 1);
    if (!r) r = if_block(b, l + 1);
    if (!r) r = block(b, l + 1);
    if (!r) r = code_line(b, l + 1);
    exit_section_(b, l, m, CODE_CHUNK_VALID, r, false, null);
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
  // variable_definition
  //     | method_call
  //     | function_call
  //     | function
  //     | array_value
  //     | hash_value
  //     | scalar_value
  //     | glob
  //     | multiline_string
  //     | multiline_marker
  //     | PERL_OPERATOR
  public static boolean code_line_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_element")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<code line element>");
    r = variable_definition(b, l + 1);
    if (!r) r = method_call(b, l + 1);
    if (!r) r = function_call(b, l + 1);
    if (!r) r = function(b, l + 1);
    if (!r) r = array_value(b, l + 1);
    if (!r) r = hash_value(b, l + 1);
    if (!r) r = scalar_value(b, l + 1);
    if (!r) r = glob(b, l + 1);
    if (!r) r = multiline_string(b, l + 1);
    if (!r) r = multiline_marker(b, l + 1);
    if (!r) r = consumeToken(b, PERL_OPERATOR);
    exit_section_(b, l, m, CODE_LINE_ELEMENT, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // code_line_element +
  public static boolean code_line_elements(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_elements")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<code line elements>");
    r = code_line_element(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!code_line_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "code_line_elements", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, l, m, CODE_LINE_ELEMENTS, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // code_line_element | package_bare | PERL_VERSION | controls
  public static boolean code_line_invalid_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_invalid_element")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<code line invalid element>");
    r = code_line_element(b, l + 1);
    if (!r) r = package_bare(b, l + 1);
    if (!r) r = consumeToken(b, PERL_VERSION);
    if (!r) r = controls(b, l + 1);
    exit_section_(b, l, m, CODE_LINE_INVALID_ELEMENT, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ',' | '[' | ']' | '(' | ')'
  public static boolean controls(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "controls")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<controls>");
    r = consumeToken(b, PERL_COMMA);
    if (!r) r = consumeToken(b, PERL_LBRACK);
    if (!r) r = consumeToken(b, PERL_RBRACK);
    if (!r) r = consumeToken(b, PERL_LPAREN);
    if (!r) r = consumeToken(b, PERL_RPAREN);
    exit_section_(b, l, m, CONTROLS, r, false, null);
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
  // op_24
  //     | op_23
  //     | op_22
  //     | op_21
  // //    | op_20
  //     | op_19
  //     | op_18
  //     | op_17
  //     | op_16
  //     | op_15
  //     | op_14
  //     | op_13
  //     | op_12
  //     | op_11
  //     | op_10
  //     | op_9
  //     | op_8
  //     | op_7
  //     | op_6
  //     | op_5
  //     | op_4
  //     | op_3
  // //    | op_2
  // //    | op_1
  //     | scalar_value
  //     | array_value
  public static boolean expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<expr>");
    r = op_24(b, l + 1);
    if (!r) r = op_23(b, l + 1);
    if (!r) r = op_22(b, l + 1);
    if (!r) r = op_21(b, l + 1);
    if (!r) r = op_19(b, l + 1);
    if (!r) r = op_18(b, l + 1);
    if (!r) r = op_17(b, l + 1);
    if (!r) r = op_16(b, l + 1);
    if (!r) r = op_15(b, l + 1);
    if (!r) r = op_14(b, l + 1);
    if (!r) r = op_13(b, l + 1);
    if (!r) r = op_12(b, l + 1);
    if (!r) r = op_11(b, l + 1);
    if (!r) r = op_10(b, l + 1);
    if (!r) r = op_9(b, l + 1);
    if (!r) r = op_8(b, l + 1);
    if (!r) r = op_7(b, l + 1);
    if (!r) r = op_6(b, l + 1);
    if (!r) r = op_5(b, l + 1);
    if (!r) r = op_4(b, l + 1);
    if (!r) r = op_3(b, l + 1);
    if (!r) r = scalar_value(b, l + 1);
    if (!r) r = array_value(b, l + 1);
    exit_section_(b, l, m, EXPR, r, false, null);
    return r;
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
  // PERL_FUNCTION array_value ?
  public static boolean function_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call")) return false;
    if (!nextTokenIs(b, PERL_FUNCTION)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_FUNCTION);
    r = r && function_call_1(b, l + 1);
    exit_section_(b, m, FUNCTION_CALL, r);
    return r;
  }

  // array_value ?
  private static boolean function_call_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_1")) return false;
    array_value(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // function_definition_named | function_definition_anon
  public static boolean function_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_definition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<function definition>");
    r = function_definition_named(b, l + 1);
    if (!r) r = function_definition_anon(b, l + 1);
    exit_section_(b, l, m, FUNCTION_DEFINITION, r, false, null);
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
  public static boolean hash_dereference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_dereference")) return false;
    if (!nextTokenIs(b, PERL_SIGIL_HASH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SIGIL_HASH);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && scalar_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, HASH_DEREFERENCE, r);
    return r;
  }

  /* ********************************************************** */
  // array '{' (array_value | scalar_value) '{'
  public static boolean hash_slice(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_slice")) return false;
    if (!nextTokenIs(b, PERL_ARRAY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = array(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && hash_slice_2(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACE);
    exit_section_(b, m, HASH_SLICE, r);
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
  // 'local' variable_definition_arguments
  public static boolean local_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "local_definition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<local definition>");
    r = consumeToken(b, "local");
    r = r && variable_definition_arguments(b, l + 1);
    exit_section_(b, l, m, LOCAL_DEFINITION, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // package_function_call
  //     | package_method_call
  //     | object_method_call
  public static boolean method_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_call")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<method call>");
    r = package_function_call(b, l + 1);
    if (!r) r = package_method_call(b, l + 1);
    if (!r) r = object_method_call(b, l + 1);
    exit_section_(b, l, m, METHOD_CALL, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PERL_MULTILINE_MARKER
  //     | PERL_MULTILINE_MARKER_HTML
  //     | PERL_MULTILINE_MARKER_XHTML
  //     | PERL_MULTILINE_MARKER_XML
  public static boolean multiline_marker(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiline_marker")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<multiline marker>");
    r = consumeToken(b, PERL_MULTILINE_MARKER);
    if (!r) r = consumeToken(b, PERL_MULTILINE_MARKER_HTML);
    if (!r) r = consumeToken(b, PERL_MULTILINE_MARKER_XHTML);
    if (!r) r = consumeToken(b, PERL_MULTILINE_MARKER_XML);
    exit_section_(b, l, m, MULTILINE_MARKER, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PERL_STRING_MULTILINE PERL_MULTILINE_MARKER
  public static boolean multiline_string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiline_string")) return false;
    if (!nextTokenIs(b, PERL_STRING_MULTILINE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, PERL_STRING_MULTILINE, PERL_MULTILINE_MARKER);
    exit_section_(b, m, MULTILINE_STRING, r);
    return r;
  }

  /* ********************************************************** */
  // 'my' variable_definition_arguments
  public static boolean my_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "my_definition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<my definition>");
    r = consumeToken(b, "my");
    r = r && variable_definition_arguments(b, l + 1);
    exit_section_(b, l, m, MY_DEFINITION, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // scalar_value_mutable '->' function_call
  public static boolean object_method_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_method_call")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<object method call>");
    r = scalar_value_mutable(b, l + 1);
    r = r && consumeToken(b, PERL_DEREFERENCE);
    r = r && function_call(b, l + 1);
    exit_section_(b, l, m, OBJECT_METHOD_CALL, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ('not'|'defined'|'ref'|'exists') expr
  public static boolean op_10(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_10")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 10>");
    r = op_10_0(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_10, r, false, null);
    return r;
  }

  // 'not'|'defined'|'ref'|'exists'
  private static boolean op_10_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_10_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "not");
    if (!r) r = consumeToken(b, "defined");
    if (!r) r = consumeToken(b, "ref");
    if (!r) r = consumeToken(b, "exists");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expr ('>='|'<='|'<'|'>'|'lt'|'gt'|'le'|'ge') expr
  public static boolean op_11(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_11")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 11>");
    r = expr(b, l + 1);
    r = r && op_11_1(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_11, r, false, null);
    return r;
  }

  // '>='|'<='|'<'|'>'|'lt'|'gt'|'le'|'ge'
  private static boolean op_11_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_11_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ">=");
    if (!r) r = consumeToken(b, "<=");
    if (!r) r = consumeToken(b, "<");
    if (!r) r = consumeToken(b, ">");
    if (!r) r = consumeToken(b, "lt");
    if (!r) r = consumeToken(b, "gt");
    if (!r) r = consumeToken(b, "le");
    if (!r) r = consumeToken(b, "ge");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expr ('=='|'!='|'<=>'|'eq'|'ne'|'cmp'|'~~') expr
  public static boolean op_12(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_12")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 12>");
    r = expr(b, l + 1);
    r = r && op_12_1(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_12, r, false, null);
    return r;
  }

  // '=='|'!='|'<=>'|'eq'|'ne'|'cmp'|'~~'
  private static boolean op_12_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_12_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "==");
    if (!r) r = consumeToken(b, "!=");
    if (!r) r = consumeToken(b, "<=>");
    if (!r) r = consumeToken(b, "eq");
    if (!r) r = consumeToken(b, "ne");
    if (!r) r = consumeToken(b, "cmp");
    if (!r) r = consumeToken(b, "~~");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expr '&' expr
  public static boolean op_13(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_13")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 13>");
    r = expr(b, l + 1);
    r = r && consumeToken(b, "&");
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_13, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // expr ('|'|'^') expr
  public static boolean op_14(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_14")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 14>");
    r = expr(b, l + 1);
    r = r && op_14_1(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_14, r, false, null);
    return r;
  }

  // '|'|'^'
  private static boolean op_14_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_14_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "|");
    if (!r) r = consumeToken(b, "^");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expr '&&' expr
  public static boolean op_15(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_15")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 15>");
    r = expr(b, l + 1);
    r = r && consumeToken(b, "&&");
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_15, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // expr ('||'|'//') expr
  public static boolean op_16(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_16")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 16>");
    r = expr(b, l + 1);
    r = r && op_16_1(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_16, r, false, null);
    return r;
  }

  // '||'|'//'
  private static boolean op_16_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_16_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "||");
    if (!r) r = consumeToken(b, "//");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expr ('..'|'...') expr
  public static boolean op_17(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_17")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 17>");
    r = expr(b, l + 1);
    r = r && op_17_1(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_17, r, false, null);
    return r;
  }

  // '..'|'...'
  private static boolean op_17_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_17_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "..");
    if (!r) r = consumeToken(b, "...");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expr '?' expr ':' expr
  public static boolean op_18(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_18")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 18>");
    r = expr(b, l + 1);
    r = r && consumeToken(b, "?");
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, ":");
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_18, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // expr ('='|'+='|'-='|'*=') expr
  public static boolean op_19(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_19")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 19>");
    r = expr(b, l + 1);
    r = r && op_19_1(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_19, r, false, null);
    return r;
  }

  // '='|'+='|'-='|'*='
  private static boolean op_19_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_19_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "=");
    if (!r) r = consumeToken(b, "+=");
    if (!r) r = consumeToken(b, "-=");
    if (!r) r = consumeToken(b, "*=");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expr	(',' | '=>') expr
  public static boolean op_20(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_20")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 20>");
    r = expr(b, l + 1);
    r = r && op_20_1(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_20, r, false, null);
    return r;
  }

  // ',' | '=>'
  private static boolean op_20_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_20_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_COMMA);
    if (!r) r = consumeToken(b, "=>");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ('scalar') expr
  public static boolean op_21(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_21")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 21>");
    r = op_21_0(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_21, r, false, null);
    return r;
  }

  // ('scalar')
  private static boolean op_21_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_21_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "scalar");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'not' expr
  public static boolean op_22(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_22")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 22>");
    r = consumeToken(b, "not");
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_22, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // expr 'and' expr ':' expr
  public static boolean op_23(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_23")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 23>");
    r = expr(b, l + 1);
    r = r && consumeToken(b, "and");
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, ":");
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_23, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // expr ('or'|'xor') expr ':' expr
  public static boolean op_24(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_24")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 24>");
    r = expr(b, l + 1);
    r = r && op_24_1(b, l + 1);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, ":");
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_24, r, false, null);
    return r;
  }

  // 'or'|'xor'
  private static boolean op_24_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_24_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "or");
    if (!r) r = consumeToken(b, "xor");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (('++'|'--') expr) | ( expr ('++'|'--'))
  public static boolean op_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_3")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 3>");
    r = op_3_0(b, l + 1);
    if (!r) r = op_3_1(b, l + 1);
    exit_section_(b, l, m, OP_3, r, false, null);
    return r;
  }

  // ('++'|'--') expr
  private static boolean op_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = op_3_0_0(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '++'|'--'
  private static boolean op_3_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_3_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "++");
    if (!r) r = consumeToken(b, "--");
    exit_section_(b, m, null, r);
    return r;
  }

  // expr ('++'|'--')
  private static boolean op_3_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_3_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1);
    r = r && op_3_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '++'|'--'
  private static boolean op_3_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_3_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "++");
    if (!r) r = consumeToken(b, "--");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expr '**' expr
  public static boolean op_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_4")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 4>");
    r = expr(b, l + 1);
    r = r && consumeToken(b, "**");
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_4, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ('\' | '~'| '!'| '+' | '-') expr
  public static boolean op_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_5")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 5>");
    r = op_5_0(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_5, r, false, null);
    return r;
  }

  // '\' | '~'| '!'| '+' | '-'
  private static boolean op_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "\\");
    if (!r) r = consumeToken(b, "~");
    if (!r) r = consumeToken(b, "!");
    if (!r) r = consumeToken(b, "+");
    if (!r) r = consumeToken(b, "-");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expr ('=~'|'!~') expr
  public static boolean op_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_6")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 6>");
    r = expr(b, l + 1);
    r = r && op_6_1(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_6, r, false, null);
    return r;
  }

  // '=~'|'!~'
  private static boolean op_6_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_6_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "=~");
    if (!r) r = consumeToken(b, "!~");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expr ('*'|'/'|'%'|'x') expr
  public static boolean op_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_7")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 7>");
    r = expr(b, l + 1);
    r = r && op_7_1(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_7, r, false, null);
    return r;
  }

  // '*'|'/'|'%'|'x'
  private static boolean op_7_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_7_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "*");
    if (!r) r = consumeToken(b, "/");
    if (!r) r = consumeToken(b, PERL_SIGIL_HASH);
    if (!r) r = consumeToken(b, "x");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expr ('+'|'-'|'.') expr
  public static boolean op_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_8")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 8>");
    r = expr(b, l + 1);
    r = r && op_8_1(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_8, r, false, null);
    return r;
  }

  // '+'|'-'|'.'
  private static boolean op_8_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_8_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "+");
    if (!r) r = consumeToken(b, "-");
    if (!r) r = consumeToken(b, ".");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expr ('<<'|'>>') expr
  public static boolean op_9(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_9")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 9>");
    r = expr(b, l + 1);
    r = r && op_9_1(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, OP_9, r, false, null);
    return r;
  }

  // '<<'|'>>'
  private static boolean op_9_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_9_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "<<");
    if (!r) r = consumeToken(b, ">>");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // array_value
  public static boolean op_right_array_operand(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_right_array_operand")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op right array operand>");
    r = array_value(b, l + 1);
    exit_section_(b, l, m, OP_RIGHT_ARRAY_OPERAND, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '(' scalar_value ')' | scalar_value
  public static boolean op_right_scalar_operand(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_right_scalar_operand")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op right scalar operand>");
    r = op_right_scalar_operand_0(b, l + 1);
    if (!r) r = scalar_value(b, l + 1);
    exit_section_(b, l, m, OP_RIGHT_SCALAR_OPERAND, r, false, null);
    return r;
  }

  // '(' scalar_value ')'
  private static boolean op_right_scalar_operand_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_right_scalar_operand_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && scalar_value(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'our' variable_definition_arguments
  public static boolean our_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "our_definition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<our definition>");
    r = consumeToken(b, "our");
    r = r && variable_definition_arguments(b, l + 1);
    exit_section_(b, l, m, OUR_DEFINITION, r, false, null);
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
  // 'package' package_bare PERL_VERSION ? (block | ';' package_item * )
  public static boolean package_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_definition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package definition>");
    r = consumeToken(b, "package");
    r = r && package_bare(b, l + 1);
    r = r && package_definition_2(b, l + 1);
    r = r && package_definition_3(b, l + 1);
    exit_section_(b, l, m, PACKAGE_DEFINITION, r, false, null);
    return r;
  }

  // PERL_VERSION ?
  private static boolean package_definition_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_definition_2")) return false;
    consumeToken(b, PERL_VERSION);
    return true;
  }

  // block | ';' package_item *
  private static boolean package_definition_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_definition_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = block(b, l + 1);
    if (!r) r = package_definition_3_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ';' package_item *
  private static boolean package_definition_3_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_definition_3_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SEMI);
    r = r && package_definition_3_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // package_item *
  private static boolean package_definition_3_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_definition_3_1_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!package_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "package_definition_3_1_1", c)) break;
      c = current_position_(b);
    }
    return true;
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
  // package_bare '::' function_call
  public static boolean package_function_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_function_call")) return false;
    if (!nextTokenIs(b, PERL_PACKAGE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_bare(b, l + 1);
    r = r && consumeToken(b, PERL_DEPACKAGE);
    r = r && function_call(b, l + 1);
    exit_section_(b, m, PACKAGE_FUNCTION_CALL, r);
    return r;
  }

  /* ********************************************************** */
  // code_chunk_valid
  //      | PERL_POD
  //      | PERL_COMMENT
  //      | PERL_COMMENT_BLOCK
  public static boolean package_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_item")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package item>");
    r = code_chunk_valid(b, l + 1);
    if (!r) r = consumeToken(b, PERL_POD);
    if (!r) r = consumeToken(b, PERL_COMMENT);
    if (!r) r = consumeToken(b, PERL_COMMENT_BLOCK);
    exit_section_(b, l, m, PACKAGE_ITEM, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // package_bare '->' function_call
  public static boolean package_method_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_method_call")) return false;
    if (!nextTokenIs(b, PERL_PACKAGE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_bare(b, l + 1);
    r = r && consumeToken(b, PERL_DEREFERENCE);
    r = r && function_call(b, l + 1);
    exit_section_(b, m, PACKAGE_METHOD_CALL, r);
    return r;
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
  // package_bare PERL_VERSION array_value ?
  //     | package_bare array_value ?
  //     | PERL_VERSION
  public static boolean package_use_arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments")) return false;
    if (!nextTokenIs(b, "<package use arguments>", PERL_PACKAGE, PERL_VERSION)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package use arguments>");
    r = package_use_arguments_0(b, l + 1);
    if (!r) r = package_use_arguments_1(b, l + 1);
    if (!r) r = consumeToken(b, PERL_VERSION);
    exit_section_(b, l, m, PACKAGE_USE_ARGUMENTS, r, false, null);
    return r;
  }

  // package_bare PERL_VERSION array_value ?
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

  // array_value ?
  private static boolean package_use_arguments_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments_0_2")) return false;
    array_value(b, l + 1);
    return true;
  }

  // package_bare array_value ?
  private static boolean package_use_arguments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_bare(b, l + 1);
    r = r && package_use_arguments_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // array_value ?
  private static boolean package_use_arguments_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments_1_1")) return false;
    array_value(b, l + 1);
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
  // '[' array_value * ']'
  public static boolean scalar_anon_array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_anon_array")) return false;
    if (!nextTokenIs(b, PERL_LBRACK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LBRACK);
    r = r && scalar_anon_array_1(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACK);
    exit_section_(b, m, SCALAR_ANON_ARRAY, r);
    return r;
  }

  // array_value *
  private static boolean scalar_anon_array_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_anon_array_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!array_value(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scalar_anon_array_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // '{' array_value * '}'
  public static boolean scalar_anon_hash(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_anon_hash")) return false;
    if (!nextTokenIs(b, PERL_LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LBRACE);
    r = r && scalar_anon_hash_1(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, SCALAR_ANON_HASH, r);
    return r;
  }

  // array_value *
  private static boolean scalar_anon_hash_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_anon_hash_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!array_value(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "scalar_anon_hash_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // scalar '[' scalar_value ']'
  public static boolean scalar_array_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_array_element")) return false;
    if (!nextTokenIs(b, PERL_SCALAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scalar(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACK);
    r = r && scalar_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACK);
    exit_section_(b, m, SCALAR_ARRAY_ELEMENT, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_SIGIL_SCALAR '{' scalar_value '}'
  public static boolean scalar_dereference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_dereference")) return false;
    if (!nextTokenIs(b, PERL_SIGIL_SCALAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SIGIL_SCALAR);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && scalar_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, SCALAR_DEREFERENCE, r);
    return r;
  }

  /* ********************************************************** */
  // function_call
  public static boolean scalar_function_result(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_function_result")) return false;
    if (!nextTokenIs(b, PERL_FUNCTION)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = function_call(b, l + 1);
    exit_section_(b, m, SCALAR_FUNCTION_RESULT, r);
    return r;
  }

  /* ********************************************************** */
  // scalar '{' scalar_value '}'
  public static boolean scalar_hash_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_hash_element")) return false;
    if (!nextTokenIs(b, PERL_SCALAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scalar(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && scalar_value(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, SCALAR_HASH_ELEMENT, r);
    return r;
  }

  /* ********************************************************** */
  // scalar_value_determined
  public static boolean scalar_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<scalar value>");
    r = scalar_value_determined(b, l + 1);
    exit_section_(b, l, m, SCALAR_VALUE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // string
  //     | PERL_NUMBER
  //     | multiline_marker
  //     | scalar_anon_array
  //     | scalar_anon_hash
  //     | scalar_value_mutable
  public static boolean scalar_value_determined(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_value_determined")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<scalar value determined>");
    r = string(b, l + 1);
    if (!r) r = consumeToken(b, PERL_NUMBER);
    if (!r) r = multiline_marker(b, l + 1);
    if (!r) r = scalar_anon_array(b, l + 1);
    if (!r) r = scalar_anon_hash(b, l + 1);
    if (!r) r = scalar_value_mutable(b, l + 1);
    exit_section_(b, l, m, SCALAR_VALUE_DETERMINED, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // scalar_dereference
  //     | scalar_hash_element
  //     | scalar_array_element
  //     | scalar
  //     | scalar_function_result
  public static boolean scalar_value_mutable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_value_mutable")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<scalar value mutable>");
    r = scalar_dereference(b, l + 1);
    if (!r) r = scalar_hash_element(b, l + 1);
    if (!r) r = scalar_array_element(b, l + 1);
    if (!r) r = scalar(b, l + 1);
    if (!r) r = scalar_function_result(b, l + 1);
    exit_section_(b, l, m, SCALAR_VALUE_MUTABLE, r, false, null);
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

  /* ********************************************************** */
  // PERL_SCALAR | PERL_ARRAY | PERL_HASH | PERL_GLOB
  public static boolean variable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<variable>");
    r = consumeToken(b, PERL_SCALAR);
    if (!r) r = consumeToken(b, PERL_ARRAY);
    if (!r) r = consumeToken(b, PERL_HASH);
    if (!r) r = consumeToken(b, PERL_GLOB);
    exit_section_(b, l, m, VARIABLE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // my_definition | our_definition | local_definition
  public static boolean variable_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_definition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<variable definition>");
    r = my_definition(b, l + 1);
    if (!r) r = our_definition(b, l + 1);
    if (!r) r = local_definition(b, l + 1);
    exit_section_(b, l, m, VARIABLE_DEFINITION, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // package_bare ? variable | '(' variables ')'
  public static boolean variable_definition_arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_definition_arguments")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<variable definition arguments>");
    r = variable_definition_arguments_0(b, l + 1);
    if (!r) r = variable_definition_arguments_1(b, l + 1);
    exit_section_(b, l, m, VARIABLE_DEFINITION_ARGUMENTS, r, false, null);
    return r;
  }

  // package_bare ? variable
  private static boolean variable_definition_arguments_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_definition_arguments_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable_definition_arguments_0_0(b, l + 1);
    r = r && variable(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // package_bare ?
  private static boolean variable_definition_arguments_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_definition_arguments_0_0")) return false;
    package_bare(b, l + 1);
    return true;
  }

  // '(' variables ')'
  private static boolean variable_definition_arguments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_definition_arguments_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && variables(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // variable (PERL_COMMA variable)+
  public static boolean variables(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variables")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<variables>");
    r = variable(b, l + 1);
    r = r && variables_1(b, l + 1);
    exit_section_(b, l, m, VARIABLES, r, false, null);
    return r;
  }

  // (PERL_COMMA variable)+
  private static boolean variables_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variables_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variables_1_0(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!variables_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "variables_1", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_COMMA variable
  private static boolean variables_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variables_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_COMMA);
    r = r && variable(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

}
