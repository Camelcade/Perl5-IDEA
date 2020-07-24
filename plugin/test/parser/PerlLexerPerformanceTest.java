/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package parser;


import categories.Performance;
import com.intellij.testFramework.PlatformTestUtil;
import com.perl5.lang.perl.lexer.PerlLexingContext;
import com.perl5.lang.perl.lexer.adapters.PerlMergingLexerAdapter;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;


@Ignore
@Category(Performance.class)
public class PerlLexerPerformanceTest extends PerlParserTestBase {
  @Test
  public void testPerlTidyLexing() {

    String testData = getPerlTidy();

    final int iterations = 100;

    System.err.println("Warming up...");
    for (int i = 0; i < iterations; i++) {
      testLexing(testData);
    }

    final int time = 70;

    PlatformTestUtil.startPerformanceTest("PerlTidy lexing", iterations * time, () ->
    {
      long length = 0;
      for (int i = 0; i < iterations; i++) {
        length += testLexing(testData);
      }
      System.err.println("Lexing done in " + length / iterations + " ms per iteration of " + time);
    }).attempts(1).assertTiming();
  }

  private long testLexing(String testData) {
    PerlLexingContext lexingContext = PerlLexingContext.create(getProject()).withEnforcedSublexing(true);
    PerlMergingLexerAdapter perlLexer = new PerlMergingLexerAdapter(lexingContext);
    perlLexer.start(testData, 0, testData.length(), 0);

    long start = System.currentTimeMillis();

    int tokens = 0;
    while (perlLexer.getTokenType() != null) {
      perlLexer.advance();
      tokens++;
    }
    //			System.err.println("Total tokens: " + tokens);

    return System.currentTimeMillis() - start;
  }
}
