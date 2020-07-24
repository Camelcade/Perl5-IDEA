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

package com.perl5.lang.perl.psi.properties;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.psi.PsiPerlBlock;
import org.jetbrains.annotations.Nullable;

/**
 * Implement this interface if element contains block
 */
public interface PerlBlockOwner extends PsiElement {
  /**
   * @return use {@link #getBlockSmart()} instead. This method can't handle lazy elements, it's auto-generated
   */
  @Deprecated
  default @Nullable PsiPerlBlock getBlock() {
    return PsiTreeUtil.getChildOfType(this, PsiPerlBlock.class);
  }

  /**
   * @return a nested block. This method is aware about lazy parsable blocks
   */
  default @Nullable PsiPerlBlock getBlockSmart() {
    return getBlock();
  }
}
