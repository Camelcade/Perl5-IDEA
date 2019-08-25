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

package com.perl5.lang.perl.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.ObjectUtils;
import com.perl5.lang.perl.psi.properties.PerlCompound;
import com.perl5.lang.perl.psi.properties.PerlLoop;
import org.jetbrains.annotations.Nullable;

import static com.perl5.lang.perl.lexer.PerlElementTypesGenerated.LP_CODE_BLOCK;

/**
 * Represents block with optional continue block
 */
public interface PerlBlockCompound extends PerlLoop, PerlCompound {
  /**
   * @return a nested block. This method is aware about lazy parsable blocks
   */
  @Nullable
  default PsiPerlBlock getBlockSmart() {
    PsiPerlBlock block = getBlock();
    if (block != null) {
      return block;
    }
    PsiElement child = getFirstChild();
    if (PsiUtilCore.getElementType(child) != LP_CODE_BLOCK) {
      return null;
    }
    return ObjectUtils.tryCast(child.getFirstChild(), PsiPerlBlock.class);
  }

}
