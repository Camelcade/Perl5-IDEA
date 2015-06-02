package com.perl5.lang.perl.psi.stubs.variables.types;

import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.stubs.StubIndexKey;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.impl.PsiPerlArrayVariableImpl;
import com.perl5.lang.perl.psi.stubs.PerlStubElementTypes;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariableStub;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariableStubIndexKeys;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 02.06.2015.
 */
public class PerlArrayIndexStubElementType extends PerlVariableStubElementType
{
	public PerlArrayIndexStubElementType(@NotNull String debugName)
	{
		super(debugName);
	}

	@Override
	public PerlVariable createPsi(@NotNull PerlVariableStub stub)
	{
		return new PsiPerlArrayVariableImpl(stub,this);
	}

	@Override
	protected IStubElementType getStubElementType()
	{
		return PerlStubElementTypes.PERL_ARRAY_INDEX;
	}

	protected StubIndexKey getStubIndexKey()
	{
		return PerlVariableStubIndexKeys.KEY_ARRAY_INDEX;
	}

}
