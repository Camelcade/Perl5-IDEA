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

import com.intellij.psi.PsiFile;

/**
 * Created by hurricup on 09.05.2015.
 * Represents a package file. I guess package should be renamed to the namespace
 * Package file may contain code or packages (namespaces)
 */
public class PerlPackageFile
{
	private String name;
	private PsiFile psiFile;

	public PerlPackageFile( String packageName )
	{
		assert packageName != null;
		name = packageName;
	}

	public String getName()
	{
		return name;
	}

	/**
	 * Builds package filename from package name
	 * @return relative filename
	 */
	public String getFilename()
	{
		StringBuilder path = new StringBuilder("");

		for( String part: name.split("::"))
		{
			if( path.length() > 0 )
				path.append("/");

			path.append(part);
		}
		path.append(".pm");
		return path.toString();
	}

	public PsiFile getPsiFile()
	{
		return psiFile;
	}

	public void setPsiFile(PsiFile psiFile)
	{
		this.psiFile = psiFile;
	}

	public boolean isLoaded()
	{
		return psiFile != null;
	}

}
