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

package com.perl5.lang.perl.idea.coverage;

import com.intellij.coverage.*;
import com.intellij.coverage.view.CoverageViewExtension;
import com.intellij.coverage.view.CoverageViewManager;
import com.intellij.coverage.view.DirectoryCoverageViewExtension;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.coverage.CoverageEnabledConfiguration;
import com.intellij.execution.testframework.AbstractTestProxy;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.fileTypes.PurePerlFileType;
import com.perl5.lang.perl.idea.run.PerlConfiguration;
import com.perl5.lang.perl.psi.PerlFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class PerlCoverageEngine extends CoverageEngine {

  @Override
  public boolean isApplicableTo(@Nullable RunConfigurationBase conf) {
    return conf instanceof PerlConfiguration;
  }

  @Override
  public boolean canHavePerTestCoverage(@Nullable RunConfigurationBase conf) {
    return false;
  }

  @NotNull
  @Override
  public CoverageEnabledConfiguration createCoverageEnabledConfiguration(@Nullable RunConfigurationBase conf) {
    return new PerlCoverageEnabledConfiguration(conf);
  }

  @Nullable
  @Override
  public CoverageSuite createCoverageSuite(@NotNull CoverageRunner covRunner,
                                           @NotNull String name,
                                           @NotNull CoverageFileProvider coverageDataFileProvider,
                                           @Nullable String[] filters,
                                           long lastCoverageTimeStamp,
                                           @Nullable String suiteToMerge,
                                           boolean coverageByTestEnabled,
                                           boolean tracingEnabled,
                                           boolean trackTestFolders,
                                           Project project) {
    return new PerlCoverageSuite(name, coverageDataFileProvider, lastCoverageTimeStamp, coverageByTestEnabled, tracingEnabled,
                                 trackTestFolders, covRunner, project);
  }

  @Nullable
  @Override
  public CoverageSuite createCoverageSuite(@NotNull CoverageRunner covRunner,
                                           @NotNull String name,
                                           @NotNull CoverageFileProvider coverageDataFileProvider,
                                           @NotNull CoverageEnabledConfiguration config) {
    if (config instanceof PerlCoverageEnabledConfiguration) {
      PerlConfiguration perlRunConfiguration = (PerlConfiguration)config.getConfiguration();
      return createCoverageSuite(covRunner, name, coverageDataFileProvider, null, new Date().getTime(), null, false, false, true,
                                 perlRunConfiguration.getProject());
    }
    return null;
  }

  @Nullable
  @Override
  public CoverageSuite createEmptyCoverageSuite(@NotNull CoverageRunner coverageRunner) {
    return new PerlCoverageSuite();
  }

  @NotNull
  @Override
  public CoverageAnnotator getCoverageAnnotator(Project project) {
    return PerlCoverageAnnotator.getInstance(project);
  }

  @Override
  public boolean coverageEditorHighlightingApplicableTo(@NotNull PsiFile psiFile) {
    return isOurFile(psiFile.getVirtualFile());
  }

  @Override
  public boolean coverageProjectViewStatisticsApplicableTo(VirtualFile fileOrDir) {
    return isOurFile(fileOrDir);
  }

  @Override
  public CoverageViewExtension createCoverageViewExtension(Project project,
                                                           CoverageSuitesBundle suiteBundle,
                                                           CoverageViewManager.StateBean stateBean) {
    return new DirectoryCoverageViewExtension(project, getCoverageAnnotator(project), suiteBundle, stateBean);
  }

  @Override
  public boolean acceptedByFilters(@NotNull PsiFile psiFile, @NotNull CoverageSuitesBundle suite) {
    return true;
  }

  @Override
  public boolean recompileProjectAndRerunAction(@NotNull Module module,
                                                @NotNull CoverageSuitesBundle suite,
                                                @NotNull Runnable chooseSuiteAction) {
    return false;
  }

  @NotNull
  @Override
  public Set<String> getQualifiedNames(@NotNull PsiFile sourceFile) {
    return Collections.singleton(buildQualifiedName(sourceFile));
  }

  @Nullable
  @Override
  public String getQualifiedName(@NotNull File outputFile, @NotNull PsiFile sourceFile) {
    return buildQualifiedName(sourceFile);
  }

  private static String buildQualifiedName(@NotNull PsiFile sourceFile) {
    return FileUtil.toSystemIndependentName(sourceFile.getVirtualFile().getPath());
  }

  @Override
  public List<PsiElement> findTestsByNames(@NotNull String[] testNames, @NotNull Project project) {
    return Collections.emptyList();
  }

  @Nullable
  @Override
  public String getTestMethodName(@NotNull PsiElement element, @NotNull AbstractTestProxy testProxy) {
    return null;
  }

  @Override
  public String getPresentableText() {
    return "PerlCoverage";
  }

  public static PerlCoverageEngine getInstance() {
    return Extensions.findExtension(CoverageEngine.EP_NAME, PerlCoverageEngine.class);
  }

  private static boolean isOurFile(@Nullable VirtualFile virtualFile) {
    return virtualFile != null && virtualFile.getFileType() instanceof PurePerlFileType;
  }
}
