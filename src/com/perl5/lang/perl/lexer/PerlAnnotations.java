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

package com.perl5.lang.perl.lexer;

import com.intellij.psi.tree.IElementType;

import java.util.HashMap;

/**
 * Created by hurricup on 03.06.2015.
 */
public class PerlAnnotations implements PerlElementTypes
{
	public static final HashMap<String, IElementType> TOKEN_TYPES = new HashMap<>();

	static
	{
		TOKEN_TYPES.put("deprecated", ANNOTATION_DEPRECATED_KEY);
		TOKEN_TYPES.put("override", ANNOTATION_OVERRIDE_KEY);
		TOKEN_TYPES.put("method", ANNOTATION_METHOD_KEY);
		TOKEN_TYPES.put("returns", ANNOTATION_RETURNS_KEY);
	}
}
