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

import base.HTMLMasonLightTestCase;
import com.intellij.configurationStore.XmlSerializer;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonCustomTag;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonCustomTagRole;
import com.perl5.lang.htmlmason.idea.configuration.HTMLMasonSettings;
import com.perl5.lang.mason2.idea.configuration.VariableDescription;
import org.junit.Test;

import java.util.List;

public class HtmlMasonSettingsPersistenceTest extends HTMLMasonLightTestCase {
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
    assertNull(XmlSerializer.serialize(new HTMLMasonSettings(getProject())));
  }

  @Test
  public void testSettingsSerialization() {
    var settings = new HTMLMasonSettings(getProject());
    settings.substitutedExtensions.addAll(List.of("ext1", "ext2", "ext3"));
    settings.autoHandlerName = "customautohandler";
    settings.defaultHandlerName = "customdefaulthandler";
    settings.customTags.add(new HTMLMasonCustomTag("testargs", HTMLMasonCustomTagRole.ARGS));
    settings.customTags.add(new HTMLMasonCustomTag("testdef", HTMLMasonCustomTagRole.DEF));
    settings.customTags.add(new HTMLMasonCustomTag("testperl", HTMLMasonCustomTagRole.PERL));
    settings.customTags.add(new HTMLMasonCustomTag("testmethod", HTMLMasonCustomTagRole.METHOD));
    settings.globalVariables.add(new VariableDescription("foo_bar", "Foo::Bar"));
    compareSerializedStateWithFile(settings);
  }

  @Test
  public void testSettingsDeserialization() {
    var settings = new HTMLMasonSettings(getProject());
    assertEmpty(settings.substitutedExtensions);
    assertEmpty(settings.customTags);
    assertEquals("autohandler", settings.autoHandlerName);
    assertEquals("dhandler", settings.defaultHandlerName);
    assertEquals(
      List.of(new VariableDescription("$m", "HTML::Mason::Request"), new VariableDescription("$r", "Apache::Request")),
      settings.globalVariables);

    loadSettings("settingsSerialization.xml.txt", settings, HTMLMasonSettings.class);
    assertEquals(List.of("ext1", "ext2", "ext3"), settings.substitutedExtensions);
    assertEquals("customautohandler", settings.autoHandlerName);
    assertEquals("customdefaulthandler", settings.defaultHandlerName);
    assertContainsElements(settings.globalVariables, new VariableDescription("foo_bar", "Foo::Bar"));
    assertContainsElements(settings.customTags, new HTMLMasonCustomTag("testargs", HTMLMasonCustomTagRole.ARGS));
    assertContainsElements(settings.customTags, new HTMLMasonCustomTag("testdef", HTMLMasonCustomTagRole.DEF));
    assertContainsElements(settings.customTags, new HTMLMasonCustomTag("testperl", HTMLMasonCustomTagRole.PERL));
    assertContainsElements(settings.customTags, new HTMLMasonCustomTag("testmethod", HTMLMasonCustomTagRole.METHOD));
  }
}
