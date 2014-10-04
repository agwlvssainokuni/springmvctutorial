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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cherry.spring.common.helper.querydsl.SQLQueryConfigurer;
import cherry.spring.common.helper.querydsl.SQLQueryHelper;
import cherry.spring.common.helper.querydsl.SQLQueryResult;
import cherry.spring.common.type.DeletedFlag;
import cherry.spring.common.type.FlagCode;
import cherry.spring.common.type.jdbc.RowMapperCreator;
import cherry.spring.tutorial.db.gen.dto.Todo;
import cherry.spring.tutorial.db.gen.dto.TodoCriteria;
import cherry.spring.tutorial.db.gen.dto.TodoCriteria.Criteria;
import cherry.spring.tutorial.db.gen.mapper.TodoMapper;
import cherry.spring.tutorial.db.gen.query.QTodo;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.types.Expression;

@Service
public class TodoServiceImpl implements TodoService {

	@Autowired
	private TodoMapper todoMapper;

	@Autowired
	private SQLQueryHelper sqlQueryHelper;

	@Autowired
	private RowMapperCreator rowMapperCreator;

	@Transactional
	@Override
	public Integer create(Todo todo) {
		int count = todoMapper.insertSelective(todo);
		if (count != 1) {
			return null;
		}
		return todo.getId();
	}

	@Transactional
	@Override
	public boolean update(String loginId, int id, Todo todo) {

		Todo record = new Todo();
		record.setDueDate(todo.getDueDate());
		record.setDescription(todo.getDescription());
		record.setDoneFlg(todo.getDoneFlg());
		record.setDoneAt(todo.getDoneAt());
		record.setLockVersion(todo.getLockVersion() + 1);

		TodoCriteria crit = new TodoCriteria();
		Criteria c = crit.createCriteria();
		c.andIdEqualTo(id);
		c.andPostedByEqualTo(loginId);
		c.andDeletedFlgEqualTo(DeletedFlag.NOT_DELETED);
		c.andLockVersionEqualTo(todo.getLockVersion());

		int count = todoMapper.updateByExampleSelective(record, crit);
		return count == 1;
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

	@Transactional(readOnly = true)
	@Override
	public SearchResult searh(String loginId, SearchCondition cond, int pageNo,
			int pageSz) {
		QTodo t = new QTodo("t");
		SQLQueryResult<Todo> r = sqlQueryHelper.search(
				configurer(t, loginId, cond), pageNo, pageSz,
				rowMapperCreator.create(Todo.class), columns(t));
		SearchResult result = new SearchResult();
		result.setPageSet(r.getPageSet());
		result.setResultList(r.getResultList());
		return result;
	}

	private Expression<?>[] columns(QTodo t) {
		return new Expression<?>[] { t.id, t.postedBy, t.postedAt, t.dueDate,
				t.doneAt, t.doneFlg, t.description, t.updatedAt, t.createdAt,
				t.lockVersion, t.deletedFlg };
	}

	private SQLQueryConfigurer configurer(final QTodo t, final String loginId,
			final SearchCondition cond) {
		return new SQLQueryConfigurer() {

			@Override
			public SQLQuery configure(SQLQuery query) {

				BooleanBuilder where = new BooleanBuilder();
				where.and(t.postedBy.eq(loginId));
				if (cond.getPostedFrom() != null) {
					where.and(t.postedAt.goe(cond.getPostedFrom()));
				}
				if (cond.getPostedTo() != null) {
					where.and(t.postedAt.lt(cond.getPostedTo()));
				}
				if (cond.getDueDateFrom() != null) {
					where.and(t.dueDate.goe(cond.getDueDateFrom()));
				}
				if (cond.getDueDateTo() != null) {
					where.and(t.dueDate.lt(cond.getDueDateTo()));
				}
				if (!cond.getDoneFlg().isEmpty()) {
					List<Integer> flg = new ArrayList<>();
					for (FlagCode f : cond.getDoneFlg()) {
						flg.add(f.code());
					}
					where.and(t.doneFlg.in(flg));
				}
				where.and(t.deletedFlg.eq(DeletedFlag.NOT_DELETED.code()));

				return query.from(t).where(where);
			}

			@Override
			public SQLQuery orderBy(SQLQuery query) {
				if (cond.getOrderBy() == OrderBy.NONE) {
					return query;
				} else if (cond.getOrderBy() == OrderBy.ID) {
					if (cond.getOrderDir() == OrderDir.DESC) {
						return query.orderBy(t.id.desc());
					} else {
						return query.orderBy(t.id.asc());
					}
				} else if (cond.getOrderBy() == OrderBy.POSTED_AT) {
					if (cond.getOrderDir() == OrderDir.DESC) {
						return query.orderBy(t.postedAt.desc());
					} else {
						return query.orderBy(t.postedAt.asc());
					}
				} else if (cond.getOrderBy() == OrderBy.DUE_DATE) {
					if (cond.getOrderDir() == OrderDir.DESC) {
						return query.orderBy(t.dueDate.desc());
					} else {
						return query.orderBy(t.dueDate.asc());
					}
				} else {
					return query;
				}
			}
		};
	}

}
