package com.xmedius.sendsecure.helper;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class DownloadActivityDocument represents the settings of a Download Activity Document
 */
public class DownloadActivityDocument {

    @Expose
    @SerializedName("downloaded_bytes")
    private long downloadedBytes;
    @Expose
    @SerializedName("download_date")
    private Date downloadDate;
    @Expose
    private String id;

    public long getDownloadedBytes() {
        return downloadedBytes;
    }

    public Date getDownloadDate() {
        return downloadDate;
    }

    public String getId() {
        return id;
    }
}