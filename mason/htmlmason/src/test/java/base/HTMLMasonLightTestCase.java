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
import com.perl5.lang.htmlmason.filetypes.HTMLMasonFileType;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSourceRootType;
import org.jetbrains.annotations.NotNull;

public abstract class HTMLMasonLightTestCase extends PerlLightTestCase {
  @Override
  public String getFileExtension() {
    return HTMLMasonFileType.DEFAULT_EXTENSION;
  }

  @Override
  protected @NotNull String getTestLibPath() {
    return TEST_LIB_PATH_FROM_NESTED;
  }

  protected void markAsComponentRoot(@NotNull VirtualFile componentRoot) {
    markAsPerlRoot(componentRoot, true, HTMLMasonSourceRootType.INSTANCE);
  }
}
