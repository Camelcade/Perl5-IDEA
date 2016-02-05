// This is a generated file. Not intended for manual editing.
package com.perl5.lang.perl.psi;

import com.intellij.psi.StubBasedPsiElement;
import com.perl5.lang.perl.idea.stubs.subsdeclarations.PerlSubDeclarationStub;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PsiPerlSubDeclaration extends PerlSubDeclaration, StubBasedPsiElement<PerlSubDeclarationStub>
{

	@NotNull
	List<PsiPerlAnnotation> getAnnotationList();

	@NotNull
	List<PsiPerlAttribute> getAttributeList();

}
