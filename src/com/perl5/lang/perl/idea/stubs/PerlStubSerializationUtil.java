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

package com.perl5.lang.perl.idea.stubs;

import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 16.07.2015.
 */
public class PerlStubSerializationUtil
{
	public static void writeStringsList(@NotNull StubOutputStream dataStream, List<String> stringList) throws IOException
	{
		dataStream.writeInt(stringList.size());
		for( String stringItem: stringList)
			dataStream.writeName(stringItem);
	}

	public static List<String> readStringsList(@NotNull StubInputStream dataStream) throws IOException
	{
		int listSize = dataStream.readInt();
		ArrayList<String> result = new ArrayList<>(listSize);
		for( int i = 0; i < listSize; i++)
			result.add(dataStream.readName().toString());
		return result;
	}

}
