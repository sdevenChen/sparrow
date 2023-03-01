package com.chen.sdeven.sparrow.commons.base.commons.result;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description 响应参数基类
 * @Author sdeven
 * @Create 11/13/20 18:19
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

	private static final long serialVersionUID = -655403293051655566L;
	/**
	 * 返回数据
	 */
	private T data;

	private List<T> datas;
	/**
	 * 返回码
 	 */
	private Integer code;
	/**
	 * 返回描述
	 */
	private String message;

	public Boolean success(){
		return Integer.valueOf(0).equals(code);
	}

	public Boolean fail(){
		return !success();
	}

	public Boolean nullData(){
		return data == null;
	}

	public Boolean nonNullData(){
		return !nullData();
	}

}