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

package com.perl5.lang.perl.idea.modules;

import com.intellij.openapi.module.ModuleType;
import com.perl5.PerlIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by hurricup on 28.05.2015.
 */
public class PerlModuleType extends ModuleType<PerlModuleBuilder>
{
	public static PerlModuleType INSTANCE = new PerlModuleType();

	public static final String PERL_MODULE = "PERL5_MODULE";
	public static final String MODULE_NAME = "Perl5";
	public static final String MODULE_DESCRIPTION = "Anyting written in Perl5";
//	public static final String PERL5_GROUP = "Perl5";

	public PerlModuleType(){super(PERL_MODULE);}

	@NotNull
	@Override
	public PerlModuleBuilder createModuleBuilder()
	{
		return new PerlModuleBuilder();
	}

	@NotNull
	@Override
	public String getName()
	{
		return MODULE_NAME;
	}

	@NotNull
	@Override
	public String getDescription()
	{
		return MODULE_DESCRIPTION;
	}

	@Override
	public Icon getBigIcon()
	{
		return PerlIcons.PERL_MODULE;
	}

	@Override
	public Icon getNodeIcon(@Deprecated boolean isOpened)
	{
		return PerlIcons.PM_FILE;
	}



}
