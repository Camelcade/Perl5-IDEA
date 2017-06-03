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

package com.perl5.lang.pod.parser.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.PsiWhiteSpace;
import com.perl5.lang.pod.filetypes.PodFileType;
import com.perl5.lang.pod.parser.psi.impl.PodFileImpl;

/**
 * Created by hurricup on 10.04.2016.
 */
public class PodElementFactory {
  public static String getHeaderText(Project project, String text) {
    PodFileImpl file = createFile(project, "=head1 " + text + "\n\n");
    PsiElement section = file.getFirstChild();
    assert section instanceof PodSectionH1;
    return ((PodSectionH1)section).getTitleText();
  }

  public static PsiElement getSpace(Project project) {
    PodFileImpl file = createFile(project, " ");
    PsiElement space = file.getFirstChild();
    assert space instanceof PsiWhiteSpace;
    return space;
  }

  public static PodFileImpl createFile(Project project, String text) {
    return (PodFileImpl)PsiFileFactory.getInstance(project).
      createFileFromText("file.dummy", PodFileType.INSTANCE, text);
  }
}
