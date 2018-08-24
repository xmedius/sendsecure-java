package com.xmedius.sendsecure.helper;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * Class DownloadActivityDetail represents the settings of a Download Activity
 */
public class DownloadActivityDetail {

    @Expose
    private String id;
    @Expose
    private List<DownloadActivityDocument> documents;

    public String getId() {
        return id;
    }

    public List<DownloadActivityDocument> getDocuments() {
        return documents;
    }


}