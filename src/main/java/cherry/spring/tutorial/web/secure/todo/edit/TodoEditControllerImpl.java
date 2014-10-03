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

package cherry.spring.tutorial.web.secure.todo.edit;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;

import cherry.spring.common.helper.bizdate.BizdateHelper;
import cherry.spring.common.helper.logicalerror.LogicalErrorHelper;
import cherry.spring.common.helper.onetimetoken.OneTimeTokenValidator;
import cherry.spring.common.mvc.Contract;
import cherry.spring.common.type.FlagCode;
import cherry.spring.tutorial.db.gen.dto.Todo;
import cherry.spring.tutorial.web.PathDef;
import cherry.spring.tutorial.web.secure.todo.TodoService;

@Controller
public class TodoEditControllerImpl implements TodoEditController {

	@Autowired
	private TodoService todoService;

	@Autowired
	private BizdateHelper bizdateHelper;

	@Autowired
	private LogicalErrorHelper logicalErrorHelper;

	@Autowired
	private OneTimeTokenValidator oneTimeTokenValidator;

	@Override
	public TodoEditForm getForm(int id, Authentication auth) {

		Todo todo = todoService.findById(auth.getName(), id);
		Contract.shouldExist(todo, Todo.class, auth.getName(), id);

		TodoEditForm form = new TodoEditForm();
		form.setDueDate(todo.getDueDate());
		form.setDescription(todo.getDescription());
		form.setDoneFlg(todo.getDoneFlg() == FlagCode.TRUE);
		if (todo.getDoneFlg() == FlagCode.TRUE) {
			form.setDoneAt(todo.getDoneAt());
		}
		form.setLockVersion(todo.getLockVersion());
		return form;
	}

	@Override
	public ModelAndView init(int id, Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_EDIT);
		mav.addObject(PathDef.PATH_VAR_ID, id);
		return mav;
	}

	@Override
	public ModelAndView execute(int id, TodoEditForm form,
			BindingResult binding, Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request,
			RedirectAttributes redirAttr) {

		Todo todo = todoService.findById(auth.getName(), id);
		Contract.shouldExist(todo, Todo.class, auth.getName(), id);

		UriComponents uc = MvcUriComponentsBuilder.fromMethodName(
				TodoEditController.class, PathDef.METHOD_INIT, id, auth,
				locale, sitePref, request).build();

		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView(uc.toUriString(), true));
		return mav;
	}

}
