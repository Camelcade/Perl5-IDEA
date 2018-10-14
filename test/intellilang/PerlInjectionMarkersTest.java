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

package intellilang;

import base.PerlLightTestCase;
import com.intellij.lang.html.HTMLLanguage;
import com.intellij.openapi.util.Pair;
import com.intellij.util.FileContentUtil;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.intellilang.PerlInjectionMarkersService;

import java.util.Collections;

import static com.perl5.lang.perl.idea.intellilang.PerlDefaultInjectionMarkers.PERL5_MARKER;

public class PerlInjectionMarkersTest extends PerlLightTestCase {
  private static final String TEST_MARKER = "_MYHTML";

  @Override
  protected String getTestDataPath() {
    return "testData/intellilang/perl/injeciton_markers";
  }

  public void testCustomMarkerAddition() {
    PerlInjectionMarkersService markersService = PerlInjectionMarkersService.getInstance(getProject());
    assertNull(markersService.getLanguageByMarker(TEST_MARKER));
    markersService.setCustomMarkersMap(ContainerUtil.newHashMap(Pair.create(TEST_MARKER, HTMLLanguage.INSTANCE.getID())));
    assertEquals(HTMLLanguage.INSTANCE, markersService.getLanguageByMarker(TEST_MARKER));
  }

  public void testMarkerOverride() {
    PerlInjectionMarkersService markersService = PerlInjectionMarkersService.getInstance(getProject());
    assertEquals(PerlLanguage.INSTANCE, markersService.getLanguageByMarker(PERL5_MARKER));
    markersService.setCustomMarkersMap(ContainerUtil.newHashMap(Pair.create(PERL5_MARKER, HTMLLanguage.INSTANCE.getID())));
    assertEquals(HTMLLanguage.INSTANCE, markersService.getLanguageByMarker(PERL5_MARKER));
    markersService.setCustomMarkersMap(Collections.emptyMap());
    assertEquals(PerlLanguage.INSTANCE, markersService.getLanguageByMarker(PERL5_MARKER));
  }

  public void testMarkerCompletion() {
    PerlInjectionMarkersService markersService = PerlInjectionMarkersService.getInstance(getProject());
    markersService.setCustomMarkersMap(ContainerUtil.newHashMap(Pair.create(TEST_MARKER, HTMLLanguage.INSTANCE.getID())));
    doTestCompletion();
  }


  public void testAnnotated() {
    doFileTest();
  }

  public void testHereDoc() {
    doFileTest();
  }


  private void doFileTest() {
    PerlInjectionMarkersService markersService = PerlInjectionMarkersService.getInstance(getProject());
    initWithFileSmartWithoutErrors();
    assertNotInjected();
    markersService.setCustomMarkersMap(ContainerUtil.newHashMap(Pair.create(TEST_MARKER, HTMLLanguage.INSTANCE.getID())));
    FileContentUtil.reparseOpenedFiles();
    initWithFileSmartWithoutErrors();
    assertInjected();
  }
}
