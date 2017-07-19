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

import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.components.PersistentStateComponentWithModificationTracker;
import com.intellij.openapi.components.impl.ModulePathMacroManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleExtension;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PerlModuleExtension extends ModuleExtension implements PersistentStateComponentWithModificationTracker<Element> {
  private static final Logger LOG = Logger.getInstance(PerlModuleExtension.class);
  private static String ELEMENT_PATH = "path";
  private static String ATTRIBUTE_VALUE = "value";
  private final boolean myIsWritable;
  private long myModificationTracker;
  private PerlModuleExtension myOriginal;
  private Module myModule;
  private List<VirtualFile> myLibraries = new ArrayList<>();

  public PerlModuleExtension(Module module) {
    myModule = module;
    myIsWritable = false;
    myOriginal = null;
  }

  private PerlModuleExtension(@NotNull PerlModuleExtension original, boolean writable) {
    myModule = original.myModule;
    myLibraries.addAll(original.myLibraries);
    myIsWritable = writable;
    myOriginal = original;
  }

  @Override
  public ModuleExtension getModifiableModel(boolean writable) {
    return new PerlModuleExtension(this, writable);
  }

  @Override
  public void commit() {
    if (isChanged()) {
      synchronized (myOriginal) {
        myOriginal.myLibraries.clear();
        myOriginal.myLibraries.addAll(myLibraries);
        myModificationTracker++;
      }
    }
  }

  @Override
  public boolean isChanged() {
    return !myLibraries.equals(myOriginal.myLibraries);
  }

  public synchronized void addLibrary(@NotNull VirtualFile library) {
    LOG.assertTrue(myIsWritable, "Obtain modifiableModel first");
    myLibraries.add(library);
    myModificationTracker++;
  }

  public synchronized void setLibraries(@NotNull List<VirtualFile> libraries) {
    LOG.assertTrue(myIsWritable, "Obtain modifiableModel first");
    myLibraries.clear();
    myLibraries.addAll(libraries);
    myModificationTracker++;
  }

  @Override
  public void dispose() {
    myOriginal = null;
    myModule = null;
    myLibraries = null;
  }

  @Override
  public long getStateModificationCount() {
    return myModificationTracker;
  }

  @Nullable
  @Override
  public Element getState() {
    Element state = new Element("libs");
    PathMacroManager macroManager = ModulePathMacroManager.getInstance(myModule);
    for (VirtualFile library : myLibraries) {
      if (!library.isValid() || !library.isDirectory()) {
        continue;
      }
      String collapsedPath = macroManager.collapsePath(library.getCanonicalPath());
      if (StringUtil.isEmpty(collapsedPath)) {
        continue;
      }

      Element pathElement = new Element(ELEMENT_PATH);
      pathElement.setAttribute(ATTRIBUTE_VALUE, collapsedPath);
      state.addContent(pathElement);
    }
    return state;
  }

  @Override
  public void loadState(Element state) {
    myLibraries.clear();
    PathMacroManager macroManager = ModulePathMacroManager.getInstance(myModule);
    for (Element pathElement : state.getChildren(ELEMENT_PATH)) {
      String expandedPath = macroManager.expandPath(pathElement.getAttributeValue(ATTRIBUTE_VALUE));
      VirtualFile libRoot = VfsUtil.findFileByIoFile(new File(expandedPath), true);
      if (libRoot != null) {
        myLibraries.add(libRoot);
      }
    }
  }

  public static PerlModuleExtension getInstance(@NotNull Module module) {
    return ModuleRootManager.getInstance(module).getModuleExtension(PerlModuleExtension.class);
  }
}
