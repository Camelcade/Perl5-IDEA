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

package com.perl5.lang.perl.fileTypes;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.PsiElement;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class PerlFileTypeTest extends PurePerlFileType {
  public static final FileType INSTANCE = new PerlFileTypeTest();
  public static final String EXTENSION = "t";

  @Override
  public @NotNull String getName() {
    return "Perl5 Test";
  }

  @Override
  public @NotNull String getDescription() {
    return PerlBundle.message("label.perl5.test");
  }

  @Override
  public @NotNull String getDefaultExtension() {
    return EXTENSION;
  }

  @Override
  public @Nullable Icon getIcon() {
    return PerlIcons.TEST_FILE;
  }

  public static boolean isMy(@Nullable PsiElement psiElement) {
    return isMyFile(psiElement, INSTANCE);
  }
}
