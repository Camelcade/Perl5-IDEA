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

package com.perl5.lang.perl.idea.generation.handlers;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.SpeedSearchComparator;
import com.intellij.util.Processor;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.extensions.PerlCodeGenerator;
import com.perl5.lang.perl.idea.codeInsight.PerlMethodMember;
import com.perl5.lang.perl.psi.PerlFile;
import com.perl5.lang.perl.psi.PerlNamespaceDefinition;
import com.perl5.lang.perl.psi.PerlSubDefinitionBase;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hurricup on 29.01.2016.
 */
public class PerlOverrideMethodHandler extends GeneratePackageMemberHandlerBase
{
	@Override
	protected void generateAfterElement(PsiElement anchor, Editor editor, PsiFile file)
	{
		if (anchor != null && file instanceof PerlFile)
		{
			final List<PerlMethodMember> subDefinitions = new ArrayList<PerlMethodMember>();

			PerlPackageUtil.processNotOverridedMethods(
					PsiTreeUtil.getParentOfType(anchor, PerlNamespaceDefinition.class),
					new Processor<PerlSubDefinitionBase>()
					{
						@Override
						public boolean process(PerlSubDefinitionBase subDefinitionBase)
						{
							subDefinitions.add(new PerlMethodMember(subDefinitionBase));
							return true;
						}
					}
			);

			final MemberChooser<PerlMethodMember> chooser =
					new MemberChooser<PerlMethodMember>(subDefinitions.toArray(new PerlMethodMember[subDefinitions.size()]), false, true, anchor.getProject())
					{
						@Override
						protected SpeedSearchComparator getSpeedSearchComparator()
						{
							return new SpeedSearchComparator(false)
							{
								@Nullable
								@Override
								public Iterable<TextRange> matchingFragments(String pattern, String text)
								{
									return super.matchingFragments(PerlMethodMember.trimUnderscores(pattern), text);
								}
							};
						}

						@Override
						protected ShowContainersAction getShowContainersAction()
						{
							return new ShowContainersAction(IdeBundle.message("action.show.classes"), PerlIcons.PACKAGE_GUTTER_ICON);
						}
					};

			chooser.setTitle("Override/Implement Method");
			chooser.setCopyJavadocVisible(false);
			chooser.show();
			if (chooser.getExitCode() != DialogWrapper.OK_EXIT_CODE)
			{
				return;
			}

			PerlCodeGenerator codeGenerator = ((PerlFile) file).getCodeGenerator();
			StringBuilder generatedCode = new StringBuilder("");

			if (chooser.getSelectedElements() != null)
			{
				for (PerlMethodMember methodMember : chooser.getSelectedElements())
				{
					String code = codeGenerator.getOverrideCodeText(methodMember.getPsiElement());
					if (StringUtil.isNotEmpty(code))
					{
						generatedCode.append(code);
						generatedCode.append("\n\n");
					}
				}

				insertCodeAfterElement(anchor, generatedCode.toString());
			}
		}
	}
}
