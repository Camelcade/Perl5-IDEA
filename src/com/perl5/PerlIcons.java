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

package com.perl5;

/**
 * Created by hurricup on 11.04.2015.
 */

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.LayeredIcon;

import javax.swing.*;

public class PerlIcons {
  public static final Icon PERL_LANGUAGE_ICON = IconLoader.getIcon("/icons/perl5language.png");
  public static final Icon PERL_MODIFIER = IconLoader.getIcon("/icons/perl_modifier.png");

  public static final Icon PERL_OPTION = IconLoader.getIcon("/icons/option.png");
  public static final Icon PERL_OPTIONS = IconLoader.getIcon("/icons/options.png");
  public static final Icon TEMPLATE_ROOT = IconLoader.getIcon("/icons/templateRoot.png");

  public static final Icon PM_FILE = IconLoader.getIcon("/icons/perl5.png");
  public static final Icon MOJO_FILE = IconLoader.getIcon("/icons/mojolicious.png");
  public static final Icon MOJO_MODIFIER = IconLoader.getIcon("/icons/mojo-modifier.png");
  public static final Icon MOJO_ROOT = new LayeredIcon(TEMPLATE_ROOT, MOJO_MODIFIER);
  public static final Icon POD_FILE = PERL_LANGUAGE_ICON;
  public static final Icon PERL_SCRIPT_FILE_ICON = IconLoader.getIcon("/icons/perl5classic.png");
  public static final Icon CAMEL_MODIFIER = IconLoader.getIcon("/icons/camel_modifier.png");
  public static final Icon TEST_FILE = IconLoader.getIcon("/icons/file-test.png");
  public static final Icon XS_FILE = IconLoader.getIcon("/icons/xsicon.png");
  public static final Icon EMBEDDED_PERL_FILE = IconLoader.getIcon("/icons/embeddedperl.png");
  public static final Icon EMBEDDED_MODIFIER = IconLoader.getIcon("/icons/embedded-modifier.png");
  public static final Icon EMBEDDED_ROOT = new LayeredIcon(TEMPLATE_ROOT, EMBEDDED_MODIFIER);

  public static final Icon TTK2_ICON = IconLoader.getIcon("/icons/template_toolkit_gutter_icon.png");
  public static final Icon TTK2_MODIFIER = IconLoader.getIcon("/icons/tt-modifier.png");
  public static final Icon TTK2_ROOT = new LayeredIcon(TEMPLATE_ROOT, TTK2_MODIFIER);
  public static final Icon TTK2_BLOCK_ICON = IconLoader.getIcon("/icons/template_toolkit_block_gutter_icon.png");

  public static final Icon LIB_ROOT = IconLoader.getIcon("/icons/library_root.png");

  public static final Icon PACKAGE_GUTTER_ICON = IconLoader.getIcon("/icons/package_gutter_icon.png");
  public static final Icon PRAGMA_GUTTER_ICON = IconLoader.getIcon("/icons/pragmaicon.png");

  public static final Icon ATTRIBUTE_GUTTER_ICON = IconLoader.getIcon("/icons/attributes_gutter_icon.png");
  public static final Icon ATTRIBUTE_GROUP_ICON = IconLoader.getIcon("/icons/attributes_group_icon.png");

  public static final Icon XSUB_GUTTER_ICON = IconLoader.getIcon("/icons/xsub_gutter_icon.png");
  public static final Icon SUB_DECLARATION_GUTTER_ICON = IconLoader.getIcon("/icons/sub_declaration_gutter_icon.png");
  public static final Icon METHOD_GUTTER_ICON = IconLoader.getIcon("/icons/method_gutter_icon.png");
  public static final Icon SUB_GUTTER_ICON = IconLoader.getIcon("/icons/subroutine_gutter_icon.png");
  public static final Icon CONSTANT_GUTTER_ICON = IconLoader.getIcon("/icons/constant_gutter_icon.png");

  public static final Icon ANNOTATION_GUTTER_ICON = IconLoader.getIcon("/icons/annotation_gutter_icon.png");

  public static final Icon REGEX_GUTTER_ICON = IconLoader.getIcon("/icons/re_gutter_icon.png");
  public static final Icon FORMAT_GUTTER_ICON = IconLoader.getIcon("/icons/format_gutter_icon.png");
  public static final Icon HANDLE_GUTTER_ICON = IconLoader.getIcon("/icons/handle_gutter_icon.png");
  public static final Icon ARGS_GUTTER_ICON = IconLoader.getIcon("/icons/args_gutter_icon.png");
  public static final Icon MAIN_GUTTER_ICON = IconLoader.getIcon("/icons/main_gutter_icon.png");
  public static final Icon MY_GUTTER_ICON = IconLoader.getIcon("/icons/my_gutter_icon.png");
  public static final Icon OUR_GUTTER_ICON = IconLoader.getIcon("/icons/our_gutter_icon.png");

  public static final Icon SCALAR_GUTTER_ICON = IconLoader.getIcon("/icons/scalar_gutter_icon.png");
  public static final Icon UTF_SCALAR_GUTTER_ICON = IconLoader.getIcon("/icons/scalar_utf_gutter_icon.png");
  public static final Icon ARRAY_GUTTER_ICON = IconLoader.getIcon("/icons/array_gutter_icon.png");
  public static final Icon HASH_GUTTER_ICON = IconLoader.getIcon("/icons/hash_gutter_icon.png");
  public static final Icon GLOB_GUTTER_ICON = IconLoader.getIcon("/icons/glob_gutter_icon.png");
}
