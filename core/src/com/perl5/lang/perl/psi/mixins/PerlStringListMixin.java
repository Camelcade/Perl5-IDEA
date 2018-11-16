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

package com.perl5.lang.perl.psi.mixins;

import com.intellij.lang.ASTNode;
import com.intellij.psi.ElementManipulators;
import com.perl5.lang.perl.psi.PerlStringList;
import com.perl5.lang.perl.psi.impl.PerlCompositeElementImpl;
import com.perl5.lang.perl.psi.utils.PerlPsiUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 24.05.2016.
 */
public class PerlStringListMixin extends PerlCompositeElementImpl implements PerlStringList {
  public PerlStringListMixin(@NotNull ASTNode node) {
    super(node);
  }

  @NotNull
  @Override
  public List<String> getStringContents() {
    List<String> result = new ArrayList<>();
    PerlPsiUtil.processStringElements(getFirstChild(), stringContent -> {
      result.add(ElementManipulators.getValueText(stringContent));
      return true;
    });
    return result;
  }
}
