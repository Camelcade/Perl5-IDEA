/*
 * Copyright 2015-2020 Alexandr Evstigneev
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
import com.intellij.openapi.diagnostic.Logger;
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
import com.perl5.lang.perl.lexer.PerlTokenSets;
import com.perl5.lang.perl.psi.PerlSignatureElement;
import com.perl5.lang.perl.psi.PsiPerlStatementModifier;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;

import static com.intellij.formatting.WrapType.*;
import static com.intellij.psi.codeStyle.CommonCodeStyleSettings.*;
import static com.perl5.lang.perl.idea.formatter.settings.PerlCodeStyleSettings.OptionalConstructions.*;
import static com.perl5.lang.perl.lexer.PerlTokenSets.*;

public class PerlFormattingContext implements PerlFormattingTokenSets {
  private static final Logger LOG = Logger.getInstance(PerlFormattingContext.class);
  private final Map<ASTNode, Wrap> myWrapMap = new THashMap<>();
  private final Map<Integer, Alignment> myAssignmentsAlignmentsMap = new THashMap<>();
  private final Map<Integer, Alignment> myCommentsAlignmentMap = FactoryMap.create(line -> Alignment.createAlignment(true));
  private final Map<ASTNode, Alignment> myOperatorsAlignmentsMap = FactoryMap.create(sequence -> Alignment.createAlignment(true));
  private final Map<ASTNode, Alignment> myElementsALignmentsMap = FactoryMap.create(sequence -> Alignment.createAlignment(true));
  private final Map<ASTNode, Alignment> myAssignmentsElementAlignmentsMap = FactoryMap.create(sequence -> Alignment.createAlignment(true));
  private final Map<ASTNode, Map<ASTNode, Alignment>> myStringListAlignmentMap = FactoryMap.create(listNode -> {
    Map<Integer, Alignment> generatingMap = FactoryMap.create(key -> Alignment.createAlignment(true));

    int column = 0;
    Map<ASTNode, Alignment> itemsMap = new THashMap<>();
    ASTNode run = listNode.getFirstChildNode();
    while (run != null) {
      if (PsiUtilCore.getElementType(run) == STRING_BARE) {
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
  private final @NotNull BitSet myCommentsLines;
  private final @Nullable Document myDocument;
  private final @NotNull CommonCodeStyleSettings mySettings;
  private final @NotNull PerlCodeStyleSettings myPerlSettings;
  private final @NotNull SpacingBuilder mySpacingBuilder;
  private final @NotNull TextRange myTextRange;
  private final @NotNull FormattingMode myFormattingMode;
  private final List<TextRange> myHeredocRangesList = new ArrayList<>();

  private static final MultiMap<IElementType, IElementType> OPERATOR_COLLISIONS_MAP = new MultiMap<>();

  static {
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_PLUS_PLUS, OPERATOR_PLUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_PLUS, OPERATOR_PLUS_PLUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_PLUS, OPERATOR_PLUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_MINUS_MINUS, OPERATOR_MINUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_MINUS, OPERATOR_MINUS_MINUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_MINUS, OPERATOR_MINUS);
    OPERATOR_COLLISIONS_MAP.putValue(OPERATOR_SMARTMATCH, OPERATOR_BITWISE_NOT);
  }

  public PerlFormattingContext(@NotNull PsiElement element,
                               @NotNull TextRange textRange,
                               @NotNull CodeStyleSettings settings,
                               @NotNull FormattingMode formattingMode) {
    mySettings = settings.getCommonSettings(PerlLanguage.INSTANCE);
    myPerlSettings = settings.getCustomSettings(PerlCodeStyleSettings.class);
    mySpacingBuilder = createSpacingBuilder();
    PsiFile containingFile = element.getContainingFile();
    myDocument = containingFile == null ? null : containingFile.getViewProvider().getDocument();
    myCommentsLines = myDocument == null ? new BitSet() : new BitSet(myDocument.getLineCount());
    myFormattingMode = formattingMode;
    myTextRange = textRange;
  }

  protected SpacingBuilder createSpacingBuilder() {
    return PerlSpacingBuilderFactory.createSpacingBuilder(mySettings, myPerlSettings);
  }

  /**
   * registers an ASTNode in some internal structure if it's necessary.
   * e.g. comments, here-docs
   */
  public @NotNull ASTNode registerNode(@NotNull ASTNode node) {
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

  public @NotNull CommonCodeStyleSettings getSettings() {
    return mySettings;
  }

  public @NotNull PerlCodeStyleSettings getPerlSettings() {
    return myPerlSettings;
  }

  public @NotNull SpacingBuilder getSpacingBuilder() {
    return mySpacingBuilder;
  }

  private @Nullable Document getDocument() {
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


  public @Nullable Spacing getSpacing(@NotNull ASTBlock parent, @Nullable Block child1, @NotNull Block child2) {
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
      IElementType child1Type = PsiUtilCore.getElementType(child1Node);
      ASTNode child2Node = ((ASTBlock)child2).getNode();
      IElementType child2Type = PsiUtilCore.getElementType(child2Node);

      if (ALL_QUOTE_OPENERS.contains(child1Type) && child2Node != null) {
        CharSequence openerChars = child2Node.getChars();
        int spaces = 0;
        if (openerChars.length() > 0 && Character.isUnicodeIdentifierPart(openerChars.charAt(0))) {
          spaces = 1;
        }
        return Spacing.createSpacing(spaces, spaces, 0, true, 1);
      }

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
          if (runType == NUMBER && StringUtil.endsWith(run.getText(), ".")) {
            return Spacing.createSpacing(1, 1, 0, true, 1);
          }
        }
      }

      // LF after opening brace and before closing need to check if here-doc opener is in the line
      if (LF_ELEMENTS.contains(child1Type) && LF_ELEMENTS.contains(child2Type)) {
        if (!isNewLineForbiddenAt(child2Node)) {
          return Spacing.createSpacing(0, 0, 1, true, 1);
        }
        else {
          return Spacing.createSpacing(1, Integer.MAX_VALUE, 0, true, 1);
        }
      }

      if (parentNodeType == BLOCK) {
        boolean afterOpener = BLOCK_OPENERS.contains(child1Type) && ((PerlFormattingBlock)child1).isFirst();
        boolean beforeCloser = BLOCK_CLOSERS.contains(child2Type) && ((PerlFormattingBlock)child2).isLast();
        IElementType grandParentElementType = PsiUtilCore.getElementType(parentNode.getTreeParent());
        if (PerlTokenSets.CAST_EXPRESSIONS.contains(grandParentElementType)) {
          int spaces = getSettings().SPACE_WITHIN_CAST_PARENTHESES ? 1 : 0;
          return Spacing.createSpacing(spaces, spaces, 0, true, 0);
        }

        // small inline blocks
        if (child1Node != null && grandParentElementType != GREP_EXPR &&
            grandParentElementType != SORT_EXPR && grandParentElementType != MAP_EXPR) {
          boolean isSmallBlock = blockHasLessChildrenThan(parentNode, 2);
          boolean isNewLineAllowed = !isNewLineForbiddenAt(child1Node);
          if (!isSmallBlock && (afterOpener || beforeCloser) && isNewLineAllowed) {
            return Spacing.createSpacing(0, 0, 1, true, 1);
          }
        }
      }
      if (parentNodeType == PARENTHESISED_CALL_ARGUMENTS &&
          child2Type == RIGHT_PAREN &&
          child1Node != null &&
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

      if (child1Type == METHOD && child2Type == CALL_ARGUMENTS &&
          parentNodeType == SUB_CALL && PsiUtilCore.getElementType(parentNode.getTreeParent()) == TYPE_SPECIFIER) {
        return Spacing.createSpacing(0, 0, 0, false, 0);
      }
    }

    return getSpacingBuilder().getSpacing(parent, child1, child2);
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

  public @Nullable Alignment getAlignment(@NotNull ASTNode childNode) {
    ASTNode parentNode = childNode.getTreeParent();
    IElementType parentNodeType = PsiUtilCore.getElementType(parentNode);
    IElementType childNodeType = PsiUtilCore.getElementType(childNode);
    PerlCodeStyleSettings perlCodeStyleSettings = getPerlSettings();
    if (childNodeType == FAT_COMMA &&
        parentNodeType == COMMA_SEQUENCE_EXPR &&
        perlCodeStyleSettings.ALIGN_FAT_COMMA) {
      return myOperatorsAlignmentsMap.get(parentNode);
    }
    else if (parentNodeType == TERNARY_EXPR && mySettings.ALIGN_MULTILINE_TERNARY_OPERATION) {
      return myElementsALignmentsMap.get(parentNode);
    }
    else if (parentNodeType == DEREF_EXPR && mySettings.ALIGN_MULTILINE_CHAINED_METHODS) {
      if (perlCodeStyleSettings.METHOD_CALL_CHAIN_SIGN_NEXT_LINE) {
        if (childNodeType == OPERATOR_DEREFERENCE) {
          return myOperatorsAlignmentsMap.get(parentNode);
        }
      }
      else {
        if (childNodeType != OPERATOR_DEREFERENCE && childNode.getTreePrev() != null) {
          return myOperatorsAlignmentsMap.get(parentNode);
        }
      }
    }
    else if (childNodeType == STRING_BARE &&
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
    else if (parentNodeType == SIGNATURE_CONTENT) {
      return mySettings.ALIGN_MULTILINE_PARAMETERS ? myElementsALignmentsMap.get(parentNode) : null;
    }
    else if ((childNodeType == VARIABLE_DECLARATION_ELEMENT ||
              (childNodeType == UNDEF_EXPR && PerlTokenSets.VARIABLE_DECLARATIONS.contains(parentNodeType))) &&
             myPerlSettings.ALIGN_VARIABLE_DECLARATIONS) {
      return myElementsALignmentsMap.get(parentNode);
    }
    else if (BINARY_EXPRESSIONS.contains(parentNodeType) && mySettings.ALIGN_MULTILINE_BINARY_OPERATION) {
      return myElementsALignmentsMap.get(parentNode);
    }
    else if ((parentNodeType == ASSIGN_EXPR || parentNodeType == SIGNATURE_ELEMENT) && OPERATORS_ASSIGNMENT.contains(childNodeType)) {
      if (myPerlSettings.ALIGN_CONSECUTIVE_ASSIGNMENTS == ALIGN_LINES) {
        return getLineBasedAlignment(childNode, myAssignmentsAlignmentsMap);
      }
      else if (myPerlSettings.ALIGN_CONSECUTIVE_ASSIGNMENTS == ALIGN_IN_STATEMENT) {
        return myAssignmentsElementAlignmentsMap.get(parentNodeType == SIGNATURE_ELEMENT ? parentNode.getTreeParent() : parentNode);
      }
    }
    else if (myPerlSettings.ALIGN_ATTRIBUTES && parentNodeType == ATTRIBUTES &&
             (childNodeType == COLON || isAttributeWithoutColon(childNode))) {
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
  private @Nullable Alignment getLineBasedAlignment(@NotNull ASTNode childNode, @NotNull Map<Integer, Alignment> alignmentsMap) {
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

  private @Nullable ASTNode getPrevNonSpaceNode(@NotNull ASTNode node) {
    ASTNode prevNode = node.getTreePrev();
    return PsiUtilCore.getElementType(prevNode) != TokenType.WHITE_SPACE ? prevNode : prevNode.getTreePrev();
  }

  public @Nullable Wrap getWrap(@NotNull ASTNode childNode) {
    ASTNode parentNode = childNode.getTreeParent();
    IElementType parentNodeType = PsiUtilCore.getElementType(parentNode);
    IElementType childNodeType = PsiUtilCore.getElementType(childNode);

    if (isNewLineForbiddenAt(childNode)) {
      return null;
    }
    else if (childNodeType == COMMENT_LINE) {
      return mySettings.WRAP_COMMENTS ? Wrap.createWrap(WRAP_AS_NEEDED, false) : Wrap.createWrap(NONE, false);
    }
    else if (parentNodeType == TERNARY_EXPR) {
      if (mySettings.TERNARY_OPERATION_SIGNS_ON_NEXT_LINE) {
        if (childNodeType == COLON || childNodeType == QUESTION) {
          return getWrapBySettings(parentNode, mySettings.TERNARY_OPERATION_WRAP, true);
        }
      }
      else if (childNodeType != COLON && childNodeType != QUESTION) {
        return getWrapBySettings(parentNode, mySettings.TERNARY_OPERATION_WRAP, false);
      }
    }
    else if (parentNodeType == SIGNATURE_CONTENT && childNodeType != COMMA && childNodeType != FAT_COMMA) {
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
             (childNodeType == STRING_BARE || childNodeType == QUOTE_SINGLE_CLOSE)) {
      ASTNode anchorNode = parentNodeType == LP_STRING_QW ? parentNode.getTreeParent() : parentNode;
      return getWrapBySettings(anchorNode, myPerlSettings.QW_LIST_WRAP, false);
    }
    else if (childNodeType == VARIABLE_DECLARATION_ELEMENT && parentNodeType != SIGNATURE_ELEMENT ||
             (childNodeType == UNDEF_EXPR && PerlTokenSets.VARIABLE_DECLARATIONS.contains(parentNodeType))) {
      return getWrapBySettings(parentNode, myPerlSettings.VARIABLE_DECLARATION_WRAP, false);
    }
    else if (parentNodeType == DEREF_EXPR) {
      if (myPerlSettings.METHOD_CALL_CHAIN_SIGN_NEXT_LINE) {
        if (childNodeType == OPERATOR_DEREFERENCE) {
          return getWrapBySettings(parentNode, mySettings.METHOD_CALL_CHAIN_WRAP, true);
        }
      }
      else {
        if (childNodeType != OPERATOR_DEREFERENCE) {
          return getWrapBySettings(parentNode, mySettings.METHOD_CALL_CHAIN_WRAP, false);
        }
      }
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
    else if (parentNodeType == SIGNATURE_ELEMENT) {
      PsiElement signatureElement = parentNode.getPsi();
      LOG.assertTrue(signatureElement instanceof PerlSignatureElement);
      PsiElement declarationElement = ((PerlSignatureElement)signatureElement).getDeclarationElement();
      PsiElement defaultValueElement = ((PerlSignatureElement)signatureElement).getDefaultValueElement();
      if (defaultValueElement != null && declarationElement != null &&
          declarationElement.getStartOffsetInParent() < childNode.getStartOffsetInParent() &&
          (childNodeType == OPERATOR_ASSIGN) == mySettings.PLACE_ASSIGNMENT_SIGN_ON_NEXT_LINE) {
        return getWrapBySettings(parentNode, mySettings.ASSIGNMENT_WRAP, true);
      }
    }
    else if (parentNodeType == ATTRIBUTES && (childNodeType == COLON || isAttributeWithoutColon(childNode))) {
      return getWrapBySettings(parentNode, myPerlSettings.ATTRIBUTES_WRAP, false);
    }
    return null;
  }

  private boolean isAttributeWithoutColon(@NotNull ASTNode node) {
    return PsiUtilCore.getElementType(node) == ATTRIBUTE &&
           PsiUtilCore.getElementType(PerlPsiUtil.getPrevSignificantSibling(node.getPsi())) != COLON;
  }

  private @NotNull Wrap getWrapBySettings(@NotNull ASTNode parentNode, int settingsOption, boolean wrapFirst) {
    return getWrap(parentNode, getWrapType(settingsOption), wrapFirst);
  }

  private @NotNull WrapType getWrapType(int settingsOption) {
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

  private @NotNull Wrap getWrap(@NotNull ASTNode parentNode, @NotNull WrapType type, boolean wrapFirst) {
    return myWrapMap.computeIfAbsent(parentNode, key -> Wrap.createWrap(type, wrapFirst));
  }

  public final @NotNull ChildAttributes getChildAttributes(@NotNull PerlAstBlock block, int newChildIndex) {
    return new ChildAttributes(getIndentProcessor().getChildIndent(block, newChildIndex), getChildAlignment(block, newChildIndex));
  }

  public @Nullable Alignment getChildAlignment(@NotNull PerlAstBlock block, int newChildIndex) {
    ASTNode node = block.getNode();
    IElementType elementType = PsiUtilCore.getElementType(node);
    ASTNode parentNode = node == null ? null : node.getTreeParent();

    if (myPerlSettings.ALIGN_CONSECUTIVE_ASSIGNMENTS != NO_ALIGN && elementType == SIGNATURE_ELEMENT) {
      IElementType lastChildNodeType = PsiUtilCore.getElementType(node.getLastChildNode());
      if (lastChildNodeType == OPERATOR_ASSIGN) {
        return null;
      }
      PsiElement psiElement = node.getPsi();
      assert psiElement instanceof PerlSignatureElement;
      if (((PerlSignatureElement)psiElement).hasDeclarationElement()) {
        if (myPerlSettings.ALIGN_CONSECUTIVE_ASSIGNMENTS == ALIGN_IN_STATEMENT) {
          return myAssignmentsElementAlignmentsMap.get(parentNode);
        }
        else if (myPerlSettings.ALIGN_CONSECUTIVE_ASSIGNMENTS == ALIGN_LINES) {
          return getLineBasedAlignment(node, myAssignmentsAlignmentsMap);
        }
      }
    }
    if (elementType == SIGNATURE_CONTENT) {
      return mySettings.ALIGN_MULTILINE_PARAMETERS ? myElementsALignmentsMap.get(block.getNode()) : null;
    }
    if (elementType == ATTRIBUTES && myPerlSettings.ALIGN_ATTRIBUTES) {
      return myElementsALignmentsMap.get(node);
    }
    if (PerlFormattingTokenSets.COMMA_LIKE_SEQUENCES.contains(elementType)) {
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

  public @Nullable Boolean isIncomplete(@NotNull PerlAstBlock block) {
    ASTNode blockNode = block.getNode();
    if (blockNode == null) {
      return null;
    }
    IElementType elementType = block.getElementType();
    if (elementType == ATTRIBUTES) {
      return true;
    }
    if (elementType == SIGNATURE_ELEMENT) {
      PsiElement psiElement = blockNode.getPsi();
      assert psiElement instanceof PerlSignatureElement;
      return !((PerlSignatureElement)psiElement).hasDefaultValueElement();
    }
    if (COMMA_LIKE_SEQUENCES.contains(elementType)) {
      IElementType lastNodeType = PsiUtilCore.getElementType(blockNode.getLastChildNode());
      if (lastNodeType == COMMA || lastNodeType == FAT_COMMA) {
        return true;
      }
      if (elementType == SIGNATURE_CONTENT) {
        Block lastSubBlock = block.getLastSubBlock();
        if (lastSubBlock != null && lastSubBlock.isIncomplete()) {
          return true;
        }
      }
    }
    else if (STATEMENTS.contains(elementType)) {
      PsiElement lastLeaf = PsiTreeUtil.getDeepestLast(blockNode.getPsi());
      return PsiUtilCore.getElementType(lastLeaf) != SEMICOLON;
    }
    else if (LAZY_CODE_BLOCKS.contains(elementType) && PsiUtilCore.getElementType(blockNode.getTreeParent()) == REPLACEMENT_REGEX) {
      return true;
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

  public @NotNull FormattingMode getFormattingMode() {
    return myFormattingMode;
  }

  public @NotNull TextRange getTextRange() {
    return myTextRange;
  }
}
