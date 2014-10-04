/*
 * Copyright 2008-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.data.jdbc.query;

import com.mysema.query.sql.dml.SQLUpdateClause;

/**
 * An interface used by {@link QueryDslJdbcTemplate} for update calls
 * where you are able to provide a {@link SQLUpdateClause} 
 * implementation to handle the update logic.
 *
 * @author Thomas Risberg
 * @since 1.0
 * @see QueryDslJdbcTemplate
 * @see SQLUpdateClause
 */
public interface SqlUpdateCallback {
	
	long doInSqlUpdateClause(SQLUpdateClause update);

}
