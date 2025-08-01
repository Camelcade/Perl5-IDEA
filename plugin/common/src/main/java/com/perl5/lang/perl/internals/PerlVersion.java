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

package com.perl5.lang.perl.internals;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.xmlb.annotations.Tag;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.util.PerlUtil;
import org.apache.groovy.util.Maps;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;

import static com.perl5.lang.perl.internals.PerlVersionRegexps.*;

/**
 * Represents perl version
 */
public final class PerlVersion implements Comparable<PerlVersion> {
  private static final Logger LOG = Logger.getInstance(PerlVersion.class);
  public static final PerlVersion V5_10 = new PerlVersion(5.010);
  public static final PerlVersion V5_12 = new PerlVersion(5.012);
  public static final PerlVersion V5_14 = new PerlVersion(5.014);
  public static final PerlVersion V5_16 = new PerlVersion(5.016);
  public static final PerlVersion V5_18 = new PerlVersion(5.018);
  public static final PerlVersion V5_20 = new PerlVersion(5.020);
  public static final PerlVersion V5_22 = new PerlVersion(5.022);
  public static final PerlVersion V5_24 = new PerlVersion(5.024);
  public static final PerlVersion V5_26 = new PerlVersion(5.026);
  public static final PerlVersion V5_28 = new PerlVersion(5.028);
  public static final PerlVersion V5_30 = new PerlVersion(5.030);
  public static final PerlVersion V5_32 = new PerlVersion(5.032);
  public static final PerlVersion V5_34 = new PerlVersion(5.034);
  public static final PerlVersion V5_36 = new PerlVersion(5.036);
  public static final PerlVersion V5_38 = new PerlVersion(5.038);
  public static final Predicate<PerlVersion> GREATER_OR_EQUAL_V520 = version -> !version.lesserThan(V5_20);
  public static final Predicate<PerlVersion> GREATER_OR_EQUAL_V522 = version -> !version.lesserThan(V5_22);
  public static final Predicate<PerlVersion> GREATER_OR_EQUAL_V532 = version -> !version.lesserThan(V5_32);
  public static final List<PerlVersion> ALL_VERSIONS = List.of(
    V5_10, V5_12, V5_14, V5_16, V5_18, V5_20, V5_22, V5_24, V5_26, V5_28, V5_30, V5_32, V5_34, V5_36, V5_38
  );

  public static final Map<PerlVersion, @Nls String> PERL_VERSION_DESCRIPTIONS = Maps.of(
    V5_10, PerlBundle.message("perl.version.description.5.10"),
    V5_12, PerlBundle.message("perl.version.description.5.12"),
    V5_14, PerlBundle.message("perl.version.description.5.14"),
    V5_16, PerlBundle.message("perl.version.description.5.16"),
    V5_18, PerlBundle.message("perl.version.description.5.18"),
    V5_20, PerlBundle.message("perl.version.description.5.20"),
    V5_22, PerlBundle.message("perl.version.description.5.22"),
    V5_24, PerlBundle.message("perl.version.description.5.24"),
    V5_26, PerlBundle.message("perl.version.description.5.26"),
    V5_28, PerlBundle.message("perl.version.description.5.28"),
    V5_30, PerlBundle.message("perl.version.description.5.30"),
    V5_32, PerlBundle.message("perl.version.description.5.32"),
    V5_34, PerlBundle.message("perl.version.description.5.34"),
    V5_36, PerlBundle.message("perl.version.description.5.36"),
    V5_38, PerlBundle.message("perl.version.description.5.38")
  );

  @Tag("isAlpha")
  private boolean myIsAlpha;
  @Tag("isStrict")
  private boolean myIsStrict;
  @Tag("isValid")
  private boolean myIsValid;
  @Tag("revision")
  private int myRevision;
  @Tag("major")
  private int myMajor;
  @Tag("minor")
  private int myMinor;
  @Tag("extraChunks")
  private List<Integer> myExtraChunks = Collections.emptyList();

  @SuppressWarnings("unused")
  private PerlVersion() {
  }

  public PerlVersion(double version) {
    try {
      parseDoubleVersion(version);
    }
    catch (NumberFormatException e) {
      LOG.debug("Unable to parse version: ", version, "; message: ", e.getMessage());
    }
  }

  public PerlVersion(String versionString) {
    init(versionString);
  }

  private void init(String versionString) {
    try {
      Matcher matcher;

      if (NUMERIC_VERSION.matcher(versionString).matches()) {
        parseDoubleVersion(Double.parseDouble(versionString.replace("_", "")));
        myIsAlpha = versionString.contains("_");
      }
      else if ((matcher = DOTTED_VERSION.matcher(versionString)).matches()) {
        parseDottedVersion(versionString, matcher);
      }
      else {
        myIsValid = false;
        return;
      }
      myIsStrict = STRICT_VERSION_PATTERN.matcher(versionString).matches();
      myIsValid = true;
    }
    catch (NumberFormatException e) {
      myIsValid = myIsStrict = myIsAlpha = false;
      myRevision = myMajor = myMinor = 0;
    }
  }

  private void parseDottedVersion(String versionString, Matcher matcher) {
    List<String> versionChunks = PerlUtil.mutableList(versionString.replace("v", "").replace('_', '.').split("\\."));
    myIsAlpha = matcher.group(1) != null;
    myRevision = Integer.parseInt(versionChunks.removeFirst());

    if (versionChunks.isEmpty()) {
      return;
    }
    if (versionChunks.getFirst().length() > 3) {
      throw new NumberFormatException();
    }

    myMajor = Integer.parseInt(versionChunks.removeFirst());

    if (versionChunks.isEmpty()) {
      return;
    }
    if (versionChunks.getFirst().length() > 3) {
      throw new NumberFormatException();
    }

    myMinor = Integer.parseInt(versionChunks.removeFirst());

    if (versionChunks.isEmpty()) {
      return;
    }
    myExtraChunks = new ArrayList<>();
    for (String chunk : versionChunks) {
      if (chunk.length() > 3) {
        throw new NumberFormatException();
      }
      else {
        myExtraChunks.add(Integer.parseInt(chunk));
      }
    }
  }

  public void parseDoubleVersion(double version) throws NumberFormatException {
    if (version <= Integer.MAX_VALUE) {
      long longVersion = (long)(version * 1000000);
      myIsStrict = true;
      myIsAlpha = false;
      myIsValid = version > 0;
      if (myIsValid) {
        myRevision = (int)version;
        longVersion = (longVersion - myRevision * 1000000L);
        myMajor = (int)(longVersion / 1000);
        myMinor = (int)(longVersion % 1000);
      }
    }
    else {
      throw new NumberFormatException("Version is too big");
    }
  }

  public double getDoubleVersion() {
    double version = myRevision + ((double)myMajor / 1000) + ((double)myMinor / 1000000);

    if (!myExtraChunks.isEmpty()) {
      long divider = 1000000000;
      for (Integer chunk : myExtraChunks) {
        version += ((double)chunk) / divider;
        divider *= 1000;
      }
    }

    return version;
  }

  public @NlsSafe String getStrictDottedVersion() {
    List<String> result = new ArrayList<>(Collections.singletonList(Integer.toString(myRevision)));

    if (myMajor > 0 || myMinor > 0 || !myExtraChunks.isEmpty()) {
      result.add(Integer.toString(myMajor));
    }

    if (myMinor > 0 || !myExtraChunks.isEmpty()) {
      result.add(Integer.toString(myMinor));
    }

    for (Integer chunk : myExtraChunks) {
      result.add(Integer.toString(chunk));
    }

    return "v" + StringUtil.join(result, ".");
  }

  public int getRevision() {
    return myRevision;
  }

  public int getMajor() {
    return myMajor;
  }

  public int getMinor() {
    return myMinor;
  }

  public boolean isAlpha() {
    return myIsAlpha;
  }

  public boolean isStrict() {
    return myIsStrict;
  }

  public boolean isValid() {
    return myIsValid;
  }

  @Override
  public int compareTo(@NotNull PerlVersion o) {
    return Double.compare(getDoubleVersion(), o.getDoubleVersion());
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof PerlVersion perlVersion && (this == o || this.getDoubleVersion() == perlVersion.getDoubleVersion());
  }

  @Override
  public int hashCode() {
    return Double.hashCode(getDoubleVersion());
  }

  public boolean lesserThan(PerlVersion o) {
    return compareTo(o) < 0;
  }

  public boolean greaterThan(PerlVersion o) {
    return compareTo(o) > 0;
  }
}
