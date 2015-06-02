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

package com.perl5.lang.perl.parser;

import com.perl5.lang.perl.exceptions.PerlParsingException;
import com.perl5.lang.perl.exceptions.SubDeclaredException;
import com.perl5.lang.perl.exceptions.SubDefinedException;
import com.perl5.lang.perl.exceptions.SubDefinitionDiffersDeclarationException;
import com.perl5.lang.perl.util.PerlFunctionUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hurricup on 09.05.2015.
 * Represents perl package namespace
 */
public class PerlPackage
{
	protected String name;
	protected PerlVersion version;
	protected HashMap<String,PerlUseParameters> importedPackages = new HashMap<String, PerlUseParameters>();
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

	public boolean isImported(String packageName)
	{
		return importedPackages.get(packageName) != null;
	}

	public void importPackage(PerlUseParameters parameters)
	{
		importedPackages.put(parameters.getPackageName(), parameters);
	}

	/**
	 * Method checks barewrod with defined and built-in functions
	 * @param name bareword value
	 * @return	true if known function, false if not
	 * @todo we should check imported functions here
	 */
	public boolean isKnownFunction(String name)
	{
		boolean r = isSubDeclared(name) || isSubDefined(name) || PerlFunctionUtil.isBuiltIn(name);
		if( !r )
		{
			for(PerlUseParameters importedPackage: importedPackages.values())
			{
				ArrayList<String> imports =  importedPackage.getPackageParams(); // we asuume that all qw are imports
				if( r = (imports != null && imports.contains(name)) )
					break;
			}
		}
		return r;
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
