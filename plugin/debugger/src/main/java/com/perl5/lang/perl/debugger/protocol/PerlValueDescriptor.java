/*
 * Copyright 2015-2021 Alexandr Evstigneev
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

package com.perl5.lang.perl.debugger.protocol;

import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;


public class PerlValueDescriptor {
  private String name;
  private String type;
  private String value;
  private String rendered;
  private boolean render_error;
  private int ref_depth; // how many references been skipped
  private String key;        // key to fetch additional elements, basically text representation of reference
  private int size;        // size used for arrays/hashes elements
  private boolean expandable;
  private boolean blessed;
  private boolean is_utf;
  private @Nullable String fileno;
  private @Nullable PerlLayersDescriptor layers;
  private @Nullable PerlValueDescriptor tied_with;

  public @Nullable String getFileno() {
    return fileno;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  public boolean isExpandable() {
    return expandable;
  }

  public int getSize() {
    return size;
  }

  public String getKey() {
    return key;
  }

  public boolean isBlessed() {
    return blessed;
  }

  public int getRefDepth() {
    return ref_depth;
  }

  public boolean isUtf() {
    return is_utf;
  }

  public String getRendered() {
    return rendered;
  }

  public boolean isRenderError() {
    return render_error;
  }

  public @Nullable PerlLayersDescriptor getLayers() {
    return layers;
  }

  public @Nullable PerlValueDescriptor getTiedWith() {
    return tied_with;
  }

  public boolean isExpandableNode() {
    return isExpandable() || getLayers() != null || getTiedWith() != null || getFileno() != null;
  }

  @Override
  public String toString() {
    return "PerlValueDescriptor{" +
           "name='" + name + '\'' +
           ", type='" + type + '\'' +
           ", value='" + value + '\'' +
           ", ref_depth=" + ref_depth +
           ", key='" + key + '\'' +
           ", size=" + size +
           ", expandable=" + expandable +
           ", blessed=" + blessed +
           ", is_utf=" + is_utf +
           ", fileno='" + fileno + '\'' +
           ", layers=" + layers +
           ", tied_with=" + tied_with +
           '}';
  }

  @TestOnly
  public String testDebugString() {
    return "PerlValueDescriptor{" +
           "\n\tname='" + name + '\'' +
           "\n\ttype='" + type.replaceAll("\\(.+\\)", "") + '\'' +
           "\n\tvalue='" + value + '\'' +
           "\n\tref_depth=" + ref_depth +
           "\n\tsize=" + size +
           "\n\texpandable=" + expandable +
           "\n\tblessed=" + blessed +
           "\n\tis_utf=" + is_utf +
           "\n\tfileno='" + fileno + '\'' +
           "\n\tlayers=" + layers +
           "\n\ttied_with=" + (tied_with == null ? null : tied_with.testDebugString()) +
           '}';
  }
}
