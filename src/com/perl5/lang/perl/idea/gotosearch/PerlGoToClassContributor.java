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

import com.intellij.navigation.ChooseByNameContributor;
import com.intellij.navigation.GotoClassContributor;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
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
        return PerlPackageUtil.getDefinedPackageNames(project).toArray(new String[]{});
    }

    @NotNull
    @Override
    public NavigationItem[] getItemsByName(String packageName, String searchTerm, Project project, boolean b) {
        PsiPerlNamespaceDefinition[] nameSpaces = PerlPackageUtil.getNamespaceDefinitions(project, packageName).toArray(new PsiPerlNamespaceDefinition[]{});
        NavigationItem[] navigationItems = new NavigationItem[nameSpaces.length];
        for (int i = 0; i < nameSpaces.length; i++) {
            PsiPerlNamespaceDefinition nameSpace = nameSpaces[i];
            navigationItems[i] = new PerlNameSpaceNavigationItem(project,nameSpace);
        }

        return navigationItems;
    }
}
