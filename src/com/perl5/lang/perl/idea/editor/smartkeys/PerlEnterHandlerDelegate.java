/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.editor.smartkeys;

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.codeInsight.Perl5CodeInsightSettings;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlHeredocOpener;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 06.09.2015.
 */
public class PerlEnterHandlerDelegate implements EnterHandlerDelegate, PerlElementTypes {
  private final Pattern EMPTY_OPENER_PATTERN = Pattern.compile("<<\\s*(?:\"\"|''|``)");

  @Override
  public Result preprocessEnter(@NotNull PsiFile file,
                                @NotNull Editor editor,
                                @NotNull Ref<Integer> caretOffset,
                                @NotNull Ref<Integer> caretAdvance,
                                @NotNull DataContext dataContext,
                                EditorActionHandler originalHandler) {
    if (file instanceof PerlFileImpl) {
      int offset = caretOffset.get();
      PsiElement currentElement = file.findElementAt(offset);

      //noinspection ConstantConditions
      if (PsiUtilCore.getElementType(currentElement) == COMMENT_LINE && offset > currentElement.getTextOffset()) {
        Document document = file.getViewProvider().getDocument();

        if (document != null) {
          int lineNumber = document.getLineNumber(offset);
          int lineEnd = document.getLineEndOffset(lineNumber);

          if (lineEnd > offset) {
            document.insertString(offset, "# ");
          }
        }
      }
    }
    return Result.Continue;
  }

  @Override
  public Result postProcessEnter(@NotNull PsiFile file, @NotNull Editor editor, @NotNull DataContext dataContext) {
    if (file instanceof PerlFileImpl) {
      final CaretModel caretModel = editor.getCaretModel();
      int offset = caretModel.getOffset();
      PsiElement currentElement = file.findElementAt(offset);

      if (currentElement == null ||
          currentElement.getParent() instanceof PerlHeredocElementImpl && Perl5CodeInsightSettings.getInstance().HEREDOC_AUTO_INSERTION) {
        LogicalPosition currentPosition = caretModel.getLogicalPosition();
        final int enterLine = currentPosition.line - 1;
        final int currentOffset = caretModel.getOffset();
        if (enterLine > -1 && currentOffset > 0) {
          final Document document = editor.getDocument();
          final PsiDocumentManager manager = PsiDocumentManager.getInstance(file.getProject());
          manager.commitDocument(document);

          final int lineStartOffset = document.getLineStartOffset(enterLine);

          PsiElement firstLineElement = file.findElementAt(lineStartOffset);
          if (firstLineElement != null) {
            HeredocCollector collector = new HeredocCollector(currentOffset - 1);
            PerlPsiUtil.iteratePsiElementsRight(firstLineElement, collector);

            SmartPsiElementPointer<PerlHeredocOpener> lastOpenerPointer = null;
            for (SmartPsiElementPointer<PerlHeredocOpener> currentOpenerPointer : collector.getResult()) {

              PerlHeredocOpener currentOpener = currentOpenerPointer.getElement();

              if (currentOpener == null) {
                //								System.err.println("Opener invalidated on reparse");
                return Result.Continue;
              }


              String openerName = currentOpener.getName();
              boolean emptyOpener = StringUtil.isEmpty(openerName);
              PsiReference inboundReference = ReferencesSearch.search(currentOpener).findFirst();

              if (inboundReference != null) {
                boolean falseAlarm = false;

                PsiElement run = inboundReference.getElement().getPrevSibling();
                while (run instanceof PsiWhiteSpace) {
                  run = run.getPrevSibling();
                }

                if (run instanceof PerlHeredocElementImpl) {
                  Pattern openerPattern = EMPTY_OPENER_PATTERN;
                  if (!emptyOpener) {
                    openerPattern = Pattern.compile("<<~?(\\s*)(?:" +
                                                    "\"" + openerName + "\"" + "|" +
                                                    "`" + openerName + "`" + "|" +
                                                    "'" + openerName + "'" + "|" +
                                                    "\\\\" + openerName + "|" +
                                                    openerName +
                                                    ")"
                    );
                  }

                  falseAlarm = openerPattern.matcher(run.getNode().getChars()).find();
                }

                if (falseAlarm) // looks like overlapping heredocs
                {
                  inboundReference = null;
                }
                else {
                  lastOpenerPointer = currentOpenerPointer;
                }
              }


              if (inboundReference == null) // disclosed marker
              {
                int addOffset = -1;
                String closeMarker = "\n" + openerName + "\n";

                if (lastOpenerPointer == null) // first one
                {
                  addOffset = currentOffset;
                }
                else // sequentional
                {
                  PerlHeredocOpener lastOpener = lastOpenerPointer.getElement();
                  if (lastOpener == null) {
                    return Result.Continue;
                  }

                  PsiReference lastOpenerReference = ReferencesSearch.search(lastOpener).findFirst();
                  if (lastOpenerReference != null) {
                    PsiElement element = lastOpenerReference.getElement();
                    addOffset = element.getTextRange().getEndOffset();
                    if (!emptyOpener) {
                      closeMarker = "\n" + closeMarker;
                    }
                  }
                  else {
                    return Result.Continue;
                  }
                }

                document.insertString(addOffset, closeMarker);
                manager.commitDocument(document);

                currentOpener = currentOpenerPointer.getElement();

                if (currentOpener == null) {
                  return Result.Continue;
                }

                inboundReference = ReferencesSearch.search(currentOpener).findFirst();

                if (inboundReference != null) {
                  lastOpenerPointer = currentOpenerPointer;
                }
                else {
                  return Result.Continue;
                }
              }
            }
          }
        }
      }
    }
    return Result.Continue;
  }

  static private class HeredocCollector extends PerlPsiUtil.HeredocProcessor {
    protected List<SmartPsiElementPointer<PerlHeredocOpener>> myResult = new ArrayList<SmartPsiElementPointer<PerlHeredocOpener>>();

    public HeredocCollector(int lineEndOffset) {
      super(lineEndOffset);
    }

    @Override
    public boolean process(PsiElement element) {
      if (element == null || element.getNode().getStartOffset() >= lineEndOffset) {
        return false;
      }
      if (element instanceof PerlHeredocOpener) {
        //SmartPointerManager#createSmartPsiElementPointer
        myResult.add(SmartPointerManager.getInstance(element.getProject()).createSmartPsiElementPointer((PerlHeredocOpener)element));
      }
      return true;
    }

    public List<SmartPsiElementPointer<PerlHeredocOpener>> getResult() {
      return myResult;
    }
  }
}
