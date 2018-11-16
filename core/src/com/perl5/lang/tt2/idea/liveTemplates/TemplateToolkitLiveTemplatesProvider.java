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

package com.perl5.lang.tt2.idea.liveTemplates;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 13.06.2016.
 */
public class TemplateToolkitLiveTemplatesProvider implements DefaultLiveTemplatesProvider {
  public static final String[] TEMPLATES = new String[]{
    "/liveTemplates/TemplateToolkit2",
    "/liveTemplates/TemplateToolkit2Postfix",
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
