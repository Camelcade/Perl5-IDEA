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

import com.intellij.util.containers.ContainerUtil;
import com.perl5.PerlBundle;
import gnu.trove.THashMap;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;

/**
 * Created by hurricup on 23.08.2015.
 * Represents perl version
 */
public class PerlVersion implements PerlVersionRegexps, Comparable<PerlVersion> {
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
  public static final List<PerlVersion> ALL_VERSIONS = ContainerUtil.immutableList(
    V5_10, V5_12, V5_14, V5_16, V5_18, V5_20, V5_22, V5_24, V5_26, V5_28
  );

  public static final Map<PerlVersion, String> PERL_VERSION_DESCRIPTIONS = new THashMap<>();

  static {
    PERL_VERSION_DESCRIPTIONS.put(V5_10, PerlBundle.message("perl.version.description.5.10"));
    PERL_VERSION_DESCRIPTIONS.put(V5_12, PerlBundle.message("perl.version.description.5.12"));
    PERL_VERSION_DESCRIPTIONS.put(V5_14, PerlBundle.message("perl.version.description.5.14"));
    PERL_VERSION_DESCRIPTIONS.put(V5_16, PerlBundle.message("perl.version.description.5.16"));
    PERL_VERSION_DESCRIPTIONS.put(V5_18, PerlBundle.message("perl.version.description.5.18"));
    PERL_VERSION_DESCRIPTIONS.put(V5_20, PerlBundle.message("perl.version.description.5.20"));
    PERL_VERSION_DESCRIPTIONS.put(V5_22, PerlBundle.message("perl.version.description.5.22"));
    PERL_VERSION_DESCRIPTIONS.put(V5_24, PerlBundle.message("perl.version.description.5.24"));
    PERL_VERSION_DESCRIPTIONS.put(V5_26, PerlBundle.message("perl.version.description.5.26"));
    PERL_VERSION_DESCRIPTIONS.put(V5_28, PerlBundle.message("perl.version.description.5.28"));
  }

  protected boolean isAlpha;
  protected boolean isStrict;
  protected boolean isValid;
  protected int revision;
  protected int major;
  protected int minor;
  protected List<Integer> extraChunks = Collections.emptyList();

  private PerlVersion() {}

  public PerlVersion(double version) {
    try {
      parseDoubleVersion(version);
    }
    catch (Exception ignore) {
    }
  }

  public PerlVersion(String versionString) {

    try {
      Matcher matcher;

      if (numericVersion.matcher(versionString).matches()) {
        parseDoubleVersion(Double.parseDouble(versionString.replace("_", "")));
        isAlpha = versionString.contains("_"); // fixme not sure about this at all
      }
      else if ((matcher = dottedVersion.matcher(versionString)).matches()) {
        List<String> versionChunks = new ArrayList<>(Arrays.asList(versionString.replace("v", "").replace('_', '.').split("\\.")));
        isAlpha = matcher.group(1) != null;
        revision = Integer.parseInt(versionChunks.remove(0));

        if (!versionChunks.isEmpty()) {
          if (versionChunks.get(0).length() > 3) {
            throw new Exception();
          }

          major = Integer.parseInt(versionChunks.remove(0));

          if (!versionChunks.isEmpty()) {
            if (versionChunks.get(0).length() > 3) {
              throw new Exception();
            }

            minor = Integer.parseInt(versionChunks.remove(0));

            if (!versionChunks.isEmpty()) {
              extraChunks = new ArrayList<>();
              for (String chunk : versionChunks) {
                if (chunk.length() > 3) {
                  throw new Exception();
                }
                else {
                  extraChunks.add(Integer.parseInt(chunk));
                }
              }
            }
          }
        }
      }
      else {
        isValid = false;
        return;
      }
      isStrict = strict.matcher(versionString).matches();
      isValid = true;
    }
    catch (Exception e) // catching numberformat exception
    {
      isValid = isStrict = isAlpha = false;
      revision = major = minor = 0;
    }
  }

  public void parseDoubleVersion(double version) throws Exception {
    if (version <= Integer.MAX_VALUE) {
      long longVersion = (long)(version * 1000000);
      isStrict = true;
      isAlpha = false;
      isValid = version > 0;
      if (isValid) {
        revision = (int)version;
        longVersion = (longVersion - revision * 1000000);
        major = (int)(longVersion / 1000);
        minor = (int)(longVersion % 1000);
      }
    }
    else {
      throw new Exception("Version is too big");
    }
  }

  public double getDoubleVersion() {
    double version = (double)revision + ((double)major / 1000) + ((double)minor / 1000000);

    if (!extraChunks.isEmpty()) {
      long divider = 1000000000;
      for (Integer chunk : extraChunks) {
        version += ((double)chunk) / divider;
        divider *= 1000;
      }
    }

    return version;
  }

  public String getStrictDottedVersion() {
    List<String> result = new ArrayList<>(Collections.singletonList(Integer.toString(revision)));

    if (major > 0 || minor > 0 || !extraChunks.isEmpty()) {
      result.add(Integer.toString(major));
    }

    if (minor > 0 || !extraChunks.isEmpty()) {
      result.add(Integer.toString(minor));
    }

    for (Integer chunk : extraChunks) {
      result.add(Integer.toString(chunk));
    }

    return "v" + StringUtils.join(result, ".");
  }

  public String getStrictNumericVersion() {
    return Double.toString(getDoubleVersion());
  }

  public int getRevision() {
    return revision;
  }

  public void setRevision(int revision) {
    this.revision = revision;
  }

  public int getMajor() {
    return major;
  }

  public void setMajor(int major) {
    this.major = major;
  }

  public int getMinor() {
    return minor;
  }

  public void setMinor(int minor) {
    this.minor = minor;
  }

  public boolean isAlpha() {
    return isAlpha;
  }

  public boolean isStrict() {
    return isStrict;
  }

  public boolean isValid() {
    return isValid;
  }

  @Override
  public int compareTo(@NotNull PerlVersion o) {
    return Double.compare(getDoubleVersion(), o.getDoubleVersion());
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof PerlVersion && (this == o || this.getDoubleVersion() == ((PerlVersion)o).getDoubleVersion());
  }

  public boolean lesserThan(PerlVersion o) {
    return compareTo(o) < 0;
  }

  public boolean greaterThan(PerlVersion o) {
    return compareTo(o) > 0;
  }
}
