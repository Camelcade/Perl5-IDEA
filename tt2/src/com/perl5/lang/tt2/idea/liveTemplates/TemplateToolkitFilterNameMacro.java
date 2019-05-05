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

package com.perl5.lang.tt2.idea.liveTemplates;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.template.Expression;
import com.intellij.codeInsight.template.ExpressionContext;
import com.intellij.codeInsight.template.Macro;
import com.intellij.codeInsight.template.Result;
import com.perl5.lang.tt2.TemplateToolkitFilterNames;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class TemplateToolkitFilterNameMacro extends Macro implements TemplateToolkitFilterNames {
  public static final LookupElement[] LOOKUP_ELEMENTS;

  static {
    List<LookupElement> list = new ArrayList<>();
    for (String filterName : FILTER_NAMES) {
      list.add(LookupElementBuilder.create(filterName));
    }
    LOOKUP_ELEMENTS = list.toArray(LookupElement.EMPTY_ARRAY);
  }

  @Override
  public String getName() {
    return "tt2FilterName";
  }

  @Override
  public String getPresentableName() {
    return "tt2FilterName()";
  }

  @NotNull
  @Override
  public String getDefaultValue() {
    return "filtername";
  }

  @Nullable
  @Override
  public Result calculateResult(@NotNull Expression[] params, ExpressionContext context) {
    return null;
  }

  @Nullable
  @Override
  public Result calculateQuickResult(@NotNull Expression[] params, ExpressionContext context) {
    return calculateResult(params, context);
  }

  @Nullable
  @Override
  public LookupElement[] calculateLookupItems(@NotNull Expression[] params, ExpressionContext context) {
    return LOOKUP_ELEMENTS;
  }
}
