package com.xmedius.sendsecure.json;

import com.xmedius.sendsecure.helper.Attachment;

import com.google.gson.annotations.SerializedName;

/**
 * Class TemporaryDocument builds an object containing the server response.
 */
public class TemporaryDocument {

    @SerializedName("temporary_document")
    private Attachment temporaryDocument;

    public String getGuid() {
        return temporaryDocument.getGuid();
    }
}
