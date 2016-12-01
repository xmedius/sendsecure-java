package com.xmedius.sendsecure.helper;

import com.google.gson.annotations.SerializedName;

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
