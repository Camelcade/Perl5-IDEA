/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Represents internal %^H
 */
public class PerlFeaturesTable implements Cloneable {
  public static final HashMap<String, String> AVAILABLE_FEATURES = new HashMap<>();
  public static final HashMap<String, List<String>> AVAILABLE_FEATURES_BUNDLES = new HashMap<>();

  static {
    AVAILABLE_FEATURES.put("bareword_filehandles",
                           "v5.34, enables bareword filehandles for builtin functions operations, a generally discouraged practice.");
    AVAILABLE_FEATURES.put("bitwise",
                           "v5.22, makes the four standard bitwise operators treat their operands consistently as numbers, and introduces four new dotted operators that treat their operands consistently as strings. The same applies to the assignment variants. Requires no warnings \"experimental::bitwise\"");
    AVAILABLE_FEATURES.put("current_sub",
                           "v5.16, provides the __SUB__ token that returns a reference to the current subroutine or undef outside of a subroutine");
    AVAILABLE_FEATURES.put("declared_refs",
                           "v5.26, allows a reference to a variable to be declared with my, state, our our, or localized with local. Requires no warnings \"experimental::declared_refs\"");
    AVAILABLE_FEATURES.put("evalbytes", "v5.16, force eval to treat string argument as a string of bytes");
    AVAILABLE_FEATURES.put("fc", "v5.16, enables the fc function, which implements Unicode casefolding");
    AVAILABLE_FEATURES.put("indirect", "v5.32, allows the use of indirect object syntax for method calls, e.g. new Foo 1, 2;");
    AVAILABLE_FEATURES.put("isa",
                           "v5.32, allows the use of the isa infix operator, which tests whether the scalar given by the left operand is an object of the class given by the right operand. Requires no warnings \"experimental::isa\"");
    AVAILABLE_FEATURES.put("multidimensional", "v5.34, enables multidimensional array emulation like $foo{$x, $y}");
    AVAILABLE_FEATURES.put("postderef_qq",
                           "v5.20, allows the use of postfix dereference syntax in within interpolated strings. Requires no warnings \"experimental::postderef\"");
    AVAILABLE_FEATURES
      .put("refaliasing", "v5.22, enables aliasing via assignment to references. Requires  no warnings \"experimental::refaliasing\"");
    AVAILABLE_FEATURES.put("say", "v5.10, enables the Perl 6 style say function");
    AVAILABLE_FEATURES.put("signatures",
                           "v5.20, enables unpacking of subroutine arguments into lexical variables. Requires  no warnings \"experimental::signatures\"");
    AVAILABLE_FEATURES.put("state", "v5.10, enables state variables");
    AVAILABLE_FEATURES.put("switch", "v5.10, enables the Perl 6 given/when construct. Requires no warnings \"experimental::smartmatch\"");
    AVAILABLE_FEATURES
      .put("try", "v5.34, enables the try and catch syntax, which allows exception handling. Requires no warnings \"experimental::try\"");
    AVAILABLE_FEATURES
      .put("unicode_eval", "v5.16, force eval to treat string argument as a string of characters ignoring any use utf8 declarations");
    AVAILABLE_FEATURES.put("unicode_strings",
                           "v5.16, use Unicode rules in all string operations executed within its scope (unless they are also within the scope of either use locale or use bytes)");
  }

  static {
    AVAILABLE_FEATURES_BUNDLES.put("all", new ArrayList<>(AVAILABLE_FEATURES.keySet()));
    AVAILABLE_FEATURES_BUNDLES.put("default", Arrays.asList("bareword_filehandles", "indirect", "multidimensional"));

    List<String> features = Arrays.asList("bareword_filehandles", "indirect", "multidimensional", "say", "state", "switch");
    AVAILABLE_FEATURES_BUNDLES.put("5.9.5", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.10", features);

    features = Arrays.asList("bareword_filehandles", "indirect", "multidimensional", "say", "state", "switch", "unicode_strings");
    AVAILABLE_FEATURES_BUNDLES.put("5.11", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.12", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.13", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.14", features);

    features = Arrays
      .asList("bareword_filehandles", "current_sub", "evalbytes", "fc", "indirect", "multidimensional", "say", "state", "switch",
              "unicode_eval", "unicode_strings");
    AVAILABLE_FEATURES_BUNDLES.put("5.15", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.16", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.17", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.18", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.19", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.20", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.21", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.22", features);

    features = Arrays
      .asList("bareword_filehandles", "current_sub", "evalbytes", "fc", "indirect", "multidimensional", "postderef_qq", "say", "state",
              "switch", "unicode_eval", "unicode_strings");
    AVAILABLE_FEATURES_BUNDLES.put("5.23", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.24", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.25", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.26", features);

    features = Arrays
      .asList("bareword_filehandles", "bitwise", "current_sub", "evalbytes", "fc", "indirect", "multidimensional", "postderef_qq", "say",
              "state", "switch", "unicode_eval", "unicode_strings");
    AVAILABLE_FEATURES_BUNDLES.put("5.27", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.28", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.29", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.30", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.31", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.32", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.33", features);
    AVAILABLE_FEATURES_BUNDLES.put("5.34", features);
  }

  protected HashMap<String, Boolean> featuresMap;

  public PerlFeaturesTable() {
    featuresMap = new HashMap<>();
    for (String feature : AVAILABLE_FEATURES.keySet()) {
      featuresMap.put(feature, false);
    }
  }

  @Override
  public PerlFeaturesTable clone() {
    try {
      PerlFeaturesTable newTable = (PerlFeaturesTable)super.clone();
      newTable.featuresMap = (HashMap<String, Boolean>)featuresMap.clone();
      return newTable;
    }
    catch (CloneNotSupportedException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
