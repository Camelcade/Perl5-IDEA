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

package com.perl5.lang.perl.idea.filetemplates;

import com.intellij.ide.fileTemplates.DefaultTemplatePropertiesProvider;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Properties;

/**
 * Created by hurricup on 03.09.2015.
 */
public class PerlFilePropertiesProvider implements DefaultTemplatePropertiesProvider {
  @Override
  public void fillProperties(@NotNull PsiDirectory directory, @NotNull Properties props) {
    VirtualFile directoryFile = directory.getVirtualFile();
    VirtualFile newInnermostRoot = PerlUtil.getFileClassRoot(directory.getProject(), directoryFile);

    if (newInnermostRoot != null) {
      String newRelativePath = VfsUtil.getRelativePath(directoryFile, newInnermostRoot);
      props.put("PERL_PACKAGE_PREFIX",
                newRelativePath == null || newRelativePath.isEmpty() ? "" : PerlPackageUtil.getPackageNameByPath(newRelativePath));
    }
    else {
      props.put("PERL_PACKAGE_PREFIX", "");
    }
  }
}

