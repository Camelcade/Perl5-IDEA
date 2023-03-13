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

package base;

import categories.Light;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.util.lang.PathClassLoader;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.stream.Stream;

@Category(Light.class)
public abstract class PerlInstrumentationTestCase extends BasePlatformTestCase {

  private final Class<?> myClass;


  protected PerlInstrumentationTestCase(@NotNull Class<?> aClass) {
    myClass = aClass;
  }

  @SuppressWarnings("JUnitMixedFramework")
  @Test
  public void testDependencyInstrumentation() {
    var classLoader = myClass.getClassLoader();
    assertInstanceOf(classLoader, PathClassLoader.class);
    var pathClassLoader = (PathClassLoader)classLoader;
    assertTrue(
      "Class " + myClass + " does not look instrumented, classpaths: " + pathClassLoader.getClassPath().getBaseUrls(),
      Stream.of(myClass.getDeclaredMethods()).anyMatch(m -> m.getName().startsWith("$$$reportNull$$$")));
  }
}
