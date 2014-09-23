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

package cherry.spring.common.custom.jdbc;

import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import cherry.spring.common.lib.mask.Masker;

public interface RowMapperCreator {

	<T> RowMapper<T> create(Class<T> mappedClass);

	<T> RowMapper<T> create(Class<T> mappedClass,
			Map<String, Masker<?>> maskerMap);

}
