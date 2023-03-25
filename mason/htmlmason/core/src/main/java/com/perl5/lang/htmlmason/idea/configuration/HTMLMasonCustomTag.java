/*
 * Copyright 2015-2023 Alexandr Evstigneev
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

package com.perl5.lang.htmlmason.idea.configuration;


import java.util.Objects;

public class HTMLMasonCustomTag {
  private String myText;
  private HTMLMasonCustomTagRole myRole;

  @SuppressWarnings("unused")
  private HTMLMasonCustomTag() {
  }

  public HTMLMasonCustomTag(String myText, HTMLMasonCustomTagRole myRole) {
    this.myText = myText;
    this.myRole = myRole;
  }

  protected HTMLMasonCustomTag copy() {
    return new HTMLMasonCustomTag(myText, myRole);
  }

  public String getText() {
    return myText;
  }

  public void setText(String myText) {
    this.myText = myText;
  }

  public HTMLMasonCustomTagRole getRole() {
    return myRole;
  }

  public void setRole(HTMLMasonCustomTagRole myRole) {
    this.myRole = myRole;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof HTMLMasonCustomTag tag)) {
      return false;
    }

    if (!Objects.equals(myText, tag.myText)) {
      return false;
    }
    return myRole == tag.myRole;
  }

  @Override
  public int hashCode() {
    int result = myText != null ? myText.hashCode() : 0;
    result = 31 * result + (myRole != null ? myRole.hashCode() : 0);
    return result;
  }

  public String getOpenTagText() {
    return "<%" + getText() + (getRole().isSimple() ? ">" : "");
  }

  public String getCloseTagText() {
    return "</%" + getText() + ">";
  }
}
