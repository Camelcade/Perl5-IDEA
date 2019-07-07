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

package com.perl5.lang.mojolicious.idea.commenter;

import com.intellij.codeInsight.generation.SelfManagingCommenter;
import com.intellij.lang.Commenter;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class MojoliciousCommenter implements Commenter, SelfManagingCommenter<MojoliciousCommenter.EmbeddedPerlCommenterDataHolder> {

  @Nullable
  @Override
  public EmbeddedPerlCommenterDataHolder createLineCommentingState(int startLine,
                                                                   int endLine,
                                                                   @NotNull Document document,
                                                                   @NotNull PsiFile file) {
    System.err.println("createLineCommentingState" + " " + startLine + " " + endLine + " " + document + " " + file);
    return new EmbeddedPerlCommenterDataHolder(startLine, endLine, document, file);
  }

  @Nullable
  @Override
  public EmbeddedPerlCommenterDataHolder createBlockCommentingState(int selectionStart,
                                                                    int selectionEnd,
                                                                    @NotNull Document document,
                                                                    @NotNull PsiFile file) {
    System.err.println("createLineCommentingState" + " " + selectionStart + " " + selectionEnd + " " + document + " " + file);
    return new EmbeddedPerlCommenterDataHolder(selectionStart, selectionEnd, document, file);
  }

  @Override
  public void commentLine(int line, int offset, @NotNull Document document, @NotNull EmbeddedPerlCommenterDataHolder data) {
    System.err.println("commentLine" + " " + line + " " + offset + " " + document + " " + data);
  }

  @Override
  public void uncommentLine(int line, int offset, @NotNull Document document, @NotNull EmbeddedPerlCommenterDataHolder data) {
    System.err.println("unCommentLine" + " " + line + " " + offset + " " + document + " " + data);
  }

  @Override
  public boolean isLineCommented(int line, int offset, @NotNull Document document, @NotNull EmbeddedPerlCommenterDataHolder data) {
    System.err.println("isLineCommented" + " " + line + " " + offset + " " + document + " " + data);
    return false;
  }

  @Nullable
  @Override
  public String getCommentPrefix(int line, @NotNull Document document, @NotNull EmbeddedPerlCommenterDataHolder data) {
    System.err.println("getCommentPrefix" + " " + line + " " + document + " " + data);
    return null;
  }

  @Nullable
  @Override
  public TextRange getBlockCommentRange(int selectionStart,
                                        int selectionEnd,
                                        @NotNull Document document,
                                        @NotNull EmbeddedPerlCommenterDataHolder data) {
    System.err.println("getBlockCommentRange" + " " + selectionStart + " " + selectionEnd + " " + document + " " + data);
    return null;
  }

  @Nullable
  @Override
  public String getBlockCommentPrefix(int selectionStart, @NotNull Document document, @NotNull EmbeddedPerlCommenterDataHolder data) {
    System.err.println("getBlockCommentPrefix" + " " + selectionStart + " " + document + " " + data);
    return null;
  }

  @Nullable
  @Override
  public String getBlockCommentSuffix(int selectionEnd, @NotNull Document document, @NotNull EmbeddedPerlCommenterDataHolder data) {
    System.err.println("getBlockCommentSuffix" + " " + selectionEnd + " " + document + " " + data);
    return null;
  }

  @Override
  public void uncommentBlockComment(int startOffset, int endOffset, Document document, EmbeddedPerlCommenterDataHolder data) {
    System.err.println("uncommentBlockComment" + " " + startOffset + " " + endOffset + " " + document + " " + data);
  }

  @NotNull
  @Override
  public TextRange insertBlockComment(int startOffset, int endOffset, Document document, EmbeddedPerlCommenterDataHolder data) {
    System.err.println("insertBlockComment" + " " + startOffset + " " + endOffset + " " + document + " " + data);
    return new TextRange(0, 0);
  }

  @Nullable
  @Override
  public String getLineCommentPrefix() {
    return "#linecommentprefix";
  }

  @Nullable
  @Override
  public String getBlockCommentPrefix() {
    return "#blockcommentprefix";
  }

  @Nullable
  @Override
  public String getBlockCommentSuffix() {
    return "#blockcommentsuffix";
  }

  @Nullable
  @Override
  public String getCommentedBlockCommentPrefix() {
    return "#blockcommentprefix";
  }

  @Nullable
  @Override
  public String getCommentedBlockCommentSuffix() {
    return "#blockcommentsuffix";
  }

  static class EmbeddedPerlCommenterDataHolder extends com.intellij.codeInsight.generation.CommenterDataHolder {
    protected int start;    // context-dependent start line or selection start
    protected int end;        // context-dependent end line or selectionend
    protected Document document;
    protected PsiFile file;

    public EmbeddedPerlCommenterDataHolder(int start, int end, Document document, PsiFile file) {
      this.start = start;
      this.end = end;
      this.document = document;
      this.file = file;
    }

    public int getStart() {
      return start;
    }

    public int getEnd() {
      return end;
    }

    public Document getDocument() {
      return document;
    }

    public PsiFile getFile() {
      return file;
    }
  }
}
