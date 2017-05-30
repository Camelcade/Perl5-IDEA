package com.perl5.lang.perl.idea.intellilang;

import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostInjector;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.psi.PerlHeredocTerminatorElement;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 30.05.2017.
 */
public class PerlHeredocLanguageInjector extends AbstractPerlLanguageInjector implements MultiHostInjector {
  private static final List<? extends Class<? extends PsiElement>> ELEMENTS_TO_INJECT =
    Collections.singletonList(PerlHeredocElementImpl.class);

  @Override
  public void getLanguagesToInject(@NotNull MultiHostRegistrar registrar, @NotNull PsiElement context) {
    assert context instanceof PerlHeredocElementImpl;

    if (context.getTextLength() == 0) {
      return;
    }

    PerlHeredocTerminatorElement terminator = ((PerlHeredocElementImpl)context).getTerminatorElement();

    if (terminator == null) {
      return;
    }

    String terminatorText = terminator.getText();
    Language mappedLanguage = LANGUAGE_MAP.get(terminatorText);

    if (mappedLanguage == null) {
      return;
    }

    registrar.startInjecting(mappedLanguage);
    addPlace((PerlHeredocElementImpl)context, registrar);
    registrar.doneInjecting();
  }

  private void addPlace(@NotNull PerlHeredocElementImpl heredocElement, @NotNull MultiHostRegistrar registrar) {
    int indentSize = heredocElement.getRealIndentSize();
    if (indentSize == 0) {
      registrar.addPlace(null, null, heredocElement, ElementManipulators.getValueTextRange(heredocElement));
      return;
    }

    CharSequence sourceText = heredocElement.getNode().getChars();

    int currentLineIndent = 0;
    int sourceOffset = 0;

    int sourceLength = sourceText.length();
    while (sourceOffset < sourceLength) {
      char currentChar = sourceText.charAt(sourceOffset);
      if (currentChar == '\n') {
        registrar.addPlace(null, null, heredocElement, TextRange.from(sourceOffset, 1));
        currentLineIndent = 0;
      }
      else if (Character.isWhitespace(currentChar) && currentLineIndent < indentSize) {
        currentLineIndent++;
      }
      else {
        // got non-space or indents ended, consume till the EOL or EOHost
        int sourceEnd = sourceOffset;
        while (sourceEnd < sourceLength) {
          currentChar = sourceText.charAt(sourceEnd);
          sourceEnd++;
          if (currentChar == '\n') {
            break;
          }
        }

        registrar.addPlace(null, null, heredocElement, TextRange.create(sourceOffset, sourceEnd));
        sourceOffset = sourceEnd;
        currentLineIndent = 0;
        continue;
      }
      sourceOffset++;
    }
  }

  @NotNull
  @Override
  public List<? extends Class<? extends PsiElement>> elementsToInjectIn() {
    return ELEMENTS_TO_INJECT;
  }
}
