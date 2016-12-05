package com.xmedius.sendsecure.json;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.xmedius.sendsecure.helper.Recipient;
import com.xmedius.sendsecure.helper.SecurityProfile.LongTimeUnit;
import com.xmedius.sendsecure.helper.SecurityProfile.RetentionPeriodType;
import com.xmedius.sendsecure.helper.SecurityProfile.TimeUnit;

public class CommitSafebox {

	@SerializedName("guid")
	private String guid;
	@SerializedName("recipients")
	private List<Recipient> recipients;
	@SerializedName("subject")
	private String subject;
	@SerializedName("message")
	private String message;
	@SerializedName("document_ids")
	private List<String> documentIds;
	@SerializedName("security_profile_id")
	private Integer securityProfileId;
	@SerializedName("reply_enabled")
	private boolean replyEnabled;
	@SerializedName("group_replies")
	private boolean groupReplies;
	@SerializedName("expiration_value")
	private int expirationValue;
	@SerializedName("expiration_unit")
	private TimeUnit expirationUnit;
	@SerializedName("retention_period_type")
	private RetentionPeriodType retentionPeriodType;
	@SerializedName("retention_period_value")
	private Integer retentionPeriodValue;
	@SerializedName("retention_period_unit")
	private LongTimeUnit retentionPeriodUnit;
	@SerializedName("encrypt_message")
	private boolean encryptMessage;
	@SerializedName("double_encryption")
	private boolean doubleEncryption;
	@SerializedName("public_encryption_key")
	private String publicEncryptionKey;
	@SerializedName("notification_language")
	private String notificationLanguage;

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public void setRecipients(List<Recipient> recipients) {
		this.recipients = recipients;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setDocumentIds(List<String> documentIds) {
		this.documentIds = documentIds;
	}

	public void setSecurityProfileId(int securityProfileId) {
		this.securityProfileId = securityProfileId;
	}

	public void setReplyEnabled(boolean replyEnabled) {
		this.replyEnabled = replyEnabled;
	}

	public void setGroupReplies(boolean groupReplies) {
		this.groupReplies = groupReplies;
	}

	public void setExpirationValue(Integer expirationValue) {
		this.expirationValue = expirationValue;
	}

	public void setExpirationUnit(TimeUnit expirationUnit) {
		this.expirationUnit = expirationUnit;
	}

	public void setRetentionPeriodType(RetentionPeriodType retentionPeriodType) {
		this.retentionPeriodType = retentionPeriodType;
	}

	public void setRetentionPeriodValue(Integer retentionPeriodValue) {
		this.retentionPeriodValue = retentionPeriodValue;
	}

	public void setRetentionPeriodUnit(LongTimeUnit retentionPeriodUnit) {
		this.retentionPeriodUnit = retentionPeriodUnit;
	}

	public void setEncryptMessage(boolean encryptMessage) {
		this.encryptMessage = encryptMessage;
	}

	public void setDoubleEncryption(boolean doubleEncryption) {
		this.doubleEncryption = doubleEncryption;
	}

	public void setPublicEncryptionKey(String publicEncryptionKey) {
		this.publicEncryptionKey = publicEncryptionKey;
	}

	public void setNotificationLanguage(String notificationLanguage) {
		this.notificationLanguage = notificationLanguage;
	}
}
