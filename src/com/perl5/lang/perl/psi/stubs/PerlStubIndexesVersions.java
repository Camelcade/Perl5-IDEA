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

package com.perl5.lang.perl.psi.stubs;

/**
 * Created by hurricup on 14.08.2016.
 */
public interface PerlStubIndexesVersions
{
	int _NS_FORMAT_VERSION = 1;
	int _SUB_FORMAT_VERSION = 1;
	int _VAR_FORMAT_VERSION = 2;

	int MASON_FLAGS_INDEX_VERSION = 2;
	int MASON_PARENT_NS_INDEX_VERSION = 1;
	int MASON_NS_INDEX_VERSION = _NS_FORMAT_VERSION + 2;

	int MOJO_HELPER_INDEX_VERSION = _SUB_FORMAT_VERSION + 4;

	int EXTERNAL_ANNOTATIONS_SUB_INDEX_VERSION = 1;
	int EXTERNAL_ANNOTATIONS_NS_INDEX_VERSION = 1;

	int NS_STUB_INDEX_VERSION = _NS_FORMAT_VERSION + 1;
	int PARENT_NS_STUB_INDEX_VERSION = _NS_FORMAT_VERSION + 3;

	int SUB_DECLARATION_INDEX_VERSION = _SUB_FORMAT_VERSION + 1;
	int SUB_DEFINITIONS_INDEX_VERSION = _SUB_FORMAT_VERSION + 3;

	int GLOB_STUB_INDEX_VERSION = _VAR_FORMAT_VERSION + 6;
	int VAR_STUB_INDEX_VERSION = _VAR_FORMAT_VERSION + 1;

	int POD_STUB_INDEX_VERSION = 3;
}
