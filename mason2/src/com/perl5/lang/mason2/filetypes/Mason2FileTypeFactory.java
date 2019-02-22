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

package com.perl5.lang.mason2.filetypes;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.perl5.lang.perl.fileTypes.PerlFileTypeFactoryBase;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 20.12.2015.
 */
public class Mason2FileTypeFactory extends PerlFileTypeFactoryBase {
  public static final String TOP_LEVEL_COMPONENT_EXTENSION = "mc";
  public static final String INTERNAL_COMPONENT_EXTENSION = "mi";
  public static final String PURE_PERL_COMPONENT_EXTENSION = "mp";

  @Override
  protected void createFileTypesInner(@NotNull FileTypeConsumer fileTypeConsumer) {
    fileTypeConsumer.consume(MasonTopLevelComponentFileType.INSTANCE, TOP_LEVEL_COMPONENT_EXTENSION);
    fileTypeConsumer.consume(MasonInternalComponentFileType.INSTANCE, INTERNAL_COMPONENT_EXTENSION);
    fileTypeConsumer.consume(MasonPurePerlComponentFileType.INSTANCE, PURE_PERL_COMPONENT_EXTENSION);
  }
}
