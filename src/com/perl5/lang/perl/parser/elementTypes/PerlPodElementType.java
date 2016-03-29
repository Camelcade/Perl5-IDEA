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

package com.perl5.lang.perl.parser.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.lang.PsiParser;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.ILazyParseableElementType;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.pod.PodLanguage;
import com.perl5.lang.pod.lexer.PodLexerAdapter;
import com.perl5.lang.pod.parser.PodParser;
import com.perl5.lang.pod.parser.psi.mixin.PodCompositeElementMixin;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 27.03.2016.
 */
public class PerlPodElementType extends ILazyParseableElementType implements PsiElementProvider
{
	public PerlPodElementType(@NotNull @NonNls String debugName)
	{
		super(debugName, PerlLanguage.INSTANCE);
	}

	@Override
	public ASTNode parseContents(ASTNode chameleon)
	{
		PsiElement parentElement = chameleon.getTreeParent().getPsi();
		Project project = parentElement.getProject();
		PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(
				project,
				chameleon,
				new PodLexerAdapter(project),
				PodLanguage.INSTANCE,
				chameleon.getText());
		PsiParser parser = new PodParser();

		return parser.parse(this, builder).getFirstChildNode();
	}

	@NotNull
	@Override
	public PsiElement getPsiElement(@NotNull ASTNode node)
	{
		return new PodCompositeElementMixin(node);
	}
}
