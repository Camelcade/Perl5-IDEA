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

package com.perl5.lang.perl.idea.index;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.indexing.*;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import com.perl5.lang.perl.PerlFileTypePackage;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Map;

/**
 * Created by hurricup on 07.06.2015.
 */
public class PerlPackageIndex extends ScalarIndexExtension<String>
{
	private static final ID<String, Void> PERL_PACKAGE_INDEX = ID.create("PerlPackageIndex");
	private static final int INDEX_VERSION = 1;
	private static final EnumeratorStringDescriptor DESCRIPTOR = new EnumeratorStringDescriptor();
	private static final FileBasedIndex.InputFilter PERL_PACKAGE_INDEX_FILTER = new PerlPackageIndexFilter();

	@NotNull
	private DataIndexer<String, Void, FileContent> myDataIndexer = new PerlPackageIndexer();

	@NotNull
	@Override
	public ID<String, Void> getName()
	{
		return PERL_PACKAGE_INDEX;
	}

	@NotNull
	@Override
	public DataIndexer<String, Void, FileContent> getIndexer()
	{
		return myDataIndexer;
	}

	@NotNull
	@Override
	public KeyDescriptor<String> getKeyDescriptor()
	{
		return DESCRIPTOR;
	}

	@NotNull
	@Override
	public FileBasedIndex.InputFilter getInputFilter()
	{
		return PERL_PACKAGE_INDEX_FILTER;
	}

	@Override
	public boolean dependsOnFileContent()
	{
		return false;
	}

	@Override
	public int getVersion()
	{
		return INDEX_VERSION;
	}

	private static class PerlPackageIndexer implements DataIndexer<String, Void, FileContent>{
		@NotNull
		@Override
		public Map<String, Void> map(@NotNull FileContent inputData)
		{
			return Collections.EMPTY_MAP;
		}
	}

	private static class PerlPackageIndexFilter implements FileBasedIndex.InputFilter
	{
		@Override
		public boolean acceptInput(@NotNull VirtualFile file)
		{
			return file.getFileType() == PerlFileTypePackage.INSTANCE;
		}
	}

}
