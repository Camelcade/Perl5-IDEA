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

package unit;

import base.TemplateToolkitLightTestCase;
import com.intellij.configurationStore.XmlSerializer;
import com.perl5.lang.tt2.idea.settings.TemplateToolkitSettings;
import org.junit.Test;

import java.util.List;

public class TemplateToolkitSettingsPersistenceTest extends TemplateToolkitLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "unit/settings";
  }

  @Override
  public String getFileExtension() {
    return "xml";
  }

  @Test
  public void testSettingsDefault() {
    assertNull(XmlSerializer.serialize(new TemplateToolkitSettings()));
  }

  @Test
  public void testSettingsSerialization() {
    var settings = new TemplateToolkitSettings();
    settings.substitutedExtensions.addAll(List.of("ext1", "ext2", "ext3"));
    settings.START_TAG = "%start";
    settings.END_TAG = "%end";
    settings.OUTLINE_TAG = "%%outline";
    settings.ENABLE_ANYCASE = true;
    compareSerializedStateWithFile(settings);
  }

  @Test
  public void testSettingsDeserialization() {
    var settings = new TemplateToolkitSettings();
    assertEmpty(settings.substitutedExtensions);
    assertEquals("[%", settings.START_TAG);
    assertEquals("%]", settings.END_TAG);
    assertEquals("%%", settings.OUTLINE_TAG);
    assertFalse(settings.ENABLE_ANYCASE);

    loadSettings("settingsSerialization.xml.txt", settings, TemplateToolkitSettings.class);
    assertEquals(List.of("ext1", "ext2", "ext3"), settings.substitutedExtensions);
    assertEquals("%start", settings.START_TAG);
    assertEquals("%end", settings.END_TAG);
    assertEquals("%%outline", settings.OUTLINE_TAG);
    assertTrue(settings.ENABLE_ANYCASE);
  }
}
