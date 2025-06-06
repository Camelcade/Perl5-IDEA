/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

import com.perl5.lang.perl.psi.properties.PerlConvertableCompoundSimple;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PerlIfUnlessCompound extends PerlConvertableCompoundSimple {

  @Override
  default boolean isConvertableToModifier() {
    return PerlConvertableCompoundSimple.super.isConvertableToModifier() &&
           getUnconditionalBlock() == null && getConditionalBlockList().size() == 1;
  }


  @Override
  default @Nullable PsiPerlBlock getBlock() {
    List<PsiPerlConditionalBlock> conditionalBlockList = getConditionalBlockList();
    return conditionalBlockList.isEmpty() ? null : conditionalBlockList.getFirst().getBlock();
  }

  @Override
  default @Nullable PsiPerlConditionExpr getConditionExpr() {
    List<PsiPerlConditionalBlock> conditionalBlockList = getConditionalBlockList();
    if (conditionalBlockList.size() != 1) {
      return null;
    }
    return conditionalBlockList.getFirst().getConditionExpr();
  }

  @SuppressWarnings("override")
  @NotNull
  List<PsiPerlConditionalBlock> getConditionalBlockList();

  @SuppressWarnings("override")
  @Nullable
  PsiPerlUnconditionalBlock getUnconditionalBlock();
}
