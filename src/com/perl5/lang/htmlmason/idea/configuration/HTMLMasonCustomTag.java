/*
 * Copyright 2016 Alexandr Evstigneev
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

/**
 * Created by hurricup on 10.03.2016.
 */
public class HTMLMasonCustomTag {
  private String myText;
  private HTMLMasonCustomTagRole myRole;

  public HTMLMasonCustomTag() {
  }

  public HTMLMasonCustomTag(String myText, HTMLMasonCustomTagRole myRole) {
    this.myText = myText;
    this.myRole = myRole;
  }

  @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
  @Override
  protected HTMLMasonCustomTag clone() {
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
    if (!(o instanceof HTMLMasonCustomTag)) {
      return false;
    }

    HTMLMasonCustomTag that = (HTMLMasonCustomTag)o;

    return myText.equals(that.myText);
  }

  @Override
  public int hashCode() {
    return myText.hashCode();
  }

  public String getOpenTagText() {
    return "<%" + getText() + (getRole().isSimple() ? ">" : "");
  }

  public String getCloseTagText() {
    return "</%" + getText() + ">";
  }
}
