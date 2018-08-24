package com.xmedius.sendsecure.helper;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * Class ConsentMessage builds an object containing the informations of a consent message in a specific locale.
 */
public class ConsentMessage {

	private String locale;
	private String value;
	@SerializedName("created_at")
	private Date createdAt;
	@SerializedName("updated_at")
	private Date updatedAt;

	public String getLocale() {
		return locale;
	}

	public String getValue() {
		return value;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}
}
