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

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDate;

import cherry.spring.fwcore.type.format.CustomDateTimeFormat;
import cherry.spring.fwcore.validator.MaxLength;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class TodoCreateForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@CustomDateTimeFormat()
	private LocalDate dueDate;

	@NotEmpty
	@MaxLength(5000)
	private String description;

}
