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

package com.perl5.lang.perl.idea.project;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootEvent;
import com.intellij.openapi.roots.ModuleRootListener;
import com.intellij.openapi.util.AtomicNotNullLazyValue;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlIcons;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

import static com.intellij.ProjectTopics.PROJECT_ROOTS;

public class PerlTreeStructureProvider implements TreeStructureProvider {
  private final PerlProjectManager myProjectManager;
  private AtomicNotNullLazyValue<Set<VirtualFile>> myRootsProvider;

  public PerlTreeStructureProvider(Project project) {
    myProjectManager = PerlProjectManager.getInstance(project);
    initProvider();
    project.getMessageBus().connect().subscribe(PROJECT_ROOTS, new ModuleRootListener() {
      @Override
      public void rootsChanged(ModuleRootEvent event) {
        initProvider();
      }
    });
  }

  private void initProvider() {
    myRootsProvider = AtomicNotNullLazyValue.createValue(() -> new THashSet<>(myProjectManager.getAllLibraryRoots()));
  }

  @NotNull
  @Override
  public Collection<AbstractTreeNode> modify(@NotNull AbstractTreeNode parent,
                                             @NotNull Collection<AbstractTreeNode> children,
                                             ViewSettings settings) {
    Set<VirtualFile> roots = myRootsProvider.getValue();
    return ContainerUtil.map2List(children, node -> {
      if (!(node instanceof PsiDirectoryNode)) {
        return node;
      }

      Project nodeProject = node.getProject();
      if (nodeProject == null) {
        return node;
      }

      // fixme support different roots here
      VirtualFile virtualFile = ((PsiDirectoryNode)node).getVirtualFile();
      if (roots.contains(virtualFile)) {
        node = new PsiDirectoryNode(node.getProject(), ((PsiDirectoryNode)node).getValue(), ((PsiDirectoryNode)node).getSettings()) {
          @Override
          protected void updateImpl(PresentationData data) {
            super.updateImpl(data);
            data.setIcon(PerlIcons.LIB_ROOT);
            data.addText(virtualFile.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
            data.addText(" (perl5 libs)", SimpleTextAttributes.GRAY_ATTRIBUTES);
          }
        };
      }

      return node;
    });
  }
}
