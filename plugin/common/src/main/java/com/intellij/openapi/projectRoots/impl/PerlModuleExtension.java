/*
 * Copyright 2015-2025 Alexandr Evstigneev
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
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleExtension;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ex.ProjectRootManagerEx;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.indexing.BuildableRootsChangeRescanningInfo;
import com.perl5.lang.perl.idea.modules.PerlSourceRootType;
import org.jdom.Element;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.jps.model.module.JpsModuleSourceRootType;
import org.jetbrains.jps.model.serialization.JpsModelSerializerExtension;
import org.jetbrains.jps.model.serialization.module.JpsModuleSourceRootPropertiesSerializer;

import java.io.File;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PerlModuleExtension extends ModuleExtension implements PersistentStateComponentWithModificationTracker<Element> {
  private static final Logger LOG = Logger.getInstance(PerlModuleExtension.class);
  private static final String PERL_CONFIG = "perl5";
  private static final String ELEMENT_PATH = "path";
  private static final String ATTRIBUTE_VALUE = "value";
  private static final String ATTRIBUTE_TYPE = "type";
  private final boolean myIsWritable;
  private long myModificationTracker;
  private final PerlModuleExtension myOriginal;
  private final Module myModule;
  private Map<VirtualFile, PerlSourceRootType> myRoots = new LinkedHashMap<>();

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

  @ApiStatus.OverrideOnly
  @Override
  public @NotNull ModuleExtension getModifiableModel(boolean writable) {
    return new PerlModuleExtension(this, writable);
  }

  @Override
  public void commit() {
    LOG.assertTrue(myOriginal != null, "Attempt to commit non-modifiable model");
    if (isChanged()) {
      synchronized (myOriginal) {
        WriteAction.run(
          () -> ProjectRootManagerEx.getInstanceEx(myModule.getProject()).makeRootsChange(() -> {
            myOriginal.myRoots = new LinkedHashMap<>(myRoots);
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

  public synchronized void setRoot(@NotNull VirtualFile root, @NotNull JpsModuleSourceRootType<?> type) {
    LOG.assertTrue(myIsWritable, "Obtain modifiableModel first");
    LOG.assertTrue(type instanceof PerlSourceRootType, type + " is not a PerlSourceRootType");
    myRoots.put(root, (PerlSourceRootType)type);
    myModificationTracker++;
  }

  public synchronized void removeRoot(@NotNull VirtualFile root) {
    LOG.assertTrue(myIsWritable, "Obtain modifiableModel first");
    myRoots.remove(root);
    myModificationTracker++;
  }

  public Map<VirtualFile, PerlSourceRootType> getRoots() {
    return Collections.unmodifiableMap(myRoots);
  }

  public synchronized List<VirtualFile> getRootsByType(@NotNull PerlSourceRootType type) {
    return myRoots.entrySet().stream()
      .filter(entry -> type.equals(entry.getValue()))
      .map(Map.Entry::getKey)
      .collect(Collectors.toList());
  }

  public @Nullable PerlSourceRootType getRootType(@NotNull VirtualFile virtualFile) {
    return myRoots.get(virtualFile);
  }

  @Override
  public long getStateModificationCount() {
    return myModificationTracker;
  }

  @Override
  public @Nullable Element getState() {
    Element perlConfig = new Element(PERL_CONFIG);

    PathMacroManager macroManager = PathMacroManager.getInstance(myModule);

    for (VirtualFile root : myRoots.keySet()) {
      if (!root.isValid() || !root.isDirectory()) {
        continue;
      }
      JpsModuleSourceRootPropertiesSerializer<?> serializer = getSerializer(it -> it != null && it.getType().equals(myRoots.get(root)));
      if (serializer == null) {
        continue;
      }

      String collapsedUrl = macroManager.collapsePath(root.getUrl());
      if (StringUtil.isEmpty(collapsedUrl)) {
        continue;
      }


      Element pathElement = new Element(ELEMENT_PATH);
      pathElement.setAttribute(ATTRIBUTE_VALUE, collapsedUrl);
      pathElement.setAttribute(ATTRIBUTE_TYPE, serializer.getTypeId());
      perlConfig.addContent(pathElement);
    }

    Element root = new Element("root");
    if (!perlConfig.getChildren().isEmpty()) {
      root.addContent(perlConfig);
    }

    return root;
  }

  @Override
  public void loadState(@NotNull Element state) {
    state = state.getChild(PERL_CONFIG);
    myRoots.clear();
    if (state == null) {
      return;
    }
    PathMacroManager macroManager = PathMacroManager.getInstance(myModule);
    for (Element pathElement : state.getChildren(ELEMENT_PATH)) {
      JpsModuleSourceRootPropertiesSerializer<?> serializer =
        getSerializer(it -> it != null && it.getTypeId().equals(pathElement.getAttributeValue(ATTRIBUTE_TYPE)));
      if (serializer == null) {
        continue;
      }
      String expandedPathOrUrl = macroManager.expandPath(pathElement.getAttributeValue(ATTRIBUTE_VALUE));
      var libRoot = VirtualFileManager.getInstance().findFileByUrl(expandedPathOrUrl);
      if (libRoot == null) {
        // fixme migration part, to be removed
        libRoot = VfsUtil.findFileByIoFile(new File(expandedPathOrUrl), false);
      }
      if (libRoot != null && libRoot.isValid() && libRoot.isDirectory()) {
        myRoots.put(libRoot, (PerlSourceRootType)serializer.getType());
      }
    }
  }

  public static PerlModuleExtension getInstance(@NotNull Module module) {
    return ModuleRootManager.getInstance(module).getModuleExtension(PerlModuleExtension.class);
  }

  /**
   * Modifies {@link PerlModuleExtension} for the {@code module} using {@code mutator}. Incapsulates proper logic of working with modifiable
   * model, described in {@link ModuleExtension#getModifiableModel(boolean) javadoc}.
   *
   * @see ModuleExtension#getModifiableModel(boolean)
   */
  public static void modify(@NotNull Module module, @NotNull Consumer<? super PerlModuleExtension> mutator) {
    ModifiableRootModel modifiableModel = ModuleRootManager.getInstance(module).getModifiableModel();
    PerlModuleExtension moduleExtensionModifiableModel = modifiableModel.getModuleExtension(PerlModuleExtension.class);
    try {
      mutator.accept(moduleExtensionModifiableModel);
    }
    catch (Throwable e) {
      LOG.error(e);
      modifiableModel.dispose();
    }
    finally {
      if( moduleExtensionModifiableModel.isChanged()){
        WriteAction.run(modifiableModel::commit);
      }
      else {
        modifiableModel.dispose();
      }
    }
  }

  private static @Nullable JpsModuleSourceRootPropertiesSerializer<?> getSerializer(
    Predicate<? super JpsModuleSourceRootPropertiesSerializer<?>> predicate) {
    for (JpsModelSerializerExtension extension : JpsModelSerializerExtension.getExtensions()) {
      for (JpsModuleSourceRootPropertiesSerializer<?> serializer : extension.getModuleSourceRootPropertiesSerializers()) {
        if (predicate.test(serializer)) {
          return serializer;
        }
      }
    }
    return null;
  }
}
