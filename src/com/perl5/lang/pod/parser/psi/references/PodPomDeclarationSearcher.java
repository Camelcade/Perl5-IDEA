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

package com.perl5.lang.pod.parser.psi.references;

import com.intellij.pom.PomDeclarationSearcher;
import com.intellij.pom.PomTarget;
import com.intellij.psi.PsiElement;
import com.intellij.util.Consumer;
import com.perl5.lang.pod.parser.psi.PodSectionTitle;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 10.04.2016.
 */
public class PodPomDeclarationSearcher extends PomDeclarationSearcher
{
	@Override
	public void findDeclarationsAt(@NotNull PsiElement element, int offsetInElement, Consumer<PomTarget> consumer)
	{
		if (element.getParent() instanceof PodSectionTitle)
		{
			consumer.consume((PomTarget) element.getParent().getParent());
		}
	}
}
