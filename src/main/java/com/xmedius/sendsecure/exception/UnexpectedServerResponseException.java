package com.xmedius.sendsecure.exception;

public class UnexpectedServerResponseException extends SendSecureException {

	private static final long serialVersionUID = 1L;

	public UnexpectedServerResponseException() {
		super("1", "Unexpected server response format");
	}

	public UnexpectedServerResponseException(Throwable cause) {
		super("1", "Unexpected server response format", cause);
	}
}
