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

package com.perl5.lang.mason.idea.editor;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.parser.MasonPerlParserExtension;

/**
 * Created by hurricup on 21.12.2015.
 */
public class MasonBraceMatcher implements PairedBraceMatcher, MasonPerlParserExtension
{
	private static final BracePair[] PAIRS = new BracePair[]{
			new BracePair(MASON_BLOCK_OPENER, MASON_BLOCK_CLOSER, false),
			new BracePair(MASON_CALL_OPENER, MASON_CALL_CLOSER, false),
			new BracePair(MASON_METHOD_OPENER, MASON_METHOD_CLOSER, false),
			new BracePair(MASON_OVERRIDE_OPENER, MASON_OVERRIDE_CLOSER, false),
			new BracePair(MASON_FILTER_OPENER, MASON_FILTER_CLOSER, false),
			new BracePair(MASON_AFTER_OPENER, MASON_AFTER_CLOSER, false),
			new BracePair(MASON_BEFORE_OPENER, MASON_BEFORE_CLOSER, false),
			new BracePair(MASON_AUGMENT_OPENER, MASON_AUGMENT_CLOSER, false),
			new BracePair(MASON_AROUND_OPENER, MASON_AROUND_CLOSER, false),
			new BracePair(MASON_CLASS_OPENER, MASON_CLASS_CLOSER, false),
			new BracePair(MASON_DOC_OPENER, MASON_DOC_CLOSER, false),
			new BracePair(MASON_TEXT_OPENER, MASON_TEXT_CLOSER, false),
			new BracePair(MASON_PERL_OPENER, MASON_PERL_CLOSER, false),
			new BracePair(MASON_INIT_OPENER, MASON_INIT_CLOSER, false),
			new BracePair(MASON_FLAGS_OPENER, MASON_FLAGS_CLOSER, false),
	};

	@Override
	public BracePair[] getPairs()
	{
		return PAIRS;
	}

	@Override
	public boolean isPairedBracesAllowedBeforeType(IElementType lbraceType, IElementType contextType)
	{
		return true;
	}

	@Override
	public int getCodeConstructStart(PsiFile file, int openingBraceOffset)
	{
		return openingBraceOffset;
	}
}
