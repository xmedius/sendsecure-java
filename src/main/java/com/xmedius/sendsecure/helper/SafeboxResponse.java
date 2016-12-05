package com.xmedius.sendsecure.helper;

import com.google.gson.annotations.SerializedName;

/**
 * Class SafeboxResponse represent the response to a successful creation of a Safebox
 */
public class SafeboxResponse {

	private String guid;
	@SerializedName("preview_url")
	private String previewUrl;
	@SerializedName("encryption_key")
	private String encryptionKey;

	public String getGuid() {
		return guid;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public String getEncryptionKey() {
		return encryptionKey;
	}
}
