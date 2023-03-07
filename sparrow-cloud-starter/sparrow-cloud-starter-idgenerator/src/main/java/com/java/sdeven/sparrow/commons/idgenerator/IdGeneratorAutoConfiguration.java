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
package com.java.sdeven.sparrow.commons.idgenerator;

import com.java.sdeven.sparrow.commons.idgenerator.snowflake.SnowflakeZkIdGenerator;
import com.java.sdeven.sparrow.commons.idgenerator.snowflake.internal.SnowflakeIdProperties;
import com.java.sdeven.sparrow.commons.idgenerator.zookeeper.ZookeeperService;
import com.java.sdeven.sparrow.commons.idgenerator.zookeeper.ZookeeperServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * ZookeeperAutoConfigurer
 *
 * @author sdeven
 */
@Configuration
@ConditionalOnClass(IdGenerator.class)
@ConditionalOnProperty(prefix = "sdeven.commons.idgenerator", name = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties(IdGeneratorProperties.class)
public class IdGeneratorAutoConfiguration {

	@Autowired
	private IdGeneratorProperties idGeneratorProperties =new IdGeneratorProperties();

	@Bean
	public CuratorFramework zkClient() {

		CuratorProperties curatorProperties = idGeneratorProperties.getCurator();
		/** Retry strategy */
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(curatorProperties.getBaseSleepTimeMs(), curatorProperties.getMaxRetries());
		/** Create Curator by Factory */
		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder().connectString(curatorProperties.getUrl()).retryPolicy(retryPolicy)
				.sessionTimeoutMs(curatorProperties.getSessionTimeoutMs()).connectionTimeoutMs(curatorProperties.getConnectionTimeoutMs())
				.namespace(curatorProperties.getNamespace());
		if (StringUtils.isNotEmpty(curatorProperties.getScheme()) && StringUtils.isNotEmpty(curatorProperties.getAuth())) {
			builder.authorization(curatorProperties.getScheme(), curatorProperties.getAuth().getBytes(StandardCharsets.UTF_8));
			builder.aclProvider(new ACLProvider() {
				@Override
				public List<ACL> getDefaultAcl() {
					return ZooDefs.Ids.CREATOR_ALL_ACL;
				}

				@Override
				public List<ACL> getAclForPath(final String path) {
					return ZooDefs.Ids.CREATOR_ALL_ACL;
				}
			});
		}
		CuratorFramework client = builder.build();
		client.start();
		return client;
	}

	@Bean
	public ZookeeperService zkService(CuratorFramework zkClient) {
		return new ZookeeperServiceImpl(zkClient);
	}

	@Bean
	public IdGenerator idGeneratorFactory(ZookeeperService zkService) {
		SnowflakeIdProperties properties = new SnowflakeIdProperties();
		properties.setDataCenterId(idGeneratorProperties.getDataCenterId());
		properties.setZkIdAppPath(idGeneratorProperties.getZkAppPath());
		return new SnowflakeZkIdGenerator(zkService, properties);
	}
}
