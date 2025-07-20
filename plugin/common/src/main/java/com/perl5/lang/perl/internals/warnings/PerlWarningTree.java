/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * taken from regen/warnings.pl, up to date with origin/maint-5.22
 */
public class PerlWarningTree {
  public static final PerlWarningTreeNode TREE = new PerlWarningTreeNode(
    5.008, "all", List.of(
    new PerlWarningTreeNode(5.008, "io", List.of(
      new PerlWarningTreeLeaf(5.008, "pipe"),
      new PerlWarningTreeLeaf(5.008, "unopened"),
      new PerlWarningTreeLeaf(5.008, "closed"),
      new PerlWarningTreeLeaf(5.008, "newline"),
      new PerlWarningTreeLeaf(5.008, "exec"),
      new PerlWarningTreeLeaf(5.008, "layer"),

      new PerlWarningTreeLeaf(5.019, "syscalls")
    )),

    new PerlWarningTreeNode(5.008, "syntax", List.of(
      new PerlWarningTreeLeaf(5.008, "ambiguous"),
      new PerlWarningTreeLeaf(5.008, "semicolon"),
      new PerlWarningTreeLeaf(5.008, "precedence"),
      new PerlWarningTreeLeaf(5.008, "bareword"),
      new PerlWarningTreeLeaf(5.008, "reserved"),
      new PerlWarningTreeLeaf(5.008, "digit"),
      new PerlWarningTreeLeaf(5.008, "parenthesis"),
      new PerlWarningTreeLeaf(5.008, "printf"),
      new PerlWarningTreeLeaf(5.008, "prototype"),
      new PerlWarningTreeLeaf(5.008, "qw"),

      new PerlWarningTreeLeaf(5.011, "illegalproto")
    )),

    new PerlWarningTreeNode(5.008, "severe", List.of(
      new PerlWarningTreeLeaf(5.008, "inplace"),
      new PerlWarningTreeLeaf(5.008, "internal"),
      new PerlWarningTreeLeaf(5.008, "debugging"),
      new PerlWarningTreeLeaf(5.008, "malloc")
    )),

    new PerlWarningTreeLeaf(5.008, "deprecated"),
    new PerlWarningTreeLeaf(5.008, "void"),
    new PerlWarningTreeLeaf(5.008, "recursion"),
    new PerlWarningTreeLeaf(5.008, "redefine"),
    new PerlWarningTreeLeaf(5.008, "numeric"),
    new PerlWarningTreeLeaf(5.008, "uninitialized"),
    new PerlWarningTreeLeaf(5.008, "once"),
    new PerlWarningTreeLeaf(5.008, "misc"),
    new PerlWarningTreeLeaf(5.008, "regexp"),
    new PerlWarningTreeLeaf(5.008, "glob"),
    new PerlWarningTreeLeaf(5.008, "untie"),
    new PerlWarningTreeLeaf(5.008, "substr"),
    new PerlWarningTreeLeaf(5.008, "taint"),
    new PerlWarningTreeLeaf(5.008, "signal"),
    new PerlWarningTreeLeaf(5.008, "closure"),
    new PerlWarningTreeLeaf(5.008, "overflow"),
    new PerlWarningTreeLeaf(5.008, "portable"),

    new PerlWarningTreeNode(5.008, "utf8", List.of(
      new PerlWarningTreeLeaf(5.013, "surrogate"),
      new PerlWarningTreeLeaf(5.013, "nonchar"),
      new PerlWarningTreeLeaf(5.013, "non_unicode")
    )),

    new PerlWarningTreeLeaf(5.008, "exiting"),
    new PerlWarningTreeLeaf(5.008, "pack"),
    new PerlWarningTreeLeaf(5.008, "unpack"),
    new PerlWarningTreeLeaf(5.008, "threads"),

    new PerlWarningTreeLeaf(5.011, "imprecision"),

    new PerlWarningTreeNode(5.017, "experimental", List.of(
      new PerlWarningTreeLeaf(5.017, "experimental::lexical_subs"),
      new PerlWarningTreeLeaf(5.017, "experimental::regex_sets"),
      new PerlWarningTreeLeaf(5.017, "experimental::lexical_topic"),
      new PerlWarningTreeLeaf(5.017, "experimental::smartmatch"),

      new PerlWarningTreeLeaf(5.019, "experimental::postderef"),
      new PerlWarningTreeLeaf(5.019, "experimental::autoderef"),
      new PerlWarningTreeLeaf(5.019, "experimental::signatures"),

      new PerlWarningTreeLeaf(5.021, "experimental::win32_perlio"),
      new PerlWarningTreeLeaf(5.021, "experimental::refaliasing"),
      new PerlWarningTreeLeaf(5.021, "experimental::re_strict"),
      new PerlWarningTreeLeaf(5.021, "experimental::const_attr"),
      new PerlWarningTreeLeaf(5.021, "experimental::bitwise")
    )),

    new PerlWarningTreeLeaf(5.021, "missing"),
    new PerlWarningTreeLeaf(5.021, "redundant"),
    new PerlWarningTreeLeaf(5.021, "locale")
  ));

  public static final Map<String, PerlWarningTreeLeaf> LEAF_OPTIONS;
  public static final Map<String, PerlWarningTreeNode> NODE_OPTIONS;

  static {
    LEAF_OPTIONS = new HashMap<>();

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
    NODE_OPTIONS = new HashMap<>();

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
