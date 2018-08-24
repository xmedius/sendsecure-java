package com.xmedius.sendsecure.helper;

import com.google.gson.annotations.Expose;

/**
 * Class MessageDocument represents the settings of a Message Document
 */
public class MessageDocument {

    @Expose
    private String name;
    @Expose
    private String sha;
    @Expose
    private Integer size;
    @Expose
    private String url;
    @Expose
    private String id;

    public Integer getSize() {
        return size;
    }

    public String getSha() {
        return sha;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }
}