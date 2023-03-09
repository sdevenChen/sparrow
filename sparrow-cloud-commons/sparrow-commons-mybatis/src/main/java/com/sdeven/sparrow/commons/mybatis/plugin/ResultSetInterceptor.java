/**
 *    Copyright 2023 sdeven.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.sdeven.sparrow.commons.mybatis.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.PreparedStatement;
import java.sql.Statement;

@Intercepts(@Signature(type = StatementHandler.class, method = "query", args = { Statement.class,
		ResultHandler.class }))
public class ResultSetInterceptor implements Interceptor {

	private String defaultColumnPrefix;

	public void setDefaultColumnPrefix(String defaultColumnPrefix) {
		this.defaultColumnPrefix = defaultColumnPrefix;
	}

	@SuppressWarnings("unchecked")
	private static <T> T readField(Object target, String field) throws Throwable {
		return (T) SystemMetaObject.forObject(target).getValue(field);
//		return (T) FieldUtils.readField(target, field, true);
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		Object[] args = invocation.getArgs();
		Statement stmt = (Statement) args[0];
		ResultHandler<?> resultHandler = (ResultHandler<?>) args[1];

//		PreparedStatementHandler handler = (PreparedStatementHandler) invocation.getTarget();
		Object target = invocation.getTarget();
		if(target instanceof RoutingStatementHandler) {
			target = readField(target, "delegate");
		}

		ParameterHandler parameterHandler = readField(target, "parameterHandler");

		Executor executor = readField(target, "executor");
		MappedStatement mappedStatement = readField(target, "mappedStatement");
		RowBounds rowBounds = readField(target, "rowBounds");

		BoundSql boundSql = readField(target, "boundSql");

		MybatisResultSetHandler mybatisResultSetHandler = new com.sdeven.sparrow.commons.mybatis.plugin.MybatisResultSetHandler(executor, mappedStatement,
				parameterHandler, resultHandler, boundSql, rowBounds);
		mybatisResultSetHandler.setDefaultColumnPrefix(defaultColumnPrefix);

		if(stmt instanceof PreparedStatement) {
			((PreparedStatement)stmt).execute();
		} else {
			stmt.execute(boundSql.getSql());
		}

		return mybatisResultSetHandler.handleResultSets(stmt);
	}

}
