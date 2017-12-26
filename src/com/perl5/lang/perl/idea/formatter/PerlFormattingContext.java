/*
 * Copyright 2015-2017 Alexandr Evstigneev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.perl5.lang.perl.idea.formatter;

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.containers.FactoryMap;
import com.intellij.util.containers.MultiMap;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.formatter.blocks.PerlFormattingBlock;
import com.perl5.lang.perl.idea.formatter.blocks.PerlSyntheticBlock;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import com.perl5.lang.perl.psi.PsiPerlStatementModifier;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.intellij.formatting.WrapType.*;
import static com.intellij.psi.codeStyle.CommonCodeStyleSettings.*;

public class PerlFormattingContext implements PerlFormattingTokenSets {
  public final static TokenSet BLOCK_OPENERS = TokenSet.create(
    LEFT_BRACE,
    LEFT_BRACKET,
    LEFT_PAREN
  );
  public final static TokenSet BLOCK_CLOSERS = TokenSet.create(
    RIGHT_BRACE,
    RIGHT_BRACKET,
    RIGHT_PAREN,

    SEMICOLON
  );
  public static final TokenSet COMMA_LIKE_SEQUENCES = TokenSet.create(
    COMMA_SEQUENCE_EXPR,
    SUB_SIGNATURE,
    METHOD_SIGNATURE_CONTENT,
    FUNC_SIGNATURE_CONTENT,
    TRENAR_EXPR
  );

  private static final TokenSet NORMAL_WRAP_ELEMENTS = TokenSet.create(
    SUB_SIGNATURE,
    METHOD_SIGNATURE_CONTENT,
    FUNC_SIGNATURE_CONTENT,
    COMMA_SEQUENCE_EXPR
  );

  private final Map<ASTNode, Wrap> myWrapMap = new THashMap<>();
  private final Map<ASTNode, Alignment> myOperatorsAlignmentsMap = FactoryMap.create(sequence -> Alignment.createAlignment(true));
  private final Map<ASTNode, Alignment> myElementsALignmentsMap = FactoryMap.create(sequence -> Alignment.createAlignment(true));
  private final Map<ASTNode, Alignment> myCommentsAlignmentMap = FactoryMap.create(parent -> Alignment.createAlignment(true));
  private final Map<ASTNode, Map<ASTNode, Alignment>> myStringListAlignmentMap = FactoryMap.create(listNode -> {
    Map<Integer, Alignment> generatingMap = FactoryMap.create(key -> Alignment.createAlignment(true));

    int column = 0;
    Map<ASTNode, Alignment> itemsMap = new THashMap<>();
    ASTNode run = listNode.getFirstChildNode();
    while (run != null) {
      if (PsiUtilCore.getElementType(run) == STRING_CONTENT) {
        itemsMap.put(run, generatingMap.get(column++));
      }
      else if (StringUtil.containsLineBreak(run.getChars())) {
        column = 0;
      }
      run = run.getTreeNext();
    }

    return itemsMap;
  });

  private final CommonCodeStyleSettings mySettings;
  private final PerlCodeStyleSettings myPerlSettings;
  private final SpacingBuilder mySpacingBuilder;
  private final Map<PsiFile, List<TextRange>> myHeredocRangesMap = FactoryMap.create(file -> {
    if (!(file instanceof PerlFileImpl)) {
      return Collections.emptyList();
    }
    final Document document = file.getViewProvider().getDocument();
    if (document == null) {
      return Collections.emptyList();
    }
    List<TextRange> result = new ArrayList<>();
    file.accept(new PsiElementVisitor() {
      @Override
      public void visitElement(PsiElement element) {
        if (PsiUtilCore.getElementType(element) == HEREDOC_OPENER) {
          int startOffset = element.getNode().getStartOffset();
          result.add(TextRange.create(startOffset + 1, document.getLineEndOffset(document.getLineNumber(startOffset))));
        }
        else {
          element.acceptChildren(this);
        }
      }
    });
    return result;
  });
  /**
   * Elements that must have LF between them
   */
  public final static TokenSet LF_ELEMENTS = TokenSet.create(
    LABEL_DECLARATION,
    STATEMENT,
    FOR_COMPOUND,
    WHILE_COMPOUND,
    WHEN_COMPOUND,
    UNTIL_COMPOUND,
    IF_COMPOUND,
    USE_STATEMENT
  );
  private final static MultiMap<IElementType, IElementType> OPERATOR_COLLISIONS_MAP = new MultiMap<>();

  static {
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_PLUS_PLUS, OPERATOR_PLUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_PLUS, OPERATOR_PLUS_PLUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_PLUS, OPERATOR_PLUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_MINUS_MINUS, OPERATOR_MINUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_MINUS, OPERATOR_MINUS_MINUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_MINUS, OPERATOR_MINUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_SMARTMATCH, OPERATOR_BITWISE_NOT);
  }

  public PerlFormattingContext(@NotNull CodeStyleSettings settings) {
    mySettings = settings.getCommonSettings(PerlLanguage.INSTANCE);
    myPerlSettings = settings.getCustomSettings(PerlCodeStyleSettings.class);
    mySpacingBuilder = createSpacingBuilder();
  }

  protected SpacingBuilder createSpacingBuilder() {
    return PerlSpacingBuilderFactory.createSpacingBuilder(mySettings, myPerlSettings);
  }

  public CommonCodeStyleSettings getSettings() {
    return mySettings;
  }

  public PerlCodeStyleSettings getPerlSettings() {
    return myPerlSettings;
  }

  public SpacingBuilder getSpacingBuilder() {
    return mySpacingBuilder;
  }

  public PerlIndentProcessor getIndentProcessor() {
    return PerlIndentProcessor.INSTANCE;
  }

  /**
   * Checks if Heredoc is ahead of current block and it's not possible to insert newline
   *
   * @param node in question
   * @return check result
   */
  public boolean isNewLineForbiddenAt(@NotNull ASTNode node) {
    List<TextRange> heredocRanges = myHeredocRangesMap.get(node.getPsi().getContainingFile());

    int startOffset = node.getStartOffset();
    for (TextRange range : heredocRanges) {
      if (range.contains(startOffset)) {
        return true;
      }
    }

    return false;
  }


  @Nullable
  public Spacing getSpacing(@NotNull ASTBlock parent, @Nullable Block child1, @NotNull Block child2) {
    if (parent instanceof PerlSyntheticBlock) {
      parent = ((PerlSyntheticBlock)parent).getRealBlock();
    }

    if (child1 instanceof PerlSyntheticBlock) {
      child1 = ((PerlSyntheticBlock)child1).getLastRealBlock();
    }

    if (child2 instanceof PerlSyntheticBlock) {
      child2 = ((PerlSyntheticBlock)child2).getFirstRealBlock();
    }

    if (child1 instanceof ASTBlock && child2 instanceof ASTBlock) {
      ASTNode parentNode = parent.getNode();
      IElementType parentNodeType = PsiUtilCore.getElementType(parentNode);
      ASTNode child1Node = ((ASTBlock)child1).getNode();
      IElementType child1Type = child1Node.getElementType();
      ASTNode child2Node = ((ASTBlock)child2).getNode();
      IElementType child2Type = child2Node.getElementType();

      if (parentNodeType == PARENTHESISED_EXPR &&
          (child1Type == LEFT_PAREN || child2Type == RIGHT_PAREN) &&
          parentNode.getPsi().getParent() instanceof PsiPerlStatementModifier) {
        return getSettings().SPACE_WITHIN_IF_PARENTHESES ?
               Spacing.createSpacing(1, 1, 0, true, 1) :
               Spacing.createSpacing(0, 0, 0, true, 1);
      }

      // fix for number/concat
      if (child2Type == OPERATOR_CONCAT) {
        ASTNode run = child1Node;
        while (run instanceof CompositeElement) {
          run = run.getLastChildNode();
        }

        if (run != null) {
          IElementType runType = run.getElementType();
          if (runType == NUMBER_SIMPLE || runType == NUMBER && StringUtil.endsWith(run.getText(), ".")) {
            return Spacing.createSpacing(1, 1, 0, true, 1);
          }
        }
      }

      // LF after opening brace and before closing need to check if here-doc opener is in the line
      if (LF_ELEMENTS.contains(child1Type) && LF_ELEMENTS.contains(child2Type)) {
        if (!isNewLineForbiddenAt(child1Node)) {
          return Spacing.createSpacing(0, 0, 1, true, 1);
        }
        else {
          return Spacing.createSpacing(1, Integer.MAX_VALUE, 0, true, 1);
        }
      }

      // small inline blocks
      if (parentNodeType == BLOCK && !inGrepMapSort(parentNode) && !blockHasLessChildrenThan(parentNode, 2) &&
          (BLOCK_OPENERS.contains(child1Type) && ((PerlFormattingBlock)child1).isFirst()
           || BLOCK_CLOSERS.contains(child2Type) && ((PerlFormattingBlock)child2).isLast()
          )
          && !isNewLineForbiddenAt(child1Node)
        ) {
        return Spacing.createSpacing(0, 0, 1, true, 1);
      }
      if (parentNodeType == PARENTHESISED_CALL_ARGUMENTS &&
          child2Type == RIGHT_PAREN &&
          PsiUtilCore.getElementType(PsiTreeUtil.getDeepestLast(child1Node.getPsi())) == RIGHT_PAREN
        ) {
        return Spacing.createSpacing(0, 0, 0, true, 0);
      }

      // hack for + ++/- --/~~ ~
      if ((child2Type == PREFIX_UNARY_EXPR || child2Type == PREF_PP_EXPR) && OPERATOR_COLLISIONS_MAP.containsKey(child1Type)) {
        IElementType rightSignType = PsiUtilCore.getElementType(child2Node.getFirstChildNode());
        if (OPERATOR_COLLISIONS_MAP.get(child1Type).contains(rightSignType)) {
          return Spacing.createSpacing(1, 1, 0, true, 1);
        }
      }
      // hack for ++ +/-- -
      else if (child1Type == SUFF_PP_EXPR && OPERATOR_COLLISIONS_MAP.containsKey(child2Type)) {
        IElementType leftSignType = PsiUtilCore.getElementType(child1Node.getLastChildNode());
        if (OPERATOR_COLLISIONS_MAP.get(child2Type).contains(leftSignType)) {
          return Spacing.createSpacing(1, 1, 0, true, 1);
        }
      }
    }
    return getSpacingBuilder().getSpacing(parent, child1, child2);
  }

  /**
   * Check if we are in grep map or sort
   *
   * @return check result
   */
  private static boolean inGrepMapSort(@NotNull ASTNode node) {
    ASTNode parent = node.getTreeParent();
    IElementType parentElementType;
    return parent != null &&
           ((parentElementType = parent.getElementType()) == GREP_EXPR || parentElementType == SORT_EXPR || parentElementType == MAP_EXPR);
  }

  /**
   * Checks if block contains more than specified number of meaningful children. Spaces and line comments are being ignored
   *
   * @return check result
   */
  private static boolean blockHasLessChildrenThan(@NotNull ASTNode node, int maxChildren) {
    int counter = -2; // for braces
    ASTNode childNode = node.getFirstChildNode();
    while (childNode != null) {
      IElementType nodeType = childNode.getElementType();
      if (nodeType != TokenType.WHITE_SPACE && nodeType != COMMENT_LINE && nodeType != SEMICOLON) {
        if (++counter >= maxChildren) {
          return false;
        }
      }
      childNode = childNode.getTreeNext();
    }
    return true;
  }

  @Nullable
  public Alignment getAlignment(@NotNull ASTNode childNode) {
    ASTNode parentNode = childNode.getTreeParent();
    IElementType parentNodeType = PsiUtilCore.getElementType(parentNode);
    IElementType childNodeType = PsiUtilCore.getElementType(childNode);
    PerlCodeStyleSettings perlCodeStyleSettings = getPerlSettings();
    if (childNodeType == FAT_COMMA &&
        parentNodeType == COMMA_SEQUENCE_EXPR &&
        perlCodeStyleSettings.ALIGN_FAT_COMMA) {
      return myOperatorsAlignmentsMap.get(parentNode);
    }
    else if (parentNodeType == TRENAR_EXPR &&
             (childNodeType == QUESTION || childNodeType == COLON) &&
             perlCodeStyleSettings.ALIGN_TERNARY) {
      return myOperatorsAlignmentsMap.get(parentNode);
    }
    else if (childNodeType == OPERATOR_DEREFERENCE &&
             parentNodeType == DEREF_EXPR &&
             perlCodeStyleSettings.ALIGN_DEREFERENCE_IN_CHAIN) {
      return myOperatorsAlignmentsMap.get(parentNode);
    }
    else if (childNodeType == STRING_CONTENT &&
             (parentNodeType == STRING_LIST || parentNodeType == LP_STRING_QW) &&
             perlCodeStyleSettings.ALIGN_QW_ELEMENTS) {
      return myStringListAlignmentMap.get(parentNode).get(childNode);
    }
    else if (childNodeType == COMMENT_LINE && myPerlSettings.ALIGN_COMMENTS_IN_LIST) {
      ASTNode prevNode = childNode.getTreePrev();
      if (prevNode == null || StringUtil.containsLineBreak(prevNode.getChars())) {
        return null;
      }
      if (parentNodeType == COMMA_SEQUENCE_EXPR) {
        return myCommentsAlignmentMap.get(parentNode);
      }
      ASTNode prevNonSpaceNode = getPrevNonSpaceNode(childNode);
      if (PsiUtilCore.getElementType(prevNonSpaceNode) == COMMA_SEQUENCE_EXPR) {
        return myCommentsAlignmentMap.get(prevNonSpaceNode);
      }
    }
    else if (parentNodeType == COMMA_SEQUENCE_EXPR &&
             childNodeType != COMMA &&
             childNodeType != FAT_COMMA &&
             myPerlSettings.ALIGN_LIST_ELEMENTS) {
      return myElementsALignmentsMap.get(parentNode);
    }
    else if (( childNodeType == VARIABLE_DECLARATION_ELEMENT ||
               ( childNodeType == RESERVED_UNDEF && VARIABLE_DECLARATIONS.contains(parentNodeType) ) ) &&
             myPerlSettings.ALIGN_LIST_ELEMENTS) {
      return myElementsALignmentsMap.get(parentNode);
    }
    return null;
  }

  @Nullable
  private ASTNode getPrevNonSpaceNode(@NotNull ASTNode node) {
    ASTNode prevNode = node.getTreePrev();
    return PsiUtilCore.getElementType(prevNode) != TokenType.WHITE_SPACE ? prevNode : prevNode.getTreePrev();
  }

  @Nullable
  public Wrap getWrap(@NotNull ASTNode childNode) {
    ASTNode parentNode = childNode.getTreeParent();
    IElementType parentNodeType = PsiUtilCore.getElementType(parentNode);
    IElementType childNodeType = PsiUtilCore.getElementType(childNode);

    if (isNewLineForbiddenAt(childNode)) {
      return null;
    }
    else if (childNodeType == COMMENT_LINE) {
      return mySettings.WRAP_COMMENTS ? Wrap.createWrap(WRAP_AS_NEEDED, false) : Wrap.createWrap(NONE, false);
    }
    else if (parentNodeType == TRENAR_EXPR && childNodeType != COLON && childNodeType != QUESTION) {
      return getWrap(parentNode, CHOP_DOWN_IF_LONG, false);
    }
    else if (parentNodeType != TRENAR_EXPR &&
             COMMA_LIKE_SEQUENCES.contains(parentNodeType) &&
             childNodeType != COMMA &&
             childNodeType != FAT_COMMA) {
      if (parentNodeType == COMMA_SEQUENCE_EXPR) {
        return getWrap(parentNode, NORMAL, false);
      }
      return getWrap(parentNode, CHOP_DOWN_IF_LONG, false);
    }
    else if (( parentNodeType == STRING_LIST || parentNodeType == LP_STRING_QW) &&
             ( childNodeType == STRING_CONTENT || childNodeType == QUOTE_SINGLE_CLOSE)) {
      return getWrap(parentNode, NORMAL, false);
    }
    else if (childNodeType == VARIABLE_DECLARATION_ELEMENT ||
             ( childNodeType == RESERVED_UNDEF && VARIABLE_DECLARATIONS.contains(parentNodeType) )) {
      return getWrap(parentNode, CHOP_DOWN_IF_LONG, false);
    }
    else if (parentNodeType == DEREF_EXPR && childNodeType == OPERATOR_DEREFERENCE) {
      return getWrapBySettings(parentNode, mySettings.METHOD_CALL_CHAIN_WRAP, true);
    }
    else if (BINARY_EXPRESSIONS.contains(parentNodeType) && !BINARY_OPERATORS.contains(childNodeType)) {
      return getWrapBySettings(parentNode, mySettings.BINARY_OPERATION_WRAP, false);
    }
    else if (NORMAL_WRAP_ELEMENTS.contains(childNodeType)) {
      return Wrap.createWrap(WrapType.NORMAL, false);
    }
    return null;
  }

  @NotNull
  private Wrap getWrapBySettings(@NotNull ASTNode parentNode, int settingsOption, boolean wrapFirst) {
    return getWrap(parentNode, getWrapType(settingsOption), wrapFirst);
  }

  @NotNull
  private WrapType getWrapType(int settingsOption) {
    if (( settingsOption & WRAP_ON_EVERY_ITEM ) != 0) {
      return CHOP_DOWN_IF_LONG;
    }
    else if (( settingsOption & WRAP_ALWAYS ) != 0) {
      return ALWAYS;
    }
    else if (( settingsOption & WRAP_AS_NEEDED ) != 0) {
      return NORMAL;
    }
    return NONE;
  }

  @NotNull
  private Wrap getWrap(@NotNull ASTNode parentNode, @NotNull WrapType type, boolean wrapFirst) {
    return myWrapMap.computeIfAbsent(parentNode, key -> Wrap.createWrap(type, wrapFirst));
  }
}
