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
    else if (t == EXPR) {
      r = expr(b, 0, -1);
    }
    else if (t == LABEL) {
      r = label(b, 0);
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
    else if (t == SCALAR_PRIMITIVE) {
      r = scalar_primitive(b, 0);
    }
    else if (t == USE_STATEMENT) {
      r = use_statement(b, 0);
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
  // label ?  "{" file_items "}"
  public static boolean block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block")) return false;
    if (!nextTokenIs(b, "<block>", PERL_LBRACE, PERL_BAREWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<block>");
    r = block_0(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACE);
    p = r; // pin = 2
    r = r && report_error_(b, file_items(b, l + 1));
    r = p && consumeToken(b, PERL_RBRACE) && r;
    exit_section_(b, l, m, BLOCK, r, p, null);
    return r || p;
  }

  // label ?
  private static boolean block_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_0")) return false;
    label(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // <<parseBlock>>
  static boolean block_parsed(PsiBuilder b, int l) {
    return parseBlock(b, l + 1);
  }

  /* ********************************************************** */
  // block_parsed PERL_SEMI ?
  static boolean code_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_block")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = block_parsed(b, l + 1);
    r = r && code_block_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_SEMI ?
  private static boolean code_block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "code_block_1")) return false;
    consumeToken(b, PERL_SEMI);
    return true;
  }

  /* ********************************************************** */
  // sub_definition_or_declaration
  //     | code_block
  static boolean declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declaration")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_definition_or_declaration(b, l + 1);
    if (!r) r = code_block(b, l + 1);
    exit_section_(b, m, null, r);
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
    Marker m = enter_section_(b, l, _NONE_, null);
    int c = current_position_(b);
    while (true) {
      if (!file_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "file_items", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, l, m, null, true, false, recover_block_parser_);
    return true;
  }

  /* ********************************************************** */
  // PERL_BAREWORD ":"
  public static boolean label(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "label")) return false;
    if (!nextTokenIs(b, PERL_BAREWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_BAREWORD);
    r = r && consumeToken(b, ":");
    exit_section_(b, m, LABEL, r);
    return r;
  }

  /* ********************************************************** */
  // 'no' <<captureStrings>> package_use_arguments <<processNoStatement>>
  public static boolean no_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "no_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<no statement>");
    r = consumeToken(b, "no");
    p = r; // pin = 1
    r = r && report_error_(b, captureStrings(b, l + 1));
    r = p && report_error_(b, package_use_arguments(b, l + 1)) && r;
    r = p && processNoStatement(b, l + 1) && r;
    exit_section_(b, l, m, NO_STATEMENT, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'package' perl_package perl_version ? package_namespace_content
  public static boolean package_namespace(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<package namespace>");
    r = consumeToken(b, "package");
    p = r; // pin = 1
    r = r && report_error_(b, perl_package(b, l + 1));
    r = p && report_error_(b, package_namespace_2(b, l + 1)) && r;
    r = p && package_namespace_content(b, l + 1) && r;
    exit_section_(b, l, m, PACKAGE_NAMESPACE, r, p, null);
    return r || p;
  }

  // perl_version ?
  private static boolean package_namespace_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_2")) return false;
    perl_version(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // block_parsed | PERL_SEMI <<parsePackageContents>>
  static boolean package_namespace_content(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_content")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = block_parsed(b, l + 1);
    if (!r) r = package_namespace_content_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_SEMI <<parsePackageContents>>
  private static boolean package_namespace_content_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_content_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SEMI);
    r = r && parsePackageContents(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // declaration PERL_SEMI ? | statement PERL_SEMI
  static boolean package_namespace_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_item")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_namespace_item_0(b, l + 1);
    if (!r) r = package_namespace_item_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // declaration PERL_SEMI ?
  private static boolean package_namespace_item_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_item_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = declaration(b, l + 1);
    r = r && package_namespace_item_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // PERL_SEMI ?
  private static boolean package_namespace_item_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_item_0_1")) return false;
    consumeToken(b, PERL_SEMI);
    return true;
  }

  // statement PERL_SEMI
  private static boolean package_namespace_item_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace_item_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = statement(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    exit_section_(b, m, null, r);
    return r;
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
  // perl_package perl_version expr ?
  //     | perl_package expr ?
  //     | perl_version
  static boolean package_use_arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = package_use_arguments_0(b, l + 1);
    if (!r) r = package_use_arguments_1(b, l + 1);
    if (!r) r = perl_version(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // perl_package perl_version expr ?
  private static boolean package_use_arguments_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_package(b, l + 1);
    r = r && perl_version(b, l + 1);
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

  // perl_package expr ?
  private static boolean package_use_arguments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_use_arguments_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = perl_package(b, l + 1);
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
  // <<parseBarewordPackage>>
  static boolean perl_package(PsiBuilder b, int l) {
    return parseBarewordPackage(b, l + 1);
  }

  /* ********************************************************** */
  // <<parseVersion>>
  static boolean perl_version(PsiBuilder b, int l) {
    return parseVersion(b, l + 1);
  }

  /* ********************************************************** */
  // !(PERL_RBRACE | <<eof>>)
  static boolean recover_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_block")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_, null);
    r = !recover_block_0(b, l + 1);
    exit_section_(b, l, m, null, r, false, null);
    return r;
  }

  // PERL_RBRACE | <<eof>>
  private static boolean recover_block_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_block_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_RBRACE);
    if (!r) r = eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(PERL_SEMI | PERL_RBRACE | <<eof>>)
  static boolean recover_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_, null);
    r = !recover_statement_0(b, l + 1);
    exit_section_(b, l, m, null, r, false, null);
    return r;
  }

  // PERL_SEMI | PERL_RBRACE | <<eof>>
  private static boolean recover_statement_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_statement_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SEMI);
    if (!r) r = consumeToken(b, PERL_RBRACE);
    if (!r) r = eof(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // PERL_NUMBER
  //     | string
  //     | string_list
  public static boolean scalar_primitive(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_primitive")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<scalar primitive>");
    r = consumeToken(b, PERL_NUMBER);
    if (!r) r = string(b, l + 1);
    if (!r) r = string_list(b, l + 1);
    exit_section_(b, l, m, SCALAR_PRIMITIVE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // use_statement
  //     | no_statement
  //     | expr
  static boolean statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = use_statement(b, l + 1);
    if (!r) r = no_statement(b, l + 1);
    if (!r) r = expr(b, l + 1, -1);
    exit_section_(b, l, m, null, r, false, recover_statement_parser_);
    return r;
  }

  /* ********************************************************** */
  // string_quoted | string_unquoted | <<parseStringContent>> | <<parseBarewordString>>
  static boolean string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = string_quoted(b, l + 1);
    if (!r) r = string_unquoted(b, l + 1);
    if (!r) r = parseStringContent(b, l + 1);
    if (!r) r = parseBarewordString(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'qw' PERL_QUOTE <<parseStringContent>> * PERL_QUOTE
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

  // <<parseStringContent>> *
  private static boolean string_list_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_list_2")) return false;
    int c = current_position_(b);
    while (true) {
      if (!parseStringContent(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "string_list_2", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // ('qq' | 'qx' | 'q') ? PERL_QUOTE <<parseStringContent>> PERL_QUOTE
  static boolean string_quoted(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_quoted")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = string_quoted_0(b, l + 1);
    r = r && consumeToken(b, PERL_QUOTE);
    r = r && parseStringContent(b, l + 1);
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
  // <<parseStringContent>>
  static boolean string_unquoted(PsiBuilder b, int l) {
    return parseStringContent(b, l + 1);
  }

  /* ********************************************************** */
  // 'NYI'
  static boolean sub_attributes(PsiBuilder b, int l) {
    return consumeToken(b, "NYI");
  }

  /* ********************************************************** */
  // sub_prototype sub_attributes ?
  //     | sub_attributes
  static boolean sub_declaration_parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_declaration_parameters")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_declaration_parameters_0(b, l + 1);
    if (!r) r = sub_attributes(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // sub_prototype sub_attributes ?
  private static boolean sub_declaration_parameters_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_declaration_parameters_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_prototype(b, l + 1);
    r = r && sub_declaration_parameters_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // sub_attributes ?
  private static boolean sub_declaration_parameters_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_declaration_parameters_0_1")) return false;
    sub_attributes(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'sub' PERL_BAREWORD sub_definition_or_declaration_parameters
  static boolean sub_definition_or_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_definition_or_declaration")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "sub");
    r = r && consumeToken(b, PERL_BAREWORD);
    r = r && sub_definition_or_declaration_parameters(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // sub_definition_parameters ? block_parsed PERL_SEMI ?
  //     | sub_declaration_parameters ? PERL_SEMI
  static boolean sub_definition_or_declaration_parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_definition_or_declaration_parameters")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_definition_or_declaration_parameters_0(b, l + 1);
    if (!r) r = sub_definition_or_declaration_parameters_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // sub_definition_parameters ? block_parsed PERL_SEMI ?
  private static boolean sub_definition_or_declaration_parameters_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_definition_or_declaration_parameters_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_definition_or_declaration_parameters_0_0(b, l + 1);
    r = r && block_parsed(b, l + 1);
    r = r && sub_definition_or_declaration_parameters_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // sub_definition_parameters ?
  private static boolean sub_definition_or_declaration_parameters_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_definition_or_declaration_parameters_0_0")) return false;
    sub_definition_parameters(b, l + 1);
    return true;
  }

  // PERL_SEMI ?
  private static boolean sub_definition_or_declaration_parameters_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_definition_or_declaration_parameters_0_2")) return false;
    consumeToken(b, PERL_SEMI);
    return true;
  }

  // sub_declaration_parameters ? PERL_SEMI
  private static boolean sub_definition_or_declaration_parameters_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_definition_or_declaration_parameters_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_definition_or_declaration_parameters_1_0(b, l + 1);
    r = r && consumeToken(b, PERL_SEMI);
    exit_section_(b, m, null, r);
    return r;
  }

  // sub_declaration_parameters ?
  private static boolean sub_definition_or_declaration_parameters_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_definition_or_declaration_parameters_1_0")) return false;
    sub_declaration_parameters(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // sub_attributes ? sub_signature
  //     | sub_declaration_parameters
  static boolean sub_definition_parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_definition_parameters")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_definition_parameters_0(b, l + 1);
    if (!r) r = sub_declaration_parameters(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // sub_attributes ? sub_signature
  private static boolean sub_definition_parameters_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_definition_parameters_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_definition_parameters_0_0(b, l + 1);
    r = r && sub_signature(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // sub_attributes ?
  private static boolean sub_definition_parameters_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_definition_parameters_0_0")) return false;
    sub_attributes(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // "(" sub_prototype_element * (";" sub_prototype_element *) ? ")"
  static boolean sub_prototype(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_prototype")) return false;
    if (!nextTokenIs(b, PERL_LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LPAREN);
    r = r && sub_prototype_1(b, l + 1);
    r = r && sub_prototype_2(b, l + 1);
    r = r && consumeToken(b, PERL_RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // sub_prototype_element *
  private static boolean sub_prototype_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_prototype_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!sub_prototype_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "sub_prototype_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // (";" sub_prototype_element *) ?
  private static boolean sub_prototype_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_prototype_2")) return false;
    sub_prototype_2_0(b, l + 1);
    return true;
  }

  // ";" sub_prototype_element *
  private static boolean sub_prototype_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_prototype_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SEMI);
    r = r && sub_prototype_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // sub_prototype_element *
  private static boolean sub_prototype_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_prototype_2_0_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!sub_prototype_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "sub_prototype_2_0_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // "$" | "@" | "+" | "*" | "&"
  static boolean sub_prototype_char(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_prototype_char")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SIGIL_SCALAR);
    if (!r) r = consumeToken(b, PERL_SIGIL_ARRAY);
    if (!r) r = consumeToken(b, "+");
    if (!r) r = consumeToken(b, "*");
    if (!r) r = consumeToken(b, "&");
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // "\\" ( "[" sub_prototype_char + "]" | sub_prototype_char )
  //         | sub_prototype_char
  static boolean sub_prototype_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_prototype_element")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_prototype_element_0(b, l + 1);
    if (!r) r = sub_prototype_char(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // "\\" ( "[" sub_prototype_char + "]" | sub_prototype_char )
  private static boolean sub_prototype_element_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_prototype_element_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "\\");
    r = r && sub_prototype_element_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // "[" sub_prototype_char + "]" | sub_prototype_char
  private static boolean sub_prototype_element_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_prototype_element_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_prototype_element_0_1_0(b, l + 1);
    if (!r) r = sub_prototype_char(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // "[" sub_prototype_char + "]"
  private static boolean sub_prototype_element_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_prototype_element_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_LBRACK);
    r = r && sub_prototype_element_0_1_0_1(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACK);
    exit_section_(b, m, null, r);
    return r;
  }

  // sub_prototype_char +
  private static boolean sub_prototype_element_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "sub_prototype_element_0_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = sub_prototype_char(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!sub_prototype_char(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "sub_prototype_element_0_1_0_1", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'NYI'
  static boolean sub_signature(PsiBuilder b, int l) {
    return consumeToken(b, "NYI");
  }

  /* ********************************************************** */
  // PERL_ARRAY
  //     | PERL_HASH
  //     | PERL_GLOB
  //     | PERL_SCALAR
  //     | scalar_primitive
  static boolean term(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_ARRAY);
    if (!r) r = consumeToken(b, PERL_HASH);
    if (!r) r = consumeToken(b, PERL_GLOB);
    if (!r) r = consumeToken(b, PERL_SCALAR);
    if (!r) r = scalar_primitive(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'use' <<captureStrings>> package_use_arguments <<processUseStatement>>
  public static boolean use_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "use_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<use statement>");
    r = consumeToken(b, "use");
    p = r; // pin = 1
    r = r && report_error_(b, captureStrings(b, l + 1));
    r = p && report_error_(b, package_use_arguments(b, l + 1)) && r;
    r = p && processUseStatement(b, l + 1) && r;
    exit_section_(b, l, m, USE_STATEMENT, r, p, null);
    return r || p;
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

  // call_rightward
  public static boolean op_21_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_21_expr")) return false;
    if (!nextTokenIsFast(b, CALL_RIGHTWARD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, CALL_RIGHTWARD);
    exit_section_(b, m, OP_21_EXPR, r);
    return r;
  }

  // ',' | '=>'
  private static boolean op_20_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_20_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, PERL_COMMA);
    if (!r) r = consumeTokenSmart(b, PERL_ARROW_COMMA);
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

  // '->' nested_element
  private static boolean op_2_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_2_expr_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "->");
    r = r && consumeToken(b, NESTED_ELEMENT);
    exit_section_(b, m, null, r);
    return r;
  }

  // term
  public static boolean op_1_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "op_1_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<op 1 expr>");
    r = term(b, l + 1);
    exit_section_(b, l, m, OP_1_EXPR, r, false, null);
    return r;
  }

  final static Parser recover_block_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_block(b, l + 1);
    }
  };
  final static Parser recover_statement_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return recover_statement(b, l + 1);
    }
  };
}
