/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.lang.perl.extensions.generation;

import com.intellij.ide.IdeBundle;
import com.intellij.ide.util.MemberChooser;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.SpeedSearchComparator;
import com.perl5.PerlBundle;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.extensions.PerlCodeGenerator;
import com.perl5.lang.perl.idea.codeInsight.PerlMethodMember;
import com.perl5.lang.perl.idea.codeInsight.typeInference.value.PerlValue;
import com.perl5.lang.perl.parser.PerlParserUtil;
import com.perl5.lang.perl.psi.PerlMethodDefinition;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PerlSubElement;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.util.PerlPackageUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.util.*;
import java.util.function.UnaryOperator;


public class PerlCodeGeneratorImpl implements PerlCodeGenerator {
  private static UnaryOperator<List<PerlMethodMember>> ourTestChooser = null;

  @Override
  public @Nullable String getOverrideCodeText(PsiElement subBase) {
    if (subBase instanceof PerlSubElement perlSubBase) {
      StringBuilder code = new StringBuilder();
      code.append("#@override\n");

      PerlSubAnnotations annotations = perlSubBase.getAnnotations();
      if (annotations != null) {
        if (annotations.isDeprecated()) {
          code.append("#@deprecated\n");
        }
        if (annotations.isAbstract()) {
          code.append("#@abstract\n");
        }
        if (annotations.isMethod() || subBase instanceof PerlMethodDefinition) {
          code.append("#@method\n");
        }
        PerlValue returnValue = annotations.getReturnValue();
        if (!returnValue.isUnknown()) {
          code.append("#@returns ");
          code.append(returnValue.toCode());
          code.append("\n");
        }
      }

      code.append("sub ");
      code.append(perlSubBase.getSubName());
      code.append("{\n");

      List<String> superArgs = new ArrayList<>();
      List<PerlSubArgument> arguments = Collections.emptyList();

      if (perlSubBase instanceof PerlSubDefinitionElement subDefinitionElement) {
        //noinspection unchecked
        arguments = subDefinitionElement.getSubArgumentsList();

        if (!arguments.isEmpty()) {
          boolean useShift = false;

          for (PerlSubArgument argument : arguments) {
            if (StringUtil.isNotEmpty(argument.getVariableClass())) {
              useShift = true;
              break;
            }
          }

          if (useShift) {
            for (PerlSubArgument argument : arguments) {
              if (!argument.isEmpty()) {
                code.append("my ");
                code.append(argument.getVariableClass());
                code.append(" ");
                String superArg = argument.toStringShort();
                superArgs.add(superArg);
                code.append(superArg);
                code.append(" = ");
              }
              code.append("shift;\n");
            }
          }
          else {
            code.append("my ");
            code.append('(');
            boolean insertComma = false;
            for (PerlSubArgument argument : arguments) {
              if (insertComma) {
                code.append(", ");
              }
              else {
                insertComma = true;
              }

              String superArg = argument.toStringShort();
              superArgs.add(superArg);
              code.append(superArg);
            }
            code.append(") = @_;\n");
          }
        }
        else {
          code.append("my ($self) = @_;\n");
        }
      }

      if (!superArgs.isEmpty()) {
        superArgs.removeFirst();
      }

      if (!arguments.isEmpty() && !arguments.getFirst().isEmpty()) {
        //noinspection StringConcatenationInsideStringBufferAppend
        code.append(
          arguments.getFirst().toStringShort() + "->SUPER::" + perlSubBase.getSubName() + "(" + StringUtil.join(superArgs, ", ") + ");\n");
      }
      code.append("}");
      return code.toString();
    }
    return null;
  }

  @Override
  public void generateOverrideMethod(PsiElement anchor, Editor editor) {
    if (anchor != null) {
      final List<PerlMethodMember> subDefinitions = new ArrayList<>();

      PerlPackageUtil.processNotOverridedMethods(
        PsiTreeUtil.getParentOfType(anchor, PerlNamespaceDefinitionElement.class),
        subDefinitionBase ->
        {
          subDefinitions.add(new PerlMethodMember(subDefinitionBase));
          return true;
        }
      );

      List<PerlMethodMember> selectedElements = getMembersToOverride(anchor, subDefinitions);
      if (selectedElements == null) {
        return;
      }
      StringBuilder generatedCode = new StringBuilder();
      for (PerlMethodMember methodMember : selectedElements) {
        String code = getOverrideCodeText(methodMember.getPsiElement());
        if (StringUtil.isNotEmpty(code)) {
          generatedCode.append(code);
          generatedCode.append("\n\n");
        }
      }

      insertCodeAfterElement(anchor, generatedCode.toString(), editor);
    }
  }

  private @Nullable List<PerlMethodMember> getMembersToOverride(@NotNull PsiElement anchor,
                                                                @NotNull List<PerlMethodMember> subDefinitions) {
    if (ourTestChooser != null) {
      return ourTestChooser.apply(subDefinitions);
    }

    final MemberChooser<PerlMethodMember> chooser =
      new MemberChooser<>(subDefinitions.toArray(PerlMethodMember.EMPTY_ARRAY), false, true,
                          anchor.getProject()) {
        @Override
        protected SpeedSearchComparator getSpeedSearchComparator() {
          return new SpeedSearchComparator(false) {
            @Override
            public @Nullable Iterable<TextRange> matchingFragments(@NotNull String pattern, @NotNull String text) {
              return super.matchingFragments(PerlMethodMember.trimUnderscores(pattern), text);
            }
          };
        }

        @Override
        protected ShowContainersAction getShowContainersAction() {
          return new ShowContainersAction(() -> IdeBundle.message("action.show.classes"), PerlIcons.PACKAGE_GUTTER_ICON);
        }
      };

    chooser.setTitle(PerlBundle.message("dialog.title.override.implement.method"));
    chooser.setCopyJavadocVisible(false);
    chooser.show();
    if (chooser.getExitCode() != DialogWrapper.OK_EXIT_CODE) {
      return null;
    }

    return chooser.getSelectedElements();
  }

  @Override
  public void generateSetters(PsiElement anchor, Editor editor) {
    StringBuilder code = new StringBuilder();

    for (String name : askFieldsNames(anchor.getProject(), PerlBundle.message("dialog.message.type.comma.separated.setters.names"),
                                      PerlBundle.message("dialog.title.generating.setters"))) {
      code.append(getSetterCode(name));
    }

    if (!code.isEmpty()) {
      insertCodeAfterElement(anchor, code.toString(), editor);
    }
  }

  @Override
  public void generateGetters(PsiElement anchor, Editor editor) {
    StringBuilder code = new StringBuilder();

    for (String name : askFieldsNames(anchor.getProject(), PerlBundle.message("dialog.message.type.comma.separated.getters.names"),
                                      PerlBundle.message("dialog.title.generating.getters"))) {
      code.append(getGetterCode(name));
    }

    if (!code.isEmpty()) {
      insertCodeAfterElement(anchor, code.toString(), editor);
    }
  }

  @Override
  public void generateGettersAndSetters(PsiElement anchor, Editor editor) {
    StringBuilder code = new StringBuilder();

    for (String name : askFieldsNames(anchor.getProject(), PerlBundle.message("dialog.message.type.comma.separated.accessors.names"),
                                      PerlBundle.message("dialog.title.generating.getters.setters"))) {
      code.append(getGetterCode(name));
      code.append(getSetterCode(name));
    }

    if (!code.isEmpty()) {
      insertCodeAfterElement(anchor, code.toString(), editor);
    }
  }

  @Override
  public void generateConstructor(PsiElement anchor, Editor editor) {
    insertCodeAfterElement(anchor, getConstructorCode(), editor);
  }

  protected List<String> askFieldsNames(@NotNull Project project,
                                        @NlsContexts.DialogMessage String promptText,
                                        @NlsContexts.DialogTitle String promptTitle
  ) {
    Set<String> result = new HashSet<>();
    String name = Messages.showInputDialog(project, promptText, promptTitle, Messages.getQuestionIcon(), "", null);

    if (!StringUtil.isEmpty(name)) {

      for (String nameChunk : name.split("[ ,]+")) {
        if (!nameChunk.isEmpty() && PerlParserUtil.isIdentifier(nameChunk)) {
          result.add(nameChunk);
        }
      }
    }
    return new ArrayList<>(result);
  }

  protected void insertCodeAfterElement(PsiElement anchor, String code, Editor editor) {
    ApplicationManager.getApplication().runWriteAction(() ->
                                                       {
                                                         FileType fileType = anchor.getContainingFile().getFileType();
                                                         final PsiDocumentManager manager =
                                                           PsiDocumentManager.getInstance(anchor.getProject());
                                                         final Document document = manager.getDocument(anchor.getContainingFile());

                                                         if (!code.isEmpty() && document != null) {
                                                           manager.doPostponedOperationsAndUnblockDocument(document);

                                                           PsiFile newFile =
                                                             PerlElementFactory.createFile(anchor.getProject(), "\n" + code, fileType);
                                                           PsiElement container = anchor.getParent();
                                                           int newOffset = anchor.getTextOffset() + anchor.getTextLength();

                                                           if (newFile.getFirstChild() != null && newFile.getLastChild() != null) {
                                                             container
                                                               .addRangeAfter(newFile.getFirstChild(), newFile.getLastChild(), anchor);
                                                           }

                                                           manager.commitDocument(document);
                                                           editor.getCaretModel().moveToOffset(newOffset);
                                                           editor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
                                                         }
                                                       });
  }

  public static String getGetterCode(String name) {
    return "sub get_" + name + "\n" +
           "{\n" +
           "	return $_[0]->{" + name + "};\n" +
           "}\n";
  }

  public static String getSetterCode(String name) {
    return "sub set_" + name + "\n" +
           "{\n" +
           "	my ($self, $new_value) = @_;\n" +
           "	$$self{" + name + "} = $new_value;\n" +
           "	return $self;\n" +
           "}\n";
  }

  @SuppressWarnings("SameReturnValue")
  public static String getConstructorCode() {
    return """
      
      sub new
      {
        my ($proto) = @_;
        my $self = bless {}, $proto;
        return $self;
      }
      
      """;
  }

  @TestOnly
  public static void withChooser(@NotNull UnaryOperator<List<PerlMethodMember>> testChooser, @NotNull Runnable runnable) {
    ourTestChooser = testChooser;
    try {
      runnable.run();
    }
    finally {
      ourTestChooser = null;
    }
  }
}
