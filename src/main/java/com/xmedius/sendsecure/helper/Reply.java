package com.xmedius.sendsecure.helper;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class Reply builds an object containing the required informations to reply to an existing Safebox.
 */
public class Reply {
	@Expose
	private String message;
	@Expose
	private Boolean consent;
	private List<Attachment> attachments;
	@Expose
	@SerializedName("document_ids")
	private List<String> documentIds;

	public Reply(String message) {
		this.message = message;
		this.attachments = new ArrayList<Attachment>();
		this.documentIds = new ArrayList<String>();
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public void setConsent(Boolean consent) {
		this.consent = consent;
	}

	public boolean getConsent() {
		return this.consent.booleanValue();
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public List<Attachment> getAttachments() {
		return this.attachments;
	}

	public void setDocumentIds(List<String> documentIds) {
		this.documentIds = documentIds;
	}

	public List<String> getDocumentIds() {
		return this.documentIds;
	}

}
