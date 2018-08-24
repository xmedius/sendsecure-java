package com.xmedius.sendsecure.helper;

import java.util.Date;

import com.google.gson.annotations.SerializedName;
import com.xmedius.sendsecure.json.Value;

/**
 * Class SecurityProfile represent the settings of an Security Profile
 */
public class SecurityProfile {

	private int id;
	private String name;
	private String description;
	@SerializedName("created_at")
	private Date createdAt;
	@SerializedName("updated_at")
	private Date updatedAt;
	@SerializedName("code_length")
	private Value<Integer> codeLength;
	@SerializedName("encrypt_attachments")
	private Value<Boolean> encryptAttachments;
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
	@SerializedName("auto_extend_value")
	private Value<Integer> autoExtendValue;
	@SerializedName("auto_extend_unit")
	private Value<SecurityEnums.TimeUnit> autoExtendUnit;
	@SerializedName("two_factor_required")
	private Value<Boolean> twoFactorRequired;
	@SerializedName("encrypt_message")
	private Value<Boolean> encryptMessage;
	@SerializedName("expiration_value")
	private Value<Integer> expirationValue;
	@SerializedName("expiration_unit")
	private Value<SecurityEnums.TimeUnit> expirationUnit;
	@SerializedName("reply_enabled")
	private Value<Boolean> replyEnabled;
	@SerializedName("group_replies")
	private Value<Boolean> groupReplies;
	@SerializedName("double_encryption")
	private Value<Boolean> doubleEncryption;
	@SerializedName("retention_period_type")
	private Value<SecurityEnums.RetentionPeriodType> retentionPeriodType;
	@SerializedName("retention_period_value")
	private Value<Integer> retentionPeriodValue;
	@SerializedName("retention_period_unit")
	private Value<SecurityEnums.LongTimeUnit> retentionPeriodUnit;
	@SerializedName("allow_manual_delete")
	private Value<Boolean> allowManualDelete;
	@SerializedName("allow_manual_close")
	private Value<Boolean> allowManualClose;
	@SerializedName("allow_for_secure_links")
	private Value<Boolean> allowForSecureLinks;
	@SerializedName("use_captcha")
	private Value<Boolean> useCaptcha;
	@SerializedName("verify_email")
	private Value<Boolean> verifyEmail;
	@SerializedName("distribute_key")
	private Value<Boolean> distributeKey;
	@SerializedName("consent_group_id")
	private Value<Integer> consentGroupId;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public Value<Integer> getAllowedLoginAttempts() {
		return allowedLoginAttempts;
	}

	public Value<Boolean> getAllowRememberMe() {
		return allowRememberMe;
	}

	public Value<Boolean> getAllowSms() {
		return allowSms;
	}

	public Value<Boolean> getAllowVoice() {
		return allowVoice;
	}

	public Value<Boolean> getAllowEmail() {
		return allowEmail;
	}

	public Value<Integer> getCodeTimeLimit() {
		return codeTimeLimit;
	}

	public Value<Integer> getCodeLength() {
		return codeLength;
	}

	public Value<Integer> getAutoExtendValue() {
		return autoExtendValue;
	}

	public Value<SecurityEnums.TimeUnit> getAutoExtendUnit() {
		return autoExtendUnit;
	}

	public Value<Boolean> getTwoFactorRequired() {
		return twoFactorRequired;
	}

	public Value<Boolean> getEncryptAttachments() {
		return encryptAttachments;
	}

	public Value<Boolean> getEncryptMessage() {
		return encryptMessage;
	}

	public Value<Integer> getExpirationValue() {
		return expirationValue;
	}

	public Value<SecurityEnums.TimeUnit> getExpirationUnit() {
		return expirationUnit;
	}

	public Value<Boolean> getReplyEnabled() {
		return replyEnabled;
	}

	public Value<Boolean> getGroupReplies() {
		return groupReplies;
	}

	public Value<Boolean> getDoubleEncryption() {
		return doubleEncryption;
	}

	public Value<Integer> getRetentionPeriodValue() {
		return retentionPeriodValue;
	}

	public Value<SecurityEnums.RetentionPeriodType> getRetentionPeriodType() {
		return retentionPeriodType;
	}

	public Value<SecurityEnums.LongTimeUnit> getRetentionPeriodUnit() {
		return retentionPeriodUnit;
	}

	public Value<Boolean> getAllowManualDelete() {
	    return allowManualDelete;
	}

	public Value<Boolean> getAllowManualClose() {
	    return allowManualClose;
	}

	public Value<Boolean> getAllowForSecureLinks() {
	    return allowForSecureLinks;
	}

	public Value<Boolean> getUseCaptcha() {
	    return useCaptcha;
	}

	public Value<Boolean> getVerifyEmail() {
	    return verifyEmail;
	}

	public Value<Boolean> getDistributeKey() {
	    return distributeKey;
	}

	public Value<Integer> getConsentGroupId() {
	    return consentGroupId;
	}
}