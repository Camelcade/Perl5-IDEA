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

package com.perl5.lang.perl.idea;


public interface PerlPathMacros {
  String PERL5_APP_SETTINGS_FILE = "perl5.xml";

  // this file supposed to contain machine-dependent settings and should not be shared with VCS
  String PERL5_PROJECT_SETTINGS_FILE = "perl5local.xml";

  // this file supposed to be shared with VCS, should contain project-dependent and machine-independent settings
  String PERL5_PROJECT_SHARED_SETTINGS_FILE = "perl5shared.xml";

  String APP_OTHER_SETTINGS_FILE = "other.xml";
  String APP_CODEINSIGHT_SETTINGS_FILE = "editor.codeinsight.xml";
}
