package com.perl5.lang.perl.parser;

import com.perl5.lang.perl.exceptions.PerlParsingException;
import com.perl5.lang.perl.exceptions.SubDeclaredException;
import com.perl5.lang.perl.exceptions.SubDefinedException;
import com.perl5.lang.perl.exceptions.SubDefinitionDiffersDeclarationException;
import com.perl5.lang.perl.util.PerlFunctionUtil;
import org.apache.commons.lang.ArrayUtils;
import org.codehaus.groovy.runtime.ArrayUtil;

import java.util.HashMap;

/**
 * Created by hurricup on 09.05.2015.
 * Represents perl package namespace
 */
public class PerlPackage
{
	protected String name;
	protected PerlVersion version;
	protected HashMap<String,String> usedPackages = new HashMap<String, String>();
	protected final HashMap<String,PerlSub> definedSubs = new HashMap<String, PerlSub>();
	protected final HashMap<String,PerlSub> declaredSubs = new HashMap<String, PerlSub>();

	public PerlPackage(String name)
	{
		this.name = name.replaceFirst("::$", "");
	}

	public String getName()
	{
		return name;
	}

	public boolean equals(PerlPackage o)
	{
		return this == o || this.name.equals(o.getName());

	}

	public PerlVersion getVersion()
	{
		return version;
	}

	public void setVersion(PerlVersion version)
	{
		this.version = version;
	}

	public boolean isUsing(String packageName)
	{
		return usedPackages.get(packageName) != null;
	}

	public void use(String packageName)
	{
		usedPackages.put(packageName,packageName);
	}

	/**
	 * Method checks barewrod with defined and built-in functions
	 * @param name bareword value
	 * @return	true if known function, false if not
	 * @todo we should check imported functions here
	 */
	public boolean isKnownFunction(String name)
	{
		return isSubDeclared(name) || isSubDefined(name) || PerlFunctionUtil.isBuiltIn(name);
	}

	public boolean isSubDefined(String name)
	{
		return definedSubs.get(name) != null;
	}

	public boolean isSubDeclared(String name)
	{
		return declaredSubs.get(name) != null;
	}

	public PerlSub getSubDeclaration(String name)
	{
		return declaredSubs.get(name);
	}

	public void defineSub(PerlSub sub) throws PerlParsingException
	{
		String subName = sub.getName();
		if( isSubDefined(subName))
			throw new SubDefinedException("Sub %s already defined in the package %s", subName, getName());

		if( isSubDeclared(subName) && !getSubDeclaration(subName).lookAlike(sub))
			throw new SubDefinitionDiffersDeclarationException("Sub %s differs from it's declaration in package %s", subName, getName());

		definedSubs.put(sub.getName(), sub);
	}

	public void declareSub(PerlSub sub) throws PerlParsingException
	{
		String subName = sub.getName();

		if( isSubDefined(subName) )
			throw new SubDefinedException("Sub %s already defined in the package %s", subName, getName());

		if( isSubDeclared(subName) )
			throw new SubDeclaredException("Sub %s already declared in the package %s", subName, getName());

		declaredSubs.put(sub.getName(), sub);
	}
}
