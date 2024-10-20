/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.htmlmason.parser.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonArgsBlock;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonBlock;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonCompositeElement;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonNamedElement;
import com.perl5.lang.perl.psi.PerlSubNameElement;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public abstract class HTMLMasonStubBasedNamedElementImpl<T extends StubElement<?>> extends HTMLMasonStubBasedElement<T>
  implements HTMLMasonNamedElement {
  public HTMLMasonStubBasedNamedElementImpl(@NotNull T stub, @NotNull IStubElementType nodeType) {
    super(stub, nodeType);
  }

  public HTMLMasonStubBasedNamedElementImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
    if (!HTML_MASON_IDENTIFIER_PATTERN.matcher(name).matches()) {
      throw new IncorrectOperationException("Incorrect HTML::Mason identifier");
    }

    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier instanceof LeafPsiElement leafPsiElement) {
      leafPsiElement.replaceWithText(name);
    }

    return this;
  }

  protected abstract @Nullable String getNameFromStub();


  @Override
  public String getName() {
    String name = getNameFromStub();
    if (name != null) {
      return name;
    }

    PsiElement nameIdentifier = getNameIdentifier();
    if (nameIdentifier != null) {
      return nameIdentifier.getText();
    }
    return super.getName();
  }

  @Override
  public @Nullable PsiElement getNameIdentifier() {
    PsiElement firstChild = getFirstChild();
    if (firstChild != null) {
      firstChild = PerlPsiUtil.getNextSignificantSibling(firstChild);
      if (firstChild instanceof PerlSubNameElement) {
        return firstChild;
      }
    }
    return null;
  }

  @Override
  public int getTextOffset() {
    PsiElement nameIdentifier = getNameIdentifier();

    return nameIdentifier == null
           ? super.getTextOffset()
           : nameIdentifier.getTextOffset();
  }


  public @NotNull List<HTMLMasonCompositeElement> getArgsBlocks() {
    StubElement<?> rootStub = getGreenStub();

    //noinspection Duplicates duplicates file implementation
    if (rootStub != null) {
      final List<HTMLMasonCompositeElement> result = new ArrayList<>();

      PerlPsiUtil.processElementsFromStubs(
        rootStub,
        psi -> {
          if (psi instanceof HTMLMasonArgsBlock argsBlock) {
            result.add(argsBlock);
          }
          return true;
        },
        HTMLMasonNamedElement.class
      );
      return result;
    }

    HTMLMasonBlock block = PsiTreeUtil.getChildOfType(this, HTMLMasonBlock.class);
    if (block != null) {
      return block.getArgsBlocks();
    }

    return Collections.emptyList();
  }
}
