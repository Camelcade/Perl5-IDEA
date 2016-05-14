/*
 * Copyright 2016 Alexandr Evstigneev
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
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.FileColorManager;
import com.intellij.ui.ListCellRendererWrapper;
import com.intellij.ui.SortedListModel;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;

/**
 * Created by hurricup on 14.05.2016.
 */
public class JavaScriptListPanel extends JPanel
{
	@NotNull
	private final Project myProject;
	private SortedListModel<VirtualFile> myModel = SortedListModel.create(new Comparator<VirtualFile>()
	{
		@Override
		public int compare(VirtualFile o1, VirtualFile o2)
		{
			return FileUtil.comparePaths(o1.getPath(), o2.getPath());
		}
	});

	public JavaScriptListPanel(@NotNull Project project)
	{
		super(new BorderLayout());
		init();
		myProject = project;
	}

	private void init()
	{
		final JBList jbList = new JBList(myModel);
		jbList.setCellRenderer(new ListCellRendererWrapper<VirtualFile>()
		{
			@Override
			public void customize(JList list, VirtualFile virtualFile, int index, boolean selected, boolean hasFocus)
			{
				if (virtualFile == null)
				{
					setText("<invalid>");
				}
				else
				{
					setBackground(FileColorManager.getInstance(myProject).getFileColor(virtualFile));
					setText(virtualFile.getPath());
					setIcon(virtualFile.getFileType().getIcon());
				}
			}
		});
		jbList.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
				{
					VirtualFile selectedVirtualFile = (VirtualFile) jbList.getSelectedValue();
					if (selectedVirtualFile == null)
					{
						return;
					}

					OpenFileAction.openFile(selectedVirtualFile, myProject);
				}
			}
		});

		add(new JBScrollPane(jbList), BorderLayout.CENTER);
	}


	public void add(VirtualFile value)
	{
		myModel.add(value);
	}
}