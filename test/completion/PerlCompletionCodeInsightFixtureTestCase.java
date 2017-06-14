/*
 * Copyright 2015-2017 Alexandr Evstigneev
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

package completion;

import base.PerlLightCodeInsightFixtureTestCase;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.perl5.lang.perl.idea.intellilang.AbstractPerlLanguageInjector;
import com.perl5.lang.perl.util.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by hurricup on 04.03.2016.
 */
public abstract class PerlCompletionCodeInsightFixtureTestCase extends PerlLightCodeInsightFixtureTestCase {
  protected static List<String> LIBRARY_PACKAGES = Arrays.asList(
    "MyTest::Some::Package",
    "MyTest::Something",
    "MyTest::Constants",
    "MyTest::Exceptions",
    // these has no files
    "Library::Exception",
    "LibraryException",
    "Library::Exception2",
    "LibraryException3"
  );

  protected static List<String> LIBRARY_PM_FILES = Arrays.asList(
    "MyTest::Some::Package",
    "MyTest::Something",
    "MyTest::Constants",
    "MyTest::Exceptions"
  );

  protected static List<String> LIBRARY_GLOBAL_ARRAYS = Arrays.asList(
    "MyTest::Some::Package::EXPORT",
    "MyTest::Some::Package::EXPORT_OK"
  );

  protected static List<String> BUILT_IN_VERSIONS = Arrays.asList(
    "v5.10", "v5.12", "v5.14", "v5.16", "v5.18", "v5.20", "v5.22",
    "v5.11", "v5.13", "v5.15", "v5.17", "v5.19", "v5.9.5"
  );

  protected static List<String> REF_TYPES =
    Arrays.asList("ARRAY", "CODE", "FORMAT", "GLOB", "HASH", "IO", "LVALUE", "REF", "Regexp", "SCALAR", "VSTRING");

  protected static List<String> BUILT_IN_SCALARS = new ArrayList<>(PerlScalarUtil.BUILT_IN);
  protected static List<String> BUILT_IN_ARRAYS = new ArrayList<>(PerlArrayUtil.BUILT_IN);
  protected static List<String> BUILT_IN_HASHES = new ArrayList<>(PerlHashUtil.BUILT_IN);
  protected static List<String> BUILT_IN_GLOBS = new ArrayList<>(PerlGlobUtil.BUILT_IN);

  protected static List<String> SCALAR_LOOKUPS = mergeLists(BUILT_IN_SCALARS, BUILT_IN_ARRAYS, BUILT_IN_HASHES, LIBRARY_GLOBAL_ARRAYS);
  protected static List<String> ARRAY_LOOKUPS = mergeLists(BUILT_IN_ARRAYS, BUILT_IN_HASHES, LIBRARY_GLOBAL_ARRAYS);
  protected static List<String> HASH_LOOKUPS = mergeLists(BUILT_IN_HASHES);

  protected static List<String> BUILT_IN_SUBS = new ArrayList<>(PerlBuiltInSubs.BUILT_IN);

  protected static List<String> PACKAGES_LOOKUPS = new ArrayList<>();

  static {
    for (String packageName : LIBRARY_PACKAGES) {
      PACKAGES_LOOKUPS.add(packageName + "::");
      PACKAGES_LOOKUPS.add(packageName + "->");
    }
  }

  protected List<String> getLanguageMarkers() {
    ArrayList<String> languageMarkers = new ArrayList<>(AbstractPerlLanguageInjector.LANGUAGE_MAP.keySet());
    assert !languageMarkers.isEmpty();
    return languageMarkers;
  }

  @Override
  public void initWithFileContent(String filename, String extension, String content) throws IOException {
    super.initWithFileContent(filename, extension, content);
    CodeInsightTestFixtureImpl.ensureIndexesUpToDate(getProject());
    myFixture.complete(CompletionType.BASIC, 1);
  }

  public void assertCompletionIs(String... pattern) {
    assertCompletionIs(Arrays.asList(pattern));
  }

  @SafeVarargs
  public final void assertCompletionIs(List<String>... expected) {
    initWithFileSmart();
    assertLookupIs(mergeLists(expected));
  }

  protected void doTest() {
    doTest(Collections.emptyList());
  }

  @SafeVarargs
  protected final void doTest(List<String>... result) {
    assertCompletionIs(result);
  }

  protected void doTestContains(String... args) {
    initWithFileSmart();
    assertLookupContains(Arrays.asList(args));
  }

  protected void doTest(String... result) {
    doTest(Arrays.asList(result));
  }

  @SafeVarargs
  protected static List<String> mergeLists(List<String>... sources) {
    List<String> resultList = new ArrayList<>();
    for (List<String> strings : sources) {
      resultList.addAll(strings);
    }
    return resultList;
  }
}
