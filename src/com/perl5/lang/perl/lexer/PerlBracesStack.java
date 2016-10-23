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

package com.perl5.lang.perl.lexer;

import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.Nullable;

import java.util.EmptyStackException;

/**
 * Created by hurricup on 18.10.2016.
 */
public class PerlBracesStack
{
	private int[] data;
	private Object[] additionalData;
	private int size;

	public PerlBracesStack(int initialCapacity)
	{
		data = new int[initialCapacity];
		additionalData = new Object[initialCapacity];
		size = 0;
	}

	public PerlBracesStack()
	{
		this(5);
	}

	public void push(int t)
	{
		push(t, null);
	}

	public void push(int t, @Nullable Object od)
	{
		if (size >= data.length)
		{
			data = ArrayUtil.realloc(data, data.length * 3 / 2);
			additionalData = ArrayUtil.realloc(additionalData, additionalData.length * 3 / 2, Object[]::new);
		}
		data[size] = t;
		additionalData[size++] = od;
	}

	public int peek()
	{
		if (size == 0)
		{
			throw new EmptyStackException();
		}
		return data[size - 1];
	}

	@Nullable
	public Object peekAdditional()
	{
		if (size == 0)
		{
			throw new EmptyStackException();
		}
		return additionalData[size - 1];
	}

	public void pop()
	{
		if (size == 0)
		{
			throw new EmptyStackException();
		}
		--size;
	}

	public int size()
	{
		return size;
	}

	public boolean isEmpty()
	{
		return size == 0;
	}

	public void clear()
	{
		size = 0;
	}

	public int incLast()
	{
		return ++data[size - 1];
	}

	public int decLast()
	{
		return --data[size - 1];
	}
}
