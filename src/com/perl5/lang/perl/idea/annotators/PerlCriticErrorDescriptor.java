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

package com.perl5.lang.perl.idea.annotators;

import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hurricup on 16.04.2016.
 */
public class PerlCriticErrorDescriptor {
  public static final Pattern PERL_CRITIC_MESSAGE_PATTERN = Pattern.compile("(.+?) at line (\\d+), column (\\d+)\\.\\s*( .+)");
  private final int myLine;
  private final int myCol;
  private StringBuilder myMessage;

  private PerlCriticErrorDescriptor(StringBuilder message, int line, int col) {
    myMessage = message;
    myCol = col;
    myLine = line;
  }

  public String getMessage() {
    return myMessage.toString();
  }

  public void append(String text) {
    if (text != null) {
      myMessage.append(text);
    }
  }

  public int getLine() {
    return myLine;
  }

  public int getCol() {
    return myCol;
  }

  @Nullable
  public static PerlCriticErrorDescriptor getFromString(String message) {
    Matcher m = PERL_CRITIC_MESSAGE_PATTERN.matcher(message);
    if (m.matches()) {
      StringBuilder realMessage = new StringBuilder();
      realMessage.append(m.group(1));
      realMessage.append(m.group(4));
      int line = Integer.parseInt(m.group(2));
      int pos = Integer.parseInt(m.group(3));
      return new PerlCriticErrorDescriptor(realMessage, line, pos);
    }
    return null;
  }
}
