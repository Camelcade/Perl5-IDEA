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

package com.perl5.lang.perl.idea.intellilang;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.LiteralTextEscaper;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hurricup on 02.08.2015.
 */
public class PerlHeredocLiteralEscaper extends LiteralTextEscaper<PerlHeredocElementImpl>
{
	public PerlHeredocLiteralEscaper(PerlHeredocElementImpl host)
	{
		super(host);
	}

	@Override
	public boolean isOneLine()
	{
		return true;
	}

	@Override
	public boolean decode(@NotNull final TextRange rangeInsideHost, @NotNull StringBuilder outChars)
	{
		outChars.append(myHost.getText(), rangeInsideHost.getStartOffset(), rangeInsideHost.getEndOffset());
		return true;
	}

	@Override
	public int getOffsetInHost(int offsetInDecoded, @NotNull final TextRange rangeInsideHost)
	{
		int offset = offsetInDecoded + rangeInsideHost.getStartOffset();
		if (offset < rangeInsideHost.getStartOffset()) offset = rangeInsideHost.getStartOffset();
		if (offset > rangeInsideHost.getEndOffset()) offset = rangeInsideHost.getEndOffset();
		return offset;
	}

	@NotNull
	@Override
	public TextRange getRelevantTextRange()
	{
		return TextRange.from(0, myHost.getTextLength());
	}
}
