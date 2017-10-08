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

package com.perl5.lang.htmlmason.filetypes;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.perl.fileTypes.PerlFileTypeProvider;
import com.perl5.lang.perl.fileTypes.PerlFileTypeService;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class HTMLMasonFileTypeProvider implements PerlFileTypeProvider {

  @Override
  public void addDescriptors(@NotNull Project project, Consumer<PerlFileTypeService.RootDescriptor> descriptorConsumer) {
    HTMLMasonSettings settings = HTMLMasonSettings.getInstance(project);
    for (VirtualFile root : settings.getComponentsRoots()) {
      descriptorConsumer.accept(PerlFileTypeService.RootDescriptor.create(root, virtualFile -> {
        String virtualFileName = virtualFile.getName();
        if (StringUtil.equals(settings.autoHandlerName, virtualFileName) ||
            StringUtil.equals(settings.defaultHandlerName, virtualFileName)) {
          return HTMLMasonFileType.INSTANCE;
        }
        return null;
      }));
    }
  }
}
