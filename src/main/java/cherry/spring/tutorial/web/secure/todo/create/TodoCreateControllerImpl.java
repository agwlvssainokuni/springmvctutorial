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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;

import cherry.spring.common.helper.bizdate.BizdateHelper;
import cherry.spring.tutorial.web.PathDef;

@Controller
public class TodoCreateControllerImpl implements TodoCreateController {

	@Value("${tutorial.web.secure.todo.create.defaultOffsetOfDueDate}")
	private int defaultOffsetOfDueDate;

	@Autowired
	private BizdateHelper bizdateHelper;

	@Override
	public TodoCreateForm getForm() {
		TodoCreateForm form = new TodoCreateForm();
		form.setDueDate(bizdateHelper.today().minusDays(defaultOffsetOfDueDate));
		return form;
	}

	@Override
	public ModelAndView init(Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_CREATE);
		return mav;
	}

	@Override
	public ModelAndView confirm(TodoCreateForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request) {

		if (binding.hasErrors()) {
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_CREATE);
			return mav;
		}

		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_CREATE_CONFIRM);
		return mav;
	}

	@Override
	public ModelAndView execute(TodoCreateForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request) {

		Integer id = 0;

		if (binding.hasErrors()) {
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_CREATE);
			return mav;
		}

		UriComponents uc = MvcUriComponentsBuilder.fromMethodName(
				TodoCreateController.class, PathDef.METHOD_FINISH, id, auth,
				locale, sitePref, request).build();

		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView(uc.toUriString(), true));
		return mav;
	}

	@Override
	public ModelAndView finish(int id, Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_CREATE_FINISH);
		return mav;
	}

}
