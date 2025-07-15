// This is a generated file. Not intended for manual editing.
package com.perl5.lang.pod.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.perl5.lang.pod.parser.PodElementTypesGenerated.*;
import static com.perl5.lang.pod.parser.PodParserUtil.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class PodParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    parseLight(root_, builder_);
    return builder_.getTreeBuilt();
  }

  public void parseLight(IElementType root_, PsiBuilder builder_) {
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this, null);
    Marker marker_ = enter_section_(builder_, 0, _COLLAPSE_, null);
    result_ = parse_root_(root_, builder_);
    exit_section_(builder_, 0, marker_, root_, result_, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType root_, PsiBuilder builder_) {
    return parse_root_(root_, builder_, 0);
  }

  static boolean parse_root_(IElementType root_, PsiBuilder builder_, int level_) {
    return root(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // pod_section
  // 	| over_section
  // 	| pod_format_indexes
  // 	| for_section
  // 	| encoding_section
  // 	| unknown_section
  // 	| cut_section
  // 	| pod_paragraph
  // 	| pod_verbatim_paragraph
  // 	| 'NL'+
  static boolean any_level_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "any_level_item")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = pod_section(builder_, level_ + 1);
    if (!result_) result_ = over_section(builder_, level_ + 1);
    if (!result_) result_ = pod_format_indexes(builder_, level_ + 1);
    if (!result_) result_ = for_section(builder_, level_ + 1);
    if (!result_) result_ = encoding_section(builder_, level_ + 1);
    if (!result_) result_ = unknown_section(builder_, level_ + 1);
    if (!result_) result_ = cut_section(builder_, level_ + 1);
    if (!result_) result_ = pod_paragraph(builder_, level_ + 1);
    if (!result_) result_ = pod_verbatim_paragraph(builder_, level_ + 1);
    if (!result_) result_ = any_level_item_9(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // 'NL'+
  private static boolean any_level_item_9(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "any_level_item_9")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, POD_NEWLINE);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, POD_NEWLINE)) break;
      if (!empty_element_parsed_guard_(builder_, "any_level_item_9", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // '=cut' [parse_section_title] 'NL'*
  public static boolean cut_section(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "cut_section")) return false;
    if (!nextTokenIs(builder_, POD_CUT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, CUT_SECTION, null);
    result_ = consumeToken(builder_, POD_CUT);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, cut_section_1(builder_, level_ + 1));
    result_ = pinned_ && cut_section_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [parse_section_title]
  private static boolean cut_section_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "cut_section_1")) return false;
    parse_section_title(builder_, level_ + 1);
    return true;
  }

  // 'NL'*
  private static boolean cut_section_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "cut_section_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, POD_NEWLINE)) break;
      if (!empty_element_parsed_guard_(builder_, "cut_section_2", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // {&formatting_section_allowed_tokens pod_term}+
  static boolean default_formatting_section_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "default_formatting_section_item")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = default_formatting_section_item_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!default_formatting_section_item_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "default_formatting_section_item", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // &formatting_section_allowed_tokens pod_term
  private static boolean default_formatting_section_item_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "default_formatting_section_item_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = default_formatting_section_item_0_0(builder_, level_ + 1);
    result_ = result_ && pod_term(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // &formatting_section_allowed_tokens
  private static boolean default_formatting_section_item_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "default_formatting_section_item_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = formatting_section_allowed_tokens(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '=encoding' <<collapseNonSpaceTo 'POD_ENCODING_NAME '>> 'NL'+
  public static boolean encoding_section(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "encoding_section")) return false;
    if (!nextTokenIs(builder_, POD_ENCODING)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ENCODING_SECTION, null);
    result_ = consumeToken(builder_, POD_ENCODING);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, collapseNonSpaceTo(builder_, level_ + 1, POD_ENCODING_NAME ));
    result_ = pinned_ && encoding_section_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'NL'+
  private static boolean encoding_section_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "encoding_section_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, POD_NEWLINE);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, POD_NEWLINE)) break;
      if (!empty_element_parsed_guard_(builder_, "encoding_section_2", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // {'=for'|'=begin'|'=end'} parse_for_section_content 'NL'*
  public static boolean for_section(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_section")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FOR_SECTION, "<for section>");
    result_ = for_section_0(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, parse_for_section_content(builder_, level_ + 1));
    result_ = pinned_ && for_section_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // '=for'|'=begin'|'=end'
  private static boolean for_section_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_section_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, POD_FOR);
    if (!result_) result_ = consumeToken(builder_, POD_BEGIN);
    if (!result_) result_ = consumeToken(builder_, POD_END);
    return result_;
  }

  // 'NL'*
  private static boolean for_section_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_section_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, POD_NEWLINE)) break;
      if (!empty_element_parsed_guard_(builder_, "for_section_2", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // pod_term *
  public static boolean for_section_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "for_section_content")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, FOR_SECTION_CONTENT, "<for section content>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!pod_term(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "for_section_content", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // !('NL'|'>')
  static boolean formatting_section_allowed_tokens(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "formatting_section_allowed_tokens")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !formatting_section_allowed_tokens_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // 'NL'|'>'
  private static boolean formatting_section_allowed_tokens_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "formatting_section_allowed_tokens_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, POD_NEWLINE);
    if (!result_) result_ = consumeToken(builder_, POD_ANGLE_RIGHT);
    return result_;
  }

  /* ********************************************************** */
  // <<x1>>
  public static boolean formatting_section_content(PsiBuilder builder_, int level_, Parser x1) {
    if (!recursion_guard_(builder_, level_, "formatting_section_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = x1.parse(builder_, level_);
    exit_section_(builder_, marker_, FORMATTING_SECTION_CONTENT, result_);
    return result_;
  }

  /* ********************************************************** */
  // '=head1' [ parse_section_title [head1_section_content]]
  public static boolean head1_section(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head1_section")) return false;
    if (!nextTokenIs(builder_, POD_HEAD1)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HEAD_1_SECTION, null);
    result_ = consumeToken(builder_, POD_HEAD1);
    pinned_ = result_; // pin = 1
    result_ = result_ && head1_section_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [ parse_section_title [head1_section_content]]
  private static boolean head1_section_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head1_section_1")) return false;
    head1_section_1_0(builder_, level_ + 1);
    return true;
  }

  // parse_section_title [head1_section_content]
  private static boolean head1_section_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head1_section_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_section_title(builder_, level_ + 1);
    result_ = result_ && head1_section_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [head1_section_content]
  private static boolean head1_section_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head1_section_1_0_1")) return false;
    head1_section_content(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // head1_section_item *
  public static boolean head1_section_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head1_section_content")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HEAD_1_SECTION_CONTENT, "<head 1 section content>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!head1_section_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "head1_section_content", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // head2_section | head3_section | head4_section | any_level_item
  static boolean head1_section_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head1_section_item")) return false;
    boolean result_;
    result_ = head2_section(builder_, level_ + 1);
    if (!result_) result_ = head3_section(builder_, level_ + 1);
    if (!result_) result_ = head4_section(builder_, level_ + 1);
    if (!result_) result_ = any_level_item(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '=head2' [parse_section_title [head2_section_content]]
  public static boolean head2_section(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head2_section")) return false;
    if (!nextTokenIs(builder_, POD_HEAD2)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HEAD_2_SECTION, null);
    result_ = consumeToken(builder_, POD_HEAD2);
    pinned_ = result_; // pin = 1
    result_ = result_ && head2_section_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [parse_section_title [head2_section_content]]
  private static boolean head2_section_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head2_section_1")) return false;
    head2_section_1_0(builder_, level_ + 1);
    return true;
  }

  // parse_section_title [head2_section_content]
  private static boolean head2_section_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head2_section_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_section_title(builder_, level_ + 1);
    result_ = result_ && head2_section_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [head2_section_content]
  private static boolean head2_section_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head2_section_1_0_1")) return false;
    head2_section_content(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // head2_section_item *
  public static boolean head2_section_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head2_section_content")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HEAD_2_SECTION_CONTENT, "<head 2 section content>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!head2_section_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "head2_section_content", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // head3_section | head4_section | any_level_item
  static boolean head2_section_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head2_section_item")) return false;
    boolean result_;
    result_ = head3_section(builder_, level_ + 1);
    if (!result_) result_ = head4_section(builder_, level_ + 1);
    if (!result_) result_ = any_level_item(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '=head3' [parse_section_title [head3_section_content]]
  public static boolean head3_section(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head3_section")) return false;
    if (!nextTokenIs(builder_, POD_HEAD3)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HEAD_3_SECTION, null);
    result_ = consumeToken(builder_, POD_HEAD3);
    pinned_ = result_; // pin = 1
    result_ = result_ && head3_section_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [parse_section_title [head3_section_content]]
  private static boolean head3_section_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head3_section_1")) return false;
    head3_section_1_0(builder_, level_ + 1);
    return true;
  }

  // parse_section_title [head3_section_content]
  private static boolean head3_section_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head3_section_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_section_title(builder_, level_ + 1);
    result_ = result_ && head3_section_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [head3_section_content]
  private static boolean head3_section_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head3_section_1_0_1")) return false;
    head3_section_content(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // head3_section_item *
  public static boolean head3_section_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head3_section_content")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HEAD_3_SECTION_CONTENT, "<head 3 section content>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!head3_section_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "head3_section_content", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // head4_section | any_level_item
  static boolean head3_section_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head3_section_item")) return false;
    boolean result_;
    result_ = head4_section(builder_, level_ + 1);
    if (!result_) result_ = any_level_item(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '=head4' [parse_section_title [head4_section_content]]
  public static boolean head4_section(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head4_section")) return false;
    if (!nextTokenIs(builder_, POD_HEAD4)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HEAD_4_SECTION, null);
    result_ = consumeToken(builder_, POD_HEAD4);
    pinned_ = result_; // pin = 1
    result_ = result_ && head4_section_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [parse_section_title [head4_section_content]]
  private static boolean head4_section_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head4_section_1")) return false;
    head4_section_1_0(builder_, level_ + 1);
    return true;
  }

  // parse_section_title [head4_section_content]
  private static boolean head4_section_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head4_section_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_section_title(builder_, level_ + 1);
    result_ = result_ && head4_section_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [head4_section_content]
  private static boolean head4_section_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head4_section_1_0_1")) return false;
    head4_section_content(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // head4_section_item *
  public static boolean head4_section_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "head4_section_content")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, HEAD_4_SECTION_CONTENT, "<head 4 section content>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!head4_section_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "head4_section_content", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // any_level_item
  static boolean head4_section_item(PsiBuilder builder_, int level_) {
    return any_level_item(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // '=item' {
  //         parse_item_section_title [item_section_content] |
  //         'NL' + [item_section_content]
  //         }
  public static boolean item_section(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "item_section")) return false;
    if (!nextTokenIs(builder_, POD_ITEM)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ITEM_SECTION, null);
    result_ = consumeToken(builder_, POD_ITEM);
    pinned_ = result_; // pin = 1
    result_ = result_ && item_section_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // parse_item_section_title [item_section_content] |
  //         'NL' + [item_section_content]
  private static boolean item_section_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "item_section_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = item_section_1_0(builder_, level_ + 1);
    if (!result_) result_ = item_section_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // parse_item_section_title [item_section_content]
  private static boolean item_section_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "item_section_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_item_section_title(builder_, level_ + 1);
    result_ = result_ && item_section_1_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [item_section_content]
  private static boolean item_section_1_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "item_section_1_0_1")) return false;
    item_section_content(builder_, level_ + 1);
    return true;
  }

  // 'NL' + [item_section_content]
  private static boolean item_section_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "item_section_1_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = item_section_1_1_0(builder_, level_ + 1);
    result_ = result_ && item_section_1_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // 'NL' +
  private static boolean item_section_1_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "item_section_1_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, POD_NEWLINE);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, POD_NEWLINE)) break;
      if (!empty_element_parsed_guard_(builder_, "item_section_1_1_0", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [item_section_content]
  private static boolean item_section_1_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "item_section_1_1_1")) return false;
    item_section_content(builder_, level_ + 1);
    return true;
  }

  /* ********************************************************** */
  // any_level_item +
  public static boolean item_section_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "item_section_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ITEM_SECTION_CONTENT, "<item section content>");
    result_ = any_level_item(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!any_level_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "item_section_content", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // pod_term+
  public static boolean item_section_title(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "item_section_title")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, ITEM_SECTION_TITLE, "<item section title>");
    result_ = pod_term(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!pod_term(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "item_section_title", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // {
  //      link_url |
  //      link_name [parse_link_section] |
  //      parse_link_section |
  //      parse_quoted_section |
  //      parse_unquoted_section
  //    } & '>'
  static boolean link_after_title(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_after_title")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = link_after_title_0(builder_, level_ + 1);
    result_ = result_ && link_after_title_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // link_url |
  //      link_name [parse_link_section] |
  //      parse_link_section |
  //      parse_quoted_section |
  //      parse_unquoted_section
  private static boolean link_after_title_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_after_title_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = link_url(builder_, level_ + 1);
    if (!result_) result_ = link_after_title_0_1(builder_, level_ + 1);
    if (!result_) result_ = parse_link_section(builder_, level_ + 1);
    if (!result_) result_ = parse_quoted_section(builder_, level_ + 1);
    if (!result_) result_ = parse_unquoted_section(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // link_name [parse_link_section]
  private static boolean link_after_title_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_after_title_0_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = link_name(builder_, level_ + 1);
    result_ = result_ && link_after_title_0_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // [parse_link_section]
  private static boolean link_after_title_0_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_after_title_0_1_1")) return false;
    parse_link_section(builder_, level_ + 1);
    return true;
  }

  // & '>'
  private static boolean link_after_title_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_after_title_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = consumeToken(builder_, POD_ANGLE_RIGHT);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // link_with_title | link_after_title
  static boolean link_formatting_section(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_formatting_section")) return false;
    boolean result_;
    result_ = link_with_title(builder_, level_ + 1);
    if (!result_) result_ = link_after_title(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // {&link_name_negation pod_token}+ &('/'|'>')
  public static boolean link_name(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_name")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LINK_NAME, "<link name>");
    result_ = link_name_0(builder_, level_ + 1);
    result_ = result_ && link_name_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // {&link_name_negation pod_token}+
  private static boolean link_name_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_name_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = link_name_0_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!link_name_0_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "link_name_0", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // &link_name_negation pod_token
  private static boolean link_name_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_name_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = link_name_0_0_0(builder_, level_ + 1);
    result_ = result_ && pod_token(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // &link_name_negation
  private static boolean link_name_0_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_name_0_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = link_name_negation(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // &('/'|'>')
  private static boolean link_name_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_name_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = link_name_1_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // '/'|'>'
  private static boolean link_name_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_name_1_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, POD_DIV);
    if (!result_) result_ = consumeToken(builder_, POD_ANGLE_RIGHT);
    return result_;
  }

  /* ********************************************************** */
  // !('"'|'>'|'NL'|'/')
  static boolean link_name_negation(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_name_negation")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !link_name_negation_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // '"'|'>'|'NL'|'/'
  private static boolean link_name_negation_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_name_negation_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, POD_QUOTE_DOUBLE);
    if (!result_) result_ = consumeToken(builder_, POD_ANGLE_RIGHT);
    if (!result_) result_ = consumeToken(builder_, POD_NEWLINE);
    if (!result_) result_ = consumeToken(builder_, POD_DIV);
    return result_;
  }

  /* ********************************************************** */
  // {&<<x1>> pod_term}+
  public static boolean link_section(PsiBuilder builder_, int level_, Parser x1) {
    if (!recursion_guard_(builder_, level_, "link_section")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = link_section_0(builder_, level_ + 1, x1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!link_section_0(builder_, level_ + 1, x1)) break;
      if (!empty_element_parsed_guard_(builder_, "link_section", pos_)) break;
    }
    exit_section_(builder_, marker_, LINK_SECTION, result_);
    return result_;
  }

  // &<<x1>> pod_term
  private static boolean link_section_0(PsiBuilder builder_, int level_, Parser x1) {
    if (!recursion_guard_(builder_, level_, "link_section_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = link_section_0_0(builder_, level_ + 1, x1);
    result_ = result_ && pod_term(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // &<<x1>>
  private static boolean link_section_0_0(PsiBuilder builder_, int level_, Parser x1) {
    if (!recursion_guard_(builder_, level_, "link_section_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = x1.parse(builder_, level_);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // {&link_text_negation pod_term}+ &'|'
  public static boolean link_text(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_text")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, LINK_TEXT, "<link text>");
    result_ = link_text_0(builder_, level_ + 1);
    result_ = result_ && link_text_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // {&link_text_negation pod_term}+
  private static boolean link_text_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_text_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = link_text_0_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!link_text_0_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "link_text_0", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // &link_text_negation pod_term
  private static boolean link_text_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_text_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = link_text_0_0_0(builder_, level_ + 1);
    result_ = result_ && pod_term(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // &link_text_negation
  private static boolean link_text_0_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_text_0_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = link_text_negation(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // &'|'
  private static boolean link_text_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_text_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = consumeToken(builder_, POD_PIPE);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // !('>'|'NL'|'/'|'|'|'L')
  static boolean link_text_negation(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_text_negation")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !link_text_negation_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // '>'|'NL'|'/'|'|'|'L'
  private static boolean link_text_negation_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_text_negation_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, POD_ANGLE_RIGHT);
    if (!result_) result_ = consumeToken(builder_, POD_NEWLINE);
    if (!result_) result_ = consumeToken(builder_, POD_DIV);
    if (!result_) result_ = consumeToken(builder_, POD_PIPE);
    if (!result_) result_ = consumeToken(builder_, POD_L);
    return result_;
  }

  /* ********************************************************** */
  // 'identifier' ':' '/' '/' {!('>'|'NL') pod_token}+ &'>'
  public static boolean link_url(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_url")) return false;
    if (!nextTokenIs(builder_, POD_IDENTIFIER)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeTokens(builder_, 0, POD_IDENTIFIER, POD_COLON, POD_DIV, POD_DIV);
    result_ = result_ && link_url_4(builder_, level_ + 1);
    result_ = result_ && link_url_5(builder_, level_ + 1);
    exit_section_(builder_, marker_, LINK_URL, result_);
    return result_;
  }

  // {!('>'|'NL') pod_token}+
  private static boolean link_url_4(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_url_4")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = link_url_4_0(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!link_url_4_0(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "link_url_4", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !('>'|'NL') pod_token
  private static boolean link_url_4_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_url_4_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = link_url_4_0_0(builder_, level_ + 1);
    result_ = result_ && pod_token(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !('>'|'NL')
  private static boolean link_url_4_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_url_4_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !link_url_4_0_0_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // '>'|'NL'
  private static boolean link_url_4_0_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_url_4_0_0_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, POD_ANGLE_RIGHT);
    if (!result_) result_ = consumeToken(builder_, POD_NEWLINE);
    return result_;
  }

  // &'>'
  private static boolean link_url_5(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_url_5")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = consumeToken(builder_, POD_ANGLE_RIGHT);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // link_text '|'  link_after_title
  static boolean link_with_title(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "link_with_title")) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = link_text(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, POD_PIPE);
    pinned_ = result_; // pin = 2
    result_ = result_ && link_after_title(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // over_section_opener  over_section_content over_section_closer
  public static boolean over_section(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "over_section")) return false;
    if (!nextTokenIs(builder_, POD_OVER)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, OVER_SECTION, null);
    result_ = over_section_opener(builder_, level_ + 1);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, over_section_content(builder_, level_ + 1));
    result_ = pinned_ && over_section_closer(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // '=back' 'NL'*
  static boolean over_section_closer(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "over_section_closer")) return false;
    if (!nextTokenIs(builder_, POD_BACK)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, POD_BACK);
    pinned_ = result_; // pin = 1
    result_ = result_ && over_section_closer_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // 'NL'*
  private static boolean over_section_closer_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "over_section_closer_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, POD_NEWLINE)) break;
      if (!empty_element_parsed_guard_(builder_, "over_section_closer_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // over_section_item*
  public static boolean over_section_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "over_section_content")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, OVER_SECTION_CONTENT, "<over section content>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!over_section_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "over_section_content", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, PodParser::recover_over_section);
    return true;
  }

  /* ********************************************************** */
  // item_section
  // 	| any_level_item
  static boolean over_section_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "over_section_item")) return false;
    boolean result_;
    result_ = item_section(builder_, level_ + 1);
    if (!result_) result_ = any_level_item(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '=over' [<<checkAndConvert 'POD_NUMBER, POD_INDENT_LEVEL'>>] 'NL'+
  static boolean over_section_opener(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "over_section_opener")) return false;
    if (!nextTokenIs(builder_, POD_OVER)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, POD_OVER);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, over_section_opener_1(builder_, level_ + 1));
    result_ = pinned_ && over_section_opener_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [<<checkAndConvert 'POD_NUMBER, POD_INDENT_LEVEL'>>]
  private static boolean over_section_opener_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "over_section_opener_1")) return false;
    checkAndConvert(builder_, level_ + 1, POD_NUMBER, POD_INDENT_LEVEL);
    return true;
  }

  // 'NL'+
  private static boolean over_section_opener_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "over_section_opener_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, POD_NEWLINE);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, POD_NEWLINE)) break;
      if (!empty_element_parsed_guard_(builder_, "over_section_opener_2", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // <<parse_section_content default_formatting_section_item>>
  static boolean parse_default_section_content(PsiBuilder builder_, int level_) {
    return parse_section_content(builder_, level_ + 1, PodParser::default_formatting_section_item);
  }

  /* ********************************************************** */
  // &':' pod_section_format for_section_content
  // 	 | pod_section_format <<parsePodSectionContent 'POD_NEWLINE, FOR_SECTION_CONTENT, "Unclosed for section"'>>
  static boolean parse_for_section_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_for_section_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_for_section_content_0(builder_, level_ + 1);
    if (!result_) result_ = parse_for_section_content_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // &':' pod_section_format for_section_content
  private static boolean parse_for_section_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_for_section_content_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_for_section_content_0_0(builder_, level_ + 1);
    result_ = result_ && pod_section_format(builder_, level_ + 1);
    result_ = result_ && for_section_content(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // &':'
  private static boolean parse_for_section_content_0_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_for_section_content_0_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = consumeToken(builder_, POD_COLON);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // pod_section_format <<parsePodSectionContent 'POD_NEWLINE, FOR_SECTION_CONTENT, "Unclosed for section"'>>
  private static boolean parse_for_section_content_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_for_section_content_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = pod_section_format(builder_, level_ + 1);
    result_ = result_ && parsePodSectionContent(builder_, level_ + 1, POD_NEWLINE, FOR_SECTION_CONTENT, "Unclosed for section");
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // '*' pod_format_index* | POD_NUMBER pod_format_index* | item_section_title 'NL'*
  static boolean parse_item_section_title(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_item_section_title")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parse_item_section_title_0(builder_, level_ + 1);
    if (!result_) result_ = parse_item_section_title_1(builder_, level_ + 1);
    if (!result_) result_ = parse_item_section_title_2(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // '*' pod_format_index*
  private static boolean parse_item_section_title_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_item_section_title_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, POD_ASTERISK);
    result_ = result_ && parse_item_section_title_0_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // pod_format_index*
  private static boolean parse_item_section_title_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_item_section_title_0_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!pod_format_index(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "parse_item_section_title_0_1", pos_)) break;
    }
    return true;
  }

  // POD_NUMBER pod_format_index*
  private static boolean parse_item_section_title_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_item_section_title_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, POD_NUMBER);
    result_ = result_ && parse_item_section_title_1_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // pod_format_index*
  private static boolean parse_item_section_title_1_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_item_section_title_1_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!pod_format_index(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "parse_item_section_title_1_1", pos_)) break;
    }
    return true;
  }

  // item_section_title 'NL'*
  private static boolean parse_item_section_title_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_item_section_title_2")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = item_section_title(builder_, level_ + 1);
    result_ = result_ && parse_item_section_title_2_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // 'NL'*
  private static boolean parse_item_section_title_2_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_item_section_title_2_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, POD_NEWLINE)) break;
      if (!empty_element_parsed_guard_(builder_, "parse_item_section_title_2_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // '/' {parse_quoted_section | parse_unquoted_section}
  static boolean parse_link_section(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_link_section")) return false;
    if (!nextTokenIs(builder_, POD_DIV)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, POD_DIV);
    pinned_ = result_; // pin = 1
    result_ = result_ && parse_link_section_1(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // parse_quoted_section | parse_unquoted_section
  private static boolean parse_link_section_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_link_section_1")) return false;
    boolean result_;
    result_ = parse_quoted_section(builder_, level_ + 1);
    if (!result_) result_ = parse_unquoted_section(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '"' <<link_section quoted_section_negation>> '"' &'>'
  static boolean parse_quoted_section(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_quoted_section")) return false;
    if (!nextTokenIs(builder_, POD_QUOTE_DOUBLE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, POD_QUOTE_DOUBLE);
    result_ = result_ && link_section(builder_, level_ + 1, PodParser::quoted_section_negation);
    result_ = result_ && consumeToken(builder_, POD_QUOTE_DOUBLE);
    result_ = result_ && parse_quoted_section_3(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // &'>'
  private static boolean parse_quoted_section_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_quoted_section_3")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _AND_);
    result_ = consumeToken(builder_, POD_ANGLE_RIGHT);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // '<' <<formatting_section_content <<x1>>>> ? <<completeOrReport 'POD_ANGLE_RIGHT, "Malformed formatter"'>>
  static boolean parse_section_content(PsiBuilder builder_, int level_, Parser x1) {
    if (!recursion_guard_(builder_, level_, "parse_section_content")) return false;
    if (!nextTokenIs(builder_, POD_ANGLE_LEFT)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_);
    result_ = consumeToken(builder_, POD_ANGLE_LEFT);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, parse_section_content_1(builder_, level_ + 1, x1));
    result_ = pinned_ && completeOrReport(builder_, level_ + 1, POD_ANGLE_RIGHT, "Malformed formatter") && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // <<formatting_section_content <<x1>>>> ?
  private static boolean parse_section_content_1(PsiBuilder builder_, int level_, Parser x1) {
    if (!recursion_guard_(builder_, level_, "parse_section_content_1")) return false;
    formatting_section_content(builder_, level_ + 1, x1);
    return true;
  }

  /* ********************************************************** */
  // section_title 'NL'*
  static boolean parse_section_title(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_section_title")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = section_title(builder_, level_ + 1);
    result_ = result_ && parse_section_title_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // 'NL'*
  private static boolean parse_section_title_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "parse_section_title_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, POD_NEWLINE)) break;
      if (!empty_element_parsed_guard_(builder_, "parse_section_title_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // <<link_section section_negation>>
  static boolean parse_unquoted_section(PsiBuilder builder_, int level_) {
    return link_section(builder_, level_ + 1, PodParser::section_negation);
  }

  /* ********************************************************** */
  // !<<eof>>
  // {
  // 	head1_section
  // 	| head2_section
  // 	| head3_section
  // 	| head4_section
  // 	| any_level_item
  // 	| item_section
  // }
  static boolean pod_file_item(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_file_item")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = pod_file_item_0(builder_, level_ + 1);
    result_ = result_ && pod_file_item_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // !<<eof>>
  private static boolean pod_file_item_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_file_item_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !eof(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // head1_section
  // 	| head2_section
  // 	| head3_section
  // 	| head4_section
  // 	| any_level_item
  // 	| item_section
  private static boolean pod_file_item_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_file_item_1")) return false;
    boolean result_;
    result_ = head1_section(builder_, level_ + 1);
    if (!result_) result_ = head2_section(builder_, level_ + 1);
    if (!result_) result_ = head3_section(builder_, level_ + 1);
    if (!result_) result_ = head4_section(builder_, level_ + 1);
    if (!result_) result_ = any_level_item(builder_, level_ + 1);
    if (!result_) result_ = item_section(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // pod_format_italic
  //  	| pod_format_bold
  //  	| pod_format_code
  //  	| pod_format_link
  //  	| pod_format_escape
  //  	| pod_format_file
  //  	| pod_format_nbsp
  //  	| pod_format_index
  //  	| pod_format_null
  static boolean pod_format(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_format")) return false;
    boolean result_;
    result_ = pod_format_italic(builder_, level_ + 1);
    if (!result_) result_ = pod_format_bold(builder_, level_ + 1);
    if (!result_) result_ = pod_format_code(builder_, level_ + 1);
    if (!result_) result_ = pod_format_link(builder_, level_ + 1);
    if (!result_) result_ = pod_format_escape(builder_, level_ + 1);
    if (!result_) result_ = pod_format_file(builder_, level_ + 1);
    if (!result_) result_ = pod_format_nbsp(builder_, level_ + 1);
    if (!result_) result_ = pod_format_index(builder_, level_ + 1);
    if (!result_) result_ = pod_format_null(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // 'B' parse_default_section_content
  public static boolean pod_format_bold(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_format_bold")) return false;
    if (!nextTokenIs(builder_, POD_B)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POD_FORMAT_BOLD, null);
    result_ = consumeToken(builder_, POD_B);
    pinned_ = result_; // pin = 1
    result_ = result_ && parse_default_section_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'C' parse_default_section_content
  public static boolean pod_format_code(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_format_code")) return false;
    if (!nextTokenIs(builder_, POD_C)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POD_FORMAT_CODE, null);
    result_ = consumeToken(builder_, POD_C);
    pinned_ = result_; // pin = 1
    result_ = result_ && parse_default_section_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'E' parse_default_section_content
  public static boolean pod_format_escape(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_format_escape")) return false;
    if (!nextTokenIs(builder_, POD_E)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POD_FORMAT_ESCAPE, null);
    result_ = consumeToken(builder_, POD_E);
    pinned_ = result_; // pin = 1
    result_ = result_ && parse_default_section_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'F' parse_default_section_content
  public static boolean pod_format_file(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_format_file")) return false;
    if (!nextTokenIs(builder_, POD_F)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POD_FORMAT_FILE, null);
    result_ = consumeToken(builder_, POD_F);
    pinned_ = result_; // pin = 1
    result_ = result_ && parse_default_section_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'X' parse_default_section_content
  public static boolean pod_format_index(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_format_index")) return false;
    if (!nextTokenIs(builder_, POD_X)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POD_FORMAT_INDEX, null);
    result_ = consumeToken(builder_, POD_X);
    pinned_ = result_; // pin = 1
    result_ = result_ && parse_default_section_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // pod_format_index+ {'NL'+ | <<eof>>}
  static boolean pod_format_indexes(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_format_indexes")) return false;
    if (!nextTokenIs(builder_, POD_X)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = pod_format_indexes_0(builder_, level_ + 1);
    result_ = result_ && pod_format_indexes_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // pod_format_index+
  private static boolean pod_format_indexes_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_format_indexes_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = pod_format_index(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!pod_format_index(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "pod_format_indexes_0", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // 'NL'+ | <<eof>>
  private static boolean pod_format_indexes_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_format_indexes_1")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = pod_format_indexes_1_0(builder_, level_ + 1);
    if (!result_) result_ = eof(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // 'NL'+
  private static boolean pod_format_indexes_1_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_format_indexes_1_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, POD_NEWLINE);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, POD_NEWLINE)) break;
      if (!empty_element_parsed_guard_(builder_, "pod_format_indexes_1_0", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // 'I' parse_default_section_content
  public static boolean pod_format_italic(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_format_italic")) return false;
    if (!nextTokenIs(builder_, POD_I)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POD_FORMAT_ITALIC, null);
    result_ = consumeToken(builder_, POD_I);
    pinned_ = result_; // pin = 1
    result_ = result_ && parse_default_section_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'L' <<parse_section_content link_formatting_section>>
  public static boolean pod_format_link(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_format_link")) return false;
    if (!nextTokenIs(builder_, POD_L)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POD_FORMAT_LINK, null);
    result_ = consumeToken(builder_, POD_L);
    pinned_ = result_; // pin = 1
    result_ = result_ && parse_section_content(builder_, level_ + 1, PodParser::link_formatting_section);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'S' parse_default_section_content
  public static boolean pod_format_nbsp(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_format_nbsp")) return false;
    if (!nextTokenIs(builder_, POD_S)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POD_FORMAT_NBSP, null);
    result_ = consumeToken(builder_, POD_S);
    pinned_ = result_; // pin = 1
    result_ = result_ && parse_default_section_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // 'Z' parse_default_section_content
  public static boolean pod_format_null(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_format_null")) return false;
    if (!nextTokenIs(builder_, POD_Z)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POD_FORMAT_NULL, null);
    result_ = consumeToken(builder_, POD_Z);
    pinned_ = result_; // pin = 1
    result_ = result_ && parse_default_section_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  /* ********************************************************** */
  // pod_paragraph_content
  public static boolean pod_paragraph(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_paragraph")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POD_PARAGRAPH, "<pod paragraph>");
    result_ = pod_paragraph_content(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  /* ********************************************************** */
  // pod_term+ 'NL'*
  static boolean pod_paragraph_content(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_paragraph_content")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = pod_paragraph_content_0(builder_, level_ + 1);
    result_ = result_ && pod_paragraph_content_1(builder_, level_ + 1);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // pod_term+
  private static boolean pod_paragraph_content_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_paragraph_content_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = pod_term(builder_, level_ + 1);
    while (result_) {
      int pos_ = current_position_(builder_);
      if (!pod_term(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "pod_paragraph_content_0", pos_)) break;
    }
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  // 'NL'*
  private static boolean pod_paragraph_content_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_paragraph_content_1")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, POD_NEWLINE)) break;
      if (!empty_element_parsed_guard_(builder_, "pod_paragraph_content_1", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // '=pod'
  public static boolean pod_section(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_section")) return false;
    if (!nextTokenIs(builder_, POD_POD)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, POD_POD);
    exit_section_(builder_, marker_, POD_SECTION, result_);
    return result_;
  }

  /* ********************************************************** */
  // ':' ? <<collapseNonSpaceTo 'POD_FORMAT_NAME '>>
  public static boolean pod_section_format(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_section_format")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, POD_SECTION_FORMAT, "<pod section format>");
    result_ = pod_section_format_0(builder_, level_ + 1);
    result_ = result_ && collapseNonSpaceTo(builder_, level_ + 1, POD_FORMAT_NAME );
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // ':' ?
  private static boolean pod_section_format_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_section_format_0")) return false;
    consumeToken(builder_, POD_COLON);
    return true;
  }

  /* ********************************************************** */
  // pod_format | pod_token
  static boolean pod_term(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_term")) return false;
    boolean result_;
    result_ = pod_format(builder_, level_ + 1);
    if (!result_) result_ = pod_token(builder_, level_ + 1);
    return result_;
  }

  /* ********************************************************** */
  // '/' | '\' | '*' | '|' | ':'
  //                       | '"' | "'" | '`'
  //                       | '(' | ')' | '[' | ']' | '{' | '}' | '<' | '>'
  //                       | 'identifier' | 'number' | 'symbol'
  static boolean pod_token(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_token")) return false;
    boolean result_;
    result_ = consumeToken(builder_, POD_DIV);
    if (!result_) result_ = consumeToken(builder_, POD_BACKREF);
    if (!result_) result_ = consumeToken(builder_, POD_ASTERISK);
    if (!result_) result_ = consumeToken(builder_, POD_PIPE);
    if (!result_) result_ = consumeToken(builder_, POD_COLON);
    if (!result_) result_ = consumeToken(builder_, POD_QUOTE_DOUBLE);
    if (!result_) result_ = consumeToken(builder_, POD_QUOTE_SINGLE);
    if (!result_) result_ = consumeToken(builder_, POD_QUOTE_TICK);
    if (!result_) result_ = consumeToken(builder_, POD_PAREN_LEFT);
    if (!result_) result_ = consumeToken(builder_, POD_PAREN_RIGHT);
    if (!result_) result_ = consumeToken(builder_, POD_BRACKET_LEFT);
    if (!result_) result_ = consumeToken(builder_, POD_BRACKET_RIGHT);
    if (!result_) result_ = consumeToken(builder_, POD_BRACE_LEFT);
    if (!result_) result_ = consumeToken(builder_, POD_BRACE_RIGHT);
    if (!result_) result_ = consumeToken(builder_, POD_ANGLE_LEFT);
    if (!result_) result_ = consumeToken(builder_, POD_ANGLE_RIGHT);
    if (!result_) result_ = consumeToken(builder_, POD_IDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, POD_NUMBER);
    if (!result_) result_ = consumeToken(builder_, POD_SYMBOL);
    return result_;
  }

  /* ********************************************************** */
  // 'code'
  public static boolean pod_verbatim_paragraph(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "pod_verbatim_paragraph")) return false;
    if (!nextTokenIs(builder_, POD_CODE)) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = consumeToken(builder_, POD_CODE);
    exit_section_(builder_, marker_, POD_VERBATIM_PARAGRAPH, result_);
    return result_;
  }

  /* ********************************************************** */
  // !('"' '>'|'>'|'NL'|'L')
  static boolean quoted_section_negation(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "quoted_section_negation")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !quoted_section_negation_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // '"' '>'|'>'|'NL'|'L'
  private static boolean quoted_section_negation_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "quoted_section_negation_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = parseTokens(builder_, 0, POD_QUOTE_DOUBLE, POD_ANGLE_RIGHT);
    if (!result_) result_ = consumeToken(builder_, POD_ANGLE_RIGHT);
    if (!result_) result_ = consumeToken(builder_, POD_NEWLINE);
    if (!result_) result_ = consumeToken(builder_, POD_L);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // !(<<eof>>|'=back'|'=head1'|'=head2'|'=head3'|'=head4')
  static boolean recover_over_section(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_over_section")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !recover_over_section_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // <<eof>>|'=back'|'=head1'|'=head2'|'=head3'|'=head4'
  private static boolean recover_over_section_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "recover_over_section_0")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_);
    result_ = eof(builder_, level_ + 1);
    if (!result_) result_ = consumeToken(builder_, POD_BACK);
    if (!result_) result_ = consumeToken(builder_, POD_HEAD1);
    if (!result_) result_ = consumeToken(builder_, POD_HEAD2);
    if (!result_) result_ = consumeToken(builder_, POD_HEAD3);
    if (!result_) result_ = consumeToken(builder_, POD_HEAD4);
    exit_section_(builder_, marker_, null, result_);
    return result_;
  }

  /* ********************************************************** */
  // pod_file_item *
  static boolean root(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "root")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!pod_file_item(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "root", pos_)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // !('>'|'NL'|'L')
  static boolean section_negation(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "section_negation")) return false;
    boolean result_;
    Marker marker_ = enter_section_(builder_, level_, _NOT_);
    result_ = !section_negation_0(builder_, level_ + 1);
    exit_section_(builder_, level_, marker_, result_, false, null);
    return result_;
  }

  // '>'|'NL'|'L'
  private static boolean section_negation_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "section_negation_0")) return false;
    boolean result_;
    result_ = consumeToken(builder_, POD_ANGLE_RIGHT);
    if (!result_) result_ = consumeToken(builder_, POD_NEWLINE);
    if (!result_) result_ = consumeToken(builder_, POD_L);
    return result_;
  }

  /* ********************************************************** */
  // pod_term*
  public static boolean section_title(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "section_title")) return false;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, SECTION_TITLE, "<section title>");
    while (true) {
      int pos_ = current_position_(builder_);
      if (!pod_term(builder_, level_ + 1)) break;
      if (!empty_element_parsed_guard_(builder_, "section_title", pos_)) break;
    }
    exit_section_(builder_, level_, marker_, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // '=unknown' [parse_section_title] 'NL'*
  public static boolean unknown_section(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unknown_section")) return false;
    if (!nextTokenIs(builder_, POD_UNKNOWN)) return false;
    boolean result_, pinned_;
    Marker marker_ = enter_section_(builder_, level_, _NONE_, UNKNOWN_SECTION, null);
    result_ = consumeToken(builder_, POD_UNKNOWN);
    pinned_ = result_; // pin = 1
    result_ = result_ && report_error_(builder_, unknown_section_1(builder_, level_ + 1));
    result_ = pinned_ && unknown_section_2(builder_, level_ + 1) && result_;
    exit_section_(builder_, level_, marker_, result_, pinned_, null);
    return result_ || pinned_;
  }

  // [parse_section_title]
  private static boolean unknown_section_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unknown_section_1")) return false;
    parse_section_title(builder_, level_ + 1);
    return true;
  }

  // 'NL'*
  private static boolean unknown_section_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "unknown_section_2")) return false;
    while (true) {
      int pos_ = current_position_(builder_);
      if (!consumeToken(builder_, POD_NEWLINE)) break;
      if (!empty_element_parsed_guard_(builder_, "unknown_section_2", pos_)) break;
    }
    return true;
  }

}
