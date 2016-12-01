package com.xmedius.sendsecure.exception;

public class SendSecureException extends Exception {

	private static final long serialVersionUID = 1L;

	public static final int UNEXPECTED_FORMAT = 1;

	private String code;


	//TODO: Better error handling to return full json body on error
	public SendSecureException() {
		super();
	}

	public SendSecureException(String code, String message) {
		super(message);
		this.code = code;
	}

	public SendSecureException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}

}
