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

package com.perl5.lang.htmlmason.idea.configuration;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.fileTypes.FileNameMatcher;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.util.AtomicNullableLazyValue;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.perl5.lang.htmlmason.elementType.HTMLMasonElementTypes;
import com.perl5.lang.mason2.idea.configuration.VariableDescription;
import com.perl5.lang.perl.idea.PerlPathMacros;
import com.perl5.lang.perl.idea.modules.PerlSourceRootType;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 05.03.2016.
 */
@State(
  name = "HTMLMasonSettings",
  storages = @Storage(PerlPathMacros.PERL5_PROJECT_SHARED_SETTINGS_FILE)

)

public class HTMLMasonSettings extends AbstractMasonSettings implements PersistentStateComponent<HTMLMasonSettings>, HTMLMasonElementTypes {

  public String autoHandlerName = "autohandler";
  public String defaultHandlerName = "dhandler";
  public List<String> substitutedExtensions = new ArrayList<>();
  public List<HTMLMasonCustomTag> customTags = new ArrayList<>();

  @Transient
  private transient AtomicNotNullLazyValue<List<FileNameMatcher>> myLazyMatchers;

  private transient Map<String, String> myOpenCloseMap;
  private transient AtomicNullableLazyValue<Map<String, HTMLMasonCustomTag>> myCustomTagsMapProvider;

  public HTMLMasonSettings(@NotNull Project project) {
    this();
    myProject = project;
    createLazyObjects();
  }

  private HTMLMasonSettings() {
    globalVariables.add(new VariableDescription("$m", "HTML::Mason::Request"));
    globalVariables.add(new VariableDescription("$r", "Apache::Request"));
    changeCounter++;
    initCustomTagsMapProvider();
    createLazyObjects();
  }

  private void initCustomTagsMapProvider() {
    myCustomTagsMapProvider = AtomicNullableLazyValue.createValue(() -> {
      if (customTags.isEmpty()) {
        return null;
      }

      Map<String, HTMLMasonCustomTag> result = new THashMap<>();

      for (HTMLMasonCustomTag customTag : customTags) {
        result.put(customTag.getText(), customTag);
      }

      return result;
    });
  }

  @Nullable
  @Override
  public HTMLMasonSettings getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull HTMLMasonSettings state) {
    XmlSerializerUtil.copyBean(state, this);
    changeCounter++;
  }

  private void createLazyObjects() {
    myLazyMatchers = AtomicNotNullLazyValue.createValue(() -> {
      List<FileNameMatcher> result = new ArrayList<>();
      FileTypeManager fileTypeManager = FileTypeManager.getInstance();
      for (FileType fileType : fileTypeManager.getRegisteredFileTypes()) {
        if (fileType instanceof LanguageFileType) {
          for (FileNameMatcher matcher : fileTypeManager.getAssociations(fileType)) {
            if (substitutedExtensions.contains(matcher.getPresentableString())) {
              result.add(matcher);
            }
          }
        }
      }
      return result;
    });
  }

  public boolean isVirtualFileNameMatches(@NotNull VirtualFile file) {
    for (FileNameMatcher matcher : myLazyMatchers.getValue()) {
      if (matcher.accept(file.getName())) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected PerlSourceRootType getSourceRootType() {
    return HTMLMasonSourceRootType.INSTANCE;
  }

  @Override
  public void settingsUpdated() {
    super.settingsUpdated();
    createLazyObjects();
    myOpenCloseMap = null;
    initCustomTagsMapProvider();
  }

  public void prepareLexerConfiguration() {
    myOpenCloseMap = new THashMap<>();
    myOpenCloseMap.put(KEYWORD_PERL_OPENER, KEYWORD_PERL_CLOSER);
    myOpenCloseMap.put(KEYWORD_INIT_OPENER, KEYWORD_INIT_CLOSER);
    myOpenCloseMap.put(KEYWORD_CLEANUP_OPENER, KEYWORD_CLEANUP_CLOSER);
    myOpenCloseMap.put(KEYWORD_ONCE_OPENER, KEYWORD_ONCE_CLOSER);
    myOpenCloseMap.put(KEYWORD_SHARED_OPENER, KEYWORD_SHARED_CLOSER);
    myOpenCloseMap.put(KEYWORD_FLAGS_OPENER, KEYWORD_FLAGS_CLOSER);
    myOpenCloseMap.put(KEYWORD_ATTR_OPENER, KEYWORD_ATTR_CLOSER);
    myOpenCloseMap.put(KEYWORD_ARGS_OPENER, KEYWORD_ARGS_CLOSER);
    myOpenCloseMap.put(KEYWORD_FILTER_OPENER, KEYWORD_FILTER_CLOSER);
    myOpenCloseMap.put(KEYWORD_DOC_OPENER, KEYWORD_DOC_CLOSER);
    myOpenCloseMap.put(KEYWORD_TEXT_OPENER, KEYWORD_TEXT_CLOSER);

    // parametrized
    myOpenCloseMap.put(KEYWORD_METHOD_OPENER, KEYWORD_METHOD_CLOSER);
    myOpenCloseMap.put(KEYWORD_DEF_OPENER, KEYWORD_DEF_CLOSER);

    // iterate custom tags
    for (HTMLMasonCustomTag customTag : customTags) {
      myOpenCloseMap.put(customTag.getOpenTagText(), customTag.getCloseTagText());
    }

    //		System.err.println("HTML::Mason lexer settings prepared");
  }

  public Map<String, String> getOpenCloseMap() {
    if (myOpenCloseMap == null) {
      prepareLexerConfiguration();
    }
    return myOpenCloseMap;
  }

  @Nullable
  public Map<String, HTMLMasonCustomTag> getCustomTagsMap() {
    return myCustomTagsMapProvider.getValue();
  }

  @NotNull
  public static HTMLMasonSettings getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, HTMLMasonSettings.class);
  }
}
