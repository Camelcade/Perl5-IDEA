// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import com.intellij.psi.StubBasedPsiElement;
import com.perl5.lang.perl.idea.stubs.imports.runtime.PerlRuntimeImportStub;
import org.jetbrains.annotations.Nullable;

public interface PsiPerlDoExpr extends PsiPerlExpr, PerlDoExpr, StubBasedPsiElement<PerlRuntimeImportStub>
{

	@Nullable
	PsiPerlBlock getBlock();

	@Nullable
	PsiPerlExpr getExpr();

}
