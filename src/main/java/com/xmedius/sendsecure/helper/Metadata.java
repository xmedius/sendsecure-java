package com.xmedius.sendsecure.helper;

import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class Metadata {

    @Expose
    private List<String> emails;
    @Expose
    @SerializedName("attachment_count")
    private Integer attachmentCount;

    public List<String> getEmails() {
        return emails;
    }

    public Integer attachmentCount() {
        return attachmentCount;
    }

}