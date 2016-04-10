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

package com.perl5.lang.pod.parser.psi;

import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 26.03.2016.
 */
public interface PodCompositeElement extends PsiElement, PodRenderableElement, ItemPresentation
{
	/**
	 * Returns if element contains direct child X<>
	 *
	 * @return true if is
	 */
	boolean isIndexed();

	/**
	 * Calculating list level for current element
	 *
	 * @return list level zero-based
	 */
	int getListLevel();


	/**
	 * Heading elements should return true
	 *
	 * @return
	 */
	boolean isHeading();

	/**
	 * returns heading level
	 *
	 * @return
	 */
	int getHeadingLevel();

	/**
	 * Returns text for UsageViewTypeLocation.INSTANCE
	 *
	 * @return
	 */
	@Nullable
	String getUsageViewTypeLocation();

	/**
	 * Returns text for UsageViewLongNameLocation.INSTANCE
	 *
	 * @return
	 */
	@Nullable
	String getUsageViewLongNameLocation();

	/**
	 * Returns text for UsageViewShortNameLocation.INSTANCE
	 *
	 * @return
	 */
	@Nullable
	String getUsageViewShortNameLocation();
}
