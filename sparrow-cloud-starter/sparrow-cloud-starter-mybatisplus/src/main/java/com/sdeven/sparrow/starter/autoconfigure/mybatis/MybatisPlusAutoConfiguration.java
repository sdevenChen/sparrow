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
package com.sdeven.sparrow.starter.autoconfigure.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.sdeven.sparrow.commons.mybatis.mybatisplus.CustomSqlInjector;
import com.sdeven.sparrow.commons.mybatis.mybatisplus.LocalDateTimeTypeHandler;
import com.sdeven.sparrow.commons.mybatis.plugin.ResultSetInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @author sdeven
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(MybatisPlusProperties.class)
public class MybatisPlusAutoConfiguration {
	@Autowired
	public MybatisPlusProperties mybatisProperties;

	@Bean
	public PaginationInnerInterceptor paginationInterceptor() {
		PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
		//Set the operation after the requested page is larger than the maximum page, true to return to the first page, false to continue the request default false
		paginationInterceptor.setOverflow(mybatisProperties.getLastToIndex());
		//Set the maximum number of single page limit, default 500 items, -1 is not limited
		paginationInterceptor.setMaxLimit(mybatisProperties.getQueryMaxLimit());
		paginationInterceptor.setDbType(DbType.MYSQL);
		return paginationInterceptor;
	}

	/**
	 * New paging plugin, one slow and two slow follow mybatis rules,
	 * need to set MybatisConfiguration#useDeprecatedExecutor = false to avoid caching problems (this property will be removed together with the old plugin removal)
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(paginationInterceptor());
		return interceptor;
	}

	/**
	 * Override Customizer Configuration
	 * @return {@link ConfigurationCustomizer}
	 */
	@Bean
	public ConfigurationCustomizer configurationCustomizer() {
		return configuration -> {
			ResultSetInterceptor resultSetInterceptor = new ResultSetInterceptor();
			resultSetInterceptor.setDefaultColumnPrefix(mybatisProperties.getColumnPrefix());
			configuration.addInterceptor(resultSetInterceptor);
			configuration.setUseDeprecatedExecutor(false);
			configuration.getTypeHandlerRegistry().register(LocalDateTimeTypeHandler.class);
		};
	}

	@Bean
	public CustomSqlInjector customSqlInjector() {
		return new CustomSqlInjector();
	}

	/**
	 * Optimistic lock plug-in [need to enable the plug-in to avoid parameter error
	 * @return {@link OptimisticLockerInterceptor}
	 */
	@Bean
	public OptimisticLockerInterceptor optimisticLockerInterceptor() {
		return new OptimisticLockerInterceptor();
	}

}
