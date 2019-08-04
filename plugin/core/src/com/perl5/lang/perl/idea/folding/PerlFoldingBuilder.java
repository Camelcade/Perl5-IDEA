/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.folding;

import com.intellij.codeInsight.folding.CodeFoldingSettings;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.templateLanguages.OuterLanguageElementImpl;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.impl.PerlNoStatementElement;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElement;
import com.perl5.lang.perl.psi.impl.PerlUseStatementElementBase;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.perl5.lang.perl.lexer.PerlTokenSets.HEREDOC_BODIES_TOKENSET;
import static com.perl5.lang.perl.psi.stubs.PerlStubElementTypes.USE_STATEMENT;

public class PerlFoldingBuilder extends PerlFoldingBuilderBase implements PerlElementTypes, DumbAware {
  public static final String PH_CODE_BLOCK = "{code block}";

  protected static final TokenSet COMMENT_EXCLUDED_TOKENS = TokenSet.EMPTY;

  @NotNull
  @Override
  public FoldingDescriptor[] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
    // @todo handle this
    if (root instanceof OuterLanguageElementImpl) {
      return FoldingDescriptor.EMPTY;
    }

    FoldingRegionsCollector collector = getCollector(document);
    root.accept(collector);
    List<FoldingDescriptor> descriptors = collector.getDescriptors();

    descriptors.addAll(getCommentsDescriptors(collector.getComments(), document));
    descriptors.addAll(getImportDescriptors(collector.getImports()));

    return descriptors.toArray(FoldingDescriptor.EMPTY);
  }

  protected FoldingRegionsCollector getCollector(Document document) {
    return new FoldingRegionsCollector(document);
  }

  /**
   * Searching for sequential comments (starting from newline or sub-block beginning) and making folding descriptors for such blocks of size > 1
   *
   * @param comments list of collected comments
   * @param document document to search in
   */
  private List<FoldingDescriptor> getCommentsDescriptors(@NotNull List<PsiComment> comments, @NotNull Document document) {
    List<FoldingDescriptor> descriptors = new ArrayList<>();

    TokenSet commentExcludedTokens = getCommentExcludedTokens();

    int currentOffset = 0;

    for (PsiComment comment : comments) {
      ASTNode commentNode = comment.getNode();
      IElementType commentElementType = commentNode.getElementType();

      if (currentOffset <= comment.getTextOffset() &&
          !commentExcludedTokens.contains(commentElementType))    // skips already collapsed blocks
      {

        if (commentElementType == POD) {
          TextRange commentRange = comment.getTextRange();
          int startOffset = commentRange.getStartOffset();
          int endOffset = commentRange.getEndOffset();

          if (comment.getText().endsWith("\n")) {
            endOffset--;
          }
          currentOffset = endOffset;
          descriptors.add(new FoldingDescriptor(commentNode, new TextRange(startOffset, endOffset)));
          continue;
        }

        boolean isCollapsible = false;
        PsiElement lastComment = comment;


        if (commentElementType == COMMENT_BLOCK ||
            commentElementType == getTemplateBlockElementType() // template blocks are always collapsible
          ) {
          isCollapsible = true;
        }
        else {
          // checking if this is a first element of block or starts from newline
          while (true) {
            lastComment = lastComment.getPrevSibling();

            if (lastComment == null || lastComment instanceof PsiComment) {
              isCollapsible = true;
              break;
            }
            else if (lastComment instanceof PsiWhiteSpace) {
              // whitespace with newline
              if (StringUtil.containsLineBreak(lastComment.getNode().getChars())) {
                isCollapsible = true;
                break;
              }
            }
            // non-whitespace block
            else {
              break;
            }
          }
        }

        if (isCollapsible) {
          // looking for an end
          int startOffset = comment.getTextOffset();
          if (comment.getText().startsWith("\n") &&
              startOffset > 0 &&
              document.getCharsSequence().charAt(startOffset - 1) != '\n'
            ) {
            startOffset++;
          }
          int endOffset = comment.getTextRange().getEndOffset();

          PsiElement currentComment = comment;

          while (currentComment != null) {
            if (
              currentComment instanceof PsiComment &&
              !commentExcludedTokens.contains(currentComment.getNode().getElementType()) &&
              !currentComment.getText().contains("todo") &&
              !currentComment.getText().contains("fixme")
              ) {
              endOffset = currentComment.getTextOffset() + currentComment.getTextLength();
              if (currentComment.getText().endsWith("\n")) {
                endOffset--;
              }
            }
            else if (!(currentComment instanceof PsiWhiteSpace)) {
              break;
            }

            currentComment = currentComment.getNextSibling();
          }

          if (endOffset > startOffset) {
            int startLine = document.getLineNumber(startOffset);
            int endLine = document.getLineNumber(endOffset);

            if (endLine > startLine) {
              currentOffset = endOffset;
              descriptors.add(new FoldingDescriptor(commentNode, new TextRange(startOffset, endOffset)));
            }
          }
        }
      }
    }

    return descriptors;
  }

  /**
   * Searching for sequential uses and requires, ignoring statement modifiers, comments, pods and whitespaces and making folding descriptors for such blocks of size > 1
   *
   * @param imports list of collected imports
   */
  private List<FoldingDescriptor> getImportDescriptors(@NotNull List<PerlNamespaceElementContainer> imports) {
    List<FoldingDescriptor> descriptors;
    descriptors = new ArrayList<>();

    int currentOffset = -1;

    for (PsiElement perlImport : imports) {
      if (currentOffset < perlImport.getTextOffset()) {
        PsiElement currentStatement = perlImport;

        if (currentStatement instanceof PerlRequireExpr) {
          currentStatement = currentStatement.getParent();
        }

        if (currentStatement instanceof PerlUseStatementElementBase || currentStatement.getFirstChild() instanceof PerlRequireExpr) {
          int blockStart = currentStatement.getTextOffset();
          int blockEnd = blockStart;
          ASTNode blockNode = perlImport.getNode();

          int importsNumber = 0;

          while (currentStatement != null) {
            if (currentStatement instanceof PerlUseStatementElementBase &&
                !((PerlUseStatementElementBase)currentStatement).isPragma() &&
                !((PerlUseStatementElementBase)currentStatement).isVersion()
                || currentStatement.getFirstChild() instanceof PerlRequireExpr) {
              blockEnd = currentStatement.getTextOffset() + currentStatement.getTextLength();
              importsNumber++;
            }
            else if (!(currentStatement instanceof PsiComment || currentStatement instanceof PsiWhiteSpace)) {
              break;
            }

            currentStatement = currentStatement.getNextSibling();
          }

          if (blockEnd != blockStart && importsNumber > 1) {
            currentOffset = blockEnd;
            descriptors.add(new FoldingDescriptor(blockNode, new TextRange(blockStart, blockEnd)));
          }
        }
      }
    }

    return descriptors;
  }

  @Nullable
  @Override
  public String getPlaceholderText(@NotNull ASTNode node) {
    IElementType elementType = node.getElementType();

    if (elementType == BLOCK) {
      return PH_CODE_BLOCK;
    }
    if (elementType == STRING_LIST) {
      return "{strings list}";
    }
    else if (elementType == ANON_ARRAY) {
      return "[array]";
    }
    else if (elementType == ANON_HASH) {
      return "{hash}";
    }
    else if (elementType == PARENTHESISED_EXPR) {
      return "(list)";
    }
    else if (HEREDOC_BODIES_TOKENSET.contains(elementType)) {
      return "<< heredoc >>";
    }
    else if (elementType == POD) {
      return "= POD block =";
    }
    else if (elementType == COMMENT_BLOCK) {
      return "# Block comment";
    }
    else if (elementType == COMMENT_LINE) {
      return "# comments...";
    }
    else if (elementType == USE_STATEMENT || elementType == REQUIRE_EXPR) {
      return "imports...";
    }
    else if (elementType == getTemplateBlockElementType()) {
      return "/ template /";
    }
    else {
      return "unknown entity " + elementType;
    }
  }

  @Override
  public boolean isCollapsedByDefault(@NotNull ASTNode node) {
    IElementType elementType = node.getElementType();
    PsiElement psi = node.getPsi();
    if (elementType == POD) {
      return CodeFoldingSettings.getInstance().COLLAPSE_DOC_COMMENTS;
    }
    else if (elementType == USE_STATEMENT || elementType == REQUIRE_EXPR) {
      return CodeFoldingSettings.getInstance().COLLAPSE_IMPORTS;
    }
    else if (elementType == BLOCK) {
      return CodeFoldingSettings.getInstance().COLLAPSE_METHODS;
    }
    else if (elementType == COMMENT_LINE || elementType == COMMENT_BLOCK) {
      return PerlFoldingSettings.getInstance().COLLAPSE_COMMENTS;
    }
    else if (elementType == ANON_ARRAY) {
      return PerlFoldingSettings.getInstance().COLLAPSE_ANON_ARRAYS;
    }
    else if (elementType == ANON_HASH) {
      return PerlFoldingSettings.getInstance().COLLAPSE_ANON_HASHES;
    }
    else if (elementType == PARENTHESISED_EXPR) {
      return PerlFoldingSettings.getInstance().COLLAPSE_PARENTHESISED;
    }
    else if (elementType == HEREDOC) {
      return PerlFoldingSettings.getInstance().COLLAPSE_HEREDOCS;
    }
    else if (elementType == STRING_LIST) {
      return PerlFoldingSettings.getInstance().COLLAPSE_QW;
    }
    else if (elementType == getTemplateBlockElementType()) {
      return PerlFoldingSettings.getInstance().COLLAPSE_TEMPLATES;
    }
    else {
      return false;
    }
  }

  @Nullable
  protected IElementType getTemplateBlockElementType() {
    return null;
  }

  /**
   * @return list of tokens in PSIComment that should not be included in folding regions
   */
  @NotNull
  protected TokenSet getCommentExcludedTokens() {
    return COMMENT_EXCLUDED_TOKENS;
  }

  public static class FoldingRegionsCollector extends PerlRecursiveVisitor {
    protected final Document myDocument;
    protected List<FoldingDescriptor> myDescriptors = new ArrayList<>();
    protected ArrayList<PerlNamespaceElementContainer> myImports = new ArrayList<>();
    protected ArrayList<PsiComment> myComments = new ArrayList<>();

    public FoldingRegionsCollector(Document document) {
      myDocument = document;
    }

    @Override
    public void visitElement(@NotNull PsiElement element) {
      if (PsiUtilCore.getElementType(element) == POD) {
        addDescriptorFor(myDescriptors, myDocument, element, 0, 0, 0, null, false);
      }
      super.visitElement(element);
    }

    @Override
    public void visitBlock(@NotNull PsiPerlBlock o) {
      addDescriptorFor(myDescriptors, myDocument, o, 0, 0, 1, null, false);
      super.visitBlock(o);
    }

    @Override
    public void visitAnonArray(@NotNull PsiPerlAnonArray o) {
      addDescriptorFor(myDescriptors, myDocument, o, 0, 0, 2, null, false);
      super.visitAnonArray(o);
    }

    @Override
    public void visitParenthesisedExpr(@NotNull PsiPerlParenthesisedExpr o) {
      addDescriptorFor(myDescriptors, myDocument, o, 0, 0, 2, null, false);
      super.visitParenthesisedExpr(o);
    }

    @Override
    public void visitHeredocElement(@NotNull PerlHeredocElementImpl o) {
      addDescriptorFor(myDescriptors, myDocument, o, 0, 1, 0, null, false);
      super.visitHeredocElement(o);
    }

    @Override
    public void visitStringList(@NotNull PsiPerlStringList o) {
      addDescriptorFor(myDescriptors, myDocument, o, 0, 0, 2, null, false);
      super.visitStringList(o);
    }

    @Override
    public void visitAnonHash(@NotNull PsiPerlAnonHash o) {
      PsiElement parent = o.getParent();
      if (parent instanceof PerlUseStatementElementBase) {
        addDescriptorFor(myDescriptors, myDocument, o, 0, 0, 2,
                         ((PerlUseStatementElementBase)parent).getArgumentsFoldingText(),
                         ((PerlUseStatementElementBase)parent).isFoldedByDefault());
      }
      else {
        addDescriptorFor(myDescriptors, myDocument, o, 0, 0, 2, null, false);
      }
      super.visitAnonHash(o);
    }

    @Override
    public void visitComment(PsiComment comment) {
      myComments.add(comment);
      super.visitComment(comment);
    }

    @Override
    public void visitUseStatement(@NotNull PerlUseStatementElement o) {
      myImports.add(o);
      super.visitUseStatement(o);
    }

    @Override
    public void visitNoStatement(@NotNull PerlNoStatementElement o) {
      myImports.add(o);
      super.visitNoStatement(o);
    }

    @Override
    public void visitRequireExpr(@NotNull PsiPerlRequireExpr o) {
      myImports.add(o);
      super.visitRequireExpr(o);
    }

    public List<FoldingDescriptor> getDescriptors() {
      return myDescriptors;
    }

    public ArrayList<PerlNamespaceElementContainer> getImports() {
      return myImports;
    }

    public ArrayList<PsiComment> getComments() {
      return myComments;
    }
  }
}
