package com.xmedius.sendsecure.json;

import com.google.gson.annotations.SerializedName;

public class TemporaryDocument {

	@SerializedName("temporary_document")
	private Document temporaryDocument;

	public String getGuid() {
		return temporaryDocument.getGuid();
	}

}
