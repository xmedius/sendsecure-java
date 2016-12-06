package com.xmedius.sendsecure.exception;

public class UnexpectedServerResponseException extends SendSecureException {

	private static final long serialVersionUID = 1L;

	public UnexpectedServerResponseException(String responseContent) {
		super("1", "Unexpected server response format", responseContent);
	}

	public UnexpectedServerResponseException(String responseContent, Throwable cause) {
		super("1", "Unexpected server response format", responseContent, cause);
	}
}
