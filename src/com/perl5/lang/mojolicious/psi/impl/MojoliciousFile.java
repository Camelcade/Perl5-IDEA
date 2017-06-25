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

package com.perl5.lang.mojolicious.psi.impl;

import com.perl5.lang.perl.extensions.packageprocessor.PerlExportDescriptor;
import com.perl5.lang.perl.psi.PerlFile;

import java.util.Arrays;
import java.util.List;

public interface MojoliciousFile extends PerlFile {
  String MOJO_CONTROLLER_NS = "Mojolicious::Controller";
  String MOJO_SANDBOX_NS_PREFIX = "Mojo::Template::Sandbox";
  String MOJO_TAG_HELPERS_NS = "Mojolicious::Plugin::TagHelpers";
  String MOJO_DEFAULT_HELPERS_NS = "Mojolicious::Plugin::DefaultHelpers";

  // fixme make a builder
  List<PerlExportDescriptor> HARDCODED_DESCRIPTORS = Arrays.asList(

    PerlExportDescriptor.create(MOJO_DEFAULT_HELPERS_NS, "csrf_token", "_csrf_token"),
    PerlExportDescriptor.create(MOJO_DEFAULT_HELPERS_NS, "current_route", "_current_route"),
    PerlExportDescriptor.create(MOJO_DEFAULT_HELPERS_NS, "delay", "_delay"),
    PerlExportDescriptor.create(MOJO_DEFAULT_HELPERS_NS, "inactivity_timeout", "_inactivity_timeout"),
    PerlExportDescriptor.create(MOJO_DEFAULT_HELPERS_NS, "is_fresh", "_is_fresh"),
    PerlExportDescriptor.create(MOJO_DEFAULT_HELPERS_NS, "url_with", "_url_with"),

    PerlExportDescriptor.create(MOJO_CONTROLLER_NS, "extends", "stash"),
    PerlExportDescriptor.create(MOJO_CONTROLLER_NS, "layout", "stash"),
    PerlExportDescriptor.create(MOJO_CONTROLLER_NS, "title", "stash"),

    PerlExportDescriptor.create(MOJO_CONTROLLER_NS, "app", "app"),
    PerlExportDescriptor.create(MOJO_CONTROLLER_NS, "flash", "flash"),
    PerlExportDescriptor.create(MOJO_CONTROLLER_NS, "param", "param"),
    PerlExportDescriptor.create(MOJO_CONTROLLER_NS, "stash", "stash"),
    PerlExportDescriptor.create(MOJO_CONTROLLER_NS, "session", "session"),
    PerlExportDescriptor.create(MOJO_CONTROLLER_NS, "url_for", "url_for"),
    PerlExportDescriptor.create(MOJO_CONTROLLER_NS, "validation", "validation"),

    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "color_field", "_input"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "email_field", "_input"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "number_field", "_input"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "range_field", "_input"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "search_field", "_input"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "tel_field", "_input"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "text_field", "_input"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "url_field", "_input"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "date_field", "_input"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "month_field", "_input"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "time_field", "_input"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "week_field", "_input"),

    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "csrf_field", "_csrf_field"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "form_for", "_form_for"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "hidden_field", "_hidden_field"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "javascript", "_javascript"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "label_for", "_label_for"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "link_to", "_link_to"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "select_field", "_select_field"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "stylesheet", "_stylesheet"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "submit_button", "_submit_button"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "tag_with_error", "_tag_with_error"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "text_area", "_text_area"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "t", "_tag"),
    PerlExportDescriptor.create(MOJO_TAG_HELPERS_NS, "tag", "_tag")
  );
}
