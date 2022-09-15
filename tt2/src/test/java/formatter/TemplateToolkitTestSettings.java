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

package formatter;

import com.intellij.openapi.project.Project;
import com.perl5.lang.tt2.idea.settings.TemplateToolkitSettings;


public class TemplateToolkitTestSettings {
  private final Project myProject;

  private String myOpenTagBackup;
  private String myCloseTagBackup;
  private String myOutlineTagBackup;
  private boolean myEnableAnyCase;

  public TemplateToolkitTestSettings(Project project) {
    myProject = project;
  }

  public void setUp() {
    TemplateToolkitSettings templateToolkitSettings = TemplateToolkitSettings.getInstance(myProject);
    myOpenTagBackup = templateToolkitSettings.START_TAG;
    myCloseTagBackup = templateToolkitSettings.END_TAG;
    myOutlineTagBackup = templateToolkitSettings.OUTLINE_TAG;
    myEnableAnyCase = templateToolkitSettings.ENABLE_ANYCASE;

    templateToolkitSettings.START_TAG = TemplateToolkitSettings.DEFAULT_START_TAG;
    templateToolkitSettings.END_TAG = TemplateToolkitSettings.DEFAULT_END_TAG;
    templateToolkitSettings.OUTLINE_TAG = TemplateToolkitSettings.DEFAULT_OUTLINE_TAG;
    templateToolkitSettings.ENABLE_ANYCASE = false;
  }

  public void tearDown() {
    TemplateToolkitSettings templateToolkitSettings = TemplateToolkitSettings.getInstance(myProject);
    templateToolkitSettings.START_TAG = myOpenTagBackup;
    templateToolkitSettings.END_TAG = myCloseTagBackup;
    templateToolkitSettings.OUTLINE_TAG = myOutlineTagBackup;
    templateToolkitSettings.ENABLE_ANYCASE = myEnableAnyCase;
  }
}
