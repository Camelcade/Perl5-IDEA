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

package com.perl5.lang.perl.idea.configuration.settings;

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.impl.PerlSdkTable;
import com.intellij.openapi.util.AtomicNullableLazyValue;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiManager;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Transient;
import com.perl5.lang.perl.idea.PerlPathMacros;
import com.perl5.lang.perl.internals.PerlVersion;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.perl5.lang.perl.util.PerlScalarUtil.DEFAULT_SELF_NAME;

/**
 * Created by hurricup on 30.08.2015.
 */
@State(
  name = "Perl5Settings",
  storages = {
    @Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
    @Storage(id = "dir", file = PerlPathMacros.PERL5_PROJECT_SHARED_SETTINGS_FILE, scheme = StorageScheme.DIRECTORY_BASED)
  }
)

public class PerlSharedSettings implements PersistentStateComponent<PerlSharedSettings> {
  public String myPerlInterpreter;
  public List<String> selfNames = new ArrayList<>(Arrays.asList(DEFAULT_SELF_NAME, "this", "class", "proto"));
  public boolean SIMPLE_MAIN_RESOLUTION = true;
  public boolean AUTOMATIC_HEREDOC_INJECTIONS = true;
  public boolean ALLOW_INJECTIONS_WITH_INTERPOLATION = false;
  public boolean PERL_CRITIC_ENABLED = false;
  public boolean PERL_ANNOTATOR_ENABLED = false;
  public String PERL_DEPARSE_ARGUMENTS = "";
  public String PERL_TIDY_ARGS = "";
  public String PERL_CRITIC_ARGS = "";
  public boolean PERL_SWITCH_ENABLED = false;
  private PerlVersion myTargetPerlVersion = PerlVersion.V5_10;
  private List<String> libRootUrls = new ArrayList<>();

  @Transient
  private Set<String> SELF_NAMES_SET = null;
  @Transient
  private AtomicNullableLazyValue<Sdk> mySdkProvider;

  @Transient
  private Project myProject;

  private PerlSharedSettings() {
  }

  public PerlSharedSettings(Project project) {
    myProject = project;
    resetSdk();
    myProject.getMessageBus().connect().subscribe(PerlSdkTable.PERL_TABLE_TOPIC, new ProjectJdkTable.Listener() {
      @Override
      public void jdkAdded(@NotNull Sdk jdk) {

      }

      @Override
      public void jdkRemoved(@NotNull Sdk jdk) {
        if (StringUtil.equals(jdk.getName(), myPerlInterpreter)) {
          myPerlInterpreter = null;
        }
        resetSdk();
      }

      @Override
      public void jdkNameChanged(@NotNull Sdk jdk, @NotNull String previousName) {
        if (StringUtil.equals(myPerlInterpreter, previousName)) {
          myPerlInterpreter = jdk.getName();
        }
      }
    });
  }

  private void resetSdk() {
    mySdkProvider = new AtomicNullableLazyValue<Sdk>() {
      @Nullable
      @Override
      protected Sdk compute() {
        return PerlSdkTable.getInstance().findJdk(myPerlInterpreter);
      }
    };
  }

  @Nullable
  @Transient
  public Sdk getSdk() {
    return mySdkProvider.getValue();
  }

  public void setSdk(@Nullable Sdk sdk) {
    myPerlInterpreter = sdk == null ? null : sdk.getName();
    resetSdk();
    notifyPlatform();
  }

  @Nullable
  @Override
  public PerlSharedSettings getState() {
    return this;
  }

  public List<String> getLibRootUrls() {
    return libRootUrls;
  }

  @Override
  public void loadState(PerlSharedSettings state) {
    XmlSerializerUtil.copyBean(state, this);
    resetSdk();
  }

  @NotNull
  public PerlVersion getTargetPerlVersion() {
    return myTargetPerlVersion;
  }

  @NotNull
  public PerlSharedSettings setTargetPerlVersion(@NotNull PerlVersion targetPerlVersion) {
    myTargetPerlVersion = targetPerlVersion;
    return this;
  }

  public void settingsUpdated() {
    SELF_NAMES_SET = null;
    notifyPlatform();
  }

  private void notifyPlatform() {
    PsiManager.getInstance(myProject).dropResolveCaches();
    DaemonCodeAnalyzer.getInstance(myProject).restart();
  }

  public boolean isSelfName(String name) {
    if (SELF_NAMES_SET == null) {
      SELF_NAMES_SET = new THashSet<>(selfNames);
    }
    return SELF_NAMES_SET.contains(name);
  }

  public void setDeparseOptions(String optionsString) {
    while (optionsString.length() > 0 && optionsString.charAt(0) != '-') {
      optionsString = optionsString.substring(1);
    }
    PERL_DEPARSE_ARGUMENTS = optionsString;
  }

  public static PerlSharedSettings getInstance(@NotNull Project project) {
    return ServiceManager.getService(project, PerlSharedSettings.class);
  }
}
