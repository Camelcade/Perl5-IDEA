/*
 * Copyright 2015-2018 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.sdk.host.local;

import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.projectRoots.impl.ProjectJdkImpl;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.sdk.PerlHandlerBean;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.idea.sdk.host.PerlHostHandler;
import com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.perl5.lang.perl.idea.sdk.host.os.PerlOsHandlers.*;

public class PerlLocalHostHandler extends PerlHostHandler<PerlLocalHostData, PerlLocalHostHandler> {

  public PerlLocalHostHandler(@NotNull PerlHandlerBean bean) {
    super(bean);
  }

  @NotNull
  @Override
  public String getMenuItemTitle() {
    return PerlBundle.message("perl.host.handler.localhost.menu.title");
  }

  @NotNull
  @Override
  public PerlLocalHostData createData() {
    return new PerlLocalHostData(this);
  }

  @Override
  public void chooseFileInteractively(@NotNull String dialogTitle,
                                      @Nullable Path defaultPath,
                                      @NotNull Predicate<String> nameValidator,
                                      @NotNull Function<String, String> pathValidator,
                                      @NotNull BiConsumer<String, PerlHostData<?, ?>> selectionConsumer) {
    final FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, false, false, false, false) {
      @Override
      public boolean isFileVisible(VirtualFile file, boolean showHiddenFiles) {
        return super.isFileVisible(file, showHiddenFiles) && (file.isDirectory() || nameValidator.test(file.getName()));
      }

      @Override
      public void validateSelectedFiles(VirtualFile[] files) throws Exception {
        if (files.length != 0) {
          String errorMessage = pathValidator.apply(files[0].getPath());
          if (StringUtil.isNotEmpty(errorMessage)) {
            throw new Exception(errorMessage);
          }
        }
      }
    };
    descriptor.setTitle(dialogTitle);
    VirtualFile fileToStart = defaultPath == null ? null : VfsUtil.findFile(defaultPath, false);
    FileChooser.chooseFiles(descriptor, null, fileToStart, chosen -> {
      String selectedPath = chosen.get(0).getPath();
      if (StringUtil.isEmpty(pathValidator.apply(selectedPath))) {
        selectionConsumer.accept(selectedPath, createData());
      }
    });
  }

  @Nullable
  @Override
  public PerlLocalHostData createData(@Nullable ProjectJdkImpl sdk) {
    return sdk == null ? null : createData();
  }

  @Override
  public boolean isApplicable() {
    return true;
  }

  @NotNull
  @Override
  public PerlOsHandler getOsHandler() {
    return SystemInfo.isWin10OrNewer ? WINDOWS10 :
           SystemInfo.isWindows ? WINDOWS :
           SystemInfo.isMac ? MACOS :
           SystemInfo.isLinux ? LINUX :
           SystemInfo.isFreeBSD ? FREEBSD :
           SystemInfo.isSolaris ? SOLARIS :
           UNIX;
  }
}
