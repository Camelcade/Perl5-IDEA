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

package com.perl5.lang.perl.psi;

import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.perl5.lang.perl.extensions.PerlCodeGenerator;
import com.perl5.lang.perl.psi.properties.PerlLabelScope;
import com.perl5.lang.perl.psi.properties.PerlLexicalScope;
import com.perl5.lang.pod.parser.psi.PodLinkTarget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * Created by hurricup on 09.08.2015.
 */
public interface PerlFile extends PsiFile, PerlLexicalScope, PerlNamespaceContainer, PerlLabelScope, ItemPresentation, PodLinkTarget
{
	/**
	 * Checks variable type from cache or using callback getVariableTypeHeavy
	 *
	 * @param element variable element
	 * @return variable type string
	 */
	String getVariableType(PerlVariable element);

	/**
	 * Checks method namespace in cache or using callback getContextPackageNameHeavy
	 *
	 * @param element method element
	 * @return variable type string
	 */
	String getMethodNamespace(PerlMethod element);

	/**
	 * Returns lib paths for current file
	 *
	 * @return lib paths list in normal order
	 */
	@NotNull
	List<VirtualFile> getLibPaths();

	/**
	 * Returns resolve scope for nested elements. Probably it should be getResolveScope, but still not sure
	 *
	 * @return search scope
	 */
//	@NotNull
//	GlobalSearchScope getElementsResolveScope();

	/**
	 * Recursively collects virtual files included in current file
	 *
	 * @param includedVirtualFiles set of already gathered files
	 */
	void collectIncludedFiles(Set<VirtualFile> includedVirtualFiles);

	/**
	 * Resolves package name according to the current file contents
	 *
	 * @param canonicalPackageName canonical package name
	 * @return target PsiFile if resolved
	 */
	@Nullable
	PsiFile resolvePackageNameToPsi(String canonicalPackageName);

	/**
	 * Resolves package name according to the current file contents
	 *
	 * @param canonicalPackageName canonical package name
	 * @return target VirtualFile if resolved
	 */
	@Nullable
	VirtualFile resolvePackageNameToVirtualFile(String canonicalPackageName);

	/**
	 * Resolves relative path (from do/require) according to the current file contents
	 *
	 * @param relativePath relative path
	 * @return target PsiFile if resolved
	 */
	@Nullable
	PsiFile resolveRelativePathToPsi(String relativePath);

	/**
	 * Resolves relative path (from do/require) according to the current file contents
	 *
	 * @param relativePath relative path
	 * @return target VirtualFile if resolved
	 */
	@Nullable
	VirtualFile resolveRelativePathToVirtualFile(String relativePath);

	/**
	 * Returns generator for overriding elements
	 *
	 * @return override generator
	 */
	PerlCodeGenerator getCodeGenerator();

	/**
	 * Returns perl content with templating injections replaced with spaces
	 *
	 * @return bytes for external analysis/formatting
	 */
	@Nullable
	byte[] getPerlContentInBytes();

	/**
	 * Returns true if contents may be re-formatted with Perl::Tidy
	 *
	 * @return answer
	 */
	boolean isPerlTidyReformattable();
}
