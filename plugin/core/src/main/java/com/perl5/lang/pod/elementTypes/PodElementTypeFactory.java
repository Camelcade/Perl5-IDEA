/*
 * Copyright 2015-2024 Alexandr Evstigneev
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
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.StubElement;
import com.intellij.psi.tree.IElementType;
import com.perl5.lang.pod.parser.psi.impl.PodIdentifierImpl;
import com.perl5.lang.pod.parser.psi.mixin.*;
import com.perl5.lang.pod.parser.psi.stubs.PodSectionStub;
import com.perl5.lang.pod.psi.impl.*;
import org.jetbrains.annotations.NotNull;


public class PodElementTypeFactory {
  private static final Logger LOG = Logger.getInstance(PodElementTypeFactory.class);

  public static IElementType getTokenType(String debugName) {
    if (debugName.equals("identifier")) {
      return new PodTokenTypeEx(debugName) {
        @Override
        public @NotNull ASTNode createLeafNode(@NotNull CharSequence leafText) {
          return new PodIdentifierImpl(this, leafText);
        }
      };
    }
    return new PodTokenType(debugName);
  }

  public static IElementType getElementType(String name) {
    return switch (name) {
      case "POD_FORMAT_INDEX" -> new PodFormatterXElementType(name);
      case "CUT_SECTION" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiCutSectionImpl(node);
        }
      };
      case "ENCODING_SECTION" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiEncodingSectionImpl(node);
        }
      };
      case "FORMATTING_SECTION_CONTENT" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiFormattingSectionContentImpl(node);
        }
      };
      case "FOR_SECTION" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiForSectionImpl(node);
        }
      };
      case "FOR_SECTION_CONTENT" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiForSectionContentImpl(node);
        }
      };
      case "HEAD_1_SECTION" -> new PodStubBasedTitledSectionElementType<PodSectionH1>(name) {
        @Override
        public PodSectionH1 createPsi(@NotNull PodSectionStub stub) {
          return new PsiHead1SectionImpl(stub, this);
        }

        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiHead1SectionImpl(node);
        }
      };
      case "HEAD_1_SECTION_CONTENT" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiHead1SectionContentImpl(node);
        }
      };
      case "HEAD_2_SECTION" -> new PodStubBasedTitledSectionElementType<PodSectionH2>(name) {
        @Override
        public PodSectionH2 createPsi(@NotNull PodSectionStub stub) {
          return new PsiHead2SectionImpl(stub, this);
        }

        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiHead2SectionImpl(node);
        }
      };
      case "HEAD_2_SECTION_CONTENT" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiHead2SectionContentImpl(node);
        }
      };
      case "HEAD_3_SECTION" -> new PodStubBasedTitledSectionElementType<PodSectionH3>(name) {
        @Override
        public PodSectionH3 createPsi(@NotNull PodSectionStub stub) {
          return new PsiHead3SectionImpl(stub, this);
        }

        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiHead3SectionImpl(node);
        }
      };
      case "HEAD_3_SECTION_CONTENT" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiHead3SectionContentImpl(node);
        }
      };
      case "HEAD_4_SECTION" -> new PodStubBasedTitledSectionElementType<PodSectionH4>(name) {
        @Override
        public PodSectionH4 createPsi(@NotNull PodSectionStub stub) {
          return new PsiHead4SectionImpl(stub, this);
        }

        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiHead4SectionImpl(node);
        }
      };
      case "HEAD_4_SECTION_CONTENT" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiHead4SectionContentImpl(node);
        }
      };
      case "ITEM_SECTION" -> new PodSectionItemElementType(name);
      case "ITEM_SECTION_CONTENT" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiItemSectionContentImpl(node);
        }
      };
      case "ITEM_SECTION_TITLE" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiItemSectionTitleImpl(node);
        }
      };
      case "OVER_SECTION" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiOverSectionImpl(node);
        }
      };
      case "OVER_SECTION_CONTENT" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiOverSectionContentImpl(node);
        }
      };
      case "POD_VERBATIM_PARAGRAPH" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiPodVerbatimParagraphImpl(node);
        }
      };
      case "POD_FORMAT_BOLD" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiPodFormatBoldImpl(node);
        }
      };
      case "POD_FORMAT_CODE" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiPodFormatCodeImpl(node);
        }
      };
      case "POD_FORMAT_ESCAPE" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiPodFormatEscapeImpl(node);
        }
      };
      case "POD_FORMAT_FILE" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiPodFormatFileImpl(node);
        }
      };
      case "POD_FORMAT_ITALIC" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiPodFormatItalicImpl(node);
        }
      };
      case "POD_FORMAT_LINK" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiPodFormatLinkImpl(node);
        }
      };
      case "POD_FORMAT_NBSP" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiPodFormatNbspImpl(node);
        }
      };
      case "POD_FORMAT_NULL" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiPodFormatNullImpl(node);
        }
      };
      case "POD_PARAGRAPH" -> new PodStubBasedSectionElementType<PodSectionParagraph>(name) {
        @Override
        public PodSectionParagraph createPsi(@NotNull PodSectionStub stub) {
          return new PsiPodParagraphImpl(stub, this);
        }

        @Override
        public @NotNull PodSectionStub createStub(@NotNull PodSectionParagraph psi, StubElement parentStub) {
          return new PodSectionStub(parentStub, this, psi.getPresentableText());
        }

        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiPodParagraphImpl(node);
        }
      };
      case "POD_SECTION" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiPodSectionImpl(node);
        }
      };
      case "POD_SECTION_FORMAT" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiPodSectionFormatImpl(node);
        }
      };
      case "SECTION_TITLE" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiSectionTitleImpl(node);
        }
      };
      case "UNKNOWN_SECTION" -> new PodStubBasedTitledSectionElementType<PsiUnknownSectionImpl>(name) {
        @Override
        public PsiUnknownSectionImpl createPsi(@NotNull PodSectionStub stub) {
          return new PsiUnknownSectionImpl(stub, this);
        }

        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiUnknownSectionImpl(node);
        }
      };
      case "LINK_TEXT" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiLinkTextImpl(node);
        }
      };
      case "LINK_NAME" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiLinkNameImpl(node);
        }
      };
      case "LINK_SECTION" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiLinkSectionImpl(node);
        }
      };
      case "LINK_URL" -> new PodElementType(name) {
        @Override
        public @NotNull PsiElement getPsiElement(@NotNull ASTNode node) {
          return new PsiLinkUrlImpl(node);
        }
      };
      default -> {
        LOG.error("Missing element: " + name);
        throw new RuntimeException();
      }
    };
  }
}
