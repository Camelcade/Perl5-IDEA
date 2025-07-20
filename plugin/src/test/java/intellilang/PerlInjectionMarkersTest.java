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

package intellilang;


import base.PerlLightTestCase;
import com.intellij.lang.html.HTMLLanguage;
import com.intellij.util.FileContentUtil;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.idea.intellilang.PerlInjectionMarkersService;
import org.intellij.lang.xpath.xslt.psi.impl.XsltLanguage;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static com.perl5.lang.perl.idea.intellilang.PerlDefaultInjectionMarkers.PERL5_MARKER;
public class PerlInjectionMarkersTest extends PerlLightTestCase {
  private static final String TEST_MARKER = "_MYHTML";

  @Override
  protected String getBaseDataPath() {
    return "intellilang/perl/injeciton_markers";
  }

  @Test
  public void testCustomMarkerAddition() {
    PerlInjectionMarkersService markersService = PerlInjectionMarkersService.getInstance(getProject());
    assertNull(markersService.getLanguageByMarker(TEST_MARKER));
    markersService.setCustomMarkersMap(Map.of(TEST_MARKER, HTMLLanguage.INSTANCE.getID()));
    assertEquals(HTMLLanguage.INSTANCE, markersService.getLanguageByMarker(TEST_MARKER));
  }

  @Test
  public void testMarkerOverride() {
    PerlInjectionMarkersService markersService = PerlInjectionMarkersService.getInstance(getProject());
    assertEquals(PerlLanguage.INSTANCE, markersService.getLanguageByMarker(PERL5_MARKER));
    markersService.setCustomMarkersMap(Map.of(PERL5_MARKER, HTMLLanguage.INSTANCE.getID()));
    assertEquals(HTMLLanguage.INSTANCE, markersService.getLanguageByMarker(PERL5_MARKER));
    markersService.setCustomMarkersMap(Collections.emptyMap());
    assertEquals(PerlLanguage.INSTANCE, markersService.getLanguageByMarker(PERL5_MARKER));
  }

  @Test
  public void testMarkerCompletion() {
    LOG.debug("Loading ", XsltLanguage.INSTANCE);
    PerlInjectionMarkersService markersService = PerlInjectionMarkersService.getInstance(getProject());
    markersService.setCustomMarkersMap(Map.of(TEST_MARKER, HTMLLanguage.INSTANCE.getID()));
    doTestCompletion();
  }


  @Test
  public void testAnnotated() {
    doFileTest();
  }

  @Test
  public void testHereDoc() {
    doFileTest();
  }


  private void doFileTest() {
    PerlInjectionMarkersService markersService = PerlInjectionMarkersService.getInstance(getProject());
    initWithFileSmartWithoutErrors();
    assertNotInjected();
    markersService.setCustomMarkersMap(Map.of(TEST_MARKER, HTMLLanguage.INSTANCE.getID()));
    FileContentUtil.reparseOpenedFiles();
    initWithFileSmartWithoutErrors();
    assertInjected();
  }
}
