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
import com.perl5.lang.perl.psi.PerlMethod;
import com.perl5.lang.perl.util.PerlFunctionUtil;
import com.perl5.lang.perl.util.PerlGlobUtil;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

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
				assert method instanceof PerlMethod;

				String packageName = ((PerlMethod) method).getPackageName();
				assert packageName != null;

				String packagePrefix = packageName + "::";

				boolean hasExplicitNamespace = ((PerlMethod) method).hasExplicitNamespace();
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
							resultSet.addElement(
									LookupElementBuilder
											.create(subName)
											.withIcon(PerlIcons.SUBROUTINE_GUTTER_ICON)
											.withPresentableText(subName)
											.withInsertHandler(SUB_SELECTION_HANDLER)
							);
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
										.withIcon(PerlIcons.PM_FILE)
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
