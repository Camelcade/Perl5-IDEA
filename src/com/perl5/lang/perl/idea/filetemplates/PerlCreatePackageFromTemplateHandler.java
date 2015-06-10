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

package com.perl5.lang.perl.idea.filetemplates;

import com.intellij.ide.fileTemplates.DefaultCreateFromTemplateHandler;
import com.intellij.ide.fileTemplates.FileTemplate;
import com.perl5.lang.perl.PerlFileTypePackage;

import java.util.Map;

/**
 * Created by hurricup on 10.06.2015.
 */
public class PerlCreatePackageFromTemplateHandler extends DefaultCreateFromTemplateHandler
{

	@Override
	public boolean handlesTemplate(FileTemplate template)
	{
		return template.isTemplateOfType(PerlFileTypePackage.INSTANCE);
	}


	@Override
	public void prepareProperties(Map<String, Object> props)
	{
		String fileName = (String) props.get("NAME");
		if (fileName.endsWith(".pm"))
			fileName = fileName.replace(".pm", "");

		String packageName = (String) props.get("PACKAGE_NAME");
		if (!packageName.isEmpty())
			packageName = packageName + "." + fileName;
		else
			packageName = fileName;

		props.put("PERL_PACKAGE_NAME", packageName.replace(".", "::"));

		super.prepareProperties(props);
	}
}
