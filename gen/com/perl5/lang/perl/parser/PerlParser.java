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
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    if (t == ARRAY) {
      r = array(b, 0);
    }
    else if (t == BLOCK) {
      r = block(b, 0);
    }
    else if (t == BLOCK_BLOCK) {
      r = block_block(b, 0);
    }
    else if (t == BLOCK_CONDITIONAL) {
      r = block_conditional(b, 0);
    }
    else if (t == CALEE) {
      r = calee(b, 0);
    }
    else if (t == CALL_LEFTWARD) {
      r = call_leftward(b, 0);
    }
    else if (t == CALL_RIGHTWARD) {
      r = call_rightward(b, 0);
    }
    else if (t == CODE_LINE) {
      r = code_line(b, 0);
    }
    else if (t == EVAL) {
      r = eval(b, 0);
    }
    else if (t == EXPR) {
      r = expr(b, 0, -1);
    }
    else if (t == FILE_ITEM) {
      r = file_item(b, 0);
    }
    else if (t == FOR_BLOCK) {
      r = for_block(b, 0);
    }
    else if (t == FOR_BLOCK_ARGUMENTS) {
      r = for_block_arguments(b, 0);
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
    else if (t == GIVEN_BLOCK) {
      r = given_block(b, 0);
    }
    else if (t == GREP_OP) {
      r = grep_op(b, 0);
    }
    else if (t == HASH) {
      r = hash(b, 0);
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
    else if (t == IF_POSTFIX) {
      r = if_postfix(b, 0);
    }
    else if (t == LIST) {
      r = list(b, 0);
    }
    else if (t == LOCAL_DEFINITION) {
      r = local_definition(b, 0);
    }
    else if (t == MULTILINE_STRING) {
      r = multiline_string(b, 0);
    }
    else if (t == MY_DEFINITION) {
      r = my_definition(b, 0);
    }
    else if (t == OBJECT_METHOD) {
      r = object_method(b, 0);
    }
    else if (t == OP_10_EXPR) {
      r = op_10_expr(b, 0);
    }
    else if (t == OP_11_EXPR) {
      r = expr(b, 0, 12);
    }
    else if (t == OP_12_EXPR) {
      r = expr(b, 0, 11);
    }
    else if (t == OP_13_EXPR) {
      r = expr(b, 0, 10);
    }
    else if (t == OP_14_EXPR) {
      r = expr(b, 0, 9);
    }
    else if (t == OP_15_EXPR) {
      r = expr(b, 0, 8);
    }
    else if (t == OP_16_EXPR) {
      r = expr(b, 0, 7);
    }
    else if (t == OP_17_EXPR) {
      r = expr(b, 0, 6);
    }
    else if (t == OP_18_EXPR) {
      r = expr(b, 0, 5);
    }
    else if (t == OP_19_EXPR) {
      r = expr(b, 0, 4);
    }
    else if (t == OP_1_EXPR) {
      r = op_1_expr(b, 0);
    }
    else if (t == OP_20_EXPR) {
      r = expr(b, 0, 3);
    }
    else if (t == OP_21_EXPR) {
      r = op_21_expr(b, 0);
    }
    else if (t == OP_22_EXPR) {
      r = op_22_expr(b, 0);
    }
    else if (t == OP_23_EXPR) {
      r = expr(b, 0, 0);
    }
    else if (t == OP_24_EXPR) {
      r = expr(b, 0, -1);
    }
    else if (t == OP_2_EXPR) {
      r = expr(b, 0, 21);
    }
    else if (t == OP_3_PREF_EXPR) {
      r = op_3_pref_expr(b, 0);
    }
    else if (t == OP_3_SUFF_EXPR) {
      r = expr(b, 0, 20);
    }
    else if (t == OP_4_EXPR) {
      r = expr(b, 0, 19);
    }
    else if (t == OP_5_EXPR) {
      r = op_5_expr(b, 0);
    }
    else if (t == OP_6_EXPR) {
      r = expr(b, 0, 17);
    }
    else if (t == OP_7_EXPR) {
      r = expr(b, 0, 16);
    }
    else if (t == OP_8_EXPR) {
      r = expr(b, 0, 15);
    }
    else if (t == OP_9_EXPR) {
      r = expr(b, 0, 14);
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
    else if (t == PACKAGE_FUNCTION) {
      r = package_function(b, 0);
    }
    else if (t == PACKAGE_ITEM) {
      r = package_item(b, 0);
    }
    else if (t == PACKAGE_METHOD) {
      r = package_method(b, 0);
    }
    else if (t == PACKAGE_NO) {
      r = package_no(b, 0);
    }
    else if (t == PACKAGE_REQUIRE) {
      r = package_require(b, 0);
    }
    else if (t == PACKAGE_USE) {
      r = package_use(b, 0);
    }
    else if (t == PACKAGE_USE_ARGUMENTS) {
      r = package_use_arguments(b, 0);
    }
    else if (t == SCALAR) {
      r = scalar(b, 0);
    }
    else if (t == SORT_OP) {
      r = sort_op(b, 0);
    }
    else if (t == SORT_OP_ARGS) {
      r = sort_op_args(b, 0);
    }
    else if (t == TERM) {
      r = term(b, 0);
    }
    else if (t == VARIABLE_DEFINITION) {
      r = variable_definition(b, 0);
    }
    else if (t == VARIABLE_DEFINITION_ARGUMENTS) {
      r = variable_definition_arguments(b, 0);
    }
    else if (t == WHILE_BLOCK) {
      r = while_block(b, 0);
    }
    else {
      r = parse_root_(t, b, 0);
    }
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return perlFile(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(EXPR, OP_10_EXPR, OP_11_EXPR, OP_12_EXPR,
      OP_13_EXPR, OP_14_EXPR, OP_15_EXPR, OP_16_EXPR,
      OP_17_EXPR, OP_18_EXPR, OP_19_EXPR, OP_1_EXPR,
      OP_20_EXPR, OP_21_EXPR, OP_22_EXPR, OP_23_EXPR,
      OP_24_EXPR, OP_2_EXPR, OP_3_PREF_EXPR, OP_3_SUFF_EXPR,
      OP_4_EXPR, OP_5_EXPR, OP_6_EXPR, OP_7_EXPR,
      OP_8_EXPR, OP_9_EXPR),
  };

  /* ********************************************************** */
  // array_safe '[' expr ']'              // array slice
  //     | array_safe '{' expr '}'              // hash slice
  //     | array_safe
  //     | '(' ')'
  public static boolean array(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<array>");
    r = array_0(b, l + 1);
    if (!r) r = array_1(b, l + 1);
    if (!r) r = array_safe(b, l + 1);
    if (!r) r = array_3(b, l + 1);
    exit_section_(b, l, m, ARRAY, r, false, null);
    return r;
  }

  // array_safe '[' expr ']'
  private static boolean array_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = array_safe(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACK);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, PERL_RBRACK);
    exit_section_(b, m, null, r);
    return r;
  }

  // array_safe '{' expr '}'
  private static boolean array_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = array_safe(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // '(' ')'
  private static boolean array_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_SIGIL_ARRAY '{' expr '}'   // scalar dereference
  //     | PERL_SIGIL_ARRAY scalar       // scalar dereference
  //     | PERL_ARRAY
  static boolean array_safe(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_safe")) return false;
    if (!nextTokenIs(b, "", PERL_ARRAY, PERL_SIGIL_ARRAY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = array_safe_0(b, l + 1);
    if (!r) r = array_safe_1(b, l + 1);
    if (!r) r = consumeToken(b, PERL_ARRAY);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_SIGIL_ARRAY '{' expr '}'
  private static boolean array_safe_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_safe_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SIGIL_ARRAY);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_SIGIL_ARRAY scalar
  private static boolean array_safe_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_safe_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SIGIL_ARRAY);
    r = r && scalar(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '{' file_item* '}'
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

  // file_item*
  private static boolean block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!file_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "block_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // block ('continue' block ) ?
  public static boolean block_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_block")) return false;
    if (!nextTokenIs(b, PERL_LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = block(b, l + 1);
    r = r && block_block_1(b, l + 1);
    exit_section_(b, m, BLOCK_BLOCK, r);
    return r;
  }

  // ('continue' block ) ?
  private static boolean block_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_block_1")) return false;
    block_block_1_0(b, l + 1);
    return true;
  }

  // 'continue' block
  private static boolean block_block_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_block_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "continue");
    r = r && block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '(' expr ')' block
  public static boolean block_conditional(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_conditional")) return false;
    if (!nextTokenIs(b, PERL_LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, PERL_RPAREN);
    r = r && block(b, l + 1);
    exit_section_(b, m, BLOCK_CONDITIONAL, r);
    return r;
  }

  /* ********************************************************** */
  // package_method
  //     | package_function
  //     | object_method
  //     | PERL_FUNCTION
  public static boolean calee(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "calee")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<calee>");
    r = package_method(b, l + 1);
    if (!r) r = package_function(b, l + 1);
    if (!r) r = object_method(b, l + 1);
    if (!r) r = consumeToken(b, PERL_FUNCTION);
    exit_section_(b, l, m, CALEE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // calee '(' expr ? ')'
  //     | scalar '(' expr ? ')'
  //     | scalar '->' '(' expr ? ')'
  public static boolean call_leftward(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_leftward")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<call leftward>");
    r = call_leftward_0(b, l + 1);
    if (!r) r = call_leftward_1(b, l + 1);
    if (!r) r = call_leftward_2(b, l + 1);
    exit_section_(b, l, m, CALL_LEFTWARD, r, false, null);
    return r;
  }

  // calee '(' expr ? ')'
  private static boolean call_leftward_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_leftward_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = calee(b, l + 1);
    r = r && consumeToken(b, PERL_LPAREN);
    r = r && call_leftward_0_2(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr ?
  private static boolean call_leftward_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_leftward_0_2")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  // scalar '(' expr ? ')'
  private static boolean call_leftward_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_leftward_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scalar(b, l + 1);
    r = r && consumeToken(b, PERL_LPAREN);
    r = r && call_leftward_1_2(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr ?
  private static boolean call_leftward_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_leftward_1_2")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  // scalar '->' '(' expr ? ')'
  private static boolean call_leftward_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_leftward_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scalar(b, l + 1);
    r = r && consumeToken(b, PERL_DEREFERENCE);
    r = r && consumeToken(b, PERL_LPAREN);
    r = r && call_leftward_2_3(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr ?
  private static boolean call_leftward_2_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_leftward_2_3")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // calee expr ?
  public static boolean call_rightward(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_rightward")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<call rightward>");
    r = calee(b, l + 1);
    r = r && call_rightward_1(b, l + 1);
    exit_section_(b, l, m, CALL_RIGHTWARD, r, false, null);
    return r;
  }

  // expr ?
  private static boolean call_rightward_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "call_rightward_1")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // expr if_postfix ?
  public static boolean code_line(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<code line>");
    r = expr(b, l + 1, -1);
    r = r && code_line_1(b, l + 1);
    exit_section_(b, l, m, CODE_LINE, r, false, null);
    return r;
  }

  // if_postfix ?
  private static boolean code_line_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_line_1")) return false;
    if_postfix(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'eval' (block | scalar) ';'
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

  // block | scalar
  private static boolean eval_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "eval_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = block(b, l + 1);
    if (!r) r = scalar(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // package_definition | package_item
  public static boolean file_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file_item")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<file item>");
    r = package_definition(b, l + 1);
    if (!r) r = package_item(b, l + 1);
    exit_section_(b, l, m, FILE_ITEM, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ('foreach' | 'for') for_block_arguments
  public static boolean for_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_block")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<for block>");
    r = for_block_0(b, l + 1);
    r = r && for_block_arguments(b, l + 1);
    exit_section_(b, l, m, FOR_BLOCK, r, false, null);
    return r;
  }

  // 'foreach' | 'for'
  private static boolean for_block_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_block_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "foreach");
    if (!r) r = consumeToken(b, "for");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '(' expr ? ';' expr ? ';' expr ? ')' block
  //      | (variable_definition | variable ) '(' list ')' block ('continue' block ) ?
  public static boolean for_block_arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_block_arguments")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<for block arguments>");
    r = for_block_arguments_0(b, l + 1);
    if (!r) r = for_block_arguments_1(b, l + 1);
    exit_section_(b, l, m, FOR_BLOCK_ARGUMENTS, r, false, null);
    return r;
  }

  // '(' expr ? ';' expr ? ';' expr ? ')' block
  private static boolean for_block_arguments_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_block_arguments_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && for_block_arguments_0_1(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    r = r && for_block_arguments_0_3(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    r = r && for_block_arguments_0_5(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    r = r && block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr ?
  private static boolean for_block_arguments_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_block_arguments_0_1")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  // expr ?
  private static boolean for_block_arguments_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_block_arguments_0_3")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  // expr ?
  private static boolean for_block_arguments_0_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_block_arguments_0_5")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  // (variable_definition | variable ) '(' list ')' block ('continue' block ) ?
  private static boolean for_block_arguments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_block_arguments_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = for_block_arguments_1_0(b, l + 1);
    r = r && consumeToken(b, PERL_LPAREN);
    r = r && list(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    r = r && block(b, l + 1);
    r = r && for_block_arguments_1_5(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // variable_definition | variable
  private static boolean for_block_arguments_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_block_arguments_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable_definition(b, l + 1);
    if (!r) r = variable(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ('continue' block ) ?
  private static boolean for_block_arguments_1_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_block_arguments_1_5")) return false;
    for_block_arguments_1_5_0(b, l + 1);
    return true;
  }

  // 'continue' block
  private static boolean for_block_arguments_1_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_block_arguments_1_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "continue");
    r = r && block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
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
  // 'sub' PERL_FUNCTION block ';' ?
  public static boolean function_definition_named(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_definition_named")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<function definition named>");
    r = consumeToken(b, "sub");
    r = r && consumeToken(b, PERL_FUNCTION);
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
  // 'given' block_conditional
  public static boolean given_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "given_block")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<given block>");
    r = consumeToken(b, "given");
    r = r && block_conditional(b, l + 1);
    exit_section_(b, l, m, GIVEN_BLOCK, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ('grep' | 'map' ) block list
  public static boolean grep_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grep_op")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<grep op>");
    r = grep_op_0(b, l + 1);
    r = r && block(b, l + 1);
    r = r && list(b, l + 1);
    exit_section_(b, l, m, GREP_OP, r, false, null);
    return r;
  }

  // 'grep' | 'map'
  private static boolean grep_op_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "grep_op_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "grep");
    if (!r) r = consumeToken(b, "map");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_SIGIL_HASH '{' expr '}'   // hash dereference w braces
  //     | PERL_SIGIL_ARRAY scalar       // scalar dereference wo braces
  //     | PERL_HASH
  public static boolean hash(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<hash>");
    r = hash_0(b, l + 1);
    if (!r) r = hash_1(b, l + 1);
    if (!r) r = consumeToken(b, PERL_HASH);
    exit_section_(b, l, m, HASH, r, false, null);
    return r;
  }

  // PERL_SIGIL_HASH '{' expr '}'
  private static boolean hash_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SIGIL_HASH);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_SIGIL_ARRAY scalar
  private static boolean hash_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "hash_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SIGIL_ARRAY);
    r = r && scalar(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ('if' | 'unless') block_conditional if_block_elsif * if_block_else ?
  public static boolean if_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_block")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<if block>");
    r = if_block_0(b, l + 1);
    r = r && block_conditional(b, l + 1);
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
  // 'else' block
  public static boolean if_block_else(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_block_else")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<if block else>");
    r = consumeToken(b, "else");
    r = r && block(b, l + 1);
    exit_section_(b, l, m, IF_BLOCK_ELSE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'elsif' block_conditional
  public static boolean if_block_elsif(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_block_elsif")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<if block elsif>");
    r = consumeToken(b, "elsif");
    r = r && block_conditional(b, l + 1);
    exit_section_(b, l, m, IF_BLOCK_ELSIF, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ('if' | 'unless') expr
  public static boolean if_postfix(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_postfix")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<if postfix>");
    r = if_postfix_0(b, l + 1);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, l, m, IF_POSTFIX, r, false, null);
    return r;
  }

  // 'if' | 'unless'
  private static boolean if_postfix_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_postfix_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "if");
    if (!r) r = consumeToken(b, "unless");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // array | expr
  public static boolean list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<list>");
    r = array(b, l + 1);
    if (!r) r = expr(b, l + 1, -1);
    exit_section_(b, l, m, LIST, r, false, null);
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
  // PERL_MULTILINE_MARKER
  //     | PERL_MULTILINE_MARKER_HTML
  //     | PERL_MULTILINE_MARKER_XHTML
  //     | PERL_MULTILINE_MARKER_XML
  static boolean multiline_marker(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multiline_marker")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_MULTILINE_MARKER);
    if (!r) r = consumeToken(b, PERL_MULTILINE_MARKER_HTML);
    if (!r) r = consumeToken(b, PERL_MULTILINE_MARKER_XHTML);
    if (!r) r = consumeToken(b, PERL_MULTILINE_MARKER_XML);
    exit_section_(b, m, null, r);
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
  // scalar '->' PERL_FUNCTION
  public static boolean object_method(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_method")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<object method>");
    r = scalar(b, l + 1);
    r = r && consumeToken(b, PERL_DEREFERENCE);
    r = r && consumeToken(b, PERL_FUNCTION);
    exit_section_(b, l, m, OBJECT_METHOD, r, false, null);
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
  // package_bare '::' PERL_FUNCTION
  public static boolean package_function(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_function")) return false;
    if (!nextTokenIs(b, PERL_PACKAGE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_bare(b, l + 1);
    r = r && consumeToken(b, PERL_DEPACKAGE);
    r = r && consumeToken(b, PERL_FUNCTION);
    exit_section_(b, m, PACKAGE_FUNCTION, r);
    return r;
  }

  /* ********************************************************** */
  // eval
  //     | function_definition
  //     | package_use
  //     | package_no
  //     | package_require
  //     | for_block
  //     | if_block
  //     | while_block
  //     | given_block
  //     | block_block
  //     | code_line ';' multiline_marker?
  //     | PERL_POD
  //     | PERL_COMMENT
  //     | PERL_COMMENT_BLOCK
  public static boolean package_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_item")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package item>");
    r = eval(b, l + 1);
    if (!r) r = function_definition(b, l + 1);
    if (!r) r = package_use(b, l + 1);
    if (!r) r = package_no(b, l + 1);
    if (!r) r = package_require(b, l + 1);
    if (!r) r = for_block(b, l + 1);
    if (!r) r = if_block(b, l + 1);
    if (!r) r = while_block(b, l + 1);
    if (!r) r = given_block(b, l + 1);
    if (!r) r = block_block(b, l + 1);
    if (!r) r = package_item_10(b, l + 1);
    if (!r) r = consumeToken(b, PERL_POD);
    if (!r) r = consumeToken(b, PERL_COMMENT);
    if (!r) r = consumeToken(b, PERL_COMMENT_BLOCK);
    exit_section_(b, l, m, PACKAGE_ITEM, r, false, null);
    return r;
  }

  // code_line ';' multiline_marker?
  private static boolean package_item_10(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_item_10")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = code_line(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    r = r && package_item_10_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // multiline_marker?
  private static boolean package_item_10_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_item_10_2")) return false;
    multiline_marker(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // package_bare '->' PERL_FUNCTION
  public static boolean package_method(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_method")) return false;
    if (!nextTokenIs(b, PERL_PACKAGE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_bare(b, l + 1);
    r = r && consumeToken(b, PERL_DEREFERENCE);
    r = r && consumeToken(b, PERL_FUNCTION);
    exit_section_(b, m, PACKAGE_METHOD, r);
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
    if (!r) r = consumeToken(b, STRING);
    exit_section_(b, m, null, r);
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
  // package_bare PERL_VERSION expr ?
  //     | package_bare expr ?
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

  // package_bare PERL_VERSION expr ?
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

  // expr ?
  private static boolean package_use_arguments_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments_0_2")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  // package_bare expr ?
  private static boolean package_use_arguments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_bare(b, l + 1);
    r = r && package_use_arguments_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr ?
  private static boolean package_use_arguments_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments_1_1")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // file_item*
  static boolean perlFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perlFile")) return false;
    int c = current_position_(b);
    while (true) {
      if (!file_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "perlFile", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // scalar_safe '->'? '[' expr ']'      // array element
  //     | scalar_safe '->'? '{' expr '}'    // hash element
  //     | scalar_safe                       // scalar or deref scalar
  //     | '[' expr ? ']'                      // anonymous array
  //     | '{' expr ? '}'                      // anonymous hash
  //     |  multiline_marker                 // deferred string
  //     | PERL_STRING                       // string
  //     | PERL_NUMBER
  public static boolean scalar(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<scalar>");
    r = scalar_0(b, l + 1);
    if (!r) r = scalar_1(b, l + 1);
    if (!r) r = scalar_safe(b, l + 1);
    if (!r) r = scalar_3(b, l + 1);
    if (!r) r = scalar_4(b, l + 1);
    if (!r) r = multiline_marker(b, l + 1);
    if (!r) r = consumeToken(b, PERL_STRING);
    if (!r) r = consumeToken(b, PERL_NUMBER);
    exit_section_(b, l, m, SCALAR, r, false, null);
    return r;
  }

  // scalar_safe '->'? '[' expr ']'
  private static boolean scalar_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scalar_safe(b, l + 1);
    r = r && scalar_0_1(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACK);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, PERL_RBRACK);
    exit_section_(b, m, null, r);
    return r;
  }

  // '->'?
  private static boolean scalar_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_0_1")) return false;
    consumeToken(b, PERL_DEREFERENCE);
    return true;
  }

  // scalar_safe '->'? '{' expr '}'
  private static boolean scalar_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scalar_safe(b, l + 1);
    r = r && scalar_1_1(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // '->'?
  private static boolean scalar_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_1_1")) return false;
    consumeToken(b, PERL_DEREFERENCE);
    return true;
  }

  // '[' expr ? ']'
  private static boolean scalar_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LBRACK);
    r = r && scalar_3_1(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACK);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr ?
  private static boolean scalar_3_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_3_1")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  // '{' expr ? '}'
  private static boolean scalar_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LBRACE);
    r = r && scalar_4_1(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr ?
  private static boolean scalar_4_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_4_1")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // PERL_SIGIL_SCALAR '{' expr '}'       // scalar dereference
  //     | PERL_SCALAR
  static boolean scalar_safe(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_safe")) return false;
    if (!nextTokenIs(b, "", PERL_SCALAR, PERL_SIGIL_SCALAR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scalar_safe_0(b, l + 1);
    if (!r) r = consumeToken(b, PERL_SCALAR);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_SIGIL_SCALAR '{' expr '}'
  private static boolean scalar_safe_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_safe_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SIGIL_SCALAR);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'sort' sort_op_args
  public static boolean sort_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sort_op")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<sort op>");
    r = consumeToken(b, "sort");
    r = r && sort_op_args(b, l + 1);
    exit_section_(b, l, m, SORT_OP, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PERL_FUNCTION list
  //     | block ? list
  public static boolean sort_op_args(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sort_op_args")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<sort op args>");
    r = sort_op_args_0(b, l + 1);
    if (!r) r = sort_op_args_1(b, l + 1);
    exit_section_(b, l, m, SORT_OP_ARGS, r, false, null);
    return r;
  }

  // PERL_FUNCTION list
  private static boolean sort_op_args_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sort_op_args_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_FUNCTION);
    r = r && list(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // block ? list
  private static boolean sort_op_args_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sort_op_args_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sort_op_args_1_0(b, l + 1);
    r = r && list(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // block ?
  private static boolean sort_op_args_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sort_op_args_1_0")) return false;
    block(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '(' expr ')'
  //     | array
  //     | hash
  //     | scalar
  //     | '<' scalar? '>'
  public static boolean term(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<term>");
    r = term_0(b, l + 1);
    if (!r) r = array(b, l + 1);
    if (!r) r = hash(b, l + 1);
    if (!r) r = scalar(b, l + 1);
    if (!r) r = term_4(b, l + 1);
    exit_section_(b, l, m, TERM, r, false, null);
    return r;
  }

  // '(' expr ')'
  private static boolean term_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // '<' scalar? '>'
  private static boolean term_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "<");
    r = r && term_4_1(b, l + 1);
    r = r && consumeToken(b, ">");
    exit_section_(b, m, null, r);
    return r;
  }

  // scalar?
  private static boolean term_4_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term_4_1")) return false;
    scalar(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // PERL_SCALAR | PERL_ARRAY | PERL_HASH
  static boolean variable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SCALAR);
    if (!r) r = consumeToken(b, PERL_ARRAY);
    if (!r) r = consumeToken(b, PERL_HASH);
    exit_section_(b, m, null, r);
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
  // variable (',' variable ) *
  static boolean variables(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variables")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable(b, l + 1);
    r = r && variables_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' variable ) *
  private static boolean variables_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variables_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!variables_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "variables_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // ',' variable
  private static boolean variables_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variables_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_COMMA);
    r = r && variable(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ('until' | 'while' ) block_conditional ('continue' block ) ?
  public static boolean while_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_block")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<while block>");
    r = while_block_0(b, l + 1);
    r = r && block_conditional(b, l + 1);
    r = r && while_block_2(b, l + 1);
    exit_section_(b, l, m, WHILE_BLOCK, r, false, null);
    return r;
  }

  // 'until' | 'while'
  private static boolean while_block_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_block_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "until");
    if (!r) r = consumeToken(b, "while");
    exit_section_(b, m, null, r);
    return r;
  }

  // ('continue' block ) ?
  private static boolean while_block_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_block_2")) return false;
    while_block_2_0(b, l + 1);
    return true;
  }

  // 'continue' block
  private static boolean while_block_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_block_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "continue");
    r = r && block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // Expression root: expr
  // Operator priority table:
  // 0: BINARY(op_24_expr)
  // 1: BINARY(op_23_expr)
  // 2: PREFIX(op_22_expr)
  // 3: ATOM(op_21_expr)
  // 4: BINARY(op_20_expr)
  // 5: BINARY(op_19_expr)
  // 6: BINARY(op_18_expr)
  // 7: BINARY(op_17_expr)
  // 8: BINARY(op_16_expr)
  // 9: BINARY(op_15_expr)
  // 10: BINARY(op_14_expr)
  // 11: BINARY(op_13_expr)
  // 12: BINARY(op_12_expr)
  // 13: BINARY(op_11_expr)
  // 14: PREFIX(op_10_expr)
  // 15: BINARY(op_9_expr)
  // 16: BINARY(op_8_expr)
  // 17: BINARY(op_7_expr)
  // 18: BINARY(op_6_expr)
  // 19: PREFIX(op_5_expr)
  // 20: N_ARY(op_4_expr)
  // 21: PREFIX(op_3_pref_expr) POSTFIX(op_3_suff_expr)
  // 22: BINARY(op_2_expr)
  // 23: ATOM(op_1_expr)
  public static boolean expr(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expr")) return false;
    addVariant(b, "<expr>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<expr>");
    r = op_22_expr(b, l + 1);
    if (!r) r = op_21_expr(b, l + 1);
    if (!r) r = op_10_expr(b, l + 1);
    if (!r) r = op_5_expr(b, l + 1);
    if (!r) r = op_3_pref_expr(b, l + 1);
    if (!r) r = op_1_expr(b, l + 1);
    p = r;
    r = r && expr_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean expr_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expr_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 0 && op_24_expr_0(b, l + 1)) {
        r = expr(b, l, 0);
        exit_section_(b, l, m, OP_24_EXPR, r, true, null);
      }
      else if (g < 1 && consumeTokenSmart(b, "and")) {
        r = expr(b, l, 1);
        exit_section_(b, l, m, OP_23_EXPR, r, true, null);
      }
      else if (g < 4 && op_20_expr_0(b, l + 1)) {
        r = expr(b, l, 4);
        exit_section_(b, l, m, OP_20_EXPR, r, true, null);
      }
      else if (g < 5 && op_19_expr_0(b, l + 1)) {
        r = expr(b, l, 4);
        exit_section_(b, l, m, OP_19_EXPR, r, true, null);
      }
      else if (g < 6 && consumeTokenSmart(b, "?")) {
        r = report_error_(b, expr(b, l, 5));
        r = op_18_expr_1(b, l + 1) && r;
        exit_section_(b, l, m, OP_18_EXPR, r, true, null);
      }
      else if (g < 7 && op_17_expr_0(b, l + 1)) {
        r = expr(b, l, 7);
        exit_section_(b, l, m, OP_17_EXPR, r, true, null);
      }
      else if (g < 8 && op_16_expr_0(b, l + 1)) {
        r = expr(b, l, 8);
        exit_section_(b, l, m, OP_16_EXPR, r, true, null);
      }
      else if (g < 9 && consumeTokenSmart(b, "&&")) {
        r = expr(b, l, 9);
        exit_section_(b, l, m, OP_15_EXPR, r, true, null);
      }
      else if (g < 10 && op_14_expr_0(b, l + 1)) {
        r = expr(b, l, 10);
        exit_section_(b, l, m, OP_14_EXPR, r, true, null);
      }
      else if (g < 11 && consumeTokenSmart(b, "&")) {
        r = expr(b, l, 11);
        exit_section_(b, l, m, OP_13_EXPR, r, true, null);
      }
      else if (g < 12 && op_12_expr_0(b, l + 1)) {
        r = expr(b, l, 12);
        exit_section_(b, l, m, OP_12_EXPR, r, true, null);
      }
      else if (g < 13 && op_11_expr_0(b, l + 1)) {
        r = expr(b, l, 13);
        exit_section_(b, l, m, OP_11_EXPR, r, true, null);
      }
      else if (g < 15 && op_9_expr_0(b, l + 1)) {
        r = expr(b, l, 15);
        exit_section_(b, l, m, OP_9_EXPR, r, true, null);
      }
      else if (g < 16 && op_8_expr_0(b, l + 1)) {
        r = expr(b, l, 16);
        exit_section_(b, l, m, OP_8_EXPR, r, true, null);
      }
      else if (g < 17 && op_7_expr_0(b, l + 1)) {
        r = expr(b, l, 17);
        exit_section_(b, l, m, OP_7_EXPR, r, true, null);
      }
      else if (g < 18 && op_6_expr_0(b, l + 1)) {
        r = expr(b, l, 18);
        exit_section_(b, l, m, OP_6_EXPR, r, true, null);
      }
      else if (g < 20 && consumeTokenSmart(b, "**")) {
        while (true) {
          r = report_error_(b, expr(b, l, 20));
          if (!consumeTokenSmart(b, "**")) break;
        }
        exit_section_(b, l, m, OP_4_EXPR, r, true, null);
      }
      else if (g < 21 && op_3_suff_expr_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, OP_3_SUFF_EXPR, r, true, null);
      }
      else if (g < 22 && consumeTokenSmart(b, PERL_DEREFERENCE)) {
        r = expr(b, l, 22);
        exit_section_(b, l, m, OP_2_EXPR, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // 'or'|'xor'
  private static boolean op_24_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_24_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "or");
    if (!r) r = consumeTokenSmart(b, "xor");
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean op_22_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_22_expr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, "not");
    p = r;
    r = p && expr(b, l, 2);
    exit_section_(b, l, m, OP_22_EXPR, r, p, null);
    return r || p;
  }

  // call_rightward
  public static boolean op_21_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_21_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 21 expr>");
    r = call_rightward(b, l + 1);
    exit_section_(b, l, m, OP_21_EXPR, r, false, null);
    return r;
  }

  // ',' | '=>'
  private static boolean op_20_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_20_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, PERL_COMMA);
    if (!r) r = consumeTokenSmart(b, "=>");
    exit_section_(b, m, null, r);
    return r;
  }

  // '=' | '**='|'+='|'-='| '*='|'/='|'x='| '&='|'|='|'.='| '<<='|'>>='|'%='| '&&='|'||='|'^='| '//='
  private static boolean op_19_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_19_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "=");
    if (!r) r = consumeTokenSmart(b, "**=");
    if (!r) r = consumeTokenSmart(b, "+=");
    if (!r) r = consumeTokenSmart(b, "-=");
    if (!r) r = consumeTokenSmart(b, "*=");
    if (!r) r = consumeTokenSmart(b, "/=");
    if (!r) r = consumeTokenSmart(b, "x=");
    if (!r) r = consumeTokenSmart(b, "&=");
    if (!r) r = consumeTokenSmart(b, "|=");
    if (!r) r = consumeTokenSmart(b, ".=");
    if (!r) r = consumeTokenSmart(b, "<<=");
    if (!r) r = consumeTokenSmart(b, ">>=");
    if (!r) r = consumeTokenSmart(b, "%=");
    if (!r) r = consumeTokenSmart(b, "&&=");
    if (!r) r = consumeTokenSmart(b, "||=");
    if (!r) r = consumeTokenSmart(b, "^=");
    if (!r) r = consumeTokenSmart(b, "//=");
    exit_section_(b, m, null, r);
    return r;
  }

  // ':' expr
  private static boolean op_18_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_18_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ":");
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '..'|'...'
  private static boolean op_17_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_17_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "..");
    if (!r) r = consumeTokenSmart(b, "...");
    exit_section_(b, m, null, r);
    return r;
  }

  // '||'|'//'
  private static boolean op_16_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_16_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "||");
    if (!r) r = consumeTokenSmart(b, "//");
    exit_section_(b, m, null, r);
    return r;
  }

  // '|'|'^'
  private static boolean op_14_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_14_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "|");
    if (!r) r = consumeTokenSmart(b, "^");
    exit_section_(b, m, null, r);
    return r;
  }

  // '=='|'!='|'<=>'|'eq'|'ne'|'cmp'|'~~'
  private static boolean op_12_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_12_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "==");
    if (!r) r = consumeTokenSmart(b, "!=");
    if (!r) r = consumeTokenSmart(b, "<=>");
    if (!r) r = consumeTokenSmart(b, "eq");
    if (!r) r = consumeTokenSmart(b, "ne");
    if (!r) r = consumeTokenSmart(b, "cmp");
    if (!r) r = consumeTokenSmart(b, "~~");
    exit_section_(b, m, null, r);
    return r;
  }

  // '>='|'<='|'<'|'>'|'lt'|'gt'|'le'|'ge'
  private static boolean op_11_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_11_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, ">=");
    if (!r) r = consumeTokenSmart(b, "<=");
    if (!r) r = consumeTokenSmart(b, "<");
    if (!r) r = consumeTokenSmart(b, ">");
    if (!r) r = consumeTokenSmart(b, "lt");
    if (!r) r = consumeTokenSmart(b, "gt");
    if (!r) r = consumeTokenSmart(b, "le");
    if (!r) r = consumeTokenSmart(b, "ge");
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean op_10_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_10_expr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = op_10_expr_0(b, l + 1);
    p = r;
    r = p && expr(b, l, 14);
    exit_section_(b, l, m, OP_10_EXPR, r, p, null);
    return r || p;
  }

  // 'not'|'defined'|'ref'|'exists'|'scalar'
  private static boolean op_10_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_10_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "not");
    if (!r) r = consumeTokenSmart(b, "defined");
    if (!r) r = consumeTokenSmart(b, "ref");
    if (!r) r = consumeTokenSmart(b, "exists");
    if (!r) r = consumeTokenSmart(b, "scalar");
    exit_section_(b, m, null, r);
    return r;
  }

  // '<<'|'>>'
  private static boolean op_9_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_9_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "<<");
    if (!r) r = consumeTokenSmart(b, ">>");
    exit_section_(b, m, null, r);
    return r;
  }

  // '+'|'-'|'.'
  private static boolean op_8_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_8_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "+");
    if (!r) r = consumeTokenSmart(b, "-");
    if (!r) r = consumeTokenSmart(b, ".");
    exit_section_(b, m, null, r);
    return r;
  }

  // '*'|'/'|'%'|'x'
  private static boolean op_7_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_7_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "*");
    if (!r) r = consumeTokenSmart(b, "/");
    if (!r) r = consumeTokenSmart(b, PERL_SIGIL_HASH);
    if (!r) r = consumeTokenSmart(b, "x");
    exit_section_(b, m, null, r);
    return r;
  }

  // '=~'|'!~'
  private static boolean op_6_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_6_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "=~");
    if (!r) r = consumeTokenSmart(b, "!~");
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean op_5_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_5_expr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = op_5_expr_0(b, l + 1);
    p = r;
    r = p && expr(b, l, 19);
    exit_section_(b, l, m, OP_5_EXPR, r, p, null);
    return r || p;
  }

  // '\' | '~'| '!'| '+' | '-'
  private static boolean op_5_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_5_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "\\");
    if (!r) r = consumeTokenSmart(b, "~");
    if (!r) r = consumeTokenSmart(b, "!");
    if (!r) r = consumeTokenSmart(b, "+");
    if (!r) r = consumeTokenSmart(b, "-");
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean op_3_pref_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_3_pref_expr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = op_3_pref_expr_0(b, l + 1);
    p = r;
    r = p && expr(b, l, 21);
    exit_section_(b, l, m, OP_3_PREF_EXPR, r, p, null);
    return r || p;
  }

  // '++'|'--'
  private static boolean op_3_pref_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_3_pref_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "++");
    if (!r) r = consumeTokenSmart(b, "--");
    exit_section_(b, m, null, r);
    return r;
  }

  // '++'|'--'
  private static boolean op_3_suff_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_3_suff_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "++");
    if (!r) r = consumeTokenSmart(b, "--");
    exit_section_(b, m, null, r);
    return r;
  }

  // term | grep_op | sort_op | call_leftward | variable_definition
  public static boolean op_1_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_1_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 1 expr>");
    r = term(b, l + 1);
    if (!r) r = grep_op(b, l + 1);
    if (!r) r = sort_op(b, l + 1);
    if (!r) r = call_leftward(b, l + 1);
    if (!r) r = variable_definition(b, l + 1);
    exit_section_(b, l, m, OP_1_EXPR, r, false, null);
    return r;
  }

}
