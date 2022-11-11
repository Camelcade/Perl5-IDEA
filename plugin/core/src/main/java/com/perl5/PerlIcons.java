/*
 * Copyright 2015-2022 Alexandr Evstigneev
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

package com.perl5;


import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.ui.LayeredIcon;
import com.intellij.util.IconUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public final class PerlIcons {
  public static final Icon PERL_LANGUAGE_ICON = load("/icons/perl5language.png");
  public static final Icon PERLBREW_ICON = load("/icons/perlbrew.png");
  public static final Icon STRAWBERRY_ICON = load("/icons/strawberry.png");
  public static final Icon WINDOWS10_ICON = load("/icons/windows.svg");
  public static final Icon LINUX_ICON = load("/icons/linux.svg");
  public static final Icon PLENV_ICON = PERL_LANGUAGE_ICON;
  public static final Icon MAC_ICON = load("/icons/mac.svg");
  public static final Icon WINDOWS_ICON = WINDOWS10_ICON;
  public static final Icon DOCKER_ICON = load("/icons/docker.svg");
  public static final Icon FREEBSD_ICON = LINUX_ICON;
  public static final Icon SOLARIS_ICON = LINUX_ICON;
  public static final Icon UNIX_ICON = LINUX_ICON;
  public static final Icon PERL_OPTION = load("/icons/option.png");
  public static final Icon PERL_OPTIONS = load("/icons/options.png");
  public static final Icon TEMPLATE_ROOT = load("/icons/templateRoot.png");
  public static final Icon PM_FILE = load("/icons/perl5.png");
  public static final Icon PERL_SCRIPT_FILE_ICON = load("/icons/perl5classic.png");
  public static final Icon CAMEL_MODIFIER = load("/icons/camel_modifier.png");
  public static final Icon POD_FILE = PERL_LANGUAGE_ICON;
  public static final Icon TEST_FILE = load("/icons/file-test.png");
  public static final Icon XS_FILE = load("/icons/xsicon.png");
  public static final Icon LIB_ROOT = load("/icons/library_root.png");
  public static final Icon METACPAN = load("/icons/metacpan.png");
  public static final Icon PACKAGE_GUTTER_ICON = load("/icons/package_gutter_icon.png");
  public static final Icon PRAGMA_GUTTER_ICON = load("/icons/pragmaicon.png");
  public static final Icon ATTRIBUTE_GUTTER_ICON = load("/icons/attributes_gutter_icon.png");
  public static final Icon ATTRIBUTE_GROUP_ICON = load("/icons/attributes_group_icon.png");
  public static final Icon XSUB_GUTTER_ICON = load("/icons/xsub_gutter_icon.png");
  public static final Icon SUB_DECLARATION_GUTTER_ICON = load("/icons/sub_declaration_gutter_icon.png");
  public static final Icon METHOD_GUTTER_ICON = load("/icons/method_gutter_icon.png");
  public static final Icon SUB_GUTTER_ICON = load("/icons/subroutine_gutter_icon.png");
  public static final Icon CONSTANT_GUTTER_ICON = load("/icons/constant_gutter_icon.png");
  public static final Icon BEFORE_MODIFIER_GUTTER_ICON = METHOD_GUTTER_ICON;
  public static final Icon AFTER_MODIFIER_GUTTER_ICON = METHOD_GUTTER_ICON;
  public static final Icon AROUND_MODIFIER_GUTTER_ICON = METHOD_GUTTER_ICON;
  public static final Icon AUGMENT_MODIFIER_GUTTER_ICON = METHOD_GUTTER_ICON;
  public static final Icon ANNOTATION_GUTTER_ICON = load("/icons/annotation_gutter_icon.png");
  public static final Icon REGEX_GUTTER_ICON = load("/icons/re_gutter_icon.png");
  public static final Icon ANON_SUB_ICON = SUB_GUTTER_ICON;
  public static final Icon FORMAT_GUTTER_ICON = load("/icons/format_gutter_icon.png");
  public static final Icon HANDLE_GUTTER_ICON = load("/icons/handle_gutter_icon.png");
  public static final Icon ARGS_GUTTER_ICON = load("/icons/args_gutter_icon.png");
  public static final Icon MAIN_GUTTER_ICON = load("/icons/main_gutter_icon.png");
  public static final Icon MY_GUTTER_ICON = load("/icons/my_gutter_icon.png");
  public static final Icon OUR_GUTTER_ICON = load("/icons/our_gutter_icon.png");
  public static final Icon SCALAR_GUTTER_ICON = load("/icons/scalar_gutter_icon.png");
  public static final Icon UTF_SCALAR_GUTTER_ICON = load("/icons/scalar_utf_gutter_icon.png");
  public static final Icon ARRAY_GUTTER_ICON = load("/icons/array_gutter_icon.png");
  public static final Icon HASH_GUTTER_ICON = load("/icons/hash_gutter_icon.png");
  public static final Icon GLOB_GUTTER_ICON = load("/icons/glob_gutter_icon.png");

  private PerlIcons() {
  }

  private static @NotNull Icon load(@NotNull String resourcePath) {
    return IconLoader.getIcon(resourcePath, PerlIcons.class);
  }

  public static final Icon PERL_TEST_CONFIGURATION = TEST_FILE;

  /**
   * @see #createIconWithModifier(Icon, Icon)
   */
  public static @NotNull NotNullLazyValue<Icon> createLazyIconWithModifier(@NotNull Icon baseIcon, @NotNull Icon modifierIcon) {
    return NotNullLazyValue.createValue(() -> createIconWithModifier(baseIcon, modifierIcon));
  }

  public static @NotNull NotNullLazyValue<Icon> createLazyIconWithModifier(@NotNull Icon baseIcon,
                                                                           @NotNull Icon modifierIcon,
                                                                           float modifierScale) {
    return NotNullLazyValue.createValue(() -> createIconWithModifier(baseIcon, modifierIcon, modifierScale));
  }

  public static @NotNull Icon createIconWithModifier(@NotNull Icon baseIcon, @NotNull Icon modifierIcon) {
    return createIconWithModifier(baseIcon, modifierIcon, 0.5f);
  }

  /**
   * Creates an icon by combining {@code baseIcon} and scaled {@code modifierIcon}
   *
   * @apiNote modifier icon is scaled in {@code modifierScale} and put into right bottom corner of the base icon
   */
  private static @NotNull Icon createIconWithModifier(@NotNull Icon baseIcon,
                                                      @NotNull Icon modifierIcon,
                                                      float modifierScale) {
    LayeredIcon result = new LayeredIcon(2);
    result.setIcon(baseIcon, 0);
    Icon modifier = IconUtil.scale(modifierIcon, null, modifierScale);
    result.setIcon(modifier, 1, baseIcon.getIconHeight() - modifier.getIconHeight(), baseIcon.getIconWidth() - modifier.getIconWidth());
    return result;
  }
}
