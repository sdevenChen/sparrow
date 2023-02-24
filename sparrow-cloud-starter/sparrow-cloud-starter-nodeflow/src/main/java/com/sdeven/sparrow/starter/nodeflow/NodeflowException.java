package com.sdeven.sparrow.starter.nodeflow;


 /**
   * @Description Nodeflow 异常
   * @Author sdeven.chen.dongwei@gmail.com
   * @Create 12/12/20 10:19
   */
public class NodeflowException extends Exception {
	private static final long serialVersionUID = 9222894717715477267L;
	private String[] params;

	public NodeflowException(String code, String... params) {
		super(code);
		this.params = params;
	}

	public NodeflowException(String code, Throwable t) {
		super(code, t);
	}

	public NodeflowException(String code, Throwable t, String... params) {
		super(code, t);
		this.params = params;
	}

	public String[] getParams() {
		return params;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

}
