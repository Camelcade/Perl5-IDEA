package com.perl5.lang.perl.idea.project;

import com.intellij.injected.editor.DocumentWindowImpl;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.source.tree.injected.InjectedLanguageUtil;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.HEREDOC_END_INDENTABLE;

/**
 * Created by hurricup on 27.05.2017.
 */
public class Perl5HeredocInjectionsWatcher extends PsiTreeChangeAdapter {
  private void dropCache(@NotNull PerlHeredocElementImpl heredocElement) {

    PsiFile file = heredocElement.getContainingFile();

    if (file == null) {
      return;
    }
    PsiFile perlPsi = file.getViewProvider().getPsi(PerlLanguage.INSTANCE);
    if (perlPsi == null) {
      return;
    }

    Project project = heredocElement.getProject();
    List<Pair<PsiElement, TextRange>> files = InjectedLanguageManager.getInstance(
      project).getInjectedPsiFiles(heredocElement);
    if (files == null || files.isEmpty()) {
      return;
    }

    PsiElement injectedFile = files.get(0).first;
    if (!(injectedFile instanceof PsiFile)) {
      return;
    }
    Document document = PsiDocumentManager.getInstance(project).getCachedDocument((PsiFile)injectedFile);
    if (!(document instanceof DocumentWindowImpl)) {
      return;
    }

    InjectedLanguageUtil.clearCaches((PsiFile)injectedFile, (DocumentWindowImpl)document);
  }

  private void processElement(@Nullable PsiElement element) {
    if (element == null) {
      return;
    }
    PsiElement possibleTerminator = element.getNextSibling();
    if (PsiUtilCore.getElementType(possibleTerminator) == HEREDOC_END_INDENTABLE) {
      PsiElement heredocBody = element.getPrevSibling();
      if (heredocBody instanceof PerlHeredocElementImpl) {
        dropCache((PerlHeredocElementImpl)heredocBody);
      }
    }
    else if (element instanceof PerlHeredocElementImpl && ((PerlHeredocElementImpl)element).isIndentable()) {
      dropCache((PerlHeredocElementImpl)element);
    }
    else {
      PerlHeredocElementImpl heredocElement = PsiTreeUtil.getParentOfType(element, PerlHeredocElementImpl.class);
      if (heredocElement != null && heredocElement.isIndentable()) {
        dropCache(heredocElement);
      }
    }
  }

  @Override
  public void beforeChildRemoval(@NotNull PsiTreeChangeEvent event) {
    processElement(event.getChild());
  }

  @Override
  public void beforeChildReplacement(@NotNull PsiTreeChangeEvent event) {
    processElement(event.getOldChild());
  }

  @Override
  public void childAdded(@NotNull PsiTreeChangeEvent event) {
    processElement(event.getChild());
  }
}
