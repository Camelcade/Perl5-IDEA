/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.modules;

import com.intellij.ide.util.projectWizard.EmptyModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PerlModuleType extends ModuleType<ModuleBuilder> {
  public static final String PERL_MODULE_TYPE_ID = "PERL5_MODULE";

  public PerlModuleType() {
    super(PERL_MODULE_TYPE_ID);
  }

  @Override
  public @NotNull ModuleBuilder createModuleBuilder() {
    return new EmptyModuleBuilder() {
      @Override
      public boolean isAvailable() {
        return false;
      }

      @Override
      public ModuleType<?> getModuleType() {
        return PerlModuleType.this;
      }
    };
  }

  @Override
  public @NotNull String getName() {
    return PerlBundle.message("perl.module.name");
  }

  @Override
  public @NotNull String getDescription() {
    return PerlBundle.message("perl.module.description");
  }

  @Override
  public @NotNull Icon getNodeIcon(@Deprecated boolean isOpened) {
    return PerlIcons.PERL_LANGUAGE_ICON;
  }

  public static PerlModuleType getInstance() {
    return (PerlModuleType)ModuleTypeManager.getInstance().findByID(PERL_MODULE_TYPE_ID);
  }
}
