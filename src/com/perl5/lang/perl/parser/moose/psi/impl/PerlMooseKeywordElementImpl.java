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

package com.perl5.lang.perl.parser.moose.psi.impl;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.parser.moose.psi.PerlMooseKeywordElement;
import com.perl5.lang.perl.psi.impl.PerlSubNameElementImpl;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 22.01.2016.
 */
public class PerlMooseKeywordElementImpl extends PerlSubNameElementImpl implements PerlMooseKeywordElement {
  public PerlMooseKeywordElementImpl(@NotNull IElementType type, CharSequence text) {
    super(type, text);
  }

  @Nullable
  @Override
  public String getPackageName() {
    return PerlPackageUtil.getContextPackageName(this);
  }
}
