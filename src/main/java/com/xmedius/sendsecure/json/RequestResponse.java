package com.xmedius.sendsecure.json;

/**
 * Class RequestResponse builds an object containing the server response to client requests (See Client.java for more informations).
 */
public class RequestResponse {
	private Boolean result;
	private String message;

	public String getMessage() {
		return message;
	}

	public Boolean getResult() {
		return result;
	}
}
