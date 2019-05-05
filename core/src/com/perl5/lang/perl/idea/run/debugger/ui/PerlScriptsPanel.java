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

package com.perl5.lang.perl.idea.run.debugger.ui;

import com.intellij.ide.actions.OpenFileAction;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.FileColorManager;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.SortedListModel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.perl5.lang.perl.fileTypes.PerlFileTypeScript;
import com.perl5.lang.perl.idea.run.debugger.PerlDebugThread;
import com.perl5.lang.perl.idea.run.debugger.PerlRemoteFileSystem;
import com.perl5.lang.perl.idea.run.debugger.protocol.PerlLoadedFileDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PerlScriptsPanel extends JPanel {
  private static final Comparator<PerlLoadedFileDescriptor> compareEntries =
    (o1, o2) -> StringUtil.compare(o1.getPresentableName(), o2.getPresentableName(), false);
  @NotNull
  private final Project myProject;
  private final PerlDebugThread myDebugThread;
  private final SortedListModel<PerlLoadedFileDescriptor> myModel = SortedListModel.create(compareEntries);

  public PerlScriptsPanel(@NotNull Project project, PerlDebugThread debugThread) {
    super(new BorderLayout());
    init();
    myProject = project;
    myDebugThread = debugThread;
  }


  @Nullable
  private VirtualFile getVirtualFileByName(String virtualFileName) {
    VirtualFile result = VfsUtil.findFileByIoFile(new File(virtualFileName), true);

    if (result != null) {
      return result;
    }

    return VirtualFileManager.getInstance().findFileByUrl(PerlRemoteFileSystem.PROTOCOL_PREFIX + virtualFileName);
  }

  private void init() {
    final JBList jbList = new JBList(myModel);
    jbList.setCellRenderer(new ListCellRendererWrapper<PerlLoadedFileDescriptor>() {
      @Override
      public void customize(JList list, PerlLoadedFileDescriptor fileDescriptor, int index, boolean selected, boolean hasFocus) {
        String remotePath = fileDescriptor.getPath();
        String localPath = myDebugThread.getDebugProfileState().mapPathToLocal(remotePath);
        VirtualFile virtualFile = getVirtualFileByName(localPath);

        setIcon(PerlFileTypeScript.INSTANCE.getIcon());
        setText(fileDescriptor.getPresentableName());

        if (virtualFile != null) {
          setBackground(FileColorManager.getInstance(myProject).getFileColor(virtualFile));
          setText(fileDescriptor.getPresentableName());
          setIcon(virtualFile.getFileType().getIcon());
        }
      }
    });
    jbList.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
          PerlLoadedFileDescriptor fileDescriptor = (PerlLoadedFileDescriptor)jbList.getSelectedValue();
          String remotePath = fileDescriptor.getPath();
          String localPath = myDebugThread.getDebugProfileState().mapPathToLocal(remotePath);
          VirtualFile selectedVirtualFile = getVirtualFileByName(localPath);
          if (selectedVirtualFile == null) {
            selectedVirtualFile = myDebugThread.loadRemoteSource(remotePath);
          }

          if (selectedVirtualFile != null) {
            OpenFileAction.openFile(selectedVirtualFile, myProject);
          }
        }
      }
    });

    add(new JBScrollPane(jbList), BorderLayout.CENTER);
  }

  public void clear() {
    myModel.clear();
  }

  public void add(final PerlLoadedFileDescriptor value) {
    ApplicationManager.getApplication().invokeLater(() -> {
      if (myModel.indexOf(value) == -1) {
        myModel.add(value);
      }
    });
  }

  public void remove(final PerlLoadedFileDescriptor value) {
    ApplicationManager.getApplication().invokeLater(() -> {
      if (myModel.indexOf(value) != -1) {
        myModel.remove(value);
      }
    });
  }

  public void bulkChange(final List<PerlLoadedFileDescriptor> toAdd, final List<PerlLoadedFileDescriptor> toRemove) {
    // based on synthetic benchmarks, at 5000 items the performance of bulkChangeNow is definitely
    // better than the naive method; the axact number might still need some tweaking
    if (toAdd.size() + toRemove.size() < 5000) {
      for (PerlLoadedFileDescriptor value : toRemove) {
        remove(value);
      }
      for (PerlLoadedFileDescriptor value : toAdd) {
        add(value);
      }
    }
    else {
      ApplicationManager.getApplication().invokeLater(() -> bulkChangeNow(toAdd, toRemove));
    }
  }

  // so the naive version is good for "small" added/removed; where the exact value needs to be determined with benchamrks;
  // there is a slightly more detailed analysis in the commit message
  private void bulkChangeNow(final List<PerlLoadedFileDescriptor> toAdd,
                             final List<PerlLoadedFileDescriptor> toRemove) {
    List<PerlLoadedFileDescriptor> currentEntries = myModel.getItems();
    if (toRemove.size() > 0) {
      // first find all indices to be removed, then remove them in reverse order by overwriting with
      // the last element and then removing the last element
      int removeSize = toRemove.size();
      int[] indices = new int[removeSize];
      for (int i = 0; i < removeSize; ++i) {
        indices[i] = Collections.binarySearch(currentEntries, toRemove.get(i), compareEntries);
      }
      Arrays.sort(indices);
      int lastIndex = currentEntries.size() - 1;
      for (int i = removeSize - 1; i >= 0; --i) {
        int index = indices[i];
        if (index >= 0) {
          currentEntries.set(index, currentEntries.get(lastIndex));
          currentEntries.remove(lastIndex);
          --lastIndex;
        }
      }
    }
    currentEntries.addAll(toAdd);
    myModel.setAll(currentEntries);
  }
}