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

package com.perl5.lang.perl.extensions.packageprocessor;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.text.StringUtil;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.idea.completion.providers.processors.PerlCompletionProcessor;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class PerlExportDescriptor {
  public static final String ALL_SIGILS = "$@%*&";

  private final char mySigil;
  private final @NotNull String myImportedName;
  private final @NotNull String myRealName;
  private final @NotNull String myRealPackage;

  private PerlExportDescriptor(@NotNull String realPackage, @NotNull String realName, @NotNull String importedName) {
    myRealPackage = realPackage;
    if (realName.length() > 0 && StringUtil.containsChar(ALL_SIGILS, realName.charAt(0)))  // canonical export
    {
      mySigil = realName.charAt(0);
      myRealName = realName.substring(1);
      myImportedName = importedName.substring(1);
    }
    else // suppose it's sigilles code
    {
      mySigil = '&';
      myImportedName = importedName;
      myRealName = realName;
    }
  }

  public @NotNull String getImportedName() {
    return myImportedName;
  }

  public @NotNull String getTargetCanonicalName() {
    return getRealPackage() + PerlPackageUtil.NAMESPACE_SEPARATOR + getRealName();
  }

  public @NotNull String getRealName() {
    return myRealName;
  }

  public @NotNull String getRealPackage() {
    return myRealPackage;
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
    if (this == o) return true;
    if (!(o instanceof PerlExportDescriptor)) return false;

    PerlExportDescriptor that = (PerlExportDescriptor)o;

    if (mySigil != that.mySigil) return false;
    if (!getImportedName().equals(that.getImportedName())) return false;
    if (!getRealName().equals(that.getRealName())) return false;
    return getRealPackage().equals(that.getRealPackage());
  }

  @Override
  public int hashCode() {
    int result = mySigil;
    result = 31 * result + getImportedName().hashCode();
    result = 31 * result + getRealName().hashCode();
    result = 31 * result + getRealPackage().hashCode();
    return result;
  }

  public boolean processLookupElement(@NotNull PerlCompletionProcessor completionProcessor) {
    String lookupString = getImportedName();
    if (completionProcessor.matches(lookupString)) {
      return completionProcessor.process(
        LookupElementBuilder.create(this, lookupString)
          .withIcon(getIcon())
          .withTypeText(getRealPackage(), true));
    }
    return completionProcessor.result();
  }

  public @Nullable Icon getIcon() {
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

  @Override
  public String toString() {
    return mySigil + myImportedName + " => " + mySigil + PerlPackageUtil.join(myRealPackage, myRealName);
  }

  public static PerlExportDescriptor create(@NonNls @NotNull String sourcePackageName, @NonNls @NotNull String sourceSubName) {
    return new PerlExportDescriptor(sourcePackageName, sourceSubName, sourceSubName);
  }

  public static PerlExportDescriptor create(@NonNls @NotNull String sourcePackageName,
                                            @NonNls @NotNull String sourceSubName,
                                            @NonNls @NotNull String importedSubName) {
    return new PerlExportDescriptor(sourcePackageName, importedSubName, sourceSubName);
  }
}
