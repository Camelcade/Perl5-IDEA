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

package com.perl5.lang.perl.idea.stubs.namespaces;

import com.intellij.psi.stubs.StubBase;
import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.idea.stubs.PerlStubElementTypes;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.psi.mro.PerlMroType;

import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 28.05.2015.
 */
public class PerlNamespaceDefinitionStubImpl extends StubBase<PsiPerlNamespaceDefinition> implements PerlNamespaceDefinitionStub
{
	private final String myPackageName;
	private final PerlMroType myMroType;
	private final List<String> myParentNamespaces;
	private final boolean myIsDeprecated;
	private final List<String> myEXPORT;
	private final List<String> myEXPORT_OK;
	private final Map<String, List<String>> myEXPORT_TAGS;

	public PerlNamespaceDefinitionStubImpl(
			StubElement parent,
			String packageName,
			PerlMroType mroType,
			List<String> parentNamespaces,
			boolean isDeprecated,
			List<String> EXPORT,
			List<String> EXPORT_OK,
			Map<String, List<String>> EXPORT_TAGS
	)
	{
		super(parent, PerlStubElementTypes.PERL_NAMESPACE);
		myPackageName = packageName;
		myMroType = mroType;
		myParentNamespaces = parentNamespaces;
		myIsDeprecated = isDeprecated;
		myEXPORT = EXPORT;
		myEXPORT_OK = EXPORT_OK;
		myEXPORT_TAGS = EXPORT_TAGS;
	}

	@Override
	public String getPackageName()
	{
		return myPackageName;
	}

	@Override
	public PerlMroType getMroType()
	{
		return myMroType;
	}

	@Override
	public List<String> getParentNamespaces()
	{
		return myParentNamespaces;
	}

	@Override
	public boolean isDeprecated()
	{
		return myIsDeprecated;
	}

	@Override
	public List<String> getEXPORT()
	{
		return myEXPORT;
	}

	@Override
	public List<String> getEXPORT_OK()
	{
		return myEXPORT_OK;
	}

	@Override
	public Map<String, List<String>> getEXPORT_TAGS()
	{
		return myEXPORT_TAGS;
	}
}
