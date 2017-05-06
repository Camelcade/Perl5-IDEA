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
import com.perl5.lang.perl.idea.stubs.globs.PerlGlobStubElementType;
import com.perl5.lang.perl.idea.stubs.imports.PerlNoStatementStubElementType;
import com.perl5.lang.perl.idea.stubs.imports.PerlUseStatementStubElementType;
import com.perl5.lang.perl.idea.stubs.imports.runtime.PerlDoExprElementType;
import com.perl5.lang.perl.idea.stubs.imports.runtime.PerlRequireExprElementType;
import com.perl5.lang.perl.idea.stubs.namespaces.PerlNamespaceDefinitionStubElementType;
import com.perl5.lang.perl.idea.stubs.subsdeclarations.PerlSubDeclarationStubElementType;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlConstantDefinitionElementType;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlFuncDefinitionStubElementType;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlMethodDefinitionStubElementType;
import com.perl5.lang.perl.idea.stubs.subsdefinitions.PerlSubDefinitionStubElementType;
import com.perl5.lang.perl.idea.stubs.variables.PerlVariableStubElementType;

/**
 * Created by hurricup on 25.05.2015.
 */
public interface PerlStubElementTypes {
  IStubElementType SUB_DECLARATION = new PerlSubDeclarationStubElementType("SUB_DECLARATION");
  IStubElementType SUB_DEFINITION = new PerlSubDefinitionStubElementType("SUB_DEFINITION");
  IStubElementType FUNC_DEFINITION = new PerlFuncDefinitionStubElementType("FUNC_DEFINITION");
  IStubElementType METHOD_DEFINITION = new PerlMethodDefinitionStubElementType("METHOD_DEFINITION");
  IStubElementType CONSTANT_DEFINITION = new PerlConstantDefinitionElementType("CONSTANT_DEFINITION");

  IStubElementType PERL_NAMESPACE = new PerlNamespaceDefinitionStubElementType("NAMESPACE");

  IStubElementType PERL_USE_STATEMENT = new PerlUseStatementStubElementType("USE_STATEMENT");
  IStubElementType PERL_NO_STATEMENT = new PerlNoStatementStubElementType("NO_STATEMENT");

  IStubElementType PERL_DO_EXPR = new PerlDoExprElementType("PERL_DO_EXPR");
  IStubElementType PERL_REQUIRE_EXPR = new PerlRequireExprElementType("PERL_REQUIRE_EXPR");

  IStubElementType PERL_GLOB = new PerlGlobStubElementType("*");

  IStubElementType PERL_VARIABLE_DECLARATION_WRAPPER = new PerlVariableStubElementType("VARIABLE_DECLARATION_WRAPPER");
}
