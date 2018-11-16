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

package com.perl5.lang.perl.extensions.packageprocessor.impl;

import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageOptionsProvider;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPragmaProcessorBase;
import com.perl5.lang.perl.extensions.packageprocessor.PerlWarningsProvider;
import com.perl5.lang.perl.internals.warnings.PerlWarningTree;
import com.perl5.lang.perl.internals.warnings.PerlWarningTreeLeaf;
import com.perl5.lang.perl.internals.warnings.PerlWarningTreeNode;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 18.08.2015.
 */
public class WarningsProcessor extends PerlPragmaProcessorBase implements PerlPackageOptionsProvider, PerlWarningsProvider {
  protected static final HashMap<String, String> OPTIONS = new HashMap<>();
  protected static final HashMap<String, String> OPTIONS_BUNDLES = new HashMap<>();

  static {
    OPTIONS.put("FATAL", "FATALITY!");

    for (Map.Entry<String, PerlWarningTreeLeaf> option : PerlWarningTree.LEAF_OPTIONS.entrySet()) {
      OPTIONS.put(option.getKey(), option.getValue().getMinVersion().getStrictDottedVersion());
    }
  }

  static {
    for (Map.Entry<String, PerlWarningTreeNode> option : PerlWarningTree.NODE_OPTIONS.entrySet()) {
      List<String> subElements = new ArrayList<>();
      for (PerlWarningTreeLeaf leaf : option.getValue().collectChildLeafs()) {
        subElements.add(leaf.getStringIdentifier() + "(" + leaf.getMinVersion().getStrictDottedVersion() + ")");
      }

      OPTIONS_BUNDLES.put(option.getKey(),
                          option.getValue().getMinVersion().getStrictDottedVersion()
                          + ", "
                          + StringUtils.join(subElements, " ")
      );
    }
  }

  @NotNull
  @Override
  public Map<String, String> getOptions() {
    return OPTIONS;
  }


  @NotNull
  @Override
  public Map<String, String> getOptionsBundles() {
    return OPTIONS_BUNDLES;
  }
}
