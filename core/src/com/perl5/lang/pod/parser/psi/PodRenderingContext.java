/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

public class PodRenderingContext {
  private boolean myIsHtmlSafe;

  public PodRenderingContext() {
    this(false);
  }

  public PodRenderingContext(boolean isHtmlSafe) {
    myIsHtmlSafe = isHtmlSafe;
  }

  /**
   * @return true iff content suppoosed to be HTML text. In html formatter. And should be rendered without escaping
   */
  public boolean isHtmlSafe() {
    return myIsHtmlSafe;
  }

  /**
   * @see #isHtmlSafe()
   */
  public void setHtmlSafe(boolean safe) {
    myIsHtmlSafe = safe;
  }
}
