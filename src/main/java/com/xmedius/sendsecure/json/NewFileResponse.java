package com.xmedius.sendsecure.json;

import com.google.gson.annotations.SerializedName;

/**
 * Class NewFileResponse builds an object containing the server response to new document call.
 */
public class NewFileResponse {
	@SerializedName("temporary_document_guid")
	private String temporaryDocumentGuid;
	@SerializedName("upload_url")
	private String uploadUrl;

	public String getTemporaryDocumentGuid() {
		return this.temporaryDocumentGuid;
	}

	public String getUploadUrl() {
		return this.uploadUrl;
	}
}
