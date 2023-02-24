package com.java.sdeven.sparrow.commons.idgenerator;

import lombok.Data;

@Data
public class CuratorProperties {
	private Integer baseSleepTimeMs;
	private Integer maxRetries;
	private String url;
	private String scheme;
	private String auth;
	private Integer sessionTimeoutMs;
	private Integer connectionTimeoutMs;
	private String namespace;
}
