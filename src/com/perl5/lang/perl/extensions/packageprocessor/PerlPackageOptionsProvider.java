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

package com.perl5.lang.perl.extensions.packageprocessor;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Created by hurricup on 18.08.2015.
 * Implement this interface if package should provide options for autocompletion
 */
public interface PerlPackageOptionsProvider
{
	/**
	 * Returns full list of available options with explanations
	 *
	 * @return HashMap of options
	 */
	@NotNull
	public Map<String, String> getOptions();

	/**
	 * Returns full list of available bundled options, atm they are with other icon
	 * fixme Probably we should return options with icons?
	 *
	 * @return HashMap of bundled options
	 */
	@NotNull
	public Map<String, String> getOptionsBundles();
}
