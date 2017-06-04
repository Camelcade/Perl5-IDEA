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

package intellilang;

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.lang.Language;
import com.intellij.lang.injection.MultiHostRegistrar;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.perl5.lang.perl.idea.intellilang.PerlHeredocLanguageInjector;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PerlHeredocInjectionTestCase extends PerlLightCodeInsightFixtureTestCase {
  @NotNull
  protected PerlHeredocElementImpl getHeredocUnderCursor() {
    return getElementAtCaretWithoutInjection(PerlHeredocElementImpl.class);
  }

  protected String getHeredocDecodedText(@Nullable PerlHeredocElementImpl heredocElement) {
    assertNotNull(heredocElement);
    StringBuilder sb = new StringBuilder();
    LiteralTextEscaper<PerlHeredocElementImpl> escaper = heredocElement.createLiteralTextEscaper();
    // host MUST be auto-injected with our own injector
    new PerlHeredocLanguageInjector().getLanguagesToInject(new MultiHostRegistrar() {
      @NotNull
      @Override
      public MultiHostRegistrar startInjecting(@NotNull Language language) {
        return this;
      }

      @NotNull
      @Override
      public MultiHostRegistrar addPlace(@Nullable String prefix,
                                         @Nullable String suffix,
                                         @NotNull PsiLanguageInjectionHost host,
                                         @NotNull TextRange rangeInsideHost) {
        escaper.decode(rangeInsideHost, sb);
        return this;
      }

      @Override
      public void doneInjecting() {

      }
    }, heredocElement);
    return sb.toString();
  }
}
