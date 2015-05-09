// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.perl5.lang.perl.lexer.PerlElementTypes.*;
import static com.perl5.lang.perl.parser.PerlParserUitl.*;
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
    if (t == BLOCK) {
      r = block(b, 0);
    }
    else if (t == BLOCK_COMPOUND) {
      r = block_compound(b, 0);
    }
    else if (t == CALLABLE) {
      r = callable(b, 0);
    }
    else if (t == COMPILE_REGEX) {
      r = compile_regex(b, 0);
    }
    else if (t == COMPOUND_STATEMENT) {
      r = compound_statement(b, 0);
    }
    else if (t == DO_TERM) {
      r = do_term(b, 0);
    }
    else if (t == EVAL_TERM) {
      r = eval_term(b, 0);
    }
    else if (t == EXPR) {
      r = expr(b, 0, -1);
    }
    else if (t == FILE_READ_TERM) {
      r = file_read_term(b, 0);
    }
    else if (t == FOR_COMPOUND) {
      r = for_compound(b, 0);
    }
    else if (t == FOR_STATEMENT_MODIFIER) {
      r = for_statement_modifier(b, 0);
    }
    else if (t == FOREACH_COMPOUND) {
      r = foreach_compound(b, 0);
    }
    else if (t == FOREACH_STATEMENT_MODIFIER) {
      r = foreach_statement_modifier(b, 0);
    }
    else if (t == GIVEN_COMPOUND) {
      r = given_compound(b, 0);
    }
    else if (t == IF_COMPOUND) {
      r = if_compound(b, 0);
    }
    else if (t == IF_STATEMENT_MODIFIER) {
      r = if_statement_modifier(b, 0);
    }
    else if (t == LABEL) {
      r = label(b, 0);
    }
    else if (t == LABEL_DECLARATION) {
      r = label_declaration(b, 0);
    }
    else if (t == LAST_STATEMENT) {
      r = last_statement(b, 0);
    }
    else if (t == MATCH_REGEX) {
      r = match_regex(b, 0);
    }
    else if (t == NEXT_STATEMENT) {
      r = next_statement(b, 0);
    }
    else if (t == NO_STATEMENT) {
      r = no_statement(b, 0);
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
    else if (t == PACKAGE_NAMESPACE) {
      r = package_namespace(b, 0);
    }
    else if (t == PERL_REGEX) {
      r = perl_regex(b, 0);
    }
    else if (t == PERL_REGEX_MODIFIERS) {
      r = perl_regex_modifiers(b, 0);
    }
    else if (t == REDO_STATEMENT) {
      r = redo_statement(b, 0);
    }
    else if (t == REFERENCE_VALUE) {
      r = reference_value(b, 0);
    }
    else if (t == REPLACEMENT_REGEX) {
      r = replacement_regex(b, 0);
    }
    else if (t == REQUIRE_STATEMENT) {
      r = require_statement(b, 0);
    }
    else if (t == SUB_DECLARATION) {
      r = sub_declaration(b, 0);
    }
    else if (t == SUB_DEFINITION) {
      r = sub_definition(b, 0);
    }
    else if (t == TR_MODIFIERS) {
      r = tr_modifiers(b, 0);
    }
    else if (t == TR_REGEX) {
      r = tr_regex(b, 0);
    }
    else if (t == TR_REPLACEMENTLIST) {
      r = tr_replacementlist(b, 0);
    }
    else if (t == TR_SEARCHLIST) {
      r = tr_searchlist(b, 0);
    }
    else if (t == UNDEF_STATEMENT) {
      r = undef_statement(b, 0);
    }
    else if (t == UNLESS_COMPOUND) {
      r = unless_compound(b, 0);
    }
    else if (t == UNLESS_STATEMENT_MODIFIER) {
      r = unless_statement_modifier(b, 0);
    }
    else if (t == UNTIL_COMPOUND) {
      r = until_compound(b, 0);
    }
    else if (t == UNTIL_STATEMENT_MODIFIER) {
      r = until_statement_modifier(b, 0);
    }
    else if (t == USE_STATEMENT) {
      r = use_statement(b, 0);
    }
    else if (t == VARIABLE_DECLARATION_GLOBAL) {
      r = variable_declaration_global(b, 0);
    }
    else if (t == VARIABLE_DECLARATION_LEXICAL) {
      r = variable_declaration_lexical(b, 0);
    }
    else if (t == VARIABLE_DECLARATION_LOCAL) {
      r = variable_declaration_local(b, 0);
    }
    else if (t == WHEN_STATEMENT_MODIFIER) {
      r = when_statement_modifier(b, 0);
    }
    else if (t == WHILE_COMPOUND) {
      r = while_compound(b, 0);
    }
    else if (t == WHILE_STATEMENT_MODIFIER) {
      r = while_statement_modifier(b, 0);
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
  // '[' expr ? ']'
  static boolean anon_array_ref(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anon_array_ref")) return false;
    if (!nextTokenIs(b, PERL_LBRACK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LBRACK);
    r = r && anon_array_ref_1(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACK);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr ?
  private static boolean anon_array_ref_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anon_array_ref_1")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // '{' expr ? '}'
  static boolean anon_hash_ref(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anon_hash_ref")) return false;
    if (!nextTokenIs(b, PERL_LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LBRACE);
    r = r && anon_hash_ref_1(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr ?
  private static boolean anon_hash_ref_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "anon_hash_ref_1")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // "{" file_items "}"
  public static boolean block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block")) return false;
    if (!nextTokenIs(b, PERL_LBRACE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeToken(b, PERL_LBRACE);
    p = r; // pin = 1
    r = r && report_error_(b, file_items(b, l + 1));
    r = p && consumeToken(b, PERL_RBRACE) && r;
    exit_section_(b, l, m, BLOCK, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // block_safe compound_continue_block ?
  public static boolean block_compound(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_compound")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<block compound>");
    r = block_safe(b, l + 1);
    r = r && block_compound_1(b, l + 1);
    exit_section_(b, l, m, BLOCK_COMPOUND, r, false, null);
    return r;
  }

  // compound_continue_block ?
  private static boolean block_compound_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_compound_1")) return false;
    compound_continue_block(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // <<parseBlock>>
  static boolean block_safe(PsiBuilder b, int l) {
    return parseBlock(b, l + 1);
  }

  /* ********************************************************** */
  // referencable_method
  //     | <<guessBareword>>
  public static boolean callable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "callable")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<callable>");
    r = referencable_method(b, l + 1);
    if (!r) r = guessBareword(b, l + 1);
    exit_section_(b, l, m, CALLABLE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "sub" block_safe
  static boolean code_ref(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_ref")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "sub");
    r = r && block_safe(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'qr' match_regex_body
  public static boolean compile_regex(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compile_regex")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<compile regex>");
    r = consumeToken(b, "qr");
    r = r && match_regex_body(b, l + 1);
    exit_section_(b, l, m, COMPILE_REGEX, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '(' expr ')' block_safe
  static boolean compound_conditional_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_conditional_block")) return false;
    if (!nextTokenIs(b, PERL_LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, PERL_RPAREN);
    r = r && block_safe(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'continue' block_safe
  static boolean compound_continue_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_continue_block")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "continue");
    r = r && block_safe(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // label_declaration ? (
  //         block_compound
  //         | if_compound
  //         | unless_compound
  //         | given_compound
  //         | while_compound
  //         | until_compound
  //         | for_compound
  //         | foreach_compound
  //      )
  public static boolean compound_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<compound statement>");
    r = compound_statement_0(b, l + 1);
    r = r && compound_statement_1(b, l + 1);
    exit_section_(b, l, m, COMPOUND_STATEMENT, r, false, null);
    return r;
  }

  // label_declaration ?
  private static boolean compound_statement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_statement_0")) return false;
    label_declaration(b, l + 1);
    return true;
  }

  // block_compound
  //         | if_compound
  //         | unless_compound
  //         | given_compound
  //         | while_compound
  //         | until_compound
  //         | for_compound
  //         | foreach_compound
  private static boolean compound_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "compound_statement_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = block_compound(b, l + 1);
    if (!r) r = if_compound(b, l + 1);
    if (!r) r = unless_compound(b, l + 1);
    if (!r) r = given_compound(b, l + 1);
    if (!r) r = while_compound(b, l + 1);
    if (!r) r = until_compound(b, l + 1);
    if (!r) r = for_compound(b, l + 1);
    if (!r) r = foreach_compound(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // "do" block_safe
  public static boolean do_term(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "do_term")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<do term>");
    r = consumeToken(b, "do");
    r = r && block_safe(b, l + 1);
    exit_section_(b, l, m, DO_TERM, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "eval" block_safe
  public static boolean eval_term(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "eval_term")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<eval term>");
    r = consumeToken(b, "eval");
    r = r && block_safe(b, l + 1);
    exit_section_(b, l, m, EVAL_TERM, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // package_namespace | package_namespace_item
  static boolean file_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file_item")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_namespace(b, l + 1);
    if (!r) r = package_namespace_item(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // file_item*
  static boolean file_items(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file_items")) return false;
    int c = current_position_(b);
    while (true) {
      if (!file_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "file_items", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // '<' perl_handle ? '>'
  public static boolean file_read_term(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file_read_term")) return false;
    if (!nextTokenIs(b, PERL_LANGLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LANGLE);
    r = r && file_read_term_1(b, l + 1);
    r = r && consumeToken(b, PERL_RANGLE);
    exit_section_(b, m, FILE_READ_TERM, r);
    return r;
  }

  // perl_handle ?
  private static boolean file_read_term_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file_read_term_1")) return false;
    perl_handle(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'for' for_compound_arguments
  public static boolean for_compound(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_compound")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<for compound>");
    r = consumeToken(b, "for");
    r = r && for_compound_arguments(b, l + 1);
    exit_section_(b, l, m, FOR_COMPOUND, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // for_compound_arguments_iteration | for_compound_arguments_list
  static boolean for_compound_arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_compound_arguments")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = for_compound_arguments_iteration(b, l + 1);
    if (!r) r = for_compound_arguments_list(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '(' expr ? PERL_SEMI expr ? PERL_SEMI expr ? ')' block_safe
  static boolean for_compound_arguments_iteration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_compound_arguments_iteration")) return false;
    if (!nextTokenIs(b, PERL_LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && for_compound_arguments_iteration_1(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    r = r && for_compound_arguments_iteration_3(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    r = r && for_compound_arguments_iteration_5(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    r = r && block_safe(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr ?
  private static boolean for_compound_arguments_iteration_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_compound_arguments_iteration_1")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  // expr ?
  private static boolean for_compound_arguments_iteration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_compound_arguments_iteration_3")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  // expr ?
  private static boolean for_compound_arguments_iteration_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_compound_arguments_iteration_5")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // (variable_declaration | variable ) ? '(' expr ')' block_compound
  static boolean for_compound_arguments_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_compound_arguments_list")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = for_compound_arguments_list_0(b, l + 1);
    r = r && consumeToken(b, PERL_LPAREN);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, PERL_RPAREN);
    r = r && block_compound(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (variable_declaration | variable ) ?
  private static boolean for_compound_arguments_list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_compound_arguments_list_0")) return false;
    for_compound_arguments_list_0_0(b, l + 1);
    return true;
  }

  // variable_declaration | variable
  private static boolean for_compound_arguments_list_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_compound_arguments_list_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable_declaration(b, l + 1);
    if (!r) r = variable(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'for' expr
  public static boolean for_statement_modifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_statement_modifier")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<for statement modifier>");
    r = consumeToken(b, "for");
    p = r; // pin = 1
    r = r && expr(b, l + 1, -1);
    exit_section_(b, l, m, FOR_STATEMENT_MODIFIER, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'foreach' for_compound_arguments
  public static boolean foreach_compound(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_compound")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<foreach compound>");
    r = consumeToken(b, "foreach");
    r = r && for_compound_arguments(b, l + 1);
    exit_section_(b, l, m, FOREACH_COMPOUND, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'foreach' expr
  public static boolean foreach_statement_modifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "foreach_statement_modifier")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<foreach statement modifier>");
    r = consumeToken(b, "foreach");
    p = r; // pin = 1
    r = r && expr(b, l + 1, -1);
    exit_section_(b, l, m, FOREACH_STATEMENT_MODIFIER, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // callable "(" expr ? ")"
  static boolean function_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = callable(b, l + 1);
    r = r && consumeToken(b, PERL_LPAREN);
    r = r && function_call_2(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr ?
  private static boolean function_call_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_2")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // 'given' compound_conditional_block
  public static boolean given_compound(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "given_compound")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<given compound>");
    r = consumeToken(b, "given");
    r = r && compound_conditional_block(b, l + 1);
    exit_section_(b, l, m, GIVEN_COMPOUND, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PERL_GLOB "{" glob_item_ref_variant "}"
  static boolean glob_item_ref(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "glob_item_ref")) return false;
    if (!nextTokenIs(b, PERL_GLOB)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_GLOB);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && glob_item_ref_variant(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // "SCALAR"
  //     | "ARRAY"
  //     | "HASH"
  //     | "CODE"
  //     | "IO"
  //     | "GLOB"
  //     | "FORMAT"
  //     | "NAME"
  //     | "PACKAGE"
  static boolean glob_item_ref_variant(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "glob_item_ref_variant")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "SCALAR");
    if (!r) r = consumeToken(b, "ARRAY");
    if (!r) r = consumeToken(b, "HASH");
    if (!r) r = consumeToken(b, "CODE");
    if (!r) r = consumeToken(b, "IO");
    if (!r) r = consumeToken(b, "GLOB");
    if (!r) r = consumeToken(b, "FORMAT");
    if (!r) r = consumeToken(b, "NAME");
    if (!r) r = consumeToken(b, "PACKAGE");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'if' compound_conditional_block if_compound_elsif * if_compound_else ?
  public static boolean if_compound(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_compound")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<if compound>");
    r = consumeToken(b, "if");
    r = r && compound_conditional_block(b, l + 1);
    r = r && if_compound_2(b, l + 1);
    r = r && if_compound_3(b, l + 1);
    exit_section_(b, l, m, IF_COMPOUND, r, false, null);
    return r;
  }

  // if_compound_elsif *
  private static boolean if_compound_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_compound_2")) return false;
    int c = current_position_(b);
    while (true) {
      if (!if_compound_elsif(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "if_compound_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // if_compound_else ?
  private static boolean if_compound_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_compound_3")) return false;
    if_compound_else(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'else' block_safe
  static boolean if_compound_else(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_compound_else")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "else");
    r = r && block_safe(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'elsif' compound_conditional_block
  static boolean if_compound_elsif(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_compound_elsif")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "elsif");
    r = r && compound_conditional_block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'if' expr
  public static boolean if_statement_modifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_statement_modifier")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<if statement modifier>");
    r = consumeToken(b, "if");
    p = r; // pin = 1
    r = r && expr(b, l + 1, -1);
    exit_section_(b, l, m, IF_STATEMENT_MODIFIER, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // PERL_BAREWORD
  public static boolean label(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label")) return false;
    if (!nextTokenIs(b, PERL_BAREWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_BAREWORD);
    exit_section_(b, m, LABEL, r);
    return r;
  }

  /* ********************************************************** */
  // label ":"
  public static boolean label_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label_declaration")) return false;
    if (!nextTokenIs(b, PERL_BAREWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = label(b, l + 1);
    r = r && consumeToken(b, PERL_COLON);
    exit_section_(b, m, LABEL_DECLARATION, r);
    return r;
  }

  /* ********************************************************** */
  // 'last' lnr_param ?
  public static boolean last_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "last_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<last statement>");
    r = consumeToken(b, "last");
    r = r && last_statement_1(b, l + 1);
    exit_section_(b, l, m, LAST_STATEMENT, r, false, null);
    return r;
  }

  // lnr_param ?
  private static boolean last_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "last_statement_1")) return false;
    lnr_param(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // string_list
  static boolean list_primitive(PsiBuilder b, int l) {
    return string_list(b, l + 1);
  }

  /* ********************************************************** */
  // label | expr
  static boolean lnr_param(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "lnr_param")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = label(b, l + 1);
    if (!r) r = expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'm'? match_regex_body
  public static boolean match_regex(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "match_regex")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<match regex>");
    r = match_regex_0(b, l + 1);
    r = r && match_regex_body(b, l + 1);
    exit_section_(b, l, m, MATCH_REGEX, r, false, null);
    return r;
  }

  // 'm'?
  private static boolean match_regex_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "match_regex_0")) return false;
    consumeToken(b, "m");
    return true;
  }

  /* ********************************************************** */
  // PERL_REGEX_QUOTE perl_regex ? PERL_REGEX_QUOTE perl_regex_modifiers ?
  static boolean match_regex_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "match_regex_body")) return false;
    if (!nextTokenIs(b, PERL_REGEX_QUOTE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_REGEX_QUOTE);
    r = r && match_regex_body_1(b, l + 1);
    r = r && consumeToken(b, PERL_REGEX_QUOTE);
    r = r && match_regex_body_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // perl_regex ?
  private static boolean match_regex_body_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "match_regex_body_1")) return false;
    perl_regex(b, l + 1);
    return true;
  }

  // perl_regex_modifiers ?
  private static boolean match_regex_body_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "match_regex_body_3")) return false;
    perl_regex_modifiers(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // "{" (PERL_BAREWORD | expr ) "}"
  //     | "[" (PERL_NUMBER | expr ) "]"
  //     | function_call
  static boolean nested_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nested_element")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = nested_element_0(b, l + 1);
    if (!r) r = nested_element_1(b, l + 1);
    if (!r) r = function_call(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // "{" (PERL_BAREWORD | expr ) "}"
  private static boolean nested_element_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nested_element_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LBRACE);
    r = r && nested_element_0_1(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_BAREWORD | expr
  private static boolean nested_element_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nested_element_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_BAREWORD);
    if (!r) r = expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // "[" (PERL_NUMBER | expr ) "]"
  private static boolean nested_element_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nested_element_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LBRACK);
    r = r && nested_element_1_1(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACK);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_NUMBER | expr
  private static boolean nested_element_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "nested_element_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_NUMBER);
    if (!r) r = expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'next' lnr_param ?
  public static boolean next_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "next_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<next statement>");
    r = consumeToken(b, "next");
    r = r && next_statement_1(b, l + 1);
    exit_section_(b, l, m, NEXT_STATEMENT, r, false, null);
    return r;
  }

  // lnr_param ?
  private static boolean next_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "next_statement_1")) return false;
    lnr_param(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'no' <<parseNoStatement>>
  public static boolean no_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "no_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<no statement>");
    r = consumeToken(b, "no");
    p = r; // pin = 1
    r = r && parseNoStatement(b, l + 1);
    exit_section_(b, l, m, NO_STATEMENT, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'package' <<parsePerlPackage>>
  public static boolean package_namespace(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<package namespace>");
    r = consumeToken(b, "package");
    p = r; // pin = 1
    r = r && parsePerlPackage(b, l + 1);
    exit_section_(b, l, m, PACKAGE_NAMESPACE, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // sub_definition | compound_statement PERL_SEMI ? | statement
  static boolean package_namespace_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_item")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_definition(b, l + 1);
    if (!r) r = package_namespace_item_1(b, l + 1);
    if (!r) r = statement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // compound_statement PERL_SEMI ?
  private static boolean package_namespace_item_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_item_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = compound_statement(b, l + 1);
    r = r && package_namespace_item_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_SEMI ?
  private static boolean package_namespace_item_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_item_1_1")) return false;
    consumeToken(b, PERL_SEMI);
    return true;
  }

  /* ********************************************************** */
  // package_namespace_item *
  static boolean package_plain(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_plain")) return false;
    int c = current_position_(b);
    while (true) {
      if (!package_namespace_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "package_plain", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // <<parseFile>> <<eof>>
  static boolean perlFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perlFile")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseFile(b, l + 1);
    r = r && eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // <<parseBarewordHandle>> | PERL_SCALAR | "{" expr "}"
  static boolean perl_handle(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_handle")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseBarewordHandle(b, l + 1);
    if (!r) r = consumeToken(b, PERL_SCALAR);
    if (!r) r = perl_handle_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // "{" expr "}"
  private static boolean perl_handle_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_handle_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LBRACE);
    r = r && expr(b, l + 1, -1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // <<parseBarewordPackage>>
  static boolean perl_package(PsiBuilder b, int l) {
    return parseBarewordPackage(b, l + 1);
  }

  /* ********************************************************** */
  // PERL_REGEX_TOKEN +
  public static boolean perl_regex(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_regex")) return false;
    if (!nextTokenIs(b, PERL_REGEX_TOKEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_REGEX_TOKEN);
    int c = current_position_(b);
    while (r) {
      if (!consumeToken(b, PERL_REGEX_TOKEN)) break;
      if (!empty_element_parsed_guard_(b, "perl_regex", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, PERL_REGEX, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_REGEX_MODIFIER +
  public static boolean perl_regex_modifiers(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_regex_modifiers")) return false;
    if (!nextTokenIs(b, PERL_REGEX_MODIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_REGEX_MODIFIER);
    int c = current_position_(b);
    while (r) {
      if (!consumeToken(b, PERL_REGEX_MODIFIER)) break;
      if (!empty_element_parsed_guard_(b, "perl_regex_modifiers", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, PERL_REGEX_MODIFIERS, r);
    return r;
  }

  /* ********************************************************** */
  // <<parseVersion>>
  static boolean perl_version(PsiBuilder b, int l) {
    return parseVersion(b, l + 1);
  }

  /* ********************************************************** */
  // perl_handle expr | expr
  static boolean print_arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "print_arguments")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = print_arguments_0(b, l + 1);
    if (!r) r = expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // perl_handle expr
  private static boolean print_arguments_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "print_arguments_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_handle(b, l + 1);
    r = r && expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ("print"|"say") ( "(" print_arguments ? ")" | print_arguments ? )
  static boolean print_term(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "print_term")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = print_term_0(b, l + 1);
    p = r; // pin = 1
    r = r && print_term_1(b, l + 1);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  // "print"|"say"
  private static boolean print_term_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "print_term_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "print");
    if (!r) r = consumeToken(b, "say");
    exit_section_(b, m, null, r);
    return r;
  }

  // "(" print_arguments ? ")" | print_arguments ?
  private static boolean print_term_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "print_term_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = print_term_1_0(b, l + 1);
    if (!r) r = print_term_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // "(" print_arguments ? ")"
  private static boolean print_term_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "print_term_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && print_term_1_0_1(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // print_arguments ?
  private static boolean print_term_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "print_term_1_0_1")) return false;
    print_arguments(b, l + 1);
    return true;
  }

  // print_arguments ?
  private static boolean print_term_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "print_term_1_1")) return false;
    print_arguments(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'redo' lnr_param ?
  public static boolean redo_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "redo_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<redo statement>");
    r = consumeToken(b, "redo");
    r = r && redo_statement_1(b, l + 1);
    exit_section_(b, l, m, REDO_STATEMENT, r, false, null);
    return r;
  }

  // lnr_param ?
  private static boolean redo_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "redo_statement_1")) return false;
    lnr_param(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '&'
  //     (
  //         <<guessBareword>>
  //         | {expr}
  //         | scalar
  //     )
  static boolean referencable_method(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "referencable_method")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "&");
    r = r && referencable_method_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // <<guessBareword>>
  //         | {expr}
  //         | scalar
  private static boolean referencable_method_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "referencable_method_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = guessBareword(b, l + 1);
    if (!r) r = referencable_method_1_1(b, l + 1);
    if (!r) r = scalar(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // {expr}
  private static boolean referencable_method_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "referencable_method_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // anon_array_ref
  //     | anon_hash_ref
  //     | code_ref
  //     | glob_item_ref
  public static boolean reference_value(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reference_value")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<reference value>");
    r = anon_array_ref(b, l + 1);
    if (!r) r = anon_hash_ref(b, l + 1);
    if (!r) r = code_ref(b, l + 1);
    if (!r) r = glob_item_ref(b, l + 1);
    exit_section_(b, l, m, REFERENCE_VALUE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PERL_REGEX_QUOTE perl_regex ? PERL_REGEX_QUOTE
  //     | perl_regex ? PERL_REGEX_QUOTE
  static boolean regex_replacement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "regex_replacement")) return false;
    if (!nextTokenIs(b, "", PERL_REGEX_QUOTE, PERL_REGEX_TOKEN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = regex_replacement_0(b, l + 1);
    if (!r) r = regex_replacement_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_REGEX_QUOTE perl_regex ? PERL_REGEX_QUOTE
  private static boolean regex_replacement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "regex_replacement_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_REGEX_QUOTE);
    r = r && regex_replacement_0_1(b, l + 1);
    r = r && consumeToken(b, PERL_REGEX_QUOTE);
    exit_section_(b, m, null, r);
    return r;
  }

  // perl_regex ?
  private static boolean regex_replacement_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "regex_replacement_0_1")) return false;
    perl_regex(b, l + 1);
    return true;
  }

  // perl_regex ? PERL_REGEX_QUOTE
  private static boolean regex_replacement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "regex_replacement_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = regex_replacement_1_0(b, l + 1);
    r = r && consumeToken(b, PERL_REGEX_QUOTE);
    exit_section_(b, m, null, r);
    return r;
  }

  // perl_regex ?
  private static boolean regex_replacement_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "regex_replacement_1_0")) return false;
    perl_regex(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // compile_regex
  //     | replacement_regex
  //     | tr_regex
  //     | match_regex
  static boolean regex_term(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "regex_term")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = compile_regex(b, l + 1);
    if (!r) r = replacement_regex(b, l + 1);
    if (!r) r = tr_regex(b, l + 1);
    if (!r) r = match_regex(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 's' PERL_REGEX_QUOTE perl_regex ? PERL_REGEX_QUOTE regex_replacement perl_regex_modifiers ?
  public static boolean replacement_regex(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "replacement_regex")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<replacement regex>");
    r = consumeToken(b, "s");
    r = r && consumeToken(b, PERL_REGEX_QUOTE);
    r = r && replacement_regex_2(b, l + 1);
    r = r && consumeToken(b, PERL_REGEX_QUOTE);
    r = r && regex_replacement(b, l + 1);
    r = r && replacement_regex_5(b, l + 1);
    exit_section_(b, l, m, REPLACEMENT_REGEX, r, false, null);
    return r;
  }

  // perl_regex ?
  private static boolean replacement_regex_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "replacement_regex_2")) return false;
    perl_regex(b, l + 1);
    return true;
  }

  // perl_regex_modifiers ?
  private static boolean replacement_regex_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "replacement_regex_5")) return false;
    perl_regex_modifiers(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'require' (perl_package | perl_version | string)
  public static boolean require_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "require_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<require statement>");
    r = consumeToken(b, "require");
    p = r; // pin = 1
    r = r && require_statement_1(b, l + 1);
    exit_section_(b, l, m, REQUIRE_STATEMENT, r, p, null);
    return r || p;
  }

  // perl_package | perl_version | string
  private static boolean require_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "require_statement_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_package(b, l + 1);
    if (!r) r = perl_version(b, l + 1);
    if (!r) r = string(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_SCALAR nested_element    // hash or array item
  //     | PERL_SCALAR
  //     | PERL_SIGIL_SCALAR "{" PERL_BAREWORD "}"
  //     | 'undef'
  static boolean scalar(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scalar_0(b, l + 1);
    if (!r) r = consumeToken(b, PERL_SCALAR);
    if (!r) r = scalar_2(b, l + 1);
    if (!r) r = consumeToken(b, "undef");
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_SCALAR nested_element
  private static boolean scalar_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SCALAR);
    r = r && nested_element(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_SIGIL_SCALAR "{" PERL_BAREWORD "}"
  private static boolean scalar_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SIGIL_SCALAR);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && consumeToken(b, PERL_BAREWORD);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_NUMBER
  //     | string
  static boolean scalar_primitive(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_primitive")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_NUMBER);
    if (!r) r = string(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // statement_variation statement_modifier ? (<<statementSemi>>|<<eof>>)
  static boolean statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement_variation(b, l + 1);
    r = r && statement_1(b, l + 1);
    r = r && statement_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // statement_modifier ?
  private static boolean statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_1")) return false;
    statement_modifier(b, l + 1);
    return true;
  }

  // <<statementSemi>>|<<eof>>
  private static boolean statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statementSemi(b, l + 1);
    if (!r) r = eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // if_statement_modifier
  //     | unless_statement_modifier
  //     | while_statement_modifier
  //     | until_statement_modifier
  //     | for_statement_modifier
  //     | foreach_statement_modifier
  //     | when_statement_modifier
  static boolean statement_modifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_modifier")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = if_statement_modifier(b, l + 1);
    if (!r) r = unless_statement_modifier(b, l + 1);
    if (!r) r = while_statement_modifier(b, l + 1);
    if (!r) r = until_statement_modifier(b, l + 1);
    if (!r) r = for_statement_modifier(b, l + 1);
    if (!r) r = foreach_statement_modifier(b, l + 1);
    if (!r) r = when_statement_modifier(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // use_statement
  //     | no_statement
  //     | undef_statement
  //     | last_statement
  //     | next_statement
  //     | redo_statement
  //     | require_statement
  //     | sub_declaration
  //     | expr
  static boolean statement_variation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_variation")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = use_statement(b, l + 1);
    if (!r) r = no_statement(b, l + 1);
    if (!r) r = undef_statement(b, l + 1);
    if (!r) r = last_statement(b, l + 1);
    if (!r) r = next_statement(b, l + 1);
    if (!r) r = redo_statement(b, l + 1);
    if (!r) r = require_statement(b, l + 1);
    if (!r) r = sub_declaration(b, l + 1);
    if (!r) r = expr(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // string_quoted
  static boolean string(PsiBuilder b, int l) {
    return string_quoted(b, l + 1);
  }

  /* ********************************************************** */
  // 'qw' PERL_QUOTE <<parseBarewordString>> * PERL_QUOTE
  static boolean string_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_list")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "qw");
    r = r && consumeToken(b, PERL_QUOTE);
    r = r && string_list_2(b, l + 1);
    r = r && consumeToken(b, PERL_QUOTE);
    exit_section_(b, m, null, r);
    return r;
  }

  // <<parseBarewordString>> *
  private static boolean string_list_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_list_2")) return false;
    int c = current_position_(b);
    while (true) {
      if (!parseBarewordString(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "string_list_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // ('qq' | 'qx' | 'q') ? PERL_QUOTE <<parseBarewordString>> PERL_QUOTE
  static boolean string_quoted(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_quoted")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = string_quoted_0(b, l + 1);
    r = r && consumeToken(b, PERL_QUOTE);
    r = r && parseBarewordString(b, l + 1);
    r = r && consumeToken(b, PERL_QUOTE);
    exit_section_(b, m, null, r);
    return r;
  }

  // ('qq' | 'qx' | 'q') ?
  private static boolean string_quoted_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_quoted_0")) return false;
    string_quoted_0_0(b, l + 1);
    return true;
  }

  // 'qq' | 'qx' | 'q'
  private static boolean string_quoted_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_quoted_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "qq");
    if (!r) r = consumeToken(b, "qx");
    if (!r) r = consumeToken(b, "q");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ":" <<parseSubAttributes>>
  static boolean sub_attributes(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_attributes")) return false;
    if (!nextTokenIs(b, PERL_COLON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_COLON);
    r = r && parseSubAttributes(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'sub' <<parseSubDeclaration>>
  public static boolean sub_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_declaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<sub declaration>");
    r = consumeToken(b, "sub");
    r = r && parseSubDeclaration(b, l + 1);
    exit_section_(b, l, m, SUB_DECLARATION, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // sub_prototype ? sub_attributes ?
  static boolean sub_declaration_parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_declaration_parameters")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_declaration_parameters_0(b, l + 1);
    r = r && sub_declaration_parameters_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // sub_prototype ?
  private static boolean sub_declaration_parameters_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_declaration_parameters_0")) return false;
    sub_prototype(b, l + 1);
    return true;
  }

  // sub_attributes ?
  private static boolean sub_declaration_parameters_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_declaration_parameters_1")) return false;
    sub_attributes(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'sub' <<parseSubDefinition>>
  public static boolean sub_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_definition")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<sub definition>");
    r = consumeToken(b, "sub");
    r = r && parseSubDefinition(b, l + 1);
    exit_section_(b, l, m, SUB_DEFINITION, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // sub_prototype sub_attributes ?
  //     | sub_attributes sub_signature ?
  //     | sub_signature
  static boolean sub_definition_parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_definition_parameters")) return false;
    if (!nextTokenIs(b, "", PERL_LPAREN, PERL_COLON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_definition_parameters_0(b, l + 1);
    if (!r) r = sub_definition_parameters_1(b, l + 1);
    if (!r) r = sub_signature(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // sub_prototype sub_attributes ?
  private static boolean sub_definition_parameters_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_definition_parameters_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_prototype(b, l + 1);
    r = r && sub_definition_parameters_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // sub_attributes ?
  private static boolean sub_definition_parameters_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_definition_parameters_0_1")) return false;
    sub_attributes(b, l + 1);
    return true;
  }

  // sub_attributes sub_signature ?
  private static boolean sub_definition_parameters_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_definition_parameters_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_attributes(b, l + 1);
    r = r && sub_definition_parameters_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // sub_signature ?
  private static boolean sub_definition_parameters_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_definition_parameters_1_1")) return false;
    sub_signature(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // "(" <<parseSubPrototype>> ")"
  static boolean sub_prototype(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_prototype")) return false;
    if (!nextTokenIs(b, PERL_LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && parseSubPrototype(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // "(" <<parseSubSignature>> ")"
  static boolean sub_signature(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_signature")) return false;
    if (!nextTokenIs(b, PERL_LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && parseSubSignature(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // variable
  //     | variable_declaration
  //     | PERL_OPERATOR_UNARY "(" expr ? ")" // named operators as functions
  //     | "(" expr ? ")"
  //     | scalar_primitive
  //     | list_primitive
  //     | do_term
  //     | eval_term
  //     | regex_term
  //     | file_read_term
  //     | reference_value
  //     | print_term
  //     | function_call
  static boolean term(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable(b, l + 1);
    if (!r) r = variable_declaration(b, l + 1);
    if (!r) r = term_2(b, l + 1);
    if (!r) r = term_3(b, l + 1);
    if (!r) r = scalar_primitive(b, l + 1);
    if (!r) r = list_primitive(b, l + 1);
    if (!r) r = do_term(b, l + 1);
    if (!r) r = eval_term(b, l + 1);
    if (!r) r = regex_term(b, l + 1);
    if (!r) r = file_read_term(b, l + 1);
    if (!r) r = reference_value(b, l + 1);
    if (!r) r = print_term(b, l + 1);
    if (!r) r = function_call(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_OPERATOR_UNARY "(" expr ? ")"
  private static boolean term_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_OPERATOR_UNARY);
    r = r && consumeToken(b, PERL_LPAREN);
    r = r && term_2_2(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr ?
  private static boolean term_2_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term_2_2")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  // "(" expr ? ")"
  private static boolean term_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && term_3_1(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // expr ?
  private static boolean term_3_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term_3_1")) return false;
    expr(b, l + 1, -1);
    return true;
  }

  /* ********************************************************** */
  // PERL_REGEX_MODIFIER +
  public static boolean tr_modifiers(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tr_modifiers")) return false;
    if (!nextTokenIs(b, PERL_REGEX_MODIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_REGEX_MODIFIER);
    int c = current_position_(b);
    while (r) {
      if (!consumeToken(b, PERL_REGEX_MODIFIER)) break;
      if (!empty_element_parsed_guard_(b, "tr_modifiers", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, TR_MODIFIERS, r);
    return r;
  }

  /* ********************************************************** */
  // ('tr'|'y') PERL_REGEX_QUOTE tr_searchlist PERL_REGEX_QUOTE PERL_REGEX_QUOTE ? tr_replacementlist PERL_REGEX_QUOTE tr_modifiers ?
  public static boolean tr_regex(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tr_regex")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<tr regex>");
    r = tr_regex_0(b, l + 1);
    r = r && consumeToken(b, PERL_REGEX_QUOTE);
    r = r && tr_searchlist(b, l + 1);
    r = r && consumeToken(b, PERL_REGEX_QUOTE);
    r = r && tr_regex_4(b, l + 1);
    r = r && tr_replacementlist(b, l + 1);
    r = r && consumeToken(b, PERL_REGEX_QUOTE);
    r = r && tr_regex_7(b, l + 1);
    exit_section_(b, l, m, TR_REGEX, r, false, null);
    return r;
  }

  // 'tr'|'y'
  private static boolean tr_regex_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tr_regex_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "tr");
    if (!r) r = consumeToken(b, "y");
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_REGEX_QUOTE ?
  private static boolean tr_regex_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tr_regex_4")) return false;
    consumeToken(b, PERL_REGEX_QUOTE);
    return true;
  }

  // tr_modifiers ?
  private static boolean tr_regex_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tr_regex_7")) return false;
    tr_modifiers(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // PERL_STRING_CONTENT
  public static boolean tr_replacementlist(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tr_replacementlist")) return false;
    if (!nextTokenIs(b, PERL_STRING_CONTENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_STRING_CONTENT);
    exit_section_(b, m, TR_REPLACEMENTLIST, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_STRING_CONTENT
  public static boolean tr_searchlist(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tr_searchlist")) return false;
    if (!nextTokenIs(b, PERL_STRING_CONTENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_STRING_CONTENT);
    exit_section_(b, m, TR_SEARCHLIST, r);
    return r;
  }

  /* ********************************************************** */
  // "undef" variable
  public static boolean undef_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "undef_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<undef statement>");
    r = consumeToken(b, "undef");
    p = r; // pin = 1
    r = r && variable(b, l + 1);
    exit_section_(b, l, m, UNDEF_STATEMENT, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'unless' compound_conditional_block if_compound_elsif * if_compound_else ?
  public static boolean unless_compound(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unless_compound")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<unless compound>");
    r = consumeToken(b, "unless");
    r = r && compound_conditional_block(b, l + 1);
    r = r && unless_compound_2(b, l + 1);
    r = r && unless_compound_3(b, l + 1);
    exit_section_(b, l, m, UNLESS_COMPOUND, r, false, null);
    return r;
  }

  // if_compound_elsif *
  private static boolean unless_compound_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unless_compound_2")) return false;
    int c = current_position_(b);
    while (true) {
      if (!if_compound_elsif(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "unless_compound_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // if_compound_else ?
  private static boolean unless_compound_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unless_compound_3")) return false;
    if_compound_else(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'unless' expr
  public static boolean unless_statement_modifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unless_statement_modifier")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<unless statement modifier>");
    r = consumeToken(b, "unless");
    p = r; // pin = 1
    r = r && expr(b, l + 1, -1);
    exit_section_(b, l, m, UNLESS_STATEMENT_MODIFIER, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'until' compound_conditional_block compound_continue_block ?
  public static boolean until_compound(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "until_compound")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<until compound>");
    r = consumeToken(b, "until");
    r = r && compound_conditional_block(b, l + 1);
    r = r && until_compound_2(b, l + 1);
    exit_section_(b, l, m, UNTIL_COMPOUND, r, false, null);
    return r;
  }

  // compound_continue_block ?
  private static boolean until_compound_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "until_compound_2")) return false;
    compound_continue_block(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'until' expr
  public static boolean until_statement_modifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "until_statement_modifier")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<until statement modifier>");
    r = consumeToken(b, "until");
    p = r; // pin = 1
    r = r && expr(b, l + 1, -1);
    exit_section_(b, l, m, UNTIL_STATEMENT_MODIFIER, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'use' <<parseUseStatement>>
  public static boolean use_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "use_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<use statement>");
    r = consumeToken(b, "use");
    p = r; // pin = 1
    r = r && parseUseStatement(b, l + 1);
    exit_section_(b, l, m, USE_STATEMENT, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // scalar | PERL_ARRAY | PERL_HASH | PERL_GLOB
  static boolean variable(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = scalar(b, l + 1);
    if (!r) r = consumeToken(b, PERL_ARRAY);
    if (!r) r = consumeToken(b, PERL_HASH);
    if (!r) r = consumeToken(b, PERL_GLOB);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // variable_declaration_global
  //     | variable_declaration_lexical
  //     | variable_declaration_local
  static boolean variable_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declaration")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable_declaration_global(b, l + 1);
    if (!r) r = variable_declaration_lexical(b, l + 1);
    if (!r) r = variable_declaration_local(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'our' perl_package ? variable_definition_variation
  public static boolean variable_declaration_global(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declaration_global")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<variable declaration global>");
    r = consumeToken(b, "our");
    r = r && variable_declaration_global_1(b, l + 1);
    r = r && variable_definition_variation(b, l + 1);
    exit_section_(b, l, m, VARIABLE_DECLARATION_GLOBAL, r, false, null);
    return r;
  }

  // perl_package ?
  private static boolean variable_declaration_global_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declaration_global_1")) return false;
    perl_package(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ('my' | 'state') perl_package ? variable_definition_variation
  public static boolean variable_declaration_lexical(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declaration_lexical")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<variable declaration lexical>");
    r = variable_declaration_lexical_0(b, l + 1);
    r = r && variable_declaration_lexical_1(b, l + 1);
    r = r && variable_definition_variation(b, l + 1);
    exit_section_(b, l, m, VARIABLE_DECLARATION_LEXICAL, r, false, null);
    return r;
  }

  // 'my' | 'state'
  private static boolean variable_declaration_lexical_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declaration_lexical_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "my");
    if (!r) r = consumeToken(b, "state");
    exit_section_(b, m, null, r);
    return r;
  }

  // perl_package ?
  private static boolean variable_declaration_lexical_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declaration_lexical_1")) return false;
    perl_package(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'local' perl_package ?  variable_definition_variation
  public static boolean variable_declaration_local(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declaration_local")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<variable declaration local>");
    r = consumeToken(b, "local");
    r = r && variable_declaration_local_1(b, l + 1);
    r = r && variable_definition_variation(b, l + 1);
    exit_section_(b, l, m, VARIABLE_DECLARATION_LOCAL, r, false, null);
    return r;
  }

  // perl_package ?
  private static boolean variable_declaration_local_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declaration_local_1")) return false;
    perl_package(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '(' variable (',' variable ) + ')'
  //     | variable
  static boolean variable_definition_variation(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_definition_variation")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable_definition_variation_0(b, l + 1);
    if (!r) r = variable(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '(' variable (',' variable ) + ')'
  private static boolean variable_definition_variation_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_definition_variation_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && variable(b, l + 1);
    r = r && variable_definition_variation_0_2(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // (',' variable ) +
  private static boolean variable_definition_variation_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_definition_variation_0_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable_definition_variation_0_2_0(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!variable_definition_variation_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "variable_definition_variation_0_2", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // ',' variable
  private static boolean variable_definition_variation_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_definition_variation_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_COMMA);
    r = r && variable(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'when' expr
  public static boolean when_statement_modifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "when_statement_modifier")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<when statement modifier>");
    r = consumeToken(b, "when");
    p = r; // pin = 1
    r = r && expr(b, l + 1, -1);
    exit_section_(b, l, m, WHEN_STATEMENT_MODIFIER, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'while' compound_conditional_block compound_continue_block ?
  public static boolean while_compound(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_compound")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<while compound>");
    r = consumeToken(b, "while");
    r = r && compound_conditional_block(b, l + 1);
    r = r && while_compound_2(b, l + 1);
    exit_section_(b, l, m, WHILE_COMPOUND, r, false, null);
    return r;
  }

  // compound_continue_block ?
  private static boolean while_compound_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_compound_2")) return false;
    compound_continue_block(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'while' expr
  public static boolean while_statement_modifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_statement_modifier")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<while statement modifier>");
    r = consumeToken(b, "while");
    p = r; // pin = 1
    r = r && expr(b, l + 1, -1);
    exit_section_(b, l, m, WHILE_STATEMENT_MODIFIER, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // Expression root: expr
  // Operator priority table:
  // 0: BINARY(op_24_expr)
  // 1: BINARY(op_23_expr)
  // 2: PREFIX(op_22_expr)
  // 3: ATOM(op_21_expr)
  // 4: POSTFIX(op_20_expr)
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
  // 22: POSTFIX(op_2_expr)
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
        r = true;
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
      else if (g < 22 && op_2_expr_0(b, l + 1)) {
        r = true;
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

  // callable !"(" <<parseExpressionLevel 3>> ?
  public static boolean op_21_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_21_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 21 expr>");
    r = callable(b, l + 1);
    r = r && op_21_expr_1(b, l + 1);
    r = r && op_21_expr_2(b, l + 1);
    exit_section_(b, l, m, OP_21_EXPR, r, false, null);
    return r;
  }

  // !"("
  private static boolean op_21_expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_21_expr_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_, null);
    r = !consumeTokenSmart(b, PERL_LPAREN);
    exit_section_(b, l, m, null, r, false, null);
    return r;
  }

  // <<parseExpressionLevel 3>> ?
  private static boolean op_21_expr_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_21_expr_2")) return false;
    parseExpressionLevel(b, l + 1, 3);
    return true;
  }

  // (','|'=>') <<parseExpressionLevel 4>> ?
  private static boolean op_20_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_20_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = op_20_expr_0_0(b, l + 1);
    r = r && op_20_expr_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ','|'=>'
  private static boolean op_20_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_20_expr_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, PERL_COMMA);
    if (!r) r = consumeTokenSmart(b, PERL_ARROW_COMMA);
    exit_section_(b, m, null, r);
    return r;
  }

  // <<parseExpressionLevel 4>> ?
  private static boolean op_20_expr_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_20_expr_0_1")) return false;
    parseExpressionLevel(b, l + 1, 4);
    return true;
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
    r = consumeToken(b, PERL_COLON);
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
    if (!r) r = consumeTokenSmart(b, PERL_LANGLE);
    if (!r) r = consumeTokenSmart(b, PERL_RANGLE);
    if (!r) r = consumeTokenSmart(b, "lt");
    if (!r) r = consumeTokenSmart(b, "gt");
    if (!r) r = consumeTokenSmart(b, "le");
    if (!r) r = consumeTokenSmart(b, "ge");
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean op_10_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_10_expr")) return false;
    if (!nextTokenIsFast(b, PERL_OPERATOR_FILETEST, PERL_OPERATOR_UNARY)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = op_10_expr_0(b, l + 1);
    p = r;
    r = p && expr(b, l, 14);
    exit_section_(b, l, m, OP_10_EXPR, r, p, null);
    return r || p;
  }

  // PERL_OPERATOR_UNARY !"(" | PERL_OPERATOR_FILETEST
  private static boolean op_10_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_10_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = op_10_expr_0_0(b, l + 1);
    if (!r) r = consumeTokenSmart(b, PERL_OPERATOR_FILETEST);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_OPERATOR_UNARY !"("
  private static boolean op_10_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_10_expr_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, PERL_OPERATOR_UNARY);
    r = r && op_10_expr_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // !"("
  private static boolean op_10_expr_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_10_expr_0_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_, null);
    r = !consumeTokenSmart(b, PERL_LPAREN);
    exit_section_(b, l, m, null, r, false, null);
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

  // ('+'|'-'|'.') !PERL_BAREWORD
  private static boolean op_8_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_8_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = op_8_expr_0_0(b, l + 1);
    r = r && op_8_expr_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '+'|'-'|'.'
  private static boolean op_8_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_8_expr_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "+");
    if (!r) r = consumeTokenSmart(b, "-");
    if (!r) r = consumeTokenSmart(b, ".");
    exit_section_(b, m, null, r);
    return r;
  }

  // !PERL_BAREWORD
  private static boolean op_8_expr_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_8_expr_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_, null);
    r = !consumeTokenSmart(b, PERL_BAREWORD);
    exit_section_(b, l, m, null, r, false, null);
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

  // '->' ? nested_element
  private static boolean op_2_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_2_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = op_2_expr_0_0(b, l + 1);
    r = r && nested_element(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '->' ?
  private static boolean op_2_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_2_expr_0_0")) return false;
    consumeTokenSmart(b, PERL_DEREFERENCE);
    return true;
  }

  // term
  public static boolean op_1_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_1_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, "<op 1 expr>");
    r = term(b, l + 1);
    exit_section_(b, l, m, OP_1_EXPR, r, false, null);
    return r;
  }

}
