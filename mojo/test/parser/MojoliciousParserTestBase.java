/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package parser;

import com.intellij.lang.ParserDefinition;
import com.intellij.psi.LanguageFileViewProviders;
import com.perl5.lang.mojolicious.MojoliciousLanguage;
import com.perl5.lang.mojolicious.psi.MojoliciousFileViewProviderFactory;
import com.perl5.lang.perl.extensions.parser.PerlParserExtension;
import com.perl5.lang.perl.parser.MojoParserExtension;
import org.jetbrains.annotations.NotNull;

public abstract class MojoliciousParserTestBase extends PerlParserTestBase {
  public MojoliciousParserTestBase() {
  }

  public MojoliciousParserTestBase(@NotNull String dataPath,
                                   @NotNull String fileExt,
                                   @NotNull ParserDefinition... definitions) {
    super(dataPath, fileExt, definitions);
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    LanguageFileViewProviders.INSTANCE.addExplicitExtension(MojoliciousLanguage.INSTANCE, new MojoliciousFileViewProviderFactory());
  }

  @Override
  protected void registerParserExtensions() {
    super.registerParserExtensions();
    registerExtension(PerlParserExtension.EP_NAME, new MojoParserExtension());
  }
}
