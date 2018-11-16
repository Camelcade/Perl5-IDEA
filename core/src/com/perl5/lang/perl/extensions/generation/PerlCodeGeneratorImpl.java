/*
 * Copyright 2015-2017 Alexandr Evstigneev
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
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.ui.SpeedSearchComparator;
import com.perl5.PerlIcons;
import com.perl5.lang.perl.extensions.PerlCodeGenerator;
import com.perl5.lang.perl.idea.codeInsight.PerlMethodMember;
import com.perl5.lang.perl.lexer.PerlLexer;
import com.perl5.lang.perl.psi.PerlMethodDefinition;
import com.perl5.lang.perl.psi.PerlNamespaceDefinitionElement;
import com.perl5.lang.perl.psi.PerlSubDefinitionElement;
import com.perl5.lang.perl.psi.PerlSubElement;
import com.perl5.lang.perl.psi.utils.PerlElementFactory;
import com.perl5.lang.perl.psi.utils.PerlSubAnnotations;
import com.perl5.lang.perl.psi.utils.PerlSubArgument;
import com.perl5.lang.perl.util.PerlPackageUtil;
import gnu.trove.THashSet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by hurricup on 30.01.2016.
 */
public class PerlCodeGeneratorImpl implements PerlCodeGenerator {
  public static final PerlCodeGenerator INSTANCE = new PerlCodeGeneratorImpl();

  @Nullable
  @Override
  public String getOverrideCodeText(PsiElement subBase) {
    if (subBase instanceof PerlSubElement) {
      PerlSubElement perlSubBase = (PerlSubElement)subBase;
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
        if (StringUtil.isNotEmpty(annotations.getReturns())) {
          code.append("#@returns ");
          code.append(annotations.getReturns());
          code.append("\n");
        }
      }

      code.append("sub ");
      code.append(perlSubBase.getSubName());
      code.append("{\n");

      List<String> superArgs = new ArrayList<>();
      List<PerlSubArgument> arguments = Collections.emptyList();

      if (perlSubBase instanceof PerlSubDefinitionElement) {
        //noinspection unchecked
        arguments = ((PerlSubDefinitionElement)perlSubBase).getSubArgumentsList();

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
        superArgs.remove(0);
      }

      if (!arguments.isEmpty() && !arguments.get(0).isEmpty()) {
        //noinspection StringConcatenationInsideStringBufferAppend
        code.append(
          arguments.get(0).toStringShort() + "->SUPER::" + perlSubBase.getSubName() + "(" + StringUtil.join(superArgs, ", ") + ");\n");
      }
      code.append("}");
      return code.toString();
    }
    return null;
  }

  @Nullable
  @Override
  public String getMethodModifierCodeText(PsiElement subBase, String modifierType) {
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

      final MemberChooser<PerlMethodMember> chooser =
        new MemberChooser<PerlMethodMember>(subDefinitions.toArray(new PerlMethodMember[subDefinitions.size()]), false, true,
                                            anchor.getProject()) {
          @Override
          protected SpeedSearchComparator getSpeedSearchComparator() {
            return new SpeedSearchComparator(false) {
              @Nullable
              @Override
              public Iterable<TextRange> matchingFragments(@NotNull String pattern, @NotNull String text) {
                return super.matchingFragments(PerlMethodMember.trimUnderscores(pattern), text);
              }
            };
          }

          @Override
          protected ShowContainersAction getShowContainersAction() {
            return new ShowContainersAction(IdeBundle.message("action.show.classes"), PerlIcons.PACKAGE_GUTTER_ICON);
          }
        };

      chooser.setTitle("Override/Implement Method");
      chooser.setCopyJavadocVisible(false);
      chooser.show();
      if (chooser.getExitCode() != DialogWrapper.OK_EXIT_CODE) {
        return;
      }

      StringBuilder generatedCode = new StringBuilder("");

      if (chooser.getSelectedElements() != null) {
        for (PerlMethodMember methodMember : chooser.getSelectedElements()) {
          String code = getOverrideCodeText(methodMember.getPsiElement());
          if (StringUtil.isNotEmpty(code)) {
            generatedCode.append(code);
            generatedCode.append("\n\n");
          }
        }

        insertCodeAfterElement(anchor, generatedCode.toString(), editor);
      }
    }
  }

  @Override
  public void generateSetters(PsiElement anchor, Editor editor) {
    StringBuilder code = new StringBuilder();

    for (String name : askFieldsNames(anchor.getProject(), "Type comma-separated setters names:", "Generating Setters")) {
      code.append(getSetterCode(name));
    }

    if (code.length() > 0) {
      insertCodeAfterElement(anchor, code.toString(), editor);
    }
  }

  @Override
  public void generateGetters(PsiElement anchor, Editor editor) {
    StringBuilder code = new StringBuilder();

    for (String name : askFieldsNames(anchor.getProject(), "Type comma-separated getters names:", "Generating Getters")) {
      code.append(getGetterCode(name));
    }

    if (code.length() > 0) {
      insertCodeAfterElement(anchor, code.toString(), editor);
    }
  }

  @Override
  public void generateGettersAndSetters(PsiElement anchor, Editor editor) {
    StringBuilder code = new StringBuilder();

    for (String name : askFieldsNames(anchor.getProject(), "Type comma-separated accessors names:", "Generating Getters and Setters")) {
      code.append(getGetterCode(name));
      code.append(getSetterCode(name));
    }

    if (code.length() > 0) {
      insertCodeAfterElement(anchor, code.toString(), editor);
    }
  }

  @Override
  public void generateConstructor(PsiElement anchor, Editor editor) {
    insertCodeAfterElement(anchor, getConstructorCode(), editor);
  }

  protected List<String> askFieldsNames(
    Project project,
    String promptText,
    String promptTitle
  ) {
    Set<String> result = new THashSet<>();
    String name = Messages.showInputDialog(project, promptText, promptTitle, Messages.getQuestionIcon(), "", null);

    if (!StringUtil.isEmpty(name)) {

      for (String nameChunk : name.split("[ ,]+")) {
        if (!nameChunk.isEmpty() && PerlLexer.IDENTIFIER_PATTERN.matcher(nameChunk).matches()) {
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

                                                         if (code.length() > 0 && document != null) {
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

  public static String getConstructorCode() {
    return "\n" +
           "sub new\n" +
           "{\n" +
           "	my ($proto) = @_;\n" +
           "	my $self = bless {}, $proto;\n" +
           "	return $self;\n" +
           "}\n\n";
  }
}
