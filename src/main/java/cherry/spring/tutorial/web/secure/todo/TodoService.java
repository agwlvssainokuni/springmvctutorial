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

import cherry.spring.tutorial.db.gen.dto.Todo;

public interface TodoService {

	Integer create(Todo todo);

	boolean update(String loginId, int id, Todo todo);

	Todo findById(String loginId, int id);

	SearchResult searh(String loginId, SearchCondition cond, long pageNo,
			long pageSz);

}
