/*
 * Copyright 2015-2019 Alexandr Evstigneev
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
import com.intellij.openapi.roots.ui.configuration.ModuleSourceRootEditHandler;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.modules.PerlSourceRootType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;

public class PerlTreeStructureProvider implements TreeStructureProvider {
  @NotNull
  private final PerlProjectManager myProjectManager;

  public PerlTreeStructureProvider(Project project) {
    myProjectManager = PerlProjectManager.getInstance(project);
  }

  @NotNull
  @Override
  public Collection<AbstractTreeNode<?>> modify(@NotNull AbstractTreeNode<?> parent,
                                                @NotNull Collection<AbstractTreeNode<?>> children,
                                                ViewSettings settings) {
    if (!myProjectManager.isPerlEnabled()) {
      return children;
    }

    Map<VirtualFile, PerlSourceRootType> roots = myProjectManager.getAllModulesRoots();
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
      if (virtualFile != null && roots.containsKey(virtualFile)) {
        PerlSourceRootType rootType = roots.get(virtualFile);
        ModuleSourceRootEditHandler handler = rootType.getEditHandler();
        node = new PsiDirectoryNode(node.getProject(), ((PsiDirectoryNode)node).getValue(), ((PsiDirectoryNode)node).getSettings()) {
          @Override
          protected void updateImpl(@NotNull PresentationData data) {
            super.updateImpl(data);
            data.setIcon(handler.getRootIcon());
            data.setLocationString(handler.getRootTypeName().toLowerCase());
          }
        };
      }

      return node;
    });
  }
}
