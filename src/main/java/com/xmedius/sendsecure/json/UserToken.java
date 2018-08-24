package com.xmedius.sendsecure.json;

import com.google.gson.annotations.SerializedName;

/**
 * Class UserToken builds an object containing the server response to Client.getUserToken method.
 */
public class UserToken {
	private String result;
	@SerializedName("user_id")
	private Integer userId;
	private String token;
	private String message;
	private String code;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
