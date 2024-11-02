/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

import java.util.regex.Pattern;
import java.util.stream.Stream;

@SuppressWarnings("JUnitMixedFramework")
@Category(Light.class)
public abstract class PerlInstrumentationTestCase extends BasePlatformTestCase {

  protected static final String PLUGIN_PATTERN_STRING = "/plugin/build/libs/plugin-.+?\\.jar!";
  // this is wrong, should be build/libs/lang\\.embedded-.+?\\.jar!
  protected static final String EMBEDDED_PATTERN_STRING = "/embedded/core/build/libs/core-.+?\\.jar";
  protected static final String MOJO_PATTERN_STRING = "/mojo/core/build/libs/core-.+?\\.jar!";
  protected static final String TT2_PATTERN_STRING = "/tt2/core/build/libs/core-.+?\\.jar!";
  protected static final String MASON_FRAMEWORK_PATTERN_STRING = "/mason/framework/build/libs/lang\\.mason\\.framework-.+?\\.jar!";
  protected static final String MASON_PATTERN_STRING = "/mason/htmlmason/core/build/libs/core-.+?\\.jar!";
  protected static final String MASON2_PATTERN_STRING = "/mason/mason2/core/build/libs/core-.+?\\.jar!";

  private final @NotNull Class<?> myClass;

  private final @NotNull Pattern myClassPathPattern;

  protected PerlInstrumentationTestCase(@NotNull Class<?> aClass, @NotNull String patternString) {
    myClass = aClass;
    myClassPathPattern = Pattern.compile(patternString);
  }

  @Test
  public void testDependencyInstrumentation() {
    var classLoader = myClass.getClassLoader();
    assertInstanceOf(classLoader, PathClassLoader.class);
    var pathClassLoader = (PathClassLoader)classLoader;
    assertTrue(
      "Class " + myClass + " does not look instrumented, classpaths: " + pathClassLoader.getClassPath().getBaseUrls(),
      Stream.of(myClass.getDeclaredMethods()).anyMatch(m -> m.getName().startsWith("$$$reportNull$$$")));
  }

  @Test
  public void testProperClassSource() {
    var className = myClass.getName();
    var classPath = myClass.getResource("/" + className.replace('.', '/') + ".class").getPath();
    if (!myClassPathPattern.matcher(classPath).find()) {
      fail("Classpath for the " + className + " is expected to match " + myClassPathPattern + ", but was " + classPath);
    }
  }
}
