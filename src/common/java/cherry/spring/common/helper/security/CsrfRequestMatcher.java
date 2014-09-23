/*
 * Copyright 2014 agwlvssainokuni
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cherry.spring.common.helper.security;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.RequestMatcher;

public class CsrfRequestMatcher implements RequestMatcher {

	private Pattern allowedMethods = Pattern.compile(
			"^(GET|HEAD|TRACE|OPTIONS)$", Pattern.CASE_INSENSITIVE);

	private List<RequestMatcher> excludes = new ArrayList<>();

	public void setAllowedMethods(Pattern allowedMethods) {
		this.allowedMethods = allowedMethods;
	}

	public void setExcludes(List<RequestMatcher> excludes) {
		this.excludes = excludes;
	}

	@Override
	public boolean matches(HttpServletRequest request) {
		if (allowedMethods.matcher(request.getMethod()).matches()) {
			return false;
		}
		for (RequestMatcher matcher : excludes) {
			if (!matcher.matches(request)) {
				return false;
			}
		}
		return true;
	}

}
