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

package com.perl5.lang.perl.idea;

import com.intellij.ide.util.DeleteNameDescriptionLocation;
import com.intellij.ide.util.DeleteTypeDescriptionLocation;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.*;
import com.intellij.usageView.UsageViewLongNameLocation;
import com.intellij.usageView.UsageViewNodeTextLocation;
import com.intellij.usageView.UsageViewShortNameLocation;
import com.intellij.usageView.UsageViewTypeLocation;
import com.perl5.PerlBundle;
import com.perl5.lang.perl.PerlLanguage;
import com.perl5.lang.perl.parser.Class.Accessor.psi.impl.PerlClassAccessorMethod;
import com.perl5.lang.perl.parser.Exception.Class.psi.light.PerlLightExceptionClassDefinition;
import com.perl5.lang.perl.parser.constant.psi.light.PerlLightConstantDefinitionElement;
import com.perl5.lang.perl.parser.moose.psi.impl.PerlAttributeDefinition;
import com.perl5.lang.perl.psi.*;
import com.perl5.lang.perl.psi.impl.PerlFileImpl;
import com.perl5.lang.perl.psi.mixins.PerlFuncDefinitionMixin;
import com.perl5.lang.perl.psi.utils.PerlVariableType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;

public class PerlElementDescriptionProvider implements ElementDescriptionProvider {
  @Nullable
  @Override
  public String getElementDescription(@NotNull PsiElement element, @NotNull ElementDescriptionLocation location) {
    if (!element.getLanguage().isKindOf(PerlLanguage.INSTANCE)) {
      return null;
    }

    if (location == DeleteNameDescriptionLocation.INSTANCE) {
      return ElementDescriptionUtil.getElementDescription(element, UsageViewShortNameLocation.INSTANCE);
    }
    else if (location == DeleteTypeDescriptionLocation.SINGULAR) {
      return ElementDescriptionUtil.getElementDescription(element, UsageViewTypeLocation.INSTANCE);
    }
    else if (location == DeleteTypeDescriptionLocation.PLURAL) {
      return StringUtil.pluralize(ElementDescriptionUtil.getElementDescription(element, DeleteTypeDescriptionLocation.SINGULAR));
    }
    else if (location == UsageViewShortNameLocation.INSTANCE) {
      if (element instanceof PsiNamedElement) {
        return ((PsiNamedElement)element).getName();
      }
    }
    else if (location == UsageViewLongNameLocation.INSTANCE) {
      return MessageFormat.format(
        "{0} ''{1}''",
        ElementDescriptionUtil.getElementDescription(element, UsageViewTypeLocation.INSTANCE),
        ElementDescriptionUtil.getElementDescription(element, UsageViewShortNameLocation.INSTANCE)
      );
    }
    else if (location == UsageViewNodeTextLocation.INSTANCE) {
      return ElementDescriptionUtil.getElementDescription(element, UsageViewShortNameLocation.INSTANCE);
    }
    else if (location == UsageViewTypeLocation.INSTANCE) {
      if (element instanceof PerlAttributeDefinition) {
        return PerlBundle.message("perl.type.attribute");
      }
      else if (element instanceof PerlClassAccessorMethod) {
        return PerlBundle.message("perl.type.class.accessor");
      }
      else if (element instanceof PerlLightExceptionClassDefinition) {
        return PerlBundle.message("perl.type.exception");
      }
      else if (element instanceof PerlLightConstantDefinitionElement) {
        return PerlBundle.message("perl.type.constant");
      }
      else if (element instanceof PerlMethodDefinition || element instanceof PerlSubDefinition && ((PerlSubDefinition)element).isMethod()) {
        return PerlBundle.message("perl.type.method");
      }
      else if (element instanceof PerlSubDeclarationElement) {
        return PerlBundle.message("perl.type.sub.declaration");
      }
      else if (element instanceof PerlHeredocOpener) {
        return PerlBundle.message("perl.type.heredoc.marker");
      }
      else if (element instanceof PerlFuncDefinitionMixin) {
        return PerlBundle.message("perl.type.function");
      }
      else if (element instanceof PerlSubDefinitionElement) {
        return PerlBundle.message("perl.type.sub.definition");
      }
      else if (element instanceof PerlNamespaceDefinitionWithIdentifier) {
        return PerlBundle.message("perl.type.namespace");
      }
      else if (element instanceof PerlFileImpl) {
        return PerlBundle.message("perl.type.file");
      }
      else if (element instanceof PsiDirectoryContainer) {
        return PerlBundle.message("perl.type.directory");
      }
      else if (element instanceof PerlGlobVariable) {
        return PerlBundle.message("perl.type.typeglob");
      }
      else if (element instanceof PerlVariableDeclarationElement) {
        PerlVariableType actualType = ((PerlVariableDeclarationElement)element).getVariable().getActualType();
        if (actualType == PerlVariableType.ARRAY) {
          return PerlBundle.message("perl.type.array");
        }
        else if (actualType == PerlVariableType.HASH) {
          return PerlBundle.message("perl.type.hash");
        }
        else if (actualType == PerlVariableType.SCALAR) {
          return PerlBundle.message("perl.type.scalar");
        }
      }
      else if (element instanceof PerlVariableNameElement) {
        return getElementDescription(element.getParent(), location);
      }
    }
    return null;
  }
}
