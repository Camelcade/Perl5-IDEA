/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.folding;

import com.intellij.openapi.application.ApplicationManager;


public class PerlFoldingSettings {
  public boolean COLLAPSE_COMMENTS = true;
  public boolean COLLAPSE_CONSTANT_BLOCKS = false;
  public boolean COLLAPSE_ANON_ARRAYS = false;
  public boolean COLLAPSE_ANON_HASHES = false;
  public boolean COLLAPSE_PARENTHESISED = false;
  public boolean COLLAPSE_HEREDOCS = false;
  public boolean COLLAPSE_TEMPLATES = false;
  public boolean COLLAPSE_QW = false;
  public boolean COLLAPSE_CHAR_SUBSTITUTIONS = true;

  public static PerlFoldingSettings getInstance() {
    return ApplicationManager.getApplication().getService(PerlFoldingSettings.class);
  }
}
