package com.xmedius.sendsecure.helper;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Safebox {

	@SerializedName("user_email")
	private String userEmail;
	private String subject;
	private String message;
	private String guid;
	@SerializedName("public_encryption_key")
	private String publicEncryptionKey;
	@SerializedName("upload_url")
	private String uploadUrl;
	private List<Recipient> recipients = new ArrayList<Recipient>();
	private List<Attachment> attachments = new ArrayList<Attachment>();
	@SerializedName("security_profile")
	private SecurityProfile securityProfile;
	@SerializedName("notification_language")
	private String notificationLanguage = "en";

	public Safebox(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getPublicEncryptionKey() {
		return publicEncryptionKey;
	}

	public void setPublicEncryptionKey(String publicEncryptionKey) {
		this.publicEncryptionKey = publicEncryptionKey;
	}

	public String getUploadUrl() {
		return uploadUrl;
	}

	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}

	public List<Recipient> getRecipients() {
		return recipients;
	}

	public void setRecipients(List<Recipient> recipients) {
		this.recipients = recipients;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public SecurityProfile getSecurityProfile() {
		return securityProfile;
	}

	public void setSecurityProfile(SecurityProfile securityProfile) {
		this.securityProfile = securityProfile;
	}

	public String getNotificationLanguage() {
		return notificationLanguage;
	}

	public void setNotificationLanguage(String notificationLanguage) {
		this.notificationLanguage = notificationLanguage;
	}
}
