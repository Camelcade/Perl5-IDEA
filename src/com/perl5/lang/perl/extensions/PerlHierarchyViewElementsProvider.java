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

package com.perl5.lang.perl.extensions;

import com.intellij.ide.util.treeView.smartTree.TreeElement;

import java.util.List;
import java.util.Set;

/**
 * Created by hurricup on 23.01.2016.
 * Implement this interface to declarations, which provides their own TreeView elements
 * Useful for multi-declarations, like best practice Class::Accessor accessors/mutators
 */
public interface PerlHierarchyViewElementsProvider {
  /**
   * Fill treeElements with appropriate elements
   *
   * @param treeElements   result elements list
   * @param duplicationMap element should not be added if it's already in there and shouild be added to map otherwise
   */
  void fillHierarchyViewElements(List<TreeElement> treeElements, Set<String> duplicationMap, boolean isInherited, boolean isImported);
}
