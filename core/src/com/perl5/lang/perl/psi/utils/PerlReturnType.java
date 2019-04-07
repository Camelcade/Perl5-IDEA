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

package com.perl5.lang.perl.psi.utils;

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.lang.perl.psi.stubs.PerlStubSerializationUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 03.06.2015.
 * @deprecated use {@link com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue}
 */
@Deprecated
public enum PerlReturnType {
  VALUE,        // default
  REF,        // Package::Name	NYI
  ARRAY,        // @Package::Name	NYI
  HASH,        // %Package::Name	NYI
  ARRAY_REF,    // [Package::Name]
  HASH_REF,    // {Package::Name}
  CODE_REF;    // &				NYI

  public void serialize(@NotNull StubOutputStream dataStream) throws IOException {
    dataStream.writeName(toString());
  }

  public static PerlReturnType deserialize(@NotNull StubInputStream dataStream) throws IOException {
    return PerlReturnType.valueOf(PerlStubSerializationUtil.readString(dataStream));
  }

}
