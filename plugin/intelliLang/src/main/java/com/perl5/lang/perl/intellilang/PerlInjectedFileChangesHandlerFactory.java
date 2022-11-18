/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

package com.perl5.lang.perl.intellilang;

import com.intellij.injected.editor.InjectedFileChangesHandler;
import com.intellij.injected.editor.InjectedFileChangesHandlerProvider;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.util.ProperTextRange;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.impl.source.tree.injected.changesHandler.CommonInjectedFileChangesHandler;
import com.intellij.psi.impl.source.tree.injected.changesHandler.MarkersMapping;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApiStatus.ScheduledForRemoval(inVersion = "2020.3")
@Deprecated
public class PerlInjectedFileChangesHandlerFactory implements InjectedFileChangesHandlerProvider {
  @Override
  public InjectedFileChangesHandler createFileChangesHandler(List<? extends PsiLanguageInjectionHost.Shred> shreds,
                                                             Editor hostEditor,
                                                             Document newDocument,
                                                             PsiFile injectedFile) {
    return new PerlInjectedChangesHandler(shreds, hostEditor, newDocument, injectedFile);
  }

  private static class PerlInjectedChangesHandler extends CommonInjectedFileChangesHandler {
    public PerlInjectedChangesHandler(@NotNull List<? extends PsiLanguageInjectionHost.Shred> shreds,
                                      @NotNull Editor hostEditor,
                                      @NotNull Document fragmentDocument,
                                      @NotNull PsiFile injectedFile) {
      super(shreds, hostEditor, fragmentDocument, injectedFile);
    }

    @Override
    public void commitToOriginal(@NotNull DocumentEvent e) {
      var text = myFragmentDocument.getText();
      var map = ContainerUtil.groupBy(getMarkers(), MarkersMapping::getHost);

      var documentManager = PsiDocumentManager.getInstance(myProject);
      documentManager.commitDocument(myHostDocument);

      for (PsiLanguageInjectionHost host : map.keySet()) {
        if (host == null) {
          continue;
        }
        var hostRange = host.getTextRange();
        var hostOffset = hostRange.getStartOffset();
        var originalText = host.getText();
        var currentHost = host;
        var mappings = new ArrayList<>(map.get(host));
        if (mappings.isEmpty()) {
          continue;
        }
        Collections.reverse(mappings);

        for (var mapping : mappings) {
          var hostMarker = mapping.getHostMarker();
          var fragmentMarker = mapping.getFragmentMarker();
          var localInsideHost = new ProperTextRange(hostMarker.getStartOffset() - hostOffset, hostMarker.getEndOffset() - hostOffset);
          var localInsideFile = new ProperTextRange(fragmentMarker.getStartOffset(), fragmentMarker.getEndOffset());

          // fixme we could optimize here and check if host text has been changed and update only really changed fragments, not all of them
          if (currentHost != null && localInsideFile.getEndOffset() <= text.length() && !localInsideFile.isEmpty()) {
            var decodedText = localInsideFile.substring(text);
            currentHost = ElementManipulators.handleContentChange(currentHost, localInsideHost, decodedText);
            if (currentHost == null) {
              failAndReport("Updating host returned null. Original host" + host +
                            "; original text: " + originalText +
                            "; updated range in host: " + localInsideHost +
                            "; decoded text to replace: " + decodedText, e, null);
            }
          }
        }
      }
    }
  }
}
