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

package com.perl5.lang.perl.extensions.parser;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.impl.source.tree.LeafPsiElement;

/**
 * Created by hurricup on 04.12.2015.
 * Range provider may be used to override getRange in StringContentElement, used in Moose attributes, that can be started with +
 */
public interface PerlElementRangeProvider
{
	/**
	 * Provides TextRange for nested LeafElement. Used in Moose and other extensions
	 * @param element LeafElement
	 * @return textRange
	 */
	TextRange getNestedElementTextRange(LeafPsiElement element);
}
