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

package unit.perl;

import base.PerlLightTestCase;
import com.intellij.configurationStore.XmlSerializer;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.testFramework.UsefulTestCase;
import com.perl5.lang.perl.idea.codeInsight.Perl5CodeInsightSettings;
import com.perl5.lang.perl.idea.configuration.settings.PerlApplicationSettings;
import com.perl5.lang.perl.idea.configuration.settings.PerlLocalSettings;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.folding.PerlFoldingSettingsImpl;
import com.perl5.lang.perl.idea.intellilang.PerlInjectionMarkersService;
import com.perl5.lang.perl.idea.sdk.PerlConfig;
import com.perl5.lang.perl.idea.sdk.host.docker.PerlDockerProjectSettings;
import com.perl5.lang.perl.internals.PerlVersion;
import com.perl5.lang.perl.xsubs.PerlXSubsState;
import org.jdom.Element;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.perl5.lang.perl.util.PerlScalarUtil.DEFAULT_SELF_NAME;

public class PerlSettingsPersistenceTest extends PerlLightTestCase {
  @Override
  protected String getBaseDataPath() {
    return "unit/perl/settings";
  }

  @Override
  public String getFileExtension() {
    return "xml";
  }

  @Test
  public void testPerlConfigSerialization() {
    Map<String, String> configData = new LinkedHashMap<>();
    configData.put("vendorarch", "/usr/lib/x86_64-linux-gnu/perl5/5.34");
    configData.put("vendorarchexp", "/usr/lib/x86_64-linux-gnu/perl5/5.34");
    configData.put("vendorbin", "/usr/bin");
    configData.put("vendorbinexp", "/usr/bin");
    configData.put("vendorhtml1dir", " ");
    configData.put("vendorhtml3dir", " ");
    configData.put("vendorlib", "/usr/share/perl5");
    configData.put("vendorlibexp", "/usr/share/perl5");
    configData.put("vendorman1dir", "/usr/share/man/man1");
    configData.put("vendorman1direxp", "/usr/share/man/man1");
    configData.put("vendorman3dir", "/usr/share/man/man3");
    configData.put("vendorman3direxp", "/usr/share/man/man3");
    configData.put("vendorprefix", "/usr");
    configData.put("vendorprefixexp", "/usr");
    configData.put("vendorscript", "/usr/bin");
    configData.put("vendorscriptexp", "/usr/bin");
    configData.put("version", "5.34.0");
    configData.put("version_patchlevel_string", "version 34 subversion 0");
    configData.put("xlibpth", "/usr/lib/386 /lib/386");
    configData.put("yacc", "yacc");
    configData.put("zip", "zip");
    var perlConfig = PerlConfig.create(configData);
    assertFalse(perlConfig.isEmpty());
    var sdkData = new Element("sdkData");
    perlConfig.save(sdkData);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), JDOMUtil.write(sdkData));
  }

  @Test
  public void testPerlConfigDeserialization() {
    var perlConfig = PerlConfig.load(loadState("perlConfigSerialization.xml.txt"));
    assertFalse(perlConfig.isEmpty());
    assertEquals("5.34.0", perlConfig.get("version"));
    assertEquals("/usr/share/man/man3", perlConfig.get("vendorman3dir"));
  }

  @Test
  public void testPerlConfigEmptySerialization() {
    var perlConfig = PerlConfig.create(new HashMap<>());
    assertTrue(perlConfig.isEmpty());
    var sdkData = new Element("sdkData");
    perlConfig.save(sdkData);
    UsefulTestCase.assertSameLinesWithFile(getTestResultsFilePath(), JDOMUtil.write(sdkData));
  }

  @Test
  public void testPerlConfigEmptyDeserialization() {
    var perlConfig = PerlConfig.load(loadState("perlConfigEmptySerialization.xml.txt"));
    assertTrue(perlConfig.isEmpty());
  }

  @Test
  public void testDockerSettingsDefault() {
    assertNull(XmlSerializer.serialize(new PerlDockerProjectSettings()));
  }

  @Test
  public void testDockerSettingsSerialization() {
    var settings = new PerlDockerProjectSettings();
    settings.setAdditionalDockerParameters("additional params");
    compareSerializedStateWithFile(settings);
  }

  @Test
  public void testDockerSettingsDeserialization() {
    var settings = new PerlDockerProjectSettings();
    assertEmpty(settings.getAdditionalDockerParameters());

    loadSettings("dockerSettingsSerialization.xml.txt", settings, PerlDockerProjectSettings.class);
    assertEquals("additional params", settings.getAdditionalDockerParameters());
  }

  @Test
  public void testXSubsStateSettingsDefault() {
    assertNull(XmlSerializer.serialize(new PerlXSubsState()));
  }

  @Test
  public void testXSubsStateSettingsSerialization() {
    var settings = new PerlXSubsState();
    settings.isActual = false;
    settings.myFilesMap = new LinkedHashMap<>();
    settings.myFilesMap.put("testfile1", 1L);
    settings.myFilesMap.put("testfile2", 2L);
    compareSerializedStateWithFile(settings);
  }

  @Test
  public void testXSubsStateSettingsDeserialization() {
    var settings = new PerlXSubsState();
    assertTrue(settings.isActual);
    assertEmpty(settings.myFilesMap.entrySet());

    loadSettings("xSubsStateSettingsSerialization.xml.txt", settings, PerlXSubsState.class);

    assertFalse(settings.isActual);
    assertEquals(Long.valueOf(1L), settings.myFilesMap.get("testfile1"));
    assertEquals(Long.valueOf(2L), settings.myFilesMap.get("testfile2"));
  }

  @Test
  public void testApplicationSettingsDefault() {
    assertNull(XmlSerializer.serialize(new PerlApplicationSettings()));
  }

  @Test
  public void testApplicationSettingsSerialization() {
    var settings = new PerlApplicationSettings();
    settings.pluginVersion = "test version";
    settings.popupShown = true;

    compareSerializedStateWithFile(settings);
  }

  @Test
  public void testApplicationSettingsDeserialization() {
    var settings = new PerlApplicationSettings();
    assertEmpty(settings.pluginVersion);
    assertFalse(settings.popupShown);
    assertTrue(settings.shouldShowAnnounce());

    loadSettings("applicationSettingsSerialization.xml.txt", settings, PerlApplicationSettings.class);
    assertEquals("test version", settings.pluginVersion);
    assertTrue(settings.popupShown);
    assertTrue(settings.shouldShowAnnounce());
  }

  @Test
  public void testInjectionMarkerSettingsDefault() {
    assertNull(XmlSerializer.serialize(new PerlInjectionMarkersService()));
  }

  @Test
  public void testInjectionMarkerSettingsSerialization() {
    var settings = new PerlInjectionMarkersService();
    settings.setCustomMarkersMap(Map.of("TEST_MARKER1", "HTML", "TEST_MARKER2", "TEXT"));
    compareSerializedStateWithFile(settings);
  }

  @Test
  public void testInjectionMarkerSettingsDeserialization() {
    var settings = new PerlInjectionMarkersService();
    assertNull(settings.getLanguageByMarker("TEST_MARKER1"));
    assertNull(settings.getLanguageByMarker("TEST_MARKER2"));

    loadSettings("injectionMarkerSettingsSerialization.xml.txt", settings, PerlInjectionMarkersService.class);
    assertNotNull(settings.getLanguageByMarker("TEST_MARKER1"));
    assertNotNull(settings.getLanguageByMarker("TEST_MARKER2"));
  }

  @Test
  public void testPerlFoldingSettingsDefault() {
    assertNull(XmlSerializer.serialize(new PerlFoldingSettingsImpl()));
  }

  @Test
  public void testPerlFoldingSettingsSerialization() {
    var settings = new PerlFoldingSettingsImpl();
    settings.COLLAPSE_COMMENTS = false;
    settings.COLLAPSE_CONSTANT_BLOCKS = true;
    settings.COLLAPSE_ANON_ARRAYS = true;
    settings.COLLAPSE_ANON_HASHES = true;
    settings.COLLAPSE_PARENTHESISED = true;
    settings.COLLAPSE_HEREDOCS = true;
    settings.COLLAPSE_TEMPLATES = true;
    settings.COLLAPSE_QW = true;
    settings.COLLAPSE_CHAR_SUBSTITUTIONS = false;
    compareSerializedStateWithFile(settings);
  }

  @Test
  public void testPerlFoldingSettingsDeserialization() {
    var settings = new PerlFoldingSettingsImpl();
    assertTrue(settings.COLLAPSE_COMMENTS);
    assertFalse(settings.COLLAPSE_CONSTANT_BLOCKS);
    assertFalse(settings.COLLAPSE_ANON_ARRAYS);
    assertFalse(settings.COLLAPSE_ANON_HASHES);
    assertFalse(settings.COLLAPSE_PARENTHESISED);
    assertFalse(settings.COLLAPSE_HEREDOCS);
    assertFalse(settings.COLLAPSE_TEMPLATES);
    assertFalse(settings.COLLAPSE_QW);
    assertTrue(settings.COLLAPSE_CHAR_SUBSTITUTIONS);

    loadSettings("perlFoldingSettingsSerialization.xml.txt", settings, PerlFoldingSettingsImpl.class);

    assertFalse(settings.COLLAPSE_COMMENTS);
    assertTrue(settings.COLLAPSE_CONSTANT_BLOCKS);
    assertTrue(settings.COLLAPSE_ANON_ARRAYS);
    assertTrue(settings.COLLAPSE_ANON_HASHES);
    assertTrue(settings.COLLAPSE_PARENTHESISED);
    assertTrue(settings.COLLAPSE_HEREDOCS);
    assertTrue(settings.COLLAPSE_TEMPLATES);
    assertTrue(settings.COLLAPSE_QW);
    assertFalse(settings.COLLAPSE_CHAR_SUBSTITUTIONS);
  }

  @Test
  public void testPerlLocalSettingsDefault() {
    assertNull(XmlSerializer.serialize(new PerlLocalSettings()));
  }

  @Test
  public void testPerlLocalSettingsSerialization() {
    var settings = new PerlLocalSettings();
    settings.DISABLE_NO_INTERPRETER_WARNING = true;
    settings.ENABLE_REGEX_INJECTIONS = true;
    settings.setPerlInterpreter("test_interpreter");
    settings.setExternalLibrariesPaths(List.of("testlib1", "testlib2"));
    compareSerializedStateWithFile(settings);
  }

  @Test
  public void testPerlLocalSettingsDeserialization() {
    var settings = new PerlLocalSettings();
    assertFalse(settings.DISABLE_NO_INTERPRETER_WARNING);
    assertFalse(settings.ENABLE_REGEX_INJECTIONS);
    assertEmpty(settings.getPerlInterpreter());
    assertEmpty(settings.getExternalLibrariesPaths());

    loadSettings("perlLocalSettingsSerialization.xml.txt", settings, PerlLocalSettings.class);
    assertTrue(settings.DISABLE_NO_INTERPRETER_WARNING);
    assertTrue(settings.ENABLE_REGEX_INJECTIONS);
    assertEquals("test_interpreter", settings.getPerlInterpreter());
    assertEquals(List.of("testlib1", "testlib2"), settings.getExternalLibrariesPaths());
  }

  @Test
  public void testCodeInsightSettingsDefault() {
    assertNull(XmlSerializer.serialize(new Perl5CodeInsightSettings()));
  }

  @Test
  public void testCodeInsightSettingsSerialization() {
    var settings = new Perl5CodeInsightSettings();
    settings.SMART_COMMA_SEQUENCE_TYPING = false;
    settings.AUTO_BRACE_HEX_SUBSTITUTION = false;
    settings.AUTO_BRACE_OCT_SUBSTITUTION = false;
    settings.AUTO_INSERT_COLON = false;
    settings.HEREDOC_AUTO_INSERTION = false;
    compareSerializedStateWithFile(settings);
  }

  @Test
  public void testCodeInsightSettingsDeserialization() {
    var settings = new Perl5CodeInsightSettings();
    assertTrue(settings.SMART_COMMA_SEQUENCE_TYPING);
    assertTrue(settings.AUTO_BRACE_HEX_SUBSTITUTION);
    assertTrue(settings.AUTO_BRACE_OCT_SUBSTITUTION);
    assertTrue(settings.AUTO_INSERT_COLON);
    assertTrue(settings.HEREDOC_AUTO_INSERTION);

    loadSettings("codeInsightSettingsSerialization.xml.txt", settings, Perl5CodeInsightSettings.class);
    assertFalse(settings.SMART_COMMA_SEQUENCE_TYPING);
    assertFalse(settings.AUTO_BRACE_HEX_SUBSTITUTION);
    assertFalse(settings.AUTO_BRACE_OCT_SUBSTITUTION);
    assertFalse(settings.AUTO_INSERT_COLON);
    assertFalse(settings.HEREDOC_AUTO_INSERTION);
  }

  @Test
  public void testSharedSettingsDefault() {
    assertNull(XmlSerializer.serialize(new PerlSharedSettings(getProject())));
  }

  @Test
  public void testSharedSettingsSerialization() {
    var settings = new PerlSharedSettings(getProject());
    settings.setTargetPerlVersion(PerlVersion.V5_20);
    settings.SIMPLE_MAIN_RESOLUTION = false;
    settings.AUTOMATIC_HEREDOC_INJECTIONS = false;
    settings.ALLOW_INJECTIONS_WITH_INTERPOLATION = true;
    settings.PERL_CRITIC_ENABLED = true;
    settings.PERL_ANNOTATOR_ENABLED = true;
    settings.PERL_SWITCH_ENABLED = true;
    settings.PERL_DEPARSE_ARGUMENTS = "deparse args";
    settings.PERL_TIDY_ARGS = "perltidy args";
    settings.PERL_CRITIC_ARGS = "perlcritic args";
    settings.selfNames = List.of("selfname");
    compareSerializedStateWithFile(settings);
  }

  @Test
  public void testSharedSettingsDeserialization() {
    var settings = new PerlSharedSettings(getProject());
    assertEquals(PerlVersion.V5_10, settings.getTargetPerlVersion());
    assertTrue(settings.SIMPLE_MAIN_RESOLUTION);
    assertTrue(settings.AUTOMATIC_HEREDOC_INJECTIONS);
    assertFalse(settings.ALLOW_INJECTIONS_WITH_INTERPOLATION);
    assertFalse(settings.PERL_CRITIC_ENABLED);
    assertFalse(settings.PERL_ANNOTATOR_ENABLED);
    assertFalse(settings.PERL_SWITCH_ENABLED);
    assertEmpty(settings.PERL_DEPARSE_ARGUMENTS);
    assertEmpty(settings.PERL_TIDY_ARGS);
    assertEmpty(settings.PERL_CRITIC_ARGS);
    assertEquals(List.of(DEFAULT_SELF_NAME, "this", "class", "proto"), settings.selfNames);

    loadSettings("sharedSettingsSerialization.xml.txt", settings, PerlSharedSettings.class);
    assertEquals(PerlVersion.V5_20, settings.getTargetPerlVersion());
    assertFalse(settings.SIMPLE_MAIN_RESOLUTION);
    assertFalse(settings.AUTOMATIC_HEREDOC_INJECTIONS);
    assertTrue(settings.ALLOW_INJECTIONS_WITH_INTERPOLATION);
    assertTrue(settings.PERL_CRITIC_ENABLED);
    assertTrue(settings.PERL_ANNOTATOR_ENABLED);
    assertTrue(settings.PERL_SWITCH_ENABLED);
    assertEquals("deparse args", settings.PERL_DEPARSE_ARGUMENTS);
    assertEquals("perltidy args", settings.PERL_TIDY_ARGS);
    assertEquals("perlcritic args", settings.PERL_CRITIC_ARGS);
    assertEquals(List.of("selfname"), settings.selfNames);
  }
}
