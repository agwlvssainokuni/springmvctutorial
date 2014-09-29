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
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;

import cherry.spring.common.helper.bizdate.BizdateHelper;
import cherry.spring.common.helper.logicalerror.LogicalError;
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
	public ModelAndView init(int id, Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request) {

		Todo todo = todoService.findById(auth.getName(), id);
		Contract.shouldExist(todo, Todo.class, auth.getName(), id);

		TodoEditForm form = new TodoEditForm();
		form.setDueDate(todo.getDueDate());
		form.setDescription(todo.getDescription());
		form.setDoneFlg(todo.getDoneFlg().isTrue());
		form.setLockVersion(todo.getLockVersion());

		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_EDIT);
		mav.addObject(PathDef.PATH_VAR_ID, id);
		mav.addObject(todo);
		mav.addObject(form);
		return mav;
	}

	@Override
	public ModelAndView confirm(int id, TodoEditForm form,
			BindingResult binding, Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request) {

		Todo todo = todoService.findById(auth.getName(), id);
		Contract.shouldExist(todo, Todo.class, auth.getName(), id);

		if (binding.hasErrors()) {
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_EDIT);
			mav.addObject(PathDef.PATH_VAR_ID, id);
			mav.addObject(todo);
			return mav;
		}

		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_EDIT_CONFIRM);
		mav.addObject(PathDef.PATH_VAR_ID, id);
		mav.addObject(todo);
		return mav;
	}

	@Override
	public ModelAndView execute(int id, TodoEditForm form,
			BindingResult binding, Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request) {

		Todo todo = todoService.findById(auth.getName(), id);
		Contract.shouldExist(todo, Todo.class, auth.getName(), id);

		if (binding.hasErrors()) {
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_EDIT);
			mav.addObject(PathDef.PATH_VAR_ID, id);
			mav.addObject(todo);
			return mav;
		}

		if (!oneTimeTokenValidator.isValid(request)) {
			logicalErrorHelper.reject(binding, LogicalError.OneTimeTokenError);
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_EDIT);
			mav.addObject(PathDef.PATH_VAR_ID, id);
			mav.addObject(todo);
			return mav;
		}

		Todo newTodo = new Todo();
		newTodo.setDueDate(form.getDueDate());
		newTodo.setDescription(form.getDescription());
		newTodo.setDoneFlg(FlagCode.valueOf(form.isDoneFlg()));
		if (form.isDoneFlg() && !todo.getDoneFlg().isTrue()) {
			newTodo.setDoneAt(bizdateHelper.now());
		}
		newTodo.setLockVersion(form.getLockVersion());

		boolean result = todoService.update(auth.getName(), id, newTodo);
		if (!result) {
			logicalErrorHelper
					.reject(binding, LogicalError.OptimisticLockError);
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_EDIT);
			mav.addObject(PathDef.PATH_VAR_ID, id);
			mav.addObject(todo);
			return mav;
		}

		UriComponents uc = MvcUriComponentsBuilder.fromMethodName(
				TodoEditController.class, PathDef.METHOD_FINISH, id, auth,
				locale, sitePref, request).build();

		ModelAndView mav = new ModelAndView();
		mav.setView(new RedirectView(uc.toUriString(), true));
		return mav;
	}

	@Override
	public ModelAndView finish(int id, Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request) {

		Todo todo = todoService.findById(auth.getName(), id);
		Contract.shouldExist(todo, Todo.class, auth.getName(), id);

		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_EDIT_FINISH);
		mav.addObject(PathDef.PATH_VAR_ID, id);
		mav.addObject(todo);
		return mav;
	}

}
