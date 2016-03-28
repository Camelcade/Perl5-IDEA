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

package com.perl5.lang.perl.documentation;

import com.intellij.codeInsight.documentation.DocumentationManager;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.PsiElementProcessor;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.perl5.lang.perl.PerlScopes;
import com.perl5.lang.perl.lexer.PerlElementTypes;
import com.perl5.lang.perl.psi.PerlHeredocOpener;
import com.perl5.lang.perl.psi.PerlHeredocTerminatorElement;
import com.perl5.lang.perl.psi.PerlVariable;
import com.perl5.lang.perl.psi.impl.PerlHeredocElementImpl;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import com.perl5.lang.perl.util.PerlPackageUtil;
import com.perl5.lang.pod.PodSearchHelper;
import com.perl5.lang.pod.parser.psi.*;
import com.perl5.lang.pod.parser.psi.impl.PodFileImpl;
import com.perl5.lang.pod.parser.psi.util.PodFileUtil;
import com.perl5.lang.pod.parser.psi.util.PodRenderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 26.03.2016.
 */
public class PerlDocUtil implements PerlElementTypes
{
	// fixme this shit must be refactored
	@Nullable
	public static PsiElement resolveDocLink(String link, PsiElement origin)
	{
		final Project project = origin.getProject();
		PodLinkDescriptor descriptor = PodLinkDescriptor.getDescriptor(link);

		if (descriptor != null)
		{
			PsiFile targetFile = origin.getContainingFile();

			if (descriptor.getFileId() != null)
			{
				// seek file
				String fileId = descriptor.getFileId();
				targetFile = null;

				if (fileId.contains(PerlPackageUtil.PACKAGE_SEPARATOR) || !StringUtil.startsWith(fileId, "perl")) // can be Foo/Bar.pod or Foo/Bar.pm
				{
					String podRelativePath = PodFileUtil.getFilenameFromPackage(fileId);

					for (VirtualFile classRoot : ProjectRootManager.getInstance(project).orderEntries().getClassesRoots())
					{
						VirtualFile targetVirtualFile = classRoot.findFileByRelativePath(podRelativePath);
						if (targetVirtualFile != null)
						{
							if ((targetFile = PsiManager.getInstance(project).findFile(targetVirtualFile)) != null)
							{
								break;
							}
						}
					}

					if (targetFile == null) // pod not found, search for PM
					{
						String packageRelativePath = PerlPackageUtil.getPackagePathByName(fileId);

						for (VirtualFile classRoot : ProjectRootManager.getInstance(project).orderEntries().getClassesRoots())
						{
							VirtualFile targetVirtualFile = classRoot.findFileByRelativePath(packageRelativePath);
							if (targetVirtualFile != null)
							{
								if ((targetFile = PsiManager.getInstance(project).findFile(targetVirtualFile)) != null)
								{
									break;
								}
							}
						}
					}
				}
				else // top level file perl.*
				{
					fileId += "." + PodFileUtil.POD_FILE_EXTENSION;

					PsiFile[] psiFiles = FilenameIndex.getFilesByName(
							origin.getProject(),
							fileId,
							PerlScopes.getProjectAndLibrariesScope(origin.getProject()));
					if (psiFiles.length > 0)
					{
						targetFile = psiFiles[0];
					}
				}

/*				// fixme invoked multiple times
				if (targetFile == null)
				{
					Messages.showErrorDialog(project, "Unable to find pod file: " + fileId, "File Not Found");
				}
*/
			}

			if (targetFile != null)
			{
				if (descriptor.getSection() == null)
				{
					if (targetFile instanceof PodFile)
						return targetFile;

					return PsiTreeUtil.findChildOfType(targetFile, PodCompositeElement.class);
				}
				else    // seek section
				{
					PodDocumentPattern pattern = PodDocumentPattern.headingAndItemPattern(descriptor.getSection());
					PsiElement targetElement = searchPodElement(targetFile, pattern);
					if (targetElement == null)
					{
						System.err.println("Unable to resolve: " + descriptor.getSection());
					}
					return targetElement;
				}
			}
		}
		return null;
	}

	@Nullable
	public static PsiElement getPerlVarDoc(PerlVariable variable)
	{
		final Project project = variable.getProject();

		if (variable.isBuiltIn())
		{
			PerlVariableType actualType = variable.getActualType();
			String variableName = variable.getName();

			if (actualType != null && StringUtil.isNotEmpty(variableName))
			{
				String text = actualType.getSigil() + variableName;
				PodDocumentPattern pattern = PodDocumentPattern.itemPattern(text);

				if (text.matches("$[123456789]"))
				{
					pattern.setItemPattern("$<digits>");
				}

				return searchPodElementInFile(project, PodSearchHelper.PERL_VAR_FILE_NAME, PodDocumentPattern.itemPattern(text));
			}
		}

		return null;
	}

	@Nullable
	public static PsiElement getPerlFuncDoc(PsiElement element)
	{
		final Project project = element.getProject();
		String text = element.getText();

		if (text.matches("-[rwxoRWXOeszfdlpSbctugkTBMAC]"))
		{
			text = "-X";
		}

		PodCompositeElement podElement = searchPodElementInFile(project, PodSearchHelper.PERL_FUNC_FILE_NAME, PodDocumentPattern.itemPattern(text));

		return podElement == null ? getPerlOpDoc(element) : podElement;
	}

	@Nullable
	public static PsiElement getPerlOpDoc(@NotNull PsiElement element)
	{
		final Project project = element.getProject();
		String text = element.getText();
		IElementType elementType = element.getNode().getElementType();

		// fixme use map?
		PodDocumentPattern pattern = PodDocumentPattern.indexPattern(text);
		if (element instanceof PerlHeredocOpener || element instanceof PerlHeredocTerminatorElement || element instanceof PerlHeredocElementImpl)
		{
			pattern.setIndexKey("heredoc");    // searches with X<>
		}
		else if (elementType == RESERVED_QR)
		{
			pattern.setIndexKey(null);
			pattern.setItemPattern("qr/STRING/");
		}
		else if (elementType == RESERVED_S)
		{
			pattern.setIndexKey("regexp, replace");
		}
		else if (elementType == RESERVED_M)
		{
			pattern.setIndexKey("operator, match");
		}
		else if (text.matches("-[rwxoRWXOeszfdlpSbctugkTBMAC]"))
		{
			pattern.setIndexKey("-X");
		}
		else if ("=>".equals(text))
		{
			pattern.setIndexKey(",");
		}
		else if ("?".equals(text) || ":".equals(text))
		{
			pattern.setIndexKey("?:");
		}

		return searchPodElementInFile(project, PodSearchHelper.PERL_OP_FILE_NAME, pattern);
	}

	protected static PodCompositeElement searchPodElementInFile(Project project, String fileName, PodDocumentPattern pattern)
	{
		PsiFile[] psiFiles = FilenameIndex.getFilesByName(project, fileName, PerlScopes.getProjectAndLibrariesScope(project));
		if (psiFiles.length > 0)
		{
			return searchPodElement(psiFiles[0], pattern);
		}
		return null;
	}

	@Nullable
	protected static PodCompositeElement searchPodElement(PsiFile psiFile, final PodDocumentPattern pattern)
	{
		final List<PodCompositeElement> result = new ArrayList<PodCompositeElement>();

		PsiTreeUtil.processElements(psiFile, new PsiElementProcessor()
		{
			@Override
			public boolean execute(@NotNull PsiElement element)
			{
				if (pattern.accepts(element))
				{
					if (element instanceof PodFormatterX)
					{
						PsiElement container = PsiTreeUtil.getParentOfType(element, PodTitledSection.class);
						if (container != null)
						{
							result.add((PodCompositeElement) container);
							return false;
						}
					}
					else
					{
						result.add((PodCompositeElement) element);
						return false;
					}
				}
				return true;
			}
		});

		return result.isEmpty() ? null : result.get(0);
	}

	@Nullable
	protected static String renderElement(PodCompositeElement element)
	{
		if (element == null)
			return null;

		if (element instanceof PodFileImpl)
		{
			return ((PodFileImpl) element).getAsHTML();
		}
		else if (element.getNode().getElementType() == PerlElementTypes.POD)
		{
			return PodRenderUtil.renderPsiRangeAsHTML(element.getFirstChild(), element.getLastChild());
		}

		PodTitledSection podSection = null;

		if (element instanceof PodTitledSection)
		{
			podSection = (PodTitledSection) element;
		}

		if (podSection != null)
		{
			boolean hasContent = podSection.hasContent();
			PsiElement run = podSection;
			PsiElement lastSection = podSection;

			// detecting first section
			while (true)
			{
				PsiElement prevSibling = run.getPrevSibling();
				if (prevSibling == null)
				{
					break;
				}
				if (prevSibling instanceof PodSection && ((PodSection) prevSibling).hasContent())
				{
					break;
				}
				if (prevSibling instanceof PodTitledSection)
				{
					podSection = (PodTitledSection) prevSibling;
				}
				run = prevSibling;
			}

			// detecting last section
			while (!hasContent)
			{
				PsiElement nextSibling = lastSection.getNextSibling();

				if (nextSibling == null)
					break;
				hasContent = nextSibling instanceof PodSection && ((PodSection) nextSibling).hasContent();

				lastSection = nextSibling;
			}

			StringBuilder builder = new StringBuilder();

			// appending toplink
			PsiFile file = podSection.getContainingFile();
			if (file instanceof PodLinkTarget)
			{
				ItemPresentation filePresentation = file.getPresentation();
				String fileLink = ((PodLinkTarget) file).getPodLink();
				if (filePresentation != null && fileLink != null)
				{
					builder.append("<p><a href=\"");
					builder.append(DocumentationManager.PSI_ELEMENT_PROTOCOL);
					builder.append(PodRenderUtil.encodeLink(fileLink));
					builder.append("\">");
					builder.append(filePresentation.getPresentableText());
					builder.append("</a></p>");

				}
			}

			String closeTag = "";

			if (podSection instanceof PodSectionItem)
			{
				if (((PodSectionItem) podSection).isBulleted())
				{
					builder.append("<ul style=\"fon-size:200%;\">");
					closeTag = "</ul>";
				}
				else
				{
					builder.append("<dl>");
					closeTag = "</dl>";
				}
			}

			builder.append(PodRenderUtil.renderPsiRangeAsHTML(podSection, lastSection));
			builder.append(closeTag);
			return builder.toString();
		}
		return null;
	}

}
