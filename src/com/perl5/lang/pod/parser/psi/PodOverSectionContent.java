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

package com.perl5.lang.pod.parser.psi;

import com.intellij.psi.PsiElement;
import com.perl5.lang.pod.lexer.PodElementTypes;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 26.03.2016.
 */
public interface PodOverSectionContent extends PsiElement, PodElementTypes {
  /**
   * Checks if first element is bulleted, according to http://perldoc.perl.org/perlpodspec.html#About-%3dover...%3dback-Regions
   *
   * @return true if list should be bulleted
   */
  boolean isBulleted();

  /**
   * Returns first list item
   *
   * @return item or null if not any
   */
  @Nullable
  PodSectionItem getFirstItem();
}
