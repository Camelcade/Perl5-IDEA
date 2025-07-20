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

package com.perl5.lang.mason2.psi.impl

import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StubIndex
import com.intellij.util.Processor
import com.perl5.lang.htmlmason.MasonCoreUtil
import com.perl5.lang.mason2.Mason2Constants.MASON_DEFAULT_COMPONENT_PARENT
import com.perl5.lang.mason2.Mason2Util
import com.perl5.lang.mason2.idea.configuration.MasonSettings
import com.perl5.lang.mason2.psi.MasonNamespaceDefinition
import com.perl5.lang.mason2.psi.stubs.MasonNamespaceDefitnitionsStubIndex
import com.perl5.lang.mason2.psi.stubs.MasonParentNamespacesStubIndex
import com.perl5.lang.perl.psi.PerlDefaultBackendNamespaceDefinitionHandler
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionHandler
import com.perl5.lang.perl.util.PerlFileUtil
import com.perl5.lang.perl.util.PerlNamespaceUtil


class MasonBackendNamespaceDefinitionHandler : PerlDefaultBackendNamespaceDefinitionHandler<MasonNamespaceDefinitionImpl>() {
  override fun getParentNamespaceDefinitions(namespace: MasonNamespaceDefinitionImpl): List<PerlNamespaceDefinitionElement> {
    val parentsPaths: MutableList<String?> = namespace.getParentNamespacesNames()

    val containingFile = MasonCoreUtil.getContainingVirtualFile(namespace.containingFile)
    val parentsNamespaces: MutableList<PerlNamespaceDefinitionElement>

    if (!parentsPaths.isEmpty() && containingFile != null) {
      parentsNamespaces = Mason2Util.collectComponentNamespacesByPaths(namespace.getProject(), parentsPaths, containingFile.parent)
    }
    else {
      val autobaseParent: String? = namespace.getParentNamespaceFromAutobase()
      if (autobaseParent != null) {
        parentsNamespaces = Mason2Util.getMasonNamespacesByAbsolutePath(namespace.getProject(), autobaseParent)
      }
      else {
        parentsNamespaces = mutableListOf()
      }
    }

    if (parentsNamespaces.isEmpty()) {
      parentsNamespaces.addAll(
        PerlNamespaceUtil.getNamespaceDefinitions(
          namespace.getProject(),
          namespace.resolveScope,
          MASON_DEFAULT_COMPONENT_PARENT
        )
      )
    }

    return parentsNamespaces
  }

  override fun getChildNamespaceDefinitions(namespace: MasonNamespaceDefinitionImpl): List<PerlNamespaceDefinitionElement> {
    val project = namespace.getProject()
    val masonSettings = MasonSettings.getInstance(project)
    val childNamespaces: MutableList<PerlNamespaceDefinitionElement> = mutableListOf()


    // collect psi children
    val projectScope = GlobalSearchScope.projectScope(project)
    val componentPath: String? = namespace.getComponentPath()
    if (componentPath != null) {
      val relativePaths: MutableList<String> = mutableListOf()
      val exactPaths: MutableList<String> = mutableListOf()

      StubIndex.getInstance().processAllKeys(MasonParentNamespacesStubIndex.KEY, project, Processor { parentPath ->
        if (parentPath[0] == VfsUtil.VFS_SEPARATOR_CHAR) { // absolute path, should be equal
          if (componentPath == parentPath.substring(1)) {
            exactPaths.add(parentPath)
          }
        }
        else if (componentPath.endsWith(parentPath)) {  // relative path
          relativePaths.add(parentPath)
        }
        true
      })

      for (parentPath in exactPaths) {
        childNamespaces.addAll(
          StubIndex.getElements(
            MasonParentNamespacesStubIndex.KEY,
            parentPath,
            project,
            projectScope,
            MasonNamespaceDefinition::class.java
          )
        )
      }

      for (parentPath in relativePaths) {
        for (masonNamespaceDefinition in StubIndex.getElements(
          MasonParentNamespacesStubIndex.KEY,
          parentPath,
          project,
          projectScope,
          MasonNamespaceDefinition::class.java
        )) {
          if (PerlNamespaceDefinitionHandler.instance(masonNamespaceDefinition)
              .getParentNamespaceDefinitions(masonNamespaceDefinition).contains(namespace)
          ) {
            childNamespaces.add(masonNamespaceDefinition)
          }
        }
      }
    }


    // collect autobased children
    if (masonSettings.autobaseNames.contains(namespace.containingFile.name)) {
      val containingFile = MasonCoreUtil.getContainingVirtualFile(namespace.containingFile)
      if (containingFile != null) {
        val basePath = PerlFileUtil.getPathRelativeToContentRoot(containingFile.parent, project)

        if (basePath != null) {
          val componentPaths: MutableList<String> = ArrayList()
          StubIndex.getInstance().processAllKeys(
            MasonNamespaceDefitnitionsStubIndex.KEY, project, Processor { componentPath ->
              if (componentPath.startsWith(basePath)) {
                componentPaths.add(componentPath)
              }
              true
            }
          )

          for (compoPath in componentPaths) {
            for (namespaceDefinition in StubIndex.getElements(
              MasonNamespaceDefitnitionsStubIndex.KEY,
              compoPath,
              project,
              projectScope,
              MasonNamespaceDefinition::class.java
            )) {
              if (PerlNamespaceDefinitionHandler.instance(namespaceDefinition)
                  .getParentNamespaceDefinitions(namespaceDefinition).contains(namespace)
                && !childNamespaces.contains(namespaceDefinition)
              ) {
                childNamespaces.add(namespaceDefinition)
              }
            }
          }
        }
      }
    }
    return childNamespaces
  }
}