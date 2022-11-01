package com.perl5.lang.perl.debugger.values;

import com.perl5.lang.perl.debugger.PerlStackFrame;
import org.jetbrains.annotations.NotNull;

/**
 * Represents group or value, which may be downloaded by clicking on `...`
 */
public interface PerlLoadableXValueContainer {
  int getCurrentOffset();

  int getSize();

  void incCurrentOffset();

  @NotNull PerlStackFrame getStackFrame();

  /**
   * @return data key that should be sent to the debugger to obtain next chunk of the data
   */
  @NotNull String getDataKey();

  default int getRemaining() {
    return getSize() - getCurrentOffset();
  }

  default boolean isLast() {
    return getCurrentOffset() >= getSize();
  }
}
