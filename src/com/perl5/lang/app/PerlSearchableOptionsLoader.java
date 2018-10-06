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

package com.perl5.lang.app;

import com.intellij.ide.ui.search.SearchableOptionsRegistrar;
import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.util.ResourceUtil;
import org.jdom.Document;
import org.jdom.Element;

import java.net.URL;
import java.util.List;

/**
 * Loads UI search words, partial copy-paste of com.intellij.ide.ui.search.SearchableOptionsRegistrarImpl
 */
public class PerlSearchableOptionsLoader implements BaseComponent {
  private static final Logger LOG = Logger.getInstance(PerlSearchableOptionsLoader.class);

  @Override
  public void initComponent() {
    SearchableOptionsRegistrar registrar = SearchableOptionsRegistrar.getInstance();
    if (registrar == null) {
      return;
    }
    try {
      //index
      final URL indexResource = ResourceUtil.getResource(PerlSearchableOptionsLoader.class, "/search/", "searchableOptions.xml");
      if (indexResource == null) {
        LOG.info("No /search/searchableOptions.xml found, settings search won't work!");
        return;
      }

      Document document = JDOMUtil.loadDocument(indexResource);
      Element root = document.getRootElement();
      List configurables = root.getChildren("configurable");
      for (final Object o : configurables) {
        final Element configurable = (Element)o;
        final String configurableId = configurable.getAttributeValue("id");
        final String configurableDisplayName = configurable.getAttributeValue("configurable_name");
        final List options = configurable.getChildren("option");
        for (Object o1 : options) {
          Element optionElement = (Element)o1;
          final String option = optionElement.getAttributeValue("name");
          final String path = optionElement.getAttributeValue("path");
          final String hit = optionElement.getAttributeValue("hit");
          registrar.addOption(option, path, hit, configurableId, configurableDisplayName);
        }
      }
    }
    catch (Exception e) {
      LOG.error(e);
    }
  }
}
