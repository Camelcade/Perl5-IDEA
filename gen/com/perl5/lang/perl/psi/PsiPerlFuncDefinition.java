// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import com.intellij.psi.StubBasedPsiElement;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface PsiPerlFuncDefinition extends PerlFuncDefinition, StubBasedPsiElement<PerlSubDefinitionStub>
{

	@NotNull
	List<PsiPerlAnnotation> getAnnotationList();

	@NotNull
	List<PsiPerlAttribute> getAttributeList();

	@NotNull
	PsiPerlBlock getBlock();

	@Nullable
	PsiPerlFuncSignatureContent getFuncSignatureContent();

}
