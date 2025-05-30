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

package com.perl5.lang.perl.buildSystem;

import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.module.Module;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PerlBuildSystemHandler {
  ExtensionPointName<PerlBuildSystemHandler> EP_NAME = ExtensionPointName.create("com.perl5.buildSystemHandler");

  /**
   * @return true iff {@code module}s tests are handled by this build system, e.g. running via make or smth.
   */
  boolean handlesTests(@NotNull Module module);

  /**
   * @return build system handler that {@link #handlesTests(Module)} this module. First wins.
   */
  @Contract("null->null")
  static @Nullable PerlBuildSystemHandler getTestsHandler(@Nullable Module module) {
    if( module == null){
      return null;
    }
    for (PerlBuildSystemHandler buildSystemHandler : EP_NAME.getExtensionList()) {
      if( buildSystemHandler.handlesTests(module)){
        return buildSystemHandler;
      }
    }
    return null;
  }
}
