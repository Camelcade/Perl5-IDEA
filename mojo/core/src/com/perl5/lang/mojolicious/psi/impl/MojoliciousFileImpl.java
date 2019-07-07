/*
 * Copyright 2015-2019 Alexandr Evstigneev
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
import com.intellij.util.ObjectUtils;
import com.perl5.lang.mojolicious.MojoliciousLanguage;
import com.perl5.lang.mojolicious.filetypes.MojoliciousFileType;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlScalarValue;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.psi.PerlVariableDeclarationElement;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.impl.PerlImplicitVariableDeclaration;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.perl.util.PerlScalarUtil;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


public class MojoliciousFileImpl extends PerlFileImpl implements MojoliciousFile {
  @NotNull
  private final String myNamespaceName;
  private List<PerlVariableDeclarationElement> myImplicitVariables;

  public MojoliciousFileImpl(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, MojoliciousLanguage.INSTANCE);
    VirtualFile virtualFile = getVirtualFile();
    String canonicalPath = virtualFile == null ? null : virtualFile.getCanonicalPath();
    String namespaceName = null;
    if (canonicalPath != null) {
      try {
        namespaceName = MOJO_SANDBOX_NS_PREFIX + PerlPackageUtil.NAMESPACE_SEPARATOR +
                        new HexBinaryAdapter().marshal(MessageDigest.getInstance("MD5").digest(canonicalPath.getBytes()));
      }
      catch (NoSuchAlgorithmException ignore) {
      }
    }
    myNamespaceName = ObjectUtils.notNull(namespaceName, MOJO_SANDBOX_NS_PREFIX);
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

  @NotNull
  @Override
  public String getNamespaceName() {
    return myNamespaceName;
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
  public PerlValue getSelfType() {
    return PerlScalarValue.create(MOJO_CONTROLLER_NS); // fixme this is a hack for #1497
  }
}
