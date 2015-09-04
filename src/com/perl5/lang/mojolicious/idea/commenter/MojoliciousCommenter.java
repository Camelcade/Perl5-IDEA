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

package com.perl5.lang.mojolicious.idea.commenter;

import com.intellij.codeInsight.generation.SelfManagingCommenter;
import com.intellij.lang.Commenter;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 04.09.2015.
 */
public class MojoliciousCommenter implements Commenter, SelfManagingCommenter<MojoliciousCommenter.CommenterDataHolder>
{
	@Nullable
	@Override
	public String getLineCommentPrefix()
	{
		return null;
	}

	@Nullable
	@Override
	public String getBlockCommentPrefix()
	{
		return null;
	}

	@Nullable
	@Override
	public String getBlockCommentSuffix()
	{
		return null;
	}

	@Nullable
	@Override
	public String getCommentedBlockCommentPrefix()
	{
		return null;
	}

	@Nullable
	@Override
	public String getCommentedBlockCommentSuffix()
	{
		return null;
	}

	@Nullable
	@Override
	public CommenterDataHolder createLineCommentingState(int startLine, int endLine, Document document, PsiFile file)
	{
		return null;
	}

	@Nullable
	@Override
	public CommenterDataHolder createBlockCommentingState(int selectionStart, int selectionEnd, Document document, PsiFile file)
	{
		return null;
	}

	@Override
	public void commentLine(int line, int offset, Document document, CommenterDataHolder data)
	{

	}

	@Override
	public void uncommentLine(int line, int offset, Document document, CommenterDataHolder data)
	{

	}

	@Override
	public boolean isLineCommented(int line, int offset, Document document, CommenterDataHolder data)
	{
		return false;
	}

	@Nullable
	@Override
	public String getCommentPrefix(int line, Document document, CommenterDataHolder data)
	{
		return null;
	}

	@Nullable
	@Override
	public TextRange getBlockCommentRange(int selectionStart, int selectionEnd, Document document, CommenterDataHolder data)
	{
		return null;
	}

	@Nullable
	@Override
	public String getBlockCommentPrefix(int selectionStart, Document document, CommenterDataHolder data)
	{
		return null;
	}

	@Nullable
	@Override
	public String getBlockCommentSuffix(int selectionEnd, Document document, CommenterDataHolder data)
	{
		return null;
	}

	@Override
	public void uncommentBlockComment(int startOffset, int endOffset, Document document, CommenterDataHolder data)
	{

	}

	@NotNull
	@Override
	public TextRange insertBlockComment(int startOffset, int endOffset, Document document, CommenterDataHolder data)
	{
		return null;
	}

	static class CommenterDataHolder extends com.intellij.codeInsight.generation.CommenterDataHolder
	{

	}

}
