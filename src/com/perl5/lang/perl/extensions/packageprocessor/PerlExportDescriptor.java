/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.extensions.packageprocessor;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by hurricup on 02.06.2016.
 */
public class PerlExportDescriptor {
  public static final String ALL_SIGILS = "$@%*&";

  private final char mySigil;
  private final String myExporter;
  private final String myExportedName;
  private final String myTargetName;
  private final String myTargetPackage;

  public PerlExportDescriptor(@NotNull String exportedName, @NotNull String exportedBy) {
    this(exportedBy, exportedName, exportedBy, exportedName);
  }

  public PerlExportDescriptor(@NotNull String exportedBy, @NotNull String exportedName, @NotNull String targetPackage) {
    this(exportedBy, exportedName, targetPackage, exportedName);
  }

  public PerlExportDescriptor(@NotNull String exportedBy,
                              @NotNull String exportedName,
                              @NotNull String targetPackage,
                              @NotNull String targetName) {
    myExporter = exportedBy;
    myTargetPackage = targetPackage;
    if (targetName.length() > 0 && StringUtil.containsChar(ALL_SIGILS, targetName.charAt(0)))  // canonical export
    {
      mySigil = targetName.charAt(0);
      myTargetName = targetName.substring(1);
      myExportedName = exportedName.substring(1); // fixme dangerous, but depends only on PackageProcessor author
    }
    else // suppose it's sigilles code
    {
      mySigil = '&';
      myExportedName = exportedName;
      myTargetName = targetName;
    }
  }

  @NotNull
  public String getExporterName() {
    return myExporter;
  }

  @NotNull
  public String getExportedName() {
    return myExportedName;
  }

  @NotNull
  public String getTargetCanonicalName() {
    return getTargetPackage() + PerlPackageUtil.PACKAGE_SEPARATOR + getTargetName();
  }

  @NotNull
  public String getTargetName() {
    return myTargetName;
  }

  @NotNull
  public String getTargetPackage() {
    return myTargetPackage;
  }

  public boolean isScalar() {
    return mySigil == '$';
  }

  public boolean isArray() {
    return mySigil == '@';
  }

  public boolean isHash() {
    return mySigil == '%';
  }

  public boolean isGlob() {
    return mySigil == '*';
  }

  public boolean isSub() {
    return mySigil == '&';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PerlExportDescriptor)) {
      return false;
    }

    PerlExportDescriptor that = (PerlExportDescriptor)o;

    if (mySigil != that.mySigil) {
      return false;
    }
    if (!getExportedName().equals(that.getExportedName())) {
      return false;
    }
    if (!getTargetName().equals(that.getTargetName())) {
      return false;
    }
    return getTargetPackage().equals(that.getTargetPackage());
  }

  @Override
  public int hashCode() {
    int result = (int)mySigil;
    result = 31 * result + getExportedName().hashCode();
    result = 31 * result + getTargetName().hashCode();
    result = 31 * result + getTargetPackage().hashCode();
    return result;
  }

  @NotNull
  public LookupElementBuilder getLookupElement() {
    return LookupElementBuilder.create(getExportedName())
      .withIcon(getIcon())
      .withTypeText(getTargetPackage(), true);
  }

  @Nullable
  public Icon getIcon() {
    if (isHash()) {
      return PerlIcons.HASH_GUTTER_ICON;
    }
    else if (isScalar()) {
      return PerlIcons.SCALAR_GUTTER_ICON;
    }
    else if (isArray()) {
      return PerlIcons.ARRAY_GUTTER_ICON;
    }
    else if (isGlob()) {
      return PerlIcons.GLOB_GUTTER_ICON;
    }
    else if (isSub()) {
      return PerlIcons.SUB_GUTTER_ICON;
    }
    return null;
  }
}
