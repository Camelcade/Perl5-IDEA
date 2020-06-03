// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.lexer;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.perl5.lang.pod.elementTypes.PodElementTypeFactory;
import com.perl5.lang.pod.psi.impl.*;

public interface PodElementTypesGenerated {

  IElementType BEGIN_SECTION = PodElementTypeFactory.getElementType("BEGIN_SECTION");
  IElementType BEGIN_SECTION_CONTENT = PodElementTypeFactory.getElementType("BEGIN_SECTION_CONTENT");
  IElementType CUT_SECTION = PodElementTypeFactory.getElementType("CUT_SECTION");
  IElementType ENCODING_SECTION = PodElementTypeFactory.getElementType("ENCODING_SECTION");
  IElementType FORMATTING_SECTION_CONTENT = PodElementTypeFactory.getElementType("FORMATTING_SECTION_CONTENT");
  IElementType FOR_SECTION = PodElementTypeFactory.getElementType("FOR_SECTION");
  IElementType FOR_SECTION_CONTENT = PodElementTypeFactory.getElementType("FOR_SECTION_CONTENT");
  IElementType HEAD_1_SECTION = PodElementTypeFactory.getElementType("HEAD_1_SECTION");
  IElementType HEAD_1_SECTION_CONTENT = PodElementTypeFactory.getElementType("HEAD_1_SECTION_CONTENT");
  IElementType HEAD_2_SECTION = PodElementTypeFactory.getElementType("HEAD_2_SECTION");
  IElementType HEAD_2_SECTION_CONTENT = PodElementTypeFactory.getElementType("HEAD_2_SECTION_CONTENT");
  IElementType HEAD_3_SECTION = PodElementTypeFactory.getElementType("HEAD_3_SECTION");
  IElementType HEAD_3_SECTION_CONTENT = PodElementTypeFactory.getElementType("HEAD_3_SECTION_CONTENT");
  IElementType HEAD_4_SECTION = PodElementTypeFactory.getElementType("HEAD_4_SECTION");
  IElementType HEAD_4_SECTION_CONTENT = PodElementTypeFactory.getElementType("HEAD_4_SECTION_CONTENT");
  IElementType ITEM_SECTION = PodElementTypeFactory.getElementType("ITEM_SECTION");
  IElementType ITEM_SECTION_CONTENT = PodElementTypeFactory.getElementType("ITEM_SECTION_CONTENT");
  IElementType ITEM_SECTION_TITLE = PodElementTypeFactory.getElementType("ITEM_SECTION_TITLE");
  IElementType LINK_NAME = PodElementTypeFactory.getElementType("LINK_NAME");
  IElementType LINK_SECTION = PodElementTypeFactory.getElementType("LINK_SECTION");
  IElementType LINK_TEXT = PodElementTypeFactory.getElementType("LINK_TEXT");
  IElementType LINK_URL = PodElementTypeFactory.getElementType("LINK_URL");
  IElementType OVER_SECTION = PodElementTypeFactory.getElementType("OVER_SECTION");
  IElementType OVER_SECTION_CONTENT = PodElementTypeFactory.getElementType("OVER_SECTION_CONTENT");
  IElementType POD_FORMAT_BOLD = PodElementTypeFactory.getElementType("POD_FORMAT_BOLD");
  IElementType POD_FORMAT_CODE = PodElementTypeFactory.getElementType("POD_FORMAT_CODE");
  IElementType POD_FORMAT_ESCAPE = PodElementTypeFactory.getElementType("POD_FORMAT_ESCAPE");
  IElementType POD_FORMAT_FILE = PodElementTypeFactory.getElementType("POD_FORMAT_FILE");
  IElementType POD_FORMAT_INDEX = PodElementTypeFactory.getElementType("POD_FORMAT_INDEX");
  IElementType POD_FORMAT_ITALIC = PodElementTypeFactory.getElementType("POD_FORMAT_ITALIC");
  IElementType POD_FORMAT_LINK = PodElementTypeFactory.getElementType("POD_FORMAT_LINK");
  IElementType POD_FORMAT_NBSP = PodElementTypeFactory.getElementType("POD_FORMAT_NBSP");
  IElementType POD_FORMAT_NULL = PodElementTypeFactory.getElementType("POD_FORMAT_NULL");
  IElementType POD_PARAGRAPH = PodElementTypeFactory.getElementType("POD_PARAGRAPH");
  IElementType POD_SECTION = PodElementTypeFactory.getElementType("POD_SECTION");
  IElementType POD_SECTION_FORMAT = PodElementTypeFactory.getElementType("POD_SECTION_FORMAT");
  IElementType POD_VERBATIM_PARAGRAPH = PodElementTypeFactory.getElementType("POD_VERBATIM_PARAGRAPH");
  IElementType SECTION_TITLE = PodElementTypeFactory.getElementType("SECTION_TITLE");
  IElementType UNKNOWN_SECTION = PodElementTypeFactory.getElementType("UNKNOWN_SECTION");

  IElementType POD_ANGLE_LEFT = PodElementTypeFactory.getTokenType("<");
  IElementType POD_ANGLE_RIGHT = PodElementTypeFactory.getTokenType(">");
  IElementType POD_ASTERISK = PodElementTypeFactory.getTokenType("*");
  IElementType POD_B = PodElementTypeFactory.getTokenType("B");
  IElementType POD_BACK = PodElementTypeFactory.getTokenType("=back");
  IElementType POD_BACKREF = PodElementTypeFactory.getTokenType("\\");
  IElementType POD_BEGIN = PodElementTypeFactory.getTokenType("=begin");
  IElementType POD_BRACE_LEFT = PodElementTypeFactory.getTokenType("{");
  IElementType POD_BRACE_RIGHT = PodElementTypeFactory.getTokenType("}");
  IElementType POD_BRACKET_LEFT = PodElementTypeFactory.getTokenType("[");
  IElementType POD_BRACKET_RIGHT = PodElementTypeFactory.getTokenType("]");
  IElementType POD_C = PodElementTypeFactory.getTokenType("C");
  IElementType POD_CODE = PodElementTypeFactory.getTokenType("code");
  IElementType POD_COLON = PodElementTypeFactory.getTokenType(":");
  IElementType POD_CUT = PodElementTypeFactory.getTokenType("=cut");
  IElementType POD_DIV = PodElementTypeFactory.getTokenType("/");
  IElementType POD_E = PodElementTypeFactory.getTokenType("E");
  IElementType POD_ENCODING = PodElementTypeFactory.getTokenType("=encoding");
  IElementType POD_ENCODING_NAME = PodElementTypeFactory.getTokenType("encoding_name");
  IElementType POD_END = PodElementTypeFactory.getTokenType("=end");
  IElementType POD_F = PodElementTypeFactory.getTokenType("F");
  IElementType POD_FOR = PodElementTypeFactory.getTokenType("=for");
  IElementType POD_FORMATTED_BLOCK = PodElementTypeFactory.getTokenType("formatted_block");
  IElementType POD_FORMAT_NAME = PodElementTypeFactory.getTokenType("format_name");
  IElementType POD_HEAD1 = PodElementTypeFactory.getTokenType("=head1");
  IElementType POD_HEAD2 = PodElementTypeFactory.getTokenType("=head2");
  IElementType POD_HEAD3 = PodElementTypeFactory.getTokenType("=head3");
  IElementType POD_HEAD4 = PodElementTypeFactory.getTokenType("=head4");
  IElementType POD_I = PodElementTypeFactory.getTokenType("I");
  IElementType POD_IDENTIFIER = PodElementTypeFactory.getTokenType("identifier");
  IElementType POD_INDENT_LEVEL = PodElementTypeFactory.getTokenType("indent_level");
  IElementType POD_ITEM = PodElementTypeFactory.getTokenType("=item");
  IElementType POD_L = PodElementTypeFactory.getTokenType("L");
  IElementType POD_NEWLINE = PodElementTypeFactory.getTokenType("NL");
  IElementType POD_NUMBER = PodElementTypeFactory.getTokenType("number");
  IElementType POD_OVER = PodElementTypeFactory.getTokenType("=over");
  IElementType POD_PACKAGE = PodElementTypeFactory.getTokenType("package");
  IElementType POD_PAREN_LEFT = PodElementTypeFactory.getTokenType("(");
  IElementType POD_PAREN_RIGHT = PodElementTypeFactory.getTokenType(")");
  IElementType POD_PIPE = PodElementTypeFactory.getTokenType("|");
  IElementType POD_POD = PodElementTypeFactory.getTokenType("=pod");
  IElementType POD_QUOTE_DOUBLE = PodElementTypeFactory.getTokenType("\"");
  IElementType POD_QUOTE_SINGLE = PodElementTypeFactory.getTokenType("'");
  IElementType POD_QUOTE_TICK = PodElementTypeFactory.getTokenType("`");
  IElementType POD_S = PodElementTypeFactory.getTokenType("S");
  IElementType POD_SYMBOL = PodElementTypeFactory.getTokenType("symbol");
  IElementType POD_UNKNOWN = PodElementTypeFactory.getTokenType("=unknown");
  IElementType POD_X = PodElementTypeFactory.getTokenType("X");
  IElementType POD_Z = PodElementTypeFactory.getTokenType("Z");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == BEGIN_SECTION) {
        return new PsiBeginSectionImpl(node);
      }
      else if (type == BEGIN_SECTION_CONTENT) {
        return new PsiBeginSectionContentImpl(node);
      }
      else if (type == CUT_SECTION) {
        return new PsiCutSectionImpl(node);
      }
      else if (type == ENCODING_SECTION) {
        return new PsiEncodingSectionImpl(node);
      }
      else if (type == FORMATTING_SECTION_CONTENT) {
        return new PsiFormattingSectionContentImpl(node);
      }
      else if (type == FOR_SECTION) {
        return new PsiForSectionImpl(node);
      }
      else if (type == FOR_SECTION_CONTENT) {
        return new PsiForSectionContentImpl(node);
      }
      else if (type == HEAD_1_SECTION) {
        return new PsiHead1SectionImpl(node);
      }
      else if (type == HEAD_1_SECTION_CONTENT) {
        return new PsiHead1SectionContentImpl(node);
      }
      else if (type == HEAD_2_SECTION) {
        return new PsiHead2SectionImpl(node);
      }
      else if (type == HEAD_2_SECTION_CONTENT) {
        return new PsiHead2SectionContentImpl(node);
      }
      else if (type == HEAD_3_SECTION) {
        return new PsiHead3SectionImpl(node);
      }
      else if (type == HEAD_3_SECTION_CONTENT) {
        return new PsiHead3SectionContentImpl(node);
      }
      else if (type == HEAD_4_SECTION) {
        return new PsiHead4SectionImpl(node);
      }
      else if (type == HEAD_4_SECTION_CONTENT) {
        return new PsiHead4SectionContentImpl(node);
      }
      else if (type == ITEM_SECTION) {
        return new PsiItemSectionImpl(node);
      }
      else if (type == ITEM_SECTION_CONTENT) {
        return new PsiItemSectionContentImpl(node);
      }
      else if (type == ITEM_SECTION_TITLE) {
        return new PsiItemSectionTitleImpl(node);
      }
      else if (type == LINK_NAME) {
        return new PsiLinkNameImpl(node);
      }
      else if (type == LINK_SECTION) {
        return new PsiLinkSectionImpl(node);
      }
      else if (type == LINK_TEXT) {
        return new PsiLinkTextImpl(node);
      }
      else if (type == LINK_URL) {
        return new PsiLinkUrlImpl(node);
      }
      else if (type == OVER_SECTION) {
        return new PsiOverSectionImpl(node);
      }
      else if (type == OVER_SECTION_CONTENT) {
        return new PsiOverSectionContentImpl(node);
      }
      else if (type == POD_FORMAT_BOLD) {
        return new PsiPodFormatBoldImpl(node);
      }
      else if (type == POD_FORMAT_CODE) {
        return new PsiPodFormatCodeImpl(node);
      }
      else if (type == POD_FORMAT_ESCAPE) {
        return new PsiPodFormatEscapeImpl(node);
      }
      else if (type == POD_FORMAT_FILE) {
        return new PsiPodFormatFileImpl(node);
      }
      else if (type == POD_FORMAT_INDEX) {
        return new PsiPodFormatIndexImpl(node);
      }
      else if (type == POD_FORMAT_ITALIC) {
        return new PsiPodFormatItalicImpl(node);
      }
      else if (type == POD_FORMAT_LINK) {
        return new PsiPodFormatLinkImpl(node);
      }
      else if (type == POD_FORMAT_NBSP) {
        return new PsiPodFormatNbspImpl(node);
      }
      else if (type == POD_FORMAT_NULL) {
        return new PsiPodFormatNullImpl(node);
      }
      else if (type == POD_PARAGRAPH) {
        return new PsiPodParagraphImpl(node);
      }
      else if (type == POD_SECTION) {
        return new PsiPodSectionImpl(node);
      }
      else if (type == POD_SECTION_FORMAT) {
        return new PsiPodSectionFormatImpl(node);
      }
      else if (type == POD_VERBATIM_PARAGRAPH) {
        return new PsiPodVerbatimParagraphImpl(node);
      }
      else if (type == SECTION_TITLE) {
        return new PsiSectionTitleImpl(node);
      }
      else if (type == UNKNOWN_SECTION) {
        return new PsiUnknownSectionImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
