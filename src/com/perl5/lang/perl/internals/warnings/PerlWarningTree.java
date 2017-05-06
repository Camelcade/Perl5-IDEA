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

package com.perl5.lang.perl.internals.warnings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by hurricup on 23.08.2015.
 * taken from regen/warnings.pl, up to date with origin/maint-5.22
 */
public class PerlWarningTree {
  public static final PerlWarningTreeNode TREE = new PerlWarningTreeNode(
    5.008, "all", new ArrayList<PerlAbstractWarningTreeElement>(Arrays.asList(
    new PerlWarningTreeNode(5.008, "io", new ArrayList<PerlAbstractWarningTreeElement>(Arrays.asList(
      new PerlWarningTreeLeaf(5.008, "pipe", false),
      new PerlWarningTreeLeaf(5.008, "unopened", false),
      new PerlWarningTreeLeaf(5.008, "closed", false),
      new PerlWarningTreeLeaf(5.008, "newline", false),
      new PerlWarningTreeLeaf(5.008, "exec", false),
      new PerlWarningTreeLeaf(5.008, "layer", false),

      new PerlWarningTreeLeaf(5.019, "syscalls", true)
    ))),

    new PerlWarningTreeNode(5.008, "syntax", new ArrayList<PerlAbstractWarningTreeElement>(Arrays.asList(
      new PerlWarningTreeLeaf(5.008, "ambiguous", false),
      new PerlWarningTreeLeaf(5.008, "semicolon", false),
      new PerlWarningTreeLeaf(5.008, "precedence", false),
      new PerlWarningTreeLeaf(5.008, "bareword", false),
      new PerlWarningTreeLeaf(5.008, "reserved", false),
      new PerlWarningTreeLeaf(5.008, "digit", false),
      new PerlWarningTreeLeaf(5.008, "parenthesis", false),
      new PerlWarningTreeLeaf(5.008, "printf", false),
      new PerlWarningTreeLeaf(5.008, "prototype", false),
      new PerlWarningTreeLeaf(5.008, "qw", false),

      new PerlWarningTreeLeaf(5.011, "illegalproto", false)
    ))),

    new PerlWarningTreeNode(5.008, "severe", new ArrayList<PerlAbstractWarningTreeElement>(Arrays.asList(
      new PerlWarningTreeLeaf(5.008, "inplace", true),
      new PerlWarningTreeLeaf(5.008, "internal", false),
      new PerlWarningTreeLeaf(5.008, "debugging", true),
      new PerlWarningTreeLeaf(5.008, "malloc", true)
    ))),

    new PerlWarningTreeLeaf(5.008, "deprecated", true),
    new PerlWarningTreeLeaf(5.008, "void", false),
    new PerlWarningTreeLeaf(5.008, "recursion", false),
    new PerlWarningTreeLeaf(5.008, "redefine", false),
    new PerlWarningTreeLeaf(5.008, "numeric", false),
    new PerlWarningTreeLeaf(5.008, "uninitialized", false),
    new PerlWarningTreeLeaf(5.008, "once", false),
    new PerlWarningTreeLeaf(5.008, "misc", false),
    new PerlWarningTreeLeaf(5.008, "regexp", false),
    new PerlWarningTreeLeaf(5.008, "glob", true),
    new PerlWarningTreeLeaf(5.008, "untie", false),
    new PerlWarningTreeLeaf(5.008, "substr", false),
    new PerlWarningTreeLeaf(5.008, "taint", false),
    new PerlWarningTreeLeaf(5.008, "signal", false),
    new PerlWarningTreeLeaf(5.008, "closure", false),
    new PerlWarningTreeLeaf(5.008, "overflow", false),
    new PerlWarningTreeLeaf(5.008, "portable", false),

    new PerlWarningTreeNode(5.008, "utf8", new ArrayList<PerlAbstractWarningTreeElement>(Arrays.asList(
      new PerlWarningTreeLeaf(5.013, "surrogate", false),
      new PerlWarningTreeLeaf(5.013, "nonchar", false),
      new PerlWarningTreeLeaf(5.013, "non_unicode", false)
    ))),

    new PerlWarningTreeLeaf(5.008, "exiting", false),
    new PerlWarningTreeLeaf(5.008, "pack", false),
    new PerlWarningTreeLeaf(5.008, "unpack", false),
    new PerlWarningTreeLeaf(5.008, "threads", false),

    new PerlWarningTreeLeaf(5.011, "imprecision", false),

    new PerlWarningTreeNode(5.017, "experimental", new ArrayList<PerlAbstractWarningTreeElement>(Arrays.asList(
      new PerlWarningTreeLeaf(5.017, "experimental::lexical_subs", true),
      new PerlWarningTreeLeaf(5.017, "experimental::regex_sets", true),
      new PerlWarningTreeLeaf(5.017, "experimental::lexical_topic", true),
      new PerlWarningTreeLeaf(5.017, "experimental::smartmatch", true),

      new PerlWarningTreeLeaf(5.019, "experimental::postderef", true),
      new PerlWarningTreeLeaf(5.019, "experimental::autoderef", true),
      new PerlWarningTreeLeaf(5.019, "experimental::signatures", true),

      new PerlWarningTreeLeaf(5.021, "experimental::win32_perlio", true),
      new PerlWarningTreeLeaf(5.021, "experimental::refaliasing", true),
      new PerlWarningTreeLeaf(5.021, "experimental::re_strict", true),
      new PerlWarningTreeLeaf(5.021, "experimental::const_attr", true),
      new PerlWarningTreeLeaf(5.021, "experimental::bitwise", true)
    ))),

    new PerlWarningTreeLeaf(5.021, "missing", false),
    new PerlWarningTreeLeaf(5.021, "redundant", false),
    new PerlWarningTreeLeaf(5.021, "locale", true)
  )));

  public static final HashMap<String, PerlWarningTreeLeaf> LEAF_OPTIONS;
  public static final HashMap<String, PerlWarningTreeNode> NODE_OPTIONS;

  static {
    LEAF_OPTIONS = new HashMap<String, PerlWarningTreeLeaf>();

    for (PerlWarningTreeLeaf leaf : TREE.collectChildLeafs()) {
      if (LEAF_OPTIONS.containsKey(leaf.getStringIdentifier())) {
        throw new RuntimeException("Duplicate warnings tree leaf element: " + leaf.getStringIdentifier());
      }
      else {
        LEAF_OPTIONS.put(leaf.getStringIdentifier(), leaf);
      }
    }
  }

  static {
    NODE_OPTIONS = new HashMap<String, PerlWarningTreeNode>();

    for (PerlWarningTreeNode node : TREE.collectChildNodes()) {
      if (NODE_OPTIONS.containsKey(node.getStringIdentifier())) {
        throw new RuntimeException("Duplicate warnings tree node element: " + node.getStringIdentifier());
      }
      else {
        NODE_OPTIONS.put(node.getStringIdentifier(), node);
      }
    }
  }
}
