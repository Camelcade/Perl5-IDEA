/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi;

import com.intellij.openapi.util.NlsSafe;
import com.perl5.lang.perl.psi.properties.PerlPackageMember;
import com.perl5.lang.perl.util.PerlPackageUtilCore;

public interface PerlCallable extends PerlDeprecatable, PerlPackageMember {
  /**
   * Returns canonical name PackageName::SubName
   *
   * @return name
   */
  @Override
  default @NlsSafe String getCanonicalName() {
    String packageName = getNamespaceName();
    if (packageName == null) {
      return null;
    }

    return PerlPackageUtilCore.join(packageName, getCallableName());
  }

  /**
   * @return a callable name. Sub or typeglob.
   */
  @NlsSafe String getCallableName();
}
