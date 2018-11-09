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

package com.perl5.lang.perl.idea.annotators;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.BaseProcessHandler;
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.ExternalAnnotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilCore;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.idea.configuration.settings.PerlSharedSettings;
import com.perl5.lang.perl.idea.execution.PerlCommandLine;
import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import com.perl5.lang.perl.psi.PerlFile;
import com.perl5.lang.perl.util.PerlRunUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by hurricup on 16.04.2016.
 */
public class PerlCriticAnnotator extends ExternalAnnotator<PerlFile, List<PerlCriticErrorDescriptor>> {
  private static final String SCRIPT_NAME = "perlcritic";
  private static final String PACKAGE_NAME = "Perl::Critic";
  private static final Logger LOG = Logger.getInstance(PerlCriticAnnotator.class);

  @Nullable
  @Override
  public PerlFile collectInformation(@NotNull PsiFile file) {
    return file instanceof PerlFile && file.isPhysical() && PerlSharedSettings.getInstance(file.getProject()).PERL_CRITIC_ENABLED
           ? (PerlFile)file : null;
  }

  @Nullable
  protected PerlCommandLine getPerlCriticCommandLine(Project project) throws ExecutionException {
    PerlSharedSettings sharedSettings = PerlSharedSettings.getInstance(project);
    VirtualFile perlCriticScript =
      ReadAction.compute(() -> PerlRunUtil.findLibraryScriptWithNotification(project, SCRIPT_NAME, PACKAGE_NAME));
    if (perlCriticScript == null) {
      return null;
    }
    PerlCommandLine commandLine = PerlRunUtil.getPerlCommandLine(project, perlCriticScript);
    if (commandLine == null) {
      return null;
    }
    commandLine.withWorkDirectory(project.getBasePath());

    if (StringUtil.isNotEmpty(sharedSettings.PERL_CRITIC_ARGS)) {
      commandLine.addParameters(StringUtil.split(sharedSettings.PERL_CRITIC_ARGS, " "));
    }

    return commandLine;
  }


  @Nullable
  @Override
  public List<PerlCriticErrorDescriptor> doAnnotate(final PerlFile sourcePsiFile) {
    if (sourcePsiFile == null) {
      return null;
    }

    VirtualFile virtualFile = PsiUtilCore.getVirtualFile(sourcePsiFile);
    if (virtualFile == null) {
      return null;
    }

    byte[] sourceBytes = ReadAction.compute(sourcePsiFile::getPerlContentInBytes);
    if (sourceBytes == null) {
      return null;
    }

    try {
      PerlCommandLine criticCommandLine = getPerlCriticCommandLine(sourcePsiFile.getProject());
      if (criticCommandLine == null) {
        PerlSharedSettings.getInstance(sourcePsiFile.getProject()).PERL_CRITIC_ENABLED = false;
        return null;
      }

      BaseProcessHandler processHandler = PerlHostData.createProcessHandler(
        criticCommandLine.withCharset(virtualFile.getCharset())
      );

      OutputStream outputStream = Objects.requireNonNull(processHandler.getProcessInput());
      outputStream.write(sourceBytes);
      outputStream.close();

      List<PerlCriticErrorDescriptor> errors = new ArrayList<>();
      PerlCriticErrorDescriptor lastDescriptor = null;
      for (String output : PerlHostData.getOutput(processHandler).getStdoutLines()) {
        PerlCriticErrorDescriptor fromString = PerlCriticErrorDescriptor.getFromString(output);
        if (fromString != null) {
          errors.add(lastDescriptor = fromString);
        }
        else if (lastDescriptor != null) {
          lastDescriptor.append(" " + output);
        }
        else if (!StringUtil.equals(output, "source OK")) {
          LOG.warn("Could not parse line: " + output);
        }
      }
      return errors;
    }
    catch (Exception e) {
      LOG.error("Error running perlcritic", e);

      Notifications.Bus.notify(new Notification(
        PerlBundle.message("perl.critic.notification.group"),
        PerlBundle.message("perl.critic.execution.error.title"),
        PerlBundle.message("perl.critic.execution.error.message", e.getMessage()),
        NotificationType.ERROR
      ));
      PerlSharedSettings.getInstance(sourcePsiFile.getProject()).PERL_CRITIC_ENABLED = false;
    }
    return null;
  }

  @Override
  public void apply(@NotNull PsiFile file, List<PerlCriticErrorDescriptor> annotationResult, @NotNull AnnotationHolder holder) {
    if (annotationResult == null) {
      return;
    }

    Document document = file.getViewProvider().getDocument();
    if (document != null) {
      for (PerlCriticErrorDescriptor descriptor : annotationResult) {
        int line = descriptor.getLine();
        int col = descriptor.getCol();

        TextRange warningRange = null;
        if (col == 1) // suppose it's stupid output without column
        {
          warningRange = new TextRange(document.getLineStartOffset(line - 1), document.getLineEndOffset(line - 1));
        }
        else // trying to find real element
        {
          int offset = document.getLineStartOffset(line - 1) + col;
          PsiElement targetElement = file.findElementAt(offset);
          if (targetElement != null) {
            warningRange = targetElement.getTextRange();
          }
          else {
            LOG.warn("Error creating annotation for " + descriptor);
          }
        }

        if (warningRange != null) {
          holder.createAnnotation(HighlightSeverity.WARNING, warningRange, descriptor.getMessage());
        }
      }
    }
  }
}
