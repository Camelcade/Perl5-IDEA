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

package com.perl5.lang.perl.parser.perlswitch.idea.liveTemplates;

import com.intellij.psi.PsiElement;
import com.perl5.lang.perl.idea.livetemplates.PerlTemplateContextType;
import com.perl5.lang.perl.parser.perlswitch.PerlSwitchElementPatterns;

/**
 * Created by hurricup on 15.01.2016.
 */
public class PerlInSwitchAfterCaseTemplateContextType extends PerlTemplateContextType.Prefix
{
	public PerlInSwitchAfterCaseTemplateContextType()
	{
		super("PERL5_INSIDE_SWITCH_AFTER_CASE", "Inside Switch after case");
	}

	@Override
	public boolean isInContext(PsiElement element)
	{
		return super.isInContext(element) && PerlSwitchElementPatterns.SWITCH_PREFIX_AFTER_CASE_PATTERN.accepts(element);
	}

}
