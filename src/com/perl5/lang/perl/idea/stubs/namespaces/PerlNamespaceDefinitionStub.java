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

import com.intellij.psi.stubs.StubElement;
import com.perl5.lang.perl.psi.PsiPerlNamespaceDefinition;
import com.perl5.lang.perl.psi.mro.PerlMroType;

import java.util.List;
import java.util.Map;

/**
 * Created by hurricup on 28.05.2015.
 */
public interface PerlNamespaceDefinitionStub extends StubElement<PsiPerlNamespaceDefinition>
{
	public String getPackageName();

	public PerlMroType getMroType();

	public List<String> getParentNamespaces();

	public boolean isDeprecated();

	public List<String> getEXPORT();

	public List<String> getEXPORT_OK();

	public Map<String, List<String>> getEXPORT_TAGS();
}
