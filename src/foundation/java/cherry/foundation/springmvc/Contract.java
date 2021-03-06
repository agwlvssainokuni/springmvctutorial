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

package cherry.foundation.springmvc;

import com.google.common.base.Joiner;

public class Contract {

	public static <T> void shouldExist(T reference, Class<T> type,
			Object... args) throws NotFoundException {
		if (reference == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(type.getSimpleName()).append(" not found");
			if (args.length > 0) {
				sb.append(": ");
				Joiner.on(", ").appendTo(sb, args);
			}
			throw new NotFoundException(sb.toString());
		}
	}

}
