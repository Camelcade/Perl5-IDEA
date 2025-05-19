/*
 * Copyright 2015-2025 Alexandr Evstigneev
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

package com.perl5.errorHandler;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.ArrayList;
import java.util.List;

@VisibleForTesting
public final class YoutrackApi {
  // https://camelcade.myjetbrains.com/youtrack/api/admin/projects
  private static final String PROJECT_ID = "81-0";
  private static final YoutrackProject PROJECT = new YoutrackProject(PROJECT_ID);

  // https://camelcade.myjetbrains.com/youtrack/api/admin/projects/81-0/customFields?fields=id,canBeEmpty,emptyFieldText,project(id,name),field(id,name,fieldType(id))
  private static final YoutrackSingleCustomField PRIORITY =
    new YoutrackSingleCustomField("Priority", "SingleEnumIssueCustomField", "Normal");
  private static final YoutrackSingleCustomField TYPE = new YoutrackSingleCustomField("Type", "SingleEnumIssueCustomField", "Exception");

  private YoutrackApi() {
  }

  static final class YoutrackCustomFieldValue {
    @SuppressWarnings("unused") @Expose
    public final String name;

    public YoutrackCustomFieldValue(String name) {
      this.name = name;
    }
  }

  public abstract static class YoutrackCustomField {
    @SuppressWarnings("unused") @Expose
    public final String name;
    @SuppressWarnings("unused") @Expose
    @SerializedName("$type")
    public final String type;

    protected YoutrackCustomField(String name, String type) {
      this.name = name;
      this.type = type;
    }
  }

  static final class YoutrackSingleCustomField extends YoutrackCustomField {
    @SuppressWarnings("unused") @Expose
    public final YoutrackCustomFieldValue value;

    public YoutrackSingleCustomField(String name, String type, String value) {
      super(name, type);
      this.value = new YoutrackCustomFieldValue(value);
    }
  }


  public static final class YoutrackProject {
    @SuppressWarnings("unused") @Expose
    public final String id;

    YoutrackProject(String id) {
      this.id = id;
    }
  }

  @VisibleForTesting
  public static final class YoutrackIssue {
    @Expose
    public final YoutrackProject project = PROJECT;
    @Expose
    public final List<? super YoutrackCustomField> customFields = new ArrayList<>();
    @Expose
    public String summary;
    @Expose
    public String description;

    public YoutrackIssue(String summary, String description) {
      this.summary = summary;
      this.description = description;
      customFields.add(PRIORITY);
      customFields.add(TYPE);
    }

    @Override
    public String toString() {
      return "YoutrackIssue{" +
             "project=" + project +
             ", customFields=" + customFields +
             ", summary='" + summary + '\'' +
             ", description='" + description + '\'' +
             '}';
    }
  }

  @VisibleForTesting
  public static final class YoutrackIssueResponse {
    @Expose
    public String id;
    @Expose
    public String idReadable;
    @Expose
    @SerializedName("$type")
    public String type;

    public YoutrackIssue issue;
    public int attachmentsAdded = 0;

    @Override
    public String toString() {
      return "YoutrackIssueResponse{" +
             "id='" + id + '\'' +
             ", idReadable='" + idReadable + '\'' +
             ", type='" + type + '\'' +
             ", issue=" + issue +
             '}';
    }
  }
}
