package com.xmedius.sendsecure.helper;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.xmedius.sendsecure.exception.SendSecureException;

/**
 * Class Safebox builds an object to send all the necessary information to create a new Safebox
 */

public class Safebox {

	public enum Status
	{
	@SerializedName("in_progress") IN_PROGRESS,
	@SerializedName("closed") CLOSED,
	@SerializedName("content_deleted") CONTENT_DELETED,
	@SerializedName("unread") UNREAD
	}

	@Expose
	private String guid;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("user_id")
	private int userId;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("enterprise_id")
	private int enterpriseId;
	@Expose
	private String subject;
	@Expose
	@SerializedName("notification_language")
	private String notificationLanguage = "en";
	@Expose(serialize = false, deserialize = true)
	private Status status;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("security_profile_name")
	private String securityProfileName;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("unread_count")
	private int unreadCount;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("double_encryption_status")
	private String doubleEncryptionStatus;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("audit_record_pdf")
	private String auditRecordPdf;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("secure_link")
	private String secureLink;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("secure_link_title")
	private String secureLinkTitle;
	@Expose
	@SerializedName("email_notification_enabled")
	private boolean emailNotificationEnabled;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("created_at")
	private Date createdAt;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("updated_at")
	private Date updatedAt;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("assigned_at")
	private Date assignedAt;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("latest_activity")
	private Date latestActivity;
	@Expose(serialize = false, deserialize = true)
	private Date expiration;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("closed_at")
	private Date closedAt;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("content_deleted_at")
	private Date contentDeletedAt;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("security_options")
	private SecurityOptions securityOptions;
	@Expose
	private List<Participant> participants = new ArrayList<Participant>();
	@Expose(serialize = false, deserialize = true)
	private List<Message> messages;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("download_activity")
	private DownloadActivity downloadActivity;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("event_history")
	private List<EventHistory> eventHistory;
	@Expose(serialize = true, deserialize = false)
	private String message;
	private List<Attachment> attachments = new ArrayList<Attachment>();
	@SerializedName("upload_url")
	private String uploadUrl;
	@Expose(serialize = true, deserialize = false)
	@SerializedName("security_profile_id")
	private Integer securityProfileId;
	@Expose(serialize = true, deserialize = false)
	@SerializedName("public_encryption_key")
	private String publicEncryptionKey;
	@Expose
	@SerializedName("user_email")
	private String userEmail;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("preview_url")
	private String previewUrl;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("encryption_key")
	private String encryptionKey;

	public Safebox(String userEmail) {
		this.userEmail = userEmail;
		this.securityOptions = new SecurityOptions();
	}

	static public String getStatusSerializedName(Enum<?> e){
		try{
			Field f = e.getClass().getField(e.name());
			SerializedName a = f.getAnnotation(SerializedName.class);
			return a.value();
		} catch (NoSuchFieldException exception){
			return null;
		}
	}

	public List<String> getDocumentIds(){
		List<String> documentIds = new ArrayList<String>();
		if (this.attachments != null) {
		    for(Attachment attachment: this.attachments){
			documentIds.add(attachment.getGuid());
		    }
		}
		return documentIds;
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

	public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Integer getSecurityProfileId() {
		return securityProfileId;
	}

	public void setSecurityProfileId(Integer securityProfileId) {
		this.securityProfileId = securityProfileId;
	}

	public String getNotificationLanguage() {
		return notificationLanguage;
	}

	public void setNotificationLanguage(String notificationLanguage) {
		this.notificationLanguage = notificationLanguage;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public int getUserId() {
		return userId;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public Status getStatus() {
		return status;
	}

	public String getSecurityProfileName() {
		return securityProfileName;
	}

	public int getUnreadCount() {
		return unreadCount;
	}

	public String getDoubleEncryptionStatus() {
		return doubleEncryptionStatus;
	}

	public String getAuditRecordPdf() {
		return auditRecordPdf;
	}

	public String  getSecureLink() {
		return secureLink;
	}

	public String getSecureLinkTitle() {
		return secureLinkTitle;
	}

	public void setEmailNotificationEnabled(boolean emailNotificationEnabled) {
		this.emailNotificationEnabled = emailNotificationEnabled;
	}

	public boolean isEmailNotificationEnabled() {
		return emailNotificationEnabled;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public Date getAssignedAt() {
		return assignedAt;
	}

	public Date getLatestActivity() {
		return latestActivity;
	}

	public Date getClosedAt() {
		return closedAt;
	}

	public Date getContentDeletedAt() {
		return contentDeletedAt;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public String getEncryptionKey() {
		return encryptionKey;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public List<EventHistory> getEventHistory() {
		return eventHistory;
	}

	public void setEventHistory(List<EventHistory> eventHistory) {
		this.eventHistory = eventHistory;
	}

	public DownloadActivity getDownloadActivity() {
		return downloadActivity;
	}

	public void setDownloadActivity(DownloadActivity downloadActivity) {
		this.downloadActivity = downloadActivity;
	}

	public SecurityOptions getSecurityOptions() {
		return securityOptions;
	}

	public void setSecurityOptions(SecurityOptions securityOptions) {
		this.securityOptions = securityOptions;
	}

	public void setExpirationValues(Date date) throws SendSecureException {
		if(status != null) {
			throw new SendSecureException("Cannot change the expiration of a committed safebox, please see the method addTime to extend the lifetime of the safebox");
		}
		securityOptions.setExpirationDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
		securityOptions.setExpirationTime(new SimpleDateFormat("HH:MM:ss").format(date));
		securityOptions.setExpirationTimeZone(new SimpleDateFormat("XXX").format(date));
	}

	public String getTemporaryDocumentParams(long fileSize) {
		StringBuilder builder = new StringBuilder("{\"temporary_document\":{")
				.append("\"document_file_size\":")
				.append(fileSize)
				.append("},\"multipart\":false");
		if(StringUtils.isNotEmpty(this.publicEncryptionKey)) {
			builder.append(",\"public_encryption_key\":")
			.append("\"" + this.publicEncryptionKey + "\"");
		}
		return builder.append("}").toString();
	}
}
