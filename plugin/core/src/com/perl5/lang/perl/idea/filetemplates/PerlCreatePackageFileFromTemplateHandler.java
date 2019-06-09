/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

import com.intellij.ide.fileTemplates.FileTemplate;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;


public class PerlCreatePackageFileFromTemplateHandler extends PerlCreateFileFromTemplateHandler {
  @Override
  public boolean handlesTemplate(@NotNull FileTemplate template) {
    return template.isTemplateOfType(PerlFileTypePackage.INSTANCE);
  }

  @Override
  public void prepareProperties(@NotNull Map<String, Object> props) {
    String fileName = (String)props.get("NAME");
    if (fileName.endsWith(".pm")) {
      fileName = fileName.replace(".pm", "");
    }

    String packagePrefix = (String)props.get("PERL_PACKAGE_PREFIX");

    assert packagePrefix != null;

    if (packagePrefix.isEmpty()) {
      props.put("PERL_PACKAGE_NAME", fileName);
    }
    else {
      props.put("PERL_PACKAGE_NAME", packagePrefix + PerlPackageUtil.PACKAGE_SEPARATOR + fileName);
    }

    super.prepareProperties(props);
  }
}
