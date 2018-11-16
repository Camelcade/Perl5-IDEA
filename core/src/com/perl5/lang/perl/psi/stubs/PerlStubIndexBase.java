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

package com.perl5.lang.perl.psi.stubs;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StringStubIndexExtension;

/**
 * Created by hurricup on 13.11.2016.
 */
public abstract class PerlStubIndexBase<Psi extends PsiElement> extends StringStubIndexExtension<Psi> {
  private final static int VERSION = 6;

  @Override
  public int getVersion() {
    return super.getVersion() + VERSION;
  }
}
