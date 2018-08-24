package com.xmedius.sendsecure.helper;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * Class EnterpriseSettings represents the settings of an Enterprise Account
 */
public class EnterpriseSettings {

	@SerializedName("created_at")
	private Date createdAt;
	@SerializedName("updated_at")
	private Date updatedAt;
	@SerializedName("default_security_profile_id")
	private int defaultSecurityProfileId;
	@SerializedName("pdf_language")
	private String pdfLanguage;
	@SerializedName("use_pdfa_audit_records")
	private boolean usePdfaAuditRecords;
	@SerializedName("international_dialing_plan")
	private String internationalDialingPlan;
	@SerializedName("extension_filter")
	private ExtensionFilter extensionFilter;
	@SerializedName("virus_scan_enabled")
	private Boolean virusScanEnabled;
	@SerializedName("max_file_size_value")
	private int maxFileSizeValue;
	@SerializedName("max_file_size_unit")
	private String maxFileSizeUnit;
	@SerializedName("users_public_url")
	private Boolean usersPublicUrl;
	@SerializedName("include_users_in_autocomplete")
	private boolean includeUsersInAutocomplete;
	@SerializedName("include_favorites_in_autocomplete")
	private boolean includeFavoritesInAutocomplete;

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public int getDefaultSecurityProfileId() {
		return defaultSecurityProfileId;
	}

	public String getPdfLanguage() {
		return pdfLanguage;
	}

	public boolean isUsePdfaAudit_records() {
		return usePdfaAuditRecords;
	}

	public String getInternationalDialingPlan() {
		return internationalDialingPlan;
	}

	public ExtensionFilter getExtensionFilter() {
		return extensionFilter;
	}

	public boolean isIncludeUsersInAutocomplete() {
		return includeUsersInAutocomplete;
	}

	public boolean isIncludeFavoritesInAutocomplete() {
		return includeFavoritesInAutocomplete;
	}

	public Boolean getVirusScanEnabled() {
		return virusScanEnabled;
	}

	public Boolean getUsersPublicUrl() {
		return usersPublicUrl;
	}

	public int getMaxFileSizeValue() {
		return maxFileSizeValue;
	}

	public String getMaxFileSizeUnit() {
		return maxFileSizeUnit;
	}
}
