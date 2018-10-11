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

package com.intellij.openapi.projectRoots.impl;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.IncorrectOperationException;
import com.intellij.util.ReflectionUtil;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.Topic;
import com.intellij.util.messages.impl.MessageListenerList;
import com.perl5.lang.perl.idea.PerlPathMacros;
import com.perl5.lang.perl.idea.sdk.PerlSdkType;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@State(
  name = "PerlInterpreters",
  storages = @Storage(PerlPathMacros.PERL5_APP_SETTINGS_FILE)
)
public class PerlSdkTable extends ProjectJdkTable implements PersistentStateComponent<Element> {
  public static final Topic<Listener> PERL_TABLE_TOPIC = Topic.create("Perl Interpreters table", Listener.class);

  private static final String PERL = "perl";

  private final List<Sdk> myInterpretersList = new ArrayList<>();
  private final MessageBus myMessageBus;
  private final MessageListenerList<Listener> myListenerList;

  public PerlSdkTable() {
    myMessageBus = ApplicationManager.getApplication().getMessageBus();
    myListenerList = new MessageListenerList<>(myMessageBus, PERL_TABLE_TOPIC);
  }

  @Override
  public void loadState(@NotNull Element element) {
    myInterpretersList.clear();

    for (Element child : element.getChildren(PERL)) {
      ProjectJdkImpl sdk = new ProjectJdkImpl(null, null);
      sdk.readExternal(child, this);
      myInterpretersList.add(sdk);
    }
  }

  @Override
  public Element getState() {
    Element element = new Element("state");
    for (Sdk sdk : myInterpretersList) {
      Element e = new Element(PERL);
      ((ProjectJdkImpl)sdk).writeExternal(e);
      element.addContent(e);
    }
    return element;
  }

  @Nullable
  @Override
  public Sdk findJdk(String name) {
    for (Sdk interpreter : myInterpretersList) {
      if (StringUtil.equals(name, interpreter.getName())) {
        return interpreter;
      }
    }
    return null;
  }

  @Nullable
  @Override
  public Sdk findJdk(String name, String type) {
    return findJdk(name);
  }

  @NotNull
  @Override
  public Sdk[] getAllJdks() {
    return myInterpretersList.toArray(new Sdk[myInterpretersList.size()]);
  }

  @NotNull
  @Override
  public List<Sdk> getSdksOfType(@NotNull SdkTypeId type) {
    throw new IncorrectOperationException();
  }

  @Override
  public void addJdk(@NotNull Sdk jdk) {
    myInterpretersList.add(jdk);
    myMessageBus.syncPublisher(PERL_TABLE_TOPIC).jdkAdded(jdk);
  }

  @Override
  public void removeJdk(@NotNull Sdk jdk) {
    myInterpretersList.remove(jdk);
    myMessageBus.syncPublisher(PERL_TABLE_TOPIC).jdkRemoved(jdk);
  }

  @Override
  public void updateJdk(@NotNull Sdk originalJdk, @NotNull Sdk modifiedJdk) {
    final String previousName = originalJdk.getName();
    final String newName = modifiedJdk.getName();

    Method method = ReflectionUtil.getDeclaredMethod(ProjectJdkImpl.class, "copyTo", ProjectJdkImpl.class);
    if (method == null) {
      throw new RuntimeException("Missing copyTo method");
    }
    try {
      method.invoke(modifiedJdk, (ProjectJdkImpl)originalJdk);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

    if (!previousName.equals(newName)) {
      // fire changes because after renaming JDK its name may match the associated jdk name of modules/project
      myMessageBus.syncPublisher(PERL_TABLE_TOPIC).jdkNameChanged(originalJdk, previousName);
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public void addListener(@NotNull Listener listener) {
    myListenerList.add(listener);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void removeListener(@NotNull Listener listener) {
    myListenerList.remove(listener);
  }

  @NotNull
  @Override
  public SdkTypeId getDefaultSdkType() {
    return PerlSdkType.INSTANCE;
  }

  @NotNull
  @Override
  public SdkTypeId getSdkTypeByName(@NotNull String name) {
    return PerlSdkType.INSTANCE;
  }

  @NotNull
  @Override
  public Sdk createSdk(@NotNull String name, @NotNull SdkTypeId sdkType) {
    return ProjectJdkTable.getInstance().createSdk(name, PerlSdkType.INSTANCE);
  }

  public static PerlSdkTable getInstance() {
    return ServiceManager.getService(PerlSdkTable.class);
  }
}
