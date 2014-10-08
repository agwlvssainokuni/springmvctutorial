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

package cherry.spring.common.lib.util;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;

public class LocalDateTimeUtil {

	private static Period unitOfTime = Period.seconds(1);

	public static Period setUnitOfTime(Period period) {
		unitOfTime = period;
		return unitOfTime;
	}

	public static LocalDateTime rangeFrom(LocalDate from) {
		if (from == null) {
			return null;
		}
		return from.toLocalDateTime(LocalTime.MIDNIGHT);
	}

	public static LocalDateTime rangeFrom(LocalDate fromD, LocalTime fromT) {
		if (fromD == null) {
			return null;
		}
		if (fromT == null) {
			return fromD.toLocalDateTime(LocalTime.MIDNIGHT);
		}
		return fromD.toLocalDateTime(fromT);
	}

	public static LocalDateTime rangeFrom(LocalDateTime from) {
		return from;
	}

	public static LocalDateTime rangeTo(LocalDate to) {
		if (to == null) {
			return null;
		}
		return to.toLocalDateTime(LocalTime.MIDNIGHT).plusDays(1);
	}

	public static LocalDateTime rangeTo(LocalDate toD, LocalTime toT) {
		if (toD == null) {
			return null;
		}
		if (toT == null) {
			return toD.toLocalDateTime(LocalTime.MIDNIGHT).plusDays(1);
		}
		return toD.toLocalDateTime(toT).plus(unitOfTime);
	}

	public static LocalDateTime rangeTo(LocalDateTime to) {
		if (to == null) {
			return null;
		}
		return to.plus(unitOfTime);
	}

}
