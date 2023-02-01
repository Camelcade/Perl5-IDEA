/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package base;

import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.mason2.idea.configuration.Mason2SourceRootType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static com.perl5.lang.mason2.filetypes.MasonInternalComponentFileType.INTERNAL_COMPONENT_EXTENSION;
import static com.perl5.lang.mason2.filetypes.MasonTopLevelComponentFileType.TOP_LEVEL_COMPONENT_EXTENSION;

public abstract class Mason2LightTestCase extends PerlLightTestCaseBase {

  @Override
  protected @NotNull String getTestLibPath() {
    return super.getTestLibPathFromNested();
  }

  protected void markAsComponentRoot(@NotNull VirtualFile componentRoot) {
    markAsPerlRoot(componentRoot, true, Mason2SourceRootType.INSTANCE);
  }

  public static @NotNull List<Object[]> componentsExtensionsData() {
    return Arrays.asList(new Object[][]{
      {INTERNAL_COMPONENT_EXTENSION},
      {TOP_LEVEL_COMPONENT_EXTENSION},
    });
  }
}
