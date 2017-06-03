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

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.PermanentInstallationID;
import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.event.EditorFactoryAdapter;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.util.JDOMUtil;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.io.HttpRequests;
import com.perl5.lang.perl.fileTypes.PerlPluginBaseFileType;
import org.jdom.JDOMException;
import org.jetbrains.annotations.NotNull;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import static com.perl5.lang.perl.util.PerlPluginUtil.getPlugin;

/**
 * Created by hurricup on 23.11.2016.
 */
public class PerlUpdateComponent implements ApplicationComponent, Disposable {
  private static final String KEY = "perl.last.update.timestamp";

  private final EditorFactoryAdapter myListener = new EditorFactoryAdapter() {
    @Override
    public void editorCreated(@NotNull EditorFactoryEvent event) {
      Document document = event.getEditor().getDocument();

      VirtualFile file = FileDocumentManager.getInstance().getFile(document);
      if (file != null && file.getFileType() instanceof PerlPluginBaseFileType) {
        checkForUpdates();
      }
    }
  };

  @Override
  public void initComponent() {
    if (!ApplicationManager.getApplication().isUnitTestMode()) {
      EditorFactory.getInstance().addEditorFactoryListener(myListener, this);
    }
  }

  @Override
  public void disposeComponent() {

  }

  @NotNull
  @Override
  public String getComponentName() {
    return getClass().getName();
  }

  @Override
  public void dispose() {
    disposeComponent();
  }

  private static void checkForUpdates() {
    PropertiesComponent propertiesComponent = PropertiesComponent.getInstance();
    long lastUpdate = propertiesComponent.getOrInitLong(KEY, 0);
    if (lastUpdate == 0 || System.currentTimeMillis() - lastUpdate > TimeUnit.DAYS.toMillis(1)) {
      ApplicationManager.getApplication().executeOnPooledThread(() ->
                                                                {
                                                                  try {
                                                                    String buildNumber =
                                                                      ApplicationInfo.getInstance().getBuild().asString();
                                                                    IdeaPluginDescriptor plugin = getPlugin();
                                                                    String pluginVersion = plugin.getVersion();
                                                                    String pluginId = plugin.getPluginId().getIdString();
                                                                    String os = URLEncoder
                                                                      .encode(SystemInfo.OS_NAME + " " + SystemInfo.OS_VERSION,
                                                                              CharsetToolkit.UTF8);
                                                                    String uid = PermanentInstallationID.get();
                                                                    String url =
                                                                      "https://plugins.jetbrains.com/plugins/list" +
                                                                      "?pluginId=" + pluginId +
                                                                      "&build=" + buildNumber +
                                                                      "&pluginVersion=" + pluginVersion +
                                                                      "&os=" + os +
                                                                      "&uuid=" + uid;
                                                                    PropertiesComponent.getInstance()
                                                                      .setValue(KEY, String.valueOf(System.currentTimeMillis()));
                                                                    HttpRequests.request(url).connect(
                                                                      request ->
                                                                      {
                                                                        try {
                                                                          JDOMUtil.load(request.getReader());
                                                                        }
                                                                        catch (JDOMException ignore) {
                                                                        }
                                                                        return null;
                                                                      }
                                                                    );
                                                                  }
                                                                  catch (Exception ignored) {
                                                                  }
                                                                });
    }
  }
}
