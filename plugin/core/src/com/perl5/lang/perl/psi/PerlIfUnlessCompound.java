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


  @Nullable
  @Override
  default PsiPerlBlock getBlock() {
    List<PsiPerlConditionalBlock> conditionalBlockList = getConditionalBlockList();
    return conditionalBlockList.isEmpty() ? null : conditionalBlockList.get(0).getBlock();
  }

  @Nullable
  @Override
  default PsiPerlConditionExpr getConditionExpr() {
    List<PsiPerlConditionalBlock> conditionalBlockList = getConditionalBlockList();
    if (conditionalBlockList.size() != 1) {
      return null;
    }
    return conditionalBlockList.get(0).getConditionExpr();
  }

  @NotNull
  List<PsiPerlConditionalBlock> getConditionalBlockList();

  @Nullable
  PsiPerlUnconditionalBlock getUnconditionalBlock();
}
