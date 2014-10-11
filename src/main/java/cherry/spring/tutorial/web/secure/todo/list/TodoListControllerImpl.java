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

package cherry.spring.tutorial.web.secure.todo.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import cherry.spring.common.helper.bizdate.BizdateHelper;
import cherry.spring.common.helper.download.DownloadHelper;
import cherry.spring.common.helper.querydsl.SQLQueryHelper;
import cherry.spring.common.lib.util.LocalDateTimeUtil;
import cherry.spring.common.lib.util.LocalDateUtil;
import cherry.spring.common.type.FlagCode;
import cherry.spring.tutorial.web.PathDef;
import cherry.spring.tutorial.web.secure.todo.OrderBy;
import cherry.spring.tutorial.web.secure.todo.OrderDir;
import cherry.spring.tutorial.web.secure.todo.SearchCondition;
import cherry.spring.tutorial.web.secure.todo.SearchResult;
import cherry.spring.tutorial.web.secure.todo.TodoService;

@Controller
public class TodoListControllerImpl implements TodoListController {

	@Value("${tutorial.web.secure.todo.list.defaultOffsetOfDueDate}")
	private int defaultOffsetOfDueDate;

	@Value("${tutorial.web.secure.todo.list.defaultPageSize}")
	private long defaultPageSize;

	@Value("${tutorial.web.secure.todo.list.contentType}")
	private String contentType;

	@Value("${tutorial.web.secure.todo.list.filename}")
	private String filename;

	@Autowired
	private TodoService todoService;

	@Autowired
	private BizdateHelper bizdateHelper;

	@Autowired
	private SQLQueryHelper sqlQueryHelper;

	@Autowired
	private DownloadHelper downloadHelper;

	@Override
	public TodoListForm getForm() {
		TodoListForm form = new TodoListForm();
		form.setDueDateTo(bizdateHelper.today()
				.plusDays(defaultOffsetOfDueDate));
		form.setNotDone(true);
		form.setOrderBy(OrderBy.POSTED_AT);
		form.setOrderDir(OrderDir.DESC);
		return form;
	}

	@Override
	public ModelAndView init(Authentication auth, Locale locale,
			SitePreference sitePref, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_LIST);
		mav.addObject(getForm());
		return mav;
	}

	@Override
	public ModelAndView execute(TodoListForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request) {

		if (binding.hasErrors()) {
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_LIST);
			return mav;
		}

		String loginId = auth.getName();
		SearchCondition cond = createCondition(form);
		int pageNo = form.getPageNo();
		int pageSz = form.getPageSz() <= 0 ? defaultPageSize : form.getPageSz();

		SearchResult result = todoService.searh(loginId, cond, pageNo, pageSz);

		ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_LIST);
		mav.addObject(result);
		return mav;
	}

	@Override
	public ModelAndView download(TodoListForm form, BindingResult binding,
			Authentication auth, Locale locale, SitePreference sitePref,
			HttpServletRequest request, HttpServletResponse response) {

		if (binding.hasErrors()) {
			ModelAndView mav = new ModelAndView(PathDef.VIEW_TODO_LIST);
			return mav;
		}

		return null;
	}

	private SearchCondition createCondition(TodoListForm form) {

		SearchCondition cond = new SearchCondition();
		cond.setPostedFrom(LocalDateTimeUtil.rangeFrom(form.getPostedFrom()));
		cond.setPostedTo(LocalDateTimeUtil.rangeTo(form.getPostedTo()));
		cond.setDueDateFrom(LocalDateUtil.rangeFrom(form.getDueDateFrom()));
		cond.setDueDateTo(LocalDateUtil.rangeTo(form.getDueDateTo()));

		List<FlagCode> doneFlg = new ArrayList<>();
		if (form.isDone()) {
			doneFlg.add(FlagCode.TRUE);
		}
		if (form.isNotDone()) {
			doneFlg.add(FlagCode.FALSE);
		}
		cond.setDoneFlg(doneFlg);

		cond.setOrderBy(form.getOrderBy());
		cond.setOrderDir(form.getOrderDir());

		return cond;
	}

}
