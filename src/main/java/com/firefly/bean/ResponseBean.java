package com.firefly.bean;

public class ResponseBean {
	private String code;
	private String message;
	private Object data;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public static ResponseBean errorMessage(String code,String message) {
		ResponseBean bean = new ResponseBean();
		bean.setCode(code);
		bean.setMessage(message);
		return bean;
	}

}
