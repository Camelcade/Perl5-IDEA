package com.perl5.lang.perl.util;

import com.intellij.psi.tree.IElementType;
import com.perl5.lang.perl.lexer.PerlElementTypes;

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
			BUILT_IN_MAP.put(packageName, PERL_PACKAGE_PRAGMA);
		}
		for( String packageName: BUILT_IN_DEPRECATED )
		{
			BUILT_IN_MAP.put(packageName, PERL_PACKAGE_DEPRECATED);
		}
	}

	public static boolean isBuiltIn(String variable)
	{
		return BUILT_IN_MAP.containsKey(variable.replaceFirst("::$", ""));
	}

	public static IElementType getPackageType(String variable)
	{
		IElementType packageType = BUILT_IN_MAP.get(variable.replaceFirst("::$", ""));
		return packageType == null ? PERL_PACKAGE : packageType;
	}

//
//	/**
//	 * Looks for specific package definitions in all project files
//	 * @param project	project to look in
//	 * @param packageName	exact package name
//	 * @return a list of Psi elements
//	 */
//	public static List<PsiElement> findPackageDefinitions(Project project, String packageName) {
//		List<PsiElement> result = null;
//
//		Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, PerlFileTypePackage.INSTANCE,
//				GlobalSearchScope.allScope(project));
//
//		virtualFiles.addAll(FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, PerlFileTypeScript.INSTANCE,
//				GlobalSearchScope.allScope(project)));
//
////		for (VirtualFile virtualFile : virtualFiles) {
////			PerlFile perlFile = (PerlFile) PsiManager.getInstance(project).findFile(virtualFile);
////			if (perlFile != null) {
////				PerlPackageDefinition[] definitions = PsiTreeUtil.getChildrenOfType(perlFile, PerlPackageDefinition.class);
////				if (definitions != null) {
////					for (PerlPackageDefinition definition: definitions) {
////						if (packageName.equals(definition.getPackageBare().getText())) {
////							if (result == null) {
////								result = new ArrayList<PsiElement>();
////							}
////							result.add(definition.getPackageBare());
////						}
////					}
////				}
////			}
////		}
//		return result != null ? result : Collections.<PsiElement>emptyList();
//	}
//
//	/**
//	 * Looks for all package definitions in all project files
//	 * @param project project to look in
//	 * @return list of psi elements
//	 */
//	public static List<PsiElement> findPackageDefinitions(Project project) {
//		List<PsiElement> result = null;
//
//		Collection<VirtualFile> virtualFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, PerlFileTypePackage.INSTANCE,
//				GlobalSearchScope.allScope(project));
//
//		virtualFiles.addAll(FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, PerlFileTypeScript.INSTANCE,
//				GlobalSearchScope.allScope(project)));
//
///*
//		for (VirtualFile virtualFile : virtualFiles) {
//			PerlFile perlFile = (PerlFile) PsiManager.getInstance(project).findFile(virtualFile);
//			if (perlFile != null) {
//				PerlPackageDefinition[] definitions = PsiTreeUtil.getChildrenOfType(perlFile, PerlPackageDefinition.class);
//				if (definitions != null) {
//					for (PerlPackageDefinition definition: definitions) {
//						if (result == null) {
//							result = new ArrayList<PsiElement>();
//						}
//						result.add(definition.getPackageBare());
//					}
//				}
//			}
//		}
//*/
//		return result != null ? result : Collections.<PsiElement>emptyList();
//	}
}
