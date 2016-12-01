package com.xmedius.sendsecure.json;

import com.google.gson.annotations.SerializedName;

public class Document {

	@SerializedName("document_guid")
	private String documentGuid;

	public String getGuid() {
		return documentGuid;
	}

}
