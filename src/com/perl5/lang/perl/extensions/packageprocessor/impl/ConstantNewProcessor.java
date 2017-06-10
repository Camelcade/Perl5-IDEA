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

package com.perl5.lang.perl.extensions.packageprocessor.impl;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.parser.GeneratedParserUtilBase;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPragmaProcessorBase;
import com.perl5.lang.perl.parser.PerlParserImpl;
import com.perl5.lang.perl.parser.PerlParserUtil;
import com.perl5.lang.perl.parser.builder.PerlBuilder;
import com.perl5.lang.perl.psi.stubs.PerlStubElementTypes;
import org.jetbrains.annotations.NotNull;

public class ConstantNewProcessor extends PerlPragmaProcessorBase {
  @Override
  public boolean parseUseParameters(@NotNull PerlBuilder b, int l, @NotNull GeneratedParserUtilBase.Parser defaultParser) {
    PerlParserUtil.passPackageAndVersion(b, l);
    PsiBuilder.Marker m = b.mark();
    if (PerlParserImpl.expr(b, l, -1)) {
      m.done(PerlStubElementTypes.CONSTANT_WRAPPER);
      return true;
    }
    m.rollbackTo();
    return false;
  }
}
