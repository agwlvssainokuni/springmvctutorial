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

package cherry.foundation.type.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import cherry.foundation.type.SecureString;

@MappedTypes(SecureString.class)
public class SecureStringTypeHandler extends BaseTypeHandler<SecureString> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			SecureString parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, parameter.crypto());
	}

	@Override
	public SecureString getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		String crypto = rs.getString(columnName);
		if (crypto == null) {
			return null;
		}
		return SecureString.cryptoValueOf(crypto);
	}

	@Override
	public SecureString getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		String crypto = rs.getString(columnIndex);
		if (crypto == null) {
			return null;
		}
		return SecureString.cryptoValueOf(crypto);
	}

	@Override
	public SecureString getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		String crypto = cs.getString(columnIndex);
		if (crypto == null) {
			return null;
		}
		return SecureString.cryptoValueOf(crypto);
	}

}
