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

package base;

import categories.Light;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;
import com.intellij.util.lang.PathClassLoader;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.regex.Pattern;
import java.util.stream.Stream;

@SuppressWarnings("JUnitMixedFramework")
@Category(Light.class)
@RunWith(Parameterized.class)
public abstract class PerlInstrumentationTestCase extends BasePlatformTestCase {

  protected static final Pattern PLUGIN_PATTERN_STRING = Pattern.compile("lib/plugin-.+?\\.jar!");
  protected static final Pattern EMBEDDED_PATTERN_STRING = Pattern.compile("lib/lang\\.embedded-.+?\\.jar");
  protected static final Pattern MOJO_PATTERN_STRING = Pattern.compile("lib/lang\\.mojo-.+?\\.jar!");
  protected static final Pattern TT2_PATTERN_STRING = Pattern.compile("lib/lang\\.tt2-.+?\\.jar!");
  protected static final Pattern MASON_FRAMEWORK_PATTERN_STRING = Pattern.compile("lib/lang.mason.framework-.+?\\.jar!");
  protected static final Pattern MASON_PATTERN_STRING = Pattern.compile("lib/lang.mason.htmlmason-.+?\\.jar!");
  protected static final Pattern MASON2_PATTERN_STRING = Pattern.compile("lib/lang.mason.mason2-.+?\\.jar!");

  @Parameterized.Parameter() public @NotNull String myName;

  @Parameterized.Parameter(1) public @NotNull Class<?> myClass;

  @Parameterized.Parameter(2) public @NotNull Pattern myClassPathPattern;

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
