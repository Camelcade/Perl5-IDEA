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

package com.perl5.lang.perl.idea.completion;

import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;

/**
 * Created by hurricup on 31.05.2015.
 */
public class PerlInsertHandlers
{
	public static final InsertHandler<LookupElement> SEMI_NEWLINE_INSERT_HANDLER = new SemiNewlineInsertHandler();
	public static final InsertHandler<LookupElement> ARRAY_ELEMENT_INSERT_HANDLER = new ArrayElementInsertHandler();
	public static final InsertHandler<LookupElement> HASH_ELEMENT_INSERT_HANDLER = new HashElementInsertHandler();

	/**
	 * Semicolon and newline insert handler
	 */
	static class SemiNewlineInsertHandler implements InsertHandler<LookupElement>
	{
		@Override
		public void handleInsert(final InsertionContext context, LookupElement item)
		{
			final Editor editor = context.getEditor();
			EditorModificationUtil.insertStringAtCaret(editor, ";\n");
		}
	}

	/**
	 * Array element/slice insert handler
	 */
	static class ArrayElementInsertHandler implements InsertHandler<LookupElement>
	{
		@Override
		public void handleInsert(final InsertionContext context, LookupElement item)
		{
			final Editor editor = context.getEditor();
			EditorModificationUtil.insertStringAtCaret(editor, "[]");
			editor.getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
		}
	}

	/**
	 * Hash element/slice insert handler
	 */
	static class HashElementInsertHandler implements InsertHandler<LookupElement>
	{
		@Override
		public void handleInsert(final InsertionContext context, LookupElement item)
		{
			final Editor editor = context.getEditor();
			EditorModificationUtil.insertStringAtCaret(editor, "{}");
			editor.getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
		}
	}
}
