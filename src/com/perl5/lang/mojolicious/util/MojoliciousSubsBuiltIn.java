/*
 * Copyright 2015 Alexandr Evstigneev
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

package com.perl5.lang.mojolicious.util;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by hurricup on 02.08.2015.
 */
public interface MojoliciousSubsBuiltIn
{
	public static final HashSet<String> MOJO_DEFAULT_HELPERS = new HashSet<String>(Arrays.asList(
			"accepts",
			"app",
			"b",
			"c",
			"config",
			"content",
			"content_for",
			"content_with",
			"csrf_token",
			"current_route",
			"delay",
			"dumper",
			"extends",
			"flash",
			"inactivity_timeout",
			"include",
			"is_fresh",
			"layout",
			"param",
			// fixme this is temporary fast solution
			"reply->asset",
			"reply->exception",
			"reply->not_found",
			"reply->static",
			"session",
			"stash",
			"title",
			"ua",
			"url_for",
			"url_with",
			"validation"
	));

	public static final HashSet<String> MOJO_TAG_HELPERS = new HashSet<String>(Arrays.asList(
			"check_box",
			"color_field",
			"csrf_field",
			"date_field",
			"datetime_field",
			"email_field",
			"file_field",
			"form_for",
			"hidden_field",
			"image",
			"input_tag",
			"javascript",
			"label_for",
			"link_to",
			"month_field",
			"number_field",
			"password_field",
			"radio_button",
			"range_field",
			"search_field",
			"select_field",
			"stylesheet",
			"submit_button",
			"t",
			"tag",
			"tag_with_error",
			"tel_field",
			"text_area",
			"text_field",
			"time_field",
			"url_field",
			"week_field"
	));
}
