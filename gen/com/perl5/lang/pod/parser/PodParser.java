// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.perl5.lang.pod.lexer.PodElementTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class PodParser implements PsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b, 0);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return pod_file(b, l + 1);
  }

  /* ********************************************************** */
  // POD_TAG (paragraph | POD_NEWLINE)
  static boolean control_text(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "control_text")) return false;
    if (!nextTokenIs(b, POD_TAG)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, POD_TAG);
    r = r && control_text_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // paragraph | POD_NEWLINE
  private static boolean control_text_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "control_text_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = paragraph(b, l + 1);
    if (!r) r = consumeToken(b, POD_NEWLINE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // (POD_TEXT POD_NEWLINE) +
  static boolean paragraph(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paragraph")) return false;
    if (!nextTokenIs(b, POD_TEXT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = paragraph_0(b, l + 1);
    int c = current_position_(b);
    while (r) {
      if (!paragraph_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "paragraph", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // POD_TEXT POD_NEWLINE
  private static boolean paragraph_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paragraph_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, POD_TEXT, POD_NEWLINE);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // pod_paragraph*
  static boolean pod_file(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pod_file")) return false;
    int c = current_position_(b);
    while (true) {
      if (!pod_paragraph(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "pod_file", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // (control_text | POD_CODE | paragraph) POD_NEWLINE +
  static boolean pod_paragraph(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pod_paragraph")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = pod_paragraph_0(b, l + 1);
    r = r && pod_paragraph_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // control_text | POD_CODE | paragraph
  private static boolean pod_paragraph_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pod_paragraph_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = control_text(b, l + 1);
    if (!r) r = consumeToken(b, POD_CODE);
    if (!r) r = paragraph(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // POD_NEWLINE +
  private static boolean pod_paragraph_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pod_paragraph_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, POD_NEWLINE);
    int c = current_position_(b);
    while (r) {
      if (!consumeToken(b, POD_NEWLINE)) break;
      if (!empty_element_parsed_guard_(b, "pod_paragraph_1", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, m, null, r);
    return r;
  }

}
