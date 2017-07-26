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

import com.intellij.openapi.application.WriteAction;
import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.components.PersistentStateComponentWithModificationTracker;
import com.intellij.openapi.components.impl.ModulePathMacroManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleExtension;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ex.ProjectRootManagerEx;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.FactoryMap;
import com.intellij.util.containers.Predicate;
import gnu.trove.THashMap;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;
import org.jetbrains.jps.model.serialization.JpsModelSerializerExtension;
import org.jetbrains.jps.model.serialization.module.JpsModuleSourceRootPropertiesSerializer;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PerlModuleExtension extends ModuleExtension implements PersistentStateComponentWithModificationTracker<Element> {
  private static final Logger LOG = Logger.getInstance(PerlModuleExtension.class);
  private static String PERL_CONFIG = "perl5";
  private static String ELEMENT_PATH = "path";
  private static String ATTRIBUTE_VALUE = "value";
  private static String ATTRIBUTE_TYPE = "type";
  private final boolean myIsWritable;
  private long myModificationTracker;
  private PerlModuleExtension myOriginal;
  private Module myModule;
  private static FactoryMap<String, JpsModuleSourceRootPropertiesSerializer> SERIALIZER_BY_ID_MAP =
    FactoryMap.createMap(key -> getSerializer(serializer -> serializer != null && serializer.getTypeId().equals(key)));
  private static FactoryMap<JpsModuleSourceRootType, JpsModuleSourceRootPropertiesSerializer> SERIALIZER_BY_TYPE_MAP =
    FactoryMap.createMap(key -> getSerializer(serializer -> serializer != null && serializer.getType().equals(key)));
  private Map<VirtualFile, JpsModuleSourceRootType> myRoots = new LinkedHashMap<>();

  public PerlModuleExtension(Module module) {
    myModule = module;
    myIsWritable = false;
    myOriginal = null;
  }

  private PerlModuleExtension(@NotNull PerlModuleExtension original, boolean writable) {
    myModule = original.myModule;
    myRoots.putAll(original.myRoots);
    myIsWritable = writable;
    myOriginal = original;
  }

  @Override
  public ModuleExtension getModifiableModel(boolean writable) {
    return new PerlModuleExtension(this, writable);
  }

  @Override
  public void commit() {
    LOG.assertTrue(myOriginal != null, "Attempt to commit non-modifyable model");
    if (isChanged()) {
      synchronized (myOriginal) {
        WriteAction.run(
          () -> ProjectRootManagerEx.getInstanceEx(myModule.getProject()).makeRootsChange(() -> {
            myOriginal.myRoots = new THashMap<>(myRoots);
            myOriginal.myModificationTracker++;
          }, false, true)
        );
      }
    }
  }

  @Override
  public boolean isChanged() {
    return !myRoots.equals(myOriginal.myRoots);
  }

  public synchronized void setRoot(@NotNull VirtualFile root, @NotNull JpsModuleSourceRootType type) {
    LOG.assertTrue(myIsWritable, "Obtain modifiableModel first");
    myRoots.put(root, type);
    myModificationTracker++;
  }

  public synchronized void removeRoot(@NotNull VirtualFile root) {
    LOG.assertTrue(myIsWritable, "Obtain modifiableModel first");
    myRoots.remove(root);
    myModificationTracker++;
  }

  public synchronized List<VirtualFile> getRootsByType(@NotNull JpsModuleSourceRootType type) {
    return myRoots.entrySet().stream()
      .filter(entry -> type.equals(entry.getValue()))
      .map(Map.Entry::getKey)
      .collect(Collectors.toList());
  }

  @Nullable
  public JpsModuleSourceRootType getRootType(@NotNull VirtualFile virtualFile) {
    return myRoots.get(virtualFile);
  }

  @Override
  public void dispose() {
    myOriginal = null;
    myModule = null;
    myRoots = null;
  }

  @Override
  public long getStateModificationCount() {
    return myModificationTracker;
  }

  @Nullable
  @Override
  public Element getState() {
    Element state = new Element("root");
    Element perlConfig = new Element(PERL_CONFIG);
    state.addContent(perlConfig);

    PathMacroManager macroManager = ModulePathMacroManager.getInstance(myModule);

    for (VirtualFile root : myRoots.keySet()) {
      if (!root.isValid() || !root.isDirectory()) {
        continue;
      }
      JpsModuleSourceRootPropertiesSerializer serializer = SERIALIZER_BY_TYPE_MAP.get(myRoots.get(root));
      if (serializer == null) {
        continue;
      }

      String collapsedPath = macroManager.collapsePath(root.getCanonicalPath());
      if (StringUtil.isEmpty(collapsedPath)) {
        continue;
      }

      Element pathElement = new Element(ELEMENT_PATH);
      pathElement.setAttribute(ATTRIBUTE_VALUE, collapsedPath);
      pathElement.setAttribute(ATTRIBUTE_TYPE, serializer.getTypeId());
      perlConfig.addContent(pathElement);
    }
    return state;
  }

  @Override
  public void loadState(Element state) {
    state = state.getChild(PERL_CONFIG);
    myRoots.clear();
    if (state == null) {
      return;
    }
    PathMacroManager macroManager = ModulePathMacroManager.getInstance(myModule);
    for (Element pathElement : state.getChildren(ELEMENT_PATH)) {
      JpsModuleSourceRootPropertiesSerializer serializer = SERIALIZER_BY_ID_MAP.get(pathElement.getAttributeValue(ATTRIBUTE_TYPE));
      if (serializer == null) {
        continue;
      }
      String expandedPath = macroManager.expandPath(pathElement.getAttributeValue(ATTRIBUTE_VALUE));
      VirtualFile libRoot = VfsUtil.findFileByIoFile(new File(expandedPath), true);
      if (libRoot != null && libRoot.isValid() && libRoot.isDirectory()) {
        myRoots.put(libRoot, (JpsModuleSourceRootType)serializer.getType());
      }
    }
  }

  public static PerlModuleExtension getInstance(@NotNull Module module) {
    return ModuleRootManager.getInstance(module).getModuleExtension(PerlModuleExtension.class);
  }

  @Nullable
  private static JpsModuleSourceRootPropertiesSerializer getSerializer(Predicate<JpsModuleSourceRootPropertiesSerializer> predicate) {
    for (JpsModelSerializerExtension extension : JpsModelSerializerExtension.getExtensions()) {
      for (JpsModuleSourceRootPropertiesSerializer<?> serializer : extension.getModuleSourceRootPropertiesSerializers()) {
        if (predicate.apply(serializer)) {
          return serializer;
        }
      }
    }
    return null;
  }
}
