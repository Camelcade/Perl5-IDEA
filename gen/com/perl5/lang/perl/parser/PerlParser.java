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
    else if (t == PACKAGE_DEFINITION) {
      r = package_definition(b, 0);
    }
    else if (t == PACKAGE_NAMESPACE) {
      r = package_namespace(b, 0);
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
    else if (t == PERL_EXPRESSION) {
      r = perl_expression(b, 0);
    }
    else if (t == PERL_HASH_VALUE) {
      r = perl_hash_value(b, 0);
    }
    else if (t == PERL_SCALAR_VALUE) {
      r = perl_scalar_value(b, 0);
    }
    else if (t == STATIC_CALL) {
      r = static_call(b, 0);
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
  // package_namespace | package_item
  static boolean block_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_item")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_namespace(b, l + 1);
    if (!r) r = package_item(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // code_line_elements* ';' perl_multiline_string ?
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

  // code_line_elements*
  private static boolean code_line_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_0")) return false;
    int c = current_position_(b);
    while (true) {
      if (!code_line_elements(b, l + 1)) break;
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
  // method_call | function_call| perl_chars | perl_call | perl_array_value | perl_hash_value | perl_scalar_value | perl_glob |  perl_multiline_string | PERL_MULTILINE_MARKER | PERL_OPERATOR
  static boolean code_line_elements(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_elements")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = method_call(b, l + 1);
    if (!r) r = function_call(b, l + 1);
    if (!r) r = consumeToken(b, PERL_CHARS);
    if (!r) r = perl_call(b, l + 1);
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
  // perl_function perl_call_params ?
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

  // perl_call_params ?
  private static boolean function_call_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_1")) return false;
    perl_call_params(b, l + 1);
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
  // static_call | object_call
  public static boolean method_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "method_call")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<method call>");
    r = static_call(b, l + 1);
    if (!r) r = object_call(b, l + 1);
    exit_section_(b, l, m, METHOD_CALL, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // perl_scalar_value_var '->' function_call
  public static boolean object_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_call")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<object call>");
    r = perl_scalar_value_var(b, l + 1);
    r = r && consumeToken(b, PERL_DEREFERENCE);
    r = r && function_call(b, l + 1);
    exit_section_(b, l, m, OBJECT_CALL, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'package' perl_package PERL_VERSION ? ';' ?
  public static boolean package_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_definition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package definition>");
    r = consumeToken(b, "package");
    r = r && perl_package(b, l + 1);
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

  // ';' ?
  private static boolean package_definition_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_definition_3")) return false;
    consumeToken(b, PERL_SEMI);
    return true;
  }

  /* ********************************************************** */
  // function_definition | if_block | code_line  | PERL_POD | PERL_COMMENT | PERL_COMMENT_BLOCK | perl_block
  static boolean package_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_item")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = function_definition(b, l + 1);
    if (!r) r = if_block(b, l + 1);
    if (!r) r = code_line(b, l + 1);
    if (!r) r = consumeToken(b, PERL_POD);
    if (!r) r = consumeToken(b, PERL_COMMENT);
    if (!r) r = consumeToken(b, PERL_COMMENT_BLOCK);
    if (!r) r = perl_block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // package_definition (perl_block | package_item * )
  public static boolean package_namespace(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package namespace>");
    r = package_definition(b, l + 1);
    r = r && package_namespace_1(b, l + 1);
    exit_section_(b, l, m, PACKAGE_NAMESPACE, r, false, null);
    return r;
  }

  // perl_block | package_item *
  private static boolean package_namespace_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_block(b, l + 1);
    if (!r) r = package_namespace_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // package_item *
  private static boolean package_namespace_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_1_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!package_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "package_namespace_1_1", c)) break;
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
  // '[' perl_array_value ']'
  static boolean perl_anon_array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_anon_array")) return false;
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
  static boolean perl_anon_hash(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_anon_hash")) return false;
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
  // PERL_VARIABLE_ARRAY | PERL_VARIABLE_ARRAY_BUILT_IN
  static boolean perl_array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_array")) return false;
    if (!nextTokenIs(b, "", PERL_VARIABLE_ARRAY, PERL_VARIABLE_ARRAY_BUILT_IN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_VARIABLE_ARRAY);
    if (!r) r = consumeToken(b, PERL_VARIABLE_ARRAY_BUILT_IN);
    exit_section_(b, m, null, r);
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
  // perl_scalar '[' perl_scalar_value ']'
  static boolean perl_array_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_array_element")) return false;
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
  // PERL_STATIC_METHOD_CALL | PERL_INSTANCE_METHOD_CALL
  static boolean perl_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_call")) return false;
    if (!nextTokenIs(b, "", PERL_INSTANCE_METHOD_CALL, PERL_STATIC_METHOD_CALL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_STATIC_METHOD_CALL);
    if (!r) r = consumeToken(b, PERL_INSTANCE_METHOD_CALL);
    exit_section_(b, m, null, r);
    return r;
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
  // '(' perl_call_param ? (',' perl_call_param ) * ')'
  public static boolean perl_call_params(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_call_params")) return false;
    if (!nextTokenIs(b, PERL_LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && perl_call_params_1(b, l + 1);
    r = r && perl_call_params_2(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, PERL_CALL_PARAMS, r);
    return r;
  }

  // perl_call_param ?
  private static boolean perl_call_params_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_call_params_1")) return false;
    perl_call_param(b, l + 1);
    return true;
  }

  // (',' perl_call_param ) *
  private static boolean perl_call_params_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_call_params_2")) return false;
    int c = current_position_(b);
    while (true) {
      if (!perl_call_params_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "perl_call_params_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ',' perl_call_param
  private static boolean perl_call_params_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_call_params_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_COMMA);
    r = r && perl_call_param(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // code_line_elements +
  public static boolean perl_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<perl expression>");
    r = code_line_elements(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!code_line_elements(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "perl_expression", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, l, m, PERL_EXPRESSION, r, false, null);
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
  // PERL_VARIABLE_GLOB | PERL_VARIABLE_GLOB_BUILT_IN
  static boolean perl_glob(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_glob")) return false;
    if (!nextTokenIs(b, "", PERL_VARIABLE_GLOB, PERL_VARIABLE_GLOB_BUILT_IN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_VARIABLE_GLOB);
    if (!r) r = consumeToken(b, PERL_VARIABLE_GLOB_BUILT_IN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_VARIABLE_HASH | PERL_VARIABLE_HASH_BUILT_IN
  static boolean perl_hash(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_hash")) return false;
    if (!nextTokenIs(b, "", PERL_VARIABLE_HASH, PERL_VARIABLE_HASH_BUILT_IN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_VARIABLE_HASH);
    if (!r) r = consumeToken(b, PERL_VARIABLE_HASH_BUILT_IN);
    exit_section_(b, m, null, r);
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
  // perl_scalar '{' perl_scalar_value '}'
  static boolean perl_hash_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_hash_element")) return false;
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
  // (PERL_MULTILINE_SQ | PERL_MULTILINE_DQ| PERL_MULTILINE_XML | PERL_MULTILINE_HTML) PERL_MULTILINE_MARKER
  static boolean perl_multiline_string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_multiline_string")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_multiline_string_0(b, l + 1);
    r = r && consumeToken(b, PERL_MULTILINE_MARKER);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_MULTILINE_SQ | PERL_MULTILINE_DQ| PERL_MULTILINE_XML | PERL_MULTILINE_HTML
  private static boolean perl_multiline_string_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_multiline_string_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_MULTILINE_SQ);
    if (!r) r = consumeToken(b, PERL_MULTILINE_DQ);
    if (!r) r = consumeToken(b, PERL_MULTILINE_XML);
    if (!r) r = consumeToken(b, PERL_MULTILINE_HTML);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_PACKAGE_USER | PERL_PACKAGE_BUILT_IN | PERL_PACKAGE_BUILT_IN_DEPRECATED | PERL_PACKAGE_BUILT_IN_PRAGMA
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
  static boolean perl_scalar(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_scalar")) return false;
    if (!nextTokenIs(b, "", PERL_VARIABLE_SCALAR, PERL_VARIABLE_SCALAR_BUILT_IN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_VARIABLE_SCALAR);
    if (!r) r = consumeToken(b, PERL_VARIABLE_SCALAR_BUILT_IN);
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
  // perl_string | PERL_NUMBER | PERL_VERSION | perl_anon_array | perl_anon_hash | perl_scalar_value_var
  public static boolean perl_scalar_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_scalar_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, "<perl scalar value>");
    r = perl_string(b, l + 1);
    if (!r) r = consumeToken(b, PERL_NUMBER);
    if (!r) r = consumeToken(b, PERL_VERSION);
    if (!r) r = perl_anon_array(b, l + 1);
    if (!r) r = perl_anon_hash(b, l + 1);
    if (!r) r = perl_scalar_value_var(b, l + 1);
    exit_section_(b, l, m, PERL_SCALAR_VALUE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // perl_scalar_dereference | perl_hash_element | perl_array_element | perl_scalar
  static boolean perl_scalar_value_var(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_scalar_value_var")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_scalar_dereference(b, l + 1);
    if (!r) r = perl_hash_element(b, l + 1);
    if (!r) r = perl_array_element(b, l + 1);
    if (!r) r = perl_scalar(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_DQ_STRING | PERL_SQ_STRING
  static boolean perl_string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_string")) return false;
    if (!nextTokenIs(b, "", PERL_DQ_STRING, PERL_SQ_STRING)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_DQ_STRING);
    if (!r) r = consumeToken(b, PERL_SQ_STRING);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // perl_package '::' function_call
  public static boolean static_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "static_call")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<static call>");
    r = perl_package(b, l + 1);
    r = r && consumeToken(b, PERL_DEPACKAGE);
    r = r && function_call(b, l + 1);
    exit_section_(b, l, m, STATIC_CALL, r, false, null);
    return r;
  }

}
