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

package com.perl5.lang.mojolicious.idea.projectView;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.TreeStructureProvider;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.mojolicious.model.MojoProject;
import com.perl5.lang.mojolicious.model.MojoProjectManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class MojoTreeStructureProvider implements TreeStructureProvider {
  @NotNull
  @Override
  public Collection<AbstractTreeNode> modify(@NotNull AbstractTreeNode parent,
                                             @NotNull Collection<AbstractTreeNode> children,
                                             ViewSettings settings) {
    return ContainerUtil.map(children, node -> {
      if (!(node instanceof PsiDirectoryNode)) {
        return node;
      }
      Project project = node.getProject();
      if (project == null || project.isDefault()) {
        return node;
      }

      MojoProject mojoProject = MojoProjectManager.getInstance(project).getMojoProject(((PsiDirectoryNode)node).getVirtualFile());
      if (mojoProject == null) {
        return node;
      }

      return new PsiDirectoryNode(project, ((PsiDirectoryNode)node).getValue(), ((PsiDirectoryNode)node).getSettings()) {
        @Override
        protected void updateImpl(@NotNull PresentationData data) {
          super.updateImpl(data);
          data.setIcon(mojoProject.getIcon());
          data.setLocationString(mojoProject.getTypeName().toLowerCase());
        }
      };
    });
  }
}
