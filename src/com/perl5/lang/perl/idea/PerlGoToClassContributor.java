package com.perl5.lang.perl.idea;

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.GotoClassContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * GoToClassContributor looks up namespaces (packages names) - windows shortcut Ctrl+N
 */
public class PerlGoToClassContributor implements ChooseByNameContributor, GotoClassContributor {
    @Nullable
    @Override
    public String getQualifiedName(NavigationItem navigationItem) {
        return navigationItem.getName();
    }

    @Nullable
    @Override
    public String getQualifiedNameSeparator() {
        return PerlPackageUtil.PACKAGE_SEPARATOR;
    }

    @NotNull
    @Override
    public String[] getNames(Project project, boolean b) {
        return PerlPackageUtil.listDefinedPackageNames(project).toArray(new String[]{});
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String packageName, String searchTerm, Project project, boolean b) {
        PerlNamespaceDefinition[] nameSpaces = PerlPackageUtil.findNamespaceDefinitions(project, packageName).toArray(new PerlNamespaceDefinition[]{});
        NavigationItem[] navigationItems = new NavigationItem[nameSpaces.length];
        for (int i = 0; i < nameSpaces.length; i++) {
            PerlNamespaceDefinition nameSpace = nameSpaces[i];
            navigationItems[i] = new PerlNameSpaceNavigationItem(project,nameSpace);
        }

        return navigationItems;
    }
}
