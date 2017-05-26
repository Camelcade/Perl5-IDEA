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

package com.perl5.lang.perl.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.*;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.intellilang.PerlHeredocLiteralEscaper;
import com.perl5.lang.perl.psi.PerlHeredocTerminatorElement;
import com.perl5.lang.perl.psi.PerlVisitor;
import com.perl5.lang.perl.psi.PsiPerlVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.HEREDOC_END_INDENTABLE;

/**
 * Created by hurricup on 10.06.2015.
 */
public class PerlHeredocElementImpl extends PerlCompositeElementImpl implements PsiLanguageInjectionHost {
  public PerlHeredocElementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public boolean isValidHost() {
    return PerlSharedSettings.getInstance(getProject()).ALLOW_INJECTIONS_WITH_INTERPOLATION || getChildren().length == 0;
  }

  @Override
  public PsiLanguageInjectionHost updateText(@NotNull final String text) {
    return ElementManipulators.handleContentChange(this, text);
  }


  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiPerlVisitor) {
      ((PerlVisitor)visitor).visitHeredocElement(this);
    }
    else {
      super.accept(visitor);
    }
  }

  @NotNull
  @Override
  public LiteralTextEscaper<PerlHeredocElementImpl> createLiteralTextEscaper() {
    return new PerlHeredocLiteralEscaper(this);
  }

  public boolean isIndentable() {
    return PsiUtilCore.getElementType(getTerminatorElement()) == HEREDOC_END_INDENTABLE;
  }

  @Nullable
  public PerlHeredocTerminatorElement getTerminatorElement() {
    return CachedValuesManager.getCachedValue(this, () -> {
      PsiElement terminator = getNextSibling();
      while (terminator instanceof PsiWhiteSpace) {
        terminator = terminator.getNextSibling();
      }
      return CachedValueProvider.Result.create(
        terminator instanceof PerlHeredocTerminatorElementImpl ? (PerlHeredocTerminatorElement)terminator : null,
        this
      );
    });
  }

  public int getIndentSize() {
    if (!isIndentable()) {
      return 0;
    }

    PerlHeredocTerminatorElement terminatorElement = getTerminatorElement();
    if (terminatorElement == null) {
      return 0;
    }

    return terminatorElement.getTextRange().getStartOffset() - getTextRange().getEndOffset();
  }
}
