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

package com.perl5.lang.pod.parser.psi.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.Processor;
import com.perl5.lang.perl.fileTypes.PerlFileTypePackage;
import com.perl5.lang.perl.idea.project.PerlProjectManager;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlPackageUtilCore;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.filetypes.PodFileType;
import com.perl5.lang.pod.parser.psi.PodLinkDescriptor;
import com.perl5.lang.pod.parser.psi.mixin.PodFormatterL;
import com.perl5.lang.pod.parser.psi.references.PodLinkToFileReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PodFileUtil {
  public static final String PM_OR_POD_EXTENSION_PATTERN = ".(" + PodFileType.EXTENSION + "|" + PerlFileTypePackage.EXTENSION + ")$";

  public static @Nullable String getPackageName(PsiFile file) {
    VirtualFile virtualFile = file.getVirtualFile();
    if (virtualFile == null) {
      return null;
    }

    var fileExtension = virtualFile.getExtension();
    if (!PodFileType.EXTENSION.equals(fileExtension) && !PerlFileTypePackage.EXTENSION.equals(fileExtension)) {
      return null;
    }

    VirtualFile classRoot = PerlPackageUtil.getClosestIncRoot(file);
    return classRoot == null ? virtualFile.getName() : getPackageNameFromVirtualFile(virtualFile, classRoot);
  }

  /**
   * Searching perl file related to specified pod file
   *
   * @param podFile pod psi file
   * @return perl psi file if found
   */
  public static @Nullable PsiFile getTargetPerlFile(PsiFile podFile) {
    if (podFile == null) {
      return null;
    }

    final PsiFile baseFile = podFile.getViewProvider().getStubBindingRoot();
    if (baseFile != podFile) {
      return baseFile;
    }

    String packageName = getPackageName(baseFile);
    Project project = baseFile.getProject();
    if (StringUtil.isNotEmpty(packageName)) {
      PsiFile packageFileByName = PerlPackageUtil.getPackagePsiFileByPackageName(project, packageName);
      if (packageFileByName != null) {
        return packageFileByName;
      }
    }

    VirtualFile baseVirtualFile = PsiUtilCore.getVirtualFile(baseFile);
    if (baseVirtualFile == null) {
      return null;
    }
    VirtualFile containingDirectory = baseVirtualFile.getParent();
    if (containingDirectory == null) {
      return null;
    }
    String neighborPackageFileName = baseVirtualFile.getNameWithoutExtension() + "." + PerlFileTypePackage.EXTENSION;
    VirtualFile neighborPackageFile = containingDirectory.findChild(neighborPackageFileName);
    return neighborPackageFile == null ? null : PsiManager.getInstance(project).findFile(neighborPackageFile);
  }

  public static @Nullable String getPackageNameFromVirtualFile(VirtualFile file, VirtualFile classRoot) {
    String relativePath = VfsUtilCore.getRelativePath(file, classRoot);
    if (relativePath != null) {
      return StringUtil.join(relativePath.replaceAll(PM_OR_POD_EXTENSION_PATTERN, "").split("/"), PerlPackageUtilCore.NAMESPACE_SEPARATOR);
    }
    return null;
  }

  public static String getFilenameFromPackage(@NotNull String packageName) {
    return StringUtil.join(PerlPackageUtilCore.getCanonicalNamespaceName(packageName).split(PerlPackageUtilCore.NAMESPACE_SEPARATOR), "/") +
           "." +
           PodFileType.EXTENSION;
  }

  public static @Nullable PsiFile getPodOrPackagePsiByDescriptor(Project project, PodLinkDescriptor descriptor) {
    final List<PsiFile> result = new ArrayList<>();

    PodFileUtil.processPodFilesByDescriptor(project, descriptor, psiFile -> {
      if (psiFile != null) {
        if (psiFile.getFileType() == PodFileType.INSTANCE) {
          result.clear();
          result.add(psiFile);
          return false;
        }
        else if ((psiFile = psiFile.getViewProvider().getPsi(PodLanguage.INSTANCE)) != null) {
          result.add(psiFile);
        }
      }
      return true;
    });

    return result.isEmpty() ? null : result.getFirst();
  }

  public static void processPodFilesByDescriptor(@NotNull Project project,
                                                 @NotNull PodLinkDescriptor descriptor,
                                                 @NotNull Processor<? super PsiFile> processor) {
    if (descriptor.getName() == null) {
      return;
    }
    // seek file
    String fileId = descriptor.getName();

    final PsiManager psiManager = PsiManager.getInstance(project);
    if (fileId.contains(PerlPackageUtilCore.NAMESPACE_SEPARATOR) ||
        !StringUtil.startsWith(fileId, "perl")) { // can be Foo/Bar.pod or Foo/Bar.pm
      String podRelativePath = PodFileUtil.getFilenameFromPackage(fileId);
      String packageRelativePath = PerlPackageUtilCore.getPackagePathByName(fileId);

      for (VirtualFile classRoot : PerlProjectManager.getInstance(project).getAllLibraryRoots()) {
        VirtualFile targetVirtualFile = classRoot.findFileByRelativePath(podRelativePath);
        if (targetVirtualFile != null) {
          if (!processor.process(psiManager.findFile(targetVirtualFile))) {
            return;
          }
        }

        targetVirtualFile = classRoot.findFileByRelativePath(packageRelativePath);
        if (targetVirtualFile != null) {
          if (!processor.process(psiManager.findFile(targetVirtualFile))) {
            return;
          }
        }
      }
    }
    else { // top level file perl.*
      fileId += "." + PodFileType.EXTENSION;

      for (var podFile : FilenameIndex.getVirtualFilesByName(fileId, GlobalSearchScope.allScope(project))) {
        if (!processor.process(psiManager.findFile(podFile))) {
          return;
        }
      }
    }
  }

  public static @Nullable PsiFile getTargetFile(@NotNull PodFormatterL linkFormatter) {
    PsiReference[] references = linkFormatter.getReferences();

    for (PsiReference reference : references) {
      if (reference instanceof PodLinkToFileReference) {
        return (PsiFile)reference.resolve();
      }
    }

    return linkFormatter.getContainingFile();
  }

}
