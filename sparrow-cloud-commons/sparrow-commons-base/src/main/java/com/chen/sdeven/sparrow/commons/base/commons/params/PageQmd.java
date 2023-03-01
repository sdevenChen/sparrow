package com.chen.sdeven.sparrow.commons.base.commons.params;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @Description 公共分页请求类
 * @Author sdeven
 */
@Data
public class PageQmd implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 当前页
	 */
	@NotNull(message = "当前页不能为空")
	public Long current = 1L;

	/**
	 * 每页显示条数，默认 10
	 */
	@NotNull(message = "每页显示条数不能为空")
	public Long size = 10L;

	/**
	 * 排序字段
	 */
	public String order;

}
