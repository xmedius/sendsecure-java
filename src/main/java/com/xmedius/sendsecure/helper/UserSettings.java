package com.xmedius.sendsecure.helper;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

/**
 * Class UserSettings represent the settings of a User Account
 */
public class UserSettings {

    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("updated_at")
    private Date updatedAt;
    @SerializedName("mask_note")
    private Boolean maskNote;
    @SerializedName("open_first_transaction")
    private Boolean openFirstTransaction;
    @SerializedName("mark_as_read")
    private Boolean markAsRead;
    @SerializedName("mark_as_read_delay")
    private Integer markAsReadDelay;
    @SerializedName("remember_key")
    private Boolean rememberKey;
    @SerializedName("default_filter")
    private String defaultFilter;
    @SerializedName("recipient_language")
    private String recipientLanguage;
    @SerializedName("secure_link")
    private PersonnalSecureLink secureLink = new PersonnalSecureLink();

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public Boolean isMaskNote() {
        return maskNote;
    }

    public void setMaskNote(Boolean maskNote) {
        this.maskNote = maskNote;
    }

    public Boolean isOpenFirstTransaction() {
        return openFirstTransaction;
    }

    public void setOpenFirstTransaction(Boolean openFirstTransaction) {
        this.openFirstTransaction = openFirstTransaction;
    }

    public Boolean isMarkAsRead() {
        return markAsRead;
    }

    public void setMarkAsRead(Boolean markAsRead) {
        this.markAsRead = markAsRead;
    }

    public Integer getMarkAsReadDelay() {
        return markAsReadDelay;
    }

    public void setMarkAsReadDelay(Integer markAsReadDelay) {
        this.markAsReadDelay = markAsReadDelay;
    }

    public Boolean isRememberKey() {
        return rememberKey;
    }

    public void setRememberKey(Boolean rememberKey) {
        this.rememberKey = rememberKey;
    }

    public String getDefaultFilter() {
        return defaultFilter;
    }

    public void setDefaultFilter(String defaultFilter) {
        this.defaultFilter = defaultFilter;
    }

    public String getRecipientLanguage() {
        return recipientLanguage;
    }

    public void setRecipientLanguage(String recipientLanguage) {
        this.recipientLanguage = recipientLanguage;
    }

    public PersonnalSecureLink getSecureLink() {
        return secureLink;
    }

}