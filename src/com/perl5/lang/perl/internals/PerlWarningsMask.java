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

package com.perl5.lang.perl.internals;

import com.perl5.lang.perl.internals.warnings.PerlWarningTree;
import com.perl5.lang.perl.internals.warnings.PerlWarningTreeLeaf;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hurricup on 23.08.2015.
 * Represents ${^WARNING_BITS}
 */
public class PerlWarningsMask implements Cloneable {
  protected final HashMap<String, Boolean> currentMask;

  public PerlWarningsMask() {
    currentMask = new HashMap<>();
    for (Map.Entry<String, PerlWarningTreeLeaf> leaf : PerlWarningTree.LEAF_OPTIONS.entrySet()) {
      currentMask.put(leaf.getKey(), leaf.getValue().getDefaultValue());
    }
  }

  public PerlWarningsMask clone() {
    try {
      return (PerlWarningsMask)super.clone();
    }
    catch (CloneNotSupportedException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
