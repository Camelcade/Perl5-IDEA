/*
 * Copyright 2015-2024 Alexandr Evstigneev
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

package com.perl5.lang.perl.internals.warnings;

import java.util.ArrayList;
import java.util.List;


public class PerlWarningTreeNode extends PerlAbstractWarningTreeElement {
  protected final List<PerlAbstractWarningTreeElement> subElements;

  public PerlWarningTreeNode(double minVersion, String stringIdentifier, List<PerlAbstractWarningTreeElement> subElements) {
    super(minVersion, stringIdentifier);
    this.subElements = subElements;
  }

  public List<PerlWarningTreeLeaf> collectChildLeafs() {
    List<PerlWarningTreeLeaf> result = new ArrayList<>();

    for (PerlAbstractWarningTreeElement subElement : subElements) {
      if (subElement instanceof PerlWarningTreeNode warningTreeNode) {
        result.addAll(warningTreeNode.collectChildLeafs());
      }
      else if (subElement instanceof PerlWarningTreeLeaf warningTreeLeaf) {
        result.add(warningTreeLeaf);
      }
      else {
        throw new RuntimeException("Unknown type of warnings tree: " + subElement.getClass());
      }
    }

    return result;
  }

  public List<PerlWarningTreeNode> collectChildNodes() {
    List<PerlWarningTreeNode> result = new ArrayList<>();
    result.add(this);

    for (PerlAbstractWarningTreeElement subElement : subElements) {
      if (subElement instanceof PerlWarningTreeNode warningTreeNode) {
        result.addAll(warningTreeNode.collectChildNodes());
      }
      else if (!(subElement instanceof PerlWarningTreeLeaf)) {
        throw new RuntimeException("Unknown type of warnings tree: " + subElement.getClass());
      }
    }

    return result;
  }
}
