package com.perl5.lang.perl.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.perl5.lang.perl.files.PerlFileTypePackage;
import com.perl5.lang.perl.files.PerlFileTypeScript;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlFile;
import com.perl5.lang.perl.psi.PerlPackageNamespace;
import com.perl5.lang.perl.psi.PerlScalar;
import com.perl5.lang.perl.psi.impl.PerlScalarImpl;

import java.util.*;

/**
 * Created by hurricup on 24.04.2015.
 */
public class PerlPackageUtil implements PerlElementTypes, PerlPackageUtilBuiltIn
{
	protected static final HashMap<String,IElementType> BUILT_IN_MAP = new HashMap<String,IElementType>();


	static{
		for( String packageName: BUILT_IN )
		{
			BUILT_IN_MAP.put(packageName, PERL_PACKAGE_BUILT_IN);
		}
		for( String packageName: BUILT_IN_PRAGMA )
		{
			BUILT_IN_MAP.put(packageName, PERL_PACKAGE_BUILT_IN_PRAGMA);
		}
		for( String packageName: BUILT_IN_DEPRECATED )
		{
			BUILT_IN_MAP.put(packageName, PERL_PACKAGE_BUILT_IN_DEPRECATED);
		}
	}

	/**
	 * Checking if specific package is perl core package
	 * @param packageName	exact package name
	 * @return package token type
	 */
	public static IElementType getPackageType(String packageName)
	{
		IElementType packageType = BUILT_IN_MAP.get(packageName);

		return packageType == null
				? PERL_PACKAGE_USER
				: packageType;
	}

	/**
	 * Looks for specific package definitions in all project files
	 * @param project	project to look in
	 * @param packageName	exact package name
	 * @return a list of Psi elements
	 */
	public static List<PsiElement> findPackageDefinitions(Project project, String packageName) {
		List<PsiElement> result = null;

		Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, PerlFileTypePackage.INSTANCE,
				GlobalSearchScope.allScope(project));

		virtualFiles.addAll(FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, PerlFileTypeScript.INSTANCE,
				GlobalSearchScope.allScope(project)));

		for (VirtualFile virtualFile : virtualFiles) {
			PerlFile perlFile = (PerlFile) PsiManager.getInstance(project).findFile(virtualFile);
			if (perlFile != null) {
				PerlPackageNamespace[] definitions = PsiTreeUtil.getChildrenOfType(perlFile, PerlPackageNamespace.class);
				if (definitions != null) {
					for (PerlPackageNamespace definition: definitions) {
						if (packageName.equals(definition.getPackageBare().getText())) {
							if (result == null) {
								result = new ArrayList<PsiElement>();
							}
							result.add(definition.getPackageBare());
						}
					}
				}
			}
		}
		return result != null ? result : Collections.<PsiElement>emptyList();
	}

	/**
	 * Looks for all package definitions in all project files
	 * @param project project to look in
	 * @return list of psi elements
	 */
	public static List<PsiElement> findPackageDefinitions(Project project) {
		List<PsiElement> result = null;

		Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, PerlFileTypePackage.INSTANCE,
				GlobalSearchScope.allScope(project));

		virtualFiles.addAll(FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, PerlFileTypeScript.INSTANCE,
				GlobalSearchScope.allScope(project)));

		for (VirtualFile virtualFile : virtualFiles) {
			PerlFile perlFile = (PerlFile) PsiManager.getInstance(project).findFile(virtualFile);
			if (perlFile != null) {
				PerlPackageNamespace[] definitions = PsiTreeUtil.getChildrenOfType(perlFile, PerlPackageNamespace.class);
				if (definitions != null) {
					for (PerlPackageNamespace definition: definitions) {
						if (result == null) {
							result = new ArrayList<PsiElement>();
						}
						result.add(definition.getPackageBare());
					}
				}
			}
		}
		return result != null ? result : Collections.<PsiElement>emptyList();
	}
}
