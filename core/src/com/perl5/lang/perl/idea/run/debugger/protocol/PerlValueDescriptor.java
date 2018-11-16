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

package com.perl5.lang.perl.idea.run.debugger.protocol;

import org.jetbrains.annotations.Nullable;

/**
 * Created by hurricup on 08.05.2016.
 */
public class PerlValueDescriptor {
  private String name;
  private String type;
  private String value;
  private int ref_depth; // how many references been skipped
  private String key;        // key to fetch additional elements, basically text representation of reference
  private int size;        // size used for arrays/hashes elements
  private boolean expandable;
  private boolean blessed;
  private boolean is_utf;
  @Nullable
  private String fileno;
  @Nullable
  private PerlLayersDescriptor layers;
  @Nullable
  private PerlValueDescriptor tied_with;

  @Nullable
  public String getFileno() {
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

  @Nullable
  public PerlLayersDescriptor getLayers() {
    return layers;
  }

  @Nullable
  public PerlValueDescriptor getTiedWith() {
    return tied_with;
  }

  public boolean isExpandableNode() {
    return isExpandable() || getLayers() != null || getTiedWith() != null || getFileno() != null;
  }
}
