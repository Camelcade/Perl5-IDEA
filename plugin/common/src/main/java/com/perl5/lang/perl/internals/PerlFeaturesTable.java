/*
 * Copyright 2015-2026 Alexandr Evstigneev
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

import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import org.jetbrains.annotations.Nls;

import java.util.*;

import static java.util.Map.entry;

/**
 * Represents internal {@code %^H}, built from {@code feature.pm}
 */
@SuppressWarnings("unused")
public final class PerlFeaturesTable implements Cloneable {

  private static final String FEATURE_APOSTROPHE_AS_PACKAGE_SEPARATOR = "feature_apos_as_name_sep";
  private static final String FEATURE_BAREWORD_FILEHANDLES = "bareword_filehandles";
  private static final String FEATURE_BITWISE = "bitwise";
  private static final String FEATURE_CLASS = "class";
  private static final String FEATURE_CURRENT_SUB = "__SUB__";
  private static final String FEATURE_DECLARED_REFS = "myref";
  private static final String FEATURE_DEFER = "defer";
  private static final String FEATURE_EVALBYTES = "evalbytes";
  private static final String FEATURE_EXTRA_PAIRED_DELIMITERS = "more_delims";
  private static final String FEATURE_FC = "fc";
  private static final String FEATURE_INDIRECT = "indirect";
  private static final String FEATURE_ISA = "isa";
  private static final String FEATURE_KEYWORD_ALL = "feature_keyword_all";
  private static final String FEATURE_KEYWORD_ANY = "feature_keyword_any";
  private static final String FEATURE_MODULE_TRUE = "module_true";
  private static final String FEATURE_MULTIDIMENSIONAL = "multidimensional";
  private static final String FEATURE_POSTDEREF_QQ = "postderef_qq";
  private static final String FEATURE_REFALIASING = "refaliasing";
  private static final String FEATURE_SAY = "say";
  private static final String FEATURE_SIGNATURES = "signatures";
  private static final String FEATURE_SMARTMATCH = "feature_smartmatch";
  private static final String FEATURE_STATE = "state";
  private static final String FEATURE_SWITCH = "switch";
  private static final String FEATURE_TRY = "try";
  private static final String FEATURE_UNICODE_EVAL = "unieval";
  private static final String FEATURE_UNICODE_STRINGS = "unicode";

  public static final Map<String, @Nls String> AVAILABLE_FEATURES = Map.ofEntries(
    entry(FEATURE_APOSTROPHE_AS_PACKAGE_SEPARATOR, PerlBundle.message("feature.apostrophe")),
    entry(FEATURE_BAREWORD_FILEHANDLES, PerlBundle.message("perl.feature.bareword.filehandle.description")),
    entry(FEATURE_BITWISE, PerlBundle.message("perl.feature.bitwise.description")),
    entry(FEATURE_CLASS, PerlBundle.message("perl.feature.class")),
    entry(FEATURE_CURRENT_SUB, PerlBundle.message("perl.feature.current.sub.description")),
    entry(FEATURE_DECLARED_REFS, PerlBundle.message("perl.feature.declared.refs.description")),
    entry(FEATURE_DEFER, PerlBundle.message("perl.feature.defer.description")),
    entry(FEATURE_EVALBYTES, PerlBundle.message("perl.feature.evalbytes.description")),
    entry(FEATURE_EXTRA_PAIRED_DELIMITERS, PerlBundle.message("perl.feature.extra.delimiters.description")),
    entry(FEATURE_FC, PerlBundle.message("perl.feature.fc.description")),
    entry(FEATURE_INDIRECT, PerlBundle.message("perl.feature.indirect.description")),
    entry(FEATURE_ISA, PerlBundle.message("perl.feature.isa.description")),
    entry(FEATURE_KEYWORD_ALL, PerlBundle.message("feature.all.keyword")),
    entry(FEATURE_KEYWORD_ANY, PerlBundle.message("feature.any.keyword")),
    entry(FEATURE_MODULE_TRUE, PerlBundle.message("perl.feature.module.true")),
    entry(FEATURE_MULTIDIMENSIONAL, PerlBundle.message("perl.feature.multidimensional.description")),
    entry(FEATURE_POSTDEREF_QQ, PerlBundle.message("perl.feature.postderef.qq.description")),
    entry(FEATURE_REFALIASING, PerlBundle.message("perl.feature.refaliasing.description")),
    entry(FEATURE_SAY, PerlBundle.message("perl.feature.say.description")),
    entry(FEATURE_SIGNATURES, PerlBundle.message("perl.feature.signatures.description")),
    entry(FEATURE_SMARTMATCH, PerlBundle.message("feature.enabled.smartmatch.operator")),
    entry(FEATURE_STATE, PerlBundle.message("perl.feature.state.description")),
    entry(FEATURE_SWITCH, PerlBundle.message("perl.feature.switch.description")),
    entry(FEATURE_TRY, PerlBundle.message("perl.feature.try.description")),
    entry(FEATURE_UNICODE_EVAL, PerlBundle.message("perl.feature.unicode.eval.description")),
    entry(FEATURE_UNICODE_STRINGS, PerlBundle.message("perl.feature.unicode.strings.description"))
  );

  private static final List<String> FEATURES_5_10 = List.of(
    FEATURE_BAREWORD_FILEHANDLES, FEATURE_INDIRECT, FEATURE_MULTIDIMENSIONAL, FEATURE_SAY, FEATURE_STATE, FEATURE_SWITCH);
  private static final List<String> FEATURES_5_11 = List.of(
    FEATURE_BAREWORD_FILEHANDLES, FEATURE_INDIRECT, FEATURE_MULTIDIMENSIONAL, FEATURE_SAY, FEATURE_STATE, FEATURE_SWITCH,
    FEATURE_UNICODE_STRINGS);
  private static final List<String> FEATURES_5_15 = List.of(
    FEATURE_BAREWORD_FILEHANDLES, FEATURE_CURRENT_SUB, FEATURE_EVALBYTES, FEATURE_FC, FEATURE_INDIRECT, FEATURE_MULTIDIMENSIONAL,
    FEATURE_SAY, FEATURE_STATE, FEATURE_SWITCH, FEATURE_UNICODE_EVAL, FEATURE_UNICODE_STRINGS);
  private static final List<String> FEATURES_5_23 = List.of(
    FEATURE_BAREWORD_FILEHANDLES, FEATURE_CURRENT_SUB, FEATURE_EVALBYTES, FEATURE_FC, FEATURE_INDIRECT, FEATURE_MULTIDIMENSIONAL,
    FEATURE_POSTDEREF_QQ, FEATURE_SAY, FEATURE_STATE, FEATURE_SWITCH, FEATURE_UNICODE_EVAL, FEATURE_UNICODE_STRINGS);
  private static final List<String> FEATURES_5_27 = List.of(
    FEATURE_BAREWORD_FILEHANDLES, FEATURE_BITWISE, FEATURE_CURRENT_SUB, FEATURE_EVALBYTES, FEATURE_FC, FEATURE_INDIRECT,
    FEATURE_MULTIDIMENSIONAL, FEATURE_POSTDEREF_QQ, FEATURE_SAY, FEATURE_STATE, FEATURE_SWITCH, FEATURE_UNICODE_EVAL,
    FEATURE_UNICODE_STRINGS);
  private static final List<String> FEATURES_5_35 = List.of(
    FEATURE_BAREWORD_FILEHANDLES, FEATURE_BITWISE, FEATURE_CURRENT_SUB, FEATURE_EVALBYTES, FEATURE_FC, FEATURE_ISA, FEATURE_POSTDEREF_QQ,
    FEATURE_SAY, FEATURE_SIGNATURES, FEATURE_STATE, FEATURE_UNICODE_EVAL, FEATURE_UNICODE_STRINGS);
  private static final List<String> FEATURES_5_37 = List.of(
    FEATURE_BITWISE, FEATURE_CURRENT_SUB, FEATURE_EVALBYTES, FEATURE_FC, FEATURE_ISA, FEATURE_MODULE_TRUE, FEATURE_POSTDEREF_QQ,
    FEATURE_SAY, FEATURE_SIGNATURES, FEATURE_STATE, FEATURE_UNICODE_EVAL, FEATURE_UNICODE_STRINGS);
  private static final List<String> FEATURES_5_39 =
    List.of(FEATURE_APOSTROPHE_AS_PACKAGE_SEPARATOR, FEATURE_BITWISE, FEATURE_CURRENT_SUB, FEATURE_EVALBYTES, FEATURE_FC, FEATURE_ISA,
            FEATURE_MODULE_TRUE, FEATURE_POSTDEREF_QQ, FEATURE_SAY, FEATURE_SIGNATURES, FEATURE_SMARTMATCH, FEATURE_STATE, FEATURE_TRY,
            FEATURE_UNICODE_EVAL, FEATURE_UNICODE_STRINGS);
  private static final List<String> FEATURES_5_41 =
    List.of(FEATURE_BITWISE, FEATURE_CURRENT_SUB, FEATURE_EVALBYTES, FEATURE_FC, FEATURE_ISA, FEATURE_MODULE_TRUE, FEATURE_POSTDEREF_QQ,
            FEATURE_SAY, FEATURE_SIGNATURES, FEATURE_STATE, FEATURE_TRY, FEATURE_UNICODE_EVAL, FEATURE_UNICODE_STRINGS);

  public static final Map<String, List<String>> AVAILABLE_FEATURES_BUNDLES = Map.ofEntries(
    entry("all", new ArrayList<>(ContainerUtil.sorted(AVAILABLE_FEATURES.keySet()))),
    entry("default",
          Arrays.asList(FEATURE_APOSTROPHE_AS_PACKAGE_SEPARATOR, FEATURE_BAREWORD_FILEHANDLES, FEATURE_INDIRECT, FEATURE_MULTIDIMENSIONAL,
                        FEATURE_SMARTMATCH)),

    entry("5.9.5", FEATURES_5_10),
    entry("5.10", FEATURES_5_10),

    entry("5.11", FEATURES_5_11),
    entry("5.12", FEATURES_5_11),
    entry("5.13", FEATURES_5_11),
    entry("5.14", FEATURES_5_11),

    entry("5.15", FEATURES_5_15),
    entry("5.16", FEATURES_5_15),
    entry("5.17", FEATURES_5_15),
    entry("5.18", FEATURES_5_15),
    entry("5.19", FEATURES_5_15),
    entry("5.20", FEATURES_5_15),
    entry("5.21", FEATURES_5_15),
    entry("5.22", FEATURES_5_15),

    entry("5.23", FEATURES_5_23),
    entry("5.24", FEATURES_5_23),
    entry("5.25", FEATURES_5_23),
    entry("5.26", FEATURES_5_23),

    entry("5.27", FEATURES_5_27),
    entry("5.28", FEATURES_5_27),
    entry("5.29", FEATURES_5_27),
    entry("5.30", FEATURES_5_27),
    entry("5.31", FEATURES_5_27),
    entry("5.32", FEATURES_5_27),
    entry("5.33", FEATURES_5_27),
    entry("5.34", FEATURES_5_27),

    entry("5.35", FEATURES_5_35),
    entry("5.36", FEATURES_5_35),

    entry("5.37", FEATURES_5_37),
    entry("5.38", FEATURES_5_37),

    entry("5.40", FEATURES_5_39),
    entry("5.42", FEATURES_5_41)
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
