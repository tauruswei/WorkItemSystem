package com.firefly.exception;

public class CustomException extends Exception {

	private static final long serialVersionUID = -7284990650053367302L;

	public CustomException(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public CustomException(String message) {
		this.code = "999999";
		this.message = message;
	}

	private String code;
	private String message;
	private String detail;

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

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
}
