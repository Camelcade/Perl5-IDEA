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

package com.perl5.lang.perl.parser.Class.Accessor.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IndexSink;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStub;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStubElementType;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionsStubIndex;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.parser.Class.Accessor.psi.PerlClassAccessorDeclaration;
import com.perl5.lang.perl.parser.Class.Accessor.psi.impl.PerlClassAccessorDeclarationImpl;
import com.perl5.lang.perl.parser.Class.Accessor.psi.stubs.PerlClassAccessorDeclarationStub;
import com.perl5.lang.perl.parser.Class.Accessor.psi.stubs.PerlClassAccessorDeclarationStubImpl;
import com.perl5.lang.perl.parser.elementTypes.PsiElementProvider;
import com.perl5.lang.perl.psi.PerlSubDefinitionBase;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * Created by hurricup on 22.01.2016.
 */
public class ClassAccessorDeclarationStubElementType extends PerlSubDefinitionStubElementType
  implements PerlElementTypes, PsiElementProvider {
  public ClassAccessorDeclarationStubElementType(String name) {
    super(name);
  }


  @Override
  public PerlSubDefinitionStub createStub(@NotNull PerlSubDefinitionBase psi, StubElement parentStub) {
    assert psi instanceof PerlClassAccessorDeclaration;
    //noinspection unchecked
    return new PerlClassAccessorDeclarationStubImpl(
      parentStub,
      psi.getPackageName(),
      psi.getSubName(),
      psi.getSubArgumentsList(),
      psi.getLocalAnnotations(),
      ((PerlClassAccessorDeclaration)psi).isFollowsBestPractice(),
      ((PerlClassAccessorDeclaration)psi).isAccessorReadable(),
      ((PerlClassAccessorDeclaration)psi).isAccessorWritable(),
      this
    );
  }

  @Override
  public PerlSubDefinitionBase createPsi(@NotNull PerlSubDefinitionStub stub) {
    return new PerlClassAccessorDeclarationImpl(stub, this);
  }

  @Override
  public boolean shouldCreateStub(ASTNode node) {
    PsiElement psi = node.getPsi();
    return psi instanceof PerlClassAccessorDeclaration &&
           StringUtil.isNotEmpty(((PerlClassAccessorDeclaration)psi).getPackageName()) &&
           StringUtil.isNotEmpty(((PerlClassAccessorDeclaration)psi).getName())
      ;
  }

  @Override
  public void indexStub(@NotNull PerlSubDefinitionStub stub, @NotNull IndexSink sink) {
    assert stub instanceof PerlClassAccessorDeclarationStub;

/*
                System.err.println("Indexing stub for " + stub.getCanonicalName() +
				" " + ((PerlClassAccessorDeclarationStub) stub).isFollowsBestPractice() +
				" " + ((PerlClassAccessorDeclarationStub) stub).isAccessorReadable() +
				" " + ((PerlClassAccessorDeclarationStub) stub).isAccessorWritable()
		);
*/

    if (((PerlClassAccessorDeclarationStub)stub).isFollowsBestPractice()) {
      // fixme these should depend on declaration type
      if (((PerlClassAccessorDeclarationStub)stub).isAccessorReadable()) {
        sink.occurrence(PerlSubDefinitionsStubIndex.KEY, ((PerlClassAccessorDeclarationStub)stub).getGetterCanonicalName());
      }
      if (((PerlClassAccessorDeclarationStub)stub).isAccessorWritable()) {
        sink.occurrence(PerlSubDefinitionsStubIndex.KEY, ((PerlClassAccessorDeclarationStub)stub).getSetterCanonicalName());
      }

      sink.occurrence(PerlSubDefinitionsStubIndex.KEY, "*" + stub.getPackageName());
    }
    else {
      super.indexStub(stub, sink);
    }
  }

  @NotNull
  @Override
  public PsiElement getPsiElement(@NotNull ASTNode node) {
    return new PerlClassAccessorDeclarationImpl(node);
  }

  @Override
  public void serialize(@NotNull PerlSubDefinitionStub stub, @NotNull StubOutputStream dataStream) throws IOException {
    assert stub instanceof PerlClassAccessorDeclarationStub;
    dataStream.writeName(stub.getPackageName());
    dataStream.writeName(stub.getSubName());

    PerlSubArgument.serializeList(dataStream, stub.getSubArgumentsList());

    PerlSubAnnotations subAnnotations = stub.getAnnotations();
    if (subAnnotations == null) {
      dataStream.writeBoolean(false);
    }
    else {
      dataStream.writeBoolean(true);
      subAnnotations.serialize(dataStream);
    }

    dataStream.writeBoolean(((PerlClassAccessorDeclarationStub)stub).isFollowsBestPractice());
    dataStream.writeBoolean(((PerlClassAccessorDeclarationStub)stub).isAccessorReadable());
    dataStream.writeBoolean(((PerlClassAccessorDeclarationStub)stub).isAccessorWritable());
  }

  @NotNull
  @Override
  public PerlSubDefinitionStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException {
    //noinspection ConstantConditions
    String packageName = dataStream.readName().toString();
    //noinspection ConstantConditions
    String functionName = dataStream.readName().toString();

    List<PerlSubArgument> arguments = PerlSubArgument.deserializeList(dataStream);

    PerlSubAnnotations annotations = null;
    if (dataStream.readBoolean()) {
      annotations = PerlSubAnnotations.deserialize(dataStream);
    }

    boolean followsBestPractice = dataStream.readBoolean();
    boolean isReadable = dataStream.readBoolean();
    boolean isWritable = dataStream.readBoolean();

    return new PerlClassAccessorDeclarationStubImpl(
      parentStub,
      packageName,
      functionName,
      arguments,
      annotations,
      followsBestPractice,
      isReadable,
      isWritable,
      this
    );
  }
}
