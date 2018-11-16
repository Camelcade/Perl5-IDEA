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

package com.perl5.lang.perl.idea.modules;

import com.intellij.util.containers.ContainerUtil;
import com.perl5.lang.perl.idea.configuration.settings.sdk.Perl5SettingsConfigurableExtension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.model.serialization.JpsModelSerializerExtension;
import org.jetbrains.jps.model.serialization.module.JpsModuleSourceRootDummyPropertiesSerializer;
import org.jetbrains.jps.model.serialization.module.JpsModuleSourceRootPropertiesSerializer;

import java.util.List;

/**
 * Created by hurricup on 29.08.2015.
 */
public class PerlSourceTypesSerializationExtension extends JpsModelSerializerExtension {

  @NotNull
  @Override
  public List<? extends JpsModuleSourceRootPropertiesSerializer<?>> getModuleSourceRootPropertiesSerializers() {
    List<JpsModuleSourceRootDummyPropertiesSerializer> result = ContainerUtil.newArrayList();
    Perl5SettingsConfigurableExtension.forEach(extension -> extension.getSourceRootTypes().forEach(
      type -> result.add(new JpsModuleSourceRootDummyPropertiesSerializer(type, type.getSerializationKey()))
    ));
    return result;
  }
}
