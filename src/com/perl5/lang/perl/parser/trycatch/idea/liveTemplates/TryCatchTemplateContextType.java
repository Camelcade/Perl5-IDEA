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

package com.perl5.lang.perl.parser.trycatch.idea.liveTemplates;

import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.livetemplates.PerlTemplateContextType;
import com.perl5.lang.perl.parser.trycatch.TryCatchElementPatterns;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 17.04.2016.
 */
public class TryCatchTemplateContextType extends PerlTemplateContextType.Prefix implements TryCatchElementPatterns {
  public TryCatchTemplateContextType() {
    super("PERL5_TRY_CATCH", "Try compound");
  }

  public TryCatchTemplateContextType(@NotNull String id, @NotNull String presentableName) {
    super(id, presentableName);
  }

  @Override
  public boolean isInContext(PsiElement element) {
    return super.isInContext(element);
  }

  public static class Catch extends TryCatchTemplateContextType {
    public Catch() {
      super("PERL5_TRY_CATCH_CATCH", "Catch compound");
    }

    @Override
    public boolean isInContext(PsiElement element) {
      return super.isInContext(element) && ELEMENT_AFTER_TRY_CATCH.accepts(element);
    }
  }
}
