package com.perl5.lang.perl.idea.sdk.host.docker;

import com.perl5.lang.perl.idea.sdk.host.PerlHostData;
import org.jetbrains.annotations.NotNull;

public final class PerlDockerTestUtil {
  private PerlDockerTestUtil() {
  }

  public static @NotNull PerlHostData<?, ?> createHostData(@NotNull String imageName) {
    return PerlDockerHandler.getInstance().createData().withImageName(imageName);
  }
}
