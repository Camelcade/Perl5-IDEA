/*
 * Copyright 2015-2023 Alexandr Evstigneev
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
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class HTMLMasonBlockVariablesResolveTest extends HTMLMasonVariableResolveTestCase {
  private final boolean myAddCustomVariable;

  public HTMLMasonBlockVariablesResolveTest(@NotNull String baseDataPath, boolean addCustomVariable) {
    super(baseDataPath);
    myAddCustomVariable = addCustomVariable;
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    if (myAddCustomVariable) {
      HTMLMasonSettings settings = HTMLMasonSettings.getInstance(getProject());
      settings.globalVariables.add(new VariableDescription("$product", "Foo::Bar"));
      settings.settingsUpdated();
      addPerlTearDownListener(() -> {
        settings.globalVariables.remove(new VariableDescription("$product", "Foo::Bar"));
        settings.settingsUpdated();
      });
    }
  }

  @Test
  public void testSecondEntry() {
    doTestResolve();
  }

  @Test
  public void testFromSecondEntryBackwards() {
    doTestResolve();
  }

  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
      {"filtered_block", false},
      {"init", false},
      {"cleanup", false},
      {"args_method", false},
      {"def", false},
      {"method", false},
      {"once", false},
      {"args_file", false},
      {"init_def", false},
      {"shared", false},
      {"filter", false},
      {"args_def", true},
      {"implicit", true},
    });
  }
}
