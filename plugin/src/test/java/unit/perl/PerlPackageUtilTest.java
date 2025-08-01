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

package unit.perl;

import base.PerlLightTestCase;
import com.intellij.testFramework.Parameterized;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;

import java.util.Arrays;
import java.util.Collections;

@SuppressWarnings("Junit4RunWithInspection")
@RunWith(Parameterized.class)
public class PerlPackageUtilTest extends PerlLightTestCase {
  @Parameter public @Nullable String myFqn;
  @Parameter(1) public @Nullable String myPackageName;
  @Parameter(2) public @Nullable String mySubName;

  @Test
  public void test() {
    var result = PerlPackageUtilCore.splitNames(myFqn);
    if (myFqn == null || myFqn.isEmpty()) {
      assertNull(result);
    }
    else {
      assertNotNull(result);
      assertEquals(myPackageName, result.getFirst());
      assertEquals(mySubName, result.getSecond());
    }
  }

  @org.junit.runners.Parameterized.Parameters(name = "{0}")
  public static Iterable<Object[]> fakeData() {
    return Collections.emptyList();
  }

  @Parameterized.Parameters(name = "{0}")
  public static Iterable<Object[]> realData(Class<?> clazz) {
    return Arrays.asList(new Object[][]{
      {null, null, null},
      {"", null, null},
      {"::subname", "main", "subname"},
      {"Foo::", "Foo", null},
      {"Foo::subname", "Foo", "subname"},
    });
  }
}
