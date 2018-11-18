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

package resolve;

import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.mason2.idea.configuration.VariableDescription;

/**
 * Created by hurricup on 13.03.2016.
 */
public class HTMLMasonArgsDefVariablesResolveTest extends HTMLMasonBlockVariablesResolveTestCase {
  protected String getTestDataPath() {
    return "testData/resolve/args_def";
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    HTMLMasonSettings settings = HTMLMasonSettings.getInstance(getProject());
    settings.globalVariables.add(new VariableDescription("$product", "Foo::Bar"));
    settings.settingsUpdated();
  }

  @Override
  protected void tearDown() throws Exception {
    HTMLMasonSettings settings = HTMLMasonSettings.getInstance(getProject());
    settings.globalVariables.remove(new VariableDescription("$product", "Foo::Bar"));
    settings.settingsUpdated();
    super.tearDown();
  }
}
