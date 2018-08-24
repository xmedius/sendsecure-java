package com.xmedius.sendsecure.helper;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Class ConsentMessageGroup builds an object containing all localized versions of the same consent message.
 */
public class ConsentMessageGroup {
	private int id;
	private String name;
	@SerializedName("created_at")
	private Date createdAt;
	@SerializedName("updated_at")
	private Date updatedAt;
	@SerializedName("consent_messages")
	private List<ConsentMessage> consentMessages;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<ConsentMessage> getConsentMessages() {
		return consentMessages;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}
}
