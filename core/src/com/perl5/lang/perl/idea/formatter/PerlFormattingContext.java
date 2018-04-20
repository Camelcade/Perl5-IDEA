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
import com.perl5.lang.perl.idea.formatter.blocks.PerlAstBlock;
import com.perl5.lang.perl.idea.formatter.blocks.PerlFormattingBlock;
import com.perl5.lang.perl.idea.formatter.blocks.PerlSyntheticBlock;
import com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PsiPerlStatementModifier;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import static com.intellij.formatting.WrapType.*;
import static com.intellij.psi.codeStyle.CommonCodeStyleSettings.*;
import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.ALIGN_IN_STATEMENT;
import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.ALIGN_LINES;
import static com.perl5.lang.perl.lexer.PerlTokenSets.STATEMENTS;

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

  public static final TokenSet SIGNATURES_CONTAINERS = TokenSet.create(
    SUB_SIGNATURE,
    METHOD_SIGNATURE_CONTENT,
    FUNC_SIGNATURE_CONTENT
  );

  public static final TokenSet COMMA_LIKE_SEQUENCES = TokenSet.orSet(
    SIGNATURES_CONTAINERS,
    TokenSet.create(COMMA_SEQUENCE_EXPR)
  );

  private final Map<ASTNode, Wrap> myWrapMap = new THashMap<>();
  private final Map<Integer, Alignment> myAssignmentsAlignmentsMap = new THashMap<>();
  private final Map<Integer, Alignment> myCommentsAlignmentMap = FactoryMap.create(line -> Alignment.createAlignment(true));
  private final Map<ASTNode, Alignment> myOperatorsAlignmentsMap = FactoryMap.create(sequence -> Alignment.createAlignment(true));
  private final Map<ASTNode, Alignment> myElementsALignmentsMap = FactoryMap.create(sequence -> Alignment.createAlignment(true));
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

  /**
   * Contains a bitmask of lines with comments not started with newline
   */
  @NotNull
  private final BitSet myCommentsLines;
  @Nullable
  private final Document myDocument;
  @NotNull
  private final CommonCodeStyleSettings mySettings;
  @NotNull
  private final PerlCodeStyleSettings myPerlSettings;
  @NotNull
  private final SpacingBuilder mySpacingBuilder;
  private final List<TextRange> myHeredocRangesList = new ArrayList<>();

  /**
   * Elements that must have LF between them
   */
  public final static TokenSet LF_ELEMENTS = TokenSet.orSet(
    STATEMENTS,
    TokenSet.create(
      LABEL_DECLARATION,
      FOR_COMPOUND,
      TRYCATCH_COMPOUND,
      FOREACH_COMPOUND,
      WHILE_COMPOUND,
      WHEN_COMPOUND,
      UNTIL_COMPOUND,
      IF_COMPOUND
    ));
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

  public PerlFormattingContext(@NotNull PsiElement element, @NotNull CodeStyleSettings settings) {
    mySettings = settings.getCommonSettings(PerlLanguage.INSTANCE);
    myPerlSettings = settings.getCustomSettings(PerlCodeStyleSettings.class);
    mySpacingBuilder = createSpacingBuilder();
    PsiFile containingFile = element.getContainingFile();
    myDocument = containingFile == null ? null : containingFile.getViewProvider().getDocument();
    myCommentsLines = myDocument == null ? new BitSet() : new BitSet(myDocument.getLineCount());
  }

  protected SpacingBuilder createSpacingBuilder() {
    return PerlSpacingBuilderFactory.createSpacingBuilder(mySettings, myPerlSettings);
  }

  /**
   * registers an ASTNode in some internal structure if it's necessary.
   * e.g. comments, here-docs
   */
  @NotNull
  public ASTNode registerNode(@NotNull ASTNode node) {
    if (getDocument() == null) {
      return node;
    }
    IElementType nodeType = PsiUtilCore.getElementType(node);
    if (nodeType == COMMENT_LINE) {
      ASTNode prevNode = node.getTreePrev();
      if (prevNode != null && !StringUtil.containsLineBreak(prevNode.getChars())) {
        myCommentsLines.set(getNodeLine(node));
      }
    }
    else if (nodeType == HEREDOC_OPENER) {
      myHeredocRangesList.add(
        TextRange.create(node.getStartOffset() + 1, getDocument().getLineEndOffset(getNodeLine(node)))
      );
    }

    return node;
  }

  @NotNull
  public CommonCodeStyleSettings getSettings() {
    return mySettings;
  }

  @NotNull
  public PerlCodeStyleSettings getPerlSettings() {
    return myPerlSettings;
  }

  @NotNull
  public SpacingBuilder getSpacingBuilder() {
    return mySpacingBuilder;
  }

  @Nullable
  private Document getDocument() {
    return myDocument;
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
    int startOffset = node.getStartOffset();
    for (TextRange range : myHeredocRangesList) {
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
    else if (parentNodeType == TRENAR_EXPR && mySettings.ALIGN_MULTILINE_TERNARY_OPERATION) {
      return myElementsALignmentsMap.get(parentNode);
    }
    else if (childNodeType == OPERATOR_DEREFERENCE &&
             parentNodeType == DEREF_EXPR &&
             mySettings.ALIGN_MULTILINE_CHAINED_METHODS) {
      return myOperatorsAlignmentsMap.get(parentNode);
    }
    else if (childNodeType == STRING_CONTENT &&
             (parentNodeType == STRING_LIST || parentNodeType == LP_STRING_QW) &&
             perlCodeStyleSettings.ALIGN_QW_ELEMENTS) {
      return myStringListAlignmentMap.get(parentNode).get(childNode);
    }
    else if (childNodeType == COMMENT_LINE &&
             myPerlSettings.ALIGN_COMMENTS_ON_CONSEQUENT_LINES) {
      int commentLine = getNodeLine(childNode);
      if (myCommentsLines.get(commentLine)) {
        return myCommentsAlignmentMap.get(myCommentsLines.previousClearBit(commentLine));
      }
    }
    else if (parentNodeType == COMMA_SEQUENCE_EXPR &&
             childNodeType != COMMA &&
             childNodeType != FAT_COMMA) {
      IElementType grandParentNodeType = PsiUtilCore.getElementType(parentNode.getTreeParent());
      if (grandParentNodeType == CALL_ARGUMENTS || grandParentNodeType == PARENTHESISED_CALL_ARGUMENTS) {
        if (mySettings.ALIGN_MULTILINE_PARAMETERS_IN_CALLS) {
          return myElementsALignmentsMap.get(parentNode);
        }
      }
      else if (mySettings.ALIGN_MULTILINE_ARRAY_INITIALIZER_EXPRESSION) {
        return myElementsALignmentsMap.get(parentNode);
      }
    }
    else if (SIGNATURES_CONTAINERS.contains(parentNodeType)) {
      return mySettings.ALIGN_MULTILINE_PARAMETERS ? myElementsALignmentsMap.get(parentNode) : null;
    }
    else if ((childNodeType == VARIABLE_DECLARATION_ELEMENT ||
              (childNodeType == RESERVED_UNDEF && VARIABLE_DECLARATIONS.contains(parentNodeType))) &&
             myPerlSettings.ALIGN_VARIABLE_DECLARATIONS) {
      return myElementsALignmentsMap.get(parentNode);
    }
    else if (BINARY_EXPRESSIONS.contains(parentNodeType) && mySettings.ALIGN_MULTILINE_BINARY_OPERATION) {
      return myElementsALignmentsMap.get(parentNode);
    }
    else if (parentNodeType == ASSIGN_EXPR && OPERATORS_ASSIGNMENT.contains(childNodeType)) {
      if (myPerlSettings.ALIGN_CONSECUTIVE_ASSIGNMENTS == ALIGN_LINES) {
        return getLineBasedAlignment(childNode, myAssignmentsAlignmentsMap);
      }
      else if (myPerlSettings.ALIGN_CONSECUTIVE_ASSIGNMENTS == ALIGN_IN_STATEMENT) {
        return myElementsALignmentsMap.get(parentNode);
      }
    }
    else if (parentNodeType == ATTRIBUTES && childNodeType == COLON && myPerlSettings.ALIGN_ATTRIBUTES) {
      return myElementsALignmentsMap.get(parentNode);
    }
    return null;
  }

  /**
   * Returns line-based alignment for the {@code childNode}. Uses previous line alignment from the {@code alignmentsMap}
   * or creates a new one.
   *
   * @param alignmentsMap map for caching line-based values
   */
  @Nullable
  private Alignment getLineBasedAlignment(@NotNull ASTNode childNode, @NotNull Map<Integer, Alignment> alignmentsMap) {
    int nodeLine = getNodeLine(childNode);
    if (nodeLine < 0) {
      return null;
    }
    Alignment alignment = alignmentsMap.get(nodeLine);
    if (alignment != null) {
      return alignment;
    }
    alignment = alignmentsMap.get(nodeLine - 1);
    if (alignment == null) {
      alignment = alignmentsMap.get(nodeLine + 1);
    }
    if (alignment == null) {
      alignment = Alignment.createAlignment(true);
    }
    alignmentsMap.put(nodeLine, alignment);
    return alignment;
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
    else if (parentNodeType == TRENAR_EXPR) {
      if (mySettings.TERNARY_OPERATION_SIGNS_ON_NEXT_LINE) {
        if (childNodeType == COLON || childNodeType == QUESTION) {
          return getWrapBySettings(parentNode, mySettings.TERNARY_OPERATION_WRAP, true);
        }
      }
      else if (childNodeType != COLON && childNodeType != QUESTION) {
        return getWrapBySettings(parentNode, mySettings.TERNARY_OPERATION_WRAP, false);
      }
    }
    else if (SIGNATURES_CONTAINERS.contains(parentNodeType) && childNodeType != COMMA && childNodeType != FAT_COMMA) {
      return getWrapBySettings(parentNode, mySettings.METHOD_PARAMETERS_WRAP, false);
    }
    else if (parentNodeType == COMMA_SEQUENCE_EXPR && childNodeType != COMMA && childNodeType != FAT_COMMA) {
      IElementType grandParentNodeType = PsiUtilCore.getElementType(parentNode.getTreeParent());
      if (grandParentNodeType == CALL_ARGUMENTS || grandParentNodeType == PARENTHESISED_CALL_ARGUMENTS) {
        return getWrapBySettings(parentNode, mySettings.CALL_PARAMETERS_WRAP, false);
      }
      else {
        return getWrapBySettings(parentNode, mySettings.ARRAY_INITIALIZER_WRAP, false);
      }
    }
    else if ((parentNodeType == STRING_LIST || parentNodeType == LP_STRING_QW) &&
             (childNodeType == STRING_CONTENT || childNodeType == QUOTE_SINGLE_CLOSE)) {
      return getWrapBySettings(parentNode, myPerlSettings.QW_LIST_WRAP, false);
    }
    else if (childNodeType == VARIABLE_DECLARATION_ELEMENT ||
             (childNodeType == RESERVED_UNDEF && VARIABLE_DECLARATIONS.contains(parentNodeType))) {
      return getWrapBySettings(parentNode, myPerlSettings.VARIABLE_DECLARATION_WRAP, false);
    }
    else if (parentNodeType == DEREF_EXPR && childNodeType == OPERATOR_DEREFERENCE) {
      return getWrapBySettings(parentNode, mySettings.METHOD_CALL_CHAIN_WRAP, true);
    }
    else if (BINARY_EXPRESSIONS.contains(parentNodeType)) {
      if (mySettings.BINARY_OPERATION_SIGN_ON_NEXT_LINE && BINARY_OPERATORS.contains(childNodeType)) {
        return getWrapBySettings(parentNode, mySettings.BINARY_OPERATION_WRAP, true);
      }
      else if (!mySettings.BINARY_OPERATION_SIGN_ON_NEXT_LINE && !BINARY_OPERATORS.contains(childNodeType)) {
        return getWrapBySettings(parentNode, mySettings.BINARY_OPERATION_WRAP, false);
      }
    }
    else if (parentNodeType == ASSIGN_EXPR &&
             OPERATORS_ASSIGNMENT.contains(childNodeType) == mySettings.PLACE_ASSIGNMENT_SIGN_ON_NEXT_LINE) {
      return getWrapBySettings(parentNode, mySettings.ASSIGNMENT_WRAP, OPERATORS_ASSIGNMENT.contains(childNodeType));
    }
    else if (parentNodeType == ATTRIBUTES && childNodeType == COLON) {
      return getWrapBySettings(parentNode, myPerlSettings.ATTRIBUTES_WRAP, false);
    }
    return null;
  }

  @NotNull
  private Wrap getWrapBySettings(@NotNull ASTNode parentNode, int settingsOption, boolean wrapFirst) {
    return getWrap(parentNode, getWrapType(settingsOption), wrapFirst);
  }

  @NotNull
  private WrapType getWrapType(int settingsOption) {
    if ((settingsOption & WRAP_ON_EVERY_ITEM) != 0) {
      return CHOP_DOWN_IF_LONG;
    }
    else if ((settingsOption & WRAP_ALWAYS) != 0) {
      return ALWAYS;
    }
    else if ((settingsOption & WRAP_AS_NEEDED) != 0) {
      return NORMAL;
    }
    return NONE;
  }

  @NotNull
  private Wrap getWrap(@NotNull ASTNode parentNode, @NotNull WrapType type, boolean wrapFirst) {
    return myWrapMap.computeIfAbsent(parentNode, key -> Wrap.createWrap(type, wrapFirst));
  }

  @NotNull
  public final ChildAttributes getChildAttributes(@NotNull PerlAstBlock block, int newChildIndex) {
    return new ChildAttributes(getIndentProcessor().getChildIndent(block, newChildIndex), getChildAlignment(block, newChildIndex));
  }

  @Nullable
  public Alignment getChildAlignment(@NotNull PerlAstBlock block, int newChildIndex) {
    ASTNode node = block.getNode();
    IElementType elementType = PsiUtilCore.getElementType(node);
    if (PerlFormattingContext.COMMA_LIKE_SEQUENCES.contains(elementType)) {
      return null;
    }

    // this is default algorythm from AbstractBlock#getFirstChildAlignment()
    List<Block> subBlocks = block.getSubBlocks();
    for (final Block subBlock : subBlocks) {
      if (subBlock instanceof ASTBlock && PsiUtilCore.getElementType(((ASTBlock)subBlock).getNode()) == PerlElementTypes.COMMENT_LINE) {
        continue;
      }
      Alignment alignment = subBlock.getAlignment();
      if (alignment != null) {
        return alignment;
      }
    }
    return null;
  }

  @Nullable
  public Boolean isIncomplete(@NotNull PerlAstBlock block) {
    IElementType elementType = block.getElementType();
    if (elementType == COMMA_SEQUENCE_EXPR) {
      IElementType lastNodeType = PsiUtilCore.getElementType(block.getNode().getLastChildNode());
      if (lastNodeType == COMMA || lastNodeType == FAT_COMMA) {
        return true;
      }
    }
    else if (STATEMENTS.contains(elementType)) {
      PsiElement lastLeaf = PsiTreeUtil.getDeepestLast(block.getNode().getPsi());
      return PsiUtilCore.getElementType(lastLeaf) != SEMICOLON;
    }

    return null;
  }

  /**
   * @return line number of the node or -1 if node is null or document missing
   */
  private int getNodeLine(@Nullable ASTNode node) {
    Document document = getDocument();
    return node == null || document == null ? -1 : document.getLineNumber(node.getStartOffset());
  }
}
