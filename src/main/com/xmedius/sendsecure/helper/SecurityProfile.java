package com.xmedius.sendsecure.helper;

import java.util.Date;

import com.google.gson.annotations.SerializedName;
import com.xmedius.sendsecure.json.Value;

public class SecurityProfile {

	public enum TimeUnit {
		@SerializedName("hours")  HOURS,
		@SerializedName("days")   DAYS,
		@SerializedName("weeks")  WEEKS,
		@SerializedName("months") MONTHS
	}

	public enum LongTimeUnit {
		@SerializedName("hours")  HOURS,
		@SerializedName("days")   DAYS,
		@SerializedName("weeks")  WEEKS,
		@SerializedName("months") MONTHS,
		@SerializedName("years")  YEARS
	}

	public enum RetentionPeriodType {
		@SerializedName("discard_at_expiration") DISCARD_AT_EXPIRATION,
		@SerializedName("retain_at_expiration")  RETAIN_AT_EXPIRATION,
		@SerializedName("do_not_discard")  DO_NOT_DISCARD
	}

	private int id;
	private String name;
	private String description;
	@SerializedName("created_at")
	private Date createdAt;
	@SerializedName("updated_at")
	private Date updatedAt;
	@SerializedName("allowed_login_attempts")
	private Value<Integer> allowedLoginAttempts;
	@SerializedName("allow_remember_me")
	private Value<Boolean> allowRememberMe;
	@SerializedName("allow_sms")
	private Value<Boolean> allowSms;
	@SerializedName("allow_voice")
	private Value<Boolean> allowVoice;
	@SerializedName("allow_email")
	private Value<Boolean> allowEmail;
	@SerializedName("code_time_limit")
	private Value<Integer> codeTimeLimit;
	@SerializedName("code_length")
	private Value<Integer> codeLength;
	@SerializedName("auto_extend_value")
	private Value<Integer> autoExtendValue;
	@SerializedName("auto_extend_unit")
	private Value<TimeUnit> autoExtendUnit;
	@SerializedName("two_factor_required")
	private Value<Boolean> twoFactorRequired;
	@SerializedName("encryptAttachments")
	private Value<Boolean> encryptAttachments;
	@SerializedName("encrypt_message")
	private Value<Boolean> encryptMessage;
	@SerializedName("expiration_value")
	private Value<Integer> expirationValue;
	@SerializedName("expiration_unit")
	private Value<TimeUnit> expirationUnit;
	@SerializedName("reply_enabled")
	private Value<Boolean> replyEnabled;
	@SerializedName("group_replies")
	private Value<Boolean> groupReplies;
	@SerializedName("double_encryption")
	private Value<Boolean> doubleEncryption;
	@SerializedName("retention_period_type")
	private Value<RetentionPeriodType> retentionPeriodType;
	@SerializedName("retention_period_value")
	private Value<Integer> retentionPeriodValue;
	@SerializedName("retention_period_unit")
	private Value<LongTimeUnit> retentionPeriodUnit;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Value<Integer> getAllowedLoginAttempts() {
		return allowedLoginAttempts;
	}

	public void setAllowedLoginAttempts(Value<Integer> allowedLoginAttempts) {
		this.allowedLoginAttempts = allowedLoginAttempts;
	}

	public Value<Boolean> getAllowRememberMe() {
		return allowRememberMe;
	}

	public void setAllowRememberMe(Value<Boolean> allowRememberMe) {
		this.allowRememberMe = allowRememberMe;
	}

	public Value<Boolean> getAllowSms() {
		return allowSms;
	}

	public void setAllowSms(Value<Boolean> allowSms) {
		this.allowSms = allowSms;
	}

	public Value<Boolean> getAllowVoice() {
		return allowVoice;
	}

	public void setAllowVoice(Value<Boolean> allowVoice) {
		this.allowVoice = allowVoice;
	}

	public Value<Boolean> getAllowEmail() {
		return allowEmail;
	}

	public void setAllowEmail(Value<Boolean> allowEmail) {
		this.allowEmail = allowEmail;
	}

	public Value<Integer> getCodeTimeLimit() {
		return codeTimeLimit;
	}

	public void setCodeTimeLimit(Value<Integer> codeTimeLimit) {
		this.codeTimeLimit = codeTimeLimit;
	}

	public Value<Integer> getCodeLength() {
		return codeLength;
	}

	public void setCodeLength(Value<Integer> codeLength) {
		this.codeLength = codeLength;
	}

	public Value<Integer> getAutoExtendValue() {
		return autoExtendValue;
	}

	public void setAutoExtendValue(Value<Integer> autoExtendValue) {
		this.autoExtendValue = autoExtendValue;
	}

	public Value<TimeUnit> getAutoExtendUnit() {
		return autoExtendUnit;
	}

	public void setAutoExtendUnit(Value<TimeUnit> autoExtendUnit) {
		this.autoExtendUnit = autoExtendUnit;
	}

	public Value<Boolean> getTwoFactorRequired() {
		return twoFactorRequired;
	}

	public void setTwoFactorRequired(Value<Boolean> twoFactorRequired) {
		this.twoFactorRequired = twoFactorRequired;
	}

	public Value<Boolean> getEncryptAttachments() {
		return encryptAttachments;
	}

	public void setEncryptAttachments(Value<Boolean> encryptAttachments) {
		this.encryptAttachments = encryptAttachments;
	}

	public Value<Boolean> getEncryptMessage() {
		return encryptMessage;
	}

	public void setEncryptMessage(Value<Boolean> encryptMessage) {
		this.encryptMessage = encryptMessage;
	}

	public Value<Integer> getExpirationValue() {
		return expirationValue;
	}

	public void setExpirationValue(Value<Integer> expirationValue) {
		this.expirationValue = expirationValue;
	}

	public Value<TimeUnit> getExpirationUnit() {
		return expirationUnit;
	}

	public void setExpirationUnit(Value<TimeUnit> expirationUnit) {
		this.expirationUnit = expirationUnit;
	}

	public Value<Boolean> getReplyEnabled() {
		return replyEnabled;
	}

	public void setReplyEnabled(Value<Boolean> replyEnabled) {
		this.replyEnabled = replyEnabled;
	}

	public Value<Boolean> getGroupReplies() {
		return groupReplies;
	}

	public void setGroupReplies(Value<Boolean> groupReplies) {
		this.groupReplies = groupReplies;
	}

	public Value<Boolean> getDoubleEncryption() {
		return doubleEncryption;
	}

	public void setDoubleEncryption(Value<Boolean> doubleEncryption) {
		this.doubleEncryption = doubleEncryption;
	}

	public Value<Integer> getRetentionPeriodValue() {
		return retentionPeriodValue;
	}

	public Value<RetentionPeriodType> getRetentionPeriodType() {
		return retentionPeriodType;
	}

	public void setRetentionPeriodType(Value<RetentionPeriodType> retentionPeriodType) {
		this.retentionPeriodType = retentionPeriodType;
	}

	public void setRetention_periodValue(Value<Integer> retention_periodValue) {
		this.retentionPeriodValue = retention_periodValue;
	}

	public Value<LongTimeUnit> getRetentionPeriodUnit() {
		return retentionPeriodUnit;
	}

	public void setRetentionPeriodUnit(Value<LongTimeUnit> retentionPeriodUnit) {
		this.retentionPeriodUnit = retentionPeriodUnit;
	}

}
