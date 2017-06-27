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

package com.perl5.lang.mojolicious.psi.impl;

import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.perl5.lang.mojolicious.MojoliciousLanguage;
import com.perl5.lang.mojolicious.filetypes.MojoliciousFileType;
import com.perl5.lang.perl.PerlScopes;
import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlLightSubDefinitionsReverseIndex;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlScalarUtil;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 21.07.2015.
 */
public class MojoliciousFileImpl extends PerlFileImpl implements MojoliciousFile {
  private String myNamespace;
  private List<PerlVariableDeclarationElement> myImplicitVariables;

  public MojoliciousFileImpl(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, MojoliciousLanguage.INSTANCE);
    VirtualFile virtualFile = getVirtualFile();
    String canonicalPath = virtualFile == null ? null : virtualFile.getCanonicalPath();
    if (canonicalPath != null) {
      try {
        myNamespace = MOJO_SANDBOX_NS_PREFIX + PerlPackageUtil.PACKAGE_SEPARATOR +
                      new HexBinaryAdapter().marshal(MessageDigest.getInstance("MD5").digest(canonicalPath.getBytes()));
      }
      catch (NoSuchAlgorithmException ignore) {
      }
    }
    if (myNamespace == null) {
      myNamespace = MOJO_SANDBOX_NS_PREFIX;
    }
  }

  @Override
  public String toString() {
    return "Mojolicious Perl5 Template";
  }

  @Override
  protected FileType getDefaultFileType() {
    return MojoliciousFileType.INSTANCE;
  }

  @Override
  public byte[] getPerlContentInBytes() {
    return null;
  }

  @Override
  public String getPackageName() {
    return myNamespace;
  }

  @NotNull
  @Override
  public List<PerlExportDescriptor> getImportedSubsDescriptors() {
    return CachedValuesManager.getCachedValue(this, () -> CachedValueProvider.Result.create(calcImportDescriptors(), this));
  }

  private List<PerlExportDescriptor> calcImportDescriptors() {
    List<PerlExportDescriptor> result = super.getImportedSubsDescriptors();
    result.addAll(HARDCODED_DESCRIPTORS);
    PerlLightSubDefinitionsReverseIndex
      .processSubDefinitionsInPackage(getProject(), MOJO_CONTROLLER_NS, PerlScopes.getProjectAndLibrariesScope(getProject()), sub -> {
        if (sub instanceof MojoHelperDefinition) {
          String packageName = sub.getPackageName();
          assert packageName != null;
          result.add(PerlExportDescriptor.create(packageName, sub.getSubName()));
        }
        return true;
      });
    return result;
  }

  @NotNull
  @Override
  public List<PerlVariableDeclarationElement> getImplicitVariables() {
    if (myImplicitVariables == null) {
      List<PerlVariableDeclarationElement> implicitVariables = new ArrayList<>();
      implicitVariables
        .add(PerlImplicitVariableDeclaration.createLexical(this, PerlScalarUtil.DEFAULT_SELF_SCALAR_NAME, MOJO_CONTROLLER_NS));
      implicitVariables.add(PerlImplicitVariableDeclaration.createLexical(this, "$c", MOJO_CONTROLLER_NS));
      implicitVariables.add(PerlImplicitVariableDeclaration.createLexical(this, "$cb"));
      implicitVariables.add(PerlImplicitVariableDeclaration.createLexical(this, "$_O"));
      myImplicitVariables = implicitVariables;
    }
    return myImplicitVariables;
  }

  @NotNull
  @Override
  public String getSelfNamespace() {
    return MOJO_CONTROLLER_NS; // fixme this is a hack for #1497
  }
}
