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

package com.perl5.lang.perl.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;

/**
 * Created by hurricup on 14.05.2015.
 */
public class PerlFunctionAttributeImpl extends ASTWrapperPsiElement
{
		public PerlFunctionAttributeImpl(ASTNode node) {
			super(node);
		}

		// @todo what for?
//	public void accept(@NotNull PsiElementVisitor visitor) {
//		if (visitor instanceof PerlVisitor) ((PerlVisitor)visitor).visitBlock(this);
//		else super.accept(visitor);
//	}

}
