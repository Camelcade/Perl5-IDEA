package com.perl5.lang.perl.idea.completion.providers;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorModificationUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.lexer.PerlAnnotations;
import com.perl5.lang.perl.psi.PsiPerlMethod;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.psi.PsiPerlSubDefinition;
import com.perl5.lang.perl.util.PerlFunctionUtil;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hurricup on 01.06.2015.
 *
 */
public class PerlFunctionCompletionProvider extends CompletionProvider<CompletionParameters>
{
	public static final NamespaceSelectionHanlder NAMESPACE_SELECTION_HANLDER = new NamespaceSelectionHanlder();
	public static final SubSelectionHandler SUB_SELECTION_HANDLER = new SubSelectionHandler();

	public void addCompletions(@NotNull final CompletionParameters parameters,
							   ProcessingContext context,
							   @NotNull final CompletionResultSet resultSet)
	{

		ApplicationManager.getApplication().runReadAction(new Runnable()
		{


			@Override
			public void run()
			{
				PsiElement method = parameters.getPosition().getParent();
				assert method instanceof PsiPerlMethod;

				String packageName = ((PsiPerlMethod) method).getPackageName();
				assert packageName != null;

				String packagePrefix = packageName + "::";

				boolean hasExplicitNamespace = ((PsiPerlMethod) method).hasExplicitNamespace();
				Project project = parameters.getPosition().getProject();

				// todo we should show declared and not defined subs too for XS extensions
				Collection<String> definedSubs = PerlFunctionUtil.getDefinedSubsNames(project);
				definedSubs.addAll(PerlGlobUtil.getDefinedGlobsNames(project));

				// todo take SUPER into account, search in superclasses
				for (String canonicalSubName : definedSubs)
				{
					if (canonicalSubName.startsWith(packagePrefix))
					{
						String subName = canonicalSubName.substring(packagePrefix.length());

						if (!subName.contains("::"))
						{
							Collection<PsiPerlSubDefinition> subDefinitions = PerlFunctionUtil.findSubDefinitions(project,canonicalSubName);

							for(PsiPerlSubDefinition subDefinition: subDefinitions )
							{
								// todo set method icon if isMethod is true
								// todo omit first argument is isMethod is true
								// todo think about decorations for other annotations
								Collection<PerlSubArgument> subArguments = subDefinition.getArgumentsList();
								PerlSubAnnotations subAnnotations = subDefinition.getAnnotations();

								int argumentsNumber = subArguments.size();

								List<String> argumentsList = new ArrayList<String>();
								for( PerlSubArgument argument: subArguments)
								{
									// todo we can mark optional subArguments after prototypes implementation
									argumentsList.add(argument.toStringShort());

									int compiledListSize = argumentsList.size();
									if( compiledListSize > 4 && argumentsNumber > compiledListSize )
									{
										argumentsList.add("...");
										break;
									}
								}

								String argsString = "(" + StringUtils.join(argumentsList, ", ") + ")";

								resultSet.addElement(LookupElementBuilder
										.create(subName)
										.withIcon(PerlIcons.SUBROUTINE_GUTTER_ICON)
										.withPresentableText(subName + argsString)
										.withInsertHandler(SUB_SELECTION_HANDLER)
										.withStrikeoutness(subAnnotations.isDeprecated())
								);
							}
						}
					}
				}

				// subs for incomplete ns and inital enter
				Collection<String> knownNamespaces = PerlPackageUtil.listDefinedPackageNames(project);

				// todo take SUPER into account
				if (!hasExplicitNamespace)
					packagePrefix = resultSet.getPrefixMatcher().getPrefix();

				for (String namespaceName : knownNamespaces)
				{
					if (namespaceName.startsWith(packagePrefix))
					{
						String replacement = hasExplicitNamespace
								? namespaceName.substring(packagePrefix.length())
								: namespaceName;

						resultSet.addElement(
								LookupElementBuilder
										.create(replacement)
										.withIcon(PerlIcons.PACKAGE_GUTTER_ICON)
										.withPresentableText(namespaceName + "...")
										.withInsertHandler(NAMESPACE_SELECTION_HANLDER));
					}
				}
			}
		});
	}

	static class NamespaceSelectionHanlder implements InsertHandler<LookupElement>
	{
		@Override
		public void handleInsert(final InsertionContext context, LookupElement item)
		{
			final Editor editor = context.getEditor();

			EditorModificationUtil.insertStringAtCaret(editor, "::");

			context.setLaterRunnable(new Runnable()
			{
				@Override
				public void run()
				{
					new CodeCompletionHandlerBase(CompletionType.BASIC).invokeCompletion(context.getProject(), editor);
				}
			});
		}
	}

	static class SubSelectionHandler implements InsertHandler<LookupElement>
	{
		@Override
		public void handleInsert(final InsertionContext context, LookupElement item)
		{
			final Editor editor = context.getEditor();

			// todo why autocompletion is not auto-opening after -> or :: ?
			// todo if prefix is :: and target function is a method (got self/this/proto) replace :: with ->
			// todo here we could check a method prototype and position caret accordingly
			// todo we need hint with prototype here, but prototypes handling NYI
			EditorModificationUtil.insertStringAtCaret(editor, "()");
			editor.getCaretModel().moveCaretRelatively(-1, 0, false, false, true);
		}
	}

}
