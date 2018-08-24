package com.xmedius.sendsecure.helper;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * Class DownloadActivity represents the settings of a Download Activity
 */
public class DownloadActivity {

    @Expose
    private List<DownloadActivityDetail> guests;
    @Expose
    private DownloadActivityDetail owner;

    public List<DownloadActivityDetail> getGuests() {
        return guests;
    }

    public DownloadActivityDetail getOwner() {
        return owner;
    }
}
