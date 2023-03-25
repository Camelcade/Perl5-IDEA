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

import base.Mason2LightTestCase;
import com.intellij.configurationStore.XmlSerializer;
import com.perl5.lang.mason2.idea.configuration.MasonSettings;
import com.perl5.lang.mason2.idea.configuration.VariableDescription;
import org.junit.Test;

import java.util.List;

public class Mason2SettingsPersistenceTest extends Mason2LightTestCase {
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
    assertNull(XmlSerializer.serialize(new MasonSettings(getProject())));
  }

  @Test
  public void testSettingsSerialization() {
    var settings = new MasonSettings(getProject());
    settings.autobaseNames.addAll(List.of("name1", "name2", "name3"));
    settings.globalVariables.add(new VariableDescription("foo_bar", "Foo::Bar"));
    compareSerializedStateWithFile(settings);
  }

  @Test
  public void testSettingsDeserialization() {
    var settings = new MasonSettings(getProject());
    assertEquals(List.of("Base.mp", "Base.mc"), settings.autobaseNames);
    assertEquals(List.of(new VariableDescription("$m", "Mason::Request")), settings.globalVariables);

    loadSettings("settingsSerialization.xml.txt", settings, MasonSettings.class);
    assertContainsElements(settings.autobaseNames, "name1", "name2", "name3");
    assertContainsElements(settings.globalVariables, new VariableDescription("foo_bar", "Foo::Bar"));
  }
}
