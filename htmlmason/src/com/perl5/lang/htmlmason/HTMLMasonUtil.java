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

package com.perl5.lang.htmlmason;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonArgsBlock;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonCompositeElement;
import com.perl5.lang.htmlmason.parser.psi.HTMLMasonParametrizedEntity;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.util.PerlSubUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class HTMLMasonUtil {
  @Nullable
  public static VirtualFile getComponentRoot(@NotNull Project project, @Nullable VirtualFile file) {
    return MasonCoreUtil.getComponentRoot(HTMLMasonSettings.getInstance(project), file);
  }

  public static List<PerlSubArgument> getArgumentsList(HTMLMasonParametrizedEntity entity) {
    List<PerlSubArgument> result = new ArrayList<>();

    for (HTMLMasonCompositeElement argsBlock : entity.getArgsBlocks()) {
      result.addAll(((HTMLMasonArgsBlock)argsBlock).getArgumentsList());
    }

    return result;
  }

  public static String getArgumentsListAsString(HTMLMasonParametrizedEntity entity) {
    return PerlSubUtil.getArgumentsListAsString(getArgumentsList(entity));
  }
}
