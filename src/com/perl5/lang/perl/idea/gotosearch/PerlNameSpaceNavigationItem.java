/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.gotosearch;

import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx;
import com.intellij.openapi.project.Project;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Navigation item for GoToByClass Feature
 */
public class PerlNameSpaceNavigationItem implements NavigationItem {
    private final Project project;
    private final PsiPerlNamespaceDefinition nameSpace;

    public PerlNameSpaceNavigationItem(Project project, PsiPerlNamespaceDefinition nameSpace) {
        this.project = project;
        this.nameSpace = nameSpace;
    }

    @Nullable
    @Override
    public String getName() {
        return nameSpace.getText();
    }

    @Nullable
    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Nullable
            @Override
            public String getPresentableText() {
                return nameSpace.getName();
            }

            @Nullable
            @Override
            public String getLocationString() {
                return nameSpace.getContainingFile().getVirtualFile().getCanonicalPath();
            }

            @Nullable
            @Override
            public Icon getIcon(boolean b) {
                return PerlIcons.PACKAGE_GUTTER_ICON;
            }
        };
    }

    @Override
    public void navigate(boolean b) {
        FileEditorManagerEx.getInstance(project).openFile(nameSpace.getContainingFile().getVirtualFile(), true);
    }

    @Override
    public boolean canNavigate() {
        return true;
    }

    @Override
    public boolean canNavigateToSource() {
        return true;
    }
}
