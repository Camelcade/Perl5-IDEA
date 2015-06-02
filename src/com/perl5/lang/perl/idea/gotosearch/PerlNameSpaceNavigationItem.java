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
                return nameSpace.getText();
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
