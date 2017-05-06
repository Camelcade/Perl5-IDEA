/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.findusages;

import com.intellij.psi.PsiElement;
import com.intellij.usages.impl.rules.UsageType;
import com.intellij.usages.impl.rules.UsageTypeProvider;
import com.perl5.lang.pod.PodLanguage;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 11.04.2016.
 */
public class PerlUsageTypeProvider implements UsageTypeProvider {
  public static final UsageType DOCUMENT_USAGE = new UsageType("Documentation");

  @Nullable
  @Override
  public UsageType getUsageType(PsiElement element) {
    if (element.getLanguage() == PodLanguage.INSTANCE) {
      return DOCUMENT_USAGE;
    }
    return null;
  }
}
