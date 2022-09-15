/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package annotator;


import base.PodLightTestCase;
import com.intellij.spellchecker.inspections.SpellCheckingInspection;
import com.perl5.lang.pod.idea.inspections.PodLegacySectionLinkInspection;
import com.perl5.lang.pod.idea.inspections.PodOverlessItemInspection;
import com.perl5.lang.pod.idea.inspections.PodUnresolvableLinkInspection;
import org.junit.Test;
public class PodAnnotatorTest extends PodLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "annotator/pod";
  }

  @Test
  public void testSpellChecker() {doInspectionTest(SpellCheckingInspection.class);}

  @Test
  public void testLegacyLink() {doInspectionTest(PodLegacySectionLinkInspection.class);}

  @Test
  public void testUnresolvedLink() {doInspectionTest(PodUnresolvableLinkInspection.class);}

  @Test
  public void testItemOutsideOver() {doInspectionTest(PodOverlessItemInspection.class);}
}
