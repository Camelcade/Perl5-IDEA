/*
 * Copyright 2015-2019 Alexandr Evstigneev
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

package com.perl5.lang.perl.extensions.packageprocessor.impl;

import java.util.Collections;
import java.util.List;


public class MooProcessor extends MooseProcessor {
  public static final String MOO_OBJECT = "Moo::Object";
  protected static final List<String> LOADED_CLASSES = Collections.singletonList(MOO_OBJECT);
  protected static final List<String> PARENT_CLASSES = LOADED_CLASSES;

  @Override
  public List<String> getLoadedClasses() {
    return LOADED_CLASSES;
  }

  @Override
  public List<String> getParentClasses() {
    return PARENT_CLASSES;
  }
}
