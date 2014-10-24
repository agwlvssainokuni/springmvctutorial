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

package cherry.spring.tutorial.web.secure.todo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cherry.spring.fwcore.type.DeletedFlag;
import cherry.spring.tutorial.db.gen.dto.Todo;
import cherry.spring.tutorial.db.gen.dto.TodoCriteria;
import cherry.spring.tutorial.db.gen.dto.TodoCriteria.Criteria;
import cherry.spring.tutorial.db.gen.mapper.TodoMapper;

@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoMapper todoMapper;

	@Transactional
	@Override
	public Integer create(Todo todo) {
		int count = todoMapper.insertSelective(todo);
		if (count != 1) {
			return null;
		}
		return todo.getId();
	}

	@Transactional(readOnly = true)
	@Override
	public Todo findById(String loginId, int id) {
		TodoCriteria crit = new TodoCriteria();
		Criteria c = crit.createCriteria();
		c.andIdEqualTo(id);
		c.andPostedByEqualTo(loginId);
		c.andDeletedFlgEqualTo(DeletedFlag.NOT_DELETED);
		List<Todo> list = todoMapper.selectByExample(crit);
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

}
