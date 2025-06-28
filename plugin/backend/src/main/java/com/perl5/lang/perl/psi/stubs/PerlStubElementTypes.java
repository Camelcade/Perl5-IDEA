/*
 * Copyright 2015-2020 Alexandr Evstigneev
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

package com.perl5.lang.perl.psi.stubs;

import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.moose.psi.elementTypes.PerlMooseAttributeElementType;
import com.perl5.lang.perl.psi.stubs.globs.PerlGlobStubElementType;
import com.perl5.lang.perl.psi.stubs.imports.PerlNoStatementStubElementType;
import com.perl5.lang.perl.psi.stubs.imports.PerlUseStatementStubElementType;
import com.perl5.lang.perl.psi.stubs.imports.runtime.PerlDoExprElementType;
import com.perl5.lang.perl.psi.stubs.imports.runtime.PerlRequireExprElementType;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlLightNamespaceDefinitionElementType;
import com.perl5.lang.perl.psi.stubs.namespaces.PerlNamespaceDefinitionElementType;
import com.perl5.lang.perl.psi.stubs.subsdeclarations.PerlSubDeclarationElementType;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlFuncDefinitionElementType;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlLightSubDefinitionElementType;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlMethodDefinitionElementType;
import com.perl5.lang.perl.psi.stubs.subsdefinitions.PerlSubDefinitionElementType;
import com.perl5.lang.perl.psi.stubs.variables.PerlVariableStubElementType;


public interface PerlStubElementTypes {
  PerlSubDeclarationElementType SUB_DECLARATION = new PerlSubDeclarationElementType("SUB_DECLARATION");
  PerlSubDefinitionElementType SUB_DEFINITION = new PerlSubDefinitionElementType("SUB_DEFINITION");
  PerlFuncDefinitionElementType FUNC_DEFINITION = new PerlFuncDefinitionElementType("FUNC_DEFINITION");
  PerlMethodDefinitionElementType METHOD_DEFINITION = new PerlMethodDefinitionElementType("METHOD_DEFINITION");
  PerlLightSubDefinitionElementType LIGHT_SUB_DEFINITION = new PerlLightSubDefinitionElementType("LIGHT_SUB_DEFINITION");
  PerlLightSubDefinitionElementType LIGHT_METHOD_DEFINITION = new PerlLightSubDefinitionElementType("LIGHT_METHOD_DEFINITION");
  PerlLightNamespaceDefinitionElementType LIGHT_NAMESPACE_DEFINITION =
    new PerlLightNamespaceDefinitionElementType("LIGHT_NAMESPACE_DEFINITION");
  PerlMooseAttributeElementType LIGHT_ATTRIBUTE_DEFINITION = new PerlMooseAttributeElementType("LIGHT_ATTRIBUTE");

  PerlNamespaceDefinitionElementType PERL_NAMESPACE = new PerlNamespaceDefinitionElementType("NAMESPACE");

  PerlUseStatementStubElementType USE_STATEMENT = new PerlUseStatementStubElementType("USE_STATEMENT");
  PerlNoStatementStubElementType NO_STATEMENT = new PerlNoStatementStubElementType("NO_STATEMENT");

  PerlDoExprElementType PERL_DO_EXPR = new PerlDoExprElementType("PERL_DO_EXPR");
  PerlRequireExprElementType PERL_REQUIRE_EXPR = new PerlRequireExprElementType("PERL_REQUIRE_EXPR");

  PerlGlobStubElementType PERL_GLOB = new PerlGlobStubElementType("*");

  PerlVariableStubElementType PERL_VARIABLE_DECLARATION_ELEMENT = new PerlVariableStubElementType("VARIABLE_DECLARATION_ELEMENT");
  PerlFileElementType FILE = new PerlFileElementType("Perl5", PerlLanguage.INSTANCE);
}
