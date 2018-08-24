package com.xmedius.sendsecure.helper;

import java.util.Date;
import com.google.gson.annotations.Expose;

/**
 * Class EventHistory represents the settings of an Event History
 */
public class EventHistory {

    @Expose
    private String type;
    @Expose
    private Date date;
    @Expose
    private String message;
    @Expose
    private Metadata metadata;

    public String getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public Metadata getMetadata() {
        return metadata;
    }
}
