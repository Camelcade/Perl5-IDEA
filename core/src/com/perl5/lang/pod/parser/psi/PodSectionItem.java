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

package com.perl5.lang.pod.parser.psi;

import com.perl5.lang.pod.lexer.PodElementTypes;

/**
 * Created by hurricup on 26.03.2016.
 */
public interface PodSectionItem extends PodTitledSection, PodElementTypes {
  /**
   * Checks if this item defines bullteted list, as described in http://perldoc.perl.org/perlpodspec.html#About-%3dover...%3dback-Regions
   *
   * @return true if list should be bulleted
   */
  boolean isBulleted();

  /**
   * Check if this item container is bulleted
   *
   * @return true if yep
   */
  boolean isContainerBulleted();
}
