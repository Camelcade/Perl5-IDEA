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

package com.perl5.lang.perl.idea.stubs;

import com.intellij.psi.stubs.IStubElementType;
import com.perl5.lang.perl.idea.stubs.constants.PerlConstantStubElementType;
import com.perl5.lang.perl.idea.stubs.globs.PerlGlobStubElementType;
import com.perl5.lang.perl.idea.stubs.imports.PerlUseStatementStubElementType;
import com.perl5.lang.perl.idea.stubs.namespaces.PerlNamespaceDefinitionStubElementType;
import com.perl5.lang.perl.idea.stubs.subsdeclarations.PerlSubDeclarationStubElementType;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStubElementType;
import com.perl5.lang.perl.idea.stubs.variables.types.PerlArrayStubElementType;
import com.perl5.lang.perl.idea.stubs.variables.types.PerlHashStubElementType;
import com.perl5.lang.perl.idea.stubs.variables.types.PerlScalarStubElementType;

/**
 * Created by hurricup on 25.05.2015.
 */
public interface PerlStubElementTypes
{
	IStubElementType SUB_DEFINITION = new PerlSubDefinitionStubElementType("SUB_DEFINITION");
	IStubElementType SUB_DECLARATION = new PerlSubDeclarationStubElementType("SUB_DECLARATION");

	IStubElementType PERL_NAMESPACE = new PerlNamespaceDefinitionStubElementType("NAMESPACE");

	IStubElementType PERL_CONSTANT = new PerlConstantStubElementType("PERL_CONSTANT");

	IStubElementType PERL_USE_STATEMENT = new PerlUseStatementStubElementType("USE_STATEMENT");

	IStubElementType PERL_GLOB = new PerlGlobStubElementType("*");

	IStubElementType PERL_SCALAR = new PerlScalarStubElementType("$");
	IStubElementType PERL_HASH = new PerlHashStubElementType("%");
	IStubElementType PERL_ARRAY = new PerlArrayStubElementType("@");
	IStubElementType PERL_ARRAY_INDEX = new PerlArrayStubElementType("$#");

}
