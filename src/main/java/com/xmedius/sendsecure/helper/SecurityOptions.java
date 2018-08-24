package com.xmedius.sendsecure.helper;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
* Class SecurityOptions represent the security options of a Safebox
*/
public class SecurityOptions {

	@Expose(serialize = false, deserialize = true)
	@SerializedName("security_code_length")
	private Integer securityCodeLength;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("allowed_login_attempts")
	private Integer allowedLoginAttempts;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("allow_remember_me")
	private Boolean allowRememberMe;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("allow_sms")
	private Boolean allowSms;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("allow_voice")
	private Boolean allowVoice;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("allow_email")
	private Boolean allowEmail;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("code_time_limit")
	private Integer codeTimeLimit;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("auto_extend_value")
	private Integer autoExtendValue;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("auto_extend_unit")
	private SecurityEnums.TimeUnit autoExtendUnit;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("two_factor_required")
	private Boolean twoFactorRequired;
	@Expose
	@SerializedName("encrypt_message")
	private Boolean encryptMessage;
	@Expose
	@SerializedName("double_encryption")
	private Boolean doubleEncryption;
	@Expose
	@SerializedName("reply_enabled")
	private Boolean replyEnabled;
	@Expose
	@SerializedName("group_replies")
	private Boolean groupReplies;
	@Expose
	@SerializedName("retention_period_type")
	private SecurityEnums.RetentionPeriodType retentionPeriodType;
	@Expose
	@SerializedName("retention_period_value")
	private Integer retentionPeriodValue;
	@Expose
	@SerializedName("retention_period_unit")
	private SecurityEnums.LongTimeUnit retentionPeriodUnit;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("allow_manual_delete")
	private Boolean allowManualDelete;
	@Expose(serialize = false, deserialize = true)
	@SerializedName("allow_manual_close")
	private Boolean allowManualClose;
	@Expose
	@SerializedName("expiration_unit")
	private SecurityEnums.TimeUnit expirationUnit;
	@Expose
	@SerializedName("expiration_value")
	private Integer expirationValue;
	@Expose
	@SerializedName("encrypt_attachments")
	private Boolean encryptAttachments;
	@Expose
	@SerializedName("consent_group_id")
	private Integer consentGroupId;

	@Expose
	@SerializedName("expiration_date")
	private String expirationDate;
	@Expose
	@SerializedName("expiration_time")
	private String expirationTime;
	@Expose
	@SerializedName("expiration_time_zone")
	private String expirationTimeZone;


	public Integer getAllowedLoginAttempts() {
		return allowedLoginAttempts;
	}

	public Boolean getAllowRememberMe() {
		return allowRememberMe;
	}

	public Boolean getAllowSms() {
	  return allowSms;
	}

	public Boolean getAllowVoice() {
		return allowVoice;
	}

	public Boolean getAllowEmail() {
		return allowEmail;
	}

	public Integer getCodeTimeLimit() {
		return codeTimeLimit;
	}

	public Integer getSecurityCodeLength() {
		return securityCodeLength;
	}

	public Integer getAutoExtendValue() {
		return autoExtendValue;
	}

	public SecurityEnums.TimeUnit getAutoExtendUnit() {
		return autoExtendUnit;
	}

	public Boolean getTwoFactorRequired() {
		return twoFactorRequired;
	}

	public Boolean getEncryptMessage() {
		return encryptMessage;
	}

	public void setEncryptMessage(Boolean encryptMessage) {
		this.encryptMessage = encryptMessage;
	}

	public Boolean getReplyEnabled() {
		return replyEnabled;
	}

	public void setReplyEnabled(Boolean replyEnabled) {
		this.replyEnabled = replyEnabled;
	}

	public Boolean getGroupReplies() {
		return groupReplies;
	}

	public void setGroupReplies(Boolean groupReplies) {
		this.groupReplies = groupReplies;
	}

	public Integer getRetentionPeriodValue() {
		return retentionPeriodValue;
	}

	public SecurityEnums.RetentionPeriodType getRetentionPeriodType() {
		return retentionPeriodType;
	}

	public void setRetentionPeriodType(SecurityEnums.RetentionPeriodType retentionPeriodType) {
		this.retentionPeriodType = retentionPeriodType;
	}

	public void setRetention_periodValue(Integer retention_periodValue) {
		this.retentionPeriodValue = retention_periodValue;
	}

	public SecurityEnums.LongTimeUnit getRetentionPeriodUnit() {
		return retentionPeriodUnit;
	}

	public void setRetentionPeriodUnit(SecurityEnums.LongTimeUnit retentionPeriodUnit) {
		this.retentionPeriodUnit = retentionPeriodUnit;
	}

	public Boolean getAllowManualDelete() {
		return allowManualDelete;
	}

	public Boolean getAllowManualClose() {
		return allowManualClose;
	}

	public Integer getExpirationValue() {
		return expirationValue;
	}

	public void setExpirationValue(Integer expirationValue) {
		this.expirationValue = expirationValue;
	}

	public SecurityEnums.TimeUnit getExpirationUnit() {
		return expirationUnit;
	}

	public void setExpirationUnit(SecurityEnums.TimeUnit expirationUnit) {
		this.expirationUnit = expirationUnit;
	}

	public Boolean getDoubleEncryption() {
		return doubleEncryption;
	}

	public void setDoubleEncryption(Boolean doubleEncryption) {
		this.doubleEncryption = doubleEncryption;
	}

	public Boolean getEncryptAttachments() {
	    return encryptAttachments;
	}

	public int getConsentGroupId() {
	    return consentGroupId.intValue();
	}

	public String getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(String expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getExpirationTimeZone() {
		return expirationTimeZone;
	}

	public void setExpirationTimeZone(String expirationTimeZone) {
		this.expirationTimeZone = expirationTimeZone;
	}
}