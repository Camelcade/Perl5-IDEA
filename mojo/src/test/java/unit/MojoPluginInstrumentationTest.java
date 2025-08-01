/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package unit;

import base.PerlInstrumentationTestCase;
import categories.CategoriesFilter;
import com.perl5.lang.mojolicious.MojoliciousParserDefinition;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class MojoPluginInstrumentationTest extends PerlInstrumentationTestCase {
  @Parameterized.Parameters(name = "{0}")
  public static Collection<Object[]> data() {
    return !CategoriesFilter.shouldRun(MojoPluginInstrumentationTest.class) ? Collections.emptyList() : Arrays.asList(new Object[][]{
      PARSER_DEFINITION_CLASS_DATA,
      {"mojo.common", MojoliciousParserDefinition.class, MOJO_PATTERN_STRING},
    });
  }
}
