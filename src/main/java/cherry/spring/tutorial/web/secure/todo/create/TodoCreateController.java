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

package cherry.spring.tutorial.web.secure.todo.create;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.mobile.device.site.SitePreference;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cherry.spring.tutorial.web.PathDef;

@RequestMapping(PathDef.URI_TODO_CREATE)
public interface TodoCreateController {

	@ModelAttribute()
	TodoCreateForm getForm();

	@RequestMapping()
	ModelAndView init(Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request);

	@RequestMapping(PathDef.SUBURI_CONFIRM)
	ModelAndView confirm(@Validated TodoCreateForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request);

	@RequestMapping(PathDef.SUBURI_EXECUTE)
	ModelAndView execute(@Validated TodoCreateForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request, RedirectAttributes redirAttr);

	@RequestMapping(PathDef.SUBURI_FINISH)
	ModelAndView finish(Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request,
			RedirectAttributes redirAttr);

}
