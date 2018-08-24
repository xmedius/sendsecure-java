package com.xmedius.sendsecure.helper;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class Message represents the attributes of a Message object
 */
public class Message {

    @Expose
    private String id;
    @Expose
    private String note;
    @Expose
    @SerializedName("note_size")
    private int noteSize;
    @Expose
    private boolean read;
    @Expose
    @SerializedName("author_id")
    private String authorId;
    @Expose
    @SerializedName("author_type")
    private String authorType;
    @Expose
    @SerializedName("created_at")
    private Date createdAt;
    @Expose
    private List<MessageDocument> documents;

    public String getId() {
    	return id;
    }

    public String getNote() {
    	return note;
    }

    public int getNoteSize() {
    	return noteSize;
    }

    public String getAuthorId() {
    	return authorId;
    }

    public String getAuthorType() {
    	return authorType;
    }

    public Date getCreatedAt() {
    	return createdAt;
    }

    public List<MessageDocument> getDocuments() {
    	return documents;
    }

    public boolean isRead() {
    	return read;
    }
}
