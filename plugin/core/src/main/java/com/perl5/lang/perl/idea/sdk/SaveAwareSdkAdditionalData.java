package com.perl5.lang.perl.idea.sdk;

import com.intellij.openapi.projectRoots.SdkAdditionalData;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

public interface SaveAwareSdkAdditionalData extends SdkAdditionalData {
  void save(@NotNull Element additional);
}
