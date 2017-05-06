/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.parser.trycatch.idea.liveTemplates;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 17.04.2016.
 */
public class TryCatchLiveTemplatesProvider implements DefaultLiveTemplatesProvider {
  public static final String[] TEMPLATES = new String[]{
    "/liveTemplates/Perl5TryCatch"
  };

  @Override
  public String[] getDefaultLiveTemplateFiles() {
    return TEMPLATES;
  }

  @Nullable
  @Override
  public String[] getHiddenLiveTemplateFiles() {
    return null;
  }
}
