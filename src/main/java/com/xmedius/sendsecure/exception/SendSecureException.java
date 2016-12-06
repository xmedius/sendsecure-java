package com.xmedius.sendsecure.exception;

public class SendSecureException extends Exception {

	private static final long serialVersionUID = 1L;

	public static final int UNEXPECTED_FORMAT = 1;

	private String code;
	private String responseContent;

	public SendSecureException() {
		super();
	}

	public SendSecureException(String message) {
		super(message);
	}

	public SendSecureException(String code, String message, String responseContent) {
		super(message);
		this.code = code;
		this.responseContent = responseContent;
	}

	public SendSecureException(String code, String message, String responseContent, Throwable cause) {
		super(message, cause);
		this.code = code;
		this.responseContent = responseContent;
	}

	public String getCode() {
		return code;
	}

	public String getResponseContent() {
		return responseContent;
	}
}
