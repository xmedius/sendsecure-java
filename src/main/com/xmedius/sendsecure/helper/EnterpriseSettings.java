package com.xmedius.sendsecure.helper;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

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
	@SerializedName("include_users_in_autocomplete")
	private boolean includeUsersInAutocomplete;
	@SerializedName("include_favorites_in_autocomplete")
	private boolean includeFavoritesInAutocomplete;

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

	public int getDefaultSecurityProfileId() {
		return defaultSecurityProfileId;
	}

	public void setDefaultSecurityProfileId(int defaultSecurityProfileId) {
		this.defaultSecurityProfileId = defaultSecurityProfileId;
	}

	public String getPdfLanguage() {
		return pdfLanguage;
	}

	public void setPdfLanguage(String pdfLanguage) {
		this.pdfLanguage = pdfLanguage;
	}

	public boolean isUsePdfaAudit_records() {
		return usePdfaAuditRecords;
	}

	public void setUsePdfaAuditRecords(boolean usePdfaAuditRecords) {
		this.usePdfaAuditRecords = usePdfaAuditRecords;
	}

	public String getInternationalDialingPlan() {
		return internationalDialingPlan;
	}

	public void setInternationalDialingPlan(String internationalDialingPlan) {
		this.internationalDialingPlan = internationalDialingPlan;
	}

	public ExtensionFilter getExtensionFilter() {
		return extensionFilter;
	}

	public void setExtensionFilter(ExtensionFilter extensionFilter) {
		this.extensionFilter = extensionFilter;
	}

	public boolean isIncludeUsersInAutocomplete() {
		return includeUsersInAutocomplete;
	}

	public void setIncludeUsersInAutocomplete(boolean includeUsersInAutocomplete) {
		this.includeUsersInAutocomplete = includeUsersInAutocomplete;
	}

	public boolean isIncludeFavoritesInAutocomplete() {
		return includeFavoritesInAutocomplete;
	}

	public void setIncludeFavoritesInAutocomplete(boolean includeFavoritesInAutocomplete) {
		this.includeFavoritesInAutocomplete = includeFavoritesInAutocomplete;
	}
}
