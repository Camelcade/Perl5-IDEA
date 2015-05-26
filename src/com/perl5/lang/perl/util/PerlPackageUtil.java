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

package com.perl5.lang.perl.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.impl.PerlNamespaceDefinitionImpl;
import com.perl5.lang.perl.psi.impl.PerlNamespaceImpl;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by hurricup on 24.04.2015.
 */
public class PerlPackageUtil implements PerlElementTypes, PerlPackageUtilBuiltIn
{
	protected static final HashMap<String,IElementType> BUILT_IN_MAP = new HashMap<String,IElementType>();

	static{
		for( String packageName: BUILT_IN )
		{
			BUILT_IN_MAP.put(packageName, PERL_PACKAGE_BUILT_IN);
		}
		for( String packageName: BUILT_IN_PRAGMA )
		{
			BUILT_IN_MAP.put(packageName, PERL_PACKAGE_PRAGMA);
		}
		for( String packageName: BUILT_IN_DEPRECATED )
		{
			BUILT_IN_MAP.put(packageName, PERL_PACKAGE_DEPRECATED);
		}
	}

	/**
	 * Checks if package is built in
	 * @param variable package name
	 * @return result
	 */
	public static boolean isBuiltIn(String variable)
	{
		return BUILT_IN_MAP.containsKey(canonicalPackageName(variable));
	}

	/**
	 * Returns token type depending on package name
	 * @param variable package name
	 * @return token type
	 */
	public static IElementType getPackageType(String variable)
	{
		IElementType packageType = BUILT_IN_MAP.get(canonicalPackageName(variable));
		return packageType == null ? PERL_PACKAGE : packageType;
	}

	/**
	 * Make canonical package name, atm crude, jut chop off :: from end and begining
	 * @param name package name
	 * @return canonical package name
	 */
	public static String canonicalPackageName(String name)
	{
		if( "::".equals(name))
			return "main";
		else
			return name.replaceFirst("::$", "").replaceFirst("^::", "");
	}

	@NotNull
	public static String getElementPackageName(PsiElement element)
	{
		PerlNamespaceDefinitionImpl namespace = PsiTreeUtil.getParentOfType(element, PerlNamespaceDefinitionImpl.class);
		if (namespace != null && namespace.getNamespace() != null)
			return ((PerlNamespaceImpl) namespace.getNamespace()).getName();
		else
			return "main";

	}
}
