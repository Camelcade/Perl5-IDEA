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
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    if (t == BLOCK) {
      r = block(b, 0);
    }
    else if (t == LABEL) {
      r = label(b, 0);
    }
    else if (t == NO_STATEMENT) {
      r = no_statement(b, 0);
    }
    else if (t == PACKAGE_NAMESPACE) {
      r = package_namespace(b, 0);
    }
    else if (t == PERL_VERSION) {
      r = perl_version(b, 0);
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

  /* ********************************************************** */
  // label ?  "{" file_items "}"
  public static boolean block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block")) return false;
    if (!nextTokenIs(b, "<block>", PERL_LBRACE, PERL_BAREWORD)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<block>");
    r = block_0(b, l + 1);
    r = r && consumeToken(b, PERL_LBRACE);
    r = r && file_items(b, l + 1);
    r = r && consumeToken(b, PERL_RBRACE);
    exit_section_(b, l, m, BLOCK, r, false, null);
    return r;
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
    int c = current_position_(b);
    while (true) {
      if (!file_item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "file_items", c)) break;
      c = current_position_(b);
    }
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
  // 'no' package_use_arguments <<processNoStatement>>
  public static boolean no_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "no_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<no statement>");
    r = consumeToken(b, "no");
    p = r; // pin = 1
    r = r && report_error_(b, package_use_arguments(b, l + 1));
    r = p && processNoStatement(b, l + 1) && r;
    exit_section_(b, l, m, NO_STATEMENT, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'package' perl_package perl_version ? package_namespace_content
  public static boolean package_namespace(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "package_namespace")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<package namespace>");
    r = consumeToken(b, "package");
    r = r && perl_package(b, l + 1);
    r = r && package_namespace_2(b, l + 1);
    r = r && package_namespace_content(b, l + 1);
    exit_section_(b, l, m, PACKAGE_NAMESPACE, r, false, null);
    return r;
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
    Marker m = enter_section_(b, l, _NONE_, null);
    r = package_use_arguments_0(b, l + 1);
    if (!r) r = package_use_arguments_1(b, l + 1);
    if (!r) r = perl_version(b, l + 1);
    exit_section_(b, l, m, null, r, false, statement_recover_parser_);
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
    consumeToken(b, EXPR);
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
    consumeToken(b, EXPR);
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
  // PERL_NUMBER_VERSION | PERL_NUMBER
  public static boolean perl_version(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "perl_version")) return false;
    if (!nextTokenIs(b, "<perl version>", PERL_NUMBER, PERL_NUMBER_VERSION)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<perl version>");
    r = consumeToken(b, PERL_NUMBER_VERSION);
    if (!r) r = consumeToken(b, PERL_NUMBER);
    exit_section_(b, l, m, PERL_VERSION, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // PERL_NUMBER
  //     | string
  public static boolean scalar_primitive(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_primitive")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<scalar primitive>");
    r = consumeToken(b, PERL_NUMBER);
    if (!r) r = string(b, l + 1);
    exit_section_(b, l, m, SCALAR_PRIMITIVE, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // use_statement
  //     | no_statement
  //     | scalar_primitive
  static boolean statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = use_statement(b, l + 1);
    if (!r) r = no_statement(b, l + 1);
    if (!r) r = scalar_primitive(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // !(PERL_SEMI | PERL_RBRACE)
  static boolean statement_recover(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_recover")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_, null);
    r = !statement_recover_0(b, l + 1);
    exit_section_(b, l, m, null, r, false, null);
    return r;
  }

  // PERL_SEMI | PERL_RBRACE
  private static boolean statement_recover_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement_recover_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PERL_SEMI);
    if (!r) r = consumeToken(b, PERL_RBRACE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // string_quoted | string_unquoted | PERL_STRING_CONTENT | <<parseBarewordString>>
  static boolean string(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = string_quoted(b, l + 1);
    if (!r) r = string_unquoted(b, l + 1);
    if (!r) r = consumeToken(b, PERL_STRING_CONTENT);
    if (!r) r = parseBarewordString(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ('qq' | 'qx' | 'q') ? PERL_QUOTE PERL_STRING_CONTENT PERL_QUOTE
  static boolean string_quoted(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_quoted")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = string_quoted_0(b, l + 1);
    r = r && consumeTokens(b, 0, PERL_QUOTE, PERL_STRING_CONTENT, PERL_QUOTE);
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
  // PERL_STRING_CONTENT
  static boolean string_unquoted(PsiBuilder b, int l) {
    return consumeToken(b, PERL_STRING_CONTENT);
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
  // 'use' package_use_arguments <<processUseStatement>>
  public static boolean use_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "use_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<use statement>");
    r = consumeToken(b, "use");
    p = r; // pin = 1
    r = r && report_error_(b, package_use_arguments(b, l + 1));
    r = p && processUseStatement(b, l + 1) && r;
    exit_section_(b, l, m, USE_STATEMENT, r, p, null);
    return r || p;
  }

  final static Parser statement_recover_parser_ = new Parser() {
    public boolean parse(PsiBuilder b, int l) {
      return statement_recover(b, l + 1);
    }
  };
}
