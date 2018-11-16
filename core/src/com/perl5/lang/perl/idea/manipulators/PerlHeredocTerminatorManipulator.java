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

package com.perl5.lang.perl.idea.manipulators;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.AbstractElementManipulator;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.IncorrectOperationException;
import com.perl5.lang.perl.psi.PerlHeredocTerminatorElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 27.09.2015.
 */
public class PerlHeredocTerminatorManipulator extends AbstractElementManipulator<PerlHeredocTerminatorElement> {
  @Override
  public PerlHeredocTerminatorElement handleContentChange(@NotNull PerlHeredocTerminatorElement element,
                                                          @NotNull TextRange range,
                                                          String newContent) throws IncorrectOperationException {
    if (newContent.isEmpty()) {
      throw new IncorrectOperationException("You can't set heredoc terminator to the empty one");
    }
    return (PerlHeredocTerminatorElement)((LeafPsiElement)element).replaceWithText(newContent);
  }
}
