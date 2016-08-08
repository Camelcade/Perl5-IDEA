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

package com.perl5.lang.perl.psi.utils;

import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubInputStream;
import com.intellij.psi.stubs.StubOutputStream;
import com.intellij.util.io.StringRef;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.properties.PerlNamespaceElementContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * Created by hurricup on 03.06.2015.
 */
public class PerlSubAnnotations
{
	boolean isMethod = false;
	boolean isDeprecated = false;
	boolean isAbstract = false;
	boolean isOverride = false;
	PerlReturnType returnType = PerlReturnType.VALUE;
	String returns = null;

	public PerlSubAnnotations()
	{
	}

	public PerlSubAnnotations(boolean isMethod, boolean isDeprecated, boolean isAbstract, boolean isOverride, String returns, PerlReturnType returnType)
	{
		this.isMethod = isMethod;
		this.isDeprecated = isDeprecated;
		this.isAbstract = isAbstract;
		this.isOverride = isOverride;
		this.returns = returns;
		this.returnType = returnType;
	}

	public PerlSubAnnotations(boolean isMethod, boolean isDeprecated, boolean isAbstract, boolean isOverride, StringRef returns, PerlReturnType returnType)
	{
		this(isMethod, isDeprecated, isAbstract, isOverride, returns == null ? null : returns.toString(), returnType);
	}

	public static PerlSubAnnotations deserialize(@NotNull StubInputStream dataStream) throws IOException
	{
		return new PerlSubAnnotations(
				dataStream.readBoolean(),
				dataStream.readBoolean(),
				dataStream.readBoolean(),
				dataStream.readBoolean(),
				dataStream.readName(),
				PerlReturnType.valueOf(dataStream.readName().toString())
		);
	}

	@Nullable
	public static PerlSubAnnotations createFromAnnotationsList(List<PerlAnnotation> annotations)
	{
		if (annotations.isEmpty())
		{
			return null;
		}

		PerlSubAnnotations myAnnotations = new PerlSubAnnotations();

		for (PerlAnnotation annotation : annotations)
		{
			if (annotation instanceof PsiPerlAnnotationAbstract)
			{
				myAnnotations.setIsAbstract(true);
			}
			else if (annotation instanceof PsiPerlAnnotationDeprecated)
			{
				myAnnotations.setIsDeprecated(true);
			}
			else if (annotation instanceof PsiPerlAnnotationMethod)
			{
				myAnnotations.setIsMethod(true);
			}
			else if (annotation instanceof PsiPerlAnnotationOverride)
			{
				myAnnotations.setIsOverride(true);
			}
			else if (annotation instanceof PsiPerlAnnotationReturns) // returns
			{
				PsiElement possibleNamespace = annotation.getLastChild();
				if (possibleNamespace instanceof PerlNamespaceElement)
				{
					myAnnotations.setReturns(((PerlNamespaceElement) possibleNamespace).getCanonicalName());
					myAnnotations.setReturnType(PerlReturnType.REF);
					// todo implement brackets and braces
				}
			}
		}

		return myAnnotations;
	}

	public void serialize(@NotNull StubOutputStream dataStream) throws IOException
	{
		dataStream.writeBoolean(isMethod);
		dataStream.writeBoolean(isDeprecated);
		dataStream.writeBoolean(isAbstract);
		dataStream.writeBoolean(isOverride);
		dataStream.writeName(returns);
		dataStream.writeName(returnType.toString());
	}

	public boolean isMethod()
	{
		return isMethod;
	}

	public void setIsMethod(boolean isMethod)
	{
		this.isMethod = isMethod;
	}

	public boolean isDeprecated()
	{
		return isDeprecated;
	}

	public void setIsDeprecated(boolean isDeprecated)
	{
		this.isDeprecated = isDeprecated;
	}

	public boolean isAbstract()
	{
		return isAbstract;
	}

	public void setIsAbstract(boolean isAbstract)
	{
		this.isAbstract = isAbstract;
	}

	public boolean isOverride()
	{
		return isOverride;
	}

	public void setIsOverride(boolean isOverride)
	{
		this.isOverride = isOverride;
	}

	public String getReturns()
	{
		return returns;
	}

	public void setReturns(String returns)
	{
		this.returns = returns;
	}

	public PerlReturnType getReturnType()
	{
		return returnType;
	}

	public void setReturnType(PerlReturnType returnType)
	{
		this.returnType = returnType;
	}

}
