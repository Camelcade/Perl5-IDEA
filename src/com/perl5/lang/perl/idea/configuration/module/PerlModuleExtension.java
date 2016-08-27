/*
 * Copyright 2016 Alexandr Evstigneev
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

package com.perl5.lang.perl.idea.configuration.module;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleExtension;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;

/**
 * Created by hurricup on 23.08.2016.
 */
public class PerlModuleExtension extends ModuleExtension<PerlModuleExtension>
{
	private Module myModule;
	private boolean myWritable;
	private PerlModuleExtension myOriginal;

	public static PerlModuleExtension getInstance(final Module module)
	{
		return ModuleRootManager.getInstance(module).getModuleExtension(PerlModuleExtension.class);
	}

	public PerlModuleExtension(Module module)
	{
		this(module, false, null);
	}

	private void debug(String msg)
	{
		System.err.println(this + " " + msg);
	}

	private PerlModuleExtension(Module module, boolean writable, PerlModuleExtension original)
	{
		myModule = module;
		myWritable = writable;
		myOriginal = original;
		debug("Constructed " + module + " " + writable + " " + original);
	}

	private PerlModuleExtension(PerlModuleExtension perlModuleExtension, boolean writable)
	{
		this(perlModuleExtension.myModule, writable, perlModuleExtension);
	}

	@Override
	public ModuleExtension getModifiableModel(boolean writable)
	{
		debug("getting model");
		return new PerlModuleExtension(this, writable);
	}

	@Override
	public void commit()
	{
		debug("commiting");
	}

	@Override
	public boolean isChanged()
	{
		debug("check if changed");
		return true;
	}

	@Override
	public void dispose()
	{
		debug("disposing");
		myOriginal = null;
		myModule = null;
	}

	@Override
	public void readExternal(Element element) throws InvalidDataException
	{
		debug("read external " + element);
	}

	@Override
	public void writeExternal(Element element) throws WriteExternalException
	{
		debug("write external " + element);
	}
}
