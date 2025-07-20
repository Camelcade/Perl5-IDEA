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

package com.perl5.lang.pod.elementTypes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.perl5.lang.pod.parser.psi.mixin.*;
import com.perl5.lang.pod.psi.impl.*;
import org.jetbrains.annotations.NotNull;

public interface PodStubElementTypes {
  String HEAD_1_SECTION_DEBUG_NAME = "HEAD_1_SECTION";
  String HEAD_2_SECTION_DEBUG_NAME = "HEAD_2_SECTION";
  String HEAD_3_SECTION_DEBUG_NAME = "HEAD_3_SECTION";
  String HEAD_4_SECTION_DEBUG_NAME = "HEAD_4_SECTION";
  String POD_PARAGRAPH_DEBUG_NAME = "POD_PARAGRAPH";
  String UNKNOWN_SECTION_DEBUG_NAME = "UNKNOWN_SECTION";

  PodStubBasedTitledSectionElementType<PodSectionH1> HEAD_1_SECTION =
    new PodStubBasedTitledSectionElementType<>(HEAD_1_SECTION_DEBUG_NAME) {
      @Override
      public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
        return new PsiHead1SectionImpl(node);
      }
    };
  PodStubBasedTitledSectionElementType<PodSectionH2>
    HEAD_2_SECTION = new PodStubBasedTitledSectionElementType<>(HEAD_2_SECTION_DEBUG_NAME) {
    @Override
    public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PsiHead2SectionImpl(node);
    }
  };
  PodStubBasedTitledSectionElementType<PodSectionH3>
    HEAD_3_SECTION = new PodStubBasedTitledSectionElementType<>(HEAD_3_SECTION_DEBUG_NAME) {
    @Override
    public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PsiHead3SectionImpl(node);
    }
  };
  PodStubBasedTitledSectionElementType<PodSectionH4>
    HEAD_4_SECTION = new PodStubBasedTitledSectionElementType<>(HEAD_4_SECTION_DEBUG_NAME) {
    @Override
    public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PsiHead4SectionImpl(node);
    }
  };
  PodStubBasedSectionElementType<PodSectionParagraph> POD_PARAGRAPH = new PodStubBasedSectionElementType<>(POD_PARAGRAPH_DEBUG_NAME) {
    @Override
    public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PsiPodParagraphImpl(node);
    }
  };
  PodStubBasedTitledSectionElementType<PsiUnknownSectionImpl> UNKNOWN_SECTION =
    new PodStubBasedTitledSectionElementType<>(UNKNOWN_SECTION_DEBUG_NAME) {
    @Override
    public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
      return new PsiUnknownSectionImpl(node);
    }
  };
  String ITEM_SECTION_DEBUG_NAME = "ITEM_SECTION";
  PodSectionItemElementType ITEM_SECTION = new PodSectionItemElementType(ITEM_SECTION_DEBUG_NAME);
  String POD_FORMAT_INDEX_DEBUG_NAME = "POD_FORMAT_INDEX";
  PodFormatterXElementType POD_FORMAT_INDEX = new PodFormatterXElementType(POD_FORMAT_INDEX_DEBUG_NAME);
}
