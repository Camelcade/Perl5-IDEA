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

package com.perl5.lang.perl.fileTypes;

import com.intellij.openapi.fileTypes.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.perl5.lang.perl.idea.editor.notification.PerlAssociationEditorNotification.registerFileType;

public abstract class PerlFileTypeFactoryBase extends FileTypeFactory {

  protected abstract void createFileTypesInner(@NotNull FileTypeConsumer consumer);

  @Override
  public final void createFileTypes(@NotNull FileTypeConsumer consumer) {
    createFileTypesInner(new FileTypeConsumer() {
      @Override
      public void consume(@NotNull FileType fileType) {
        consumer.consume(fileType);
        registerFileType(fileType, parse(fileType.getDefaultExtension()));
      }

      @Override
      public void consume(@NotNull FileType fileType, String extensions) {
        consumer.consume(fileType, extensions);
        registerFileType(fileType, parse(extensions));
      }

      @Override
      public void consume(@NotNull FileType fileType, @NotNull FileNameMatcher... matchers) {
        consumer.consume(fileType, matchers);
        registerFileType(fileType, new ArrayList<>(Arrays.asList(matchers)));
      }

      @Nullable
      @Override
      public FileType getStandardFileTypeByName(@NotNull String name) {
        return consumer.getStandardFileTypeByName(name);
      }
    });
  }

  /**
   * copy-paste from {@link com.intellij.openapi.fileTypes.impl.FileTypeManagerImpl#parse}
   */
  @NotNull
  private static List<FileNameMatcher> parse(@Nullable String semicolonDelimited) {
    if (semicolonDelimited == null) {
      return Collections.emptyList();
    }

    StringTokenizer tokenizer = new StringTokenizer(semicolonDelimited, FileTypeConsumer.EXTENSION_DELIMITER, false);
    ArrayList<FileNameMatcher> list = new ArrayList<>(semicolonDelimited.length() / "py;".length());
    while (tokenizer.hasMoreTokens()) {
      list.add(new ExtensionFileNameMatcher(tokenizer.nextToken().trim()));
    }
    return list;
  }
}
