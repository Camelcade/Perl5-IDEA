/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.stubs.strings;

import com.intellij.lang.ASTNode;
import com.intellij.psi.stubs.*;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.PerlParserDefinition;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlStringContentElement;
import com.perl5.lang.perl.psi.impl.PerlStringContentElementImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by hurricup on 03.08.2015.
 */
public class PerlStringStubElementType extends IStubElementType<PerlStringStub, PerlStringContentElement> implements PerlElementTypes
{

	public PerlStringStubElementType(String name)
	{
		super(name, PerlLanguage.INSTANCE);
	}

	@Override
	public PerlStringContentElement createPsi(@NotNull PerlStringStub stub)
	{
		return new PerlStringContentElementImpl(stub, this);
	}

	@Override
	public PerlStringStub createStub(@NotNull PerlStringContentElement psi, StubElement parentStub)
	{
		return new PerlStringStubImpl(parentStub, psi.getPackageName(), psi.getName());
	}

	@NotNull
	@Override
	public String getExternalId()
	{
		return "perl." + super.toString();
	}

	@Override
	public void serialize(@NotNull PerlStringStub stub, @NotNull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeName(stub.getPackageName());
		dataStream.writeName(stub.getName());
	}

	@NotNull
	@Override
	public PerlStringStub deserialize(@NotNull StubInputStream dataStream, StubElement parentStub) throws IOException
	{
		return new PerlStringStubImpl(parentStub, dataStream.readName().toString(), dataStream.readName().toString());
	}

	@Override
	public void indexStub(@NotNull PerlStringStub stub, @NotNull IndexSink sink)
	{
		String name = stub.getPackageName() + "::" + stub.getName();
		System.err.println("Indexing: " + name);
		sink.occurrence(PerlConstantsStubIndex.KEY, name);
		sink.occurrence(PerlConstantsStubIndex.KEY, "*" + stub.getPackageName());
	}

	@Override
	public boolean shouldCreateStub(ASTNode node)
	{
		ASTNode stringNode = node.getTreeParent();

		if (stringNode != null)
		{
			ASTNode commaSequence = stringNode.getTreeParent();
			if (commaSequence != null && commaSequence.getElementType() == COMMA_SEQUENCE_EXPR)
			{
				ASTNode useStatement = commaSequence.getTreeParent();

				ASTNode packageSeeker = stringNode.getTreePrev();

				if (useStatement != null && useStatement.getElementType() == ANON_HASH)    // multiple constants
				{
					// check that it's an even element
					int elementCounter = 0;

					for (ASTNode childElement : useStatement.getChildren(null))
					{
						if (childElement == stringNode)
							break;
						if (childElement.getElementType() == OPERATOR_COMMA || childElement == OPERATOR_COMMA_ARROW)
							elementCounter++;
					}

					if (elementCounter % 2 != 0)
						return false;

					useStatement = useStatement.getTreeParent();
					packageSeeker = commaSequence.getTreePrev();
				}


				if (packageSeeker != null && useStatement != null && useStatement.getElementType() == USE_STATEMENT)
				{
					ASTNode prevElement = stringNode.getTreePrev();
					while (prevElement != null && PerlParserDefinition.WHITE_SPACE_AND_COMMENTS.contains(prevElement.getElementType()))
						prevElement = prevElement.getTreePrev();

					return prevElement != null && prevElement.getElementType() == PACKAGE && prevElement.getText().equals("constant");
				}
			}
		}

		return false;
	}

}
