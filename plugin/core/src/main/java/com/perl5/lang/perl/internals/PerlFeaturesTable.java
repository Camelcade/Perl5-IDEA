/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

import org.apache.groovy.util.Maps;

import java.util.*;

/**
 * Represents internal %^H
 */
public final class PerlFeaturesTable implements Cloneable {

  private static final String FEATURE_FC = "fc";
  private static final String FEATURE_ISA = "isa";
  private static final String FEATURE_SAY = "say";
  private static final String FEATURE_TRY = "try";
  private static final String FEATURE_STATE = "state";
  private static final String FEATURE_SWITCH = "switch";
  private static final String FEATURE_BITWISE = "bitwise";
  private static final String FEATURE_INDIRECT = "indirect";
  private static final String FEATURE_EVALBYTES = "evalbytes";
  private static final String FEATURE_SIGNATURES = "signatures";
  private static final String FEATURE_CURRENT_SUB = "current_sub";
  private static final String FEATURE_REFALIASING = "refaliasing";
  private static final String FEATURE_POSTDEREF_QQ = "postderef_qq";
  private static final String FEATURE_UNICODE_EVAL = "unicode_eval";
  private static final String FEATURE_DECLARED_REFS = "declared_refs";
  private static final String FEATURE_UNICODE_STRINGS = "unicode_strings";
  private static final String FEATURE_MULTIDIMENSIONAL = "multidimensional";
  private static final String FEATURE_BAREWORD_FILEHANDLES = "bareword_filehandles";
  public static final Map<String, String> AVAILABLE_FEATURES = Maps.of(
    FEATURE_BAREWORD_FILEHANDLES, "v5.34, enables bareword filehandles for builtin functions operations, a generally discouraged practice.",
    FEATURE_BITWISE,
    "v5.22, makes the four standard bitwise operators treat their operands consistently as numbers, and introduces four new dotted operators that treat their operands consistently as strings. The same applies to the assignment variants. Requires no warnings \"experimental::bitwise\"",
    FEATURE_CURRENT_SUB,
    "v5.16, provides the __SUB__ token that returns a reference to the current subroutine or undef outside of a subroutine",
    FEATURE_DECLARED_REFS,
    "v5.26, allows a reference to a variable to be declared with my, state, our our, or localized with local. Requires no warnings \"experimental::declared_refs\"",
    FEATURE_EVALBYTES, "v5.16, force eval to treat string argument as a string of bytes",
    FEATURE_FC, "v5.16, enables the fc function, which implements Unicode casefolding",
    FEATURE_INDIRECT, "v5.32, allows the use of indirect object syntax for method calls, e.g. new Foo 1, 2;",
    FEATURE_ISA,
    "v5.32, allows the use of the isa infix operator, which tests whether the scalar given by the left operand is an object of the class given by the right operand. Requires no warnings \"experimental::isa\"",
    FEATURE_MULTIDIMENSIONAL, "v5.34, enables multidimensional array emulation like $foo{$x, $y}",
    FEATURE_POSTDEREF_QQ,
    "v5.20, allows the use of postfix dereference syntax in within interpolated strings. Requires no warnings \"experimental::postderef\"",
    FEATURE_REFALIASING, "v5.22, enables aliasing via assignment to references. Requires  no warnings \"experimental::refaliasing\"",
    FEATURE_SAY, "v5.10, enables the Perl 6 style say function",
    FEATURE_SIGNATURES,
    "v5.20, enables unpacking of subroutine arguments into lexical variables. Requires  no warnings \"experimental::signatures\"",
    FEATURE_STATE, "v5.10, enables state variables",
    FEATURE_SWITCH, "v5.10, enables the Perl 6 given/when construct. Requires no warnings \"experimental::smartmatch\"",
    FEATURE_TRY, "v5.34, enables the try and catch syntax, which allows exception handling. Requires no warnings \"experimental::try\"",
    FEATURE_UNICODE_EVAL, "v5.16, force eval to treat string argument as a string of characters ignoring any use utf8 declarations",
    FEATURE_UNICODE_STRINGS,
    "v5.16, use Unicode rules in all string operations executed within its scope (unless they are also within the scope of either use locale or use bytes)"
  );

  private static final List<String> FEATURES_5_9_5 = List.of(
    FEATURE_BAREWORD_FILEHANDLES, FEATURE_INDIRECT, FEATURE_MULTIDIMENSIONAL, FEATURE_SAY, FEATURE_STATE, FEATURE_SWITCH);
  private static final List<String> FEATURES_5_27 = List.of(
    FEATURE_BAREWORD_FILEHANDLES, FEATURE_BITWISE, FEATURE_CURRENT_SUB, FEATURE_EVALBYTES, FEATURE_FC, FEATURE_INDIRECT,
    FEATURE_MULTIDIMENSIONAL, FEATURE_POSTDEREF_QQ, FEATURE_SAY, FEATURE_STATE, FEATURE_SWITCH, FEATURE_UNICODE_EVAL,
    FEATURE_UNICODE_STRINGS);
  private static final List<String> FEATURES_5_23 = List.of(
    FEATURE_BAREWORD_FILEHANDLES, FEATURE_CURRENT_SUB, FEATURE_EVALBYTES, FEATURE_FC, FEATURE_INDIRECT, FEATURE_MULTIDIMENSIONAL,
    FEATURE_POSTDEREF_QQ, FEATURE_SAY, FEATURE_STATE, FEATURE_SWITCH, FEATURE_UNICODE_EVAL, FEATURE_UNICODE_STRINGS);
  private static final List<String> FEATURES_5_15 = List.of(
    FEATURE_BAREWORD_FILEHANDLES, FEATURE_CURRENT_SUB, FEATURE_EVALBYTES, FEATURE_FC, FEATURE_INDIRECT, FEATURE_MULTIDIMENSIONAL,
    FEATURE_SAY, FEATURE_STATE, FEATURE_SWITCH, FEATURE_UNICODE_EVAL, FEATURE_UNICODE_STRINGS);
  private static final List<String> FEATURES_5_11 = List.of(
    FEATURE_BAREWORD_FILEHANDLES, FEATURE_INDIRECT, FEATURE_MULTIDIMENSIONAL, FEATURE_SAY, FEATURE_STATE, FEATURE_SWITCH,
    FEATURE_UNICODE_STRINGS);

  public static final Map<String, List<String>> AVAILABLE_FEATURES_BUNDLES = Maps.of(
    "all", new ArrayList<>(AVAILABLE_FEATURES.keySet()),
    "default", Arrays.asList(FEATURE_BAREWORD_FILEHANDLES, FEATURE_INDIRECT, FEATURE_MULTIDIMENSIONAL),

    "5.9.5", FEATURES_5_9_5,
    "5.10", FEATURES_5_9_5,

    "5.11", FEATURES_5_11,
    "5.12", FEATURES_5_11,
    "5.13", FEATURES_5_11,
    "5.14", FEATURES_5_11,

    "5.15", FEATURES_5_15,
    "5.16", FEATURES_5_15,
    "5.17", FEATURES_5_15,
    "5.18", FEATURES_5_15,
    "5.19", FEATURES_5_15,
    "5.20", FEATURES_5_15,
    "5.21", FEATURES_5_15,
    "5.22", FEATURES_5_15,

    "5.23", FEATURES_5_23,
    "5.24", FEATURES_5_23,
    "5.25", FEATURES_5_23,
    "5.26", FEATURES_5_23,

    "5.27", FEATURES_5_27,
    "5.28", FEATURES_5_27,
    "5.29", FEATURES_5_27,
    "5.30", FEATURES_5_27,
    "5.31", FEATURES_5_27,
    "5.32", FEATURES_5_27,
    "5.33", FEATURES_5_27,
    "5.34", FEATURES_5_27
  );

  private Map<String, Boolean> featuresMap;

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
      newTable.featuresMap = Map.copyOf(featuresMap);
      return newTable;
    }
    catch (CloneNotSupportedException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
