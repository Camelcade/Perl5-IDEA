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

package com.perl5.lang.perl.psi;

import com.intellij.psi.StubBasedPsiElement;
import com.perl5.lang.perl.extensions.packageprocessor.PerlPackageProcessor;
import com.perl5.lang.perl.idea.stubs.imports.PerlUseStatementStub;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by hurricup on 31.05.2015.
 */
public interface PerlUseStatement extends StubBasedPsiElement<PerlUseStatementStub>, PerlNamespaceElementContainer, PerlCompositeElement
{
	String getPackageName();

	boolean isPragma();

	boolean isVersion();

	boolean isPragmaOrVersion();

	@Nullable
	List<String> getImportParameters();

	PerlVersionElement getVersionElement();

	@NotNull
	PerlPackageProcessor getPackageProcessor();

	@Nullable
	PsiPerlExpr getExpr();

	String getOuterPackageName();

}
