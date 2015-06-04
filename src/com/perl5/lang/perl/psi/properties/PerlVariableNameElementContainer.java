package com.perl5.lang.perl.psi.properties;

import com.perl5.lang.perl.psi.PerlVariableNameElement;

/**
 * Created by hurricup on 02.06.2015.
 */
public interface PerlVariableNameElementContainer
{
	/**
	 * Returns variable name element
	 * @return PsiElement
	 */
	PerlVariableNameElement getVariableNameElement();

	/**
	 * Checks if variable is built in
	 * @return result
	 */
	public boolean isBuiltIn();

	/**
	 * Checks if variable is deprecated
	 * @return result
	 */
	public boolean isDeprecated();

}
